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

package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCharFormat.UnderlineStyle;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.explorer.editors.jmleditor.completer.HtmlCompletionItem;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


public class TagInfo {    

    private String title;
    private String displayName;
    private long startPos;
    private long endPos;
    protected String fullName;
    private QTextCharFormat charFormat = new QTextCharFormat();
    private boolean valid = true;
    protected final IClientEnvironment environment;
    private boolean isDeprecated;

    public TagInfo(final IClientEnvironment environment, final long pos, final boolean isDeprecated/*,String path*/) {
        this.environment = environment;
        this.startPos = pos;
        this.isDeprecated=isDeprecated;
        setCharFormat(/*path*/);
    }
    
    public TagInfo(final IClientEnvironment environment, final TagInfo res) {
        this.environment = environment;
        this.endPos = res.endPos;
        this.displayName = res.displayName;
        this.fullName = res.fullName;
        this.startPos = res.startPos;
        this.title = res.title;
        this.charFormat = res.charFormat;
        this.valid=res.valid;
        this.isDeprecated=res.isDeprecated;
    }
    
    protected void setIsDeprecated(final boolean isDeprecated){
        this.isDeprecated=isDeprecated;
        charFormat.setFontStrikeOut(isDeprecated);
    }

    public String getDisplayName() {
       displayName = displayName == null ? "null" : displayName;
       return displayName;
    }

    public String getToolTip() {
        return HtmlCompletionItem.changeColor(title);
    }
    
    protected final void setValid(final boolean isValid){
        valid = isValid;
    }

    public final boolean isValid() {
        return valid;
    }

    /**
     * ??????? ???????? ????????????? ????
     * @param tag - ???
     * @return - ????????? ????????????? ????
     */
    public String createHtmlTag(final String space) {
        final String sName = Html.string2HtmlString(getDisplayName());
        return "<a href=\"1\" title=\"" + getToolTip() + "\">" + sName + "</a>" + space;
    }

    public boolean setDisplayedInfo(final EDefinitionDisplayMode showMode) {
        return false;
    }

    protected void setDisplayedInfo(final String title, final String name, final EDefinitionDisplayMode showMode) {
        this.fullName = name;
        this.title = title;
        if (title != null) {
            charFormat.setToolTip(getToolTip());
        }
        setDisplayedInfo(showMode);
    }

    protected void setDisplayedInfo(final String title, final String displayName) {
        this.displayName = displayName;      
        if (title != null) {
            this.title = title;
            charFormat.setToolTip(getToolTip());
        }
        endPos = startPos + displayName.length();
    }

    public TagInfo copy() {
        return new TagInfo(environment, this);
    }
    
    public TagInfo copyWithScmlItem(){
        return copy();
    }

    public boolean showEditDialog(final XscmlEditor editText, final EDefinitionDisplayMode showMode) {
        return false;
    }

    protected String getNameWithoutModule() {
        return getNameWithoutModule(fullName);
    }

    public static String getNameWithoutModule(final String name) {
        String res = name;
        final int start = name.lastIndexOf("::");
        if (start != -1) {
            final String s = name.substring(0, start);
            final int n = s.lastIndexOf(' ') == -1 ? 0 : s.lastIndexOf(' ') + 1;
            res = name.substring(0, n) + name.substring(start + 2, name.length());
        }
        return res;
    }

    public QTextCharFormat getCharFormat() {
        return charFormat;
    }

    protected QTextCharFormat getDefaultCharFormat() {
        final QTextCharFormat ch = new QTextCharFormat();
        if (getToolTip() != null) {
            ch.setToolTip(getToolTip());
        }
        ch.setForeground(new QBrush(QColor.blue));
        ch.setUnderlineStyle(UnderlineStyle.SingleUnderline);
        ch.setUnderlineColor(QColor.blue);
        return ch;
    }

    public void updateCharFormat() {
        setCharFormat(/*getPath()*/);
    }

    protected String getSettingsPath() {
        return null;
    }

    private void setCharFormat(/*String path*/) {
        final String path = getSettingsPath();
        if (path==null){
            charFormat = getDefaultCharFormat();
            return;
        }
        final ExplorerTextOptions textOptions = (ExplorerTextOptions)environment.getConfigStore().readTextOptions(path);
        if (textOptions==null) {
            charFormat = getDefaultCharFormat();
            return;
        }
        final QColor fc = textOptions.getForeground();
        final QColor bc = textOptions.getBackground();
        final QFont font = textOptions.getQFont();
        charFormat.setForeground(new QBrush(fc));
        if (bc!=null){
            charFormat.setBackground(new QBrush(bc));
        }
        charFormat.setUnderlineColor(fc);
        charFormat.setUnderlineStyle(UnderlineStyle.SingleUnderline);
        if (getToolTip() != null) {
            charFormat.setToolTip(getToolTip());
        }
        setFont(font);
    }

    private void setFont(final QFont font) {
        charFormat.setFontCapitalization(font.capitalization());
        charFormat.setFontFamily(font.family());
        charFormat.setFontFixedPitch(font.fixedPitch());
        charFormat.setFontItalic(font.italic());
        charFormat.setFontLetterSpacing(font.letterSpacing());
        charFormat.setFontOverline(font.overline());
        charFormat.setFontPointSize(font.pointSize());
        charFormat.setFontWeight(font.weight());
        charFormat.setFontWordSpacing(font.wordSpacing());
        charFormat.setFontStrikeOut(isDeprecated);
    }

    public long getStartPos() {
        return startPos;
    }

    public long getEndPos() {
        return endPos;
    }

    public final void calcTagPos(final long startPosForTag) {
        startPos = startPosForTag + 1;
        endPos = startPos + getDisplayName().length();
    }
    
    public void onMouseReleased(QMouseEvent e, IClientEnvironment env) {
        //do nothing
    }
    
    public Qt.CursorShape getCursorShape(QMouseEvent e) {
        return null;
    }
    
}
