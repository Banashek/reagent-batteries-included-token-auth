(ns reagent-batteries-included-token-auth.auth.register
  (:require [reagent.core :as ratom]
            [ajax.core :refer [GET POST]]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(def user-info (ratom/atom {:username "" :password "" :email ""}))

(defn error-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn response-handler [response]
  (.log js/console response))

(defn register-new-user []
  (POST "https://button-pusher-server.herokuapp.com/api/user" {:params {:password (:password @user-info)
                                                                        :username (:username @user-info)
                                                                        :email    (:email @user-info)}
                                                               :handler         response-handler
                                                               :error-handler   error-handler
                                                               :response-format :json
                                                               :keywords?       true}))
(defn registration-page []
  [:div {:id "register-wrapper"}
   [:h1 "Register"]
   [:input {:type        "text"
            :id          "registration-username"
            :class       "form-control"
            :placeholder "Username"
            :on-change   #(swap! user-info assoc :username (-> % .-target .-value))}]
   [:input {:type        "text"
            :id          "registration-password"
            :class       "form-control"
            :placeholder "Password"
            :on-change   #(swap! user-info assoc :password (-> % .-target .-value))}]
   [:input {:type        "text"
            :id          "registration-email"
            :class       "form-control"
            :placeholder "Email"
            :on-change   #(swap! user-info assoc :email (-> % .-target .-value))}]
   [:input {:type        "button"
            :id          "register-btn"
            :class       "btn btn-info btn-outline"
            :value       "Register"
            :on-click    #(register-new-user)}]])
