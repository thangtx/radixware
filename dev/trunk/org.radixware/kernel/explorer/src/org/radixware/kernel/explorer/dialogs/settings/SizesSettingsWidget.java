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

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QSpinBox;
import com.trolltech.qt.gui.QWidget;
//import com.trolltech.qt.gui.QHBoxLayout;
import com.trolltech.qt.core.QSize;
//import com.trolltech.qt.core.Qt.Alignment;
//import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QLabel;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.ExplorerSettings;

public class SizesSettingsWidget extends SettingsWidget {

    private QLabel sizeLabel;
    private QSpinBox sizeSpinBox;
    private final QSize defaultsize;
    private QSize size;
    private QGridLayout parentGridLayout;

    public SizesSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String descr, final QGridLayout parentGridLayout) {
        super(environment, parent, gr, sub, n);
        this.defaultsize = getDefaultSettings().readQSize(getSettingCfgName());
        this.size = new QSize(defaultsize.width(), defaultsize.height());
        this.parentGridLayout = parentGridLayout;

        sizeLabel = new QLabel(this);
        sizeLabel.setText(descr);

        sizeSpinBox = new QSpinBox();
        sizeSpinBox.setRange(16, 32);
        sizeSpinBox.setSingleStep(1);
        sizeSpinBox.setValue(defaultsize.height());
        sizeSpinBox.setFixedSize(64, sizeSpinBox.sizeHint().height());
        sizeSpinBox.valueChanged.connect(this, "onChooseSize()");
    }

    public void addToParent(final int row) {
        parentGridLayout.addWidget(sizeLabel, row, 0);
        parentGridLayout.addWidget(sizeSpinBox, row, 1);
    }

    @SuppressWarnings("unused")
    private void onChooseSize() {
        final int s = sizeSpinBox.value();
        size.setWidth(s);
        size.setHeight(s);
    }

    @Override
    public void restoreDefaults() {
        sizeSpinBox.setValue(this.defaultsize.height());
    }

    @Override
    public void readSettings(ExplorerSettings src) {
        QSize d = new QSize(defaultsize.width(), defaultsize.height());
        size = src.readQSize(getSettingCfgName(), d);
        sizeSpinBox.setValue(size.width());
    }

    @Override
    public void writeSettings(ExplorerSettings dst) {
        dst.writeQSize(getSettingCfgName(), size);
    }
}
