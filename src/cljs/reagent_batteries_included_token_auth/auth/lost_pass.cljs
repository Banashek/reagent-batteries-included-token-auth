(ns reagent-batteries-included-token-auth.auth.lost-pass
  (:require [reagent.core :as ratom]
            [ajax.core :refer [POST]]
            [reagent-batteries-included-token-auth.shared-state :as ss]))

(def user-email (ratom/atom ""))

(def emails {:html  "<h3>Password Reset Request</h3>\n<p>A request was made to reset the password for the account matching this email address.\nUse the link below to complete the request.</p>"
             :plain "A request was made to reset the password for the account matching this email address. Use the link below to complete the request."})

(defn request-reset-handler [response]
  (reset! ss/flash-message {:kind "success" :text (:message response)}))

(defn request-reset-error-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn request-reset []
  (POST "https://button-pusher-server.herokuapp.com/api/password/reset-request" {:params {:userEmail      @user-email
                                                                                          :fromEmail      "jarrod.c.taylor@winning.com"
                                                                                          :subject        "Password reset request"
                                                                                          :emailBodyPlain (:plain emails)
                                                                                          :emailBodyHtml  (:html emails)
                                                                                          :responseBaseLink "localhost.3349"}
                                                                                 :handler         request-reset-handler
                                                                                 :error-handler   request-reset-error-handler
                                                                                 :response-format :json
                                                                                 :keywords?       true
                                                                                 :prefix          true}))

(defn public-page []
  [:div {:id "request-reset-wrapper"}
   [:h1 "Request Password Reset"]
   [:input {:type        "text"
            :id          "email"
            :class       "form-control"
            :placeholder "Email"
            :on-change   #(reset! user-email (-> % .-target .-value))}]
   [:input {:type     "button"
            :id       "request-reset-btn"
            :class    "btn btn-info btn-outline"
            :value    "Request reset"
            :on-click #(request-reset)}]])
