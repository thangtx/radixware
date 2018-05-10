/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.editors.editmask.refeditor;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QWidget;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.EEditMaskOption;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.RadClassPresentationDef;
import org.radixware.kernel.common.client.meta.RadSelectorPresentationDef;
import org.radixware.kernel.common.client.meta.RadSortingDef;
import org.radixware.kernel.common.client.meta.filters.RadFilterDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskRef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.client.widgets.IEditMaskEditor;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.IllegalArgumentError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.editors.editmask.IEditMaskEditorSetting;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.schemas.editmask.RadixEditMaskDocument;

public class RefEditMaskEditorWidget extends ExplorerFrame implements IEditMaskEditor {

    private final IClientEnvironment environment;
    private final EnumMap<EEditMaskOption, IEditMaskEditorSetting> widgets = new EnumMap<>(EEditMaskOption.class);
    private RefEntityIdSetting refEntityId = null;
    private RefSelectorPresentationIdSetting refSelectorId = null;
    private RefEditorPresentationIdsSetting refEditorIds = null;
    private RefFilterSetting refFilterId = null;
    private RefSortingSetting refSortingId = null;

    public RefEditMaskEditorWidget(final IClientEnvironment environment, final QWidget parent) {
        super(environment, parent);
        this.environment = environment;
        setUpUi(environment);
        this.setFrameShape(Shape.StyledPanel);
    }

    private void setUpUi(IClientEnvironment environment) {
        QGridLayout mainLayout = new QGridLayout();
        this.setLayout(mainLayout);
        mainLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignTop));

        List<ISqmlDefinition> tables = new LinkedList<>();
        for (ISqmlTableDef table : environment.getSqmlDefinitions().getTables()) {
            try {
                RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(table.getId());
                Collection<Id> selectorPresentationIds = classDef.getSelectorPresentationIds();
                if (!selectorPresentationIds.isEmpty()) {
                    tables.add(table);
                } else {
                    Collection<Id> descendantsIds = environment.getDefManager().getRepository().getDescendantsRecursively(table.getId());
                    for (Id descendantId : descendantsIds) {
                        RadClassPresentationDef descandantClassDef = environment.getDefManager().getClassPresentationDef(descendantId);
                        Collection<Id> descandantSelectorPresentationIds = descandantClassDef.getSelectorPresentationIds();
                        if (!descandantSelectorPresentationIds.isEmpty()) {
                            tables.add(table);
                        }
                    }
                }
            } catch (DefinitionError error) {
            }
        }
        MessageProvider mp = environment.getMessageProvider();
        final SqmlTreeModel entityTreeModel = new SqmlTreeModel(environment, tables,
                EnumSet.noneOf(SqmlTreeModel.ItemType.class));
        entityTreeModel.setDisplayMode(EDefinitionDisplayMode.SHOW_FULL_NAMES);
        refEntityId = new RefEntityIdSetting(environment, this, entityTreeModel);
        mainLayout.addWidget(refEntityId, 0, 1, 1, 1);
        final QLabel entityLabel = new QLabel(mp.translate("EditMaskRef", "Entity:"), this);
        mainLayout.addWidget(entityLabel, 0, 0, 1, 1);
        final SqmlTreeModel selectorTreeModel = new SqmlTreeModel(environment, null,
                EnumSet.noneOf(SqmlTreeModel.ItemType.class));
        selectorTreeModel.setDisplayMode(EDefinitionDisplayMode.SHOW_FULL_NAMES);
        refSelectorId = new RefSelectorPresentationIdSetting(environment, this, selectorTreeModel);
        refSelectorId.setEditorDialogTitle(mp.translate("EditMaskRef", "Choose Selector Presentation"));
        widgets.put(EEditMaskOption.REF_SELECTOR_PRESENTATION_ID, refSelectorId);
        mainLayout.addWidget(refSelectorId, 1, 1, 1, 1);
        final QLabel selectorLabel = new QLabel(mp.translate("EditMaskRef", "Selector Presentation:"), this);
        mainLayout.addWidget(selectorLabel, 1, 0, 1, 1);
        refEditorIds = new RefEditorPresentationIdsSetting(environment, this);
        refEditorIds.setEditorDialogTitle(mp.translate("EditMaskRef", "Choose Editor Presentations"));
        widgets.put(EEditMaskOption.REF_EDITOR_PRESENTATION_ID, refEditorIds);
        mainLayout.addWidget(refEditorIds, 2, 1, 1, 1);
        final QLabel editorLabel = new QLabel(mp.translate("EditMaskRef", "Editor Presentation:"), this);
        mainLayout.addWidget(editorLabel, 2, 0, 1, 1);
        refFilterId = new RefFilterSetting(environment, this);
        refFilterId.setEditorDialogTitle(mp.translate("EditMaskRef", "Choose Default Filter"));
        refFilterId.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING, IListWidget.EFeatures.SELECTION_LABEL));
        widgets.put(EEditMaskOption.REF_FILTER_ID, refFilterId);
        mainLayout.addWidget(refFilterId, 3, 1, 1, 1);
        final QLabel filterLabel = new QLabel(mp.translate("EditMaskRef", "Default filter:"), this);
        mainLayout.addWidget(filterLabel, 3, 0, 1, 1);
        refSortingId = new RefSortingSetting(environment, this);
        refSortingId.setEditorDialogTitle(mp.translate("EditMaskRef", "Choose Default Sorting"));
        refSortingId.setFeatures(EnumSet.of(IListWidget.EFeatures.FILTERING));
        widgets.put(EEditMaskOption.REF_SORTING_ID, refSortingId);
        mainLayout.addWidget(refSortingId, 4, 1, 1, 1);
        final QLabel sortingLabel = new QLabel(mp.translate("EditMaskRef", "Default sorting:"), this);
        mainLayout.addWidget(sortingLabel, 4, 0, 1, 1);
        final QLabel useDropDownLabel = new QLabel(mp.translate("EditMaskRef", "Use drop down list:"), this);
        mainLayout.addWidget(useDropDownLabel, 5, 0, 1, 1);
        RefDropDownListUsageSetting refDropDown = new RefDropDownListUsageSetting(this);
        widgets.put(EEditMaskOption.REF_DROP_DOWN, refDropDown);
        mainLayout.addWidget(refDropDown, 5, 1, 1, 1);
        refEntityId.entityChanged.connect(this, "entityChangedSlot()");
        refSelectorId.selectorPresentationChanged.connect(this, "selectorChangedSlot(RadSelectorPresentationDef)");
        refFilterId.filterChanged.connect(this, "filterChangedSlot(RadFilterDef)");
    }

    @Override
    public EditMask getEditMask() {
        final RadixEditMaskDocument xmlBeanInstance
                = RadixEditMaskDocument.Factory.newInstance();
        final org.radixware.schemas.editmask.EditMask editMask = xmlBeanInstance.addNewRadixEditMask();
        editMask.addNewReference();
        final Set<EEditMaskOption> keySet = widgets.keySet();
        for (EEditMaskOption key : keySet) {
            widgets.get(key).addToXml(editMask);
        }
        return EditMask.loadFrom(xmlBeanInstance);
    }

    @Override
    public void setEditMask(EditMask editMask) {
        if (editMask instanceof EditMaskRef) {
            final org.radixware.schemas.editmask.RadixEditMaskDocument xmlBeanInstance
                    = org.radixware.schemas.editmask.RadixEditMaskDocument.Factory.newInstance();
            final org.radixware.schemas.editmask.EditMask em
                    = xmlBeanInstance.addNewRadixEditMask();
            editMask.writeToXml(em);
            //try to get Entity from selector presentation or editor if selector is not set
            boolean isSelectorPresentationSet = em.getReference().getSelectorPresentationId() != null && !em.getReference().getSelectorPresentationId().toString().isEmpty();
            RadSelectorPresentationDef selectorPresentationDef = null;
            try {
                selectorPresentationDef = getEnvironment().getDefManager().getSelectorPresentationDef(em.getReference().getSelectorPresentationId());
            } catch (Exception ex) {
                //do nothing if not found
            }
            if (isSelectorPresentationSet && selectorPresentationDef != null) {
                Id tableId = selectorPresentationDef.getClassPresentation().getTableId();
                refEntityId.setValue(getEnvironment().getSqmlDefinitions().findTableById(tableId));
            } else if (em.getReference().getEditorPresentationIds() != null && !em.getReference().getEditorPresentationIds().isEmpty()) {
                boolean isValidEditorPresentationSet = false;
                for (Id id : em.getReference().getEditorPresentationIds()) {
                    try {
                        Id tableId = getEnvironment().getDefManager().getEditorPresentationDef(id).getClassPresentation().getTableId();
                        refEntityId.setValue(getEnvironment().getSqmlDefinitions().findTableById(tableId));
                        isValidEditorPresentationSet = true;
                        break;
                    } catch (DefinitionError error) {
                    }
                }
                if (isValidEditorPresentationSet == false) {
                    refSelectorId.setValue(null);
                    refSelectorId.setEnabled(false);
                    refEditorIds.setEnabled(false);
                    refEditorIds.setValue(null);
                }
            } else {
                refSelectorId.setValue(null);
                refSelectorId.setEnabled(false);
                refEditorIds.setEnabled(false);
                refEditorIds.setValue(null);
            }
            final Set<EEditMaskOption> keySet = widgets.keySet();
            for (EEditMaskOption key : keySet) {
                widgets.get(key).loadFromXml(em);
            }
        } else {
            throw new IllegalArgumentError("Instance of EditMaskRef expected");
        }
    }

    @Override
    public void setHiddenOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.hide();
            }
        }
    }

    @Override
    public void setVisibleOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.show();
            }
        }
    }

    @Override
    public void setEnabledOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.setEnabled(true);
            }
        }
    }

    @Override
    public void setDisabledOptions(EnumSet<EEditMaskOption> options) {
        IEditMaskEditorSetting setting = null;
        for (EEditMaskOption key : options) {
            setting = widgets.get(key);
            if (setting != null) {
                setting.setDisabled(true);
            }
        }
    }

    @Override
    public boolean checkOptions() {
        return refEditorIds.check() && refFilterId.check() && refSortingId.check();
    }

    @SuppressWarnings("unused")
    private void entityChangedSlot() {
        refFilterId.setValue(null);
        refFilterId.setEnabled(false);
        refSortingId.setValue(null);
        refSortingId.setEnabled(false);
        refSelectorId.setValue(null);
        refEditorIds.setValue(null);
        if (refEntityId.getValue() != null) {
            List<ListWidgetItem> selectors = new LinkedList<>();
            List<ListWidgetItem> editors = new LinkedList<>();
            RadClassPresentationDef classDef = environment.getDefManager().getClassPresentationDef(refEntityId.getValue().getId());
            Collection<Id> selectorPresentationIds = classDef.getSelectorPresentationIds();
            Collection<Id> editorPresentationIds = classDef.getEditorPresentationIds();

            for (Id selectorId : selectorPresentationIds) {
                selectors.add(refSelectorId.createListWidgetItemFromId(selectorId));
            }

            for (Id editorId : editorPresentationIds) {
                editors.add(refEditorIds.createListWidgetItemFromId(editorId));
            }

            Collection<Id> descendantsIds = environment.getDefManager().getRepository().getDescendantsRecursively(classDef.getId());
            for (Id descendantId : descendantsIds) {
                RadClassPresentationDef descandantClassDef = environment.getDefManager().getClassPresentationDef(descendantId);
                Collection<Id> descandantSelectorPresentationIds = descandantClassDef.getSelectorPresentationIds();
                Collection<Id> descandantEditorPresentationIds = descandantClassDef.getEditorPresentationIds();
                for (Id selectorId : descandantSelectorPresentationIds) {
                    selectors.add(refSelectorId.createListWidgetItemFromId(selectorId));
                }
                for (Id editorId : descandantEditorPresentationIds) {
                    editors.add(refEditorIds.createListWidgetItemFromId(editorId));
                }
            }
            refSelectorId.setItems(selectors);
            refSelectorId.setEnabled(true);
            refEditorIds.setItems(editors);
            refEditorIds.setEnabled(true);
        } else {
            refSelectorId.setEnabled(false);
            refEditorIds.setEnabled(false);
        }
    }

    @SuppressWarnings("Unused")
    private void selectorChangedSlot(RadSelectorPresentationDef selectorRadDef) {
        refFilterId.setValue(null);
        refSortingId.setValue(null);
        if (selectorRadDef != null) {
            List<RadFilterDef> filterIds = new LinkedList<>(selectorRadDef.getFilters());
            List<ListWidgetItem> filtersList = new LinkedList<>();
            for (RadFilterDef radFilterDef : filterIds) {
                ListWidgetItem listWidgetItem = new ListWidgetItem(radFilterDef.getTitle(), radFilterDef);
                RadClassPresentationDef ownerClassDef = environment.getDefManager().getClassPresentationDef(radFilterDef.getOwnerClassId());
                if (ownerClassDef != null) {
                    listWidgetItem.setExtendedTitle(ownerClassDef.getName());
                }
                filtersList.add(listWidgetItem);
            }
            refFilterId.setItems(filtersList);
            refFilterId.setEnabled(true);
            refSortingId.setItems(getSortingList(null));
            refSelectorId.setEnabled(true);
            refSortingId.setEnabled(true);
        } else {
            refFilterId.setEnabled(false);
            refSortingId.setEnabled(false);
        }
    }

    @SuppressWarnings("Unused")
    private void filterChangedSlot(RadFilterDef radFilterdef) {
        refSortingId.setItems(getSortingList(radFilterdef));
        refSortingId.setFilterId(radFilterdef == null ? null : radFilterdef.getId());
    }

    List<ListWidgetItem> getSortingList(RadFilterDef filterDef) {
        List<ListWidgetItem> selectorList = refSelectorId.getValue();
        List<RadSortingDef> radSortList = null;
        List<ListWidgetItem> sortingList = new LinkedList<>();
        if (selectorList != null && !selectorList.isEmpty()) {
            RadSelectorPresentationDef classDef = (RadSelectorPresentationDef)selectorList.get(0).getValue();
            if (classDef != null) {
                radSortList = new LinkedList<>(classDef.getSortings());
            } 
        }
        if (radSortList != null && !radSortList.isEmpty()) {
            if (filterDef != null) {
                for (Iterator<RadSortingDef> it = radSortList.iterator(); it.hasNext();) {
                    RadSortingDef sorting = it.next();
                    if (!filterDef.isBaseSortingEnabledById(sorting.getId())) {
                        it.remove();
                    }
                }
            }
            for (RadSortingDef radSorting : radSortList) {
                ListWidgetItem listWidgetItem = new ListWidgetItem(radSorting.getTitle(), radSorting);
                RadClassPresentationDef ownerClassDef = environment.getDefManager().getClassPresentationDef(radSorting.getOwnerClassId());
                if (ownerClassDef != null) {
                    listWidgetItem.setExtendedTitle(ownerClassDef.getName());
                }
                sortingList.add(listWidgetItem);
            }
        }
        return sortingList;
    }
}
