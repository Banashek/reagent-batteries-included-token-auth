(ns reagent-batteries-included-token-auth.auth.reset-pass
  (:require [reagent.core :as ratom]
            [ajax.core :refer [POST]]
            [secretary.core :as secretary :include-macros true]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.index :as index]))

(def new-password (ratom/atom ""))

(defn reset-pass-request-handler [response]
  (reset! ss/flash-message {:kind "success" :text (:message response)})
  (swap! ss/nav-state assoc :active-route "/index")
  (secretary/dispatch! "/index")
  (set! (.-location js/window) "#/"))

(defn reset-pass-error-request-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn reset-pass []
  (POST "https://button-pusher-server.herokuapp.com/api/password/reset-confirm" {:params {:newPassword @new-password
                                                                                          :resetKey    (:reset-pass-id @ss/route-params)}
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
