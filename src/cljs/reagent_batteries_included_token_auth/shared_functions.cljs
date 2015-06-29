(ns reagent-batteries-included-token-auth.shared-functions
  (:require [alandipert.storage-atom :refer [local-storage]]
            [reagent-batteries-included-token-auth.shared-state :refer [nav-state]]
            [secretary.core :as secretary :include-macros true]))

(defn mobile-nav-click [route active-name]
  (swap! nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @nav-state)))
  (swap! nav-state assoc :active-route active-name)
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (if (= route-name (:active-route @nav-state)) "active" ""))
