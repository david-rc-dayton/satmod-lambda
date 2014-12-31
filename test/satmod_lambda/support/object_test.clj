(ns satmod-lambda.support.object-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.support.object :refer :all]
            [satmod-lambda.support.data :as data]))

(deftest constructor-test
  (testing "object creation"
    (let [sat-type :satellite
          sat-name "Test Satellite"
          sat-id "1234567890"
          sml-sat (gen-object sat-type sat-name sat-id)]
      (is (= sat-type (:type sml-sat)))
      (is (= sat-name (:name sml-sat)))
      (is (= sat-id (:id sml-sat)))
      (is (= sat-name (.toString sml-sat))))))

(deftest list-test
  (testing "list creation"
    (data/add-construct! :satellite "test-1")
    (data/add-construct! :satellite "test-2")
    (data/add-construct! :satellite "test-3")
    (let [construct-list (gen-list :satellite)]
      (is (= (list "test-1" "test-2" "test-3")
             (sort (map #(.toString %) construct-list)))))))
