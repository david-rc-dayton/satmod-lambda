(ns satmod-lambda.ui.coverage
  (:require [satmod-lambda.support.time :as time]
            [seesaw.core :as s]))

(def root (atom nil))

(def simulation-time (atom (merge (time/date->hash-map (time/now))
                                  {:hour 0 :minute 0 :second 0})))

(defn time-fn
  "Update coverage window display based on time-slider values."
  [& _]
  (let [slider (s/selection (s/select @root [:#time-slider]))
        hour (int (/ slider 60))
        minute (rem slider 60)
        label (format "%02d:%02dz" hour minute)]
    (s/text! (s/select @root [:#time-label]) label)))

(defn time-panel
  "Generate panel for controling simulation time in coverage window."
  []
  (let [time-slider (s/slider :id :time-slider
                              :orientation :horizontal
                              :min 0 :max 1439 :value 0
                              :border 2)
        time-label (s/label :id :time-label :border 2 :text "00:00z")]
    (s/listen time-slider :selection (partial time-fn))
    (s/horizontal-panel :items [time-label time-slider])))

(defn coverage-panel
  "Panel for viewing satellite coverage."
  []
  (reset! root (s/border-panel :north (time-panel)
                               :center "coverage-panel")))