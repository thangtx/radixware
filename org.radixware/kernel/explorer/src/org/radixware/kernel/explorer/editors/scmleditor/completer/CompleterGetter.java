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

import com.trolltech.qt.gui.QFont;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Callable;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionItem;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionProvider;
import org.radixware.kernel.explorer.editors.scml.IScmlCompletionRequestor;
import org.radixware.kernel.explorer.editors.scml.IScmlItem;
import org.radixware.kernel.explorer.editors.scml.ScmlTag;
import org.radixware.kernel.explorer.editors.scml.ScmlText;
import org.radixware.kernel.explorer.editors.scmleditor.TagView;


public class CompleterGetter implements Callable<LinkedHashMap<String,IScmlCompletionItem> >{
        private int scmlItemIndex;
        private int scmlItemOffset;
        private final QFont font;
        private TagView prevComplItem=null;
        private int completerSize=0;
        private List<TagView> items;
        private IScmlCompletionProvider[] completionProviders;

        CompleterGetter(List<TagView>  items, int info[], /*int caretOffset,*/ QFont font,IScmlCompletionProvider[] completionProviders){
            this.items=items;
            this.font=font;
            scmlItemIndex = info[0];
            scmlItemOffset = info[1];
            if(completionProviders == null) { 
                this.completionProviders = new IScmlCompletionProvider[0]; 
            } else { 
                this.completionProviders = Arrays.copyOf(completionProviders, completionProviders.length); 
            } 
        }

       private TagView getPrevComplItem(List<TagView> items){
            IScmlItem item=items.get(scmlItemIndex).getScmlItem();
            if(item instanceof ScmlText){
                 String text=((ScmlText)item).getText();
                 text=text.trim();
                 if((text.charAt(0)=='.')||(text.charAt(0)==':'))
                    return items.get(scmlItemIndex-1);
            }
            return null;
        }

    @Override
    public LinkedHashMap<String, IScmlCompletionItem> call() throws Exception {
        LinkedHashMap<String, IScmlCompletionItem> resMap=new LinkedHashMap<String,IScmlCompletionItem>();
        if(completionProviders!=null){
            List<IScmlCompletionItem> resultItems=new ArrayList<>();
           // List<Scml.Item> items = jml.getItems();
            if (scmlItemIndex >= 0 && items.size() > scmlItemIndex) {
                 IScmlItem item = items.get(scmlItemIndex).getScmlItem();
                 if (item instanceof ScmlTag) {
                       if (scmlItemOffset == 0) {
                             if (scmlItemIndex > 0) {
                                    IScmlItem prev = items.get(scmlItemIndex - 1).getScmlItem();
                                    if (prev instanceof ScmlText) {
                                         item = prev;
                                         scmlItemOffset = ((ScmlText) item).getText().length();
                                    }
                              }
                        }
                  }
                  //CompletionProviderFactory factory = CompletionProviderFactoryManager.getInstance().first(jml);
                 for(int i=0;i<completionProviders.length;i++){
                      IScmlCompletionProvider provider = completionProviders[i];//factory.findCompletionProvider(item);
                      if (provider != null) {// new ScmlCompletionRequestor(resultItems),IScmlItem item,int offset,int complexity
                          provider.computeCompletion(new ScmlCompletionRequestor(resultItems), item, scmlItemOffset,1);
                      }
                 }
             }
             
             if((!resultItems.isEmpty())){
                 List<HtmlCompletionItem> htmlCompletionItemList=new ArrayList<>();
                 completerSize=0;
                 for( IScmlCompletionItem item :resultItems){
                     HtmlCompletionItem htmlitem=new HtmlCompletionItem(item,font);
                     htmlCompletionItemList.add(htmlitem);
                     //String text=item.getLeadDisplayText();
                     if(/*(( prefix==null || prefix.isEmpty() || (prefix!=null && !prefix.isEmpty() && text!=null && text.startsWith(prefix)))
                             &&*/ htmlitem.getLenght()>completerSize)
                         completerSize=htmlitem.getLenght();     
                    
                     //resMap.put(HtmlItemCreator.createTable( item.getLeadDisplayText(), item.getTailDisplayText(), font,445),item);
                 }                  
                 completerSize=completerSize>1000? 1000 : completerSize;
                 for(HtmlCompletionItem item:htmlCompletionItemList){
                     resMap.put(item.getText(completerSize),item.getCompletionItem());
                 }
                 prevComplItem=getPrevComplItem(items);
            }
        }
        return resMap;
    }
        
   public int  getCompleterSize(){
      return completerSize;        
   }

    public TagView getPrevComplItem(){
        return prevComplItem;
    }

    private class ScmlCompletionRequestor implements IScmlCompletionRequestor {
        List<IScmlCompletionItem> items;

        public ScmlCompletionRequestor(List<IScmlCompletionItem> items) {
            this.items = items;
        }

      /*  @Override
        public void accept(org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem item) {
            items.add(item);
        }*/

        public boolean isAll() {
            return true;
        }

        @Override
        public void accept(IScmlCompletionItem item) {
             items.add(item);
        }
    }

}

