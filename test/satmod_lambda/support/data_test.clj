(ns satmod-lambda.support.data-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.support.data :refer :all]))

(deftest gen-id-test
  (testing "unique id values"
    (is (apply distinct? (take 100000 (repeatedly gen-id))))))
