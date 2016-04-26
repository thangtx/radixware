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

package org.radixware.kernel.explorer.editors.xml;

import org.apache.xmlbeans.SchemaType;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QListWidget;
import com.trolltech.qt.gui.QListWidgetItem;
import com.trolltech.qt.gui.QWidget;
import java.util.EnumSet;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.ExplorerIcon;

public class RootList extends ExplorerDialog {

    private final QListWidget typeList = new QListWidget(this);

    public RootList(IClientEnvironment environment, final QWidget parent, final SchemaType[] types) {
        super(environment, parent, null);
        setupUi(types);
    }

    private void setupUi(final SchemaType[] types) {
        setWindowTitle(Application.translate("XmlEditor", "Choose Document Type"));
        setWindowIcon(ExplorerIcon.getQIcon(XmlEditor.Icons.ADD_CHILD_NODE));

        layout().addWidget(typeList);
        if (types != null && types.length > 0) {
            for (SchemaType schemaType : types) {
                final String itemText = schemaType.getContentModel().getName().getLocalPart();
                QListWidgetItem item = new QListWidgetItem(itemText);
                typeList.addItem(item);
                item.setData(Qt.ItemDataRole.UserRole, schemaType);
            }
            typeList.setCurrentRow(0);
            typeList.itemDoubleClicked.connect(this, "accept()");
        }

        addButtons(EnumSet.of(EDialogButtonType.OK,EDialogButtonType.CANCEL),true);
    }

    public SchemaType getSchemaType() {
        if (typeList.currentItem() == null) {
            return null;
        } else {
            return (SchemaType) typeList.currentItem().data(Qt.ItemDataRole.UserRole);
        }
    }
}