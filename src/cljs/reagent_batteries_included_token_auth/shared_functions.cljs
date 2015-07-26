(ns reagent-batteries-included-token-auth.shared-functions
  (:require [ajax.core :refer [GET]]
            [cljs-time.core :as t]
            [promesa.core :as p]
            [cljs-time.coerce :as coerce-t]
            [secretary.core :as secretary :include-macros true]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(defn mobile-nav-click [route active-name]
  (swap! ss/nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @ss/nav-state)))
  (swap! ss/nav-state assoc :active-route active-name)
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (if (= route-name (:active-route @ss/nav-state)) "active" ""))

(defn token-fresh? []
  (let [set-at (coerce-t/from-long (:time-stamp @ss/auth-creds-ratom))
        now    (t/date-time (t/now))
        delta  (t/in-minutes (t/interval set-at now))]
    (< delta 15)))

(defn refresh-token-success-handler [response]
  (swap! ss/auth-creds-ls assoc :token (:token response))
  (swap! ss/auth-creds-ls assoc :refresh-token (:refreshToken response))
  (swap! ss/auth-creds-ls assoc :time-stamp (coerce-t/to-long (t/date-time (t/now)))))

(defn refresh-token-request []
  (p/promise
    (fn [resolve reject]
      (GET (str "https://button-pusher-server.herokuapp.com/api/refresh-token/" (:refresh-token @ss/auth-creds-ratom)) {:handler resolve
                                                                                                                        :error-handler reject}))))

(defn refresh-token [callback]
  (-> (refresh-token-request)
      (p/then #(refresh-token-success-handler %))
      (p/then (callback))))
