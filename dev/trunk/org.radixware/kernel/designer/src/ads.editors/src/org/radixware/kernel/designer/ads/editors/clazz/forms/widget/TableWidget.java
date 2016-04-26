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
package org.radixware.kernel.designer.ads.editors.clazz.forms.widget;

import java.beans.PropertyChangeEvent;
import java.util.List;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.EditProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ui.AdsItemWidgetDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIProperty;
import org.radixware.kernel.common.defs.ads.ui.UiProperties;
import org.radixware.kernel.designer.ads.editors.clazz.forms.GraphSceneImpl;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.EditorDialog;
import org.radixware.kernel.designer.ads.editors.clazz.forms.dialog.TablePanel;
import org.radixware.kernel.designer.ads.editors.clazz.forms.props.UIPropertySupport;

public class TableWidget extends BaseWidget {

    public TableWidget(GraphSceneImpl scene, AdsItemWidgetDef node) {
        super(scene, node);

        syncProperties(node);
    }
    private static final String PROP_COLUMN_COUNT_NAME = "columnCount";
    private static final String PROP_ROW_COUNT_NAME = "rowCount";

    private static void syncProperties(AdsItemWidgetDef widget) {
        if (widget != null) {
            UiProperties properties = widget.getProperties();

            AdsUIProperty.IntProperty columnCount = (AdsUIProperty.IntProperty) properties.getByName(PROP_COLUMN_COUNT_NAME);
            int colSize = widget.getColumns().size();
            if (columnCount != null) {
                columnCount.value = colSize;
            } else {
                columnCount = new AdsUIProperty.IntProperty(PROP_COLUMN_COUNT_NAME, colSize);
                properties.add(columnCount);
            }

            AdsUIProperty.IntProperty rowCount = (AdsUIProperty.IntProperty) properties.getByName(PROP_ROW_COUNT_NAME);
            int rowSize = widget.getRows().size();
            if (rowCount != null) {
                rowCount.value = rowSize;
            } else {
                rowCount = new AdsUIProperty.IntProperty(PROP_ROW_COUNT_NAME, rowSize);
                properties.add(rowCount);
            }
        }
    }

    @Override
    protected List<WidgetAction> getInitialActions(GraphSceneImpl scene, RadixObject node) {
        List<WidgetAction> actions = super.getInitialActions(scene, node);
        actions.add(ActionFactory.createEditAction(new EditProvider() {

            @Override
            public void edit(Widget widget) {
                if (notifyEdited()) {
                    repaint();
                }
            }
        }));
        return actions;
    }

    protected boolean notifyEdited() {
        AdsItemWidgetDef widget = (AdsItemWidgetDef) getNode();
        return EditorDialog.execute(new TablePanel(((GraphSceneImpl) getScene()).getUI(), widget));
    }
    private static final String ITEM_PROP_NAME_TEXT = "text";
    private static final ItemContainer rowContainer = new RowContainer();
    private static final ItemContainer columnContainer = new ColumnContainer();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof UIPropertySupport || evt.getSource() == this) {
            AdsUIProperty prop = (AdsUIProperty) evt.getNewValue();
            if (prop != null) {
                if ("columnCount".equals(prop.getName())) {
                    AdsUIProperty.IntProperty columnCount = (AdsUIProperty.IntProperty) prop;
                    columnContainer.syncItems(columnCount.value, (AdsItemWidgetDef) getNode());
                } else if ("rowCount".equals(prop.getName())) {
                    AdsUIProperty.IntProperty rowCount = (AdsUIProperty.IntProperty) prop;
                    rowContainer.syncItems(rowCount.value, (AdsItemWidgetDef) getNode());
                }
            }
        }

        super.propertyChange(evt);
    }

    private static abstract class ItemContainer {

        ItemContainer() {
        }

        final void syncItems(int propValue, AdsItemWidgetDef table) {
            if (table != null) {
                int valDiff = size(table) - propValue;
                if (valDiff < 0) {
                    while (valDiff++ != 0) {
                        add(table);
                    }
                } else if (valDiff > 0) {
                    while (valDiff-- != 0) {
                        removeLast(table);
                    }
                }
            }
        }

        abstract int size(AdsItemWidgetDef table);

        abstract void removeLast(AdsItemWidgetDef table);

        abstract void add(AdsItemWidgetDef table);
    }

    private static final class ColumnContainer extends ItemContainer {

        @Override
        int size(AdsItemWidgetDef table) {
            return table.getColumns().size();
        }

        @Override
        void removeLast(AdsItemWidgetDef table) {
            int size = size(table);
            if (size > 0) {
                table.getColumns().remove(size - 1);
            }
        }

        @Override
        void add(AdsItemWidgetDef table) {
            AdsItemWidgetDef.Column column = new AdsItemWidgetDef.Column();
            AdsUIProperty.LocalizedStringRefProperty text = new AdsUIProperty.LocalizedStringRefProperty(ITEM_PROP_NAME_TEXT);
            column.getProperties().add(text);
            table.getColumns().add(column);
        }
    }

    private static final class RowContainer extends ItemContainer {

        @Override
        int size(AdsItemWidgetDef table) {
            return table.getRows().size();
        }

        @Override
        void removeLast(AdsItemWidgetDef table) {
            int size = size(table);
            if (size > 0) {
                table.getRows().remove(size - 1);
            }
        }

        @Override
        void add(AdsItemWidgetDef table) {
            AdsItemWidgetDef.Row row = new AdsItemWidgetDef.Row();
            AdsUIProperty.LocalizedStringRefProperty text = new AdsUIProperty.LocalizedStringRefProperty(ITEM_PROP_NAME_TEXT);
            row.getProperties().add(text);
            table.getRows().add(row);
        }
    }
}
