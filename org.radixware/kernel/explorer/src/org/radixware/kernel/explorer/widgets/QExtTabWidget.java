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

import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QLayout;
import com.trolltech.qt.gui.QStackedLayout;
import com.trolltech.qt.gui.QStackedWidget;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QWidget;
import java.awt.Dimension;
import org.radixware.kernel.common.client.enums.EFontWeight;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.utils.WidgetUtils;


public class QExtTabWidget extends QTabWidget{        
    
    private boolean tabBarIsVisible = true;    
    
    private static class TabBar extends QTabBar{
        
        private final static String STYLESHEET = "QTabBar::tab:!selected { font-weight: normal; }";
        
        private boolean currentTabMarked;        
        
        public TabBar(final QWidget parent){
            super(parent);
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
    }    
    
    public QExtTabWidget(final QWidget parent){
        super(parent);   
        setTabBar(new TabBar(this));
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
}