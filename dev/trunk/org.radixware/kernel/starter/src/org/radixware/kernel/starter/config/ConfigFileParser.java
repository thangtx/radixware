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

package org.radixware.kernel.starter.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ConfigFileParser {

    public ConfigFileParser() {
    }

    public ConfigFileParseResult parse(final InputStream stream) throws ConfigFileParseException {
        return parseData(getChars(stream));
    }

    private static char[] getChars(final InputStream stream) throws ConfigFileParseException {
        try {
            java.util.Scanner s = new java.util.Scanner(stream, Charset.defaultCharset().name()).useDelimiter("\\A");
            return s.hasNext() ? s.next().toCharArray() : new char[]{};
        } catch (RuntimeException ex) {
            throw new ConfigFileParseException(ex);
        }
    }

    private ConfigFileParseResult parseData(final char[] data) throws ConfigFileParseException {
        int currentLineStartOffset = 0;
        final ParseContext context = new ParseContext();
        for (int i = 0; i <= data.length; i++) {
            int lineEndLength = lineEndLength(data, i);
            if (lineEndLength > 0) {
                context.newLine(currentLineStartOffset);
                if (currentLineStartOffset < i) {
                    parseLine(new String(data, currentLineStartOffset, i - currentLineStartOffset), context);
                }
                currentLineStartOffset = i + lineEndLength;
            }
        }
        return context.getResult();
    }

    private void parseLine(final String line, final ParseContext context) throws ConfigFileParseException {
        int offset = 0;
        //skip initial whitespaces
        while (offset < line.length() && Character.isWhitespace(line.charAt(offset))) {
            offset++;
        }
        //check for empty line
        if (offset == line.length()) {
            return;
        }
        //check for comment
        if (line.charAt(offset) == '#') {
            return;
        }
        //check for section
        if (line.charAt(offset) == '[') {
            String sectionLine = line.trim();
            if (sectionLine.indexOf('#') >= 0) {
                sectionLine = sectionLine.substring(0, sectionLine.indexOf('#')).trim();
            }
            if (!sectionLine.endsWith("]")) {
                throw new ConfigFileParseException(context.currentLine, line.length() - 1, "Section definition line must be ended with ]");
            }
            final String sectionName = sectionLine.substring(1, sectionLine.length() - 1).trim();
            if (sectionName.isEmpty()) {
                throw new ConfigFileParseException(context.currentLine, offset + 1, "Section name should be non-empty");
            }
            context.newSection(sectionName, context.getCurrentLineOffset() + offset, sectionLine.length() - offset);
            return;
        }
        //parse key
        final int keyStartOffset = offset;
        while (offset < line.length() && isKeyPart(line.charAt(offset))) {
            offset++;
        }
        //check for non-empty key
        if (offset == keyStartOffset) {
            throw new ConfigFileParseException(context.getCurrentLine(), offset, "Illegal start of the key");
        }
        final String key = line.substring(keyStartOffset, offset);
        //skip whitespaces
        while (offset < line.length() && Character.isWhitespace(line.charAt(offset))) {
            offset++;
        }
        //check for key-only (flag) value
        if (offset == line.length()) {
            context.newEntry(new ConfigFileParsedEntry(context.currentLineOffset + keyStartOffset, context.currentLineOffset + keyStartOffset + key.length() - 1, key, null));
            return;
        }
        //check for '=' presense
        if (line.charAt(offset) != '=') {
            throw new ConfigFileParseException(context.currentLine, offset, "'=' character expected, but not found");
        }
        offset++;
        //skip whitespaces
        while (offset < line.length() && Character.isWhitespace(line.charAt(offset))) {
            offset++;
        }
        //read value
        if (offset == line.length()) {
            context.newEntry(new ConfigFileParsedEntry(context.currentLineOffset + keyStartOffset, context.currentLineOffset + offset, key, ""));
        } else if (line.charAt(offset) == '\'' || line.charAt(offset) == '\"') {
            final char quoteChar = line.charAt(offset);
            final int quoteStartOffset = offset;
            do {
                offset++;
            } while (offset < line.length() && line.charAt(offset) != quoteChar);
            if (offset == line.length()) {
                throw new ConfigFileParseException(context.currentLine, context.currentLineOffset + offset - 1, "Closing " + quoteChar + " expected, but not found");
            }
            final String value = line.substring(quoteStartOffset + 1, offset);
            context.newEntry(new ConfigFileParsedEntry(context.currentLineOffset + keyStartOffset, context.currentLineOffset + quoteStartOffset + 1 + value.length(), key, value));
            offset++;
        } else {
            final int valueStartOffset = offset;
            while (offset < line.length() && isValuePart(line.charAt(offset)) && line.charAt(offset) != '#') {
                offset++;
            }
            if (offset == valueStartOffset) {
                throw new ConfigFileParseException(context.currentLine, offset, "Illegal start of the value: " + line.charAt(offset));
            }
            final String value = line.substring(valueStartOffset, offset);
            context.newEntry(new ConfigFileParsedEntry(context.currentLineOffset + keyStartOffset, context.currentLineOffset + valueStartOffset + value.length(), key, value));
        }
        //check that all characters left are whitespaces
        while (offset < line.length() && Character.isWhitespace(line.charAt(offset))) {
            offset++;
        }
        if (offset < line.length() && line.charAt(offset) != '#') {
            throw new ConfigFileParseException(context.currentLine, offset, "Unexpected character: " + line.charAt(offset));
        }
    }

    private boolean isKeyPart(char c) {
        return Character.isJavaIdentifierPart(c);
    }

    private boolean isValuePart(char c) {
        return !Character.isWhitespace(c);
    }

    private int lineEndLength(final char[] data, final int offset) {
        if (offset == data.length) {
            return 1;
        }
        if (data[offset] == '\r' && offset < data.length - 1 && data[offset + 1] == '\n') {
            return 2;
        }
        if (data[offset] == '\n') {
            return 1;
        }
        return 0;
    }

    public static class ConfigFileCursor {

        private ConfigFileParseResult parseResult = null;
        private Iterator<ConfigFileParsedSection> sectionsIterator;
        private Iterator<ConfigFileParsedEntry> entriesIterator;
        private ConfigFileParsedSection currentSection;
        private ConfigFileParsedEntry currentEntry;
        private final List<Modification> modifications = new ArrayList<>();
        private char[] data;
        private Modification currentModification;
        private boolean isClosed = false;
        private final InputStream stream;

        public ConfigFileCursor(final InputStream stream) throws ConfigFileParseException {
            this.stream = stream;
            prepare();
        }

        private void prepare() throws ConfigFileParseException {
            if (isClosed) {
                throw new IllegalStateException("Cursor is already closed");
            }
            if (parseResult != null) {
                throw new IllegalStateException("Cursor is already prepared");
            }
            data = getChars(stream);
            parseResult = new ConfigFileParser().parseData(data);
            sectionsIterator = parseResult.getParsedSections().iterator();
        }

        public boolean hasNextSection() {
            return sectionsIterator.hasNext();
        }

        public boolean nextSection() {
            if (sectionsIterator.hasNext()) {
                currentSection = sectionsIterator.next();
                entriesIterator = currentSection.getParsedEntries().iterator();
                currentEntry = null;
                submitModification();
                return true;
            } else {
                return false;
            }
        }

        public boolean nextEntry() {
            if (entriesIterator.hasNext()) {
                currentEntry = entriesIterator.next();
                submitModification();
                return true;
            } else {
                return false;
            }
        }

        public String getSectionName() {
            if (currentSection == null) {
                throw new IllegalStateException("Cursor is not positioned over any section");
            }
            return currentSection.getName();
        }

        public ConfigEntry getEntry() {
            ensureEntry();
            return new ConfigEntry(currentEntry.getKey(), currentEntry.getValue());
        }

        private void ensureEntry() throws IllegalStateException {
            if (currentEntry == null) {
                throw new IllegalStateException("Cursor is not positioned over any entry");
            }
        }

        public void removeEntry() {
            ensureEntry();
            ensureModification();
            if (currentModification.isRemoveLine()) {
                throw new IllegalStateException("Entry has already been removed");
            }
            currentModification.setRemoveLine();
        }

        public void replaceEntry(final ConfigEntry newEntry) {
            ensureEntry();
            ensureModification();
            if (currentModification.isRemoveLine()) {
                throw new IllegalStateException("Can not replace deleted line");
            }
            if (currentModification.isReplaceEntry()) {
                throw new IllegalStateException("Entry at current position has already been replaced");
            }
            currentModification.replaceEntry(getLine(newEntry));
        }

        public void insertBefore(final ConfigEntry entry) {
            ensureModification();
            currentModification.addNewLineBefore(getLine(entry));
        }

        public void insertAfter(final ConfigEntry entry) {
            ensureModification();
            currentModification.addNewLineAfter(getLine(entry));
        }

        private String getLine(final ConfigEntry entry) {
            if (entry.getKey() == null) {
                throw new IllegalArgumentException("null keys are not supported");
            }
            return entry.getKey() + getValuePart(entry.getValue());
        }

        private String getValuePart(final String value) {
            if (value == null) {
                return "";
            }
            if (value.contains(" ")) {
                return " = '" + value + "'";
            } else {
                return " = " + value;
            }
        }

        private void ensureModification() {
            if (currentEntry == null && currentSection == null) {
                throw new IllegalStateException("Cursor is not positioned at section or entry");
            }
            if (currentModification == null) {
                if (currentEntry != null) {
                    currentModification = new Modification(currentEntry.getStartOffset(), currentEntry.getEndOffset(), false);
                } else {
                    currentModification = new Modification(currentSection.getStartOffset() + currentSection.getSectionDefinitionLength(), -1, true);
                }
            }
        }

        private void submitModification() {
            if (currentModification != null) {
                modifications.add(currentModification);
                currentModification = null;
            }
        }

        public char[] flush() throws IOException {
            submitModification();
            if (modifications.isEmpty()) {
                return data;
            }
            final List<CharBuffer> chunks = new ArrayList<>();
            int dataPos = 0;
            int modificationPos = 0;
            final CharBuffer systemEOL = CharBuffer.wrap(System.getProperty("line.separator"));
            for (Modification modification : modifications) {
                int[] lineStartAndEnd = lineStartAndEnd(data, modification.getStartOffset());
                boolean needLineBreakBefore = false;
                if (modification.isReplaceEntry()) {
                    modificationPos = modification.getStartOffset();
                } else if (modification.isImmediatelyAfterSectionDefition()) {
                    modificationPos = lineStartAndEnd[1];
                    needLineBreakBefore = data[lineStartAndEnd[1] - 1] != '\n';
                } else {
                    modificationPos = lineStartAndEnd[0];
                }

                chunks.add(CharBuffer.wrap(data, dataPos, modificationPos - dataPos));
                if (needLineBreakBefore) {
                    chunks.add(systemEOL);
                }
                for (String lineBefore : modification.getNewLinesBefore()) {
                    chunks.add(CharBuffer.wrap(lineBefore));
                    chunks.add(systemEOL);
                }
                if (modification.isReplaceEntry()) {
                    chunks.add(CharBuffer.wrap(modification.getReplaceText()));
                    chunks.add(CharBuffer.wrap(data, modification.getEndOffset(), lineStartAndEnd[1] - modification.getEndOffset()));
                } else if (!modification.isRemoveLine() && !modification.isImmediatelyAfterSectionDefition()) {
                    chunks.add(CharBuffer.wrap(data, lineStartAndEnd[0], lineStartAndEnd[1] - lineStartAndEnd[0]));
                }
                boolean first = true;
                boolean linesInserted = false;
                for (String lineAfter : modification.getNewLinesAfter()) {
                    if (!first) {
                        chunks.add(systemEOL);
                    } else {
                        first = false;
                    }
                    chunks.add(CharBuffer.wrap(lineAfter));
                    linesInserted = true;
                }
                if (linesInserted) {
                    chunks.add(systemEOL);
                }
                dataPos = lineStartAndEnd[1];
            }
            if (dataPos < data.length - 1) {
                chunks.add(CharBuffer.wrap(data, dataPos, data.length - dataPos));
            }
            int newLength = 0;
            for (CharBuffer chunk : chunks) {
                newLength += chunk.length();
            }
            final CharBuffer result = CharBuffer.allocate(newLength);
            for (CharBuffer chunk : chunks) {
                result.append(chunk);
            }
            return result.array();
        }

        private int[] lineStartAndEnd(final char[] data, final int offset) {
            int lineStart = offset;
            while (lineStart > 0 && data[lineStart - 1] != '\n') {
                lineStart--;
            }
            int lineEnd = Math.min(offset + 1, data.length);
            while (lineEnd < data.length && data[lineEnd - 1] != '\n') {
                lineEnd++;
            }
            return new int[]{lineStart, lineEnd};
        }

        private static class Modification {

            private final int startOffset;
            private final int endOffset;
            private boolean removeLine = false;
            private final List<String> newLinesBefore = new ArrayList<String>();
            private final List<String> newLinesAfter = new ArrayList<String>();
            private final boolean needLineBreakBefore;
            private String replaceText = null;

            public Modification(final int startOffset, final int endOffset, final boolean isImmediatelyAfterSectionDefinition) {
                this.startOffset = startOffset;
                this.endOffset = endOffset;
                this.needLineBreakBefore = isImmediatelyAfterSectionDefinition;
            }

            public boolean isImmediatelyAfterSectionDefition() {
                return needLineBreakBefore;
            }

            public void setRemoveLine() {
                if (replaceText != null) {
                    throw new IllegalStateException("replaceEntry() and removeLine() can not be used together");
                }
                removeLine = true;
            }

            public boolean isRemoveLine() {
                return removeLine;
            }

            public boolean isReplaceEntry() {
                return replaceText != null;
            }

            public int getStartOffset() {
                return startOffset;
            }

            public int getEndOffset() {
                return endOffset;
            }

            public void replaceEntry(final String replaceText) {
                if (this.replaceText != null) {
                    throw new IllegalStateException("replaceEntry was already called");
                }
                if (isRemoveLine()) {
                    throw new IllegalStateException("replaceEntry() and removeLine() can not be used together");
                }
                this.replaceText = replaceText;
            }

            public String getReplaceText() {
                return replaceText;
            }

            public void addNewLineBefore(final String line) {
                newLinesBefore.add(line);
            }

            public void addNewLineAfter(final String line) {
                newLinesAfter.add(line);
            }

            public List<String> getNewLinesBefore() {
                return newLinesBefore;
            }

            public List<String> getNewLinesAfter() {
                return newLinesAfter;
            }
        }
    }

    private static class ParseContext {

        private int currentLine = 0;
        private int currentLineOffset = 0;
        private String currentSectionName = null;
        private int currentSectionStartOffset = 0;
        private int currentSectionLineLength = 0;
        private final List<ConfigFileParsedSection> sections = new ArrayList<ConfigFileParsedSection>();
        private final List<ConfigFileParsedEntry> currentEntries = new ArrayList<ConfigFileParsedEntry>();
        private boolean lastSectionAdded = false;

        public void newLine(final int lineOffset) {
            currentLine++;
            currentLineOffset = lineOffset;
        }

        public void newSection(final String sectionName, final int startOffset, final int secLineLength) {
            if (currentSectionName != null) {
                sections.add(new ConfigFileParsedSection(currentSectionName, currentSectionStartOffset, currentSectionLineLength, currentEntries));
            }
            currentSectionName = sectionName;
            currentSectionStartOffset = startOffset;
            currentSectionLineLength = secLineLength;
            currentEntries.clear();
        }

        public void newEntry(final ConfigFileParsedEntry entry) {
            currentEntries.add(entry);
        }

        public int getCurrentLine() {
            return currentLine;
        }

        public int getCurrentLineOffset() {
            return currentLineOffset;
        }

        public ConfigFileParseResult getResult() {
            if (!lastSectionAdded) {
                sections.add(new ConfigFileParsedSection(currentSectionName, currentSectionStartOffset, currentSectionLineLength, currentEntries));
                lastSectionAdded = true;
            }
            return new ConfigFileParseResult(sections);
        }
    }
}
