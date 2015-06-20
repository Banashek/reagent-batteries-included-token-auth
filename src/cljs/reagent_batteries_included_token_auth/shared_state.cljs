(ns reagent-batteries-included-token-auth.shared-state
  (:require [alandipert.storage-atom :refer [local-storage]]))

(def auth-creds (local-storage (atom {}) :auth-creds))

; (add-watch auth-creds
;            :new
;            (fn [_ _ _ v]
;              (.log js/console "new preference" v)))

; (swap! auth-creds assoc :user-token "777zzz")

; (defn get-storage-atom-local-value [key-name]
;  (.getItem js/localStorage (str "[\"k\",\"" key-name "\"]")))

; (.log js/console "<>=<>")
; (.log js/console (get-storage-atom-local-value "auth-creds"))
; (.log js/console "<>=<>")
