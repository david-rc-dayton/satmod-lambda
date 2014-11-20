(ns satmod-lambda.ui.coverage
  (:require [satmod-lambda.support.time :as time]
            [satmod-lambda.support.color :as color]
            [satmod-lambda.support.data :as data]
            [seesaw.core :as s]
            [seesaw.mig :as sm])
  (:import [java.awt.event ActionListener ComponentListener]
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

(defn time-fn
  "Update coverage window display based on time-slider values."
  [slider label & _]
  (let [hour (int (/ (s/selection slider) 60))
        minute (rem (s/selection slider) 60)
        text (format "  %02d:%02dz  " hour minute)]
    (s/text! label text)
    (when-not (.getValueIsAdjusting slider)
      (swap! simulation-time merge {:hour hour :minute minute}))))

(defn date-fn
  "Update coverage window display based on date-picker values."
  [date-picker & _]
  (let [date (.getDate date-picker)
        date-map (apply dissoc (time/date->hash-map date)
                        [:hour :minute :second])]
    (swap! simulation-time merge date-map)))

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
      (reify ActionListener (actionPerformed [& _] (date-fn date-picker))))
    (s/horizontal-panel :items [date-picker time-label time-slider])))

(defn option-panel
  "Generate options panel in coverage window"
  []
  (let [image-button (s/button :id :image-button :text "Save Image")
        animate-button (s/button :id :animate-button :text "Save Animation")
        grid-toggle (s/toggle :id :grid-toggle :text "Show Grid Lines")]
    (s/scrollable (sm/mig-panel :items [[image-button "span, grow"] 
                                        [animate-button "span, grow"]
                                        [(s/separator) "span, grow"]
                                        [grid-toggle "span, grow"]])
                  :hscroll :never)))

(defn copy-image
  "Make a deep copy of a buffered image."
  [buffered-image]
  (let [cm (.getColorModel buffered-image)
        alpha? (.isAlphaPremultiplied cm)
        raster (.copyData buffered-image nil)]
    (BufferedImage. cm raster alpha? nil)))

(defn initialize-image
  "Paint coverage image with a zero-coverage baseline."
  [image]
  (let [x (.getWidth image)
        y (.getHeight image)
        c (color/map->color (merge (first (:coverage @data/settings)) {:a 165}))
        g (.getGraphics image)]
    (.setColor g c)
    (.fillRect g 0 0 (.getWidth image) (.getHeight image))
    image))

(defn draw-image
  "Create new satellite coverage image and store in coverage-image atom."
  [& _]
  (let [img (copy-image base-image)
        g (.getGraphics img)
        overlay-in (BufferedImage. 360 180 BufferedImage/TYPE_4BYTE_ABGR)
        overlay-out (-> overlay-in initialize-image)
        proc (.getScaledInstance overlay-out
               (.getWidth img) (.getHeight img) Image/SCALE_FAST)]
    (.drawImage g proc 0 0 nil)
    (reset! coverage-image img)))

(defn update-image
  "Update coverage image display."
  [& _]
  (let [mp (s/select @root [:#map-panel])
        scaled-img (.getScaledInstance @coverage-image
                     (.getWidth mp) (.getHeight mp) Image/SCALE_FAST)]
    (s/invoke-now (s/config! mp :items [(JLabel. (ImageIcon. scaled-img))]))))

(defn map-panel
  "Generate coverage map panel in coverage window."
  []
  (doto (s/horizontal-panel :id :map-panel)
    (s/listen :component-resized (partial update-image))))

(defn coverage-panel
  "Panel for viewing satellite coverage."
  []
  (draw-image)
  (reset! root (s/border-panel :north (time-panel)
                               :west (option-panel)
                               :center (map-panel))))
