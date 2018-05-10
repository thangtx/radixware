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
package org.radixware.kernel.common.compiler.core.ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;

public class RadixObjectLocator {

    public static final String PRINTER_PROPERTY_NAME = "RO_L1";

    public static class RadixObjectData {

        public final RadixObject radixObject;
        public final int start;
        public int end;
        private CodePrinter printer;
        private int[] leadingTabs;

        public RadixObjectData(RadixObject radixObject, int start, CodePrinter printer) {
            this.radixObject = radixObject;
            this.start = start;
            this.printer = printer;
        }

        public void commit() {

            end = printer.length() - 1;
            //TODO: compute additional offsets for each line: they are inserted by printer if startBlock() methods
            //were invoked before printing current object
            //useful only for scml.text instances
            if (radixObject instanceof Scml.Text) {
                int tabCount = 0;
                int line = 0;
                int[] lines = new int[end - start];
                for (int i = start; i < end; i++) {
                    char c = printer.charAt(i);
                    if (c == '\n') {
                        lines[line] = tabCount;
                        line++;
                        tabCount = 0;
                    } else if (c == '\t') {
                        tabCount++;
                    }
                }
                if (tabCount > 0) {
                    lines[line] = tabCount;
                    line++;
                }
                if (line > 0) {
                    this.leadingTabs = new int[line];
                    System.arraycopy(lines, 0, this.leadingTabs, 0, line);
                }
            }
            printer = null;
        }

        public int convertSrcPosToObjectOffset(int position) {
            if (this.leadingTabs == null) {
                return position - start;
            } else {
                String text = ((Scml.Text) radixObject).getText();
                String[] lines = text.split("\n");
                
                int lineStart = this.start;
                int scmlLineStart = 0;
                //int count = Math.min(lines.length, leadingTabs.length);
                int leadingTabsCount = leadingTabs.length;
                for (int i = 0; i < lines.length; i++) {
                    int scmlLineLen = lines[i].length();
                    int count = i < leadingTabsCount? leadingTabs[i] : 0;
                    int lineLen = scmlLineLen + count;
                    int lineEnd = lineStart + lineLen;

                    if (position >= lineStart && position <= lineEnd) {
                        int offsetInLine = position - lineStart;
                        int offsetInScmlLine = offsetInLine - count;
                        return scmlLineStart + offsetInScmlLine;
                    }
                    lineStart = lineEnd + 1;
                    scmlLineStart += scmlLineLen + 1;
                }
                return start;
            }
        }
    }
    private CodePrinter printer;
    private final List<RadixObjectData> data = new ArrayList<>();

    public RadixObjectLocator(CodePrinter printer) {
        this.printer = printer;
    }

    public RadixObjectData start(RadixObject radixObject) {
        final RadixObjectData data = new RadixObjectData(radixObject, printer.length(), printer);
        this.data.add(data);
        return data;
    }

    public void commit() {
        this.printer = null;
    }

    public RadixObjectData[] take(int start, int end) {
        //TODO: rewrite to binary search
        List<RadixObjectData> result = new ArrayList<>(3);
        for (int i = 0, len = data.size(); i < len; i++) {
            final RadixObjectData data = this.data.get(i);
            if (data.start > end) {
                break;
            }
            if ((data.start <= start && data.end >= start) || (data.start >= start && data.start <= end)) {
                result.add(data);
            }
        }
        return result.isEmpty() ? null : result.toArray(new RadixObjectData[result.size()]);
    }

    public RadixObjectData take(RadixObject radixObject) {
        for (int i = 0, len = data.size(); i < len; i++) {
            final RadixObjectData data = this.data.get(i);
            if (data.radixObject == radixObject) {
                return data;
            }
        }
        return null;
    }
}