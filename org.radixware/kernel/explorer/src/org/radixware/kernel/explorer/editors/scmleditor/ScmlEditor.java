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

import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.editors.scml.AbstractScml;
import org.radixware.kernel.explorer.editors.scml.IScml;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionProvider;
import org.radixware.kernel.explorer.editors.scml.IScmlHighliter;
import org.radixware.kernel.explorer.editors.scml.IScmlTagGroup;
import org.radixware.kernel.explorer.editors.scml.IScmlTagKind;
import org.radixware.kernel.explorer.editors.scml.IScmlTagProvider;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.widgets.ExplorerWidget;


public class ScmlEditor extends ExplorerWidget{
    protected ScmlTextEditor textEditor;
    protected ToolBar toolBar;
    protected Highlighter hightlighter; 
    
    
    public ScmlEditor(final IClientEnvironment environment,QWidget parent){
        super(environment,parent);  
        
        createUi(parent);
    } 
    
    public ScmlTextEditor getTextEditor(){
        return textEditor;
    }
    
    private void createUi(QWidget parent){
        //QWidget mainWidget=new QWidget(parent);               
        textEditor=new ScmlTextEditor(getEnvironment(), parent);
        hightlighter=new Highlighter(/*getEnvironment(),*/ textEditor.document(),
                textEditor.getTagConverter(),null);
        toolBar=new ToolBar(textEditor);
        toolBar.getToolBar().setParent(this);
        //((Application) getEnvironment().getApplication()).actions.
        //               settingsChanged.connect(this, "updateEditorHightlight()");
        textEditor.textChanged.connect(this, "textChange()");
        textEditor.pastEnabled.connect(this, "setPastEnabled()");
        textEditor.updateUndoRedoBtns.connect(this, "updateUndoRedoBtns()");
        //textEditor.afterDeleteTag.connect(this, "afterDeleteTag()");
        
        QVBoxLayout layout=new QVBoxLayout();
        layout.setMargin(0);
        layout.setWidgetSpacing(0);
        this.setLayout(layout);        
        layout.addWidget(toolBar.getToolBar());   
        layout.addWidget(textEditor); 
    } 
    
    public void setPastEnabled(){
       toolBar.setEnabledForBtnPast(true); 
    }  
    
    public void updateUndoRedoBtns(){
       toolBar.updateUndoRedoBtns();  
    }
    /*public void afterEditTag(){ 
       System.out.println("afterEditTag"); 
    }    
    public void afterDeleteTag(){ 
        System.out.println("afterDeleteTag");
    }*/
    
    public void setReadOnly(boolean readOnly){
        textEditor.setReadOnly(readOnly);
        toolBar.setReadOnlyMode(readOnly);
    }
    
    public void open(AbstractScml scml){        
        open(scml, null);
    }
    
    public void open(AbstractScml scml, IScmlTagProvider provider){
        open(scml, provider, null);
    }  
    
    public void open(AbstractScml scml, IScmlTagProvider provider,IScmlCompletionProvider[] completionProvider){      
        fillToolBar(scml,provider);
        textEditor.open(scml, completionProvider);
        toolBar.setEnabledForBtnPast(textEditor.canPaste());
        toolBar.undoEnable(false);
        textEditor.setCursorInEditor(0);
    }  
    
    public IScml getScml(){
       return textEditor.getScml();
    }
     
    private ToolBar fillToolBar(IScml scml, IScmlTagProvider provider){        
        if(provider!=null){
            List<IScmlTagKind> tagKinds=provider.getAll(); 
            List<IScmlTagKind> tagKindsForToolBar=new  ArrayList<>(tagKinds);
            List<IScmlTagGroup> tagGroups= provider.getGroups();
            if(tagKinds!=null){                
                if(tagGroups!=null){
                    for(IScmlTagGroup tagGroup:tagGroups){
                        for(IScmlTagKind tagKind:tagGroup.getTags()){
                            if(tagKindsForToolBar.contains(tagKind)){
                                tagKindsForToolBar.remove(tagKind);
                            }
                        }
                    }
                }                    
            }
            toolBar.open(scml,tagKindsForToolBar, tagGroups);        
        }
        return null;         
    }
    
   /* @SuppressWarnings("unused")
    protected void updateEditorHightlight() {

    }*/
    
    public void addHighliter(IScmlHighliter highliter){
        if(hightlighter!=null){
            hightlighter.addRule(highliter);
            textChange();
        }
    } 
    
    public void removeHighliter(IScmlHighliter highliter){
        if(hightlighter!=null){
            hightlighter.removeRule(highliter);
            textChange();
        }
    }

    public void moveCursorToProblem(int endErrPos) {        
        textEditor.setCursorInEditor(endErrPos);
        textEditor.setFocus();
    }
    
    //public void updateEditor(){        
    //    updateEditorHightlight();
    //    this.update();
    //}
    
   /* public void addAnnotation(ScmlAnnotation annotation){
        
    }
    
    public void removeAnnotation(ScmlAnnotation annotation){
        
    }
    
    public void clearAnnotations(){
        
    }*/
  
     
    @SuppressWarnings("unused")
    private void textChange() {
         if(hightlighter!=null){
            textEditor.blockSignals(true);
            hightlighter.rehighlight();
            textEditor.blockSignals(false);
         }
    }
}
