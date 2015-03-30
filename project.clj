(defproject satmod-lambda "0.0.3-SNAPSHOT"
  :description "Satellite constellation analysis software."
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [clj-predict "0.0.2"]
                 [com.github.insubstantial/substance "7.3"]
                 [seesaw "1.4.5"]]
  :profiles {:uberjar {:aot :all :omit-source true}}
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :aot [satmod-lambda.core]
  :main satmod-lambda.core
  :manifest {"SplashScreen-Image" "splash.png"})
