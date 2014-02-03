(ns airport.xml
  (:require [airport.util :refer [dbg as-short-xml dz az]]
            [clojure.xml :as c-xml]
            [clojure.data.xml :as c-d-xml :refer [parse parse-str]]
            [clojure.zip :as c-zip :refer [xml-zip]]
            [clojure.data.zip :as c-d-zip]
            [clojure.data.zip.xml :as c-d-z-xml :refer [xml-> xml1-> attr= text]]
            [clojure.java.io :as io]
            [clojure.pprint :refer [pprint]]
            [camel-snake-kebab :as csk]))

(def load-dict)
(def load-array)

(defn parse-val [v]
  (let [tag (:tag (c-zip/node v))]
    (case tag
      :dict (load-dict v)
      :integer (Integer/parseInt (text v))
      :string (text v)
      :data "<data>"
      :false false
      :true true
      :array (load-array v)
      :else (str "unknown:" tag))))

(defn load-array [array]
  (map parse-val (c-d-zip/children array)))

(defn load-dict [dict]
  (into {}
        (for [[k v] (partition 2 (c-d-zip/children dict))]
          (let [key (keyword (csk/->kebab-case (text k)))
                val (parse-val v)]
            [key val]))))

(defn parse-airport-xml [s]
  (let [xml (parse-str s)]
    (xml-> (xml-zip xml)
           ;:plist
           :array
           :dict
           load-dict
           )))
