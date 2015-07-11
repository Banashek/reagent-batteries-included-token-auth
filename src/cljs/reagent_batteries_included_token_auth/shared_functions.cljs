(ns reagent-batteries-included-token-auth.shared-functions
  (:require [alandipert.storage-atom :refer [local-storage]]
            [reagent-batteries-included-token-auth.shared-state :refer [nav-state auth-creds-ratom]]
            [cljs-time.core :as t]
            [cljs-time.coerce :as coerce-t]
            [secretary.core :as secretary :include-macros true]))

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
