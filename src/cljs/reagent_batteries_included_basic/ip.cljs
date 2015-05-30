(ns reagent-batteries-included-basic.ip
  (:require [reagent.core :refer [atom]]
            [ajax.core :refer [GET]]))

(def state (atom {:ajax-info {"login" "", "avatar_url" ""} :username ""}))

(defn response-handler [response]
  (swap! state assoc :ajax-info response))

(defn error-handler [{:keys [status status-text]}]
  (.log js/console (str "That was not what we were going for: " status " " status-text)))

(defn get-github-info [username]
  (GET (str "https://api.github.com/users/" username) {:handler response-handler
                                                       :error-handler error-handler}))

(defn username-input-box []
  [:input {:type        "text"
           :value       (:username @state)
           :placeholder "Filter contents"
           :on-change   #(swap! state assoc :username (-> % .-target .-value))}])

(defn get-user-button []
  [:input {:type     "button"
           :class    "button"
           :value    "Get user info"
           :on-click #(get-github-info (:username @state))}])

(defn ip-page []
  [:div
   [:h1 "Example Ajax Request"]
   [username-input-box]
   [get-user-button]
   [:p "Github Username: " (get-in @state [:ajax-info "login"])]
   [:img {:src (get-in @state [:ajax-info "avatar_url"])}]])