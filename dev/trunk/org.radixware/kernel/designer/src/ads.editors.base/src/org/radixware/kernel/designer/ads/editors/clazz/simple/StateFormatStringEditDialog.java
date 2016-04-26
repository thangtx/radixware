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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.designer.common.dialogs.components.state.StateAbstractDialog;


public class StateFormatStringEditDialog extends StateAbstractDialog {

    public StateFormatStringEditDialog(final AdsObjectTitleFormatDef objectTitleFormat, final AdsObjectTitleFormatDef.TitleItem titleItem) {
        super(new FormatStringEditorPanel(), "Format String Editor");
        ((FormatStringEditorPanel) getComponent()).open(objectTitleFormat, titleItem);
    }

    public StateFormatStringEditDialog(AdsTypeDeclaration inputType, String inputPattern,
            String groupSeparator,int groupSize){
        super(new FormatStringEditorPanel(), "Format String Editor");
        ((FormatStringEditorPanel) getComponent()).open(inputType, inputPattern,
                groupSeparator,groupSize);
    }

    public String getPattern(){
        return ((FormatStringEditorPanel) getComponent()).getPattern();
    }
    
    public String getGroupSeparator(){
        return ((FormatStringEditorPanel) getComponent()).getGroupSeparator();
    }
    
    public int getGroupSize(){
        return ((FormatStringEditorPanel) getComponent()).getGroupSize();
    }

    @Override
    protected void apply() {
        ((FormatStringEditorPanel) getComponent()).apply();
    }   
}
