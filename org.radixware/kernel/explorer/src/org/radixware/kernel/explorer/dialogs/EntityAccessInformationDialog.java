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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ItemDataRole;
import com.trolltech.qt.core.Qt.ItemFlag;
import com.trolltech.qt.core.Qt.WidgetAttribute;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.*;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.DefManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadEditorPresentationDef;
import org.radixware.kernel.common.client.meta.RadPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMaskNone;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinitionsFilter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.types.Reference;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.exceptions.ServiceClientException;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.utils.html.HtmlBuilder;
import org.radixware.kernel.explorer.editors.valeditors.MultiValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValRefEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValSqmlDefEditor;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.schemas.entityaccessinfo.*;

public class EntityAccessInformationDialog extends ExplorerDialog {

    
    private enum Filter {BY_TITLE, BY_ID};
    
    private final class CustomFilterModel extends QSortFilterProxyModel {
        private String filterExp = "";
        private Filter filterBy = Filter.BY_TITLE;

        private CustomFilterModel(QAbstractItemModel model) {
            super(model);
            setFilterKeyColumn(0);
            
        }
        @Override
        protected boolean filterAcceptsRow(int source_row, QModelIndex source_parent) {
            final QModelIndex index = sourceModel().index(source_row, 0, source_parent);
            if(index != null) {
                if(filterBy == Filter.BY_ID) {
                    final RadPresentationDef pres = (RadPresentationDef) source_parent.data(ItemDataRole.UserRole);
                    final String id = (pres==null) ? "" : pres.getId().toString();
                    return filterExp.isEmpty() || id.contains(filterExp);
                }

                if(filterBy == Filter.BY_TITLE) {
                    final String text = (String) source_parent.data();
                    return filterExp.isEmpty() || text.contains(filterExp);
                }
            }
            return super.filterAcceptsRow(source_row, source_parent);
        }
        
        public void updateFilter(final String filter) {
            this.filterExp = filter;
            
        }
        
        public void updateFilter(final Filter filter) {
            this.filterBy = filter;
        }
    } 
    
    private final class Tree extends QTreeWidget {
        
        public void addTopLevelItem(final RadClassPresentationDef classPresentation) {
            final List<QTreeWidgetItem> selPresentations = getSelectorPresentations(classPresentation);
            final List<QTreeWidgetItem> editorPresentations = getEditorPresentations(classPresentation);
            if(selPresentations.isEmpty() && editorPresentations.isEmpty()) return;
            
            final QTreeWidgetItem item = new QTreeWidgetItem();
            item.setText(0, classPresentation.getSimpleName());
            item.setIcon(0, ExplorerIcon.getQIcon(classPresentation.getIcon()));
            item.setFlags(ItemFlag.ItemIsEnabled);
                        
            if(!selPresentations.isEmpty()) {
                final QTreeWidgetItem selPresentationExpander = new QTreeWidgetItem();
                selPresentationExpander.setText(0, "Selector Presentations");
                selPresentationExpander.setFlags(ItemFlag.ItemIsEnabled);
                selPresentationExpander.addChildren(selPresentations);
                item.addChild(selPresentationExpander);
            }
                        
            if(!editorPresentations.isEmpty()) {
                final QTreeWidgetItem edPresentationExpander = new QTreeWidgetItem();
                edPresentationExpander.setText(0, "Editor Presentations");
                edPresentationExpander.setFlags(ItemFlag.ItemIsEnabled);
                edPresentationExpander.addChildren(editorPresentations);
                item.addChild(edPresentationExpander);
            }
            
            
            addTopLevelItem(item);
        }
        
        private List<QTreeWidgetItem> getSelectorPresentations(final RadClassPresentationDef classPres) {
            final List<QTreeWidgetItem> result = new LinkedList<QTreeWidgetItem>();
            final List<Id> presentations = new ArrayList<Id>(classPres.getSelectorPresentationIds());
            final DefManager defManager = EntityAccessInformationDialog.this.environment.getDefManager();
            
            for(Id id : presentations) {
                final RadSelectorPresentationDef selPresentation = defManager.getSelectorPresentationDef(id);
                final QTreeWidgetItem item = new QTreeWidgetItem();
                item.setText(0, selPresentation.getName());
                item.setIcon(0, ExplorerIcon.getQIcon(selPresentation.getIcon()));
                item.setData(0, ItemDataRole.UserRole, selPresentation);
                result.add(item);
            }
            
            return result;
        } 
        
        private List<QTreeWidgetItem> getEditorPresentations(final RadClassPresentationDef classPres) {
            final List<QTreeWidgetItem> result = new LinkedList<QTreeWidgetItem>();
            final List<Id> presentations = new ArrayList<Id>(classPres.getEditorPresentationIds());
            final DefManager defManager = EntityAccessInformationDialog.this.environment.getDefManager();
            
            for(Id id : presentations) {
                final RadEditorPresentationDef editorPresentation = defManager.getEditorPresentationDef(id);
                final QTreeWidgetItem item = new QTreeWidgetItem();
                item.setText(0, editorPresentation.getName());
                item.setIcon(0, ExplorerIcon.getQIcon(editorPresentation.getIcon()));
                item.setData(0, ItemDataRole.UserRole, editorPresentation);
                result.add(item);
            }
            
            return result;
        }
    }
    
    private final class FilteredTreeWidget extends QGroupBox {
        private final Tree tree ;
        private QRadioButton optionTitle;
        private QRadioButton optionId;
        private CustomFilterModel filterModel;
        
        public FilteredTreeWidget(final QWidget parent, final Tree tree) {
            super(parent);
            
            this.tree = tree;
//            PresentationDefTreeModel treeModel = new PresentationDefTreeModel();
//            filterModel = new CustomFilterModel(tree.model());
            tree.setSizePolicy(Policy.Expanding, Policy.Expanding);
            setUpUi();
            
        }
        
        private void setUpUi() {
            final QVBoxLayout layout = new QVBoxLayout();
            layout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
            this.setLayout(layout);
            
            layout.addWidget(tree);
//            layout.addLayout(buildSearchBox());
//            
//            optionTitle = new QRadioButton("Title", this);
//            layout.addWidget(optionTitle);
//            optionTitle.clicked.connect(this, "onFilterOptionChanged()");
//            optionTitle.setChecked(true);
//            
//            optionId = new QRadioButton("Id", this);
//            layout.addWidget(optionId);
//            optionId.clicked.connect(this, "onFilterOptionChanged()");
        }
        
        private QLayout buildSearchBox() {
            final QHBoxLayout layout = new QHBoxLayout();
            layout.setMargin(0);
            
            final QLineEdit lineEdit = new QLineEdit(this);
            lineEdit.textChanged.connect(filterModel, "updateFilter(String)");
            layout.addWidget(lineEdit);
            
            final QToolButton searchButton = new QToolButton(this);
            searchButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
            layout.addWidget(searchButton);
            
            return layout;
        }

        private void onFilterOptionChanged() {
            if(optionTitle.isChecked()) {
                filterModel.updateFilter(Filter.BY_TITLE);
                return;
            }
            if(optionId.isChecked()) {
                filterModel.updateFilter(Filter.BY_ID);
                return;
            }
        }
    }
    
    private final static String USERS_PRESENTATION_ID = "sprZONO647CNRGC5O5GHBHD6F7C7A";    
    private final static String ADMIN_USER = "ADMINISTRATOR";
    private final static String COMMAND = "clc5RUZGNI7EZFUJLSX3363K37MMY";
    
    private final IClientEnvironment environment;
    private final Tree tree = new Tree();
    private ValRefEditor usernames;
    private MultiValEditor objectIdEditor;
    private boolean isIdEditorSelector = false;
    private ValSqmlDefEditor tableChooser;
    
    public EntityAccessInformationDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        setAttribute(WidgetAttribute.WA_DeleteOnClose);
        setWindowTitle(environment.getMessageProvider().translate("EntityAccessInformationDialog","Access Rights Information"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.TESTER));
        this.environment = environment;
        setUpUi();
    }

    private void setUpUi() {
        layout().setAlignment(new Alignment(AlignmentFlag.AlignTop));
        final MessageProvider messageProvider = environment.getMessageProvider();
        
        // Entity selector label
        final QLabel lblTable = 
                new QLabel(messageProvider.translate("EntityAccessInformationDialog","Table:"), (QWidget)this);
        layout().addWidget(lblTable);
        // Entity object selector
        final SqmlTreeModel treeModel = new SqmlTreeModel(environment, null, EnumSet.noneOf(SqmlTreeModel.ItemType.class));
        tableChooser = new ValSqmlDefEditor(environment, this, treeModel, false, false);
        tableChooser.setDefinitionsFilter(new ISqmlDefinitionsFilter() {
            @Override
            public boolean isAccepted(final ISqmlDefinition definition, final ISqmlDefinition ownerDefinition) {
                final ISqmlTableDef tableDef = (ISqmlTableDef) definition;
                return tableDef.hasEntityClass();
            }
        });
        tableChooser.setDefinitionDisplayMode(EDefinitionDisplayMode.SHOW_SHORT_NAMES);
        tableChooser.valueChanged.connect(this, "onClassSelect(Object)");
        layout().addWidget(tableChooser);
        
        tree.setHeaderHidden(true);
        tree.clicked.connect(this, "onSelectPresentation(QModelIndex)");
        final FilteredTreeWidget treeWidget = new FilteredTreeWidget(this, tree);
        treeWidget.setTitle(messageProvider.translate("EntityAccessInformationDialog","Presentations:"));
        layout().addWidget(treeWidget);
        // Users
        final QLabel lblUsernames = 
                new QLabel(messageProvider.translate("EntityAccessInformationDialog", "Users:"), this);
        usernames = new ValRefEditor(environment, this);
        final RadSelectorPresentationDef usersSelPres = environment.getDefManager().getSelectorPresentationDef(Id.Factory.loadFrom(USERS_PRESENTATION_ID));
        usernames.setSelectorPresentation(usersSelPres);
        usernames.setEnabled(environment.getUserName().equals(ADMIN_USER));
        layout().addWidget(lblUsernames);
        layout().addWidget(usernames);
        // Id
        final QLabel lblId =
                new QLabel(messageProvider.translate("EntityAccessInformationDialog", "Id:"));
        
        objectIdEditor = new MultiValEditor(environment, this);
        objectIdEditor.showValEditor(EValType.STR, new EditMaskStr(), false, false);
        objectIdEditor.setSizePolicy(Policy.Expanding, Policy.Preferred);
        
        
        layout().addWidget(lblId);
        layout().addWidget(objectIdEditor);
        
        addButton(EDialogButtonType.OK).setTitle("Get info");
        acceptButtonClick.connect(this,"onGet()");
    }
    
    @SuppressWarnings("unused")
    private void onClassSelect(final Object selectedTable) {
        final List<Id> descendants = new ArrayList<Id>();
        //tree.clear();
        if(selectedTable != null) {
            final ISqmlDefinition tableDefinition = (ISqmlDefinition) selectedTable;
            descendants.add(tableDefinition.getId());
            descendants.addAll(
                environment
                    .getDefManager()
                    .getRepository()
                    .getDescendantsRecursively(tableDefinition.getId())
            );
        }
        tree.setHidden(descendants.isEmpty());
        showSelectorPresentations(descendants);
    }

    @SuppressWarnings("unused")
    private void onSelectPresentation(final QModelIndex itemIndex) {
        final RadPresentationDef presentation = (RadPresentationDef) itemIndex.data(ItemDataRole.UserRole);
        if(presentation instanceof RadSelectorPresentationDef) {
            if(objectIdEditor.getCurrentValType() != EValType.PARENT_REF) {
                objectIdEditor.showValEditor(EValType.PARENT_REF, new EditMaskNone(), false, false);
            }
            ((ValRefEditor)objectIdEditor.getCurrentValEditor()).setSelectorPresentation((RadSelectorPresentationDef)presentation);
        } else {
            if(objectIdEditor.getCurrentValType() != EValType.STR) {
                objectIdEditor.showValEditor(EValType.STR, new EditMaskStr(), false, false);
            }
        }
    }
    
    private void showSelectorPresentations(final List<Id> descendants) {
//        final List<RadClassPresentationDef> rcpd = new LinkedList<RadClassPresentationDef>();
        tree.clear();
        for(Id id: descendants) {
            try {
                final RadClassPresentationDef classPresentation = environment.getDefManager().getClassPresentationDef(id);
                tree.addTopLevelItem(classPresentation);
//                rcpd.add(classPresentation);
            } catch(DefinitionNotFoundError ex) {
                environment.getTracer().debug(ex.getLocalizedMessage());
            }
        }
//        ((PresentationDefTreeModel)tree.model()).fillModel(rcpd);
    }
    
    @SuppressWarnings("unused")
    private void onGet() {
        
        if(!isComplete()) {
            final String message = getEnvironment().getMessageProvider().translate(
                    "EntityAccessInformationDialog", "Please, fill all parameters.");
            getEnvironment().messageWarning(message);
            return;
        }
        
        final Id command = Id.Factory.loadFrom(COMMAND);
        try {
            final EntityAccessInfoRqDocument rq = EntityAccessInfoRqDocument.Factory.newInstance();
            final AcsEntityAccessRq request = rq.addNewEntityAccessInfoRq();
            request.setUsername(usernames.getValue().getTitle());
            request.setClassId(Id.Factory.changePrefix(tableChooser.getValue().getId(), EDefinitionIdPrefix.DDS_TABLE));
            final Object selectedObject = objectIdEditor.getCurrentValEditor().getValue();
            request.setObjectPid(objectIdEditor.getCurrentValType() == EValType.STR
                    ? (String)selectedObject
                    : ((Reference)selectedObject).getPid().toString());
            
            
            final org.radixware.schemas.entityaccessinfo.EntityAccessInfoRpDocument resp =
                    (org.radixware.schemas.entityaccessinfo.EntityAccessInfoRpDocument)
                    getEnvironment().getEasSession().executeContextlessCommand(command, rq, org.radixware.schemas.entityaccessinfo.EntityAccessInfoRpDocument.class);
                        
            final AcsEntityAccessResp accessInfo = resp.getEntityAccessInfoRp();
            
            final HtmlBuilder html = getInfoAsHtml(accessInfo);
            System.out.println(html.getHtml());
        } catch (ServiceClientException ex) {
            environment.processException(ex);
        } catch (InterruptedException ex) {
        }
    }
    
    private HtmlBuilder beforeHtmlBuild() {
        final HtmlBuilder html = new HtmlBuilder();
        html.append("<html><head><title>ACS Info</title>");
        html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        html.append("</head><body>");
        html.startTag("table border=2 cellpadding=4");
        
        return html;
    }
    
    private void afterHtmlBuild(final HtmlBuilder html) {
        html.endTable();
        html.append("</body></html>");
    }
    
    public HtmlBuilder getInfoAsHtml(final AcsEntityAccessResp accessInfo) {
        System.out.println(accessInfo.xmlText());
        final HtmlBuilder html = beforeHtmlBuild();
        html.startRow();
        html.appendCell(HtmlBuilder.makeBold("Group name"));
        html.appendCell(HtmlBuilder.makeBold("Role title"));
        for(String s : accessInfo.getApfNamesList()) {
            html.appendCell(HtmlBuilder.makeBold(s));
        }
        
        html.endRow();
        for (Group g : accessInfo.getGroupsList()) { 
            
            if(g.sizeOfAreasArray() == 0) continue;
            html.startRow();
            html.startTag("td rowspan=" + g.sizeOfAreasArray());
            html.append(g.getName());
            html.endTag("td");

            for(Area a : g.getAreasList()) {
                html.appendCell(a.getRoleId());
                for(Coordinate c : a.getCoordinatesList()) {
                    if(c.isNil()) continue;
                    if(Long.valueOf(1L).equals(c.getApMode())) {
                        html.appendCell(c.getApKey());
                    } else if(Long.valueOf(0L).equals(c.getApMode())) {
                        html.appendCell("Unlimited");
                    } else {

                        html.appendCell("Restricted");
                    }

                }
            }
            html.endRow();
        }
        html.endTable();
        afterHtmlBuild(html);
        
        QDialog dlg = new QDialog();
        dlg.setWindowFlags(Qt.WindowType.Dialog);
        dlg.setLayout(new QVBoxLayout());
        QLabel lbl = new QLabel(dlg);
        dlg.layout().addWidget(lbl);
        lbl.setText(html.getHtml());
        dlg.exec();
        
        return html;
    }
    
    private boolean isComplete() {
        return tableChooser.getValue() != null && objectIdEditor.getCurrentValEditor().getValue() != null && usernames.getValue() != null;
    }
}
