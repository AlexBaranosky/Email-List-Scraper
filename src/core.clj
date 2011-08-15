(ns core
  (:gen-class)
  (:use [clojure.contrib.duck-streams :only [reader]]
        [net.cgrand.enlive-html :only [html-resource select text]]
        [clojure.contrib.core :only [-?>]])) ;; unused.

(def num-search-results-to-use 10)
(def search-terms ["bananas", "blamma"])
(def selectors [[:#someid]])

(defn google-results [search-term]
  ["http://www.google.com", "http://www.amazon.com"])

(defn urls-from-search-terms [terms]
  (mapcat #(take num-search-results-to-use (google-results %)) terms))

(defn url-matches? [url selector]
  (let [html (-> url reader html-resource)]
    (-> html (select selector) seq))) 
  
(defn -main [& _]
  (doseq [u (urls-from-search-terms search-terms)]
    (doseq [s selectors]
      (if (url-matches? u s)
        (println (format "'%s' matches selector: %s" u s))
        (println (format "'%s' does not match selector: %s" u s))))))
