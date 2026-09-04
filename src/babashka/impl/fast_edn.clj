(ns babashka.impl.fast-edn
  {:no-doc true}
  (:require
   [fast-edn.core]
   [sci.core :as sci]))

(def ens (sci/create-ns 'fast-edn.core))

(def fast-edn-namespace (sci/copy-ns fast-edn.core ens))
