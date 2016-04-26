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

package org.radixware.kernel.common.builder.check.dds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.radixware.kernel.common.builder.check.common.ModuleChecker;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.HierarchyWalker;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitions;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.repository.Layer;

import org.radixware.kernel.common.repository.Modules;
import org.radixware.kernel.common.rights.SystemTablesSearcher;
import org.radixware.kernel.common.types.Id;


@RadixObjectCheckerRegistration
public class DdsModuleChecker<T extends DdsModule> extends ModuleChecker<T> {

    private static Id DDS_ACS_MODULE_ID = Id.Factory.loadFrom("mdlOB5NQX24YXNBDGMBABQAQH3XQ4");

    public DdsModuleChecker() {
    }

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return DdsModule.class;
    }

    @Override
    public void check(T module, IProblemHandler problemHandler) {
        super.check(module, problemHandler);

        try {
            module.getModelManager().getModel();
            if (module.getId().equals(DDS_ACS_MODULE_ID)) {
                checkAcs(module, problemHandler);
            }

        } catch (IOException cause) {
            error(module, problemHandler, "Unable to load DDS model: " + cause.getMessage());
        }
    }

    // RADIX-1441
    private void checkAcs(DdsModule module, IProblemHandler problemHandler) {
        final List<DdsAccessPartitionFamilyDef> apfList = new ArrayList<>(0);
        DdsTableDef u2r = null;
        DdsTableDef ug2r = null;
        Layer layer = module.getSegment().getLayer();


//        while (true) {
        Layer.HierarchyWalker.walk(layer, new Layer.HierarchyWalker.Acceptor<Object>() {

            @Override
            public void accept(HierarchyWalker.Controller<Object> controller, Layer layer) {
                Modules<? extends Module> modules = layer.getDds().getModules();
                for (Module m : modules) {
                    DdsModule ddsModule = (DdsModule) m;
                    DdsModelDef model = ddsModule.getModelManager().findModel();
                    for (DdsAccessPartitionFamilyDef apf : model.getAccessPartitionFamilies()) {
                        if (!apfList.contains(apf)) {
                            apfList.add(apf);
                        }
                    }
                }
            }
        });
        //modules
        DdsModelDef model = module.getModelManager().findModel();
        DdsDefinitions<DdsTableDef> tables = model.getTables();

        for (DdsTableDef tbl : tables) {
            if (SystemTablesSearcher.USER2ROLE_ID.equals(tbl.getId())) {
                u2r = tbl;
                if (ug2r != null) {
                    break;
                }
            } else if (SystemTablesSearcher.USERGROUP2ROLE_ID.equals(tbl.getId())) {
                ug2r = tbl;
                if (u2r != null) {
                    break;
                }
            }
        }
        //      }
        if (u2r == null) {
            error(module, problemHandler, "System table not found \'user2role\'");
        } else {
            checkAcsTable(u2r, problemHandler, apfList);
        }
        if (ug2r == null) {
            error(module, problemHandler, "System table not found \'userGroup2role\'");
        } else {
            checkAcsTable(ug2r, problemHandler, apfList);
        }
    }

    private void checkAcsTable(DdsTableDef tbl, IProblemHandler problemHandler, List<DdsAccessPartitionFamilyDef> apfList) {

        List<Boolean> findKeys = new ArrayList<>(apfList.size());
        List<Boolean> findModes = new ArrayList<>(apfList.size());
        for (int i = 0; i < apfList.size(); i++) {
            findKeys.add(Boolean.FALSE);
            findModes.add(Boolean.FALSE);
        }
        boolean isMustReCreate = false;
        loop:
        for (DdsColumnDef col : tbl.getColumns().get(EScope.ALL)) {
            if (col.getDbName().length() == 30 && col.getDbName().substring(2, 4).equals("$$")) {
                List<Boolean> x;
                switch (col.getDbName().substring(0, 1)) {
                    case "M":
                        x = findModes;
                        break;
                    case "P":
                        x = findKeys;
                        break;
                    default:
                        isMustReCreate = true;
                        break loop;
                }
                Id id = Id.Factory.loadFrom(EDefinitionIdPrefix.DDS_ACCESS_PARTITION_FAMILY.getValue()
                        + col.getDbName().substring(4));
                boolean isFind = false;
                for (int i = 0; i < x.size(); i++) {
                    if (apfList.get(i).getId().equals(id)) {
                        x.set(i, Boolean.TRUE);
                        isFind = true;
                        break;
                    }
                }
                if (!isFind) {
                    isMustReCreate = true;
                    break;
                }
            }
        }
        if (!isMustReCreate) {
            for (int i = 0; i < findKeys.size(); i++) {
                if (!findKeys.get(i) || !findModes.get(i)) {
                    isMustReCreate = true;
                    break;
                }
            }
        }
        if (isMustReCreate) {
            error(tbl, problemHandler, "Access control system not synchronized.");
        }


    }
}
