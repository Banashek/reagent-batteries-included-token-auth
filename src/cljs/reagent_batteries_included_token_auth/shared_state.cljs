(ns reagent-batteries-included-token-auth.shared-state
  (:require [alandipert.storage-atom :refer [local-storage]]
            [reagent.core :as ratom]))

(def auth-creds-ls (local-storage (atom {}) :auth-creds))
(def auth-creds-ratom (ratom/atom {}))
(reset! auth-creds-ratom @auth-creds-ls)

(add-watch auth-creds-ls :anything
  (fn [key atom old-state new-state]
    (reset! auth-creds-ratom new-state)))

;; ==========
;  Perhaps useful for debugging
;; ==========
; (defn get-storage-atom-local-value [key-name]
;  (.getItem js/localStorage (str "[\"k\",\"" key-name "\"]")))

; (.log js/console "<>=<>")
; (.log js/console (get-storage-atom-local-value "auth-creds"))
; (.log js/console "<>=<>")
