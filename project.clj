(defproject org.soulspace.clj/clj.swing "0.8.3"
  :description "clj.swing is a library for building Java Swing user interfaces in Clojure"
  :url "https://github.com/soulspace-org/clj.swing"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}

  ; use deps.edn dependencies
  :plugins [[lein-tools-deps "0.4.5"]]
  :middleware [lein-tools-deps.plugin/resolve-dependencies-with-deps-edn]
  :lein-tools-deps/config {:config-files [:install :user :project]}

  :test-paths ["test"]

  :profiles {:dev {:dependencies [[djblue/portal "0.37.1"]]
                   :global-vars {*warn-on-reflection* true}}}

  :scm {:name "git" :url "https://github.com/soulspace-org/clj.swing"}
  :deploy-repositories [["clojars" {:sign-releases false :url "https://clojars.org/repo"}]])
