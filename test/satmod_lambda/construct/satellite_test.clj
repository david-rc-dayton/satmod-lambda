(ns satmod-lambda.construct.satellite-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.construct.satellite :as sat]))

(deftest constructor-test
  (testing "satellite object creation"
           (let [sat-name "Test Satellite"
                 sml-sat (sat/gen-satellite-object sat-name)]
             (is (= sat-name (.toString sml-sat)))
             (is (= sat-name (:name sml-sat)))
             (is (= 32 (count (:id sml-sat)))))))