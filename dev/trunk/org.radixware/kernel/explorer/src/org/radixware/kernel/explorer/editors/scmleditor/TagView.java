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

package org.radixware.kernel.explorer.editors.scmleditor;

import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCharFormat.UnderlineStyle;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.editors.scmleditor.completer.HtmlCompletionItem;


public class TagView {    
    private long startPos;
    private long endPos;    
    protected QTextCharFormat charFormat=null;
    //protected final IClientEnvironment environment;
    private final ScmlTag tag;
    
    public TagView(final ScmlTag tag,/*final IClientEnvironment environment,*/long position){
        this.tag=tag;
        calcTagPos(position);
       // this.environment=environment;
    }
    
    public QTextCharFormat getCharFormat() {
        return charFormat==null ? getDefaultCharFormat():charFormat;
    }
    
    protected final QTextCharFormat getDefaultCharFormat() {
        QTextCharFormat ch = new QTextCharFormat();
        if (tag !=null && tag.getTranslator()!=null && tag.getTranslator().getToolTip() != null) {
            ch.setToolTip(tag.getTranslator().getToolTip());
        }
        ch.setForeground(new QBrush(QColor.blue));
        ch.setUnderlineStyle(UnderlineStyle.SingleUnderline);
        ch.setUnderlineColor(QColor.blue);
        return ch;
    }

    public long getStartPos() {
        return startPos;
    }

    public long getEndPos() {
        return endPos;
    }
    
    public final void calcTagPos(long startPosForTag) {
        startPos = startPosForTag + 1;
        if(tag !=null && tag.getTranslator()!=null && tag.getTranslator().getDisplayString()!=null)
            endPos = startPos + tag.getTranslator().getDisplayString().length();
        else{
            endPos=startPos;
        }
    }   
    
    public TagView copy(){
        return tag !=null ? new TagView((ScmlTag)tag.getCopy(),startPos-1) : null;
    }
    
    /**
     * ??????? ???????? ????????????? ????
     * @param tag - ???
     * @return - ????????? ????????????? ????
     */
    /*public String createHtml(String space) {
        String res = "";
        String sName = tag.getTranslator().getDisplayString();
        if (sName.indexOf("<") != -1) {
            sName = sName.replaceAll("<", "&#60;");
        }
        //String toolTip=getToolTip(tag.getTranslator().getToolTip());
        res = "<a href=\"1\">" + sName + "</a>" + space;// title=\"" + toolTip + "\"
        return res;
    }*/
    
    public String getToolTip(String title) {
        return HtmlCompletionItem.removeColor(title);
    }
    
    public String getDisplayString(){
        return tag !=null && tag.getTranslator()!=null ? tag.getTranslator().getDisplayString() : "";
    }
    
    public XmlObject asXml(){
        return tag !=null ? tag.saveToXml():null;
    }
    
    public IScmlItem getScmlItem(){
        return tag;
    }
    
   /* public void updateCharFormat(final IClientEnvironment environment) {
        setCharFormat(environment);
    }
    
     private void setCharFormat(final IClientEnvironment environment) {
        String path = tag.getSettingsPath();
        if ((path == null) || (environment.getConfigStore().readPropertySettings(path) == null)) {
            charFormat = getDefaultCharFormat();
            return;
        }
        QColor fc = new QColor(environment.getConfigStore().readPropertySettings(path).foreground);
        String bcrgb = environment.getConfigStore().readPropertySettings(path).background;
        QColor bc = bcrgb == null ? null : new QColor(bcrgb);
        QFont font = ExplorerSettings.getQFont(environment.getConfigStore().readPropertySettings(path).font);
        charFormat.setForeground(new QBrush(fc));
        charFormat.setBackground(new QBrush(bc));
        charFormat.setUnderlineColor(fc);
        charFormat.setUnderlineStyle(UnderlineStyle.SingleUnderline);
        String tollTip=tag.getTranslator().getToolTip() ;
        if (tollTip != null) {
            charFormat.setToolTip(tollTip);
        }
        setFont(font);
    }

    private void setFont(QFont font) {
        charFormat.setFontCapitalization(font.capitalization());
        charFormat.setFontFamily(font.family());
        charFormat.setFontFixedPitch(font.fixedPitch());
        charFormat.setFontItalic(font.italic());
        charFormat.setFontLetterSpacing(font.letterSpacing());
        charFormat.setFontOverline(font.overline());
        charFormat.setFontPointSize(font.pointSize());
        charFormat.setFontStrikeOut(font.strikeOut());
        charFormat.setFontWeight(font.weight());
        charFormat.setFontWordSpacing(font.wordSpacing());
    }*/

    
}
