(ns satmod-lambda.construct.satellite
  (:require [satmod-lambda.support.data :as data]))

(defrecord SMLSatellite [name id]
  Object
  (toString [this] (:name this)))

(defn gen-satellite-object
  "Create new satellite object for List display."
  [name id]
  (->SMLSatellite name id))