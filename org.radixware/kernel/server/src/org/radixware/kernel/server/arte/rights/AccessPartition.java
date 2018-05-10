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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.enums.EAccessAreaMode;
import org.radixware.kernel.common.enums.ETimingSection;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.types.ArrStr;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.ExceptionTextFormatter;
import org.radixware.kernel.server.arte.Arte;
import static org.radixware.kernel.server.arte.rights.Rights.ERR_CANT_CHECK_USER_RIGHTS_;
import org.radixware.kernel.server.exceptions.DatabaseError;
import org.radixware.kernel.server.types.Entity;
import org.radixware.kernel.server.types.Pid;

public class AccessPartition {

    private static final Id PARTITION_GROUT_ID = Id.Factory.loadFrom("tblLNMBHO73GNE2LF5TXFR7QEMICM");
    private static final Id PARTITION_GROUT_PARTITION_PROP_ID = Id.Factory.loadFrom("colIDPIK7BQQREGBLDSYFORHOMRRQ");//Radix::Acs::PartitionGroup:partitions

    private final List<String> partitions;
    private final EAccessAreaMode mode;

    protected EAccessAreaMode getMode() {
        return mode;
    }

    protected String getPartitionsAsStr() {
        final ArrStr arr = new ArrStr(partitions);
        return arr.toString();
    }

    public static class Factory {

        public static AccessPartition createForUnbounded() {
            final List<String> partitions = new ArrayList<>(1);
            partitions.add(null);
            return new AccessPartition(partitions, EAccessAreaMode.UNBOUNDED);
        }

        public static AccessPartition createForBoundedByPart(final String partition) {
            final List<String> partitions = new ArrayList<>(1);
            partitions.add(partition);
            return new AccessPartition(partitions, EAccessAreaMode.BOUNDED_BY_PART);
        }

        public static AccessPartition createForProhibited() {
            final List<String> partitions = new ArrayList<>(1);
            partitions.add(null);
            return new AccessPartition(partitions, EAccessAreaMode.PROHIBITED);
        }

        public static AccessPartition createForBoundedByGroup(final Arte arte, final int groupId) {
            final Pid pid = new Pid(arte, PARTITION_GROUT_ID, String.valueOf(groupId));
            final Entity entityObject = arte.getEntityObject(pid);
            final Object partitions = entityObject.getProp(PARTITION_GROUT_PARTITION_PROP_ID);
            if (partitions == null) {
                return createForProhibited();
            }
            
            final Clob clob = (Clob) partitions;
            final ArrStr arr;

            try {
                arr = ArrStr.fromValAsStr(clob.getSubString(1, (int)clob.length()));
            } catch (SQLException ex) {
                throw new RadixError("Unable get partitions value", ex);
            }
            if (arr==null || arr.isEmpty()) {
                return createForProhibited();
            }
            
            final List<String> arrayList = new ArrayList<>();
            for (String part : arr) {
                arrayList.add(RightsUtils.calcRealPartition(part));
            }
            final AccessPartition ap = new AccessPartition(arrayList, EAccessAreaMode.BOUNDED_BY_PART);
            return ap;
        }

        public static AccessPartition createForBoundedByUser(final Arte arte, final Id familyId) {

            final String partition = calcUserAssignmentPartition(arte, familyId);
            return createForBoundedByPart(partition);
        }

    }

    private AccessPartition(final List<String> partitions, final EAccessAreaMode mode) {
        this.partitions = partitions;
        this.mode = mode;

        if (!validMode(mode)) {
            throw new RadixError("Invalid EAccessAreaMode mode");
        }
    }

    private static String calcUserAssignmentPartition(final Arte arte, final Id familyId) {
        try {
            final String userName = arte.getUserName();
            final String postfix = familyId.toString().substring(3);
            final String packageName = "ACS$" + postfix;
            final CallableStatement qry;
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            try {
                qry = arte.getDbConnection().get().prepareCall("begin ? := " + packageName + ".getUserAssignment(?); end;");
            } finally {
                arte.getProfiler().leaveTimingSection(ETimingSection.RDX_ARTE_DB_QRY_PREPARE);
            }
            arte.getProfiler().enterTimingSection(ETimingSection.RDX_ARTE_DB_QRY_EXEC);
            try {
                try {
                    qry.registerOutParameter(1, java.sql.Types.VARCHAR);
                    qry.setString(2, userName);
                    qry.execute();
                    return qry.getString(1);
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

    private static boolean validMode(final EAccessAreaMode mode) {
        return !EAccessAreaMode.BOUNDED_BY_GROUP.equals(mode)
                && !EAccessAreaMode.BOUNDED_BY_USER.equals(mode);
    }

}
