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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QTextBrowser;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import com.trolltech.qt.gui.QTextDocument.FindFlag;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QTextOption;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.trace.ClientTraceParser;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.trace.TraceParser;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.env.trace.ExplorerTraceItem;

import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.explorer.views.Splitter;


class MessageViewDialog extends QDialog {
    
    private static final String CONFIG_PREFIX = SettingNames.SYSTEM + "/TraceDialog/MessageViewDialog";
    
    private final QLineEdit timeLineEdit = new QLineEdit(this);
    private final QLineEdit sourceLineEdit = new QLineEdit(this);
    private final QPushButton showXmlButton = new QPushButton(this);    
    private QPlainTextEdit stackTraceTextEdit;
    private QPlainTextEdit textBrowser;
    private Splitter splitter;
    private String message;
    private final SearchDialog searchDialog;
    private final ExplorerSettings settings;
    private final ClientTraceParser clientTraceParser;
    private final TraceParser traceParser;
    private final MessageProvider messageProvider;
    private final ExplorerTraceItem traceItem;
    
    @SuppressWarnings("LeakingThisInConstructor")
    public MessageViewDialog(final IClientEnvironment environment, final ExplorerTraceItem item, final QWidget parent) {
        super(parent);
        this.traceItem = item;
        this.settings = (ExplorerSettings)environment.getConfigStore();
        messageProvider = environment.getMessageProvider();        
        if (environment.getApplication().isReleaseRepositoryAccessible()){
            clientTraceParser = new ClientTraceParser(environment);            
            traceParser = new TraceParser(environment.getDefManager().getClassLoader());//NOPMD
        }else{
            clientTraceParser = null;
            traceParser = null;            
        }
        setWindowFlags(WidgetUtils.WINDOW_FLAGS_FOR_DIALOG);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        setWindowIcon(item.getIcon(environment));
        setMinimumSize(520, 360);
        searchDialog = new SearchDialog(this);
        searchDialog.find.connect(this, "find()");
        setupUi();
        if (settings.contains(CONFIG_PREFIX)) {
            this.restoreGeometry(settings.readQByteArray(CONFIG_PREFIX));
        }
        setMessage();
    }

    @Override
    public void closeEvent(final QCloseEvent event) {
        settings.writeQByteArray(CONFIG_PREFIX, this.saveGeometry());
        if (splitter!=null){
            splitter.saveRatioToConfig();
        }
        super.closeEvent(event);
    }

    private void setMessage() {
        if (traceItem.getSeverity()== EEventSeverity.DEBUG) {
            this.setWindowTitle(messageProvider.translate("TraceDialog", "Debug Message"));
        } else if (traceItem.getSeverity() == EEventSeverity.EVENT) {
            this.setWindowTitle(messageProvider.translate("TraceDialog", "Event Message"));
        } else if (traceItem.getSeverity() == EEventSeverity.WARNING) {
            this.setWindowTitle(messageProvider.translate("TraceDialog", "Warning Message"));
        } else if (traceItem.getSeverity() == EEventSeverity.ERROR) {
            this.setWindowTitle(messageProvider.translate("TraceDialog", "Error Message"));
        } else if (traceItem.getSeverity() == EEventSeverity.ALARM) {
            this.setWindowTitle(messageProvider.translate("TraceDialog", "Alarm Message"));
        }
        assignMessage(traceItem.getMessageText(), false);
        timeLineEdit.setText(getTime(traceItem.getTime()));
        sourceLineEdit.setText(traceItem.getSource());
        if (stackTraceTextEdit!=null){
            stackTraceTextEdit.setPlainText(traceItem.getStackTrace());
        }
    }

    @SuppressWarnings("PMD.SimpleDateFormatNeedsLocale")
    private static String getTime(final long time) {
        final Date d = new Date(time);
        final DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss.S");
        return df.format(d);
    }

    private void assignMessage(final String str, final boolean translate) {
        message = String.valueOf(str);
        showXmlButton.setEnabled(isXml(str));
        int p = 0;
        textBrowser.clear();
        textBrowser.insertPlainText(translate ? clientTraceParser.parse(message) : message);
        textBrowser.moveCursor(MoveOperation.Start);
    }

    private void setupUi() {
        final QVBoxLayout vBoxLayout = new QVBoxLayout();
        setLayout(vBoxLayout);

        final QGridLayout gridLayout = new QGridLayout();
        vBoxLayout.addLayout(gridLayout);

        final QLabel timeLabel = new QLabel(messageProvider.translate("TraceDialog", "Time:"));
        final QLabel sourceLabel = new QLabel(messageProvider.translate("TraceDialog", "Source:"));

        gridLayout.addWidget(timeLabel, 1, 1);
        gridLayout.addWidget(sourceLabel, 2, 1);

        gridLayout.addWidget(timeLineEdit, 1, 2);
        gridLayout.addWidget(sourceLineEdit, 2, 2);

        timeLineEdit.setReadOnly(true);
        sourceLineEdit.setReadOnly(true);

        final QHBoxLayout hBoxLayout = new QHBoxLayout();
        vBoxLayout.addLayout(hBoxLayout);

        final QPushButton findButton = new QPushButton(this);
        hBoxLayout.addWidget(findButton);
        findButton.setMinimumSize(110, 20);
        findButton.setText(messageProvider.translate("TraceDialog", "&Find"));
        findButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND));
        findButton.setShortcut(QKeySequence.StandardKey.Find);
        findButton.clicked.connect(searchDialog, "show()");

        final QPushButton findNextButton = new QPushButton(this);
        hBoxLayout.addWidget(findNextButton);
        findNextButton.setMinimumSize(110, 20);
        findNextButton.setText(messageProvider.translate("TraceDialog", "Find &Next"));
        findNextButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.FIND_NEXT));
        findNextButton.setShortcut(QKeySequence.StandardKey.FindNext);
        findNextButton.clicked.connect(this, "find()");

        final QPushButton translateButton = new QPushButton(this);
        hBoxLayout.addWidget(translateButton);
        translateButton.setCheckable(true);
        translateButton.setMinimumSize(110, 20);
        translateButton.setText(messageProvider.translate("TraceDialog", "&Translate"));
        translateButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.TRANSLATE));
        translateButton.toggled.connect(this, "translate()");
        final boolean canTranslateMessage = 
            clientTraceParser!=null && (traceItem.getSeverity() == EEventSeverity.ERROR || traceItem.getSeverity() == EEventSeverity.ALARM);
        final boolean canTranslateStackTrace = traceItem.getStackTrace()!=null && traceParser!=null;
        final boolean isTranslateButtonVisible = canTranslateMessage || canTranslateStackTrace;
        translateButton.setVisible(isTranslateButtonVisible);
        
        hBoxLayout.addStretch();
        hBoxLayout.addWidget(showXmlButton, 0, new Alignment(AlignmentFlag.AlignRight));

        final boolean isStackTraceVisible = traceItem.getStackTrace()!=null;
        if (isStackTraceVisible){
            splitter = new Splitter(this, settings, CONFIG_PREFIX, 2./3);
            splitter.setObjectName("messageSplitter");
            splitter.setOrientation(Qt.Orientation.Vertical);
            textBrowser = new QPlainTextEdit();
            splitter.addWidget(textBrowser);
            final QWidget stackTraceContainer = new QWidget();
            stackTraceContainer.setObjectName("Rdx.MessageViewDialog.stackTraceContainer");
            final QVBoxLayout stackTraceLayout = new QVBoxLayout(stackTraceContainer);
            final QLabel stackTraceLabel = 
                new QLabel(messageProvider.translate("TraceDialog", "Call stack:"),stackTraceContainer);            
            stackTraceLayout.addWidget(stackTraceLabel);
            stackTraceTextEdit = new QPlainTextEdit(stackTraceContainer);
            stackTraceTextEdit.setReadOnly(true);
            stackTraceTextEdit.setUndoRedoEnabled(false);
            stackTraceTextEdit.setWordWrapMode(QTextOption.WrapMode.NoWrap);            
            stackTraceLayout.addWidget(stackTraceTextEdit);
            splitter.addWidget(stackTraceContainer);
            vBoxLayout.addWidget(splitter);
        }else{
            textBrowser = new QPlainTextEdit(this);
            vBoxLayout.addWidget(textBrowser);
        }        
        textBrowser.setReadOnly(true);
        textBrowser.setUndoRedoEnabled(false);
        textBrowser.setWordWrapMode(QTextOption.WrapMode.NoWrap);        
                
        showXmlButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.ValueTypes.XML));
        showXmlButton.setMinimumSize(140, 20);
        showXmlButton.setText(messageProvider.translate("TraceDialog", "Show &Xml Tree"));
        showXmlButton.clicked.connect(this, "showXml()");
    }

    private boolean contains(final String str) {
        if (searchDialog.isCaseSensetive()) {
            if (searchDialog.isForward()) {
                return textBrowser.find(str, FindFlag.FindCaseSensitively);
            } else {
                return textBrowser.find(str, FindFlag.createQFlags(FindFlag.FindBackward, FindFlag.FindCaseSensitively));
            }
        } else {
            if (searchDialog.isForward()) {
                return textBrowser.find(str);
            } else {
                return textBrowser.find(str, FindFlag.FindBackward);
            }
        }
    }

    @SuppressWarnings("unused")
    private void find() {
        final String str = searchDialog.getSearchString();
        if (str == null || str.equals("")) {
            return;
        }
        boolean ok = contains(str);
        if (!ok) {
            if (searchDialog.isForward()) {
                textBrowser.moveCursor(MoveOperation.Start);
            } else {
                textBrowser.moveCursor(MoveOperation.End);
            }
            ok = contains(str);
        }
        if (!ok) {
            QWidget parent;
            if (searchDialog.isVisible()) {
                parent = (QWidget) searchDialog;
            } else {
                parent = (QWidget) this;
            }
            final String msgTemplate = messageProvider.translate("TraceDialog", "Could not find string \"%s\"");
            ExplorerMessageBox.information(parent, messageProvider.translate("TraceDialog", "Information"), String.format(msgTemplate, str));
        }
    }

    @SuppressWarnings("unused")
    private void showXml() {
        final XmlTreeDialog dialog = new XmlTreeDialog(settings,this);
        dialog.setXml(message);
        dialog.exec();
    }
    
    @SuppressWarnings("unused")
    private void translate() {
        final QPushButton sender = (QPushButton)QObject.signalSender();
        if (traceItem.getSeverity()==EEventSeverity.ERROR || traceItem.getSeverity()==EEventSeverity.ALARM){
            assignMessage(traceItem.getMessageText(), sender.isChecked());
        }
        if (stackTraceTextEdit!=null && traceParser!=null){
            if (sender.isChecked()){
                stackTraceTextEdit.setPlainText(traceParser.parse(traceItem.getStackTrace()));
            }else{
                stackTraceTextEdit.setPlainText(traceItem.getStackTrace());
            }
        }
    }
    
    private static boolean isXml(final String str) {
        if (str!=null){
            final int beg = str.indexOf('<');
            final int end = str.lastIndexOf('>');
            return beg != -1 && end != -1 && end > beg;
        }
        return false;
    }
    
    public static void show(final IClientEnvironment environment, final ExplorerTraceItem item, final QWidget parent) {
        final MessageViewDialog messageViewDialog = new MessageViewDialog(environment, item, parent);
        messageViewDialog.show();
    }    
}
