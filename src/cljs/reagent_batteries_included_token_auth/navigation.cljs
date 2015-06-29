(ns reagent-batteries-included-token-auth.navigation
  (:require [reagent-batteries-included-token-auth.shared-state :refer [nav-state]]
            [reagent-batteries-included-token-auth.shared-functions :refer [active-route?]]
            [reagent-batteries-included-token-auth.auth.navigation :refer [auth-nav-mobile auth-nav-desktop]]
            [reagent-batteries-included-token-auth.shared-functions :refer [mobile-nav-click]]
            [secretary.core :as secretary :include-macros true]))

(defn mobile-nav []
  [:div {:class "container"}
    [:div {:class (str "mobile-menu " (when (:mobile-menu-visiable @nav-state) "visible"))}
      [:div {:class "mobile-menu-header"} [:h3 "Base App"]]
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
