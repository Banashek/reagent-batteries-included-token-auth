(ns reagent-batteries-included-token-auth.shared-state
  (:require [alandipert.storage-atom :refer [local-storage]]
            [reagent.core :as ratom]))

(def auth-creds-ls (local-storage (atom {}) :auth-creds))
(def auth-creds-ratom (ratom/atom {}))
(def flash-message (ratom/atom ""))
(def nav-state (ratom/atom {:mobile-menu-visiable false :active-route ""}))

;; ====
;  On booting the app get auth-creds from localStorage
;; ====
(reset! auth-creds-ratom @auth-creds-ls)

(add-watch flash-message :the-flash
  (fn [key atom old-state new-state]
    (if (not= new-state "")
      (js/setTimeout #(reset! flash-message "") 2000))))

(add-watch auth-creds-ls :cred-change
  (fn [key atom old-state new-state]
    (reset! auth-creds-ratom new-state)))
