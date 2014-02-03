(ns airport.core
  (:require [airport.xml :refer [parse-airport-xml]]
            [clojure.pprint :refer [pprint]]
            [clj-time.core :refer [now]]
            [clj-time.format :as fmt]
            [me.raynes.conch :as conch]

            [overtone.at-at :as at-at])
  (:gen-class))


(defn dump-data [d]
  (doseq [ap (sort-by :ssid-str d)]
    (printf "%20s channel: %d noise: %3d rssi: %3d\n" (:ssid-str ap)
             (:channel ap)
             (:noise ap)
             (:rssi ap))))

(def airport-timeout 5000)

(defn now-str []
  (fmt/unparse (fmt/formatters :hour-minute-second) (now)))

(defn run []
  (conch/let-programs [airport "/System/Library/PrivateFrameworks/Apple80211.framework/Versions/Current/Resources/airport"]
                      (println "scanning at " (now-str))
                      (let [result (airport "-s" "-x" {:timeout airport-timeout :verbose true})
                            exit-code @(:exit-code result)
                            out (:stdout result)]
                        (case exit-code
                          :timeout (println "timed out after" airport-timeout "ms")
                          0 (if-not (re-find #"^\s*<\?xml" out)
                              (println "Error: " out)
                              (-> out
                                  parse-airport-xml
                                  dump-data))
                          :else (println "Error returned: " exit-code "\n Output:\n" out)))))


#_(def filename "../airport.xml")
#_(pprint (parse-airport-xml (slurp filename)))

;(def my-pool (at-at/mk-pool))

(defn -main []
  (doseq [i (range 10000000)]
    (run)))

