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

package org.radixware.kernel.explorer.editors.scmleditor.completer;

import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionItem;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionProvider;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.ScmlText;
import org.radixware.kernel.explorer.editors.scmleditor.ScmlTextEditor;
import org.radixware.kernel.explorer.editors.scmleditor.TagProcessor;
import org.radixware.kernel.explorer.editors.scmleditor.TagView;
import org.radixware.kernel.explorer.env.Application;


public class CompleterProcessor {
    private IScmlCompletionProvider[] completionProviders;
    
    private QCompleter completer=null;
    private ScmlTextEditor editor;
    private TagProcessor tagConverter;
    private boolean wasOpen=false;
    private int startPosForCompletion=-1;
    private String completionPrefix="";
    private final ItemDelegate delegate = new ItemDelegate();
    protected Map <String, IScmlCompletionItem>  completionList;    
    protected TagView prevComplItem=null;

    public int completerWidth= 500;
    public final static String NO_SUGGEST =Application.translate("JmlEditor", "No suggestions");
    public final static String PLEASE_WAIT=Application.translate("JmlEditor", "Please wait...");
    
    private CompleterWaiter waiter;
    protected  CompletionListModel wordList;
    private int prefixPos=0;
    
    private int prevRow=0;

    public CompleterProcessor(ScmlTextEditor editor,TagProcessor tagConverter,IScmlCompletionProvider[] completionProviders){
        this.editor=editor;
        this.tagConverter=tagConverter;
        if(completionProviders == null) { 
            this.completionProviders = new IScmlCompletionProvider[0]; 
        } else { 
            this.completionProviders = Arrays.copyOf(completionProviders, completionProviders.length); 
        }           
        setCompleter();
    }

    public QCompleter getCompleter(){
        return completer;
    }

    public boolean getWasOpen(){
        return wasOpen;
    }

    public void setWasOpen(boolean wasOpen){
        this.wasOpen=wasOpen;
        if(!wasOpen){
            completionPrefix="";
            cancelWaiter();
        }
    }

    public void cancelWaiter(){        
        if(waiter!=null){            
            waiter.cancel();
        }
    }

    private void setCompleter(){
        completer=new QCompleter(this.editor);
        wasOpen=false;
        if (completer!=null){
            completer.disconnect(editor);
        }

        if (completer==null){return;}
        completer.setWidget(editor);
        completer.highlightedIndex.connect(this, "pressed(QModelIndex)");
        completer.setCompletionMode(QCompleter.CompletionMode.PopupCompletion);
        completer.setCaseSensitivity(CaseSensitivity.CaseInsensitive);
        completer.activatedIndex.connect(this, "insertCompletionIndex(QModelIndex)");
    }
    
    public void exposeCompleter(){
        // if(tagConverter instanceof JmlProcessor){
             if(waiter!=null)return;

             popupPleaseWait();
             //Jml jml=(tagConverter).toXml( editor.toPlainText(),editor.textCursor());
             setStartForComplerion();
             List<TagView> tags=tagConverter.getCurrentTagList();
             int info[] = itemIndexAndOffset(startPosForCompletion+1,tagConverter.getCurrentTagList(),tagConverter);
             QFont font=new QFont();
             CompleterGetter task= new CompleterGetter(tags,info,/*startPosForCompletion,*/
                     font.resolve(completer.popup().font()),completionProviders);

             waiter=new CompleterWaiter();
             try {
                 LinkedHashMap<String,IScmlCompletionItem> cl=waiter.runAndWait(task);                 
                 prevComplItem=task.getPrevComplItem();
                 if((cl!=null) && (!waiter.wasCanceled())&&wasOpen){
                     completerWidth=task.getCompleterSize()+10+ItemDelegate.height*2;
                     popupCompleter(cl);
                 }else  {
                     completer.popup().hide();
                     wasOpen=false;
                 }
             } catch (ExecutionException ex) {
                 //Logger.getLogger(CompleterProcessor.class.getName()).log(Level.SEVERE, null, ex);
                 Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
             } catch (InterruptedException ex) {
                 //Logger.getLogger(XscmlEditor.class.getName()).log(Level.SEVERE, null, ex);
                 Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
             }finally{
                 waiter.close();
                 waiter=null;
             }             
        // }
    }

    private void popupPleaseWait(){
        LinkedHashMap<String,IScmlCompletionItem> cl=new LinkedHashMap<String,IScmlCompletionItem>();
        SuggestCompletionItem item=new SuggestCompletionItem(null,PLEASE_WAIT);
        cl.put(PLEASE_WAIT, item);
        setCompletionListModel(cl);
        //completer.popup().setItemDelegate(delegate);
        //completer.popup().setAlternatingRowColors(true);
        //completer.popup().setIconSize(new QSize(ItemDelegate.height,ItemDelegate.height));
        wasOpen=true;
        completer.setCompletionPrefix(PLEASE_WAIT);
        HtmlCompletionItem complItem=new HtmlCompletionItem(PLEASE_WAIT,null,completer.popup().font());
        int width= complItem.getLenght()+15;//HtmlItemCreator.getColumnWidth(PLEASE_WAIT, completer.popup().font())+15;
        setCompleterParam(width);
    }

   /* public void exposeQuickCompleter(TagView tag){
        if(tagConverter instanceof JmlProcessor){
           JmlTag jmlTag=(JmlTag)tag;
           if(waiter!=null)return;
            popupPleaseWait();
            waiter=new CompleterWaiter();
            QFont font=new QFont();
            QuickCompleterGetter quickTask = new QuickCompleterGetter(jmlTag.getTag(),font.resolve(completer.popup().font()));
            try {
                 prevComplItem=jmlTag.getTag();
                 LinkedHashMap<String,CompletionItem>  cl=waiter.runAndWait(quickTask);
                 if(cl!=null   && (!waiter.wasCanceled())&&wasOpen){
                    completerWidth=quickTask.getCompleterSize()+10+ItemDelegate.height*2;
                    popupCompleter(cl);
                 }else  {
                     completer.popup().hide();
                     wasOpen=false;
                 }
             } catch (InterruptedException ex) {
                 //Logger.getLogger(XscmlEditor.class.getName()).log(Level.SEVERE, null, ex);
             } catch (ExecutionException ex){
                 Logger.getLogger(CompleterProcessor.class.getName()).log(Level.SEVERE, null, ex);
             } finally {
                 waiter.close();
             }
             waiter=null;
        }
    }*/
    
    protected void setStartForComplerion(){
        final QTextCursor tc = editor.textCursor();
        int curPos =tc.position(),pos;
        if(tc.hasSelection()){            
            curPos=tc.selectionStart();
        }
        pos=curPos;
        while((curPos>0)&&(!isPoint(editor.toPlainText().charAt(curPos-1))&&(!isOperatorOrSeparator(editor.toPlainText().charAt(curPos-1))))){
            curPos--;
        }
        boolean isPoint= (curPos>0) ? isPoint(editor.toPlainText().charAt(curPos-1)) : false;
        if(isPoint){
            startPosForCompletion=curPos;
            completionPrefix=editor.toPlainText().substring(startPosForCompletion,pos/*tc.position()*/);
            while((completionPrefix.charAt(0)==' ') || (completionPrefix.charAt(0)=='\n')){
                 completionPrefix=completionPrefix.substring(1);
                 startPosForCompletion++;
            }
            prefixPos=startPosForCompletion;
        }else{
           completionPrefix=editor.toPlainText().substring(curPos,pos/*tc.position()*/);
           while((completionPrefix.charAt(0)==' ') || (completionPrefix.charAt(0)=='\n')){
              completionPrefix=completionPrefix.substring(1);
              curPos++;
           }
           prefixPos=curPos;
           startPosForCompletion=((curPos+1)<=pos/*tc.position()*/)?(curPos+1):curPos;
        }
    }

    private boolean isOperatorOrSeparator(char c){
        return (c=='+')||(c=='*')||(c=='|')||(c=='&')||(c=='>')||(c=='!')||(c=='=')||
               (c=='-')||(c=='/')||(c=='^')||(c=='%')||(c=='<')||(c=='~')||
               (c==']')||(c=='(')||(c==')')||(c==';')||(c==',')||(c==' ')||(c=='\n');
    }

    private boolean isOperatorOrSeparator(String s){
       return s.contains("+")||s.contains("*")||s.contains("|")||s.contains("&")||s.contains(">")||s.contains("!")||s.contains("=")||
              s.contains("-")||s.contains("/")||s.contains("^")||s.contains("%")||s.contains("<")||s.contains("~")||
              s.contains("]")||s.contains("(")||s.contains(")")||s.contains(";")||s.contains(",");
    }

    private boolean isPoint(char c){
        return (c=='.')||(c==':')||(c=='[');
    }

    private boolean isPoint(String s){
        return s.contains(".")||s.contains(":")||s.contains("[");
    }

    protected void setCompletionListModel(Map<String,IScmlCompletionItem>  completionList){
         this.completionList = completionList;
         wordList=new CompletionListModel();
         wordList.setList(completionList);
         completer.setModel(wordList);
    }

    private void popupCompleter(Map<String,IScmlCompletionItem>  completionList){
        setCompletionListModel(completionList);
        //completer.popup().setItemDelegate(delegate);
        //completer.popup().setAlternatingRowColors(true);
        //completer.popup().setIconSize(new QSize(ItemDelegate.height,ItemDelegate.height));
        showCompleter();
    }

    protected void showCompleter(){
       wasOpen=true;
       completer.setCompletionPrefix(completionPrefix);
       if(completer.popup().model().rowCount()>0){
           wordList.removeItem(NO_SUGGEST);
           wordList.removeItem(PLEASE_WAIT);           
           setCompleterParam(completerWidth);
       } else{
           showNoSuggestionFound(NO_SUGGEST);
       }
    }

    private void showNoSuggestionFound(String text){
         SuggestCompletionItem item=new SuggestCompletionItem(null,text);
         if(!wordList.containsItem(text)){
            wordList.addItem(item, text);
            completionList.put(text, item);
         }
         completer.setCompletionPrefix(text);
         HtmlCompletionItem complItem=new HtmlCompletionItem(text,null,completer.popup().font());
         int width= complItem.getLenght()+15;
         //int width= HtmlItemCreator.getColumnWidth(text, completer.popup().font())+15;
         setCompleterParam(width);
    }

    public void showCompleter(QKeyEvent e,int curPos){
        final boolean ctrlOrShift = ((e.modifiers().value() & (KeyboardModifier.ControlModifier.value() | KeyboardModifier.ShiftModifier.value()))>0);
        if ((completer==null) || (ctrlOrShift && e.text().isEmpty())){
             return ;
        }
        if(wasOpen){
            //final QTextCursor tc = editor.textCursor();
            //int curPos =tc.position();

            final String s=editor.toPlainText();
            if((startPosForCompletion>=0)&&(startPosForCompletion<=curPos)){
                 completionPrefix=s.substring(prefixPos,curPos);
                 if((isOperatorOrSeparator(completionPrefix))||(isPoint(completionPrefix))){
                    completer.popup().hide();
                    wasOpen=false;
                    return ;
                 }
            }else if((startPosForCompletion>0)&&(curPos>=0)&&(curPos<=editor.toPlainText().length())){
                completer.popup().hide();
                wasOpen=false;
                return ;
            }

            while((completionPrefix.charAt(0)==' ') || (completionPrefix.charAt(0)=='\n'))
                completionPrefix=completionPrefix.substring(1);
            completer.setCompletionPrefix(completionPrefix);
            if(completer.popup().model().rowCount()>0){
                wordList.removeItem(NO_SUGGEST);
                setCompleterParam(completerWidth);
            }else{
                showNoSuggestionFound(NO_SUGGEST);
           }
        }
    }

    private void setCompleterParam(int width){
          final QRect completerRect = editor.cursorRect();
          completerRect.setWidth(width);
          if(completer.popup().model().rowCount()>0){              
              completer.popup().setCurrentIndex(completer.popup().model().index(0, 0));
              completer.popup().setItemDelegate(delegate);
              completer.popup().setAlternatingRowColors(true);
              completer.popup().setIconSize(new QSize(ItemDelegate.height,ItemDelegate.height));
              completer.complete(completerRect);
              completer.popup().setFocusPolicy(FocusPolicy.StrongFocus);
          }
     }
   
     @SuppressWarnings("unused")
     private void pressed(QModelIndex index){
          if(index==null){
              QModelIndex prevIndex=completer.popup().model().index(prevRow, 0);
              final IScmlCompletionItem obj=completionList.get((String)prevIndex.data());
              if(obj instanceof SuggestCompletionItem){
                  completer.popup().hide();
                  wasOpen=false;
              }
          }else{
              prevRow=index.row();
          }
     }

     @SuppressWarnings("unused")
     private void insertCompletionIndex(final QModelIndex current){
          if ((completer.widget() != editor)||(!wasOpen)){ return;}
          wasOpen=false;

          final IScmlCompletionItem obj=completionList.get((String)current.data());
          if(obj!=null){
              insertCompletionIndex(obj, prevComplItem);
          }
     }

     private void insertCompletionIndex(IScmlCompletionItem obj,TagView prevComplItem){
        //if(editor.getUserFunc()==null)return;
        QTextCursor tc=editor.textCursor();
        if((obj!=null)&&(obj.getItems() !=null)){
            if(tc.hasSelection()){
                editor.deleteSelectedText(tc);
            }
            try{
                editor.undoTextChange();
                editor.blockSignals(true);
                editor.document().blockSignals(true);
                tc.beginEditBlock();

                deletePrefix(tc,obj.getReplaceStartOffset());
                removePreviousTag( tc, obj, prevComplItem);
                
                IScmlItem[] items=new IScmlItem[]{};//=obj.getNewItems();

                for(int i=0;i<items.length;i++){
                    //editor.getUserFunc().getSource().getItems().add(items[i]);
                    if(items[i] instanceof ScmlText){
                        final String completion=((ScmlText)items[i]).getText();
                        tc.insertText(completion,editor.getDefaultCharFormat());
                    }else{
                        editor.insertTag(items[i],tc,"");
                    }
                }
            }finally{
                tc.endEditBlock();
                editor.document().blockSignals(false);
                editor.blockSignals(false);
                editor.textChanged.emit();
            }
        }
        editor.setTextCursor(tc);
        editor.setFocusInText();
    }

    private void removePreviousTag(QTextCursor tc,IScmlCompletionItem obj,TagView prevComplItem){
       if((prevComplItem!=null)&&(prevComplItem.getScmlItem().isTag())&&(obj.removePrevious(prevComplItem.getScmlItem()))){
           int pos=tc.position();
           //TagView tag=/(tagConverter).getTagInfoFromMap((ScmlTag)prevComplItem);
           //if(prevComplItem!=null){
               editor.deleteTag(tc,prevComplItem);
               pos=pos-(int)(prevComplItem.getEndPos()-prevComplItem.getStartPos());
               if(pos>tc.position()){
                   tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, pos-tc.position());
                   tc.removeSelectedText();
               }
           //}
       }
   }

   private void deletePrefix(QTextCursor tc,int start){
       if (start>=0){
           tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, start+(tc.position()-startPosForCompletion));
           tc.removeSelectedText();
           int n=tc.position()-5;
           if((n>=0)&&(editor.toPlainText().substring(n, tc.position()).equals("idof["))){
               tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, "idof[".length());
               tc.removeSelectedText();
           }
       }
   }

   private int[] itemIndexAndOffset(int pos,List<TagView> tagsViews,TagProcessor jmlConverter) {
        if (pos-1 > editor.toPlainText().length()) {
            return null;
        }
        Iterator<TagView> it = tagsViews.listIterator();
        IScmlItem item;// = null;
        int idx = -1;
        int begin = 1;
        int end = 1;
        while (end <= pos && it.hasNext()) {
            item = it.next().getScmlItem();
            begin = end;
            if (item.isText()) {
                end += ((ScmlText) item).getText().length();
            } else if (item.isTag()) {
                TagView tag=jmlConverter.getTagInfoForCursorMove(end,false);
                if(tag!=null){
                    long endPos=tag.getEndPos();
                    end = (int)endPos;
                }
            }
            idx++;
        }
        return new int[]{idx, pos - begin};
    }
    
}
