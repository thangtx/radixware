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

package org.radixware.kernel.common.defs.ads.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.ui.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.ui.Spacer;


public class AdsLayout extends RadixObject {

    private static final String LAYOUT_NAME_PROPERTY = "layoutName";

    public static abstract class Item extends RadixObject {

        public int row, column, rowSpan, columnSpan;

        protected Item() {
            this.row = 0;
            this.column = 0;
            this.rowSpan = 1;
            this.columnSpan = 1;
        }

        protected Item(int row, int column, int rowSpan, int columnSpan) {
            this.row = row;
            this.column = column;
            this.rowSpan = rowSpan;
            this.columnSpan = columnSpan;
        }

        protected Item(org.radixware.schemas.ui.LayoutItem xItem) {
            this.row = xItem.getRow();
            this.column = xItem.getColumn();
            this.rowSpan = xItem.getRowSpan() < 1 ? 1 : xItem.getRowSpan();
            this.columnSpan = xItem.getColSpan() < 1 ? 1 : xItem.getColSpan();
        }

        protected Item(String name) {
            super(name);
        }

        public AdsLayout getOwnerLayout() {
            RadixObject owner = getContainer();
            if (owner != null) {
                owner = owner.getContainer();
            }
            if (owner instanceof AdsLayout) {
                return (AdsLayout) owner;
            }
            return null;
        }

        @Override
        public boolean setName(String name) {
            return super.setName(name == null ? "" : name);
        }

        protected void appendTo(org.radixware.schemas.ui.LayoutItem item) {
            item.setColumn(column);
            item.setRow(row);
            item.setColSpan(columnSpan);
            item.setRowSpan(rowSpan);
        }

        @Override
        protected boolean isQualifiedNamePart() {
            return false;
        }

        @Override
        public ENamingPolicy getNamingPolicy() {
            return ENamingPolicy.FREE;
        }
    }

    public static class LayoutItem extends Item {

        private AdsLayout layout;

        public LayoutItem(AdsLayout layout) {
            super();
            this.layout = layout;
            this.layout.setContainer(this);
        }

        public LayoutItem(AdsLayout layout, int row, int column, int rowSpan, int columnSpan) {
            super(row, column, rowSpan, columnSpan);
            this.layout = layout;
            this.layout.setContainer(this);
        }

        private LayoutItem(org.radixware.schemas.ui.LayoutItem layout) {
            super(layout);
            this.layout = new AdsLayout(layout.getLayout());
            this.layout.setContainer(this);
        }

        @Override
        protected void appendTo(org.radixware.schemas.ui.LayoutItem item) {
            super.appendTo(item);
            layout.appendTo(item.addNewLayout());
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            layout.visit(visitor, provider);
        }

        public AdsLayout getLayout() {
            return layout;
        }
    }

    public static class WidgetItem extends Item {

        AdsWidgetDef widget;

        public WidgetItem(AdsWidgetDef widget) {
            super();
            this.widget = widget;
            this.widget.setContainer(this);
        }

        public WidgetItem(AdsWidgetDef widget, int row, int column, int rowSpan, int columnSpan) {
            super(row, column, rowSpan, columnSpan);
            this.widget = widget;
            this.widget.setContainer(this);
        }

        private WidgetItem(org.radixware.schemas.ui.LayoutItem xWidget) {
            super(xWidget);
            if (AdsUIUtil.isItemWidget(xWidget.getWidget().getClass1())) {
                widget = new AdsItemWidgetDef(this, xWidget.getWidget());
            } else {
                widget = new AdsWidgetDef(this, xWidget.getWidget());
            }
        }

        @Override
        protected void appendTo(org.radixware.schemas.ui.LayoutItem item) {
            super.appendTo(item);
            widget.appendTo(item.addNewWidget());
        }

        @Override
        public void visitChildren(IVisitor visitor, VisitorProvider provider) {
            super.visitChildren(visitor, provider);
            widget.visit(visitor, provider);
        }

        public AdsWidgetDef getWidget() {
            return widget;
        }
    }

    public static class SpacerItem extends Item {

        private UiProperties properties = new UiProperties(this);

        public SpacerItem() {
            super();
        }

        public SpacerItem(int row, int column, int rowSpan, int columnSpan) {
            super(row, column, rowSpan, columnSpan);
        }

        private SpacerItem(org.radixware.schemas.ui.LayoutItem xSpacer) {
            super(xSpacer);
            properties.loadFrom(xSpacer.getSpacer().getPropertyList());
        }

        //used for navigator nodes
        //private Id id = null;
        //final public synchronized Id getId() {
        //    if (id == null)
        //        id = Id.Factory.newInstance(EDefinitionIdPrefix.WIDGET);
        //    return id;
        //}
        @Override
        protected void appendTo(org.radixware.schemas.ui.LayoutItem item) {
            super.appendTo(item);
            Spacer spacer = item.addNewSpacer();
            spacer.setPropertyArray(properties.toXml());
            spacer.setName(getName());
        }

        @Override
        public String getName() {
            AdsUIProperty.StringProperty spacerName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, "spacerName");
            if (spacerName != null) {
                return spacerName.value;
            }
            return super.getName();
        }

        @Override
        public boolean setName(String name) {
            AdsUIProperty.StringProperty spacerName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, "spacerName");
            if (spacerName != null) {
                if (spacerName.getContainer() == null) {
                    properties.add(spacerName);
                }
                String oldName = spacerName.value;
                if (!Utils.equals(oldName, name)) {
                    spacerName.value = name;
                    fireNameChange();
                    setEditState(EEditState.MODIFIED);
                    return true;
                } else {
                    return false;
                }
            }
            return super.setName(name);
        }

        @Override
        public RadixIcon getIcon() {
            return AdsDefinitionIcon.WIDGETS.calcIcon(AdsMetaInfo.SPACER_CLASS);
        }

        public UiProperties getProperties() {
            return properties;
        }

        @Override
        public void setContainer(RadixObject container) {
            if (getContainer() != null) {
                super.setContainer(null);
            }
            if (container != null) {
                super.setContainer(container);
            }
        }
        private List<PropertyChangeListener> listeners = Collections.synchronizedList(new LinkedList<PropertyChangeListener>());

        void addPropertyChangeListener(PropertyChangeListener pcl) {
            listeners.add(pcl);
        }

        void removePropertyChangeListener(PropertyChangeListener pcl) {
            listeners.remove(pcl);
        }

        void fire(AdsUIProperty prop, Object source) {
            PropertyChangeListener[] pcls = listeners.toArray(new PropertyChangeListener[0]);
            for (int i = 0; i < pcls.length; i++) {
                pcls[i].propertyChange(new PropertyChangeEvent(source != null ? source : this, prop.getName(), prop, prop));
            }
        }

        private class SpacerClipboardSupport extends AdsClipboardSupport<SpacerItem> {

            public SpacerClipboardSupport() {
                super(SpacerItem.this);
            }

            @Override
            public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
                return CanPasteResult.NO;
            }

            @Override
            protected XmlObject copyToXml() {
                org.radixware.schemas.ui.LayoutItem xItem = org.radixware.schemas.ui.LayoutItem.Factory.newInstance();
                appendTo(xItem);
                return xItem;
            }

            @Override
            protected SpacerItem loadFrom(XmlObject xmlObject) {
                org.radixware.schemas.ui.LayoutItem xItem = (org.radixware.schemas.ui.LayoutItem) xmlObject;
                return new SpacerItem(xItem);
            }
        }

        @Override
        public ClipboardSupport<? extends SpacerItem> getClipboardSupport() {
            return new SpacerClipboardSupport();
        }

        @Override
        public String getTypeTitle() {
            return super.getTypeTitle();
        }
        private NodeUpdateSupport nodeUpdateSupport = null;

        public synchronized NodeUpdateSupport getNodeUpdateSupport() {
            if (nodeUpdateSupport == null) {
                nodeUpdateSupport = new NodeUpdateSupport();
            }
            return nodeUpdateSupport;
        }

        public void fireNodeUpdate() {
            if (nodeUpdateSupport != null) {
                nodeUpdateSupport.fireEvent(new NodeUpdateEvent(this));
            }
        }
    }

    public class Items extends RadixObjects<Item> {

        private Items() {
            super(AdsLayout.this);
        }
    }
    private final UiProperties properties = new UiProperties(this);
    private final UiProperties attributes = new UiProperties(this);
    private final Items items = new Items();
    private String className;

    public AdsLayout(String className) {
        super();
        this.className = className;
    }

    public AdsLayout(org.radixware.schemas.ui.Layout xLayout) {
        super(xLayout.getName());
        className = xLayout.getClass1();
        properties.loadFrom(xLayout.getPropertyList());
        attributes.loadFrom(xLayout.getAttributeList());
        for (org.radixware.schemas.ui.LayoutItem item : xLayout.getItemList()) {
            if (item.isSetLayout()) {
                items.add(new LayoutItem(item));
            } else if (item.isSetWidget()) {
                items.add(new WidgetItem(item));
            } else if (item.isSetSpacer()) {
                items.add(new SpacerItem(item));
            }
        }
    }

    //used for navigator nodes
    //private Id id = null;
    //final public synchronized Id getId() {
    //    if (id == null)
    //        id = Id.Factory.newInstance(EDefinitionIdPrefix.WIDGET);
    //    return id;
    //}
    @Override
    public String getName() {
        AdsUIProperty.StringProperty layoutName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, LAYOUT_NAME_PROPERTY);
        if (layoutName != null) {
            return layoutName.value;
        }
        return super.getName();
    }

    @Override
    public boolean setName(String name) {
        AdsUIProperty.StringProperty layoutName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, LAYOUT_NAME_PROPERTY);
        if (layoutName != null) {
            if (layoutName.getContainer() == null) {
                properties.add(layoutName);
            }
            String oldName = layoutName.value;
            if (!Utils.equals(oldName, name)) {
                layoutName.value = name;
                fireNameChange();
                setEditState(EEditState.MODIFIED);
                return true;
            } else {
                return false;
            }
        }
        return super.setName(name);
    }

    public boolean isSetName() {
        final AdsUIProperty name = getProperties().getByName(LAYOUT_NAME_PROPERTY);
        return name instanceof AdsUIProperty.StringProperty;
    }

    public Item[][] getItemsAsArray() {
        int colCount = 0, rowCount = 0;
        for (Item item : items) {
            colCount = Math.max(item.column + item.columnSpan, colCount);
            rowCount = Math.max(item.row + item.rowSpan, rowCount);
        }

        AdsLayout.Item it[][] = new AdsLayout.Item[rowCount][colCount];
        for (Item item : items) {
            for (int i = 0; i < item.rowSpan; i++) {
                for (int j = 0; j < item.columnSpan; j++) {
                    it[item.row + i][item.column + j] = item;
                }
            }
        }

        return it;
    }

    public void adjustItems() {
        Item it[][] = getItemsAsArray();
        int rowCount = it.length;
        int colCount = rowCount > 0 ? it[0].length : 0;
        if (rowCount == 0 || colCount == 0) {
            return;
        }

        // clear empty rows
        for (int i = rowCount - 1; i >= 0; i--) {
            int count = 0;
            for (int j = 0; j < colCount; j++) {
                if (it[i][j] != null) {
                    count++;
                }
            }
            if (count == 0) {
                for (Item item : items) {
                    if (item.row > i) {
                        item.row--;
                    }
                }
            }
        }

        // clear empty columns
        for (int j = colCount - 1; j >= 0; j--) {
            int count = 0;
            for (int i = 0; i < rowCount; i++) {
                if (it[i][j] != null) {
                    count++;
                }
            }
            if (count == 0) {
                for (Item item : items) {
                    if (item.column > j) {
                        item.column--;
                    }
                }
            }
        }
    }

    public Items getItems() {
        return items;
    }

    public UiProperties getProperties() {
        return properties;
    }

    public UiProperties getAttributes() {
        return attributes;
    }

    public void appendTo(org.radixware.schemas.ui.Layout xDef) {
        for (Item item : items) {
            item.appendTo(xDef.addNewItem());
        }
        xDef.setPropertyArray(properties.toXml());
        xDef.setAttributeArray(attributes.toXml());
        xDef.setClass1(className);
        xDef.setName(getName());
    }

    public String getClassName() {
        return className;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.WIDGETS.calcIcon(getClassName());
    }

    @Override
    public void setContainer(RadixObject container) {
        if (getContainer() != null) {
            super.setContainer(null);
        }
        if (container != null) {
            super.setContainer(container);
        }
    }

    public AdsWidgetDef findWidgetById(Id id) {
        for (Item item : items) {
            if (item instanceof WidgetItem) {
                AdsWidgetDef w = ((WidgetItem) item).widget.findWidgetById(id);
                if (w != null) {
                    return w;
                }
            } else if (item instanceof LayoutItem) {
                AdsWidgetDef w = ((LayoutItem) item).layout.findWidgetById(id);
                if (w != null) {
                    return w;
                }
            }
        }
        return null;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        items.visit(visitor, provider);
        properties.visit(visitor, provider);
        attributes.visit(visitor, provider);
    }

    @Override
    public boolean canDelete() {
        return !isReadOnly();
    }

    @Override
    public boolean delete() {
        RadixObject owner = getContainer();
        if ((owner instanceof AdsLayout.Item) || (owner instanceof AdsWidgetDef && AdsUIUtil.getUiClassName(owner).equals(AdsMetaInfo.WIDGET_CLASS))) {
            boolean del = owner.delete();
            if (del) {
                setContainer(null);
            }
            return del;
        }
        return super.delete();
    }
    private List<PropertyChangeListener> listeners = Collections.synchronizedList(new LinkedList<PropertyChangeListener>());

    void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    void fire(AdsUIProperty prop, Object source) {
        PropertyChangeListener[] pcls = listeners.toArray(new PropertyChangeListener[0]);
        for (int i = 0; i < pcls.length; i++) {
            pcls[i].propertyChange(new PropertyChangeEvent(source != null ? source : this, prop.getName(), prop, prop));
        }
    }

    private class AdsLayoutClipboardSupport extends AdsClipboardSupport<AdsLayout> {

        public AdsLayoutClipboardSupport() {
            super(AdsLayout.this);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            AdsLayout layout = AdsLayout.this;
            for (Transfer transfer : objectsInClipboard) {
                if (!(transfer.getObject() instanceof AdsWidgetDef || transfer.getObject() instanceof AdsLayout || transfer.getObject() instanceof AdsLayout.SpacerItem)) {
                    return CanPasteResult.NO;
                }
                if (transfer.getObject() == layout) {// attempt to cut and paste to it self
                    return CanPasteResult.NO;
                }
            }

            if (layout.isReadOnly()) {
                return CanPasteResult.NO;
            }

            return CanPasteResult.YES;
        }

        @Override
        public void paste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            checkForCanPaste(objectsInClipboard,resolver);

            AdsLayout.Item[][] items = getItemsAsArray();
            int i = items.length;
            int j = i > 0 ? items[0].length : 0;

            for (Transfer transfer : objectsInClipboard) {
                AdsLayout.Item item = null;
                if (transfer.getObject() instanceof AdsLayout) {
                    item = new AdsLayout.LayoutItem((AdsLayout) transfer.getObject());
                } else if (transfer.getObject() instanceof AdsWidgetDef) {
                    item = new AdsLayout.WidgetItem((AdsWidgetDef) transfer.getObject());
                } else if (transfer.getObject() instanceof AdsLayout.SpacerItem) {
                    item = (AdsLayout.SpacerItem) transfer.getObject();
                }

                if (item == null) {
                    continue;
                }

                if (getClassName().equals(AdsMetaInfo.VERTICAL_LAYOUT_CLASS)) {
                    item.row = i++;
                } else {
                    item.column = j++;
                }

                getItems().add(item);
            }
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ui.Layout xLayout = org.radixware.schemas.ui.Layout.Factory.newInstance();
            appendTo(xLayout);
            return xLayout;
        }

        @Override
        protected AdsLayout loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ui.Layout xLayout = (org.radixware.schemas.ui.Layout) xmlObject;
            return new AdsLayout(xLayout);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsLayout> getClipboardSupport() {
        return new AdsLayoutClipboardSupport();
    }

    @Override
    public String getTypeTitle() {
        return super.getTypeTitle();
    }
    private NodeUpdateSupport nodeUpdateSupport = null;

    public synchronized NodeUpdateSupport getNodeUpdateSupport() {
        if (nodeUpdateSupport == null) {
            nodeUpdateSupport = new NodeUpdateSupport();
        }
        return nodeUpdateSupport;
    }

    public void fireNodeUpdate() {
        if (nodeUpdateSupport != null) {
            nodeUpdateSupport.fireEvent(new NodeUpdateEvent(this));
        }
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
}
