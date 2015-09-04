(ns slurp-test.core
  (:require [clojure.java.io :as jio]
            [criterium.core :as c]))

(defn- normalize-slurp-opts
  [opts]
  (if (string? (first opts))
    (do
      (println "WARNING: (slurp f enc) is deprecated, use (slurp f :encoding enc).")
      [:encoding (first opts)])
    opts))

(defn new-slurp
  "Opens a reader on f and reads all its contents, returning a string.
  See clojure.java.io/reader for a complete list of supported arguments."
  {:added "1.0"}
  ([f & opts]
     (let [opts (normalize-slurp-opts opts)
           sw (java.io.StringWriter.)]
       (with-open [^java.io.Reader r (apply jio/reader f opts)]
         (jio/copy r sw)
         (.toString sw)))))

(defn old-slurp
  "Opens a reader on f and reads all its contents, returning a string.
  See clojure.java.io/reader for a complete list of supported arguments."
  {:added "1.0"}
  ([f & opts]
     (let [opts (normalize-slurp-opts opts)
           sb (StringBuilder.)]
       (with-open [^java.io.Reader r (apply jio/reader f opts)]
         (loop [c (.read r)]
           (if (neg? c)
             (str sb)
             (do
               (.append sb (char c))
               (recur (.read r)))))))))

(char (+ (long \a) (rand-int 26)))

(defn bench-slurp [nc filename]
  (println "writing test file")
  (println "filename:" filename)
  (println "file size:" nc "bytes")
  (spit filename (apply str (repeatedly nc
                                #(char (+ (long \a) (rand-int 26))))))
  (println "benching old slurp")
  (criterium.core/bench
    (old-slurp filename))
  (println "benching new slurp")
  (criterium.core/bench
    (new-slurp filename)))

(defn main [size & [filename]]
  (bench-slurp (Long/parseLong size) (or filename "foo.txt")))
