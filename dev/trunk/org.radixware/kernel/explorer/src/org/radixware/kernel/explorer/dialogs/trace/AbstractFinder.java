/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.explorer.dialogs.trace;

import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;


abstract class AbstractFinder {
    
    private final IClientEnvironment environment;
    private final QWidget parentWidget;
    
    private String searchString;
    private boolean forward;
    private boolean caseSensitive;
    
    public AbstractFinder(final IClientEnvironment environment,final QWidget parent) {
        this.environment = environment;
        this.parentWidget = parent;
    }
    
    private boolean getSearchParams(){
        final SearchDialog dialog = new SearchDialog(environment, parentWidget);
        if (dialog.exec()==QDialog.DialogCode.Accepted.value()){
            searchString = dialog.getSearchString();
            forward = dialog.isForward();
            caseSensitive = dialog.isCaseSensitive();
            return true;
        }else{
            searchString = null;
            return false;
        }
    }
    
    public boolean find() {
        return getSearchParams() ? findNext() : false;
    }    
    
    public boolean findNext() {
        if ((searchString!=null && !searchString.isEmpty()) || getSearchParams()){
            return findNext(environment, searchString, forward, caseSensitive);
        }else{
            return false;
        }
    }
    
    protected final void showStringNotFoundMessage(){
        final MessageProvider messageProvider = environment.getMessageProvider();
        final String messageTitle = messageProvider.translate("TraceDialog", "Information");
        final String messageTextTemplate = 
                messageProvider.translate("TraceDialog", "Could not find string \"%s\"");
        environment.messageInformation(messageTitle, String.format(messageTextTemplate,searchString));        
    }
    
    protected abstract boolean findNext(final IClientEnvironment environment,
                                                     final String searchString, 
                                                     final boolean forward, 
                                                     final boolean caseSensitive);
}
