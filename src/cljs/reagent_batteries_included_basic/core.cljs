(ns reagent-batteries-included-basic.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [reagent-batteries-included-basic.input :as input])
    (:import goog.History))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to reagent-batteries-included-basic"]
   [:div [:a {:href "#/input"} "go to input page"]]])

;; Relocate
;; -------------------------

(def mobile-menu-visiable (atom false))
(def active-route (atom ""))

(defn transition-to [route]
  (reset! mobile-menu-visiable (not @mobile-menu-visiable))
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (when (= route-name @active-route)
    {:class "active"}))

(defn mobile-nav []
  [:div {:class "container"}
   [:div {:class (str "mobile-menu " (when @mobile-menu-visiable "visible"))}
     [:div {:class "mobile-menu-header"}
      [:h3 "Base App"]]
     [:ul {:class "nav affix-top"}
      [:li {:class "mobile-menu-text"} [:a {:href "#/"      :on-click #(transition-to "/")} "Index"]]
      [:li {:class "mobile-menu-text"} [:a {:href "#/input" :on-click #(transition-to "/input")} "Input"]]]]])

(defn desktop-nav []
  [:div {:class "navbar navbar-inverse"}
   [:div {:class "container"}
    [:div {:class "navbar-header"}
     [:button {:id "mobile-menu-button"
               :class "navbar-toggle pull-left"
               :on-click #(reset! mobile-menu-visiable (not @mobile-menu-visiable))}
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]]]
    [:div {:class "navbar-collapse collapse"}
     [:ul {:class "nav navbar-nav nav-desktop"}
      [:li (active-route? "index") [:a {:href "#/" :on-click #(reset! active-route "index")} "Index"]]
      [:li (active-route? "input") [:a {:href "#/input" :on-click #(reset! active-route "input")} "Input"]]]]]])

(defn current-page []
  [:div
   [desktop-nav]
   [mobile-nav]
   [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'home-page))

(secretary/defroute "/input" []
  (session/put! :current-page #'input/input-page))

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
