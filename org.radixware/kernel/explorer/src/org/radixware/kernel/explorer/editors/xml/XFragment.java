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

package org.radixware.kernel.explorer.editors.xml;

import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;


class XFragment extends XTreeTag{
    
    public XFragment(final TreeWindow tw, final XmlObject xmlObj){
        super(tw,xmlObj,null,null);
    }

    @Override
    public String getElementPrefix() {
        return "";
    }

    @Override
    public boolean isExternalTypeSystem() {
        return false;
    }

    @Override
    public boolean isAnyType() {
        return false;
    }

    @Override
    public void init() {
        //nothing to do
    }

    @Override
    public SchemaParticle[] getChoices() {
        return null;
    }

    @Override
    public boolean anyElementAllowed() {
        return false;
    }

    @Override
    public boolean isOdd() {
        return false;
    }

    @Override
    public boolean isFirstChoice() {
        return false;
    }

    @Override
    public void setFirstChoice(final boolean v) {
        //nothing to do
    }

    @Override
    public SchemaParticle[] checkForSequenceInChoice(final XmlObject node, final SchemaParticle[] choice) {
        return null;
    }

    @Override
    public ValEditor getEditor() {
        return null;//not editable
    }

    @Override
    public void setEditor(final ValEditor e) {
        if (e!=null){
            e.setParent(tw.getTree());
        }
        //not editable
    }

    @Override
    public int getMinOccurs() {
        return 1;
    }

    @Override
    public int getMaxOccurs() {
        return 1;
    }
    
    
}
