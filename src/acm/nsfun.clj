(ns acm.nsfun
  (:require [clojure.string :as cstr]))

(in-ns 'clojure.string)
(defn ends-with? [^String s ^String ending-s]
  (.endsWith s ending-s))
(in-ns 'acm.nsfun)

(defn ends-with-world? [x]
  (cstr/ends-with? x "World"))

