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

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.core.Qt.WindowFlags;
import com.trolltech.qt.core.Qt.WindowType;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QPalette.ColorRole;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMandatoryPropertiesConfirmationDialog;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.MessageFilter;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class MandatoryPropertiesConfirmationDialog extends ExplorerDialog implements IMandatoryPropertiesConfirmationDialog {
    
    private final QLabel lbMessage, lbQuestion;
    private final QTextEdit teProperties;
    private final QWidget messageWidget = new QWidget(this);
    private final QCheckBox cbDontAskAgain = new QCheckBox(this);
    private static final int MAX_MESSAGE_LINES = 5;
    
    private String questionText;
    private boolean isDontAskAgainOptionVisible;
    private boolean dontAskAgain;
    private List<String> propertyTitles;

    public MandatoryPropertiesConfirmationDialog(final IClientEnvironment environment, final QWidget parent) {
        super(environment,parent);

        final WindowFlags f = new WindowFlags();
        f.set(new WindowType[]{WindowType.Dialog,
                    WindowType.MSWindowsFixedSizeDialogHint,
                    WindowType.WindowTitleHint,
                    WindowType.WindowSystemMenuHint});

        setWindowFlags(f);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose,true);
        final QVBoxLayout vLayout = dialogLayout();
        setLayout(vLayout);

        final String message =
            environment.getMessageProvider().translate("Editor", "The following mandatory properties are not defined: ");
        lbMessage = new QLabel(message,this);
        lbMessage.setObjectName("lbMessage");

        lbQuestion = new QLabel(this);
        lbMessage.setObjectName("lbQuestion");
        lbQuestion.setAlignment(Qt.AlignmentFlag.AlignCenter);
        lbQuestion.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);

        messageWidget.setObjectName("exceptionbox_message_widget");
        final QHBoxLayout hLayout = new QHBoxLayout();
        hLayout.setMargin(0);
        messageWidget.setLayout(hLayout);
        messageWidget.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);

        final QIcon icon = standardIcon();
        final QLabel iconLabel = new QLabel(this);
        iconLabel.setObjectName("question_icon");
        iconLabel.setFixedSize(32, 32);
        iconLabel.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        if (icon!=null){
            iconLabel.setPixmap(icon.pixmap(32));
        }
        hLayout.addWidget(iconLabel, 0, new AlignmentFlag[]{AlignmentFlag.AlignTop, AlignmentFlag.AlignLeft});
        hLayout.addWidget(lbMessage);

        vLayout.addWidget(messageWidget);

        {
            final QHBoxLayout hlProperties = new QHBoxLayout();
            teProperties = new QTextEdit(this);
            final QPalette palette = new QPalette(teProperties.palette());
            palette.setColor(ColorRole.Base, palette.color(ColorRole.Window));
            teProperties.setObjectName("properties_list");
            teProperties.setFrameShape(QFrame.Shape.NoFrame);
            teProperties.setContentsMargins(0, 0, 0, 0);
            teProperties.setPalette(palette);
            teProperties.setReadOnly(true);
            teProperties.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Maximum);
            teProperties.moveCursor(MoveOperation.Start);
            hlProperties.addWidget(teProperties);
            hlProperties.setContentsMargins(iconLabel.sizeHint().width()+2, 0, 0, 0);
            vLayout.addLayout(hlProperties);
        }

        vLayout.addWidget(lbQuestion);
        vLayout.addSpacing(vLayout.widgetSpacing());

        {
            cbDontAskAgain.setText(getEnvironment().getMessageProvider().translate("Editor", "Don't ask me again"));
            cbDontAskAgain.setObjectName("cbDontAskAgain");
            vLayout.addWidget(cbDontAskAgain);            
            vLayout.addSpacing(vLayout.widgetSpacing());
        }
            
        final IPushButton noButton =  
            addButtons(EnumSet.of(EDialogButtonType.YES,EDialogButtonType.NO),true).get(EDialogButtonType.NO);
        noButton.setDefault(true);        
        ((QWidget)noButton).setFocus();
        
        setCenterButtons(true);        
        setModal(true);
        updateSize();
    }

    private QIcon standardIcon()
    {
        final QStyle style = this.style();
        return style.standardIcon(QStyle.StandardPixmap.SP_MessageBoxQuestion);
    }

    public void setTitle(final String text) {
        setWindowTitle(text);
        updateSize();
    }

    private DialogResult checkFilter(){
        final MessageFilter filter = Application.getMessageFilter();
        if (filter!=null){
            final String message = lbMessage.text() + "\n"+teProperties.toPlainText() +"\n"+lbQuestion.text();
            final QMessageBox.StandardButtons buttons  =
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Yes, QMessageBox.StandardButton.No);
            final QMessageBox.StandardButton button =
                            filter.beforeMessageBox(windowTitle(), message, QMessageBox.Icon.Question, buttons);
            if (button==QMessageBox.StandardButton.Yes){
                return DialogResult.ACCEPTED;
            }
            else if (button!=null){
                return DialogResult.REJECTED;
            }
        }
        return null;
    }

    @Override
    public void done(final int result) {
        dontAskAgain = cbDontAskAgain.isChecked();        
        super.done(result);
    }
        
    @Override
    public DialogResult execDialog(final IWidget parent) {
        final DialogResult result = checkFilter();
        if (result==null){
            getEnvironment().getProgressHandleManager().blockProgress();
            try{
                return super.execDialog(parent);
            }
            finally{
                getEnvironment().getProgressHandleManager().unblockProgress();
            }            
        }
        else{
            return result;
        }        
    }

    @Override
    public DialogResult execDialog() {
        final DialogResult result = checkFilter();
        if (result==null){
            getEnvironment().getProgressHandleManager().blockProgress();
            try{
                return super.execDialog();
            }
            finally{
                getEnvironment().getProgressHandleManager().unblockProgress();
            }            
        }
        else{
            return result;
        }
    }

    private static int calcMessageHeight(final int linesCount, final QFontMetrics fontMetrics) {
        final int visibleLinesCount = Math.min(linesCount, MAX_MESSAGE_LINES);
        return visibleLinesCount * fontMetrics.height() + 8;
    }

    private void updateSize() {        
        final int hardLimit = WidgetUtils.getWndowMaxSize().width;            

        final String[] lines = teProperties.toPlainText().split("\n");
        final int ln_count = lines.length;
        final QFontMetrics fm = teProperties.fontMetrics();

        teProperties.setMinimumHeight(calcMessageHeight(ln_count, fm));
        teProperties.resize(teProperties.width(), calcMessageHeight(ln_count, fm));
        if (ln_count > MAX_MESSAGE_LINES) {
            teProperties.setVerticalScrollBarPolicy(ScrollBarPolicy.ScrollBarAsNeeded);
        }

        messageWidget.layout().activate();
        layout().activate();
        teProperties.adjustSize();
        int maxLine = ln_count - 1;
        for (int i = ln_count - 2; i >= 0; i--) {
            if (lines[maxLine].length() < lines[i].length()) {
                maxLine = i;
            }
        }
        int width = fm.width(maxLine >= 0 ? lines[maxLine] : "") + 72;

        if (width < layout().totalMinimumSize().width()) {
            width = layout().totalMinimumSize().width();
        }

        if (width > hardLimit) {
            width = hardLimit;
            teProperties.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOn);
            teProperties.adjustSize();
            final int messageHeight = calcMessageHeight(ln_count, fm)
                    + teProperties.horizontalScrollBar().sizeHint().height();
            teProperties.setMinimumHeight(messageHeight);
            teProperties.resize(teProperties.width(), messageHeight);
        }
        
        final int windowTitleWidth = Math.min(WidgetUtils.calcWindowHeaderWidth(this), hardLimit);
        if (windowTitleWidth > width) {
            width = windowTitleWidth;
        }

        final int height = layout().totalMinimumSize().height();
        setMinimumSize(width, height);
        setMaximumSize(width, height + ((ln_count * fm.height() + 8) - teProperties.minimumHeight()));
        resize(width, height);
    }

    @Override
    public void setPropertyTitles(final List<String> titles) {        
        propertyTitles = titles;
        teProperties.clear();
        final StringBuilder propertyText = new StringBuilder();
        for (String propertyTitle: propertyTitles){
            if (propertyText.length()>0){
                propertyText.append("\n");
            }
            propertyText.append(propertyTitle);
        }
        teProperties.append(propertyText.toString());
        updateSize();
    }

    @Override
    public List<String> getPropertyTitles() {
        return propertyTitles;
    }

    @Override
    public void setConfirmationMessage(final String text) {
        questionText = text;
        lbQuestion.setText(text);
    }

    @Override
    public String getConfirmationMessage() {
        return questionText;
    }

    @Override
    public void setDontAskAgainOptionVisible(final boolean isVisible) {
        isDontAskAgainOptionVisible = isVisible;
        cbDontAskAgain.setVisible(isVisible);
        updateSize();
    }

    @Override
    public boolean isDontAskAgainOptionVisible() {
        return isDontAskAgainOptionVisible;
    }

    @Override
    public boolean isDontAskAgainOptionChecked() {
        return isDontAskAgainOptionVisible && dontAskAgain;
    }       
}
