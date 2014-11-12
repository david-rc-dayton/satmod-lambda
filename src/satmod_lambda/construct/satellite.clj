(ns satmod-lambda.construct.satellite
  (:require [satmod-lambda.support.data :as data]))

(defn generate-update-panel
  "Generate update panel for satellite construct."
  [id]
  (let [construct (data/get-construct :satellite id)
        name-field (s/text :text (:name construct))
        id-field (s/text :text id :editable? false)
        tle-one-field (s/text :text (nth (:tle construct) 0))
        tle-two-field (s/text :text (nth (:tle construct) 1))
        enabled-box (s/checkbox :text "Enabled?"
                                :selected (:enabled? construct))
        update-button (s/button :text "Update")]
    (s/grid-layout :columns 1 :border data/border-size
                   :items ["Name:" name-field
                           "Id:" id-field
                           "TLE Line 1:" tle-one-field
                           "TLE Line 2:" tle-two-field
                           enabled-box update-button])))
