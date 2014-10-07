(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.support.object :as obj]
            [seesaw.core :as s]
            [seesaw.chooser :as choose])
  (:import [java.awt Color]))

(def grid-rows 20)

(def root
  "Panel for editing software constructs."
  (atom nil))

(defn update-panel
  "Placeholder for construct update panes."
  []
  (s/grid-panel :id :update-panel))

(defn select-panel
  "Panel for selecting contructs."
  []
  (let [category-box (s/combobox :model (keys data/categories)
                                 :id :category-box)
        selection-box (s/listbox :id :selection-box)
        add-button (s/button :text "Add" :id :add-button)
        rm-button (s/button :text "Remove" :id :rm-button)]
    (s/border-panel :north category-box 
                    :center (s/scrollable selection-box)
                    :south (s/grid-panel :items [add-button rm-button]
                                         :columns 2))))

(defn edit-panel
  "Panel for editing software constructs."
  []
  (reset! root (s/border-panel :west (select-panel) :center (update-panel))))
