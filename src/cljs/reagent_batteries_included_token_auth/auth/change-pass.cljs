(ns reagent-batteries-included-token-auth.auth.change-pass
  (:require [reagent.core :as ratom]
            [secretary.core :as secretary :include-macros true]
            [reagent.session :as session]
            [ajax.core :refer [PATCH]]
            [promesa.core :as p]
            [reagent-batteries-included-token-auth.index :as index]
            [reagent-batteries-included-token-auth.config :refer [endpoints]]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.shared-functions :as sf]
            [reagent-batteries-included-token-auth.auth.login :as login]))

(def new-pass (ratom/atom ""))

(defn change-pass-handler [response]
  (reset! ss/flash-message {:kind "success" :text "Password successfully changed"})
  (swap! ss/nav-state assoc :active-route "/index")
  (session/put! :current-page #'index/index-page)
  (set! (.-location js/window) "#/"))

(defn change-pass-error-handler [{:keys [status status-text]}]
  (reset! ss/flash-message {:kind "error" :text status-text}))

(defn change-pass-request []
  (let [endpoint    (str (:user endpoints) "/" (:id @ss/auth-creds-ratom))
        auth-header {"Authorization" (str "Token " (:token @ss/auth-creds-ratom))}]
    (PATCH endpoint {:headers         auth-header
                     :params          {:password @new-pass}
                     :handler         change-pass-handler
                     :error-handler   change-pass-error-handler
                     :response-format :json
                     :keywords?       true
                     :prefix          true})))

(defn change-pass []
  (if (sf/token-fresh?)
    (change-pass-request)
    (sf/refresh-token change-pass-request)))

(defn change-pass-page []
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
            :value    "Update Password"
            :on-click #(change-pass)}]])

(defn authenticated-page []
  (if (nil? (:username @ss/auth-creds-ratom))
    (do
      (reset! ss/secured-route #'change-pass-page)
      (set! (.-location js/window) "#/login-page")
      (swap! ss/nav-state assoc :active-route "")
      (login/login-page))
    (change-pass-page)))
