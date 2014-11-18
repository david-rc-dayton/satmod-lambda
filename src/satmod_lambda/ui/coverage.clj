(ns satmod-lambda.ui.coverage
  (:require [satmod-lambda.support.time :as time]
            [seesaw.core :as s])
  (:import [java.awt.event ActionListener]))

(def root (atom nil))

(def simulation-time (atom (merge (time/date->hash-map (time/now))
                                  {:hour 0 :minute 0 :second 0})))

(defn time-fn
  "Update coverage window display based on time-slider values."
  [slider label & _]
  (let [hour (int (/ (s/selection slider) 60))
        minute (rem (s/selection slider) 60)
        text (format "%02d:%02dz" hour minute)]
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
        time-label (s/label :id :time-label :text "00:00z")
        date-picker (doto (org.jdesktop.swingx.JXDatePicker.)
                      (.setTimeZone (java.util.TimeZone/getTimeZone "UTC")))]
    (.setDate date-picker (time/now))
    (s/listen time-slider :selection (partial time-fn time-slider time-label))
    (.addActionListener date-picker
      (reify ActionListener (actionPerformed [& _] (date-fn date-picker))))
    (s/horizontal-panel :items [time-label time-slider date-picker])))

(defn coverage-panel
  "Panel for viewing satellite coverage."
  []
  (reset! root (s/border-panel :north (time-panel)
                               :center "coverage-panel")))