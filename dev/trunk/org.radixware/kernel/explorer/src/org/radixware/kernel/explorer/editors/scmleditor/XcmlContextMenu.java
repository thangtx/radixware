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

package org.radixware.kernel.explorer.editors.scmleditor;

import com.trolltech.qt.QNoSuchSlotException;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QMenu;
import org.radixware.kernel.common.client.localization.MessageProvider;


public class XcmlContextMenu extends QMenu {

    private final boolean isReadOnly;
    private final ScmlTextEditor editor;
    private final boolean hasSelection;
    private final boolean canUndo;
    private final boolean canRedo;
    private final boolean canPast;
    private final boolean enable;

    public XcmlContextMenu(final ScmlTextEditor edutor, final boolean hasSelection, final boolean canUndo, final boolean canRedo, final boolean canPast, final boolean enable) {
        this.isReadOnly = edutor.isReadOnly();
        this.editor = edutor;
        this.hasSelection = hasSelection;
        this.canUndo = canUndo;
        this.canRedo = canRedo;
        this.canPast = canPast;
        this.enable = enable;
        if (isReadOnly) {
            createMenuForReadOnly();
        } else {
            createMenu();
        }
    }

    private void createMenu() {
        createActionEditTag(enable);
        this.addSeparator();
        final MessageProvider msgTranslator = editor.getEnvironment().getMessageProvider();
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Undo") + "\t Ctrl+Z", "undoTextFromContextMenu()", canUndo);
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Redo") + "\t Ctrl+Y", "redoText()", canRedo);
        this.addSeparator();
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Cut") + "\t Ctrl+X", "cutText()", hasSelection);
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Copy") + "\t Ctrl+C", "copyText()", hasSelection);
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Paste") + "\t Ctrl+V", "pastText()", canPast);
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Delete"), "deleteSelectedText()", hasSelection);
        this.addSeparator();
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Select All") + "\t Ctrl+A", "selectAll()", true);
    }

    private QAction createActionEditTag(boolean enable) {
        final QAction actEdit;
        final MessageProvider msgTranslator = editor.getEnvironment().getMessageProvider();
        if (!isReadOnly) {
            actEdit = createActionForContextMenu(msgTranslator.translate("JmlEditor", "Edit Tag"), "showTagEditor()", enable);//.setText(Environment.translate("SqmlEditor","Edit Tag"));
        } else {
            actEdit = createActionForContextMenu(msgTranslator.translate("JmlEditor", "View Tag"), "showTagEditor()", enable);//actEdit.setText(Environment.translate("SqmlEditor","View Tag"));
        }
        if (actEdit != null) {
            QFont font = actEdit.font();
            font.setBold(true);
            actEdit.setFont(font);
        }
        return actEdit;
    }

    private QAction createActionForContextMenu(String text, String func, boolean enable) {
        try {
            QAction action = new QAction(this);
            action.setObjectName(text);
            action.setText(editor.getEnvironment().getMessageProvider().translate("SqmlEditor", text));
            action.setEnabled(enable);
            action.triggered.connect(editor, func);
            this.addAction(action);
            return action;
        } catch (QNoSuchSlotException e) {
            return null;
        }
    }

    private void createMenuForReadOnly() {
        createActionEditTag(enable);
        this.addSeparator();
        final MessageProvider msgTranslator = editor.getEnvironment().getMessageProvider();
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Copy") + "\t Ctrl+C", "copyText()", hasSelection);
        this.addSeparator();
        createActionForContextMenu(msgTranslator.translate("JmlEditor", "Select All") + "\t Ctrl+A", "selectAll()", true);
    }
}
