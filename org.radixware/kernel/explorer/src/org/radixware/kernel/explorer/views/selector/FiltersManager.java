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

import com.trolltech.qt.core.QDir;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QFileDialog;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QWidget;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.filters.RadUserFilter;
import org.radixware.kernel.common.client.models.FilterModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.groupsettings.Filters;
import org.radixware.kernel.common.client.models.groupsettings.IGroupSetting;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.schemas.groupsettings.CustomFilter;

public class FiltersManager extends GroupSettingsEditor<FilterModel> {

    public static final class Icons extends ExplorerIcon.CommonOperations {

        private Icons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon INHERIT = ClientIcon.ValueModification.INHERIT;
        public static final Icons CREATE_FILTER = new Icons("classpath:images/create_filter.svg");
        public static final Icons CREATE_FILTER_GROUP = new Icons("classpath:images/create_filter_group.svg");
        public static final Icons CREATE_FILTER_IN_GROUP = new Icons("classpath:images/create_filter_in_group.svg");
        public static final Icons FILTER_GROUP = new Icons("classpath:images/filter_group.svg");
        public static final Icons EXPORT_FILTER = new Icons("classpath:images/export_f.svg");
        public static final Icons IMPORT_FILTER = new Icons("classpath:images/import_f.svg");
        public static final Icons CONVERT_FILTER = new Icons("classpath:images/convert_filter.svg");
    }
    private QAction exportFilterActon, importFilterActon, convertFilterActon;
    private final Filters filters;
    private static final Id CMD_CONVERT_ID= Id.Factory.loadFrom("clcYNPNRKD6R5GLVDVVORCDQ2QL3I");

    public FiltersManager(IClientEnvironment environment, final QWidget parent, final Filters filters) {
        super(environment, parent, filters);
        this.filters = filters;        
        setSettingsGroupIcon(ExplorerIcon.getQIcon(Icons.FILTER_GROUP));
        setCustomSettingsGroupTitle(environment.getMessageProvider().translate("SelectorAddons", "User filters"));
        refreshTree();
    }

    @Override
    protected void setupAction(QAction action) {
        if (action == createSettingAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.CREATE_FILTER));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Filter"));
            action.setText(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Filter"));
        } else if (action == createSettingInGroupAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.CREATE_FILTER_IN_GROUP));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Filter in Current Group"));
            action.setText(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Filter in Current Group"));
        } else if (action == createSettingGroupAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.CREATE_FILTER_GROUP));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Create Filters Group"));
        } else if (action == inheritSettingAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.INHERIT));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Qualify Predefined Filter"));
        } else if (action == removeAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.DELETE));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Remove"));
        } else if (action == moveUpAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.UP));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Move Up"));
        } else if (action == moveDownAction) {
            action.setIcon(ExplorerIcon.getQIcon(Icons.DOWN));
            action.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Move Down"));
        }
    }

    @SuppressWarnings("unused")
    private void exportCurrentFilter() {
        final IGroupSetting currentSetting = getCurrentSetting();
        if (currentSetting instanceof FilterModel) {
            final FilterModel model = (FilterModel) currentSetting;
            if (model.getDefinition() instanceof RadUserFilter) {
                final RadUserFilter userFilter = (RadUserFilter) model.getDefinition();
                final String fileFilter = getEnvironment().getMessageProvider().translate("SelectorAddons", "Xml Document (%s);;All Files (%s)");
                final String fileName =
                        QFileDialog.getSaveFileName(this, getEnvironment().getMessageProvider().translate("SelectorAddons", "Export Filter"), QDir.homePath(),
                        new QFileDialog.Filter(String.format(fileFilter, "*.xml", "*.*")));
                if (fileName != null && !fileName.isEmpty()) {
                    final String filterAsStr = userFilter.saveToString();
                    final File xmlFile = new File(fileName);
                    try {
                        FileUtils.writeString(xmlFile, filterAsStr, FileUtils.XML_ENCODING);
                    } catch (IOException exception) {
                        final String title = getEnvironment().getMessageProvider().translate("ExplorerError", "Input/Output Exception");
                        if (exception.getLocalizedMessage() != null && !exception.getLocalizedMessage().isEmpty()) {
                            final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't export into file '%s':\n%s");
                            getEnvironment().messageError(title, String.format(message, fileName, exception.getLocalizedMessage()));
                        } else {
                            final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't export into file '%s'");
                            getEnvironment().messageError(title, String.format(message, fileName));
                        }
                    }
                }
            }
        }
    }    
    
    @SuppressWarnings("unused")
    private void convertCurrentFilter(){
        final IGroupSetting currentSetting = getCurrentSetting();
        if (currentSetting instanceof FilterModel) {
            final FilterModel model = (FilterModel) currentSetting;
            org.radixware.schemas.xscml.Sqml  sqml=model.getFinalCondition();
            if(sqml==null || sqml.getItemList()==null|| sqml.getItemList().isEmpty()){
                final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't convert filter!");
                final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't convert current filter to common filter. Condition must be set.");
                getEnvironment().messageError(title, message);
                
            }else if (model.getDefinition() instanceof RadUserFilter) {
                final RadUserFilter userFilter = (RadUserFilter) model.getDefinition();
               
                final org.radixware.schemas.groupsettings.FilterDocument document =
                org.radixware.schemas.groupsettings.FilterDocument.Factory.newInstance(); 
                CustomFilter xFilter=document.addNewFilter();
                userFilter.writeToXml(xFilter);
                
                Id newId=executeConvertCmd(document);
                if(newId!=null){
                    xFilter.setId(newId);                
                    filters.addCommonFilter(xFilter); 

                    filters.remove(userFilter.getId());                
                    refreshTree();
                }
            }
        }
    }    
    
    private Id executeConvertCmd(final org.radixware.schemas.groupsettings.FilterDocument document){        
        try {           
            final org.radixware.schemas.groupsettings.ConvertFilterRsDocument resp =
                    (org.radixware.schemas.groupsettings.ConvertFilterRsDocument)
                    getEnvironment().getEasSession().executeContextlessCommand(CMD_CONVERT_ID, document, org.radixware.schemas.groupsettings.ConvertFilterRsDocument.class);
            return resp!=null && resp.getConvertFilterRs()!=null ? resp.getConvertFilterRs().getFilterId():null;
        } catch (ServiceClientException ex) {
            getEnvironment().processException(ex);
            return null;
        } catch (InterruptedException ex) {
            return null;
        }
    }

    @SuppressWarnings("unused")
    private void importFilter() {
        final String fileFilter = getEnvironment().getMessageProvider().translate("SelectorAddons", "Xml Document (%s);;All Files (%s)");
        final String fileName =
                QFileDialog.getOpenFileName(this, getEnvironment().getMessageProvider().translate("SelectorAddons", "Import Filter"), QDir.homePath(),
                new QFileDialog.Filter(String.format(fileFilter, "*.xml", "*.*")));
        if (fileName != null && !fileName.isEmpty()) {
            final File xmlFile = new File(fileName);
            if (xmlFile.exists()) {
                final String filterAsStr;
                try {
                    filterAsStr = FileUtils.readTextFile(xmlFile, FileUtils.XML_ENCODING);
                } catch (IOException exception) {
                    final String title = getEnvironment().getMessageProvider().translate("ExplorerError", "Input/Output Exception");
                    if (exception.getLocalizedMessage() != null && !exception.getLocalizedMessage().isEmpty()) {
                        final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't import from file '%s':\n%s");
                        getEnvironment().messageError(title, String.format(message, fileName, exception.getLocalizedMessage()));
                    } else {
                        final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't import from file '%s'");
                        getEnvironment().messageError(title, String.format(message, fileName));
                    }
                    return;
                }
                final RadSelectorPresentationDef selectorPresentation = filters.getGroupModel().getSelectorPresentationDef();
                final RadUserFilter filterDef;
                try {
                    filterDef = RadUserFilter.Factory.loadFromString(filters.getGroupModel().getEnvironment(), filterAsStr, selectorPresentation, false);
                } catch (XmlException exception) {
                    final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't Import Filter");
                    final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "File '%s' has wrong format");
                    getEnvironment().messageError(title, String.format(message, fileName));
                    return;
                } catch (DefinitionError exception) {
                    final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't Import Filter");
                    getEnvironment().messageError(title, exception.getMessage());
                    return;
                }
                if (!filterDef.isValid()) {
                    final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Can't Import Filter");
                    final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Base filter is not exists");
                    getEnvironment().messageError(title, message);
                    return;
                }
                final Collection<String> filterNames = filters.getAllSettingTitles();
                if (filterNames.contains(filterDef.getTitle())) {
                    String filterTitle = filterDef.getTitle() + " 1";
                    for (int i = 2; filterNames.contains(filterTitle); i++) {
                        filterTitle = filterDef.getTitle() + " " + i;
                    }
                    filterDef.setTitle(filterTitle);
                }

                final FilterModel model = new FilterModel(getEnvironment(), filterDef);
                model.setContext(new IContext.Filter(filters.getGroupModel()));
                addCustomSetting(model);
            }
        }
    }

    @Override
    protected void setupToolBar(final QToolBar toolbar) {
        super.setupToolBar(toolbar);
        toolbar.addSeparator();
        exportFilterActon = new QAction(this);
        exportFilterActon.triggered.connect(this, "exportCurrentFilter()");
        exportFilterActon.setIcon(ExplorerIcon.getQIcon(Icons.EXPORT_FILTER));
        exportFilterActon.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Export Filter"));
        exportFilterActon.setShortcuts(StandardKey.Save);
        toolbar.addAction(exportFilterActon);
        importFilterActon = new QAction(this);
        importFilterActon.triggered.connect(this, "importFilter()");
        importFilterActon.setIcon(ExplorerIcon.getQIcon(Icons.IMPORT_FILTER));
        importFilterActon.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Import Filter"));
        importFilterActon.setShortcuts(StandardKey.Open);        
        toolbar.addAction(importFilterActon);     
        
        convertFilterActon = new QAction(this);
        convertFilterActon.triggered.connect(this, "convertCurrentFilter()");
        convertFilterActon.setIcon(ExplorerIcon.getQIcon(Icons.CONVERT_FILTER));
        convertFilterActon.setToolTip(getEnvironment().getMessageProvider().translate("SelectorAddons", "Convert to Common Filter"));
        convertFilterActon.setVisible(getEnvironment().isContextlessCommandAccessible(CMD_CONVERT_ID));
        toolbar.addAction(convertFilterActon);
    }

    @Override
    public void refreshActions() {
        super.refreshActions();
        final IGroupSetting currentSetting = getCurrentSetting();        
        if (currentSetting instanceof FilterModel) {
            final FilterModel filter = ((FilterModel) currentSetting);
            if (filter.getCustomViewId() != null) {
                inheritSettingAction.setEnabled(false);
            }            
            exportFilterActon.setEnabled(filter.isUserDefined() && filter.isValid());
            convertFilterActon.setEnabled(filter.isUserDefined() && filter.isValid());
            inheritSettingAction.setEnabled(inheritSettingAction.isEnabled() && !filter.isCommon());
        } else {
            exportFilterActon.setEnabled(false);
            convertFilterActon.setEnabled(false);
        }
        importFilterActon.setEnabled(createSettingAction.isEnabled());                        
    }

    @Override
    protected String getNewSettingName() {
        return getEnvironment().getMessageProvider().translate("SelectorAddons", "New Filter");
    }

    @Override
    protected boolean confirmToRemoveSetting(String name) {
        final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Confirm Filter Deletion");
        final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Do you really want to delete filter \'%s\'?");
        return getEnvironment().messageConfirmation(title, String.format(message, name));
    }

    @Override
    protected boolean confirmToRemoveGroup(String name) {
        final String title = getEnvironment().getMessageProvider().translate("SelectorAddons", "Confirm Group Deletion");
        final String message = getEnvironment().getMessageProvider().translate("SelectorAddons", "Do you really want to delete group \'%s\' and all its filters?");
        return getEnvironment().messageConfirmation(title, String.format(message, name));
    }
}