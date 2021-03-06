(ns satmod-lambda.core
  (:require [satmod-lambda.support.data :as data]
            [satmod-lambda.ui.coverage :as cov]
            [satmod-lambda.ui.edit :as edit]
            [seesaw.core :as s]
            [clojure.java.io :as io])
  (:import [org.pushingpixels.substance.api SubstanceLookAndFeel]
           [org.pushingpixels.substance.api.skin GraphiteSkin])
  (:gen-class))

(def title "SatMod-\u03BB")

(defmacro version []
  (System/getProperty "satmod-lambda.version"))

(def display-name (str title " " (version)))

(def icon "icon.png")

(def window-size [854 :by 480])

(def root
  "Main program panel."
  (atom nil))

(defn center!
  "Center frame on screen."
  [^javax.swing.JFrame frame]
  (.setLocationRelativeTo frame nil))

(defn maximize!
  "Maximize frame."
  [^javax.swing.JFrame frame]
  (.setExtendedState frame 6))

(defn main-panel
  "Generate program's main panel."
  []
  (let [coverage-button (s/button :text "Coverage" :id :coverage-button)
        edit-button (s/button :text "Edit" :id :edit-button)
        display-panel (s/card-panel :items [[(cov/coverage-panel) :cov-panel]
                                            [(edit/edit-panel) :edit-panel]]
                                    :border data/border-size)]
    (s/listen coverage-button
              :action (fn [_] (s/show-card! display-panel :cov-panel)
                        (cov/refresh-image)))
    (s/listen edit-button
              :action (fn [_] (s/show-card! display-panel :edit-panel)))
    (reset! root (s/border-panel
                   :north (s/horizontal-panel :items [coverage-button
                                                      edit-button]
                                              :border data/border-size)
                   :center display-panel))))

(defn -main
  "Program entry point."
  [& args]
  (SubstanceLookAndFeel/setSkin (GraphiteSkin.))
  (data/load-settings!)
  (s/invoke-later
    (doto (s/frame :title display-name
                   :content (main-panel)
                   :icon icon
                   :on-close :exit
                   :size window-size
                   :minimum-size window-size)
      center! maximize! s/show!)))
