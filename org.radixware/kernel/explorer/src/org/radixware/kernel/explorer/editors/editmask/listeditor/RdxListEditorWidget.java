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
package org.radixware.kernel.explorer.editors.editmask.listeditor;

import com.trolltech.qt.core.QAbstractItemModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QHeaderView.ResizeMode;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QStyleOptionViewItem;
import com.trolltech.qt.gui.QTableWidget;
import com.trolltech.qt.gui.QTableWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.explorer.editors.valeditors.AdvancedValBoolEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValIntEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValNumEditor;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;

public final class RdxListEditorWidget extends QTableWidget {

    private class ConcreteDelegate extends QItemDelegate {

        private final EValType type;
        private final IClientEnvironment environment;

        public ConcreteDelegate(final IClientEnvironment environment, final EValType type) {
            super();
            this.type = type;
            this.environment = environment;
        }

        @Override
        public ValEditor createEditor(final QWidget parent, final QStyleOptionViewItem option, final QModelIndex index) {
            ValEditor editor = null;
            switch (type) {
                case CHAR:
                case ARR_CHAR:
                    editor = new ValStrEditor(environment, parent);
                    final EditMaskStr editMask = new EditMaskStr();
                    editMask.setMaxLength(1);
                    editor.setEditMask(editMask);
                    break;
                case INT:
                case ARR_INT:
                    editor = new ValIntEditor(environment, parent);
                    break;
                case NUM:
                case ARR_NUM:
                    editor = new ValNumEditor(environment, parent, new EditMaskNum(), true, false);
                    break;
                case STR:
                case ARR_STR:
                    editor = new ValStrEditor(environment, parent);
                    break;
                case BOOL:
                case ARR_BOOL:
                    editor = new AdvancedValBoolEditor(environment, parent);
                    break;
                default:
                    throw new IllegalArgumentException("This mask editor doesn't support type " + type);
            }
            if (editor != null) {
                editor.changeStateForGrid();
            }
            return editor;
        }

        @Override
        public void setEditorData(final QWidget editor, final QModelIndex index) {
            final Object data = index.data(Qt.ItemDataRole.UserRole);
            if (data == null) {
                super.setEditorData(editor, index);
                return;
            }

            if (editor instanceof ValIntEditor) {
                ((ValIntEditor) editor).setValue((Long) data);
            } else if (editor instanceof ValNumEditor) {
                ((ValNumEditor) editor).setValue(BigDecimal.valueOf((Double) data));
            } else if (editor instanceof ValStrEditor) {
                ((ValStrEditor) editor).setValue((String) data);
            } else {
                super.setEditorData(editor, index);
            }

        }

        @Override
        public void setModelData(final QWidget editor, final QAbstractItemModel model, final QModelIndex index) {
            if (editor instanceof ValEditor) {

                final Object value = ((ValEditor) editor).getValue();
                model.setData(index, value, Qt.ItemDataRole.UserRole);
                final String newData = (value == null) ? "" : String.valueOf(value);
                model.setData(index, newData, Qt.ItemDataRole.DisplayRole);

            } else {
                super.setModelData(editor, model, index);
            }
        }

        @Override
        public QSize sizeHint(final QStyleOptionViewItem option, final QModelIndex index) {
            return super.sizeHint(option, index);
        }
    } // ConcreteDelegate

    private final IClientEnvironment environment;

    public RdxListEditorWidget(final IClientEnvironment environment, final QWidget parent, final EValType type) {
        super(parent);
        this.environment = environment;
        final ConcreteDelegate valuesDelegate = new ConcreteDelegate(environment, type);
        final ConcreteDelegate titlesDelegate = new ConcreteDelegate(environment, EValType.STR);

        setSelectionMode(SelectionMode.SingleSelection);
        setSelectionBehavior(SelectionBehavior.SelectItems);
        verticalHeader().hide();
        setColumnCount(3);
        horizontalHeader().setResizeMode(ResizeMode.Stretch);
        final String columnIconImg = environment.getMessageProvider().translate("EditMask", "Icon");
        setHorizontalHeaderItem(0, new QTableWidgetItem(columnIconImg));
        final String columnValuesName = environment.getMessageProvider().translate("EditMask", "Value");
        setHorizontalHeaderItem(1, new QTableWidgetItem(columnValuesName));
        final String columnTitleName = environment.getMessageProvider().translate("EditMask", "Name");
        setHorizontalHeaderItem(2, new QTableWidgetItem(columnTitleName));

        setItemDelegateForColumn(1, valuesDelegate);
        setItemDelegateForColumn(2, titlesDelegate);

    }

    public boolean validate() {
        if (hasNullValues()) {
            final String hasNulls = environment.getMessageProvider().translate("EditMask", "The list contains null-values.");
            environment.messageError(hasNulls);
            return false;
        }
        return !hasSameKeys();
    }

    private boolean hasNullValues() {
        final int rowCount = rowCount();
        final int colCount = columnCount();
        for(int i = 0; i < rowCount; i++) {
            for(int j = 1; j < colCount; j++) {
                QTableWidgetItem item = item(i, j);
                if (item == null) {
                    setCurrentCell(i, j);
                    return true;
                }
                final String data = (String) item.data(Qt.ItemDataRole.DisplayRole);
                if(data == null || data.isEmpty()) {
                    setCurrentCell(i,j);
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasSameKeys() {
        final int rowCount = rowCount();
        List<String> uniqueValues = new LinkedList<>();
        for (int i = 0; i < rowCount; i++) {
            final String value = item(i, 1).text();
            if (uniqueValues.contains(value)) {
                final String hasNulls = environment.getMessageProvider().translate("EditMask", "The list contains same keys.");
                environment.messageError(hasNulls);
                return true;
            } else {
                uniqueValues.add(value);
            }
        }
            for (int i = 0; i <= rowCount-2; i++) {
                for (int j = i + 1; j < rowCount; j++) {
                        if (Utils.equals(item(i, 1).text(), item(j, 1).text())) {
                            final MessageProvider messageProvider = environment.getMessageProvider();
                            final String title = messageProvider.translate("ExplorerMessage", "Duplicate Values Are Not Allowed");
                            final String message = messageProvider.translate("ExplorerMessage", "Duplicate values detected for %s and %s items");
                            environment.messageError(title, String.format(message, i, j));
                            setCurrentCell(j, 1);
                            editItem(currentItem());
                            return false;
                        }
                    }
                }
        return false;
    }

    public void finishEditing() {
        final QModelIndex currentIndex = currentIndex();
        setCurrentItem(null);
        setCurrentIndex(currentIndex);
    }
}
