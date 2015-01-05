(ns satmod-lambda.support.time)

(defn now
  "Return Date Object for current time."
  []
  (java.util.Date.))

(defn hash-map->date
  "Create a Java Date Object using a map containing: <br>
  `{:year :month :day :hour :minute: :second}` <br>
  in UTC time.
  Values default to 0 if not included in the input map."
  [{:keys [year month day hour minute second]
    :or {year 0 month 0 day 1 hour 0 minute 0 second 0}}]
  (let [calendar (doto (java.util.Calendar/getInstance
                         (java.util.TimeZone/getTimeZone "UTC"))
                   (.set year (dec month) day hour minute second)
                   (.set java.util.Calendar/MILLISECOND 0))]
    (.getTime calendar)))

(defn date->hash-map
  "Create a map containing the keys `{:year :month :day :hour :minute :second}`
  in UTC time for a given Java Date Object."
  [date]
  (let [calendar (doto (java.util.Calendar/getInstance
                         (java.util.TimeZone/getTimeZone "UTC"))
                   (.setTime date))
        year (.get calendar java.util.Calendar/YEAR)
        month (inc (.get calendar java.util.Calendar/MONTH))
        day (.get calendar java.util.Calendar/DATE)
        hour (.get calendar java.util.Calendar/HOUR_OF_DAY)
        minute (.get calendar java.util.Calendar/MINUTE)
        second (.get calendar java.util.Calendar/SECOND)]
    {:year year :month month :day day
     :hour hour :minute minute :second second}))
