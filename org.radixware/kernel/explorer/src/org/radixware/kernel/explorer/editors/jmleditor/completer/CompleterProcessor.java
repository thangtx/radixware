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
package org.radixware.kernel.explorer.editors.jmleditor.completer;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.CaseSensitivity;
import com.trolltech.qt.core.Qt.FocusPolicy;
import com.trolltech.qt.core.Qt.KeyboardModifier;
import com.trolltech.qt.gui.QAbstractItemView;
import com.trolltech.qt.gui.QCompleter;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFontMetrics;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextCursor.MoveMode;
import com.trolltech.qt.gui.QTextCursor.MoveOperation;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.radixware.kernel.common.compiler.IWorkspaceProvider;
import org.radixware.kernel.common.compiler.core.completion.JmlCompletionEngine;
import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.userfunc.AdsUserFuncDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlCompletionProvider;
import org.radixware.kernel.common.scml.ScmlCompletionProvider.CompletionItem;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.SystemPropUtils;
import org.radixware.kernel.explorer.editors.jmleditor.JmlProcessor;
import org.radixware.kernel.explorer.editors.jmleditor.jmltags.JmlTag;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.TagProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;

public class CompleterProcessor extends QObject {
        
    private final boolean useWorkspaceCache = SystemPropUtils.getBooleanSystemProp("rdx.userfunc.use.workspace.cache", true);
    private final org.radixware.kernel.common.compiler.Compiler.WorkspaceCache wsCache = 
            new org.radixware.kernel.common.compiler.Compiler.WorkspaceCache(JmlCompletionEngine.getReporter());
    private QCompleter completer = null;
    private XscmlEditor editor;
    private TagProcessor tagConverter;
    
    private final PreloadCompletionHelper preloadHelper;
    private Callable<?> deferredCompletionTask;
            
    private boolean wasOpen = false;
    private boolean isCompleterRunning = false;
    private int startPosForCompletion = -1;
    private String completionPrefix = "";
    //private final ItemDelegate delegate = new ItemDelegate();
    //private boolean isPoint = false;
    private final CompleterWaiter waiter;
    private int prefixPos = 0;
    private int prevRow = 0;
    protected Map<String, HtmlCompletionItem> completionList;
    protected Scml.Item prevComplItem = null;
    protected int completerWidth = 500;
    protected CompletionListModel wordList;
    private int completerWidthDelta;
    QFontMetrics fontMetrics;
    QFontMetrics boldMetrics;
    private int complItemIconSize;

    public final static int MAX_COMPLETER_SIZE = 800;
    public final static int INITIAL_COMPLETER_ITEM_SIZE = 100;
    public final String NO_SUGGEST = Application.translate("JmlEditor", "No suggestions");
    public final String PLEASE_WAIT = Application.translate("JmlEditor", "Please wait...");
    public final String LOADING_BRANCH = Application.translate("JmlEditor", "Loading branch...");

    public CompleterProcessor(final XscmlEditor editor, final TagProcessor tagConverter) {
        this.editor = editor;
        this.tagConverter = tagConverter;
        this.waiter = new CompleterWaiter(editor.getEnvironment().getTracer());
        if (tagConverter instanceof JmlProcessor) {
            setCompleter();
        }
        preloadHelper = new PreloadCompletionHelper(this, editor.getEnvironment());
    }
    
    ScmlCompletionProvider.CompletionRequestor createRequestorWrapper(ScmlCompletionProvider.CompletionRequestor rq) {
        return useWorkspaceCache ? new CachedWorkspaceRequestor(rq, wsCache) : rq;
    }
    
    private boolean isCompletionReady() {
        return preloadHelper.isCompletionReady();
    }

    public void startPreloadCompletion() {
        preloadHelper.startPreloadCompletion();
    }
    
    void beginPreloadCompletion() {
        final PreloadCompletionTask task = new PreloadCompletionTask(this,
                (AdsUserFuncDef) tagConverter.getSource().getOwnerForQualifedName(),
                editor.getEnvironment());
        waiter.submitTask(task);
    }
        
    void afterPreloadCompletion() {
        if (deferredCompletionTask != null) {
            if (wasOpen) {
                waiter.submitTask(deferredCompletionTask);
            }
            deferredCompletionTask = null;
        }
    }

    public QCompleter getCompleter() {
        return completer;
    }

    public boolean getWasOpen() {
        return wasOpen;
    }

    public void setWasOpen(final boolean wasOpen) {
        this.wasOpen = wasOpen;
        if (!wasOpen) {
            completionPrefix = "";
            cancelWaiter();
        }
    }

    public void cancelWaiter() {
        if (waiter != null) {
            //We should cancel task only when preload is done            
            if (isCompletionReady()) {
                waiter.cancel();
            }
            isCompleterRunning = false;
        }
    }

    private void setCompleter() {
        completer = new QCompleter(this.editor);
        wasOpen = false;
        //if (completer != null) {
        completer.disconnect(editor);
        //}

        //if (completer == null) {
        //    return;
        //}
        completer.setWidget(editor);
        completer.highlightedIndex.connect(this, "pressed(QModelIndex)");
        completer.setCompletionMode(QCompleter.CompletionMode.PopupCompletion);
        completer.setCaseSensitivity(CaseSensitivity.CaseInsensitive);
        completer.activatedIndex.connect(this, "insertCompletionIndex(QModelIndex)");

        final QFont font = completer.popup().font();
        fontMetrics = new QFontMetrics(font);
        font.setBold(true);
        boldMetrics = new QFontMetrics(font);
        font.setBold(false);

        complItemIconSize = fontMetrics.height() + ItemDelegate.SPACE_HEIGHT;
    }

    public void exposeCompleter() {
        exposeCompleterImpl(null);
    }
    
    public void exposeQuickCompleter(final TagInfo tag) {
        exposeCompleterImpl(tag);
    }
    
    private void exposeCompleterImpl(final TagInfo tag) {
        if (tagConverter instanceof JmlProcessor && !isCompleterRunning) {
            editor.beforeExposeCompleter.emit();
            isCompleterRunning = true;
            popupPleaseWait();

            final QFont font = new QFont();
            final Callable<Map<String, HtmlCompletionItem>> task;
            if (tag != null) {
                final JmlTag jmlTag = (JmlTag) tag;
                prevComplItem = jmlTag.getTag();
                task = new QuickCompleterGetter(this, jmlTag.getTag(), font.resolve(completer.popup().font()));
            } else {
                Jml jml = ((JmlProcessor) tagConverter).toXml(editor.toPlainText(), editor.textCursor());
                setStartForComplerion();
                int info[] = itemIndexAndOffset(startPosForCompletion + 1, jml, ((JmlProcessor) tagConverter));
                task = new CompleterGetter(this, jml, info, font.resolve(completer.popup().font()), editor.getEnvironment().getTracer());
            }
            
            if (isCompletionReady()) {
                waiter.submitTask(task);
            } else {
                deferredCompletionTask = task;
            }
        }
    }

    private void hideCompleter() {
        completer.popup().hide();
        //completer.setQuickCompleter(false);
        wasOpen = false;
    }
    
    private void popupPleaseWait() {
        final LinkedHashMap<String, HtmlCompletionItem> cl = new LinkedHashMap<>();
        final String waitMessage = isCompletionReady() ? PLEASE_WAIT : LOADING_BRANCH;
        
        final SuggestCompletionItem item = new SuggestCompletionItem(null, waitMessage);
        final HtmlCompletionItem complItem = new HtmlCompletionItem(item, fontMetrics, boldMetrics, false, true);

        cl.put(waitMessage, complItem);
        setCompletionListModel(cl);
        //completer.popup().setItemDelegate(delegate);
        //completer.popup().setAlternatingRowColors(true);
        //completer.popup().setIconSize(new QSize(ItemDelegate.HEIGHT,ItemDelegate.HEIGHT));
        wasOpen = true;
        if (completer.model() instanceof CompletionListModel) {
            ((CompletionListModel) completer.model()).setCompletionPrefix(null);
        }
        completer.setCompletionPrefix(waitMessage.replace(".", ""));

        completerWidthDelta = 15;
        int width = complItem.getLenght() + completerWidthDelta;
        setCompleterParam(width);
    }

    protected void setStartForComplerion() {
        final QTextCursor tc = editor.textCursor();
        int curPos = tc.hasSelection() ? tc.selectionStart() : tc.position();
        int pos = curPos;
        final String text = editor.toPlainText();
        while (curPos > 0 && !isPoint(text.charAt(curPos - 1)) && !isOperatorOrSeparator(text.charAt(curPos - 1))) {
            curPos--;
        }
        final boolean isPoint = curPos > 0 ? isPoint(text.charAt(curPos - 1)) : false;
        completionPrefix = text.substring(curPos, pos);
        while (completionPrefix.length() > 0 && (completionPrefix.charAt(0) == ' ' || completionPrefix.charAt(0) == '\n')) {
            completionPrefix = completionPrefix.substring(1);
            curPos++;
        }

        prefixPos = curPos;
        if (isPoint) {
            startPosForCompletion = curPos;
        } else {
            startPosForCompletion = curPos + 1 <= pos ? curPos + 1 : curPos;
        }
    }

    private boolean isOperatorOrSeparator(final char c) {
        return (c == '+') || (c == '*') || (c == '|') || (c == '&') || (c == '>') || (c == '!') || (c == '=')
                || (c == '-') || (c == '/') || (c == '^') || (c == '%') || (c == '<') || (c == '~')
                || (c == ']') || (c == '(') || (c == ')') || (c == ';') || (c == ',') || (c == ' ') || (c == '\n');
    }

    private boolean isOperatorOrSeparator(final String s) {
        return s.contains("+") || s.contains("*") || s.contains("|") || s.contains("&") || s.contains(">") || s.contains("!") || s.contains("=")
                || s.contains("-") || s.contains("/") || s.contains("^") || s.contains("%") || s.contains("<") || s.contains("~")
                || s.contains("]") || s.contains("(") || s.contains(")") || s.contains(";") || s.contains(",");
    }

    private boolean isPoint(final char c) {
        return (c == '.') || (c == ':') || (c == '[');
    }

    private boolean isPoint(final String s) {
        return s.contains(".") || s.contains(":") || s.contains("[");
    }

    protected void setCompletionListModel(final Map<String, HtmlCompletionItem> completionList) {
        if (wordList == null) {
            wordList = new CompletionListModel();
            completer.setModel(wordList);
        }
        this.completionList = completionList;
        wordList.setList(completionList);
    }

    private void popupCompleter(final Map<String, HtmlCompletionItem> completionList) {
        setCompletionListModel(completionList);
        //completer.popup().setItemDelegate(delegate);
        //completer.popup().setAlternatingRowColors(true);
        //completer.popup().setIconSize(new QSize(ItemDelegate.HEIGHT,ItemDelegate.HEIGHT));
        showCompleter();
    }

    protected void showCompleter() {
        wasOpen = true;
        if (completer.model() instanceof CompletionListModel) {
            ((CompletionListModel) completer.model()).setCompletionPrefix(completionPrefix);
        }
        completer.setCompletionPrefix(completionPrefix);
        if (completer.popup().model().rowCount() > 0) {
            setCompleterParam(completerWidth);
        } else {
            showNoSuggestionFound();
        }
    }

    private void showNoSuggestionFound() {
        SuggestCompletionItem item = new SuggestCompletionItem(null, NO_SUGGEST);

        HtmlCompletionItem htmlItem = new HtmlCompletionItem(item, fontMetrics, boldMetrics, false, true);
        if (!wordList.containsItem(NO_SUGGEST)) {
            wordList.addItem(NO_SUGGEST, htmlItem);
            completionList.put(NO_SUGGEST, htmlItem);
        }
        if (completer.model() instanceof CompletionListModel) {
            ((CompletionListModel) completer.model()).setCompletionPrefix(null);
        }
        completer.setCompletionPrefix(NO_SUGGEST);

        completerWidthDelta = 15;
        int width = htmlItem.getLenght() + completerWidthDelta;
        //int width= HtmlItemCreator.getColumnWidth(text, completer.popup().font())+15;
        setCompleterParam(width);
    }

    public void showCompleter(final QKeyEvent e) {
        final boolean ctrlOrShift = ((e.modifiers().value() & (KeyboardModifier.ControlModifier.value() | KeyboardModifier.ShiftModifier.value())) > 0);
        if ((completer == null) || (ctrlOrShift && e.text().isEmpty())) {
            return;
        }
        if (wasOpen) {
            final QTextCursor tc = editor.textCursor();
            final int curPos = tc.position();

            final String s = editor.toPlainText();
            if ((startPosForCompletion >= 0) && (startPosForCompletion <= curPos)) {
                completionPrefix = s.substring(prefixPos, curPos);
                if ((isOperatorOrSeparator(completionPrefix)) || (isPoint(completionPrefix))) {
                    hideCompleter();
                    return;
                }
            } else if ((startPosForCompletion > 0) && (curPos >= 0) && (curPos <= editor.toPlainText().length())) {
                hideCompleter();
                return;
            }

            while (completionPrefix.length() > 0 && ((completionPrefix.charAt(0) == ' ') || (completionPrefix.charAt(0) == '\n'))) {
                completionPrefix = completionPrefix.substring(1);
            }
            if (completer.model() instanceof CompletionListModel) {
                ((CompletionListModel) completer.model()).setCompletionPrefix(completionPrefix);
            }
            completer.setCompletionPrefix(completionPrefix);
            if (completer.popup().model().rowCount() > 0) {
                wordList.removeItem(NO_SUGGEST);
                setCompleterParam(completerWidth);
            } else {
                showNoSuggestionFound();
            }
        }
    }

    private void setCompleterParam(int width) {
        final QRect completerRect = editor.cursorRect();
        completerRect.setWidth(width);
        wordList.setItemWidth(width - completerWidthDelta);
        QAbstractItemView popup = completer.popup();
        if (popup.model().rowCount() > 0) {
            popup.setCurrentIndex(popup.model().index(0, 0));
            popup.setItemDelegate(new ItemDelegate(popup));
            popup.setAlternatingRowColors(true);
            popup.setIconSize(new QSize(complItemIconSize, complItemIconSize));
            completer.complete(completerRect);
            popup.setFocusPolicy(FocusPolicy.StrongFocus);
        }
    }

    @SuppressWarnings("unused")
    private void pressed(final QModelIndex index) {
        if (index == null) {
            final QModelIndex prevIndex = completer.popup().model().index(prevRow, 0);
            final HtmlCompletionItem obj = completionList.get((String) prevIndex.data(Qt.ItemDataRole.UserRole));
            if (obj != null && obj.getCompletionItem() instanceof SuggestCompletionItem) {
                hideCompleter();
            }
        } else {
            prevRow = index.row();
        }
    }

    @SuppressWarnings("unused")
    private void insertCompletionIndex(final QModelIndex current) {
        if ((completer.widget() != editor) || (!wasOpen)) {
            return;
        }
        wasOpen = false;

        final HtmlCompletionItem obj = completionList.get((String) current.data(Qt.ItemDataRole.UserRole));
        if (obj != null && obj.getCompletionItem() != null) {
            insertCompletionIndex(obj.getCompletionItem(), prevComplItem);
        }
    }

    private void insertCompletionIndex(final CompletionItem obj, final Scml.Item prevComplItem) {
        if (tagConverter.getSource() == null) {
            return;
        }
        final QTextCursor tc = editor.textCursor();
        if ((obj != null) && (obj.getNewItems() != null)) {
            if (tc.hasSelection()) {
                editor.deleteSelectedText(tc);
            }
            try {
                editor.undoTextChange();
                editor.blockSignals(true);
                //editor.document().blockSignals(true);
                tc.beginEditBlock();

                deletePrefix(tc, obj.getReplaceStartOffset());
                removePreviousTag(tc, obj, prevComplItem);

                final Scml.Item[] items = obj.getNewItems();
                int[] insertPos = new int[]{-1};
                for (int i = 0; i < items.length; i++) {
                    tagConverter.getSource().getItems().add(items[i]);
                    if (items[i] instanceof Scml.Text) {
                        final String completion = ((Scml.Text) items[i]).getText();
                        if (completion.startsWith(EDefinitionIdPrefix.LIB_USERFUNC_PREFIX.getValue())) {
                            Definition owner = tagConverter.getSource().getOwnerDefinition();
                            if (owner instanceof AdsUserFuncDef) {
                                int index = completion.indexOf("()");
                                index = index == -1 ? completion.length() - 1 : index;
                                String sId = completion.substring(0, index);//.replace(AdsLibUserFuncWrapper.LIB_USERFUNC_NAME_SEPARATOR, EDefinitionIdPrefix.LIB_USERFUNC_ID_SEPARATOR);
                                AdsDefinition def = AdsUserFuncDef.Lookup.findTopLevelDefinition((AdsUserFuncDef) owner, Id.Factory.loadFrom(sId));
                                JmlTagInvocation invokTag = JmlTagInvocation.Factory.newInstance(def);
                                ((AdsUserFuncDef) owner).getSource().getItems().add(invokTag);
                                editor.insertTagFromCompleter(invokTag, tc, "", insertPos);
                            }
                        } else {
                            tc.insertText(completion, editor.getDefaultCharFormat());
                        }
                    } else {
                        editor.insertTagFromCompleter(items[i], tc, "", insertPos);
                    }
                }
            } finally {
                tc.endEditBlock();
                //editor.document().blockSignals(false);
                editor.blockSignals(false);
                editor.textChanged.emit();
            }
        }
        editor.setTextCursor(tc);
        editor.setFocusInText();
    }

    private void removePreviousTag(final QTextCursor tc, final CompletionItem obj, final Scml.Item prevComplItem) {
        if ((prevComplItem != null) && (prevComplItem instanceof Scml.Tag) && (obj.removePrevious(prevComplItem))) {
            int pos = tc.position();
            final TagInfo tag = ((JmlProcessor) tagConverter).getTagInfoFromMap((Scml.Tag) prevComplItem);
            if (tag != null) {
                editor.deleteTag(tc, tag);
                pos = pos - (int) (tag.getEndPos() - tag.getStartPos());
                if (pos > tc.position()) {
                    tc.movePosition(MoveOperation.Right, MoveMode.KeepAnchor, pos - tc.position());
                    tc.removeSelectedText();
                }
            }
        }
    }

    private void deletePrefix(final QTextCursor tc, final int start) {
        if (start >= 0) {
            tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, start + (tc.position() - startPosForCompletion));
            if (tc.hasSelection()) {
                final List<TagInfo> deletedTags = editor.getTagConverter().getTagListInSelection(tc.selectionStart() + 1, tc.selectionEnd() + 1, true);
                for (TagInfo tag : deletedTags) {
                    tagConverter.deleteTag(tag);
                }
                tc.removeSelectedText();
            }
            final int n = tc.position() - 5;
            if (n >= 0) {
                final String s = editor.toPlainText().substring(n, tc.position());
                if ("idof[".equals(s)) {
                    tc.movePosition(MoveOperation.Left, MoveMode.KeepAnchor, "idof[".length());
                    tc.removeSelectedText();
                }
            }
        }
    }

    private int[] itemIndexAndOffset(final int pos, final Jml jml, final JmlProcessor jmlConverter) {
        if (pos - 1 > editor.toPlainText().length()) {
            return null;
        }
        final Iterator<Scml.Item> it = jml.getItems().iterator();
        Scml.Item item;
        int idx = -1;
        int begin = 1;
        int end = 1;
        while (end <= pos && it.hasNext()) {
            item = it.next();
            begin = end;
            if (item instanceof Scml.Text) {
                end += ((Scml.Text) item).getText().length();
            } else if (item instanceof Scml.Tag) {
                TagInfo tag = jmlConverter.getTagInfoForCursorMove(end, false);
                if (tag != null) {
                    long endPos = tag.getEndPos();
                    end = (int) endPos;
                }
            }
            idx++;
        }
        return new int[]{idx, pos - begin};
    }

    private void doExposeCompleter(Map<String, HtmlCompletionItem> cl, Scml.Item prevComplItem, int itemWidth) {
        if (!isCompleterRunning) {
            return;
        }
        this.prevComplItem = prevComplItem;
        try {
            if ((cl != null) && wasOpen) {
                completerWidthDelta = 10 + complItemIconSize * 2;
                completerWidth = itemWidth + completerWidthDelta;
                popupCompleter(cl);
            } else if (wasOpen) {
                hideCompleter();
            }
        } finally {
            isCompleterRunning = false;
        }
    }

    private void doExposeQuickCompleter(Map<String, HtmlCompletionItem> cl, int itemWidth) {
        if (!isCompleterRunning) {
            return;
        }
        try {
            if (cl != null && wasOpen) {
                final QTextCursor tc = editor.textCursor();
                prefixPos = startPosForCompletion = tc.position();
                completerWidthDelta = 10 + complItemIconSize * 2;
                completerWidth = itemWidth + completerWidthDelta;
                popupCompleter(cl);
            } else if (wasOpen) {
                hideCompleter();
            }
        } finally {
            isCompleterRunning = false;
        }
    }
    
    static class ExposeCompleterEvent extends QEvent {

        public final Scml.Item prevComplItem;
        public final int itemWidth;
        public final Map<String, HtmlCompletionItem> cl;

        public ExposeCompleterEvent(final Map<String, HtmlCompletionItem> cl, final int itemWidth, final Scml.Item prevComplItem) {
            super(QEvent.Type.User);
            this.prevComplItem = prevComplItem;
            this.itemWidth = itemWidth;
            this.cl = cl;
        }
    }

    static class ExposeQuickCompleterEvent extends QEvent {

        public final int itemWidth;
        public final Map<String, HtmlCompletionItem> cl;

        public ExposeQuickCompleterEvent(final Map<String, HtmlCompletionItem> cl, final int itemWidth) {
            super(QEvent.Type.User);
            this.cl = cl;
            this.itemWidth = itemWidth;
        }
    }
        
    @Override
    protected void customEvent(QEvent event) {
        if (event instanceof ExposeCompleterEvent) {
            ExposeCompleterEvent ev = (ExposeCompleterEvent) event;
            doExposeCompleter(ev.cl, ev.prevComplItem, ev.itemWidth);
        } else if (event instanceof ExposeQuickCompleterEvent) {
            ExposeQuickCompleterEvent ev = (ExposeQuickCompleterEvent) event;
            doExposeQuickCompleter(ev.cl, ev.itemWidth);
        } else {
            super.customEvent(event);
        }
    }
    
    public void cleanup() {
        preloadHelper.cleanup();
        waiter.close();
    }
    
    private static final class CachedWorkspaceRequestor implements IWorkspaceProvider, ScmlCompletionProvider.CompletionRequestor {

        private final ScmlCompletionProvider.CompletionRequestor delegate;
        private final org.radixware.kernel.common.compiler.Compiler.WorkspaceCache wsCache;
        
        public CachedWorkspaceRequestor(ScmlCompletionProvider.CompletionRequestor delegate, org.radixware.kernel.common.compiler.Compiler.WorkspaceCache wsCache) {
            this.delegate = delegate;
            this.wsCache = wsCache;
        }

        @Override
        public void accept(CompletionItem item) {
            delegate.accept(item);
        }

        @Override
        public boolean isAll() {
            return delegate.isAll();
        }

        @Override
        public AdsWorkspace getWorkspace(Layer l, ERuntimeEnvironmentType env) {
            return wsCache.get(l, env);
        }
        
    }
}
