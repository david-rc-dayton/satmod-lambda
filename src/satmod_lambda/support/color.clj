(ns satmod-lambda.support.color)

(defn map->color
  "Convert map containing the keys `{:r :g :b :a}` for red, blue, green, and
   alpha integer values (0-255) to a Java Color object."
  [{:keys [r g b a]
    :or {:r 0 :g 0 :b 0 :a 255}}]
  (java.awt.Color. ^int r ^int g ^int b ^int a))