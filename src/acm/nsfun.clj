(ns acm.nsfun
  (:require [clojure.string :as string]))

(in-ns 'clojure.string)
(defn another-ends-with? [^String s ^String ending-s]
  (.endsWith s ending-s))
(in-ns 'acm.nsfun)

(defn ends-with-world? [x]
  (string/another-ends-with? x "World"))

