(ns reagent-batteries-included-token-auth.auth.login
  (:require [reagent.core :as ratom]
            [reagent.session :as session]
            [ajax.core :refer [GET]]
            [goog.crypt.base64 :as b64]
            [cljs-time.core :as t]
            [cljs-time.coerce :as coerce-t]
            [reagent-batteries-included-token-auth.index :as index]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(def password-visible (ratom/atom false))
(def credentials (ratom/atom {:username "" :password ""}))

(defn toggle-visibility []
  (reset! password-visible (not @password-visible)))

(defn response-handler [response]
  (swap! ss/auth-creds-ls assoc :token (:token response))
  (swap! ss/auth-creds-ls assoc :refresh-token (:refreshToken response))
  (swap! ss/auth-creds-ls assoc :permissions (:permissions response))
  (swap! ss/auth-creds-ls assoc :username (:username response))
  (swap! ss/auth-creds-ls assoc :time-stamp (coerce-t/to-long (t/date-time (t/now))))
  (reset! credentials {:username "" :password ""})
  (reset! ss/flash-message {:kind "success" :text (str "Welcome " (:username response))})
  (if (= @ss/secured-route "")
    (session/put! :current-page #'index/index-page)
    (do (session/put! :current-page @ss/secured-route) (reset! ss/secured-route ""))))

(defn error-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn auth-header []
  (str "Basic " (b64/encodeString (str (:username @credentials) ":" (:password @credentials)))))

(defn attempt-login []
  (GET "https://button-pusher-server.herokuapp.com/api/auth" {:headers         {"Authorization" (auth-header)}
                                                              :handler         response-handler
                                                              :error-handler   error-handler
                                                              :response-format :json
                                                              :keywords?       true
                                                              :prefix          true}))

(defn username-input-box []
  [:input {:type        "text"
           :id          "login-username"
           :class       "form-control"
           :value       (:username @credentials)
           :placeholder "Username or Email"
           :on-change   #(swap! credentials assoc :username (-> % .-target .-value))}])

(defn password-input-box []
  [:input {:type        (if @password-visible "text" "password")
           :id          "login-password"
           :class       "form-control"
           :value       (:password @credentials)
           :placeholder "Password"
           :on-change   #(swap! credentials assoc :password (-> % .-target .-value))}])

(defn login-page []
  [:div {:id "login-wrapper"}
    [:h1 "Log In "]
    [username-input-box]
    [password-input-box]
    [:div {:id "pass-toggle" :class (str "glyphicon " (if @password-visible "glyphicon-eye-open" "glyphicon-eye-close")) :on-click #(toggle-visibility)}]
    [:input {:type "button" :id "login-submit-btn" :class "btn btn-info btn-outline" :value "Log In" :on-click #(attempt-login)}]
    [:div {:class "row login-links-row"}
      [:div {:class "col-xs-6"} [:a {:href "google.com"} "Register"]]
      [:div {:class "col-xs-6 forgot-pass-link"} [:a {:href "google.com"} "Forgot Password"]]]])
