/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QTreeWidget;
import com.trolltech.qt.gui.QTreeWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.inspector.delegates.InspectorDelegate;
import org.radixware.kernel.explorer.inspector.propertyEditors.IPropertyEditor;

class PropertyBrowser<T> extends QWidget {

    private final QTreeWidget propTreeWidget;
    private boolean isGroupByClassFlag = true;
    private final QLineEdit filter;
    private WidgetInfo<T> currentWdgetInfo;
    private final IClientEnvironment environment;

    PropertyBrowser(QWidget parent, WidgetInfo<T> currentWdgtInfo, IClientEnvironment environment) {
        super(parent);
        this.environment = environment;
        this.currentWdgetInfo = currentWdgtInfo;
        QLayout topLayout = new QVBoxLayout(this);
        this.setLayout(topLayout);
        propTreeWidget = new QTreeWidget(parent);
        propTreeWidget.setColumnCount(2);
        propTreeWidget.setHeaderLabels(java.util.Arrays.asList(environment.getMessageProvider().translate("inspector", "Property"), environment.getMessageProvider().translate("inspector", "Value")));
        propTreeWidget.header().setDefaultAlignment(Qt.AlignmentFlag.AlignCenter);

        QCheckBox isGroupByClass = new QCheckBox(environment.getMessageProvider().translate("inspector", "Group by class"), this);
        isGroupByClass.setChecked(true);
        QFrame buttonFrame = new QFrame(this);
        QLabel filterLabel = new QLabel(environment.getMessageProvider().translate("inspector", "Filter:"), buttonFrame);
        filterLabel.setAlignment(Qt.AlignmentFlag.AlignLeft);
        filter = new QLineEdit(buttonFrame);
        filter.textEdited.connect(this, "filterSlot(String)");

        QLayout buttonLayout = new QHBoxLayout(buttonFrame);
        buttonLayout.setMargin(0);
        buttonFrame.setLayout(buttonLayout);
        topLayout.addWidget(buttonFrame);

        buttonLayout.addWidget(filterLabel);
        buttonLayout.addWidget(filter);
        topLayout.addWidget(isGroupByClass);

        topLayout.addWidget(propTreeWidget);
        isGroupByClass.clicked.connect(this, "sortCheckBoxClickedSlot(boolean)");
        propTreeWidget.itemExpanded.connect(this, "complexItemExpandedSlot(QTreeWidgetItem)");
    }

    public void build(WidgetInfo<T> currentWdgtInfo, IClientEnvironment environment) {
        propTreeWidget.clear();
        List<WidgetProperty> wdgPropList;
        if (currentWdgtInfo != null) {
            this.currentWdgetInfo = currentWdgtInfo;
            wdgPropList = currentWdgtInfo.getWidgetProperties();
        } else {
            wdgPropList = this.currentWdgetInfo.getWidgetProperties();
        }

        if (!wdgPropList.isEmpty()) {
            if (!isGroupByClassFlag) {
                for (WidgetProperty wdgProp : getSortedWidgetProperties(wdgPropList)) {
                    QTreeWidgetItem qTreeWdgtItem = createQTreeWidgetItem(wdgProp, null);
                    if (qTreeWdgtItem != null) {
                        propTreeWidget.addTopLevelItem(qTreeWdgtItem);
                        propTreeWidget.setItemWidget(qTreeWdgtItem, 1, getWidget(wdgProp));
                        InspectorDelegate inspectorDelegate = wdgProp.getDelegate();

                        if (inspectorDelegate != null && wdgProp.getDelegate().hasChildren()) {
                            qTreeWdgtItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
                        }
                    }
                }
            } else {
                List<WidgetProperty> tempList = new LinkedList<>();
                String currentClassName = wdgPropList.get(0).getClassName();
                QTreeWidgetItem qClassNameTreeWidgetItem = createQTreeWidgetItemForClass(currentClassName);
                for (WidgetProperty nextWdgProp : wdgPropList) {
                    if (!nextWdgProp.getClassName().equals(currentClassName)) {
                        tempList = getSortedWidgetProperties(tempList);
                        for (WidgetProperty wdgProp : tempList) {
                            QTreeWidgetItem qTreeWidgetItem = createQTreeWidgetItem(wdgProp, qClassNameTreeWidgetItem);
                            if (qTreeWidgetItem != null) {
                                qClassNameTreeWidgetItem.addChild(qTreeWidgetItem);
                                propTreeWidget.setItemWidget(qTreeWidgetItem, 1, getWidget(wdgProp));
                                InspectorDelegate inspectorDelegate = wdgProp.getDelegate();
                                if (inspectorDelegate != null && wdgProp.getDelegate().hasChildren()) {
                                    qTreeWidgetItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
                                }
                            }
                        }
                        tempList.clear();
                        currentClassName = nextWdgProp.getClassName();
                        qClassNameTreeWidgetItem = createQTreeWidgetItemForClass(currentClassName);
                    }
                    tempList.add(nextWdgProp);
                }

                if (!tempList.isEmpty()) {
                    QTreeWidgetItem qTreeWidgetItem = createQTreeWidgetItem(tempList.get(0), qClassNameTreeWidgetItem);
                    if (qTreeWidgetItem != null) {
                        qClassNameTreeWidgetItem.addChild(qTreeWidgetItem);
                        propTreeWidget.setItemWidget(qTreeWidgetItem, 1, getWidget(tempList.get(0)));
                    }
                }
            }
        }
        propTreeWidget.resizeColumnToContents(0);
        filterSlot(filter.text());
    }

    private QTreeWidgetItem createQTreeWidgetItem(final WidgetProperty widgetProp, QTreeWidgetItem parent) {
        if (widgetProp.getDelegate() != null) {
            QTreeWidgetItem qTreeWdgtItem = (parent == null) ? new QTreeWidgetItem() : new QTreeWidgetItem(parent);
            qTreeWdgtItem.setText(0, widgetProp.getName());
            qTreeWdgtItem.setData(2, Qt.ItemDataRole.UserRole, widgetProp);
            return qTreeWdgtItem;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unused")
    private void sortCheckBoxClickedSlot(boolean clicked) {
        isGroupByClassFlag = clicked;
        build(null, environment);
        filterSlot(filter.text());
    }

    @SuppressWarnings("unused")
    private void filterSlot(String searchText) {
        if (!isGroupByClassFlag) {
            for (int i = 0; i < propTreeWidget.topLevelItemCount(); i++) {
                if (propTreeWidget.topLevelItem(i).text(0).toLowerCase().contains(searchText.toLowerCase())) {
                    propTreeWidget.topLevelItem(i).setHidden(false);
                } else {
                    propTreeWidget.topLevelItem(i).setHidden(true);
                }
            }
        } else {
            for (int i = 0; i < propTreeWidget.topLevelItemCount(); i++) {
                boolean hideParent = true;
                for (int j = 0; j < propTreeWidget.topLevelItem(i).childCount(); j++) {
                    if (propTreeWidget.topLevelItem(i).child(j).text(0).toLowerCase().contains(searchText.toLowerCase())) {
                        hideParent = false;
                        propTreeWidget.topLevelItem(i).child(j).setHidden(false);
                    } else {
                        propTreeWidget.topLevelItem(i).child(j).setHidden(true);
                    }
                }
                propTreeWidget.topLevelItem(i).setHidden(hideParent);
            }
        }
    }

    private QWidget getWidget(final WidgetProperty widgetProp) {
        if (widgetProp.getDelegate() != null) {
            final IPropertyEditor propertyEditor = widgetProp.getDelegate().createEditorForClass(widgetProp.getType(), propTreeWidget, environment);
            if (propertyEditor != null) {
                propertyEditor.setPropertyValue(widgetProp);

                IPropertyEditor.ValueListener valueListener = new IPropertyEditor.ValueListener() {
                    @Override
                    public void onChangeValue(Object newValue) {
                        currentWdgetInfo.getWidgetInspector().setWidgetProperty(currentWdgetInfo.getWidget(), widgetProp.getSetterMethod(), newValue);
                    }
                };
                propertyEditor.addValueListener(valueListener);
                if (widgetProp.isReadOnly()) {
                    propertyEditor.setReadOnly(true);
                }
                return propertyEditor.asQWidget();
            }
        }
        return null;
    }

    private QWidget getChildWidget(final WidgetProperty childWidgetProp, final WidgetProperty parentWidgetProp) {
        if (childWidgetProp.getDelegate() != null) {
            IPropertyEditor propertyEditor = childWidgetProp.getDelegate().createEditorForClass(childWidgetProp.getType(), propTreeWidget, environment);
            if (propertyEditor != null) {
                propertyEditor.setPropertyValue(childWidgetProp);
                if (parentWidgetProp.isReadOnly()) {
                    propertyEditor.setReadOnly(true);
                }
                return propertyEditor.asQWidget();
            }
        }
        return null;
    }

    public void complexItemExpandedSlot(QTreeWidgetItem expandedItem) {
        if (propTreeWidget.itemWidget(expandedItem, 1) != null && expandedItem.childCount() == 0) {
            WidgetProperty expandedItemWidgetProperty = (WidgetProperty) expandedItem.data(2, Qt.ItemDataRole.UserRole);
            for (WidgetProperty childWidgetProp : expandedItemWidgetProperty.getDelegate().getChildProperties(expandedItemWidgetProperty)) {
                QTreeWidgetItem propChildWidgetItem = createQTreeWidgetItem(childWidgetProp, expandedItem);
                QWidget childPropertyEditor = getChildWidget(childWidgetProp, expandedItemWidgetProperty);
                propTreeWidget.setItemWidget(propChildWidgetItem, 1, childPropertyEditor);
                expandedItem.addChild(propChildWidgetItem);
                ((IPropertyEditor) propTreeWidget.itemWidget(expandedItem, 1)).registerChildPropEditor(expandedItemWidgetProperty, childWidgetProp, (IPropertyEditor) childPropertyEditor);
                if (childWidgetProp.getDelegate() != null && childWidgetProp.getDelegate().hasChildren()) {
                    propChildWidgetItem.setChildIndicatorPolicy(QTreeWidgetItem.ChildIndicatorPolicy.ShowIndicator);
                }
            }
        }
    }

    private static List<WidgetProperty> getSortedWidgetProperties(List<WidgetProperty> wdgtPropList) {
        Collections.sort(wdgtPropList, new Comparator<WidgetProperty>() {
            @Override
            public int compare(WidgetProperty o1, WidgetProperty o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return wdgtPropList;
    }

    private QTreeWidgetItem createQTreeWidgetItemForClass(String className) {
        QTreeWidgetItem qClassNameTreeWidgetItem = new QTreeWidgetItem();
        qClassNameTreeWidgetItem.setText(0, className);
        propTreeWidget.addTopLevelItem(qClassNameTreeWidgetItem);
        propTreeWidget.setFirstItemColumnSpanned(qClassNameTreeWidgetItem, true);
        qClassNameTreeWidgetItem.setExpanded(true);
        return qClassNameTreeWidgetItem;
    }
}
