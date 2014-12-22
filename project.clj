(defproject satmod-lambda "0.0.1"
  :description "Satellite constellation analysis software."
  :repositories {"local" "file:lib"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.github.insubstantial/substance "7.3"]
                 [commons-lang "2.6"]
                 [predict4java "1.1.158.4"]
                 [seesaw "1.4.4"]]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :profiles {:uberjar {:global-vars {*warn-on-reflection* true}}}
  :aot :all
  :omit-source true
  :main satmod-lambda.core)
