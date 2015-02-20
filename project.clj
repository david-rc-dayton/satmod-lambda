(defproject satmod-lambda "0.0.2-SNAPSHOT"
  :description "Satellite constellation analysis software."
  :repositories {"local" "file:lib"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.github.insubstantial/substance "7.3"]
                 [commons-lang "2.6"]
                 [predict4java "1.1.158.4"]
                 [seesaw "1.4.5"]]
  :profiles {:uberjar {:aot :all
                       :omit-source true}}
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :aot [satmod-lambda.core]
  :main satmod-lambda.core
  :manifest {"SplashScreen-Image" "splash.png"})
