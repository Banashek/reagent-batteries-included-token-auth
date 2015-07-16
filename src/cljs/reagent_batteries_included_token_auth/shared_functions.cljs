(ns reagent-batteries-included-token-auth.shared-functions
  (:require [alandipert.storage-atom :refer [local-storage]]
            [reagent.session :as session]
            [ajax.core :refer [GET]]
            [reagent-batteries-included-token-auth.shared-state :refer [nav-state auth-creds-ratom]]
            [cljs-time.core :as t]
            [cljs-time.coerce :as coerce-t]
            [goog.crypt.base64 :as b64]
            [secretary.core :as secretary :include-macros true]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.index :as index]))

(defn mobile-nav-click [route active-name]
  (swap! nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @nav-state)))
  (swap! nav-state assoc :active-route active-name)
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (if (= route-name (:active-route @nav-state)) "active" ""))


(defn token-fresh? []
  (let [set-at (coerce-t/from-long (:time-stamp @auth-creds-ratom))
        now    (t/date-time (t/now))
        delta  (t/in-minutes (t/interval set-at now))]
    (< delta 15)))

;; ==============================
;  LogIn User
(defn login-response-handler [response]
  (swap! ss/auth-creds-ls assoc :token (:token response))
  (swap! ss/auth-creds-ls assoc :refresh-token (:refreshToken response))
  (swap! ss/auth-creds-ls assoc :permissions (:permissions response))
  (swap! ss/auth-creds-ls assoc :username (:username response))
  (swap! ss/auth-creds-ls assoc :time-stamp (coerce-t/to-long (t/date-time (t/now))))
  (reset! ss/flash-message {:kind "success" :text (str "Welcome " (:username response))})
  (if (= @ss/secured-route "")
    (session/put! :current-page #'index/index-page)
    (do (session/put! :current-page @ss/secured-route) (reset! ss/secured-route ""))))

(defn error-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn auth-header [username password]
  (str "Basic " (b64/encodeString (str username ":" password))))

(defn attempt-login [username password]
  (GET "https://button-pusher-server.herokuapp.com/api/auth" {:headers         {"Authorization" (auth-header username password)}
                                                              :handler         login-response-handler
                                                              :error-handler   error-handler
                                                              :response-format :json
                                                              :keywords?       true
                                                              :prefix          true}))

