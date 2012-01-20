(ns lingr-fav-bot.core
  (:use
    [clojure.data.json :only (read-json)]
    [compojure.core]
    [ring.adapter.jetty]))

(def favs (atom []))
(def lastmsg "dummy")

(defn format-fav [fav]
  (str "* [" (:nickname fav) "] " (:text fav)))

(defroutes hello
  (GET "/" [] "fav is working")
  (POST "/"
        {body :body}
        (let [message (:message (first (:events (read-json (slurp body)))))]
          (cond
            (= (:text message) "f:all") (apply str (interpose "\n" (map format-fav @favs)))
            (= (:text message) "f:av") (do
                                         (swap! favs #(cons lastmsg %))
                                         "")
            :else (def lastmsg message)))))

(defn -main []
  (run-jetty hello {:port 4002}))
