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

package org.radixware.kernel.utils.spellchecker;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.check.spelling.Word;
import org.radixware.kernel.common.utils.FileUtils;


public class WordReader implements Closeable {

    private Reader reader;
    private final int buffSize;
    private int cursor;
    private char[] buff;
    private boolean isOpened;
    private int swapLength;

    public WordReader(Reader reader) {
        this.cursor = 0;
        this.buffSize = 128;
        this.buff = new char[buffSize];
        this.isOpened = false;
        this.reader = reader;
        this.swapLength = 0;
    }

    public WordReader(InputStream stream) {
        this.cursor = 0;
        this.buffSize = 128;
        this.buff = new char[buffSize];
        this.isOpened = false;
        this.reader = new InputStreamReader(stream, Charset.forName(FileUtils.XML_ENCODING));
        this.swapLength = 0;
    }

    private int swapping() throws IOException {
        isOpened = true;
        resetCursor();
        return reader.read(buff);
    }

    private void moveCursorNext() {
        ++cursor;
    }

    private void resetCursor() {
        cursor = 0;
    }

    private char getCurrentChar() {
        return buff[cursor];
    }

    private final StringBuilder wordBuilder = new StringBuilder();

    public Word readWord() {
        wordBuilder.setLength(0);

        try {
            while (true) {
                if (!isOpened || cursor >= swapLength) {
                    swapLength = swapping();
                    if (swapLength <= 0) {
                        if (wordBuilder.length() > 0) {
                            return Word.Factory.newInstance(wordBuilder);
                        }
                        return null;
                    }
                }

                while (cursor < swapLength) {
                    char ch = getCurrentChar();
                    if (ch == '\n' || ch == '\r') {
                        if (wordBuilder.length() > 0) {
                            return Word.Factory.newInstance(wordBuilder);
                        }
                    } else {
                        wordBuilder.append(ch);
                    }
                    moveCursorNext();
                }
            }
        } catch (IOException ex) {
            return null;
        }
    }

    public Word[] readAllWords() {

        List<Word> words = new ArrayList<>();
        Word word;
        while ((word = readWord()) != null) {
            words.add(word);
        }

        Word[] arr = new Word[words.size()];
        return words.toArray(arr);
    }

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException ex) {
            //...
        }
    }
}
