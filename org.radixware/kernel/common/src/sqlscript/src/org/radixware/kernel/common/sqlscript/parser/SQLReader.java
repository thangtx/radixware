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


public class SQLReader implements ISQLReader {
    private final Reader reader;
    private SQLPosition pos;
    private SQLPosition posBefore;
    private boolean preRead;
    private int preReadChar;
    private int prevLineLen;

    public SQLReader(final Reader pReader, final String pSrcName) throws IOException {
        if (pReader == null) {
            throw new IllegalArgumentException("Reader can't be null");
        }
        else if (!pReader.markSupported()) {
            throw new IllegalArgumentException("Reader not supports marks. Use another reader");
        }
        else {
            this.reader = pReader;
            this.reader.mark(1);   // ! was 0 - size before resettion 'undo' buffer
            this.pos = new SQLPosition(0, 1, 1, 1, pSrcName);
            this.preRead = false;
            this.prevLineLen = 0;
        }
    }

    @Override
    public int read() throws IOException {
        int ch;
        posBefore = pos.fork();
        
        if (preRead) {
            ch = preReadChar;
            preRead = false;
        } else {
            ch = reader.read();
            if (ch == -1) {
                return -1;
            }
        }
        pos.setIndex(pos.getIndex() + 1);
        pos.setColumn(pos.getColumn() + 1);
        
        if (ch == '\n') {
            prevLineLen = pos.getColumn();
            pos.setColumn(1);
            pos.setLine(pos.getLine() + 1);
            pos.setSourceLine(pos.getSourceLine() + 1);
        }
        return ch;
    }

    @Override
    public void unread(int ch) {
        if (preRead) {
            throw new IllegalStateException("SQLReader : unread twice");
        }
        if (ch == '\n') {
            pos.setLine(pos.getLine() - 1);
            pos.setColumn(prevLineLen);
        } else {
            pos.setColumn(pos.getColumn() - 1);
        }
        pos.setIndex(pos.getIndex() - 1);
        pos = posBefore.fork();
        preReadChar = ch;
        preRead = true;
    }

    @Override
    public void setPosition(final SQLPosition pPos) throws IOException {
        if (pPos == null) {
            throw new IllegalArgumentException("New position can't be null");
        }
        else {
            pos = pPos;
            reader.reset();
            reader.skip(pos.getIndex());
            preRead = false;
        }
    }

    @Override
    public SQLPosition getPosition() {
        return pos;
    }

    @Override
    public String toString() {
        return "SQLReader{" + "reader=" + reader + ", pos=" + pos + ", posBefore=" + posBefore + ", preRead=" + preRead + ", preReadChar=" + preReadChar + ", prevLineLen=" + prevLineLen + '}';
    }
}
