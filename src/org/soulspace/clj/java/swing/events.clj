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

(ns org.soulspace.clj.java.swing.events
  "Functions to create and add Swing event listeners."
  (:import [javax.swing.event AncestorListener CaretListener CellEditorListener ChangeListener DocumentListener
            HyperlinkListener InternalFrameListener ListDataListener ListSelectionListener
            MenuDragMouseListener MenuKeyListener MenuListener MouseInputAdapter MouseInputListener PopupMenuListener
            RowSorterListener TableColumnModelListener TableModelListener
            TreeExpansionListener TreeModelListener TreeSelectionListener TreeWillExpandListener
            UndoableEditListener]))

;;;
;;; Create listeners
;;;

;; TODO add adapters for multi listeners and add listener functions

(defn ancestor-listener
  "Creates an ancestor listener. Calls function 'f' on ancestor updates."
  [added-f moved-f removed-f & args]
  (proxy [AncestorListener] []
    (ancestorAdded [event] (apply added-f event args))
    (ancestorMoved [event] (apply moved-f event args))
    (ancestorRemoved [event] (apply removed-f event args))))

(defn ancestor-added-listener
  "Creates an ancestor listener. Calls function 'f' on ancestor updates."
  [f & args]
  (proxy [AncestorListener] []
    (ancestorAdded [event] (apply f event args))
    (ancestorMoved [_] nil)
    (ancestorRemoved [_] nil)))

(defn ancestor-moved-listener
  "Creates an ancestor listener. Calls function 'f' on ancestor updates."
  [f & args]
  (proxy [AncestorListener] []
    (ancestorAdded [_] nil)
    (ancestorMoved [event] (apply f event args))
    (ancestorRemoved [_] nil)))

(defn ancestor-removed-listener
  "Creates an ancestor listener. Calls function 'f' on ancestor updates."
  [f & args]
  (proxy [AncestorListener] []
    (ancestorAdded [_] nil)
    (ancestorMoved [_] nil)
    (ancestorRemoved [event] (apply f event args))))

(defn caret-listener
  "Creates a caret listener. Calls function 'f' on caret updates."
  [f & args]
  (proxy [CaretListener] []
    (caretUpdate [event] (apply f event args))))

(defn change-listener
  "Creates a change listener. Calls function 'f' on state changes."
  [f & args]
  (proxy [ChangeListener] []
    (stateChanged [event] (apply f event args))))

(defn hyperlink-listener
  "Creates a hyperlink listener. Calls function 'f' on hyperlink updates."
  [f & args]
  (proxy [HyperlinkListener] []
    (hyperlinkUpdate [event] (apply f event args))))

(defn list-selection-listener
  "Creates a list selection listener. Calls function 'f' on value changes."
  [f & args]
  (proxy [ListSelectionListener] []
    (valueChanged [event] (apply f event args))))

(comment
  ; duplicate from awt events
 (defn mouse-clicked-listener
   "Creates a mouse input listener. Calls function 'f' on mouse clicks."
   [f args]
   (proxy [MouseInputAdapter] []
     (mouseClicked [event] (f event args)))))


;;
;; Add listeners
;;

(defn add-action-listener
  "Adds an action listener to the component."
  [c ^java.awt.event.ActionListener l]
  (.addActionListener c l))

(defn add-ancestor-listener
  "Adds an ancestor listener to the JComponent."
  [^javax.swing.JComponent c ^javax.swing.event.AncestorListener l]
  (.addAncestorListener c l))

(defn add-change-listener
  "Adds a change listener to the JComponent."
  [c ^javax.swing.event.ChangeListener l]
  (.addChangeListener c l))

(defn add-item-listener
  "Adds an item listener to the component."
  [^java.awt.ItemSelectable c ^java.awt.event.ItemListener l]
  (.addItemListener c l))

(defn add-list-selection-listener
  "Adds a change listener to the JComponent."
  [^javax.swing.ListSelectionModel c ^javax.swing.event.ListSelectionListener l]
  (.addListSelectionListener c l))

(defn add-popup-menu-listener
  "Adds a popup menu listener to the component."
  [c ^javax.swing.event.PopupMenuListener l]
  (.addPopupMenuListener c l))
