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

package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.core.QRegExp;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.QTimer;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QLineEdit;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextDocument;
import com.trolltech.qt.gui.QTextEdit;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.kernel.explorer.editors.xscmleditor.XscmlEditor;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class SearchPanel extends QWidget {

    private final XscmlEditor editText;
    private final JmlEditor parent;
    private final QLineEdit edSearchingText;
    private final QPushButton btnPrev;
    private final QPushButton btnNext;
    private final QCheckBox cbCaseSensitive;
    private final QCheckBox cbWholeWord;
    private final QCheckBox cbRegExpression;
    private final QCheckBox cbHighlightResults;
    private QWidget replacePanel;
    private final QLineEdit edReplaceText;
    private final QPushButton btnReplace;
    private final QPushButton btnReplaceAll;
    private final QCheckBox cbBackwards;
    private final QToolButton btnClose;
    private final QTimer searchTimer;
    private final MatchesLabel matchesLabel;
    private final ISearchEngine engine;
    
    public static final int SEARCH_TIMER_INTERVAL_MS = 300;
    private static final String NO_MATCHES_MESSAGE = Application.translate("JmlEditor", "No matches");
    private static final String MATCH_MESSAGE = Application.translate("JmlEditor", "%d of %d matches");

    SearchPanel(final JmlEditor parent) {
        super(parent);
        this.parent = parent;
        this.editText = parent.getTextEditor();
        edSearchingText = new QLineEdit(this);
        edSearchingText.setObjectName("edSearchingText");
        btnPrev = new QPushButton(this);
        btnPrev.setObjectName("btnPrev");
        btnNext = new QPushButton(this);
        btnNext.setObjectName("btnNext");
        
        btnClose = new QToolButton(this);
        btnClose.setObjectName("btnClose");
        btnClose.setIcon(ExplorerIcon.getQIcon(JmlEditor.JmlEditorIcons.CLOSE));
        btnClose.setBaseSize(15, 15);
        btnClose.setIconSize(new QSize(15, 15));
         
        QSize minSize = new QSize(0, 0);
        List<QCheckBox> cboxes = new ArrayList<>(4);
        cbCaseSensitive = new CheckBoxWithMinSize(this, minSize);
        cbCaseSensitive.setObjectName("checkBoxCase");
        cbWholeWord = new CheckBoxWithMinSize(this, minSize);
        cbWholeWord.setObjectName("checkBoxWhole");
        cbRegExpression = new CheckBoxWithMinSize(this, minSize);
        cbHighlightResults = new CheckBoxWithMinSize(this, minSize);
        cboxes.add(cbCaseSensitive);
        cboxes.add(cbWholeWord);
        cboxes.add(cbRegExpression);
        cboxes.add(cbHighlightResults);
        QSizePolicy sp = new QSizePolicy(QSizePolicy.Policy.Maximum, QSizePolicy.Policy.Maximum);
        for (QCheckBox cb : cboxes) {
            cb.setSizePolicy(sp);
        }

        edReplaceText = new QLineEdit(this);
        edReplaceText.setObjectName("edReplaceText");
        btnReplace = new QPushButton(this);
        btnReplace.setObjectName("btnReplace");
        btnReplaceAll = new QPushButton(this);
        btnReplaceAll.setObjectName("btnReplaceAll");
        cbBackwards = new QCheckBox(this);
        cbBackwards.setObjectName("cbBackwards");
        
        matchesLabel = new MatchesLabel(this);
        engine = new SearchEngine(editText, null);
        
        searchTimer = new QTimer(this);
        searchTimer.setSingleShot(true);
        searchTimer.setInterval(SEARCH_TIMER_INTERVAL_MS);
        
        createUi();
    }
    
    private class MatchesLabel extends QLabel {
        
        private int matchCnt = -1;
        private int totalCnt = -1;

        public MatchesLabel(QWidget parent) {
            super(parent);
        }
        
        public int getMatchCnt() {
            return matchCnt;
        }
        
        public int getTotalCnt() {
            return totalCnt;
        }

        public void setMatchCnt(int matchCnt, int totalCnt) {
            this.matchCnt = matchCnt;
            this.totalCnt = totalCnt;
            if (matchCnt < 0 || totalCnt < 0) {
                setText(null);
            } else if (totalCnt == 0) {
                setText(NO_MATCHES_MESSAGE);
            } else {
                setText(String.format(MATCH_MESSAGE, matchCnt, totalCnt));
            }
        }
    }
    
    private class CheckBoxWithMinSize extends QCheckBox {

        private final QSize minSize;

        public CheckBoxWithMinSize(QWidget parent, QSize minSize) {
            super(parent);
            this.minSize = minSize;
        }
        
        @Override
        public QSize minimumSizeHint() {
            return minSize;
        }
        
    }
    
    public static interface ITextLocation {

        int getStart();

        int getEnd();
    }
    
    public static interface ISearchEngine {
        
        List<ITextLocation> getMatches();
        
        int getMatchLen();
        
        int[] getCurPosAndIndex();
        
        void setCurPosAndIndex(int pos, int index);
        
        boolean setPattern(QRegExp pattern);
        
    }
    
    private class SearchEngine implements ISearchEngine {
        
        private final QTextEdit textEditor;
        private QRegExp regex;
        private List<ITextLocation> matches;
        private int[] curPosAndIndex = null;
        private boolean isSearchResultActual = false;
        private int matchedLength = -1;

        public SearchEngine (QTextEdit qte, QRegExp regex) {
            if (qte == null) {
                throw new NullPointerException("SearchEngine construnctor parameter is null");
            }
            this.textEditor = qte;
            this.regex = regex;
            
            textEditor.textChanged.connect(this, "onTextChanged()");
        }

        @Override
        public int getMatchLen() {
            return matchedLength;
        }
        
        @Override
        public List<ITextLocation> getMatches() {
            if(regex == null) {
                return Collections.<ITextLocation>emptyList();
            }
            if (!isSearchResultActual) {
                matches = new ArrayList<>();
                final String text = textEditor.toPlainText();
                int endPos, startPos = regex.indexIn(text, 0);
                matchedLength = regex.matchedLength();
                while (startPos >= 0) {
                    endPos = startPos + matchedLength;
                    matches.add(new Location(startPos, endPos));
                    startPos = regex.indexIn(text, endPos);
                }
                isSearchResultActual = true;
            }
            return matches;
        } 

        @Override
        public int[] getCurPosAndIndex() {
            return curPosAndIndex;
        }

        @Override
        public void setCurPosAndIndex(int curPos, int index) {
            this.curPosAndIndex = new int[] {curPos, index};
        }
        
        @Override
        public boolean setPattern(QRegExp regex) {
            if (this.regex != null && regex != null && this.regex.equals(regex)) {
                return false;
            } else {
                this.regex = regex;
                clearState();
                return true;
            }
        }
                
        private void clearState() {
            isSearchResultActual = false;
            matchedLength = -1;
            curPosAndIndex = null;
        }
        
        private void onTextChanged() {
            clearState();
        }
        
    }
    
    private class Location implements ITextLocation {
        
        private final int start;
        private final int end;

        public Location(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
        
        @Override
        public int getStart() {
            return start; 
        }

        @Override
        public int getEnd() {
            return end;
        }
    }

    private void createUi() {
        this.setMinimumHeight(25);
        this.setMaximumHeight(25);
        final QVBoxLayout mainLayout = new QVBoxLayout();
        mainLayout.setMargin(0);

        final QHBoxLayout searchLayout = new QHBoxLayout();
        searchLayout.setMargin(0);
        final QLabel lb = new QLabel(this);
        lb.setText(Application.translate("JmlEditor", "Search") + ": ");
        edSearchingText.textChanged.connect(this, "findText(String)");
        edSearchingText.setMinimumWidth(200);
        btnPrev.setText(Application.translate("JmlEditor", "Previous"));
        btnPrev.clicked.connect(this, "btnPrevClicked()");
        btnNext.setText(Application.translate("JmlEditor", "Next"));
        btnNext.clicked.connect(this, "btnNextClicked()");
        btnClose.setText(null);
        btnClose.clicked.connect(this, "btnCloseClicked()");
        btnClose.setAutoRaise(true);
        //btnClose.setStyle(QStyle.);
        cbCaseSensitive.setText(Application.translate("JmlEditor", "Case sensitive"));
        cbCaseSensitive.stateChanged.connect(this, "btnNextClicked()");
        cbWholeWord.setText(Application.translate("JmlEditor", "Whole word"));
        cbWholeWord.stateChanged.connect(this, "btnNextClicked()");
        cbRegExpression.setText(Application.translate("JmlEditor", "Regular Expression"));
        cbRegExpression.stateChanged.connect(this, "changeRegExpState(Integer)");
        cbHighlightResults.setText(Application.translate("JmlEditor", "Highlight results"));
        cbHighlightResults.setCheckState(Qt.CheckState.Checked);
        cbHighlightResults.stateChanged.connect(this, "changeHighlightResultsState(Integer)");
        
        final QWidget searchOptsPanel = new QWidget(this) {

            private final QSize size = new QSize(0, 0);
            
            @Override
            public QSize minimumSizeHint() {
                return size;
            }
            
        };
        final QHBoxLayout searchOptsLayout = new QHBoxLayout();
        searchOptsLayout.setMargin(0);
        searchOptsPanel.setLayout(searchOptsLayout);
        
        searchLayout.addWidget(lb);
        searchLayout.addWidget(edSearchingText);
        searchLayout.addWidget(btnPrev);
        searchLayout.addWidget(btnNext);
        
        searchOptsLayout.addWidget(cbCaseSensitive);
        searchOptsLayout.addWidget(cbWholeWord);
        searchOptsLayout.addWidget(cbRegExpression);
        searchOptsLayout.addWidget(cbHighlightResults);
        searchLayout.addWidget(searchOptsPanel);
        
        searchLayout.addWidget(matchesLabel, 1, Qt.AlignmentFlag.AlignRight);
        searchLayout.addWidget(btnClose, 0, Qt.AlignmentFlag.AlignRight);
        
        replacePanel = createReplasePanel();
        replacePanel.setVisible(false);

        mainLayout.addLayout(searchLayout);
        mainLayout.addWidget(replacePanel);
        this.setLayout(mainLayout);
        
        searchTimer.timeout.connect(this, "btnNextClicked()");
    }

    private QWidget createReplasePanel() {
        final QWidget replasePanel = new QWidget(this);
        final QHBoxLayout replaceLayout = new QHBoxLayout();
        replaceLayout.setMargin(0);
        final QLabel lb = new QLabel(this);
        lb.setText(Application.translate("JmlEditor", "Replace") + ":");
        //edReplaceText.textChanged.connect(this,"findText(String)");
        edReplaceText.setMinimumWidth(200);
        btnReplace.setText(Application.translate("JmlEditor", "Replace"));
        btnReplace.clicked.connect(this, "replaceText()");
        btnReplaceAll.setText(Application.translate("JmlEditor", "Replace all"));
        btnReplaceAll.clicked.connect(this, "replaceTextAll()");
        cbBackwards.setText(Application.translate("JmlEditor", "Backwards"));
        //cbBackwards.stateChanged.connect(this,"replaceText()");       
        replaceLayout.addWidget(lb);
        replaceLayout.addWidget(edReplaceText);
        replaceLayout.addWidget(btnReplace);
        replaceLayout.addWidget(btnReplaceAll);
        replaceLayout.addWidget(cbBackwards, 1, Qt.AlignmentFlag.AlignLeft);
        replasePanel.setLayout(replaceLayout);
        return replasePanel;
    }

    public void setReplacePanelVisible(final boolean isVisible) {
        if (isVisible /*&& !isVisible()*/) {
            setVisible(isVisible);
            this.setMinimumHeight(50);
            this.setMaximumHeight(50);
        } else {
            this.setMinimumHeight(25);
            this.setMaximumHeight(25);
        }
        replacePanel.setVisible(isVisible);
    }

    public String getSearchingText() {
        return edSearchingText.text();
    }
    
    public void setSearchingText(final String text) {
        edSearchingText.setText(text);
    }

    public boolean isCaseSensitive() {
        return cbCaseSensitive.isChecked();
    }

    public boolean isWholeWord() {
        return cbWholeWord.isChecked();
    }

    public boolean isRegulerExpression() {
        return cbRegExpression.isChecked();
    }

    public boolean isBackWardsReplace() {
        return cbBackwards.isChecked();
    }

    public String getReplaceText() {
        return edReplaceText.text();
    }

    void setFocusInSearchEditor() {
        edSearchingText.setFocus();
        final String text = edSearchingText.text();
        if (text != null && !text.isEmpty()) {
            edSearchingText.setSelection(0, edSearchingText.text().length());
        }
    }

    @SuppressWarnings("unused")
    private void changeHighlightResultsState(final Integer highlightResults) {
        final boolean isHighlightResults = cbHighlightResults.isChecked();
        highlightSearchResult(isHighlightResults ? getSearchingText() : null);
    }

    @SuppressWarnings("unused")
    private void changeRegExpState(final Integer highlightResults) {
        final boolean isRegExp = cbRegExpression.isChecked();
        cbWholeWord.setEnabled(!isRegExp);
        btnNextClicked();
    }

    @SuppressWarnings("unused")
    private void btnPrevClicked() {
        editText.focusWidget();
        searchText(getSearchingText(), false);
    }

    @SuppressWarnings("unused")
    private void btnNextClicked() {
        editText.focusWidget();
        searchText(getSearchingText(), true);
    }
    
    @SuppressWarnings("unused")
    private void btnCloseClicked(){
        parent.closeSearchPanel();
        parent.setSearchPanelVisible(false);
    }

    @SuppressWarnings("unused")
    private void findText(final String s) {
        searchTimer.start();
    }

    private void searchText(final String text,final boolean isForward) {
        final QRegExp searchReg = createSearchText(text);
        if (engine.setPattern(searchReg) && cbHighlightResults.isChecked()) {
            highlightSearchResult(searchReg);
        }
        final QTextDocument.FindFlags flags = getFindOptions(isForward);
        selectCurrent(text, flags);
    }
    
    private void highlightSearchResult(final String text) {
        final QRegExp searchReg = createSearchText(text);
        highlightSearchResult(searchReg);
    }
    
    private void highlightSearchResult(final QRegExp searchReg) {
        parent.setSearchText(searchReg);
    }

    private void selectCurrent(final String findText, final QTextDocument.FindFlags flags) {
        final QTextCursor tcOld = editText.textCursor();
        final int curPos = tcOld.position();
        final boolean isBackward = flags.isSet(QTextDocument.FindFlag.FindBackward);
        edSearchingText.setStyleSheet("color: black;");
        
        List<ITextLocation> matches = engine.getMatches();
        if (matches.isEmpty()) {
            tcOld.clearSelection();
            editText.setTextCursor(tcOld);
            if (!findText.isEmpty()) {
                edSearchingText.setStyleSheet("color: darkRed ;");
                matchesLabel.setMatchCnt(0, 0);
            } else {
                matchesLabel.setMatchCnt(-1, -1);
            }
            return;
        }
        
        int foundIndex = -1;
        int[] posAndIndex = engine.getCurPosAndIndex();
        if (posAndIndex != null && posAndIndex[0] == curPos) {
            if (isBackward) {
                foundIndex = (posAndIndex[1] - 1) % matches.size();
            } else {
                foundIndex = (posAndIndex[1] + 1) % matches.size();
            }
        } else {
            if (isBackward) {
                for (int index = matches.size() - 1; index >= 0; index--) {
                    if (curPos >= matches.get(index).getStart()) {
                        foundIndex = index;
                        break;
                    }
                }
            } else {
                for (int index = 0; index < matches.size(); index++) {
                    if (matches.get(index).getStart() >= curPos) {
                        foundIndex = index;
                        break;
                    }
                }
            }
        }
        
        if (foundIndex == -1) {
            if (isBackward) {
                foundIndex = matches.size() - 1;
            } else {
                foundIndex = 0;
            }
        }
        
        int newPos = matches.get(foundIndex).getStart();
        tcOld.setPosition(newPos);
        if (!isFindTextInTag(tcOld, false)) {
            tcOld.movePosition(QTextCursor.MoveOperation.Right, QTextCursor.MoveMode.KeepAnchor, engine.getMatchLen());
            editText.setTextCursor(tcOld);
        }
        
        engine.setCurPosAndIndex(editText.textCursor().position(), foundIndex);
        matchesLabel.setMatchCnt(foundIndex + 1, matches.size());
    }

    private QRegExp createSearchText(final String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }

        final QRegExp res = new QRegExp();
        String pattern = text;

        if (!isRegulerExpression()) {
            pattern = QRegExp.escape(pattern);
            if (isWholeWord()) {
                pattern = "\\b" + pattern + "\\b";
            }
        }
        final Qt.CaseSensitivity caseSensitive = isCaseSensitive() ? Qt.CaseSensitivity.CaseSensitive : Qt.CaseSensitivity.CaseInsensitive;
        res.setCaseSensitivity(caseSensitive);
        res.setPattern(pattern);
        return res;
    }

    private QTextDocument.FindFlags getFindOptions(final boolean isForward) {
        final QTextDocument.FindFlags flags = new QTextDocument.FindFlags();
        if (!isRegulerExpression() && isWholeWord()) {
            flags.set(QTextDocument.FindFlag.FindWholeWords);
        }
        if (isCaseSensitive()) {
            flags.set(QTextDocument.FindFlag.FindCaseSensitively);
        }
        if (!isForward) {
            flags.set(QTextDocument.FindFlag.FindBackward);
        }
        return flags;
    }

    private boolean isFindTextInTag(final QTextCursor tc, final boolean replace) {
        if (editText.isCursorOnTag(tc.position(), true)) {
            tc.setPosition(tc.position() + 1);
            final TagInfo tag = editText.selectTagUnderCursor(tc);
            if (replace) {
                editText.getTagConverter().deleteTag(tag);
                tc.removeSelectedText();
                tc.setCharFormat(editText.getDefaultCharFormat());
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings("unused")
    private void replaceTextAll() {
        final QTextDocument.FindFlags flags = getFindOptions(!isBackWardsReplace());
        flags.clear(QTextDocument.FindFlag.FindBackward);
        final String findWhat = getSearchingText();
        final String replaceWith = getReplaceText();

        final QTextCursor tc = editText.textCursor();
        tc.setPosition(0);
        editText.setTextCursor(tc);
        while (editText.find(findWhat, flags)) {
            replaceText(flags, findWhat, replaceWith);
            editText.setTextCursor(editText.textCursor());
        }
        //findDialog.accept();
        editText.setCursorInEditor(editText.toPlainText().length());
        editText.setFocusInText();
    }

    @SuppressWarnings("unused")
    private void replaceText() {
        final String findWhat = getSearchingText();
        final String replaceWith = getReplaceText();
        if (findWhat.equals(replaceWith)) {
            return;
        }
        final QTextDocument.FindFlags flags = getFindOptions(!isBackWardsReplace());
        replaceText(flags, findWhat, replaceWith);
        if ((flags.value() & QTextDocument.FindFlag.FindBackward.value()) > 0) {
            editText.textCursor().movePosition(QTextCursor.MoveOperation.Left, QTextCursor.MoveMode.MoveAnchor, replaceWith.length());
        }
        editText.setTextCursor(editText.textCursor());
        editText.focusWidget();
        searchText(findWhat, !isBackWardsReplace());
    }

    private void replaceText(final QTextDocument.FindFlags flags, final String findWhat, final String replaceWith) {
        if (editText.textCursor().hasSelection()) {
            replaseSelectedText(replaceWith);
        } else {
            if (editText.find(findWhat, flags)) {
                replaseSelectedText(replaceWith);
            } else {
                exposeMessageInformation(findWhat);
            }
        }

    }

    private void replaseSelectedText(final String replaceWith) {
        final QTextCursor tc = editText.textCursor();
        try {
            editText.undoTextChange();
            editText.blockSignals(true);
            //editText.document().blockSignals(true);
            tc.beginEditBlock();

            isFindTextInTag(tc, true);
            tc.insertText(replaceWith);
        } finally {
            tc.endEditBlock();
            //editText.document().blockSignals(false);
            editText.blockSignals(false);
            editText.textChanged.emit();
        }
    }

    private void exposeMessageInformation(final String text) {
        final String title = Application.translate("ExplorerDialog", "Information");
        final String message = Application.translate("ExplorerDialog", "String \'%s\' not found");
        Application.messageInformation(title, String.format(message, text));
    }
    /*void setFindDialog(FindAndReplaceDialog d) {
     findDialog = d;
     }
    
     private FindAndReplaceDialog findDialog;
     public void replaceTextCall() {
     final ExplorerSettings settings = (ExplorerSettings) parent.getEnvironment().getConfigStore();
     FindAndReplaceDialog dialog = new FindAndReplaceDialog((QWidget) editText, settings, "jmlEditor/findAndReplaseDialog", true);
     dialog.setWindowTitle(Application.translate("JmlEditor", "Replace") + "...");
     findDialog = dialog;
     dialog.find.connect(this, "findText()");
     dialog.replace.connect(this, "replaceText()");
     dialog.replaceAll.connect(this, "replaceTextAll()");
     dialog.finished.connect(this,"onCloseReplaceDialog()");
     dialog.exec();
     }
    
     @SuppressWarnings("unused")
     private void onCloseReplaceDialog(){
     //parent.clearFindTextPos();
     parent.setSearchText(null);
     parent.rehighlight();
     }
    
     private QTextDocument.FindFlags getFindOptions() {
     QTextDocument.FindFlags flags = new QTextDocument.FindFlags();
     if (findDialog.isWholeWordChecked()) {
     flags.set(QTextDocument.FindFlag.FindWholeWords);
     }
     if (findDialog.isMatchCaseChecked()) {
     flags.set(QTextDocument.FindFlag.FindCaseSensitively);
     }
     if (!findDialog.isForwardChecked()) {
     flags.set(QTextDocument.FindFlag.FindBackward);
     }
     return flags;
     }
     * private void findText(String findText ,QTextDocument.FindFlags flags,boolean forReplace) { 
     parent.clearFindTextPos();
     final QTextCursor tcOld=editText.textCursor();     
     final QTextCursor tc=editText.textCursor();
     int position=(flags.value() & QTextDocument.FindFlag.FindBackward.value()) > 0 ? editText.toPlainText().length()-1 : 0;
     tc.setPosition(position);
        
     boolean isFound=findAll( findText, flags, tc);
     editText.setTextCursor(tcOld);
     if(!forReplace)
     edSearchingText.setStyleSheet("color: black;");
     if (!editText.find(findText, flags)) {
     if(isFound){               
     tc.setPosition(position);
     editText.setTextCursor(tc);
     findText(findText, flags,forReplace);
     }else { 
     tcOld.clearSelection();
     editText.setTextCursor(tcOld);
     if(!findText.isEmpty()){
     if(forReplace)
     exposeMessageInformation(findText);
     else{
     edSearchingText.setStyleSheet("color: red;");
     }     
     }
     }
     } else {            
     isFindTextInTag(editText.textCursor(), false);            
     //editText.focusWidget();
     }
     parent.rehighlight();
     }
    
     private boolean findAll(String findText ,QTextDocument.FindFlags flags,final QTextCursor tc){
     boolean isFound=false;
     editText.setTextCursor(tc);        
     try{
     editText.blockSignals(true);
     tc.beginEditBlock();
     QTextCharFormat f=new QTextCharFormat();
     f.setBackground(new QBrush(new QColor(255,153,59)));
     while(editText.find(findText,flags)){
     int start=editText.textCursor().selectionStart();
     int length=editText.textCursor().selectionEnd()-start;
   
     tc.setPosition(start);                
     tc.movePosition(QTextCursor.MoveOperation.NextCharacter,QTextCursor.MoveMode.KeepAnchor,length); 
                
     tc.setCharFormat(f);
     if(!isFound)
     isFound=true;
     }
     editText.setTextCursor(tc);
     }finally{
     tc.endEditBlock();
     editText.blockSignals(false);
     }
     return isFound;
     }
     * 
     * @SuppressWarnings("unused")
     private void findText(){       
     String findText=findDialog.getFindWhat();
     searchText(findText,true);
     }*/
}
