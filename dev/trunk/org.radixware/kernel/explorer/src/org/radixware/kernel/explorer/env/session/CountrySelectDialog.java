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
package org.radixware.kernel.explorer.env.session;

import com.trolltech.qt.gui.QGridLayout;
import com.trolltech.qt.gui.QGroupBox;
import com.trolltech.qt.gui.QLabel;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QWidget;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.env.LocaleManager;
import org.radixware.kernel.common.client.localization.MessageProvider;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.widgets.IListWidget;
import org.radixware.kernel.common.client.widgets.ListWidgetItem;
import org.radixware.kernel.common.enums.EDateTimeStyle;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EIsoCountry;
import org.radixware.kernel.explorer.dialogs.ExplorerDialog;
import org.radixware.kernel.explorer.editors.valeditors.ValStrEditor;
import org.radixware.kernel.explorer.widgets.ExplorerListWidget;

final class CountrySelectDialog extends ExplorerDialog {

    private final IClientEnvironment environment;
    private ExplorerListWidget explorerListWidget;
    private ValStrEditor numberFormatTest;
    private ValStrEditor dateTimeLong;
    private ValStrEditor dateTimeMedium;
    private ValStrEditor dateTimeShort;
    private ValStrEditor dateTimeFull;

    public CountrySelectDialog(IClientEnvironment environment, QWidget parent, List<EIsoCountry> countryList, String currentCountry, final String language) {
        super(environment, parent, null, 450, 400);
        this.environment = environment;
        setupUI(countryList, currentCountry, language);
    }

    private void setupUI(List<EIsoCountry> countryList, final String currentCountry, final String language) {
        final MessageProvider msgProvider = getEnvironment().getMessageProvider();
        setWindowTitle(msgProvider.translate("CountrySelectDialog", "Select Country"));

        QVBoxLayout verticalLayout = new QVBoxLayout();
        explorerListWidget = new ExplorerListWidget(environment, this);
        explorerListWidget.setFeatures(EnumSet.of(IListWidget.EFeatures.AUTO_SORTING, IListWidget.EFeatures.FILTERING));

        List<ListWidgetItem> list = new LinkedList<>();
        for (EIsoCountry country : countryList) {
            list.add(new ListWidgetItem(LocaleManager.getLocalizedCountryName(country, environment), country));
        }

        explorerListWidget.setItems(list);
        for (int i = 0; i < explorerListWidget.getItems().size(); i++) {
            if (((EIsoCountry) explorerListWidget.getItem(i).getValue()).getValue().equals(currentCountry)) {
                explorerListWidget.setCurrentRow(i);
            }
        }
        verticalLayout.addWidget(explorerListWidget);
        verticalLayout.addSpacing(5);
        QGridLayout gridLayout = new QGridLayout();
        gridLayout.setSpacing(5);

        numberFormatTest = new ValStrEditor(environment, this, new EditMaskStr(), true, true);
        dateTimeLong = new ValStrEditor(environment, this, new EditMaskStr(), true, true);
        dateTimeMedium = new ValStrEditor(environment, this, new EditMaskStr(), true, true);
        dateTimeShort = new ValStrEditor(environment, this, new EditMaskStr(), true, true);
        dateTimeFull = new ValStrEditor(environment, this, new EditMaskStr(), true, true);

        QGroupBox gb = new QGroupBox(msgProvider.translate("CountrySelectDialog", "Format Examples"), this);

        QLabel number = new QLabel(msgProvider.translate("CountrySelectDialog", "Number format: "), this);
        QLabel dateTimeFullLabel = new QLabel(msgProvider.translate("CountrySelectDialog", "Full date/time format: "), this);
        QLabel dateTimeLongLabel = new QLabel(msgProvider.translate("CountrySelectDialog", "Long date/time format: "), this);
        QLabel dateTimeMediumLabel = new QLabel(msgProvider.translate("CountrySelectDialog", "Medium date/time format: "), this);
        QLabel dateTimeShortLabel = new QLabel(msgProvider.translate("CountrySelectDialog", "Short date/time format: "), this);

        gridLayout.addWidget(number, 0, 0);
        gridLayout.addWidget(numberFormatTest, 0, 1);
        gridLayout.addWidget(dateTimeShortLabel, 1, 0);
        gridLayout.addWidget(dateTimeShort, 1, 1);
        gridLayout.addWidget(dateTimeMediumLabel, 2, 0);
        gridLayout.addWidget(dateTimeMedium, 2, 1);
        gridLayout.addWidget(dateTimeLongLabel, 3, 0);
        gridLayout.addWidget(dateTimeLong, 3, 1);
        gridLayout.addWidget(dateTimeFullLabel, 4, 0);
        gridLayout.addWidget(dateTimeFull, 4, 1);

        Locale curLoc = new Locale(language, currentCountry);
        refreshData(curLoc);

        explorerListWidget.addCurrentItemListener(new IListWidget.ICurrentItemListener() {
            @Override
            public void currentItemChanged(ListWidgetItem item) {
                Locale curLoc = new Locale(language, ((EIsoCountry) item.getValue()).getAlpha2Code());
                refreshData(curLoc);
            }
        });

        explorerListWidget.addDoubleClickListener(new IListWidget.IDoubleClickListener() {
            @Override
            public void itemDoubleClick(ListWidgetItem item) {
                CountrySelectDialog.super.acceptDialog();
            }
        });

        gb.setLayout(gridLayout);
        verticalLayout.addWidget(gb);
        this.layout().addItem(verticalLayout);
        this.addButtons(EnumSet.of(EDialogButtonType.OK, EDialogButtonType.CANCEL), true);
    }

    public EIsoCountry getCurrentValue() {
        return (EIsoCountry) explorerListWidget.getCurrent().getValue();
    }

    private void refreshData(Locale curLoc) {
        Date date = new Date();
        dateTimeFull.setValue(getDateStr(EDateTimeStyle.FULL, curLoc, date));
        dateTimeLong.setValue(getDateStr(EDateTimeStyle.LONG, curLoc, date));
        dateTimeMedium.setValue(getDateStr(EDateTimeStyle.MEDIUM, curLoc, date));
        dateTimeShort.setValue(getDateStr(EDateTimeStyle.SHORT, curLoc, date));   

        EditMaskNum editMaskNum1 = new EditMaskNum();
        editMaskNum1.setTriadDelimeterType(null);
        NumberFormat numFormat = editMaskNum1.getNumberFormat(curLoc);
        numberFormatTest.setValue(numFormat.format(123456789.12345));
    }
    
    @SuppressWarnings("deprecation")
    private String getDateStr(EDateTimeStyle dateTimeStyle, Locale locale, Date date) {
        String customPattern = new EditMaskDateTime(dateTimeStyle, dateTimeStyle, null, null).getDisplayFormat(locale);
        return new SimpleDateFormat(customPattern, locale).format(date);
    }
}
