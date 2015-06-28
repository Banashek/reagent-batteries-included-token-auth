(ns ^:figwheel-no-load reagent-batteries-included-token-auth.dev
  (:require [reagent-batteries-included-token-auth.router :as router]
            [figwheel.client :as figwheel :include-macros true]))

(enable-console-print!)

(figwheel/watch-and-reload
  :websocket-url "ws://localhost:3449/figwheel-ws"
  :jsload-callback router/mount-root)

(router/init!)
