(ns satmod-lambda.construct.earth-station-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.construct.earth-station :as es]))

(deftest constructor-test
  (testing "earth-station object creation"
           (let [es-name "Test Earth Station"
                 sml-es (es/gen-earth-station-object es-name)]
             (is (= es-name (.toString sml-es)))
             (is (= es-name (:name sml-es)))
             (is (= 32 (count (:id sml-es)))))))