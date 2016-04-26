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

package org.radixware.kernel.explorer.editors.xscmleditor;

import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerMenu;


public class XcmlContextMenu extends ExplorerMenu{
    private final boolean isReadOnly;
    private final XscmlEditor editor;
    private final boolean hasSelection;
    private final boolean canUndo;
    private final boolean canRedo;
    private final boolean canPast;
    private final boolean enable;

    public XcmlContextMenu(final XscmlEditor edutor, final boolean hasSelection,final boolean canUndo,final boolean canRedo,final boolean canPast,final boolean enable){
        this.isReadOnly=edutor.isReadOnly();
        this.editor=edutor;
        this.hasSelection=hasSelection;
        this.canUndo=canUndo;
        this.canRedo=canRedo;
        this.canPast=canPast;
        this.enable=enable;
        if(isReadOnly) {
            createMenuForReadOnly();
        }else {
            createMenu();
        }
    }

    private void createMenu(){
        createActionEditTag(enable);
        this.addSeparator();
        final MessageProvider msgTranslator = editor.getEnvironment().getMessageProvider();
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Undo")+"\t Ctrl+Z", "undoTextFromContextMenu()",canUndo);
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Redo")+"\t Ctrl+Y", "redoText()",canRedo);
        this.addSeparator();
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Cut")+"\t Ctrl+X", "cutText()",hasSelection);
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Copy")+"\t Ctrl+C", "copyText()",hasSelection);
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Paste")+"\t Ctrl+V", "pastText()",canPast);
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Delete"), "deleteSelectedText()",hasSelection);
        this.addSeparator();
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Select All")+"\t Ctrl+A", "selectAll()",true);  
        createActionForContextMenu(msgTranslator.translate("JmlEditor","Copy as Xml Text"), "copyAsXmlText()",hasSelection);
    }

    private ExplorerAction createActionEditTag(final boolean enable){
       final ExplorerAction actEdit;
       final MessageProvider msgTranslator = editor.getEnvironment().getMessageProvider();
       if(!isReadOnly){
           actEdit=createActionForContextMenu(msgTranslator.translate("JmlEditor","Edit Tag"), "showTagEditor()", enable);//.setText(Environment.translate("SqmlEditor","Edit Tag"));
       }else{
           actEdit=createActionForContextMenu(msgTranslator.translate("JmlEditor","View Tag"), "showTagEditor()", enable);//actEdit.setText(Environment.translate("SqmlEditor","View Tag"));
       }
       final QFont font=actEdit.font();
       font.setBold(enable);
       actEdit.setFont(font);
       return actEdit;
    }

    private ExplorerAction createActionForContextMenu(final String text, final String func, final boolean enable){
       final ExplorerAction action=new ExplorerAction((QIcon)null,"",this);
       action.setObjectName(text);
       action.setText(editor.getEnvironment().getMessageProvider().translate("SqmlEditor",text));
       action.setEnabled(enable);
       action.triggered.connect(editor, func);
       this.addAction((Action)action);
       return action;
    }

    private void createMenuForReadOnly(){
       createActionEditTag(enable);
       this.addSeparator();
       final MessageProvider msgTranslator = editor.getEnvironment().getMessageProvider();
       createActionForContextMenu(msgTranslator.translate("JmlEditor","Copy")+"\t Ctrl+C","copyText()",hasSelection);
       this.addSeparator();
       createActionForContextMenu(msgTranslator.translate("JmlEditor","Select All")+"\t Ctrl+A","selectAll()",true);
    }    
}
