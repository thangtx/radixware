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

package org.radixware.kernel.common.sqlscript.parser;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.sqlscript.parser.SQLParsePragmaStatement.SuppressItem;


class SQLPragmasOwner {
    
    private final List< List<SuppressItem> > stack = new ArrayList<>();
    
    protected void processStatement(final SQLParsePragmaStatement stat)  throws SQLScriptException {        
        if (stat.isSuppressPragma()) {
            stack.add(stat.suppressItems);
            
        }
        else if (stat.isUnsuppressPragma()) {
            if (stack.isEmpty()) {
                throw new SQLScriptException("Unable unsuppress pragma", stat.getPosition());                
            }
            final int size = stack.size();
            stack.remove(size-1);
        }
    }
    
    protected boolean canIgnore(final String serverErrorMessage) throws SQLScriptException {
        final String serverErrorMessageUpper = String.valueOf(serverErrorMessage).toUpperCase();
        
        for (int i=stack.size()-1; i>=0; --i) {
            final List<SuppressItem> items = stack.get(i);
            for (SuppressItem item : items) {
                if (canIgnore(item, serverErrorMessageUpper)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private static boolean canIgnore(final SuppressItem suppressItem, final String mess) throws SQLScriptException {
        if (suppressItem.customError != null) {
            return canIgnore(suppressItem.customError, mess);
        }
        else if (suppressItem.predefinedSuccess != null) {
            for (SQLParsePragmaStatementUtils.InnerError item : suppressItem.predefinedSuccess.innerErrors) {
                if (canIgnore(item, mess)) {
                    return true;
                }
            }
            return false;
        }
        else {
            throw new SQLScriptException("Internal pragma runtime error"); 
        }
    }
    
    private static boolean canIgnore(final SQLParsePragmaStatementUtils.InnerError item, final String mess) {
        final int MAX = 7;
        String zeros = "";
        final String codeAsStr = String.valueOf(item.postfix);
        
        for (int i=0; i<MAX; i++) {
            final String fullError = item.prefix.value + "-" + zeros + codeAsStr;
            if (mess.contains(fullError)) {
                return true;
            }
            zeros += "0";
        }
        return false;
    }

}
