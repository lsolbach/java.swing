;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.java.swing.constants
  (:import [java.awt Event]
           [java.awt.event ActionEvent]
           [javax.swing Action JComponent JFileChooser JFormattedTextField JOptionPane JTextField ListSelectionModel
            SwingConstants WindowConstants]))

;;
;; Definitions of Swing constants
;;

;;
;; Constant maps
;;

(def swing-keys
  "Maps keywords to the constants in SwingConstants."
  {:bottom     SwingConstants/BOTTOM
   :center     SwingConstants/CENTER
   :east       SwingConstants/EAST
   :horizontal SwingConstants/HORIZONTAL
   :leading    SwingConstants/LEADING
   :left       SwingConstants/LEFT
   :next       SwingConstants/NEXT
   :north      SwingConstants/NORTH
   :north-east SwingConstants/NORTH_EAST
   :north-west SwingConstants/NORTH_WEST
   :previous   SwingConstants/PREVIOUS
   :right      SwingConstants/RIGHT
   :south      SwingConstants/SOUTH
   :south-east SwingConstants/SOUTH_EAST
   :south-west SwingConstants/SOUTH_WEST
   :top        SwingConstants/TOP
   :trailing   SwingConstants/TRAILING
   :vertical   SwingConstants/VERTICAL
   :west       SwingConstants/WEST})

(def window-keys
  "Maps keywords to the constants in WindowConstants."
  {:nothing WindowConstants/DO_NOTHING_ON_CLOSE
   :dispose WindowConstants/DISPOSE_ON_CLOSE
   :hide    WindowConstants/HIDE_ON_CLOSE
   :exit    WindowConstants/EXIT_ON_CLOSE})

(def focus-condition-keys
  "Maps keywords to the constants in JComponent."
  {:undefined                          JComponent/UNDEFINED_CONDITION ; used by some of the APIs to mean that no condition is defined.
   :when-ancestor-of-focused-component JComponent/WHEN_ANCESTOR_OF_FOCUSED_COMPONENT ; used for registerKeyboardAction that means that the command should be invoked when the receiving component is an ancestor of the focused component or is itself the focused component.
   :when-focused                       JComponent/WHEN_FOCUSED ; used for registerKeyboardAction that means that the command should be invoked when the component has the focus.
   :when-in-focused-window             JComponent/WHEN_IN_FOCUSED_WINDOW}) ; used for registerKeyboardAction that means that the command should be invoked when the receiving component is in the window that has the focus or is itself the focused component.


(def action-keys
  "Maps keywords to the constants in Action."
  {:name        Action/NAME
   :accelerator Action/ACCELERATOR_KEY
   :command-key Action/ACTION_COMMAND_KEY
   :long-desc   Action/LONG_DESCRIPTION
   :short-desc  Action/SHORT_DESCRIPTION
   :mnemonic    Action/MNEMONIC_KEY
   :icon        Action/SMALL_ICON})

(def modifier-mask-keys
  "Maps keywords to the constants in ActionEvent."
  {:ctrl  ActionEvent/CTRL_MASK
   :shift ActionEvent/CTRL_MASK
   :alt   ActionEvent/ALT_MASK})

(def list-selection-keys
  "Maps keywords to the constants in ListSelectionModel."
  {:single            ListSelectionModel/SINGLE_SELECTION
   :single-interval   ListSelectionModel/SINGLE_INTERVAL_SELECTION
   :multiple-interval ListSelectionModel/MULTIPLE_INTERVAL_SELECTION})

(def commit-edit-keys
  "Maps keywords to the constants in JFormattedTextField."
  {:commit           JFormattedTextField/COMMIT
   :commit-or-revert JFormattedTextField/COMMIT_OR_REVERT
   :persist          JFormattedTextField/PERSIST
   :revert           JFormattedTextField/REVERT})

(def filechooser-keys
  "Maps keywords to the constants in JFileChooser."
  {:approve               JFileChooser/APPROVE_OPTION
   :cancel                JFileChooser/CANCEL_OPTION
   :error                 JFileChooser/ERROR_OPTION
   :directories-only      JFileChooser/DIRECTORIES_ONLY
   :files-only            JFileChooser/FILES_ONLY
   :files-and-directories JFileChooser/FILES_AND_DIRECTORIES})

; TODO use in message dialogs
(def option-pane-message-keys
  "Maps keywords to the constants in JOptionsPane."
  {:error    JOptionPane/ERROR_MESSAGE
   :warning  JOptionPane/WARNING_MESSAGE
   :question JOptionPane/QUESTION_MESSAGE
   :info     JOptionPane/INFORMATION_MESSAGE
   :plain    JOptionPane/PLAIN_MESSAGE})

(def option-pane-keys
  "Maps keywords to the constants in JOptionsPane."
  {:default       JOptionPane/DEFAULT_OPTION
   :yes-no        JOptionPane/YES_NO_OPTION
   :yes-no-cancel JOptionPane/YES_NO_CANCEL_OPTION
   :ok-cancel     JOptionPane/OK_CANCEL_OPTION
   :yes           JOptionPane/YES_OPTION
   :no            JOptionPane/NO_OPTION
   :cancel        JOptionPane/CANCEL_OPTION
   :ok            JOptionPane/OK_OPTION
   :closed        JOptionPane/CLOSED_OPTION})
