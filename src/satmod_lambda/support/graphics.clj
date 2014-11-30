(ns satmod-lambda.support.graphics)

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

(defn imagetoolkit->bufferedimage
  [image-toolkit]
  (let [buffered-image (java.awt.image.BufferedImage. 
                         (.getWidth image-toolkit) (.getHeight image-toolkit)
                         java.awt.image.BufferedImage/TYPE_INT_ARGB)
        bgr (.createGraphics buffered-image)]
    (.drawImage bgr image-toolkit 0 0 nil)
    (.dispose bgr) buffered-image))

(defn copy-image
  "Make a deep copy of a buffered image."
  [^java.awt.image.BufferedImage buffered-image]
  (let [cm (.getColorModel buffered-image)
        alpha? (.isAlphaPremultiplied cm)
        raster (.copyData buffered-image nil)]
    (java.awt.image.BufferedImage. cm raster alpha? nil)))

(defn save-image
  "Save PNG image to specified file-name after optionally scaling to
   [width height] dimensions."
  ([image file-name]
    (let [check-name (.toLowerCase file-name)
          f-n (if-not (.endsWith check-name ".png")
                (str file-name ".png") file-name)]
      (println image)
      (println file-name)
      (javax.imageio.ImageIO/write (imagetoolkit->bufferedimage image)
                                   "png" (java.io.File. f-n))))
  ([image file-name [width height]]
    (let [scaled-image (.getScaledInstance image width height
                         java.awt.Image/SCALE_AREA_AVERAGING)]
      (save-image scaled-image file-name))))