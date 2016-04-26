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

import com.trolltech.qt.QtPropertyWriter;
import com.trolltech.qt.core.QCoreApplication;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QSizeF;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextOption;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QAccessible;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QMessageBox;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QAbstractButton;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QDialogButtonBox;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QPixmap;
import com.trolltech.qt.gui.QCloseEvent;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QShowEvent;
import java.awt.Dimension;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.dialogs.IMessageBox;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.utils.ClientValueFormatter;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EDialogIconType;
import org.radixware.kernel.common.utils.SystemTools;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class ExplorerMessageBox extends QDialog implements IMessageBox {

    public final Signal1<QAbstractButton> buttonClicked = new Signal1<>();
    private static final Qt.WindowFlags WINDOW_FLAGS = calcWindowFlags();            
    private static final Qt.Alignment CENTER_ALIGNMENT = new Qt.Alignment(Qt.AlignmentFlag.AlignCenter);
    private final QCheckBox cbOption = new QCheckBox(this);
    private final QTextEdit teMessage = new QTextEdit(this);
    private final QLabel lbIcon = new QLabel(this);
    private final QGridLayout layout = new QGridLayout();
    private final QDialogButtonBox buttonBox = new QDialogButtonBox(Qt.Orientation.Horizontal, this);
    private final List<QAbstractButton> customButtonList = new LinkedList<>();
    private QMessageBox.Icon icon = QMessageBox.Icon.NoIcon;
    private String messageText = "";
    private String clickedButtonName;
    private String clickedButtonTitle;
    private String optionText;
    private boolean isOptionActivated;
    private QAbstractButton escapeButton;
    private QAbstractButton detectedEscapeButton;
    private QPushButton defaultButton;
    private boolean autoAddOkButton = true;
    private final EnumSet<EDialogButtonType> buttonsWithIcon = EnumSet.noneOf(EDialogButtonType.class);
    private final EnumSet<EDialogButtonType> buttonsWithText = EnumSet.noneOf(EDialogButtonType.class);
    
    private static Qt.WindowFlags calcWindowFlags(){
        final Qt.WindowFlags flags = 
            new Qt.WindowFlags(Qt.WindowType.WindowTitleHint, Qt.WindowType.WindowSystemMenuHint, Qt.WindowType.WindowCloseButtonHint);
        if (WidgetUtils.MODAL_DIALOG_WINDOW_TYPE==Qt.WindowType.Window){
            flags.set(Qt.WindowType.Window);
        }else{
            flags.set(Qt.WindowType.MSWindowsFixedSizeDialogHint);
        }
        return flags;
    }

    public ExplorerMessageBox(final QWidget parentWidget) {
        this(parentWidget, null, null);
    }

    private ExplorerMessageBox(final QWidget parentWidget, final String title, final String text) {
        super(parentWidget);
        setupUi();
        if (title != null && !title.isEmpty()) {
            setWindowTitle(title);
        }
        if (text != null && !text.isEmpty()) {
            setText(text);
        }
    }

    private void setupUi() {
        setWindowFlags(WINDOW_FLAGS);
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);

        teMessage.setObjectName("teMessage");
        teMessage.setFrameShape(QFrame.Shape.NoFrame);
        final QPalette palette = new QPalette(teMessage.palette());
        palette.setColor(QPalette.ColorRole.Base, palette.color(QPalette.ColorRole.Window));
        teMessage.setPalette(palette);
        teMessage.setReadOnly(true);
        teMessage.setWordWrapMode(QTextOption.WrapMode.NoWrap);
        teMessage.setSizePolicy(QSizePolicy.Policy.MinimumExpanding, QSizePolicy.Policy.Expanding);

        if (SystemTools.isOSX) {
            teMessage.setContentsMargins(16, 0, 0, 0);
        } else {
            teMessage.setContentsMargins(2, 0, 0, 0);
        }
        lbIcon.setObjectName("lbIcon");
        lbIcon.setSizePolicy(QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Fixed);
        cbOption.setObjectName("cbOption");
        cbOption.setVisible(false);
        cbOption.clicked.connect(this, "onOptionClicked()");
        buttonBox.setObjectName("buttonBox");
        updateCenterButtons();
        buttonBox.clicked.connect(this, "onButtonClicked(QAbstractButton)");

        if (SystemTools.isOSX) {
            layout.setMargin(0);
            layout.setVerticalSpacing(8);
            layout.setHorizontalSpacing(0);
            setContentsMargins(24, 15, 24, 20);
            layout.addWidget(lbIcon, 0, 0, 2, 1, Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignLeft);
            layout.addWidget(teMessage, 0, 1, 1, 1);
            // -- leave space for information label --
            layout.setRowStretch(1, 100);
            layout.setRowMinimumHeight(2, 6);
            layout.addWidget(cbOption, 3, 0, 1, 2);
            layout.addWidget(buttonBox, 4, 1, 1, 1);

            final ExplorerFont font = ExplorerFont.Factory.getFont(this.font());
            teMessage.setFont(font.getBold().getQFont());
        } else {
            layout.addWidget(lbIcon, 0, 0, 2, 1, Qt.AlignmentFlag.AlignTop);
            //layout.addWidget(teMessage, 0, 1, 1, 1, Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignLeft);
            layout.addWidget(teMessage, 0, 1, 1, 1);
            // -- leave space for information label --
            layout.addWidget(cbOption, 2, 0, 1, 2);
            layout.addWidget(buttonBox, 3, 0, 1, 2);
        }

        layout.setSizeConstraint(QLayout.SizeConstraint.SetNoConstraint);
        setLayout(layout);
        setModal(true);
    }

    private void updateCenterButtons() {
        buttonBox.setCenterButtons(this.style().styleHint(QStyle.StyleHint.SH_MessageBox_CenterButtons, null, this) != 0);
    }

    public final void addButton(final QAbstractButton button, final QMessageBox.ButtonRole role) {
        if (button == null) {
            return;
        }
        removeButton(button);
        buttonBox.addButton(button, QDialogButtonBox.ButtonRole.resolve(role.value()));
        customButtonList.add(button);
        autoAddOkButton = false;
    }

    public final QPushButton addButton(final String text, final QMessageBox.ButtonRole role) {
        final QPushButton pushButton = new QPushButton(text);
        addButton(pushButton, role);
        updateSize();
        return pushButton;
    }

    public final QPushButton addButton(final QMessageBox.StandardButton button) {
        final QPushButton pushButton = buttonBox.addButton(QDialogButtonBox.StandardButton.resolve(button.value()));
        if (pushButton != null) {
            autoAddOkButton = false;
        }
        return pushButton;
    }

    public final void addStandardButton(final QMessageBox.StandardButton buttonType, final QIcon icon, final String buttonText) {
        final QPushButton button = addButton(buttonType);
        if (icon != null) {
            button.setIcon(icon);
            buttonsWithIcon.add(getMessageBoxButton(buttonType));
        }
        if (buttonText != null && !buttonText.isEmpty()) {
            button.setText(buttonText);
            buttonsWithText.add(getMessageBoxButton(buttonType));
        }
    }

    @Override
    public final void addButton(EDialogButtonType buttonType, String title, Icon icon) {
        final QMessageBox.StandardButton qButtonType = getQButton(buttonType);
        if (qButtonType!=null){
            final QIcon qIcon = icon==null ? null : ExplorerIcon.getQIcon(icon);            
            addStandardButton(qButtonType, qIcon, title);
        }     
    }
    
    

    public final void removeButton(final QAbstractButton button) {
        customButtonList.removeAll(Collections.singleton(button));
        if (escapeButton == button) {//NOPMD
            escapeButton = null;
        }
        if (defaultButton == button) {//NOPMD
            defaultButton = null;
        }
        buttonBox.removeButton(button);
        updateSize();
    }

    public final void setStandardButtons(final QMessageBox.StandardButtons buttons) {
        buttonBox.setStandardButtons(new QDialogButtonBox.StandardButtons(buttons.value()));

        final List<QAbstractButton> buttonList = buttonBox.buttons();
        if (!buttonList.contains(escapeButton)) {
            escapeButton = null;
        }
        if (!buttonList.contains(defaultButton)) {
            defaultButton = null;
        }
        autoAddOkButton = false;
        updateSize();
    }

    public final QMessageBox.StandardButtons standardButtons() {
        return new QMessageBox.StandardButtons(buttonBox.standardButtons().value());
    }

    public final QMessageBox.StandardButton standardButton(final QAbstractButton button) {
        return QMessageBox.StandardButton.resolve(buttonBox.standardButton(button).value());
    }

    public final QAbstractButton button(final QMessageBox.StandardButton which) {
        return buttonBox.button(QDialogButtonBox.StandardButton.resolve(which.value()));
    }

    public final List<QAbstractButton> buttons() {
        return buttonBox.buttons();
    }

    public final QMessageBox.ButtonRole buttonRole(final QAbstractButton button) {
        return QMessageBox.ButtonRole.resolve(buttonBox.buttonRole(button).value());
    }

    public final QAbstractButton escapeButton() {
        return escapeButton;
    }

    public final void setEscapeButton(final QAbstractButton button) {
        if (buttonBox.buttons().contains(button)) {
            escapeButton = button;
        }
    }

    @QtPropertyWriter(enabled = false)
    public final void setEscapeButton(final QMessageBox.StandardButton button) {
        setEscapeButton(buttonBox.button(QDialogButtonBox.StandardButton.resolve(button.value())));
    }

    private void detectEscapeButton() {
        if (escapeButton != null) { // escape button explicitly set
            detectedEscapeButton = escapeButton;
            return;
        }

        // Cancel button automatically becomes escape button
        detectedEscapeButton = buttonBox.button(QDialogButtonBox.StandardButton.Cancel);
        if (detectedEscapeButton != null) {
            return;
        }

        // If there is only one button, make it the escape button
        final List<QAbstractButton> buttons = buttonBox.buttons();
        if (buttons.size() == 1) {
            detectedEscapeButton = buttons.get(0);
            return;
        }

        // if the message box has one RejectRole button, make it the escape button
        for (QAbstractButton button : buttons) {
            if (buttonBox.buttonRole(button) == QDialogButtonBox.ButtonRole.RejectRole) {
                if (detectedEscapeButton != null) { // already detected!
                    detectedEscapeButton = null;
                    break;
                }
                detectedEscapeButton = button;
            }
        }
        if (detectedEscapeButton != null) {
            return;
        }

        // if the message box has one NoRole button, make it the escape button
        for (QAbstractButton button : buttons) {
            if (buttonBox.buttonRole(button) == QDialogButtonBox.ButtonRole.NoRole) {
                if (detectedEscapeButton != null) { // already detected!
                    detectedEscapeButton = null;
                    break;
                }
                detectedEscapeButton = button;
            }
        }
    }

    public final QPushButton defaultButton() {
        return defaultButton;
    }

    public final void setDefaultButton(final QPushButton button) {
        if (!buttonBox.buttons().contains(button)) {
            return;
        }
        defaultButton = button;
        button.setDefault(true);
        button.setFocus();
    }

    @QtPropertyWriter(enabled = false)
    public final void setDefaultButton(final QMessageBox.StandardButton button) {
        setDefaultButton(buttonBox.button(QDialogButtonBox.StandardButton.resolve(button.value())));
    }

    public final String getClickedButtonName() {
        return clickedButtonName;
    }

    public final String getClickedButtonTitle() {
        return clickedButtonTitle;
    }

    @SuppressWarnings("unused")
    private void onButtonClicked(QAbstractButton button) {
        clickedButtonName = button.objectName();
        clickedButtonTitle = button.text();

        done(execReturnCode(button)); // does not trigger closeEvent
        buttonClicked.emit(button);
    }

    @SuppressWarnings("unused")
    private void onOptionClicked() {
        isOptionActivated = cbOption.isChecked();
    }

    private int execReturnCode(final QAbstractButton button) {
        final int ret = buttonBox.standardButton(button).value();
        if (ret == QMessageBox.StandardButton.NoButton.value()) {
            return customButtonList.indexOf(button); // if button == 0, correctly sets ret = -1
        }
        return ret;
    }

    private static QMessageBox.StandardButtons getQMSGButtons(Set<EDialogButtonType> buttons) {        
        QMessageBox.StandardButton[] qt = new QMessageBox.StandardButton[buttons.size()];
        int i = 0;
        for (EDialogButtonType bt : buttons) {
            qt[i] = getQButton(bt);
            i++;
        }
        return new QMessageBox.StandardButtons(qt);        
    }

    public static QMessageBox.StandardButton getQButton(EDialogButtonType button) {
        if (button == null) {
            return null;
        }
        switch (button) {
            case ABORT:
                return QMessageBox.StandardButton.Abort;
            case YES_TO_ALL:
                return QMessageBox.StandardButton.YesToAll;
            case ALL:
                return QMessageBox.StandardButton.YesToAll;
            case CANCEL:
                return QMessageBox.StandardButton.Cancel;
            case CLOSE:
                return QMessageBox.StandardButton.Close;
            case HELP:
                return QMessageBox.StandardButton.Help;
            case IGNORE:
                return QMessageBox.StandardButton.Ignore;
            case NO_BUTTON:
            case NO:
                return QMessageBox.StandardButton.No;
            case NO_TO_ALL:
                return QMessageBox.StandardButton.NoToAll;
            case OK:
                return QMessageBox.StandardButton.Ok;
            case OPEN:
                return QMessageBox.StandardButton.Open;
            case RETRY:
                return QMessageBox.StandardButton.Retry;
            case SAVE:
                return QMessageBox.StandardButton.Save;
            case YES:
                return QMessageBox.StandardButton.Yes;
            default:
                return null;
        }
    }

    public static EDialogButtonType getMessageBoxButton(final QMessageBox.StandardButton button) {
        if (button == null) {
            return null;
        }
        switch (button) {
            case Abort:
                return EDialogButtonType.ABORT;
            case YesToAll:
                return EDialogButtonType.YES_TO_ALL;
            case Cancel:
                return EDialogButtonType.CANCEL;
            case Close:
                return EDialogButtonType.CLOSE;
            case Help:
                return EDialogButtonType.HELP;
            case Ignore:
                return EDialogButtonType.IGNORE;
            case No:
                return EDialogButtonType.NO;
            case Ok:
                return EDialogButtonType.OK;
            case Open:
                return EDialogButtonType.OPEN;
            case Retry:
                return EDialogButtonType.RETRY;
            case Save:
                return EDialogButtonType.SAVE;
            case Yes:
                return EDialogButtonType.YES;
            default:
                return null;
        }
    }

    public final String text() {
        return messageText;
    }

    public final void setText(final String text) {
        messageText = text;
        teMessage.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        teMessage.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
        teMessage.setText(text);
        teMessage.adjustSize();
        updateSize();
    }

    public final QMessageBox.Icon icon() {
        return icon;
    }

    public final void setIcon(final QMessageBox.Icon icon) {
        setIconPixmap(standardIcon(icon, this));
        this.icon = icon;
    }

    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private static QPixmap standardIcon(final QMessageBox.Icon icon, final ExplorerMessageBox mb) {
        final QStyle.StandardPixmap stdPixmap;
        switch (icon) {
            case Information:
                stdPixmap = QStyle.StandardPixmap.SP_MessageBoxInformation;
                break;
            case Warning:
                stdPixmap = QStyle.StandardPixmap.SP_MessageBoxWarning;
                break;
            case Critical:
                stdPixmap = QStyle.StandardPixmap.SP_MessageBoxCritical;
                break;
            case Question:
                stdPixmap = QStyle.StandardPixmap.SP_MessageBoxQuestion;
                break;
            default:
                stdPixmap = null;
        }
        if (stdPixmap == null) {
            return new QPixmap();
        }
        final QStyle style = mb == null ? QApplication.style() : mb.style();
        final QIcon tmpIcon = style.standardIcon(stdPixmap, null, mb);
        if (!tmpIcon.isNull()) {
            final int iconSize = style.pixelMetric(QStyle.PixelMetric.PM_MessageBoxIconSize, null, mb);
            return tmpIcon.pixmap(iconSize, iconSize);
        }
        return new QPixmap();
    }

    @QtPropertyWriter(enabled = false)
    public final void setIcon(final EDialogIconType icon) {
        setIcon(getQMSGIcon(icon));
    }

    public final QPixmap iconPixmap() {
        if (lbIcon != null && lbIcon.pixmap() != null) {
            return lbIcon.pixmap();
        }
        return new QPixmap();
    }

    public final void setIconPixmap(final QPixmap pixmap) {
        lbIcon.setPixmap(pixmap);
        updateSize();
        icon = QMessageBox.Icon.NoIcon;
    }

    private static QMessageBox.Icon getQMSGIcon(final EDialogIconType icon) {
        if (icon == null) {
            return QMessageBox.Icon.NoIcon;
        }
        switch (icon) {
            case INFORMATION:
                return QMessageBox.Icon.Information;
            case WARNING:
                return QMessageBox.Icon.Warning;
            case CRITICAL:
                return QMessageBox.Icon.Critical;
            case QUESTION:
                return QMessageBox.Icon.Question;
            default:
                return QMessageBox.Icon.NoIcon;
        }
    }

    @Override
    public final void setOptionText(final String text) {
        if (text == null || text.isEmpty()) {
            cbOption.setChecked(false);
            cbOption.setVisible(false);
        } else {
            cbOption.setText(text);
            cbOption.setVisible(true);
        }
        optionText = text;
        updateSize();
    }

    @Override
    public final String getOptionText() {
        return optionText;
    }

    @Override
    public final boolean isOptionActivated() {
        return isOptionActivated;
    }

    @Override
    public boolean event(final QEvent e) {
        boolean result = super.event(e);
        if (e != null && e.type() == QEvent.Type.LayoutRequest) {
            updateSize();
        }
        return result;
    }

    @Override
    public void closeEvent(final QCloseEvent e) {
        if (detectedEscapeButton == null) {
            e.ignore();
            return;
        }
        final int returnCode = execReturnCode(detectedEscapeButton);
        super.closeEvent(e);
        clickedButtonName = detectedEscapeButton.objectName();
        clickedButtonTitle = detectedEscapeButton.text();
        setResult(returnCode);
    }

    @Override
    @SuppressWarnings({"fallthrough", "PMD.MissingBreakInSwitch"})
    public void changeEvent(final QEvent ev) {
        switch (ev.type()) {
            case StyleChange: {
                if (icon != QMessageBox.Icon.NoIcon) {
                    setIcon(icon);
                }
                updateCenterButtons();
            }
            case FontChange:
            case ApplicationFontChange:
                if (SystemTools.isOSX) {
                    final ExplorerFont font = ExplorerFont.Factory.getFont(this.font());
                    teMessage.setFont(font.getBold().getQFont());
                }
                break;
        }
        super.changeEvent(ev);
    }

    @Override
    public void keyPressEvent(final QKeyEvent e) {
        final boolean isEscape =
                e.key() == Qt.Key.Key_Escape.value() || (SystemTools.isOSX && e.modifiers().value() == Qt.KeyboardModifier.ControlModifier.value() && e.key() == Qt.Key.Key_Period.value());
        if (isEscape) {
            if (detectedEscapeButton != null) {
                if (SystemTools.isOSX) {
                    detectedEscapeButton.animateClick();
                } else {
                    detectedEscapeButton.click();
                }
            }
            return;
        }

        final boolean isAlt = e.modifiers().isSet(Qt.KeyboardModifier.AltModifier);
        if (!isAlt) {
            int key = e.key() & ~(Qt.KeyboardModifier.KeyboardModifierMask.value());
            if (key != 0) {
                final List<QAbstractButton> buttons = buttonBox.buttons();
                for (QAbstractButton pb : buttons) {
                    int acc = pb.shortcut().toInt() & ~(Qt.KeyboardModifier.KeyboardModifierMask.value());
                    if (acc == key) {
                        pb.animateClick();
                        return;
                    }
                }
            }
        }
        super.keyPressEvent(e);
    }

    @Override
    public void showEvent(QShowEvent e) {
        if (autoAddOkButton) {
            addButton(QMessageBox.StandardButton.Ok);
        }
        detectEscapeButton();
        updateSize();

        final QRect parentGeometry;
        if (parentWidget() == null) {
            parentGeometry = QApplication.desktop().availableGeometry();
        } else {
            parentGeometry = parentWidget().window().frameGeometry();
        }
        setGeometry(QStyle.alignedRect(QApplication.layoutDirection(), CENTER_ALIGNMENT, size(), parentGeometry));
        QAccessible.updateAccessibility(this, 0, QAccessible.Event.Alert);

        super.showEvent(e);
    }

    @Override
    public final int exec() {
        if (Application.getMessageFilter() != null) {
            final QMessageBox.StandardButton result = 
                Application.getMessageFilter().beforeMessageBox(windowTitle(), text(), icon(), standardButtons());
            if (result != null) {
                return result.value();
            }
        } 
        if (icon() != QMessageBox.Icon.Critical) {
            configureStandardButton(QMessageBox.StandardButton.Ok, ClientIcon.Dialog.BUTTON_OK, Application.translate("ExplorerDialog", "OK"));
            configureStandardButton(QMessageBox.StandardButton.Cancel, ClientIcon.Dialog.BUTTON_CANCEL, Application.translate("ExplorerDialog", "Cancel"));
            configureStandardButton(QMessageBox.StandardButton.Yes, ClientIcon.Dialog.BUTTON_OK, Application.translate("ExplorerDialog", "Yes"));
            configureStandardButton(QMessageBox.StandardButton.No, ClientIcon.Dialog.BUTTON_CANCEL, Application.translate("ExplorerDialog", "No"));
        }

        final Qt.WindowFlags windowFlags = new Qt.WindowFlags(WINDOW_FLAGS.value());
        if (parentWidget() != null && windowModality() == Qt.WindowModality.WindowModal) {
            windowFlags.set(Qt.WindowType.Widget, Qt.WindowType.Sheet);
            setParent(parentWidget(), Qt.WindowType.Sheet);
        } else {
            if (WidgetUtils.MODAL_DIALOG_WINDOW_TYPE==Qt.WindowType.Window){
                windowFlags.set(Qt.WindowType.Widget, Qt.WindowType.Window);
            }else{
                windowFlags.set(Qt.WindowType.Widget, Qt.WindowType.Window, Qt.WindowType.Dialog);
            }
            setParent(parentWidget(), WidgetUtils.MODAL_DIALOG_WINDOW_TYPE);
        }
        setWindowFlags(windowFlags);
        setDefaultButton(defaultButton);
        Application.getInstance().getEnvironment().getProgressHandleManager().blockProgress();        
        try{
            return super.exec();
        }finally{
            Application.getInstance().getEnvironment().getProgressHandleManager().unblockProgress();
        }
    }

    @Override
    public final EDialogButtonType execMessageBox() {
        final QMessageBox.StandardButton result = QMessageBox.StandardButton.resolve(exec());
        return getMessageBoxButton(result);
    }

    public static QMessageBox.StandardButton critical(final QWidget parent, final String title, final String message) {
        return showMessage(parent, title, message, QMessageBox.Icon.Critical,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok), null);
    }

    public static QMessageBox.StandardButton information(final QWidget parent, final String title, final String message) {
        return showMessage(parent, title, message, QMessageBox.Icon.Information,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok), null);
    }

    public static QMessageBox.StandardButton warning(final QWidget parent, final String title, final String message) {
        return showMessage(parent, title, message, QMessageBox.Icon.Warning,
                new QMessageBox.StandardButtons(QMessageBox.StandardButton.Ok), null);
    }

    public static QMessageBox.StandardButton showMessage(final String title, final String message, final QMessageBox.Icon icon, final QMessageBox.StandardButtons buttons, final QMessageBox.StandardButton defaultButton) {
        return showMessage(Application.getMainWindow(), title, message, icon, buttons, defaultButton);
    }

    public static EDialogButtonType showMessage(final String title, final String message, final EDialogIconType icon, final Set<EDialogButtonType> buttons, final EDialogButtonType defaultButton) {
        final QMessageBox.StandardButton button =
                showMessage(Application.getMainWindow(), title, message, getQMSGIcon(icon), getQMSGButtons(buttons), getQButton(defaultButton));
        return getMessageBoxButton(button);
    }

    public static QMessageBox.StandardButton showMessage(final QWidget parent, final String title, final String message, final QMessageBox.Icon icon, final QMessageBox.StandardButtons buttons, final QMessageBox.StandardButton defaultButton) {
        final String windowTitle;
        if (title == null || title.isEmpty()) {
            if (icon != null) {
                switch (icon) {
                    case Critical:
                        windowTitle = Application.translate("ExplorerDialog", "Error");
                        break;
                    case Information:
                        windowTitle = Application.translate("ExplorerDialog", "Information");
                        break;
                    case Warning:
                        windowTitle = Application.translate("ExplorerDialog", "Warning");
                        break;
                    case Question:
                        windowTitle = Application.translate("ExplorerDialog", "Confirmation");
                        break;
                    default:
                        windowTitle = Application.translate("ExplorerDialog", "Message");
                        break;
                }
            } else {
                windowTitle = Application.translate("ExplorerDialog", "Message");
            }
        } else {
            windowTitle = ClientValueFormatter.capitalizeIfNecessary(Application.getInstance().getEnvironment(), title);
        }

        final ExplorerMessageBox messageBox = new ExplorerMessageBox(parent);
        messageBox.setIcon(icon);
        messageBox.setWindowTitle(windowTitle);
        messageBox.setText(message);
        messageBox.setStandardButtons(buttons);
        if (defaultButton != null) {
            messageBox.setDefaultButton(defaultButton);
        }        
        return QMessageBox.StandardButton.resolve(messageBox.exec());                
    }

    private void configureStandardButton(final QMessageBox.StandardButton standardButton, final ClientIcon icon, final String buttonText) {
        final QAbstractButton button = button(standardButton);
        if (button != null) {
            if (!buttonsWithIcon.contains(getMessageBoxButton(standardButton))
                    && (button.icon() == null || button.icon().isNull())) {
                button.setIcon(ExplorerIcon.getQIcon(icon));
            }
            if (!buttonsWithText.contains(getMessageBoxButton(standardButton))) {
                button.setText(buttonText);
            }
        }
    }

    @Override
    public final EDialogButtonType show(IClientEnvironment env, String message, String title, EDialogIconType icon, Set<EDialogButtonType> buttons) {
        return getMessageBoxButton(showMessage(title, message, getQMSGIcon(icon), getQMSGButtons(buttons), null));
    }

    private int calcMinimumHeight() {
        int result = contentsMargins().bottom() + contentsMargins().top();
        result += layout.contentsMargins().bottom() + layout.contentsMargins().top();
        result += Math.max(teMessage.minimumSizeHint().height(), lbIcon.minimumSizeHint().height());
        result += layout.verticalSpacing();
        if (SystemTools.isOSX) {
            result += 6 + layout.verticalSpacing();
        }
        if (cbOption.isVisible()) {
            result += cbOption.minimumSizeHint().height() + layout.verticalSpacing();
        }
        result += buttonBox.minimumSizeHint().height();
        return result;
    }

    private void updateSize() {
        if (!isVisible()) {
            return;
        }
        
        final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();

        final String optionText = cbOption.text();
        {//initial settings
            cbOption.setText("");
            setMinimumSize(0, 0);
            setMaximumSize(WidgetUtils.MAXIMUM_SIZE, WidgetUtils.MAXIMUM_SIZE);
            teMessage.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            teMessage.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            teMessage.setWordWrapMode(QTextOption.WrapMode.NoWrap);
            teMessage.setFixedSize(teMessage.minimumSizeHint());
        }

        layout.activate();
        teMessage.document().adjustSize();
        teMessage.adjustSize();

        QSize layoutMinimumSize = layout.totalMinimumSize();
        final QSize messageMinimumSize = teMessage.minimumSizeHint();
        QSizeF documentSize = teMessage.document().documentLayout().documentSize();
        final double documentMargin = teMessage.document().documentMargin();

        int minimumWidth = layoutMinimumSize.width();
        final int buttonsDelta = minimumWidth - buttonBox.sizeHint().width();
        final int messageDelta = minimumWidth - messageMinimumSize.width() - lbIcon.sizeHint().width() - layout.horizontalSpacing();
        final int horizontalDelta;
        final int buttonsWidth;
        if (buttonsDelta < messageDelta) {
            horizontalDelta = buttonsDelta + lbIcon.sizeHint().width() + layout.horizontalSpacing();
            buttonsWidth = minimumWidth;
        } else {
            buttonsWidth = -1;
            horizontalDelta = minimumWidth - messageMinimumSize.width();
        }
        double width = documentSize.width() + documentMargin + horizontalDelta;

        teMessage.setMinimumWidth(0);
        teMessage.setMaximumWidth(WidgetUtils.MAXIMUM_SIZE);

        final boolean horizontalScrolling;
        if (width > sizeLimit.getWidth()) {
            teMessage.setWordWrapMode(QTextOption.WrapMode.WordWrap);
            teMessage.document().adjustSize();
            documentSize = teMessage.document().documentLayout().documentSize();

            width = documentSize.width() + documentMargin + horizontalDelta;
            if (width > sizeLimit.getWidth()) {
                horizontalScrolling = true;
                teMessage.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOn);
            } else {
                horizontalScrolling = false;
            }
            width = sizeLimit.getWidth();
        } else {
            horizontalScrolling = false;
        }

        final QFontMetrics fm =
                ExplorerFont.Factory.getFont(QApplication.font("QWorkspaceTitleBar")).getQFontMetrics();
        final int windowTitleWidth = Math.min(fm.width(windowTitle()) + 150, (int)sizeLimit.getWidth());
        if (windowTitleWidth > width) {
            width = windowTitleWidth;
        }

        if (buttonsWidth > width) {
            width = buttonsWidth;
        }

        if (!optionText.isEmpty()) {
            cbOption.setText(optionText);
            layout.activate();
            layoutMinimumSize = layout.totalMinimumSize();
            minimumWidth = Math.min(layoutMinimumSize.width(), (int)sizeLimit.getWidth());

            if (minimumWidth > width) {
                width = minimumWidth;
            }
        }

        setFixedWidth((int) width);
        layout.activate();
        documentSize = teMessage.document().documentLayout().documentSize();

        final int minimumHeight = calcMinimumHeight();
        final int scrollBarHeight = horizontalScrolling ? teMessage.horizontalScrollBar().sizeHint().height() : 0;
        final int verticalMargins = minimumHeight - messageMinimumSize.height();
        double height = documentSize.height() + documentMargin + scrollBarHeight + verticalMargins;

        if (height > sizeLimit.getHeight()) {
            height = sizeLimit.getHeight();            
            if (!horizontalScrolling) {
                teMessage.setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
                setFixedWidth((int)width+teMessage.verticalScrollBar().sizeHint().width());
            }
            teMessage.setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAsNeeded);
        }
        final int messageHeight = (int) height - verticalMargins;
        teMessage.setFixedHeight(messageHeight);
        layout.activate();
        setFixedHeight(layout.minimumSize().height());
        QCoreApplication.removePostedEvents(this, QEvent.Type.LayoutRequest.value());
    }
}