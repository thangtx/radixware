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
import org.radixware.kernel.common.utils.Reference;

public class SQLParserUtils {

    protected static int getPreprocessorLen(final ISQLReader inReader, final Reference<SQLPreprocessor.EPreprocessorType> preprocessorType) throws IOException, SQLScriptException {//check after #
        preprocessorType.set(null);
        if (!checkPreprocessorBefore(inReader)) {
            return -1;
        }
        preprocessorType.set(SQLPreprocessor.EPreprocessorType.OTHER);
        final int len = checkPreprocessorAfter(inReader, preprocessorType);
        return len;
    }

    private static int getPreprocessorStatementLen(final String line, final String requestStatement, final SQLPosition posBefore, final ISQLReader inReader) throws IOException {
        if (!line.startsWith(requestStatement)) {
            return -1;
        }

        final int requestStatementLen = requestStatement.length();

//        posBefore.setIndex(posBefore.getIndex() + requestStatementLen);
//        posBefore.setColumn(posBefore.getColumn() + requestStatementLen);
//        
//        posBefore.setIndex(posBefore.getIndex());
//        posBefore.setColumn(posBefore.getColumn());        

        inReader.setPosition(posBefore);

        return requestStatementLen;
    }

    private static int checkPreprocessorAfter(final ISQLReader inReader, final Reference<SQLPreprocessor.EPreprocessorType> preprocessorType) throws IOException, SQLScriptException {//check before # - isNewLine
        final SQLPosition posBefore = inReader.getPosition().fork();
        final StringBuilder sb = new StringBuilder();
        for (;;) {
            final char ch1 = (char) inReader.read();
            if (ch1 < 1 || ch1 == '\n' || ch1 == '\r') {
                break;
            }
            sb.append(ch1);
        }

        final String line = sb.toString().toUpperCase();
        int len;

        if ((len = getPreprocessorStatementLen(line, "IF", posBefore, inReader)) > 0) {
            return len;
        }
        if ((len = getPreprocessorStatementLen(line, "ELSE", posBefore, inReader)) > 0) {
            return len;
        }
        if ((len = getPreprocessorStatementLen(line, "ENDIF", posBefore, inReader)) > 0) {
            return len;
        }
        if ((len = getPreprocessorStatementLen(line, "PRAGMA", posBefore, inReader)) > 0) {
            preprocessorType.set(SQLPreprocessor.EPreprocessorType.PRAGMA);
            return len;
        }
        throw new SQLScriptException("Invalid preprocessor PL/SQL statement: \'" + line + "\'", posBefore);
    }

    private static boolean checkPreprocessorBefore(final ISQLReader inReader) throws IOException {//check before # - isNewLine
        final SQLPosition cur_pos = inReader.getPosition().fork();
        final int currColumn = cur_pos.getColumn();
        if (currColumn == 2) {//first - #
            return true;
        }

        final SQLPosition posBu = cur_pos.fork();

        final int delta = currColumn - 1;
        cur_pos.setIndex(cur_pos.getIndex() - delta);
        cur_pos.setColumn(1);
        inReader.setPosition(cur_pos);

        boolean onlySpaces = true;
        for (int i = 0; i < delta-1; ++i) {
            final char ch = (char) inReader.read();
            if (ch != ' ' && ch != '\t') {
                onlySpaces = false;
                break;
            }
        }
        inReader.setPosition(posBu);
        return onlySpaces;
    }

}
