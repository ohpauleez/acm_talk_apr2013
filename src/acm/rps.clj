(ns acm.rps
  "The core Rock-Paper-Scissors game;
  Completely functional")

(def winning-combos {:rock :scissors
                     :paper :rock
                     :scissors :paper})

(defn generate-move
  "Returns a random, valid move - as a keyword
  Optionally pass in a collection that supports `nth` lookup"
  ([] (generate-move (keys winning-combos)))
  ([move-combos]
   (rand-nth move-combos)))

(defn valid-choice?
  "Determines if a given keyword-move is a valid choice;
  ie: in the set #{:rock :paper :scissors}"
  ([kw-choice]
   (valid-choice? winning-combos kw-choice))
  ([combos kw-choice]
   (contains? combos kw-choice)))

(defn winning-choice
  "Given two keyword choices (two players)
  Return a vector of winning choice (as a keyword) or :draw if they are equal
  and the winning player, as a keyword
  ie: [:rock :player-1]"
  [player-1 player-2]
  (when (every? valid-choice? [player-1 player-2])
    (cond
      (= player-1 player-2) [:draw nil]
      (= (winning-combos player-1) player-2) [player-1 :player-1]
      :else [player-2 :player-2])))

(defn play-game [player-1 player-2]
  ;{:pre [(every? valid-choice? [player-1 player-2])]}
  (let [[winning-choice winning-player] (winning-choice player-1 player-2)]
    {:player-1 player-1
     :player-2 player-2
     :winning-choice winning-choice
     :winning-player winning-player}))

(comment

  ;; An example
  ;; -----------
  ;;
  ;; This example runs four games.
  ;; I picked choices for one player: `[:rock :paper :rock :rock]`
  ;; The choices for the other player are randomly generated
  ;;
  ;; I generate the sequence of the winning players per game,
  ;; and use the Clojure function, `frequencies`, to get the final score.

  (frequencies
    (keep identity ; gets rid of nils
          (map :winning-player ; Get the sequence of winning players
               (map (fn [[player-1 player-2]] (play-game player-1 player-2))
                    (map vector [:rock :paper :rock :rock] (repeatedly generate-move))))))
  )

