(ns satmod-lambda.support.data-test
  (:require [clojure.test :refer :all]
            [satmod-lambda.support.data :refer :all]))

(deftest gen-id-test
  (testing "unique id values"
           (is (apply distinct? (take 100000 (repeatedly gen-id))))))

(deftest construct-test
  (add-construct! :satellite "satellite")
  (add-construct! :earth-station "earth-station")
  (let [satellite-key (key (first (:satellite @settings)))
        earth-station-key (key (first (:earth-station @settings)))]
    (testing "add settings construct"
             (is (= (:name (get-construct :satellite satellite-key))
                    "satellite"))
             (is (= (:name (get-construct :earth-station earth-station-key))
                    "earth-station")))
    (update-construct! :satellite satellite-key 
                       :name "satellite-update")
    (update-construct! :earth-station earth-station-key 
                       :name "earth-station-update")
    (testing "update settings construct"
             (is (= (:name (get-construct :satellite satellite-key))
                    "satellite-update"))
             (is (= (:name (get-construct :earth-station earth-station-key))
                    "earth-station-update")))
    (remove-construct! :satellite satellite-key)
    (remove-construct! :earth-station earth-station-key)
    (testing "remove settings construct"
             (is (= @settings {:satellite {} :earth-station {}})))))