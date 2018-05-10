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
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.StatementType;
import org.radixware.kernel.common.sqlscript.parser.SQLParsePragmaStatementUtils.InnerError;
import org.radixware.kernel.common.sqlscript.parser.SQLParsePragmaStatementUtils.InnerErrorPrefix;
import org.radixware.kernel.common.sqlscript.parser.SQLParsePragmaStatementUtils.PredefinedSuccess;
 
class SQLParsePragmaStatement extends SQLParseStatement {
    
    
    protected static class SuppressItem {
       
        protected final List<SQLToken> tokens = new ArrayList<>();
        protected PredefinedSuccess  predefinedSuccess = null;
        protected InnerError customError = null;
    }

    protected SQLParsePragmaStatement(final SQLPosition position) {
        super(position, StatementType.ST_PRAGMA);
    }

    private final List<SQLToken> tokens = new ArrayList<>();
    private EPragmaType pragmaType = null;
    
    protected List<SuppressItem> suppressItems = null;
    
    
    protected boolean isSuppressPragma() {
        return EPragmaType.Suppress.equals(pragmaType);
    }
    protected boolean isUnsuppressPragma() {
        return EPragmaType.Unsuppress.equals(pragmaType);
    }

    protected void appendToken(final SQLToken token) {
        tokens.add(token);
    }

    protected void checkTokensCorrectness() throws SQLScriptException {
        check();
    }
    
    
    
    private void check() throws SQLScriptException {
        final SQLPosition position = getPosition();
        if (tokens.isEmpty()) {
            throw new SQLScriptException(collectMessage("found empty tokens list"), position);
        }
        if (tokens.isEmpty()) {
            final SQLToken firstToken = tokens.get(0);
            firstToken.checkType(SQLConstants.TokenType.TK_SCRIPT_PP_PRAGMA);
        }
        pragmaType = checkAndGetPragmaType(tokens, position);
        if (isSuppressPragma()) {
            checkSuppress(tokens, position);
        }
        else if (isUnsuppressPragma()) {
            checkUnsuppress(tokens, position);
        }
        else {
            //already checked
        }
    }
     
    private static enum EPragmaType {
        Suppress , Unsuppress
    }
    
    private static EPragmaType checkAndGetPragmaType(final List<SQLToken> tokens, final SQLPosition position) throws SQLScriptException {
        final String errorMessage = collectMessage("\'SUPPRESS\' or \'UNSUPPRESS\' keyword not found");
        if (tokens.size()<2) {
            throw new SQLScriptException(errorMessage, position);
        }
        final SQLToken namedToken = tokens.get(1);
        if (!SQLConstants.TokenType.TK_SCRIPT_NAME.equals(namedToken.getType())) {
            throw new SQLScriptException(errorMessage, position);
        }
        final String name = namedToken.getScriptName();
        if ("SUPPRESS".equals(name)) {
            return EPragmaType.Suppress;
        }
        if ("UNSUPPRESS".equals(name)) {
            return EPragmaType.Unsuppress;
        }
        throw new SQLScriptException(errorMessage, position);
    }
    
    private static String collectMessage(final String mess) {
        return "Pragma parse error - " + mess + ".";
    }
    

    private static void checkUnsuppress(final List<SQLToken> tokens, final SQLPosition position) throws SQLScriptException {
        if (tokens.size() > 2) {
            throw new SQLScriptException(collectMessage("too many tokens after \'UNSUPPRESS\'"), position);
        }
    }
    
    private void checkSuppress(final List<SQLToken> tokens, final SQLPosition position) throws SQLScriptException {
        if (tokens.size() == 2) {
            throw new SQLScriptException(collectMessage("there are no arguments after \'SUPPRESS\'"), position);
        }
        final List<SuppressItem> suppressItems_ = collectSuppressItems(tokens);
        checkSuppressItems(suppressItems_, position);
        this.suppressItems = suppressItems_;
    }
    
    private static void checkSuppressItems(final List<SuppressItem> suppressItems, final SQLPosition position) throws SQLScriptException {
        for (SuppressItem suppressItem : suppressItems) {
            checkSuppressItem(suppressItem, position);
        }
    }
    private static void checkSuppressItem(final SuppressItem suppressItem, final SQLPosition position) throws SQLScriptException {
        final String errorMessage = collectMessage("invalid arguments after \'SUPPRESS\'");
        if (suppressItem.tokens.size() == 1) {            
            final SQLToken token = suppressItem.tokens.get(0);
            if (!SQLConstants.TokenType.TK_SCRIPT_NAME.equals(token.getType())) {
                throw new SQLScriptException(errorMessage, position);
            }
            final String value = token.getScriptName();
            final SQLParsePragmaStatementUtils.PredefinedSuccess  predefinedSuccess = SQLParsePragmaStatementUtils.getCorrectPredefinedSuccessPragma(value);
            if (predefinedSuccess == null) {
                throw new SQLScriptException(errorMessage, position);
            }
            suppressItem.predefinedSuccess = predefinedSuccess;
        }
        else if (suppressItem.tokens.size() == 3) {
            final SQLToken token0 = suppressItem.tokens.get(0);
            final SQLToken token1 = suppressItem.tokens.get(1);
            final SQLToken token2 = suppressItem.tokens.get(2);
            
            if (!SQLConstants.TokenType.TK_SCRIPT_NAME.equals(token0.getType())
               || !SQLConstants.TokenType.TK_SCRIPT_MINUS.equals(token1.getType())
               || !SQLConstants.TokenType.TK_SCRIPT_VALUE.equals(token2.getType())
                    ) {
                throw new SQLScriptException(errorMessage, position);
            }
            String value0 = token0.getScriptName();
            if (value0!=null) {
                value0 = value0.toUpperCase();
            }
            
            final boolean isOra = SQLParsePragmaStatementUtils.ORA_PREFIX.value.equals(value0);
            final boolean isPg = SQLParsePragmaStatementUtils.PG_PREFIX.value.equals(value0);
            
            if (!isOra && !isPg) {
                throw new SQLScriptException(errorMessage, position);
            }
            final int errorCode = checkAndGetIntegerValue(token2);            
            final InnerErrorPrefix prefix = isOra ? SQLParsePragmaStatementUtils.ORA_PREFIX : SQLParsePragmaStatementUtils.PG_PREFIX;            
            
            suppressItem.customError = new  InnerError(prefix, errorCode);
             
            final SQLToken token3 = suppressItem.tokens.get(2);
        }
        else {
            throw new SQLScriptException(errorMessage, position);
        }
    }
    
    private static int checkAndGetIntegerValue(final SQLToken token2) throws SQLScriptException {
        final SQLScriptValue value = token2.getScriptValue();
        int rez = value.getInt();
        return rez;
    }
    
    private static List<SuppressItem> collectSuppressItems(final List<SQLToken> tokens) {
        final List<SuppressItem> rez = new ArrayList<>();
        
        {
        final SuppressItem first = new SuppressItem();
        rez.add(first);
        }
        
        for (int i=2; i<tokens.size(); ++i) {            
            final SQLToken token = tokens.get(i);
            final SQLConstants.TokenType tokenType = token.getType();
            final boolean isComma = SQLConstants.TokenType.TK_SCRIPT_COMMA.equals(tokenType);
            if (isComma) {
                final SuppressItem item = new SuppressItem();
                rez.add(item);
                continue;
            }
            
            final SuppressItem list = rez.get(rez.size()-1);
            list.tokens.add(token);
            
        }
        
        
        return rez;
    }
    

}
