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

package org.radixware.kernel.designer.common.dialogs.jmlnb;

import org.netbeans.editor.ext.java.JavaSyntax;
import org.radixware.kernel.common.jml.JmlTagTask;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagBounds;


public class JavaSyntaxWrapper extends JavaSyntax {

    private ScmlDocument document;

    public JavaSyntaxWrapper(ScmlDocument document) {
        super(null);
        this.document = document;
    }

    @Override
    public void relocate(char[] buffer, int offset, int len, boolean lastBuffer, int stopPosition) {
        fixBuffer(buffer);
        super.relocate(buffer, offset, len, lastBuffer, stopPosition);
    }

    private void fixBuffer(char[] buffer) {
        if (buffer.length == document.getLength()) {
            for (TagBounds tagBounds : document.getTagMapper().getBounds()) {
                if (tagBounds.textLocked()) { //bounds are valid
                    int offs = 0;
                    if (tagBounds.getVTag().getTag() instanceof JmlTagTask) {
                        for (int i = 0; i < 2; i++) {
                            buffer[i + tagBounds.getBeginOffset()] = '/';
                        }
                        offs = 2;
                    }
                    for (int i = tagBounds.getBeginOffset() + offs; i < tagBounds.getBeginOffset() + tagBounds.getLength(); i++) {
                        buffer[i] = 't';
                    }
                }
            }
        } 

    }

    @Override
    public void load(StateInfo stateInfo, char[] buffer, int offset, int len, boolean lastBuffer, int stopPosition) {
        fixBuffer(buffer);
        super.load(stateInfo, buffer, offset, len, lastBuffer, stopPosition);
    }
}
