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
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.TokenType;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLScriptFunctionHandler;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;


public class SQLPreprocessor {
    
    public static enum  EPreprocessorType {PRAGMA, OTHER}

    public static enum PreprocessBehavior {
        PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT,
        PT_REMOVE_UNUSED_BLOCKS
    };

    private static final String START_COMMENT = "/*";
    private static final String END_COMMENT = "*/";
    
    final String script;
    protected final Map<String, SQLScriptFunctionHandler> functionsHandlers = new HashMap<>();
    protected final Reader reader;
    protected final String fileName;
    protected final VariablesProvider variablesProvider;

    protected enum SkipState {
        NO, THIS, STATEMENT
    };

    protected enum BlockType {
        SCRIPT, WHILE, IF, ELSE
    };

    private enum Lig {
        OR, AND
    };
    public static final String FUNC_IS_DEFINED = "isDefined";
    public static final String FUNC_IS_ENABLED = "isEnabled";

    void PP_MISPLACED_EXCEPTION(String word, SQLPosition pos) throws SQLScriptException {
        throw new SQLScriptException("Misplaced \"" + word + "\" in SQL preprocessor statement" + " : " + pos.toString());
    }

    public SQLPreprocessor(final String script, final String pFileName, final VariablesProvider variablesProvider) {
        if (script == null) {
            throw new IllegalArgumentException("Script content can't be null");
        }
        else if (variablesProvider == null) {
            throw new IllegalArgumentException("Variables provider can't be null");
        }
        else {
            this.script = script;
            this.reader = new StringReader(script);
            this.fileName = pFileName;
            this.variablesProvider = variablesProvider;
            registerFunction(FUNC_IS_DEFINED, new SQLScriptFunctionHandler() {
                @Override
                public SQLScriptValue scriptFunction(Vector<SQLScriptValue> args) throws SQLScriptException {
                    if (args == null || args.size() != 1) {
                        throw new SQLScriptException("Wrong arguments count, passed to SQL script function " + FUNC_IS_DEFINED);
                    }
                    else {
                        final SQLScriptValue val = args.get(0);
                        return new SQLScriptValue(val != null && (variablesProvider.getVariable(val.getString().toUpperCase()) != null || variablesProvider.getVariable(val.getString()) != null));
                    }
                }
            });
            registerFunction(FUNC_IS_ENABLED, new SQLScriptFunctionHandler() {
                @Override
                public SQLScriptValue scriptFunction(Vector<SQLScriptValue> args) throws SQLScriptException {
                    if (args == null || args.size() != 1) {
                        throw new SQLScriptException("Wrong arguments count, passed to SQL script function " + FUNC_IS_ENABLED);
                    }
                    else {
                        final SQLScriptValue val = args.get(0);
                        SQLScriptValue varValue;
                        
                        if (val != null) {
                            if ((varValue = variablesProvider.getVariable(val.getString().toUpperCase())) == null) {
                                varValue = variablesProvider.getVariable(val.getString());
                            }
                            return new SQLScriptValue(varValue == null ? false : EOptionMode.ENABLED.getValue().equals(varValue.getString()));
                        }
                        else {
                            return new SQLScriptValue(false);
                        }
                    }
                }
            });
        }
    }

    public final void registerFunction(String funcName, SQLScriptFunctionHandler handler) {
        functionsHandlers.put(funcName.toUpperCase(), handler);
    }

    private enum CommentType {
        CT_IF_TRUE, CT_IF_FALSE, CT_ELSE, CT_ELSE_TRUE, CT_ELSE_FALSE, CT_ENDIF
    };

    static class CommentItem {
        SQLPosition start;
        SQLPosition finish;
    }

    static class CommentItemWithBoolean extends CommentItem {
        boolean mustCheck;
        int pos;

        public CommentItemWithBoolean(SQLPosition start, SQLPosition finish, boolean mustCheck) {
            this.mustCheck = mustCheck;
            this.start = start;
            this.finish = finish;
        }

        @Override
        public String toString() {
            return "CommentItem{" + "start=" + start + ", finish=" + finish + ", mustCheck=" + mustCheck + '}';
        }
    }

    static class CommentTypeItem extends CommentItem {
        CommentType type;

        public CommentTypeItem(SQLPosition start, SQLPosition finish, CommentType type) {
            this.start = start;
            this.finish = finish;
            this.type = type;
        }

        @Override
        public String toString() {
            return "CommentItem{" + "start=" + start + ", finish=" + finish + ", type=" + type + '}';
        }
    }
    private List<CommentTypeItem> commentList;
    private List<CommentTypeItem> stackCommentStack;
    private List<CommentItemWithBoolean> finalCommentList;

    private void pushComment(SQLPosition start, SQLPosition finish, CommentType type) throws SQLScriptException {
        CommentTypeItem item = new CommentTypeItem(start.fork(), finish.fork(), type);

        commentList.add(item);


        int index = stackCommentStack.size() - 1;

        if (!(item.type.equals(CommentType.CT_IF_TRUE) || item.type.equals(CommentType.CT_IF_FALSE))
                && index == -1) {
            throw new SQLScriptException("Incorrect script structure ", finish);
        }

        if (item.type.equals(CommentType.CT_IF_TRUE) || item.type.equals(CommentType.CT_IF_FALSE)) {
            stackCommentStack.add(new CommentTypeItem(start, finish, type));

            if (item.type.equals(CommentType.CT_IF_TRUE)) {
                if (commentLevel == 0) {
                    finalCommentList.add(new CommentItemWithBoolean(start, finish, false));
                }
            } else {
                commentLevel++;
            }

        } else if (item.type.equals(CommentType.CT_ELSE)) {
            CommentTypeItem priorItem = stackCommentStack.get(index);

            if (priorItem.type.equals(CommentType.CT_IF_TRUE)) {
                priorItem.type = CommentType.CT_ELSE_FALSE;
                priorItem.start = start.fork();
                priorItem.finish = finish.fork();
                commentLevel++;
            } else if (priorItem.type.equals(CommentType.CT_IF_FALSE)) {

                commentLevel--;

                if (commentLevel == 0) {
                    finalCommentList.add(new CommentItemWithBoolean(priorItem.start, finish, true));
                }

                priorItem.type = CommentType.CT_ELSE_TRUE;
                priorItem.start = start.fork();
                priorItem.finish = finish.fork();

            } else {
                throw new SQLScriptException("Incorrect script structure ", finish);
            }
        } else if (item.type.equals(CommentType.CT_ENDIF)) {
            CommentTypeItem priorItem = stackCommentStack.get(index);
            if (priorItem.type.equals(CommentType.CT_ELSE_TRUE)) {
                if (commentLevel == 0) {
                    finalCommentList.add(new CommentItemWithBoolean(start, finish, false));
                }
            } else if (priorItem.type.equals(CommentType.CT_ELSE_FALSE)) {
                commentLevel--;
                if (commentLevel == 0) {
                    //finalCommentList.add(new CommentItemWithBoolean(priorItem.start, finish, false));
                    finalCommentList.add(new CommentItemWithBoolean(priorItem.start, finish, true));//RADIXMANAGER-332
                }
            } else if (priorItem.type.equals(CommentType.CT_IF_TRUE)) {
                if (commentLevel == 0) {
                    finalCommentList.add(new CommentItemWithBoolean(start, finish, false));
                }
            } else if (priorItem.type.equals(CommentType.CT_IF_FALSE)) {
                commentLevel--;
                finalCommentList.add(new CommentItemWithBoolean(priorItem.start, finish, true));
            }
            stackCommentStack.remove(index);
        }
    }
    private int commentLevel;

    public String preprocess() throws SQLScriptException, IOException {
        return preprocess(PreprocessBehavior.PT_REMOVE_UNUSED_BLOCKS, null);
    }

    public String preprocess(final PreprocessBehavior preprocessType, final String supersedingSysUser) throws SQLScriptException, IOException {
        SQLParser parser = new SQLParser(reader, fileName, variablesProvider, true);
        SQLAdditionParserOptions additionParserOptions = SQLAdditionParserOptions.Factory.create(supersedingSysUser);
        additionParserOptions.hidePassword = true;
        parser.setAdditionOptions(additionParserOptions);

        commentLevel = 0;
        commentList = new ArrayList<>();
        stackCommentStack = new ArrayList<>();
        finalCommentList = new ArrayList<>();

        preprocessBlock(parser, BlockType.SCRIPT, SkipState.NO);

        //System.err.println("List: "+finalCommentList);
        if (finalCommentList.isEmpty()) {
            return script;
        }
        else {
            reduceCommentList(finalCommentList);    // Bug fix: nested comments can't be processed linearly!
            //System.err.println("Rediced List: "+finalCommentList);
            final StringBuilder sb = new StringBuilder(script.length() + finalCommentList.size() * 4);
            int lastIndex = 1;

            for (CommentItemWithBoolean item : finalCommentList) {
                sb.append(script.substring(lastIndex - 1, item.start.getIndex() - 1));
                if (PreprocessBehavior.PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT.equals(preprocessType)){
                    sb.append(START_COMMENT);
                    if (item.mustCheck) {
                        sb.append(crashComments(script.substring(item.start.getIndex() - 1, item.finish.getIndex())));
                    } else {
                        sb.append(script.substring(item.start.getIndex() - 1, item.finish.getIndex()));
                    }

                    sb.append(END_COMMENT);
                }
                lastIndex = item.finish.getIndex() + 1;
            }
            sb.append(script.substring(lastIndex - 1));
            return sb.toString();
        }
    }

    static void reduceCommentList(final List<CommentItemWithBoolean> list2Reduce) {
        // Reduce nested comments from the list
        final CommentItemWithBoolean[] list = list2Reduce.toArray(new CommentItemWithBoolean[list2Reduce.size()]);
    
        Arrays.sort(list,new Comparator<CommentItemWithBoolean>(){
            @Override
            public int compare(CommentItemWithBoolean o1, CommentItemWithBoolean o2) {
                return o1.start.getIndex() - o2.start.getIndex();
            }
        });
        for (int first = 0; first < list.length - 1; first++) {
            for (int second = first+1; second < list.length; second++) {
                if (list[first].start.getIndex() != -1 && list[first].start.getIndex() < list[second].start.getIndex() && list[first].finish.getIndex() > list[second].finish.getIndex()) {
                    list[second].start = new SQLPosition(-1,-1,-1,-1,null);
                }
            }
        }
        list2Reduce.clear();
        for (CommentItemWithBoolean item : list) {
            if (item.start.getIndex() != -1){
                list2Reduce.add(item);
            }
        }
    }
    
    private static String crashComments(String text) {//stupid crash
        return text.replace(START_COMMENT,"??").replace(END_COMMENT,"??");
    }

    private String preprocessBlock(SQLParser parser, BlockType blockType, SkipState skipState) throws SQLScriptException {
        StringBuilder block = new StringBuilder();
        SQLParseStatement st;// = null;
        for (;;) {
            st = parser.getPPStatement(skipState == SkipState.NO);
            if (st == null) {
                if (blockType != BlockType.SCRIPT) {
                    throw new SQLScriptException("Unexpected EOF in SQL file : SQL preprocessor statement is not complete", parser.getPosition());
                }
                break;
            }
            switch (st.getType()) {
                case ST_SCRIPT: {
                    SQLScriptStatement stat = (SQLScriptStatement) st;
                    if (stat.getTokens().isEmpty()) {
                        continue;
                    }


                    SQLToken token = stat.nextToken();
                    SQLPosition firstPos = token.getPosition().fork();
                    firstPos.setColumn(firstPos.getColumn() - 1);


                    TokenType tokenType = token.getType();



                    {
                        if (TokenType.TK_SCRIPT_ELSE.equals(tokenType)) {
                            pushComment(firstPos, parser.getPosition().fork(), CommentType.CT_ELSE);
                        } else if (TokenType.TK_SCRIPT_ENDIF.equals(tokenType)) {
                            pushComment(firstPos, parser.getPosition().fork(), CommentType.CT_ENDIF);
                        }
                    }


                    switch (tokenType) {
                        case TK_SCRIPT_IF: {
                            if (skipState == SkipState.NO) {
                                boolean condition = checkCondition(stat, false);
                                stat.nextToken().checkType(TokenType.TK_SCRIPT_THEN);

                                pushComment(firstPos, parser.getPosition().fork(), condition ? CommentType.CT_IF_TRUE : CommentType.CT_IF_FALSE);


                                String body = preprocessBlock(parser, BlockType.IF, condition ? SkipState.NO : SkipState.THIS);
                                block.append(body);
                            } else {

                                boolean condition = checkCondition(stat, false);
                                stat.nextToken().checkType(TokenType.TK_SCRIPT_THEN);

                                pushComment(firstPos, parser.getPosition().fork(), condition ? CommentType.CT_IF_TRUE : CommentType.CT_IF_FALSE);

                                preprocessBlock(parser, BlockType.IF, SkipState.STATEMENT);
                            }
                            break;
                        }
                        case TK_SCRIPT_ELSE: {
                            if (blockType != BlockType.IF) {
                                PP_MISPLACED_EXCEPTION("else", st.getPosition());
                            }
                            if (stat.hasMoreTokens()) {
                                stat.nextToken().checkType(TokenType.TK_SCRIPT_IF);
                                if (skipState == SkipState.THIS) {
                                    boolean condition = checkCondition(stat, false);
                                    skipState = condition ? SkipState.NO : SkipState.THIS;
                                    stat.nextToken().checkType(TokenType.TK_SCRIPT_THEN);

                                    pushComment(firstPos, parser.getPosition().fork(), condition ? CommentType.CT_IF_TRUE : CommentType.CT_IF_FALSE);
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
                                PP_MISPLACED_EXCEPTION("endif", st.getPosition());
                            }
                            return block.toString();
                        }
                        case TK_SCRIPT_PP_PRAGMA: {
                            return block.toString();
                        }                        
                        default: {
                            if (skipState == SkipState.NO) {
                                stat.setIndex(stat.getIndex() - 1);
                                getRightValue(stat, false);
                                if (stat.hasMoreTokens()) {
                                    throw new SQLScriptException("Invalid token in SQL preprocessor statement", stat.nextToken().getPosition());
                                }
                            }
                            break;
                        }
                    }
                    break;
                }
                case ST_TEXT: {
                    if (skipState == SkipState.NO) {
                        SQLTextStatement stat = (SQLTextStatement) st;
                        block.append("--" + SQLConstants.LINE_INFO).append(Integer.toString(stat.getPosition().getLine())).append("\n");
                        block.append(stat.getText());

                    } else if (skipState == SkipState.THIS) {
                        SQLTextStatement stat = (SQLTextStatement) st;
                        block.append(stat.getText());
                    }
                    break;
                }
                default:
                    throw new SQLScriptException("Invalid preprocessor statement", st.getPosition());
            }
        }

        return block.toString();
    }

    protected boolean checkCondition(SQLScriptStatement stat, boolean skip) throws SQLScriptException {
        boolean result = false;
        Lig ligament = Lig.OR;
        for (;;) {
            boolean res = checkCompareExpression(stat, skip);
            if (ligament == Lig.OR) {
                result = result || res;
            } else {
                result = result && res;
            }
            if (stat.hasMoreTokens()) {
                SQLToken token = stat.nextToken();
                switch (token.getType()) {
                    case TK_SCRIPT_OR:
                        if (result) {
                            checkCondition(stat, true);
                            break;
                        }
                        ligament = Lig.OR;
                        continue;
                    case TK_SCRIPT_AND:
                        if (!result) {
                            checkCondition(stat, true);
                            break;
                        }
                        ligament = Lig.AND;
                        continue;
                    default:
                        stat.setIndex(stat.getIndex() - 1);
                        break;
                }
                return result;
            }
            break;//NOPMD
        }
        return result;
    }

    private boolean checkCompareExpression(SQLScriptStatement stat, boolean skip) throws SQLScriptException {
        SQLScriptValue left = getRightValue(stat, skip);
        if (!stat.seeToken().isOperation()) {
            return (!skip) ? left.getBoolean() : false;
        }
        SQLToken oper = stat.nextToken();
        SQLScriptValue right = getRightValue(stat, skip);
        if (skip) {
            return false;
        }
        try {
            switch (oper.getType()) {
                case TK_SCRIPT_EQUALS:
                    return left.compare(right) == 0;
                case TK_SCRIPT_NOT_EQUALS:
                    return left.compare(right) != 0;
                case TK_SCRIPT_LESS:
                    return left.compare(right) < 0;
                case TK_SCRIPT_MORE:
                    return left.compare(right) > 0;
                case TK_SCRIPT_LESS_OR_EQUALS:
                    return left.compare(right) <= 0;
                case TK_SCRIPT_MORE_OR_EQUALS:
                    return left.compare(right) >= 0;
            }
        } catch (Exception ex) {
            throw new SQLScriptException("Error of the SQL script values comparing : " + ex.toString(), oper.getPosition());
        }
        throw new SQLScriptException("Invalid SQL script statement", stat.getPosition());
    }

    protected SQLScriptValue getRightValue(SQLScriptStatement stat, boolean skip) throws SQLScriptException {
        SQLToken token = stat.nextToken();
        SQLScriptValue value = new SQLScriptValue();
        if (token.getType() == TokenType.TK_SCRIPT_EXCLAMATION) {
            return getRightValue(stat, skip).negation();
        } else if (token.getType() == TokenType.TK_SCRIPT_VALUE) {
            if (!skip) {
                value = token.getScriptValue();
            }
        } else if (token.getType() == TokenType.TK_SCRIPT_NAME) {
            if (stat.hasMoreTokens() && stat.seeToken().getType() == TokenType.TK_SCRIPT_LEFT_BRACKET) {
                stat.nextToken();
                Vector<SQLScriptValue> args = new Vector<SQLScriptValue>();
                if (stat.seeToken().getType() != TokenType.TK_SCRIPT_RIGHT_BRACKET) {
                    for (;;) {
                        args.add(getRightValue(stat, skip));
                        if (stat.seeToken().getType() != TokenType.TK_SCRIPT_COMMA) {
                            break;
                        }
                        stat.nextToken();
                    }
                }
                stat.nextToken().checkType(TokenType.TK_SCRIPT_RIGHT_BRACKET);
                if (!skip) {
                    SQLScriptFunctionHandler handler = functionsHandlers.get(token.getScriptName());
                    if (handler == null) {
                        throw new SQLScriptException("SQL script function \"" + token.getScriptName() + "\" has not been defined", token.getPosition());
                    }
                    try {
                        value = handler.scriptFunction(args);
                    } catch (Exception ex) {
                        throw new SQLScriptException("Error of the SQL script function executing \"" + token.getScriptName()
                                + "\" : " + ex.toString(), token.getPosition());
                    }
                }
            } else if (stat.hasMoreTokens() && stat.seeToken().getType() == TokenType.TK_SCRIPT_ASSIGN) {
                stat.nextToken();
                if (stat.hasMoreTokens()) {
                    if (!skip) {
                        value = getRightValue(stat, false);
                        variablesProvider.putVariable(token.getScriptName(), value);
                    } else {
                        getRightValue(stat, true);
                    }
                } else if (!skip) {
                    variablesProvider.removeVariable(token.getScriptName());
                }
            } else if (!skip) {
                value = variablesProvider.getVariable(token.getScriptName());
                if (value == null) {
                    throw new SQLScriptException("The SQL script variable \"" + token.getScriptName() + "\" has not been defined", token.getPosition());
                }
            }
        } else {
            throw new SQLScriptException("Invalid token in the SQL script statement", token.getPosition());
        }
        if (stat.hasMoreTokens()) {
            SQLToken oper = stat.nextToken();
            switch (oper.getType()) {
                case TK_SCRIPT_PLUS:
                case TK_SCRIPT_MINUS: {
                    SQLScriptValue rvalue = getRightValue(stat, skip);
                    if (!skip) {
                        try {
                            switch (oper.getType()) {
                                case TK_SCRIPT_PLUS:
                                    value = value.operatorAdd(rvalue);
                                    break;
                                case TK_SCRIPT_MINUS:
                                    value = value.operatorSub(rvalue);
                                    break;
                            }
                        } catch (Exception ex) {
                            throw new SQLScriptException("Error of the arithmetical operation on the SQL script values : "
                                    + ex.toString(), oper.getPosition());
                        }
                    }
                    break;
                }
                default:
                    stat.setIndex(stat.getIndex() - 1);
            }
        }
        return value;
    }
}
