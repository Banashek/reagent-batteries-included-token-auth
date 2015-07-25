(ns reagent-batteries-included-token-auth.auth.change-pass
  (:require [reagent.core :as ratom]
            [secretary.core :as secretary :include-macros true]
            [ajax.core :refer [POST]]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.index :as index]))

(def new-pass (ratom/atom ""))

(defn authenticated-page []
  [:div {:id "change-pass-wrapper"}
   [:h1 "Change Password"]
   [:input {:type        "text"
            :id          "new-password"
            :class       "form-control"
            :placeholder "New Password"
            :on-change   #(reset! new-pass (-> % .-target .-value))}]
   [:input {:type     "button"
            :id       "change-pass-btn"
            :class    "btn btn-info btn-outline"
            :value    "Update Password"}]])
