(ns reagent-batteries-included-basic.prod
  (:require [reagent-batteries-included-basic.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
