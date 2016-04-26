/*
* Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps.views.editor.xml.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.wps.rwt.TextArea;


public class XmlTextArea extends TextArea{
    
    public static class Caret{
        private final int begin;
        private final int end;
        
        public Caret(String caret){
            if (caret!=null){
                int index = caret.indexOf('-');
                if (index>0 && index<caret.length()-1){
                    begin = parsePos(caret.substring(0, index));
                    end = parsePos(caret.substring(index+1));
                }else{
                    begin = -1;
                    end = -1;
                }
            }else{
                begin = -1;
                end = -1;                
            }
        }
        
        private static int parsePos(final String pos){
            try{
                return Integer.parseInt(pos);
            }catch(NumberFormatException exception){
                return -1;
            }
        }
        
        public boolean isValid(){
            return begin >= 0 && end >= 0;
        }

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }                
    }
    
    public static interface ICaretListener{
        void caretReceived(XmlTextArea textArea, Caret caret, String initiatorId);
    }

    public static interface ISelectedTextListener{
        void selectedTextReceived(String selectedText, String initiatorId);
    }
    
    private final List<ICaretListener> caretListeners = new LinkedList<>();
    private final List<ISelectedTextListener> selectedTextListeners = new LinkedList<>();
    
    public void addCaretListener(final ICaretListener listener){
        if (listener!=null && !caretListeners.contains(listener)){
            caretListeners.add(listener);
        }
    }
    
    public void removeCaretListener(final ICaretListener listener){
        if (listener!=null){
            caretListeners.remove(listener);
        }
    }
    
    public void addSelectedTextListener(final ISelectedTextListener listener){
        if (listener != null && !selectedTextListeners.contains(listener)){
            selectedTextListeners.add(listener);
        }
    }
    
    public void removeSelectedTextListener(final ISelectedTextListener listener){
        if (listener != null){
            selectedTextListeners.remove(listener);
        }
    }
    
    public void notifyCaretListeners(final Caret caret, final String initiatorId){
        final List<ICaretListener> listeners = new ArrayList<>(caretListeners);
        for (ICaretListener listener: listeners){
            listener.caretReceived(this,caret,initiatorId);
        }
    }
    
    public void notifySelectedTextListeners(final String selectedText, final String initiatorId){
        final List<ISelectedTextListener> listeners = new ArrayList<>(selectedTextListeners);
        for (ISelectedTextListener listener: listeners){
            listener.selectedTextReceived(selectedText,initiatorId);
        }
    }
    
    public String getFragment(final Caret caret){
        if (caret!=null && caret.isValid() && caret.getEnd()>caret.getBegin()){
            final String text = getText();
            if (text!=null){
                if (text.length()-1>caret.getEnd()){
                   return text.substring(caret.getBegin(), caret.getEnd()+1);
                }else{
                    return "";
                }
            }
        }
        return null;
    }
    
    public boolean deleteFragment(final Caret caret){
        if (caret.isValid() && caret.getEnd()>caret.getBegin()){
            final String text = getText();
            if (text!=null && text.length()>caret.getEnd()){
                final String firstPart = text.substring(0, caret.getBegin());
                final String lastPart = text.substring(caret.getEnd());
                setText(firstPart+lastPart);
                setCursorPosition(caret.getBegin());
                return true;
            }
        }
        return false;
    }
    
    public boolean replaceFragment(final Caret caret, final String newFragment){
        if (caret.isValid() && newFragment!=null){            
            final String text = getText();
            if (text!=null && text.length()>=caret.getEnd()){
                final String firstPart = text.substring(0, caret.getBegin());
                final String lastPart = text.substring(caret.getEnd());
                setText(firstPart+newFragment+lastPart);
                setCursorPosition(caret.getBegin()+newFragment.length());
                return true;
            }
        }
        return false;
    }

    @Override
    public void processAction(final String actionName, final String actionParam) {
        if ("updateSelectedText".equals(actionName)){
            final int deviderIndex = actionParam.lastIndexOf('#');
            if (deviderIndex>0 && deviderIndex<actionParam.length()-2){
                final String initiatorId = actionParam.substring(deviderIndex+1);
                notifySelectedTextListeners(actionParam.substring(0, deviderIndex),initiatorId);
            }                            
        }else if ("updateCursorPosition".equals(actionName)){
            final int deviderIndex = actionParam.lastIndexOf('#');
            if (deviderIndex>0 && deviderIndex<actionParam.length()-2){            
                final String initiatorId = actionParam.substring(deviderIndex+1);
                notifyCaretListeners(new Caret(actionParam.substring(0, deviderIndex)),initiatorId);
            }
        }else{
            super.processAction(actionName, actionParam);
        }
    }
    
    public final void setCursorPosition(final int pos){
        getHtml().setAttr("cursorPos", pos);
    }
}
