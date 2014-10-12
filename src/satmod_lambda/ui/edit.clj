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
  (s/grid-panel :id :update-panel
                :rows grid-rows))

(defn poll-category
  "Get the currently selected category keyword."
  []
  (let [c-box (s/select @root [:#category-box])]
    (get data/categories (s/selection c-box))))

(defn poll-selection
  "Get currently selected construct id."
  []
  (let [s-box (s/select @root [:#selection-box])
        s-item (.getSelectedValue s-box)]
    (:id s-item)))

(defn category-fn
  "Update construct menu to reflect selected category."
  [& _]
  (let [s-box (s/select @root [:#selection-box])
        model (obj/gen-list (poll-category))]
    (s/config! s-box :model model)))

(defn add-fn
  "Add construct to settings file."
  [& _]
  (let [cat-text (.toLowerCase (s/selection (s/select @root [:#category-box])))
        cat-key (poll-category)
        title-str (str "Enter name of new " cat-text " construct:")
        name-field (s/text)
        success-fn (fn [& _] (let [new-name (.trim (s/text name-field))]
                               (if-not (.isEmpty new-name)
                                 (data/add-construct! cat-key new-name)
                                 (s/alert "Name cannot be blank."))))]
    (doto (s/dialog :option-type :ok-cancel
                    :content (s/grid-panel :items [title-str name-field] 
                                           :columns 1)
                    :success-fn (partial success-fn))
      s/pack! (.setLocationRelativeTo @root) s/show!)
    (category-fn)))

(defn remove-fn
  "Remove construct from settings file."
  [& _]
  (println (poll-selection)))

(defn select-panel
  "Panel for selecting contructs."
  []
  (let [category-box (s/combobox :model (keys data/categories)
                                 :id :category-box)
        selection-box (s/listbox :model (obj/gen-list 
                                          (first (vals data/categories)))
                                 :id :selection-box)
        add-button (s/button :text "Add" :id :add-button)
        rm-button (s/button :text "Remove" :id :rm-button)]
    (s/listen category-box :action (partial category-fn))
    (s/listen add-button :action (partial add-fn))
    (s/listen rm-button :action (partial remove-fn))
    (s/border-panel :north category-box 
                    :center (s/scrollable selection-box)
                    :south (s/grid-panel :items [add-button rm-button]
                                         :columns 2))))

(defn edit-panel
  "Panel for editing software constructs."
  []
  (reset! root (s/border-panel :west (select-panel) :center (update-panel))))
