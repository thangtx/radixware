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

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CancellationException;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.sqlscript.connection.SQLConnection;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.StatementType;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.TokenType;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLDialogHandler;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLMonitor;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;


public class SQLScript extends SQLPreprocessor {

    final private String dbUrl;
    final private SQLDialogHandler dialogHandler;
    final private SQLMonitor monitor;
    private final List<String> errorsList;
    private final SQLPosition errorPosition;
    private List<Integer> errorsIgnoreList;
    private String specifiedUser;
    private String defaultUser;
    private int loops;
    final private SQLConnection con;
        
    
    private boolean viewOnlyMode = false;
    
    public SQLScript(SQLConnection con, String dbUrl, String script, String fileName,
            VariablesProvider variables, SQLDialogHandler dialogHandler, SQLMonitor monitor) {
        super(preformatScript(script), fileName, variables);
        this.con = con;
        this.dbUrl = dbUrl;
        this.dialogHandler = dialogHandler;
        this.monitor = monitor;
        errorsList = new LinkedList<>();
        errorPosition = new SQLPosition();
        this.loops = 0;
    }

    public boolean isViewOnlyMode() {
        return viewOnlyMode;
    }

    public void setViewOnlyMode(final boolean viewOnlyMode) {
        this.viewOnlyMode = viewOnlyMode;
    }


    public List<String> getErrorsList() {
        return errorsList;
    }

    public SQLPosition getErrorPosition() {
        return errorPosition;
    }

    public void setErrorsIgnoreList(final List<Integer> errorsIgnoreList) {
        this.errorsIgnoreList = errorsIgnoreList;
    }

    public void setDefaultUser(final String defaultUser) {
        this.defaultUser = defaultUser;
    }

    public void execute(final ICancellable isCancelled) throws SQLScriptException, IOException {
        execute(isCancelled, null) ;
    }
    
    public void execute(final ICancellable isCancelled, final SQLAdditionParserOptions  additionParserOptions) throws SQLScriptException, IOException {        
        final SQLParser parser = new SQLParser(reader, fileName, variablesProvider, false);
        if (additionParserOptions!=null){
            parser.setAdditionOptions(additionParserOptions);
        }
        processBlock(parser, BlockType.SCRIPT, SkipState.NO, isCancelled);
    }
    
    //

    private static String preformatScript(final String s) {
        StringBuilder sb = new StringBuilder();

        Scanner scanner = new Scanner(s);
        while (scanner.hasNextLine()) {
            String sVal = scanner.nextLine();
            while (true) {
                int index = sVal.indexOf("&sysdate:");
                if (index == -1) {
                    break;
                }
                int index2 = sVal.indexOf("&", index + 1);
                if (index2 == -1) {
                    break;
                }
                String sFormat = sVal.substring(index + "&sysdate:".length(), index2);
                String s2 = sVal.substring(index, index2 + 1);
                SimpleDateFormat formatter = new SimpleDateFormat(sFormat);
                String newVal = formatter.format(new Date());
                sVal = sVal.replaceAll(s2, newVal);
            }
            sb.append(sVal);
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean processBlock(final SQLParser parser, final BlockType blockType, final SkipState skipState, final ICancellable isCancelled) throws SQLScriptException, IOException {
        return processBlock(0, parser, blockType, skipState, isCancelled);
        
    }
    
    private boolean processBlock(final int deepLevel, SQLParser parser, BlockType blockType, SkipState skipState, ICancellable isCancelled) throws SQLScriptException, IOException {
        boolean breaked = false;
        SQLParseStatement st;
        for (;;) {
            if (isCancelled!=null && isCancelled.wasCancelled()) {//cancelled while executing statement
                throw new CancellationException();
            }
            SQLPosition oldPos = new SQLPosition(parser.getPosition());
            st = parser.getStatement(skipState == SkipState.NO);
            if (st == null) {
                if (blockType != BlockType.SCRIPT) {
                    throw new SQLScriptException("Unexpected EOF in Ora script : Ora script statement is not complete", parser.getPosition());
                }
                return true;
            }
            if (monitor!=null){
                monitor.beforeStatement(st);
                monitor.getPauseObject().waitForNextStep();
            }
            if (isCancelled!=null && isCancelled.wasCancelled()) {//cancelled while waiting for next step
                throw new CancellationException();
            }
            
            if (parser.getAdditionOptions()!=null && parser.getAdditionOptions().getAfterProcessBlock()!=null){                    
                parser.getAdditionOptions().getAfterProcessBlock().afterProcess(st, deepLevel, parser);
            }
            
            if (st.getType() == StatementType.ST_SCRIPT) {
                SQLScriptStatement stat = (SQLScriptStatement) st;
                if (stat.getTokens().isEmpty()) {
                    continue;
                }
                SQLToken token = stat.nextToken();
                switch (token.getType()) {
                    case TK_SCRIPT_WHILE: {
                        loops++;
                        if (skipState == SkipState.NO) {
                            for (;;) {
                                if (!checkCondition(stat, false)) {
                                    processBlock(deepLevel + 1, parser, BlockType.WHILE, SkipState.THIS, isCancelled);
                                    break;
                                } else {
                                    if (stat.hasMoreTokens()) {
                                        throw new SQLScriptException("Invalid token in Ora script statement", stat.nextToken().getPosition());
                                    }
                                    SQLPosition block_start = parser.getPosition().fork();
                                    if (!processBlock(deepLevel + 1, parser, BlockType.WHILE, SkipState.NO, isCancelled)) {
                                        break;
                                    }
                                    parser.setPosition(block_start);
                                    stat.setIndex(1);
                                }
                            }
                        } else {
                            processBlock(deepLevel + 1, parser, BlockType.WHILE, SkipState.THIS, isCancelled);
                        }
                        loops--;
                        break;
                    }
                    case TK_SCRIPT_ENDWHILE: {
                        if (blockType != BlockType.WHILE) {
                            throw new SQLScriptException("Misplaced \"endwhile\" in SQL script statement", st.getPosition());
                        }
                        return !breaked;
                    }
                    case TK_SCRIPT_BREAK: {
                        if (skipState == SkipState.NO) {
                            if (loops == 0) {
                                throw new SQLScriptException("Misplaced \"break\" in SQL script statement", st.getPosition());
                            }
                            breaked = true;
                        }
                        break;
                    }
                    case TK_SCRIPT_IF: {
                        if (skipState == SkipState.NO) {
                            boolean condition = checkCondition(stat, false);
                            stat.nextToken().checkType(TokenType.TK_SCRIPT_THEN);
                            breaked = !processBlock(deepLevel + 1, parser, BlockType.IF, condition ? SkipState.NO : SkipState.THIS, isCancelled);
                        } else {
                            breaked = !processBlock(deepLevel + 1, parser, BlockType.IF, SkipState.STATEMENT, isCancelled);
                        }
                        break;
                    }
                    case TK_SCRIPT_ELSE: {
                        if (blockType != BlockType.IF) {
                            throw new SQLScriptException("Misplaced \"else\" in SQL script statement", st.getPosition());
                        }
                        if (stat.hasMoreTokens()) {
                            stat.nextToken().checkType(TokenType.TK_SCRIPT_IF);
                            if (skipState == SkipState.THIS) {
                                skipState = checkCondition(stat, false) ? SkipState.NO : SkipState.THIS;
                                stat.nextToken().checkType(TokenType.TK_SCRIPT_THEN);
                            } else {
                                skipState = SkipState.STATEMENT;
                            }
                        } else {
                            blockType = BlockType.ELSE;
                            if (skipState == SkipState.THIS) {
                                skipState = SkipState.NO;
                            } else {
                                skipState = SkipState.STATEMENT;
                            }
                        }
                        break;
                    }
                    case TK_SCRIPT_ENDIF: {
                        if (blockType != BlockType.IF && blockType != BlockType.ELSE) {
                            throw new SQLScriptException("Misplaced \"endif\" in SQL script statement", st.getPosition());
                        }
                        return !breaked;
                    }
                    default: {
                        if (skipState == SkipState.NO) {
                            stat.setIndex(stat.getIndex() - 1);
                            getRightValue(stat, false);
                            if (stat.hasMoreTokens()) {
                                throw new SQLScriptException("Invalid token in Ora script statement", stat.nextToken().getPosition());
                            }
                        }
                    }
                }
                if (breaked) {
                    skipState = SkipState.THIS;
                }
            } else if (skipState == SkipState.NO) {
                switch (st.getType()) {
                    case ST_PROMPT: {
                        SQLParsePromptStatement stat = (SQLParsePromptStatement) st;
                        dialogHandler.printMessage(stat.getPrompt());
                        break;
                    }
                    case ST_ACCEPT: {
                        SQLParseAcceptStatement stat = (SQLParseAcceptStatement) st;
                        SQLScriptValue val = dialogHandler.getScriptValue(stat.getVarType(), stat.getDefaultValue(), stat.getPrompt(), stat.isHide());
                        variablesProvider.putVariable(stat.getVar(), val);
                        break;
                    }
                    case ST_DEFINE: {
                        SQLParseDefinetStatement stat = (SQLParseDefinetStatement) st;
                        variablesProvider.putVariable(stat.getVar(), new SQLScriptValue(stat.getValue()));
                        break;
                    }
                    case ST_CONNECT: {
                        SQLParseConnectStatement stat = (SQLParseConnectStatement) st;
                        if (stat.getBaseAlias() != null) {
                            throw new SQLScriptException("Base alias in connect string is not permitted", stat.getPosition());
                        }
                        try {
                            if (!isViewOnlyMode()){
                                if (stat.getPassword() != null) {
                                    con.login(dbUrl, stat.getUser(), stat.getPassword());
                                } else {
                                    con.login(dbUrl, stat.getUser(), dialogHandler);
                                }
                            }
                        } catch (SQLException ex) {
                            throw new SQLScriptException(ex, stat.getPosition());
                        }
                        if (!isViewOnlyMode()){
                            specifiedUser = con.getUser();
                        }
                        else{
                            specifiedUser = "TESTUSER";
                        }
                        
                        break;
                    }
                    case ST_DISCONNECT: {
                        try {
                            if (!isViewOnlyMode()){
                                con.logoff();
                            }
                        } catch (SQLException ex) {
                            throw new SQLScriptException(ex, st.getPosition());
                        }
                        specifiedUser = "";
                        break;
                    }
                    case ST_COMMAND: {
                        try {
                            if (specifiedUser == null && defaultUser != null) {
                                if (!isViewOnlyMode()){
                                    con.login(dbUrl, defaultUser, dialogHandler);
                                }
                            }
                        } catch (SQLException ex) {
                            throw new SQLScriptException(ex, st.getPosition());
                        }
                        SQLParseCommandStatement stat = (SQLParseCommandStatement) st;
                        boolean retry;
                        do {
                            retry = false;
                            try {
                                if (monitor != null) {
                                    monitor.setCurrentLine(stat.getPosition().getLine());
                                }
                                if (!isViewOnlyMode()){
                                    con.execute(stat.getCommand());
                                }
                            } catch (SQLException ex) {
                                parser.setPosition(oldPos.fork());
                                stat = (SQLParseCommandStatement) parser.getStatement(skipState == SkipState.NO);
                                if (stat == null) {//RADIX-6267
                                    if (st instanceof SQLParseCommandStatement) {
                                        stat = (SQLParseCommandStatement) st;
                                    }
                                }

                                String msg = "Error of the command executing : \"" + (stat != null && stat.getCommand() != null ? stat.getCommand() : "") + "\" : " + ex.toString();
                                if (stat != null && (stat.isIgnoreSQLErrors() || isErrorInIgnoreList(ex.getErrorCode()))) {
                                    try {
                                        if (!isViewOnlyMode()){
                                            con.dbmsOut("Warning : " + ex.toString() + " : " + stat.getPosition().toStringLC() + " ");
                                        }
                                    } catch (Exception e) {
                                    }
                                } else if (dialogHandler != null) {
                                    SQLDialogHandler.Action status = stat == null ? null : dialogHandler.handleError(msg + " : " + stat.getPosition().toStringLC());
                                    if (status == SQLDialogHandler.Action.RETRY) {
                                        retry = true;
                                    } else if (status != SQLDialogHandler.Action.IGNORE) {
                                        throw new SQLScriptException(ex, st.getPosition());
                                    } else {
                                        monitor.printErrors(Arrays.asList(new String[]{"IGNORED: " + ex.getMessage()}));
                                    }
                                } else {
                                    throw new SQLScriptException(msg, st.getPosition());
                                }
                            } catch (Exception ex) {
                                if (stat == null) {
                                    throw new SQLScriptException("Error of the command executing : \"" + ex.toString(), st.getPosition());
                                } else {
                                    throw new SQLScriptException("Error of the command executing : \"" + stat.getCommand() + "\" : " + ex.toString() + " : " + stat.getPosition().toStringLC(),
                                            st.getPosition());
                                }
                            } finally {
                                if (monitor!=null && monitor.wishToHandleDbmsOutput()) {
                                    final List<String> dbmsOutput = new ArrayList<>();
                                    try {
                                        if (!isViewOnlyMode()){
                                            con.getDbmsOutput(dbmsOutput);
                                        }
                                        monitor.printDbmsOutput(dbmsOutput);
                                    } catch (SQLException ex) {
                                        throw new SQLScriptException("Error while getting DBMS.OUTPUT: " + ex.getMessage());
                                    }
                                }
                            }
                            if (monitor!=null && monitor.wishToHandleErrors() && stat != null && stat.getNewObjectType().length() != 0) {
                                try {
                                    List<String> newErrors = new ArrayList<>();
                                    if (!isViewOnlyMode()){
                                        con.showErrors(stat.getNewObjectType().toUpperCase(), stat.getNewObjectName().toUpperCase(), newErrors, parser.getNewObjectLine(stat.getNewObjectName()), errorPosition);
                                    }
                                    monitor.printErrors(newErrors);
                                } catch (Exception ex) {
                                    throw new SQLScriptException("Error of the errors retrieving : " + ex.toString(), st.getPosition());
                                }
                            }
                        } while (retry);
                        break;
                    }
                    case ST_SHOW_ERRORS: {
                        try {
                            if (specifiedUser == null && defaultUser != null) {
                                if (!isViewOnlyMode()){
                                    con.login(dbUrl, defaultUser, dialogHandler);
                                }
                            }
                        } catch (SQLException ex) {
                            throw new SQLScriptException(ex, st.getPosition());
                        }
                        SQLParseShowErrorsStatement stat = (SQLParseShowErrorsStatement) st;
                        try {
                            List<String> newErrors = new ArrayList<>();
                            if (!isViewOnlyMode()){
                                con.showErrors(stat.getObjectType().toUpperCase(), stat.getObjectName().toUpperCase(), newErrors,                                        
                                    parser.getNewObjectLine(stat.getObjectName()), errorPosition);
                            }
                            monitor.printErrors(newErrors);
                            errorsList.addAll(newErrors);

                        } catch (Exception ex) {
                            throw new SQLScriptException("Error of the errors retrieving : " + ex.toString(), st.getPosition());
                        }
                        break;
                    }
                    case ST_INCLUDE: {
                        throw new SQLScriptException("Including of another Ora files is not supported now", st.getPosition());
                    }
                    default: {
                        throw new SQLScriptException("Invalid Ora statement");
                    }
                }
            }
        }
    }

    public boolean isErrorInIgnoreList(int errorCode) {
        if (errorsIgnoreList == null) {
            return false;
        }
        for (Integer i : errorsIgnoreList) {
            if (i == errorCode) {
                return true;
            }
        }
        return false;
    }
}
