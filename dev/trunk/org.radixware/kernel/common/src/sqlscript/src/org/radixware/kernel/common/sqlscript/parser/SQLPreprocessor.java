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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.radixware.kernel.common.enums.EOptionMode;
import org.radixware.kernel.common.sqlscript.parser.SQLConstants.TokenType;
import org.radixware.kernel.common.sqlscript.parser.spi.SQLScriptFunctionHandler;
import org.radixware.kernel.common.sqlscript.parser.spi.VariablesProvider;


public class SQLPreprocessor {

    protected final Reader reader;
    protected final String fileName;
    protected final Map<String, SQLScriptFunctionHandler> functionsHandlers;
    protected final VariablesProvider variablesProvider;

    protected enum SkipState {

        NO, THIS, STATEMENT
        //NO, THIS, STATEMENT
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
    final String script;

    public SQLPreprocessor(String script, String pFileName, final VariablesProvider variablesProvider) {
        this.script = script;
        reader = new StringReader(script);
        fileName = pFileName;
        this.variablesProvider = variablesProvider;
        functionsHandlers = new HashMap<>();
        registerFunction(FUNC_IS_DEFINED, new SQLScriptFunctionHandler() {
            @Override
            public SQLScriptValue scriptFunction(Vector<SQLScriptValue> args) throws SQLScriptException {
                if (args.size() != 1) {
                    throw new SQLScriptException("Wrong arguments count, passed to SQL script function " + FUNC_IS_DEFINED);
                }
                return new SQLScriptValue(variablesProvider.getVariable(args.get(0).getString().toUpperCase()) != null || variablesProvider.getVariable(args.get(0).getString()) != null );
            }
        });
        registerFunction(FUNC_IS_ENABLED, new SQLScriptFunctionHandler() {
            @Override
            public SQLScriptValue scriptFunction(Vector<SQLScriptValue> args) throws SQLScriptException {
                if (args.size() != 1) {
                    throw new SQLScriptException("Wrong arguments count, passed to SQL script function " + FUNC_IS_ENABLED);
                }
                SQLScriptValue varValue = variablesProvider.getVariable(args.get(0).getString().toUpperCase());
                if (varValue == null)
                    varValue = variablesProvider.getVariable(args.get(0).getString());
                return new SQLScriptValue(varValue == null ? false : EOptionMode.ENABLED.getValue().equals(varValue.getString()));
            }
        });
    }

    public final void registerFunction(String funcName, SQLScriptFunctionHandler handler) {
        functionsHandlers.put(funcName.toUpperCase(), handler);
    }

    private enum CommentType {

        CT_IF_TRUE, CT_IF_FALSE, CT_ELSE, CT_ELSE_TRUE, CT_ELSE_FALSE, CT_ENDIF
    };

    private static class CommentItem {

        SQLPosition start;
        SQLPosition finish;
    }

    private static class CommentItemWithBoolean extends CommentItem {

        boolean mustCheck;
        int pos;

        public CommentItemWithBoolean(SQLPosition start, SQLPosition finish, boolean mustCheck) {
            this.mustCheck = mustCheck;
            this.start = start;
            this.finish = finish;
        }

        @Override
        public String toString() {
            return "CommentItem{" + "start=" + start + ", filish=" + finish + ", mustCheck=" + mustCheck + '}';
        }
    }

    private static class CommentTypeItem extends CommentItem {

        CommentType type;

        public CommentTypeItem(SQLPosition start, SQLPosition finish, CommentType type) {
            this.start = start;
            this.finish = finish;
            this.type = type;
        }

        @Override
        public String toString() {
            return "CommentItem{" + "start=" + start + ", filish=" + finish + ", type=" + type + '}';
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
                    finalCommentList.add(new CommentItemWithBoolean(priorItem.start, finish, false));
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

    public static enum PreprocessBehavior {

        PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT,
        PT_REMOVE_UNUSED_BLOCKS
    };

    public String preprocess() throws SQLScriptException, IOException {
        return preprocess(PreprocessBehavior.PT_REMOVE_UNUSED_BLOCKS);
    }

    public String preprocess(PreprocessBehavior preprocessType) throws SQLScriptException, IOException {
        SQLParser parser = new SQLParser(reader, fileName, variablesProvider, true);
        SQLAdditionParserOptions additionParserOptions = new SQLAdditionParserOptions();
        additionParserOptions.hidePassword = true;
        parser.setAdditionOptions(additionParserOptions);

        commentLevel = 0;

        commentList = new ArrayList<>();
        stackCommentStack = new ArrayList<>();
        finalCommentList = new ArrayList<>();


        {

            preprocessBlock(parser, BlockType.SCRIPT, SkipState.NO);

            int lastIndex = 1;
            StringBuilder sb = new StringBuilder(script.length() + finalCommentList.size() * 4);
            if (finalCommentList.isEmpty()) {
                return script;
            }
            for (CommentItemWithBoolean item : finalCommentList) {

                sb.append(script.substring(lastIndex - 1, item.start.getIndex() - 1));
                if (PreprocessBehavior.PT_REPLACE_UNUSED_BLOCKS_TO_COMMENT.equals(preprocessType)){
                    sb.append("/*");
                    if (item.mustCheck) {
                        sb.append(crashComments(script.substring(item.start.getIndex() - 1, item.finish.getIndex())));
                    } else {
                        sb.append(script.substring(item.start.getIndex() - 1, item.finish.getIndex()));
                    }

                    sb.append("*/");
                }
                lastIndex = item.finish.getIndex() + 1;


            }
            sb.append(script.substring(lastIndex - 1));
            return sb.toString();
        }
//        throw new SQLScriptException("Incorrect preprocess behavior ");
    }

    private static String crashComments(String text) {//stupid crash
        return text.replace("/*", "??").replace("*/", "??");
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
                    //SQLPosition firstPos = parser.getPosition().fork();


                    TokenType tokenType = token.getType();
                    //if (SkipState.STATEMENT!=skipState || mainBlock)
                    if (TokenType.TK_SCRIPT_IF.equals(tokenType)) {
                        if (TokenType.TK_SCRIPT_IF.equals(tokenType)) {
                        }
                    }


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
