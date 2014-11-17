(ns satmod-lambda.ui.coverage
  (:require [satmod-lambda.support.time :as time]
            [seesaw.core :as s]))

(def root (atom nil))

(def simulation-time (atom (merge (time/date->hash-map (time/now))
                                  {:hour 0 :minute 0 :second 0})))

(defn time-fn
  "Update coverage window display based on time-slider values."
  [slider label & _]
  (let [hour (int (/ (s/selection slider) 60))
        minute (rem (s/selection slider) 60)
        text (format "%02d:%02dz" hour minute)]
    (s/text! label text)))

(defn time-panel
  "Generate panel for controling simulation time in coverage window."
  []
  (let [time-slider (s/slider :id :time-slider
                              :orientation :horizontal
                              :min 0 :max 1439 :value 0
                              :border 2)
        time-label (s/label :id :time-label :border 2 :text "00:00z")]
    (s/listen time-slider :selection (partial time-fn time-slider time-label))
    (s/horizontal-panel :items [time-label time-slider])))

(defn coverage-panel
  "Panel for viewing satellite coverage."
  []
  (reset! root (s/border-panel :north (time-panel)
                               :center "coverage-panel")))