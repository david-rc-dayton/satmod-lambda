(ns satmod-lambda.ui.coverage
  (:require [satmod-lambda.support.time :as time]
            [satmod-lambda.support.graphics :as graph]
            [satmod-lambda.support.data :as data]
            [satmod-lambda.construct.satellite :as sat]
            [seesaw.core :as s]
            [seesaw.chooser :as choose]
            [seesaw.mig :as sm])
  (:import [java.awt.event ActionListener]
           [java.awt.image BufferedImage]
           [java.awt Image]
           [javax.swing ImageIcon JLabel]
           [javax.imageio ImageIO]))

(def root (atom nil))

(def simulation-time (atom (merge (time/date->hash-map (time/now))
                                  {:hour 0 :minute 0 :second 0})))

(def base-image
  (ImageIO/read (clojure.java.io/resource "worldmap_grayscale.png")))

(def coverage-image (atom nil))

(def render-flags (atom {:show-gridlines false}))

(defn alpha [] {:a (get-in @data/settings [:coverage :alpha])})

(defn bright [] (get-in @data/settings [:coverage :bright]))

(def coordinates
  (for [lat (range -90 90) lon (range -180 180)]
    {:latitude lat :longitude lon}))

(defn convert-point [{:keys [latitude longitude]}]
  {:x (+ 180 longitude) :y (+ (* latitude -1) (dec 90))})

(defn satellite-locations
  "Get the list of enabled satellites from the data/settings atom."
  [time-map]
  (let [sats (filter :enabled? (vals (:satellite @data/settings)))
        date (time/hash-map->date time-map)]
    (map #(sat/propagate (:tle %) date) sats)))

(def satellite-view
  "Get list of points-in-view of satellites, as well as an integral coverage
  amount for each point."
  (memoize
    (fn [{:keys [latitude longitude altitude] :as satellite}]
      (let [horizon (sat/adist-horizon satellite)]
        (filter #(<= (sat/adist satellite %) horizon) coordinates)))))

(defn satellite-coverage
  "Generate frequency chart for total satellite coverage over the Earth's
   surface."
  []
  (let [loc (satellite-locations @simulation-time)]
    (if-not (zero? (count loc))
      (frequencies (apply concat
                          (map satellite-view loc)))
      (hash-map))))

(defn initialize-image
  "Paint coverage image with a zero-coverage baseline."
  [^BufferedImage image]
  (let [x (.getWidth image)
        y (.getHeight image)
        a (alpha)
        color-map (get-in @data/settings [:coverage :colors])
        c (graph/map->color (graph/adjust-brightness
                              (merge (first color-map) a) (bright)))
        g (.getGraphics image)]
    (.setColor g c)
    (.fillRect g 0 0 (.getWidth image) (.getHeight image))
    image))

(defn draw-coverage
  "Paint coverage image with satellite coverage map."
  [^BufferedImage image]
  (let [x (.getWidth image)
        y (.getHeight image)
        a (alpha)
        color-map (map graph/map->color
                       (map #(graph/adjust-brightness (merge % a) (bright))
                            (get-in @data/settings [:coverage :colors])))
        paint-fn (fn [point-cov]
                   (let [trans (convert-point (key point-cov))
                         cap-cov (if (< (val point-cov) (dec (count color-map)))
                                   (val point-cov)
                                   (dec (count color-map)))]
                     (.setRGB image (:x trans) (:y trans)
                       (.getRGB ^java.awt.Color
(nth color-map cap-cov)))))]
    (dorun (map paint-fn (satellite-coverage)))
    image))

(defn smooth-image
  "Use box-blur on input image."
  [^BufferedImage image]
  (let [box-root (get-in @data/settings [:coverage :smooth])
        bound (int (/ box-root 2))
        box-num (* box-root box-root)
        output (BufferedImage. (.getWidth image) (.getHeight image)
                               BufferedImage/TYPE_4BYTE_ABGR)
        matrix (float-array (take box-num (repeat (float (/ 1 box-num)))))
        op (java.awt.image.ConvolveOp.
             (java.awt.image.Kernel. box-root box-root matrix)
             java.awt.image.ConvolveOp/EDGE_NO_OP nil)]
    (.filter op image output)
    (.getSubimage output bound bound
      (- (.getWidth image) (* 2 bound))
      (- (.getHeight image) (* 2 bound)))))

(defn draw-gridlines
  "Draw grid lines in 15 degree intervals on coverage-map"
  [^BufferedImage image]
  (when (:show-gridlines @render-flags)
    (let [g (.getGraphics image)
          width (.getWidth image)
          height (.getHeight image)
          w-range (range 0 width (/ width 24))
          h-range (range 0 height (/ height 12))
          w-center (/ width 2)
          h-center (/ height 2)
          g-alpha (get-in @data/settings [:coverage :grid-alpha])
          l-color (graph/map->color {:r 192 :g 192 :b 192 :a g-alpha})
          d-color (graph/map->color {:r 128 :g 128 :b 128 :a g-alpha})]
      (.setColor g d-color)
      (dorun (map #(.drawLine g % 0 % height) w-range))
      (dorun (map #(.drawLine g 0 % width %) h-range))
      (.setColor g l-color)
      (.drawLine g w-center 0 w-center height)
      (.drawLine g 0 h-center width h-center)
      (.dispose g)))
  image)

(defn draw-image
  "Create new satellite coverage image and store in coverage-image atom."
  [& _]
  (let [^BufferedImage img (graph/copy-image base-image)
        g (.getGraphics img)
        overlay-in (BufferedImage. 360 180 BufferedImage/TYPE_4BYTE_ABGR)
        overlay-out (-> overlay-in
                      initialize-image draw-coverage smooth-image
                      draw-gridlines)
        proc (.getScaledInstance ^BufferedImage overlay-out
               (.getWidth img) (.getHeight img)
               Image/SCALE_AREA_AVERAGING)]
    (.drawImage g proc 0 0 nil)
    (reset! coverage-image img)))

(defn display-image
  "Update coverage image display."
  [& _]
  (let [^javax.swing.JPanel mp (s/select @root [:#map-panel])
        ^BufferedImage scaled-img (.getScaledInstance
                                    ^BufferedImage @coverage-image
                                    (.getWidth mp) (.getHeight mp)
                                    Image/SCALE_FAST)]
    (s/invoke-now (s/config! mp :items [(JLabel. (ImageIcon. scaled-img))]))))

(defn refresh-image
  "Redraw the coverage image in system memory, and display the new image in
   the coverage panel."
  [& _]
  (future (draw-image) (display-image)))

(defn time-fn
  "Update coverage window display based on time-slider values."
  [slider label & _]
  (let [hour (int (/ (s/selection slider) 60))
        minute (rem (s/selection slider) 60)
        text (format "  %02d:%02dz  " hour minute)]
    (s/text! label text)
    (when-not (.getValueIsAdjusting ^javax.swing.JSlider slider)
      (swap! simulation-time merge {:hour hour :minute minute})
      (refresh-image))))

(defn date-fn
  "Update coverage window display based on date-picker values."
  [^org.jdesktop.swingx.JXDatePicker date-picker & _]
  (let [date (.getDate date-picker)
        date-map (apply dissoc (time/date->hash-map date)
                        [:hour :minute :second])]
    (swap! simulation-time merge date-map)
    (refresh-image)))

(defn time-panel
  "Generate panel for controling simulation time in coverage window."
  []
  (let [time-slider (s/slider :id :time-slider
                              :orientation :horizontal
                              :min 0 :max 1439 :value 0
                              :minor-tick-spacing 60 :major-tick-spacing 240
                              :paint-track? true)
        time-label (s/label :id :time-label :text "  00:00z  ")
        date-picker (doto (org.jdesktop.swingx.JXDatePicker.)
                      (.setTimeZone (java.util.TimeZone/getTimeZone "UTC")))]
    (.setDate date-picker (time/now))
    (s/listen time-slider :selection (partial time-fn time-slider time-label))
    (.addActionListener date-picker
      (reify ActionListener (actionPerformed [& _]
                              (date-fn date-picker))))
    (s/horizontal-panel :items [date-picker time-label time-slider])))

(defn save-image-fn
  "Generate a file-chooser, and save coverage map to the desired location as
   a PNG image."
  [& _]
  (let [dim [720 360]
        save-fn (fn [_ ^java.io.File f] (graph/save-image
                                          (graph/copy-image @coverage-image)
                                          (.getCanonicalPath f) dim))]
    (choose/choose-file :type :save
                        :all-files? false
                        :success-fn (partial save-fn)
                        :filters [["PNG Image" ["png" "PNG"]]])))

(defn grid-fn
  "Update render-flags and redraw image to show global grid lines."
  [& _]
  (let [grid-toggle (s/select @root [:#grid-toggle])
        enabled? (s/selection grid-toggle)]
    (swap! render-flags assoc :show-gridlines enabled?)
    (refresh-image)))

(defn option-panel
  "Generate options panel in coverage window"
  []
  (let [image-button (s/button :id :image-button :text "Save Image")
        grid-toggle (s/toggle :id :grid-toggle :text "Show Grid Lines")]
    (s/listen image-button :action (partial save-image-fn))
    (s/listen grid-toggle :action (partial grid-fn))
    (s/scrollable (sm/mig-panel :items [[image-button "span, grow"]
                                        [(s/separator) "span, grow"]
                                        [grid-toggle "span, grow"]])
                  :hscroll :never)))

(defn map-panel
  "Generate coverage map panel in coverage window."
  []
  (doto (s/horizontal-panel :id :map-panel)
    (s/listen :component-resized (partial display-image))))

(defn coverage-panel
  "Panel for viewing satellite coverage."
  []
  (draw-image)
  (reset! root (s/border-panel :north (time-panel)
                               :west (option-panel)
                               :center (map-panel))))
