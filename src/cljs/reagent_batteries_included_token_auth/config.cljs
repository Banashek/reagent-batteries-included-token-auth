(ns reagent-batteries-included-token-auth.config)

(def endpoints {:user "https://button-pusher-server.herokuapp.com/api/user"
                :auth "https://button-pusher-server.herokuapp.com/api/auth"
                :refresh-token "https://button-pusher-server.herokuapp.com/api/refresh-token/"
                :reset-confirm "https://button-pusher-server.herokuapp.com/api/password/reset-confirm"})
