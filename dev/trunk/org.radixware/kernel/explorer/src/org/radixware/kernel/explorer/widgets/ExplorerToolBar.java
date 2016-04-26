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
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QToolBar;
import com.trolltech.qt.gui.QToolButton;
import com.trolltech.qt.gui.QWidget;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.widgets.IToolButton;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IToolBar;
import org.radixware.kernel.explorer.env.ExplorerWidgetFactory;
import org.radixware.kernel.explorer.views.IExplorerView;


public class ExplorerToolBar extends QToolBar implements IToolBar {
    
    private final List<Action> actions = new LinkedList<>();

    public ExplorerToolBar(QPrivateConstructor p) {
        super(p);
    }

    public ExplorerToolBar(String title, QWidget parent) {
        super(title, parent);
    }

    public ExplorerToolBar(String title) {
        super(title);
    }

    public ExplorerToolBar(QWidget parent) {
        super(parent);
    }

    public ExplorerToolBar() {
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
        insertSeparator((QAction) before);
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
            removeAction(actions.get(i));
        }
    }    

    @Override
    public void setIconSize(int w, int h) {
        setIconSize(new QSize(w, h));
    }

    @Override
    public int getIconHeight() {
        return iconSize().height();
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
    public IToolButton getWidgetForAction(Action action) {
        if (action instanceof QAction) {
            QToolButton qtButton = (QToolButton) widgetForAction((QAction) action);
            if (qtButton instanceof IToolButton) {
                return (IToolButton) qtButton;
            } else {
                return ExplorerWidgetFactory.wrapToolButton(qtButton);
            }
        } else {
            return null;
        }
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }        
}
