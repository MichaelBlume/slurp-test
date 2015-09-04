(defproject slurp-test "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :jvm-opts ^replace []
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0-alpha4"]
                 [criterium "0.4.3"]]
  :aliases {"bench" ["run" "-m" "slurp-test.core/main"]})
