(ns reagent-batteries-included-token-auth.shared-state
  (:require [alandipert.storage-atom :refer [local-storage remove-local-storage!]]
            [reagent.core :as ratom]))

(def auth-creds-ls (local-storage (atom {}) :auth-creds))
(def auth-creds-ratom (ratom/atom {}))

; (add-watch auth-creds-ls :state-maker
;            (fn [k _ _ v]
;              (.log js/console "Thing k: " k)
;              (.log js/console "Thing v: " v)))

(add-watch auth-creds-ls :anything
  (fn [key atom old-state new-state]
    (.log js/console "-- Atom Changed --")
    (.log js/console "key" key)
    (.log js/console "atom" atom)
    (.log js/console "old-state" old-state)
    (.log js/console "new-state" new-state)))

(.log js/console (:username @auth-creds-ls))
(remove-local-storage! :auth-creds)

; (swap! auth-creds assoc :user-token "777zzz")

; (defn get-storage-atom-local-value [key-name]
;  (.getItem js/localStorage (str "[\"k\",\"" key-name "\"]")))

; (.log js/console "<>=<>")
; (.log js/console (get-storage-atom-local-value "auth-creds"))
; (.log js/console "<>=<>")
