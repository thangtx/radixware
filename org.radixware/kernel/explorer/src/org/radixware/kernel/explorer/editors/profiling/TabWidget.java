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

package org.radixware.kernel.explorer.editors.profiling;

import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt.MouseButton;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QTabBar;
import com.trolltech.qt.gui.QTabWidget;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;


public class TabWidget extends QTabWidget {

    private final List<MyBtn> btnList = new ArrayList<>();
    private final boolean isFirstTabClosable;
    //private ProfilerWidget parent;
    public Signal1<Integer> onCloseTab=new Signal1<>();
    public Signal0 onLastTabRemain=new Signal0();
    private final IClientEnvironment environment;

    public TabWidget(final QWidget parent,final IClientEnvironment environment) {
        super(parent);
        this.isFirstTabClosable = false;
        this.environment=environment;
        setVisible(false);
        final QTabBar t = new QTabBar();
        this.setTabBar(t);
    }
    
    public TabWidget(final QWidget parent,final QTabBar tabBar,final IClientEnvironment environment,final boolean isFirstTabClosable) {
        super(parent);
        this.isFirstTabClosable = isFirstTabClosable;
        this.environment=environment;
        setVisible(false);
        //MyTalBar t = new MyTalBar();
        this.setTabBar(tabBar);
    }

    @Override
    protected void tabInserted(final int index) {
        super.tabInserted(index);
        if (isFirstTabClosable || index > 0) {
            final MyBtn btn = new MyBtn(index);
            //btn.clicked.connect(this, ConnectionType.AutoConnection);
            btn.setAutoRaise(true);
            btn.setCheckable(false);
            btn.setIcon(ExplorerIcon.getQIcon(ClientIcon.Dialog.BUTTON_CANCEL));
            btn.setIconSize(new QSize(12, 12));
            btn.setToolTip(Application.translate("SystemMonitoring","Close Tab"));
            btnList.add(isFirstTabClosable ? index : index-1 ,btn);
            for (int i = index+1 ; i < btnList.size(); i++) {
                btnList.get(i).setIndex(isFirstTabClosable ? i : i + 1);
            }
            this.tabBar().setTabButton(index, QTabBar.ButtonPosition.RightSide, btn);
        }
    }

    @Override
    protected void tabRemoved(int tabIndex) {
        super.tabRemoved(tabIndex);
        if ((this.count() > 0) && ((tabIndex > 0)||(isFirstTabClosable))) {
            if(!isFirstTabClosable){
               tabIndex=tabIndex-1; 
            }
            btnList.remove(tabIndex);
            onCloseTab.emit(tabIndex);
            //parent.removeSummaryTree(tabIndex - 1);
            for (int i = tabIndex ; i < btnList.size(); i++) {
                btnList.get(i).setIndex(isFirstTabClosable?i:i + 1);
            }
            if (this.count() == 1) {
                onLastTabRemain.emit();
                //parent.hideTabs();
            }
        }
    }

    public void closeTab(final int tabIndex) {
        if (isFirstTabClosable || tabIndex > 0) {
            //tabRemoved(tabIndex) ;
            this.removeTab(tabIndex);
        }
    }
    
    class MyBtn extends QToolButton {
        private int index;

        MyBtn(final int index) {
            super();
            this.index = index;
        }

        @Override
        public boolean event(final QEvent qevent) {
            if (qevent.type().equals(QEvent.Type.MouseButtonRelease)) {
                final QMouseEvent mouseEvent = (QMouseEvent) qevent;
                if (mouseEvent.button() == MouseButton.LeftButton) {
                    final String title=Application.translate("SystemMonitoring","Confirm To Close Tab");
                    final String msg=Application.translate("SystemMonitoring","Do you really want to close tab?");
                    if(environment.messageConfirmation(title, msg)){
                        TabWidget.this.closeTab(index);
                    }
                }
            }
            return super.event(qevent);
        }

        public void setIndex(final int index) {
            this.index = index;
        }
    }   
}
