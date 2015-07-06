(ns reagent-batteries-included-token-auth.router
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [reagent-batteries-included-token-auth.index :as index]
              [reagent-batteries-included-token-auth.github :as github]
              [reagent-batteries-included-token-auth.auth.login :as login]
              [reagent-batteries-included-token-auth.auth.register :as register]
              [reagent-batteries-included-token-auth.auth.lost-pass :as lost-pass]
              [reagent-batteries-included-token-auth.shared-state :refer [nav-state flash-message]]
              [reagent-batteries-included-token-auth.navigation :as nav])
    (:import goog.History))

(defn current-page []
  [:div
    [nav/desktop-nav]
    [nav/mobile-nav]
    [:div {:class "container"}
      [:div {:id (when (:mobile-menu-visiable @nav-state) "page-cover")}]
      (if (not-empty @flash-message)
        (let [{:keys [kind text]} @flash-message]
          [:div {:class (str "alert flash-message alert-" kind " flash-" kind)} [:p text]]))
      [(session/get :current-page)]]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'index/index-page))

(secretary/defroute "/github" []
  (session/put! :current-page #'github/authenticated-page))

(secretary/defroute "/login" []
  (session/put! :current-page #'login/login-page))

(secretary/defroute "/register" []
  (session/put! :current-page #'register/registration-page))

(secretary/defroute "/lost-pass" []
  (session/put! :current-page #'lost-pass/lost-pass-page))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
