(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.support.object :as obj]
            [seesaw.core :as s]))

(defn select-panel
  "Generate selection panel."
  []
  (let [category-box (s/combobox :id :category-box
                                 :model (keys data/categories))
        selection-box (s/listbox :id :selection-box)
        category-fn (fn [_] ; display construct list for selected category
                      (s/config! selection-box :model
                                 (obj/gen-list (.getSelectedItem 
                                                 category-box))))]
    (s/listen category-box :selection category-fn)
    (s/border-panel :north category-box :center selection-box
                    :border data/border-size)))

(defn edit-panel
  "Generate editing panel."
  []
  (let [left-panel (select-panel)]
    (s/border-panel :west left-panel :center "edit-panel")))