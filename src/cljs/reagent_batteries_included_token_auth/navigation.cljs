(ns reagent-batteries-included-token-auth.navigation
  (:require [reagent.core  :refer [atom]]
            [reagent-batteries-included-token-auth.shared-state :refer [auth-creds-ratom]]
            [alandipert.storage-atom :refer [remove-local-storage!]]
            [secretary.core :as secretary :include-macros true]))

(def nav-state (atom {:mobile-menu-visiable false :active-route ""}))

(defn mobile-nav-click [route active-name]
  (swap! nav-state assoc :mobile-menu-visiable (not (:mobile-menu-visiable @nav-state)))
  (swap! nav-state assoc :active-route active-name)
  (secretary/dispatch! route))

(defn active-route? [route-name]
  (if (= route-name (:active-route @nav-state)) "active" ""))

(defn auth-nav-logged-in []
  [:ul {:class "nav navbar-nav navbar-right"}
   [:li {:class "dropdown"}
    [:a {:class "dropdown-toggle" :data-toggle "dropdown"} "Logout" [:b {:class "caret"}]]
    [:ul {:class "dropdown-menu"}
     [:li [:a {:href "#/change-pass"} [:i {:class "nav-icon icon glyphicon glyphicon-refresh"}] "Change Password"]]
     [:li {:class "divider"}]
     [:li {:id "log-in-or-out" :on-click #(remove-local-storage! :auth-creds)} [:a {:href "#/"} [:i {:class "nav-icon icon glyphicon glyphicon-off"}] "Logout"]]]]])

(defn auth-nav-not-logged-in []
  [:ul {:class "nav navbar-nav navbar-right"}
   [:li {:class "dropdown"}
    [:a {:class "dropdown-toggle" :data-toggle "dropdown"} "Login" [:b {:class "caret"}]]
    [:ul {:class "dropdown-menu"}
     [:li [:a {:href "#/register"} [:i {:class "nav-icon icon glyphicon glyphicon-user"}] "Register"]]
     [:li [:a {:href "#/lost-pass"} [:i {:class "nav-icon icon glyphicon glyphicon-search"}] "Lost Password?"]]
     [:li {:class "divider"}]
     [:li {:id "log-in-or-out"} [:a {:href "#/login"} [:i {:class "nav-icon icon glyphicon glyphicon-off"}] "Login"]]]]])

(defn auth-nav-desktop []
  (if (:username @auth-creds-ratom)
    [auth-nav-logged-in]
    [auth-nav-not-logged-in]))

(defn auth-nav-mobile-not-logged-in []
  [:div
   [:div {:class "mobile-menu-header"} [:h3 "Login"]]
   [:li {:class (str "mobile-menu-link " (active-route? "register"))}
    [:a {:href "#/register" :on-click #(mobile-nav-click "/register" "register")} "Register"]]
   [:li {:class (str "mobile-menu-link " (active-route? "lost-pass"))}
    [:a {:href "#/lost-pass" :on-click #(mobile-nav-click "/lost-pass" "lost-pass")} "Lost Password?"]]
   [:li {:class (str "mobile-menu-link " (active-route? "login"))}
    [:a {:href "#/login" :on-click #(mobile-nav-click "/login" "login")} "Login"]]])

(defn auth-nav-mobile-logged-in []
  [:div
    [:div {:class "mobile-menu-header"} [:h3 "Logout"]]
    [:li {:class "mobile-menu-link"} [:a {:href "#/" :on-click #(mobile-nav-click "/" "register")} "Change Password"]]
    [:li {:class "mobile-menu-link"} [:a {:href "#/" :on-click #(do (remove-local-storage! :auth-creds) (mobile-nav-click "/" "index"))} "Logout"]]])

(defn auth-nav-mobile []
  (if (:username @auth-creds-ratom)
    [auth-nav-mobile-logged-in]
    [auth-nav-mobile-not-logged-in]))

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
