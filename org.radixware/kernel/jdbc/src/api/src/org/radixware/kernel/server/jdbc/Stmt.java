/*
 * Copyright (c) 2008-2018, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.server.jdbc;

/**
 *
 * @author achernomyrdin
 */
public class Stmt implements Comparable<Stmt> {
    final int stmtId;
    final String text;
    private final int[] types;
    private final int[] outputs;

    public Stmt() {
        this(0);
    }

    public Stmt(final String text) {
        this(0, text);
    }

    public Stmt(final String text, final int... types) {
        this(0, text, types);
    }

    public Stmt(final int stmtId) {
        this(stmtId, "select 1");
    }

    public Stmt(final int stmtId, final String text, final int... types) {
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException("Stmt #" + stmtId +": SQL text string can't be null or empty!");
        } else if (types == null) {
            throw new IllegalArgumentException("Stmt #" + stmtId + ": Types list can't be null!");
        } else {
            final int amount = text.length() - text.replace("?", "").length();
            if (amount != types.length) {
                throw new IllegalArgumentException("Stmt #" + stmtId + ": Different amount of parameters and types: SQL string contains " + amount + " question marks, but parameter list size in the constructor is " + types.length);
            } else {
                this.stmtId = stmtId;
                this.text = text;
                this.types = types;
                this.outputs = new int[0];
            }
        }
    }

    public Stmt(final Stmt stmt, final int... outputs) {
        if (stmt == null) {
            throw new IllegalArgumentException("Nested statement can't be null or empty!");
        } else if (outputs == null) {
            throw new IllegalArgumentException("Output marks list can't be null!");
        } else {
            for (int index : outputs) {
                if (index <= 0 || index > stmt.getTypes().length) {
                    throw new IllegalArgumentException("Output mark [" + index + "] outside the parameter's list area. Need be in 1.." + stmt.getTypes().length);
                }
            }
            this.stmtId = stmt.getStmtId();
            this.text = stmt.getText();
            this.types = stmt.getTypes();
            this.outputs = outputs;
        }
    }

    @Override
    public int compareTo(Stmt arg0) {
        return this.getStmtId() - arg0.getStmtId();
    }

    public int getStmtId() {
        return stmtId;
    }

    public String getText() {
        return text;
    }

    public int[] getTypes() {
        return types;
    }

    public int[] getOutputs() {
        return outputs;
    }
    
}
