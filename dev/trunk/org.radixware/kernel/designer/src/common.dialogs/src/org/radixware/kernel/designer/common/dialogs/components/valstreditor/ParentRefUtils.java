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
package org.radixware.kernel.designer.common.dialogs.components.valstreditor;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.types.Pid;

public class ParentRefUtils {

    public static String getPidDisplayName(DdsTableDef targetTable, Pid pid) {
        if (pid == null) {
            return "<Not Defined>";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(targetTable.getQualifiedName()).append("(");
        List<Id> ids = getPidColumnIds(targetTable);
        String[] vals = getPidValues(pid);
        int index = 0;
        for (Id id : ids) {
            if (index > 0) {
                sb.append(",");
            }
            DdsColumnDef col = targetTable.getColumns().findById(id, ExtendableDefinitions.EScope.ALL).get();
            if (col != null) {
                sb.append(col.getName());
            } else {
                sb.append(id);
            }

            if (index < vals.length) {
                if (vals[index] == null || vals[index].isEmpty()) {
                    sb.append(" is null");
                } else {
                    sb.append("=");
                    sb.append(vals[index]);
                }
            } else {
                sb.append(" is null");
            }
            index++;
        }
        sb.append(")");
        return sb.toString();
    }

    public static String[] getPidValues(Pid pid) {
        if (pid == null) {
            return new String[0];
        }
        String pidStr = pid.toStr();
        int pidStart = pidStr.indexOf('\n');
        if (pidStart > 0) {
            pidStr = pidStr.substring(pidStart + 1);
        }
        List<String> valuesList = new LinkedList<>();

        int index = pidStr.indexOf('~');
        int start = 0;
        while (index >= 0) {
            if (index > 0) {
                if (pidStr.charAt(index - 1) == '\\') {
                    index = pidStr.indexOf('~', index + 1);
                    continue;
                } else {
                    valuesList.add(pidStr.substring(start, index));
                    start = index + 1;
                    index = pidStr.indexOf('~', index + 1);
                }
            }
        }
        if (start < pidStr.length()) {
            valuesList.add(pidStr.substring(start));
        }

        String[] values = valuesList.toArray(new String[valuesList.size()]);
        for (int i = 0; i < values.length; i++) {
            String val = values[i];
            //remove escape slashes
            val = val.replace("\\ ", " ");
            val = val.replace("\\\n ", "\n");
            val = val.replace("\\\r ", "\r");
            val = val.replace("\\\t ", "\t");
            val = val.replace("\\\\", "\\");
            val = val.replace("\\~", "~");
            values[i] = val;
        }
        return values;
    }

    public static List<Id> getPidColumnIds(DdsTableDef targetTable) {
        List<Id> result = new LinkedList<>();
        DdsIndexDef.ColumnsInfo pk = targetTable.getPrimaryKey().getColumnsInfo();
        for (DdsIndexDef.ColumnInfo c : pk) {
            result.add(c.getColumnId());
        }

        return result;
    }
}
