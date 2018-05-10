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

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.TablespaceCalculator;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.svn.client.FileUtils;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.DdsScriptInternalUtils;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;
import org.radixware.kernel.designer.dds.script.ScriptGeneratorImpl;

public class OraDdsTableScriptGenerator implements IDdsDefinitionScriptGenerator<DdsTableDef> {

    protected OraDdsTableScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsTableDef table) {
        cp.print("drop table ").print(table.getDbName()).print(" cascade constraints").printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(final DdsTableDef oldTable, final DdsTableDef newTable) {
        if (oldTable == null) {
            throw new IllegalArgumentException("Old table definition can't be null");
        }
        else if (newTable == null) {
            throw new IllegalArgumentException("New table definition can't be null");
        }
        else {
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
            return false;
        }
    }

    private boolean isPartitionsChanged(DdsTableDef oldTable, DdsTableDef newTable) {
        Sqml oldPartition = oldTable.getPartition();
        Sqml newPartition = newTable.getPartition();
        return !DdsScriptGeneratorUtils.isTranslatedSqmlEquals(oldPartition, newPartition);
    }

    private static void printPartitionScript(CodePrinter cp, DdsTableDef table) {
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
        getCreateScriptImpl(cp, table, handler, false, false);
    }

    private String getCreateScriptImpl(CodePrinter cp, DdsTableDef table, IScriptGenerationHandler handler, boolean createIntermediateTable, boolean alwaysCreateInDb) {
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

        OraDdsColumnScriptGenerator columnScriptGenerator = new OraDdsColumnScriptGenerator();

        cp.println('(');
        boolean columnFlag = false;
        for (DdsColumnDef column : table.getColumns().getLocal()) {
            if (column.isGeneratedInDb() || alwaysCreateInDb) {
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
            cp.print("\n\ttablespace ").print(tablespace);
        }
        cp.printCommandSeparator();
        if (!createIntermediateTable) {
            getRunRoleScript(cp, table);
        }
        return dbName;
    }

    @Override
    public void getRunRoleScript(CodePrinter cp, DdsTableDef table) {
        if (table.isGeneratedInDb()) {
            cp.print("grant select, update, insert, delete on ");
        } else {//special case for meta tables
            cp.print("grant select on ");
        }
        cp.print(table.getDbName()).print(" to &USER&_RUN_ROLE").printCommandSeparator();
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsTableDef oldTable, final DdsTableDef newTable) {
        if (isPartitionsChanged(oldTable, newTable)) {
            //при изменении параметров партиционирования требуется пересоздание таблицы
            Set<DdsReferenceDef> incomingRefs = DdsScriptGeneratorUtils.printDisconnectIncomingReferences(newTable, cp);
            String intermediateName = printRenameToIntermediateScript(oldTable, cp);

            getCreateScript(cp, newTable, IScriptGenerationHandler.NOOP_HANDLER);

            printCopyDataFromIntermediateScript(newTable, intermediateName, cp, false);

            final ScriptGenerator instance = ScriptGeneratorImpl.Factory.newCreationInstance(newTable);

            instance.generateModificationScript(cp, Collections.singleton(newTable));

            DdsScriptGeneratorUtils.printConnectIncomingReferences(incomingRefs, cp);
        } else {
            String oldDbName = oldTable.getDbName();
            String newDbName = newTable.getDbName();
            boolean rename = !oldDbName.equals(newDbName);
            if (rename) {
                cp.print("rename ").print(oldDbName).print(" to ").print(newDbName).printCommandSeparator();
            }

            String oldTablespace = TablespaceCalculator.calcTablespaceForTable(oldTable);
            String newTablespace = TablespaceCalculator.calcTablespaceForTable(newTable);
            if (!oldTablespace.equals(newTablespace) && !newTablespace.isEmpty()) {
                cp.print("alter table ").print(newDbName).print(" move tablespace ").print(newTablespace).printCommandSeparator();
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
        printer.print("rename ").print(table.getDbName()).print(" to ").print(tmpName).printCommandSeparator();
        return tmpName;
    }

    public static void printCopyDataFromIntermediateScript(final DdsTableDef table, final String from, final CodePrinter printer, final boolean alwaysCreateInDb) {
        try(final InputStream is = OraDdsTableScriptGenerator.class.getResourceAsStream("copyContent.txt");) {
            printer.print(DdsScriptInternalUtils.substitute(FileUtils.readTextStream(is,"UTF-8"),"from",from,"to",table.getDbName(),"colList",DdsScriptInternalUtils.selectColumnList(table, alwaysCreateInDb)));
        } catch (IOException ex) {
            throw new IllegalArgumentException("I/O exception processing template file: "+ex.getMessage());
        }
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsTableDef definition, boolean storeData) {
        printReCreateScript(definition,storeData, printer);
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsTableDef definition, boolean enable) {
    }
    
    private void printReCreateScript(final DdsTableDef table, final boolean storeData, final CodePrinter printer) {


        final ScriptGenerator instance = ScriptGeneratorImpl.Factory.newCreationInstance(table);
        //disable foreign key constraints   
        final String tmpName;
        final Set<DdsReferenceDef> incomingRefs = printDisconnectIncomingReferences(instance,table, printer);
        final Set<DdsReferenceDef> outgoingRefs = printDisconnectOutgoingReferences(instance,table, printer);

        if (storeData) {
            tmpName = printRenameToIntermediateScript(table, printer);
        } else {
            tmpName = null;
        }

        IDdsDefinitionScriptGenerator generator = instance.getDefinitionScriptGenerator(table);

        if (!storeData) {
            generator.getDropScript(printer, table);
        }

        getCreateScriptImpl(printer, table, IScriptGenerationHandler.NOOP_HANDLER, false, true);
        if (storeData) {
            printCopyDataFromIntermediateScript(table, tmpName, printer, true);
        }

        instance.generateModificationScript(printer, Collections.singleton(table));

        printConnectIncomingReferences(instance,incomingRefs, printer);
        printConnectOutgoingReferences(instance,outgoingRefs, printer);

    }

    private static void printConnectIncomingReferences(ScriptGenerator instance, Set<DdsReferenceDef> incomingRefs, CodePrinter printer) {
        if (incomingRefs != null) {
            for (DdsReferenceDef ref : incomingRefs) {
                instance.getDefinitionScriptGenerator(ref).getEnableDisableScript(printer, ref, true);
            }
        }
    }

    private static void printConnectOutgoingReferences(ScriptGenerator instance, Set<DdsReferenceDef> incomingRefs, CodePrinter printer) {
        if (incomingRefs != null) {
            for (DdsReferenceDef ref : incomingRefs) {
                instance.getDefinitionScriptGenerator(ref).getCreateScript(printer, ref, IScriptGenerationHandler.NOOP_HANDLER);
            }
        }
    }
    
    private static Set<DdsReferenceDef> printDisconnectIncomingReferences(ScriptGenerator instance, DdsTableDef table, CodePrinter printer) {
        final Set<DdsReferenceDef> incomingRefs = table.collectIncomingReferences();
        for (DdsReferenceDef ref : incomingRefs) {
            instance.getDefinitionScriptGenerator(ref).getEnableDisableScript(printer, ref, false);
        }
        return incomingRefs;
    }

    private static Set<DdsReferenceDef> printDisconnectOutgoingReferences(ScriptGenerator instance, DdsTableDef table, CodePrinter printer) {
        final Set<DdsReferenceDef> outgoinfRefs = table.collectOutgoingReferences();
        for (DdsReferenceDef ref : outgoinfRefs) {
            instance.getDefinitionScriptGenerator(ref).getEnableDisableScript(printer, ref, false);
        }
        return outgoinfRefs;
    }

    public static final class Factory {

        private Factory() {
        }

        public static OraDdsTableScriptGenerator newInstance() {
            return new OraDdsTableScriptGenerator();
        }
    }
}
