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

package org.radixware.kernel.designer.dds.script.defs;

import org.radixware.kernel.common.scml.*;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.types.Id;


class ScmlCodePrinter extends CodePrinter {

    private final Scml scml;

    public ScmlCodePrinter(Scml buffer) {
        this.scml = buffer;
    }

    @Override
    public char[] getContents() {
        throw new UnsupportedOperationException();
    }

    private void print(final String s) {
        final Scml.Text text;
        final RadixObjects<Scml.Item> items = scml.getItems();
        if (items.isEmpty() || !(items.get(items.size() - 1) instanceof Scml.Text)) {
            text = Scml.Text.Factory.newInstance(s);
            scml.getItems().add(text);
        } else {
            text = (Scml.Text) items.get(items.size() - 1);
            text.setText(text.getText() + s);
        }
    }

    @Override
    public int length() {
        throw new UnsupportedOperationException();
    }

    @Override
    public char charAt(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void print(Id id) {
        print(String.valueOf(id));
    }

    @Override
    public void print(char c) {
        print(String.valueOf(c));
    }

    @Override
    public void print(boolean b) {
        print(String.valueOf(b));
    }

    @Override
    public void print(CharSequence text) {
        print(String.valueOf(text));
    }

    @Override
    public void print(char[] text) {
        print(String.valueOf(text));
    }

    @Override
    public void print(long l) {
        print(String.valueOf(l));
    }

    @Override
    public void printCommandSeparator() {
        final CodePrinter sqlPrinter = CodePrinter.Factory.newSqlPrinter();
        sqlPrinter.printCommandSeparator();
        final String result = sqlPrinter.toString();
        print(result);
    }

    @Override
    public void printStringLiteral(String text) {
        final CodePrinter sqlPrinter = CodePrinter.Factory.newSqlPrinter();
        sqlPrinter.printStringLiteral(text);
        final String result = sqlPrinter.toString();
        print(result);
    }

    @Override
    public void println(char[] text) {
        print(String.valueOf(text));
    }

    private static void moveRight(Scml scml) {
        if (!scml.getItems().isEmpty()) {
            scml.getItems().add(0, Scml.Text.Factory.newInstance("\t"));
        }
        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Text) {
                final Scml.Text text = (Scml.Text) item;
                final StringBuilder sb = new StringBuilder();
                boolean linePrintedFlag = false;
                final String textLines[] = text.getText().split("\\n");
                for (String textLine : textLines) {
                    if (linePrintedFlag) {
                        sb.append("\n\t");
                    } else {
                        linePrintedFlag = true;
                    }
                    sb.append(textLine);
                }
                text.setText(sb.toString());
            }
        }
    }

    public void print(Scml scml) {
        final Scml copy = scml.getClipboardSupport().copy();
        moveRight(copy);
        final List<Scml.Item> items = copy.getItems().list();
        copy.getItems().clear();
        for (Scml.Item item : items) {
            this.scml.getItems().add(item);
        }
    }

    public void print(Scml.Tag tag) {
        this.scml.getItems().add(tag);
    }

    @Override
    public void print(int l) {
        print(String.valueOf(l));
    }

    @Override
    public int getLineNumber(int globalOffset) {
        throw new UnsupportedOperationException();
    }
}
