(defproject satmod-lambda "0.0.1-SNAPSHOT"
  :description "satellite constellation analysis software"
  :repositories {"local" "file:lib"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.github.insubstantial/substance "7.3"]
                 [commons-lang "2.6"]
                 [predict4java "1.1.158.4"]
                 [seesaw "1.4.4"]]
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :profiles {:repl {:dependencies [[leiningen "2.5.0"]
                                   [lein-marginalia "0.8.0"]]
                    :plugins [[lein-marginalia "0.8.0"]]}}
  :aot [satmod-lambda.core]
  :main satmod-lambda.core)
