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

package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QComboBox;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.core.Qt.AlignmentFlag;

import java.util.Arrays;
import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;

final class ComboBoxAlignmentSettingsWidget extends SettingsWidget {

    private final QComboBox comboBox = new QComboBox(this);
    private final AlignmentFlag defaultAlignmentFlag;

    public ComboBoxAlignmentSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n) {
        super(environment, parent, gr, sub, n);

        defaultAlignmentFlag = getDefaultSettings().readAlignmentFlag(getSettingCfgName());

        setValues(Arrays.asList(AlignmentFlag.AlignCenter, AlignmentFlag.AlignLeft, AlignmentFlag.AlignRight), false);

        final QVBoxLayout vertLayout = WidgetUtils.createVBoxLayout(this);
        vertLayout.addWidget(comboBox);
    }

    public final void setValues(final List<AlignmentFlag> alignment, final boolean isPosition) {
        comboBox.clear();
        String title;
        for (Qt.AlignmentFlag alignmentFlag : alignment) {
            switch (alignmentFlag) {
                case AlignLeft:
                    title = isPosition ? Application.translate("Settings Dialog Position", "Left") : Application.translate("Settings Dialog", "Left");
                    break;
                case AlignRight:
                    title = isPosition ? Application.translate("Settings Dialog Position", "Right") : Application.translate("Settings Dialog", "Right");
                    break;
                case AlignCenter:
                    title = Application.translate("Settings Dialog", "Center");
                    break;
                case AlignTop:
                    title = Application.translate("Settings Dialog", "Top");
                    break;
                case AlignBottom:
                    title = Application.translate("Settings Dialog", "Bottom");
                    break;

                default:
                    title = "";
            }
            if (!title.isEmpty()) {
                comboBox.addItem(title, alignmentFlag);
            }
        }
    }

    @Override
    public void restoreDefaults() {
        setCurrentAlignmentFlag(defaultAlignmentFlag);
    }    

    @Override
    public void readSettings(final IExplorerSettings src) {
        final Qt.AlignmentFlag alignment = src.readAlignmentFlag(getSettingCfgName());
        setCurrentAlignmentFlag(alignment==null ? defaultAlignmentFlag : alignment);
    }
    
    private void setCurrentAlignmentFlag(final AlignmentFlag alignmentFlag){
        for (int i = 0, size = comboBox.count(); i < size; ++i) {
            if (comboBox.itemData(i, Qt.ItemDataRole.UserRole) == alignmentFlag) {
                comboBox.setCurrentIndex(i);
                break;
            }
        }        
    }    

    @Override
    public void writeSettings(final IExplorerSettings dst) {
        final int index = comboBox.currentIndex();
        if (index > -1) {
            dst.writeQAlignmentFlag(getSettingCfgName(), (Qt.AlignmentFlag) comboBox.itemData(index, Qt.ItemDataRole.UserRole));
        }
    }
}
