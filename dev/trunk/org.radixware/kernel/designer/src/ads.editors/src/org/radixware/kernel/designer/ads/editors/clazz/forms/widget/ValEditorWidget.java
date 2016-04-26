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

/*
 * 10/31/11 3:43 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.AdsUIUtil;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShadow;
import org.radixware.kernel.common.defs.ads.ui.enums.EFrameShape;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.DragDropLocator;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.LayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.common.ValEditorLayoutProcessor;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.Item;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items.ValEditorItem;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class ValEditorWidget extends BaseWidget {

    protected ValEditorWidget(GraphSceneImpl scene, AdsWidgetDef node, ValEditorLayoutProcessor.Align align) {
        super(scene, node);
        setLayoutProcessor(new ValEditorLayoutProcessor(this, node, align));
    }

    public ValEditorWidget(GraphSceneImpl scene, AdsWidgetDef node) {
        this(scene, node, ValEditorLayoutProcessor.Align.RIGHT);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (propertyAccept(evt)) {

            AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            if ("frame".equals(prop.getName())) {
                if (((AdsUIProperty.BooleanProperty) prop).value) {
                    ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(getNode(), "frameShape")).value = EFrameShape.StyledPanel;
                    ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(getNode(), "frameShadow")).value = EFrameShadow.Sunken;
                } else {
                    ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(getNode(), "frameShape")).value = EFrameShape.NoFrame;
                    ((AdsUIProperty.EnumValueProperty) AdsUIUtil.getUiProperty(getNode(), "frameShadow")).value = EFrameShadow.Plain;
                }
                return;
            }
        }
        super.propertyChange(evt);
    }

    @Override
    public void locate(DragDropLocator locator, Point localPoint) {
        super.locate(locator, localPoint);
    }

    @Override
    public boolean isContainer(String clazz, Point localPoint) {
        return super.isContainer(clazz, localPoint) && AdsMetaInfo.TOOL_BUTTON_CLASS.equals(clazz);
    }

    @Override
    public final void setLayoutProcessor(LayoutProcessor layoutProcessor) {
        if (this.getLayoutProcessor() == null) {
            super.setLayoutProcessor(layoutProcessor);
        }
    }

    @Override
    public boolean canChangeLayout() {
        return false;
    }

    @Override
    public final AdsWidgetDef getNode() {
        return (AdsWidgetDef) super.getNode();
    }

    @Override
    public void notifyClicked(Point localLocation) {
        AdsWidgetDef node = getNode();

        Insets insets = getBorder().getInsets();
        Rectangle bounds = getBounds();

        Rectangle r = new Rectangle(
            bounds.x + insets.left,
            bounds.y + insets.top,
            bounds.width - insets.right - insets.left,
            bounds.height - insets.top - insets.bottom);

        boolean in = insideClearButton(r, localLocation, node);
        if (in) {
            AdsUIProperty value = node.getProperties().getByName("value");
            if (value != null) {
                if (DialogUtils.messageConfirmation("Clear value?")) {
                    value.delete();
                    AdsUIUtil.fire(node, value, this);
                    getScene().revalidate();
                }
            }
        }
    }

    protected boolean propertyAccept(PropertyChangeEvent evt) {
        return evt.getSource() instanceof UIPropertySupport || evt.getSource() == this;
    }

    private boolean insideClearButton(Rectangle rect, Point localLocation, AdsWidgetDef node) {
        ValEditorItem item = (ValEditorItem) Item.getItem(node);
        assert item != null : "item cann't be null";

        AdsUIProperty.BooleanProperty isReadOnly = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "readOnly");
        AdsUIProperty.BooleanProperty isNotNull = (AdsUIProperty.BooleanProperty) AdsUIUtil.getUiProperty(node, "notNull");

        assert isReadOnly != null && isNotNull != null : "Properties are null";

        ValEditorItem.ValEditorPropertyCollector collector = (ValEditorItem.ValEditorPropertyCollector) item.getPropertyCollector();

        boolean result = collector.getClearButtonRect(node, rect).contains(localLocation);
        result = result && !isReadOnly.value && !isNotNull.value;

        return result;
    }
}
