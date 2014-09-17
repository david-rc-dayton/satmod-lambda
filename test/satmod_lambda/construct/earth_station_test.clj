(ns satmod-lambda.construct.earth-station-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.construct.earth-station :refer :all]))

(deftest constructor-test
  (testing "earth-station object creation"
           (let [es-name "Test Earth Station"
                 es-id "12345"
                 sml-es (gen-earth-station-object es-name es-id)]
             (is (= es-name (.toString sml-es)))
             (is (= es-name (:name sml-es)))
             (is (= es-id (:id sml-es))))))