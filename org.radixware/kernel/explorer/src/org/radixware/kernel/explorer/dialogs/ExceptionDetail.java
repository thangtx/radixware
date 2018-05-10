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

package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.trace.ClientTraceParser;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.utils.WidgetUtils;


class ExceptionDetail extends QDialog {
    private final ClientTraceParser traceParser;
    
    private final QTextEdit detailed = new QTextEdit(this) {
        @Override
        public QSize sizeHint() {
            final QSize textSize = document().documentLayout().documentSize().toSize();
            return WidgetUtils.shrinkWindowSize(textSize.width(), textSize.height());
        }
    };

    @SuppressWarnings("LeakingThisInConstructor")
    public ExceptionDetail(final IClientEnvironment environment, final QWidget parent) {
        super(parent);
        if (environment.getApplication().isReleaseRepositoryAccessible()){
            traceParser = new ClientTraceParser(environment);
        }else{
            traceParser = null;
        }
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setLayout(new QVBoxLayout());
        final QPushButton translateButton = new QPushButton(this);
        layout().addWidget(translateButton);
        translateButton.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Minimum);
        translateButton.setCheckable(true);
        translateButton.setMinimumSize(110, 26);
        translateButton.setText(Application.translate("TraceDialog", "&Translate"));
        translateButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.TRANSLATE));
        translateButton.toggled.connect(this, "translate()");        
        translateButton.setVisible(traceParser!=null);
        final QPalette palette = new QPalette(detailed.palette());
        palette.setColor(QPalette.ColorRole.Base, palette.color(QPalette.ColorRole.Window));
        setWindowTitle(environment.getMessageProvider().translate("ExceptionDialog", "Exceptions Details"));
        detailed.setPalette(palette);
        detailed.setObjectName("exception_detailed");
        detailed.setReadOnly(true);
        detailed.setMinimumSize(new QSize(400, 200));
        detailed.setWordWrapMode(QTextOption.WrapMode.NoWrap);
        layout().addWidget(detailed);
        final QDialogButtonBox buttonBox = new QDialogButtonBox(this);
        buttonBox.addButton(QDialogButtonBox.StandardButton.Ok);
        buttonBox.accepted.connect(this, "accept()");
        layout().addWidget(buttonBox);
        setModal(true);        
    }

    private String msg = null;
    public void setMessage(final String msg) {
        this.msg = msg;
        assignMessage(msg, false);
        detailed.document().adjustSize();
        detailed.adjustSize();
        adjustSize();
    }

    private void assignMessage(final String msg, final boolean translate) {
        detailed.setText(translate ? traceParser.parse(msg) : msg);
    }

    @SuppressWarnings("unused")
    private void translate() {        
        final QPushButton sender = (QPushButton)QObject.signalSender();
        assignMessage(msg, sender.isChecked());
    }    
}
