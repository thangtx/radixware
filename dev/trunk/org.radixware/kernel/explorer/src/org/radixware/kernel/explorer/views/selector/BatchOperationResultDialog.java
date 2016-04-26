/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.views.selector;

import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QTextOption;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.AbstractBatchOperationResult;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.widgets.TextView;

final class BatchOperationResultDialog extends ExplorerDialog{    
    
    private final static class ResultListModel extends QAbstractListModel{
        
        private final AbstractBatchOperationResult operationResult;
        private final List<String> rejections;
        
        @SuppressWarnings("unchecked")
        public ResultListModel(final AbstractBatchOperationResult result, final QObject parent){
            super(parent);
            rejections = new ArrayList<>(result.getRejectedObjectPids());
            for (int i=rejections.size()-1; i>=0; i--){
                final String message = result.getRejectionMessage(rejections.get(i));
                if (message==null || message.isEmpty()){
                    rejections.remove(i);
                }                
            }
            operationResult = result;
        }        

        @Override
        public Object data(final QModelIndex index, final int role) {
            if (index == null) {
                return null;
            }            
            final int row = index.row();
            if (row<0 || row>=operationResult.getNumberOfRejections()){
                return null;
            }
            switch (role) {
                case Qt.ItemDataRole.DisplayRole:
                    return operationResult.getRejectionMessage(rejections.get(row));
                case Qt.ItemDataRole.UserRole:
                    return rejections.get(row);
                default:
                    return null;
            }                       
        }

        @Override
        public int rowCount(final QModelIndex parent) {
            return rejections.size();
        }
                
        
        public AbstractBatchOperationResult.ExceptionInfo getExceptionInfo(final QModelIndex index){
            if (index == null) {
                return null;
            }            
            final int row = index.row();
            if (row<0 || row>=operationResult.getNumberOfRejections()){
                return null;
            }
            return operationResult.getExceptionInfo(rejections.get(row));
        }
        
        public boolean wasException(final QModelIndex index){
            if (index == null) {
                return false;
            }            
            final int row = index.row();
            if (row<0 || row>=operationResult.getNumberOfRejections()){
                return false;
            }
            return operationResult.getExceptionInfo(rejections.get(row))!=null;
        }                
    }
    
    private static class ExceptionInfoDialog extends ExplorerDialog{
        
        public ExceptionInfoDialog(final IClientEnvironment environment, final AbstractBatchOperationResult.ExceptionInfo exception, final QWidget parent){
            super(environment,parent,false);
            this.setObjectName(getClass().getName());
            setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
            setupUi(exception);
        }
        
        private void setupUi(final AbstractBatchOperationResult.ExceptionInfo exception){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            setWindowTitle(mp.translate("Selector", "Error Information"));
            final Icon icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.ERROR);
            setWindowIcon(icon); 
            final TextView textView = new TextView(this){
                @Override
                public QSize sizeHint() {
                    final QSize textSize = document().documentLayout().documentSize().toSize();
                    final QSize screenSize = QApplication.desktop().availableGeometry(QCursor.pos()).size();
                    final int height = Math.min(textSize.height(), screenSize.height() * 3 / 4);
                    final int width = Math.min(textSize.width(), screenSize.width() * 3 / 4);
                    return new QSize(width, height);
                }
            };
            textView.setMinimumSize(new QSize(400, 200));
            textView.setObjectName(getClass().getName()+".textView");
            textView.setWordWrapMode(QTextOption.WrapMode.NoWrap);
            textView.setPlainText(exception.getExceptionStackTrace());
            layout().addWidget(textView);
            addButtons(EnumSet.of(EDialogButtonType.CLOSE), true);
        }        
    }
    
    private final QLabel lbMessage = new QLabel(this);

    public BatchOperationResultDialog(final IClientEnvironment environment, final AbstractBatchOperationResult result, final QWidget parent){
        super(environment,parent,false);
        this.setObjectName(getClass().getName());
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        setupUi(result);
    }
    
    private void setupUi(final AbstractBatchOperationResult result){
        final MessageProvider mp = getEnvironment().getMessageProvider();
        setWindowTitle(mp.translate("Selector", "Warning"));
        final Icon icon = getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.TraceLevel.WARNING);
        setWindowIcon(icon);        
        lbMessage.setText(mp.translate("Selector", "Following objects was not processed:"));
        final QFont font = new QFont(lbMessage.font());
        font.setPointSize(12);
        font.setBold(true);
        lbMessage.setFont(font);
        lbMessage.setAlignment(Qt.AlignmentFlag.AlignHCenter);
        lbMessage.setObjectName(getClass().getName()+".label");
        layout().addWidget(lbMessage);
        final QListView listView = new QListView(this);     
        listView.setObjectName(getClass().getName()+".listView");
        listView.setMinimumSize(new QSize(400, 200));
        final ResultListModel listModel = new ResultListModel(result, this);
        listModel.setObjectName(ResultListModel.class.getName());
        listView.setModel(listModel);  
        listView.doubleClicked.connect(this,"onItemDoubleClick(QModelIndex)");
        layout().addWidget(listView);
        addButtons(EnumSet.of(EDialogButtonType.CLOSE), true);
    }
    
    public void setMessage(final String message){
        lbMessage.setText(message);
    }
    
    @SuppressWarnings("unused")
    private void onItemDoubleClick(final QModelIndex index){
        if (index!=null){
            final ResultListModel model = (ResultListModel)index.model();
            if (model.wasException(index)){
                final AbstractBatchOperationResult.ExceptionInfo exceptionInfo = model.getExceptionInfo(index);
                if (exceptionInfo!=null){
                    new ExceptionInfoDialog(getEnvironment(), exceptionInfo, this).exec();
                }
            }
        }
    }
    
}