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

import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QKeySequence.StandardKey;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QWidgetAction;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.explorer.editors.jmleditor.JmlEditor;
import org.radixware.kernel.explorer.editors.scml.IScml;
import org.radixware.kernel.explorer.editors.scml.IScmlTagGroup;
import org.radixware.kernel.explorer.editors.scml.IScmlTagKind;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.types.RdxIcon;


public class ToolBar {   
     private final QToolBar toolBarWidget; 
     private final ScmlTextEditor textEditor;       
     private final QToolButton btnCut = new QToolButton();
     private final QToolButton btnCopy = new QToolButton();
     private final QToolButton btnPast = new QToolButton();
     private final QToolButton btnUndo = new QToolButton();
     private final QToolButton btnRedo = new QToolButton();
     private final QToolButton btnFind = new QToolButton();
     private final QToolButton btnFindNext = new QToolButton();
     private final QToolButton btnReplace = new QToolButton();
     private final List<QAction> tagActions=new ArrayList<>();
     
     public ToolBar(final ScmlTextEditor textEditor){
         this.textEditor =textEditor;
         toolBarWidget = new QToolBar();
         toolBarWidget.setObjectName("ScmlEditorToolBar");
         toolBarWidget.setOrientation(Orientation.Horizontal); 
         createEditButtons();
     }
     
     private void createEditButtons(){         
        TextSearcher textFinder=new TextSearcher( textEditor);
        textEditor.selectionChanged.connect(this, "selectionChanged()");
        textEditor.textChanged.connect(this, "editor_textChange()");
        textEditor.undoAvailable.connect(this, "undoEnable(boolean)");
        textEditor.redoAvailable.connect(this, "redoEnable(boolean)");
        
        btnCut.setObjectName("btnCut");
        btnCut.setToolTip(Application.translate("JmlEditor", "Cut") + " (Ctrl+X)");
        btnCut.setEnabled(false);
        btnCut.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CUT));
        btnCut.clicked.connect(this, "btnCut_Clicked()");

        btnCopy.setObjectName("btnCopy");
        btnCopy.setToolTip(Application.translate("JmlEditor", "Copy") + " (Ctrl+C)");
        btnCopy.setEnabled(false);
        btnCopy.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.COPY));
        btnCopy.clicked.connect(this, "btnCopy_Clicked()");

        btnPast.setObjectName("btnPast");
        btnPast.setToolTip(Application.translate("JmlEditor", "Past") + " (Ctrl+V)");
        btnPast.setEnabled(false);
        btnPast.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.PASTE));
        btnPast.clicked.connect(this, "btnPast_Clicked()");

        btnUndo.setObjectName("btnUndo");
        btnUndo.setToolTip(Application.translate("JmlEditor", "Undo") + " (Ctrl+Z)");
        btnUndo.setEnabled(false);
        btnUndo.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.UNDO));
        btnUndo.clicked.connect(this, "btnUndo_Clicked()");

        btnRedo.setObjectName("btnRedo");
        btnRedo.setToolTip(Application.translate("JmlEditor", "Redo") + " (Ctrl+Y)");
        btnRedo.setEnabled(false);
        btnRedo.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.REDO));
        btnRedo.clicked.connect(this, "btnRedo_Clicked()");

        btnFind.setObjectName("btnFind");
        btnFind.setToolTip(Application.translate("JmlEditor", "Find") + " (Ctrl+F)");
        btnFind.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.FIND));
        btnFind.clicked.connect(textFinder, "find()");
        btnFind.clicked.connect(this, "btnFind_Clicked()");
        btnFind.setShortcut(StandardKey.Find);//.setShortcut("Ctrl+F");

        btnFindNext.setObjectName("btnFindNext");
        btnFindNext.setToolTip(Application.translate("JmlEditor", "Find Next") + " (F3)");
        btnFindNext.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.FIND_NEXT));
        btnFindNext.clicked.connect(textFinder, "findNext()");
        btnFindNext.setShortcut(StandardKey.FindNext);//setShortcut("F3");
        btnFindNext.setEnabled(false);

        btnReplace.setObjectName("btnReplace");
        btnReplace.setToolTip(Application.translate("JmlEditor", "Replace") + " (Ctrl+H)");
        btnReplace.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.IMG_REPLACE));
        btnReplace.clicked.connect(textFinder, "replace()");
        btnReplace.setShortcut(StandardKey.Replace);//.setShortcut("Ctrl+H");

        toolBarWidget.addWidget(btnCut);
        toolBarWidget.addWidget(btnCopy);
        toolBarWidget.addWidget(btnPast);
        toolBarWidget.addWidget(btnUndo);
        toolBarWidget.addWidget(btnRedo);
        toolBarWidget.addWidget(btnFind);
        toolBarWidget.addWidget(btnFindNext);
        toolBarWidget.addWidget(btnReplace);
     }
     

    public void setReadOnlyMode(boolean isReadOnlyMode) {
        if (isReadOnlyMode) {
            btnCopy.setEnabled(!isReadOnlyMode);
            btnCut.setEnabled(!isReadOnlyMode);
            btnPast.setEnabled(!isReadOnlyMode);
        }
        btnReplace.setEnabled(!isReadOnlyMode);
    }
     
     public void open(final IScml scml,List<IScmlTagKind> tagKindsForToolBar,List<IScmlTagGroup> tagGroups){
         redoEnable(false);
         undoEnable(false);
         
         if(tagActions!=null){
             for(QAction tagAction:tagActions){
                 toolBarWidget.removeAction(tagAction);
             }
         }
         if(tagKindsForToolBar!=null){
             for(final IScmlTagKind tagkind : tagKindsForToolBar){

                 QWidgetAction act=new  QWidgetAction(toolBarWidget){ 
                    @Override
                     protected QWidget createWidget(QWidget parent){
                         QToolButton btn=new QToolButton(parent);
                         btn.setToolTip(tagkind.getDescription());
                         btn.setIcon((RdxIcon)tagkind.getIcon());
                         btn.clicked.connect(this,"createTag()");
                         return btn;
                     } 

                    private void createTag(){
                        ScmlTag tag=tagkind.createTag(scml);
                        if(tag!=null){
                            TagView tagView=new TagView(tag, textEditor.textCursor().position());  
                            textEditor.insertTag(tagView, "");
                        }
                    }

                 };
                 toolBarWidget.addAction(act);
                 tagActions.add(act);
             }
         }
         if (tagGroups != null) {
             for (final IScmlTagGroup tagGroup : tagGroups) {
                 QWidgetAction act = new QWidgetAction(toolBarWidget) {
                     @Override
                     protected QWidget createWidget(QWidget parent) {
                         final QToolButton btn = new QToolButton(parent);
                         btn.setToolTip(tagGroup.getDescription());
                         btn.setIcon((RdxIcon) tagGroup.getIcon());
                         QMenu menu = new QMenu(btn);
                         for (final IScmlTagKind tagkind : tagGroup.getTags()) {

                             QAction menuAction = new QAction((RdxIcon) tagkind.getIcon(), tagkind.getDescription(), btn) {
                                 private void createTag() {
                                     ScmlTag tag = tagkind.createTag(scml);
                                     if (tag != null) {
                                         TagView tagView = new TagView(tag, textEditor.textCursor().position());
                                         textEditor.insertTag(tagView, "");
                                     }
                                 }
                             };
                             menuAction.triggered.connect(menuAction, "createTag()");
                             menu.addAction(menuAction);
                         }
                         btn.setMenu(menu);
                         btn.clicked.connect(btn, "showMenu()");
                         return btn;
                     }

                     @Override
                     protected void deleteWidget(final QWidget widget) {
                         if (widget instanceof QToolButton) {
                             ((QToolButton) widget).clicked.disconnect();
                         }
                         super.deleteWidget(widget);
                     }
                 };
                 toolBarWidget.addAction(act);
                 tagActions.add(act);
             }
         }
         //toolBar.addSeparator();
         //createEditButtons();
     }
     
    public void undoEnable(boolean available) {
        btnUndo.setEnabled(available);
    }

    public void redoEnable(boolean available) {
        btnRedo.setEnabled(available);
    }
    
    public void addButton(QWidget widget){
        toolBarWidget.addWidget(widget);
    }
    
    public void addSeparator(){
        toolBarWidget.addSeparator();
    }
     
     public QToolBar getToolBar(){
         return toolBarWidget;
     }
     
     @SuppressWarnings("unused")
    private void editor_textChange() {
        if (textEditor.isUndoRedoEnabled()) {
            undoEnable(true);
        }
    }    

    @SuppressWarnings("unused")
    private void selectionChanged() {
        QTextCursor tc = textEditor.textCursor();
        setButtonsEnabled(tc.hasSelection());
    }

    @SuppressWarnings("unused")
    private void btnCut_Clicked() {
        setButtonsEnabled(false);
        textEditor.cutText();
        setEnabledForBtnPast(true);
        textEditor.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void btnCopy_Clicked() {
        textEditor.copyText();
        setEnabledForBtnPast(true);
        textEditor.setFocusInText();
    }
    
    void setEnabledForBtnPast(boolean enabled) {
        if (!textEditor.isReadOnly()) {
            btnPast.setEnabled(enabled);
        }
    }

    @SuppressWarnings("unused")
    private void btnPast_Clicked() {
        setButtonsEnabled(false);
        textEditor.pastText();
        textEditor.setFocusInText();
    }
    
    @SuppressWarnings("unused")
    private void btnUndo_Clicked() {
        if (textEditor.undoText()) {
            textEditor.undo();
        }
        updateUndoRedoBtns();
        textEditor.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void btnRedo_Clicked() {
        textEditor.redoText();
        //updateUndoRedoBtns();
        textEditor.setFocusInText();
    }
    
    public void updateUndoRedoBtns(){
        redoEnable(textEditor.isRepoTextEnabled());
        undoEnable(textEditor.isUndoTextEnabled());
    }
    
    @SuppressWarnings("unused")
    private void btnFind_Clicked() {
        btnFindNext.setEnabled(true);
    }
    
    
    
    private void setButtonsEnabled(boolean state) {
        btnCopy.setEnabled(state);
        if (!textEditor.isReadOnly()) {
            btnCut.setEnabled(state);
        }
    }
    
    
}
