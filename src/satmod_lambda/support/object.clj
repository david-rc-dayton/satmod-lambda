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
  (let [obj-fn (fn [v] (gen-object category (:name (val v)) (key v)))]
    (map obj-fn (sort-by #(clojure.string/lower-case (:name (val %))) 
                         (get @data/settings category)))))