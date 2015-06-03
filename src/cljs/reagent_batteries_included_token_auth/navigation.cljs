(ns reagent-batteries-included-token-auth.navigation
  (:require [reagent.core  :refer [atom]]
            [secretary.core :as secretary :include-macros true]))

(def nav-state (atom {:mobile-menu-visiable false :active-route ""}))

;TODO move this to something global?
(def global (atom {:example-token ""}))

(defn mobile-nav-click [route active-name]
  (swap! nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @nav-state)))
  (swap! nav-state assoc :active-route active-name)
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (if (= route-name (:active-route @nav-state)) "active" ""))

(defn auth-nav-desktop []
  [:ul {:class "nav navbar-nav navbar-right"}
   [:li {:class "dropdown"}
    [:a {:class "dropdown-toggle" :data-toggle "dropdown"} "Login" [:b {:class "caret"}]]
    [:ul {:class "dropdown-menu"}
     [:li [:a {:href "#/register"} [:i {:class "nav-icon icon glyphicon glyphicon-user"}] "Register"]]
     [:li [:a {:href "#/lost-pass"} [:i {:class "nav-icon icon glyphicon glyphicon-search"}] "Lost Password?"]]
     [:li {:class "divider"}]
     [:li {:id "log-in-or-out"} [:a {:href "#/login"} [:i {:class "nav-icon icon glyphicon glyphicon-off"}] "Login"]]]]])

(defn auth-nav-mobile []
  [:div
   [:div {:class "mobile-menu-header"}
    (if (not-empty (:example-token @global))
      [:h3 "Logout"]
      [:h3 "Login"])]
   [:li {:class (str "mobile-menu-link " (active-route? "register"))}
    [:a {:href "#/register" :on-click #(mobile-nav-click "/register" "register")} "Register"]]
   [:li {:class (str "mobile-menu-link " (active-route? "lost-pass"))}
    [:a {:href "#/lost-pass" :on-click #(mobile-nav-click "/lost-pass" "lost-pass")} "Lost Password?"]]
   [:li {:class (str "mobile-menu-link " (active-route? "login"))}
    [:a {:href "#/login" :on-click #(mobile-nav-click "/login" "login")} "Login"]]])

; Mobile menu when we do have a token
;[:li {:class "mobile-menu-link"} [:a {:href "#/" :on-click #(mobile-nav-click "/" "register")} "Change Password"]]
;[:li {:class "mobile-menu-link"} [:a {:href "#/" :on-click #(mobile-nav-click "/" "register")} "Logout"]]

(defn mobile-nav []
  [:div {:class "container"}
   [:div {:class (str "mobile-menu " (when (:mobile-menu-visiable @nav-state) "visible"))}
    [:div {:class "mobile-menu-header"}
     [:h3 "Base App"]]
    [:ul {:class "nav affix-top"}
     [:li {:class (str "mobile-menu-link " (active-route? "index"))}
      [:a {:href "#/" :on-click #(mobile-nav-click "/" "index")} "Index"]]
     [:li {:class (str "mobile-menu-link " (active-route? "github"))}
      [:a {:href "#/github" :on-click #(mobile-nav-click "/github" "github")} "GitHub"]]
     [auth-nav-mobile]]]])

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
      [:li {:class (active-route? "github")} [:a {:href "#/github" :on-click #(swap! nav-state assoc :active-route "github")} "GitHub"]]]
      [auth-nav-desktop]]]])
