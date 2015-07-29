(ns reagent-batteries-included-token-auth.auth.reset-pass
  (:require [reagent.core :as ratom]
            [ajax.core :refer [POST]]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [reagent-batteries-included-token-auth.config :refer [endpoints]]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.index :as index]))

(def new-password (ratom/atom ""))

(defn reset-pass-request-handler [response]
  (reset! ss/flash-message {:kind "success" :text (:message response)})
  (swap! ss/nav-state assoc :active-route "/index")
  (session/put! :current-page #'index/index-page)
  (set! (.-location js/window) "#/"))

(defn reset-pass-error-request-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn reset-pass []
  (POST (:reset-confirm endpoints) {:params          {:newPassword @new-password
                                    :resetKey        (:reset-pass-id @ss/route-params)}
                                    :handler         reset-pass-request-handler
                                    :error-handler   reset-pass-error-request-handler
                                    :response-format :json
                                    :keywords?       true
                                    :prefix          true}))

(defn public-page []
  [:div {:id "reset-wrapper"}
   [:h1 "Reset Password"]
   [:input {:type        "text"
            :id          "new-password"
            :class       "form-control"
            :placeholder "New Password"
            :on-change   #(reset! new-password (-> % .-target .-value))}]
   [:input {:type   "button"
            :id     "pass-reset-btn"
            :class  "btn btn-info btn-outline"
            :value  "Reset Password"
            :on-click #(reset-pass)}]])
