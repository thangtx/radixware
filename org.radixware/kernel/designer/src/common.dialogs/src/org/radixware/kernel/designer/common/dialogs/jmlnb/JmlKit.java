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

import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import static javax.swing.text.DefaultEditorKit.nextWordAction;
import static javax.swing.text.DefaultEditorKit.previousWordAction;
import static javax.swing.text.DefaultEditorKit.selectionNextWordAction;
import static javax.swing.text.DefaultEditorKit.selectionPreviousWordAction;
import org.netbeans.api.editor.EditorActionRegistration;
import org.netbeans.api.java.source.CodeStyle.BracesGenerationStyle;
import org.netbeans.api.lexer.TokenHierarchy;
import org.netbeans.api.lexer.TokenSequence;
import org.netbeans.editor.*;
import static org.netbeans.editor.BaseKit.removeNextWordAction;
import org.netbeans.editor.ext.java.*;
import org.netbeans.modules.editor.NbEditorKit;
import org.netbeans.modules.editor.indent.api.Indent;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.netbeans.modules.editor.indent.spi.CodeStylePreferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.util.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagCheckLicense;
import org.radixware.kernel.common.jml.JmlTagReadLicense;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.choosetype.RadixPlatformClassPanel;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlDocument;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditor;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorKit;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorPane;
import org.radixware.kernel.designer.common.dialogs.scmlnb.ScmlEditorRegistry;
import org.radixware.kernel.designer.common.dialogs.scmlnb.library.TagCompletionRegion;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public class JmlKit extends ScmlEditorKit {

    public static final String expandAllJavadocFolds = "expand-all-javadoc-folds";
    public static final String collapseAllJavadocFolds = "collapse-all-javadoc-folds";
    public static final String expandAllCodeBlockFolds = "expand-all-code-block-folds";
    public static final String collapseAllCodeBlockFolds = "collapse-all-code-block-folds";
    static final String previousCamelCasePosition = "previous-camel-case-position";
    static final String nextCamelCasePosition = "next-camel-case-position"; //NOI18N
    static final String selectPreviousCamelCasePosition = "select-previous-camel-case-position"; //NOI18N
    static final String selectNextCamelCasePosition = "select-next-camel-case-position"; //NOI18N
    static final String deletePreviousCamelCasePosition = "delete-previous-camel-case-position"; //NOI18N
    static final String deleteNextCamelCasePosition = "delete-next-camel-case-position"; //NOI18N
    public static final String JML_FIX_IMPORT_ACTION = "jml-fix-import-action";
    static final long serialVersionUID = -5445829962533684922L;

    public JmlKit() {
    }

    @Override
    public String getContentType() {
        return ScmlEditor.JML_MIME_TYPE;
    }

    /**
     * Create new instance of syntax coloring scanner
     *
     * @param doc document to operate on. It can be null in the cases the syntax
     * creation is not related to the particular document
     */
    @Override
    public Syntax createSyntax(Document doc) {
        if (doc instanceof ScmlDocument) {
            return new JavaSyntaxWrapper((ScmlDocument) doc);
        } else {
            return new JavaSyntax(getSourceLevel((BaseDocument) doc));
        }
    }

    @Override
    public Document createDefaultDocument() {
        return new JmlDocument(getContentType());
    }

    public String getSourceLevel(BaseDocument doc) {
        return null;
    }

    @Override
    protected void initDocument(BaseDocument doc) {
        doc.putProperty(SyntaxUpdateTokens.class,
                new SyntaxUpdateTokens() {
            private List tokenList = new ArrayList();

            @Override
            public void syntaxUpdateStart() {
                tokenList.clear();
            }

            @Override
            public List syntaxUpdateEnd() {
                return tokenList;
            }

            @Override
            public void syntaxUpdateToken(TokenID id, TokenContextPath contextPath, int offset, int length) {
                if (JavaTokenContext.LINE_COMMENT == id) {
                    tokenList.add(new TokenInfo(id, contextPath, offset, length));
                }
            }
        });
    }

    @Override
    protected Action[] createActions() {
        Action[] superActions = super.createActions();
        Action[] javaActions = new Action[]{
            //            new JavaDefaultKeyTypedAction(),
            new ToggleCommentAction("//"), // NOI18N
            new JavaInsertBreakAction(),
            new JavaDeleteCharAction(deletePrevCharAction, false),
            new JavaDeleteCharAction(deleteNextCharAction, true),
            new JmlNextCCPosition(findAction(superActions, nextWordAction)),
            new JmlPreviousCCPosition(findAction(superActions, previousWordAction)),
            new JmlSelectNextCCPosition(findAction(superActions, selectionNextWordAction)),
            new JmlSelectPreviousCCPosition(findAction(superActions, selectionPreviousWordAction)),
            new JmlDeleteToNextCCPosition(findAction(superActions, removeNextWordAction)),};

        return TextAction.augmentList(superActions, javaActions);
    }

    private static Action findAction(Action[] actions, String name) {
        for (Action a : actions) {
            Object nameObj = a.getValue(Action.NAME);
            if (nameObj instanceof String && name.equals(nameObj)) {
                return a;
            }
        }
        return null;
    }

    public static class JavaDefaultKeyTypedAction extends ExtDefaultKeyTypedAction {

        private static final KeyListener SHAREABLE_KEY_LISTENER = new KeyAdapter() {
            @Override
            public synchronized void keyPressed(KeyEvent e) {
                if ((e.getKeyCode() == KeyEvent.VK_ESCAPE && e.getModifiers() == 0)
                        || (e.getKeyCode() == KeyEvent.VK_ENTER && e.getModifiers() == 0)) {
                    release((JTextComponent) e.getSource(), e.getKeyCode() == KeyEvent.VK_ENTER);
                    e.consume();
                }
            }

            private void release(JTextComponent tc, boolean approve) {
                JmlDocument jmlDoc = (JmlDocument) tc.getDocument();
                TagCompletionRegion region = (TagCompletionRegion) jmlDoc.getProperty(TagCompletionRegion.class);
                Scml.Item item = region.getItem();
                int offset = region.getRegionStart();
                jmlDoc.discardCompletionRegion();
                tc.removeKeyListener(this);
                if (approve && item != null) {
                    try {
                        jmlDoc.insertItem(item, offset);
                    } catch (BadLocationException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        };
        JTextComponent target;

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            this.target = target;
            super.actionPerformed(evt, target);
            this.target = null;
        }

        @Override
        protected void insertString(BaseDocument doc, int dotPos,
                Caret caret, String str,
                boolean overwrite) throws BadLocationException {
            char insertedChar = str.charAt(0);
            if (insertedChar == '`') {
                TokenHierarchy th = TokenHierarchy.get(doc);
                TokenSequence ts = th.tokenSequence();
                ts.move(dotPos);
                if (ts.moveNext()) {
                    if (!"comment".equals(ts.token().id().primaryCategory())) {
                        if (doc instanceof JmlDocument) {
                            if (doc.getProperty(TagCompletionRegion.class) == null) {
                                ((JmlDocument) doc).activateCompletionRegion(dotPos);
                                target.addKeyListener(SHAREABLE_KEY_LISTENER);
                                return;
                            }
                        }
                    }
                }
            }
            if (insertedChar == '\"' || insertedChar == '\'') {
                boolean inserted = JmlBraceCompletion.completeQuote(doc, dotPos, caret, insertedChar);
                if (inserted) {
                    caret.setDot(dotPos + 1);
                } else {
                    super.insertString(doc, dotPos, caret, str, overwrite);

                }
            } else {
                super.insertString(doc, dotPos, caret, str, overwrite);
                JmlBraceCompletion.onInsertChar(doc, dotPos, caret, insertedChar);
            }
        }

        @Override
        protected void replaceSelection(JTextComponent target,
                int dotPos,
                Caret caret,
                String str,
                boolean overwrite)
                throws BadLocationException {
            char insertedChar = str.charAt(0);
            Document doc = target.getDocument();
            if (insertedChar == '\"' || insertedChar == '\'') {
                if (doc != null) {
                    try {
                        boolean inserted = false;
                        int p0 = Math.min(caret.getDot(), caret.getMark());
                        int p1 = Math.max(caret.getDot(), caret.getMark());
                        if (p0 != p1) {
                            doc.remove(p0, p1 - p0);
                        }
                        int caretPosition = caret.getDot();
                        if (doc instanceof BaseDocument) {
                            inserted = JmlBraceCompletion.completeQuote(
                                    (BaseDocument) doc,
                                    caretPosition,
                                    caret, insertedChar);
                        }
                        if (inserted) {
                            caret.setDot(caretPosition + 1);
                        } else {
                            if (str != null && str.length() > 0) {
                                doc.insertString(p0, str, null);
                            }
                        }
                    } catch (BadLocationException e) {
                        Exceptions.printStackTrace(e);
                    }
                }
            } else {
                super.replaceSelection(target, dotPos, caret, str, overwrite);
                if (doc instanceof BaseDocument) {
                    JmlBraceCompletion.onInsertChar((BaseDocument) doc, caret.getDot() - 1, caret, insertedChar);
                }
            }
        }
    }

    public static class JavaInsertBreakAction extends InsertBreakAction {

        static final long serialVersionUID = -1506173310438326380L;

        @Override
        protected Object beforeBreak(JTextComponent target, BaseDocument doc, Caret caret) {
            int dotPos = caret.getDot();
            if (JmlBraceCompletion.posWithinString(doc, dotPos)) {
                try {
                    doc.insertString(dotPos, "\" + \"", null); //NOI18N
                    dotPos += 3;
                    caret.setDot(dotPos);
                    return new Integer(dotPos);
                } catch (BadLocationException ex) {
                }
            } else {
                try {
                    if (JmlBraceCompletion.isAddRightBrace(doc, dotPos)) {
                        boolean insert[] = {true};
                        final int end = JmlBraceCompletion.getRowOrBlockEnd(doc, dotPos, insert);
                        if (insert[0]) {
                            doc.insertString(end, "}", null); // NOI18N
                            // doc.getFormatter().indentNewLine(doc, end);
                            //new code
                            final Indent indenter = Indent.get(doc);
                            indenter.lock();
                            try {
                                doc.runAtomic(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            indenter.indentNewLine(end);
                                        } catch (BadLocationException ble) {
                                            Exceptions.printStackTrace(ble);
                                        }
                                    }
                                });
                            } finally {
                                indenter.unlock();
                            }
                            //new code

                        }
                        caret.setDot(dotPos);
                        return Boolean.TRUE;
                    }
                } catch (BadLocationException ex) {
                }
            }

            return null;
        }

        @Override
        protected void afterBreak(JTextComponent target, BaseDocument doc, Caret caret, Object cookie) {
            if (cookie != null) {
                if (cookie instanceof Integer) {
                    // integer
                    int nowDotPos = caret.getDot();
                    caret.setDot(nowDotPos + 1);
                }
            }
        }
    }

    public static class JavaDeleteCharAction extends ScmlEditorKit.ScmlDeleteCharAction {

        public JavaDeleteCharAction(String nm, boolean nextChar) {
            super(nm, nextChar);
        }

        @Override
        protected void charBackspaced(BaseDocument doc, int dotPos, Caret caret, char ch)
                throws BadLocationException {
            JmlBraceCompletion.charBackspaced(doc, dotPos, caret, ch);
        }

        @Override
        public void actionPerformed(final ActionEvent evt, final JTextComponent target) {
            if (target != null) {
                try {
                    target.putClientProperty(JavaDeleteCharAction.class, this);
                    super.actionPerformed(evt, target);
                } finally {
                    target.putClientProperty(JavaDeleteCharAction.class, null);
                }
            }
        }

        @Override
        public boolean getNextChar() {
            return nextChar;
        }
    }

    @EditorActionRegistration(name = BaseKit.formatAction, mimeType = "text/x-jml")
    public static class JmlFormatAction extends ActionFactory.FormatAction {

        private enum EWrapperType {

            METHOD_WRAPPER("MethodWrapper", 2),
            BODY_WRAPPER("BodyWrapper", 1),
            HEADER_WRAPPER("HeaderWrapper", 0),
            UNKNOWN_WRAPPER("", 0);
            private final String fileName;
            private final int shiftCount;

            EWrapperType(String fileName, int shiftCount) {
                this.fileName = fileName;
                this.shiftCount = shiftCount;
            }
        }
        private Map<EWrapperType, String> wrapperCache = new EnumMap<EWrapperType, String>(EWrapperType.class);
        private JEditorPane fakePane;

        public JmlFormatAction() {
            wrapperCache.put(EWrapperType.UNKNOWN_WRAPPER, "#");
        }

        @Override
        public void actionPerformed(ActionEvent evt, JTextComponent target) {
            if (target.isEditable() && target.isEnabled()) {
                reformatTroughJava(target);
            }
        }

        private void applyFormattingOptions(Document doc) {
            Preferences prefs = CodeStylePreferences.get(doc).getPreferences();
            //#RADIX-5009 - do not generate braces around one operator in if statement
            prefs.put("redundantIfBraces", BracesGenerationStyle.LEAVE_ALONE.name());
            try {
                prefs.flush();
            } catch (BackingStoreException ex) {
                //do nothing
            }
        }

        private void legalReformat(JTextComponent target) {
            final Document doc = target.getDocument();
            applyFormattingOptions(doc);
            final Reformat reformat = Reformat.get(doc);
            try {
                reformat.lock();
                try {
                    reformat.reformat(0, doc.getLength());
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            } finally {
                reformat.unlock();
            }
        }

        private void reformatTroughJava(final JTextComponent target) {
            final ScmlEditorPane originalPane = (ScmlEditorPane) target;
            if (fakePane == null) {
                fakePane = new JEditorPane();
                fakePane.setContentType("text/x-java");
                final FileSystem fs = FileUtil.createMemoryFileSystem();
                FileObject fob;
                try {
                    fob = fs.getRoot().createData("fake", "java");
                } catch (IOException ex) {
                    throw new IllegalStateException(ex);
                }
                fakePane.getDocument().putProperty(Document.StreamDescriptionProperty, fob);
                fakePane.getDocument().putProperty("mimeType", "text/x-java");
            }
            final ScmlDocument sdoc = (ScmlDocument) target.getDocument();
            try {
                fakePane.getDocument().remove(0, fakePane.getDocument().getLength());
                final String header = createHeader((Jml) sdoc.getScml());
                final int headerLen = header.length();
                fakePane.getDocument().insertString(0, header, null);
                for (Scml.Item item : sdoc.getScml().getItems()) {
                    if (item instanceof Scml.Text) {
                        fakePane.getDocument().insertString(fakePane.getDocument().getLength(), ((Scml.Text) item).getText(), null);
                    } else {
                        fakePane.getDocument().insertString(fakePane.getDocument().getLength(), getTagReplacement((Scml.Tag) item, originalPane, headerLen), null);
                    }
                }
                fakePane.getDocument().insertString(fakePane.getDocument().getLength(), createTrail((Jml) sdoc.getScml()), null);

                try {
                    FileObject fob = (FileObject) fakePane.getDocument().getProperty(Document.StreamDescriptionProperty);
                    try (OutputStream ostream = fob.getOutputStream()) {
                        ostream.write(fakePane.getText().getBytes(System.getProperty("file.encoding")));
                    }
                } catch (IOException ex) {
                    throw new IllegalStateException(ex);
                }

                final DocumentListener listener = new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        try {
                            int offset = e.getOffset() - headerLen;
                            if (offset <= sdoc.getLength() && offset >= 0) {
                                sdoc.insertString(offset, e.getDocument().getText(e.getOffset(), e.getLength()), null);
                            }
                        } catch (BadLocationException ex) {
                            throw new IllegalStateException(ex);
                        }
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        try {
                            int startOffset = e.getOffset() - headerLen;
                            if (startOffset < 0) {
                                startOffset = 0;
                            }
                            int endOffset = startOffset + e.getLength();
                            if (endOffset > sdoc.getLength()) {
                                endOffset = sdoc.getLength();
                            }
                            if (startOffset >= 0 && startOffset < sdoc.getLength()) {
                                sdoc.remove(startOffset, endOffset - startOffset);
                            }
                        } catch (BadLocationException ex) {
                            throw new IllegalStateException(ex);
                        }
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        //do nothing
                    }
                };

                fakePane.getDocument().addDocumentListener(listener);

                sdoc.runAtomicAsUser(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            legalReformat(fakePane);
                            final int caretLine = org.netbeans.editor.Utilities.getLineOffset(sdoc, originalPane.getCaretPosition());
                            final int caretCol = originalPane.getCaretPosition() - org.netbeans.editor.Utilities.getRowStart(sdoc, originalPane.getCaretPosition());
                            final Action shiftLineLeftAction = ((NbEditorKit) originalPane.getEditorKit()).getActionByName(BaseKit.shiftLineLeftAction);
                            for (int i = 0; i < getWrapperType((Jml) sdoc.getScml()).shiftCount; i++) {
                                originalPane.getCaret().setDot(0);
                                originalPane.getCaret().moveDot(originalPane.getDocument().getLength());
                                originalPane.getCaret().setSelectionVisible(true);
                                shiftLineLeftAction.actionPerformed(new ActionEvent(originalPane, -1, null));
                            }
                            originalPane.setCaretPosition(org.netbeans.editor.Utilities.getRowStartFromLineOffset(sdoc, caretLine) + Math.max(0, caretCol - 8));//8 is bad, but enough at the moment
                            //line numbers needs to be repaited
                            org.netbeans.editor.Utilities.getEditorUI(originalPane).getGlyphGutter().repaint();
                        } catch (BadLocationException ex) {
                            DialogUtils.messageError(ex);
                        } finally {
                            fakePane.getDocument().removeDocumentListener(listener);
                        }

                    }
                });

            } catch (BadLocationException ex) {
                DialogUtils.messageError(ex);
            }
        }

        private String getTagReplacement(final Scml.Tag tag, final ScmlEditorPane originalPane, final int fakeHeaderLength) {
            char[] tagReplacement = new char[originalPane.getScmlDocument().getTagMapper().findContainingBounds(fakePane.getDocument().getLength() - fakeHeaderLength).getLength()];
            Arrays.fill(tagReplacement, 'T');
            if (tag instanceof JmlTagCheckLicense || tag instanceof JmlTagReadLicense) {
                tagReplacement[tagReplacement.length - 1] = ';';
            }
            return new String(tagReplacement);
        }

        private String createHeader(Jml jml) {
            String wrapper = getWrapper(getWrapperType(jml));
            return wrapper.substring(0, wrapper.indexOf('#'));
        }

        private EWrapperType getWrapperType(Jml jml) {
            if (jml.getOwnerDef() instanceof AdsClassDef && ((AdsClassDef) jml.getOwnerDef()).getBody().ensureFirst() == jml) {
                return EWrapperType.BODY_WRAPPER;
            } else if (jml.getOwnerDef() instanceof AdsClassDef && ((AdsClassDef) jml.getOwnerDef()).getHeader().ensureFirst() == jml) {
                return EWrapperType.HEADER_WRAPPER;
            } else {
                return EWrapperType.METHOD_WRAPPER;
            }
        }

        private String createTrail(Jml jml) {
            String wrapper = getWrapper(getWrapperType(jml));
            return wrapper.substring(wrapper.indexOf('#') + 1);
        }

        private String getWrapper(EWrapperType wrapperType) {
            if (wrapperCache.containsKey(wrapperType)) {
                return wrapperCache.get(wrapperType);
            }
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/radixware/kernel/designer/common/dialogs/jmlnb/resources/" + wrapperType.fileName);
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder sb = new StringBuilder();
                int c = reader.read();
                while (c != -1) {
                    sb.appendCodePoint(c);
                    c = reader.read();
                }
                wrapperCache.put(wrapperType, sb.toString());
                return wrapperCache.get(wrapperType);
            } catch (UnsupportedEncodingException ex) {
                throw new RadixError("Unsupported encoding", ex);
            } catch (IOException ex) {
                throw new RadixError("Can't load wrapper file of type " + wrapperType, ex);
            }
        }
    }

    @EditorActionRegistration(name = JML_FIX_IMPORT_ACTION, mimeType = "text/x-jml")
    public static class JmlFixImportAction extends BaseAction {

        public JmlFixImportAction() {
            super(JML_FIX_IMPORT_ACTION);
        }

        @Override
        public void actionPerformed(final ActionEvent evt, final JTextComponent target) {
            if (target.getDocument() instanceof BaseDocument) {
                final BaseDocument baseDoc = (BaseDocument) target.getDocument();
                baseDoc.runAtomicAsUser(new Runnable() {
                    @Override
                    public void run() {
                        int caretOffset = target.getCaretPosition();
                        int startOffset = caretOffset - 1;
                        while (startOffset >= 0 && Character.isJavaIdentifierPart(baseDoc.getText().charAt(startOffset))) {
                            startOffset--;
                        }
                        startOffset++;

                        if (startOffset < caretOffset) {
                            if (!Character.isJavaIdentifierStart(baseDoc.getText().charAt(startOffset))) {
                                //identifier didn't start with right character - not a valid identifier
                                return;
                            }
                        }
                        int endOffset = caretOffset;
                        while (endOffset < baseDoc.getLength() && Character.isJavaIdentifierPart(baseDoc.getText().charAt(endOffset))) {
                            endOffset++;
                        }
                        assert startOffset <= endOffset;
                        try {
                            final String className = baseDoc.getText(new int[]{startOffset, endOffset});
                            final String importedClass = addImport(className, (AdsDefinition) ((ScmlEditorPane) target).getScml().getDefinition());
                            if (importedClass != null && endOffset == caretOffset) {
                                baseDoc.remove(startOffset, endOffset - startOffset);
                                baseDoc.insertString(startOffset, importedClass.substring(importedClass.lastIndexOf('.') + 1), null);
                            }
                        } catch (BadLocationException ex) {
                            //do nothing;
                        }

                    }
                });
            }
        }

        private String addImport(String className, AdsDefinition context) {
            final AdsClassDef classDef = getClassDef(context);
            if (classDef != null) {
                final String platformClassName = RadixPlatformClassPanel.choosePlatformClass(context, className.isEmpty() ? null : className);
                if (platformClassName != null) {
                    addImport(classDef, platformClassName);
                    return platformClassName;
                }
            }
            return null;
        }

        private AdsClassDef getClassDef(AdsDefinition context) {
            while (context != null) {
                if (context instanceof AdsClassDef) {
                    return (AdsClassDef) context;
                }
                context = context.getOwnerDef();
            }
            return null;
        }

        private void addImport(final AdsClassDef adsClass, final String className) {
            final Jml header = adsClass.getHeader().ensureFirst();
            final String headerText = getHeaderText(header);
            for (final String line : headerText.split("\\r?\\n")) {
                if (line.replaceAll("\\s", "").contains(className + ";")) {
                    return;
                }
            }
            final String importStatement = "import " + className + ";\n";
            final ScmlEditorPane editorPane = ScmlEditorRegistry.getDefault().getEditorPane(adsClass.getHeader().ensureFirst());
            if (editorPane != null) {
                try {
                    //header is opened in editor
                    editorPane.getDocument().insertString(0, importStatement, null);
                } catch (BadLocationException ex) {
                    DialogUtils.messageError(ex);
                }
            } else {
                //header is not opened in any editor
                header.getItems().add(0, Jml.Text.Factory.newInstance(importStatement));
            }
        }

        private String getHeaderText(final Jml header) {
            StringBuilder headerString = new StringBuilder();
            for (Scml.Item item : header.getItems()) {
                if (item instanceof Scml.Text) {
                    headerString.append(((Scml.Text) item).getText());
                } else {
                    // RADIX-7325
                    // ignore others
                    //throw new IllegalStateException("Unexpected Scml.Item in header: " + item);
                }
            }
            return headerString.toString();
        }
    }
}
