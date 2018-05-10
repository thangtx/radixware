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

import java.util.List;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.types.Id;


public class ScmlCodePrinter extends CodePrinter implements IScmlPrinter {
    private final Scml scml;

    public ScmlCodePrinter(ScmlCodePrinter container) {
        super(container);
        this.scml = container.scml;
    }
    
    public ScmlCodePrinter(Scml buffer) {
        this.scml = buffer;
    }

    @Override
    public char[] getContents() {
        throw new UnsupportedOperationException();
    }

    private CodePrinter print(final String s) {
        final Scml.Text text;
        final RadixObjects<Scml.Item> items = scml.getItems();
        
        if (items.isEmpty() || !(items.get(items.size() - 1) instanceof Scml.Text)) {
            text = Scml.Text.Factory.newInstance(s);
            scml.getItems().add(text);
        } else {
            text = (Scml.Text) items.get(items.size() - 1);
            text.setText(text.getText() + s);
        }
        return this;
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
    public CodePrinter print(Id id) {
        return print(String.valueOf(id));
    }

    @Override
    public CodePrinter print(char c) {
        return print(String.valueOf(c));
    }

    @Override
    public CodePrinter print(boolean b) {
        return print(String.valueOf(b));
    }

    @Override
    public CodePrinter print(CharSequence text) {
        return print(String.valueOf(text));
    }

    @Override
    public CodePrinter print(char[] text) {
        return print(String.valueOf(text));
    }

    @Override
    public CodePrinter print(long l) {
        return print(String.valueOf(l));
    }

    @Override
    public CodePrinter printCommandSeparator() {
        return print(CodePrinter.Factory.newSqlPrinter().printCommandSeparator().toString());
    }

    @Override
    public CodePrinter printStringLiteral(String text) {
        return print(CodePrinter.Factory.newSqlPrinter().printStringLiteral(text).toString());
    }

    @Override
    public CodePrinter println(char[] text) {
        return print(String.valueOf(text));
    }

    private static void moveRight(Scml scml) {
        if (!scml.getItems().isEmpty()) {
            scml.getItems().add(0, Scml.Text.Factory.newInstance("\t"));
        }
        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Text) {
                final StringBuilder sb = new StringBuilder();
                final Scml.Text text = (Scml.Text) item;
                boolean linePrintedFlag = false;
                
                for (String textLine : text.getText().split("\\n")) {
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

    @Override
    public void print(Scml scml) {
        final Scml copy = scml.getClipboardSupport().copy();
        
        moveRight(copy);
        
        final List<Scml.Item> items = copy.getItems().list();

        copy.getItems().clear();
        for (Scml.Item item : items) {
            this.scml.getItems().add(item);
        }
    }

    @Override
    public void print(Scml.Tag tag) {
        this.scml.getItems().add(tag);
    }

    @Override
    public CodePrinter print(int l) {
        return print(String.valueOf(l));
    }

    @Override
    public int getLineNumber(int globalOffset) {
        throw new UnsupportedOperationException();
    }
}
