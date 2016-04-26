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

package org.radixware.kernel.explorer.editors.jmleditor;

import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import java.util.EnumSet;
import java.util.Map;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.schemas.udsdef.UserFunctionDefinition;


class UfNameChooser extends ExplorerDialog {

    private final QComboBox cb = new QComboBox();

    public UfNameChooser(final IClientEnvironment env, final Map<Id, UserFunctionDefinition> map) {
        super(env, null);
        final QHBoxLayout hBox = new QHBoxLayout();
        dialogLayout().addLayout(hBox);
        hBox.addWidget(new QLabel(env.getMessageProvider().translate("JmlEditor", "Choose User Function")));
        hBox.addWidget(cb);
        cb.setSizePolicy(QSizePolicy.Policy.Expanding, QSizePolicy.Policy.Preferred);
        for (Map.Entry<Id, UserFunctionDefinition> d : map.entrySet()) {
            cb.addItem(d.getValue().getName(), d.getKey());
        }
        cb.setCurrentIndex(0);


        addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
    }

    public Id getSelectedId() {
        return (Id) cb.itemData(cb.currentIndex());
    }
}
