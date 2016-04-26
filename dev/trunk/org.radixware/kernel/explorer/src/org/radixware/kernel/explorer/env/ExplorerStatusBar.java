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

package org.radixware.kernel.explorer.env;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QMimeData;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QKeySequence;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPlainTextEdit;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QStatusBar;
import com.trolltech.qt.gui.QTextCharFormat;
import com.trolltech.qt.gui.QTextCursor;
import com.trolltech.qt.gui.QTextOption;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;
import org.radixware.kernel.common.client.widgets.IMainStatusBar;

final class ExplorerStatusBar extends QStatusBar implements IMainStatusBar{
    
    private final static class LabelTextChangedEvent extends QEvent{
        public LabelTextChangedEvent(){
            super(QEvent.Type.User);
        }
    }
    
    private final static class PathView extends QPlainTextEdit{
        
        public final Signal1<String> clickOnPathItem = new Signal1<>();
                
        private final static class PathItemInfo{
            
            private final int startPosition;
            private final int length;
            
            public PathItemInfo(final int pos, final int length){
                this.startPosition = pos;
                this.length = length;
            }
        
            public int getStartPosition(){
                return startPosition;
            }
            
            public int getLength(){
                return length;
            }
            
            public int getEndPosition(){
                return startPosition+length;
            }
            
            public void setCharacterFormat(final QTextCursor cursor, final QTextCharFormat format){
                final int savePosition = cursor.position();
                cursor.clearSelection();
                //select path item text
                cursor.setPosition(startPosition, QTextCursor.MoveMode.MoveAnchor);
                cursor.setPosition(getEndPosition(), QTextCursor.MoveMode.KeepAnchor);
                //change character format for selected text
                cursor.setCharFormat(format);
                cursor.clearSelection();
                cursor.setPosition(savePosition, QTextCursor.MoveMode.MoveAnchor);                
            }
            
        }                
        
        private final QTextCharFormat plainTextCharFormat = new QTextCharFormat();
        private final QLabel dummyLabel = new QLabel(this);
        private final Map<String,PathItemInfo> pathItems = new HashMap<>();
        private final QBrush highlightedBrush = new QBrush(QColor.blue);        
        private final QCursor arrowCursor = new QCursor(Qt.CursorShape.ArrowCursor);
        private boolean eventFilterInstalled;
        private final QEventFilter keyboardListener = new QEventFilter(this){
            @Override
            public boolean eventFilter(final QObject target, final QEvent event) {
                if (event instanceof QKeyEvent){
                    final QKeyEvent keyEvent = (QKeyEvent)event;
                    if (keyEvent.matches(QKeySequence.StandardKey.SelectAll)){
                        PathView.this.selectAll();
                        return true;
                    }else if (keyEvent.matches(QKeySequence.StandardKey.Copy)){
                        PathView.this.copy();
                        return true;
                    }else if (keyEvent.key()==Qt.Key.Key_Escape.value()
                              && QApplication.mouseButtons().isSet(Qt.MouseButton.LeftButton)){
                        PathView.this.clearCurrentPathFormat();
                        return true;
                    }
                }
                return false;
            }
        };
        
        private String currentPathItemId;
        private boolean mousePressedAtCurrentPathItem;
        
        public PathView(){
            dummyLabel.setVisible(false);
            final QPalette palette = new QPalette(palette());
            palette.setColor(QPalette.ColorRole.Base, palette.color(QPalette.ColorRole.Window));        
            palette.setColor(QPalette.ColorRole.Highlight, dummyLabel.palette().color(QPalette.ColorRole.Highlight));
            palette.setColor(QPalette.ColorRole.HighlightedText, dummyLabel.palette().color(QPalette.ColorRole.HighlightedText));
            setPalette(palette);            
            setVerticalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            setHorizontalScrollBarPolicy(Qt.ScrollBarPolicy.ScrollBarAlwaysOff);
            setFocusPolicy(Qt.FocusPolicy.NoFocus);
            setWordWrapMode(QTextOption.WrapMode.NoWrap);
            setTextInteractionFlags(Qt.TextInteractionFlag.TextEditorInteraction, Qt.TextInteractionFlag.TextSelectableByMouse);
            setUndoRedoEnabled(false);            
            setMouseTracking(true);
            viewport().setCursor(arrowCursor);
            setReadOnly(true);
            verticalScrollBar().setMaximum(0);
            
            final QFont font = new QFont(this.font());
            font.setPointSize(10);
            font.setBold(true);
            plainTextCharFormat.setFont(font);
            keyboardListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress,QEvent.Type.KeyRelease));            
        }
        
        public void setText(final String text){
            clearAll();
            textCursor().insertText(text, plainTextCharFormat);
            verticalScrollBar().setMaximum(0);
        }
        
        public void clearAll(){
            clearCurrentPathFormat();
            clear();
            pathItems.clear();
            removeEventFilter();
        }
                        
        public String getText(){
            return toPlainText();
        }
        
        public void addCharFormatTerminator(){
            final QTextCursor cursor = textCursor();
            try{
                cursor.movePosition(QTextCursor.MoveOperation.End);                
                cursor.insertText("\u200B", plainTextCharFormat);
                verticalScrollBar().setMaximum(0);
            }finally{
                cursor.dispose();
            }
        }
        
        public void addPathItem(final String itemTitle, final String itemId){
            final QTextCursor cursor = textCursor();
            try{
                cursor.movePosition(QTextCursor.MoveOperation.End);
                if (!pathItems.isEmpty()){
                    cursor.insertText("/", plainTextCharFormat);
                }
                final PathItemInfo pathItem = new PathItemInfo(cursor.position(), itemTitle.length());
                pathItems.put(itemId, pathItem);
                cursor.insertText(itemTitle, createCharFormatForPathItem(itemId,false));
                verticalScrollBar().setMaximum(0);
                final QPoint currentCursorPoint = viewport().mapFromGlobal(QCursor.pos());
                if (viewport().geometry().contains(currentCursorPoint)){
                    installEventFilter();
                }else{
                    clearCurrentPathFormat();
                    removeEventFilter();
                }
            }finally{
                cursor.dispose();
            }
        }
        
        private QTextCharFormat createCharFormatForPathItem(final String itemId, final boolean highlighted){
            final QTextCharFormat format = plainTextCharFormat.clone();
            format.setAnchorHref(itemId);            
            if (highlighted){
                format.setForeground(highlightedBrush);
                format.setUnderlineStyle(QTextCharFormat.UnderlineStyle.SingleUnderline);
                format.setAnchor(true);
            }
            return format;
        }

        @Override
        protected QMimeData createMimeDataFromSelection() {
            final QMimeData data = super.createMimeDataFromSelection();
            if (data!=null && data.hasText() && !pathItems.isEmpty()){
                data.setText(data.text().replace("\u200B", ""));
            }
            return data;
        }

        @Override
        protected void mouseMoveEvent(final QMouseEvent event) {
            if (!pathItems.isEmpty()){
                updateCurrentPathItem(event.pos());
            }
            super.mouseMoveEvent(event);
        }
        
        private void updateCurrentPathItem(final QPoint currentPoint){
            final QTextCursor cursor = this.cursorForPosition(currentPoint);
            if (cursor==null || !textCursor().selectedText().isEmpty()){
                clearCurrentPathFormat();
            }else{
                try{
                    final QTextCharFormat format = cursor.charFormat();
                    final String anchor = format.anchorHref();
                    if (anchor!=null && !anchor.isEmpty()){
                        setCurrentPathFormat(cursor, anchor);
                    }else{
                        clearCurrentPathFormat(cursor);
                    }
                }finally{
                    cursor.dispose();
                }
            }
        }
               
        @Override
        protected void mouseReleaseEvent(final QMouseEvent event) {            
            super.mouseReleaseEvent(event);            
            if (event.button()==Qt.MouseButton.LeftButton
                && mousePressedAtCurrentPathItem
                && currentPathItemId!=null 
                && currentPathItemId.equals(getAnchorForPosition(event.pos()))
                && textCursor().selectedText().isEmpty()){
                mousePressedAtCurrentPathItem = false;
                clickOnPathItem.emit(currentPathItemId);
                final QPoint currentCursorPoint = viewport().mapFromGlobal(QCursor.pos());
                updateCurrentPathItem(currentCursorPoint);
            }
        }

        @Override
        protected void mousePressEvent(final QMouseEvent event) {
            if (event.buttons().value()==Qt.MouseButton.LeftButton.value()){
                final int keepScrollPos = horizontalScrollBar().value();
                final QTextCursor cursor = textCursor();
                cursor.clearSelection();
                setTextCursor(cursor);
                horizontalScrollBar().setValue(keepScrollPos);
                super.mousePressEvent(event);
                mousePressedAtCurrentPathItem = 
                    currentPathItemId!=null && currentPathItemId.equals(getAnchorForPosition(event.pos()));
            }else if (event.buttons().value()==Qt.MouseButton.RightButton.value()){
                super.mousePressEvent(event);
                final QMenu menu = createStandardContextMenu();
                if (menu!=null){
                    menu.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
                    menu.popup(event.globalPos());
                }
            }else{
                super.mousePressEvent(event);
            }
        }

        private String getAnchorForPosition(final QPoint pos){
            final QTextCursor cursor = this.cursorForPosition(pos);
            if (cursor==null){
                return null;
            }else{
                try{
                    final QTextCharFormat format = cursor.charFormat();
                    return format.anchorHref();
                }finally{
                    cursor.dispose();
                }
            }
        }
        
        private void setCurrentPathFormat(final QTextCursor cursor, final String pathItemId){
            if (currentPathItemId!=null && !currentPathItemId.equals(pathItemId)){
                clearCurrentPathFormat(cursor);
            }
            final PathItemInfo pathItem = pathItems.get(pathItemId);
            if (pathItem!=null){
                pathItem.setCharacterFormat(cursor, createCharFormatForPathItem(pathItemId, true));
            }
            currentPathItemId = pathItemId;
        }
        
        private void clearCurrentPathFormat(){
            if (currentPathItemId!=null){
                final QTextCursor cursor = textCursor();
                try{
                    clearCurrentPathFormat(cursor);
                }finally{
                    cursor.dispose();
                }
            }
        }
        
        private void clearCurrentPathFormat(final QTextCursor cursor){
            if (currentPathItemId!=null){
                final PathItemInfo pathItem = pathItems.get(currentPathItemId);
                pathItem.setCharacterFormat(cursor, createCharFormatForPathItem(currentPathItemId, false));
                currentPathItemId = null;
            }
        }        
        
        private void installEventFilter(){
            if (!eventFilterInstalled){
                QApplication.instance().installEventFilter(keyboardListener);
                eventFilterInstalled = true;
            }
        }
        
        private void removeEventFilter(){
            if (eventFilterInstalled){
                QApplication.instance().removeEventFilter(keyboardListener);
                eventFilterInstalled = false;
            }
        }

        @Override
        protected void enterEvent(QEvent event) {
            super.enterEvent(event);
            if (!pathItems.isEmpty()){
                installEventFilter();
            }
        }
                
        @Override
        protected void leaveEvent(final QEvent event) {
            clearCurrentPathFormat();
            super.leaveEvent(event); 
            removeEventFilter();
        }                
    }

    private final PathView pathView = new PathView();
    private IExplorerTreeNode currentNode;

    @SuppressWarnings("LeakingThisInConstructor")
    public ExplorerStatusBar() {
        super();
        setObjectName("statusbar");
        pathView.setSizePolicy(QSizePolicy.Policy.Ignored, QSizePolicy.Policy.Ignored);
        pathView.setFrameShape(QFrame.Shape.NoFrame);
        pathView.setObjectName("statusBarTextView");        
        addWidget(pathView,1);
        setSizePolicy(com.trolltech.qt.gui.QSizePolicy.Policy.Ignored, com.trolltech.qt.gui.QSizePolicy.Policy.Preferred);
        setFocusPolicy(Qt.FocusPolicy.NoFocus);
        setSizeGripEnabled(false);
        messageChanged.connect(this,"onMessageChanged(String)");
        pathView.clickOnPathItem.connect(this,"onPathItemClick(String)");
    }        
    
    @Override
    public void setText(final String newText){
        pathView.setText(newText);
        currentNode = null;
        QApplication.postEvent(this, new LabelTextChangedEvent());
        pathView.textCursor().setPosition(0, QTextCursor.MoveMode.MoveAnchor);
    }

    @Override
    public String getText() {
        return pathView.getText();
    }

    @Override
    public void setCurrentExplorerTreeNode(final IExplorerTreeNode node) {
        if (node!=null){
            pathView.clearAll();
            final Stack<IExplorerTreeNode> path = new Stack<>();
            for (IExplorerTreeNode n=node; n!=null; n=n.getParentNode()){
                path.push(n);
            }
            pathView.addCharFormatTerminator();
            while (!path.isEmpty()){
                final IExplorerTreeNode pathItem = path.pop();
                final String title = pathItem.isValid() ? pathItem.getView().getTitle() : "????";
                pathView.addPathItem(title, String.valueOf(pathItem.getIndexInExplorerTree()));
            }
            pathView.addCharFormatTerminator();
            QApplication.postEvent(this, new LabelTextChangedEvent());
            pathView.textCursor().setPosition(0, QTextCursor.MoveMode.MoveAnchor);
        }
        currentNode = node;
    }

    @Override
    public IExplorerTreeNode getCurrentExplorerTreeNode() {
        return currentNode;
    }
    
    @SuppressWarnings("unused")
    private void onPathItemClick(final String pathItemId){
        final IExplorerTreeNode node = findNodeById(pathItemId);
        if (node!=null && node.isValid()){
            if (node==currentNode){//NOPMD
                node.getExplorerTree().getActions().getGoToCurrentNodeAction().trigger();
            }else{
                node.getExplorerTree().setCurrent(node);
            }
        }
    }
    
    private IExplorerTreeNode findNodeById(final String id){
        for (IExplorerTreeNode node=currentNode; node!=null; node=node.getParentNode()){
            if (String.valueOf(node.getIndexInExplorerTree()).equals(id)){
                return node;
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    private void onMessageChanged(final String newMessage){
        if (newMessage!=null && !newMessage.isEmpty()){
            pathView.setText(newMessage);
            blockSignals(true);
            try{
                showMessage("");
            }
            finally{
                blockSignals(false);
            }
        }
    }

    @Override
    protected void customEvent(final QEvent qevent) {
        if (qevent instanceof LabelTextChangedEvent){
            qevent.accept();
            if (pathView.nativeId()!=0){
                pathView.textCursor().setPosition(0, QTextCursor.MoveMode.MoveAnchor);
            }
            return;
        }
        super.customEvent(qevent);
    }    
}
