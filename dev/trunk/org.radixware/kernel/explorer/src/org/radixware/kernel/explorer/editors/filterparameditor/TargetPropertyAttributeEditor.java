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

package org.radixware.kernel.explorer.editors.filterparameditor;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAbstractItemView.ScrollHint;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTreeView;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.enums.ETextOptionsMarker;
import org.radixware.kernel.common.client.meta.sqml.ISqmlColumnDef;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlTableDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.dialogs.SqmlTreeModelProxy;
import org.radixware.kernel.explorer.editors.valeditors.SqmlDefComboBox;

import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.models.SqmlTreeModel;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;


final class TargetPropertyAttributeEditor extends AbstractAttributeEditor<ISqmlColumnDef> {

    private final QGroupBox gbPropertiesList;
    private final SqmlTreeModelProxy treeModel;
    private final QTreeView tree;
    private final SqmlDefComboBox cbTargetProperty;
    private final QLabel lbTargetProperty;
    public final com.trolltech.qt.QSignalEmitter.Signal0 onDoubleClick =
            new com.trolltech.qt.QSignalEmitter.Signal0();

    public TargetPropertyAttributeEditor(final IClientEnvironment environment, final ISqmlTableDef context, final boolean isReadOnly, final QWidget parent) {
        super(environment);
        setObjectName("attrEditor_" + getAttribute().name());
        final EnumSet<SqmlTreeModel.ItemType> itemTypes = EnumSet.of(SqmlTreeModel.ItemType.PROPERTY);
        if (isReadOnly) {
            lbTargetProperty = new QLabel(Application.translate("SqmlEditor", "Base &property:"), parent);
            lbTargetProperty.setObjectName("lbTargetProperty");
            lbTargetProperty.setSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Fixed);
            cbTargetProperty = new SqmlDefComboBox(environment, parent, context==null ? Collections.<ISqmlDefinition>emptyList() : context.getColumns(), true, true, EDefinitionDisplayMode.SHOW_TITLES);
            cbTargetProperty.setObjectName("cbTargetProperty");
            lbTargetProperty.setBuddy(cbTargetProperty);

            if (isReadOnly) {
                ExplorerTextOptions.Factory.getOptions(ETextOptionsMarker.READONLY_VALUE).applyTo(cbTargetProperty);
                ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.READONLY_VALUE).applyTo(lbTargetProperty);
            } else {
                ExplorerTextOptions.Factory.getDefault().applyTo(cbTargetProperty);
                ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(lbTargetProperty);
            }

            treeModel = null;
            tree = null;
            gbPropertiesList = null;
        } else {
            final List<ISqmlDefinition> props = new ArrayList<>();
            for (int count=context.getColumns().size(),i=0; i<count; i++){
                if (context.getColumns().get(i).getType()!=EValType.OBJECT){
                    props.add(context.getColumns().get(i));
                }
            }
            final SqmlTreeModel sourceTreeModel = new SqmlTreeModel(environment, props, itemTypes);
            treeModel = new SqmlTreeModelProxy(sourceTreeModel, null);
            treeModel.setFilterCaseSensitivity(Qt.CaseSensitivity.CaseInsensitive);
            treeModel.setFilterKeyColumn(0);
            treeModel.setFilterRole(SqmlTreeModel.FILTER_ROLE);
            treeModel.sort(0, Qt.SortOrder.AscendingOrder);
            final String gbTitle = Application.translate("SqmlEditor", "Columns and properties of %s:");
            gbPropertiesList = new QGroupBox(String.format(gbTitle, context.getTitle()), parent);
            gbPropertiesList.setObjectName("gbPropertiesList");
            
            final QHBoxLayout ltFilterText = new QHBoxLayout();
            final QLabel lbFind = 
                new QLabel(getEnvironment().getMessageProvider().translate("SqmlEditor", "&Find") + ":",gbPropertiesList);
            lbFind.setObjectName("lbFind");
            ExplorerTextOptions.Factory.getLabelOptions(ETextOptionsMarker.REGULAR_VALUE).applyTo(lbFind);
            lbFind.setEnabled(!isReadOnly);
            final QLineEdit leFind = new QLineEdit(gbPropertiesList);
            leFind.setObjectName("leFind");
            leFind.textChanged.connect(this, "onFindTextChange(String)");
            ExplorerTextOptions.Factory.getDefault().applyTo(leFind);
            leFind.setEnabled(!isReadOnly);
            lbFind.setBuddy(leFind);
            ltFilterText.addWidget(lbFind);
            ltFilterText.addWidget(leFind);
            
            final QVBoxLayout layout = new QVBoxLayout(gbPropertiesList);
            layout.addLayout(ltFilterText);
            tree = new QTreeView(gbPropertiesList) {

                @Override
                protected void currentChanged(QModelIndex current, QModelIndex previous) {
                    onValueChanged();
                }

                @Override
                public QSize sizeHint() {
                    final QSize size = super.sizeHint();
                    return new QSize(size.width(), Math.max(size.height(), 150));
                }

                @Override
                public QSize minimumSizeHint() {
                    final QSize size = super.sizeHint();
                    return new QSize(size.width(), Math.max(size.height(), 150));
                }

                @Override
                protected void keyPressEvent(final QKeyEvent event) {
                    try{
                        final String text = event.text();
                        if ((text!=null && !text.isEmpty()) || event.key()==Qt.Key.Key_Backspace.value()){
                            leFind.event(event);
                        }else{
                            super.keyPressEvent(event);
                        }
                    }catch(Exception exception){
                        getEnvironment().getTracer().put(exception);
                    }
                }                          
            };
            tree.setItemDelegate(new QItemDelegate(tree));
            tree.setObjectName("propertiesTree");
            layout.addWidget(tree);
            tree.setHeaderHidden(true);
            tree.setIndentation(0);
            tree.setModel(treeModel);
            if (treeModel.rowCount() > 0) {
                tree.setCurrentIndex(treeModel.index(0, 0));
            }
            tree.doubleClicked.connect(onDoubleClick);
            tree.setFocus();
            lbTargetProperty = null;
            cbTargetProperty = null;
        }
    }

    @SuppressWarnings("unused")
    private void onValueChanged() {
        attributeChanged.emit(this);
    }
    
    @SuppressWarnings("unused")
    private void onFindTextChange(final String text){
        treeModel.setFilterCaseSensitivity(Qt.CaseSensitivity.CaseInsensitive);
        treeModel.invalidate();
        treeModel.setFilterWildcard(text);
        if (treeModel.index(0, 0) != null) {
            tree.setCurrentIndex(treeModel.index(0, 0));
        }        
    }

    @Override
    public boolean updateParameter(final ISqmlParameter parameter) {
        if (parameter instanceof ISqmlModifiableParameter) {
            if (tree != null && getAttributeValue() == null) {
                Application.messageInformation(Application.translate("SqmlEditor", "Base Property Was Not Specified!"),
                        Application.translate("SqmlEditor", "Please select base property"));
                tree.setFocus();
                return false;
            } else {
                ((ISqmlModifiableParameter) parameter).setBaseProperty(getAttributeValue());
                return true;
            }
        }
        return true;
    }

    @Override
    public void updateEditor(final ISqmlParameter parameter) {
        if (tree != null) {
            final Id propertyId = parameter.getBasePropertyId();
            QModelIndex index;
            for (int i = treeModel.rowCount() - 1; i >= 0; i--) {
                index = treeModel.index(i, 0, null);
                if (treeModel.definition(index).getId().equals(propertyId)) {
                    tree.setCurrentIndex(index);
                    tree.scrollTo(index, ScrollHint.PositionAtCenter);
                    break;
                }
            }
        } else {
            cbTargetProperty.setCurrentDefinitionId(parameter.getBasePropertyId());
        }

    }

    @Override
    public EFilterParamAttribute getAttribute() {
        return EFilterParamAttribute.PROPERTY;
    }

    @Override
    public ISqmlColumnDef getAttributeValue() {
        if (tree != null && tree.currentIndex() != null) {
            return (ISqmlColumnDef) treeModel.definition(tree.currentIndex());
        } else {
            return (ISqmlColumnDef) cbTargetProperty.getValue();
        }
    }

    @Override
    public EnumSet<EFilterParamAttribute> getBaseAttributes() {
        return EnumSet.noneOf(EFilterParamAttribute.class);
    }

    @Override
    public void onBaseAttributeChanged(AbstractAttributeEditor linkedEditor) {
    }

    @Override
    public QLabel getLabel() {
        return tree == null ? lbTargetProperty : null;
    }

    @Override
    public QWidget getEditorWidget() {
        return tree == null ? cbTargetProperty : gbPropertiesList;
    }

    @Override
    public void free() {
    }
}