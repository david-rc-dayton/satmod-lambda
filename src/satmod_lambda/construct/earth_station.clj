(ns satmod-lambda.construct.earth-station
  (:require [satmod-lambda.support.data :as data]))

(defrecord SMLEarthStation [name id]
  Object
  (toString [this] (:name this)))

(defn gen-earth-station-object
  "Create new earth-station object for List display."
  [name]
  (->SMLEarthStation name (data/gen-id)))