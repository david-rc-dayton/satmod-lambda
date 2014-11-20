(ns satmod-lambda.support.color)

(defn map->color
  "Convert map containing the keys `{:r :g :b :a}` for red, blue, green, and
   alpha values to a Java Color object."
  [{:keys [r g b a]
    :or {:r 0 :g 0 :b 0 :a 255}}]
  (java.awt.Color. r g b a))