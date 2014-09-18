(ns satmod-lambda.support.object
  (:require [satmod-lambda.support.data :as data]))

(defrecord SMLObject [type name id]
  Object
  (toString [this] (:name this)))

(defn gen-object
  "Create new object for List display."
  [type name id]
  (->SMLObject type name id))

(defn gen-list
  "Generate list of items for list object model."
  [category]
  (let [type (get data/categories category)
        obj-fn (fn [v] (gen-object type (:name (val v)) (key v)))]
    (map obj-fn (get @data/settings type))))