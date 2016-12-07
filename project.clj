(defproject githubio "0.1.0-SNAPSHOT"
  :description "Introduction to clojure.spec"
  :url "https://github.com/wvdlaan/githubio"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.5.3"
  
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [org.clojure/clojurescript "1.9.293"]
                 [org.clojure/test.check "0.9.0"]
                 [devcards "0.2.2"]
                 [sablono "0.7.6"]
                 
                 ;; need to specify this for sablono
                 ;; when not using devcards
                 [cljsjs/react "15.4.0-0"]
                 [cljsjs/react-dom "15.4.0-0"]
                 [cljsjs/react-dom-server "15.4.0-0"]
                 ]

  :plugins [[lein-figwheel "0.5.8"]
            [lein-cljsbuild "1.1.5" :exclusions [org.clojure/clojure]]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "target"]
  
  :source-paths ["src"]

  :cljsbuild {
              :builds [{:id "cards"
                        :source-paths ["src"]
                        :figwheel { :devcards true } ;; <- note this
                        :compiler { :main       "cards.index"
                                    :asset-path "js/compiled/devcards_out"
                                    :output-to  "resources/public/js/compiled/devcards.js"
                                    :output-dir "resources/public/js/compiled/devcards_out"
                                    :source-map-timestamp true }}
                       {:id "hostedcards"
                        :source-paths ["src"]
                        :compiler {:devcards   true
                                   :output-to  "resources/public/js/compiled/hostedcards.js"
                                   :optimizations :advanced}}]}

  :figwheel { :css-dirs ["resources/public/css"] })
