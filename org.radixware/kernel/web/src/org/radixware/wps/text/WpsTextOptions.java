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

package org.radixware.wps.text;

import java.awt.Color;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.radixware.kernel.common.client.enums.EFontStyle;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextDecoration;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.text.IFont;
import org.radixware.kernel.common.client.text.TextOptions;
import org.radixware.wps.WpsEnvironment;


public class WpsTextOptions extends TextOptions {

    public static final WpsTextOptions EMPTY = TextOptionsManager.getInstance().getOptions(null, null, null, null);
    private final EnumSet<ECssTextOptionsStyle> EMPTY_STYLES = EnumSet.noneOf(ECssTextOptionsStyle.class);
    private final EnumSet<ECssTextOptionsStyle> cssStyles;

    public static class Factory {

        public static WpsTextOptions getDefault(final WpsEnvironment environment) {
            return new DefaultTextOptionsProvider(environment).getDefault();
        }

        public static WpsTextOptions getOptions(final WpsFont font, final ETextAlignment alignment, final Color foreground, final Color background) {
            return TextOptionsManager.getInstance().getOptions(font, alignment, foreground, background);
        }

        public static WpsTextOptions getOptions(final WpsFont font, final ETextAlignment alignment, final Color foreground) {
            return getOptions(font, alignment, foreground, null);
        }

        public static WpsTextOptions getOptions(final WpsFont font, final ETextAlignment alignment) {
            return getOptions(font, alignment, null, null);
        }

        public static WpsTextOptions getOptions(final WpsFont font, final Color foreground, final Color background) {
            return getOptions(font, null, foreground, background);
        }

        public static WpsTextOptions getOptions(final WpsFont font, final Color foreground) {
            return getOptions(font, null, foreground, null);
        }

        public static WpsTextOptions getOptions(final WpsFont font) {
            return getOptions(font, null, null, null);
        }

        public static WpsTextOptions getOptions(final Color foreground, final Color background) {
            return getOptions(null, null, foreground, background);
        }

        public static WpsTextOptions getOptions(final Color foreground) {
            return getOptions(null, null, foreground, null);
        }

        public static WpsTextOptions getOptions(final ETextAlignment alignment) {
            return getOptions(null, alignment, null, null);
        }

        public static WpsTextOptions getOptions(final ETextAlignment alignment, final Color foreground, final Color background) {
            return getOptions(null, alignment, foreground, background);
        }

        public static WpsTextOptions getOptions(final ETextAlignment alignment, final Color foreground) {
            return getOptions(null, alignment, foreground, null);
        }

        public static WpsTextOptions getOptions(final WpsEnvironment environment, final ETextOptionsMarker... markers) {
            return new DefaultTextOptionsProvider(environment).getOptions(markers);
            //return environment.getTextOptionsProvider().getOptions(markers);
        }

        public static WpsTextOptions getLabelOptions(final WpsEnvironment environment, final ETextOptionsMarker... markers) {
            EnumSet<ETextOptionsMarker> markersSet = EnumSet.of(ETextOptionsMarker.LABEL);
            markersSet.addAll(Arrays.asList(markers));
            return new DefaultTextOptionsProvider(environment).getOptions(markersSet, null);
            //return environment.getTextOptionsProvider().getOptions(markersSet, null);
        }

        public static WpsTextOptions fromStr(final String optionsAsStr) {
            final WpsFont font;
            final ETextAlignment alignment;
            final Color foreground;
            final Color background;
            final String[] parts = optionsAsStr.split(";");
            if (parts.length > 0 && !parts[0].isEmpty()) {
                font = WpsFont.Factory.fromStr(parts[0]);
            } else {
                font = null;
            }
            if (parts.length > 1 && !parts[1].isEmpty()) {
                alignment = ETextAlignment.fromStr(parts[1]);
            } else {
                alignment = null;
            }
            if (parts.length > 2 && !parts[2].isEmpty()) {
                foreground = Color.decode(parts[2]);
            } else {
                foreground = null;
            }
            if (parts.length > 3 && !parts[3].isEmpty()) {
                background = Color.decode(parts[3]);
            } else {
                background = null;
            }
            return getOptions(font, alignment, foreground, background);
        }
    }

    WpsTextOptions(final WpsFont fnt,
            final ETextAlignment align,
            final Color fg,
            final Color bg) {
        super(fnt, align, fg, bg);
        cssStyles = null;
    }

    WpsTextOptions(final EnumSet<ECssTextOptionsStyle> cssStyles,
            final WpsFont fnt,
            final ETextAlignment align,
            final Color fg,
            final Color bg) {
        super(fnt, align, fg, bg);
        if (cssStyles != null && !cssStyles.isEmpty()) {
            this.cssStyles = cssStyles.clone();
        } else {
            this.cssStyles = null;
        }
    }

    @Override
    public WpsFont getFont() {
        return (WpsFont) super.getFont();
    }

    protected TextOptionsManager textOptionsManager() {
        return TextOptionsManager.getInstance();
    }

    protected WpsFontManager fontManager() {
        return WpsFontManager.getInstance();
    }

    public static WpsTextOptions getDefault(final WpsEnvironment environment) {
        return Factory.getDefault(environment);
    }

    @Override
    public WpsTextOptions changeForegroundColor(final Color color) {
        if (Objects.equals(getForegroundColor(), color)) {
            return this;
        }
        return textOptionsManager().getOptions(WpsFont.Factory.getFont(getFont()), getAlignment(), color, getBackgroundColor());
    }

    @Override
    public WpsTextOptions changeBackgroundColor(Color color) {
        if (Objects.equals(getBackgroundColor(), color)) {
            return this;
        }
        return textOptionsManager().getOptions(WpsFont.Factory.getFont(getFont()), getAlignment(), getForegroundColor(), color);
    }

    @Override
    public WpsTextOptions changeAlignment(final ETextAlignment alignment) {
        if (Objects.equals(getAlignment(), alignment)) {
            return this;
        }
        return textOptionsManager().getOptions(WpsFont.Factory.getFont(getFont()), alignment, getForegroundColor(), getBackgroundColor());
    }

    @Override
    public WpsTextOptions changeFont(final IFont font) {
        if (Objects.equals(getFont(), font)) {
            return this;
        }
        return textOptionsManager().getOptions(font, getAlignment(), getForegroundColor(), getBackgroundColor());
    }

    @Override
    public WpsTextOptions changeFontFamily(final String newFamily) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getFont(newFamily), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else if (!getFont().getFamily().equals(newFamily)) {
            return textOptionsManager().getOptions(getFont().changeFamily(newFamily), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return this;
        }
    }

    @Override
    public WpsTextOptions changeFontSize(final int newSize) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getFont(newSize), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else if (getFont().getSize() != newSize) {
            return textOptionsManager().getOptions(getFont().changeSize(newSize), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return this;
        }
    }

    @Override
    public WpsTextOptions changeFontWeight(final EFontWeight weight) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getFont(weight), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else if (getFont().getWeight() != weight) {
            return textOptionsManager().getOptions(getFont().changeWeight(weight), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return this;
        }
    }

    @Override
    public WpsTextOptions changeFontStyle(final EFontStyle style) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getFont(style), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else if (getFont().getStyle() != style) {
            return textOptionsManager().getOptions(getFont().changeStyle(style), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return this;
        }
    }

    @Override
    public WpsTextOptions changeFontDecoration(final EnumSet<ETextDecoration> decoration) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getFont(EFontStyle.NORMAL, decoration), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else if (getFont().getDecoration() != decoration) {
            return textOptionsManager().getOptions(getFont().changeDecoration(decoration), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return this;
        }
    }

    @Override
    public WpsTextOptions increaseFontSize(int size) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getDefaultFont().increaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return textOptionsManager().getOptions(getFont().increaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    @Override
    public WpsTextOptions decreaseFontSize(int size) {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getDefaultFont().decreaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return textOptionsManager().getOptions(getFont().decreaseSize(size), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    @Override
    public WpsTextOptions getWithBoldFont() {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getDefaultFont().getBold(), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return textOptionsManager().getOptions(getFont().getBold(), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    @Override
    public WpsTextOptions getWithItalicFont() {
        if (getFont() == null) {
            return textOptionsManager().getOptions(fontManager().getDefaultFont().getItalic(), getAlignment(), getForegroundColor(), getBackgroundColor());
        } else {
            return textOptionsManager().getOptions(getFont().getItalic(), getAlignment(), getForegroundColor(), getBackgroundColor());
        }
    }

    public Map<ECssPropertyName, String> getCssPropertyValues() {
        Map<ECssPropertyName, String> keys = new HashMap<>();
        if (getBackgroundColor() != null) {
            keys.put(ECssPropertyName.BACKGROUND_COLOR, TextOptions.color2Str(getBackgroundColor()));
        }
        if (getForegroundColor() != null) {
            keys.put(ECssPropertyName.FOREGROUND_COLOR, TextOptions.color2Str(getForegroundColor()));
        }
        if (getAlignment() != null) {
            keys.put(ECssPropertyName.TEXT_ALIGNMENT, getAlignment().getCssPropertyValue());
        }
        if (getFont() != null) {
            keys.putAll(getFont().getCssPropertyValues());
        }
        return keys;
    }

    public EnumSet<ECssTextOptionsStyle> getPredefinedCssStyles() {
        return cssStyles == null ? EMPTY_STYLES : cssStyles.clone();
    }
}
