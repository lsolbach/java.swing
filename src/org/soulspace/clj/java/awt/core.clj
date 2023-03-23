;;;;
;;;;   Copyright (c) Ludger Solbach. All rights reserved.
;;;;
;;;;   The use and distribution terms for this software are covered by the
;;;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;;;   which can be found in the file license.txt at the root of this distribution.
;;;;   By using this software in any fashion, you are agreeing to be bound by
;;;;   the terms of this license.
;;;;
;;;;   You must not remove this notice, or any other, from this software.
;;;;

(ns org.soulspace.clj.java.awt.core
  "Functions and definitions for programming Java AWT in Clojure."
  (:import [java.awt BasicStroke Color Dimension Event Font GradientPaint Graphics2D Insets TexturePaint]))

;;;;
;;;; Functions and definitions for Java AWT
;;;;

;;
;; constant maps
;;

(def color-by-name {:black Color/BLACK
                    :blue Color/BLUE
                    :cyan Color/CYAN
                    :dark-gray Color/DARK_GRAY
                    :gray Color/GRAY
                    :green Color/GREEN
                    :light-gray Color/LIGHT_GRAY
                    :magenta Color/MAGENTA
                    :orange Color/ORANGE
                    :pink Color/PINK
                    :red Color/RED
                    :white Color/WHITE
                    :yellow Color/YELLOW})

(def font-styles {:bold Font/BOLD
                  :italic Font/ITALIC
                  :plain Font/PLAIN
                  :center-baseline Font/CENTER_BASELINE
                  :hanging-baseline Font/HANGING_BASELINE
                  :roman-baseline Font/ROMAN_BASELINE})

(def font-names {:dialog Font/DIALOG
                 :dialog-input Font/DIALOG_INPUT
                 :monospaced Font/MONOSPACED
                 :sans-serif Font/SANS_SERIF
                 :serif Font/SERIF})

;;
;; Basic constructors
;;

;; dimension
(defn dimension
  "Creates a dimension."
  [width height]
  (Dimension. width height))

;; insets
(defn insets
  "Creates insets."
  [top left bottom right]
  (Insets. top left bottom right))

;; color
(defn color
  "Creates a color."
  ([value]
   (Color. value))
  ([r g b]
   (Color. r g b))
  ([r g b a]
   (Color. r g b a)))

;; font
(defn font
  "Creates a font."
  ([font]
   (Font. font))
  ([font-name style-vec size]
   (Font. font-name (reduce + style-vec) size)))

(defn derive-font
  "Derives a font from the given font."
  ([font style-vec]
   (.derive font (reduce + style-vec)))
  ([fontname style-vec size]
   (.derive font (reduce + style-vec) size)))
