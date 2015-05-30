(ns reagent-batteries-included-basic.navigation
  (:require [reagent.core  :refer [atom]]
            [secretary.core :as secretary :include-macros true]))

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
     [:li {:class (str "mobile-menu-text " (active-route? "github"))}
      [:a {:href "#/github"  :on-click #(mobile-nav-click "/github" "github")}  "GitHub"]]]]])

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
      [:li {:class (active-route? "github")} [:a {:href "#/github" :on-click #(swap! nav-state assoc :active-route "github")} "GitHub"]]]]]])

