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

package org.radixware.kernel.designer.ads.editors.clazz.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef.TitleItem;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;



class ObjectTitleFormatTableModel extends AbstractTableModel{

    //private final AdsObjectTitleFormatDef objectTitleFormat;
    private final RadixObjects<AdsObjectTitleFormatDef.TitleItem> items;
    private final static EIsoLanguage EISoLanguageForCurrentLocale = getEISOLanguageForCurrentLocale();

    public ObjectTitleFormatTableModel(AdsObjectTitleFormatDef objectTitleFormat){
        //this.objectTitleFormat = objectTitleFormat;
        this.items = objectTitleFormat.getItems();
    }

    public void addTitleItem(int index, AdsObjectTitleFormatDef.TitleItem titleItem){
        items.add(index, titleItem);
        fireTableDataChanged();
    }

    public void addTitleItem(AdsObjectTitleFormatDef.TitleItem titleItem){
        items.add(titleItem);
        fireTableDataChanged();
    }

    public void removeTitleItem(AdsObjectTitleFormatDef.TitleItem titleItem){
        items.remove(titleItem);
        fireTableDataChanged();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public String getColumnName(int column) {
        return ObjectTitleFormatTableModel.columnNamesArray[column];
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        
        if (columnIndex == ObjectTitleFormatTableModel.TITLES_FORMAT_COLUMN){
            if (value instanceof AdsPropertyDefWrapper){
                items.get(rowIndex).setPropertyId( ((AdsPropertyDefWrapper)value).getId());
            }else{
                //it is "<Undefined>" value
                items.get(rowIndex).setPropertyId(null);
            }
        }//else do nothing because all necessary updates have been performed by an appropriate cell editor
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (columnIndex == ObjectTitleFormatTableModel.TITLES_FORMAT_COLUMN){
            final AdsPropertyDef adsPropertyDef = items.get(rowIndex).findProperty();
            return adsPropertyDef == null ? "<Undefined>" : adsPropertyDef.getName();
        }else{
            final TitleItem item = items.get(rowIndex);
            if (item.isMultilingual()){
                return item.getPattern(EISoLanguageForCurrentLocale);
            }else{
                return item.getPattern();
            }
        }
    }

    private static EIsoLanguage getEISOLanguageForCurrentLocale(){
        try{
            return EIsoLanguage.getForValue(Locale.getDefault().getLanguage());
        }catch (NoConstItemWithSuchValueError e){
            return EIsoLanguage.ENGLISH;
        }
    }

    public void moveUp(int rowIndex){
        items.get(rowIndex).moveUp();
        fireTableDataChanged();
    }

    public void moveDown(int rowIndex){
        items.get(rowIndex).moveDn();
        fireTableDataChanged();
    }

    public AdsObjectTitleFormatDef.TitleItem getTitleItem(int rowIndex){
        return items.get(rowIndex);
    }

    public IAdsPropertiesListProvider getPropertiesListProvider(){
        return new IAdsPropertiesListProvider() {

            @Override
            public List<AdsPropertyDef> getAdsPropertiesList() {
                final List<AdsPropertyDef> list = new ArrayList<AdsPropertyDef>();
                
                for (TitleItem titleItem : items){
                    final AdsPropertyDef adsPropertyDef = titleItem.findProperty();
                    if (! list.contains(adsPropertyDef)){
                        list.add(adsPropertyDef);
                    }
                }

                return list;
            }
        };
    }
    
    private static final String columnNamesArray[] = new String[]{"Property", "Format"};
    public static final int TITLES_FORMAT_COLUMN = 0, FORMAT_COLUMN = 1;
}
