(ns reagent-batteries-included-basic.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [reagent-batteries-included-basic.index :as index]
              [reagent-batteries-included-basic.ip :as ip])
    (:import goog.History))

(def nav-state (atom {:mobile-menu-visiable false :active-route ""}))

(defn mobile-nav-click [route active-name]
  (swap! nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @nav-state)))
  (swap! nav-state assoc :active-route active-name)
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (if (= route-name (:active-route @nav-state)) "active" ""))

(defn mobile-nav []
  [:div {:class "container"}
   [:div {:class (str "mobile-menu " (when (:mobile-menu-visiable @nav-state) "visible"))}
     [:div {:class "mobile-menu-header"}
      [:h3 "Base App"]]
     [:ul {:class "nav affix-top"}
      [:li {:class (str "mobile-menu-text " (active-route? "index"))}
       [:a {:href "#/" :on-click #(mobile-nav-click "/" "index")} "Index"]]
      [:li {:class (str "mobile-menu-text " (active-route? "ip"))}
       [:a {:href "#/ip"  :on-click #(mobile-nav-click "/ip" "ip")}  "IP"]]]]])

(defn desktop-nav []
  [:div {:class "navbar navbar-inverse"}
   [:div {:class "container"}
    [:div {:class "navbar-header"}
     [:button {:id "mobile-menu-button"
               :class "navbar-toggle pull-left"
               :on-click #(swap! nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @nav-state)))}
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]]]
    [:div {:class "navbar-collapse collapse"}
     [:ul {:class "nav navbar-nav nav-desktop"}
      [:li {:class (active-route? "index")} [:a {:href "#/" :on-click #(swap! nav-state assoc :active-route "index")} "Index"]]
      [:li {:class (active-route? "ip")} [:a {:href "#/ip" :on-click #(swap! nav-state assoc :active-route "ip")} "IP"]]]]]])

(defn current-page []
  [:div
   [desktop-nav]
   [mobile-nav]
   [:div {:id (when (:mobile-menu-visiable @nav-state) "page-cover")}]
   [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'index/index-page))

(secretary/defroute "/ip" []
  (session/put! :current-page #'ip/ip-page))

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
