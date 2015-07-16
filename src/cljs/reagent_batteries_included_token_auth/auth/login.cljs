(ns reagent-batteries-included-token-auth.auth.login
  (:require [reagent.core :as ratom]
            [reagent-batteries-included-token-auth.shared-functions :as sf]))

(def password-visible (ratom/atom false))
(def credentials (ratom/atom {:username "" :password ""}))

(defn toggle-visibility []
  (reset! password-visible (not @password-visible)))

(defn username-input-box []
  [:input {:type        "text"
           :id          "login-username"
           :class       "form-control"
           :placeholder "Username or Email"
           :on-change   #(swap! credentials assoc :username (-> % .-target .-value))}])

(defn password-input-box []
  [:input {:type        (if @password-visible "text" "password")
           :id          "login-password"
           :class       "form-control"
           :placeholder "Password"
           :on-change   #(swap! credentials assoc :password (-> % .-target .-value))}])

(defn login-page []
  [:div {:id "login-wrapper"}
    [:h1 "Log In "]
    [username-input-box]
    [password-input-box]
    [:div {:id "pass-toggle" :class (str "glyphicon " (if @password-visible "glyphicon-eye-open" "glyphicon-eye-close")) :on-click #(toggle-visibility)}]
    [:input {:type "button" :id "login-submit-btn" :class "btn btn-info btn-outline" :value "Log In" :on-click #(sf/attempt-login (:username @credentials) (:password @credentials))}]
    [:div {:class "row login-links-row"}
      [:div {:class "col-xs-6"} [:a {:href "google.com"} "Register"]]
      [:div {:class "col-xs-6 forgot-pass-link"} [:a {:href "google.com"} "Forgot Password"]]]])
