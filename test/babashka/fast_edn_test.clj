(ns babashka.fast-edn-test
  (:require
   [babashka.test-utils :as test-utils]
   [clojure.edn :as edn]
   [clojure.test :refer [deftest is testing]]))

(defn bb [& args]
  (edn/read-string
   {:eof nil}
   (apply test-utils/bb nil (map str args))))

(deftest read-string-test
  (is (= {:a 1 :b [true nil]}
         (bb "(require '[fast-edn.core :as fe])
              (fe/read-string \"{:a 1 :b [true nil]}\")")))
  (is (nil? (bb "(require '[fast-edn.core :as fe])
                 (fe/read-string nil)")))
  (testing "does not replace clojure.edn"
    (is (= {:a 1}
           (bb "(require '[clojure.edn :as edn])
                (edn/read-string \"{:a 1}\")")))))

(deftest read-once-and-parser-test
  (is (= {:a 1}
         (bb "(require '[fast-edn.core :as fe])
              (fe/read-once \"{:a 1}\")")))
  (is (= [1 2]
         (bb "(require '[fast-edn.core :as fe])
              (let [p (fe/parser {:eof ::eof} \"1 2\")]
                [(fe/read-next p) (fe/read-next p)])"))))

(deftest readers-and-inst-test
  (is (= 2
         (bb "(require '[fast-edn.core :as fe])
              (fe/read-string {:readers {'t/tag inc}} \"#t/tag 1\")")))
  (is (inst? (bb "(require '[fast-edn.core :as fe])
                  (fe/read-string \"#inst \\\"2020-01-01T00:00:00.000-00:00\\\"\")"))))

(deftest char-array-reader-test
  (is (= {:a 1}
         (bb "(import 'java.io.CharArrayReader)
              (require '[fast-edn.core :as fe])
              (fe/read-once (CharArrayReader. (char-array \"{:a 1}\")))"))))
