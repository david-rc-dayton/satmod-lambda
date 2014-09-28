(ns satmod-lambda.support.data
  (:require [clojure.java.io :as io])
  (:import [java.util Random]))

(def title "SatMod-\u03BB")

(defmacro version []
  (System/getProperty "satmod-lambda.version"))

(def icon "icon.png")

(def splash-image (io/resource "splash.png"))

(def settings-dir (str (System/getProperty "user.home") "/.satmod-lambda"))

(def settings-file (str settings-dir "/settings.edn"))

(def default-color {:r 0 :g 255 :b 0})

(def categories
  "Available categories and their keyword maps."
  {"Satellite"     :satellite
   "Earth Station" :earth-station})

(def settings (atom {}))

(def border-size 5)
(def grid-rows 20)

(defn gen-id
  "Generate (hopefully) unique hexadecimal identifier."
  []
  (let [bits 256
        random-gen (Random.)]
    (.toString (BigInteger. bits random-gen) 16)))

(defn reset-settings!
  "Remove values from settings atom."
  []
  (reset! settings {}))

(defn watch-settings
  "Update settings file to reflect changes in software settings."
  []
  (let [file (io/as-file settings-file)]
    (add-watch settings :watch-settings 
               (fn [_ _ _ nv] (spit file (with-out-str (pr nv)))))))

(defn load-settings!
  "Load settings from filesystem, or create new settings file if non-existant."
  []
  (let [dir (io/as-file settings-dir)
        file (io/as-file settings-file)]
    (when-not (.exists dir)
      (.mkdir dir))
    (when-not (.exists file)
      (spit file (with-out-str (pr @settings))))
    (reset! settings (read-string (slurp file)))
    (watch-settings)))

(defn add-construct!
  "Add new construct to settings map."
  [construct-key name]
  (swap! settings assoc-in [construct-key (gen-id)] {:name name}))

(defn remove-construct!
  "Remove construct from settings map."
  [construct-key id]
  (swap! settings update-in [construct-key] dissoc id))

(defn update-construct!
  "Update data for construct in settings map."
  [construct-key id data-key new-value]
  (swap! settings assoc-in [construct-key id data-key] new-value))

(defn get-construct
  "Get construct from settings map."
  [construct-key id]
  (get-in @settings [construct-key id]))
