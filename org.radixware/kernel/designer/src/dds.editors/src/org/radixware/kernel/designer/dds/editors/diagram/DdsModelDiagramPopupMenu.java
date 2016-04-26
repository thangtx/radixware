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

package org.radixware.kernel.designer.dds.editors.diagram;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsExtTableDef;
import org.radixware.kernel.common.defs.dds.DdsLabelDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.IPlacementSupport;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.dds.editors.diagram.widgets.DdsEditorsManager;


class DdsModelDiagramPopupMenu implements ActionListener, PopupMenuProvider {

    private final DdsModelDiagram diagram;
    private final DdsModelDef model;
    private Point popupLocation = null;

    public DdsModelDiagramPopupMenu(DdsModelDiagram diagram, DdsModelDef model) {
        this.diagram = diagram;
        this.model = model;
    }
    private final static String ADD_TABLE_ACTION = "ADD_TABLE_ACTION";
    private final static String ADD_TABLE_OVERWRITE_ACTION = "ADD_TABLE_OVERWRITE_ACTION";
    private final static String ADD_VIEW_ACTION = "ADD_VIEW_ACTION";
    private final static String ADD_SHORTCUT_TO_TABLE_ACTION = "ADD_SHORTCUT_TO_TABLE";
    private final static String ADD_REFERENCE_ACTION = "ADD_REFERENCE";
    private final static String ADD_SEQUENCE_ACTION = "ADD_SEQUENCE";
    private final static String ADD_LABEL_ACTION = "ADD_LABEL";

    protected JMenuItem addMenuItem(JPopupMenu menu, String codeName, String displayName, RadixIcon icon) {
        final boolean enabled = !model.isReadOnly();
        final JMenuItem item = new JMenuItem(displayName);
        item.setActionCommand(codeName);
        item.setIcon(icon.getIcon());
        item.addActionListener(this);
        item.setEnabled(enabled);
        menu.add(item);
        return item;
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        diagram.setSelectedObjects(Collections.emptySet());

        this.popupLocation = localLocation;

        final JPopupMenu popupMenu = DialogUtils.createPopupMenu(diagram.getView());

        popupMenu.addSeparator();
        addMenuItem(popupMenu, ADD_TABLE_ACTION, "Add Table...", DdsDefinitionIcon.TABLE);

        final Module overwtirrenModule = model.getModule().findOverwritten();
        if (overwtirrenModule != null) {
            addMenuItem(popupMenu, ADD_TABLE_OVERWRITE_ACTION, "Add Table-Overwrite...", DdsDefinitionIcon.TABLE_OVERWRITE);
        }

        addMenuItem(popupMenu, ADD_VIEW_ACTION, "Add View...", DdsDefinitionIcon.VIEW);
        addMenuItem(popupMenu, ADD_SHORTCUT_TO_TABLE_ACTION, "Add Shortcut To Table...", DdsDefinitionIcon.EXT_TABLE);
        addMenuItem(popupMenu, ADD_REFERENCE_ACTION, "Add Reference...", DdsDefinitionIcon.REFERENCE);
        addMenuItem(popupMenu, ADD_SEQUENCE_ACTION, "Add Sequence...", DdsDefinitionIcon.SEQUENCE);
        addMenuItem(popupMenu, ADD_LABEL_ACTION, "Add Label...", DdsDefinitionIcon.LABEL);

        return popupMenu;
    }

    private <T extends DdsDefinition> void add(T newDefinition, DdsDefinitions<T> container) {
        if (newDefinition instanceof IPlacementSupport) {
            final IPlacementSupport placementSupport = (IPlacementSupport) newDefinition;
            placementSupport.getPlacement().setPosX((int) (popupLocation.x / diagram.getScaleFactor()));
            placementSupport.getPlacement().setPosY((int) (popupLocation.y / diagram.getScaleFactor()));
        }
        container.add(newDefinition);
        DbNameUtils.updateAutoDbNames(newDefinition);

        if (!(newDefinition instanceof DdsExtTableDef)) {
            if (DdsEditorsManager.open(newDefinition)) {
                diagram.update();
            } else {
                newDefinition.delete();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        final String actionCodeName = actionEvent.getActionCommand();

        if (ADD_TABLE_ACTION.equals(actionCodeName)) {
            final DdsTableDef table = DdsTableDef.Factory.newInstance("NewTable");
            add(table, model.getTables());
        } else if (ADD_TABLE_OVERWRITE_ACTION.equals(actionCodeName)) {
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.forOverwrite(model, DdsVisitorProviderFactory.newTableProvider());
            final Definition overwrittenTable = ChooseDefinition.chooseDefinition(cfg);
            if (overwrittenTable instanceof DdsTableDef) {
                DdsTableDef table = DdsTableDef.Factory.newOverwrite((DdsTableDef) overwrittenTable);
                add(table, model.getTables());
            }
        } else if (ADD_VIEW_ACTION.equals(actionCodeName)) {
            final DdsViewDef view = DdsViewDef.Factory.newInstance("NewView");
            add(view, model.getViews());
        } else if (ADD_SHORTCUT_TO_TABLE_ACTION.equals(actionCodeName)) {
            final ChooseDefinitionCfg cfg = ChooseDefinitionCfg.Factory.newInstance(model, DdsVisitorProviderFactory.newTableProvider());
            final Definition sourceTable = ChooseDefinition.chooseDefinition(cfg);
            if (sourceTable instanceof DdsTableDef) {
                DdsExtTableDef extTable = DdsExtTableDef.Factory.newInstance((DdsTableDef) sourceTable);
                add(extTable, model.getExtTables());
            }
        } else if (ADD_SEQUENCE_ACTION.equals(actionCodeName)) {
            final DdsSequenceDef sequence = DdsSequenceDef.Factory.newInstance("NewSequence");
            add(sequence, model.getSequences());
        } else if (ADD_LABEL_ACTION.equals(actionCodeName)) {
            final DdsLabelDef label = DdsLabelDef.Factory.newInstance();
            add(label, model.getLabels());
        } else if (ADD_REFERENCE_ACTION.equals(actionCodeName)) {
            DialogUtils.messageInformation("Hold ALT button (Ctrl+Alt in Linux) and drag source(child) table to target(parent) table on diagram.");
        } else {
            throw new IllegalStateException("Unknown action: " + actionCodeName);
        }
    }
}
