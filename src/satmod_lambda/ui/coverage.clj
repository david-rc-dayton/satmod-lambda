(ns satmod-lambda.ui.coverage
  (:require [seesaw.core :as s]))

(def root (atom nil))

(defn coverage-panel
  "Panel for viewing satellite coverage."
  []
  (reset! root (s/border-panel :center "coverage-panel")))