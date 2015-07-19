(ns reagent-batteries-included-token-auth.github
  (:require [reagent.core :as ratom]
            [reagent.session :as session]
            [ajax.core :refer [GET]]
            [reagent-batteries-included-token-auth.auth.login :as login]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(def state (ratom/atom {:ajax-info {"login" "", "avatar_url" ""} :username ""}))

(defn response-handler [response]
  (swap! state assoc :ajax-info response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "That was not what we were going for: " status " " status-text)))

(defn get-github-info [username]
  (GET (str "https://api.github.com/users/" username) {:handler response-handler
                                                       :error-handler error-handler}))

(defn username-input-box []
  [:input {:type        "text"
           :class       "form-control"
           :value       (:username @state)
           :placeholder "Github Username"
           :on-change   #(swap! state assoc :username (-> % .-target .-value))}])

(defn get-user-button []
  [:input {:type     "button"
           :class    "btn btn-primary btn-outline"
           :value    "Get user info"
           :on-click #(get-github-info (:username @state))}])

(defn github-page []
  [:div
    [:h1 "Example Ajax Requests"]
    [:div {:id "ajax-form"}
      [username-input-box]
      [get-user-button]]
    [:p "Github Username: " (get-in @state [:ajax-info "login"])]
    [:img {:src (get-in @state [:ajax-info "avatar_url"])}]])

(defn authenticated-page []
  (if (nil? (:username @ss/auth-creds-ratom))
    (do (reset! ss/secured-route #'github-page) (login/login-page))
    (github-page)))
