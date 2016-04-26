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

package org.radixware.kernel.explorer.widgets.selector;

import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.models.BrokenEntityModel;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.ExplorerIcon;


class BrokenEntityMessageDialog extends ExplorerDialog{
    
    final private QLabel lbMessage = new QLabel(this);
    final private QTextEdit teDetailMessage = new QTextEdit(this){
        @Override
        public QSize sizeHint() {
            final QSize textSize = document().documentLayout().documentSize().toSize();
            final QSize screenSize = QApplication.desktop().availableGeometry(QCursor.pos()).size();
            final int height = Math.min(textSize.height(), screenSize.height()*3/4);
            final int width = Math.min(textSize.width(), screenSize.width()*3/4);
            return new QSize(width, height);
        }            
    };
    
    public BrokenEntityMessageDialog(final IClientEnvironment environment, final BrokenEntityModel entityModel, final QWidget parent){
        super(environment, parent, false);
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Message.ERROR));        
        final MessageProvider messageProvider = environment.getMessageProvider();
        layout().addWidget(lbMessage);
        if (entityModel.getPid()==null){
            lbMessage.setText(messageProvider.translate("ExplorerException", "Error reading entity object"));
        }
        else{
            final String messageText = messageProvider.translate("ExplorerException", "Error reading entity object \'%s\'");
            lbMessage.setText(String.format(messageText, entityModel.getPid().toString()));
        }
        setWindowTitle(messageProvider.translate("ExplorerException", "Broken Entity Object"));
        layout().addWidget(teDetailMessage);
        final StringBuilder detailMessageTextBuilder = new StringBuilder();
        final String exceptionMessage = entityModel.getExceptionMessage();
        if (exceptionMessage!=null && !exceptionMessage.isEmpty()){
            detailMessageTextBuilder.append(exceptionMessage);
            detailMessageTextBuilder.append("\n");
        }
        final String stack = entityModel.getExceptionStack();
        if (stack!=null && !stack.isEmpty()){
            detailMessageTextBuilder.append(entityModel.getExceptionStack());
        }
        final QPalette palette = new QPalette(teDetailMessage.palette());
        palette.setColor(ColorRole.Base, palette.color(ColorRole.Window));
        teDetailMessage.setPalette(palette);
        teDetailMessage.setObjectName("exception_detailed");
        teDetailMessage.setReadOnly(true);
        teDetailMessage.setMinimumSize(new QSize(400, 200));
        teDetailMessage.setWordWrapMode(WrapMode.NoWrap);
        
        teDetailMessage.setText(detailMessageTextBuilder.toString());
        teDetailMessage.document().adjustSize();
        teDetailMessage.adjustSize();
        adjustSize();
                
        addButtons(EnumSet.of(EDialogButtonType.CLOSE),true);
    }
}
