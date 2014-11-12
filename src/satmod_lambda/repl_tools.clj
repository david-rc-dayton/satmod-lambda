(ns satmod-lambda.repl-tools
  "Runnable functions for the Clojure REPL to use Leiningen plugins."
  (:require [clojure.java.io :as io]
            [leiningen.marg])
  (:use [leiningen.core.project :only [init-project read-raw]]))

(defn project-clj
  "Get project map from file tree."
  []
  (let [path (.getCanonicalPath (io/file "project.clj"))]
    (init-project (read-raw path))))

(defn build-docs
  "Construct documentation programmatically using Marginalia."
  []
  (leiningen.marg/marg (project-clj)))
