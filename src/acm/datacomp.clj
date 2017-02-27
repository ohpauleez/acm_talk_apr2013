(ns acm.datacomp)

(comment

;; Philosophy
;; ===========
;;
;; There is a lot of subtle power in composition.
;; We're even told to favor it over other methods of creation -
;; "Favor composition over inheritance".  But what does that mean in practice?

;; Let's program with values
;; --------------------------

  (def numbers [1 2 3 4 5 6])
  (def words '("one" "two" "three" "four"))
  (def keywords #{:a :b :c})
  (def regexs {:hello #"hello"
               :only-digits #"\d+"})

  (:hello regexs)
  (regexs :hello)
  (get regexs :hello)

  (:not-there regexs)
  (regexs :not-there)
  (get regexs :not-there)

  (into words numbers)
  (into numbers words)
  (conj numbers 7)
  (conj numbers (count numbers))
  (set (conj numbers (count numbers)))
  (into (conj numbers 7) keywords)
  (into (conj numbers 7) (reverse words))

  (re-find #"\d+" "This is a string with 1234 some digits 5678 in it 90")
  (re-seq #"\d+" "This is a string with 1234 some digits 5678 in it 90")

  ;; Now let's use our map
  (re-seq (:only-digits regexs) "This is a string with 1234 some digits 5678 in it 90")

  ;; And just some other numbers
  (type 3N) ;; BigInteger literal
  (type 0xa) (type 012) (type 2r1010) ;; Long, but hex, oct, and binary literals
  (type 3.14) ;; Double
  (type 3M) ;; BigDecimal literal

  ;; What about nested data structures, like nested maps?
  (def scores
    {"Paul" {:score {:game-1 10
                     :game-2 900}
             :ranking 1}
     "Jane" {:score 900
             :ranking 20}})


  (= (update-in scores ["Paul" :score :game-1] + 100)
     (assoc-in scores ["Paul" :score :game-1]
               (+ 100
                  (get-in scores ["Paul" :score :game-1]))))

  (assoc (dissoc scores "Paul")
         "Mike" (scores "Paul"))
  (-> scores
      (dissoc "Paul")
      (assoc "Mike" (scores "Paul")))


  (require '[clojure.pprint :as pprint])
  (pprint/pprint scores)
  (println scores)

  (get-in scores ["Paul" :score :game-1])
  (assoc-in scores ["Paul" :score :game-2] 20)
  (assoc-in scores [ "Paul"] {:hello "world"})
  ("Paul" scores)


  ;; Getting back to our simple collections...
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

  ;; How would we check the numbers were all in ascending order
  (apply < numbers)
  (apply < (shuffle numbers))

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
  (filter #(= (type %1) java.lang.String) l-seq)
  (filter (fn [x] (= (type x) java.lang.String)) l-seq)
  (remove #(instance? String %) l-seq)
  (remove string? l-seq)

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
  (let [x (vector 1 2 4)
        [a b] x]
    a)
  (let [[a b] (vector 1 2)]
    a)
  (let [x {:a 1 :b 2}
        {:keys [a]} x]
    a)
  (keys {:a 1, :b 2})

  ;; Tying it all together
  (defn fibo [] (map first (iterate (fn [[a b]] [b  (+ a b)]) [0 1])))

  ;; ... understand the pieces ...
  (take 10 (iterate (fn [[a b]] [b  (+ a b)]) [0 1]))
  (first (first (iterate (fn [[a b]] [b  (+ a b)]) [0 1])))

  ;; ... now let's use our Fibonacci sequence ...
  (take 10 (fibo))
  (into [] (take 10 (fibo)))
  (vec (take 10 (fibo)))

  ;; But we can decouple the "recipe" of processing data
  ;; from the source and destination of the data.
  ;;
  ;; This allows us to use our "recipes" across many contexts:
  ;; collections, sequences, channels, observables, etc.
  ;;
  ;; We can independently choose the processing characteristics as needed.
  ;; Lazy when we need it, Eager when it's more appropriate, etc.
  ;; And composed recipes can be optimized, reducing allocations and
  ;; improving processing overall.
  ;;
  ;; These are known as "Transducers" - Composable algorithmic transformations

  (def recipe (comp (filter odd?) ;; First filter for odd numbers
                    (map inc)     ;; Then increment the numbers
                    (take 5)))    ;; And take the first 5

  ;; Our recipe is indepent of our source, destination, and processing decisions
  (into [] recipe (fibo))


  ;; We can also use the full JVM/Java ecosystem...
  (Math/round 14.4566) ; This is the `round` static method of the `Math` class
  ; This below, is calling the `startsWith` method of the String Obj, "Hello World"
  (.startsWith "Hello World" "Hello")
  (filter #(.startsWith (str %) "t") l-seq)

  ;; And we can sanely do concurrent programming
  ;;
  ;; Let's get a thread...
  ;; Here we'll use a `future` (Clojure has futures and promises)
  (def result (future (apply + (range 100000000))))

  (inc (deref result))
  (inc @result)
  (def cores (.availableProcessors (Runtime/getRuntime)))
  ;; You can also use Threads directly, although it is not very common
  (.start (Thread. #(println "Hello from inside a thread")))

  (defn long-running-job [n]
    (Thread/sleep 3000)
    (+ n 10))

  (time (doall (map long-running-job (range 3))))

  (time (map long-running-job (range 3)))

  (time (doall (pmap long-running-job (range 3))))

  ;; goto
  (require 'acm.foldit)

)

