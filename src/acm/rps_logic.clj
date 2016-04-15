(ns acm.rps-logic
  (:refer-clojure :exclude (==))
  (:require [clojure.core.logic :as logic :refer [defrel fact]]))

;; This is based on Stuart Halloway's "Exploring Clojure"
;; -- https://github.com/stuarthalloway/exploring-clojure/blob/master/examples/exploring/rock_paper_scissors.clj

(defrel rps winner defeats loser)

(fact rps :scissors :cut :paper)
(fact rps :paper :covers :rock)
(fact rps :rock :crushes :lizard)
(fact rps :lizard :poisons :spock)
(fact rps :spock :melts :scissors)
(fact rps :scissors :decapitate :lizard)
(fact rps :lizard :eats :paper)
(fact rps :paper :disproves :spock)
(fact rps :spock :vaporizes :rock)
(fact rps :rock :breaks :scissors)

(defrel rank title person)
(fact rank :captain :kirk)
(fact rank :commander :spock)
(fact rank :lieutenant :uhura)
(fact rank :ensign :chekov)

;; how can I defeat paper?
(logic/run* [verb]
            (logic/fresh [winner]
                         (rps winner verb :paper)))

;; what can kill?
(logic/run* [winner]
            (logic/fresh [verb loser]
                         (rps winner verb loser)))

;; find me one thing that can kill a Star Trek officer
(logic/run 1 [winner]
           (logic/fresh [verb loser title]
                        (rps winner verb loser)
                        (rank title loser)))

