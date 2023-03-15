;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.java.awt.event
  (:import [java.beans PropertyChangeListener]
           [java.awt.event
            ActionListener AdjustmentListener ComponentAdapter ComponentListener
            ContainerAdapter ContainerListener FocusAdapter FocusListener
            HierarchyBoundsAdapter HierarchyBoundsListener HierarchyListener
            ItemListener KeyAdapter KeyEvent
            MouseAdapter MouseListener MouseMotionAdapter MouseMotionListener MouseWheelListener
            TextListener WindowAdapter WindowFocusListener WindowListener WindowStateListener]
           [java.awt.dnd DragSourceListener DragSourceAdapter DropTargetListener DropTargetAdapter]))

;;
;; Functions to create and add AWT event listeners
;;

(defn no-operation-fn
  ""
  [])

; TODO create adapter macro to create an adapter proxy with the given named methods implemented
; (defadapter ComponentAdapter args :componentResized f-resized :componentMoved f-moved)

(defmacro defadapter
  ""
  [adapter args & fn-bindings])


; Listeners
(defn action-listener
  "Creates an action listener. Calls function 'f' on performing the action."
  [f & args]
  (proxy [ActionListener] []
    (actionPerformed [event] (apply f event args))))

(defn adjustment-listener
  "Creates an adjustment listener. Calls function 'f' on adjustment value changes."
  [f & args]
  (proxy [AdjustmentListener] []
    (adjustmentValueChanged [event] (apply f event args))))

(defn component-hidden-listener
  "Creates a component hidden listener. Calls function 'f' on component hidden."
  [f & args]
  (proxy [ComponentAdapter] []
    (componentHidden [event] (apply f event args))))

(defn component-moved-listener
  "Creates a component moved listener. Calls function 'f' on component moved."
  [f & args]
  (proxy [ComponentAdapter] []
    (componentMoved [event] (apply f event args))))

(defn component-resized-listener
  "Creates a component resized listener. Calls function 'f' on component resized."
  [f & args]
  (proxy [ComponentAdapter] []
    (componentResized [event] (apply f event args))))

(defn component-shown-listener
  "Creates a component shown listener. Calls function 'f' on component shown."
  [f & args]
  (proxy [ComponentAdapter] []
    (componentShown [event] (apply f event args))))

(defn component-adapter
  "Creates a component adaper. Calls the appropriate functions on events."
  [f-hidden f-moved f-resized f-shown & args]
  (proxy [ComponentAdapter] []
   (componentHidden [event] (apply f-hidden event args))
   (componentMoved [event] (apply f-moved event args))
   (componentResized [event] (apply f-resized event args))
   (componentShown [event] (apply f-shown event args))))

(defn container-added-listener
  "Creates a container added listener. Calls function 'f' on container added."
  [f & args]
  (proxy [ContainerAdapter] []
    (componentAdded [event] (apply f event args))))

(defn container-removed-listener
  "Creates a container removed listener. Calls function 'f' on container removed."
  [f & args]
  (proxy [ContainerAdapter] []
    (componentRemoved [event] (apply f event args))))

(defn focus-gained-listener
  "Creates a focus gained listener. Calls function 'f' on focus gained event."
  [f & args]
  (proxy [FocusAdapter] []
    (focusGained [event] (apply f event args))))

(defn focus-lost-listener
  "Creates a focus lost listener. Calls function 'f' on focus lost event."
  [f & args]
  (proxy [FocusAdapter] []
    (focusLost [event] (apply f event args))))

(defn hierarchy-bounds-moved-listener
  "Creates a hierarchy bounds moved listener. Calls function 'f' on hierarchy bounds moved event."
  [f & args]
  (proxy [HierarchyBoundsAdapter] []
    (ancestorMoved [event] (apply f event args))))

(defn hierarchy-bounds-resized-listener
  "Creates a hierarchy bounds resized listener. Calls function 'f' on hierarchy bounds resized event."
  [f & args]
  (proxy [HierarchyBoundsAdapter] []
    (ancestorResized [event] (apply f event args))))

(defn hierarchy-listener
  "Creates a hierarchy listener. Calls function 'f' on hierarchy changed event."
  [f & args]
  (proxy [HierarchyListener] []
    (hierarchyChanged [event] (apply f event args))))

; TODO input method (create adapter in clojure so there's a common programming model)

(defn item-listener
  "Creates an item listener. Calls function 'f' on item state changed event."
  [f & args]
  (proxy [ItemListener] []
    (itemStateChanged [event] (apply f event args))))

(defn key-pressed-listener
  "Creates a key pressed listener. Calls function 'f' on key pressed event."
  [f & args]
  (proxy [KeyAdapter] []
    (keyPressed [event] (apply f event args))))

(defn key-released-listener
  "Creates a key released listener. Calls function 'f' on key released event."
  [f & args]
  (proxy [KeyAdapter] []
    (keyReleased [event] (apply f event args))))

(defn key-typed-listener
  "Creates a key typed listener. Calls function 'f' on key typed event."
  [f & args]
  (proxy [KeyAdapter] []
    (keyTyped [event] (apply f event args))))

(defn mouse-clicked-listener
  "Creates a mouse clicked listener. Calls function 'f' on mouse clicked event."
  [f & args]
  (proxy [MouseAdapter] []
    (mouseClicked [event] (apply f event args))))

(defn mouse-entered-listener
  "Creates a mouse entered listener. Calls function 'f' on mouse entered event."
  [f & args]
  (proxy [MouseAdapter] []
    (mouseEntered [event] (apply f event args))))

(defn mouse-exited-listener
  "Creates a mouse exited listener. Calls function 'f' on mouse exited event."
  [f & args]
  (proxy [MouseAdapter] []
    (mouseExited [event] (apply f event args))))

(defn mouse-pressed-listener
  "Creates a mouse pressed listener. Calls function 'f' on mouse pressed event."
  [f & args]
  (proxy [MouseAdapter] []
    (mousePressed [event] (apply f event args))))

(defn mouse-released-listener
  "Creates a mouse released listener. Calls function 'f' on mouse released event."
  [f & args]
  (proxy [MouseAdapter] []
    (mouseReleased [event] (apply f event args))))

(defn mouse-motion-dragged-listener
  "Creates a mouse motion dragged listener. Calls function 'f' on mouse dragged event."
  [f & args]
  (proxy [MouseMotionAdapter] []
    (mouseDragged [event] (apply f event args))))

(defn mouse-motion-moved-listener
  "Creates a mouse motion moved listener. Calls function 'f' on mouse moved event."
  [f & args]
  (proxy [MouseMotionAdapter] []
    (mouseMoved [event] (apply f event args))))

(defn mouse-wheel-listener
  "Creates a mouse wheel listener. Calls function 'f' on mouse wheel moved event."
  [f & args]
  (proxy [MouseWheelListener] []
    (mouseWheelMoved [event] (apply f event args))))

(defn text-listener
  "Creates a text listener. Calls function 'f' on text value changed event."
  [f & args]
  (proxy [TextListener] []
    (textValueChanged [event] (apply f event args))))

(defn window-focus-gained-listener
  "Creates a window focus gained listener. Calls function 'f' on window focus gained event."
  [f & args]
  (proxy [WindowListener] []
    (windowGainedFocus [event] (apply f event args))))

(defn window-focus-lost-listener
  "Creates a window focus lost listener. Calls function 'f' on window focus lost event."
  [f & args]
  (proxy [WindowListener] []
    (windowLostFocus [event] (apply f event args))))

(defn window-activated-listener
  "Creates a window activated listener. Calls function 'f' on window activated event."
  [f & args]
  (proxy [WindowListener] []
    (windowActivated [event] (apply f event args))))

(defn window-closed-listener
  "Creates a window closed listener. Calls function 'f' on window closed event."
  [f & args]
  (proxy [WindowListener] []
    (windowClosed [event] (apply f event args))))

(defn window-closing-listener
  "Creates a window closing listener. Calls function 'f' on window closing event."
  [f & args]
  (proxy [WindowListener] []
    (windowClosing [event] (apply f event args))))

(defn window-deactivated-listener
  "Creates a window deactivated listener. Calls function 'f' on window deactivated event."
  [f & args]
  (proxy [WindowListener] []
    (windowDeactivated [event] (apply f event args))))

(defn window-deiconified-listener
  "Creates a window deiconified listener. Calls function 'f' on window deiconified event."
  [f & args]
  (proxy [WindowListener] []
    (windowDeiconified [event] (apply f event args))))

(defn window-iconified-listener
  "Creates a window iconified listener. Calls function 'f' on window iconified event."
  [f & args]
  (proxy [WindowListener] []
    (windowIconified [event] (apply f event args))))

(defn window-opened-listener
  "Creates a window opened listener. Calls function 'f' on window opened event."
  [f & args]
  (proxy [WindowListener] []
    (windowOpened [event] (apply f event args))))

(defn window-state-changed-listener
  "Creates a window state changed listener. Calls function 'f' on window state changed event."
  [f & args]
  (proxy [WindowListener] []
    (windowStateChanged [event] (apply f event args))))

; Add listeners
(defn add-action-listener
  "Adds an action listener to the component."
  [c ^java.awt.event.ActionListener l]
  (.addActionListener c l))

(defn add-component-listener
  "Adds a component listener to the component."
  [^java.awt.Component c ^java.awt.event.ComponentListener l]
  (.addComponentListener c l))

(defn add-focus-listener
  "Adds a focus listener to the component."
  [^java.awt.Component c ^java.awt.event.FocusListener l]
  (.addFocusListener c l))

(defn add-hierarchy-bounds-listener
  "Adds a hierarchy bounds listener to the component."
  [^java.awt.Component c ^java.awt.event.HierarchyBoundsListener l]
  (.addHierarchyBoundsListener c l))

(defn add-hierarchy-listener
  "Adds a hierarchy listener to the component."
  [^java.awt.Component c ^java.awt.event.HierarchyListener l]
  (.addHierarchyListener c l))

(defn add-input-method-listener
  "Adds a input method listener to the component."
  [^java.awt.Component c ^java.awt.event.InputMethodListener l]
  (.addInputMethodListener c l))

(defn add-item-listener
  "Adds an item listener to the component."
  [c ^java.awt.event.ItemListener l]
  (.addItemListener c l))

(defn add-key-listener
  "Adds a key listener to the component."
  [^java.awt.Component c ^java.awt.event.KeyListener l]
  (.addKeyListener c l))

(defn add-mouse-listener
  "Adds a mouse listener to the component."
  [^java.awt.Component c ^java.awt.event.MouseListener l]
  (.addMouseListener c l))

(defn add-mouse-motion-listener
  "Adds a mouse motion listener to the component."
  [^java.awt.Component c ^java.awt.event.MouseMotionListener l]
  (.addMouseMotionListener c l))

(defn add-mouse-wheel-listener
  "Adds a mouse wheel listener to the component."
  [^java.awt.Component c ^java.awt.event.MouseWheelListener l]
  (.addMouseWheelListener c l))

(defn add-property-change-listener
  "Adds a property change listener to the component."
  ([^java.awt.Component c ^java.beans.PropertyChangeListener l]
   (.addPropertyChangeListener c l))
  ([^java.awt.Component c ^java.lang.String p ^java.beans.PropertyChangeListener l]
   (.addPropertyChangeListener c p l)))

(defn add-container-listener
  "Adds a container listener to the container."
  [^java.awt.Container c ^java.awt.event.ContainerListener l]
  (.addContainerListener c l))
