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

import com.trolltech.qt.core.Qt.Alignment;
import com.trolltech.qt.core.Qt.AlignmentFlag;
import com.trolltech.qt.gui.QSizePolicy.Policy;
import com.trolltech.qt.gui.*;
import java.awt.Color;
import java.util.ArrayList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

final class PropertySettingsWidget extends SettingsWidget {

    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>();
    public final Signal2<QColor, String> updateBgrnColorSignal = new Signal2<>();
    public final Signal2<QColor, String> updateFgrnColorSignal = new Signal2<>();
    public final Signal2<QFont, String> updateFontSignal = new Signal2<>();
    private final ColorSettingsWidget bgrnd, fgrn;
    private final FontSettingsWidget fsw;

    public PropertySettingsWidget(final IClientEnvironment environment, final QWidget parent,
            final String gr,
            final String sub,
            final String n,
            final String title,
            final boolean showBgrnColor) {
        super(environment, parent, gr, sub, n);

        QGridLayout gridLayout = new QGridLayout();

        gridLayout.setWidgetSpacing(16);
        gridLayout.setContentsMargins(0, 0, 0, 0);
        gridLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));
        int gridLayoutIdx = 0;

        QGridLayout topLayout = new QGridLayout();
        topLayout.setAlignment(new Alignment(AlignmentFlag.AlignRight, AlignmentFlag.AlignTop));

        fsw = new FontSettingsWidget(environment, parent, gr, sub, n + "/" + SettingNames.TextOptions.FONT, Application.translate("Settings Dialog", "Font"));
//         if (showBgrnColor)
        fsw.addToParent(0, topLayout);
//         else
//             fsw.addToParent(gridLayoutIdx++, gridLayout);

        settingsArrayList.add(fsw);
        fsw.updateFontSignal.connect(this, "fswChanged()");

        if (showBgrnColor) {
            bgrnd = new ColorSettingsWidget(environment, parent, gr, sub, n + "/" + SettingNames.TextOptions.BCOLOR, Application.translate("Settings Dialog", "Background color"));
            bgrnd.addToParent(gridLayoutIdx++, gridLayout);
            settingsArrayList.add(bgrnd);
            bgrnd.updateColorSignal.connect(this, "bgrnChanged()");
        } else {
            bgrnd = null;
        }

        fgrn = new ColorSettingsWidget(environment, parent, gr, sub, n + "/" + SettingNames.TextOptions.FCOLOR, Application.translate("Settings Dialog", "Foreground color"));
        fgrn.addToParent(gridLayoutIdx++, gridLayout);
        settingsArrayList.add(fgrn);
        fgrn.updateColorSignal.connect(this, "fgrnChanged()");

        QVBoxLayout vboxlayout = new QVBoxLayout();
        vboxlayout.addLayout(topLayout);
        vboxlayout.addLayout(gridLayout);

        //      
        QGroupBox groupBox = new QGroupBox(title);
        groupBox.setLayout(vboxlayout);
        groupBox.setSizePolicy(Policy.Preferred, Policy.Maximum);
        QVBoxLayout mainlayout = new QVBoxLayout(this);
        mainlayout.setMargin(0);
        mainlayout.addWidget(groupBox);
    }

    private void fswChanged() {
        updateFontSignal.emit(fsw.getFont(), name);
    }

    private void bgrnChanged() {
        updateBgrnColorSignal.emit(bgrnd.getColor(), name);
    }

    private void fgrnChanged() {
        updateFgrnColorSignal.emit(fgrn.getColor(), name);
    }

    @Override
    public void restoreDefaults() {
        for (SettingsWidget w : settingsArrayList) {
            w.restoreDefaults();
        }
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        for (SettingsWidget w : settingsArrayList) {
            w.readSettings(src);
        }
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        for (SettingsWidget w : settingsArrayList) {
            w.writeSettings(dst);
        }
    }

    public QColor getBackgroundColor() {
        return bgrnd != null ? bgrnd.getColor() : null;
    }

    public QColor getForegroundColor() {
        return fgrn.getColor();
    }

    public QFont getFont() {
        return fsw.getFont();
    }

    public void setBackgroundColor(QColor color) {
        bgrnd.setColor(color);
    }

    public void setForegroundColor(QColor color) {
        fgrn.setColor(color);
    }

    public void setSettingsFont(QFont font) {
        fsw.setSettingsFont(font);
    }

    public ExplorerTextOptions getTextOptions() {
        final ExplorerFont font = ExplorerFont.Factory.getFont(getFont());
        final Color bg = ExplorerTextOptions.qtColor2awtColor(getBackgroundColor());
        final Color fg = ExplorerTextOptions.qtColor2awtColor(getForegroundColor());
        return ExplorerTextOptions.Factory.getOptions(font, fg, bg);
    }
}
