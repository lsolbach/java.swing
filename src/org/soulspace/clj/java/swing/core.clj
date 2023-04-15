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

(ns org.soulspace.clj.java.swing.core
  "Functions to create Java Swing user interfaces.
   See the Java AWT and Swing API references for details."
  (:require [org.soulspace.clj.core :as fn]
            [org.soulspace.clj.java.beans :as b])
  (:import [java.awt CardLayout Color Component Container]
           [java.awt.event ActionEvent]
           [javax.swing Action AbstractAction AbstractListModel Action BorderFactory
            ButtonGroup ImageIcon InputMap InputVerifier
            JButton JCheckBox JCheckBoxMenuItem JColorChooser JComboBox JComponent JDialog
            JEditorPane JFileChooser JFormattedTextField JFrame JLabel JLayeredPane JList
            JMenu JMenuBar JMenuItem JOptionPane JPanel JPasswordField JPopupMenu JProgressBar
            JRadioButton JRadioButtonMenuItem JSeparator JScrollPane JSlider JSpinner
            JSplitPane JTabbedPane JTable JTextArea JTextField JTextPane
            JToggleButton JToolBar JTree JWindow
            KeyStroke ListSelectionModel LookAndFeel SwingConstants SwingUtilities
            UIManager WindowConstants]
           [javax.swing.border TitledBorder]
           [javax.swing.table AbstractTableModel DefaultTableCellRenderer]
           [javax.swing.text DefaultFormatter JTextComponent MaskFormatter]
           [javax.swing.tree DefaultMutableTreeNode]
           [java.net URL]
           [java.text Format NumberFormat ParseException]
           [net.miginfocom.swing MigLayout]))

;;;
;;; Constant maps
;;;

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

;;;
;;; Helpers
;;;

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

; TODO add init-menu

;;;
;;; InputVerifier
;;;

(defn input-verifier
  "Creates an input verifier with the verify function 'vf' and the yield function 'yf'."
  ^InputVerifier [vf yf & args]
  (proxy [InputVerifier] []
    (verify [component] (vf component))
    (shouldYieldFocus [component] (yf component args))))

(defn formatted-text-field-verifier
  "Creates an input verifier for a formatted text field."
  ^InputVerifier []
  (proxy [InputVerifier] []
    (verify [^JFormattedTextField c]
      (let [fmt (.getFormatter c)
            txt (.getText c)]
        (try
          (.stringToValue fmt txt)
          true
          (catch ParseException e
            false))))
    (shouldYieldFocus [^JFormattedTextField c]
      (.verify ^InputVerifier this c))))

;;;
;;; Actions
;;;

(defn action
  "Creates an action."
  (^AbstractAction [f]
   (let [action (proxy [AbstractAction] []
                  (actionPerformed [evt] (f evt)))]
     action))
  (^AbstractAction [f args]
   (let [action (proxy [AbstractAction] []
                  (actionPerformed [evt] (f evt)))]
     (doseq [[k v] args]
       ;(println (str k " : " (action-keys k) " -> " v))
       (.putValue action (action-keys k) v))
     (.setEnabled action true)
     action)))

(defn key-stroke
  "Returns the key stroke for the given key code and the modifiers if given."
  (^KeyStroke [keycode]
   (KeyStroke/getKeyStroke keycode))
  (^KeyStroke [keycode & modifiers]
   (KeyStroke/getKeyStroke keycode (reduce + (map modifier-mask-keys modifiers)))))

(defn get-input-map
  "Returns the input map of the component."
  (^InputMap [c]
   (.getInputMap c))
  (^InputMap [c k]
   (.getInputMap c k)))

(defn add-key-binding
  "Adds a key binding for an action to the input map of the component."
  ([^JComponent c ^String binding-name ^KeyStroke stroke ^Action action]
   (.put (.getActionMap c) binding-name action)
   (.put (get-input-map c) stroke binding-name))
  ([^JComponent c focus-key ^String binding-name ^KeyStroke stroke ^Action action]
   (.put (.getActionMap c) binding-name action)
   (.put (get-input-map c focus-key) stroke binding-name)))

;;;
;;; Look and feel
;;;

(defn installed-look-and-feels
  "Returns the installed look anf feels as a sequence of maps from names to class names."
  []
  (->>
    (UIManager/getInstalledLookAndFeels)
    (map (fn [^LookAndFeel lnf] [(.getName lnf) (.getClassName lnf)]))
    (reduce (fn [map [k v]] (assoc map k v)) {})))

(defn look-and-feel-available?
  "Checks the availability of the given look and feel by name."
  ([^LookAndFeel lnf]
   (look-and-feel-available? (installed-look-and-feels) lnf))
  ([installed-lnfs ^LookAndFeel lnf]
   (contains? installed-lnfs lnf)))

(defn set-look-and-feel
  "Sets the look and feel given by for the given frame (if it is available)."
  [^JFrame frame ^LookAndFeel lnf]
  (let [installed-lnfs (installed-look-and-feels)]
    (when (look-and-feel-available? installed-lnfs lnf)
      (UIManager/setLookAndFeel (installed-lnfs lnf))
      (SwingUtilities/updateComponentTreeUI frame))))

;;;
;;; Icons
;;;

(defn image-icon
  "Creates an image icon from the image data of the given URL."
  ^ImageIcon [^URL url args]
  (init-swing (ImageIcon. url) args))

;;;
;;; Borders
;;;

(defn titled-border
  "Creates a titled border."
  ^TitledBorder [^String title]
  (BorderFactory/createTitledBorder title))

;;;
;;; Component visibility
;;;

(defn show-component
  "Shows the component by setting its visibility to true."
  [^Component c]
  (.setVisible c true))

(defn hide-component
  "Hides the component by setting its visibility to false."
  [^Component c]
  (.setVisible c false))

;;;
;;; Swing Components
;;;

(defn label
  "Creates a label."
  ^JLabel [args]
  (init-swing (JLabel.) args))

;;
;; Text components
;;

(defn set-editable
  "Sets whether the user can edit the text component."
  [^JTextComponent c value]
  (.setEditable c value))

(defn editable?
  "Checks whether the user can edit text component."
  [^JTextComponent c]
  (.isEditable c))

; getter and setter for (typed) field values
(defn get-text
  "Returns the text component value as a string."
  ^String [^JTextComponent c]
  (.getText c))

(defn set-text
  "Sets the text component value to 's'."
  [^JTextComponent c ^String s]
  (.setText c s))

(defn set-value
  "Sets the formatted text field value to the formatted value 'v'."
  [^JFormattedTextField c v]
  (.setValue c v))

(defn select-all
  "Selects all characters in the text component."
  [^JTextComponent c]
  (.selectAll c))

(defn get-number
  "Returns the field value as a number."
  ^Number [^JFormattedTextField field]
  (.parse (NumberFormat/getNumberInstance) (.getText field)))

(defn get-integer
  "Returns the field value as an integer."
  ^Integer [^JFormattedTextField field]
  (.parse (NumberFormat/getIntegerInstance) (.getText field)))

(defn set-focus-lost-behaviour
  "Specifies the outcome of a field losing the focus.
   Possible values are defined in JFormattedTextField as COMMIT_OR_REVERT (the default),
   COMMIT (commit if valid, otherwise leave everything the same), PERSIST (do nothing),
   and REVERT (change the text to reflect the value)."
  [^JFormattedTextField c value]
  (.setFocusLostBehaviour c value))

(defn commit-edit
  "Sets the value to the object represented by the field's text, as determined by the field's formatter.
   If the text is invalid, the value remains the same and a ParseException is thrown."
  [^JFormattedTextField c]
  (.commitValue c))

(defn edit-valid?
  "Returns true if the formatter considers the current text to be valid, as determined by the field's formatter."
  [^JFormattedTextField c]
  (.isEditValid c))

(defn set-allows-invalid
  "Sets whether the value being edited is allowed to be invalid for a length of time."
  [^DefaultFormatter c value]
  (.setAllowsInvalid c value))

(defn allows-invalid?
  "Checks whether the value being edited is allowed to be invalid for a length of time."
  [^DefaultFormatter c]
  (.getAllowsInvalid c))

(defn to-value
  "Converts the given string s to a value using the given formatter."
  ^Object [^DefaultFormatter fmt ^String s]
  (.stringToValue fmt s))

(defn to-string
  "Converts the given value v to a string using the given formatter."
  ^String [^DefaultFormatter fmt ^Object v]
  (.valueToString fmt v))

(defn mask-formatter
  "Creates a mask formatter for the given pattern."
  ^MaskFormatter [^String s]
  (MaskFormatter. s))

(defn number-field
  "Creates a number field."
  (^JFormattedTextField []
   (JFormattedTextField. (NumberFormat/getNumberInstance)))
  (^JFormattedTextField [args]
   (init-swing (JFormattedTextField. (NumberFormat/getNumberInstance)) args)))

(defn integer-field
  "Creates an integer field."
  (^JFormattedTextField []
   (JFormattedTextField. (NumberFormat/getIntegerInstance)))
  (^JFormattedTextField [args]
   (init-swing (JFormattedTextField. (NumberFormat/getIntegerInstance)) args)))

(defn formatted-text-field
  "Creates a text field."
  (^JFormattedTextField [^Format fmt]
   (JFormattedTextField. fmt))
  (^JFormattedTextField [^Format fmt args]
   (init-swing (JFormattedTextField. fmt) args)))

(defn text-field
  "Creates a text field."
  (^JTextField []
   (JTextField.))
  (^JTextField [args]
   (init-swing (JTextField.) args)))

(defn password-field
  "Creates a password field."
  (^JPasswordField []
   (JPasswordField.))
  (^JPasswordField [args]
   (init-swing (JPasswordField.) args)))

(defn text-area
  "Creates a text area."
  (^JTextArea []
   (JTextArea.))
  (^JTextArea [args]
   (init-swing (JTextArea.) args)))

(defn editor-pane
  "Creates a editor pane."
  (^JEditorPane []
   (JEditorPane.))
  (^JEditorPane [args]
   (init-swing (JEditorPane.) args)))

(defn text-pane
  "Creates a text pane."
  (^JTextPane []
   (JTextPane.))
  (^JTextPane [args]
   (init-swing (JTextPane.) args)))

;;
;; Buttons
;;

(defn button
  "Creates a button."
  (^JButton []
   (JButton.))
  (^JButton [args]
   (init-swing (JButton.) args)))

(defn toggle-button
  "Creates a toggle button."
  (^JToggleButton []
   (JToggleButton.))
  (^JToggleButton [args]
   (init-swing (JToggleButton.) args)))

(defn check-box
  "Creates a check box."
  (^JCheckBox []
   (JCheckBox.))
  (^JCheckBox [args]
   (init-swing (JCheckBox.) args)))

(defn radio-button
  "Creates a radio button."
  (^JRadioButton []
   (JRadioButton.))
  (^JRadioButton [args]
   (init-swing (JRadioButton.) args)))

(defn button-group
  "Creates a button group."
  ^ButtonGroup [args items]
  (init-swing (ButtonGroup.) args items))

(defn slider
  "Creates a slider."
  (^JSlider []
   (JSlider.))
  (^JSlider [args]
   (init-swing (JSlider.) args)))

(defn spinner
  "Creates a spinner."
  (^JSpinner []
   (JSpinner.))
  (^JSpinner [args]
   (init-swing (JSpinner.) args)))

(defn combo-box
  "Creates a combo box."
  ^JComboBox [args items]
  (let [^JComboBox c (init-swing (JComboBox.) args)]
    (if (not (nil? items))
      (doseq [item items]
        (.addItem c item)))
    c))

(defn progress-bar
  "Creates a progress bar."
  (^JProgressBar []
   (JProgressBar.))
  (^JProgressBar [args]
   (init-swing (JProgressBar.) args)))

(defn table
  "Creates a table."
  (^JTable []
   (JTable.))
  (^JTable [args]
   (init-swing (JTable.) args)))

(defn j-list
  "Creates a swing list component."
  (^JList []
   (JList.))
  (^JList [args]
   (init-swing (JList.) args)))

(defn j-tree
  "Creates a swing tree component."
  (^JTree []
   (JTree.))
  (^JTree [args]
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
  (^JSeparator []
   (JSeparator.))
  (^JSeparator [args]
   (init-swing (JSeparator.) args)))

(defn popup-menu
  "Creates a popup menu."
  ^JPopupMenu [args items]
  (init-swing (JPopupMenu.) args items))

(defn menu-bar
  "Creates a menu bar."
  ^JMenuBar [args items]
  (init-swing (JMenuBar.) args items))

(defn menu
  "Creates a menu."
  ^JMenu [args items]
  (init-swing (JMenu.) args items))

(defn menu-item
  "Creates a menu item."
  ^JMenuItem [args]
  (init-swing (JMenuItem.) args))

(defn checkbox-menu-item
  "Creates a check box menu item."
  (^JCheckBoxMenuItem []
   (JCheckBoxMenuItem.))
  (^JCheckBoxMenuItem [args]
   (init-swing (JCheckBoxMenuItem.) args)))

(defn radio-button-menu-item
  "Creates a radio button menu item."
  (^JRadioButtonMenuItem []
   (JRadioButtonMenuItem.))
  (^JRadioButtonMenuItem [args]
   (init-swing (JRadioButtonMenuItem.) args)))

(defn panel
  "Creates a panel."
  ^JPanel [args items]
  (init-swing (JPanel.) args items))

(defn split-pane
  "Creates a split pane."
  ^JSplitPane [args items]
  (init-swing (JSplitPane.) args items))

(defn horizontal-split-pane
  "Creates a horizontal split pane."
  ^JSplitPane [args items]
  (init-swing (JSplitPane. (swing-keys :horizontal)) args items))

(defn vertical-split-pane
  "Creates a vertical split pane."
  ^JSplitPane [args items]
  (init-swing (JSplitPane. (swing-keys :vertical)) args items))

(defn scroll-pane
  "Creates a scroll pane."
  (^JScrollPane [item]
   (JScrollPane. item))
  (^JScrollPane [item args]
   (let [c (JScrollPane. item)]
     (b/set-properties! c args)
     c)))

(defn tabbed-pane
  "Creates a tabbed pane."
  ^JTabbedPane [args items]
  (let [^JTabbedPane c (init-swing (JTabbedPane.) args)]
    (if (not (nil? items))
      (doseq [[^String title ^Component component] items]
        (.addTab c title component)))
    c))

(defn layered-pane
  "Creates a layered pane."
  ^JLayeredPane [args items]
  (init-swing (JLayeredPane.) args items))

(defn tool-bar
  "Creates a tool bar."
  ^JToolBar [args items]
  (init-swing (JToolBar.) args items))

(defn canvas-panel
  "Creates a panel into which the paint function can paint using the provided graphics context."
  ^JPanel [paint-fn args items]
  (init-swing
    (proxy [javax.swing.JPanel] []
      (paintComponent [^java.awt.Graphics g]
        (let [^javax.swing.JComponent this this]
          (proxy-super paintComponent g)
          (paint-fn g))))
      args items))

(defn frame
  "Creates a frame."
  ^JFrame [args cp-items]
  (let [c (JFrame.)]
    (b/set-properties! c args)
    (if (seq cp-items)
      (doseq [^Component item cp-items]
        (.add ^Container (.getContentPane c) item)))
    c))

(defn window
  "Creates a window."
  ^JWindow [args cp-items]
  (let [c (JWindow.)]
    (b/set-properties! c args)
    (if (seq cp-items)
      (doseq [^Component item cp-items]
        (.add ^Container (.getContentPane c) item)))
    c))

(defn dialog
  "Creates a dialog. If a frame is provided, the dialog is centered on the frame."
  (^JDialog [args cp-items]
   (let [c (JDialog.)]
     (b/set-properties! c args)
     (if (seq cp-items)
       (doseq [^Component item cp-items]
         (.add (.getContentPane c) item)))
     (.pack c)
     c))
  (^JDialog [parent args cp-items]
   (let [c (JDialog. parent)]
     (b/set-properties! c args)
     (if (seq cp-items)
       (doseq [^Component item cp-items]
         (.add (.getContentPane c) item)))
     (.pack c)
     (.setLocationRelativeTo c parent)
     c)))

;;
;; standard dialogs
;;
;; TODO add frame parameter to the dialogs
;;
(defn file-open-dialog
  "Returns the filename to open or nil if the dialog was aborted."
  ([^String filename]
   (let [d (JFileChooser. filename)]
     (let [state (.showOpenDialog d nil)]
       (if (= state JFileChooser/APPROVE_OPTION)
         (.getSelectedFile d)
         nil)))))

(defn file-save-dialog
  "Returns the filename to save or nil if the dialog was aborted."
  ([^String filename]
   (let [d (JFileChooser. filename)]
     (let [state (.showSaveDialog d nil)]
       (if (= state JFileChooser/APPROVE_OPTION)
         (.getSelectedFile d)
         nil)))))

(defn color-choose-dialog
  "Creates a color choose dialog."
  (^Color [^Component c ^String title ^Color color]
   (JColorChooser/showDialog c title color)))

(defn message-dialog
  "Creates a message dialog."
  ([^String text]
   (JOptionPane/showMessageDialog nil text))
  ([^String text ^String title type]
   (JOptionPane/showMessageDialog
     nil text title (option-pane-message-keys type)))
  ([^String text ^String title type icon]
   (JOptionPane/showMessageDialog
     nil text title (option-pane-message-keys type) icon)))

(defn confirm-dialog
  "Creates a confirm dialog."
  ([^String text ^String title options]
   (JOptionPane/showConfirmDialog nil text title options))
  ([^String text ^String title options type]
   (JOptionPane/showConfirmDialog
     nil text title options (option-pane-message-keys type)))
  ([^String text ^String title options type icon]
   (JOptionPane/showConfirmDialog
     nil text title options (option-pane-message-keys type) icon)))

(defn input-dialog
  "Creates an input dialog."
  ([^String text]
   (JOptionPane/showInputDialog text))
  ([^String text ^String title]
   (JOptionPane/showInputDialog nil text title))
  ([^String text ^String title type]
   (JOptionPane/showInputDialog
     nil text title (option-pane-message-keys type)))
  ([^String text ^String title type icon values initial]
   (JOptionPane/showInputDialog
     nil text title (option-pane-message-keys type) icon (into-array values) initial)))

(defn option-pane
  "Creates an option pane dialog."
  (^JOptionPane []
   (JOptionPane.))
  (^JOptionPane [args]
   (init-swing (JOptionPane.) args)))

; Renderer
(defn table-cell-renderer
  "Creates a cell renderer with the render function 'rf'."
  (^DefaultTableCellRenderer [rf args]
   (proxy [DefaultTableCellRenderer] []
     (getTableCellRendererComponent [table value isSelected hasFocus row column]
       (let [result (proxy-super getTableCellRendererComponent table value isSelected hasFocus row column)]
         (b/set-properties! this args)
         (.setText this ^String (rf value))
         result)))))


; mapseq ColumnSpec
;[{:label "Text" :key :text :editable false :converter function}]

;;;
;;; Default models
;;;

(defn- get-data
  "Access data for the default models."
  [data path]
  (if (fn/reftype? data)
    (if (seq path)
      (get-in @data path)
      @data)
    (if (seq path)
      (get-in data path)
      data)))

(defn mapseq-table-model
  "Creates a table model backed with a collection of maps.
   The given 'data' can be a data structure or a ref type (e.g. an atom).
   If a vector of keys is given as 'path', the collection of maps is looked up by this keys in data.

   The col-spec map may contain the keys:

   :label - the label of the column
 
   :key - the key of the value for the column in the date

   :editable - if the column is editable
  
   :converter - an optional converter function for the value"
  ([col-spec data]
   (mapseq-table-model col-spec data nil))
  ([col-spec data path]
   (proxy [AbstractTableModel] []
     (getColumnCount [] (count col-spec))
     (getRowCount [] (count (get-data data path)))
     (isCellEditable [_ col] (:edit (nth col-spec col) false))
     (getColumnName [col] (:label (nth col-spec col) (str "Label " col)))
     (getValueAt [row col] ((:converter (nth col-spec col) identity)
                            ((:key (nth col-spec col))
                             (nth (get-data data path) row)))))))

; TODO add converter like in mapseq-table-model?
(defn seq-list-model
  "Creates a list model backed with the 'data' sequence.
 
   The given 'data' can be a data structure or a ref type (e.g. an atom).
   If a vector of keys is given as 'path', the sequence is looked up by this keys in data."
  ([data]
   (seq-list-model data nil))
  ([data path]
   (proxy [AbstractListModel] []
     (getElementAt [idx] (nth (get-data data path) idx))
     (getSize [] (count (get-data data path))))))

; DefaultMutableTreeNode
(defn tree-node
  "Creates a tree node."
  ^DefaultMutableTreeNode [obj args items]
  (init-swing (DefaultMutableTreeNode. obj) args items))

;;
;; Layouts
;;

;; CardLayout
(defn card-layout
  "Creates a card layout."
  ^CardLayout [args items]
  (init-swing (CardLayout.) args items))

(defn first-card
  "Shows the first card on the container."
  [^CardLayout cl ^Container container]
  (.first cl container))

(defn next-card
  "Shows the next card on the container."
  [^CardLayout cl ^Container container]
  (.next cl container))

(defn previous-card
  "Shows the previous card on the container."
  [^CardLayout cl ^Container container]
  (.previous cl container))

(defn last-card
  "Shows the last card on the container."
  [^CardLayout cl ^Container container]
  (.last cl container))

(defn show-card
  "Shows the card on the container with the given name."
  [^CardLayout cl ^Container container ^String name]
  (.show cl container name))

;; MigLayout
(defn mig-layout
  "Creates a mig layout."
  ^MigLayout [args]
  (init-swing (MigLayout.) args))
