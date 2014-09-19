(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.support.object :as obj]
            [seesaw.core :as s]))

(defn category-fn
  "Update listbox based on combobox selection."
  [listbox combobox & _]
  (s/config! listbox :model (obj/gen-list (.getSelectedItem combobox))))

(defn add-fn
  "Generate dialog to add construct."
  [listbox combobox & _]
  (let [name-field (s/text)
        construct-str (->> (.toLowerCase (.getSelectedItem combobox))
                        (str "Enter new " " name:"))
        success-fn (fn [& _] 
                     (let [t (.trim (s/text name-field))
                           category (get data/categories 
                                         (.getSelectedItem combobox))]
                               (when-not (.isEmpty t)
                                 (data/add-construct! category t))))]
    (-> (s/dialog :content (s/grid-panel :items [construct-str name-field])
              :option-type :ok-cancel :success-fn (partial success-fn))
      s/pack! s/show!)))

(defn select-panel
  "Generate selection panel."
  []
  (let [selection-box (s/listbox)
        category-box (s/combobox :model (sort (keys data/categories)))
        add-button (s/button :text "Add")
        remove-button (s/button :text "Remove")
        button-panel (s/grid-panel :rows 1 :items [add-button remove-button])]
    (s/listen category-box :selection (partial category-fn 
                                               selection-box category-box))
    (s/listen add-button :action (partial add-fn
                                          selection-box category-box))
    (s/border-panel :north category-box :center (s/scrollable selection-box) 
                    :south button-panel :border data/border-size)))

(defn edit-panel
  "Generate editing panel."
  []
  (let [left-panel (select-panel)]
    (s/border-panel :west left-panel :center "edit-panel")))