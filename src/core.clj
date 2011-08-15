(ns core
  (:gen-class)
  (:use [clojure.contrib.duck-streams :only [reader]]
        [net.cgrand.enlive-html :only [html-resource select text]]
        [clojure.contrib.core :only [-?>]])) ;; unused.

(def urls-to-scrape ["http://www.google.com"])

(def selectors [[:#someid]])

(defn url-matches? [url selector]
  (let [html (-> url reader html-resource)]
    (-> html (select selector) seq))) 
  
(defn -main [& _]
  (doseq [u urls-to-scrape]
    (doseq [s selectors]
      (if (url-matches? u s)
        (println (format "'%s' matches selector: %s" u s))
        (println (format "'%s' does not match selector: %s" u s))))))
