/*
 * Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. This Source Code is distributed
 * WITHOUT ANY WARRANTY; including any implied warranties but not limited to
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Mozilla Public License, v. 2.0. for more details.
 */
package org.radixware.kernel.common.client.text;

import java.awt.Color;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.ETextAlignment;

public abstract class TextOptions implements ITextOptions {

    private final IFont font;
    private final ETextAlignment alignment;
    private final Color foreground;
    private final Color background;

    protected TextOptions(
            final IFont fnt,
            final ETextAlignment align,
            final Color fg,
            final Color bg) {
        font = fnt;
        alignment = align;
        foreground = fg;
        background = bg;
    }


    @Override
    public Color getForegroundColor() {
        return foreground;
    }

    @Override
    public Color getBackgroundColor() {
        return background;
    }

    @Override
    public IFont getFont() {
        return font;
    }

    @Override
    public ETextAlignment getAlignment() {
        return alignment;
    }

    public static String getTextOptionsAsStr(IFont font, ETextAlignment alignment, Color foreground, Color background) {
        final StringBuilder optionsStr = new StringBuilder();
        if (font != null) {
            optionsStr.append(font.asStr());
        }
        optionsStr.append(";");
        if (alignment != null) {
            optionsStr.append(alignment.name());
        }
        optionsStr.append(";");
        if (foreground != null) {
            optionsStr.append(color2Str(foreground));
        }
        optionsStr.append(";");
        if (background != null) {
            optionsStr.append(color2Str(background));
        }
        return optionsStr.toString();
    }

    @Override
    public String asStr() {
        return getTextOptionsAsStr(font, alignment, foreground, background);
    }

    public static String color2Str(Color c) {
        String r = Integer.toHexString(c.getRed());
        String g = Integer.toHexString(c.getGreen());
        String b = Integer.toHexString(c.getBlue());
        if (r.length() == 1) {
            r = "0" + r;
        }
        if (g.length() == 1) {
            g = "0" + g;
        }
        if (b.length() == 1) {
            b = "0" + b;
        }

        return "#" + r + g + b;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.font);
        hash = 29 * hash + Objects.hashCode(this.alignment);
        hash = 29 * hash + Objects.hashCode(this.foreground);
        hash = 29 * hash + Objects.hashCode(this.background);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TextOptions other = (TextOptions) obj;
        if (!Objects.equals(this.font, other.font)) {
            return false;
        }
        if (this.alignment != other.alignment) {
            return false;
        }
        if (!Objects.equals(this.foreground, other.foreground)) {
            return false;
        }
        if (!Objects.equals(this.background, other.background)) {
            return false;
        }
        return true;
    }

    public final boolean isEmpty() {
        return font == null && alignment == null && foreground == null &&  background == null;
    }

    public final boolean isFullDefined() {
        return font != null && alignment != null && foreground != null && background != null;
    }

    public static Color getDarkerColor(final Color color, final int factor) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();        

        final int sub = Math.round((125f * factor) / 100);

        if ((red -= sub) > 255) {
            red = 255;
        } else if (red < 0) {
            red = 0;
        }
        if ((green -= sub) > 255) {
            green = 255;
        } else if (green < 0) {
            green = 0;
        }
        if ((blue -= sub) > 255) {
            blue = 255;
        } else if (blue < 0) {
            blue = 0;
        }

        return new Color(red, green, blue);
    }
}
