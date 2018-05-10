/*
 * Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * This Source Code is distributed WITHOUT ANY WARRANTY; including any 
 * implied warranties but not limited to warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 * License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.inspector.propertyEditors;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QPalette;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QWidget;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;

public class EditPaletteDlg extends ExplorerDialog {

    private final static List<QPalette.ColorGroup> userColorGroup = Arrays.asList(QPalette.ColorGroup.Active, QPalette.ColorGroup.Disabled, QPalette.ColorGroup.Inactive);
    private final List<ColorPushBtn> colorPushBtnList = new LinkedList<>();

    public EditPaletteDlg(QPalette pal, IClientEnvironment environment, QWidget parent, String title) {
        super(environment, parent, null);
        this.setWindowTitle(title);
        QGridLayout qGridLayout = new QGridLayout();

        qGridLayout.addWidget(new QLabel("Color Role", this), 0, 0);
        for (int counter = 0; counter < userColorGroup.size(); counter++) {
            QLabel titleLbl = new QLabel(userColorGroup.get(counter).name(), this);
            titleLbl.setAlignment(Qt.AlignmentFlag.AlignCenter);
            qGridLayout.addWidget(titleLbl, 0, counter + 1);
        }

        for (int counter = 0; counter < QPalette.ColorRole.values().length; counter++) {
            qGridLayout.addWidget(new QLabel(QPalette.ColorRole.resolve(counter).name(), this), counter + 1, 0);
        }

        int i = 1, j = 1;
        for (QPalette.ColorRole colorRole : QPalette.ColorRole.values()) {
            for (QPalette.ColorGroup colorGroup : userColorGroup) {
                ColorPushBtn colorPushBtn = new ColorPushBtn(colorRole.toString() + ", " + colorGroup.toString(), this, colorRole, colorGroup, pal.color(colorGroup, colorRole));
                colorPushBtnList.add(colorPushBtn);
                qGridLayout.addWidget(colorPushBtn, i, j);
                j++;
            }
            j = 1;
            i++;
        }
        this.dialogLayout().addLayout(qGridLayout);

        this.dialogLayout().addSpacerItem(new QSpacerItem(20, 40, QSizePolicy.Policy.Fixed, QSizePolicy.Policy.Expanding));
        this.addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
        this.setAutoSize(true);
    }

    QPalette getQPalette() {
        QPalette qPalette = new QPalette();
        for (ColorPushBtn colorPushBtn : this.colorPushBtnList) {
            if (colorPushBtn.getColor() != null) {
                qPalette.setColor(colorPushBtn.getColorGroup(), colorPushBtn.getColorRole(), colorPushBtn.getColor());
            }
        }
        return qPalette;
    }
}
