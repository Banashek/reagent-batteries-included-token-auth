(ns reagent-batteries-included-token-auth.auth.login
  (:require [reagent.core :as ratom]
            [ajax.core :refer [GET]]
            [goog.crypt.base64 :as b64]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(def password-state (ratom/atom {:password-visible false :ajax-info {}}))

(defn toggle-visibility []
  (swap! password-state assoc :password-visible (not (:password-visible @password-state))))

(defn response-handler [response]
  (swap! ss/auth-creds-ls assoc :token (:token response))
  (swap! ss/auth-creds-ls assoc :refresh-token (:refreshToken response))
  (swap! ss/auth-creds-ls assoc :permissions (:permissions response))
  (swap! ss/auth-creds-ls assoc :username (:username response)))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "That was not what we were going for: " status " " status-text)))


(defn auth-header []
  (str "Basic " (b64/encodeString "Jarrod1031:P455word1")))

(defn attempt-login []
  (GET "https://button-pusher-server.herokuapp.com/api/auth" {:headers       {"Authorization" (auth-header)}
                                                              :handler       response-handler
                                                              :error-handler error-handler
                                                              :response-format :json
                                                              :keywords? true
                                                              :prefix true}))

(defn login-page []
  [:div {:id "login-wrapper"}
   [:h1 "Log In "]
   [:input {:type "text" :id "login-username" :class "form-control" :placeholder "Username or Email"}]
   [:input {:type (if (:password-visible @password-state) "text" "password") :id "login-password" :class "form-control" :placeholder "Password"}]
   [:div {:id "pass-toggle" :class (str "glyphicon " (if (:password-visible @password-state) "glyphicon-eye-open" "glyphicon-eye-close")) :on-click #(toggle-visibility)}]
   [:input {:type "button" :id "login-submit-btn" :class "btn btn-info btn-outline" :value "Log In" :on-click #(attempt-login)}]
   [:div {:class "row login-links-row"}
    [:div {:class "col-xs-6"} [:a {:href "google.com"} "Register"]]
    [:div {:class "col-xs-6 forgot-pass-link"} [:a {:href "google.com"} "Forgot Password"]]]])
