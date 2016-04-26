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
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.explorer.views.IExplorerView;


public class ExplorerMenu extends QMenu implements IMenu {
    
    private final List<Action> actions = new LinkedList<>();

    public ExplorerMenu(QPrivateConstructor p) {
        super(p);
    }

    public ExplorerMenu(String title, QWidget parent) {
        super(title, parent);
    }

    public ExplorerMenu(String title) {
        super(title);
    }

    public ExplorerMenu(QWidget parent) {
        super(parent);
    }

    public ExplorerMenu() {
    }

    @Override
    public void insertAction(Action before, Action action) {
        if (action!=null){
            actions.add(action);
        }
        insertAction((QAction) before, (QAction) action);
    }

    @Override
    public void insertSeparator(Action before) {
        if (before == null) {
            super.addSeparator();
        } else {
            insertSeparator((QAction) before);
        }
    }

    @Override
    public void addAction(Action action) {
        if (action!=null){
            actions.add(action);
        }        
        addAction((QAction) action);
    }        

    @Override
    public void removeAction(Action action) {
        if (action!=null){
            while(actions.remove(action)){};
        }
        removeAction((QAction) action);
    }
    
    @Override
    public void removeAllActions() {
        for(int i=actions.size()-1;i>=0;i--){            
            removeAction((QAction)actions.get(i));
            actions.remove(i);
        }
    }

    @Override
    public String getObjectName() {
        return objectName();
    }

    @Override
    public void disconnect(IWidget w) {
        if (w instanceof IExplorerView) {
            disconnect(((IExplorerView) w).asQWidget());
        } else if (w instanceof QObject) {
            disconnect((QObject) w);
        }
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }        
}
