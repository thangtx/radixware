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

package org.radixware.kernel.explorer.widgets;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFocusEvent;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QStyle;
import com.trolltech.qt.gui.QStyleOptionTabBarBaseV2;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class QExtTabWidget extends QTabWidget{        
    
    private boolean tabBarIsVisible = true;    
 
    private class TabBar extends QTabBar{
        
        private final static String STYLESHEET = "QTabBar::tab:!selected { font-weight: normal; }";
        private final QMenu tabMenu = new QMenu(this);
        private final QStyleOptionTabBarBaseV2 optTabBase;
        private boolean currentTabMarked;
        private boolean firstFocus = true;
        private int pressedIndex = -1;        

        private final QToolButton menuButton = new QToolButton(this);
        
        public TabBar(){
            super(QExtTabWidget.this);
            optTabBase = new QStyleOptionTabBarBaseV2();
            menuButton.setStyleSheet("QToolButton::menu-indicator{image:none}");
            menuButton.setArrowType(Qt.ArrowType.DownArrow);
            menuButton.clicked.connect(this, "menuButtonClickedSlot()");                
            menuButton.setMenu(tabMenu);
            optTabBase.init(this);
            optTabBase.setDocumentMode(this.documentMode());
        }
        
        @SuppressWarnings("unused")
        private void menuButtonClickedSlot() {
            menuButtonClicked(menuButton);
        }
        
        protected void menuButtonClicked(final QToolButton menuButton) {
            if (menuButton != null) {
                tabMenu.clear();
                for (int i = 0; i < this.count(); i++) {
                    final ExplorerAction action = new ExplorerAction(this.tabIcon(i), this.tabText(i), tabMenu);
                    action.setUserObject(i);
                    action.addActionListener(new Action.ActionListener() {

                        @Override
                        public void triggered(final Action action) {
                            TabBar.this.setCurrentIndex((int)action.getUserObject());
                        }
                    });
                    if (i == TabBar.this.currentIndex()) {
                        final QFont font = action.font();
                        font.setBold(true);
                        action.setFont(font);
                    }
                    tabMenu.addAction(action);
                }
                menuButton.showMenu();
            }
        }
        
        public void setCurrentTabMarked(final boolean isMarked){
            if (currentTabMarked!=isMarked){
                final QFont defaultFont = font();
                if (isMarked){
                    if (!defaultFont.bold()){
                        final QFont font = ExplorerFont.Factory.getFont(font()).getBold().getQFont();
                        setFont(font);
                    }
                    setStyleSheet(STYLESHEET);
                }else{
                    setStyleSheet(null);
                    if (defaultFont.bold()){
                        final QFont font = 
                            ExplorerFont.Factory.getFont(font()).changeWeight(EFontWeight.NORMAL).getQFont();
                        setFont(font);  
                    }
                }
                currentTabMarked = isMarked;
            }            
        }

        public boolean isCurrentTabMarked(){
            return currentTabMarked;
        }

        @Override
        protected void focusInEvent(final QFocusEvent event) {
            if (firstFocus){
                firstFocus = false;
                final QExtTabWidget tabWidget = (QExtTabWidget)parent();
                if (tabWidget.onFirstFocusIn()){
                    event.accept();                    
                }else{
                    super.focusInEvent(event);
                }
            }else{
                super.focusInEvent(event);
            }            
        }
  
        @Override
        protected void tabLayoutChange() {
            super.tabLayoutChange();
            this.moveButton();
        }
        
        private void moveButton() {
            int size = 0;
            for (int  i = 0; i < this.count(); i++) {
                size += this.tabRect(i).width();
            }
            final int h = this.geometry().top();
            final int w = this.width();
            if (size > w) {
                final int menuBtnWidth = style().pixelMetric(QStyle.PixelMetric.PM_TabBarScrollButtonWidth, null, this);
                menuButton.setMaximumWidth(menuBtnWidth);
                menuButton.setMaximumHeight(size().height());
                menuButton.setMinimumHeight(size().height());
                menuButton.setVisible(true);
                menuButton.move(w-menuBtnWidth, h);
                menuButton.setAutoFillBackground(true);
                for (QObject obj : this.children()) {
                    if (obj instanceof QToolButton) {
                        if (((QToolButton)obj).arrowType().equals(Qt.ArrowType.RightArrow)) {
                            ((QToolButton)obj).move(w - menuBtnWidth*2, h);
                            ((QToolButton)obj).setAutoFillBackground(true);
                        } else if (((QToolButton)obj).arrowType().equals(Qt.ArrowType.LeftArrow)) {
                            ((QToolButton)obj).move(w - menuBtnWidth*3, h);
                            ((QToolButton)obj).setAutoFillBackground(true
                            );
                        }
                    }
                }
            } else {
                menuButton.setVisible(false);
            }
        }

        @Override
        protected void mousePressEvent(final QMouseEvent event) {
            if (event.button() != Qt.MouseButton.LeftButton) {
                event.ignore();
                return;
            }
            pressedIndex = indexAtPos(event.pos());
            if (pressedIndex>-1){
                if (style().styleHint(QStyle.StyleHint.SH_TabBar_SelectMouseType, optTabBase, this)==QEvent.Type.MouseButtonPress.value()){
                    switchCurrentTab(pressedIndex);
                    pressedIndex = -1;
                }else{
                    repaint(tabRect(pressedIndex));                    
                }
            }
        }                

        @Override
        protected void mouseReleaseEvent(final QMouseEvent event) {
            if (event.button() != Qt.MouseButton.LeftButton) {
                event.ignore();
                return;
            }
            final int index = indexAtPos(event.pos())==pressedIndex ? pressedIndex : -1;
            pressedIndex = -1;
            if (index>-1 
                && style().styleHint(QStyle.StyleHint.SH_TabBar_SelectMouseType, optTabBase, this)==QEvent.Type.MouseButtonRelease.value()){
                switchCurrentTab(index);
            }
        }

        @Override
        protected void wheelEvent(final QWheelEvent event) {
            setCurrentNextEnabledIndex(event.delta() > 0 ? -1 : 1);
            event.ignore();
        }

        @Override
        protected void keyPressEvent(final QKeyEvent event) {
            if (event.key() != Qt.Key.Key_Left.value() && event.key() != Qt.Key.Key_Right.value()) {
                event.ignore();
                return;
            }
            final int offset = 
                event.key() == (isRightToLeft() ? Qt.Key.Key_Right.value() : Qt.Key.Key_Left.value()) ? -1 : 1;
            setCurrentNextEnabledIndex(offset);
        }
        
        private void switchCurrentTab(final int newIndex){
            QExtTabWidget.this.beforeUserChangeCurrentIndex(newIndex);
            setCurrentIndex(newIndex);
        }

        private void setCurrentNextEnabledIndex(final int offset){            
            for (int index = currentIndex() + offset; isValidIndex(index); index += offset) {
                if (isTabEnabled(index)) {
                    switchCurrentTab(index);
                    break;
                }
            }
        }
        
        private int indexAtPos(final QPoint p){
            for (int i=count()-1; i >= 0; i--){
                if (isTabEnabled(i) && tabRect(i).contains(p)){
                    return i;
                }
            }
            return -1;
        }
        
        private boolean isValidIndex(final int index){
            return index>=0 && index<count();
        }
    }    
    
    public QExtTabWidget(final QWidget parent){
        super(parent);
        setTabBar(new TabBar());
        setCurrentTabMarked(true);
        {            
            //RADIX-8029. По невыясненным причинам иногда при вызове метода QTabWidget::removeTab сигнал
            //QStackedWidget.widgetRemoved отправляется дважды, что приводит к удалению двух вкладок 
            //вместо одной. Следующие действия позволяют это избежать.
            final QStackedWidget stackedWidget = (QStackedWidget)this.findChild(QStackedWidget.class);
            if (stackedWidget!=null && stackedWidget.layout() instanceof QStackedLayout){
                ((QStackedLayout)stackedWidget.layout()).widgetRemoved.disconnect();
                ((QStackedLayout)stackedWidget.layout()).widgetRemoved.connect(this,"stackedLayoutRemoved(int)");
            }
        }
    }
    
    @SuppressWarnings("unused")
    private void stackedLayoutRemoved(final int i){
        final QWidget widget = ((QLayout)QObject.signalSender()).parentWidget();
        if (widget instanceof QStackedWidget){            
            ((QStackedWidget)widget).widgetRemoved.emit(i);
        }        
    }
       
    public void setTabBarVisible(final boolean isVisible){        
        tabBar().setDrawBase(true);
        if (isVisible) {
            setDocumentMode(false);
            tabBar().setVisible(true);
            setFocusProxy(tabBar());
        } else {
            setDocumentMode(true);
            tabBar().setVisible(false);
            setFocusProxy(null);
        }
        tabBarIsVisible = isVisible;
    }
    
    public boolean isTabBarVisible(){
        return tabBarIsVisible;
    }
    
    public void setTabTextColor(final int index, final QColor color){
        tabBar().setTabTextColor(index, color);
    }
    
    public QColor getTabTextColor(final int index){
        return tabBar().tabTextColor(index);
    }
    
    public final void setCurrentTabMarked(final boolean isMarked){
        ((TabBar)tabBar()).setCurrentTabMarked(isMarked);
    }
    
    public final boolean isCurrentTabMarked(){
        return ((TabBar)tabBar()).isCurrentTabMarked();
    }
        
    @Override
    public QSize sizeHint() {
        final QSize size = super.sizeHint();
        final QSize tabBarSize = tabBar().sizeHint();
        if (!isTabBarVisible()) {
            size.setHeight(size.height() - tabBarSize.height());
        }else if (tabBarSize.width()>size.width()){
            final Dimension sizeLimit = WidgetUtils.getWndowMaxSize();
            size.setWidth(Math.min(tabBarSize.width(), (int)sizeLimit.getWidth()));
        }
        return size;
    }

    @Override
    public QSize minimumSizeHint() {
        final QSize size = super.minimumSizeHint();
        if (!isTabBarVisible()) {
            size.setHeight(size.height() - tabBar().sizeHint().height());
        }
        return size;
    }
    
    protected boolean onFirstFocusIn(){
        return false;
    }

    @Override
    protected void keyPressEvent(final QKeyEvent event) {
        final int key = event.key();
        final int pageCount = count();        
        if ((key==Qt.Key.Key_Tab.value() || key==Qt.Key.Key_Backtab.value())            
            && event.modifiers().isSet(Qt.KeyboardModifier.ControlModifier)
            && pageCount>1
           ){
            int page = currentIndex();
            final int dx = 
                event.key() == Qt.Key.Key_Backtab.value() || event.modifiers().isSet(Qt.KeyboardModifier.ShiftModifier) ? -1 : 1;
            for (int pass = 0; pass < pageCount; ++pass) {
                page+=dx;
                if (page < 0){
                    page = count() - 1;
                } else if (page >= pageCount) {
                    page = 0;
                }
                if (tabBar().isTabEnabled(page)) {
                    beforeUserChangeCurrentIndex(page);
                    setCurrentIndex(page);
                    return;
                }
            }
        }
        event.ignore();//prohibit event to parent widget
    }
    
    protected void beforeUserChangeCurrentIndex(final int newIndex){
        
    }
    
}