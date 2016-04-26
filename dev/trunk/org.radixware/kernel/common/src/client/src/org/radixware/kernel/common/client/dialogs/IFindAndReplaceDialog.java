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

package org.radixware.kernel.common.client.dialogs;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.env.ClientSettings;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.views.IDialog;
import org.radixware.kernel.common.client.widgets.IWidget;


public interface IFindAndReplaceDialog extends IDialog{        
        
    public interface ISearchWidget extends IWidget{
        String getSearchString();
        void setSearchString(String currentSearchString);
    }            
    
    public interface IFindActionListener{
        void find();
    }
    
    public interface IReplaceActionListener{
        void replace(boolean replaceAll);
    }
    
    public final static class State{
        
        final private static String MATCH_CASE_KEY = "matchCase";
        final private static String WHOLE_WORDS_KEY = "wholeWords";
        final private static String FORWARD_DIRECTION_KEY = "isForward";
        final private static String FIND_WHAT_KEY = "findWhat";
        final private static String REPLACE_WITH_KEY = "replaceWith";
        
        private boolean matchCase;
        private boolean wholeWords;
        private boolean isForward;
        private String findWhat;
        private String replaceWith;
        
        private State(){            
        }
        
        static State readFrom(final ClientSettings settings, final String configKey){
            final State state = new State();
            state.matchCase = settings.readBoolean(configKey+"/"+MATCH_CASE_KEY, false);
            state.wholeWords = settings.readBoolean(configKey+"/"+WHOLE_WORDS_KEY, false);
            state.isForward = settings.readBoolean(configKey+"/"+FORWARD_DIRECTION_KEY, true);
            state.findWhat = settings.readString(configKey+"/"+FIND_WHAT_KEY, "");
            state.replaceWith = settings.readString(configKey+"/"+REPLACE_WITH_KEY, "");
            return state;
        }
        
        static void writeTo(final ClientSettings settings, final String configKey, final IFindAndReplaceDialog dialog){
            settings.writeBoolean(configKey+"/"+MATCH_CASE_KEY, dialog.isMatchCaseChecked());
            settings.writeBoolean(configKey+"/"+WHOLE_WORDS_KEY, dialog.isWholeWordChecked());
            settings.writeBoolean(configKey+"/"+FORWARD_DIRECTION_KEY, dialog.isForwardChecked());
            settings.writeString(configKey+"/"+FIND_WHAT_KEY, dialog.getFindWhat());
            settings.writeString(configKey+"/"+REPLACE_WITH_KEY, dialog.getReplaceWith());
        }
        
        public boolean isMatchCase(){
            return matchCase;
        }
        
        public boolean isWholeWord(){
            return wholeWords;
        }

        public String getFindWhat() {
            return findWhat;
        }

        public boolean isForward() {
            return isForward;
        }

        public String getReplaceWith() {
            return replaceWith;
        }      
    }
    
    public abstract static class Presenter<T extends IFindAndReplaceDialog>{
        
        protected final T dialog;
        
        public Presenter(final T dialog){
            this.dialog = dialog;
        }
        
        protected abstract void createUi(final boolean canReplace);
        protected abstract void loadState(final ClientSettings settings, final String configKey, final State state);
        protected void saveState(final ClientSettings settings, final String configKey){
            State.writeTo(settings, configKey, dialog);
        }
    }
    
    public final static class Controller{

        private List<IFindActionListener> findListeners;
        private List<IReplaceActionListener> replaceListeners;
    
        private final ClientSettings settings;
        private final String configKey;
        private final Presenter presenter;
        private final IFindAndReplaceDialog dialog;
        
        public Controller(final IFindAndReplaceDialog dialog, final Presenter presenter, final ClientSettings settings, final String configPrefix, final boolean canReplace){
            this.settings = settings;
            this.presenter = presenter;
            this.dialog = dialog;
            if (configPrefix!=null && !configPrefix.isEmpty()){
                configKey = SettingNames.SYSTEM + "/" + configPrefix+"/"+getClass().getSimpleName();
            }
            else{
                configKey = SettingNames.SYSTEM + "/" +getClass().getSimpleName();
            }
            presenter.createUi(canReplace);
            presenter.loadState(settings, configKey, State.readFrom(settings, configKey));
        }
        
        public final void addFindActionListener(final IFindActionListener listener){
            if (listener!=null){
                if (findListeners==null)
                    findListeners = new LinkedList<>();
                findListeners.add(listener);
            }
        }

        public final void removeFindActionListener(final IFindActionListener listener){
            if (listener!=null && findListeners!=null){
                findListeners.remove(listener);
            }
        }

        public final void notifyFindActionListeners(){
            if (findListeners!=null){
                for (IFindActionListener listener: findListeners){
                    listener.find();
                }
            }
        }

        public final void addReplaceActionListener(final IReplaceActionListener listener){
            if (listener!=null){
                if (replaceListeners==null)
                    replaceListeners = new LinkedList<>();
                replaceListeners.add(listener);
            }
        }

        public final void removeReplaceActionListener(final IReplaceActionListener listener){
            if (listener!=null && replaceListeners!=null){
                replaceListeners.remove(listener);
            }
        }

        public final void notifyReplaceActionListeners(){
            if (replaceListeners!=null){
                for (IReplaceActionListener listener: replaceListeners){
                    listener.replace(false);
                }
            }
        }    

        public final void notifyReplaceAllActionListeners(){
            if (replaceListeners!=null){
                for (IReplaceActionListener listener: replaceListeners){
                    listener.replace(true);
                }
            }
        }
        
        public final boolean match(final String text){
            final String findText = dialog.getFindWhat();
            if (findText.isEmpty() || text==null)
                return false;
            if (dialog.isMatchCaseChecked() && dialog.isWholeWordChecked()) {
                return text.equals(findText);
            }
            if (dialog.isMatchCaseChecked() && !dialog.isWholeWordChecked()) {
                return text.contains(findText);
            }
            if (!dialog.isMatchCaseChecked() && dialog.isWholeWordChecked()) {
                return text.toLowerCase().equals(findText.toLowerCase());
            }
            return text.toLowerCase().contains(findText.toLowerCase());
        }
        
        public final void beforeClose(){
            presenter.saveState(settings, configKey);
        }
    }
    
    void addFindActionListener(IFindActionListener listener);
    
    void removeFindActionListener(IFindActionListener listener);
    
    void addReplaceActionListener(IReplaceActionListener listener);
    
    void removeReplaceActionListener(IReplaceActionListener listener);
        
    void addSearchParameter(String label, IWidget widget);        
    
    void setSearchWidget(ISearchWidget customWidget);
    
    boolean isMatchCaseChecked();

    boolean isWholeWordChecked();

    boolean isForwardChecked();

    String getFindWhat();

    String getReplaceWith();

    boolean match(String text);
}