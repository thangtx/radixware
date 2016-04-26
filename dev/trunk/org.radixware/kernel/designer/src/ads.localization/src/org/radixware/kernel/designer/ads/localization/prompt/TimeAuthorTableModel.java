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

package org.radixware.kernel.designer.ads.localization.prompt;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.designer.ads.localization.RowString;


public class TimeAuthorTableModel extends AbstractTableModel {
    private List<EIsoLanguage> langs;
    private RowString rowString;
    private List<String> columns;
    private final DateFormat dateTimeFormat= new SimpleDateFormat("yyyy.MM.dd HH:mm");
    
    public TimeAuthorTableModel(final RowString rowString, final List<EIsoLanguage> langs) {
            this.langs=langs;
            this.rowString=rowString;
            columns=new ArrayList<>();
            columns.add(NbBundle.getMessage(TimeAuthorTableModel.class, "LANGUAGE"));
            columns.add(NbBundle.getMessage(TimeAuthorTableModel.class, "LAST_UPDATE_AUTHOR"));
            columns.add(NbBundle.getMessage(TimeAuthorTableModel.class, "LAST_UPDATE_TIME"));
            columns.add(NbBundle.getMessage(TimeAuthorTableModel.class, "LAST_CHANGE_STATUS_AUTHOR"));
            columns.add(NbBundle.getMessage(TimeAuthorTableModel.class, "LAST_CHANGE_STATUS_TIME"));
    }

    @Override
    public int getRowCount() {
        return rowString!=null ? langs.size() : 0;
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }
    
    @Override
    public String getColumnName(final int col) {
        return columns.get(col);
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        final EIsoLanguage lang = langs.get(rowIndex);
        Timestamp time;
        if(rowString!=null){
            switch (columnIndex) {
                case 0:
                    return lang.getName();
                case 1:
                    return rowString.getLastChangeAuthor(lang);
                case 2:
                    time = rowString.getLastChangeTime(lang);
                    return time == null ? "" : dateTimeFormat.format(time);
                case 3:
                    return rowString.getChangeStatusAuthor(lang);
                case 4:
                    time = rowString.getChangeStatusTime(lang);
                    return time == null ? "" : dateTimeFormat.format(time);
            }
        }
        return "";
    }
}
