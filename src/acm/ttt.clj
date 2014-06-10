(ns acm.ttt)

;; TicTacToe

(comment
  (def ttt-board [[:x :x :x] [:_ :o :_] [:_ :o :_]])
  (def tt4-board [[:x :x :x :x] [:_ :o :_ :o] [:_ :o :_ :_] [:_ :_ :_ :_]])

  ;; Let's solve this like we think about it - How do you know somsone won TTT?
  ;; You look at the rows, you look at the columns, you look at the diagnols,
  ;; And you see if the same player occupies all the spaces.

  ;; Given a board (built of rows), let's build up the rows, columns, and diagnols
  ;; Then we can easily see if any of them are winners
  (defn winning-move [board]
    (first (filter #(apply = %) (reduce into
                                      board ;; Rows
                                      [(apply map vector board) ;; Columns
                                       (vector (mapv get (reverse board) (range)) ;; Diag bottom to upper right
                                               ;; Diag top to bottom right
                                               (mapv get board (range)))]))))
  (time (winning-move tt4-board))

)

