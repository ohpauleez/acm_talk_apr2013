(ns acm.datacomp)

(comment
  
;; Philosophy
;; ===========
;;
;; There is a lot of subtle power in composition.
;; We're even told to favor it over other methods of creation -
;; "Favor composition over inheritence".  But what does that mean in practice?

;; Let's program with values
;; --------------------------
  
  (def numbers [1 2 3 4 5 6])
  (def words '("one" "two" "three" "four"))
  (def keywords #{:a :b :c})

  (into words numbers)
  (conj numbers 7)
  (conj numbers (count numbers))
  (set (conj numbers (count numbers)))
  (into (conj numbers 7) keywords)
  (into (conj numbers 7) (reverse words))

  ;; How would we sum all the numbers above?
  ;; We'd just use addition.  In grade school you wrote:
  ;;   1 + 2 + 3 + 4 + 5 + 6
  ;
  ;; A few year later, you learned you could stack use a single addition sign:
  ;;          1
  ;;          2
  ;;          3
  ;;          4
  ;;          5
  ;;        + 6
  ;;        ----
  ;; 
  ;; So why would a programming language treat you like a child?
  (= (+ 1 2 3 4 5 6)
     (apply + numbers))

  ;; Mapping across collections?
  ;; Higher-order functions: One of many benefits of first-class functions
  (inc 1)
  (map inc numbers)
  ;; Imagine want to get the columns from a matrix...
  ;; 1 2 3
  ;; 4 5 6
  ;; 7 8 9
  (map vector [1 2 3] [4 5 6] [7 8 9])

  ;; What are the benefits of the sequence abstraction?
  (def l-seq (concat numbers words keywords))
  (type l-seq)
  (realized? l-seq)
  (take 2 l-seq)
  (realized? l-seq)
  (drop 5 l-seq)
  (take 2 (drop 3 l-seq))
  (->> l-seq (drop 3) (take 2))
  (butlast l-seq)
  (drop-last 2 l-seq)
  (filter #(= (type %) java.lang.String) l-seq)

  ;; Functions
  (defn add2
    "Given an integer/long, increment by 2"
    [x]
    (+ x 2))

  ;; Destructuring
  ;; Usually a `let` block looks like this:
  (let [x 1
        y 3]
    (+ x y))

  ;; but we can also pull sequential and associative data apart...
  (let [x (vector 1 2)]
    (first x))
  (let [x (vector 1 2)
        [a b] x]
    a)
  (let [[a b] (vector 1 2)]
    a)

  ;; Tying it all together
  (defn fibo [] (map first (iterate (fn [[a b]] [b  (+ a b)]) [0 1])))
  (take 10 (fibo))
  (into [] (take 10 (fibo)))
  (vec (take 10 (fibo)))

  ;; We can also use the full JVM/Java ecosystem...
  (Math/round 14.4566) ; This is the `round` static method of the `Math` class

  ;; And we can sanely do concurrent programming
  ;;
  ;; Let's get a thread...
  ;; Here we'll use a `future` (Clojure has futures and promises)
  (def result (future (apply + (range 100000000))))
  (inc (deref result))
  (inc @result)
  (def cores (.availableProcessors (Runtime/getRuntime)))

  (defn long-running-job [n]
    (Thread/sleep 3000)
    (+ n 10))

  (time (doall (map long-running-job (range 3))))
  (time (doall (pmap long-running-job (range 3))))

  ;; goto
  (require 'acm.foldit)

)

