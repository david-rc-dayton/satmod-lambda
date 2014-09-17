(ns satmod-lambda.construct.satellite-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.construct.satellite :refer :all]))

(deftest constructor-test
  (testing "satellite object creation"
           (let [sat-name "Test Satellite"
                 sat-id "12345"
                 sml-sat (gen-satellite-object sat-name sat-id)]
             (is (= sat-name (.toString sml-sat)))
             (is (= sat-name (:name sml-sat)))
             (is (= sat-id (:id sml-sat))))))