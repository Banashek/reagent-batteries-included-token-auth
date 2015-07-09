(ns reagent-batteries-included-token-auth.login-test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [reagent-batteries-included-token-auth.auth.login :as rc]
            [reagent-batteries-included-token-auth.utils :as utils]))

(deftest test-stuff
  (utils/with-mounted-component (rc/login-page)
    (fn [c div]
      (println div)
      (is (not (utils/found? #"The toggle is true" div))))))
  ; (reset! rc/toggle-status (not @rc/toggle-status))
  ; (utils/with-mounted-component (rc/button-page)
  ;   (fn [c div]
  ;     (is (utils/found? #"The toggle is true" div)))))
