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

import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.IModelPublishableProperty;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.IEditorPagesContainer;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsExplorerItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsParagraphExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.ui.enums.EOrientation;
import org.radixware.kernel.common.defs.ads.ui.rwt.*;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Utils;

public class AdsUIUtil {

    public static void layoutToWidgets(AdsWidgetDef widget) {
        AdsLayout layout = widget.getLayout();
        if (layout == null) {
            return;
        }

        AdsWidgetDef.Widgets widgets = widget.getWidgets();
        AdsLayout.Items items = layout.getItems();

        while (items.size() > 0) {
            AdsLayout.Item item = items.get(0);
            items.remove(item);

            AdsWidgetDef w = null;
            if (item instanceof AdsLayout.LayoutItem) {
                AdsLayout l = ((AdsLayout.LayoutItem) item).getLayout();
                w = new AdsWidgetDef(AdsMetaInfo.WIDGET_CLASS);
                w.setLayout(l);
            } else if (item instanceof AdsLayout.WidgetItem) {
                w = ((AdsLayout.WidgetItem) item).getWidget();
            }
            if (w != null) {
                widgets.add(w);
            }
        }

        widget.setLayout(null);
    }

    public static void widgetsToLayout(AdsWidgetDef widget, AdsLayout layout) {
        final AdsWidgetDef.Widgets widgets = widget.getWidgets();
        final AdsLayout.Items items = layout.getItems();

        for (final AdsWidgetDef wdg : widgets.list()) {
            if (!isSelfContained(wdg)) {
                widgets.remove(wdg);

                final AdsLayout.Item item;
                if (wdg.getLayout() != null && wdg.getClassName().equals(AdsMetaInfo.WIDGET_CLASS)) {
                    item = new AdsLayout.LayoutItem(wdg.getLayout());
                } else {
                    item = new AdsLayout.WidgetItem(wdg);
                }
                items.add(item);
            }
        }
        widget.setLayout(layout);
    }

    public static boolean isSelfContained(AdsWidgetDef node) {
        if (node == null) {
            return false;
        }

        switch (node.getClassName()) {
            case AdsMetaInfo.ACTION_CLASS:
                return true;
            default:
                return false;
        }
    }

    public static boolean isReadOnlyNode(AdsAbstractUIDef context, RadixObject node) {
        if (node.isReadOnly()) {
            return true;
        }
        AdsAbstractUIDef ownerUI = getUiDef(node);
        return ownerUI != context;
    }

    public static boolean isQtDialog(AdsAbstractUIDef def) {
        if (def == null) {
            return false;
        }

        final String cls = getQtClassName(def.getWidget());

        if (cls != null) {
            switch (cls) {
                case AdsMetaInfo.CUSTOM_FORM_DIALOG:
                case AdsMetaInfo.CUSTOM_REPORT_DIALOG:
                case AdsMetaInfo.CUSTOM_DIALOG:
                case AdsMetaInfo.CUSTOM_PROP_EDITOR:
                    return true;

            }
        }
        return false;
    }

    public static UiProperties getUiProperties(RadixObject node) {
        assert node != null;

        if (node instanceof AdsWidgetDef || node instanceof AdsRwtWidgetDef) {
            return ((AdsUIItemDef) node).getProperties();
        }
        if (node instanceof AdsLayout) {
            return ((AdsLayout) node).getProperties();
        }
        if (node instanceof AdsLayout.SpacerItem) {
            return ((AdsLayout.SpacerItem) node).getProperties();
        }

        if (node instanceof AdsItemWidgetDef.WidgetItem) {
            return ((AdsItemWidgetDef.WidgetItem) node).getProperties();
        }
        if (node instanceof AdsItemWidgetDef.Row) {
            return ((AdsItemWidgetDef.Row) node).getProperties();
        }
        if (node instanceof AdsItemWidgetDef.Column) {
            return ((AdsItemWidgetDef.Column) node).getProperties();
        }

        return null;
    }

    public static boolean isUIObject(RadixObject object) {
        return object instanceof AdsAbstractUIDef
                || object instanceof AdsWidgetDef
                || object instanceof AdsRwtWidgetDef
                || object instanceof AdsLayout
                || object instanceof AdsLayout.SpacerItem;
    }

    public static boolean isUIRoot(RadixObject object) {
        assert object != null;
        AdsAbstractUIDef ui = AdsUIUtil.getUiDef(object);
        return ui != null && object.equals(ui.getWidget());
    }

    public static RadixObject getItemNode(AdsLayout.Item item) {
        assert item != null;
        if (item instanceof AdsLayout.WidgetItem) {
            return ((AdsLayout.WidgetItem) item).getWidget();
        }
        if (item instanceof AdsLayout.LayoutItem) {
            return ((AdsLayout.LayoutItem) item).getLayout();
        }
        return (AdsLayout.SpacerItem) item;
    }

    public static RadixObject getVisualNode(AdsUIItemDef widget) {
        assert widget != null;
        if (widget instanceof AdsRwtWidgetDef) {
            return widget;
        }
        AdsWidgetDef ads = (AdsWidgetDef) widget;
        return ads.getClassName().equals(AdsMetaInfo.WIDGET_CLASS) && ads.getLayout() != null
                ? ads.getLayout() : widget;
    }

    public static void setUiContainer(RadixObject node, RadixObject container) {
        if (node instanceof AdsWidgetDef) {
            ((AdsWidgetDef) node).setContainer(container);
        }
        if (node instanceof AdsLayout) {
            ((AdsLayout) node).setContainer(container);
        }
        if (node instanceof AdsLayout.SpacerItem) {
            ((AdsLayout.SpacerItem) node).setContainer(container);
        }
        if (node instanceof AdsRwtWidgetDef) {
            ((AdsRwtWidgetDef) node).setContainer(container);
        }
    }

    public static boolean isRwtActionHolder(AdsRwtWidgetDef w) {
        String baseClass = getQtClassName(w);
        while (baseClass != null) {
            if (AdsMetaInfo.RWT_UI_DIALOG.equals(baseClass)) {
                return true;
            }
            baseClass = AdsMetaInfo.getExtends(baseClass);
        }
        return false;
    }

    public static String getUiClassName(RadixObject node) {
        assert node != null;
        if (node instanceof AdsWidgetDef) {
            return ((AdsWidgetDef) node).getClassName();
        }
        if (node instanceof AdsUIActionDef) {
            return AdsMetaInfo.RWT_ACTION;
        }
        if (node instanceof AdsRwtWidgetDef) {
            return ((AdsRwtWidgetDef) node).getClassName();
        }
        if (node instanceof AdsLayout) {
            return ((AdsLayout) node).getClassName();
        }
        if (node instanceof AdsLayout.SpacerItem) {
            return AdsMetaInfo.SPACER_CLASS;
        }
        if (node instanceof AdsItemWidgetDef.WidgetItem) {
            return AdsMetaInfo.WIDGET_ITEM_CLASS;
        }
        if (node instanceof AdsItemWidgetDef.Row) {
            return AdsMetaInfo.WIDGET_ITEM_CLASS;
        }
        if (node instanceof AdsItemWidgetDef.Column) {
            return AdsMetaInfo.WIDGET_ITEM_CLASS;
        }
        return null;
    }

    public static String getQtClassName(RadixObject node) {
        assert node != null;
        if (node instanceof AdsUIActionDef) {
            AdsUIActionDef action = (AdsUIActionDef) node;
            return AdsMetaInfo.RWT_ACTION;
        } else if (node instanceof AdsWidgetDef) {
            final String clazz = ((AdsWidgetDef) node).getClassName();
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_PROP_EDITOR.getValue())) {
                return AdsMetaInfo.CUSTOM_PROP_EDITOR;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_DIALOG.getValue())) {
                return AdsMetaInfo.CUSTOM_DIALOG;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_FORM_DIALOG.getValue())) {
                return AdsMetaInfo.CUSTOM_FORM_DIALOG;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG.getValue())) {
                return AdsMetaInfo.CUSTOM_FILTER_DIALOG;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE.getValue())) {
                return AdsMetaInfo.CUSTOM_EDITOR_PAGE;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR.getValue())) {
                return AdsMetaInfo.CUSTOM_EDITOR;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR.getValue())) {
                return AdsMetaInfo.CUSTOM_PARAG_EDITOR;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_SELECTOR.getValue())) {
                return AdsMetaInfo.CUSTOM_SELECTOR;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_REPORT_DIALOG.getValue())) {
                return AdsMetaInfo.CUSTOM_REPORT_DIALOG;
            }
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_WIDGET.getValue()) && node.getContainer() instanceof AdsUIDef) {
                return AdsMetaInfo.CUSTOM_WIDGET;
            }
            return clazz;
        } else if (node instanceof AdsRwtWidgetDef) {
            final String clazz = ((AdsRwtWidgetDef) node).getClassName();
            if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_PROP_EDITOR.getValue())) {
                return AdsMetaInfo.RWT_PROP_EDITOR_DIALOG;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_DIALOG.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_DIALOG;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR.getValue())) {
                return AdsMetaInfo.RWT_PARAG_EDITOR;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_EDITOR;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_SELECTOR.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_SELECTOR;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_EDITOR_PAGE;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_FORM_DIALOG.getValue())) {
                return AdsMetaInfo.RWT_FORM_DIALOG;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_REPORT_DIALOG.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_REPORT_DIALOG;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_FILTER_DIALOG.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_FILTER_DIALOG;
            } else if (clazz.startsWith(EDefinitionIdPrefix.CUSTOM_WIDGET.getValue())) {
                return AdsMetaInfo.RWT_CUSTOM_WIDGET;
            }
        }
        return getUiClassName(node);
    }

    public static String getUiName(RadixObject node) {
        assert node != null;
        if (node instanceof AdsWidgetDef) {
            return ((AdsWidgetDef) node).getName();
        }
        if (node instanceof AdsLayout) {
            return ((AdsLayout) node).getName();
        }
        if (node instanceof AdsLayout.SpacerItem) {
            return ((AdsLayout.SpacerItem) node).getName();
        }
        return null;
    }

    public static void setUiName(RadixObject node, String name) {
        assert node != null;
        if (node instanceof AdsWidgetDef) {
            ((AdsWidgetDef) node).setName(name);
        }
        if (node instanceof AdsRwtWidgetDef) {
            ((AdsRwtWidgetDef) node).setName(name);
        }
        if (node instanceof AdsLayout) {
            ((AdsLayout) node).setName(name);
        }
        if (node instanceof AdsLayout.SpacerItem) {
            ((AdsLayout.SpacerItem) node).setName(name);
        }
    }

    public static AdsUIProperty getUiProperty(RadixObject node, String name) {
        assert node != null;

        if (name.equals(AdsUIProperty.AccessProperty.NAME)) {
            return null;
        }

        if (name.equals(AdsWidgetProperties.GEOMETRY) && node instanceof AdsLayout) {
            RadixObject owner = node.getContainer();
            if (owner instanceof AdsWidgetDef) {
                return getUiProperty(owner, name);
            }
            return null;
        }

        UiProperties props = getUiProperties(node);
        if (props == null) {
            return null;
        }

        AdsUIProperty prop = props.getByName(name);
        if (prop == null) {
            prop = AdsMetaInfo.getPropByName(getQtClassName(node), name, node);
            if (prop != null) {
                if (node instanceof AdsLayout.SpacerItem && "sizeHint".equals(name)) {
                    AdsUIProperty.EnumValueProperty o = (AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(node, AdsWidgetProperties.ORIENTATION);
                    AdsUIProperty.SizeProperty size = (AdsUIProperty.SizeProperty) prop.duplicate();
                    if (EOrientation.Horizontal.equals(o.getValue())) {
                        size.setSize(40, 20);
                    } else {
                        size.setSize(20, 40);
                    }
                    return size;
                }

                return prop.duplicate();
            }
        }

        return prop;
    }

    public static void setUiProperty(RadixObject node, AdsUIProperty prop) {
        assert node != null && prop != null;

        if (prop instanceof AdsUIProperty.AccessProperty) {
            return;
        }

        if (prop.getName().equals(AdsWidgetProperties.GEOMETRY) && node instanceof AdsLayout) {
            RadixObject owner = node.getContainer();
            if (owner instanceof AdsWidgetDef) {
                setUiProperty(owner, prop);
            }
            return;
        }

        UiProperties props = getUiProperties(node);
        if (props == null) {
            return;
        }

        AdsUIProperty p = props.getByName(prop.getName());
        if (p != null) {
            props.remove(p);
        }

        props.add(prop);
    }

    public static void addContainerListener(RadixObject node, ContainerChangesListener listener) {
        if (node instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) node;
            if (getUiClassName(widget).equals(AdsMetaInfo.TAB_WIDGET_CLASS)
                    || getUiClassName(widget).equals(AdsMetaInfo.STACKED_WIDGET_CLASS)
                    || getUiClassName(widget).equals(AdsMetaInfo.SCROLL_AREA_CLASS)) {
                for (AdsWidgetDef w : widget.getWidgets()) {
                    addContainerListener(w, listener);
                }
            } else {
                if (widget.getLayout() != null) {
                    if (isUIRoot(node)) {
                        widget.getWidgets().getContainerChangesSupport().addEventListener(listener);
                    } else {
                        widget.getWidgets().getContainerChangesSupport().removeEventListener(listener); //for some cases
                    }
                    addContainerListener(widget.getLayout(), listener);
                } else {
                    widget.getWidgets().getContainerChangesSupport().addEventListener(listener);
                }
            }
        }
        if (node instanceof AdsLayout) {
            AdsLayout layout = (AdsLayout) node;
            layout.getItems().getContainerChangesSupport().addEventListener(listener);
        }
        if (node instanceof AdsRwtWidgetDef) {
            AdsRwtWidgetDef widget = (AdsRwtWidgetDef) node;
            if (getUiClassName(widget).equals(AdsMetaInfo.RWT_TAB_SET)) {
                for (AdsRwtWidgetDef w : widget.getWidgets()) {
                    addContainerListener(w, listener);
                }
            } else {
                widget.getWidgets().getContainerChangesSupport().addEventListener(listener);
            }
        }
    }

    public static void removeContainerListener(RadixObject node, ContainerChangesListener listener) {
        if (node instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) node;
            if (getUiClassName(widget).equals(AdsMetaInfo.TAB_WIDGET_CLASS)
                    || getUiClassName(widget).equals(AdsMetaInfo.STACKED_WIDGET_CLASS)
                    || getUiClassName(widget).equals(AdsMetaInfo.SCROLL_AREA_CLASS)) {
                for (AdsWidgetDef w : widget.getWidgets()) {
                    removeContainerListener(w, listener);
                }
            } else {
                if (widget.getLayout() != null) {
                    removeContainerListener(widget.getLayout(), listener);
                }
                widget.getWidgets().getContainerChangesSupport().removeEventListener(listener);
            }
        }
        if (node instanceof AdsLayout) {
            AdsLayout layout = (AdsLayout) node;
            layout.getItems().getContainerChangesSupport().removeEventListener(listener);
        }
        if (node instanceof AdsRwtWidgetDef) {
            AdsRwtWidgetDef widget = (AdsRwtWidgetDef) node;
            if (getUiClassName(widget).equals(AdsMetaInfo.RWT_TAB_SET)) {
                for (AdsRwtWidgetDef w : widget.getWidgets()) {
                    removeContainerListener(w, listener);
                }
            } else {
                widget.getWidgets().getContainerChangesSupport().removeEventListener(listener);
            }
        }
    }

    private static AdsWidgetDef getLayoutWidget(AdsLayout layout) {
        RadixObject owner = getOwner(layout);
        RadixObject owner2 = owner != null ? getOwner(owner) : null;

        if (owner instanceof AdsWidgetDef
                && !AdsMetaInfo.WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner))) {
            return (AdsWidgetDef) owner;
        }

        if (owner instanceof AdsWidgetDef
                && AdsMetaInfo.WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner))
                && owner2 instanceof AdsWidgetDef
                && AdsMetaInfo.TAB_WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner))) {
            return (AdsWidgetDef) owner2;
        }

        if (owner instanceof AdsWidgetDef
                && AdsMetaInfo.WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner))
                && owner2 instanceof AdsWidgetDef
                && AdsMetaInfo.STACKED_WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner))) {
            return (AdsWidgetDef) owner2;
        }

        if (owner instanceof AdsWidgetDef
                && AdsMetaInfo.WIDGET_CLASS.equals(AdsUIUtil.getUiClassName(owner))
                && owner2 instanceof AdsWidgetDef
                && AdsMetaInfo.SCROLL_AREA_CLASS.equals(AdsUIUtil.getUiClassName(owner))) {
            return (AdsWidgetDef) owner2;
        }
        return null;
    }

    public static RadixObject getOwner(RadixObject object) {
        assert object != null;
        final RadixObject c = object.getContainer();
        return (c instanceof RadixObjects) ? c.getContainer() : c;

    }

    public static boolean isCustomWidget(RadixObject node) {
        return (node instanceof AdsWidgetDef || node instanceof AdsRwtWidgetDef) && isCustomWidget(getUiClassName(node));
    }

    public static boolean isCustomWidget(String clazz) {
        return clazz.startsWith(EDefinitionIdPrefix.CUSTOM_WIDGET.getValue());
    }

    public AdsAbstractUIDef getCustomUI(String clazz, AdsDefinition context) {
        if (!isCustomWidget(clazz)) {
            return null;
        }
        Id id = Id.Factory.loadFrom(clazz);
        return (AdsAbstractUIDef) AdsSearcher.Factory.newAdsDefinitionSearcher(context).findById(id).get();
    }

    public AdsAbstractUIDef getCustomUI(AdsUIItemDef widget) {
        if (!isCustomWidget(widget)) {
            return null;
        }
        String className;
        if (widget instanceof AdsWidgetDef) {
            className = ((AdsWidgetDef) widget).getClassName();
        } else if (widget instanceof AdsRwtWidgetDef) {
            className = ((AdsRwtWidgetDef) widget).getClassName();
        } else {
            return null;
        }
        return getCustomUI(className, widget.getOwnerUIDef());
    }

    public static boolean isContainer(RadixObject node) {
        String clazz = AdsUIUtil.getQtClassName(node);
        return Utils.equals(clazz, AdsMetaInfo.HORIZONTAL_LAYOUT_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.VERTICAL_LAYOUT_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.GRID_LAYOUT_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.GROUP_BOX_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.TAB_WIDGET_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.SPLITTER_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.ADVANCED_SPLITTER_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.TOOL_BAR_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.FRAME_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.SCROLL_AREA_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.STACKED_WIDGET_CLASS)
                || Utils.equals(clazz, AdsMetaInfo.RWT_UI_GROUP_BOX)
                || Utils.equals(clazz, AdsMetaInfo.RWT_TOOL_BAR)
                || Utils.equals(clazz, AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER)
                || Utils.equals(clazz, AdsMetaInfo.RWT_UI_PANEL)
                || Utils.equals(clazz, AdsMetaInfo.RWT_UI_CONTAINER)
                || Utils.equals(clazz, AdsMetaInfo.RWT_TAB_SET)
                || Utils.equals(clazz, AdsMetaInfo.RWT_TAB_SET_TAB)
                || Utils.equals(clazz, AdsMetaInfo.RWT_SPLITTER)
                || Utils.equals(clazz, AdsMetaInfo.RWT_LABELED_EDIT_GRID)
                || Utils.equals(clazz, AdsMetaInfo.RWT_PROPERTIES_GRID)
                || Utils.equals(clazz, AdsMetaInfo.RWT_UI_ABSTRACT_CONTAINER)
                || Utils.equals(clazz, AdsMetaInfo.RWT_VERTICAL_BOX_CONTAINER)
                || Utils.equals(clazz, AdsMetaInfo.RWT_HORIZONTAL_BOX_CONTAINER)
                || isValEditr(node)
                || isUIRoot(node);
    }

    public static boolean isValEditr(RadixObject node) {
        String clazz = AdsUIUtil.getQtClassName(node);
        return clazz != null && clazz.startsWith(AdsMetaInfo.VAL_EDITOR_CLASS);
//            clazz.equals(AdsMetaInfo.VAL_BIN_EDITOR_CLASS)
//            || clazz.equals(AdsMetaInfo.VAL_BOOL_EDITOR_CLASS)
//            || clazz.equals(AdsMetaInfo.VAL_CHAR_EDITOR_CLASS)
//            || clazz.equals(AdsMetaInfo.VAL_CONST_SET_EDITOR)
//            || clazz.equals(AdsMetaInfo.VAL_DATE_TIME_EDITOR)
//            || clazz.equals(AdsMetaInfo.VAL_INT_EDITOR)
//            || clazz.equals(AdsMetaInfo.VAL_LIST_EDITOR)
//            || clazz.equals(AdsMetaInfo.VAL_STR_EDITOR);
    }

    public static boolean isItemWidget(RadixObject node) {
        return isItemWidget(AdsUIUtil.getQtClassName(node));
    }

    public static boolean isItemWidget(String clazz) {
        return clazz.equals(AdsMetaInfo.LIST_WIDGET_CLASS)
                || clazz.equals(AdsMetaInfo.TABLE_WIDGET_CLASS)
                || clazz.equals(AdsMetaInfo.TREE_WIDGET_CLASS)
                || clazz.equals(AdsMetaInfo.COMBO_BOX_CLASS)
                || clazz.equals(AdsMetaInfo.TABLE_VIEW_CLASS)
                || clazz.equals(AdsMetaInfo.TREE_VIEW_CLASS);
    }

    public static void addPropertyChangeListener(RadixObject node, PropertyChangeListener pcl) {
        assert node != null && pcl != null;
        if (node instanceof AdsWidgetDef) {
            ((AdsWidgetDef) node).addPropertyChangeListener(pcl);
        }
        if (node instanceof AdsLayout) {
            ((AdsLayout) node).addPropertyChangeListener(pcl);
        }
        if (node instanceof AdsLayout.SpacerItem) {
            ((AdsLayout.SpacerItem) node).addPropertyChangeListener(pcl);
        }
        if (node instanceof AdsRwtWidgetDef) {
            ((AdsRwtWidgetDef) node).addPropertyChangeListener(pcl);
        }
    }

    public static void removePropertyChangeListener(RadixObject node, PropertyChangeListener pcl) {
        assert node != null && pcl != null;
        if (node instanceof AdsWidgetDef) {
            ((AdsWidgetDef) node).removePropertyChangeListener(pcl);
        }
        if (node instanceof AdsLayout) {
            ((AdsLayout) node).removePropertyChangeListener(pcl);
        }
        if (node instanceof AdsLayout.SpacerItem) {
            ((AdsLayout.SpacerItem) node).removePropertyChangeListener(pcl);
        }
        if (node instanceof AdsRwtWidgetDef) {
            ((AdsRwtWidgetDef) node).removePropertyChangeListener(pcl);
        }
    }

    public static void fire(RadixObject node, AdsUIProperty prop, Object source) {
        assert node != null;
        if (prop == null) {
            return;
        }
        if (node instanceof AdsWidgetDef) {
            ((AdsWidgetDef) node).fire(prop, source);
        }
        if (node instanceof AdsLayout) {
            AdsWidgetDef widget = getLayoutWidget((AdsLayout) node);
            if (widget != null) {
                fire(widget, prop, source);
            } else {
                ((AdsLayout) node).fire(prop, source);
            }
        }
        if (node instanceof AdsLayout.SpacerItem) {
            ((AdsLayout.SpacerItem) node).fire(prop, source);
        }
        if (node instanceof AdsRwtWidgetDef) {
            ((AdsRwtWidgetDef) node).fire(prop, source);
        }
    }

    public static void fire(RadixObject node, AdsUIProperty prop) {
        fire(node, prop, null);
    }

    public static AdsUIItemDef currentWidget(AdsUIItemDef widget) {
        String cn = getUiClassName(widget);

        if (AdsMetaInfo.SCROLL_AREA_CLASS.equals(cn)) {
            return ((AdsWidgetDef) widget).getWidgets().get(0);
        }

        boolean isNormalWidget = !(AdsMetaInfo.TAB_WIDGET_CLASS.equals(cn)
                || AdsMetaInfo.RWT_TAB_SET.equals(cn)
                || AdsMetaInfo.STACKED_WIDGET_CLASS.equals(cn)
                || AdsMetaInfo.RWT_UI_GRID_BOX_CONTAINER.equals(cn));

        if (isNormalWidget) {
            return widget;
        }

        AdsUIProperty.IntProperty p = (AdsUIProperty.IntProperty) getUiProperty(widget, AdsWidgetProperties.CURRENT_INDEX);
        assert p != null;

        Definitions<? extends AdsUIItemDef> ws = null;
        if (widget instanceof AdsWidgetDef) {
            ws = ((AdsWidgetDef) widget).getWidgets();
        } else if (widget instanceof AdsRwtWidgetDef) {
            ws = ((AdsRwtWidgetDef) widget).getWidgets();
        }

        if (ws == null || ws.isEmpty()) {
            return null;
        }
        assert ws.size() != 0;

        if (p.value < 0 || p.value >= ws.size()) {
            UiProperties ps = getUiProperties(widget);
            if (!ps.contains(p)) {
                ps.add(p);
            }
            p.value = Math.min(Math.max(p.value, 0), ws.size() - 1);
            fire(widget, p);
        }

        return ws.get(p.value);
    }

    //universal visitor
    public static interface IVisitorUI {

        public void visit(RadixObject node, boolean active);
    }

    private static void visitWidget(AdsWidgetDef widget, IVisitorUI visitor, boolean active) {
        String className = widget.getClassName();
        if (className.equals(AdsMetaInfo.TAB_WIDGET_CLASS)
                || className.equals(AdsMetaInfo.STACKED_WIDGET_CLASS)
                || className.equals(AdsMetaInfo.SCROLL_AREA_CLASS)) {

            for (AdsWidgetDef w : widget.getWidgets()) {
                visitWidget(w, visitor, active && currentWidget(widget).equals(w));
            }
        } else if (widget.getLayout() != null) {
            visitLayout(widget.getLayout(), visitor, active);
            visitWidgets(widget.getWidgets(), visitor, active);
        } else {
            visitWidgets(widget.getWidgets(), visitor, active);
        }
    }

    private static void visitWidget(AdsRwtWidgetDef widget, IVisitorUI visitor, boolean active) {
        if (widget.getClassName().equals(AdsMetaInfo.RWT_TAB_SET)) {
            for (AdsRwtWidgetDef w : widget.getWidgets()) {
                visitWidget(w, visitor, active && currentWidget(widget).equals(w));
            }
        } else {
            for (AdsRwtWidgetDef w : widget.getWidgets()) {
                visitUI(w, visitor, active);
            }
        }
    }

    private static void visitWidgets(AdsWidgetDef.Widgets widgets, IVisitorUI visitor, boolean active) {
        for (final AdsWidgetDef w : widgets) {
            if (w.getLayout() != null && w.getClassName().equals(AdsMetaInfo.WIDGET_CLASS)) {
                visitUI(w.getLayout(), visitor, active);
            } else {
                visitUI(w, visitor, active);
            }
        }
    }

    private static void visitLayout(AdsLayout layout, IVisitorUI visitor, boolean active) {
        for (AdsLayout.Item item : layout.getItems()) {
            RadixObject node = AdsUIUtil.getItemNode(item);
            visitUI(node, visitor, active);
        }
    }

    public static void visitUI(RadixObject node, IVisitorUI visitor, boolean active) {
        visitor.visit(node, active);

        if (node instanceof AdsLayout.SpacerItem) {
            return;
        }

        if (node instanceof AdsLayout) {
            visitLayout((AdsLayout) node, visitor, active);
            return;
        }

        if (node instanceof AdsWidgetDef) {
            visitWidget((AdsWidgetDef) node, visitor, active);
            return;
        }
        if (node instanceof AdsRwtWidgetDef) {
            visitWidget((AdsRwtWidgetDef) node, visitor, active);
            return;
        }
    }

    public static AdsAbstractUIDef getUiDef(RadixObject node) {
        return RadixObjectsUtils.findContainer(node, AdsAbstractUIDef.class);
    }

    public static AdsClassDef getOwnerClassDef(RadixObject node) {
        AdsAbstractUIDef uiDef = getUiDef(node);
        if (uiDef instanceof AdsCustomEditorDef) {
            return ((AdsCustomEditorDef) uiDef).getOwnerClass();
        }
        if (uiDef instanceof AdsRwtCustomEditorDef) {
            return ((AdsRwtCustomEditorDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AdsCustomSelectorDef) {
            return ((AdsCustomSelectorDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AdsRwtCustomSelectorDef) {
            return ((AdsRwtCustomSelectorDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AbstractCustomFormDialogDef) {
            return ((AbstractCustomFormDialogDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AbstractRwtCustomFormDialogDef) {
            return ((AbstractRwtCustomFormDialogDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AdsCustomPageEditorDef) {
            return ((AdsCustomPageEditorDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AdsCustomParagEditorDef) {
            return ((AdsCustomParagEditorDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AdsRwtCustomPageEditorDef) {
            return ((AdsRwtCustomPageEditorDef) uiDef).getOwnerClass();
        } else if (uiDef instanceof AdsRwtCustomParagEditorDef) {
            return ((AdsRwtCustomParagEditorDef) uiDef).getOwnerClass();
        } else {
            return null;
        }
    }

    public static IEditorPagesContainer getOwnerEditorPagesContainer(RadixObject node) {
        for (RadixObject c = node.getContainer(); c != null; c = c.getContainer()) {
            if (c instanceof IEditorPagesContainer) {
                return (IEditorPagesContainer) c;
            }
        }
        return null;
    }

    public static IModelPublishableProperty.Provider getModelPublishablePropertyProvider(RadixObject node) {
        for (RadixObject c = node.getContainer(); c != null; c = c.getContainer()) {
            if (c instanceof IModelPublishableProperty.Provider) {
                return (IModelPublishableProperty.Provider) c;
            }
        }
        return null;
    }

    public static AdsLocalizingBundleDef getLocalizingBundle(RadixObject node) {
        AdsAbstractUIDef uiDef = getUiDef(node);
        if (uiDef != null) {
            return uiDef.findLocalizingBundle();
        }
        return null;
    }

    public static AdsMultilingualStringDef createStringDef(RadixObject node, String eng, String rus) {
        List<EIsoLanguage> languages = node.getModule().getSegment().getLayer().getLanguages();
        AdsMultilingualStringDef stringDef = AdsMultilingualStringDef.Factory.newInstance();
        for (EIsoLanguage l : languages) {
            if (l.equals(EIsoLanguage.ENGLISH)) {
                stringDef.setValue(EIsoLanguage.ENGLISH, eng);
            } else if (l.equals(EIsoLanguage.RUSSIAN)) {
                stringDef.setValue(EIsoLanguage.RUSSIAN, rus);
            } else {
                stringDef.setValue(l, eng);
            }
        }
        getLocalizingBundle(node).getStrings().getLocal().add(stringDef);
        return stringDef;
    }

    public static AdsModule getModule(RadixObject node) {
        AdsAbstractUIDef uiDef = getUiDef(node);
        if (uiDef == null) {
            return null;
        } else {
            return uiDef.getModule();
        }
    }

    public static AdsModelClassDef getModelByUI(final AdsAbstractUIDef uiDef, final boolean create) {
        if (uiDef == null) {
            return null;
        }
        if (uiDef instanceof AdsCustomDialogDef) {
            return ((AdsCustomDialogDef) uiDef).getModelClass();
        }
        if (uiDef instanceof AdsRwtCustomDialogDef) {
            return ((AdsRwtCustomDialogDef) uiDef).getModelClass();
        }
        final AdsDefinition ownerDef = uiDef.getOwnerDef();
        if (ownerDef instanceof AdsPresentationDef) {
            final AdsPresentationDef p = (AdsPresentationDef) ownerDef;
            if (create) {
                p.setUseDefaultModel(false);
            }
            return p.getModel();
        }
        if (ownerDef instanceof AdsFormHandlerClassDef) {
            return ((AdsFormHandlerClassDef) ownerDef).getPresentations().getModel();
        }
        if (ownerDef instanceof AdsReportClassDef) {
            return ((AdsReportClassDef) ownerDef).getPresentations().getModel();
        }
        if (ownerDef instanceof AdsFilterDef) {
            return ((AdsFilterDef) ownerDef).getModel();
        }
        if (ownerDef instanceof AdsParagraphExplorerItemDef) {
            return ((AdsParagraphExplorerItemDef) ownerDef).getModel();
        }
        if (ownerDef instanceof AdsEditorPageDef) {
            final AdsEditorPageDef pageDef = (AdsEditorPageDef) ownerDef;
            final AdsExplorerItemDef ei = pageDef.findEmbeddedExplorerItem();
            if (ei != null) {
                final AdsParagraphExplorerItemDef pei = ei.findOwnerExplorerRoot();
                if (pei != null) {
                    return pei.getModel();
                }
                final AdsEditorPresentationDef ep = ei.findOwnerEditorPresentation();
                if (ep != null) {
                    if (create) {
                        ep.setUseDefaultModel(false);
                    }
                    return ep.getModel();
                }
            }
            final AdsEditorPresentationDef ep = pageDef.findOwnerEditorPresentation();
            if (ep != null) {
                if (create) {
                    ep.setUseDefaultModel(false);
                }
                return ep.getModel();
            }
            final AbstractFormPresentations fp = pageDef.findOwnerPresentations();
            if (fp != null) {
                return fp.getModel();
            }
        }

        return null;
    }

    private static class Visitor implements IVisitor {

        private AdsAbstractUIDef uiDef = null;

        @Override
        public void accept(RadixObject radixObject) {
            uiDef = (AdsAbstractUIDef) radixObject;
        }

        public AdsAbstractUIDef getUI() {
            return uiDef;
        }
    }

    public static AdsAbstractUIDef findUIByModel(AdsModelClassDef model, final ERuntimeEnvironmentType env) {
        if (model == null) {
            return null;
        }
        if (model instanceof AdsDialogModelClassDef) {
            return (AdsAbstractUIDef) model.getOwnerDef();
        }

        Visitor visitor = new Visitor();
        AdsDefinition ownerDef = model.getOwnerDef();

        ownerDef.visit(visitor,
                new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof AdsAbstractUIDef && ((AdsAbstractUIDef) radixObject).getUsageEnvironment() == env;
                    }
                });

        return visitor.getUI();
    }

// debugging
    public static void debug(String title, AdsUIDef uiDef) {
        System.out.println("uiDef id = " + uiDef.getId().toString());
        debug(title, uiDef.getWidget());
    }

    public static void debug(String title, RadixObject node) {
        System.out.println("DEBUG TREE: " + title);
        debug(node, 0);
        System.out.println("----------");
    }

    private static String pref(int count) {
        char[] ch = new char[count];
        Arrays.fill(ch, ' ');

        return new String(ch);
    }

    private static void debug(RadixObject node, int lev) {
        if (node instanceof AdsWidgetDef) {
            AdsWidgetDef widget = (AdsWidgetDef) node;
            System.out.println(pref(lev) + "name = " + widget.getName() + " id = " + widget.getId() + " class = " + widget.getClassName()/*
             * + " owner = " + (AdsUtil.getUiOwner(widget) != null ?
             * AdsUtil.getUiName(AdsUtil.getUiOwner(widget)) : "null")
             */);
            if (widget.getLayout() != null) {
                debug(widget.getLayout(), lev + 1);
            }
            for (AdsWidgetDef w : widget.getWidgets()) {
                debug(w, lev + 1);
            }
        }
        if (node instanceof AdsLayout) {
            AdsLayout layout = (AdsLayout) node;
            System.out.println(pref(lev) + "name = " + layout.getName() + " class = " + layout.getClassName()/*
             * + " owner = " + (AdsUtil.getUiOwner(layout) != null ?
             * AdsUtil.getUiName(AdsUtil.getUiOwner(layout)) : "null")
             */);
            for (AdsLayout.Item item : layout.getItems()) {
                if (item instanceof AdsLayout.WidgetItem) {
                    if (AdsMetaInfo.GRID_LAYOUT_CLASS.equals(layout.getClassName())) {
                        System.out.println(pref(lev) + "item col = " + item.column + " row = " + item.row);
                    }
                    debug(((AdsLayout.WidgetItem) item).getWidget(), lev + 1);
                }
                if (item instanceof AdsLayout.LayoutItem) {
                    if (AdsMetaInfo.GRID_LAYOUT_CLASS.equals(layout.getClassName())) {
                        System.out.println(pref(lev) + "item col = " + item.column + " row = " + item.row);
                    }
                    debug(((AdsLayout.LayoutItem) item).getLayout(), lev + 1);
                }
                if (item instanceof AdsLayout.SpacerItem) {
                    AdsLayout.SpacerItem spacer = (AdsLayout.SpacerItem) item;
                    System.out.println(pref(lev + 1) + "name = " + spacer.getName() + " class = Spacer"/*
                     * + (AdsUtil.getUiOwner(spacer) != null ?
                     * AdsUtil.getUiName(AdsUtil.getUiOwner(spacer)) :
                     * "null")
                     */);
                }
            }
        }
    }

    public static boolean inCustomSelector(RadixObject node) {
        for (RadixObject c = node.getContainer(); c != null; c = c.getContainer()) {
            if (c instanceof AdsCustomSelectorDef || c instanceof AdsRwtCustomSelectorDef) {
                return true;
            }
        }
        return false;
    }

    public static boolean inCustomEditor(RadixObject node) {
        for (RadixObject c = node.getContainer(); c != null; c = c.getContainer()) {
            if (c instanceof AdsCustomEditorDef || c instanceof AdsRwtCustomEditorDef) {
                return true;
            }
        }
        return false;
    }

    public static AdsPresentationDef getOwnerPresentation(RadixObject node) {
        return RadixObjectsUtils.findContainer(node, AdsPresentationDef.class);
    }
}
