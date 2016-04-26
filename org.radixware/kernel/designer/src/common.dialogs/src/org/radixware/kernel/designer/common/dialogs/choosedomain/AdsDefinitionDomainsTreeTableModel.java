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

package org.radixware.kernel.designer.common.dialogs.choosedomain;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.jdesktop.swingx.treetable.AbstractTreeTableModel;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.general.utils.DefinitionsUtils;


class AdsDefinitionDomainsTreeTableModel extends AbstractTreeTableModel {

    private Map<Id, AdsDomainDef> id2def;
    private Set<Id> selectedIds;
    private DefaultMutableTreeTableNode rootNode = new DefaultMutableTreeTableNode();
    private boolean isMultipleSelectionAllowed;
    private static final int COLUMNS_COUNT = 2;
    public static final int NAME_COLUMN_INDEX = 0;
    public static final int CHECKBOX_COLUMN_INDEX = 1;
    private static final String NAME_COLUMN_TITLE = "Domain";
    private static final String CHECKBOX_COLUMN_TITLE = "Select";
    private boolean readOnly = false;

    public AdsDefinitionDomainsTreeTableModel(RadixObject radixObject, boolean isMultipleSelectionAllowed, boolean fillValuesFromContext) {
        this.isMultipleSelectionAllowed = isMultipleSelectionAllowed;
        final Collection<Definition> definitions = DefinitionsUtils.collectTopAround(radixObject, new AdsVisitorProvider() {

            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsDomainDef;
            }
        });
        final int count = definitions.size();
        final AdsDomainDef[] availableDomains = new AdsDomainDef[count];
        definitions.toArray(availableDomains);
        Id[] selectedDomainIds = null;
        if (radixObject instanceof AdsDefinition) {
            AdsDefinition adsDefinition = (AdsDefinition) radixObject;
            selectedDomainIds = adsDefinition.getDomains().getDomaindIds();
        }
        id2def = new HashMap<Id, AdsDomainDef>();
        selectedIds = new HashSet<Id>();
        for (AdsDomainDef domainDef : availableDomains) {

            final Id domainDefId = domainDef.getId();
            id2def.put(domainDefId, domainDef);

            if (fillValuesFromContext && selectedDomainIds != null) {
                for (Id definitionDomainId : selectedDomainIds) {
                    if (domainDefId.equals(definitionDomainId)) {
                        selectedIds.add(domainDefId);
                        break;
                    }
                }
            }

            //build all nodes
            if (domainDef.getOwnerDomain() == null) {
                //it is a top adsDomainDef definition
                final DefaultMutableTreeTableNode topLevelNode = new DefaultMutableTreeTableNode(domainDef);
                appendChildren(topLevelNode, domainDef);
                rootNode.add(topLevelNode);
            }
        }
        if (selectedDomainIds != null) {
            for (Id id : selectedDomainIds) {
                boolean found = false;
                for (AdsDomainDef availableDomain : availableDomains) {
                    if (id.equals(availableDomain.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    rootNode.add(new DefaultMutableTreeTableNode(id));
                    if (fillValuesFromContext) {
                        selectedIds.add(id);
                    }
                }
            }
        }
    }

    private static void appendChildren(DefaultMutableTreeTableNode node, AdsDomainDef domain) {
        final Definitions<AdsDomainDef> kids = domain.getChildDomains();
        if (kids != null) {
            for (AdsDomainDef xAdsDomainDef : kids) {
                final DefaultMutableTreeTableNode childNode = new DefaultMutableTreeTableNode(xAdsDomainDef);
                appendChildren(childNode, xAdsDomainDef);
                node.add(childNode);
            }
        }
    }

    @Override
    public Object getValueAt(Object node, int column) {
        assert (node instanceof DefaultMutableTreeTableNode);
        if (column == NAME_COLUMN_INDEX) {
            return ((DefaultMutableTreeTableNode) node).getUserObject();
        } else {
            assert (column == CHECKBOX_COLUMN_INDEX);
            return selectedIds.contains(getIdFromNode(node));
        }
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
        assert (column == CHECKBOX_COLUMN_INDEX);
        final Boolean flag = (Boolean) value;
        final Id id = getIdFromNode(node);
        if (flag) {
            selectedIds.add(id);
        } else {
            selectedIds.remove(id);
        }
    }

    private Id getIdFromNode(Object node) {
        Object userObject = ((DefaultMutableTreeTableNode) node).getUserObject();
        if (userObject instanceof AdsDomainDef) {
            return ((AdsDomainDef) userObject).getId();
        } else if (userObject instanceof Id) {
            return (Id) userObject;
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    public boolean isLeaf(Object node) {
        assert (node instanceof DefaultMutableTreeTableNode);
        return ((DefaultMutableTreeTableNode) node).isLeaf();
    }

    @Override
    public int getColumnCount() {
        return isMultipleSelectionAllowed ? COLUMNS_COUNT : COLUMNS_COUNT - 1;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            return -1;
        }
        return ((DefaultMutableTreeTableNode) parent).getIndex((DefaultMutableTreeTableNode) child);
    }

    @Override
    public int getChildCount(Object parent) {
        assert (parent instanceof DefaultMutableTreeTableNode);
        return ((DefaultMutableTreeTableNode) parent).getChildCount();
    }

    @Override
    public Object getChild(Object parent, int index) {
        assert (parent instanceof DefaultMutableTreeTableNode);
        return ((DefaultMutableTreeTableNode) parent).getChildAt(index);
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == NAME_COLUMN_INDEX) {
            return String.class;
        } else {
            return Boolean.class;
        }
    }

    @Override
    public String getColumnName(int column) {
        if (column == NAME_COLUMN_INDEX) {
            return NAME_COLUMN_TITLE;
        } else {
            assert (column == CHECKBOX_COLUMN_INDEX);
            return CHECKBOX_COLUMN_TITLE;
        }
    }

    @Override
    public Object getRoot() {
        return rootNode;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return (!readOnly) && (column == CHECKBOX_COLUMN_INDEX);
    }

    public Set<AdsDomainDef> getSelectedDomains() {
        Set<AdsDomainDef> selectedDomains = new HashSet<AdsDomainDef>();
        for (Id id : selectedIds) {
            AdsDomainDef selectedDomainDef = id2def.get(id);
            if (selectedDomainDef != null) {
                selectedDomains.add(selectedDomainDef);
            }
        }
        return selectedDomains;
    }

    public Set<Id> getSelectedDomainIds() {
        return selectedIds;
    }
}
