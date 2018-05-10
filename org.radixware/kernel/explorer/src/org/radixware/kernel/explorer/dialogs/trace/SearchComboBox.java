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

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.exceptions.WrongFormatError;
import org.radixware.kernel.common.types.ArrStr;


final class SearchComboBox extends QComboBox {
    
    private final static int SEARCH_HISTORY_SIZE = 10;
    private final static String SEARCH_HISTORY_KEY = "search_history";

    private final IClientEnvironment environment;
    private final String settingsKey;
    
    public SearchComboBox(final QWidget parent, 
                                         final IClientEnvironment environment, 
                                         final String settingsPrefix) {
        super(parent);
        setObjectName("rx_searchbox");
        setEditable(true);
        settingsKey = settingsPrefix+"/"+SEARCH_HISTORY_KEY;
        this.environment = environment;
        loadSettings();
    }
    
    private void loadSettings(){        
        final String searchHistoryStr = environment.getConfigStore().readString(settingsKey);
        if (searchHistoryStr!=null && !searchHistoryStr.isEmpty()){
            final ArrStr searchHistory;
            try{
                searchHistory = ArrStr.fromValAsStr(searchHistoryStr);
            }catch(WrongFormatError error){
                final String message = environment.getMessageProvider().translate("TraceMessage", "Failed to restore search history for trace dialog");
                environment.getTracer().error(message, error);
                return;
            }            
            if (searchHistory!=null && !searchHistory.isEmpty()){
                final List<String> loadedItems = new LinkedList<>();
                for (String searchString: searchHistory){
                    if (!loadedItems.contains(searchString)){
                        loadedItems.add(searchString);
                        addItem(searchString);
                    }
                }
            }
        }
    }
    
    public void saveHistory(){
        final String currentString = currentText();        
        final int size = this.count();
        final ArrStr searchHistory = new ArrStr();
        for (int i = 0; i < size; i++) {
            if (!this.itemText(i).equals(currentString)) {
                searchHistory.add(this.itemText(i));
            }
        }
        if (currentString!=null && !currentString.isEmpty()){
            searchHistory.add(0, currentString);
        }
        for (int i=searchHistory.size()-1; i>SEARCH_HISTORY_SIZE-1; i--){
            searchHistory.remove(i);
        }
        if (searchHistory.isEmpty()){
            environment.getConfigStore().remove(settingsKey);
        }else{
            environment.getConfigStore().writeString(settingsKey, searchHistory.toString());
        }
    }
    
}
