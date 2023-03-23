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

(ns org.soulspace.clj.java.print
  "Functions for the Java Print API."
  (:import [java.awt Graphics]
           [java.awt.print PrinterJob PageFormat Printable]
           [java.io OutputStream]
           [javax.print Doc DocFlavor DocFlavor$BYTE_ARRAY DocFlavor$CHAR_ARRAY DocFlavor$INPUT_STREAM
                        DocFlavor$READER DocFlavor$SERVICE_FORMATTED DocFlavor$STRING DocFlavor$URL
                        DocPrintJob PrintService PrintServiceLookup ServiceUI ServiceUIFactory SimpleDoc
                        StreamPrintService StreamPrintServiceFactory]
           [javax.print.attribute Attribute AttributeSet HashPrintRequestAttributeSet]
           [javax.print.attribute.standard MediaSize MediaSizeName]))

(def doc-flavors {:byte-array        DocFlavor$BYTE_ARRAY
                  :char-array        DocFlavor$CHAR_ARRAY
                  :input-stream      DocFlavor$INPUT_STREAM
                  :reader            DocFlavor$READER
                  :service-formatted DocFlavor$SERVICE_FORMATTED
                  :string            DocFlavor$STRING
                  :url               DocFlavor$URL})

(defn mime-type
  "Returns the mime type of the doc flavor."
  [^DocFlavor doc-flavor]
  (.getMimeType doc-flavor))

(defn add-attribute
  "Adds the attribute to the attribute set."
  [^AttributeSet attr-set ^Attribute attr]
  (.add attr-set attr))

(defn print-request-attribute-set
  "Creates a print request attribute set and adds the attributes if given."
  (^AttributeSet []
    (HashPrintRequestAttributeSet.))
  (^AttributeSet [coll]
    (let [attr-set (print-request-attribute-set)]
      (doseq [attr coll]
        (add-attribute attr-set attr))
      attr-set)))

(defn printable
  "Creates a Printable object."
  (^Printable [f]
    (proxy [Printable] []
      (print [^Graphics gfx ^PageFormat page-format page-idx]
        (f gfx page-format page-idx)))))

(defn lookup-stream-print-service-factories
  "Returns the stream print service factories."
  [doc-flavor mime-type]
  (StreamPrintServiceFactory/lookupStreamPrintServiceFactories doc-flavor mime-type))

(defn get-stream-print-service
  "Returns the stream print service from the stream print service factory for the output stream."
  [^StreamPrintServiceFactory factory ^OutputStream out]
  (.getPrintService factory out))

(defn lookup-default-print-service
  "Returns the default print service if it exists."
  []
  (PrintServiceLookup/lookupDefaultPrintService))

(defn lookup-print-services
  "Returns the print services compatible with the doc flavour and the attribute set."
  ([^DocFlavor doc-flavor ^AttributeSet attr-set]
   (PrintServiceLookup/lookupPrintServices doc-flavor attr-set)))

(defn print-dialog
  "Shows the print dialog and returns the print service. If no print service is returned, the print must be cancelled."
  ([x y #^PrintService print-services ^PrintService default-print-service ^AttributeSet attr-set]
    ; TODO lookup api signatures
   (ServiceUI/printDialog nil x y print-services default-print-service nil attr-set)))

(defn create-print-job
  "Creates a print job on the print service."
  ^DocPrintJob [^PrintService print-service]
  (.createPrintJob print-service))

(defn simple-doc
  "Creates a simple doc for printing."
  (^ SimpleDoc [^Object data ^DocFlavor doc-flavor ^AttributeSet attr-set]
    (SimpleDoc. print doc-flavor attr-set)))

(defn print-document
  "Print the document with the specified attribute set on the print job."
  [^DocPrintJob print-job ^Doc doc ^AttributeSet attr-set]
  (.print print-job doc attr-set))

(defn print-data
  "Print the data with the specified attribute set on the print service."
  [^PrintService print-service ^Object data ^DocFlavor doc-flavor ^AttributeSet attr-set]
  (let [print-job (create-print-job print-service)
        doc (simple-doc data doc-flavor attr-set)]
    (print-document print-job doc attr-set)))
