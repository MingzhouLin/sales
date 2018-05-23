(ns sales.core)

(defn read_file []
  (defstruct Customer :CusId :CusName :Address :Phone)
  (defstruct Product :ProdId :ProdName :Price)
  (defstruct Sale :saleId :Customer :Product :Number)
  
  (def customers (sorted-map))
  (def file (slurp "cust.txt"))
  (doseq [line (clojure.string/split-lines file)]
	    (def customer_attributes (clojure.string/split line #"\|"))
	    (def customer (struct-map Customer :CusId (nth customer_attributes 0) :CusName (nth customer_attributes 1) 
	                                 :Address (nth customer_attributes 2) :Phone (nth customer_attributes 3)))
      (def new_customer (sorted-map (nth customer_attributes 0) customer))
      (def customers (merge-with + customers new_customer))
    )
  
  (def products (sorted-map))
  (def file (slurp "prod.txt"))
  (doseq [line (clojure.string/split-lines file)]
	    (def product_attributes (clojure.string/split line #"\|"))
	    (def product (struct-map Product :ProdId (nth product_attributes 0) :ProdName (nth product_attributes 1) 
	                                :Price (nth product_attributes 2)))
      (def new_product (sorted-map (nth product_attributes 0) product))
      (def products (merge-with + products new_product))
    )
  
  (def sales (sorted-map))
  (def file (slurp "sales.txt"))
  (doseq [line (clojure.string/split-lines file)]
	    (def sale_attributes (clojure.string/split line #"\|"))
	    (def sale (struct-map Sale :saleId (nth sale_attributes 0) :Customer (nth sale_attributes 1) 
	                             :Product (nth sale_attributes 2) :Number (nth sale_attributes 3)))
      (def new_sale (sorted-map (nth sale_attributes 0) sale))
      (def sales (merge-with + sales new_sale))
    )
 )

(defn menu[]
  (println "*** Sales Menu ***")
  (println "------------------")
  (println)
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  )

(defn printCustomers[]
  (doseq [key (keys customers)]
    (def customer (get customers key))
    (println (str key ":" (:CusName customer) "," (:Address customer) "," (:Phone customer)))
  )
 )

(defn printProduct[]
  (doseq [key (keys products)]
    (def product (get products key))
    (println (str key ":" (:ProdName product) "," (:Price product)))
  )
 )

(defn printSales[]
  (doseq [key (keys sales)]
    (def sale (get sales key))
    (println (str key ":" (:CusName (get customers (:Customer sale))) "," (:ProdName (get products (:Product sale))) "," (:Number sale)))
  )
 )

(defn total_sale[]
  (println "Please input the customer name:")
  (def CusName (read-line))
  (doseq [customer (vals customers)]
    (if (= CusName (:CusName customer))
      (def cusId (:CusId customer))
      )
    )
  (def sum 0.00)
  (doseq [sale (vals sales)]
    (if (= cusId (:Customer sale))
      (do 
        (def number (:Number sale))
        (def price (:Price (get products (:Product sale))))
        (def sum (+ sum (* (Float/parseFloat price) (Float/parseFloat number))))
       )
    )  
  )
  (println (str CusName ":" (format "%.2f" sum)))
)

(defn total_number[]
  (println "Please input the product name:")
  (def prodName (read-line))
  (doseq [product (vals products)]
    (if (= prodName (:ProdName product))
      (def prodId (:ProdId product))
      )
    )
  (def sum 0)
  (doseq [sale (vals sales)]
    (if (= prodId (:Product sale))
      (do 
        (def number (:Number sale))
        (def sum (+ sum (Integer/parseInt number)))
       )
    )  
  )
  (println (str prodName ":" sum))
)
(defn main[]
  (read_file)
  (def opt "0")
  (while (not= opt "6")
    (menu)
    (do
      (def opt (read-line))
      (case opt
         "1" (printCustomers)
         "2" (printProduct)
         "3" (printSales)
         "4" (total_sale)
         "5" (total_number)
         "6" (println "Good Bye")
      )
    )
  )
)
(main)