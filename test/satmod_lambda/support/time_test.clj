(ns satmod-lambda.support.time-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.support.time :refer :all]))

(deftest date-test
  (testing "date conversion"
    (let [date-map {:year 2014 :month 11 :day 17
                    :hour 19 :minute 04 :second 48}]
      (is (= (type (now)) java.util.Date))
      (is (= (date->hash-map (hash-map->date date-map)) date-map)))))
