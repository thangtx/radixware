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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Html;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.utils.Utils;


public abstract class AbstractTextField extends UIObject {

    public interface TextChangeListener {
        public void textChanged(String oldText, String newText);
    }
    
    public interface StartTextChangeListener{
        public void startTextChange(final String changedText);
    }
    
    public interface FinishTextChangeListener{
        public void finishTextChange(boolean accepted);
    }
    
    
    private String oldText;

    public AbstractTextField(Html html) {
        super(html);
    }

    private static class DefaultTextChangeListener implements TextChangeListener {

        private final List<TextChangeListener> listeners = new LinkedList<>();

        @Override
        public void textChanged(String oldText, String newText) {
            final List<TextChangeListener> listenersList = new LinkedList<>(listeners);
            for (TextChangeListener l : listenersList) {
                l.textChanged(oldText, newText);
            }
        }

        public void addTextListener(TextChangeListener listener) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        public void removeTextListener(TextChangeListener listener) {
            listeners.remove(listener);
        }
    }
    
    private static class DefaultStartTextChangeListener implements StartTextChangeListener{
        
        private final List<StartTextChangeListener> listeners = new LinkedList<>();
        
        public void addListener(final StartTextChangeListener listener){
            if (!listeners.contains(listener)){
                listeners.add(listener);
            }
        }
        
        public void removeListener(final StartTextChangeListener listener){
            listeners.remove(listener);
        }        

        @Override
        public void startTextChange(final String changedText) {
            final List<StartTextChangeListener> listenersList = new LinkedList<>(listeners);
            for (StartTextChangeListener l : listenersList) {
                l.startTextChange(changedText);
            }            
        }                
    }
    
    private static class DefaultFinishTextChangeListener implements FinishTextChangeListener{
        
        private final List<FinishTextChangeListener> listeners = new LinkedList<>();
        
        public void addListener(final FinishTextChangeListener listener){
            if (!listeners.contains(listener)){
                listeners.add(listener);
            }
        }
        
        public void removeListener(final FinishTextChangeListener listener){
            listeners.remove(listener);
        }

        @Override
        public void finishTextChange(final boolean accepted) {
            final List<FinishTextChangeListener> listenersList = new LinkedList<>(listeners);
            for (FinishTextChangeListener l: listenersList){
                l.finishTextChange(accepted);
            }
        }
                
    }
    
    private DefaultTextChangeListener defaultTextChangeListener = null;
    private DefaultStartTextChangeListener defaultStartTextChangeListener = null;
    private DefaultFinishTextChangeListener defaultFinishTextChangeListener = null;
    private boolean inModificationState;
    private boolean isStartModificationEnabled = true;

    public void addTextListener(final TextChangeListener listener) {
        if (defaultTextChangeListener == null) {
            defaultTextChangeListener = new DefaultTextChangeListener();
        }
        defaultTextChangeListener.addTextListener(listener);
    }

    public void removeTextListener(final TextChangeListener listener) {
        if (defaultTextChangeListener != null) {
            defaultTextChangeListener.removeTextListener(listener);
        }
    }
    
    public void addStartTextChangeListener(final StartTextChangeListener listener){
        if (defaultStartTextChangeListener == null){
            defaultStartTextChangeListener = new DefaultStartTextChangeListener();
        }
        defaultStartTextChangeListener.addListener(listener);
    }
    
    public void removeStartTextChangeListener(final StartTextChangeListener listener){
        if (defaultStartTextChangeListener!=null){
            defaultStartTextChangeListener.removeListener(listener);
        }
    }
    
    public void addFinishTextChangeListener(final FinishTextChangeListener listener){
        if (defaultFinishTextChangeListener==null){
            defaultFinishTextChangeListener = new DefaultFinishTextChangeListener();
        }
        defaultFinishTextChangeListener.addListener(listener);
    }
    
    public void removeFinishTextChangeListener(final FinishTextChangeListener listener){
        if (defaultFinishTextChangeListener!=null){
            defaultFinishTextChangeListener.removeListener(listener);
        }
    }

    @Override
    public void processAction(String actionName, String actionParam) {
        if ("change".equals(actionName)) {
            setText(actionParam == null ? "" : actionParam);
        }else if ("startModification".equals(actionName)){
            inModificationState = true;  
            final boolean isActionEnabled = isStartModificationEnabled;
            isStartModificationEnabled = false;
            if (isActionEnabled){
                fireStartTextChange(actionParam);
            }
            
        }else if ("discardModification".equals(actionName)){
            inModificationState = false;
            fireFinishTextChange(false);
        }
    }

    public final void setText(final String text) {
        final String prevText = oldText;
        if (setTextNoFire(text)) {
            fireTextChange(prevText, text);
            if (inModificationState){
                inModificationState = false;
                isStartModificationEnabled = true;
                fireFinishTextChange(true);
            }
        }
    }
    
    public final void setStartModificationActionEnabled(final boolean isEnabled){
        if (isEnabled!=isStartModificationEnabled && isEnabled){
            isStartModificationEnabled = true;
            getHtml().setAttr("repeatStartModification", "true");
        }
    }
    
    public final boolean isStartModificationActionEnabled(){
        return isStartModificationEnabled;
    }

    @Override
    protected String[] clientScriptsRequired() {
        return new String[]{"org/radixware/wps/rwt/inputBox.js"};
    }

    private void fireTextChange(final String oldText, final String newText) {
        if (defaultTextChangeListener != null) {
            defaultTextChangeListener.textChanged(oldText, newText);
        }
    }
    
    private void fireStartTextChange(final String changedText){
        if (defaultStartTextChangeListener!=null){
            defaultStartTextChangeListener.startTextChange(changedText);
        }                
    }
    
    private void fireFinishTextChange(final boolean accepted){        
        if (defaultFinishTextChangeListener!=null){
            defaultFinishTextChangeListener.finishTextChange(accepted);
        }
    }

    public final String getText() {
        return getTextFromHtml();
    }

    protected abstract String getTextFromHtml();

    protected abstract void updateTextInHtml(String text);

    public boolean setTextNoFire(String text) {
        if (!Utils.equals(oldText, text)) {
            updateTextInHtml(text);
            oldText = text;
            return true;
        } else {
            return false;
        }
    }

    public abstract void setReadOnly(boolean isReadOnly);

    public abstract boolean isReadOnly();
}
