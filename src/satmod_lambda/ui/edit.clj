(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.support.object :as obj]
            [seesaw.core :as s]))

(defn category-fn
  "Update listbox based on combobox selection."
  [listbox combobox & _]
  (s/config! listbox :model (obj/gen-list (.getSelectedItem combobox))))

(defn select-panel
  "Generate selection panel."
  []
  (let [selection-box (s/listbox)
        category-box (s/combobox :model (sort (keys data/categories)))]
    (s/listen category-box :selection (partial category-fn 
                                               selection-box category-box))
    (s/border-panel :north category-box :center selection-box
                    :border data/border-size)))

(defn edit-panel
  "Generate editing panel."
  []
  (let [left-panel (select-panel)]
    (s/border-panel :west left-panel :center "edit-panel")))