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
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.gui.QAction;
import com.trolltech.qt.gui.QHelpEvent;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QMenu;
import com.trolltech.qt.gui.QToolTip;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.actions.IMenu;
import org.radixware.kernel.explorer.types.RdxIcon;
import org.radixware.kernel.explorer.views.IExplorerView;


public class ExplorerMenu extends QMenu implements IMenu {
    
    private final List<Action> actions = new LinkedList<>();
    private final QEventFilter toolTipEventListener = new QEventFilter(this){
        @Override
        public boolean eventFilter(QObject target, QEvent event) {
            if (event instanceof QHelpEvent){
                onHelpEvent((QHelpEvent)event);
            }
            return false;
        }                
    };

    public ExplorerMenu(final QPrivateConstructor p) {
        super(p);
        toolTipEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.ToolTip));
    }

    public ExplorerMenu(String title, QWidget parent) {
        super(title, parent);
        toolTipEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.ToolTip));
    }

    public ExplorerMenu(String title) {
        super(title);
        toolTipEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.ToolTip));
    }

    public ExplorerMenu(QWidget parent) {
        super(parent);
        toolTipEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.ToolTip));
    }

    public ExplorerMenu() {
        toolTipEventListener.setProcessableEventTypes(EnumSet.of(QEvent.Type.ToolTip));
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

    @Override
    public IMenu addSubMenu(String title) {
        ExplorerMenu qMenu = new ExplorerMenu(title);
        this.addMenu(qMenu);
        return qMenu;
    }

    @Override
    public IMenu addSubMenu(Icon icon, String title) {
        ExplorerMenu qMenu = new ExplorerMenu(title, this);
        qMenu.setIcon(icon);
        this.addMenu(qMenu);
        return qMenu;
    }

    @Override
    public void addSubSeparator() {
        this.addSeparator();
    }

    @Override
    public void insertMenu(Action before, IMenu menu) {
        insertMenu((ExplorerAction) before, (QMenu) menu);
    }

    @Override
    public void insertMenu(IMenu before, IMenu menu) {
        insertMenu(((ExplorerMenu) before).menuAction(), (ExplorerMenu) menu);
    }

    @Override
    public void insertSeparator(IMenu before) {
        insertSeparator(((ExplorerMenu) before).menuAction());
    }

    @Override
    public String getTitle() {
        return title();
    }

    @Override
    public void setIcon(Icon icon) {
        if (icon instanceof QIcon) {
            setWindowIcon((QIcon) icon);
        }
    }

    @Override
    public Icon getIcon() {
        QIcon icon = super.windowIcon();
        if (icon instanceof Icon) {
            return (Icon) icon;
        } else {
            return new RdxIcon(icon);
        }
    }
    
    private void onHelpEvent(final QHelpEvent event){
        final QAction action = activeAction();
        final String toolTip = action==null ? null : action.toolTip();
        if (toolTip!=null && !toolTip.isEmpty()){
            QToolTip.showText(event.globalPos(), toolTip);
        }        
    }    
}
