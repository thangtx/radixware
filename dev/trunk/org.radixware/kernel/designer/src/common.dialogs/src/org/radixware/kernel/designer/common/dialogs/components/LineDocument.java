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

package org.radixware.kernel.designer.common.dialogs.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import javax.swing.text.*;


public class LineDocument extends AbstractDocument {

    public static final String CR_PROPERTY = "LINEDOCUMENT_CR_CHARACTER";
    public static final String LF_PROPERTY = "LINEDOCUMENT_LF_CHARACTER";

    private final Element rootElement;

    public LineDocument() {
        this(new GapContent());

    }

    public LineDocument(Content data) {
        super(data);

        putProperty(LF_PROPERTY, (char) 0x21B2);
        putProperty(CR_PROPERTY, ' ');

        rootElement = createDefaultRoot();
    }

    @Override
    public Element getDefaultRootElement() {
        return rootElement;
    }

    @Override
    public Element getParagraphElement(int pos) {
        return rootElement;
    }

    private Element createDefaultRoot() {
        final LineLeafElement line = new LineLeafElement(null, null, 0, 1);
        final LineRootElement root = new LineRootElement(line, rootElement, null);
        return root;
    }

    @Override
    public void getText(int offset, int length, Segment txt) throws BadLocationException {
        super.getText(offset, length, txt);
        final char[] arr = txt.array;
        txt.array = new char[arr.length];
        System.arraycopy(arr, 0, txt.array, 0, arr.length);
        for (int i = 0; i < arr.length; i++) {
            if (txt.array[i] == '\n') {
                txt.array[i] = (Character) getProperty(LF_PROPERTY);
            }
            if (txt.array[i] == '\r') {
                txt.array[i] = (Character) getProperty(CR_PROPERTY);
            }
        }
    }

    protected class LineLeafElement extends AbstractDocument.LeafElement {

        public LineLeafElement(Element parent, AttributeSet a, int offs0, int offs1) {
            super(parent, a, offs0, offs1);
        }

        @Override
        public Enumeration children() {
            return Collections.emptyEnumeration();
        }
    }

    protected class LineRootElement extends AbstractDocument.AbstractElement {

        LineLeafElement line;

        public LineRootElement(LineLeafElement line, Element parent, AttributeSet a) {
            super(parent, a);
            this.line = line;
        }

        @Override
        public int getStartOffset() {
            return line.getStartOffset();
        }

        @Override
        public int getEndOffset() {
            return line.getEndOffset();
        }

        @Override
        public Element getElement(int index) {
            return line;
        }

        @Override
        public int getElementCount() {
            return 1;
        }

        @Override
        public int getElementIndex(int offset) {
            return 0;
        }

        @Override
        public boolean isLeaf() {
            return false;
        }

        @Override
        public boolean getAllowsChildren() {
            return true;
        }

        @Override
        public Enumeration children() {
            return Collections.enumeration(Arrays.asList(line));
        }
    }

}
