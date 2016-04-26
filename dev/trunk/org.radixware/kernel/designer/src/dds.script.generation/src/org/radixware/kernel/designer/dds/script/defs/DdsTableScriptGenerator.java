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
package org.radixware.kernel.designer.dds.script.defs;

import java.util.Collections;
import java.util.Set;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;

public class DdsTableScriptGenerator implements IDdsDefinitionScriptGenerator<DdsTableDef> {

    protected DdsTableScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsTableDef table) {
        cp.print("drop table ");
        cp.print(table.getDbName());
        cp.print(" cascade constraints");
        cp.printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsTableDef oldTable, DdsTableDef newTable) {
        boolean oldGlobalTemporaty = oldTable.isGlobalTemporary();
        boolean newGlobalTemporary = newTable.isGlobalTemporary();
        if (oldGlobalTemporaty != newGlobalTemporary) {
            return true;
        }

        if (newGlobalTemporary) {
            boolean oldOnCommitPreserveRows = oldTable.isOnCommitPreserveRows();
            boolean newOnCommitPreserveRows = newTable.isOnCommitPreserveRows();
            if (oldOnCommitPreserveRows != newOnCommitPreserveRows) {
                return true;
            }
        }

//      
        return false;
    }

    private boolean isPartitionsChanged(DdsTableDef oldTable, DdsTableDef newTable) {
        Sqml oldPartition = oldTable.getPartition();
        Sqml newPartition = newTable.getPartition();
        if (!DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldPartition, newPartition)) {
            return true;
        }
        return false;
    }

    private void printPartitionScript(CodePrinter cp, DdsTableDef table) {
        Sqml partition = table.getPartition();
        String partitionScript = DdsScriptGeneratorUtils.translateSqml(partition);

        if (partitionScript != null && !partitionScript.isEmpty()) {
            cp.println();
            DdsScriptGeneratorUtils.printPartitioningIf(cp);
            String partitionLines[] = partitionScript.split("\\n");
            boolean partitonLineFlag = false;
            for (String partitonLine : partitionLines) {
                if (partitonLineFlag) {
                    cp.print("\n\t\t");
                } else {
                    cp.print("\n\tpartition ");
                    partitonLineFlag = true;
                }
                cp.print(partitonLine);
            }
            cp.println();
            DdsScriptGeneratorUtils.printEndIf(cp);
        }
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsTableDef table, IScriptGenerationHandler handler) {
        getCreateScriptImpl(cp, table, handler, false);
    }

    private String getCreateScriptImpl(CodePrinter cp, DdsTableDef table, IScriptGenerationHandler handler, boolean createIntermediateTable) {
        if (handler != null) {
            handler.onGenerationStarted(table, cp);
        }

        cp.print("create");

        boolean isGlobalTemporary = table.isGlobalTemporary();
        if (isGlobalTemporary) {
            cp.print(" global temporary");
        }
        cp.print(" table ");

        String dbName = createIntermediateTable ? table.getDbName() + "_INT_" + org.radixware.kernel.common.types.Id.Factory.newInstance(EDefinitionIdPrefix.DDS_TABLE).toString() : table.getDbName();
        cp.print(table.getDbName());

        DdsColumnScriptGenerator columnScriptGenerator = new DdsColumnScriptGenerator();

        cp.println('(');
        boolean columnFlag = false;
        for (DdsColumnDef column : table.getColumns().getLocal()) {
            if (column.isGeneratedInDb()) {
                if (columnFlag) {
                    cp.println(',');
                } else {
                    columnFlag = true;
                }
                cp.print('\t');
                columnScriptGenerator.getColumnScript(cp, column);
            }
        }
        cp.print(')');

        printPartitionScript(cp, table);

        if (isGlobalTemporary) {
            if (table.isOnCommitPreserveRows()) {
                cp.print("\n\ton commit preserve rows");
            }
        }

        String tablespace = TablespaceCalculator.calcTablespaceForTable(table);
        if (!tablespace.isEmpty()) {
            cp.print("\n\ttablespace ");
            cp.print(tablespace);
        }
        cp.printCommandSeparator();
        if (!createIntermediateTable) {
            getRunRoleScript(cp, table);
        }
        return dbName;
    }

    @Override
    public void getRunRoleScript(CodePrinter cp, DdsTableDef table) {
        cp.print("grant select, update, insert, delete on ");
        cp.print(table.getDbName());
        cp.print(" to &USER&_RUN_ROLE");
        cp.printCommandSeparator();
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsTableDef oldTable, final DdsTableDef newTable) {
        if (isPartitionsChanged(oldTable, newTable)) {
            //при изменении параметров партиционирования требуется пересоздание таблицы
            Set<DdsReferenceDef> incomingRefs = printDisconnectIncomingReferences(newTable, cp);
            String intermediateName = printRenameToIntermediateScript(oldTable, cp);

            getCreateScript(cp, newTable, IScriptGenerationHandler.NOOP_HANDLER);

            printCopyDataFromIntermediateScript(newTable, intermediateName, cp);

            final ScriptGenerator instance = ScriptGenerator.Factory.newCreationInstance(newTable);

            instance.generateModificationScript(cp, Collections.singleton(newTable));

            printConnectIncomingReferences(incomingRefs, cp);
        } else {
            String oldDbName = oldTable.getDbName();
            String newDbName = newTable.getDbName();
            boolean rename = !oldDbName.equals(newDbName);
            if (rename) {
                cp.print("rename ");
                cp.print(oldDbName);
                cp.print(" to ");
                cp.print(newDbName);
                cp.printCommandSeparator();
            }

            String oldTablespace = TablespaceCalculator.calcTablespaceForTable(oldTable);
            String newTablespace = TablespaceCalculator.calcTablespaceForTable(newTable);
            if (!oldTablespace.equals(newTablespace) && !newTablespace.isEmpty()) {
                cp.print("alter table ");
                cp.print(newDbName);
                cp.print(" move tablespace ");
                cp.print(newTablespace);
                cp.printCommandSeparator();
            }
            if (rename) {
                getRunRoleScript(cp, newTable);
            }
        }

//        Sqml oldPartition = oldTable.getPartition();
//        Sqml newPartition = newTable.getPartition();
//        if (!DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldPartition, newPartition)) {
//            cp.print("alter table ");
//            cp.print(newDbName);
//            if (!newPartition.getItems().isEmpty()) {
//                printPartitionScript(cp, newTable);
//            } else {
//                cp.print(" drop partition");
//            }
//            cp.printCommandSeparator();
//        }
    }

    public static String printRenameToIntermediateScript(DdsTableDef table, CodePrinter printer) {
        String tmpName = table.getDbName() + "_INT";
        printer.print("rename ");
        printer.print(table.getDbName());
        printer.print(" to ");
        printer.print(tmpName);
        printer.printCommandSeparator();
        return tmpName;
    }

    private static Set<DdsReferenceDef> printDisconnectIncomingReferences(DdsTableDef table, CodePrinter printer) {
        final Set<DdsReferenceDef> incomingRefs = table.collectIncomingReferences();
        for (DdsReferenceDef ref : incomingRefs) {
            DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, false);
        }
        return incomingRefs;
    }

    private static Set<DdsReferenceDef> printDisconnectOutgoingReferences(DdsTableDef table, CodePrinter printer) {
        final Set<DdsReferenceDef> outgoinfRefs = table.collectOutgoingReferences();
        for (DdsReferenceDef ref : outgoinfRefs) {
            DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, false);
        }
        return outgoinfRefs;
    }

    private static void printConnectIncomingReferences(Set<DdsReferenceDef> incomingRefs, CodePrinter printer) {
        if (incomingRefs != null) {
            for (DdsReferenceDef ref : incomingRefs) {
                DdsReferenceScriptGenerator.getEnableDisableScript(ref, printer, true);
            }
        }
    }

    private static void printConnectOutgoingReferences(Set<DdsReferenceDef> incomingRefs, CodePrinter printer) {
        if (incomingRefs != null) {
            for (DdsReferenceDef ref : incomingRefs) {
                new DdsReferenceScriptGenerator().getCreateScript(printer, ref, IScriptGenerationHandler.NOOP_HANDLER);
            }
        }
    }

    public static void printCopyDataFromIntermediateScript(DdsTableDef table, String from, CodePrinter printer) {
        final String tableName = table.getDbName();
        final String cursorName = from + "_c";
        final String typeName = from + "_c_t";
        final String arrName = from + "_c_a";

        printer.println("declare");
        printer.enterBlock();
        printer.println("cursor " + cursorName + " is");
        printer.print("select ");
        boolean columnFlag = false;
        printer.enterBlock();
        for (DdsColumnDef column : table.getColumns().getLocal()) {
            if (column.isGeneratedInDb()) {
                if (columnFlag) {
                    printer.println(',');
                } else {
                    columnFlag = true;
                }
                printer.print(column.getDbName());
            }
        }
        printer.leaveBlock();
        printer.println();
        printer.println(" from " + from + ";");
        printer.println("type " + typeName + " is table of " + cursorName + "%rowtype;");
        printer.println(arrName + " " + typeName + ";");
        printer.leaveBlock();
        printer.println("begin");
        printer.enterBlock();
        printer.println("open " + cursorName + ";");
        printer.println("loop");
        printer.enterBlock();
        printer.println("fetch " + cursorName + " bulk collect into " + arrName + " limit 1000;");
        printer.println("forall i in " + arrName + ".First.." + arrName + ".Last");
        printer.enterBlock();
        printer.println("insert into " + tableName + " values " + arrName + "(i);");
        printer.leaveBlock();
        printer.println("commit;");
        printer.println("exit when " + cursorName + "%notfound;");
        printer.leaveBlock();
        printer.println("end loop;");
        printer.leaveBlock();
        printer.println("end;");
        printer.printCommandSeparator();

        printer.println("drop table " + from);
        printer.printCommandSeparator();

    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsTableScriptGenerator newInstance() {
            return new DdsTableScriptGenerator();
        }
    }

    public static void printReCreateScript(final DdsTableDef table, final boolean storeData, final CodePrinter printer) {

        final ScriptGenerator instance = ScriptGenerator.Factory.newCreationInstance(table);
        //disable foreign key constraints   
        final String tmpName;
        final Set<DdsReferenceDef> incomingRefs = printDisconnectIncomingReferences(table, printer);
        final Set<DdsReferenceDef> outgoingRefs = printDisconnectOutgoingReferences(table, printer);

        if (storeData) {
            tmpName = printRenameToIntermediateScript(table, printer);
        } else {
            tmpName = null;
        }

        IDdsDefinitionScriptGenerator generator = instance.getDefinitionScriptGenerator(table);

        if (!storeData) {
            generator.getDropScript(printer, table);
        }

        instance.getDefinitionScriptGenerator(table).getCreateScript(printer, table, IScriptGenerationHandler.NOOP_HANDLER);
        if (storeData) {
            printCopyDataFromIntermediateScript(table, tmpName, printer);
        }

        instance.generateModificationScript(printer, Collections.singleton(table));

        printConnectIncomingReferences(incomingRefs, printer);
        printConnectOutgoingReferences(outgoingRefs, printer);

    }
}
