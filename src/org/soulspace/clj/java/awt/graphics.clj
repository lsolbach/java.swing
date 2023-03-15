;;
;;   Copyright (c) Ludger Solbach. All rights reserved.
;;   The use and distribution terms for this software are covered by the
;;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;;   which can be found in the file license.txt at the root of this distribution.
;;   By using this software in any fashion, you are agreeing to be bound by
;;   the terms of this license.
;;   You must not remove this notice, or any other, from this software.
;;

(ns org.soulspace.clj.java.awt.graphics
  (:import [java.awt BasicStroke Color Dimension Font GradientPaint Graphics2D Image Point RenderingHints TexturePaint]
           [java.awt.geom Arc2D$Double CubicCurve2D$Double Dimension2D Ellipse2D$Double
            Line2D$Double Point2D$Double Rectangle2D$Double RoundRectangle2D$Double QuadCurve2D$Double]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]))

;;
;; Functions for creating graphics with AWT
;;

;;
;; Constant maps
;;
(def stroke-cap-styles {:cap_butt BasicStroke/CAP_BUTT
                        :cap_round BasicStroke/CAP_ROUND
                        :cap_square BasicStroke/CAP_SQUARE})

(def stroke-join-styles {:join_miter BasicStroke/JOIN_MITER
                         :join_round BasicStroke/JOIN_ROUND
                         :join_bevel BasicStroke/JOIN_BEVEL})

(def rendering-hint-keys {:alpha-interpolation RenderingHints/KEY_ALPHA_INTERPOLATION ; Alpha interpolation hint key.
                          :antialialising RenderingHints/KEY_ANTIALIASING ; Antialiasing hint key.
                          :color-rendering RenderingHints/KEY_COLOR_RENDERING ; Color rendering hint key.
                          :dithering RenderingHints/KEY_DITHERING ; Dithering hint key.
                          :fractional-metrics RenderingHints/KEY_FRACTIONALMETRICS ; Font fractional metrics hint key.
                          :interpolation RenderingHints/KEY_INTERPOLATION ; Interpolation hint key.
                          :rendering RenderingHints/KEY_RENDERING ; Rendering hint key.
                          :stroke-control RenderingHints/KEY_STROKE_CONTROL ; Stroke normalization control hint key.
                          :text-antialialising RenderingHints/KEY_TEXT_ANTIALIASING ; Text antialiasing hint key.
                          :text-lcd-contrast RenderingHints/KEY_TEXT_LCD_CONTRAST}) ; LCD text contrast rendering hint key.


(def alpha-interpolation-hints {:default RenderingHints/VALUE_ALPHA_INTERPOLATION_DEFAULT ; alpha blending algorithms are chosen by the implementation for a good tradeoff of performance vs.
                                :quality RenderingHints/VALUE_ALPHA_INTERPOLATION_QUALITY ; alpha blending algorithms are chosen with a preference for precision and visual quality.
                                :speed RenderingHints/VALUE_ALPHA_INTERPOLATION_SPEED}) ; alpha blending algorithms are chosen with a preference for calculation speed.

(def antialias-hints {:default RenderingHints/VALUE_ANTIALIAS_DEFAULT ; rendering is done with a default antialiasing mode chosen by the implementation.
                      :off RenderingHints/VALUE_ANTIALIAS_OFF ; rendering is done without antialiasing.
                      :on RenderingHints/VALUE_ANTIALIAS_ON}) ; rendering is done with antialiasing.


(def color-render-hints {:default RenderingHints/VALUE_COLOR_RENDER_DEFAULT ; perform color conversion calculations as chosen by the implementation to represent the best available tradeoff between performance and accuracy.
                         :quality RenderingHints/VALUE_COLOR_RENDER_QUALITY ; perform the color conversion calculations with the highest accuracy and visual quality.
                         :speed RenderingHints/VALUE_COLOR_RENDER_SPEED}) ; perform the fastest color conversion to the format of the output device.


(def dither-hints {:default RenderingHints/VALUE_DITHER_DEFAULT ; use a default for dithering chosen by the implementation.
                   :disable RenderingHints/VALUE_DITHER_DISABLE ; do not dither when rendering geometry.
                   :enable RenderingHints/VALUE_DITHER_ENABLE}) ; dither when rendering geometry, if needed.


(def fractional-metrics-hints {:default RenderingHints/VALUE_FRACTIONALMETRICS_DEFAULT ; character glyphs are positioned with accuracy chosen by the implementation.
                               :off RenderingHints/VALUE_FRACTIONALMETRICS_OFF ; character glyphs are positioned with advance widths rounded to pixel boundaries.
                               :on RenderingHints/VALUE_FRACTIONALMETRICS_ON}) ; character glyphs are positioned with sub-pixel accuracy.


(def interpolation-hints {:bicubic RenderingHints/VALUE_INTERPOLATION_BICUBIC ; the color samples of 9 nearby integer coordinate samples in the image are interpolated using a cubic function in both X and Y to produce a color sample.
                          :bilinear RenderingHints/VALUE_INTERPOLATION_BILINEAR ; the color samples of the 4 nearest neighboring integer coordinate samples in the image are interpolated linearly to produce a color sample.
                          :nearest-neighbor RenderingHints/VALUE_INTERPOLATION_NEAREST_NEIGHBOR}) ; the color sample of the nearest neighboring integer coordinate sample in the image is used.


(def render-hints {:default RenderingHints/VALUE_RENDER_DEFAULT ; rendering algorithms are chosen by the implementation for a good tradeoff of performance vs.
                   :quality RenderingHints/VALUE_RENDER_QUALITY ; rendering algorithms are chosen with a preference for output quality.
                   :speed RenderingHints/VALUE_RENDER_SPEED}) ; rendering algorithms are chosen with a preference for output speed.


(def stroke-hints {:default RenderingHints/VALUE_STROKE_DEFAULT ; geometry may be modified or left pure depending on the tradeoffs in a given implementation.
                   :normalize RenderingHints/VALUE_STROKE_NORMALIZE ; geometry should be normalized to improve uniformity or spacing of lines and overall aesthetics.
                   :pure RenderingHints/VALUE_STROKE_PURE}) ; geometry should be left unmodified and rendered with sub-pixel accuracy.


(def text-antialias-hints {:default RenderingHints/VALUE_TEXT_ANTIALIAS_DEFAULT ; text rendering is done according to the KEY_ANTIALIASING hint or a default chosen by the implementation.
                           :gasp RenderingHints/VALUE_TEXT_ANTIALIAS_GASP ; text rendering is requested to use information in the font resource which specifies for each point size whether to apply VALUE_TEXT_ANTIALIAS_ON or VALUE_TEXT_ANTIALIAS_OFF.
                           :lcd-hbgr RenderingHints/VALUE_TEXT_ANTIALIAS_LCD_HBGR ; request that text be displayed optimised for an LCD display with subpixels in order from display left to right of B,G,R such that the horizontal subpixel resolution is three times that of the full pixel horizontal resolution (HBGR).
                           :lcd-hrgb RenderingHints/VALUE_TEXT_ANTIALIAS_LCD_HRGB ; request that text be displayed optimised for an LCD display with subpixels in order from display left to right of R,G,B such that the horizontal subpixel resolution is three times that of the full pixel horizontal resolution (HRGB).
                           :lcd-vbgr RenderingHints/VALUE_TEXT_ANTIALIAS_LCD_VBGR ; request that text be displayed optimised for an LCD display with subpixel organisation from display top to bottom of B,G,R such that the vertical subpixel resolution is three times that of the full pixel vertical resolution (VBGR).
                           :lcd-vrgb RenderingHints/VALUE_TEXT_ANTIALIAS_LCD_VRGB ; request that text be displayed optimised for an LCD display with subpixel organisation from display top to bottom of R,G,B such that the vertical subpixel resolution is three times that of the full pixel vertical resolution (VRGB).
                           :off RenderingHints/VALUE_TEXT_ANTIALIAS_OFF ; text rendering is done without any form of antialiasing.
                           :on RenderingHints/VALUE_TEXT_ANTIALIAS_ON}) ; text rendering is done with some form of antialiasing.


(def image-type {:rgb BufferedImage/TYPE_INT_RGB ; Represents an image with 8-bit RGB color components packed into integer pixels.
                 :argb BufferedImage/TYPE_INT_ARGB ; Represents an image with 8-bit RGBA color components packed into integer pixels.
                 :argb-pre BufferedImage/TYPE_INT_ARGB_PRE ; Represents an image with 8-bit RGBA color components packed into integer pixels.
                 :indexed BufferedImage/TYPE_BYTE_INDEXED ; Represents an indexed byte image.
                 :gray BufferedImage/TYPE_BYTE_GRAY ; Represents a unsigned byte grayscale image, non-indexed.
                 :binary BufferedImage/TYPE_BYTE_BINARY ; Represents an opaque byte-packed 1, 2, or 4 bit image.
                 :custom BufferedImage/TYPE_CUSTOM ; Image type is not recognized so it must be a customized image.
                 :3byte-bgr BufferedImage/TYPE_3BYTE_BGR ; Represents an image with 8-bit RGB color components, corresponding to a Windows-style BGR color model) with the colors Blue, Green, and Red stored in 3 bytes.
                 :4byte-abgr BufferedImage/TYPE_4BYTE_ABGR ; Represents an image with 8-bit RGBA color components with the colors Blue, Green, and Red stored in 3 bytes and 1 byte of alpha.
                 :4byte-abgr-pre BufferedImage/TYPE_4BYTE_ABGR_PRE ; Represents an image with 8-bit RGBA color components with the colors Blue, Green, and Red stored in 3 bytes and 1 byte of alpha.
                 :ushort-gray BufferedImage/TYPE_USHORT_GRAY ; Represents an unsigned short grayscale image, non-indexed).
                 :ushort-555-rgb BufferedImage/TYPE_USHORT_555_RGB ; Represents an image with 5-5-5 RGB color components (5-bits red, 5-bits green, 5-bits blue) with no alpha.
                 :ushort-565-rgb BufferedImage/TYPE_USHORT_565_RGB}) ; Represents an image with 5-6-5 RGB color components (5-bits red, 6-bits green, 5-bits blue) with no alpha.

;;
;; Basic constructors
;;

; Paint
(defn gradient-paint
  "Create a gradient paint."
  ([p1 color1 p2 color2]
   (GradientPaint. p1 color1 p2 color2))
  ([p1 color1 p2 color2 cyclic]
   (GradientPaint. p1 color1 p2 color2 cyclic))
  ([x1 y1 color1 x2 y2 color2]
   (GradientPaint. x1 y1 color1 x2 y2 color2))
  ([x1 y1 color1 x2 y2 color2 cyclic]
   (GradientPaint. x1 y1 color1 x2 y2 color2 cyclic)))

(defn texture-paint
  "Create a texture paint."
  [texture anchor]
  (TexturePaint. texture anchor))

; Stroke
(defn basic-stroke
  "Creates basic strokes."
  ([]
   (BasicStroke.))
  ([width]
   (BasicStroke. width))
  ([width cap-style join-style]
   (BasicStroke. width cap-style join-style))
  ([width cap-style join-style miter-limit]
   (BasicStroke. width cap-style join-style miter-limit))
  ([width cap-style join-style miter-limit dash dash-phase]
    ; TODO check that into-array works as expected
   (BasicStroke. width cap-style join-style miter-limit (into-array dash) dash-phase)))

(defn arc2d
  "Creates a 2d arc."
  ([ellipse-bounds start extend arc-type]
   (Arc2D$Double. ellipse-bounds start extend arc-type))
  ([x y w h start extend arc-type]
   (Arc2D$Double. x y w h start extend arc-type)))

(defn cubic-curve2d
  "Creates a 2d cubic curve."
  [x1 y1 ctrlx1 ctrly1 ctrlx2 ctrly2 x2 y2]
  (CubicCurve2D$Double. x1 y1 ctrlx1 ctrly1 ctrlx2 ctrly2 x2 y2))

(defn centered-circle2d
  "Creates a 2d ellipse reperesenting a circle with the center x, y and radius r."
  [x y r]
  (let [d (* 2 r)]
    (Ellipse2D$Double. (- x r) (- y r) d d)))

(defn ellipse2d
  "Creates a 2d ellipse."
  [x y w h]
  (Ellipse2D$Double. x y w h))

(defn line2d
  "Creates a 2d line."
  ([[x1 y1] [x2 y2]]
   (line2d x1 y1 x2 y2))
  ([x1 y1 x2 y2]
   (Line2D$Double. x1 y1 x2 y2)))

(defn point2d
  "Creates a 2d point."
  [x y]
  (Point2D$Double. x y))

(defn point-coordinates
  "Returns the x/y coordinates of the point."
  [point]
  [(.getX point) (.getY point)])

(defn quad-curve2d
  "Creates a 2d quadratic curve."
  [x1 y1 ctrlx ctrly x2 y2]
  (QuadCurve2D$Double. x1 y1 ctrlx ctrly x2 y2))

(defn rectangle2d
  "Creates a 2d rectangle."
  [x y w h]
  (Rectangle2D$Double. x y w h))

(defn round-rectangle2d
  "Creates a 2d rounded rectangle."
  [x y w h arc-w arc-h]
  (RoundRectangle2D$Double. x y w h arc-w arc-h))

;;
;;
;;

(defn set-color
  "Sets the color of the graphics context gfx."
  [^java.awt.Graphics2D gfx ^java.awt.Color color]
  (.setColor gfx color))

(defn set-font
  "Sets the fon of the graphics context gfx."
  [^java.awt.Graphics2D gfx ^java.awt.Font font]
  (.setFont gfx font))

(defn set-rendering-hint
  "Sets a rendering hint on the graphics context."
  [^java.awt.Graphics2D gfx hint-key hint-value]
  (.setRenderingHint gfx hint-key hint-value))

(defn draw
  "Draws an element."
  ([^java.awt.Graphics2D gfx e]
   (.draw gfx e))
  ([^java.awt.Graphics2D gfx e color]
   (.draw gfx e color)))

(defn fill
  "Draws a filled element."
  ([^java.awt.Graphics2D gfx e]
   (.fill gfx e))
  ([^java.awt.Graphics2D gfx e color]
   (.setColor gfx color)
   (.fill gfx e)))

(defn draw-line
  "Draws a line."
  ([^java.awt.Graphics2D gfx p1 p2]
   (.draw gfx (line2d p1 p2)))
  ([^java.awt.Graphics2D gfx p1 p2 ^java.awt.Color color]
   (.setColor gfx color)
   (.draw gfx (line2d p1 p2)))
  ([^java.awt.Graphics2D gfx x1 y1 x2 y2]
   (.draw gfx (line2d x1 y1 x2 y2)))
  ([^java.awt.Graphics2D gfx x1 y1 x2 y2 ^java.awt.Color color]
   (.setColor gfx color)
   (.draw gfx (line2d x1 y1 x2 y2))))


(defn fill-rect
  "Draws a filled rectangle."
  ([^java.awt.Graphics2D gfx x y width height]
   (.fillRect gfx x y width height))
  ([^java.awt.Graphics2D gfx x y width height ^java.awt.Color color]
   (.setColor gfx color)
   (.fillRect gfx x y width height)))

(defn fill-colored-rect
  "Draws a filled rectangle with the given color."
  ([^java.awt.Graphics2D gfx x y width height ^java.awt.Color color]
   (.setColor gfx color)
   (.fillRect gfx x y width height)))

(defn draw-circle
  "Draws a circle."
  ([^java.awt.Graphics2D gfx x y diameter]
   (.draw gfx (ellipse2d x y diameter diameter)))
  ([^java.awt.Graphics2D gfx x y diameter ^java.awt.Color color]
   (.setColor gfx color)
   (.draw gfx (ellipse2d x y diameter diameter))))

(defn fill-circle
  "Draws a filled circle."
  ([^java.awt.Graphics2D gfx x y diameter]
   (.fill gfx (ellipse2d x y diameter diameter)))
  ([^java.awt.Graphics2D gfx x y diameter ^java.awt.Color color]
   (.setColor gfx color)
   (.fill gfx (ellipse2d x y diameter diameter))))

(defn draw-ellipse
  "Draws an ellipse."
  ([^java.awt.Graphics2D gfx x y w h]
   (.draw gfx (ellipse2d x y w h)))
  ([^java.awt.Graphics2D gfx x y w h ^java.awt.Color color]
   (.setColor gfx color)
   (.draw gfx (ellipse2d x y w h))))

(defn fill-ellipse
  "Draws a filled ellipse."
  ([^java.awt.Graphics2D gfx x y w h]
   (.fill gfx (ellipse2d x y w h)))
  ([^java.awt.Graphics2D gfx x y w h ^java.awt.Color color]
   (.setColor gfx color)
   (.fill gfx (ellipse2d x y w h))))

(defn draw-string
  "Draws a string."
  ([^java.awt.Graphics2D gfx ^java.lang.String s x y]
   (.drawString gfx s x y))
  ([^java.awt.Graphics2D gfx s x y ^java.awt.Color color]
   (.setColor gfx color)
   (.drawString gfx s x y)))

(defn draw-image
  "Draws an image using the graphics context."
  ([^java.awt.Graphics2D gfx ^java.awt.Image img x y]
   (.drawImage gfx img x y nil)))

;;
;;
;;

(defn buffered-image
  "Creates a buffered image."
  ([src]
   (ImageIO/read src))
  ([width height type]
   (BufferedImage. width height type)))

(defn create-graphics-from-image
  "Creates a graphics context from the a buffered image."
  [img]
  (.createGraphics img))

;;
;;
;;

(defn get-rgb
  "Returns the rgb value of the pixel at the coordinates x,y in the image."
  ([img [x y]]
   (.getRGB img x y))
  ([img x y]
   (.getRGB img x y)))

(defn set-rgb
  "Sets the rgb value of the pixel at the coordinates x,y in the image."
  ([^BufferedImage img [x y] v]
   (.setRGB img x y v))
  ([^BufferedImage img x y v]
   (.setRGB img x y v)))

;;
;; Image IO
;;

(defn write-image
  "Writes an image in the given format to the file."
  [img format file]
  (ImageIO/write img format file))

(defn read-image
  "Reads an image."
  [src]
  (ImageIO/read src))
