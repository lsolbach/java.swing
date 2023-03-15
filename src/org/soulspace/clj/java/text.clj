;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.java.text
  (:require [org.soulspace.clj.java.beans :as b])
  (:import [java.text ChoiceFormat DateFormat DecimalFormat MessageFormat NumberFormat SimpleDateFormat]))

;;
;; Functions to construct java.text.Format instances
;;

(defn message-format
  "Creates a MessageFormat."
  ([fmt]
   (MessageFormat. fmt))
  ([fmt locale]
   (MessageFormat. fmt locale)))

(defn choice-format
  "Creates a ChoiceFormat."
  ([pattern]
   (ChoiceFormat. pattern))
  ([doubles formats]
   (ChoiceFormat. doubles formats)))

(defn decimal-format
  "Creates a DecimalFormat."
  ([fmt args]
   (let [f (DecimalFormat. fmt)]
     (b/set-properties! f args)
     f))
  ([fmt locale args]
   (let [f (DecimalFormat. fmt locale)]
     (b/set-properties! f args)
     f)))

(defn number-format
  "Returns the general NumberFormat instance."
  ([]
   (NumberFormat/getInstance))
  ([locale]
   (NumberFormat/getInstance locale)))

(defn currency-format
  "Returns the currency NumberFormat instance."
  ([]
   (NumberFormat/getCurrencyInstance))
  ([locale]
   (NumberFormat/getCurrencyInstance locale)))

(defn integer-format
  "Returns the integer NumberFormat instance."
  ([]
   (NumberFormat/getIntegerInstance))
  ([locale]
   (NumberFormat/getIntegerInstance locale)))

(defn percent-format
  "Returns the percent NumberFormat instance."
  ([]
   (NumberFormat/getPercentInstance))
  ([locale]
   (NumberFormat/getPercentInstance locale)))

(defn date-format
  "Returns the DateFormat instance."
  ([]
   (DateFormat/getDateInstance))
  ([locale]
   (DateFormat/getDateInstance locale)))

(defn simple-date-format
  "Creates a SimpleDateFormat."
  ([fmt args]
   (let [f (SimpleDateFormat. fmt)]
     (b/set-properties! f args)
     f))
  ([fmt locale args]
   (let [f (SimpleDateFormat. fmt locale)]
     (b/set-properties! f args)
     f)))
