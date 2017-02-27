(ns acm.foldit
  "Examples on how to use `reducers` and their benefits"
  (:require [clojure.core.reducers :as r]))

(comment r/fold )

;; let's take a look at some Clojure math
(comment
  (+)
  (inc 1) ;; requires an argument
  (*))

;; Whoa!
;; So addition/+ carries its own identity function - it has its own seed
;; As does multiply/*
;;
;; but inc requires an argument.
;; In order to use it in a reduction (like above), we'd need a function to seed it

;; So that combining function is a MONOID!!!!
;; and participation in the parallelization is from a protocol - ie: How do YOU fold yourself?
;;
;; This is defined for all of the built-in data structures

(comment
  ;(require '[clojure.core.reducers :as r])
  (def v (vec (range 10000000)))
  (time (reduce + (map inc (filter even? v))))
  (time (reduce + (r/map inc (r/filter even? v))))
  (time (r/fold + (r/map inc (r/filter even? v))))
)

;; But we can use Transducers too - our "data transformation recipes" can be
;; used for paralell processing

(comment
  (def recipe (comp
                (filter even?)
                (map inc)))

  ;; Our reducing function needs to be our "recipe", with our "combining function" - addition
  (time (r/fold (recipe +) v))
)

