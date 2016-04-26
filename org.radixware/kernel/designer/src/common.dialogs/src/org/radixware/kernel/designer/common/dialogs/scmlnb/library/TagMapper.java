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

import java.awt.Toolkit;
import java.lang.ref.WeakReference;
import java.util.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.netbeans.editor.GuardedException;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;

/**
 * Mapping from offsets to tags and vice versa.
 *
 */
public class TagMapper {

    private final WeakReference<ScmlDocument> doc;
    private final TreeSet<TagBounds> bounds;
    private final List<TagMapperListener> listeners;
    private final Map<Scml.Tag, TagBounds> tag2bounds;

    TagMapper(ScmlDocument doc) {
        this.doc = new WeakReference<>(doc);
        bounds = new TreeSet<>();
        listeners = new LinkedList<>();
        tag2bounds = new WeakHashMap<>();
        doc.addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                //TagBounds postions with offset == 0 are not updated automatically
                if (e.getOffset() == 0 && bounds.size() > 0) {
                    final TagBounds firstBounds = bounds.first();
                    if (firstBounds.getBeginOffset() == 0) {
                        try {
                            firstBounds.setStartPosTo(e.getLength());
                        } catch (BadLocationException ex) {
                            throw new IllegalStateException(ex);
                        }
                    }
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                //do nothing
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                //do nothing
            }
        });
    }

    /**
     * Insert vtag to the given offset.
     *
     * @return {@link TagBounds TagBounds}, which is associated with this vtag
     * @throws javax.swing.text.BadLocationException
     */
    public TagBounds insertTag(final int offs, final VTag vtag) throws BadLocationException {
        TagBounds tagBounds = null;
        try {
            tagBounds = new TagBounds(offs, doc.get(), vtag);
            insertTagBounds(tagBounds);
        } catch (GuardedException ex) {
            Toolkit.getDefaultToolkit().beep();
        }

//        PrintBounds();
        return tagBounds;
    }

    private void insertTagBounds(TagBounds tagBounds) {
        bounds.add(tagBounds);
        tag2bounds.put(tagBounds.getVTag().getTag(), tagBounds);
        fireTagAddedEvent(tagBounds);
    }

    /**
     * Remove bounds that contains this offset.
     *
     * @param offs offset
     * @return true if tag was sucsessfully removed and false if this offset
     * doesn't belong to any tag
     */
    public boolean removeBounds(final int offs) {
        TagBounds tagBounds = findContainingBounds(offs);
        if (tagBounds == null) {
            return false;
        }
        removeBounds(tagBounds);
        return true;
    }

    /**
     * Remove given TagBounds from mapping collection. <p> Notice that this
     * method does not remove text from document
     *
     * @param tb
     */
    public void removeBounds(TagBounds tb) {
        tb.invalidate();
        bounds.remove(tb);
        fireTagRemovedEvent(tb);
    }

    public void removeBounds(Scml.Tag tag) {
        TagBounds tb = tag2bounds.get(tag);
        tag2bounds.remove(tag);
        removeBounds(tb);
    }

    /**
     * Remove all {@link TagBounds} specified in given Collection.<br> Does not
     * remove text from document
     *
     */
    public void removeBounds(Collection<TagBounds> removedBounds) {
        for (TagBounds tb : removedBounds) {
            removeBounds(tb);
        }
    }

    public void removeBounds(int startOffs, int endOffs) {
        removeBounds(getBounds(startOffs, endOffs));
    }

    public void clear() {
        bounds.clear();
        tag2bounds.clear();
    }

//    void PrintBounds() {
//        System.out.println("Bounds:");
//        for (TagBounds tb : bounds) {
//            System.out.print(tb.getBeginPosition().getOffset());
//            System.out.print(" ");
//            System.out.println(tb.getEndPosition().getOffset());
//        }
//    }
    /**
     * Tests whether current offset is inside of some TagBounds.<br> If offset
     * is on the edge of TagBounds, return false
     *
     * @param offs offset
     *
     */
    public boolean insideTagBounds(int offs) {
        TagBounds tagBounds = findContainingBounds(offs);
        if (tagBounds != null && tagBounds.getBeginOffset() < offs && tagBounds.getEndOffset() > offs) {
            return true;
        }
        return false;
    }

    /**
     * Find TagBounds which contains this offset.<br> Contains means that<br>
     * TagBounds.getBeginOffset() &gt;= offs && TagBounds.getEndOffset() &lt;=
     * offs
     *
     * @param offs offset
     * @return TagBounds or null, if there is no such TagBounds
     */
    public TagBounds findContainingBounds(int offs) {
        if (offs < 0 || offs > doc.get().getLength()) {
            return null;
        }
        TagBounds tagBounds = null;
        try {
            TagBounds searchBounds = new TagBounds(offs, offs + 1, doc.get(), null);
            if (bounds.contains(searchBounds)) {
                tagBounds = bounds.subSet(searchBounds, true, searchBounds, true).first();
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);

        }
        return tagBounds;
    }

    /**
     * Try's to find TagMapper instance associated with this document.
     *
     * @return Instance of TagMapper for the given document.
     * @param doc must be instance of ScmlDocument for succesfull result
     *
     */
    public static TagMapper getInstance(Document doc) {
        TagMapper tm;
        tm = (TagMapper) doc.getProperty(TagMapper.class);
        if (tm == null) {
            if (!(doc instanceof ScmlDocument)) {
                return null;
            }
            tm = new TagMapper((ScmlDocument) doc);
            doc.putProperty(TagMapper.class, tm);
        }
        return tm;
    }

    /**
     * Gets all {@link TagBounds} in mapping colection.
     *
     * @return java.util.TreeSet in current implementation
     */
    public Set<TagBounds> getBounds() {
        return bounds;
    }

    /**
     * Gets all bounds between given offsets.<br> If some bounds intersects with
     * given offsets, they will be returned too.
     *
     */
    public NavigableSet<TagBounds> getBounds(int startOffs, int endOffs) {
        NavigableSet<TagBounds> retBounds = null;
        try {
            TagBounds startBounds = new TagBounds(startOffs, startOffs, doc.get(), null);
            TagBounds endBounds = new TagBounds(endOffs, endOffs, doc.get(), null);
            retBounds = bounds.subSet(startBounds, true, endBounds, true);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        return retBounds;

    }

    public TagBounds getTagBounds(Scml.Tag tag) {
        return tag2bounds.get(tag);
    }

    /**
     * Calls {@link TagBounds#update() update()} for all TagBounds in mapping
     * collection.
     *
     */
    public void update() {
        for (TagBounds tb : bounds) {
            update(tb);
        }
    }

    /**
     * Calls {@link TagBounds#update() update()} for given TagBounds.
     *
     */
    public void update(TagBounds tb) {
        tb.update();
        fireTagUpdatedEvent(tb);
    }

    private void fireTagAddedEvent(TagBounds tagBounds) {
        for (TagMapperListener tml : listeners) {
            tml.tagAdded(tagBounds);
        }
    }

    private void fireTagRemovedEvent(TagBounds tagBounds) {
        for (TagMapperListener tml : listeners) {
            tml.tagRemoved(tagBounds);
        }
    }

    private void fireTagUpdatedEvent(TagBounds tagBounds) {
        for (TagMapperListener tml : listeners) {
            tml.tagUpdated(tagBounds);
        }
    }

    public void addListener(TagMapperListener tml) {
        listeners.add(tml);
    }

    public void removeListener(TagMapperListener tml) {
        listeners.remove(tml);
    }
}
