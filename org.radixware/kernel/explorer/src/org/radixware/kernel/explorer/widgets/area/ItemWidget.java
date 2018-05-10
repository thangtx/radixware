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
package org.radixware.kernel.explorer.widgets.area;

import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QFrame;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.widgets.IWidget;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.client.widgets.area.IWidgetAreaItemPresenter;
import org.radixware.kernel.explorer.widgets.ExplorerAction;
import org.radixware.kernel.explorer.widgets.ExplorerFrame;
import org.radixware.kernel.explorer.widgets.ExplorerToolBar;

class ItemWidget extends ExplorerFrame implements IWidgetAreaItemPresenter {

    private final QVBoxLayout mainLayout = new QVBoxLayout(this);
    private final QHBoxLayout titleLayout = new QHBoxLayout();
    private final ExplorerToolBar toolBar = new ExplorerToolBar();
    private final QWidget contentLayoutWidget = new QWidget(this);
    private final TabWidgetContent ownerTab;
    private final ExplorerAction removeAction;
    private final ExplorerAction resizeAction;
    private final QLabel titleLabel;
    
    private final static class AdminPanelIcons extends ClientIcon {

        private AdminPanelIcons(final String fileName) {
            super(fileName, true);
        }
        public static final ClientIcon MOVE_ITEM = new AdminPanelIcons("classpath:images/move.svg");
        public static final ClientIcon ADD_TAB = new AdminPanelIcons("classpath:images/addEmpty.svg");
    }
    
    ItemWidget(final TabWidgetContent ownerTab,
            final IClientEnvironment environment, String title) {
        super(environment, ownerTab);
        MessageProvider mp = environment.getMessageProvider();
        removeAction = new ExplorerAction(environment.getApplication().getImageManager().getIcon(ClientIcon.Dialog.BUTTON_CANCEL),
                mp.translate("WidgetsArea", "Remove"), toolBar);
        resizeAction = new ExplorerAction(environment.getApplication().getImageManager().getIcon(AdminPanelIcons.MOVE_ITEM),
                mp.translate("WidgetsArea", "Resize") , toolBar);
        titleLabel = new QLabel(title);
        QFont font = titleLabel.font().clone();
        font.setPixelSize(11);
        font.setBold(true);
        titleLabel.setFont(font);
        resizeAction.addActionListener(new Action.ActionListener() {

            @Override
            public void triggered(Action action) {
                ownerTab.onItemDoubleClickEvent(ItemWidget.this);
            }
        });
        this.ownerTab = ownerTab;
        setFrameShadow(Shadow.Plain);
        setFrameShape(Shape.Box);
        QFrame mainFrame = new QFrame();
        mainFrame.setMaximumHeight(24);
        mainFrame.setStyleSheet("border: 1px; border-bottom-style:solid;");
        mainFrame.setLayout(titleLayout);
        mainFrame.setAutoFillBackground(true);
        mainLayout.addWidget(mainFrame);
        
        titleLabel.setStyleSheet("border: 0px; margin-left: 4px;");
        titleLayout.addWidget(titleLabel);
        titleLayout.addWidget(toolBar);
        toolBar.setIconSize(new QSize(16, 16));
        toolBar.setStyleSheet("border: 0px");
        toolBar.setMinimumHeight(22);
        toolBar.setMaximumHeight(22);
        QWidget spacer = new QWidget();
        spacer.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        toolBar.addWidget(spacer);
        layout().setContentsMargins(0, 0, 0, 0);
        mainFrame.layout().setContentsMargins(0, 0, 0, 0);
    }

    QRect getHeaderCoordinates() {
        return titleLayout.geometry();
    }

    @Override
    public void setBounds(int gridTop, int gridLeft, int gridWidth, int gridHeight) {
        ownerTab.setBounds(this, gridTop, gridLeft, gridWidth, gridHeight);
    }

    @Override
    public void setContent(IWidget widget) {
        contentLayoutWidget.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Expanding);
        mainLayout.addWidget(contentLayoutWidget);
        QVBoxLayout contentLayout = new QVBoxLayout();
        contentLayoutWidget.setLayout(contentLayout);
        contentLayout.addWidget((QWidget) widget);
    }

    @Override
    public void addAction(Action action) {
        toolBar.addAction(action);
    }

    @Override
    public void removeAction(Action action) {
        toolBar.removeAction(action);
    }

    @Override
    public void addAction(int pos, Action action) {
        Action[] actions = toolBar.getActions();
        if (actions.length != 0 && pos < actions.length) {
            toolBar.insertAction(actions[pos], action);
        } else {
            addAction(action);
        }
    }

    @Override
    public Action getRemoveAction() {
        return removeAction;
    }

    @Override
    public Action getResizeAction() {
        return resizeAction;
    }

    @Override
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
