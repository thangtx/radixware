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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.RadixWareIcons;


public class PhraseTableModel extends AbstractTableModel {
        private List<Prompt> promptList;
        private List<String> columns;
        private final static int iconSize=16;

        private EIsoLanguage translLang;

        public PhraseTableModel(final List<Prompt> promptList, final EIsoLanguage translLang) {
            this.translLang=translLang;
            columns=new ArrayList<>();
            columns.add("");
            columns.add(NbBundle.getMessage(PhraseTableModel.class, "SRC"));
            columns.add(NbBundle.getMessage(PhraseTableModel.class, "TRANSLATION"));
            columns.add(NbBundle.getMessage(PhraseTableModel.class, "COMMENT"));
            this.promptList=new ArrayList<>(promptList);
        }

        public void setPromptList(final List<Prompt> promptList) {
            this.promptList=new ArrayList<>(promptList);
        }

        @Override
        public int getRowCount() {
            return promptList.size();
        }

        @Override
        public int getColumnCount() {
            return columns.size();
        }

        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex) {
            if(columnIndex==0){
                if(promptList.get(rowIndex).getPhraseBook()==null)
                    return RadixWareIcons.MLSTRING_EDITOR.CHOOSE_LANGS.getIcon(iconSize, iconSize);
                else
                    return AdsDefinitionIcon.PHRASE_BOOK.getIcon(iconSize, iconSize);
            }else if(columnIndex==1){
                return promptList.get(rowIndex).getSourceText();
            }else if(columnIndex==2){
                return promptList.get(rowIndex).getTranslation(translLang);
            }else
                return "";
        }

        @Override
        public String getColumnName(final int col) {
            return columns.get(col);
        }

        public Prompt getRow(final int row) {
            return promptList.get(row);
        }

        public void addRow(final Prompt row) {
            promptList.add(row);
        }
    }
