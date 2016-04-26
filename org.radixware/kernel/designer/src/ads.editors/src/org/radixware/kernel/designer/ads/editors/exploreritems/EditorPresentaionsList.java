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

package org.radixware.kernel.designer.ads.editors.exploreritems;

import java.util.List;
import javax.swing.DefaultListModel;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParentRefExplorerItemDef.EditorPresentationInfo;
import org.radixware.kernel.designer.common.dialogs.components.IRadixObjectChooserLeftComponent;
import org.radixware.kernel.designer.common.dialogs.components.RadixObjectChooserCommonComponent;


public class EditorPresentaionsList extends RadixObjectChooserCommonComponent
                                    implements IRadixObjectChooserLeftComponent {


    private AdsParentRefExplorerItemDef parentRef;
    public void open(final AdsParentRefExplorerItemDef parentRef){
        this.parentRef = parentRef;
        update();
    }

    public void update(){
        setReadonly(isReadonly() || parentRef.isReadOnly());
        updateContent();
    }

    @Override
    public void updateContent() {
        List<EditorPresentationInfo> infos = parentRef.getEditorPresentationInfos();
        DefaultListModel mainModel = new DefaultListModel();
        for (EditorPresentationInfo i : infos){
            mainModel.addElement(i.findEditorPresentation()); 
        }
        list.setModel(mainModel);
    }

    @Override
    public void addAllItems(Object[] objects) {
        DefaultListModel rm = (DefaultListModel)list.getModel();
        for (Object r : objects){
            rm.addElement(r);
            EditorPresentaionsList.this.parentRef.addEditorPresentationId(((Definition)r).getId());
        }
    }

    @Override
    public void removeAll(Object[] objects) {
        DefaultListModel rm = (DefaultListModel)list.getModel();
        for (Object r : objects){
            rm.removeElement(r);
            EditorPresentaionsList.this.parentRef.removeEditorPresentationId(((Definition)r).getId());
        }
    }

    @Override
    public boolean isOrderDependant() {
        return false;
    }

    @Override
    public void moveDown() {
        //do nothing
    }

    @Override
    public void moveUp() {
        //do nothing
    }

}
