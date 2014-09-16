(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data]
            [seesaw.core :as s]))

(defn edit-panel
  "Generate editing panel."
  []
  (s/border-panel :center "edit-panel"))