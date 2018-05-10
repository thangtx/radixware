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
import com.trolltech.qt.gui.*;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.explorer.env.IExplorerSettings;
import org.radixware.kernel.explorer.utils.WidgetUtils;
import org.radixware.kernel.starter.utils.SystemTools;

final class FontSettingsWidget extends SettingsWidget {

    private final QLabel labelTitle;
    private final QToolButton toolButtonChooseFont;
    private final QFont defaultfont;
    private QFont font;    
    public final Signal1<QFont> updateFontSignal = new Signal1<>();

    public FontSettingsWidget(final IClientEnvironment environment, final QWidget parent, final String gr, final String sub, final String n, final String descr) {
        super(environment, parent, gr, sub, n);
        defaultfont = getDefaultSettings().readQFont(getSettingCfgName());
        font = new QFont(defaultfont);

        labelTitle = new QLabel();
        labelTitle.setText(descr);

        toolButtonChooseFont = new QToolButton(this);
        updateButtonsLabel();
        toolButtonChooseFont.clicked.connect(this, "onChooseFont()");
        toolButtonChooseFont.show();
    }

    public void addToParent(final int row, final QGridLayout gridLayout) {
        gridLayout.setAlignment(new Alignment(AlignmentFlag.AlignLeft, AlignmentFlag.AlignTop));
        gridLayout.addWidget(labelTitle, row, 0);
        gridLayout.addWidget(toolButtonChooseFont, row, 1);
    }

    public void addToParent(final QBoxLayout layout) {
        final QHBoxLayout contentLayout = WidgetUtils.createHBoxLayout(null);
        contentLayout.setSpacing(5);
        contentLayout.addWidget(labelTitle);
        contentLayout.addWidget(toolButtonChooseFont, 0, AlignmentFlag.AlignLeft);
        layout.addLayout(contentLayout);
    }

    @Override
    public void readSettings(IExplorerSettings src) {
        font = src.readQFont(getSettingCfgName());
        if (font==null){
            font = new QFont(defaultfont);
        }
        updateButtonsLabel();
        updateFontSignal.emit(new QFont(font));
    }

    @Override
    public void writeSettings(IExplorerSettings dst) {
        dst.writeQFont(getSettingCfgName(), font);
    }

    @Override
    public void restoreDefaults() {
        font = new QFont(defaultfont);
        updateButtonsLabel();
        updateFontSignal.emit(new QFont(font));
    }

    @SuppressWarnings("unused")
    private void onChooseFont() {
        final QFontDialog.Result fontResult;    
        if (SystemTools.isOSX){
            QFontDialog.FontDialogOptions options = new QFontDialog.FontDialogOptions(QFontDialog.FontDialogOption.DontUseNativeDialog);
            fontResult = QFontDialog.getFont(font,this,"",options);
        }else{
            fontResult = QFontDialog.getFont(font,this);
        }
        if (fontResult.ok) {
            //�������� ����� �����
            font = fontResult.font;
            updateFontSignal.emit(new QFont(font));
            updateButtonsLabel();
        }
    }

    private void updateButtonsLabel() {
        toolButtonChooseFont.setFont(font);
        toolButtonChooseFont.setText(font.family() + " " + font.pointSize());
    }

    public QFont getFont() {
        return new QFont(font);
    }
    
    public void setSettingsFont(QFont font) {
        this.font=new QFont(font);
        updateButtonsLabel();
        updateFontSignal.emit(new QFont(font));
    }
}