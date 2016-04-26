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

import com.trolltech.qt.gui.QSlider;
import com.trolltech.qt.gui.QWidget;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.core.Qt.Orientation;
import com.trolltech.qt.gui.QCursor;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QPalette.ColorRole;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.ExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;

public class AlternativeColorSettingsWidget extends SettingsWidget {

    private final ColoredFrame styleColorFrame, oddColorFrame;
    private final QSlider slider;
    private final int defaultValue;

    AlternativeColorSettingsWidget(IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n) {
        super(environment, parent, gr, sub, n);

        defaultValue = getDefaultSettings().readInteger(getSettingCfgName());

        styleColorFrame = new ColoredFrame(this, QApplication.palette().color(ColorRole.Base), false);
        styleColorFrame.setEnabled(false);

        oddColorFrame = new ColoredFrame(this, QApplication.palette().color(ColorRole.Base), false);
        oddColorFrame.setEnabled(false);

        slider = new QSlider(Orientation.Horizontal, this);
        slider.setFixedWidth(WidgetUtils.MAXIMUM_DARKER_COLOR_FACTOR);
        slider.valueChanged.connect(this, "setValue(int)");
        slider.setCursor(new QCursor(com.trolltech.qt.core.Qt.CursorShape.PointingHandCursor));

        QGridLayout gridLayout = new QGridLayout(this);
        gridLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignBottom));
        gridLayout.addWidget(slider, 0, 0);
        gridLayout.addWidget(styleColorFrame, 1, 0);
        gridLayout.addWidget(oddColorFrame, 2, 0);

        this.setValue(defaultValue);
    }

    @Override
    public void restoreDefaults() {
        setValue(defaultValue);
    }

    @Override
    public void readSettings(ExplorerSettings src) {
        final int value = src.readInteger(getSettingCfgName(), defaultValue);
        setValue(value);
    }

    @Override
    public void writeSettings(ExplorerSettings dst) {
        final String path = getSettingCfgName();
        dst.writeInteger(path, slider.value());
    }

    public void setValue(final int value) {
        final QColor styleColor = styleColorFrame.getColor();
        final QColor oddColor = WidgetUtils.getDarkerColor(styleColor, value);
        slider.setValue(value);
        oddColorFrame.setColor(oddColor);
    }

    public void setColor(QColor color) {
        styleColorFrame.setColor(color);
        setValue(slider.value());
    }
}
