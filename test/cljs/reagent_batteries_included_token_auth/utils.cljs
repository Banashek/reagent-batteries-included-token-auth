(ns reagent-batteries-included-token-auth.utils
  (:require [reagent.core :as reagent]))

(def isClient (not (nil? (try (.-document js/window)
                              (catch js/Object e nil)))))

(def rflush reagent/flush)

(defn add-test-div [name]
  (let [doc     js/document
        body    (.-body js/document)
        div     (.createElement doc "div")]
    (.appendChild body div)
    div))

(defn with-mounted-component [comp f]
  (when isClient
    (let [div (add-test-div "_testreagent")]
      (let [comp (reagent/render-component comp div #(f comp div))]
        (reagent/unmount-component-at-node div)
        (reagent/flush)
        (.removeChild (.-body js/document) div)))))

(defn found? [re div]
  (let [res (.-innerHTML div)]
    (if (re-find re res) true false)))

(defn not-found? [re div]
  (complement (found? re div)))
