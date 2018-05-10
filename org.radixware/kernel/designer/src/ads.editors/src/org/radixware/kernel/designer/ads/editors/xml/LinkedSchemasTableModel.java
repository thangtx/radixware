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
package org.radixware.kernel.designer.ads.editors.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.enums.EXmlSchemaLinkMode;
import org.radixware.kernel.common.utils.Pair;

/**
 *
 * @author dlastochkin
 */
public class LinkedSchemasTableModel extends AbstractTableModel {

    Map<AdsXmlSchemeDef, EXmlSchemaLinkMode> schemasMap;

    public LinkedSchemasTableModel(Map<AdsXmlSchemeDef, EXmlSchemaLinkMode> linkedSchemas) {
        if (linkedSchemas != null) {
            this.schemasMap = new HashMap<>(linkedSchemas);
        } else {
            this.schemasMap = new HashMap<>();
        }
    }

    @Override
    public int getRowCount() {
        return schemasMap.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        List<AdsXmlSchemeDef> schemasList = new ArrayList<>(schemasMap.keySet());
        Collections.sort(schemasList, new Comparator<AdsXmlSchemeDef>() {
            @Override
            public int compare(AdsXmlSchemeDef o1, AdsXmlSchemeDef o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        
        if (columnIndex == 0 || columnIndex == 1 || columnIndex == 2) {
            return new Pair<>(schemasList.get(rowIndex), schemasMap.get(schemasList.get(rowIndex)));
        } else {
            return null;
        }
    }

    public void addRow(AdsXmlSchemeDef schema, EXmlSchemaLinkMode linkMode) {
        if (linkMode == EXmlSchemaLinkMode.IMPORT) {
            if (!schemasMap.containsKey(schema)) {
                schemasMap.put(schema, linkMode);
            }
        } else {
            schemasMap.put(schema, linkMode);
        }
    }

}
