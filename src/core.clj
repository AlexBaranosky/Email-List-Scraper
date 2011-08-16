(ns core
  (:gen-class)
  (:use [clojure.contrib.duck-streams :only [reader]]
        [net.cgrand.enlive-html :only [html-resource select text]]
        [clojure.contrib.http.agent :only [string http-agent]]
        [clojure.contrib.json :only [read-json]])
  (import java.net.URLEncoder))

(defn- url-encode [s]
  (URLEncoder/encode s "UTF-8"))

(def num-search-results-to-use 2)

(def search-terms (map url-encode ["banana split", 
                                   "leopards"]))
(def selectors [[:#footer] 
                [:#content]])

(defn- google-url-results-for [search-term]
  (let [request-url (str "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=" search-term)
        json-response (-> request-url 
                          http-agent 
                          string 
                          (read-json true))]
    (->> json-response :responseData :results (map :url))))

(defn- urls-from-search-terms [terms]
  (mapcat #(take num-search-results-to-use (google-url-results-for %)) terms))

(defn -main [& _]
  (doseq [u (urls-from-search-terms search-terms)]
    (let [html (-> u reader html-resource)]
      (doseq [s selectors]
        (if (-> html (select s) seq)
          (println (format "'%s' matches selector: %s" u s))
          (println (format "'%s' does not match selector: %s" u s)))))))
