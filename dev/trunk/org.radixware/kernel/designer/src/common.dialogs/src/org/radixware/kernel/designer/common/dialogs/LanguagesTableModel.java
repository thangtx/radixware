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

package org.radixware.kernel.designer.common.dialogs;

import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.enums.EIsoLanguage;




public class LanguagesTableModel extends AbstractTableModel {
  
   private final EIsoLanguage[] languages; 
   private final java.util.ArrayList<java.lang.Boolean> booleanValues;

   
   public LanguagesTableModel(final EIsoLanguage[] languages){
       this.languages = languages;

       booleanValues = new java.util.ArrayList<java.lang.Boolean>();
       for (int i = 0; i < languages.length; ++i){ 
           booleanValues.add(new java.lang.Boolean(false)); 
       }    
   }
   
   public Class getColumnClass(int c){
       return getValueAt(0, c).getClass();
   }
   /*
   public String getColumnName(int c){
       return "";
   }*/
   public int getColumnCount(){
       return 2;
   }
   public int getRowCount(){
       return languages.length;
   }
   public Object getValueAt(int r, int c){
       return c == CHECK_BOX_COLUMN ? booleanValues.get(r) : languages[r];
   }
   public void setValueAt(Object obj, int r, int c){
       
       if (c == CHECK_BOX_COLUMN){
          booleanValues.set(r, (Boolean)obj);
          fireTableCellUpdated(r, c);
       }
   }
   
   public boolean isCellEditable(int r, int c){
       return c == CHECK_BOX_COLUMN;
   }
    
   public static final int CHECK_BOX_COLUMN = 0;
   public static final int LANGUAGE_NAME_COLUMN = 1;
}
