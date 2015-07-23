(ns reagent-batteries-included-token-auth.auth.reset-pass
  (:require [reagent-batteries-included-token-auth.shared-state :as ss]))

(defn public-page []
  [:div {:id "reset-wrapper"}
   [:h1 "Reset Password"]
   [:input {:type        "text"
            :id          "new-password"
            :class       "form-control"
            :placeholder "New Password"}]
   [:input {:type   "button"
            :id     "pass-reset-btn"
            :class  "btn btn-info btn-outline"
            :value  "Reset Password"}]])
