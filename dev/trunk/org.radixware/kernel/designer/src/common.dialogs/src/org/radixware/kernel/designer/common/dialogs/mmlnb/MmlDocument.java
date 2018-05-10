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

package org.radixware.kernel.designer.common.dialogs.mmlnb;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.lexer.Language;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.Scml.Item;
import org.radixware.kernel.common.scml.Scml.Tag;
import org.radixware.kernel.common.scml.Scml.Text;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagCompletionRegion;


public class MmlDocument extends ScmlDocument {

    private CompletionRegion completionRegion;

    public MmlDocument(String mimeType) {
        super(mimeType);
        Language language = MimeLookup.getLookup(mimeType).lookup(Language.class);
        this.putProperty(Language.class, language);
    }

    @Override
    public Scml createDefaultScml() {
        return Mml.Factory.newInstance(null, "default-mml");
    }

    @Override
    public void insertString(int offset, String text, AttributeSet a) throws BadLocationException {
        if (!handleTagCompletionInsert(offset, text, a)) {
            super.insertString(offset, text, a);
        }
    }

    @Override
    public void insertTag(int offs, Tag tag) throws BadLocationException {
        if (completionRegion != null && (offs <= completionRegion.getRegionStart() || offs > completionRegion.getRegionEnd())) {
            return;//ignore modifications outside complete region
        }
        super.insertTag(offs, tag);
    }

    @Override
    public void insertText(int offs, Text text) throws BadLocationException {
        if (completionRegion != null && (offs <= completionRegion.getRegionStart() || offs > completionRegion.getRegionEnd())) {
            return;//ignore modifications outside complete region
        }
        super.insertText(offs, text);
    }

    public void activateCompletionRegion(int offset) throws BadLocationException {
        completionRegion = new CompletionRegion();
        completionRegion.start = offset;
        completionRegion.end = offset + 1;
        putProperty(TagCompletionRegion.class, completionRegion);
        super.insertString(offset, "`", null);
    }

    public void discardCompletionRegion() {
        if (completionRegion != null) {
            putProperty(TagCompletionRegion.class, null);
            try {
                super.remove(completionRegion.getRegionStart(), completionRegion.getRegionEnd() - completionRegion.getRegionStart());
            } catch (BadLocationException ex) {
                throw new IllegalStateException(ex);
            }
            completionRegion = null;
        }
    }

    private boolean handleTagCompletionInsert(int offset, String text, AttributeSet a) throws BadLocationException {
        if (completionRegion == null) {
            return false;
        }
        if (offset <= completionRegion.getRegionStart() || offset > completionRegion.getRegionEnd()) {
            return true;//ignore modifications ouside of the region
        }
        completionRegion.end += text.length();
        super.insertString(offset, text, a);
        return true;
    }

    private boolean handleTagCompletionRemove(int offset, int len) throws BadLocationException {
        if (completionRegion == null) {
            return false;
        }
        int rstart = offset;
        int rend = offset + len;
        if (rstart <= completionRegion.getRegionStart() || rend > completionRegion.getRegionEnd()) {
            return true;//ignore modifications ouside of the region
        }
        completionRegion.end -= len;
        super.remove(offset, len);
        return true;
    }

    @Override
    public void remove(int offset, int len) throws BadLocationException {
        if (!handleTagCompletionRemove(offset, len)) {
            super.remove(offset, len);
        }
    }

    private static class CompletionRegion implements TagCompletionRegion {

        private int start;
        private int end;
        private Scml.Item item = Scml.Text.Factory.newInstance("");

        @Override
        public int getRegionStart() {
            return start;
        }

        @Override
        public int getRegionEnd() {
            return end;
        }

        @Override
        public Item getItem() {
            return item;
        }

        @Override
        public void setItem(Item item) {
            this.item = item;
        }
    }
}