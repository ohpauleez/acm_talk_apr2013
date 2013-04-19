(ns acm.sudoku
  "Sudoku solver example from: https://github.com/clojure/core.logic/wiki/Examples"
  (:refer-clojure :exclude [==])
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.fd :as fd]))

(defn get-square [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]
    (get-in rows [x y])))

(defn init [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (all
        (if-not (zero? hint)
          (== (first vars) hint)
          succeed)
        (init (next vars) (next hints))))
    succeed))

(defn sudokufd [hints]
  (let [vars (repeatedly 81 lvar)
        rows (->> vars (partition 9) (map vec) (into []))
        cols (apply map vector rows)
        sqs  (for [x (range 0 9 3)
                   y (range 0 9 3)]
               (get-square rows x y))]
    (run 1 [q]
      (== q vars)
      (everyg #(fd/in % (fd/domain 1 2 3 4 5 6 7 8 9)) vars)
      (init vars hints)
      (everyg fd/distinct rows)
      (everyg fd/distinct cols)
      (everyg fd/distinct sqs))))

(comment
 (sudokufd
   [0 0 3 0 2 0 6 0 0
    9 0 0 3 0 5 0 0 1
    0 0 1 8 0 6 4 0 0

    0 0 8 1 0 2 9 0 0
    7 0 0 0 0 0 0 0 8
    0 0 6 7 0 8 2 0 0

    0 0 2 6 0 9 5 0 0
    8 0 0 2 0 3 0 0 9
    0 0 5 0 1 0 3 0 0])  
)

