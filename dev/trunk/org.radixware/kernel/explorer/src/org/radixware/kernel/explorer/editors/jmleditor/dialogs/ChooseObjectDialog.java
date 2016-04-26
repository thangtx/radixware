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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QModelIndex;
import java.util.Collection;
import java.util.Set;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef.Lookup.DefInfo;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.xscmleditor.ChoceObjectDialog;


public class ChooseObjectDialog extends ChoceObjectDialog implements IChooseDefFromList {

    private DefInfo curItem;
    private AdsDefinition def=null;
    private final Collection<DefInfo> allowedDefinitions;
    private final JmlEditor editor;
    private final Set<EDefType> templList;
    private final boolean isDbEntity;
    private ChooseDefinitionPanel w;

    public ChooseObjectDialog(final JmlEditor editor, final Collection<DefInfo> allowedDefinitions, final String title, final Set<EDefType> templList, final boolean isDbEntity) {
        super(editor.getEnvironment(), editor, "ChoceObjectUF", editor.isReadOnly());
        this.editor = editor;
        this.setWindowTitle(title);
        this.allowedDefinitions = allowedDefinitions;
        this.templList = templList;
        this.isDbEntity = isDbEntity;
        createUI();
    }

    @Override
    public boolean setCurItem(final QModelIndex modelIndex) {
        if (modelIndex != null) {
            if (w instanceof ChooseDomainPanel) {
                def = ((ChooseDomainPanel) w).getSelectedDomain();
                if (def != null) {
                    setCanAccept(true);
                    return true;
                }
            } else {
                curItem = ((ListModel) modelIndex.model()).getDefList().get(modelIndex.row());
                if (curItem != null) {
                    setCanAccept(true);
                    return true;
                }
            }
        }
        setCanAccept(false);
        return false;
    }
    
    @Override
    public boolean setSelectedDefinition(final AdsDefinition selectedDef) {
        if (selectedDef != null) { 
            def=selectedDef;
            setCanAccept(true);
            return true;
        }
        setCanAccept(false);
        return false;
    }

    public AdsDefinition getSelectedDef() {
        if (def == null && curItem != null) {
            def = curItem.getDefinition();
            if (def == null) {
                def = Lookup.findTopLevelDefinition(editor.getUserFunc(), curItem.getPath()[0]);
            }
        }
        return def;
    }

    @Override
    public void onItemClick(final QModelIndex modelIndex) {
        setCurItem(modelIndex);
    }

    @Override
    public void onItemDoubleClick(final QModelIndex modelIndex) {
        if (setCurItem(modelIndex)) {
            accept();
        }
    }    

    @Override
    protected void createListUi() {
        if(templList!=null && templList.contains(EDefType.DOMAIN)){
            w = new ChooseDomainPanel(this, allowedDefinitions, editor.getUserFunc(), templList, isDbEntity,false);
        }else{
            w = new ChooseDefinitionPanel(this, allowedDefinitions, editor.getUserFunc(), templList, isDbEntity,false);
        }
        rejectButtonClick.connect(w,"closeTread()");
        dialogLayout().addWidget(w);
    }
}