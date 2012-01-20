(ns lingr-fav-bot.core
  (:use
    [clojure.data.json :only (read-json)]
    [compojure.core]
    [ring.adapter.jetty]))

(defroutes hello
  (GET "/" [] "working")
  (POST "/"
        {body :body}
        (let [message (:message (first (:events (read-json (slurp body)))))]
          (str message))))

(defn -main []
  (run-jetty hello {:port 4002}))
