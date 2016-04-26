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

package org.radixware.wps.views.selector;

import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.Grid.DefaultRenderer;
import org.radixware.wps.rwt.IGrid.CellRenderer;
import org.radixware.wps.rwt.IGrid.CellRendererProvider;

import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;
import org.radixware.wps.rwt.tree.Tree.ICellRendererProvider;


public abstract class PropertyCellRendererProvider implements CellRendererProvider {

    private static class CommonPropertyCellRenderer {                

        private Property property;
        private final UIObject gridProvider;
        private final Html labelComponent;
        private final boolean isTree;
        private Html icon;

        public CommonPropertyCellRenderer(final UIObject gridProvider, 
                                          final Html labelComponent, 
                                          final boolean isTree) {
            this.gridProvider = gridProvider;
            this.labelComponent = labelComponent;
            this.isTree = isTree;
        }

        protected String getDisplayText(final Object value) {
            if (value instanceof Property) {
                final Property prop = (Property) value;
                if (SelectorWidgetController.canUseStandardCheckBox(prop)) {
                    final Object obj = ((PropertyBool) value).getValueObject();
                    if (obj == null) {
                        return "\u25FC";
                    }
                    if (obj instanceof Boolean && ((Boolean) obj).booleanValue()) {
                        return "\u2714";
                    } else {
                        return " ";
                    }
                } else {
                    return SelectorWidgetController.getTextToDisplay(prop);
                }
            } else {
                return String.valueOf(value);
            }
        }

        private WpsIcon getPropertyValueIcon(final Property property) {
            final RadEnumPresentationDef constSet = property.getDefinition().getConstSet();
            if (constSet == null || property.getDefinition().getType().isArrayType()) {
                return null;
            }

            final Object val = property.getValueObject();
            if (val == null) {
                return null;
            }
            
            final RadEnumPresentationDef.Item item;            

            if (val instanceof IKernelEnum){ // val instanceof Long
                item = constSet.findItemForConstant((IKernelEnum) val);
            } else {
                final EValType valType = property.getDefinition().getType();
                item = constSet.getItems().findItemByValue(ValAsStr.Factory.newInstance(val, valType));
            }
            return item == null ? null : (WpsIcon) item.getIcon();
        }
        
        private static String getToolTipForProperty(final Property property){
            if (!property.isReadonly()){
                if (property.isUnacceptableInputRegistered()){
                    final String messageTemplate = 
                        property.getUnacceptableInput().getMessageText(InvalidValueReason.EMessageType.ThisPropertyValue);
                    return String.format(messageTemplate, property.getTitle());
                }else{                
                    ValidationResult state = 
                            property.getEditMask().validate(property.getEnvironment(),property.getValueObject());
                    if (state!=ValidationResult.ACCEPTABLE){
                        return state.getInvalidValueReason().toString();
                    }
                    state = property.getOwner().getPropertyValueState(property.getId());
                    if (state!=ValidationResult.ACCEPTABLE){
                        return state.getInvalidValueReason().toString();
                    }        
                }
            }
            return property.getHint();
        }

        public void update(final int r, final int c, final Object value, final Html html, final IModelWidget modelWidget) {
            if (value instanceof Property) {
                final Property prop = (Property) value;
                if (property != prop) {//NOPMD
                    if (property != null) {
                        property.unsubscribe(modelWidget);
                    }
                    property = prop;
                    if (SelectorWidgetController.canUseStandardCheckBox(prop)) {
                        final Html innerDiv;
                        if (isTree) {
                            if (labelComponent.getParent() == html) {//нужно переложить
                                innerDiv = new Div();
                                html.remove(labelComponent);
                                labelComponent.renew();
                                html.add(innerDiv);
                                innerDiv.add(labelComponent);
                            } else {
                                innerDiv = labelComponent.getParent();
                            }
                            labelComponent.setCss("left", "0px");//NOPMD
                            labelComponent.setCss("top", "-2px");
                        }else{
                            innerDiv = html;
                        }
                        
                        drawCheckBoxBorder(innerDiv, canEditPropertyValue(property));

                    } else {
                        final WpsIcon valueIcon = getPropertyValueIcon(prop);
                        if (valueIcon == null) {
                            if (this.icon != null) {
                                this.icon.remove();
                                this.icon = null;
                                html.setCss("white-space", null);
                            }
                        } else {
                            if (this.icon == null) {
                                this.icon = new Html("img");
                                this.icon.setCss("width", "12px");
                                this.icon.setCss("height", "12px");
                                this.icon.setCss("top", "1px");
                                this.icon.setCss("position", "relative");
                                html.add(0, this.icon); 
                                html.setCss("white-space", "nowrap");
                            }
                            if (!Utils.equals(this.icon.getAttr("src"), valueIcon.getURI(gridProvider))) {
                                this.icon.setAttr("src", valueIcon.getURI(gridProvider));
                            }                            
                        }
                        resetComponentState(html);
                    }                    
                }
                html.setAttr("title", getToolTipForProperty(property));
            }
        }        
        
        private static void drawCheckBoxBorder(final Html html, final boolean isReadOnly){
            html.setCss("width", "10px");
            html.setCss("height", "12px");
            html.setCss("padding", null);
            html.setCss("padding-top", "0px");
            html.setCss("padding-bottom", "0px");
            html.setCss("padding-right", "0px");
            html.setCss("padding-left", "2px");
            html.setCss("margin-left", "4px");
            html.setCss("vertical-align", "middle");
            html.setCss("border", "solid 1px #BBB");
            html.setAttr("onClick",isReadOnly ? null : "default");
        }
        
        private static boolean canEditPropertyValue(final Property property) {
            return !property.isReadonly()
                    && (property.hasOwnValue() || !property.isValueDefined())
                    && !property.isCustomEditOnly()
                    && property.getEditPossibility() != EEditPossibility.PROGRAMMATICALLY
                    && property.isEnabled();
        }    

        protected final void resetComponentState(final Html html) {
            html.setCss("vertical-align", "middle");
            html.setCss("overflow", "hidden");
            html.setCss("width", "100%");
            html.setCss("height", "100%");
            if (labelComponent != null) {
                labelComponent.setCss("padding-right", "6px");
            }
        }

        public Property getProperty() {
            return property;
        }
        
        public final void processClick(){
            if (isEditable()){
                final Boolean currentValue = (Boolean)property.getValueObject();
                final Boolean newValue;
                if (currentValue==null){
                    newValue = Boolean.TRUE;
                }else if (currentValue.booleanValue()){
                    newValue = Boolean.FALSE;                    
                }else{
                    newValue = property.isMandatory() ? Boolean.TRUE : null;
                }
                property.setValueObject(newValue);
            }
        }
        
        public final boolean isEditable(){
            return property!=null && SelectorWidgetController.canUseStandardCheckBox(property) && canEditPropertyValue(property);
        }
    }

    private static class PropertyGridCellRenderer extends DefaultRenderer implements IModelWidget {

        private final CommonPropertyCellRenderer renderer = new CommonPropertyCellRenderer(this, null, false);
        private boolean isSelected;

        @Override
        protected String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }

        @Override
        public void update(final int r, final int c, final Object value) {
            final boolean initializing = renderer.getProperty() == null && value instanceof Property;
            renderer.update(r, c, value, this.getHtml(), this);
            if (initializing && isSelected) {
                bind();
            }
            super.update(r, c, value);
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
            if (renderer.getProperty() == null) {
                isSelected = isRowSelected;
            } else {
                if (isRowSelected) {
                    bind();
                } else {
                    renderer.getProperty().unsubscribe(this);
                }                
            }
        }

        @Override
        public void refresh(final ModelItem aThis) {
            update(0, 0, renderer.getProperty());
        }

        @Override
        public boolean setFocus(final Property aThis) {
            return false;
        }

        @Override
        public void bind() {
            if (renderer.getProperty() != null) {
                renderer.getProperty().subscribe(this);
                refresh(renderer.getProperty());
            }
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                renderer.processClick();
            }else{
                super.processAction(actionName, actionParam);
            }                
        }
        
        @Override
        public UIObject getUI() {
            final UIObject ui = super.getUI();
            if (renderer.isEditable()){
                final UIObject parent = ui.getParent();
                if (parent!=null){
                    parent.getHtml().addClass("editor-cell");
                }
            }
            return ui;
        }        
    }

    private static class PropertyFirstTreeColumnCellRenderer extends Node.DefaultTreeColumnCellRenderer implements IModelWidget {

        private final CommonPropertyCellRenderer renderer = new CommonPropertyCellRenderer(this, label, true);

        public PropertyFirstTreeColumnCellRenderer() {
            super();
            setDefaultClassName("rwt-ui-element-text");
        }

        @Override
        protected String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }

        @Override
        public void update(final Node node, final int c, final Object value) {
            renderer.update(0, c, value, this.getHtml(), this);
            super.update(node, c, value);
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
            if (renderer.getProperty() != null) {
                if (isRowSelected) {
                    bind();
                } else {
                    renderer.getProperty().unsubscribe(this);
                }
            }
        }

        private Node getNode() {
            final UIObject parent = getParent();
            if (parent == null) {
                return null;
            }
            return (Node) parent.getParent();
        }

        @Override
        public void refresh(final ModelItem aThis) {
            final Node n = getNode();
            if (n != null) {
                update(n, 0, renderer.getProperty());
            }
        }

        @Override
        public boolean setFocus(final Property aThis) {
            return false;
        }

        @Override
        public void bind() {
            if (renderer.getProperty() != null) {
                renderer.getProperty().subscribe(this);
                refresh(renderer.getProperty());
            }
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                renderer.processClick();
            }else{
                super.processAction(actionName, actionParam);
            }                
        }         

        @Override
        public UIObject getUI() {
            final UIObject ui = super.getUI();
            if (renderer.isEditable()){
                final UIObject parent = ui.getParent();
                if (parent!=null){
                    parent.getHtml().addClass("editor-cell");
                }
            }
            return ui;
        }
    }

    private static class PropertyTreeColumnCellRenderer extends Node.DefaultTreeCellRenderer implements IModelWidget {

        private final CommonPropertyCellRenderer renderer = new CommonPropertyCellRenderer(this, label, true);

        public PropertyTreeColumnCellRenderer() {
            super();
            setDefaultClassName("rwt-ui-element-text");
        }

        @Override
        protected String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }

        @Override
        public void update(final Node node, final int c, final Object value) {
            renderer.update(0, c, value, this.getHtml(), this);
            super.update(node, c, value);
        }

        @Override
        public void rowSelectionChanged(final boolean isRowSelected) {
            if (renderer.getProperty() != null) {
                if (isRowSelected) {
                    bind();
                } else {
                    renderer.getProperty().unsubscribe(this);
                }
            }
        }

        private Node getNode() {
            final UIObject parent = getParent();
            if (parent == null) {
                return null;
            }
            return (Node) parent.getParent();
        }

        @Override
        public void refresh(final ModelItem aThis) {
            final Node n = getNode();
            if (n != null) {
                update(n, 0, renderer.getProperty());
            }
        }

        @Override
        public boolean setFocus(final Property aThis) {
            return false;
        }

        @Override
        public void bind() {
            if (renderer.getProperty() != null) {
                renderer.getProperty().subscribe(this);
                refresh(renderer.getProperty());
            }
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                renderer.processClick();
            }else{
                super.processAction(actionName, actionParam);
            }                
        }

        @Override
        public UIObject getUI() {
            final UIObject ui = super.getUI();
            if (renderer.isEditable()){
                final UIObject parent = ui.getParent();
                if (parent!=null){
                    parent.getHtml().addClass("editor-cell");
                }
            }
            return ui;
        }    
    }
    
    
    private final static PropertyCellRendererProvider INSTANCE_FOR_GRID = new PropertyCellRendererProvider() {
        @Override
        public CellRenderer newCellRenderer(final int r, final int c) {
            return new PropertyGridCellRenderer();
        }
    };
    private final static ICellRendererProvider INSTANCE_FOR_TREE = new ICellRendererProvider() {
        @Override
        public INodeCellRenderer newCellRenderer(final Node n, final int c) {
            if (c == 0) {
                return new PropertyFirstTreeColumnCellRenderer();
            } else {
                return new PropertyTreeColumnCellRenderer();
            }
        }
    };

    private PropertyCellRendererProvider() {
    }

    public static PropertyCellRendererProvider getInstanceForGrid() {
        return INSTANCE_FOR_GRID;
    }

    public static ICellRendererProvider getInstanceForTree() {
        return INSTANCE_FOR_TREE;
    }
}