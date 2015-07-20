(ns reagent-batteries-included-token-auth.auth.shared-functions-test
  (:require [cemerick.cljs.test :refer-macros [is are deftest testing use-fixtures done]]
            [reagent-batteries-included-token-auth.auth.shared-functions :as sf]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(deftest login-response-handler-sets-correct-values-in-local-storage-and-ratom
  (sf/login-response-handler {:token "abc" :refreshToken "123" :permissions "basic" :username "Jrock"})
  (is (= (:username      @ss/auth-creds-ls)    "Jrock"))
  (is (= (:username      @ss/auth-creds-ratom) "Jrock"))
  (is (= (:refresh-token @ss/auth-creds-ls)    "123"))
  (is (= (:refresh-token @ss/auth-creds-ratom) "123"))
  (is (= (:token         @ss/auth-creds-ls)    "abc"))
  (is (= (:token         @ss/auth-creds-ratom) "abc"))
  (is (= (:permissions   @ss/auth-creds-ls)    "basic"))
  (is (= (:permissions   @ss/auth-creds-ratom) "basic"))
  (is (= (:kind          @ss/flash-message)    "success"))
  (is (= (:text          @ss/flash-message)    "Welcome Jrock")))
