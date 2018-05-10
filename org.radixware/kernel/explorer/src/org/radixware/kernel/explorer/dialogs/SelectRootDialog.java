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

import java.util.List;

import org.radixware.kernel.explorer.env.Application;
import com.trolltech.qt.core.QEvent;
import com.trolltech.qt.core.QEventFilter;
import com.trolltech.qt.core.QObject;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.QSize;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.core.Qt.Key;
import com.trolltech.qt.core.Qt.ScrollBarPolicy;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QIcon;
import com.trolltech.qt.gui.QItemDelegate;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.common.client.meta.explorerItems.RadParagraphDef;
import org.radixware.kernel.common.client.types.ExplorerRoot;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.env.ExplorerIcon;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public final class SelectRootDialog extends ExplorerDialog {

    final private static String DIALOG_SETTINGS_KEY = SettingNames.SYSTEM + "/SelectRootDialog";        

    final private class EventListener extends QEventFilter {
        
        public EventListener(final QObject parent){
            super(parent);
            setProcessableEventTypes(EnumSet.of(QEvent.Type.KeyPress, QEvent.Type.KeyRelease));
        }

        @Override
        public boolean eventFilter(final QObject src, final QEvent event) {
            if (event instanceof QKeyEvent) {
                final QKeyEvent key = (QKeyEvent) event;
                if (key.key() == Key.Key_Return.value() || key.key() == Key.Key_Enter.value()) {
                    accept();
                }
            }
            return false;
        }
    }
    
    final private QListWidget list = new QListWidget(this);    
    private int selectedRow = -1;

    public SelectRootDialog(final IClientEnvironment environmnet, final List<ExplorerRoot> explorerRoots) {
        this(environmnet, explorerRoots, 0);
    }

    /** конструктор принимающий в качестве парметров
     *  имена корней и текущий номер для выделения.
     */
    public SelectRootDialog(final IClientEnvironment environment, final List<ExplorerRoot> explorerRoots, final int curRow) {
        super(environment, Application.getMainWindow(),false);
        setupUi();
        final QIcon defaultIcon = ExplorerIcon.getQIcon(ClientIcon.Definitions.TREES);
        for (ExplorerRoot explorerRoot : explorerRoots) {
            list.insertItem(list.count(), createListItem(explorerRoot, defaultIcon));            
        }
        if (curRow < list.count()) {
            list.setCurrentRow(curRow);
        }        
    }
    
    private static QListWidgetItem createListItem(final ExplorerRoot explorerRoot, final QIcon defaultIcon){
        final RadParagraphDef paragraphDef = explorerRoot.getParagraphDef();
        final QIcon icon = paragraphDef.getIcon()==null ? defaultIcon : ExplorerIcon.getQIcon(paragraphDef.getIcon());
        final QListWidgetItem listItem = new QListWidgetItem(icon, explorerRoot.getTitle());//NOPMD        
        listItem.setToolTip(explorerRoot.getDescription());
        final String listItemName = "rx_explorer_root_list_item_"+explorerRoot.getId().toString();
        listItem.setData(WidgetUtils.MODEL_ITEM_ROW_NAME_DATA_ROLE, listItemName);
        listItem.setData(WidgetUtils.MODEL_ITEM_CELL_NAME_DATA_ROLE, listItemName);
        return listItem;
    }

    @Override
    public void done(final int result) {
        selectedRow = result==DialogCode.Accepted.value() ? list.currentRow() : -1;
        super.done(result);
    }        

    public void setupUi() {
        setAttribute(Qt.WidgetAttribute.WA_DeleteOnClose, true);
        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);
        setWindowTitle(Application.translate("SelectRootDialog", "Select Explorer Root"));
        setWindowIcon(ExplorerIcon.getQIcon(ClientIcon.Definitions.TREES));
        ((QVBoxLayout) layout()).insertWidget(0, list);

        list.setHorizontalScrollBarPolicy(ScrollBarPolicy.ScrollBarAlwaysOff);
        list.itemDoubleClicked.connect(this, "accept()");
        list.installEventFilter(new EventListener(this));
        list.setItemDelegate(new QItemDelegate(list));
        restoreGeometry();
        finished.connect(this, "storeGeometry()");
    }

    /** Показ модального окна для выбора корня.
     * В случае выбора, возвращается номер строки (индексация с 0),
     * иначе возвращается -1.
     */
    public int selectedItem() {     
        return exec() == DialogCode.Accepted.value() ? selectedRow : -1;
    }
        
    @SuppressWarnings("unused")
    private void storeGeometry() {
        final ExplorerSettings settings = (ExplorerSettings)getEnvironment().getConfigStore();
        final QRect geometry = geometry();
        settings.writeQPoint(DIALOG_SETTINGS_KEY + "/Pos", geometry.topLeft());
        settings.writeQSize(DIALOG_SETTINGS_KEY + "/Size", geometry.size());
    }
    
    private void restoreGeometry() {
        final ExplorerSettings settings = (ExplorerSettings)getEnvironment().getConfigStore();
        if (settings.contains(DIALOG_SETTINGS_KEY + "/Pos")) {
            final QRect geometry = 
                new QRect(settings.readQPoint(DIALOG_SETTINGS_KEY + "/Pos"), settings.readQSize(DIALOG_SETTINGS_KEY + "/Size", sizeHint()));
            geometry.setSize(WidgetUtils.shrinkWindowSize(geometry.width(), geometry.height()));
            final QSize screenSize = QApplication.desktop().availableGeometry(QCursor.pos()).size();
            if (geometry.x()<0 
                || geometry.y()<0 
                || (geometry.x()+geometry.width())>screenSize.width()
                || (geometry.y()+geometry.height())>screenSize.height()){
                final QWidget parent = (QWidget)getEnvironment().getMainWindow();
                if (parent!=null){
                    geometry.moveCenter(parent.frameGeometry().center());
                }
            }
            setGeometry(geometry);
        }
    }
}
