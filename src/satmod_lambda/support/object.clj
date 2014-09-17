(ns satmod-lambda.support.object)

(defrecord SMLObject [type name id]
  Object
  (toString [this] (:name this)))

(defn gen-object
  "Create new object for List display."
  [type name id]
  (->SMLObject type name id))