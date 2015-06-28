(ns reagent-batteries-included-token-auth.prod
  (:require [reagent-batteries-included-token-auth.router :as router]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(router/init!)
