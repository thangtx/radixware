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
package org.radixware.kernel.server.arte.rights;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.enums.EAccessAreaMode;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.DebugUtils;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.server.arte.Arte;
import static org.radixware.kernel.server.arte.rights.Rights.ERR_CANT_CHECK_USER_RIGHTS_;
import org.radixware.kernel.server.exceptions.DatabaseError;

class RightsUtils {

    protected static boolean existsRightsOnArea(final Arte arte, final String userName, final String roleId, final Map<Id, AccessPartition> accessArea) {

        try {
            final List<String> families = new ArrayList<>();

            for (Id familyId : accessArea.keySet()) {
                families.add(familyId.toString());
            }
            Collections.sort(families);

            if (families.size() != arte.getDefManager().getAccessPartitionFamilyDefs().size()) {
                throw new RadixError("Expected access partition families: "
                        + arte.getDefManager().getAccessPartitionFamilyDefs().size()
                        + ". Found: " + families.size());
            }

            final StringBuilder qrySql = new StringBuilder("begin ? := RDX_ACS_UTILS.existsRightsOnArea(?, ?");
            for (int i = 0; i < families.size(); ++i) {
                qrySql.append(", ?, ?");
            }
            qrySql.append("); end;");

            final CallableStatement qry;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qry = arte.getDbConnection().get().prepareCall(qrySql.toString());
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                final List<java.sql.Clob> tmpClobs = new ArrayList<>();

                try {
                    //System.out.println("---------------------");
                    qry.registerOutParameter(1, java.sql.Types.INTEGER);
                    qry.setString(2, userName);
                    //System.out.println(userName);
                    qry.setString(3, roleId);
                    //System.out.println(roleId);
                    int i = 3;
                    for (String familyAsStr : families) {
                        final Id familyAsId = Id.Factory.loadFrom(familyAsStr);
                        final AccessPartition accessPartition = accessArea.get(familyAsId);
                        final int mode = accessPartition.getMode().getValue().intValue();
                        ++i;                        
                        qry.setInt(i, mode);
                        //System.out.println(mode);

                        ++i;                        
                        final java.sql.Clob clob = arte.getDbConnection().createTemporaryClob();
                        clob.setString(1, accessPartition.getPartitionsAsStr());
                        qry.setClob(i, clob);
                        //System.out.println(accessPartition.getPartitionsAsStr());
                    }
                    qry.execute();
                    return qry.getLong(1) != 0;
                } finally {
                    for (java.sql.Clob clob : tmpClobs) {
                        arte.getDbConnection().freeTemporaryClob(clob);
                    }

                    qry.close();
                }
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
    
    //gup_4PQ4U65VK5HFVJ32XCUORBKRJM
    protected static String getMethodName(final Id familyId) {
        return "gup_" + familyId.toString().substring(3);
        
    }
    
    protected static ArrStr getUboundedPartitionsList(final Arte arte, final Id familyId) {
   try {
            final String qrySql = "begin ? := RDX_ACS_UTILS." + getMethodName(familyId) + "(RDX_ARTE.getUserName()); end;";

            final CallableStatement qry;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qry = arte.getDbConnection().get().prepareCall(qrySql);
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                try {
                    qry.registerOutParameter(1, java.sql.Types.CLOB);
                    qry.execute();
                    final Clob clob = qry.getClob(1);
                    if (clob == null) {
                        return null;
                    }
                    final String partitions = clob.getSubString(1, (int)clob.length());
                    if (Utils.emptyOrNull(partitions)) {
                        return null;
                    }
                    return ArrStr.fromValAsStr(partitions);
                } finally {
                    qry.close();
                }
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            }
        } catch (SQLException e) {
            throw new DatabaseError(ERR_CANT_CHECK_USER_RIGHTS_ + ExceptionTextFormatter.getExceptionMess(e), e);
        }
    }
            
        
    protected static String calcRealPartition(final String rawData) {
        if (rawData == null || rawData.length()<30) {
            return null;
        }
        String s = rawData.substring(30);
        s = s.replace("\\\\", "\\");
        s = s.replace("\\~", "~");
        s = s.replace("\\r", "\r");
        s = s.replace("\\n", "\n");
        s = s.replace("\\t", "\t");
        s = s.replace("\\ ", " ");        
        return s;
    }

    
    protected static boolean curUserHasRightsOnPartitionGroup(final Arte arte, final Id familyId, final String groupPartitions) {
        if (Utils.emptyOrNull(groupPartitions)) {//empty partition group
            return true;
        }
        
        final ArrStr uboundedPartitionsList = getUboundedPartitionsList(arte, familyId);
        
        final ArrStr partitions = ArrStr.fromValAsStr(groupPartitions);        
        for (String rawPartition : partitions) {
            final String partition = calcRealPartition(rawPartition);                
            if (uboundedPartitionsList == null || !uboundedPartitionsList.contains(partition)) {
                return false;
            }
        }        
        return true;
    } 
    
    protected static void close(final Statement qry) {
        if (qry != null) {
            try {
                qry.close();
            } catch (SQLException ex) {
                DebugUtils.suppressException(ex);
            }
        }
    }
    
    protected static AccessPartition createAccessPartition(final Arte arte, final Id familyId, 
            final EAccessAreaMode mode, final String singlePartitionVal, final Integer partitionGroupId) {
        if (EAccessAreaMode.UNBOUNDED == mode) {
            return AccessPartition.Factory.createForUnbounded();
        }
        if (EAccessAreaMode.BOUNDED_BY_PART == mode) {
            return AccessPartition.Factory.createForBoundedByPart(singlePartitionVal);
        }
        if (EAccessAreaMode.PROHIBITED == mode) {
            return AccessPartition.Factory.createForProhibited();
        }
        if (EAccessAreaMode.BOUNDED_BY_GROUP == mode) {
            if (partitionGroupId == null) {
                return AccessPartition.Factory.createForProhibited();
            }
            return AccessPartition.Factory.createForBoundedByGroup(arte, partitionGroupId);
        }
        if (EAccessAreaMode.BOUNDED_BY_USER == mode) {
            return AccessPartition.Factory.createForBoundedByUser(arte, familyId);
        }
        throw new org.radixware.kernel.common.exceptions.RadixError("Invalid AccessAreaMode " + String.valueOf(mode == null ? null : mode.getValue()));
    }

}
