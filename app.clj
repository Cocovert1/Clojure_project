(ns app
  (:require
    [clojure.string :as str]))

(defn pars
  [data text]
  (if not-empty data
                (do (
                      (concat text (first data))
                     (pars (rest data) text)
                      )
                  )
                )
  )

(defn split
  [line]
  (str/split line #"\|")
  )

(defn getData
  [filename]
  (def lines (pars (str/split-lines (slurp filename)) `()))
  (map split lines)
  )

(defn get-lines [file]
  (str/split-lines (slurp file))
  )

(defn customer []
  (doseq [i (range (count (get-lines "cust.txt")))]
    (print (nth (nth (getData "cust.txt") i) 0))
    (print " ")
    (print (nth (nth (getData "cust.txt") i) 1))
    (print " ")
    (print (nth (nth (getData "cust.txt") i) 2))
    (print " ")
    (print (nth (nth (getData "cust.txt") i) 3))
    (println)
    )
  )

(defn product []
  (doseq [i (range (count (get-lines "prod.txt")))]
    (print (nth (nth (getData "prod.txt") i) 0))
    (print " ")
    (print (nth (nth (getData "prod.txt") i) 1))
    (print " ")
    (print (nth (nth (getData "prod.txt") i) 2))
    (println)
    )
  )

(defn sales []
  (doseq [i (range (count (get-lines "sales.txt")))]
    (print  (nth (nth (getData "cust.txt") (- (Integer/parseInt (nth (nth (getData "sales.txt") i) 1)) 1)) 1))
    (print " ")
    (print  (nth (nth (getData "prod.txt") (- (Integer/parseInt (nth (nth (getData "sales.txt") i) 2)) 1)) 1))
    (print " ")
    (print  (nth (nth (getData "sales.txt") i) 3))
    (println "")
    )
  )

(defn totalSales []
  (println "Who would you like to look up: ")
  (let [name (read-line)]
    (def price (atom 0))
    (doseq [i (range (count (get-lines "cust.txt")))]
      (if (= name (nth (nth (getData "cust.txt") i) 1))
        (do (def value (nth (nth (getData "cust.txt") i) 0))
        (doseq [j (range (count (get-lines "sales.txt")))]
          (if (= value (nth (nth (getData "sales.txt") j) 1))
            (swap! price + (* (Double/parseDouble (nth (nth (getData "prod.txt") (- (Integer/parseInt (nth (nth (getData "sales.txt") j) 2)) 1)) 2)) (Integer/parseInt (nth (nth (getData "sales.txt") j) 3))))
            )
          ))
        )
      )
     (print name":"(deref price)"$")
    )
  )


(defn totalCount []
  (println "What would you like to look up: ")
  (let [item (read-line)]
    (def temp (atom 0))
    (doseq [i (range (count (get-lines "prod.txt")))]
      (if (= (nth (nth (getData "prod.txt") i ) 1) item)
        (do
          (doseq [j (range (count (get-lines "sales.txt")))]
          (if (= (Integer/parseInt (nth (nth (getData "sales.txt") j) 2)) (Integer/parseInt (nth (nth (getData "prod.txt") i ) 0)))
            (do
              (swap! temp  + (Integer/parseInt (nth (nth (getData "sales.txt") j) 3)))
              )
            )
          )
          )
        )
      )
    (print item ": " (deref temp))
    )
  )



(defn menu [] (println "\n*** Sales Menu ***
  ------------------
  1. Display Customer Table
  2. Display Product Table
  3. Display Sales Table
  4. Total Sales for Customer
  5. Total Count for Product
  6. Exit
  Enter an option?\n")
  (let [option (Integer/parseInt (read-line))]
    (if (== option 1)
      (customer))
    (if (== option 2)
      (product))
    (if (== option 3)
      (sales))
    (if (== option 4)
      (totalSales))
    (if (== option 5)
      (totalCount)
    (if (== option 6)
      (do (println "Good Bye")
      (System/exit 0))
      )
    )
  (recur)))

(defn main []
  (menu)
  )

(main)
