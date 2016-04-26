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

package org.radixware.kernel.explorer.editors.sqmleditor;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlModifiableParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameter;
import org.radixware.kernel.common.client.meta.sqml.ISqmlParameters;
import org.radixware.kernel.common.client.views.IDialog.DialogResult;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.ToolBarSeparator;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;


final class ParametersToolBar extends ExplorerFrame {

    final private QAction createParameterAction, removeParameterAction, editParameterAction,
            moveUpParameterAction, moveDownParameterAction;
    final private QHBoxLayout layout = WidgetUtils.createHBoxLayout(this);
    // final private QTreeView sqmlTree;
    final private QListWidget paramListWidget;
    final private SqmlEditor sqmlEditor;
    private ISqmlParameters parameters;    

    public ParametersToolBar(final IClientEnvironment environment, final SqmlEditor sqmlEditor, final QListWidget listWidget, final QWidget parent) {
        super(environment, parent);

        createParameterAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CREATE),
            environment.getMessageProvider().translate("SqmlEditor", "Create Parameter"),
            this);
        removeParameterAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DELETE),
            environment.getMessageProvider().translate("SqmlEditor", "Delete Parameter"),
            this);
        editParameterAction =
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.EDIT),
            environment.getMessageProvider().translate("SqmlEditor", "Edit Parameter"),
            this);
        
        moveUpParameterAction = 
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.UP),
            environment.getMessageProvider().translate("SqmlEditor", "Move Parameter Up"),
            this);
        
        moveDownParameterAction = 
            new QAction(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.DOWN),
            environment.getMessageProvider().translate("SqmlEditor", "Move Parameter Down"),
            this);

        paramListWidget = listWidget;
        this.sqmlEditor = sqmlEditor;
        layout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft));
        createToolButton(createParameterAction, "createParameter()", "btnCreateParam");
        createToolButton(removeParameterAction, "removeParameter()", "btnRemoveParam");
        createToolButton(editParameterAction, "editParameter()", "btnEditParam");
        layout.addWidget(new ToolBarSeparator(this));
        createToolButton(moveUpParameterAction, "moveUpParameter()", "btnMoveUpParam");
        createToolButton(moveDownParameterAction, "moveDownParameter()", "btnMoveDownParam");
        setVisible(false);
    }

    private QToolButton createToolButton(final QAction action, final String slotName, final String btnName) {
        final QToolButton button = new QToolButton(this);
        button.setObjectName(btnName);
        button.setDefaultAction(action);
        button.setAutoRaise(true);
        button.setFixedSize(new QSize(30, 30));
        button.setIconSize(new QSize(30, 30));
        action.triggered.connect(this, slotName);
        layout.addWidget(button);
        return button;
    }

    public void setParameters(final ISqmlParameters parameters) {
        this.parameters = parameters;
        setVisible(parameters != null);
    }

    public ISqmlParameter createParameter() {
        if (createParameterAction.isEnabled()) {
            paramListWidget.setUpdatesEnabled(false);
            try {
                final ISqmlModifiableParameter parameter = parameters.createNewParameter(getEnvironment(), this);
                if (parameter != null) {
                    parameters.addParameter(parameter);
                    final ParameterItem item = new ParameterItem(parameter);
                    if ((parameter.getIcon() != null) && (ExplorerIcon.getQIcon(parameter.getIcon()) != null)) {
                        item.setIcon(ExplorerIcon.getQIcon(parameter.getIcon()));
                    }
                    paramListWidget.addItem(item);
                    paramListWidget.setCurrentItem(item);
                    sqmlEditor.onParametersChanged.emit();
                }
                return parameter;
            } finally {
                paramListWidget.setUpdatesEnabled(true);
            }
        }
        return null;
    }
    
    private boolean removeParameter(final ISqmlParameter parameter) {
        return parameters.removeParameter(getParameterIndex(parameter));
    }

    public boolean removeParameter() {
        if (removeParameterAction.isEnabled()) {
            final QListWidgetItem curItem = paramListWidget.currentItem();
            if (curItem != null) {
                paramListWidget.setUpdatesEnabled(false);
                final String title = getEnvironment().getMessageProvider().translate("SqmlEditor", "Confirm to Delete Parameter");
                final String message = getEnvironment().getMessageProvider().translate("SqmlEditor", "Do you really want to delete parameter '%s'?");
                try {
                    final ISqmlParameter curParam = currentParameter();
                    if (curParam != null && getEnvironment().messageConfirmation(title, String.format(message, curParam.getTitle()))
                            && removeParameter(curParam)) {
                        final int curRow = paramListWidget.currentRow();
                        paramListWidget.takeItem(curRow);
                        if (curRow < paramListWidget.count()) {
                            paramListWidget.setCurrentRow(curRow);
                        } else if (paramListWidget.count() > 0) {
                            paramListWidget.setCurrentRow(0);
                        } else {
                            refresh();
                        }
                        sqmlEditor.updateTagsName();
                        sqmlEditor.onParametersChanged.emit();
                        return true;
                    }
                } finally {
                    paramListWidget.setUpdatesEnabled(true);
                }
            }
        }
        return false;
    }

    public void editParameter() {
        if (editParameterAction.isEnabled()) {
            final ISqmlParameter parameter = currentParameter();
            if (parameter != null && parameters != null) {
                if (parameters.openParameterEditor(getEnvironment(), parameter, false, this)!=DialogResult.REJECTED){
                    updateCurrentItemTitle();
                    sqmlEditor.updateTagsName();
                    sqmlEditor.onParametersChanged.emit();
                }
            }
        }
    }
    
    public boolean moveUpParameter(){
        if (moveUpParameterAction.isEnabled()){
            final int row = paramListWidget.currentRow();
            if (swapParameters(row, row-1)){
                paramListWidget.setCurrentRow(row-1);
                sqmlEditor.onParametersChanged.emit();
                return true;
            }
        }
        return false;
    }
        
    public boolean moveDownParameter(){
        if (moveDownParameterAction.isEnabled()){
            final int row = paramListWidget.currentRow();
            if (swapParameters(row, row+1)){
                paramListWidget.setCurrentRow(row+1);
                sqmlEditor.onParametersChanged.emit();
                return true;
            }
        }
        return false;
    }    

    private ISqmlParameter currentParameter() {
        final QListWidgetItem curItem = paramListWidget.currentItem();
        if (curItem == null) {
            return null;
        }
        return ((ParameterItem) curItem).getParameter();
    }
    
    private void updateCurrentItemTitle(){
        final ParameterItem curItem = (ParameterItem)paramListWidget.currentItem();
        if (curItem!=null && curItem.getParameter()!=null){
            curItem.setText(curItem.getParameter().getTitle());
        }
    }
    
    private int getParameterIndex(final ISqmlParameter parameter){
        for (int i = 0, size = parameters.size(); i < size; i++) {
            final ISqmlParameter p = parameters.get(i);
            if (p != null && p.getId().equals(parameter.getId())) {
                return i;
            }
        }
        return -1;
    }
    
    private boolean swapParameters(final int row1, final int row2){
        if (row1==row2){
            return true;
        }if (parameters.swapParameters(row1, row2)){
            final QListWidgetItem item1 = paramListWidget.takeItem(row1),
                                  item2 = paramListWidget.takeItem(row1<row2 ? row2-1 : row2);
            paramListWidget.insertItem(row1<row2 ? row1 : row1-1, item2);
            paramListWidget.insertItem(row2, item1);
            return true;
        }
        return false;
    }

    public void refresh() {
        if (parameters == null || parameters.isReadonly()) {
            createParameterAction.setDisabled(true);
            removeParameterAction.setDisabled(true);
            editParameterAction.setDisabled(true);
            moveUpParameterAction.setDisabled(true);
            moveDownParameterAction.setDisabled(true);
        } else {
            createParameterAction.setEnabled(true);
            final ISqmlParameter parameter = currentParameter();
            final boolean isCurParameterModifable = parameter instanceof ISqmlModifiableParameter;
            removeParameterAction.setEnabled(isCurParameterModifable);
            editParameterAction.setEnabled(parameter != null);
            {
                final int paramIndex = parameter==null ? -1 : getParameterIndex(parameter);
                final boolean canMoveUp = isCurParameterModifable &&
                                          paramIndex>0 &&
                                          parameters.get(paramIndex-1) instanceof ISqmlModifiableParameter;
                moveUpParameterAction.setEnabled(canMoveUp);
                final boolean canMoveDown = isCurParameterModifable &&
                                            paramIndex<parameters.size()-1 &&
                                            parameters.get(paramIndex+1) instanceof ISqmlModifiableParameter;                
                moveDownParameterAction.setEnabled(canMoveDown);
            }
        }
    }

}
