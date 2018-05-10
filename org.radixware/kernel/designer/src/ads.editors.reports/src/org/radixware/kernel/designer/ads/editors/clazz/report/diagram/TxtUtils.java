/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.designer.ads.editors.clazz.report.diagram;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComponent;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportBand;

/**
 *
 * @author akrylov
 */
public class TxtUtils {
    private static Font[] defaultFonts = new Font[]{new Font("Courier New", Font.PLAIN, 1), new Font(Font.MONOSPACED, Font.PLAIN, 1), new Font("Monospaced.plain", Font.PLAIN, 1)};
    private static boolean isMeasured = false;
    private static Font FONT;

    public static void measure(Graphics2D graphics) {
        synchronized (TxtUtils.class) {
            if (isMeasured) {
                return;
            }
            try {
                Font[] allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
                List<Font> fonts = new ArrayList<>(Arrays.asList(allFonts));
                for (Font font : defaultFonts){
                    if(fonts.contains(font)){
                        if (saveFont(font, graphics)) {
                            break;
                        }
                    }
                }
                if (FONT == null) {
                    for (Font font : allFonts) {
                        if (saveFont(font, graphics)) {
                            break;
                        }
                    }
                }
            } finally {
                isMeasured = true;
            }
        }
    }
    
    private static boolean saveFont(Font font, Graphics2D graphics) {
        FontMetrics fm = graphics.getFontMetrics(font);
        if (isMonospaced(fm)) {
            Font monospacedFont = font;
            while (true) {
                Rectangle2D rect = fm.getStringBounds("A", graphics);
                if (rect.getWidth() < 10 || rect.getHeight() < 20) {
                    monospacedFont = monospacedFont.deriveFont((float) monospacedFont.getSize() + 1f);
                    fm = graphics.getFontMetrics(monospacedFont);
                } else {
                    break;
                }
            }

            while (true) {
                Rectangle2D rect = fm.getStringBounds("A", graphics);
                if (rect.getWidth() > 10 && rect.getHeight() > 20) {
                    monospacedFont = monospacedFont.deriveFont((float) monospacedFont.getSize() - 1);
                    fm = graphics.getFontMetrics(monospacedFont);
                } else {
                    CHAR_WIDTH = (int) rect.getWidth();
                    CHAR_HEIGHT = (int) rect.getHeight();
                    FONT = monospacedFont;
                    break;
                }
            }
        }
        return FONT != null;
    }

    public static Font getFont() {
        return FONT;
    }

    private static boolean isMonospaced(FontMetrics fontMetrics) {
        int firstCharacterWidth = 0;
        boolean hasFirstCharacterWidth = false;
        for (int codePoint = 0; codePoint < 128; codePoint++) {
            if (Character.isValidCodePoint(codePoint) && (Character.isLetter(codePoint) || Character.isDigit(codePoint))) {
                char character = (char) codePoint;
                int characterWidth = fontMetrics.charWidth(character);
                if (hasFirstCharacterWidth) {
                    if (characterWidth != firstCharacterWidth) {
                        return false;

                    }
                } else {
                    firstCharacterWidth = characterWidth;
                    hasFirstCharacterWidth = true;
                }
            }
        }
        return true;
    }

    private static int CHAR_WIDTH = 10;
    private static int CHAR_HEIGHT = 20;

    public static int columns2Px(int charWidth) {
        return charWidth * CHAR_WIDTH;
    }

    public static int rows2Px(int charHeight) {
        return charHeight * CHAR_HEIGHT;
    }

    public static int px2Rows(int px) {
        return px / CHAR_HEIGHT;
    }

    public static int px2Columns(int px) {
        return px / CHAR_WIDTH;
    }

    public static int snapToGrid(final int d) {
        int c = Math.round((float) d / (float) AdsReportBand.GRID_SIZE_SYMBOLS);
        return AdsReportBand.GRID_SIZE_SYMBOLS * c;
    }

    public static int roundToTenth(final int d) {
        return Math.round(Math.round((float) 10.0 * d) / 10f);
    }

}
