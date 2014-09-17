(ns satmod-lambda.support.object-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.support.object :refer :all]))

(deftest constructor-test
  (testing "satellite object creation"
           (let [sat-type :satellite
                 sat-name "Test Satellite"
                 sat-id "1234567890"
                 sml-sat (gen-object sat-type sat-name sat-id)]
             (is (= sat-type (:type sml-sat)))
             (is (= sat-name (:name sml-sat)))
             (is (= sat-id (:id sml-sat)))
             (is (= sat-name (.toString sml-sat)))))
  (testing "earth-station object creation"
           (let [es-type :earth-station
                 es-name "Test EarthStation"
                 es-id "1234567890"
                 sml-es (gen-object es-type es-name es-id)]
             (is (= es-type (:type sml-es)))
             (is (= es-name (:name sml-es)))
             (is (= es-id (:id sml-es)))
             (is (= es-name (.toString sml-es))))))