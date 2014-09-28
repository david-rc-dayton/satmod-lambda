(ns satmod-lambda.ui.edit
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.support.object :as obj]
            [seesaw.core :as s]))

(defn default-panel
  "Generate default (blank) panel for update screen."
  []
  (s/grid-panel :items [(s/text :editable? false)]))

(defn entry-field
  "Generate default entry field with given text."
  [text editable?]
  (s/text :text text :halign :leading :editable? editable?))

(defn category-fn
  "Update listbox based on combobox selection."
  [listbox combobox & _]
  (s/config! listbox :model (obj/gen-list (.getSelectedItem combobox))))

(defn earth-station-panel
  "Generate panel for earth station update."
  [listbox combobox es-id]
  (let [es-map (get-in @data/settings [:earth-station es-id])
        name-field (entry-field (:name es-map) true)
        id-field (entry-field es-id false)
        lat-field (entry-field (:latitude es-map) true)
        lon-field (entry-field (:longitude es-map) true)
        alt-field (entry-field (:altitude es-map) true)
        enable-toggle (s/toggle :text "Enable" :selected? (:enabled? es-map))
        update-button (s/button :text "Update")
        update-fn (fn [& _] 
                    (let [n (.trim (s/text name-field))
                          lat (try (Double/parseDouble (s/text lat-field))
                                (catch Exception e nil))
                          lon (try (Double/parseDouble (s/text lon-field))
                                (catch Exception e nil))
                          alt (try (Double/parseDouble (s/text alt-field))
                                (catch Exception e nil))]
                      (when-not (.isEmpty n)
                        (data/update-construct! :earth-station es-id
                                                :name n))
                      (when-not (nil? lat)
                        (data/update-construct! :earth-station es-id
                                                :latitude lat))
                      (when-not (nil? lon)
                        (data/update-construct! :earth-station es-id
                                                :longitude lon))
                      (when-not (nil? alt)
                        (data/update-construct! :earth-station es-id
                                                :altitude alt))
                      (data/update-construct! 
                        :earth-station es-id
                        :enabled? (s/config enable-toggle :selected?)))
                    (category-fn listbox combobox))]
    (s/listen update-button :action update-fn)
    (s/grid-panel :rows data/grid-rows
                  :border data/border-size
                  :items ["Name:" name-field
                          "Identifier:" id-field
                          "Latitude (deg):" lat-field
                          "Longitude (deg):" lon-field
                          "Altitude (m):" alt-field
                          (s/flow-panel 
                            :items [enable-toggle update-button] 
                            :align :left)])))

(defn select-fn
  "Generate update panel for selected construct."
  [listbox combobox update-panel & _]
  (let [selected-item (.getSelectedValue listbox)
        selected-type (:type selected-item)
        selected-id (:id selected-item)]
    (.invalidate update-panel)
    (s/config! update-panel 
               :items [(condp = selected-type
                         :earth-station (earth-station-panel 
                                          listbox combobox selected-id)
                         (default-panel))])
    (.revalidate update-panel)))

(defn add-fn
  "Generate dialog to add construct."
  [listbox combobox & _]
  (let [name-field (s/text)
        construct-str (str "Enter new " 
                           (.toLowerCase (.getSelectedItem combobox)) " name:")
        success-fn (fn [& _] 
                     (let [t (.trim (s/text name-field))
                           category (get data/categories 
                                         (.getSelectedItem combobox))]
                               (if-not (.isEmpty t)
                                 (data/add-construct! category t)
                                 (s/alert "Name cannot be blank."))
                               (category-fn listbox combobox)))]
    (-> (s/dialog :content (s/grid-panel :items [construct-str name-field])
                  :option-type :ok-cancel :success-fn (partial success-fn))
     s/pack! (doto (.setLocationRelativeTo (.getRootPane combobox))) s/show!)))

(defn remove-fn
  "Generate dialog to remove construct"
  [listbox combobox & _]
  (let [selected-type (get data/categories (.getSelectedItem combobox))
        selected-name (:name (.getSelectedValue listbox))
        selected-id (:id (.getSelectedValue listbox))
        rm-str (str "Are you sure you want to remove " selected-name "?")
        success-fn (fn [& _] (data/remove-construct! 
                               selected-type selected-id)
                     (category-fn listbox combobox))]
    (when-not (nil? selected-name)
      (-> (s/dialog :content (s/grid-panel :items [rm-str])
                    :option-type :yes-no :success-fn (partial success-fn))
        s/pack! (doto (.setLocationRelativeTo (.getRootPane combobox)))
        s/show!))))

(defn select-panel
  "Generate selection panel."
  [edit-panel]
  (let [selection-box (s/listbox)
        category-box (s/combobox :model (sort (keys data/categories)))
        add-button (s/button :text "Add")
        remove-button (s/button :text "Remove")
        button-panel (s/grid-panel :rows 1 :items [add-button remove-button])]
    (s/listen category-box :selection (partial category-fn 
                                               selection-box category-box))
    (s/listen selection-box :selection (partial select-fn
                                                selection-box category-box
                                                edit-panel))
    (s/listen add-button :action (partial add-fn
                                          selection-box category-box))
    (s/listen remove-button :action (partial remove-fn
                                             selection-box category-box))
    (category-fn selection-box category-box)
    (s/border-panel :north category-box :center (s/scrollable selection-box) 
                    :south button-panel :border data/border-size)))

(defn edit-panel
  "Generate editing panel."
  []
  (let [center-panel (default-panel)
        left-panel (select-panel center-panel)]
    (s/border-panel :west left-panel :center center-panel)))