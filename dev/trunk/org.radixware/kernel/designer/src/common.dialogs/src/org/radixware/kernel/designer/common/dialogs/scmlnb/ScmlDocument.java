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

package org.radixware.kernel.designer.common.dialogs.scmlnb;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Position;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import org.netbeans.api.editor.mimelookup.MimeLookup;
import org.netbeans.api.lexer.Language;
import org.netbeans.editor.*;
import org.netbeans.lib.editor.util.AbstractCharSequence;
import org.netbeans.lib.editor.util.swing.DocumentUtilities;
import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.text.Annotation;
import org.openide.text.Line;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangedEvent;
import org.radixware.kernel.common.defs.RadixObjects.ContainerChangesListener;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.findsupport.visual.ReplaceAction;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTag;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane.VTagFactory;
import org.radixware.kernel.designer.common.dialogs.scmlnb.finder.ScmlLocation;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagBounds;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagMapper;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;

/**
 * Document for holding Scml <p> Permits unlike changes in Tag's text.
 *
 */
public abstract class ScmlDocument extends NbEditorDocument {

    public static final String UNDO_OR_REDO_IN_PROGRESS = "undo-or-redo-in-progress";
    public static final String REBUILD_IN_PROGRESS = "rebuild-in-progress";
    public static final String ATOMIC_EDIT_IN_PROGRESS = "atomic-edit-in-progress";
    public static final String UPDATE_IN_PROGRESS = "update-in-progress";
    private static final boolean DEBUG = System.getProperty("scml.document.debug") != null;
    private TagMapper tagMapper;
    private ScmlAnnotations scmlAnnotations;
    private Scml scml;
    private boolean autoSaveEnabled = true;
    private final Stack<Boolean> oldAutoSave = new Stack<Boolean>();
    private int atomicDeep = 0;
    private UndoManager undoRedoManager;
    private VTagFactory factory;
    private final List<UndoableEditListener> undoListeners = new LinkedList<UndoableEditListener>();
    private static final String INSERT_TO_TAG_AREA = "insert-to-tag-area";
    private int lastChangeEndOffset = 0;
    private Map hackedAnnoMap;
    private boolean firstAtomicEdit = false;
    private boolean editInProgress = false;
    private boolean valid = true;
    private final ContainerChangesListener scmlListener = new ContainerChangesListener() {
        @Override
        public void onEvent(ContainerChangedEvent e) {
            //If someone else has changed scml content, we must
            //update our inner structures for proper work.
            //One of the problems that may happen when structures are outdated is #RADIX-3308.
            if (!editInProgress) {
                invalidate();
            }
        }
    };

    public ScmlDocument(Class kitClass) {
        super(kitClass);

        putProperty(ReplaceAction.SEQUENCE_KEY, new DocumentCharView());
    }

    public ScmlDocument(String mimeType) {
        super(mimeType);
        Language language = MimeLookup.getLookup(mimeType).lookup(Language.class);
        putProperty(Language.class, language);
        putProperty(ReplaceAction.SEQUENCE_KEY, new DocumentCharView());

        this.scml = createDefaultScml();
        tagMapper = TagMapper.getInstance(this);
        undoRedoManager = new ScmlUndoManager();
        undoRedoManager.setLimit(1000);
        undoListeners.add(undoRedoManager);
        hackAnnoMap();//to find position of annotation for given problem
        scmlAnnotations = new ScmlAnnotations(this);
        setAutoSaveEnabled(true);
    }

    private void hackAnnoMap() {
        try {
            Field annoMapField = NbEditorDocument.class.getDeclaredField("annoMap");
            annoMapField.setAccessible(true);
            try {
                hackedAnnoMap = (HashMap) annoMapField.get(this);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            }
        } catch (NoSuchFieldException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void ensureIsValid() {
        if (!valid) {
            throw new InvalidScmlDocumentException("Scml has been modified outside of the editor");
        }
    }

    //marks that scml has been changed from outside
    private void invalidate() {
        this.valid = false;
    }

    public Line getLine(int offs) {
        return new ScmlDocumentLine(this, offs);
    }

    public int getProblemOffset(RadixProblem problem) {
        List<ScmlAnnotation> annotations = scmlAnnotations.getProblemAnnotations(problem);
        if (annotations != null && !annotations.isEmpty()) {
            AnnotationDesc annotationDesc = (AnnotationDesc) hackedAnnoMap.get(annotations.get(0));
            if (annotationDesc == null) {
                return -1;
            }
            return annotationDesc.getOffset();
        }
        return -1;
    }

    public abstract Scml createDefaultScml();

    public void setFactory(VTagFactory factory) {
        this.factory = factory;
    }

    public VTagFactory getFactory() {
        return factory;
    }

    public int getLastChangeEndOffset() {
        return lastChangeEndOffset;
    }

    public void update(Scml.Tag tag) {
        setAutoSaveEnabled(false);
        tagMapper.update(tagMapper.getTagBounds(tag));
        revertAutoSave();
    }

    public TagMapper getTagMapper() {
        return tagMapper;
    }

    /**
     * Checks whether scml contains empty blocks.
     */
    private void checkForEmptyBlocks() {
        if (!DEBUG) {
            return;//no debug
        }
        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Text) {
                if (((Scml.Text) item).getText().length() == 0) {
                    Logger.getLogger(ScmlDocument.class.getName()).fine("Scml contains empty text blocks");
                }
            }
        }
    }

    /**
     * Checks whether the currently displayed text is correct view of scml
     */
    private void checkScmlToTextReflection() {
        if (!DEBUG) {
            return;//no debug
        }
        try {
            String expectedText = buildText();
            String actualText = getText(0, getLength());
            if (!expectedText.equals(actualText)) {
                String prefix = "ScmlEditor_error_report_" + Calendar.getInstance().getTime().toString().replace(':', '_').replace(' ', '_');
                File reportFile = File.createTempFile(prefix, "txt");
                int index = 1;
                while (index < 100 && reportFile.exists()) {
                    reportFile = new File(prefix + '#' + index);
                    index++;
                }
                PrintWriter pw = new PrintWriter(reportFile);

                pw.println("Actual text:");
                pw.println(actualText);

                pw.println();
                pw.println("###################################################");
                pw.println();

                pw.println("Expected text:");
                pw.println(expectedText);
                pw.flush();

                rebuildDocument();
                throw new IllegalStateException("Internal error occured after last editing. "
                        + "Document has been reloaded, but some data can be lost. Please, check your code for corruption.\n"
                        + "Debug info has been written to " + reportFile.getAbsolutePath());
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private String buildText() {
        StringBuilder scmlSb = new StringBuilder();
        for (Scml.Item item : scml.getItems()) {
            if (item instanceof Scml.Tag) {
                VTag vtag = factory.createVTag((Scml.Tag) item);
                scmlSb.append(vtag.getTitle());
            } else {
                scmlSb.append(((Scml.Text) item).getText());
            }
        }
        return scmlSb.toString();
    }
    // document listener for writing typing changes
    private DocumentListener saveListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {
            try {
                String str = getText(e.getOffset(), e.getLength());
                int scmlOffset = textOffsetToScmlOffset(e.getOffset());
                insertTextAtScmlOffset(createText(str), scmlOffset, false);
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }

        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            //happens when user has typed text at the end of the document
            if (e.getType() == DocumentEvent.EventType.INSERT) {
                return;
            }

            final int p0 = textOffsetToScmlOffset(e.getOffset());
            final int p1 = textOffsetToScmlOffset(e.getOffset() + e.getLength());

            runAtomic(new Runnable() {
                @Override
                public void run() {
                    removeFromScmlOffset(p0, p1 - p0, false);
                }
            });
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    };

    public UndoManager getUndoRedo() {
        return undoRedoManager;
    }

    private void enableAutoSave() {
        addPostModificationDocumentListener(saveListener);
        autoSaveEnabled = true;
    }

    private void disableAutoSave() {
        removePostModificationDocumentListener(saveListener);
        autoSaveEnabled = false;
    }

    private void setAutoSaveEnabledImpl(boolean enabled) {
        if (enabled) {
            enableAutoSave();
        } else {
            disableAutoSave();
        }
    }

    private void setAutoSaveEnabled(boolean enabled) {
        oldAutoSave.push(autoSaveEnabled);
        setAutoSaveEnabledImpl(enabled);
    }

    private void revertAutoSave() {
        if (oldAutoSave.peek() == autoSaveEnabled) {
            oldAutoSave.pop();
        } else {
            setAutoSaveEnabledImpl(oldAutoSave.pop());
        }
    }

    /**
     * Returns array containing 2 integers:<p> First - Index of Scml.Item at
     * given position.<br> Second - Offset relative to the begining of this
     * Item.<p> Returns null, if given position doesn't belong to the document.
     *
     * @param pos Position in text
     * @return
     */
    public int[] itemIndexAndOffset(int pos) {
        ensureIsValid();

        if (pos > getLength()) {
            return null;
        }
        Iterator<Scml.Item> it = scml.getItems().iterator();
        Scml.Item item = null;
        int idx = -1;
        int begin = 0;
        int end = 0;
        while (end <= pos && it.hasNext()) {
            item = it.next();
            begin = end;
            if (item instanceof Scml.Text) {
                end += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                end += tagMapper.getTagBounds((Scml.Tag) item).getLength();
            }
            idx++;
        }
        return new int[]{idx, pos - begin};
    }

    /**
     * Returns offset of the first char in item's text representation relative
     * to the document beginnig. If there is no item with such index, returns
     * -1.
     *
     * @param idx - index of item
     */
    public int getItemStartOffset(int idx) {
        ensureIsValid();

        if (idx >= scml.getItems().size()) {
            return -1;
        }
        int offs = 0;
        Scml.Item item;
        Iterator<Scml.Item> it = scml.getItems().iterator();
        for (int i = 0; i < idx; i++) {
            item = it.next();
            if (item instanceof Scml.Text) {
                offs += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                TagBounds bounds = tagMapper.getTagBounds((Scml.Tag) item);
                if (bounds != null) {
                    offs += bounds.getLength();
                }
            }
        }
        return offs;
    }

    /**
     * Returns array containing 2 integers:<p> First - Offset of the first char
     * in item's text representation relative to the document beginnig.<br>
     * Second - Lenght of item's text representation.<p> Returns null, if there
     * is no item with such index.
     *
     * @param pos Position in text
     */
    public int[] itemOffsetAndLength(int idx) {
        ensureIsValid();

        if (idx < 0 || idx >= scml.getItems().size()) {
            return null;
        }
        int offs = 0, len = 0;
        Scml.Item item;
        Iterator<Scml.Item> it = scml.getItems().iterator();
        for (int i = 0; i < idx; i++) {
            item = it.next();
            if (item instanceof Scml.Text) {
                offs += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                final TagBounds tb = tagMapper.getTagBounds((Scml.Tag) item);
                if (tb != null) {
                    offs += tb.getLength();
                } else {
                    throw new IllegalStateException(item + " appears to be in scml, but not found in TagMapper");
                }
            }
        }
        item = it.next();
        if (item instanceof Scml.Text) {
            len = ((Scml.Text) item).getText().length();
        } else if (item instanceof Scml.Tag) {
            final TagBounds tb = tagMapper.getTagBounds((Scml.Tag) item);
            if (tb != null) {
                //offs += tb.getLength();
                len = tb.getLength();
            } else {
                throw new IllegalStateException(item + " appears to be in scml, but not found in TagMapper");
            }
        }
        return new int[]{offs, len};
    }

    private int itemIndexAtScmlOffset(int scmlOffset) {
        Iterator<Scml.Item> it = scml.getItems().iterator();
        Scml.Item item = null;
        int idx = -1;
        int p = 0;
        while (p <= scmlOffset && it.hasNext()) {
            item = it.next();
            if (item instanceof Scml.Text) {
                p += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                p += 1;
            }
            idx++;
        }
        return idx;
    }

    private int getItemScmlStartOffset(int idx) {
        int offs = 0;
        Scml.Item item;
        Iterator<Scml.Item> it = scml.getItems().iterator();
        for (int i = 0; i < idx; i++) {
            item = it.next();
            if (item instanceof Scml.Text) {
                offs += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                offs += 1;
            }
        }
        return offs;
    }

    private void clearContent() {
        tagMapper.clear();
        try {
            remove(0, getLength());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void runAtomic(Runnable r) {
        ensureIsValid();

        startAtomicEdit();

        super.runAtomic(r);

        endAtomicEdit();
    }

    @Override
    public void runAtomicAsUser(Runnable r) {
        ensureIsValid();

        startAtomicEdit();

        super.runAtomicAsUser(r);

        endAtomicEdit();
    }

    private void startAtomicEdit() {
        if (atomicDeep == 0) {
            firstAtomicEdit = true;
            putProperty(ATOMIC_EDIT_IN_PROGRESS, this);
        }
        atomicDeep++;
    }

    private void endAtomicEdit() {
        assert atomicDeep > 0;
        atomicDeep--;
        if (atomicDeep == 0) {
            putProperty(ATOMIC_EDIT_IN_PROGRESS, null);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    fireChangedUpdate(new BaseDocumentEvent(ScmlDocument.this, -1, -1, DocumentEvent.EventType.CHANGE));
                }
            });
        }
    }

    private boolean isAtomicEdit() {
        return atomicDeep > 0;
    }

    private boolean isSignificantEdit() {
        if (!isAtomicEdit()) {
            return true;
        }
        if (firstAtomicEdit) {
            firstAtomicEdit = false;
            return true;
        }
        return false;
    }

    private void rebuildDocument() {
        assert scml != null;
        putProperty(REBUILD_IN_PROGRESS, Boolean.TRUE);
        setAutoSaveEnabled(false);
        clearContent();
        try {
            for (Scml.Item item : scml.getItems()) {
                if (item instanceof Scml.Text) {
                    Scml.Text text = (Scml.Text) item;
                    insertString(getLength(), text.getText(), null);
                }
                if (item instanceof Scml.Tag) {
                    Scml.Tag tag = (Scml.Tag) item;
                    tagMapper.insertTag(getLength(), factory.createVTag(tag));
                }
            }
        } catch (BadLocationException ex) {
            //should never be happend
            Exceptions.printStackTrace(ex);
        }
        putProperty(REBUILD_IN_PROGRESS, null);
        fireChangedUpdateUnderReadLock(new BaseDocumentEvent(ScmlDocument.this, -1, -1, DocumentEvent.EventType.CHANGE));

        revertAutoSave();
    }

    @Override
    public void insertString(int offset, String text, AttributeSet a) throws BadLocationException {
        super.insertString(offset, text, a);
        lastChangeEndOffset = offset + text.length();
    }

    @Override
    public void remove(int offset, int len) throws BadLocationException {
        int p0 = offset;
        int p1 = offset + len;

        NavigableSet<TagBounds> bounds = tagMapper.getBounds(p0, p1);

        if (bounds != null && bounds.size() > 0) {

            p0 = Math.min(p0, bounds.first().getBeginOffset());
            p1 = Math.max(p1, bounds.last().getEndOffset());

            for (TagBounds tb : bounds) {
                tb.unlockText();
            }

            offset = p0;
            len = p1 - p0;
        }
        lastChangeEndOffset = offset;
        super.remove(offset, len);
    }

    public void insertItems(final RadixObjects<Scml.Item> items, final int offs) throws BadLocationException {
        if (items == null || items.size() == 0) {
            return;
        }
        setAutoSaveEnabled(false);
        final BadLocationException[] blex = new BadLocationException[1];
        runAtomic(new Runnable() {
            @Override
            public void run() {
                try {
                    Scml.Item item;
                    item = items.get(0);
                    item.delete();
                    insertItem(item, offs);
                    while (items.size() > 0) {
                        item = items.get(0);
                        item.delete();
                        insertItem(item, lastChangeEndOffset);
                    }
                } catch (BadLocationException ex) {
                    blex[0] = ex;
                }
            }
        });
        revertAutoSave();
        if (blex[0] != null) {
            throw blex[0];
        }
    }

    public void insertItem(Scml.Item item, int offs) throws BadLocationException {
        if (item instanceof Scml.Tag) {
            insertTag(offs, (Scml.Tag) item);
        } else {
            insertText(offs, (Scml.Text) item);
        }
    }

    public int textOffsetToScmlOffset(int textOffset) {
        ensureIsValid();

        int scmlOffset = 0;
        Iterator<Scml.Item> it = scml.getItems().iterator();
        Scml.Item item;
        while (textOffset > 0) {
            if (!it.hasNext()) {
                throw new IllegalStateException("Wrong text to scml offsets conversion. Scml end's before given text offset.");
            }
            item = it.next();
            if (item instanceof Scml.Tag) {
                scmlOffset += 1;
                textOffset -= tagMapper.getTagBounds((Scml.Tag) item).getLength();
            } else {
                scmlOffset += ((Scml.Text) item).getText().length();
                textOffset -= ((Scml.Text) item).getText().length();
            }
        }

        if (textOffset < 0) {
            scmlOffset += textOffset;
        }

        return scmlOffset;
    }

    public int scmlOffsetToTextOffset(int scmlOffset) {
        ensureIsValid();

        int textOffset = 0;
        Iterator<Scml.Item> it = scml.getItems().iterator();
        Scml.Item item;
        while (scmlOffset > 0) {
            if (!it.hasNext()) {
                throw new IllegalStateException("Wrong text to scml offsets conversion.");
            }
            item = it.next();
            if (item instanceof Scml.Tag) {
                scmlOffset -= 1;
                textOffset += tagMapper.getTagBounds((Scml.Tag) item).getLength();
            } else {
                scmlOffset -= ((Scml.Text) item).getText().length();
                textOffset += ((Scml.Text) item).getText().length();
            }
        }

        if (scmlOffset < 0) {
            textOffset += scmlOffset;
        }

        return textOffset;
    }

    private void insertTagAtScmlOffset(Scml.Tag tag, int scmlOffset) throws BadLocationException {
        editInProgress = true;
        try {
            insertTagAtScmlOffsetImpl(tag, scmlOffset);
        } finally {
            editInProgress = false;
        }

    }

    private void insertTagAtScmlOffsetImpl(Scml.Tag tag, int scmlOffset) throws BadLocationException {
        assert tag != null : "Can't insert null tag";

        if (tag == null) {
            return;
        }
        if (tag.canDelete()) {
            tag.delete();
        }
        int p = 0, idx = 0, endOffs, textOffs = 0;
        RadixObjects<Scml.Item> items = scml.getItems();
        for (Scml.Item item : items) {
            if (p == scmlOffset) {
                items.add(idx, tag);
                break;
            } else {
                if (item instanceof Scml.Tag) {
                    p++;
                    textOffs += tagMapper.getTagBounds((Scml.Tag) item).getLength();
                } else {
                    endOffs = p + ((Scml.Text) item).getText().length();
                    textOffs += endOffs - p;
                    if (scmlOffset < endOffs) {
                        String s = ((Scml.Text) item).getText();
                        String s1 = s.substring(0, s.length() - endOffs + scmlOffset);
                        String s2 = s.substring(s.length() - endOffs + scmlOffset);
                        textOffs -= s2.length();
                        items.remove(idx);
                        items.add(idx, createText(s1));
                        items.add(idx + 1, tag);
                        items.add(idx + 2, createText(s2));
                        break;
                    }
                    p = endOffs;
                }
                idx++;
            }
        }
        if (idx == scml.getItems().size()) {
            items.add(tag);
        }
        setAutoSaveEnabled(false);
        tagMapper.insertTag(textOffs, factory.createVTag(tag));
        revertAutoSave();

        if (getProperty(UNDO_OR_REDO_IN_PROGRESS) == null) {
            fireUndoableEditHappened(new ScmlUndoableEdit(
                    this, tag, scmlOffset, isSignificantEdit(), ScmlUndoableEdit.TYPE_INSERT));
        }


        checkScmlToTextReflection();
        checkForEmptyBlocks();
    }

    private void insertTextAtScmlOffset(Scml.Text text, int scmlOffset, boolean insertToDocument) {
        editInProgress = true;
        try {
            insertTextAtScmlOffsetImpl(text, scmlOffset, insertToDocument);
        } finally {
            editInProgress = false;
        }
    }

    private void insertTextAtScmlOffsetImpl(Scml.Text text, int scmlOffset, boolean insertToDocument) {
        assert text != null : "Can't insert null text";
        RadixObjects<Scml.Item> items = scml.getItems();
        Iterator<Scml.Item> it = items.iterator();
        Scml.Item item = null;
        int p = 0;
        int idx = 0;

        if (insertToDocument) {
            setAutoSaveEnabled(false);
            try {
                int textOffs = scmlOffsetToTextOffset(scmlOffset);
                insertString(textOffs, text.getText(), null);
                lastChangeEndOffset = textOffs + text.getText().length();
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
            revertAutoSave();
        }

        if (!it.hasNext()) {
            items.add(text);
        } else {
            while (p <= scmlOffset && it.hasNext()) {
                item = it.next();
                if (item instanceof Scml.Text) {
                    p += ((Scml.Text) item).getText().length();
                    if (p == scmlOffset) {
                        break;
                    }
                } else if (item instanceof Scml.Tag) {
                    p += 1;
                }
                idx++;
            }

            if (item instanceof Scml.Tag) {
                if (idx == scml.getItems().size() && p == scmlOffset) {
                    items.add(text);
                } else {
                    items.add(idx - 1, text);
                }
            } else {
                assert item instanceof Scml.Text;

                Scml.Text textItem = (Scml.Text) item;
                StringBuilder sb = new StringBuilder(textItem.getText());
                int pos = textItem.getText().length() - p + scmlOffset;
                sb.insert(pos, text.getText());

                textItem.setText(sb.toString());
            }
        }

        if (getProperty(UNDO_OR_REDO_IN_PROGRESS) == null) {
            fireUndoableEditHappened(new ScmlUndoableEdit(
                    this, text, scmlOffset, isSignificantEdit(), ScmlUndoableEdit.TYPE_INSERT));
        }

        
        checkScmlToTextReflection();
        checkForEmptyBlocks();
    }

    private void insertItemAtScmlOffset(Scml.Item item, int scmlOffset) throws BadLocationException {
        if (item instanceof Scml.Tag) {
            insertTagAtScmlOffset((Scml.Tag) item, scmlOffset);
        } else if (item instanceof Scml.Text) {
            insertTextAtScmlOffset((Scml.Text) item, scmlOffset, true);
        } else {
            throw new IllegalStateException("Attempt to insert illegal object into scml");
        }
    }

    private void removeFromScmlOffset(int scmlOffset, int len, boolean removeFromDocument) {
        editInProgress = true;
        try {
            removeFromScmlOffsetImpl(scmlOffset, len, removeFromDocument);
        } finally {
            editInProgress = false;
        }

    }

    private void removeFromScmlOffsetImpl(int scmlOffset, int len, boolean removeFromDocument) {

        int p0 = scmlOffset;
        int i0 = itemIndexAtScmlOffset(p0);
        int p1 = p0 + len;
        int i1 = itemIndexAtScmlOffset(p1);
        if (p1 == getItemScmlStartOffset(i1)) {
            i1--;
        }

        int t1 = scmlOffsetToTextOffset(p0);
        int t2 = scmlOffsetToTextOffset(p1);

        boolean createUndoRedoEvents = (getProperty(UNDO_OR_REDO_IN_PROGRESS) == null);

        Scml.Item item = scml.getItems().get(i0);

        if (i0 == i1) {
            if (item instanceof Scml.Tag) {
                tagMapper.removeBounds((Scml.Tag) item);
                scml.getItems().remove(i0);
                if (createUndoRedoEvents) {
                    fireUndoableEditHappened(new ScmlUndoableEdit(
                            this, item, scmlOffset, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE));
                }
                if (i0 > 0) {
                    mergeWithNextItem(i0 - 1);
                }
            } else {
                int pos = p0 - getItemScmlStartOffset(i0);
                Scml.Text textItem = (Scml.Text) item;
                if (len < textItem.getText().length()) {
                    StringBuilder sb = new StringBuilder(textItem.getText());
                    String removedString = sb.substring(pos, pos + len);
                    if (createUndoRedoEvents) {
                        fireUndoableEditHappened(new ScmlUndoableEdit(
                                this, createText(removedString), p0, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE));
                    }
                    textItem.setText(sb.delete(pos, pos + len).toString());
                } else {
                    scml.getItems().remove(i0);
                    if (createUndoRedoEvents) {
                        fireUndoableEditHappened(new ScmlUndoableEdit(
                                this, item, scmlOffset, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE));
                    }
                }
            }

            if (removeFromDocument) {

                setAutoSaveEnabled(false);
                try {
                    remove(t1, t2 - t1);
                    lastChangeEndOffset = t1;
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
                revertAutoSave();
            }
            
            checkScmlToTextReflection();
            checkForEmptyBlocks();
            return;
        }

        int removedLen = 0;
        ScmlUndoableEdit firstEdit = null;

        if (item instanceof Scml.Text) {
            int pos = p0 - getItemScmlStartOffset(i0);
            if (pos != 0) {
                Scml.Text textItem = (Scml.Text) item;
                removedLen = textItem.getText().length() - pos;
                if (createUndoRedoEvents) {
                    firstEdit = new ScmlUndoableEdit(
                            this, createText(textItem.getText().substring(pos)),
                            p0, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE);
                }
                textItem.setText(textItem.getText().substring(0, pos));
                i0++;
            }
        }

        item = scml.getItems().get(i1);
        ScmlUndoableEdit lastEdit = null;

        if (firstEdit != null) {
            fireUndoableEditHappened(firstEdit);
        }

        for (int i = i0; i < i1; i++) {
            item = scml.getItems().get(i0);
            if (item instanceof Scml.Tag) {
                removedLen += 1;
                tagMapper.removeBounds((Scml.Tag) item);
            } else {
                removedLen += ((Scml.Text) item).getText().length();
            }
            if (createUndoRedoEvents) {
                fireUndoableEditHappened(new ScmlUndoableEdit(
                        this, item, p0, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE));
            }
            scml.getItems().remove(i0);
        }

        item = scml.getItems().get(i0);
        int pos = len - removedLen;

        if (item instanceof Scml.Text && pos < ((Scml.Text) item).getText().length()) {
            Scml.Text textItem = (Scml.Text) item;
            String removedString = textItem.getText().substring(0, pos);
            if (createUndoRedoEvents) {
                lastEdit = new ScmlUndoableEdit(
                        this, createText(removedString),
                        p0, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE);
            }
            textItem.setText(textItem.getText().substring(pos));
        } else {
            if (item instanceof Scml.Tag) {
                tagMapper.removeBounds((Scml.Tag) item);
            }
            scml.getItems().remove(i0);
            if (createUndoRedoEvents) {
                lastEdit = new ScmlUndoableEdit(
                        this, item,
                        p0, isSignificantEdit(), ScmlUndoableEdit.TYPE_REMOVE);
            }
        }

        if (lastEdit != null) {
            fireUndoableEditHappened(lastEdit);
        }

        if (i0 > 0) {
            mergeWithNextItem(i0 - 1);
        }

        if (removeFromDocument) {
            setAutoSaveEnabled(false);
            try {
                remove(t1, t2 - t1);
                lastChangeEndOffset = t1;
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
            revertAutoSave();
        }

        
        checkScmlToTextReflection();
        checkForEmptyBlocks();

    }

    private boolean mergeWithNextItem(int idx) {
        Scml.Item item = scml.getItems().get(idx);
        if (item instanceof Scml.Text) {
            Scml.Text lItem = (Scml.Text) item;
            if (scml.getItems().size() > idx + 1) {
                item = scml.getItems().get(idx + 1);
                if (item instanceof Scml.Text) {
                    Scml.Text rItem = (Scml.Text) item;
                    lItem.setText(lItem.getText() + rItem.getText());
                    scml.getItems().remove(idx + 1);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canInsert(int offs) {
        boolean can = true;
        try {
            preInsertCheck(offs, "", null);
        } catch (BadLocationException ex) {
            can = false;
        }
        return can;
    }

    public void insertText(final int offs, final Scml.Text text) throws BadLocationException {
        assert text != null;
        if (text.canDelete()) {
            text.delete();
        }
        if (canInsert(offs)) {
            int scmlOffset = textOffsetToScmlOffset(offs);
            insertTextAtScmlOffset(text, scmlOffset, true);
        } else {
            throw new BadLocationException("Can't insert text at offset " + offs, offs);
        }
    }

    public void insertTag(final int offs, final Scml.Tag tag) throws BadLocationException {
        if (canInsert(offs)) {
            insertTagAtScmlOffset(tag, textOffsetToScmlOffset(offs));
        } else {
            throw new BadLocationException("Can't insert tag at offset " + offs, offs);
        }
    }

    public void setScml(Scml scml) {
        synchronized (this) {
            assert scml != null;
            if (this.scml == scml) {
                update();
                return;
            }
            if (this.scml != null) {
                this.scml.getItems().getContainerChangesSupport().removeEventListener(scmlListener);
            }
            this.scml = scml;
            setAutoSaveEnabled(false);
            rebuildDocument();
            valid = true;
            oldAutoSave.clear();
            setAutoSaveEnabled(true);
            scmlAnnotations.update();
            this.scml.getItems().getContainerChangesSupport().addEventListener(scmlListener);
        }
    }

    public Scml getScml() {
        return scml;
    }

    private Scml.Text createText(String s) {
        return Scml.Text.Factory.newInstance(s);
    }

    public boolean isValid() {
        return valid;
    }

    public void update() {
        putProperty(UPDATE_IN_PROGRESS, this);
        setAutoSaveEnabled(false);
        tagMapper.update();
        try {
            if (!valid || !buildText().equals(getText(0, getLength()))) {
                rebuildDocument();
                valid = true;
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        revertAutoSave();
        putProperty(UPDATE_IN_PROGRESS, null);
        final DocumentEvent e = new BaseDocumentEvent(this, -1, -1, DocumentEvent.EventType.CHANGE);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                fireChangedUpdateUnderReadLock(e);
            }
        });

    }

    private void fireChangedUpdateUnderReadLock(final DocumentEvent e) {
        render(new Runnable() {
            public void run() {
                fireChangedUpdate(e);
            }
        });
    }

    protected String getPreviewContent() {
        return "No preview content available";
    }

    @Override
    protected void preInsertCheck(int offset, String text, AttributeSet a)
            throws BadLocationException {
        super.preInsertCheck(offset, text, a);

        TagMapper tm = TagMapper.getInstance(this);
        TagBounds tb = tm.findContainingBounds(offset);

        if (tb != null) {
            if (offset > tb.getBeginOffset() && offset < tb.getEndOffset() && tb.textLocked()) {
                throw new GuardedException(NbBundle.getBundle(ScmlDocument.class).getString(INSERT_TO_TAG_AREA)
                        + ". Offset = " + offset + "; tag at offset: " + tb.getVTag().getTitle(), offset);
            }

        }
    }

    @Override
    protected void preRemoveCheck(int offset, int len) throws BadLocationException {
        super.preRemoveCheck(offset, len);
        if (getProperty(BaseKit.DOC_REPLACE_SELECTION_PROPERTY) != null) {
            return;
        }

        TagMapper tm = TagMapper.getInstance(this);
        TagBounds tb = tm.findContainingBounds(offset);

        if (tb != null) {
            if (offset < tb.getEndOffset() && tb.textLocked()) {
                throw new GuardedException(NbBundle.getBundle(ScmlDocument.class).getString(INSERT_TO_TAG_AREA), offset);
            }
        }
    }

    boolean isReadOnly() {
        return scml.isReadOnly();
    }

    @Override
    public void addUndoableEditListener(UndoableEditListener listener) {
        undoListeners.add(listener);
    }

    @Override
    public UndoableEditListener[] getUndoableEditListeners() {
        return undoListeners.toArray(new UndoableEditListener[undoListeners.size()]);
    }

    @Override
    public void removeUndoableEditListener(UndoableEditListener listener) {
        undoListeners.remove(listener);
    }

    private void fireUndoableEditHappened(UndoableEdit edit) {
        UndoableEditEvent event = new UndoableEditEvent(this, edit);
        for (UndoableEditListener listener : undoListeners) {
            listener.undoableEditHappened(event);
        }
    }

    public static class ScmlUndoableEdit implements UndoableEdit {

        public static final int TYPE_INSERT = 0;
        public static final int TYPE_REMOVE = TYPE_INSERT + 1;
        private Scml.Item item;
        private final int scmlOffset;
        private final boolean significant;
        private boolean alive;
        private final ScmlDocument source;
        private final int type;

        public ScmlUndoableEdit(ScmlDocument source, Scml.Item item,
                int scmlOffset, boolean significant, int type) {

            this.source = source;
            if (item instanceof Scml.Text) {
                this.item = Scml.Text.Factory.newInstance(((Scml.Text) item).getText());
            } else {
                this.item = item;
            }
            this.scmlOffset = scmlOffset;
            this.significant = significant;
            this.type = type;
            alive = true;

        }

        @Override
        public boolean addEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean canRedo() {
            return alive;
        }

        @Override
        public boolean canUndo() {
            return alive;
        }

        @Override
        public void die() {
            alive = false;
        }

        @Override
        public String getPresentationName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getRedoPresentationName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getUndoPresentationName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public boolean isSignificant() {
            return significant;
        }

        @Override
        public void redo() throws CannotRedoException {
            source.putProperty(ScmlDocument.UNDO_OR_REDO_IN_PROGRESS, this);
            try {
                switch (type) {
                    case TYPE_INSERT:
                        source.insertItemAtScmlOffset(item, scmlOffset);
                        break;
                    case TYPE_REMOVE:
                        source.removeFromScmlOffset(scmlOffset, getLength(item), true);
                        break;
                }
            } catch (BadLocationException ble) {
                ble.printStackTrace();
            } finally {
                source.putProperty(ScmlDocument.UNDO_OR_REDO_IN_PROGRESS, null);
            }
        }

        private int getLength(Scml.Item item) {
            if (item instanceof Scml.Tag) {
                return 1;
            }
            return ((Scml.Text) item).getText().length();
        }

        @Override
        public boolean replaceEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public void undo() throws CannotUndoException {
            source.putProperty(ScmlDocument.UNDO_OR_REDO_IN_PROGRESS, this);
            try {
                switch (type) {
                    case TYPE_INSERT:
                        source.removeFromScmlOffset(scmlOffset, getLength(item), true);
                        break;
                    case TYPE_REMOVE:
                        source.insertItemAtScmlOffset(item, scmlOffset);
                        break;
                }
            } catch (BadLocationException ble) {
                ble.printStackTrace();
            } finally {
                source.putProperty(ScmlDocument.UNDO_OR_REDO_IN_PROGRESS, null);
            }
        }
    }

    private static class EmptyUndoableEdit implements UndoableEdit {

        private final boolean significant;
        private boolean alive = true;

        public EmptyUndoableEdit(boolean significant) {
            this.significant = significant;
        }

        @Override
        public void undo() throws CannotUndoException {
            //do nothing
        }

        @Override
        public boolean canUndo() {
            return alive;
        }

        @Override
        public void redo() throws CannotRedoException {
            //do nothing
        }

        @Override
        public boolean canRedo() {
            return alive;
        }

        @Override
        public void die() {
            alive = false;
        }

        @Override
        public boolean addEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean replaceEdit(UndoableEdit anEdit) {
            return false;
        }

        @Override
        public boolean isSignificant() {
            return significant;
        }

        @Override
        public String getPresentationName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getUndoPresentationName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getRedoPresentationName() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
    private final PropertyChangeListener lineAnnotationListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
        }
    };

    private static class ScmlUndoManager extends UndoManager {

        @Override
        public synchronized boolean addEdit(UndoableEdit anEdit) {
            if (anEdit instanceof ScmlUndoableEdit) {
                return super.addEdit(anEdit);
            }
            return true;
        }
    }

    private static class ScmlDocumentLine extends Line {

        private Position linePosition;
        private ScmlDocument doc;
        private boolean breakpoint;
        private Annotation annotation;

        public ScmlDocumentLine(ScmlDocument document, final int offset) {
            super(document);
            this.doc = document;
            this.linePosition = new LinePosition(document, offset);
            this.addPropertyChangeListener(document.lineAnnotationListener);
        }

        private class LinePosition implements Position {

            private int offset;
            private final ScmlDocument doc;

            public LinePosition(final ScmlDocument doc, int offs) {
                this.doc = doc;
                try {
                    this.offset = Utilities.getRowStart(doc, offs, 0);
                } catch (BadLocationException ex) {
                    throw new RuntimeException("Can't create LinePosition", ex);
                }

                doc.addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        if (e.getOffset() < offset) {
                            offset += e.getLength();
                        } else if (e.getOffset() == offset) {
                            try {
                                offset = Utilities.getRowStart(doc, offset + e.getLength(), 0);
                                reannotate();
                            } catch (BadLocationException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
//                        debug();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        if (e.getOffset() < offset) {
                            try {
                                if (e.getOffset() + e.getLength() > offset) {
                                    offset = e.getOffset() + e.getLength();
                                }
                                offset = offset = Utilities.getRowStart(doc, offset - e.getLength(), 0);
                                reannotate();
                            } catch (BadLocationException ex) {
                                Exceptions.printStackTrace(ex);
                            }
                        }
//                        debug();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        //do nothing
                    }
                });

//                debug();
            }

            @Override
            public int getOffset() {
                return offset;
            }
//            private static class DebugAnnotation extends Annotation {
//
//                @Override
//                public String getAnnotationType() {
//                    return "scml-editor-annotation-error";
//                }
//
//                @Override
//                public String getShortDescription() {
//                    return "Debug Line";
//                }
//            };
//            private Annotation debugAnno;
//
//            private void debug() {
//                if (debugAnno != null) {
//                    doc.removeAnnotation(debugAnno);
//                } else {
//                    debugAnno = new DebugAnnotation();
//                }
//                try {
//                    doc.addAnnotation(doc.createPosition(offset), 1, debugAnno);
//                } catch (BadLocationException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
        }

        private void reannotate() {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    if (annotation != null) {
                        doc.removeAnnotation(annotation);
                        doc.addAnnotation(linePosition, -1, annotation);
                    }
                }
            });
        }

        @Override
        public int getLineNumber() {
            try {
                return Utilities.getLineOffset(doc, linePosition.getOffset());
            } catch (BadLocationException ex) {
                return -1;
            }
        }

        @Override
        public void show(int kind, int column) {
            final ScmlLocation location = new ScmlLocation(doc.getScml(), doc.textOffsetToScmlOffset(linePosition.getOffset()), 0);
            EditorsManager.getDefault().open(doc.getScml(), new OpenInfo(doc.getScml(), Lookups.fixed(new TargetScmlCookie(doc.getScml()), location)));
        }

        @Override
        public String getText() {
            try {
                return doc.getText(linePosition.getOffset(), Utilities.getRowEnd(doc, linePosition.getOffset()));
            } catch (BadLocationException ex) {
                return null;
            }
        }

        @Override
        public void setBreakpoint(boolean b) {
            breakpoint = b;
//            System.out.println("Breakpoint at line " + getLineNumber() + "is setted to " + b);
        }

        @Override
        public boolean isBreakpoint() {
            return breakpoint;
        }

        @Override
        public void markError() {
//            System.out.println("Line " + getLineNumber() + " is marked as error");
        }

        @Override
        public void unmarkError() {
//            System.out.println("Line " + getLineNumber() + " is unmarked as error");
        }

        @Override
        public void markCurrentLine() {
//            System.out.println("Line " + getLineNumber() + " is marked as current");
        }

        @Override
        public void unmarkCurrentLine() {
//            System.out.println("Line " + getLineNumber() + " is unmarked as current");
        }

        @Override
        protected void addAnnotation(final Annotation anno) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    annotation = anno;
                    doc.addAnnotation(linePosition, -1, anno);
                }
            });

        }

        @Override
        protected void removeAnnotation(final Annotation anno) {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    annotation = null;
                    doc.removeAnnotation(anno);
                }
            });
        }
    }

    private final class DocumentCharView extends AbstractCharSequence.StringLike {

        @Override
        public int length() {
            return getContent().length();
        }

        @Override
        public char charAt(int index) {

            final TagBounds bounds = getTagMapper().findContainingBounds(index);
            if (bounds != null) {
                return '\0';
            }

            final CharSequence content = DocumentUtilities.getText(ScmlDocument.this);
            if (content != null) {
                return content.charAt(index);
            }
            return '\0';
        }
    }
}
