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
package org.radixware.kernel.explorer.editors.jmleditor.dialogs;

import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.ShortcutContext;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QShortcut;
import com.trolltech.qt.gui.QTextEdit_ExtraSelection;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.SrcPositionLocator;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor.JmlEditorIcons;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProblemList;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class StackTraceDialog extends ExplorerDialog {

    private final QToolButton btnPast = new QToolButton();
    private final QListView list = new QListView();
    //private final JmlEditor editor;
    private IPidGetter pidGetter;
    private JmlEditor.IUserFuncLocator ufLocator;

    public interface IPidGetter {

        public Pid getPidByOwnerPid(final String upDefId);

        public AdsUserFuncDef getUserFuncByPid(long pid);
    }

    private static class StackModelItem {

        boolean isInUserFunc = false;
        int lineNumber = -1;
        String text;
        long id;
        SrcPositionLocator.SrcLocation location = null;
    }

    public StackTraceDialog(final IClientEnvironment env, final QWidget parent, final JmlEditor.IUserFuncLocator ufLocator, final IPidGetter sPid) {
        super(env, parent, "StackDialogUF");
        this.ufLocator = ufLocator;
        this.pidGetter = sPid;
        this.setWindowTitle(Application.translate("JmlEditor", "Exception Stack Trace"));
        createUI();
    }

    private void createUI() {
        this.setMinimumSize(100, 100);

        createListUi();
        final IPushButton btnGoTo
                = addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), false).get(EDialogButtonType.OK);

        btnGoTo.setTitle(Application.translate("JmlEditor", "Go"));
        btnGoTo.setToolTip(Application.translate("JmlEditor", "Go to Selected Trace Element"));
        btnGoTo.setIcon(getEnvironment().getApplication().getImageManager().getIcon(JmlEditorIcons.GO_TO_OBJECT));
        btnGoTo.setEnabled(false);
        acceptButtonClick.connect(this, "btnGoTo_Clicked()");
        rejectButtonClick.connect(this, "reject()");

        //this.setWindowModality(WindowModality.WindowModal);
    }

    private void createListUi() {
        dialogLayout().setContentsMargins(10, 0, 10, 10);

        final QVBoxLayout layout = new QVBoxLayout();
        layout.setWidgetSpacing(0);
        layout.setMargin(0);
        final QToolBar toolBar = new QToolBar(this);
        //btnGoTo.setParent(toolBar);
        //btnGoTo.setIcon(ExplorerIcon.getQIcon(JmlEditorIcons.GO_TO_OBJECT));
        //btnGoTo.setToolTip(Application.translate("JmlEditor", "Go to Selected Trace Element"));
        //btnGoTo.clicked.connect(this, "btnGoTo_Clicked()");
        //btnGoTo.setEnabled(false);
        btnPast.setParent(toolBar);
        btnPast.setIcon(ExplorerIcon.getQIcon(ExplorerIcon.Editor.PASTE));
        btnPast.setToolTip(Application.translate("JmlEditor", "Past Exception Stack from Clipboard"));
        btnPast.clicked.connect(this, "btnPastClicked()");
        final QShortcut shortcut = new QShortcut(new QKeySequence(QKeySequence.StandardKey.Paste), this);
        shortcut.setContext(ShortcutContext.WindowShortcut);
        shortcut.activated.connect(this, "btnPastClicked()");

        //toolBar.addWidget(btnGoTo);
        toolBar.addWidget(btnPast);

        list.setParent(this);
        list.setObjectName("StackTraceList");
        list.doubleClicked.connect(this, "onItemDoubleClick(QModelIndex)");
        list.clicked.connect(this, "onItemClick(QModelIndex)");
        list.activated.connect(this, "onItemClick(QModelIndex)");
        list.setIconSize(new QSize(22, 22));
        list.setAlternatingRowColors(true);
        layout.addWidget(toolBar);
        layout.addWidget(list);
        dialogLayout().addLayout(layout);
    }

    @SuppressWarnings("unused")
    private void btnPastClicked() {
        final String clipbardText = QApplication.clipboard().text();
        if (clipbardText != null) {
            StackListModel model = new StackListModel();
            model.setStackStrings(clipbardText == null || clipbardText.isEmpty() ? new String[0] : clipbardText.split("\n"));
            list.setModel(model);
            if (model.curIndex >= 0 && model.curIndex < model.rowCount(null)) {
                QModelIndex index = model.index(model.curIndex, 0);
                list.setCurrentIndex(index);
                onItemClick(index);
                list.setFocus();
            }
        }
    }

    @SuppressWarnings("unused")
    private void btnGoTo_Clicked() {

        if (goTo(list.currentIndex())) {
            accept();
        }
    }

    @SuppressWarnings("unused")
    private void onItemClick(final QModelIndex index) {
        boolean enable = false;
        if (index != null) {
            final StackModelItem item = ((StackListModel) list.model()).getItem(index.row());
            enable = item.id >= 0;
        }
        getButton(EDialogButtonType.OK).setEnabled(enable);
    }

    @SuppressWarnings("unused")
    private void onItemDoubleClick(final QModelIndex index) {
        if (goTo(index)) {
            this.accept();
        }
    }

    private boolean switchToContext(final long id, final String sourceVersionName, JmlEditor.IPostOpenAction action) {
        return ufLocator.enshureObjectIsOpened(id, sourceVersionName, true, action);
    }

    private boolean goTo(final QModelIndex index) {
        if (index != null) {
            final StackModelItem item = ((StackListModel) list.model()).getItem(index.row());
            if (item != null) {
                AdsUserFuncDef userFunc = null;

                JmlEditor editor = ufLocator.getEditorIfAny();
                final IPidGetter externalFuncLookuper = this.pidGetter;

                editor = ufLocator.getEditorIfAny();
                if (editor != null && item.id == editor.getUserFuncPid()) {
                    userFunc = editor.getUserFunc();
                } else {
                    if (switchToContext(item.id, null, new JmlEditor.IPostOpenAction() {

                        @Override
                        public void perform(final JmlEditor.IUserFuncLocator locator) {
                            final AdsUserFuncDef userFunc = locator.findUserFunc(item.id, null);
                            final JmlEditor editor = locator.getEditorIfAny();
                            if (userFunc != null && editor != null) {
                                final SrcPositionLocator.SrcLocation loc = calcLocation(item.lineNumber, userFunc);
                                final int[] offsetAndLength = JmlProblemList.convertPosition(loc, editor.getJmlProcessor());
                                QApplication.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (offsetAndLength != null) {
                                            editor.getTextEditor().setFocus();
                                            locator.moveCursorToPosition(offsetAndLength);
                                        } else {
                                            editor.getEnvironment().messageError(Application.translate("JmlEditor", "Given stack trace element not found."));
                                            editor.getTextEditor().setExtraSelections(Collections.<QTextEdit_ExtraSelection>emptyList());
                                        }
                                    }
                                });
                            }
                        }
                    })) {
                        editor = ufLocator.getEditorIfAny();
                        if (editor != null && editor.getUserFuncPid() == item.id) {
                            userFunc = editor.getUserFunc();
                        } else if (externalFuncLookuper != null) {
                            userFunc = externalFuncLookuper.getUserFuncByPid(item.id);
                        } else {
                            getEnvironment().messageWarning(Application.translate("JmlEditor", "Given stack trace element does not match to current function"));
                            return false;
                        }
                    } else {
                        //  getEnvironment().messageWarning(Application.translate("JmlEditor", "Given stack trace element does not match to current function"));
                        return false;
                    }
                }

                if (userFunc != null && editor != null) {
                    final SrcPositionLocator.SrcLocation loc = calcLocation(item.lineNumber, userFunc);
                    final int[] offsetAndLength = JmlProblemList.convertPosition(loc, editor.getJmlProcessor());
                    if (offsetAndLength != null) {
                        ufLocator.moveCursorToPosition(offsetAndLength);
                    } else {
                        editor.getEnvironment().messageError(Application.translate("JmlEditor", "Given stack trace element not found."));
                        editor.getTextEditor().setExtraSelections(Collections.<QTextEdit_ExtraSelection>emptyList());
                    }
                    return true;
                } else {
                    final String message = Application.translate("JmlEditor", "User function was not found!");
                    Application.messageError(message);
                }

            }
        }
        return false;
    }

    private SrcPositionLocator.SrcLocation calcLocation(final int lineNumber, final AdsUserFuncDef def) {
        JavaSourceSupport.CodeWriter writer = def.getJavaSourceSupport().getCodeWriter(JavaSourceSupport.UsagePurpose.getPurpose(ERuntimeEnvironmentType.SERVER, JavaSourceSupport.CodeType.EXCUTABLE));
        final char[] src;
        if (writer != null) {
            CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
            if (writer.writeCode(printer)) {
                src = printer.getContents();
            } else {
                return null;
            }
        } else {
            return null;
        }

        final SrcPositionLocator locator = SrcPositionLocator.Factory.newInstance(def, src);
        final int endLine = JavaSourceSupport.lineNumber2Position(src, lineNumber) - 1; //Last symbol before '\n'.
        return locator.calc(endLine, endLine);
    }

    private class StackListModel extends QAbstractListModel {

        private final List<StackModelItem> items = new LinkedList<>();
        int curIndex = 0;

        //StackListModel() {
        //}
        void setStackStrings(final String[] stack) {
            items.clear();
            final SrcPositionLocator.SrcLocation[] def = new SrcPositionLocator.SrcLocation[1];
            for (int i = 0; i < stack.length; i++) {
                final StackModelItem item = new StackModelItem();
                item.text = stack[i].trim();

                final long pid = getId(stack[i]);
                item.isInUserFunc = pid >= 0;
                item.id = pid;
                item.lineNumber = getLineNumber(stack[i]);
                def[0] = null;
                item.location = def[0];
                items.add(item);

                if (item.isInUserFunc) {
                    curIndex = i;
                }
            }
        }

        StackModelItem getItem(final int index) {
            return items.get(index);
        }

        private long getId(final String s) {
            StringBuilder id = null;
            for (int i = 0, len = s.length(); i < len; i++) {
                char c = s.charAt(i);
                if (Character.isDigit(c)) {
                    if (id == null) {
                        id = new StringBuilder();
                    }
                    id.append(c);
                } else {
                    if (id != null) {
                        if (c == ')' && i < s.length() - 1) {//ok id found, stop processing
                            try {
                                return Long.parseLong(id.toString());
                            } catch (Exception ex) {
                                return -1;
                            }
                        } else {//not a valid id
                            return -1;
                        }
                    }
                }
            }
            return -1;
        }

        private int getLineNumber(final String string) {
            final int lparen = string.indexOf('(');
            if (lparen < 0) {
                return -1;
            }
            final int javaMarker = string.indexOf(".java", lparen + 1);
            if (javaMarker < 0) {
                return -1;
            }
            final int colon = string.indexOf(":", javaMarker + 1);
            if (colon < 0) {
                return -1;
            }
            final int rparen = string.indexOf(")", colon + 1);
            if (rparen < 0) {
                return -1;
            }
            final String lineNumberStr = string.substring(colon + 1, rparen);
            return Integer.decode(lineNumberStr);
        }

        public Object getElementAt(final int index) {
            return items.get(index);
        }

        @Override
        public Object data(final QModelIndex index, final int role) {
            if ((index != null)) {
                if (role == Qt.ItemDataRole.DisplayRole) {
                    return items.get(index.row()).text;
                } /*else if (role == Qt.ItemDataRole.DecorationRole) {
                 if (index.column() == 0) {
                 return getIcon(defList.get(index.row()).getId());
                 }
                 }*/ else if (role == Qt.ItemDataRole.TextAlignmentRole) {
                    if (index.column() == 0) {
                        return new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignVCenter);
                    }
                } else if (role == Qt.ItemDataRole.ForegroundRole) {
                    if (items.get(index.row()).isInUserFunc) {
                        return QColor.black;
                    } else {
                        return QColor.darkGray;
                    }
                }
            }
            return null;
        }

        @Override
        public int rowCount(final QModelIndex qmi) {
            return items.size();
        }
    }
}
