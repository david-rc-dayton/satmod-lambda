(ns satmod-lambda.support.data-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.support.data :refer :all]))

(deftest gen-id-test
  (testing "unique values"
           (is (apply distinct? (take 1000000 (repeatedly gen-id))))))

(deftest construct-test
  (testing "add/retrieve setting constructs"
           (add-construct! :satellite "test-sat")
           (add-construct! :earth-station "test-es")
           (let [sat-key (key (first (:satellite @settings)))
                 es-key (key (first (:earth-station @settings)))]
             (is (= "test-sat" (:name (get-construct :satellite sat-key))))
             (is (= "test-es" (:name (get-construct :earth-station es-key)))))))