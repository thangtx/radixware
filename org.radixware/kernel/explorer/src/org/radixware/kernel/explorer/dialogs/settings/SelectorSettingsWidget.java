/*
 *  Copyright (c) 2008-2015, Compass Plus Limited. All rights reserved.
 *  This Source Code Form is subject to the terms of the Mozilla Public
 *  License, v. 2.0. If a copy of the MPL was not distributed with this
 *  file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *  This Source Code is distributed WITHOUT ANY WARRANTY; including any
 *  implied warranties but not limited to warranty of MERCHANTABILITY 
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
 *  License, v. 2.0. for more details.
 */
package org.radixware.kernel.explorer.dialogs.settings;

import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QFont;
import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QSizePolicy;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.awt.Color;
import java.util.ArrayList;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.SettingNames;
import org.radixware.kernel.explorer.env.Application;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.text.ExplorerFont;
import org.radixware.kernel.explorer.text.ExplorerTextOptions;

final class SelectorSettingsWidget extends SettingsWidget {

    private final ArrayList<SettingsWidget> settingsArrayList = new ArrayList<>();
    public final Signal2<QColor, String> updateBgrnColorSignal = new Signal2<>();
    public final Signal2<QColor, String> updateDfColorSignal = new Signal2<>();
    public final Signal2<QFont, String> updateFontSignal = new Signal2<>();
    public final Signal2<QColor, String> updateNdfColorSignal = new Signal2<>();
    private final ColorSettingsWidget bgrnd, df, ndf;
    private final FontSettingsWidget fsw;

    public SelectorSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String title) {
        super(environment, parent, gr, sub, n);

        QGridLayout gridLayout = new QGridLayout();

        gridLayout.setWidgetSpacing(16);
        gridLayout.setContentsMargins(0, 0, 0, 0);
        gridLayout.setAlignment(new Qt.Alignment(Qt.AlignmentFlag.AlignLeft, Qt.AlignmentFlag.AlignTop));
        int gridLayoutIdx = 0;

        fsw = new FontSettingsWidget(environment, parent, gr, sub,
                n + "/" + SettingNames.TextOptions.FONT,
                Application.translate("Settings Dialog", "Font"));
        fsw.addToParent(gridLayoutIdx++, gridLayout);
        settingsArrayList.add(fsw);
        fsw.updateFontSignal.connect(this, "fswChanged()");

        bgrnd = new ColorSettingsWidget(environment, parent, gr, sub,
                n + "/" + SettingNames.TextOptions.BCOLOR,
                Application.translate("Settings Dialog", "Background color"));
        bgrnd.addToParent(gridLayoutIdx++, gridLayout);
        settingsArrayList.add(bgrnd);
        bgrnd.updateColorSignal.connect(this, "bgrnChanged()");

        final QLabel textColor = new QLabel();
        textColor.setText(Application.translate("Settings Dialog", "Foreground color"));
        gridLayout.addWidget(textColor, gridLayoutIdx, 0, gridLayoutIdx, 3, new Qt.Alignment(Qt.AlignmentFlag.AlignTop, Qt.AlignmentFlag.AlignHCenter));
        gridLayout.setRowMinimumHeight(gridLayoutIdx, textColor.sizeHint().height());
        gridLayoutIdx++;

        df = new ColorSettingsWidget(environment, parent, gr, sub,
                n + "/" + SettingNames.TextOptions.FCOLOR,
                Application.translate("Settings Dialog", "Value defined"));
        df.addToParent(gridLayoutIdx++, gridLayout);
        settingsArrayList.add(df);
        df.updateColorSignal.connect(this, "dfChanged()");

        ndf = new ColorSettingsWidget(environment, parent, gr, sub,
                n + "/" + SettingNames.TextOptions.FCOLOR + "/" + SettingNames.Properties.UNDEFINED,
                Application.translate("Settings Dialog", "Value undefined"));
        ndf.addToParent(gridLayoutIdx++, gridLayout);
        settingsArrayList.add(ndf);
        ndf.updateColorSignal.connect(this, "ndfChanged()");

        QVBoxLayout vboxlayout = new QVBoxLayout();
        vboxlayout.addLayout(gridLayout);

        QGroupBox groupBox = new QGroupBox(title);
        groupBox.setLayout(vboxlayout);
        groupBox.setSizePolicy(QSizePolicy.Policy.Preferred, QSizePolicy.Policy.Maximum);
        QVBoxLayout mainlayout = new QVBoxLayout(this);
        mainlayout.setMargin(0);
        mainlayout.addWidget(groupBox);
    }

    private void fswChanged() {
        updateFontSignal.emit(fsw.getFont(), name);
    }

    private void ndfChanged() {
        updateNdfColorSignal.emit(ndf.getColor(), name);
    }

    private void bgrnChanged() {
        updateBgrnColorSignal.emit(bgrnd.getColor(), name);
    }

    private void dfChanged() {
        updateDfColorSignal.emit(df.getColor(), name);
    }

    public ExplorerTextOptions getTextOptions(final boolean valueDefined) {
        final ExplorerFont font = ExplorerFont.Factory.getFont(getFont());
        final Color bg = ExplorerTextOptions.qtColor2awtColor(getBackgroundColor());
        final Color df = ExplorerTextOptions.qtColor2awtColor(getForegroundColor());
        final Color ndf = ExplorerTextOptions.qtColor2awtColor(getUndefinedColor());
        if (valueDefined) {
            return ExplorerTextOptions.Factory.getOptions(font, df, bg);
        } else {
            return ExplorerTextOptions.Factory.getOptions(font, ndf, bg);
        }
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
        return df.getColor();
    }

    public QFont getFont() {
        return fsw.getFont();
    }

    public QColor getUndefinedColor() {
        return ndf.getColor() != null ? ndf.getColor() : null;
    }

    public void setUndefinedColor(QColor color) {
        ndf.setColor(color);
    }

    public void setBackgroundColor(QColor color) {
        bgrnd.setColor(color);
    }

    public void setForegroundColor(QColor color) {
        df.setColor(color);
    }

}
