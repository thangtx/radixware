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

package org.radixware.kernel.designer.api.editors.components;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.swing.JComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.defs.RadixObject;


final class LinkedString implements Iterable<LinkedString.Item> {

    final static class TextDecoration {

        static final TextDecoration GENERAL = new TextDecoration(false, false, false);
        static final TextDecoration BOLD = new TextDecoration(false, false, false);
        static final TextDecoration STRIKEOUT = new TextDecoration(false, false, false);
        public final boolean isBold;
        public final boolean isItalic;
        public final boolean isStrikeout;

        public TextDecoration(boolean isBold, boolean isItalic, boolean isStrikeout) {
            this.isBold = isBold;
            this.isItalic = isItalic;
            this.isStrikeout = isStrikeout;
        }
    }

    final static class Item {

        private TextDecoration decoration;
        private RadixObject ref;
        private String value;
        private boolean escape;

        public Item(TextDecoration decoration, RadixObject ref, String value) {
            this(decoration, ref, value, false);
        }
        
        public Item(TextDecoration decoration, RadixObject ref, String value, boolean escape) {
            this.decoration = decoration;
            this.ref = ref;
            this.value = value;
            this.escape = escape;
        }

        int getStrLen(JComponent component) {
            Font current = component.getFont();
            if (decoration.isBold) {
                current = current.deriveFont(Font.BOLD);
            }
            if (decoration.isItalic) {
                current = current.deriveFont(Font.ITALIC);
            }
            final FontMetrics fontMetrics = component.getFontMetrics(current);
            return fontMetrics.stringWidth(value);
        }

        String toHtml() {
            String html =  escape ? StringEscapeUtils.escapeHtml(value) : value;
            if (decoration != null) {
                if (decoration.isBold) {
                    html = "<b>" + html + "</b>";
                }
                if (decoration.isItalic) {
                    html = "<i>" + html + "</i>";
                }
                if (decoration.isStrikeout) {
                    html = "<s>" + html + "</s>";
                }
                
                if (ref != null) {
                    html = "<a href='#' style='color:#000088'>" + html + "</a>";
                }
            }
            return html;
        }
    }
    
    private final List<Item> items;

    public LinkedString() {
        items = new ArrayList<>();
    }

    @Override
    public Iterator<Item> iterator() {
        return items.iterator();
    }

    public boolean add(Item e) {
        return items.add(e);
    }

    public boolean add(Collection<Item> e) {
        return items.addAll(e);
    }

    public Item get(int index) {
        return items.get(index);
    }

    String toHtml() {
        final StringBuilder sb = new StringBuilder();
        sb.append("<html><body>");
        for (final Item item : this) {
            sb.append(item.toHtml());
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    public RadixObject getRef(int x, int y, JComponent component) {
        int pos = 0;
        for (final Item item : this) {
            int len = item.getStrLen(component);
            if (pos <= x && x < pos + len) {
                return item.ref;
            }
            pos += len;
        }
        return null;
    }
}
