;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.java.swing.swinglib
  (:require [org.soulspace.clj.function :as fn]
            [org.soulspace.clj.java.beans :as b]
            [org.soulspace.clj.java.swing.constants :as sc])
  (:import [java.awt CardLayout]
           [javax.swing AbstractAction AbstractListModel Action BorderFactory ButtonGroup
            ImageIcon InputVerifier
            JButton JCheckBox JCheckBoxMenuItem JColorChooser JComboBox JDesktopPane JDialog
            JEditorPane JFileChooser JFormattedTextField JFrame JLabel JLayeredPane JList
            JMenu JMenuBar JMenuItem JOptionPane JPanel JPasswordField JPopupMenu JProgressBar
            JRadioButton JRadioButtonMenuItem JSeparator JScrollPane JSlider JSpinner
            JSplitPane JTabbedPane JTable JTextArea JTextField JTextPane JToggleButton JToolBar
            JTree JWindow
            KeyStroke SwingConstants SwingUtilities UIManager]
           [javax.swing.event AncestorListener CaretListener CellEditorListener ChangeListener DocumentListener
            HyperlinkListener InternalFrameListener ListDataListener ListSelectionListener
            MenuDragMouseListener MenuKeyListener MenuListener MouseInputListener PopupMenuListener
            RowSorterListener TableColumnModelListener TableModelListener
            TreeExpansionListener TreeModelListener TreeSelectionListener TreeWillExpandListener
            UndoableEditListener]
           [javax.swing.table AbstractTableModel DefaultTableCellRenderer]
           [javax.swing.text MaskFormatter]
           [javax.swing.tree DefaultMutableTreeNode]
           [java.text DateFormat Format NumberFormat ParseException]
           [net.miginfocom.swing MigLayout]))

;;
;; Functions to create Swing UIs
;;

; Helpers
(defn init-swing
  "Intitializes a swing component with the arguments and items."
  ([c args]
   (if (seq args)
     (b/set-properties! c args))
   c)
  ([c args items]
   (if (seq args)
     (b/set-properties! c args))
   (if (seq items)
     (doseq [item items]
       (if (vector? item)
         ; vector contains child and constraints for it
         (let [[child constraint] item]
           (.add c child constraint))
         ; child without constraints
         (.add c item))))
   c))

; InputVerifier
(defn input-verifier
  "Creates an input verifier with the verify function 'vf' and the yield function 'yf'."
  [vf yf & args]
  (proxy [InputVerifier] []
    (verify [component] (vf component))
    (shouldYieldFocus [component] (yf component args))))

(defn formatted-text-field-verifier
  "Creates an input verifier for a formatted text field."
  []
  (proxy [InputVerifier] []
    (verify [c]
      (let [fmt (.getFormatter c)
            txt (.getText c)]
        (try
          (.stringToValue fmt txt)
          true
          (catch ParseException e
            false))))
    (shouldYieldFocus [c]
      (.verify this c))))

; Actions
(defn action
  "Creates an action."
  ([f]
   (let [action (proxy [AbstractAction] []
                  (actionPerformed [evt] (f evt)))]
     action))
  ([f args]
   (let [action (proxy [AbstractAction] []
                  (actionPerformed [evt] (f evt)))]
     (doseq [[k v] args]
       ;(println (str k " : " (action-keys k) " -> " v))
       (.putValue action (sc/action-keys k) v))
     (.setEnabled action true)
     action)))

(defn key-stroke
  "Returns the key stroke for the given key code and the modifiers if given."
  ([keycode]
   (KeyStroke/getKeyStroke keycode))
  ([keycode & modifiers]
   (KeyStroke/getKeyStroke keycode (reduce + (map sc/modifier-mask-keys modifiers)))))

(defn get-input-map
  "Returns the input map of the component."
  ([c]
   (.getInputMap c))
  ([c k]
   (.getInputMap c k)))

(defn add-key-binding
  "Adds a key binding for an action to the input map of the component."
  ([c binding-name stroke action]
   (.put (.getActionMap c) binding-name action)
   (.put (get-input-map c) stroke binding-name))
  ([c focus-key binding-name stroke action]
   (.put (.getActionMap c) binding-name action)
   (.put (get-input-map c focus-key) stroke binding-name)))

(defn installed-look-and-feels
  "Returns the installed look anf feels as a sequence of maps from names to class names."
  []
  (->>
    (UIManager/getInstalledLookAndFeels)
    (map (fn [lnf] [(.getName lnf) (.getClassName lnf)]))
    (reduce (fn [map [k v]] (assoc map k v)) {})))

(defn look-and-feel-available?
  "Checks the availability of the given look and feel by name."
  ([lnf]
   (look-and-feel-available? (installed-look-and-feels) lnf))
  ([installed-lnfs lnf]
   (contains? installed-lnfs lnf)))

; Look and feel
(defn set-look-and-feel
  "Sets the look and feel given by for the given frame (if it is available)."
  [frame lnf]
  (let [installed-lnfs (installed-look-and-feels)]
    (when (look-and-feel-available? installed-lnfs lnf)
      (UIManager/setLookAndFeel (installed-lnfs lnf))
      (SwingUtilities/updateComponentTreeUI frame))))

; Icons
(defn image-icon
  "Creates an image icon from the image data of the given URL."
  [url args]
  (init-swing (ImageIcon. url) args))

; Borders
(defn titled-border
  "Creates a titled border."
  [title]
  (BorderFactory/createTitledBorder title))

(defn show-component
  "Shows the component by setting its visibility to true."
  [c]
  (.setVisible c true))

(defn hide-component
  "Hides the component by setting its visibility to false."
  [c]
  (.setVisible c false))


; Swing Components
(defn label
  "Creates a label."
  [args]
  (init-swing (JLabel.) args))

; JTextComponent
(defn set-editable
  "Sets whether the user can edit the text component."
  [c value]
  (.setEditable c value))

(defn editable?
  "Checks whether the user can edit text component."
  [c]
  (.isEditable c))

; getter and setter for (typed) field values
(defn get-text
  "Returns the text component value as a string."
  [c]
  (.getText c))

(defn set-text
  "Sets the text component value to 's'."
  [c s]
  (.setText c s))

(defn set-value
  "Sets the formatted text field value to the formatted value 'v'."
  [c v]
  (.setValue c v))

(defn select-all
  "Selects all characters in the text component."
  [c]
  (.selectAll c))

(defn get-number
  "Returns the field value as a number."
  [field]
  (.parse (NumberFormat/getNumberInstance) (.getText field)))

(defn get-integer
  "Returns the field value as an integer."
  [field]
  (.parse (NumberFormat/getIntegerInstance) (.getText field)))

(defn set-focus-lost-behaviour
  "Specifies the outcome of a field losing the focus.
   Possible values are defined in JFormattedTextField as COMMIT_OR_REVERT (the default),
   COMMIT (commit if valid, otherwise leave everything the same), PERSIST (do nothing),
   and REVERT (change the text to reflect the value)."
  [c value]
  (.setFocusLostBehaviour c value))

(defn commit-edit
  "Sets the value to the object represented by the field's text, as determined by the field's formatter.
   If the text is invalid, the value remains the same and a ParseException is thrown."
  [c]
  (.commitValue c))

(defn edit-valid?
  "Returns true if the formatter considers the current text to be valid, as determined by the field's formatter."
  [c]
  (.isEditValid c))

(defn set-allows-invalid
  "Sets whether the value being edited is allowed to be invalid for a length of time."
  [c value]
  (.setAllowsInvalid c))

(defn allows-invalid?
  "Checks whether the value being edited is allowed to be invalid for a length of time."
  [c]
  (.getAllowsInvalid c))

(defn to-value
  "Converts the given string s to a value using the given formatter."
  [fmt s]
  (.stringToValue fmt s))

(defn to-string
  "Converts the given value v to a string using the given formatter."
  [fmt v]
  (.valueToString fmt v))

(defn mask-formatter
  "Creates a mask formatter for the given pattern."
  [s]
  (MaskFormatter. s))

(defn number-field
  "Creates a number field."
  ([]
   (JFormattedTextField. (NumberFormat/getNumberInstance)))
  ([args]
   (init-swing (JFormattedTextField. (NumberFormat/getNumberInstance)) args)))

(defn integer-field
  "Creates an integer field."
  ([]
   (JFormattedTextField. (NumberFormat/getIntegerInstance)))
  ([args]
   (init-swing (JFormattedTextField. (NumberFormat/getIntegerInstance)) args)))

(defn formatted-text-field
  "Creates a text field."
  ([^Format fmt]
   (JFormattedTextField. fmt))
  ([^Format fmt args]
   (init-swing (JFormattedTextField. fmt) args)))

(defn text-field
  "Creates a text field."
  ([]
   (JTextField.))
  ([args]
   (init-swing (JTextField.) args)))

(defn password-field
  "Creates a password field."
  ([]
   (JPasswordField.))
  ([args]
   (init-swing (JPasswordField.) args)))

(defn text-area
  "Creates a text area."
  ([]
   (JTextArea.))
  ([args]
   (init-swing (JTextArea.) args)))

(defn editor-pane
  "Creates a editor pane."
  ([]
   (JEditorPane.))
  ([args]
   (init-swing (JEditorPane.) args)))

(defn text-pane
  "Creates a text pane."
  ([]
   (JTextPane.))
  ([args]
   (init-swing (JTextPane.) args)))

(defn button
  "Creates a button."
  ([]
   (JButton.))
  ([args]
   (init-swing (JButton.) args)))

(defn toggle-button
  "Creates a toggle button."
  ([]
   (JToggleButton.))
  ([args]
   (init-swing (JToggleButton.) args)))

(defn check-box
  "Creates a check box."
  ([]
   (JCheckBox.))
  ([args]
   (init-swing (JCheckBox.) args)))

(defn radio-button
  "Creates a radio button."
  ([]
   (JRadioButton.))
  ([args]
   (init-swing (JRadioButton.) args)))

(defn button-group
  "Creates a button group."
  [args items]
  (init-swing (ButtonGroup.) args items))

(defn slider
  "Creates a slider."
  ([]
   (JSlider.))
  ([args]
   (init-swing (JSlider.) args)))

(defn spinner
  "Creates a spinner."
  ([]
   (JSpinner.))
  ([args]
   (init-swing (JSpinner.) args)))

(defn combo-box
  "Creates a combo box."
  [args items]
  (let [c (init-swing (JComboBox.) args)]
    (if (not (nil? items))
      (doseq [item items]
        (.addItem c item)))
    c))

(defn progress-bar
  "Creates a progress bar."
  ([]
   (JProgressBar.))
  ([args]
   (init-swing (JProgressBar.) args)))

(defn table
  "Creates a table."
  ([]
   (JTable.))
  ([args]
   (init-swing (JTable.) args)))

(defn j-list
  "Creates a swing list component."
  ([]
   (JList.))
  ([args]
   (init-swing (JList.) args)))

(defn j-tree
  "Creates a swing tree component."
  ([]
   (JTree.))
  ([args]
   (init-swing (JTree.) args)))

(defn get-selection-model
  "Returns the selection model of the component (e.g. table or list)."
  [c]
  (.getSelectionModel c))

(defn set-selection-mode
  "Sets the selection mode."
  [model mode]
  (.setSelectionMode model mode))

(defn separator
  "Creates a separator."
  ([]
   (JSeparator.))
  ([args]
   (init-swing (JSeparator.) args)))

(defn popup-menu
  "Creates a popup menu."
  [args items]
  (init-swing (JPopupMenu.) args items))

(defn menu-bar
  "Creates a menu bar."
  [args items]
  (init-swing (JMenuBar.) args items))

(defn menu
  "Creates a menu."
  [args items]
  (init-swing (JMenu.) args items))

(defn menu-item
  "Creates a menu item."
  [args]
  (init-swing (JMenuItem.) args))

(defn checkbox-menu-item
  "Creates a check box menu item."
  ([]
   (JCheckBoxMenuItem.))
  ([args]
   (init-swing (JCheckBoxMenuItem.) args)))

(defn radio-button-menu-item
  "Creates a radio button menu item."
  ([]
   (JRadioButtonMenuItem.))
  ([args]
   (init-swing (JRadioButtonMenuItem.) args)))

(defn panel
  "Creates a panel."
  [args items]
  (init-swing (JPanel.) args items))

(defn split-pane
  "Creates a split pane."
  [args items]
  (init-swing (JSplitPane.) args items))

(defn horizontal-split-pane
  "Creates a horizontal split pane."
  [args items]
  (init-swing (JSplitPane. (sc/swing-keys :horizontal)) args items))

(defn vertical-split-pane
  "Creates a vertical split pane."
  [args items]
  (init-swing (JSplitPane. (sc/swing-keys :vertical)) args items))

(defn scroll-pane
  "Creates a scroll pane."
  ([item]
   (JScrollPane. item))
  ([item args]
   (let [c (JScrollPane. item)]
     (b/set-properties! c args)
     c)))

(defn tabbed-pane
  "Creates a tabbed pane."
  [args items]
  (let [ c (init-swing (JTabbedPane.) args)]
    (if (not (nil? items))
      (doseq [[title component] items]
        (.addTab c title component)))
    c))

(defn layered-pane
  "Creates a layered pane."
  [args items]
  (init-swing (JLayeredPane.) args items))

(defn tool-bar
  "Creates a tool bar."
  [args items]
  (init-swing (JToolBar.) args items))

(defn canvas-panel
  "Creates a panel into which the paint function can paint using the provided graphics context."
  [paint-fn args items]
  (init-swing
    (proxy [javax.swing.JPanel] []
      (paintComponent [^java.awt.Graphics g]
        (proxy-super paintComponent g)
        (paint-fn g)))
    args items))

(defn frame
  "Creates a frame."
  [args cp-items]
  (let [c (JFrame.)]
    (b/set-properties! c args)
    (if (seq cp-items)
      (doseq [item cp-items]
        (.add (.getContentPane c) item)))
    c))

(defn window
  "Creates a window."
  [args cp-items]
  (let [c (JWindow.)]
    (b/set-properties! c args)
    (if (seq cp-items)
      (doseq [item cp-items]
        (.add (.getContentPane c) item)))
    c))

(defn dialog
  "Creates a dialog. If a frame is provided, the dialog is centered on the frame."
  ([args cp-items]
   (let [c (JDialog.)]
     (b/set-properties! c args)
     (if (seq cp-items)
       (doseq [item cp-items]
         (.add (.getContentPane c) item)))
     (.pack c)
     c))
  ([frame args cp-items]
   (let [c (JDialog. frame)]
     (b/set-properties! c args)
     (if (seq cp-items)
       (doseq [item cp-items]
         (.add (.getContentPane c) item)))
     (.pack c)
     (.setLocationRelativeTo c frame)
     c)))

;;
;; standard dialogs
;; TODO add frame parameter to the dialogs
;;
(defn file-open-dialog
  "Returns the filename to open or nil if the dialog was aborted."
  ([filename]
   (let [d (JFileChooser. filename)]
     (let [state (.showOpenDialog d nil)]
       (if (= state JFileChooser/APPROVE_OPTION)
         (.getSelectedFile d)
         nil)))))

(defn file-save-dialog
  "Returns the filename to save or nil if the dialog was aborted."
  ([filename]
   (let [d (JFileChooser. filename)]
     (let [state (.showSaveDialog d nil)]
       (if (= state JFileChooser/APPROVE_OPTION)
         (.getSelectedFile d)
         nil)))))

(defn color-choose-dialog
  "Creates a color choose dialog."
  ([c title color]
   (JColorChooser/showDialog c title color)))

(defn message-dialog
  "Creates a message dialog."
  ([text]
   (JOptionPane/showMessageDialog nil text))
  ([text title type]
   (JOptionPane/showMessageDialog
     nil text title (sc/option-pane-message-keys type)))
  ([text title type icon]
   (JOptionPane/showMessageDialog
     nil text title (sc/option-pane-message-keys type) icon)))

(defn confirm-dialog
  "Creates a confirm dialog."
  ([text title options]
   (JOptionPane/showConfirmDialog nil text title options))
  ([text title options type]
   (JOptionPane/showConfirmDialog
     nil text title options (sc/option-pane-message-keys type)))
  ([text title options type icon]
   (JOptionPane/showConfirmDialog
     nil text title options (sc/option-pane-message-keys type) icon)))

(defn input-dialog
  "Creates an input dialog."
  ([text]
   (JOptionPane/showInputDialog text))
  ([text title]
   (JOptionPane/showInputDialog nil text title))
  ([text title type]
   (JOptionPane/showInputDialog
     nil text title (sc/option-pane-message-keys type)))
  ([text title type icon values initial]
   (JOptionPane/showInputDialog
     nil text title (sc/option-pane-message-keys type) icon (into-array values) initial)))

(defn option-pane
  "Creates an option pane dialog."
  ([]
   (JOptionPane.))
  ([args]
   (init-swing (JOptionPane.) args)))

; Renderer
(defn table-cell-renderer
  "Creates a cell renderer with the render function 'rf'."
  ([rf args]
   (proxy [DefaultTableCellRenderer] []
     (getTableCellRendererComponent [table value isSelected hasFocus row column]
       (let [result (proxy-super getTableCellRendererComponent table value isSelected hasFocus row column)]
         (b/set-properties! this args)
         (.setText this (rf value))
         result)))))


; mapseq ColumnSpec
;[{:label "Text" :key :text :edit false :converter function}]

; TODO use labeled keyword arguments instead of a map
; Default models
(defn mapseq-table-model
  "Creates a table model backed with a sequence of maps. The col-spec map may contain "
  [col-spec data]
  (proxy [AbstractTableModel] []
    (getColumnCount [] (count col-spec))
    (getRowCount [] (if (fn/reftype? data)
                      (count @data)
                      (count data)))
    (isCellEditable [_ col] (:edit (nth col-spec col) false))
    (getColumnName [col] (:label (nth col-spec col) (str "Label " col)))
    (getValueAt [row col] ((:converter (nth col-spec col) identity)
                           ((:key (nth col-spec col))
                             (if (fn/reftype? data)
                               (nth @data row)
                               (nth data row)))))))

; TODO add converter like in mapseq-table-model?
(defn seq-list-model
  "Creates a list model backed with the 'data' sequence."
  ([data]
   (proxy [AbstractListModel] []
     (getElementAt [idx] (if (fn/reftype? data)
                           (nth @data idx)
                           (nth data idx)))
     (getSize [] (if (fn/reftype? data)
                   (count @data)
                   (count data))))))

; DefaultMutableTreeNode
(defn tree-node
  "Creates a tree node."
  [obj args items]
  (init-swing (DefaultMutableTreeNode. obj) args items))

;;
;; Layouts
;;

;; CardLayout
(defn card-layout
  "Creates a card layout."
  [args items]
  (init-swing (CardLayout.) args items))

(defn first-card
  "Shows the first card on the container."
  [cl container]
  (.first cl container))

(defn next-card
  "Shows the next card on the container."
  [cl container]
  (.next cl container))

(defn previous-card
  "Shows the previous card on the container."
  [cl container]
  (.previous cl container))

(defn last-card
  "Shows the last card on the container."
  [cl container]
  (.last cl container))

(defn show-card
  "Shows the card on the container with the given name."
  [cl container name]
  (.show cl container name))

;; MigLayout
(defn mig-layout
  "Creates a mig layout."
  [args]
  (init-swing (MigLayout.) args))
