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

import org.apache.xmlbeans.XmlObject;


public class ScmlText implements IScmlItem{
   // private String text;
    private ScmlTextTranslator translator;
    
    public ScmlText(String text){
        //this.text=text;
        this.translator=new ScmlTextTranslator(text);
    }

    @Override
    public boolean isText() {
        return true;
    }

    @Override
    public boolean isTag() {
        return false;
    }

    @Override
    public IScmlTranslator getTranslator() {
        return translator;
    }

    @Override
    public IScmlItem getCopy() {
        return new ScmlText(translator.getDisplayString());
    }

    @Override
    public IScmlItem getCopy(int from, int to) {
        String text=translator.getDisplayString();
        return new ScmlText(text.substring(from, to));
    }
    
    public String getText(){
        return translator.getDisplayString();
    }

    @Override
    public XmlObject saveToXml() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
