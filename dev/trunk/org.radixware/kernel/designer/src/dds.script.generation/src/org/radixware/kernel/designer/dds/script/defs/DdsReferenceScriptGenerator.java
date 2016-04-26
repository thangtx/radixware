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

import java.io.IOException;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.ColumnsInfoItem;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;

/**
 * {@linkplain DdsReferenceDef} Script Generator.
 *
 */
public class DdsReferenceScriptGenerator extends DdsConstraintScriptGenerator<DdsReferenceDef> {

    protected DdsReferenceScriptGenerator() {
    }

    private DdsModelDef getActualModel(DdsModule module) {
        try {
            return module.getModelManager().getModel();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private DdsModelDef getFixedModel(DdsModule module) {
        try {
            return module.getModelManager().getFixedModel();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private DdsModelDef getModelForOldDefinition(DdsModule module) {
        final DdsModelDef actualModel = getActualModel(module);
        final DdsModelDef fixedModel = getFixedModel(module);
        if (fixedModel != null && actualModel != fixedModel) {
            return fixedModel;
        } else {
            return actualModel;
        }
    }

    private boolean isOld(DdsReferenceDef ref) {
        final DdsModelDef ownerModel = ref.getOwnerModel();
        final DdsModule module = ownerModel.getModule();
        final DdsModelDef actualModel = getActualModel(module);
        return ownerModel != actualModel;
    }

    @Override
    protected DdsTableDef findOwnerTable(DdsReferenceDef ref) {
        if (isOld(ref)) {
            // RADIX-1996
            final Id childTableId = ref.getChildTableId();
            Layer layer = ref.getModule().getSegment().getLayer();

            return Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<DdsTableDef>() {

                @Override
                public void accept(HierarchyWalker.Controller<DdsTableDef> controller, Layer layer) {
                    final DdsSegment segment = (DdsSegment) layer.getDds();
                    for (DdsModule module : segment.getModules()) {
                        final DdsModelDef oldModel = getModelForOldDefinition(module);
                        final DdsTableDef childTable = oldModel.getTables().findById(childTableId);
                        if (childTable != null) {
                            controller.setResultAndStop(childTable);
                            return;
                        }
                        final DdsViewDef childView = oldModel.getViews().findById(childTableId);
                        if (childView != null) {
                            controller.setResultAndStop(childView);
                            return;
                        }
                    }
                }
            });
        } else {
            return ref.findChildTable(ref);
        }
    }

    public boolean isModifiedToDrop(DdsReferenceDef.ColumnsInfoItem oldColumnsInfoItem, DdsReferenceDef.ColumnsInfoItem newColumnsInfoItem) {
        Id oldChildColumnId = oldColumnsInfoItem.getChildColumnId();
        Id newChildColumnId = newColumnsInfoItem.getChildColumnId();
        if (!oldChildColumnId.equals(newChildColumnId)) {
            return true;
        }
        Id oldParentColumnId = oldColumnsInfoItem.getParentColumnId();
        Id newParentColumnId = newColumnsInfoItem.getParentColumnId();
        if (!oldParentColumnId.equals(newParentColumnId)) {
            return true;
        }
        return false;
    }

    // Reference must be dropped if its structure was changed (except renaming, and renaming of columns and tables).
    @Override
    public boolean isModifiedToDrop(DdsReferenceDef oldReference, DdsReferenceDef newReference) {
        DdsTableDef oldTbl = findOwnerTable(oldReference);
        DdsTableDef newTbl = findOwnerTable(newReference);
        DdsTableScriptGenerator tableScriptGenerator = DdsTableScriptGenerator.Factory.newInstance();
        if (oldTbl == null || tableScriptGenerator.isModifiedToDrop(oldTbl, newTbl)) {
            return true;
        }

        EDeleteMode oldDeleteMode = oldReference.getDeleteMode();
        EDeleteMode newDeleteMode = newReference.getDeleteMode();
        if (oldDeleteMode == EDeleteMode.RESTRICT) {
            oldDeleteMode = EDeleteMode.NONE;
        }
        if (newDeleteMode == EDeleteMode.RESTRICT) {
            newDeleteMode = EDeleteMode.NONE;
        }
        if (oldDeleteMode != newDeleteMode) {
            return true;
        }

        if (super.isModifiedToDrop(oldReference, newReference)) {
            return true;
        }
        final Id oldChildTableId = oldReference.getChildTableId();
        final Id newChildTableId = newReference.getChildTableId();
        if (!oldChildTableId.equals(newChildTableId)) {
            return true;
        }

        final Id oldParentKeyConstraintId = oldReference.getParentUnuqueConstraintId();
        final Id newParentKeyConstraintId = newReference.getParentUnuqueConstraintId();

        if (!Utils.equals(oldParentKeyConstraintId, newParentKeyConstraintId)) {
            return true;
        }
        RadixObjects<ColumnsInfoItem> oldColumnsInfoItems = oldReference.getColumnsInfo();
        RadixObjects<ColumnsInfoItem> newColumnsInfoItems = newReference.getColumnsInfo();
        int size = oldColumnsInfoItems.size();
        if (newColumnsInfoItems.size() != size) {
            return true;
        }
        for (int i = 0; i < size; i++) {
            ColumnsInfoItem oldColumnsInfoItem = oldColumnsInfoItems.get(i);
            ColumnsInfoItem newColumnsInfoItem = newColumnsInfoItems.get(i);
            if (isModifiedToDrop(oldColumnsInfoItem, newColumnsInfoItem)) {
                return true;
            }
        }

        // расхождения в структуре дочерних колонок потребуют такую же структуру родительских колонок, а это повлечет смену ParentUniqueConstraint.Id
        return false;
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsReferenceDef reference, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(reference, cp);
        }

        DdsTableDef childTable = reference.getChildTable(reference);
        DdsTableDef parentTable = reference.getParentTable(reference);

        String childColumns = "";
        String parentColumns = "";

        for (DdsReferenceDef.ColumnsInfoItem columnsInfoItem : reference.getColumnsInfo()) {
            if (!childColumns.isEmpty()) {
                childColumns += ", ";
                parentColumns += ", ";
            }
            childColumns += columnsInfoItem.getChildColumn().getDbName();
            parentColumns += columnsInfoItem.getParentColumn().getDbName();
        }

        cp.print("alter ");
        if (childTable instanceof DdsViewDef) {
            cp.print("view ");
        } else {
            cp.print("table ");
        }

        cp.println(childTable.getDbName());
        cp.print('\t');
        cp.print("add constraint ");
        cp.print(reference.getDbName());
        cp.print(" foreign key (");
        cp.print(childColumns);
        cp.println(")");
        cp.print("\treferences ");
        cp.print(parentTable.getDbName());
        cp.print(" (");
        cp.print(parentColumns);
        cp.print(")");

        switch (reference.getDeleteMode()) {
            case CASCADE:
                cp.print(" on delete cascade");
                break;
            case SET_NULL:
                cp.print(" on delete set null");
                break;
        }

        getCreateDbOptionsScript(cp, reference.getDbOptions());

        cp.printCommandSeparator();
    }

    public static void getEnableDisableScript(DdsReferenceDef reference, CodePrinter cp, boolean enable) {
        DdsTableDef childTable = reference.getChildTable(reference);

        cp.print("alter ");
        if (childTable instanceof DdsViewDef) {
            cp.print("view ");
        } else {
            cp.print("table ");
        }

        cp.println(childTable.getDbName());
        cp.print('\t');
        if (enable) {
            cp.print("enable");
        } else {
            cp.print("disable");
        }
        cp.print(" constraint ");
        cp.print(reference.getDbName());
        cp.printCommandSeparator();
    }

    @Override
    public void getRunRoleScript(CodePrinter printer, DdsReferenceDef definition) {

    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsReferenceScriptGenerator newInstance() {
            return new DdsReferenceScriptGenerator();
        }
    }
}
