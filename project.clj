(defproject org.clojars.punit-naik/core-async-quest "0.3.0"
  :description "A visual, game-like Clojure core.async learning lab"
  :url "https://punit-naik.github.io/core-async-quest/"
  :license {:name "EPL-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.12.2"]
                 [org.clojure/clojurescript "1.12.145"]
                 [org.clojure/core.async "1.7.701"]
                 [reagent "2.0.1"]
                 [re-frame "1.4.7"]]
  :plugins [[lein-cljsbuild "1.1.8"]]
  :profiles {:dev {:dependencies [[mvxcvi/cljstyle "0.17.642"]]}}
  :clean-targets ^{:protect false} ["node_modules" "package.json" "package-lock.json" "resources/public/js/compiled"]
  :cljsbuild
  {:builds
   [{:id "dev"
     :source-paths ["src"]
     :compiler {:main core-async-quest.core
                :output-to "resources/public/js/compiled/app.js"
                :output-dir "resources/public/js/compiled/out"
                :asset-path "js/compiled/out"
                :optimizations :advanced
                :pretty-print true
                :npm-deps {"react" "18.3.1"
                           "react-dom" "18.3.1"
                           "@xyflow/react" "12.11.5"
                           "motion" "13.1.1"
                           "canvas-confetti" "1.9.3"}
                :install-deps true}}
    {:id "prod"
     :source-paths ["src"]
     :compiler {:main core-async-quest.core
                :output-to "resources/public/js/compiled/app.js"
                :output-dir "resources/public/js/compiled/out-prod"
                :asset-path "js/compiled/out-prod"
                :optimizations :advanced
                :pretty-print false
                :npm-deps {"react" "18.3.1"
                           "react-dom" "18.3.1"
                           "@xyflow/react" "12.11.5"
                           "motion" "13.1.1"
                           "canvas-confetti" "1.9.3"}
                :install-deps true}}
    {:id "test"
     :source-paths ["src" "test/cljs"]
     :compiler {:main core-async-quest.test-runner
                :output-to "resources/public/js/compiled/test.js"
                :output-dir "resources/public/js/compiled/out-test"
                :asset-path "js/compiled/out-test"
                :optimizations :none
                :npm-deps {"react" "18.3.1"
                           "react-dom" "18.3.1"}
                :install-deps true}}]})
