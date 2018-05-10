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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.ui.Action;
import org.radixware.schemas.ui.Widget;

public class AdsWidgetDef extends AdsUIItemDef implements IJavaSource {

    private final class AdsWidgetJavaSourceSupport extends JavaSourceSupport { //by BAO

        public AdsWidgetJavaSourceSupport() {
            super(AdsWidgetDef.this);
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
                    support.writeWidgetUsage(AdsWidgetDef.this, printer);
                }
            };
        }
    }

    public static class Widgets extends Definitions<AdsWidgetDef> {

        public Widgets(RadixObject container) {
            super(container);
        }

        private void loadFrom(Widget xWidget) {
            for (Widget c : xWidget.getWidgetList()) {
                if (AdsUIUtil.isItemWidget(c.getClass1())) {
                    add(new AdsItemWidgetDef(c));
                } else {
                    add(new AdsWidgetDef(c));
                }
            }
        }

        public AdsWidgetDef findWidgetById(Id id) {
            AdsWidgetDef w = findById(id);
            if (w == null) {
                for (AdsWidgetDef w2 : this) {
                    w = w2.findWidgetById(id);
                    if (w != null) {
                        return w;
                    }
                }
            }
            return w;
        }
    }

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
    private AdsLayout layout = null;
    private final Widgets widgets = new Widgets(this);
    String className;
    private double weight = 0;

    public AdsWidgetDef(String className) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.WIDGET), "");
        this.className = className;
    }

    public AdsWidgetDef(RadixObject container, String className) {
        this(className);
        if (container != null) {
            setContainer(container);
        }
    }

    public AdsWidgetDef(Widget w) {
        this(null, w);
    }

    AdsWidgetDef(RadixObject container, Widget xWidget) {
        super(Id.Factory.loadFrom(xWidget.getId()), xWidget.getName());
        if (xWidget.isSetAccess()) {
            try {
                setAccessMode(EAccess.getForValue(Long.valueOf(xWidget.getAccess())));
            } catch (NoConstItemWithSuchValueError ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }
        if (container != null) {
            setContainer(container);
        }
        properties.loadFrom(xWidget.getPropertyList());
        attributes.loadFrom(xWidget.getAttributeList());
        widgets.loadFrom(xWidget);
        if (xWidget.getLayout() != null) {
            layout = new AdsLayout(xWidget.getLayout());
            layout.setContainer(this);
        }
        actions.loadFrom(xWidget);
        className = xWidget.getClass1();
        weight = xWidget.getWeight();
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() { // BY BAO
        return new AdsWidgetJavaSourceSupport();
    }

    @Override
    public String getName() {
        AdsUIProperty.StringProperty objectName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, AdsWidgetProperties.OBJECT_NAME);
        if (objectName != null) {
            return objectName.value;
        }
        return super.getName();
    }

    @Override
    public boolean setName(String name) {
        AdsUIProperty.StringProperty objectName = (AdsUIProperty.StringProperty) AdsUIUtil.getUiProperty(this, AdsWidgetProperties.OBJECT_NAME);
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

    @Override
    public boolean isActionWidget() {
        return AdsMetaInfo.ACTION_CLASS.equals(className);
    }

    public Id getCustomEditorPageId() {
        RadixObject obj = getOwnerDef();
        while (obj != null) {
            if (obj instanceof AdsUIDef) {
                if (obj instanceof AdsCustomPageEditorDef) {
                    return ((AdsCustomPageEditorDef) obj).getId();
                } else {
                    return null;
                }
            }
            obj = obj.getContainer();
        }
        return null;
    }

    public void appendTo(Widget xWidget) {
        xWidget.setName(getName());
        xWidget.setId(getId().toString());
        xWidget.setPropertyArray(properties.toXml());
        xWidget.setAttributeArray(attributes.toXml());
        xWidget.setClass1(className);
        xWidget.setWeight(getWeight());
        if (getAccessMode() != getDefaultAccess()) {
            xWidget.setAccess(getAccessMode().getValue().intValue());
        }
        for (AdsWidgetDef w : widgets) {
            w.appendTo(xWidget.addNewWidget());
        }
        if (layout != null) {
            layout.appendTo(xWidget.addNewLayout());
        }

        for (AdsUIActionDef a : actions) {
            a.appendTo(xWidget.addNewAction());
        }
    }

    public String getClassName() {
        String clazz = className;
        switch (clazz) {
            case "org.radixware.kernel.explorer.widgets.PropLabel":
                clazz = AdsMetaInfo.PROP_LABEL_CLASS;
                break;
            case "org.radixware.kernel.explorer.widgets.PropEditor":
                clazz = AdsMetaInfo.PROP_EDITOR_CLASS;
                break;
            case "org.radixware.kernel.explorer.widgets.CommandPushButton":
                clazz = AdsMetaInfo.COMMAND_PUSH_BUTTON_CLASS;
                break;
            case "org.radixware.kernel.explorer.widgets.EmbeddedEditor":
                clazz = AdsMetaInfo.EMBEDDED_EDITOR_CLASS;
                break;
            case "org.radixware.kernel.explorer.widgets.EmbeddedSelector":
                clazz = AdsMetaInfo.EMBEDDED_SELECTOR_CLASS;
                break;
            case "org.radixware.kernel.explorer.widgets.EditorPage":
                clazz = AdsMetaInfo.EDITOR_PAGE_CLASS;
                break;
        }

        return clazz;
    }

    public AdsLayout getLayout() {
        return layout;
    }

    public void setLayout(AdsLayout layout) {
        this.layout = layout;
        if (layout != null) {
            layout.setContainer(this);
        }
    }

    public Widgets getWidgets() {
        return widgets;
    }

    public Actions getActions() {
        return actions;
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

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (layout != null) {
            layout.visit(visitor, provider);
        }
        widgets.visit(visitor, provider);
        actions.visit(visitor, provider);
    }

    @Override
    public AdsWidgetDef findWidgetById(Id id) {
        if (id == getId()) {
            return this;
        } else {
            AdsWidgetDef w = widgets.findWidgetById(id);
            if (w == null && layout != null) {
                w = layout.findWidgetById(id);
            }
            return w;
        }
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        if (id.getPrefix() == EDefinitionIdPrefix.WIDGET) {
            return SearchResult.single(findWidgetById(id));
        } else {
            return super.findComponentDefinition(id);
        }
    }

    @Override
    public boolean canDelete() {
        return !isReadOnly() && !(getContainer() instanceof AdsUIDef);
    }

    @Override
    public boolean delete() {
        RadixObject owner = getContainer();
        if (owner instanceof AdsLayout.Item) {
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

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.WIDGETS.calcIcon(getClassName());
    }

    private class AdsWidgetClipboardSupport extends AdsClipboardSupport<AdsWidgetDef> {

        public AdsWidgetClipboardSupport() {
            super(AdsWidgetDef.this);
        }

        @Override
        public boolean canCopy() {
            return !(getContainer() instanceof AdsUIDef);
        }

        @Override
        public CanPasteResult canPaste(List<Transfer> objectsInClipboard, DuplicationResolver resolver) {
            final AdsWidgetDef widget = AdsWidgetDef.this;
            final AdsWidgetDef curWidget = (AdsWidgetDef) AdsUIUtil.currentWidget(widget);
            
            if (curWidget != null && curWidget.getLayout() != null) {
                return curWidget.getLayout().getClipboardSupport().canPaste(objectsInClipboard, resolver);
            }

            for (Transfer transfer : objectsInClipboard) {
                if (!(transfer.getObject() instanceof AdsWidgetDef || transfer.getObject() instanceof AdsLayout)) {
                    return CanPasteResult.NO;
                }
                if (transfer.getObject() == widget) // attempt to cut and paste to it self
                {
                    return CanPasteResult.NO;
                }
            }

            if (widget.isReadOnly() || !AdsUIUtil.isContainer(widget)) {
                return CanPasteResult.NO;
            }

            return CanPasteResult.YES;
        }

        @Override
        public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
            checkForCanPaste(transfers, resolver);

            final AdsWidgetDef curWidget = (AdsWidgetDef) AdsUIUtil.currentWidget(AdsWidgetDef.this);
            if (curWidget.getLayout() != null) {
                curWidget.getLayout().getClipboardSupport().paste(transfers, resolver);
                return;
            }

            for (Transfer transfer : transfers) {
                AdsWidgetDef w = null;
                if (transfer.getObject() instanceof AdsLayout) {
                    w = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
                    w.setLayout((AdsLayout) transfer.getObject());
                } else if (transfer.getObject() instanceof AdsWidgetDef) {
                    w = (AdsWidgetDef) transfer.getObject();
                }

                if (w != null) {
                    AdsUIProperty.RectProperty geometry = (AdsUIProperty.RectProperty) AdsUIUtil.getUiProperty(w, AdsWidgetProperties.GEOMETRY);
                    geometry.x += 5;
                    geometry.y += 5;
                    curWidget.getWidgets().add(w);
                }
            }
        }

        @Override
        protected XmlObject copyToXml() {
            Widget xWidget = Widget.Factory.newInstance();
            appendTo(xWidget);
            return xWidget;
        }

        @Override
        protected AdsWidgetDef loadFrom(XmlObject xmlObject) {
            Widget xWidget = (Widget) xmlObject;
            if (AdsUIUtil.isItemWidget(xWidget.getClass1())) {
                return new AdsItemWidgetDef(xWidget);
            }
            return new AdsWidgetDef(null, xWidget);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsWidgetDef> getClipboardSupport() {
        return new AdsWidgetClipboardSupport();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.WIDGET;
    }

    @Override
    public boolean isPublished() {
        return super.isPublished();
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }

    @Override
    public boolean canChangeFinality() {
        return false;
    }

    @Override
    public boolean canChangePublishing() {
        return false;
    }

    @Override
    protected EAccess getDefaultAccess() {
        return EAccess.PUBLIC;
    }

    @Override
    protected boolean getDefaultIsFinal() {
        return true;
    }

    @Override
    public boolean isFinal() {
        return true;
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
