(ns reagent-batteries-included-token-auth.auth.navigation
  (:require [ajax.core :refer [DELETE]]
            [alandipert.storage-atom :refer [remove-local-storage!]]
            [reagent-batteries-included-token-auth.config :refer [endpoints]]
            [reagent-batteries-included-token-auth.shared-state :as ss]
            [reagent-batteries-included-token-auth.shared-functions :refer [mobile-nav-click active-route?]]))

(defn logout-response-handler [response]
  (remove-local-storage! :auth-creds))

(defn logout-error-handler [{:keys [status status-text]}]
  (remove-local-storage! :auth-creds))

(defn logout []
  (DELETE (str (:refresh-token endpoints) (:refresh-token @ss/auth-creds-ratom)) {:handler         logout-response-handler
                                                                                  :error-handler   logout-error-handler}))

(defn auth-nav-logged-in []
  [:ul {:class "nav navbar-nav navbar-right"}
    [:li {:class "dropdown"}
      [:a {:class "dropdown-toggle" :data-toggle "dropdown"} "Logout" [:b {:class "caret"}]]
      [:ul {:class "dropdown-menu"}
        [:li [:a {:href "#/change-pass"} [:i {:class "nav-icon icon glyphicon glyphicon-refresh"}] "Change Password"]]
        [:li {:class "divider"}]
        [:li {:id "log-in-or-out" :on-click #(logout)} [:a {:href "#/"} [:i {:class "nav-icon icon glyphicon glyphicon-off"}] "Logout"]]]]])

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
  (if (:username @ss/auth-creds-ratom)
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
    [:li {:class "mobile-menu-link"} [:a {:href "#/" :on-click #(mobile-nav-click "/change-pass" "change-pass")} "Change Password"]]
    [:li {:class "mobile-menu-link"} [:a {:href "#/" :on-click #(do (logout) (mobile-nav-click "/" "index"))} "Logout"]]])

(defn auth-nav-mobile []
  (if (:username @ss/auth-creds-ratom)
    [auth-nav-mobile-logged-in]
    [auth-nav-mobile-not-logged-in]))

