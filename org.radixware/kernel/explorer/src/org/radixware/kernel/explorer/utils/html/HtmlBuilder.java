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

package org.radixware.kernel.explorer.utils.html;


public class HtmlBuilder {
    private final StringBuilder stringBuilder = new StringBuilder();
    //private final Stack<String> tagStack = new Stack<String>();
    //private boolean isTagOpened;
    
    public final void startTag(final String tag) {
        stringBuilder.append( String.format(START_TAG, tag) );
    }
    
    public final void endTag(final String tag) {
        stringBuilder.append( String.format(END_TAG, tag) );
    }
    
    public final void startTable() {
        stringBuilder.append( String.format(START_TAG, TABLE) );
    }
    
    public final void endTable() {
        stringBuilder.append( String.format(END_TAG, TABLE) );
    }
    
    public final void startRow() {
        stringBuilder.append( String.format(START_TAG, ROW) );
    }
    
    public final void endRow() {
        stringBuilder.append( String.format(END_TAG, ROW) );
    }
    
    public final void appendBoldString(final String s) {
        stringBuilder.append( makeBold(s) );
    }
    
    public final void appendItalicString(final String s) {
        stringBuilder.append( makeItalic(s) );
    }
    
    /**
     * Appends a string enclosed with an HTML table cell
     * @param string 
     */
    public final void appendCell(final String string) {
        stringBuilder.append( makeTagged(CELL, string) );
    }
    
    /**
     * Appends a string enclosed with a tag with attributes. For example:
     * <code>htmlBuilder.appendTaggedWithAttrs("td", "colspan=2", "Wow!");</code>
     * @param tag
     * @param attrs
     * @param string 
     */
    public final void appendTaggedWithAttrs(final String tag, final String attrs, final String string) {
        stringBuilder.append(makeTaggedWithAttrs(tag, attrs, string));
    }
    
    public final void appendTagged(final String tag, final String string) {
        stringBuilder.append(makeTagged(tag, string));
    }
    
    public final void append(final String s) {
        stringBuilder.append(s);
    }
    
    public final String getHtml(){
        return toString();
    }
    
    @Override
    public String toString() {
        return stringBuilder.toString();
    }
    
    private static final String START_TAG = "<%s>";
    private static final String END_TAG = "</%s>";
    public static final String TABLE = "table";
    public static final String ROW = "tr";
    public static final String CELL = "td";
    public static final String BOLD = "b";
    public static final String ITALIC = "i";
    
    public static String makeBold(final String s) {
        final StringBuilder builder = new StringBuilder();
        builder.append( String.format(START_TAG, BOLD) );
        builder.append(s);
        builder.append( String.format(END_TAG, BOLD) );
        return builder.toString();
    }
    
    public static String makeItalic(final String s) {
        final StringBuilder builder = new StringBuilder();
        builder.append( String.format(START_TAG, ITALIC) );
        builder.append(s);
        builder.append( String.format(END_TAG, ITALIC) );
        return builder.toString();
    }
    
    public static String makeTagged(final String tag, final String string) {
        final StringBuilder builder = new StringBuilder();
        builder.append( String.format(START_TAG, tag) );
        builder.append(string);
        builder.append( String.format(END_TAG, tag) );
        return builder.toString();
    }
    
    public static String makeTaggedWithAttrs(final String tag, final String attrs, final String string) {
        final StringBuilder builder = new StringBuilder();
        builder.append( String.format(START_TAG, tag + " " + attrs) );
        builder.append(string);
        builder.append( String.format(END_TAG, tag) );
        return builder.toString();
    }
}
