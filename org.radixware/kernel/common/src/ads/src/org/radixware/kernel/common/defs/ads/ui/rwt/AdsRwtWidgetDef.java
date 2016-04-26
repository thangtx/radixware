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
package org.radixware.kernel.common.defs.ads.ui.rwt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.ui.*;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.ui.Action;
import org.radixware.schemas.ui.Widget;

public class AdsRwtWidgetDef extends AdsUIItemDef implements IJavaSource {

    private final class AdsWidgetJavaSourceSupport extends JavaSourceSupport { //by BAO

        public AdsWidgetJavaSourceSupport() {
            super(AdsRwtWidgetDef.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new CodeWriter(AdsWidgetJavaSourceSupport.this, purpose) {
                @Override
                public boolean writeCode(CodePrinter printer) {
                    return false;
                }

                @Override
                public void writeUsage(CodePrinter printer) {
                    AdsAbstractUIDef.WrittenWidgetSupport support = (AdsAbstractUIDef.WrittenWidgetSupport) printer.getProperty(AdsAbstractUIDef.WrittenWidgetSupport.class);
                    if (support == null) {
                        support = new AdsAbstractUIDef.WrittenWidgetSupport();
                        printer.putProperty(AdsAbstractUIDef.WrittenWidgetSupport.class, support);
                    }
                    support.writeWidgetUsage(AdsRwtWidgetDef.this, printer);
                }
            };
        }
    }

    public final class PanelGrid {

        public class Row {

            public class Cell {

                private Id widgetId;
                private boolean hFit;
                private boolean vFit;

                public AdsRwtWidgetDef findWidget() {
                    if (widgetId == null) {
                        return null;
                    } else {
                        return widgets.findById(widgetId);
                    }
                }

                public void setWidget(AdsRwtWidgetDef widget) {
                    if (widget != null) {
                        this.widgetId = widget.getId();
                        widgets.add(widget);
                        setEditState(EEditState.MODIFIED);
                    } else {
                        AdsRwtWidgetDef w = findWidget();
                        if (w != widget) {
                            w.delete();
                        } else {
                            unsetWidget();
                        }
                        setEditState(EEditState.MODIFIED);
                    }
                }

                public Row getRow() {
                    return Row.this;
                }

                public void unsetWidget() {
                    widgetId = null;
                    setEditState(EEditState.MODIFIED);
                }

                private void appendTo(org.radixware.schemas.ui.PanelGrid.Row.Cell xDef) {
                    if (widgetId != null) {
                        xDef.setWidgetId(widgetId);
                    }
                    if (hFit) {
                        xDef.setHFit(true);
                    }
                    if (vFit) {
                        xDef.setVFit(vFit);
                    }
                }

                private void loadFrom(org.radixware.schemas.ui.PanelGrid.Row.Cell xDef) {
                    this.widgetId = xDef.getWidgetId();
                    if (xDef.isSetHFit()) {
                        this.hFit = xDef.getHFit();
                    }
                    if (xDef.isSetVFit()) {
                        this.vFit = xDef.getVFit();
                    }
                }

                public boolean isHFit() {
                    return hFit;
                }

                public void setHFit(boolean hFit) {
                    if (this.hFit != hFit) {
                        this.hFit = hFit;
                        setEditState(EEditState.MODIFIED);
                    }
                }

                public boolean isVFit() {
                    return vFit;
                }

                public void setVFit(boolean vFit) {
                    if (this.vFit != vFit) {
                        this.vFit = vFit;
                        setEditState(EEditState.MODIFIED);
                    }
                }
            }
            private List<Cell> cells = new LinkedList<Cell>();

            public Cell addCell() {
                Cell cell = new Cell();
                cells.add(cell);
                setEditState(EEditState.MODIFIED);
                return cell;
            }

            public void removeCell(Cell cell) {
                if (cells.remove(cell)) {
                    AdsRwtWidgetDef widget = cell.findWidget();
                    if (widget != null) {
                        widget.delete();
                    }
                    setEditState(EEditState.MODIFIED);
                }
            }

            public List<Cell> getCells() {
                return new ArrayList<Cell>(cells);
            }

            private void appendTo(org.radixware.schemas.ui.PanelGrid.Row xDef) {
                for (Cell cell : cells) {
                    cell.appendTo(xDef.addNewCell());
                }
            }

            private void loadFrom(org.radixware.schemas.ui.PanelGrid.Row xDef) {
                for (org.radixware.schemas.ui.PanelGrid.Row.Cell xCell : xDef.getCellList()) {
                    Cell cell = new Cell();
                    cell.loadFrom(xCell);
                    cells.add(cell);
                }
            }
        }
        private List<Row> rows = new LinkedList<Row>();

        public Row addRow() {
            Row row = new Row();
            rows.add(row);
            setEditState(EEditState.MODIFIED);
            return row;
        }

        public void removeRow(Row row) {
            if (rows.remove(row)) {
                for (Row.Cell cell : row.cells) {
                    row.removeCell(cell);
                }
                setEditState(EEditState.MODIFIED);
            }
        }

        public List<Row> getRows() {
            return new ArrayList<Row>(rows);
        }

        public Row.Cell findCellForWidget(AdsRwtWidgetDef w) {
            if (w == null) {
                return null;
            }
            for (Row r : rows) {
                for (Row.Cell c : r.cells) {
                    if (c.widgetId == w.getId()) {
                        return c;
                    }
                }
            }
            return null;
        }

        private void appendTo(org.radixware.schemas.ui.PanelGrid xDef) {
            for (Row r : rows) {
                r.appendTo(xDef.addNewRow());
            }
        }

        public boolean isEmpty() {
            return rows.isEmpty();
        }

        private void loadFrom(org.radixware.schemas.ui.PanelGrid xDef) {

            for (org.radixware.schemas.ui.PanelGrid.Row xRow : xDef.getRowList()) {
                Row row = new Row();
                row.loadFrom(xRow);
                rows.add(row);
            }

        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() { // BY BAO
        return new AdsWidgetJavaSourceSupport();
    }

    private static class Widgets extends AdsDefinitions<AdsRwtWidgetDef> {

        Widgets(AdsDefinition owner) {
            super(owner);
        }

        private void loadFrom(Widget xWidget) {
            for (Widget c : xWidget.getWidgetList()) {
                if (AdsMetaInfo.RWT_UI_GRID.equals(c.getClass1()) || AdsMetaInfo.RWT_UI_LIST.equals(c.getClass1())) {
                    add(new AdsRwtItemWidgetDef(c));
                } else {
                    add(new AdsRwtWidgetDef(c));
                }
            }
        }

        public AdsRwtWidgetDef findWidgetById(Id id) {
            AdsRwtWidgetDef w = findById(id);
            if (w == null) {
                for (AdsRwtWidgetDef w2 : this) {
                    w = w2.findWidgetById(id);
                    if (w != null) {
                        return w;
                    }
                }
            }
            return w;
        }
    }
    private String className;
    private double weight = 0;
    private final Widgets widgets = new Widgets(this);
    private final PanelGrid grid = new PanelGrid();

    public class Actions extends Definitions<AdsUIActionDef> {

        private Actions(RadixObject container) {
            super(container);
        }

        private void loadFrom(Widget w) {
            for (Action c : w.getActionList()) {
                add(new AdsUIActionDef(c));
            }
        }
    }
    private final Actions actions = new Actions(this);

    public AdsRwtWidgetDef(Id id, String className) {
        super(id, "");
        this.className = className;
    }

    public boolean isActionWidget() {
        return AdsMetaInfo.RWT_ACTION.equals(className);
    }

    public Actions getActions() {
        return actions;
    }

    public AdsRwtWidgetDef(RadixObject container, String className) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.WIDGET), "");
        setContainer(container);
        this.className = className;
        auteSetupGrid();
    }

    private void auteSetupGrid() {
        if (isGridPanel()) {
            PanelGrid.Row row = grid.addRow();
            row.addCell();
            row.addCell();
            row = grid.addRow();
            row.addCell();
            row.addCell();
        }
    }

    public final boolean isGridPanel() {
        return AdsMetaInfo.RWT_UI_PANEL.equals(className);
    }

    public AdsRwtWidgetDef(String className) {
        this((RadixObject) null, className);
    }

    AdsRwtWidgetDef(Widget xWidget) {
        this(null, xWidget);
    }

    AdsRwtWidgetDef(RadixObject container, Widget xWidget) {
        super(Id.Factory.loadFrom(xWidget.getId()), xWidget.getName());
        if (xWidget.isSetAccess()) {
            try {
                setAccessMode(EAccess.getForValue(Long.valueOf(xWidget.getAccess())));
            } catch (NoConstItemWithSuchValueError ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        weight = xWidget.getWeight();
        if (xWidget.getPanelGrid() != null) {
            this.grid.loadFrom(xWidget.getPanelGrid());
        } else {
            auteSetupGrid();
        }
        if (container != null) {
            setContainer(container);
        }
        properties.loadFrom(xWidget.getPropertyList());
        attributes.loadFrom(xWidget.getAttributeList());
        widgets.loadFrom(xWidget);
        actions.loadFrom(xWidget);

        //actions.loadFrom(xWidget);
        className = xWidget.getClass1();
        //weight = xWidget.getWeight();
    }

    @Override
    public boolean delete() {
        AdsRwtWidgetDef owner = getOwnerWidget();

        if (super.delete()) {
            if (owner != null) {
                AdsRwtUIDef def = (AdsRwtUIDef)owner.getOwnerUIDef();
                if (def != null) {
                    List<AdsUIConnection> remove = new LinkedList<AdsUIConnection>();
                    for (AdsUIConnection c : def.getConnections()) {
                        if (c.getSenderId() == getId()) {
                            remove.add(c);
                        }
                    }
                    for (AdsUIConnection c : remove) {
                        c.delete();
                    }
                }
                if (owner.getPanelGrid() != null) {
                    PanelGrid.Row.Cell cell = owner.getPanelGrid().findCellForWidget(this);
                    if (cell != null) {
                        cell.unsetWidget();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public PanelGrid getPanelGrid() {
        return grid;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.WIDGET;
    }

    @Override
    public AdsRwtWidgetDef findWidgetById(Id id) {
        return id == getId() ? this : widgets.findWidgetById(id);
    }

    public AdsDefinitions<AdsRwtWidgetDef> getWidgets() {
        return widgets;
    }

    @Override
    public String getName() {
        AdsUIProperty.StringProperty objectName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, "objectName");
        if (objectName != null) {
            return objectName.value;
        }
        return super.getName();
    }

    @Override
    public boolean setName(String name) {
        AdsUIProperty.StringProperty objectName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, "objectName");
        if (objectName != null) {
            if (objectName.getContainer() == null) {
                properties.add(objectName);
            }
            String oldName = objectName.value;
            if (!Utils.equals(oldName, name)) {
                objectName.value = name;
                fireNameChange();
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
        return super.setName(name);
    }

    public AdsRwtWidgetDef getOwnerWidget() {
        for (RadixObject obj = getContainer(); obj != null; obj = obj.getContainer()) {
            if (obj instanceof AdsRwtWidgetDef) {
                return (AdsRwtWidgetDef) obj;
            } else if (obj instanceof AdsAbstractUIDef) {
                return null;
            }
        }
        return null;
    }

    public String getClassName() {
        return className;
    }

    public void appendTo(Widget xWidget) {
        xWidget.setName(getName());
        xWidget.setId(getId().toString());
        xWidget.setPropertyArray(properties.toXml());
        xWidget.setAttributeArray(attributes.toXml());
        xWidget.setClass1(className);
        xWidget.setWeight(getWeight());
        if (!grid.isEmpty()) {
            grid.appendTo(xWidget.addNewPanelGrid());
        }
        if (getAccessMode() != getDefaultAccess()) {
            xWidget.setAccess(getAccessMode().getValue().intValue());
        }
        for (AdsRwtWidgetDef w : widgets) {
            w.appendTo(xWidget.addNewWidget());
        }
        for (AdsUIActionDef action : actions) {
            action.appendTo(xWidget.addNewAction());
        }

//        for (AdsUIActionDef a : actions) {
//            a.appendTo(xWidget.addNewAction());
//        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        widgets.visit(visitor, provider);
    }

    @Override
    public boolean canDelete() {
        return !isReadOnly() && !(getContainer() instanceof AdsUIDef);
    }
    private List<PropertyChangeListener> listeners = Collections.synchronizedList(new LinkedList<PropertyChangeListener>());

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    public void fire(AdsUIProperty prop, Object source) {
        PropertyChangeListener[] pcls = listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(source != null ? source : this, prop.getName(), prop, prop));
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.WEB;

    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.WIDGETS.calcIcon(getClassName());
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (AdsUIUtil.isCustomWidget(this)) {
            final AdsDefinition def = AdsSearcher.Factory.newAdsDefinitionSearcher(this).findById(Id.Factory.loadFrom(className)).get();
            if (def != null) {
                list.add(def);
            }
        }
    }

    public final double getWeight() {
        return weight;
    }

    public final void setWeight(double weight) {
        this.weight = weight;
    }
}
