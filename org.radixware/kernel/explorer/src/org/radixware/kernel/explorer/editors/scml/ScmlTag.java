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

package org.radixware.kernel.explorer.editors.scml;

import com.trolltech.qt.gui.QWidget;
import org.apache.xmlbeans.XmlObject;


public class ScmlTag implements IScmlItem{
    
    //private long startPos;
    //private long endPos;    
    //protected QTextCharFormat charFormat;
    //protected final IClientEnvironment environment;
    protected IScmlTranslator translator;
    
    public ScmlTag(/*IScmlTranslator translator*/){
        //this.translator=translator;
    }
    
    @Override
    public boolean isText() {
        return false;
    }

    @Override
    public boolean isTag() {
        return true;
    }

    @Override
    public IScmlTranslator getTranslator() {
        return translator;
    }

    @Override
    public IScmlItem getCopy() {
        return  new ScmlTag(/*translator*/);
    }

    @Override
    public IScmlItem getCopy(int from, int to) {
        throw new IllegalStateException();
    }
    
    public boolean edit(QWidget parent){
        return false;
    }
    
  /*  public QTextCharFormat getCharFormat() {
        return charFormat;
    }
    
    protected final QTextCharFormat getDefaultCharFormat() {
        QTextCharFormat ch = new QTextCharFormat();
        if (getTranslator().getToolTip() != null) {
            ch.setToolTip(getTranslator().getToolTip());
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
    
    public void calcTagPos(long startPosForTag) {
        startPos = startPosForTag + 1;
        endPos = startPos + getTranslator().getDisplayString().length();
    }*/

    @Override
    public XmlObject saveToXml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }



}
