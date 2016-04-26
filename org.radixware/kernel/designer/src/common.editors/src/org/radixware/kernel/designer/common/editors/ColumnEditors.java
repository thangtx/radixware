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

package org.radixware.kernel.designer.common.editors;



import javax.swing.table.*;
import java.util.*;

public class ColumnEditors<T extends Object> {
     protected Hashtable<T, TableCellEditor> data;

     public ColumnEditors()
     {
         data = new Hashtable<T, TableCellEditor>();
     }
     public void addEditorForItem(T item, TableCellEditor e)
     {
         data.put(item, e);
     }
     public void removeEditorForItem(T item)
     {
         data.remove(item);
     }
     public TableCellEditor getEditorForItem(T item)
     {
         return data.get(item);
     }
     public void removeEditors(){
         data.clear();
     }
}