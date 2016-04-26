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

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.ValAsStrEditPanel;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EIsoLanguage;


class PreviewObjectTitleFormatTableCellEditor extends AbstractCellEditor implements TableCellEditor{

    private JComboBox comboBox = null;
    private ValAsStrEditPanel valAsStrEditPanel = null;
    private EIsoLanguage language = EIsoLanguage.ENGLISH;
    private EValType valType;
    private boolean isAdsTypeEnum;

    public PreviewObjectTitleFormatTableCellEditor(AdsPropertyDef adsPropertyDef){
        super();

        //for AdsEnumType create and show combobox with adsType's items
        //else show ValAsStrEditPanel

        final AdsTypeDeclaration adsTypeDeclaration = adsPropertyDef.getValue().getType();
        final AdsType adsType = adsTypeDeclaration.resolve(adsPropertyDef).get();
        if (adsType instanceof AdsEnumType){
            final List<AdsEnumItemDef> items = ((AdsEnumType)adsType).getSource().getItems().list(EScope.ALL);
            final ArrayList<Object> content = new ArrayList<Object>(items.size());
            
            for (final AdsEnumItemDef xItem : items){
                content.add(new Object(){

                    @Override
                    public String toString() {
                        return xItem.getTitle(language);
                    }
                });
            }

            isAdsTypeEnum = true;
            comboBox = new JComboBox(content.toArray());
        }else{
            
            isAdsTypeEnum = false;
            valType = adsTypeDeclaration.getTypeId();
            valAsStrEditPanel = new ValAsStrEditPanel();
        }
    }

    public void setLanguage(EIsoLanguage language){
        this.language = language;
    }

    @Override
    public Object getCellEditorValue() {
        return isAdsTypeEnum ? comboBox.getSelectedItem() : valAsStrEditPanel.getValue();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

        if (isAdsTypeEnum){
            assert(comboBox != null);
            return comboBox;
        }else{
            assert(valAsStrEditPanel != null);
            final EValType valTypeForEditing = ValAsStrEditPanel.getValTypeForArgument(valType);
            valAsStrEditPanel.setValue(valTypeForEditing, ValAsStr.Factory.newInstance(value, valTypeForEditing));
            return valAsStrEditPanel;
        }
    }
}
