(ns reagent-batteries-included-token-auth.auth.login-test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [reagent-batteries-included-token-auth.auth.login :as rc]
            [reagent-batteries-included-token-auth.utils :as utils]))

(deftest password-visibility-can-toggle-between-password-and-text
  (swap! rc/credentials assoc :password "test")
  (utils/with-mounted-component (rc/login-page)
    (fn [c div]
      (is (utils/not-found? #"type=\"text\" id=\"login-password" div))
      (is (utils/found? #"type=\"password\" id=\"login-password" div))))
  (reset! rc/password-visible true)
  (utils/with-mounted-component (rc/login-page)
    (fn [c div]
      (is (utils/not-found? #"type=\"password\" id=\"login-password" div))
      (is (utils/found? #"type=\"text\" id=\"login-password" div)))))
