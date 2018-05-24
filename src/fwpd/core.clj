(ns fwpd.core
  (:gen-class))

(def filename "suspects.csv")

(def vamp-keys [:name :glitter-index])

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                  :glitter-index str->int})

(defn convert
  [vamp-key value]
  ((get conversions vamp-key) value))

(defn parse
  "Convert a CSV into rows of columns"
  [string]
  (map #(clojure.string/split % #",")
       (clojure.string/split string #"\r\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward Cullen\": :glitter-index 10}"
  [rows]
  (map (fn [unmapped-row]
         (reduce (fn [row-map [vamp-key value]]
                   (assoc row-map vamp-key (convert vamp-key value)))
                 {}
                 (map vector vamp-keys unmapped-row)))
       rows))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

(defn glitter-filter-names
  [& args]
  (map :name (apply glitter-filter args)))

(defn convert-to-string
  [suspect]
  (clojure.string/join "," (vals suspect)))

(defn append
  "Appends a new suspect to db of suspects"
  [suspect]
  (spit filename
        (str (slurp filename) "\r\n" (str (convert-to-string suspect)))))

(def suspect-keys [:name :glitter-index])

(defn validate
  "Validates that suspect has valid structure"
  [suspect]
  (every? #(% suspect) suspect-keys))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
