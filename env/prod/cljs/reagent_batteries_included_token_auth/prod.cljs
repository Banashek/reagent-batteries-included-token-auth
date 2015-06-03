(ns reagent-batteries-included-token-auth.prod
  (:require [reagent-batteries-included-token-auth.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
