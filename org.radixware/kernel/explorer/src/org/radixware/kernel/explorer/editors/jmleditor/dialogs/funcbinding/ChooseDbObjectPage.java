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

package org.radixware.kernel.explorer.editors.jmleditor.dialogs.funcbinding;

import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWizardPage;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.GroupModel;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.selector.Selector;


public class ChooseDbObjectPage extends QWizardPage {

    private Selector selector;
    private final QVBoxLayout layout;
    private  final LinkedFuncParamWizard wizard;

    public ChooseDbObjectPage(final LinkedFuncParamWizard parent) {
        super(parent);
        this.wizard = parent;
        this.setObjectName("ChooseDefPage");
        setFinalPage(true);
        layout = new QVBoxLayout();
        layout.setMargin(0);
        this.setLayout(layout);
    }

    public void clear() {
        if (layout.indexOf(selector) != -1) {
            layout.removeWidget(selector);
        }
        selector.hide();
        layout.update();
    }

    @Override
    public int nextId() {
        return -1;
    }

    @Override
    public void initializePage() {
        wizard.getFlow().setMode(WizardFlow.Mode.CHOOSE_OBJECT);
        final RadClassPresentationDef classDef = wizard.getEnvironment().getDefManager().getClassPresentationDef(wizard.getFlow().getClassId());
        final RadSelectorPresentationDef selPresDef = classDef.getDefaultSelectorPresentation();
        final GroupModel groupModel = GroupModel.openTableContextlessSelectorModel(WidgetUtils.findNearestModel(wizard), selPresDef);
        groupModel.getRestrictions().setEditorRestricted(true);
        selector = (Selector) groupModel.getView();
        if (groupModel.getView() == null) {
            selector = (Selector) groupModel.createView();
        }

        selector.open(groupModel);

        selector.setCommandBarHidden(true);
        selector.setEditorToolBarHidden(true);
        groupModel.getRestrictions().setDeleteAllRestricted(true);
        groupModel.getRestrictions().setDeleteRestricted(true);
        groupModel.getRestrictions().setCreateRestricted(true);
        groupModel.getRestrictions().setEditorRestricted(true);
        groupModel.getRestrictions().setInsertAllIntoTreeRestricted(true);
        groupModel.getRestrictions().setInsertIntoTreeRestricted(true);

        layout.addWidget(selector);
        selector.onSetCurrentEntity.connect(this, "setCurEntity(EntityModel)");
        setCurEntity(selector.getCurrentEntity());
    }

    private void setCurEntity(final EntityModel curEntity) {
        wizard.getFlow().setSelectedEntityPid(curEntity.getPid().toString());
    }

    @Override
    public boolean isComplete() {
        return wizard.getFlow().getSelectedEntityPid() != null;
    }

    @Override
    public void cleanupPage() {
        clear();
        wizard.getFlow().leaveMode(WizardFlow.Mode.CHOOSE_OBJECT);
    }
}
