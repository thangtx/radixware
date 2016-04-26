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

package org.radixware.kernel.common.scml;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.types.Id;


public abstract class CodePrinter {

    private final Map<Object, Object> properties = new HashMap<>();

    public class Monitor {

        private int localOffset;
        private int globalOffset;
        private int activation;

        public Monitor(int localOffset) {
            this.localOffset = localOffset;
        }

        private void activate() {
            this.globalOffset = CodePrinter.this.length();
            this.activation = 0;
        }

        public int getGlobalOffset() {
            return globalOffset;
        }

        protected void offset(int inc) {
            activation += inc;
            if (activation >= localOffset) {
                globalOffset = CodePrinter.this.length() - (activation - localOffset);
                CodePrinter.this.resetMonitor();
            }
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static CodePrinter newJavaPrinter() {
            return new JavaPrinter();
        }

        public static CodePrinter newNullPrinter() {
            return new NullCodePrinter();
        }

        public static CodePrinter newSqlPrinter() {
            return new SqlPrinter();
        }
    }
    private LineMatcher clm = new LineMatcher();
    private LineMatcher root = clm;

    protected CodePrinter() {
    }

    public void reset() {
    }

    public void clear() {
    }

    public void enterCodeSection(LineMatcher.ILocationDescriptor sectionName) {
        synchronized (this) {
            clm = clm.addChild(sectionName);
        }
    }

    public void leaveCodeSection() {
        synchronized (this) {
            LineMatcher matcher = clm.getParent();
            assert matcher != null;
            clm = matcher;
        }
    }

    public LineMatcher getLineMatcher() {
        return root;
    }

    protected LineMatcher getCurrentLineMatcher() {
        return clm;
    }

    public abstract char charAt(int index);

    public abstract void print(Id id);

    public abstract void print(char c);

    public abstract void print(boolean b);

    public abstract void print(CharSequence text);

    public abstract void print(char[] text);

    public abstract void println(char[] text);

    public abstract void print(long l);

    public abstract void print(int l);

    public abstract int length();

    public Monitor createMonitor(int localOffset) {
        return new Monitor(localOffset);
    }
    private Monitor activeMonitor;

    public void activateMonitor(Monitor m) {
        synchronized (this) {
            this.activeMonitor = m;
            this.activeMonitor.activate();
        }
    }

    private void resetMonitor() {
        synchronized (this) {
            this.activeMonitor = null;
        }
    }

    protected Monitor getActiveMonitor() {
        synchronized (this) {
            return activeMonitor;
        }
    }

    public final void println() {
        print('\n');
    }

    /**
     * This method must fill indentation using TAB symbol only because this
     * feature is used by SrcPositionLocator in ads to determine real offset
     * inside of text item by offset in generated code
     */
    protected final void applyIndentation() {
        synchronized (this) {
            Monitor m = activeMonitor;
            activeMonitor = null;
            if (dept > 0) {
                for (int i = 0; i < dept; i++) {
                    print('\t');
                }
            }
            activeMonitor = m;
        }
    }

    public final void println(char c) {
        print(c);
        println();
    }

    public final void println(String text) {
        print(text);
        println();
    }

    public final void println(long l) {
        print(l);
        println();
    }

    public final void printComma() {
        print(',');
    }

    public final void printMinus() {
        print('-');
    }

    public final void printColon() {
        print(':');
    }

    public final void printSpace() {
        print(' ');
    }

    public void printlnSemicolon() {
        println(';');
    }

    public abstract void printStringLiteral(String text);

    public abstract void printCommandSeparator();

    public void printCommand(String command) {
        if (command != null && !command.isEmpty()) {
            print(command);
            printCommandSeparator();
        }
    }

    public final void printError() {
        print("???");
    }
    private int dept = 0;

    public void enterBlock(int dept) {
        this.dept += dept;
    }

    public final void enterBlock() {
        enterBlock(1);
    }

    public void leaveBlock(int dept) {
        this.dept -= dept;
        if (this.dept < 0) {
            this.dept = 0;
        }
    }

    public final void leaveBlock() {
        leaveBlock(1);
    }

    public abstract char[] getContents();

    public abstract int getLineNumber(int globalOffset);

    public void putProperty(Object property, Object value) {
        properties.put(property, value);
    }

    public Object getProperty(Object property) {
        return properties.get(property);
    }
}
