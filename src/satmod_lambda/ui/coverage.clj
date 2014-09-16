(ns satmod-lambda.ui.coverage
  (:require [seesaw.core :as s]))

(defn coverage-panel
  "Generate coverage panel."
  []
  (s/border-panel :center "coverage-panel"))