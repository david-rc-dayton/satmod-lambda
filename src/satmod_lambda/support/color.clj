(ns satmod-lambda.support.color)

(defn map->color
  "Convert map containing the keys `{:r :g :b :a}` for red, blue, green, and
   alpha integer values (0-255) to a Java Color object."
  [{:keys [r g b a] :or {:r 0 :g 0 :b 0 :a 255}}]
  (java.awt.Color. ^int r ^int g ^int b ^int a))

(defn adjust-brightness
  "Adjust RGB Values of a color map based on a percent (0 - 100)."
  [{:keys [r g b a] :or {:r 0 :g 0 :b 0 :a 255}} brightness]
  (let [br (/ brightness 100)]
    {:r (int (* r br)) :g (int (* g br)) :b (int (* b br)) :a a}))