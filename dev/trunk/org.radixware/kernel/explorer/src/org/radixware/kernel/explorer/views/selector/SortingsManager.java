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

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.client.models.groupsettings.Sortings;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class SortingsManager extends GroupSettingsEditor<RadSortingDef> {

    private QAction editSortingActon;
    private final Sortings sortings;

    public SortingsManager(final IClientEnvironment environment, final QWidget parent, final Sortings sortings) {
        super(environment, parent, sortings);
        refreshTree();
        this.sortings = sortings;
    }

    @Override
    protected void setupAction(final QAction action) {
        if (action == createSettingAction) {
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Sorting"));
            action.setText(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Sorting"));
        }else if (action == removeAction) {
            action.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Remove"));        
        } else {
            action.setVisible(false);            
        }
    }

    @Override
    protected void setupToolBar(QToolBar toolbar) {
        super.setupToolBar(toolbar);
        editSortingActon = new QAction(this);
        editSortingActon.triggered.connect(this, "editCurrentSetting()");
        editSortingActon.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT));
        editSortingActon.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Edit Sorting"));
        toolbar.addAction(editSortingActon);
    }

    @Override
    public void refreshActions() {
        super.refreshActions();
        final IGroupSetting currentSetting = getCurrentSetting();
        editSortingActon.setEnabled(currentSetting!=null && currentSetting.isUserDefined());
    }
            
    @Override
    protected String getNewSettingName() {
        return getEnvironment().getMessageProvider().translate("SelectorAddons", "New Sorting");
    }

    @Override
    protected boolean confirmToRemoveSetting(final String name) {
        final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Confirm Sorting Deletion");
        final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Do you really want to delete sorting \'%s\'?");
        return getEnvironment().messageConfirmation(title, String.format(message, name));
    }

    @Override
    protected boolean confirmToRemoveGroup(final String name) {
        throw new UnsupportedOperationException("Call of confirmToRemoveGroup() in SortingManager");
    }

    @Override
    public RadSortingDef createCustomSetting() {
        final RadSortingDef newSorting = sortings.createNewSorting(this);
        if (newSorting!=null){
            refreshTree();
            setCurrentSetting(newSorting);
        }     
        return newSorting;
    }

    @Override
    public boolean canApplySetting(final IGroupSetting setting) {
        return sortings.isAcceptable((RadSortingDef)setting, sortings.getGroupModel().getCurrentFilter());
    }
    
    
}
