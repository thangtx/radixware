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
package org.radixware.kernel.common.rights;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinitionPlacement;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.IPlacementSupport;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProviderFactory;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.Reference;

public class SystemTablesBuilder {

    public static final Id DRC_MODULE_ID = Id.Factory.loadFrom("mdlOB5NQX24YXNBDGMBABQAQH3XQ4");
    private static final String USER2ROLE = "User2Role";
    private static final String USERGROUP2ROLE = "UserGroup2Role";
    private static final String PARTITIONGROUP = "PartitionGroup";
    protected static final int ConjecturalRectangleSizeX = 100;
    protected static final int ConjecturalRectangleSizeY = 180;
    
    private SystemTablesSearcher searcher;

    private DdsModelDef model = null;
    private DdsModule module = null;

    private final List<Point> pointList = new ArrayList<>(0);

    protected static class Item extends RadixObject {

        Item(final DdsAccessPartitionFamilyDef apf) {
            this.apf = apf;
        }
        final DdsAccessPartitionFamilyDef apf;
        DdsColumnDef columnMode = null;
        DdsColumnDef columnKey = null;
        DdsColumnDef columnPartGroupId = null;
        DdsIndexDef keyIndex = null;
        DdsReferenceDef keyRef = null;

        //DdsIndexDef partGroupIndex = null; // no need!
        DdsReferenceDef partGroupRef = null;

        @Override
        public String getName() {
            return apf.getName();
        }
    }

    protected static class IPlacementVisitorProvider extends DdsVisitorProvider {

        public IPlacementVisitorProvider(final List<Point> pointList) {
            this.pointList = pointList;
        }

        private final List<Point> pointList;

        @Override
        public boolean isTarget(final RadixObject obj) {
            if (obj instanceof IPlacementSupport) {
                final IPlacementSupport pls = (IPlacementSupport) obj;
                final DdsDefinitionPlacement placementDef = pls.getPlacement();
                pointList.add(new Point(placementDef.getPosX(), placementDef.getPosY()));
            }
            return false;
        }
    }

    SystemTablesBuilder(final DdsModule module) {
        if (module == null) {
            throw new DefinitionError("Dds module not defined ", this);
        }
        this.module = module;

    }

    private void searchSystemInstances() {
        searcher = new SystemTablesSearcher(module);

        if (searcher.getClerk2Role() == null) {
            throw new DefinitionError("Can't create access control service columns: overriding table for \"" + USER2ROLE + "\" is required but not found.", this);
        }
        if (searcher.getClerkGroup2Role() == null) {
            throw new DefinitionError("Can't create access control service columns: overriding table for \"" + USERGROUP2ROLE + "\" is required but not found.", this);
        }
        if (searcher.getIndexOverriteUser2Role() != searcher.getIndexOverriteUserGroup2Role() /*|| searcher.getIndexOverriteUser2Role() == 0*/) {
            throw new DefinitionError("Incorrect system overriding tables location.", this);
        }
        
        if (searcher.getPartitionGroup() == null) {
            throw new DefinitionError("Can't create access control service columns: overriding table for \"" + PARTITIONGROUP + "\" is required but not found.", this);
        }
        
    }

    private List<Item> createApfList() {
        final Collection<Definition> apfList2 = RadixObjectsUtils.collectAllAround(module, DdsVisitorProviderFactory.newAccessPartitionFamilyProvider());
        if (apfList2 == null || apfList2.isEmpty()) {
            throw new DefinitionError("Access partition families not found", this);
        }
        final List<Item> itemList = new ArrayList<>(apfList2.size());

        final Layer currLayer = this.module.getSegment().getLayer();
        for (Definition def : apfList2) {
            if (currLayer != def.getModule().getSegment().getLayer()) {
                continue;
            }
            //check dublicate
            final DdsAccessPartitionFamilyDef apf = (DdsAccessPartitionFamilyDef) def;
            for (Item def2 : itemList) {
                if (apf.findHead().equals(def2.apf.findHead())) {
                    throw new DefinitionError("Dublicate access partition family head", apf);
                }
            }

            itemList.add(new Item((DdsAccessPartitionFamilyDef) def));
        }
        RadixObjectsUtils.sortByName(itemList);
        return itemList;
    }

    private void getModel() {
        try {
            model = module.getModelManager().getModel();
        } catch (Exception e) {
            throw new DefinitionError("Model not found - " + e.toString(), this);
        }
    }
    public static void refresh(final DdsModule module) {

        final SystemTablesBuilder stBuilder = new SystemTablesBuilder(module);
        stBuilder.getModel();
        stBuilder.searchSystemInstances();
        stBuilder.reCreateColumns();
        stBuilder.reCreatePackages();
    }
    

    private void reCreateColumns() {
        createColumns(searcher.getClerk2Role());
        createColumns(searcher.getClerkGroup2Role());
    }


    private void reCreatePackages() {
        final List<Item> itemList = createApfList();
    }
    

    private void createColumns(final DdsTableDef table2Role) {
        final List<Item> itemList = createApfList();
        pointList.clear();
        final Reference<Boolean> findPoints = new Reference<>(false);

        for (Item item : itemList) {
            
            final DdsAccessPartitionFamilyDef apf = item.apf;
            final Definition apfTargetDef = apf.findHead();

            if (apfTargetDef == null) {
                throw new DefinitionError("Access partition family head not found", apf);
            }
            
            SystemTablesBuilderUtils.checkOrCreateColumnMode(item, table2Role, apf);
            SystemTablesBuilderUtils.checkOrCreateColumnKey(item, table2Role, apf, apfTargetDef);
            SystemTablesBuilderUtils.checkOrCreateColumnPartGroupId(item, table2Role, apf);

            SystemTablesBuilderUtils.checkOrCreateKeyIndex(item, table2Role, apfTargetDef);
            SystemTablesBuilderUtils.checkOrCreateKeyRef(model, searcher, pointList, module, item, apfTargetDef, findPoints, table2Role);
            SystemTablesBuilderUtils.checkOrCreatePartitionRef(model, searcher, pointList, module, item, findPoints, table2Role);
        }
    }
    
    
    
}
