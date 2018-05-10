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

package org.radixware.kernel.designer.dds.script.defs.oracle;

import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.types.Id;


public class OraUserPropTriggersUpdater {

    private final DdsTableDef table;
    private static final boolean FOR_EACH_ROW = Boolean.TRUE;
    private static final Id TBL_SYSTEM_ID = Id.Factory.loadFrom("tblX5TD7JDVVHWDBROXAAIT4AGD7E");

    public OraUserPropTriggersUpdater(final DdsTableDef table) {
        this.table = table;
    }

    private DdsTriggerDef findOrCreateTrigger(final String triggerName, final String triggerDbName, final boolean forEachRow) {
        DdsTriggerDef trigger = table.getTriggers().findByDbName(triggerDbName);
        if (trigger == null) {
            trigger = DdsTriggerDef.Factory.newInstance(triggerName);
            table.getTriggers().getLocal().add(trigger);

            trigger.setType(DdsTriggerDef.EType.FOR_USER_PROPS);
            trigger.setAutoDbName(false);
            trigger.setDbName(triggerDbName); // see DdsTriggerDef.isUserPropTrigger()
            trigger.setActuationTime(DdsTriggerDef.EActuationTime.AFTER);
            trigger.getTriggeringEvents().add(DdsTriggerDef.ETriggeringEvent.ON_DELETE);
            trigger.setForEachRow(forEachRow);
        }
        return trigger;
    }

    private void deleteTrigger(final String triggerDbName) {
        DdsTriggerDef trigger = table.getTriggers().findByDbName(triggerDbName);
        if (trigger != null) {
            trigger.delete();
        }
    }

    private void update(final String triggerName, final String triggerDbName, final String triggerBodySql, final boolean forEachRow, final boolean mustExist) {
        if (mustExist) {
            DdsTriggerDef trigger = findOrCreateTrigger(triggerName, triggerDbName, forEachRow);
            trigger.getBody().setSql(triggerBodySql);
        } else {
            deleteTrigger(triggerDbName);
        }
    }

    public void update() {
        final boolean supportAndUsedAsObj = // предотвращение мутации триггеров
                table.getExtOptions().contains(EDdsTableExtOption.SUPPORT_USER_PROPERTIES)
                && table.getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT);

        final boolean supportAndNotUsedAsObj =
                table.getExtOptions().contains(EDdsTableExtOption.SUPPORT_USER_PROPERTIES)
                && !table.getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT);

        final Id tableId = table.getId();
        final String tableIdAsStr = tableId.toString();
        final boolean isSystemTable = TBL_SYSTEM_ID.equals(tableId);
        final String postfix = tableIdAsStr.substring(3);

        update("UserPropOwnSched", "UPS_" + postfix, // OWN_SCHED
                "begin\n   RDX_ENTITY.ScheduleUserPropOnDelOwner('" + tableIdAsStr + "', " + table.getPidScript(":old") + ");\nend;",
                FOR_EACH_ROW,
                !isSystemTable && supportAndUsedAsObj);

        update("UserPropOwnFlush", "UPF_" + postfix, // OWN_FLUSH
                "begin\n   RDX_ENTITY.flushUserPropOnDelOwner('" + tableIdAsStr + "');\nend;",
                !FOR_EACH_ROW,
                !isSystemTable && supportAndUsedAsObj);

        update("UserPropOwn", "UPO_" + postfix, // OWN
                "begin\n   RDX_ENTITY.UserPropOnDelOwner('" + tableIdAsStr + "', " + table.getPidScript(":old") + ");\nend;",
                FOR_EACH_ROW,
                !isSystemTable && supportAndNotUsedAsObj);

        update("UserPropValue", "UPV_" + postfix,
                generateCheckUserConstraintsTrigger(),
                FOR_EACH_ROW,
                !isSystemTable && table.getExtOptions().contains(EDdsTableExtOption.CHECK_USER_CONSTRAINTS_ON_DELETION));
    }

    private String getThisTableId() {
        return table.getId().toString();
    }

    private String getThisTablePid() {
        return table.getPidScript(":old");
    }

    private String generateCheckUserConstraintsTrigger() {
        final CodeBuilder cb = new CodeBuilder();
        cb.append("begin");
        cb.append("    RDX_ENTITY.userPropOnDelValue('" + getThisTableId() + "', " + getThisTablePid() + ");");
        cb.append("end;");
        return cb.toString();
    }

    public static void update(DdsTableDef table) {
        final OraUserPropTriggersUpdater updater = new OraUserPropTriggersUpdater(table);
        updater.update();
    }

    private static class CodeBuilder {

        private final StringBuilder sb = new StringBuilder();
        private int level = 0;
        private final String levelSpace = "    ";

        private void append(final String string) {
            for (int i = 0; i < level; i++) {
                sb.append(levelSpace);
            }
            sb.append(string);
            sb.append("\n");
        }

        public void shiftRight() {
            level++;
        }

        public void shiftLeft() {
            if (level > 0) {
                level--;
            }
        }

        public void emptyLine() {
            sb.append("\n");
        }

        @Override
        public String toString() {
            return sb.toString();
        }
    }
}
