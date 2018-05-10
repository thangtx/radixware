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

import java.awt.Color;
import java.util.EnumSet;
import org.radixware.kernel.common.client.enums.ETextAlignment;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.html.Html;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.validators.InvalidValueReason;
import org.radixware.kernel.common.client.meta.mask.validators.ValidationResult;
import org.radixware.kernel.common.client.models.items.ModelItem;
import org.radixware.kernel.common.client.models.items.SelectorColumnModelItem;
import org.radixware.kernel.common.client.models.items.properties.Property;
import org.radixware.kernel.common.client.models.items.properties.PropertyBool;
import org.radixware.kernel.common.client.widgets.IModelWidget;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EEditPossibility;
import org.radixware.kernel.common.enums.EKeyEvent;
import org.radixware.kernel.common.enums.ESelectorColumnAlign;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.types.IKernelEnum;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.WpsEnvironment;
import org.radixware.wps.WpsSettings;
import org.radixware.wps.icons.WpsIcon;
import org.radixware.wps.rwt.*;
import org.radixware.wps.rwt.Grid.DefaultRenderer;
import org.radixware.wps.rwt.IGrid.CellRenderer;
import org.radixware.wps.rwt.IGrid.CellRendererProvider;
import org.radixware.wps.rwt.events.EHtmlEventType;
import org.radixware.wps.rwt.events.HtmlEvent;
import org.radixware.wps.rwt.events.KeyDownEventFilter;
import org.radixware.wps.rwt.events.KeyDownHtmlEvent;

import org.radixware.wps.rwt.tree.Node;
import org.radixware.wps.rwt.tree.Node.INodeCellRenderer;
import org.radixware.wps.rwt.tree.Tree.ICellRendererProvider;
import org.radixware.wps.text.WpsTextOptions;


public abstract class PropertyCellRendererProvider implements CellRendererProvider {

    public static class CommonPropertyCellRenderer {                

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
            gridProvider.getHtml().setAttr("nolayout","true");
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
                        return state.getInvalidValueReason().getMessage(property.getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
                    }
                    state = property.getOwner().getPropertyValueState(property.getId());
                    if (state!=ValidationResult.ACCEPTABLE){
                        return state.getInvalidValueReason().getMessage(property.getEnvironment().getMessageProvider(), InvalidValueReason.EMessageType.Value);
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
                        final boolean canEditPropertyValue = canEditPropertyValue(property);
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
                        
                        drawCheckBoxBorder(innerDiv, !canEditPropertyValue);

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
        
        public static void drawCheckBoxBorder(final Html html, final boolean isReadOnly){            
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
            html.setCss("background-color", "#FFFFFF");
            html.setAttr("onclick",isReadOnly ? null : "default");
        }
        
        public static boolean canEditPropertyValue(final Property property) {
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
            if (isTree && labelComponent != null) {
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

    public static class PropertyGridCellRenderer extends DefaultRenderer implements IModelWidget {

        private final CommonPropertyCellRenderer renderer = new CommonPropertyCellRenderer(this, label, false);
        private boolean isSelected;

        @Override
        protected String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }

        @Override
        public void update(final int r, final int c, final Object value) {
            final boolean initializing = getProperty() == null && value instanceof Property;
            renderer.update(r, c, value, this.getHtml(), this);
            if (initializing && isSelected) {
                bind();
            }
            super.update(r, c, value);
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
            final Property property = getProperty();
            if (property != null) {
                if (isRowSelected) {
                    bind();
                } else {
                    if (SelectorWidgetController.canUseStandardCheckBox(property)){
                        unsubscribeFromEvent(EHtmlEventType.KEYDOWN);
                    }
                    property.unsubscribe(this);
                }
            }
            isSelected = isRowSelected;
        }

        @Override
        public void refresh(final ModelItem aThis) {
            update(0, 0, getProperty());
        }

        @Override
        public boolean setFocus(final Property aThis) {
            return false;
        }

        @Override
        public void bind() {
            if (getProperty() != null) {
                getProperty().subscribe(this);
                refresh(getProperty());
                if (SelectorWidgetController.canUseStandardCheckBox(getProperty())){
                    subscribeToEvent(new KeyDownEventFilter(EKeyEvent.VK_SPACE));
                }
            }
        }

        @Override
        protected void processHtmlEvent(final HtmlEvent event) {
            if (event instanceof KeyDownHtmlEvent){
                final KeyDownHtmlEvent keyEvent = (KeyDownHtmlEvent)event;
                if (keyEvent.getButton()==EKeyEvent.VK_SPACE.getValue() || keyEvent.getKeyboardModifiers().isEmpty()){
                    final Property property = getProperty();
                    if (property!=null && CommonPropertyCellRenderer.canEditPropertyValue(property)){
                        final Boolean currentValue = (Boolean)property.getValueObject();
                        final Boolean newValue;
                        if (currentValue==null){
                            newValue = Boolean.TRUE;
                        }else if (currentValue.equals(Boolean.FALSE)){
                            newValue = property.isMandatory() ? Boolean.TRUE : null;
                        }else{
                            newValue = Boolean.FALSE;
                        }
                        property.setValueObject(newValue);
                    }
                    return;
                }
            }
            super.processHtmlEvent(event);
        }
        
        
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {                
                if (isSelected){
                    processClick();
                }else{
                    final UIObject parent = getUI().getParent();
                    if (parent!=null){
                        parent.processAction(actionName, actionParam);
                    }
                }
            }else{
                super.processAction(actionName, actionParam);
            }
        }

        @Override
        public void setBackground(final Color c) {
            if (!isEditable()){//always white background color in embedded checkbox
                super.setBackground(c);
            }
        }
        
        protected Property getProperty(){
            return renderer.getProperty();
        }
        
        protected boolean isEditable(){
            return renderer.isEditable();
        }
        
        protected void processClick(){
            renderer.processClick();
        }
    }
    
    private static class CommonTreeColumnCellRenderer implements IModelWidget{
        
        private final CommonPropertyCellRenderer renderer;
        private final INodeCellRenderer cellRenderer;
        private final int columnIndex;
        
        public CommonTreeColumnCellRenderer(final INodeCellRenderer cellRenderer, 
                                                                      final Html labelComponent, 
                                                                      final int columnIndex){
            renderer = new CommonPropertyCellRenderer((UIObject)cellRenderer, labelComponent, true);
            this.cellRenderer = cellRenderer;
            this.columnIndex = columnIndex;
        }
                
        public String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }
        
        public void update(final Node node, final int c, final Object value) {
            renderer.update(0, c, value, ((UIObject)cellRenderer).getHtml(), this);
            updateTextOptions(node, c, value);
        }
        
        private void updateTextOptions(final Node node, final int c, final Object value){
            if (node.getTree()!=null 
                && value instanceof Property 
                && node.getTree().getColumn(c).getUserData() instanceof SelectorColumnModelItem){
                final SelectorColumnModelItem column = 
                    (SelectorColumnModelItem)node.getTree().getColumn(c).getUserData();
                final Property property = (Property)value;                                
                WpsTextOptions options = getTextOptions(property);
                final Alignment alignment = applyBodyAlignmentSettings(property, column.getAlignment());
                if (alignment!=null){
                    options = options.changeAlignment(ETextAlignment.fromStr(alignment.name()));
                }
                node.setColumnTextOptions(c, options);
            }
        }
        
        private WpsTextOptions getTextOptions(final Property property) {      
            final EnumSet<ETextOptionsMarker> propertyMarkers = property.getTextOptionsMarkers();
            if (propertyMarkers.contains(ETextOptionsMarker.INVALID_VALUE)) {
                propertyMarkers.remove(ETextOptionsMarker.INVALID_VALUE);
            }
            return (WpsTextOptions) property.getValueTextOptions().getOptions(propertyMarkers);
        }        
        
        private Alignment applyBodyAlignmentSettings(Property prop, ESelectorColumnAlign defaultAlign) {
            ESelectorColumnAlign align = null;
            Alignment alignment;
            if (prop == null) {
                return null;
            } else {
                final WpsSettings settings = ((WpsEnvironment) prop.getEnvironment()).getConfigStore();
                try {
                    if (defaultAlign != ESelectorColumnAlign.DEFAULT) {
                        align = defaultAlign;
                    } else {
                        final EValType valType = prop.getType();
                        settings.beginGroup(SettingNames.SYSTEM);
                        settings.beginGroup(SettingNames.SELECTOR_GROUP);
                        settings.beginGroup(SettingNames.Selector.COMMON_GROUP);
                        settings.beginGroup(SettingNames.Selector.Common.BODY_ALIGNMENT);
                        final Long a = new Long(settings.readInteger(valType.getName(), defaultAlign.getValue().intValue()));
                        align = ESelectorColumnAlign.getForValue(a);
                    }
                } finally {
                    if (settings.group() != null) {
                        settings.endGroup();
                        settings.endGroup();
                        settings.endGroup();
                        settings.endGroup();
                    }
                    switch (align) {
                        case CENTER:
                            alignment = Alignment.CENTER;
                            break;
                        case DEFAULT:
                            alignment = Alignment.LEFT;
                            break;
                        case LEFT:
                            alignment = Alignment.LEFT;
                            break;
                        case RIGHT:
                            alignment = Alignment.RIGHT;
                            break;
                        default:
                            alignment = Alignment.LEFT;
                            break;
                    }
                }
            }
            return alignment;
        }
        
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
            final UIObject parent = ((UIObject)cellRenderer).getParent();
            if (parent == null) {
                return null;
            }
            return (Node) parent.getParent();
        }

        @Override
        public void refresh(final ModelItem aThis) {
            final Node n = getNode();
            if (n != null) {
                cellRenderer.update(n, columnIndex, renderer.getProperty());
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
                
        public boolean processAction(final String actionName, final String actionParam) {
            if (Events.EVENT_NAME_ONCLICK.equals(actionName)) {
                renderer.processClick();
                return true;
            }else{
                return false;
            }                
        }

        public UIObject getUI(UIObject rendererUi) {            
            if (renderer.isEditable()){
                final UIObject parent = rendererUi.getParent();
                if (parent!=null){
                    parent.getHtml().addClass("editor-cell");
                }
            }
            return rendererUi;
        }
        
    }

    private static class PropertyFirstTreeColumnCellRenderer extends Node.DefaultTreeColumnCellRenderer {

        private final CommonTreeColumnCellRenderer renderer;

        public PropertyFirstTreeColumnCellRenderer() {
            super();
            renderer = new CommonTreeColumnCellRenderer(this, label, 0);
            setDefaultClassName("rwt-ui-element-text");
        }

        @Override
        protected String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }

        @Override
        public void update(final Node node, final int c, final Object value) {
            renderer.update(node, c, value);
            super.update(node, c, value);
        }

        @Override
        public void rowSelectionChanged(boolean isRowSelected) {
            renderer.rowSelectionChanged(isRowSelected);
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (!renderer.processAction(actionName, actionParam)){
                super.processAction(actionName, actionParam);
            }
        }         

        @Override
        public UIObject getUI() {
            return renderer.getUI(super.getUI());
        }
    }

    private static class PropertyTreeColumnCellRenderer extends Node.DefaultTreeCellRenderer {

        private final CommonTreeColumnCellRenderer renderer;

        public PropertyTreeColumnCellRenderer(final int column) {
            super();
            renderer = new CommonTreeColumnCellRenderer(this, label, column);
            setHeight(20);
            setDefaultClassName("rwt-ui-element-text");
        }

        @Override
        protected String getDisplayText(final Object value) {
            return renderer.getDisplayText(value);
        }

        @Override
        public void update(final Node node, final int c, final Object value) {
            renderer.update(node, c, value);
            super.update(node, c, value);
        }
        
        @Override
        public void rowSelectionChanged(final boolean isRowSelected) {
            renderer.rowSelectionChanged(isRowSelected);
        }
        
        @Override
        public void processAction(final String actionName, final String actionParam) {
            if (!renderer.processAction(actionName, actionParam)){
                super.processAction(actionName, actionParam);
            }
        }

        @Override
        public UIObject getUI() {
            return renderer.getUI(super.getUI());
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
                return new PropertyTreeColumnCellRenderer(c);
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