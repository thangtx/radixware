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

import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class ColorSettingsWidget extends SettingsWidget {

    private final QLabel labelTitle;
    private final ColoredFrame coloredFrame;
    public final Signal1<QColor> updateColorSignal = new Signal1<QColor>();
//	private boolean enabled;
    private QColor defaultColor;

    public ColorSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String description) {
        super(environment, parent, gr, sub, n);

        labelTitle = new QLabel();
        labelTitle.setText(description);

        defaultColor = getDefaultSettings().readQColor(getSettingCfgName());

        coloredFrame = new ColoredFrame(this, defaultColor, true);
        coloredFrame.setColorSignal.connect(updateColorSignal, "emit(Object)");
    }

    public void addToParent(final int row, final QGridLayout gridLayout) {
        gridLayout.addWidget(labelTitle, row, 0);
        gridLayout.addWidget(coloredFrame, row, 1);
    }

    public void addToParent(final QBoxLayout layout) {
        final QHBoxLayout contentLayout = WidgetUtils.createHBoxLayout(null);
        contentLayout.setSpacing(5);
        contentLayout.addWidget(labelTitle);
        contentLayout.addWidget(coloredFrame, 0, AlignmentFlag.AlignLeft);
        layout.addLayout(contentLayout);
    }

    @Override
    public void restoreDefaults() {
        coloredFrame.setColor(defaultColor);
    }

    @Override
    public void readSettings(ExplorerSettings src) {
        final QColor color = src.readQColor(getSettingCfgName());
        coloredFrame.setColor(color);
        updateColorSignal.emit(color);
    }

    @Override
    public void writeSettings(ExplorerSettings dst) {
        dst.writeQColor(getSettingCfgName(), coloredFrame.getColor());
    }

    public QColor getColor() {
        return coloredFrame.getColor();
    }
    
     public void setColor(QColor color) {
        coloredFrame.setColor(color);
    }
}