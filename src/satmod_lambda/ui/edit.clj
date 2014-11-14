(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.support.object :as obj]
            [seesaw.core :as s]
            [seesaw.mig :as sm]
            [seesaw.chooser :as choose])
  (:import [java.awt Color]))

(def grid-rows 20)

(def root
  "Panel for editing software constructs."
  (atom nil))

(defn update-panel
  "Placeholder for construct update panel."
  []
  (s/vertical-panel :id :update-panel))

(defn poll-category-box
  "Get the current category selection box."
  []
  (s/select @root [:#category-box]))

(defn poll-category-key
  "Get the currently selected category keyword."
  []
  (get data/categories (s/selection (poll-category-box))))

(defn poll-category-text
  "Get the currenly selected category text."
  []
  (.toLowerCase (s/selection (poll-category-box))))

(defn poll-selection-box
  "Get the current category selection box."
  []
  (s/select @root [:#selection-box]))

(defn poll-selection-key
  "Get currently selected construct id."
  []
  (let [s-item (.getSelectedValue (poll-selection-box))]
    (:id s-item)))

(defn poll-selection-text
  "Get currently selected construct name"
  []
  (let [s-item (.getSelectedValue (poll-selection-box))]
    (:name s-item)))

(defn category-fn
  "Update construct menu to reflect selected category."
  [& _]
  (let [model (obj/gen-list (poll-category-key))]
    (s/config! (poll-selection-box) :model model)))

(defn add-fn
  "Add construct to settings file."
  [& _]
  (let [msg-str (str "Enter name of new " (poll-category-text) " construct:")
        name-field (s/text)
        success-fn (fn [& _] (let [new-name (.trim (s/text name-field))]
                               (if-not (.isEmpty new-name)
                                 (data/add-construct! (poll-category-key) 
                                                      new-name)
                                 (s/alert "Name cannot be blank."))))]
    (doto (s/dialog :option-type :ok-cancel
                    :content (s/vertical-panel :items [msg-str name-field])
                    :success-fn (partial success-fn))
      s/pack! (.setLocationRelativeTo @root) s/show!)
    (category-fn)))

(defn remove-fn
  "Remove construct from settings file."
  [& _]
  (let [msg-key (str "Do you want to remove " (poll-category-text)
                     " " (poll-selection-text) "?")
        success-fn (fn [& _] (data/remove-construct! (poll-category-key)
                                                     (poll-selection-key)))]
    (when-not (nil? (poll-selection-text))
      (doto (s/dialog :option-type :yes-no :content msg-key
                      :success-fn (partial success-fn))
        s/pack! (.setLocationRelativeTo @root) s/show!))
    (category-fn)))

(defn satellite-update-panel
  "Generate update panel for satellite construct."
  [id]
  (let [construct (data/get-construct :satellite id)
        name-field (s/text :text (:name construct))
        id-field (s/text :text id :editable? false)
        tle-one-field (s/text :text (nth (:tle construct) 0))
        tle-two-field (s/text :text (nth (:tle construct) 1))
        enabled-box (s/checkbox :text "Enabled?"
                                :selected? (:enabled? construct))
        update-button (s/button :text "Update")]
    (sm/mig-panel :items [["Name:"] [name-field "span, grow, pushx"]
                          ["Id:"] [id-field "span, grow"]
                          ["TLE Line 1:"] [tle-one-field "span, grow"]
                          ["TLE Line 2:"] [tle-two-field "span, grow"]
                          [enabled-box] [update-button]])))

(defn generate-update-panel
  "Generate update panel for selected construct."
  [& _]
  (let [construct-category (poll-category-key)
        construct-key (poll-selection-key)
        update-panel (s/select @root [:#update-panel])]
    (if-not (nil? (poll-selection-text))
      (s/config! update-panel 
                 :items [(condp = construct-category
                           :satellite
                           (satellite-update-panel construct-key)
                           :earth-station ; FIXME: add proper construct panel
                           (s/grid-panel))])
      (s/config! update-panel :items [(s/grid-panel)]))))

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
    (s/listen selection-box :selection (partial generate-update-panel))
    (s/border-panel :north category-box 
                    :center (s/scrollable selection-box)
                    :south (s/grid-panel :items [add-button rm-button]
                                         :columns 2))))

(defn edit-panel
  "Panel for editing software constructs."
  []
  (reset! root (s/border-panel :west (select-panel) :center (update-panel))))
