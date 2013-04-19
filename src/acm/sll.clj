(ns acm.sll)

;; Let's explore the topic of "structural sharing."
;; -------------------------------------------------

;; Here is a singly-linked list of four elements
;; There is absolutely nothing special about it
(def l '(:a :b :c :d))
(comment
  (type l)
  (count l)
  (supers (class l)))

(def ll (conj l :e))
(comment
  (= l ll)
  (= l (next ll))
  (identical? l (next ll)))

;; AHA!  So we can visualize `l` and `ll` like:
;;     l        :a - :b - :c - :d
;;     ll    e:/
;;
;; Does this hold if we keep at this?

(def rl (next l))
(def rll (conj rl :z))
(def llrll (conj rl ll))
(comment
  rl
  llrll
  (= (-> llrll first next next) (next llrll))
  (identical? (-> llrll nfirst next) (next llrll)))

;; So we're seeing that sharing is happening!  AWESOME!

