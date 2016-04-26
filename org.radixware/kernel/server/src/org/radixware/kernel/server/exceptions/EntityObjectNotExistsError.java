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

package org.radixware.kernel.server.exceptions;

import java.util.List;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.types.Pid;
import org.radixware.kernel.server.utils.SrvValAsStr;

public class EntityObjectNotExistsError extends RadixError {

    private static final long serialVersionUID = 9060586457333835628L;
    private final DdsTableDef table;
    private final String keyPres;
    private final DdsIndexDef key;
    private final List<Object> keyPropVals;

    public EntityObjectNotExistsError(final Arte arte, final DdsTableDef table, final DdsIndexDef sk, final List<Object> keyPropVals) {
        super("Object from " + table.getName() + " (#" + table.getId() + ") was not found by secondary key: " + getSecondaryKeyPres(arte, sk, keyPropVals));
        this.table = table;
        keyPres = "SK=" + getSecondaryKeyPres(arte, sk, keyPropVals);
        key = sk;
        this.keyPropVals = keyPropVals;
    }

    public EntityObjectNotExistsError(final Pid pid) {
        super(pid == null ? "Object not exist" : "Object from " + pid.getTable().getName() + " (#" + pid.getEntityId() + ") not found by PID = " + pid.toString());
        if (pid != null) {
            table = pid.getTable();
            keyPres = "PID=" + pid.toString();
            key = table.getPrimaryKey();
            keyPropVals = pid.getPkVals();
        } else {
            table = null;
            keyPres = null;
            key = null;
            keyPropVals = null;
        }
    }
    
    public EntityObjectNotExistsError(final WrongPidFormatError error){
        super(error.getMessage());
        table = error.getTableDef();
        keyPres = "PID=" + error.getPidAsStr();
        key = table.getPrimaryKey();
        keyPropVals = null;
    }

    private static String getSecondaryKeyPres(final Arte arte, final DdsIndexDef sk, final List<Object> keyPropVals) {
        final StringBuilder pres = new StringBuilder(sk.getName());
        pres.append(" (#");
        pres.append(sk.getId().toString());
        pres.append(") {");
        for (int i = 0; i < sk.getColumnsInfo().size(); i++) {
            if (i > 0) {
                pres.append(',');
            }
            final DdsColumnDef col = sk.getColumnsInfo().get(i).getColumn();
            pres.append(col.getName());
            pres.append(" (#");
            pres.append(col.getId().toString());
            pres.append(")=fromValAsStr(\"");
            pres.append(SrvValAsStr.toStr(arte, keyPropVals.get(i), col.getValType()));
            pres.append("\")");
        }
        pres.append('}');
        return pres.toString();
    }

    /**
     * @return the table
     */
    public DdsTableDef getTable() {
        return table;
    }

    /**
     * PID or detailed description of secondary key
     * @return the keyStr
     */
    public String getKeyPres() {
        return keyPres;
    }

    /**
     * Table title followed by list of key-value pairs.
     * @return
     */
    public String getBrokenRefPres() {
        if (key!=null && keyPropVals!=null){            
            final StringBuilder pres = new StringBuilder(table.getName());
            pres.append(" {");
            for (int i = 0; i < key.getColumnsInfo().size(); i++) {
                if (i > 0) {
                    pres.append("; ");
                }
                final DdsColumnDef col = key.getColumnsInfo().get(i).getColumn();
                pres.append(col.getName());
                pres.append("=");
                pres.append(String.valueOf(keyPropVals.get(i)));
            }
            pres.append("}");
            return pres.toString();
        }else{
            return getKeyPres();
        }
    }
}
