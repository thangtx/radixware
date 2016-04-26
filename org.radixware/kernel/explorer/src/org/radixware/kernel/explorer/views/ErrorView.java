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

package org.radixware.kernel.explorer.views;

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.TextInteractionFlag;
import com.trolltech.qt.core.Qt.ToolButtonStyle;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QInputMethodEvent;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.errors.ObjectNotFoundError;
import org.radixware.kernel.common.client.exceptions.BrokenEntityObjectException;
import org.radixware.kernel.common.client.exceptions.ClientException;
import org.radixware.kernel.common.client.exceptions.ExceptionMessage;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;

public final class ErrorView extends ExplorerWidget {

    final private QLabel lbTitle = new QLabel(this);
    final private QLabel lbMessage = new QLabel(this);
    final private QTextEdit teDetailMessage = new QTextEdit(this);
    final private QToolButton tbSwitchDetails = new QToolButton(this);
    final private QPushButton pbReopen = new QPushButton(this);
    public final Signal0 tryReopen = new Signal0();

    public ErrorView(IClientEnvironment environment, QWidget parent) {
        super(environment, parent);
        setupUi();
    }

    public ErrorView(IClientEnvironment environment) {
        super(environment);
        setupUi();
    }

    private void setupUi() {

        final QVBoxLayout vLayout = new QVBoxLayout();
        setLayout(vLayout);
        vLayout.setAlignment(new Alignment(AlignmentFlag.AlignTop));
        {
            final QWidget titleWidget = new QWidget(this);
            titleWidget.setObjectName("errorview_title_widget");
            final QHBoxLayout hLayout = new QHBoxLayout();
            hLayout.setMargin(0);
            titleWidget.setLayout(hLayout);

            final QIcon icon = ExplorerIcon.getQIcon(ClientIcon.Message.ERROR);
            final QLabel iconLabel = new QLabel(this);
            iconLabel.setObjectName("exceptionbox_icon");
            iconLabel.setFixedSize(32, 32);
            iconLabel.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
            iconLabel.setPixmap(icon.pixmap(32));
            hLayout.addWidget(iconLabel);
            hLayout.addWidget(lbTitle);
            final QFont titleFont = lbTitle.font();
            titleFont.setBold(true);
            lbTitle.setObjectName("lbTitle");
            lbTitle.setFont(titleFont);
            lbTitle.setTextInteractionFlags(TextInteractionFlag.TextBrowserInteraction);
            lbTitle.setTextInteractionFlags(TextInteractionFlag.TextEditorInteraction);
            lbTitle.setTextInteractionFlags(TextInteractionFlag.TextSelectableByKeyboard);
            lbTitle.setTextInteractionFlags(TextInteractionFlag.TextSelectableByMouse);
            vLayout.addWidget(titleWidget, 0, new Alignment(AlignmentFlag.AlignHCenter, AlignmentFlag.AlignTop));
        }
        {
            lbMessage.setObjectName("lbMessage");
            lbMessage.setAlignment(AlignmentFlag.AlignHCenter);
            lbMessage.setTextInteractionFlags(TextInteractionFlag.TextBrowserInteraction);
            lbMessage.setTextInteractionFlags(TextInteractionFlag.TextEditorInteraction);
            lbMessage.setTextInteractionFlags(TextInteractionFlag.TextSelectableByKeyboard);
            lbMessage.setTextInteractionFlags(TextInteractionFlag.TextSelectableByMouse);
            vLayout.addWidget(lbMessage, 0, new Alignment(AlignmentFlag.AlignHCenter, AlignmentFlag.AlignTop));
        }
        {            
            tbSwitchDetails.setObjectName("exceptionbox_detail_button");
            ExplorerFont detailBtnFont = ExplorerFont.Factory.getFont(tbSwitchDetails.font()).getItalic();
            detailBtnFont = detailBtnFont.changeSize(detailBtnFont.getSize()-1);
            tbSwitchDetails.setFont(detailBtnFont.getQFont());
            tbSwitchDetails.setToolButtonStyle(ToolButtonStyle.ToolButtonTextBesideIcon);
            tbSwitchDetails.setAutoRaise(true);
            tbSwitchDetails.setText(getEnvironment().getMessageProvider().translate("ErrorView", "show details"));
            tbSwitchDetails.clicked.connect(this, "switchDetails()");
            vLayout.addWidget(tbSwitchDetails, 0, new AlignmentFlag[]{AlignmentFlag.AlignRight});
        }
        {
            final QPalette palette = new QPalette(teDetailMessage.palette());
            palette.setColor(ColorRole.Base, palette.color(ColorRole.Window));
            
            teDetailMessage.setPalette(palette);
            teDetailMessage.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
            teDetailMessage.setObjectName("exception_detailed");
            teDetailMessage.setReadOnly(true);
            teDetailMessage.setWordWrapMode(WrapMode.NoWrap);

            vLayout.addWidget(teDetailMessage);

            teDetailMessage.setVisible(false);
        }
        {
            pbReopen.setText(getEnvironment().getMessageProvider().translate("ErrorView", "Try to &Reopen"));
            pbReopen.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.REFRESH));
            pbReopen.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
            pbReopen.setVisible(false);
            pbReopen.clicked.connect(tryReopen);
            vLayout.addWidget(pbReopen, 0, AlignmentFlag.AlignHCenter);
        }
    }

    @SuppressWarnings("unused")
    private void switchDetails() {
        final QListWidget listWidget;
        teDetailMessage.setVisible(!teDetailMessage.isVisible());
        if (teDetailMessage.isVisible()) {
            tbSwitchDetails.setText(getEnvironment().getMessageProvider().translate("ErrorView", "hide details"));
        } else {
            tbSwitchDetails.setText(getEnvironment().getMessageProvider().translate("ErrorView", "show details"));
        }
    }

    public void setTitle(final String errorTitle) {
        lbTitle.setText(errorTitle);
        tbSwitchDetails.setVisible(false);
    }

    public void setMessage(final String errorMessage) {
        lbMessage.setText(errorMessage);
        lbMessage.setVisible(true);
        tbSwitchDetails.setVisible(false);
    }
    
    public String getTitle(){
        return lbTitle.text();
    }
    
    public String getMessage(){
        return lbMessage.text();
    }

    public void setError(final String title, final Throwable error) {
        if (error instanceof ObjectNotFoundError) {
            final ObjectNotFoundError objectNotFound = (ObjectNotFoundError) error;
            if (objectNotFound.inKnownContext()) {
                lbTitle.setText(objectNotFound.getMessageToShow());
                lbMessage.setText("");
                lbMessage.setVisible(false);
            } else {
                lbTitle.setText(title);
                lbMessage.setText(objectNotFound.getMessageToShow());
                lbMessage.setVisible(true);
            }
            teDetailMessage.setText(ClientException.exceptionStackToString(error));
            tbSwitchDetails.setVisible(!objectNotFound.inKnownContext());
        } else if (error instanceof BrokenEntityObjectException){
            final MessageProvider mp = getEnvironment().getMessageProvider();
            final BrokenEntityObjectException brokenEntityException = (BrokenEntityObjectException)error;            
            lbTitle.setVisible(true);
            lbTitle.setText(title);
            lbMessage.setVisible(true);
            lbMessage.setText(brokenEntityException.getLocalizedMessage(mp));
            final int maxWidth = QApplication.desktop().width() * 3 / 4;
            lbMessage.setMaximumWidth(maxWidth);
            lbMessage.setWordWrap(lbMessage.sizeHint().width() > maxWidth);            
            teDetailMessage.setVisible(true);            
            String detailMessageText = brokenEntityException.getCauseExceptionMessage();            
            if (detailMessageText !=null && !detailMessageText.isEmpty()){
                detailMessageText += "\n";
            }
            detailMessageText+=brokenEntityException.getCauseExceptionStack();
            teDetailMessage.setText(detailMessageText);
            tbSwitchDetails.setVisible(false);
            return;
        } else {
            lbTitle.setText(title);
            QInputMethodEvent event;
            final ExceptionMessage message = new ExceptionMessage(getEnvironment(), error);
            if (message.hasDialogMessage()){
                lbMessage.setVisible(true);
                lbMessage.setText(message.getDialogMessage());
            }else{
                lbMessage.setVisible(false);
            }
            String details = message.getDetails();
            if (details!=null && !details.isEmpty()){
                details+="\n"+message.getTrace();
            }
            if (details!=null && !details.isEmpty()){
                teDetailMessage.setText(details);
                tbSwitchDetails.setVisible(true);
            }else{
                tbSwitchDetails.setVisible(false);
            }
        }
        final int maxWidth = QApplication.desktop().width() * 3 / 4;
        lbMessage.setMaximumWidth(maxWidth);
        lbMessage.setWordWrap(lbMessage.sizeHint().width() > maxWidth);
        if (ClientException.isSystemFault(error)) {
            getEnvironment().processException(error);
        }
    }

    public void setCanReopen(final boolean canReopen) {
        pbReopen.setVisible(canReopen);
    }

    public void clear() {
        lbTitle.clear();
        lbMessage.clear();
        teDetailMessage.clear();
    }
}
