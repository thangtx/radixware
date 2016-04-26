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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.*;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.TransferHandler;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import org.apache.xmlbeans.XmlException;
import org.netbeans.editor.WeakEventListenerList;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObject.EEditState;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport.AdsTransfer;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.scml.IScmlPosition;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinition;
import org.radixware.kernel.designer.common.dialogs.chooseobject.ChooseDefinitionCfg;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.*;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.EditorOpenInfo;

/**
 * JEdiorPane extension, supports displaying and editing Scml<br><br>
 *
 * Please Note, that if you will implement some CodeGenerator functionality for
 * ScmlEditorPane-based components, you should always check the client property {@code DISABLE_STANDART_GENERATORS}
 * to determine whether to enable or disable your CodeGenerator.
 *
 */
public class ScmlEditorPane extends JEditorPane {

    public static final String CURRENT_POSITION_EDITABLE = "current-position-editable";
    public static final String DISABLE_STANDART_GENERATORS = "disable-standart-generators";
    static final String USED_BY_CLONEABLE_EDITOR_PROPERTY = "usedByCloneableEditor";
    private final TagEditorFactory editorFactory;
    private final TagPopupFactory popupFactory;
    private final VTagFactory vTagFactory;
    private final LazyCaretEventDispatcher lazyCaretEventDispatcher = new LazyCaretEventDispatcher();
    private final CurrentPositionEditableListener curPosEditableListener = new CurrentPositionEditableListener();
    private final PositionHistoryUpdater positionHistoryUpdater = new PositionHistoryUpdater();

    public ScmlEditorPane(final String mimeType, final VTagFactory vTagFactory, final TagEditorFactory editorFactory, final TagPopupFactory popupFactory) {
        setContentType(mimeType);
        if (getScmlDocument() != null) {
            getScmlDocument().setFactory(vTagFactory);
        }
        this.vTagFactory = vTagFactory;
        this.editorFactory = editorFactory;
        this.popupFactory = popupFactory;
        this.setTransferHandler(new ScmlTransferHandler());
        this.addMouseListener(new TagEditPerformer());
        this.addMouseMotionListener(new CursorChanger(getCursor()));
        this.addCaretListener(new TagSelector());
        lazyCaretEventDispatcher.addCaretListener(curPosEditableListener);
        lazyCaretEventDispatcher.addCaretListener(positionHistoryUpdater);

        // workaround to prevent situation with suddenly non-working variant actions
        putClientProperty(USED_BY_CLONEABLE_EDITOR_PROPERTY, true);
//        setupCompletionAction();//simple workaround to prevent situation with suddenly non-working completion
    }

//    static {
//        // another workaround to prevent situation with suddenly non-working variant actions
//        EditorApiPackageAccessor.get().setIgnoredAncestorClass(ScmlEditor.class);
//    }
//    private void setupCompletionAction() {
//        final AbstractAction showCompletionAction = new AbstractAction() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                EditorApiPackageAccessor.get().register(ScmlEditorPane.this);
//                Completion.get().showCompletion();
//            }
//        };
//        getInputMap().put(KeyStroke.getKeyStroke("ctrl pressed SPACE"), showCompletionAction);
//        getActionMap().put(showCompletionAction, showCompletionAction);
//    }
//
    public void releasePane() {
        putClientProperty(USED_BY_CLONEABLE_EDITOR_PROPERTY, false);
    }

    private class CurrentPositionEditableListener implements CaretListener {

        private Boolean oldState = null;

        @Override
        public void caretUpdate(CaretEvent e) {
            boolean newState = isCurrentPositionEditable();
            if (oldState == null || newState != oldState) {
                firePropertyChange(CURRENT_POSITION_EDITABLE, !newState, newState);
                oldState = newState;
            }
        }
    }

    private class PositionHistoryUpdater implements CaretListener {

        @Override
        public void caretUpdate(CaretEvent e) {
            int offset = getCaretPosition();
            ScmlEditorRegistry.getDefault().setLastScmlOffset(getScml(), offset);
        }
    }

    private class LazyCaretEventDispatcher implements CaretListener {

        public LazyCaretEventDispatcher() {
            ScmlEditorPane.this.addCaretListener(this);
        }
        private final WeakEventListenerList lazyCaretListeners = new WeakEventListenerList();
        private boolean isEnabled = true;
        private final Stack<Boolean> states = new Stack<Boolean>();

        @Override
        public void caretUpdate(CaretEvent e) {
            if (shouldFireEvent(e)) {
                fireEvent(e);
            }
        }

        private void fireEvent(CaretEvent e) {
            for (EventListener caretListener : lazyCaretListeners.getListeners(CaretListener.class)) {
                ((CaretListener) caretListener).caretUpdate(e);
            }
        }

        public void addCaretListener(CaretListener cl) {
            lazyCaretListeners.add(CaretListener.class, cl);
        }

        public void removeCaretListener(CaretListener cl) {
            lazyCaretListeners.remove(CaretListener.class, cl);
        }

        protected boolean shouldFireEvent(CaretEvent e) {
            return isEnabled;
        }

        private void setEnabledImpl(boolean enabled) {
            this.isEnabled = enabled;
        }

        public void setEnabled(boolean enabled) {
            states.push(isEnabled);
            setEnabledImpl(enabled);
        }

        public void revertState() {
            if (states.empty()) {
                setEnabledImpl(true);
            } else {
                setEnabledImpl(states.pop());
            }
        }

        public void forceFire() {
            CaretEvent ce = new CaretEvent(ScmlEditorPane.this) {

                @Override
                public int getDot() {
                    return ScmlEditorPane.this.getCaret().getDot();
                }

                @Override
                public int getMark() {
                    return ScmlEditorPane.this.getCaret().getMark();
                }
            };

            caretUpdate(ce);
        }
    }

    public void addLazyCaretListener(CaretListener cl) {
        lazyCaretEventDispatcher.addCaretListener(cl);
    }

    public void removeLazyCaretListener(CaretListener cl) {
        lazyCaretEventDispatcher.removeCaretListener(cl);
    }

    @Override
    public void setDocument(Document doc) {
        if (doc instanceof ScmlDocument) {
            Scml oldValue = getScml();
            ((ScmlDocument) doc).setFactory(vTagFactory);
            super.setDocument(doc);
            firePropertyChange("scml", oldValue, getScml());
        } else {
            super.setDocument(doc);
        }
    }

    public void insertString(String string) {
        insertText(Scml.Text.Factory.newInstance(string));
    }

    public void insertText(final Scml.Text text) {
        Caret caret = getCaret();
        int offs = Math.min(caret.getDot(), caret.getMark());
        try {
            getScmlDocument().insertText(offs, text);
            getCaret().setDot(getScmlDocument().getLastChangeEndOffset());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public VTagFactory getVTagFactory() {
        return vTagFactory;
    }

    public void appendText(final Scml.Text text) {
        try {
            getScmlDocument().insertText(getScmlDocument().getLength(), text);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void insertTag(final Scml.Tag tag) {
        Caret caret = getCaret();
        int offs = Math.min(caret.getDot(), caret.getMark());
        try {
            getScmlDocument().insertTag(offs, tag);
            getCaret().setDot(getScmlDocument().getLastChangeEndOffset());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void appendTag(final Scml.Tag tag) {
        try {
            getScmlDocument().insertTag(getScmlDocument().getLength(), tag);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void insertItem(final Scml.Item item) {
        if (item instanceof Scml.Tag) {
            insertTag((Scml.Tag) item);
        } else {
            insertText((Scml.Text) item);
        }
    }

    public void insertItems(final RadixObjects<Scml.Item> items) {
        try {
            getScmlDocument().insertItems(items, getCaretPosition());
            getCaret().setDot(getScmlDocument().getLastChangeEndOffset());
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void setScml(Scml scml) {
        lazyCaretEventDispatcher.setEnabled(false);
        Scml oldValue = getScml();
        getScmlDocument().setScml(scml);
        final ScmlEditorPane editorPane = ScmlEditorRegistry.getDefault().getEditorPane(scml);
        if (editorPane != null && editorPane != this) {
            //reset previous editor to default scml.
            //it fixes a bug when one scml is opened in two editors
            //and meta info about tags in one of them become outdated
            Scml defaultScml = editorPane.getScmlDocument().createDefaultScml();
            if (editorPane.getScml() != defaultScml) {
                editorPane.setScml(defaultScml);
            }
        }
        ScmlEditorRegistry.getDefault().setEditorPane(scml, this);
        firePropertyChange("scml", oldValue, scml);
        lazyCaretEventDispatcher.revertState();
        int lastOffset = ScmlEditorRegistry.getDefault().getLastScmlOffset(scml);
        if (lastOffset < 0 || lastOffset >= getDocument().getLength()) {
            lastOffset = 0;
        }
        setCaretPosition(lastOffset);
    }

    public Scml getScml() {
        if (getScmlDocument() != null) {
            return getScmlDocument().getScml();
        }
        return null;
    }

    public void update() {
        lazyCaretEventDispatcher.setEnabled(false);
        getScmlDocument().update();
        setEditable(!getScmlDocument().isReadOnly());
        lazyCaretEventDispatcher.revertState();
    }

    public boolean isReadOnly() {
        return !isEditable() || getScmlDocument().isReadOnly();
    }

    public final ScmlDocument getScmlDocument() {
        if (getDocument() instanceof ScmlDocument) {
            return (ScmlDocument) getDocument();
        }
        return null;
    }

    public void setCursorAtScmlPosition(IScmlPosition scmlPosition) {
        int idx = getScml().getItems().indexOf(scmlPosition.getItem());
        int itemStartOffset = getScmlDocument().getItemStartOffset(idx);
        if (itemStartOffset != -1) {
            int offset = itemStartOffset + scmlPosition.getInternalOffset();
            if (offset >= 0 && offset < getDocument().getLength()) {
                setCaretPosition(offset);
            }
        }
    }

    public boolean isCurrentPositionEditable() {
        if (getCaret().getDot() != getCaret().getMark()) {
            return isEnabled() && !isReadOnly();
        }
        return isPositionEditable(getCaretPosition());
    }

    public boolean isPositionEditable(int offs) {
        if (!isEnabled() || isReadOnly()) {
            return false;
        }
        if (getScmlDocument().getTagMapper().insideTagBounds(offs)) {
            return false;
        }
        return true;
    }

    @Override
    public void cut() {
        int p0 = getSelectionStart();
        int p1 = getSelectionEnd();
        if (p1 == p0) {
            TagBounds tb = getScmlDocument().getTagMapper().findContainingBounds(p1);
            if (tb != null) {
                p0 = tb.getBeginOffset();
                p1 = tb.getEndOffset();
                setSelectionStart(p0);
                setSelectionEnd(p1);
            }
        }

        super.cut();
    }

    @Override
    public void paste() {
        if (getCaret().getDot() == getCaret().getMark()) {
            int p = getCaretPosition();
            if (getScmlDocument().getTagMapper().insideTagBounds(p)) {
                getToolkit().beep();
                return;
            }
        }
        super.paste();
    }

    public TagEditorFactory getTagEditorFactory() {
        return editorFactory;
    }

    public TagPopupFactory getTagPopupFactory() {
        return popupFactory;
    }

    /**
     * Edit some tag in currently opened scml.
     *
     * @param tag
     * @return
     */
    public boolean editTag(Scml.Tag tag) {
        return editTag(tag, Lookup.EMPTY);
    }

    public boolean showTagPopup(MouseEvent e, Scml.Tag tag) {
        return showTagPopup(e, tag, Lookup.EMPTY);
    }

    private boolean showTagPopup(MouseEvent e, Scml.Tag tag, Lookup additionalLookup) {
        TagPopup popup = getTagPopupFactory().createTagPopup(this);
        if (popup != null) {
            if (popup.open(tag)) {
                popup.show(this, e.getPoint());
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Edit some tag in currently opened scml. Additional lookup can be provided
     * for special case when tag is not in scml at the moment (e.g. before
     * inserting tag in editor).
     *
     * @param tag
     * @return
     */
    public boolean editTag(Scml.Tag tag, Lookup additionalLookup) {
        TagEditor panel = getTagEditorFactory().createTagEditor(tag);
        if (panel != null) {
            final EditorOpenInfo tagOpenInfo = getTagEditorFactory().createOpenInfo(tag);
            final ProxyModalOpenInfo proxyModalOpenInfo = new ProxyModalOpenInfo(tagOpenInfo, additionalLookup);
            panel.open(tag, proxyModalOpenInfo);
            if (panel.showModal()) {
                if (tag.getOwnerScml() != null && tag.getOwnerScml().equals(getScml())) {
                    getScmlDocument().update(tag);
                    getScml().setEditState(EEditState.MODIFIED);
                    firePropertyChange("tag-content", null, tag);
                }
                return true;
            }
        }
        return false;
    }

    private class TagEditPerformer extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getButton() == MouseEvent.BUTTON1) {
                Point p = getMousePosition();
                if (p != null) {
                    int offs = viewToModel(p);
                    TagBounds tb = getScmlDocument().getTagMapper().findContainingBounds(offs);
                    if (tb != null) {
                        evt.consume();
                        getCaret().setDot(offs);
                        Scml.Tag tag = tb.getVTag().getTag();
                        if (evt.getClickCount() == 1) {
                            if (getTagPopupFactory() != null && getTagPopupFactory().createTagPopup(ScmlEditorPane.this) != null) {
                                showTagPopup(evt, tag);
                            }
                        } else if (evt.getClickCount() == 2) {
                            if (getTagEditorFactory() == null || getTagEditorFactory().createTagEditor(tag) == null) {
                                getToolkit().beep();
                            } else {
                                editTag(tag);
                            }
                        }
                    }
                }
            }
        }
    }

    private class CursorChanger implements MouseMotionListener {

        private final Cursor defaultCursor;

        public CursorChanger(Cursor defaultCursor) {
            this.defaultCursor = defaultCursor;
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            //do nothing
        }

        @Override
        public void mouseMoved(MouseEvent evt) {
            Point p = getMousePosition();
            if (p != null) {
                int offs = viewToModel(p);
                TagBounds tb = getScmlDocument().getTagMapper().findContainingBounds(offs);
                if (tb != null) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    setCursor(defaultCursor);
                }
            }
        }
    }

    public class ScmlTransferHandler extends TransferHandler {

        @Override
        public void exportToClipboard(JComponent comp, Clipboard clipboard, int action)
                throws IllegalStateException {

            if (!(comp instanceof ScmlEditorPane)) {
                return;
            }
            ScmlEditorPane editor = (ScmlEditorPane) comp;
            if (editor.getScml() == null) {
                return;
            }

            int p0 = editor.getSelectionStart();
            int p1 = editor.getSelectionEnd();

            AdsTransfer<Scml> scmlTransfer = new AdsTransfer<Scml>(getScml()).copy();
            Scml scml = scmlTransfer.getObject();
            TagMapper tagMapper = getScmlDocument().getTagMapper();

            if (p0 != p1) {
                try {
                    NavigableSet<TagBounds> bounds = tagMapper.getBounds(p0, p1);

                    if (bounds.size() > 0) {
                        p0 = Math.min(p0, bounds.first().getBeginOffset());
                        p1 = Math.max(p1, bounds.last().getEndOffset());
                    }

                    int offs = 0;
                    TagBounds tb = null;
                    Iterator<TagBounds> it = tagMapper.getBounds().iterator();
                    RadixObjects<Scml.Item> items = scml.getItems();
                    for (int i = 0; i < items.size();) {
                        Scml.Item item = items.get(i);
                        if (item instanceof Scml.Tag) {
                            if (offs < p0 || offs >= p1) {
                                items.remove(i);
                            } else {
                                i++;
                            }
                            tb = it.next();
                            offs += tb.getLength();
                        } else if (item instanceof Scml.Text) {
                            Scml.Text text = (Scml.Text) item;
                            String s = text.getText();
                            int len = s.length();
                            if (offs + len <= p0 || offs >= p1) {
                                items.remove(i);
                            } else {
                                if (offs < p0 && len > p1 - offs) {
                                    s = s.substring(p0 - offs, p1 - offs);
                                } else if (offs < p0) {
                                    s = s.substring(p0 - offs);
                                } else if (offs + len > p1) {
                                    s = s.substring(0, p1 - offs);
                                }
                                text.setText(s);
                                i++;
                            }
                            offs += len;
                        }
                    }

                    String scmlString = editor.getText(p0, p1 - p0);

                    clipboard.setContents(new ScmlSelection(scmlTransfer, scmlString), null);
                    if (action == TransferHandler.MOVE) {
                        for (TagBounds tbd : tagMapper.getBounds(p0, p1)) {
                            tbd.unlockText();
                        }
                        editor.getDocument().remove(p0, p1 - p0);
                    }

                } catch (BadLocationException ble) {
                    editor.getToolkit().beep();
                }
            }
        }

        @Override
        public boolean importData(JComponent comp, Transferable t) {

            if (!(comp instanceof ScmlEditorPane)) {
                return false;
            }

            final ScmlEditorPane pane = (ScmlEditorPane) comp;

            final DataFlavor flavor = getSupportedFlavor(t.getTransferDataFlavors());
            if (flavor != null) {
                try {
                    lazyCaretEventDispatcher.setEnabled(false);
                    if (flavor.equals(ScmlSelection.SCML_FLAVOR)) {
                        importScml(flavor, t, pane);
                    } else if (flavor.equals(ClipboardSupport.SERIALIZED_RADIX_TRANSFER)) {
                        final RadixObject originalObject = getOriginalObject(t);
                        if (originalObject != null) {
                            importRadixObject(t, pane);
                        } else {
                            importScmlFromString(flavor, t, pane);
                        }
                    } else if (flavor.equals(DataFlavor.stringFlavor)) {
                        String data = (String) t.getTransferData(flavor);
                        pane.replaceSelection(data);
                    } else {
                        return false;
                    }
                    return true;
                } catch (UnsupportedFlavorException ufe) {
                    comp.getToolkit().beep();
                } catch (IOException ioe) {
                    comp.getToolkit().beep();
                } finally {
                    lazyCaretEventDispatcher.revertState();
                }
            }
            return false;
        }

        private void importScml(DataFlavor flavor, Transferable t, ScmlEditorPane editor) throws UnsupportedFlavorException, IOException {
            importScmlFromTransfer((AdsTransfer) t.getTransferData(flavor), editor);
        }

        private void importScmlFromTransfer(AdsTransfer scmlTransfer, ScmlEditorPane editor) throws UnsupportedFlavorException, IOException {
            Scml scml = (Scml) scmlTransfer.getObject();
            if (scmlSupported(scml)) {
                editor.replaceSelection("");
                RadixObjects<Scml.Item> items = scml.getItems();
                scmlTransfer.afterPaste(editor.getScml().getOwnerDefinition());
                editor.insertItems(items);
            } else {
                throw new IllegalArgumentException("Can not import Scml from buffer because it's type differs from Scml in editor.");
            }
        }

        private void importScmlFromString(DataFlavor flavor, Transferable t, ScmlEditorPane editor) throws UnsupportedFlavorException, IOException {
            Object obj = t.getTransferData(flavor);
            if (obj instanceof String) {
                try {
                    AdsTransfer transfer = AdsTransfer.decode((String) obj).copy();
                    importScmlFromTransfer(transfer, editor);
                } catch (XmlException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        private void importRadixObject(Transferable t, ScmlEditorPane pane) {
            RadixObject radixObject = getOriginalObject(t);
            if (radixObject == null) {
                return;
            }
            try {
                pane.putClientProperty(InsertTagCodeGeneratorFactory.RADIX_OBJECT_FOR_TAG_CREATION, radixObject);
                pane.putClientProperty(DISABLE_STANDART_GENERATORS, Boolean.TRUE);

                Action generateAction = pane.getActionMap().get("generate-code");

                if (generateAction != null) {
                    generateAction.actionPerformed(new ActionEvent(pane, -1, null));
                }

            } finally {
                pane.putClientProperty(InsertTagCodeGeneratorFactory.RADIX_OBJECT_FOR_TAG_CREATION, null);
                pane.putClientProperty(DISABLE_STANDART_GENERATORS, null);
            }
        }

        private boolean scmlSupported(Scml scml) {
            if (getContentType().contains("sqml")) {
                return scml instanceof Sqml;
            } else if (getContentType().contains("jml")) {
                return scml instanceof Jml;
            } else {
                return false;
            }
        }

        @Override
        public boolean canImport(TransferSupport support) {
            if (!(support.getComponent() instanceof ScmlEditorPane)) {
                return false;
            }
            ScmlEditorPane editor = (ScmlEditorPane) support.getComponent();


            if (support.isDrop()) {
                int offset = editor.viewToModel(support.getDropLocation().getDropPoint());
                if (offset < 0 || offset > editor.getDocument().getLength() || !(editor.isEditable() && editor.isEnabled() && editor.isPositionEditable(offset))) {
                    return false;
                }
            } else {
                if (editor == null || !(editor.isEditable() && editor.isEnabled() && editor.isCurrentPositionEditable())) {
                    return false;
                }
            }

            return (getSupportedFlavor(support.getDataFlavors()) != null);
        }

        private RadixObject getOriginalObject(Transferable t) {
            List<Transfer> transfers = ClipboardSupport.getTransfers(t);
            if (transfers != null) {
                if (transfers.size() == 1) {
                    return transfers.get(0).getInitialObject();
                }
            }
            return null;
        }

        private DataFlavor getSupportedFlavor(DataFlavor[] flavors) {
            DataFlavor scmlFlavor = null, stringFlavor = null, radixObjectFlavor = null, scmlXmlFlavor = null;
            if (flavors != null) {
                for (int i = 0; i < flavors.length; i++) {
                    if (flavors[i].equals(ScmlSelection.SCML_FLAVOR)) {
                        scmlFlavor = flavors[i];
                    } else if (flavors[i].equals(ClipboardSupport.SERIALIZED_RADIX_TRANSFER)) {
                        scmlXmlFlavor = flavors[i];
                    } else if (flavors[i].equals(DataFlavor.stringFlavor)) {
                        stringFlavor = flavors[i];
                    } else if (flavors[i].equals(ClipboardSupport.RADIX_DATA_FLAVOR)) {
                        radixObjectFlavor = flavors[i];
                    }
                }
                if (scmlFlavor != null) {
                    return scmlFlavor;
                }

                if (scmlXmlFlavor != null) {
                    return scmlXmlFlavor;
                }

                if (radixObjectFlavor != null) {
                    return radixObjectFlavor;
                }

                if (stringFlavor != null) {
                    return stringFlavor;
                }

            }
            return null;
        }
    }

    public static abstract class VTag<T extends Scml.Tag> {

        private final T tag;

        protected VTag(T tag) {
            this.tag = tag;
        }

        public T getTag() {
            return tag;
        }

        public boolean isError() {
            return getTitle().indexOf("???") > -1;
        }

        public abstract String getTokenName();

        public abstract String getTitle();

        public abstract String getToolTip();

        public AttributeSet updateAttributesBeforeRender(AttributeSet attributes) {

            return attributes;
        }



        public boolean goToObject() {
            List<Definition> dependencies = new ArrayList<Definition>();
            tag.collectDependences(dependencies);
            if (dependencies.isEmpty()) {
                return false;
            } else if (dependencies.size() == 1) {
                DialogUtils.goToObject(dependencies.get(0));
                return true;
            } else {
                Definition definition = ChooseDefinition.chooseDefinition(ChooseDefinitionCfg.Factory.newInstance(dependencies));
                if (definition == null) {
                    return false;
                } else {
                    DialogUtils.goToObject(definition);
                    return true;
                }
            }
        }

        public boolean hasGoToTargets() {
            List<Definition> dependencies = new ArrayList<Definition>();
            tag.collectDependences(dependencies);
            return !dependencies.isEmpty();
        }

        public String getGoToTooltip() {
            List<Definition> dependencies = new ArrayList<Definition>();
            tag.collectDependences(dependencies);
            if (dependencies.isEmpty()) {
                return null;
            } else if (dependencies.size() == 1) {
                return "Go to " + dependencies.get(0).getOwnerDefinition().getName() + "." + dependencies.get(0).getName();
            } else {
                return NbBundle.getMessage(ScmlEditorPane.class, "go-to-definition");
            }
        }
    }

    public static class ErrorVTag extends VTag<Scml.Tag> {

        public static final String TOKEN_NAME = "tag-error";

        public ErrorVTag(Scml.Tag tag) {
            super(tag);
        }

        @Override
        public String getTokenName() {
            return TOKEN_NAME;
        }

        @Override
        public String getTitle() {
            return "???";
        }

        @Override
        public String getToolTip() {
            return NbBundle.getBundle(ScmlEditorPane.class).getString("unknown-tag-tooltip");
        }
    }

    public static interface VTagFactory {

        public VTag createVTag(Scml.Tag tag);
    }

    public static interface TagEditorFactory {

        public TagEditor createTagEditor(Scml.Tag tag);

        public EditorOpenInfo createOpenInfo(Scml.Tag tag);
    }

    public static interface TagPopupFactory {

        public TagPopup createTagPopup(ScmlEditorPane pane);
    }

    public static abstract class TagPopup {

        protected final ScmlEditorPane editor;

        protected TagPopup(ScmlEditorPane editor) {
            this.editor = editor;
        }

        protected ScmlDocument getScmlDocument() {
            return editor.getScmlDocument();
        }

        protected Frame getParentFrame() {
            return WindowManager.getDefault().getMainWindow();
        }

        public abstract boolean open(Scml.Tag tag);

        public abstract void show(Component c, Point popupLocation);
    }
}
