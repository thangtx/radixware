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
package org.radixware.kernel.designer.tree.ads.nodes.defs;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;
import org.netbeans.spi.navigator.NavigatorLookupHint;
import org.openide.nodes.Sheet;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.RenameEvent;
import org.radixware.kernel.common.defs.RadixObject.RenameListener;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsLayout;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUIItemDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.NodeUpdateSupport.NodeUpdateEvent;
import org.radixware.kernel.common.defs.ads.ui.NodeUpdateSupport.NodeUpdateListener;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIConnectionSupport;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.common.general.nodes.IContextDependentNode;

public abstract class AdsFormObjectNode<T extends RadixObject> extends AdsMixedNode<T> implements NodeUpdateListener, IContextDependentNode { // RadixObjectNode - because not required save, rename, etc.

    final private RenameListener renameListener = new RenameListener() {
        @Override
        public void onEvent(RenameEvent e) {
            update();
        }
    };
    protected final AdsAbstractUIDef rootDef;

    public AdsFormObjectNode(AdsAbstractUIDef rootDef, T obj) {
        super(obj);
        this.rootDef = rootDef;
        obj.addRenameListener(renameListener);
        doUpdate();//NOPMD
    }

    final void doUpdate() {
        update();
    }

    @Override
    public RadixObject getContext() {
        return rootDef;
    }

    protected void update() {
        //setSheet(createSheet());
    }

    private AdsLayout getLayout(RadixObject radixObject) {
        if (!(radixObject instanceof AdsWidgetDef)) {
            return null;
        }
        AdsUIItemDef widget = AdsUIUtil.currentWidget((AdsWidgetDef) radixObject);
        return widget instanceof AdsWidgetDef ? ((AdsWidgetDef) widget).getLayout() : null;
    }

    private String getClassTitle(String clazz) {
        if (AdsUIUtil.isCustomWidget(clazz)) {
            AdsAbstractUIDef customUI = AdsMetaInfo.getCustomUI(clazz, getRadixObject());
            if (customUI != null) {
                return customUI.getName();
            }
        }
        if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_PROP_EDITOR.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_DIALOG.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_FORM_DIALOG.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_SELECTOR.getValue())
                || clazz.startsWith(EDefinitionIdPrefix.CUSTOM_REPORT_DIALOG.getValue())) {
            return "View";
        }

        return clazz;
    }

    @Override
    public String getDisplayName() {
        RadixObject radixObject = getRadixObject();
        return getClassTitle(AdsUIUtil.getUiClassName(radixObject)) + " '" + radixObject.getName() + "'";
//        return radixObject.getTypeTitle() + " '" + radixObject.getName() + "'";
    }

    private boolean availableProp(String propName) {
        final T radixObject = getRadixObject();
        if (AdsMetaInfo.RWT_PROP_EDITOR.equals(AdsUIUtil.getUiClassName(radixObject)) || AdsMetaInfo.PROP_EDITOR_CLASS.equals(AdsUIUtil.getQtClassName(radixObject))) {//radix-9158
            if ("enabled".equals(propName)) {
                return false;
            }
        }
        // свойства autoConnectAcceptSignal и autoConnectRejectSignal доступны только в диалоге
        if (AdsMetaInfo.DIALOG_BUTTON_BOX_CLASS.equals(AdsUIUtil.getQtClassName(radixObject))) {
            switch (propName) {
                case "autoConnectAcceptSignal":
                case "autoConnectRejectSignal":

                    return AdsUIUtil.isQtDialog(rootDef);
            }
        }
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        Sheet sheet = Sheet.createDefault();
        RadixObject node = getRadixObject();

        String className = AdsUIUtil.getQtClassName(node);
        final String rootClassName = className;
        UiProperties props = AdsUIUtil.getUiProperties(node);

        Stack<String> classList = new Stack<>();
        while (className != null) {
            classList.push(className);
            className = AdsMetaInfo.getExtendsForProp(className, node);
        }

        HashMap<String, UIPropertySupport> hash = new HashMap<>();

        // node properties
        while (!classList.empty()) {
            className = classList.pop();
            List<AdsUIProperty> classProps = AdsMetaInfo.getProps(className, node);
            if (classProps != null/* && !classProps.isEmpty()*/) {
                Sheet.Set propSet = Sheet.createPropertiesSet();
                propSet.setName(className);
                propSet.setDisplayName(getClassTitle(className));
                propSet.setValue("tabName", NbBundle.getMessage(getClass(), "Properties"));
                for (AdsUIProperty p : classProps) {
                    if (!availableProp(p.getName())) {
                        continue;
                    }

                    AdsUIProperty prop = props.getByName(p.getName());
                    if (prop == null) {
                        prop = p.duplicate();
                    }
                    UIPropertySupport sup = hash.get(p.getName());
                    if (sup != null) {
                        sup.setProp(prop);
                    } else {
                        sup = new UIPropertySupport(prop, rootDef, node);
                        sup.setValue("set", propSet);
                        hash.put(prop.getName(), sup);
                        propSet.put(sup);
                    }
                }
                sheet.put(propSet);
            }
        }

        // layout if exists
        AdsLayout layout = getLayout(node);
        if (layout != null) {

            classList.clear();
            className = layout.getClassName();
            while (className != null) {
                classList.push(className);
                className = AdsMetaInfo.getExtendsForProp(className, node);
            }

            props = AdsUIUtil.getUiProperties(layout);
            while (!classList.empty()) {
                className = classList.pop();
                List<AdsUIProperty> classProps = AdsMetaInfo.getProps(className, node);
                if (classProps != null) {
                    Sheet.Set propSet = Sheet.createPropertiesSet();
                    propSet.setName(className);
                    propSet.setDisplayName(getClassTitle(className));
                    propSet.setValue("tabName", NbBundle.getMessage(getClass(), "Properties"));
                    for (AdsUIProperty p : classProps) {
                        AdsUIProperty prop = props.getByName(p.getName());
                        if (prop == null) {
                            prop = p.duplicate();
                        }
                        UIPropertySupport sup = new UIPropertySupport(prop, rootDef, layout);
                        sup.setValue("set", propSet);
                        propSet.put(sup);
                    }
                    sheet.put(propSet);
                }
            }
        }

        // node activities
        //Sheet.Set connSet = Sheet.createPropertiesSet();
        //connSet.setDisplayName(NbBundle.getMessage(getClass(), "Connections"));
        //connSet.setName(NbBundle.getMessage(getClass(), "Connections"));
        //connSet.setValue("tabName", NbBundle.getMessage(getClass(), "Connections"));
        //sheet.put(connSet);
        classList = new Stack<>();
        className = rootClassName;
        while (className != null) {
            classList.push(className);
            className = AdsMetaInfo.getExtends(className, node);
        }

        // connections if exists
        while (!classList.empty()) {
            className = classList.pop();
            List<AdsUIConnection> classConns = AdsMetaInfo.getConns(className, node);
            if (classConns != null/* && !classConns.isEmpty()*/) {
                Sheet.Set connSet = Sheet.createPropertiesSet();
                connSet.setName(className + "_2");
                connSet.setDisplayName(getClassTitle(className));
                connSet.setValue("tabName", NbBundle.getMessage(getClass(), "Connections"));
                for (AdsUIConnection c : classConns) {
                    UIConnectionSupport sup = new UIConnectionSupport(c/*.duplicate()*/, rootDef, (AdsUIItemDef) node);
                    sup.setValue("set", connSet);
                    connSet.put(sup);
                }
                sheet.put(connSet);
            }
        }        
        return sheet;
    }

    @Override
    public void onEvent(NodeUpdateEvent e) {
        update();
    }

    @Override
    protected NavigatorLookupHint getNavigatorLookupHint() {
        return FormNavigatorLookupHint.getInstance();
    }
}
