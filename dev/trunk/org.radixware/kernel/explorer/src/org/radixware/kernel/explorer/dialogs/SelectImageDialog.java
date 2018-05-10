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
package org.radixware.kernel.explorer.dialogs;

import com.trolltech.qt.core.QAbstractListModel;
import com.trolltech.qt.core.QModelIndex;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QListView;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QPushButton;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.ReleaseRepository.DefinitionInfo;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IPushButton;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.widgets.ExplorerAction;

public class SelectImageDialog extends ExplorerDialog {

    private QListView imgView;
    private Id choosenIconId;

    private class LazyQListModel extends QAbstractListModel {

        private final List<DefinitionInfo> definitionInfoList;
        private final List<QIcon> iconList = new LinkedList<>();

        public LazyQListModel(QObject parent, int initSize) {
            super(parent);
            this.definitionInfoList = new LinkedList<>(getEnvironment().getDefManager().getRepository().getDefinitions(EDefType.IMAGE));
            for (int i = 0; i < initSize; i++) {
                if (i > definitionInfoList.size()) {
                    break;
                } else {
                    Id imgId = definitionInfoList.get(i).id;
                    QIcon icon = ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(imgId));
                    iconList.add(icon);
                }
            }
        }

        @Override
        public void fetchMore(QModelIndex qmi) {
            beginInsertRows(null, iconList.size(), iconList.size() + 50);
            int size = iconList.size();
            for (int i = size; i < size + 50; i++) {
                Id imgId = definitionInfoList.get(iconList.size()).id;
                QIcon icon = ExplorerIcon.getQIcon(getEnvironment().getDefManager().getImage(imgId));
                iconList.add(icon);
            }
            endInsertRows();
        }

        @Override
        public boolean canFetchMore(QModelIndex qmi) {
            if (definitionInfoList.size() > iconList.size()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public Object data(QModelIndex index, int role) {
            if (index == null) {
                return null;
            }
            final int row = index.row();
            if (row < 0) {
                return null;
            }
            switch (role) {
                case Qt.ItemDataRole.DecorationRole:
                    return iconList.get(row);
                case Qt.ItemDataRole.UserRole:
                    return definitionInfoList.get(row).id;
                default:
                    return null;
            }
        }

        @Override
        public int rowCount(QModelIndex qmi) {
            return iconList.size();
        }
    }

    public SelectImageDialog(IClientEnvironment environment, QWidget parent) {
        super(environment, parent);
        this.setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose);
        setupUI();
    }

    private void setupUI() {
        setWindowTitle(getEnvironment().getMessageProvider().translate("EditMask", "Select Image"));
        imgView = new QListView();
        QPalette palette = new QPalette();
        LazyQListModel lazyListModel = new LazyQListModel(this, 140);
        imgView.setIconSize(new QSize(30, 30));
        imgView.setViewMode(QListView.ViewMode.IconMode);
        imgView.setModel(lazyListModel);
        imgView.doubleClicked.connect(this, "iconDblClicked(QModelIndex)");
        imgView.clicked.connect(this, "iconClicked(QModelIndex)");
        this.layout().addWidget(imgView);
        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        getButton(EDialogButtonType.OK).setEnabled(false);
        final QPushButton resetButton = (QPushButton)addButton(EDialogButtonType.RESET);
        resetButton.setText("Clear");
        resetButton.setIcon(ExplorerIcon.getQIcon(ClientIcon.CommonOperations.CLEAR));
        resetButton.clicked.connect(this, "onResetClicked()");
        
//        resetButton.addClickHandler(new IButton.ClickHandler() {
//
//            @Override
//            public void onClick(IButton source) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        });
//        ExplorerAction resetAction = new ExplorerAction(getEnvironment().getApplication().getImageManager().getIcon(ClientIcon.CommonOperations.CLEAR), "Clear", this);
//        resetAction.addActionListener(new Action.ActionListener() {
//
//            @Override
//            public void triggered(Action action) {
//                SelectImageDialog.this.choosenIconId = null;
//                SelectImageDialog.this.accept();
//            }
//        });
//        getButton(EDialogButtonType.RESET).addAction(resetAction);
        palette.setColor(QPalette.ColorRole.Highlight, QColor.red); ///Test
        imgView.setPalette(palette);
        imgView.setStyleSheet("QListView::item:hover{color:red}");
    }   

    @SuppressWarnings("Unused")
    private void onResetClicked() {
        choosenIconId = null;
        accept();
    }
    
    @SuppressWarnings("Unused")
    private void iconDblClicked(QModelIndex modelIndex) {
        choosenIconId = (Id) modelIndex.data(Qt.ItemDataRole.UserRole);
        this.acceptDialog();
    }

    @SuppressWarnings("Unused")
    private void iconClicked(QModelIndex modelIndex) {
        choosenIconId = (Id) modelIndex.data(Qt.ItemDataRole.UserRole);
        if (!getButton(EDialogButtonType.OK).isEnabled()) {
            getButton(EDialogButtonType.OK).setEnabled(true);
        }
    }

    public Id getIconId() {
        return choosenIconId;
    }

}
