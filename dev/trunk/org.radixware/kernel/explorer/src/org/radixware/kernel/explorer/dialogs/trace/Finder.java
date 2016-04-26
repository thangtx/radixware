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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.explorer.dialogs.ExplorerMessageBox;
import org.radixware.kernel.explorer.env.Application;


public class Finder{

    private SearchDialog searchDialog = null;

    private final ITokenProvider tokenProvider;
    
    public Finder(ITokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }
    
    public void showDialog() {
        if (searchDialog == null) {
            searchDialog = new SearchDialog((QWidget)tokenProvider);
            searchDialog.find.connect(this, "find()");
        }
        searchDialog.show();
    }

    public void find() {
        String searchString = searchDialog==null ? null : searchDialog.getSearchString();
        if (searchString==null || searchString.isEmpty()){
            showDialog();
        }else{
            if (!searchDialog.isCaseSensetive()) 
                searchString = searchString.toLowerCase();
            if (searchDialog.isForward()) {
                while (tokenProvider.hasNextToken()) {
                    IToken token = tokenProvider.getNextToken();
                    String source = token.getValue();
                    if (!searchDialog.isCaseSensetive())
                        source = source.toLowerCase();
                    int pos = source.indexOf(searchString);
                    if (pos != -1) {
                        token.select();
                        return;
                    }
                }
            } else {
                while (tokenProvider.hasPrevToken()) {
                    IToken token = tokenProvider.getPrevToken();
                    String source = token.getValue();
                    if (!searchDialog.isCaseSensetive())
                        source = source.toLowerCase();
                    int pos = source.indexOf(searchString);
                    if (pos != -1) {
                        token.select();
                        return;
                    }
                }
            }
            QWidget parent;
            if (searchDialog.isVisible())
                parent = (QWidget)searchDialog;
            else
                parent = (QWidget)tokenProvider;
            final String message = Application.translate("TraceDialog", "Could not find string \"%s\"");
            ExplorerMessageBox.information(parent, Application.translate("TraceDialog", "Information"), String.format(message, searchString));
        }
    }
    
}
