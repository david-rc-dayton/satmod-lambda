(ns satmod-lambda.core
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.ui.coverage :as cov]
            [satmod-lambda.ui.edit :as edit]
            [seesaw.core :as s])
  (:import [org.pushingpixels.substance.api SubstanceLookAndFeel]
           [org.pushingpixels.substance.api.skin GraphiteSkin]
           [javax.swing ImageIcon])
  (:gen-class))

(def display-name (str data/title " " (data/version)))

(defn center!
  "Center frame on screen."
  [frame]
  (.setLocationRelativeTo frame nil))

(defn maximize!
  "Maximize frame."
  [frame]
  (.setExtendedState frame 6))

(defn splash-screen 
  "Build splash-screen window."
  []
  (s/window :content data/splash-image))

(defn main-panel
  "Generate program's main panel."
  []
  (let [coverage-button (s/button :text "Coverage")
        edit-button (s/button :text "Edit")
        button-panel (s/horizontal-panel :items [coverage-button edit-button]
                                         :border data/border-size)
        card-panel (s/card-panel :items [[(cov/coverage-panel) :cov-panel]
                                         [(edit/edit-panel) :edit-panel]])]
    ;; add change-card listeners to buttons
    (s/listen coverage-button :action (fn [_] (s/show-card!
                                                card-panel :cov-panel)))
    (s/listen edit-button :action (fn [_] (s/show-card! 
                                            card-panel :edit-panel)))
    (s/border-panel :north button-panel :center card-panel)))

(defn -main
  [& args]
  (let [splash (splash-screen)]
    (future (doto splash (.setAlwaysOnTop true) s/pack! center! s/show!))
    (SubstanceLookAndFeel/setSkin (GraphiteSkin.))
    (data/load-settings!)
    (s/invoke-later
      (doto (s/frame :title display-name
                     :content (main-panel)
                     :icon data/icon
                     :on-close :exit
                     :size [800 :by 600])
        center! maximize! s/show!)
      (future (do (Thread/sleep 3000)) 
        (.dispose splash)))))
