(defproject airport "0.0.1-SNAPSHOT"
  :description "Cool new project to do things and stuff"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.xml "0.0.7"]
                 [org.clojure/data.zip "0.1.1"]
                 [org.apache.commons/commons-compress "1.5"]
                 [camel-snake-kebab "0.1.3"]
                 [overtone/at-at "1.2.0"]
                 [me.raynes/conch "0.5.0"]
                 [clj-time "0.6.0"]]
  :checksum :warn
  :main airport.core
  :profiles {:dev {:dependencies [[midje "1.6.0"]]}})
  
