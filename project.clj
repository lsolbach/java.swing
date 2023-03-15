(defproject org.soulspace.clj/clj.swing "0.7.3"
  :description "clj.swing is a library for building Java Swing user interfaces in Clojure"
  :url "https://github.com/lsolbach/CljBase"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  ; use deps.edn dependencies
  :plugins [[lein-tools-deps "0.4.5"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]}

;  :dependencies [[org.clojure/clojure "1.10.1"]
;                 [swing-utils/swing-utils "0.2.0" :exclusions [org.clojure/clojure]]
;                 [org.soulspace.clj/clj.java "0.8.0"]
;                 [com.miglayout/miglayout "3.7.4"]]

  :test-paths ["test"]
  :deploy-repositories [["clojars" {:sign-releases false :url "https://clojars.org/repo"}]])
