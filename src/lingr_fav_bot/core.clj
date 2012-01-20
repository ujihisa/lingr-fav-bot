(ns lingr-fav-bot.core
  (:use
    [clojure.data.json :only (read-json)]
    [compojure.core]
    [ring.adapter.jetty]))

(def favs (atom []))
(def lastmsg "dummy")

(defroutes hello
  (GET "/" [] "working")
  (POST "/"
        {body :body}
        (let [message (:message (first (:events (read-json (slurp body)))))]
          (cond
            (= (:text message) "f:all") (apply str (interpose "\n" @favs))
            (= (:text message) "f:av") (do
                                         (swap! favs #(cons (str lastmsg) %))
                                         "")
            :else (def lastmsg message)))))

(defn -main []
  (run-jetty hello {:port 4002}))
