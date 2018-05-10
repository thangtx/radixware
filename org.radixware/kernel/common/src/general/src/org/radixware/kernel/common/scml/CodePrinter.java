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

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.types.Id;


public abstract class CodePrinter implements Closeable {
    public static final String DATABASE_TYPE = "databaseType";

    private final Map<Object, Object> properties = new HashMap<>();
    private final CodePrinter container;
    
    @Override
    public void close() throws IOException{
        
    }
    
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
        
        public static CodePrinter newJavaHumanReadablePrinter() {
            return new JavaHumanReadeablePrinter();
        }

        public static CodePrinter newJavaPrinter() {
            return new JavaPrinter();
        }
        
        public static CodePrinter newJavaPrinter(CodePrinter container){
            return (container instanceof IHumanReadablePrinter) ? CodePrinter.Factory.newJavaHumanReadablePrinter() : CodePrinter.Factory.newJavaPrinter();
        }

        public static CodePrinter newNullPrinter() {
            return new NullCodePrinter();
        }

        public static CodePrinter newStringPrinter() {
            return new JavaPrinter();
        }
        
        public static CodePrinter newSqlPrinter() {
            return new SqlPrinter4ORACLE(false);
        }

        public static CodePrinter newSqlPrinter(EDatabaseType databaseType) {
            if (databaseType == null) {
                throw new IllegalArgumentException("Database type can't be null");
            }
            else {
                switch (databaseType) {
                    case ORACLE : return new SqlPrinter4ORACLE(true);
                    case ENTERPRISEDB : return new SqlPrinter4PostgresEnterprise();
                    case POSTGRESQL : return new SqlPrinter4PostgreSQL();
                    default : throw new UnsupportedOperationException("Database type ["+databaseType+"] is not support yet");
                }
            }
        }
        
        public static CodePrinter newSqlPrinter(CodePrinter container, EDatabaseType databaseType) {
            if (container == null) {
                throw new IllegalArgumentException("Container can't be null");
            }
            else if (databaseType == null) {
                throw new IllegalArgumentException("Database type can't be null");
            }
            else if (container instanceof ScmlCodePrinter) {
                return newSqlPrinter((ScmlCodePrinter)container, databaseType);
            }
            else {
                switch (databaseType) {
                    case ORACLE : return new NestedSqlPrinter4ORACLE(container);
                    case ENTERPRISEDB : return new NestedSqlPrinter4PostgresEnterprise(container);
                    case POSTGRESQL : return new NestedSqlPrinter4PostgreSQL(container);
                    default : throw new UnsupportedOperationException("Database type ["+databaseType+"] is not support yet");
                }
            }
        }

        public static ScmlCodePrinter newSqlPrinter(ScmlCodePrinter container, EDatabaseType databaseType) {
            if (container == null) {
                throw new IllegalArgumentException("Container can't be null");
            }
            else if (databaseType == null) {
                throw new IllegalArgumentException("Database type can't be null");
            }
            else {
                switch (databaseType) {
                    case ORACLE : return new NestedScmlPrinter4ORACLE(container);
                    case ENTERPRISEDB : return new NestedScmlPrinter4PostgresEnterprise(container);
                    case POSTGRESQL : return new NestedScmlPrinter4PostgreSQL(container);
                    default : throw new UnsupportedOperationException("Database type ["+databaseType+"] is not support yet");
                }
            }
        }
    }
    
    private LineMatcher clm = new LineMatcher();
    private LineMatcher root = clm;

    protected CodePrinter() {
        this.container = null;
    }

    protected CodePrinter(CodePrinter container) {
        if (container == null) {
            throw new IllegalArgumentException("Container can't be null");
        }
        else {
            this.container = container;
            this.properties.putAll(container.properties);
        }
    }
    
    public boolean hasContainer(){
        return container != null;
    }

    public CodePrinter getContainer(){
        return container;
    }
    
    public void reset() {
        if (hasContainer()) {
            container.reset();
        }
    }

    public void clear() {
        if (hasContainer()) {
            container.clear();
        }
    }

    public void enterCodeSection(LineMatcher.ILocationDescriptor sectionName) {
        if (hasContainer()) {
            synchronized (container) {
                container.clm = container.clm.addChild(sectionName);
            }
        }
        else {
            synchronized (this) {
                clm = clm.addChild(sectionName);
            }
        }
    }

    public void leaveCodeSection() {
        if (hasContainer()) {
            synchronized (container) {
                LineMatcher matcher = container.clm.getParent();
                assert matcher != null;
                container.clm = matcher;
            }
        }
        else {
            synchronized (this) {
                LineMatcher matcher = clm.getParent();
                assert matcher != null;
                clm = matcher;
            }
        }
    }

    public LineMatcher getLineMatcher() {
        if (hasContainer()) {
            return container.root;
        }
        else {
            return root;
        }
    }

    protected LineMatcher getCurrentLineMatcher() {
        if (hasContainer()) {
            return container.clm;
        }
        else {
            return clm;
        }
    }

    public abstract char charAt(int index);

    public abstract CodePrinter print(Id id);

    public abstract CodePrinter print(char c);

    public abstract CodePrinter print(boolean b);

    public abstract CodePrinter print(CharSequence text);

    public abstract CodePrinter print(char[] text);

    public abstract CodePrinter println(char[] text);

    public abstract CodePrinter print(long l);

    public abstract CodePrinter print(int l);

    public abstract int length();

    public Monitor createMonitor(int localOffset) {
        return new Monitor(localOffset);
    }
    private Monitor activeMonitor;

    public void activateMonitor(Monitor m) {
        if (hasContainer()) {
            synchronized (container) {
                container.activeMonitor = m;
                container.activeMonitor.activate();
            }
        }
        else {
            synchronized (this) {
                this.activeMonitor = m;
                this.activeMonitor.activate();
            }
        }
    }

    private void resetMonitor() {
        if (hasContainer()) {
            synchronized (container) {
                container.activeMonitor = null;
            }
        }
        else {
            synchronized (this) {
                this.activeMonitor = null;
            }
        }
    }

    protected Monitor getActiveMonitor() {
        if (hasContainer()) {
            synchronized (container) {
                return container.activeMonitor;
            }
        }
        else {
            synchronized (this) {
                return activeMonitor;
            }
        }
    }

    public final CodePrinter println() {
        return print('\n');
    }

    /**
     * This method must fill indentation using TAB symbol only because this
     * feature is used by SrcPositionLocator in ads to determine real offset
     * inside of text item by offset in generated code
     */
    protected final void applyIndentation() {
        if (hasContainer()) {
            synchronized (container) {
                Monitor m = container.activeMonitor;
                container.activeMonitor = null;
                if (dept > 0) {
                    for (int i = 0; i < dept; i++) {
                        print('\t');
                    }
                }
                container.activeMonitor = m;
            }
        }
        else {
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
    }

    public final CodePrinter println(char c) {
        return print(c).println();
    }

    public final CodePrinter println(String text) {
        return print(text).println();
    }

    public final CodePrinter println(long l) {
        return print(l).println();
    }

    public final CodePrinter printComma() {
        return print(',');
    }

    public final CodePrinter printMinus() {
        return print('-');
    }

    public final CodePrinter printColon() {
        return print(':');
    }

    public final CodePrinter printSpace() {
        return print(' ');
    }

    public CodePrinter printlnSemicolon() {
        return println(';');
    }

    public abstract CodePrinter printStringLiteral(String text);

    public abstract CodePrinter printCommandSeparator();

    public CodePrinter printCommand(String command) {
        if (command != null && !command.isEmpty()) {
            print(command);
            printCommandSeparator();
        }
        return this;
    }

    public final CodePrinter printError() {
        return print("???");
    }
    private int dept = 0;

    public CodePrinter enterBlock(int dept) {
        this.dept += dept;
        return this;
    }

    public final CodePrinter enterBlock() {
        return enterBlock(1);
    }

    public CodePrinter leaveBlock(int dept) {
        this.dept -= dept;
        if (this.dept < 0) {
            this.dept = 0;
        }
        return this;
    }

    public final CodePrinter leaveBlock() {
        return leaveBlock(1);
    }

    public abstract char[] getContents();

    public abstract int getLineNumber(int globalOffset);

    public void putProperty(Object property, Object value) {
        properties.put(property, value);
    }

    public Object getProperty(Object property) {
        if (properties.containsKey(property)) {
            return properties.get(property);
        }
        else if (hasContainer()) {
            return container.properties.get(property);
        }
        else {
            return null;
        }
    }
}
