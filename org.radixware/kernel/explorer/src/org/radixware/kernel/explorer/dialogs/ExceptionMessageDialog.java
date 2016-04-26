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

import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.core.Qt.ToolButtonStyle;
import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QDialogButtonBox.StandardButton;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QStyle.StyleHint;
import com.trolltech.qt.gui.QTextOption.WrapMode;
import java.util.Objects;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;


public final class ExceptionMessageDialog extends QtDialog{
    
    private final QTextEdit message = new QTextEdit();
    private String detailedMessage;
    private String messageText;
    private final QWidget messageWidget = new QWidget(this);
    private final QDialogButtonBox buttonBox = new QDialogButtonBox(this);
    private final QToolButton detailedBtn = new QToolButton(this);
    private final QLabel iconLabel = new QLabel(this);
    private final ExceptionDetail detailedDialog;
    private static final int MAX_MESSAGE_LINES = 8;

    @SuppressWarnings("LeakingThisInConstructor")
    public ExceptionMessageDialog(final ExplorerSettings settings, final QWidget parent) {
        super(parent);        
        final WindowFlags f = new WindowFlags();
        f.set(new WindowType[]{WindowType.Dialog,
                    WindowType.MSWindowsFixedSizeDialogHint,
                    WindowType.WindowTitleHint,
                    WindowType.WindowSystemMenuHint});
        setWindowFlags(f);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        final QVBoxLayout vLayout = new QVBoxLayout();
        setLayout(vLayout);        
        
        detailedDialog = new ExceptionDetail(Application.getInstance().getEnvironment(), this);
        final QPalette palette = new QPalette(message.palette());
        palette.setColor(ColorRole.Base, palette.color(ColorRole.Window));

        messageWidget.setObjectName("exceptionbox_message_widget");
        final QHBoxLayout hLayout = new QHBoxLayout();
        hLayout.setMargin(0);
        messageWidget.setLayout(hLayout);

        final QIcon icon = ExplorerIcon.getQIcon(ClientIcon.Message.ERROR);
        iconLabel.setObjectName("exceptionbox_icon");
        iconLabel.setFixedSize(32, 32);
        iconLabel.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        iconLabel.setPixmap(icon.pixmap(32));
        hLayout.addWidget(iconLabel, 0, new AlignmentFlag[]{AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft});

        message.setObjectName("exceptionbox_message");
        message.setFrameShape(QFrame.Shape.NoFrame);
        message.setContentsMargins(2, 0, 0, 0);
        message.setPalette(palette);
        message.setReadOnly(true);
        message.setFixedHeight(32);
        message.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Maximum);
        hLayout.addWidget(message);

        vLayout.addWidget(messageWidget);

        detailedBtn.setObjectName("exceptionbox_detail_button");
        final QFont detailBtnFont = new QFont(detailedBtn.font());
        detailBtnFont.setPointSize(detailBtnFont.pointSize() - 1);
        detailBtnFont.setItalic(true);
        detailedBtn.setFont(detailBtnFont);
        detailedBtn.setToolButtonStyle(ToolButtonStyle.ToolButtonTextBesideIcon);
        detailedBtn.setAutoRaise(true);
        detailedBtn.setText(Application.translate("ExceptionDialog", "show details"));
        detailedBtn.setVisible(false);
        detailedBtn.clicked.connect(this, "switchDetails()");
        vLayout.addWidget(detailedBtn, 0, new AlignmentFlag[]{AlignmentFlag.AlignRight});

        buttonBox.setObjectName("exceptionbox_buttonbox");
        buttonBox.setCenterButtons(style().styleHint(StyleHint.SH_MessageBox_CenterButtons) > 0);
        buttonBox.clicked.connect(this, "close()");
        vLayout.addWidget(buttonBox);
        final QPushButton okButton = buttonBox.addButton(StandardButton.Ok);
        okButton.setDefault(true);
        okButton.setFocus();

        setModal(true);
    }
    
    public void setTitle(final String text) {
        setWindowTitle(text);
        updateSize();
    }

    public void setMessage(final String text) {
        if (!Objects.equals(text, messageText)){
            message.setWordWrapMode(WrapMode.NoWrap);
            message.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
            message.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
            message.setText(text);
            message.adjustSize();
            messageText = text;
            updateSize();
        }        
    }

    public String getMessage() {
        return messageText;
    }

    public void setDetails(final String message, final String trace) {
        if ((message != null && !message.isEmpty())
                || (trace != null && !trace.isEmpty())) {
            detailedMessage = message;
            if (trace != null && !trace.isEmpty()) {
                detailedMessage += "\n\nStack trace:\n" + trace;
            }
            detailedBtn.setVisible(true);
            detailedDialog.setMessage(detailedMessage);
        } else {
            detailedBtn.setVisible(false);
        }
        updateSize();        
    }

    public String getDetails() {
        return detailedMessage;
    }
    
    public void setIcon(final QIcon icon){
        if (icon==null){
            iconLabel.setPixmap(new QPixmap());
        }else{
            iconLabel.setPixmap(icon.pixmap(32));
        }
    }       

    private static int calcMessageHeight(final int linesCount, final QFontMetrics fontMetrics) {
        final int visibleLinesCount = Math.min(linesCount, MAX_MESSAGE_LINES);
        return visibleLinesCount * fontMetrics.height() + 8;
    }

    private void updateSize() {
        final QSize screenSize = QApplication.desktop().availableGeometry(QCursor.pos()).size();
        final int hardLimit = Math.min(screenSize.width() - 480, 1000);

        final String[] lines = message.toPlainText().split("\n");
        final int ln_count = lines.length;
        QFontMetrics fm = message.fontMetrics();

        message.setFixedHeight(calcMessageHeight(ln_count, fm));
        if (ln_count > MAX_MESSAGE_LINES) {
            message.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAsNeeded);
        }

        messageWidget.layout().activate();
        layout().activate();
        message.document().adjustSize();
        message.adjustSize();
        
        int width = (int)message.document().pageSize().width() + 72/*icon width and margins*/;

        if (width < layout().totalMinimumSize().width()) {
            width = layout().totalMinimumSize().width();
        }                

        if (width > hardLimit) {
            width = hardLimit;
            message.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);
            message.adjustSize();
            final int messageHeight = calcMessageHeight(ln_count, fm)
                    + message.horizontalScrollBar().sizeHint().height();
            message.setFixedHeight(messageHeight);
            messageWidget.layout().activate();
            layout().activate();            
        }
        
        fm = fontMetrics();
        final int windowTitleWidth = Math.min(fm.width(windowTitle()) + 150, hardLimit);
        if (windowTitleWidth > width) {
            width = windowTitleWidth;
        }

        final int height = layout().totalMinimumSize().height();
        setFixedSize(width, height);
        QCoreApplication.removePostedEvents(this, QEvent.Type.LayoutRequest.value());
    }

    @SuppressWarnings("unused")
    private void switchDetails() {
        detailedDialog.exec();
    }
}
