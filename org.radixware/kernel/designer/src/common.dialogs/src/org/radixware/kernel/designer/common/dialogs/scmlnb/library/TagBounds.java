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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.lang.ref.WeakReference;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Position;
import javax.swing.text.Position.Bias;
import javax.swing.text.StyledDocument;
import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.text.NbDocument;
import org.openide.util.Exceptions;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;

/**
 * Class for holding tag position that implements Comparable interface. Also it
 * stores information about tag
 *
 */
public class TagBounds extends Object implements Comparable {

    private Position beginPosition;
    private String title;
    private final WeakReference<NbEditorDocument> document;
    /**
     * This flag helps to prevent unlike modifications of the Tag's text
     */
    private boolean textLock = false;
    private final VTag vtag;

    TagBounds(int startOffs, int endOffs, Document doc, VTag vtag) throws BadLocationException {
        this.vtag = vtag;
        title = doc.getText(startOffs, endOffs - startOffs);
        this.beginPosition = ((NbEditorDocument) doc).createPosition(startOffs, Bias.Forward);
        this.document = new WeakReference<>((NbEditorDocument) doc);
        lockText();
    }

    TagBounds(int offs, Document doc, VTag vtag) throws BadLocationException {
        this.vtag = vtag;
        this.document = new WeakReference<>((NbEditorDocument) doc);
        title = vtag.getTitle();
        doc.insertString(offs, title, null);
        this.beginPosition = ((NbEditorDocument) doc).createPosition(offs, Bias.Forward);
        lockText();
    }

    public void setStartPosTo(final int offset) throws BadLocationException {
        this.beginPosition = (document.get()).createPosition(offset, Bias.Forward);
    }

    public void update() {
        setText(vtag.getTitle());
    }

    public void setText(final String text) {
        if (title != null && title.equals(text)) {
            return;
        }
        assert text.length() > 0 : "Text length must be non zero";
        final StyledDocument doc = document.get();
        NbDocument.runAtomic(doc, new Runnable() {

            @Override
            public void run() {
                try {
                    unlockText();
                    int len = getLength();
                    int offs = getBeginOffset();
                    doc.insertString(offs + len, text, null);
                    if (len > 0) {
                        doc.remove(offs, len);
                    }
                    title = text;
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                } finally {
                    lockText();
                }

            }
        });
    }

    public void invalidate() {
        title = null;
        unlockText();
    }

    public final void lockText() {
        textLock = true;
    }

    public void unlockText() {
        textLock = false;
    }

    public boolean textLocked() {
        return textLock;
    }

    public String getTooltip() {
        return vtag.getToolTip();
    }

    public Position getBeginPosition() {
        return beginPosition;
    }

    public Position getEndPosition() {
        throw new UnsupportedOperationException("Don't use it");
    }

    public int getBeginOffset() {
        return beginPosition.getOffset();
    }

    public int getEndOffset() {
        return beginPosition.getOffset() + getLength();
    }

    public int getLength() {
        return title == null ? 0 : title.length();
    }

    public VTag getVTag() {
        return vtag;
    }

    @Override
    public int compareTo(Object obj) {
        if (!(obj instanceof TagBounds)) {
            return -1;
        }
        TagBounds other = (TagBounds) obj;

        if (this == other) {
            return 0;
        }

        if (getEndOffset() <= other.getBeginOffset()) {
            return -1;
        }

        if (getBeginOffset() >= other.getEndOffset()) {
            return 1;
        }

        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + "[" + getBeginOffset() + "," + getEndOffset() + "]" + " locked = " + textLock;
    }
}
