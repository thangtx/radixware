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

package org.radixware.kernel.common.builder.test;

import java.io.IOException;
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsFieldRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParameterPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsProcedureClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.sqml.tags.ParameterTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.sqml.tags.TableSqlNameTag;


public class TestSqlClassFactory {

    /**
     * Fill specified SQL class by test data.
     * Place it in predefined test environment.
     * Synchronize fields and parameters.
     *
     * Generated select:
     *      Select TYPE, TITLE
     *      From TABLE
     *      Where ID=:pId and  and TYPE=:pType and true=:pCustom and ID in (:pId) and ID in (:pkParamItem)
     *
     * Columns:
     *      ID - int, pk
     *      TYPE - int, enumerated
     *      TITLE - str
     *
     * Fields:
     *      TYPE - int, enumerated
     *      cnt - int, custom
     *      TITLE - str, custom, typified by TITLE
     *
     * Parameters:
     *      pId - typified by ID
     *      pType - typified by TYPE
     *      pCustom - custom boolean parameter
     *      pId - PK parameter
     *      pkParamItem - its item
     */
    private static void fillAndPlaceSqlClass(final Branch testBranch, final AdsSqlClassDef sqlClass) throws IOException {
        final Layer layer = testBranch.getLayers().findTop().get(0);

        // =========== DDS ===========
        final DdsModule ddsModule = DdsModule.Factory.getDefault().newInstance("test");
        layer.getDds().getModules().add(ddsModule);

        final DdsModelDef model = ddsModule.getModelManager().getModel();

        // =========== TABLE ===========
        final DdsTableDef table = DdsTableDef.Factory.newInstance("table");
        model.getTables().add(table);
        sqlClass.getUsedTables().add(table, null);

        // =========== COLUMN ID ===========
        final DdsColumnDef columnId = DdsColumnDef.Factory.newInstance("ID");
        columnId.setNotNull(true);
        columnId.setDbName("ID");
        columnId.setValType(EValType.INT);
        table.getColumns().getLocal().add(columnId);

        // =========== PK ===========
        table.getPrimaryKey().getColumnsInfo().add(columnId, EOrder.ASC);

        // =========== COLUMN TYPE ===========
        final DdsColumnDef columnType = DdsColumnDef.Factory.newInstance("Type");
        columnType.setDbName("TYPE");
        columnType.setNotNull(true);
        columnType.setValType(EValType.INT);
        table.getColumns().getLocal().add(columnType);

        // =========== COLUMN TITLE ===========
        final DdsColumnDef columnTitle = DdsColumnDef.Factory.newInstance("Title");
        columnTitle.setDbName("TITLE");
        columnTitle.setValType(EValType.STR);
        table.getColumns().getLocal().add(columnTitle);

        // =========== ADS ===========
        final AdsModule adsModule = AdsModule.Factory.getDefault().newInstance("test");
        layer.getAds().getModules().add(adsModule);
        adsModule.getDependences().add(ddsModule);
        ddsModule.getDependences().add(adsModule);
        adsModule.getDefinitions().add(sqlClass);

        // =========== ENUM ETYPE ===========
        final AdsEnumDef eType = AdsEnumDef.Factory.newInstance();
        eType.setName("EType");
        eType.setItemType(EValType.INT);
        adsModule.getDefinitions().add(eType);

        // =========== AEC ===========
        final AdsEntityClassDef aec = AdsEntityClassDef.Factory.newInstance(table);
        adsModule.getDefinitions().add(aec);

        // =========== PROP ID ===========
        final AdsPropertyDef propColumnId = AdsInnateColumnPropertyDef.Factory.newInstance(columnId);
        aec.getProperties().getLocal().add(propColumnId);

        // =========== PROP TYPE ===========
        final AdsPropertyDef propColumnType = AdsInnateColumnPropertyDef.Factory.newInstance(columnType);
        aec.getProperties().getLocal().add(propColumnType);
        propColumnType.getValue().setType(AdsTypeDeclaration.Factory.newInstance(eType));

        // =========== PARAM ID ===========
        final AdsParameterPropertyDef paramById = AdsParameterPropertyDef.Factory.newInstance();
        paramById.setName("pId");
        paramById.getValue().setType(AdsTypeDeclaration.Factory.newInstance(columnId.getValType()));
        sqlClass.getProperties().getLocal().add(paramById);

        // =========== PARAM TYPE ===========
        final AdsParameterPropertyDef paramByType = AdsParameterPropertyDef.Factory.newInstance();
        paramByType.setName("pType");
        paramByType.getValue().setType(AdsTypeDeclaration.Factory.newInstance(eType));
        sqlClass.getProperties().getLocal().add(paramByType);

        // =========== CUSTOM PARAM ===========
        final AdsParameterPropertyDef customParam = AdsParameterPropertyDef.Factory.newInstance();
        customParam.setName("pCustom");
        customParam.getValue().setType(AdsTypeDeclaration.Factory.newInstance(EValType.BOOL));
        sqlClass.getProperties().getLocal().add(customParam);

        // =========== LITERAL PARAM ===========
        final AdsParameterPropertyDef literalParam = AdsParameterPropertyDef.Factory.newInstance();
        literalParam.setName("pLiteral");
        literalParam.getValue().setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
        sqlClass.getProperties().getLocal().add(literalParam);

        // =========== PK PARAM ===========
        final AdsParameterPropertyDef pkParam = AdsParameterPropertyDef.Factory.newInstance();
        pkParam.setName("pTable");
        pkParam.getValue().setType(AdsTypeDeclaration.Factory.newArrRef(aec));
        sqlClass.getProperties().getLocal().add(pkParam);

        // =========== SQML ===========
        final Sqml sqml = sqlClass.getSqml();

        // =========== SELECT ===========
        sqml.getItems().add(Sqml.Text.Factory.newInstance("select "));

        // =========== TYPE, ===========
        final PropSqlNameTag columnTypeTag = PropSqlNameTag.Factory.newInstance();
        columnTypeTag.setPropOwnerId(table.getId());
        columnTypeTag.setPropId(columnType.getId());
        sqml.getItems().add(columnTypeTag);

        sqml.getItems().add(Sqml.Text.Factory.newInstance(", "));

        // =========== TITLE ===========
        final PropSqlNameTag columnTitleTag = PropSqlNameTag.Factory.newInstance();
        columnTitleTag.setPropOwnerId(table.getId());
        columnTitleTag.setPropId(columnTitle.getId());
        sqml.getItems().add(columnTitleTag);

        // =========== FROM TABLE WHERE ===========
        final TableSqlNameTag tableTag = TableSqlNameTag.Factory.newInstance();
        tableTag.setTableId(table.getId());
        sqml.getItems().add(Sqml.Text.Factory.newInstance("\nfrom "));
        sqml.getItems().add(tableTag);

        sqml.getItems().add(Sqml.Text.Factory.newInstance("\nwhere "));

        // =========== ID= ===========
        final PropSqlNameTag columnIdTag = PropSqlNameTag.Factory.newInstance();
        columnIdTag.setPropOwnerId(table.getId());
        columnIdTag.setPropId(columnId.getId());
        sqml.getItems().add(columnIdTag);
        sqml.getItems().add(Sqml.Text.Factory.newInstance("="));

        // =========== :pID ===========
        final ParameterTag paramByIdTag = ParameterTag.Factory.newInstance();
        paramByIdTag.setParameterId(paramById.getId());
        sqml.getItems().add(paramByIdTag);

        // =========== and TYPE=:pType ===========
        sqml.getItems().add(Sqml.Text.Factory.newInstance(" and TYPE="));
        final ParameterTag paramByTypeTag = ParameterTag.Factory.newInstance();
        paramByTypeTag.setParameterId(paramByType.getId());
        sqml.getItems().add(paramByTypeTag);

        // =========== and true=:pCustom ===========
        sqml.getItems().add(Sqml.Text.Factory.newInstance(" and true="));
        final ParameterTag customParamTag = ParameterTag.Factory.newInstance();
        customParamTag.setParameterId(customParam.getId());
        sqml.getItems().add(customParamTag);

        // =========== and ID in (:pID) ===========
        sqml.getItems().add(Sqml.Text.Factory.newInstance(" and ID in "));
        final ParameterTag pkParamTag = ParameterTag.Factory.newInstance();
        pkParamTag.setParameterId(pkParam.getId());
        sqml.getItems().add(pkParamTag);

        // =========== and ID in (:pID.ID) ===========
        sqml.getItems().add(Sqml.Text.Factory.newInstance(" and ID in "));
        final ParameterTag pkParamItemTag = ParameterTag.Factory.newInstance();
        pkParamItemTag.setParameterId(pkParam.getId());
        pkParamItemTag.setPropId(propColumnId.getId());
        sqml.getItems().add(pkParamItemTag);

        // register
        final AdsDynamicClassDef pdcSqlClass = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
        final String platformClassName = (sqlClass instanceof AdsCursorClassDef ? AdsCursorClassDef.PLATFORM_CLASS_NAME : AdsSqlClassDef.PLATFORM_CLASS_NAME);
        pdcSqlClass.getTransparence().setPublishedName(platformClassName);
        final AdsTransparentMethodDef method = AdsTransparentMethodDef.Factory.newInstance();
        pdcSqlClass.getMethods().getLocal().add(method);
        method.getTransparence().setPublishedName("close()V");
        adsModule.getDefinitions().add(pdcSqlClass);

        if (sqlClass instanceof AdsCursorClassDef) {
            // fields
            final AdsFieldPropertyDef fieldType = AdsFieldPropertyDef.Factory.newInstance("TYPE");
            fieldType.getValue().setType(AdsTypeDeclaration.Factory.newInstance(eType));
            sqlClass.getProperties().getLocal().add(fieldType);

            final AdsFieldPropertyDef fieldTitle = AdsFieldPropertyDef.Factory.newInstance("TITLE");
            fieldTitle.getValue().setType(AdsTypeDeclaration.Factory.newInstance(EValType.STR));
            sqlClass.getProperties().getLocal().add(fieldTitle);

            // add field ref
            final AdsFieldRefPropertyDef fieldRef = AdsFieldRefPropertyDef.Factory.newInstance();
            sqlClass.getProperties().getLocal().add(fieldRef);
            fieldRef.getValue().setType(AdsTypeDeclaration.Factory.newParentRef(aec));
            final AdsFieldRefPropertyDef.RefMapItem refMapItem = AdsFieldRefPropertyDef.RefMapItem.Factory.newInstance();
            fieldRef.getFieldToColumnMap().add(refMapItem);
            refMapItem.setColumnId(columnId.getId());
            for (AdsPropertyDef genProp : sqlClass.getProperties().getLocal()) {
                if (genProp.getName().equals("TYPE")) {
                    refMapItem.setFieldId(propColumnType.getId());
                }
            }

            for (AdsPropertyDef field : sqlClass.getProperties().getLocal()) {
                field.setConst(false); // generate setter
            }
        }
    }

    public static AdsCursorClassDef newCursor(Branch testBranch) throws IOException {
        final AdsCursorClassDef cursor = AdsCursorClassDef.Factory.newInstance();
        fillAndPlaceSqlClass(testBranch, cursor);
        return cursor;
    }

    public static AdsProcedureClassDef newProcedure(Branch testBranch) throws IOException {
        final AdsProcedureClassDef procedure = AdsProcedureClassDef.Factory.newInstance();
        fillAndPlaceSqlClass(testBranch, procedure);

        for (Sqml.Item item : procedure.getSqml().getItems()) {
            if (item instanceof ParameterTag) {
                final ParameterTag paramTag = (ParameterTag) item;
                final IParameterDef param = paramTag.findParameter();
                if (param.getName().equals("pId")) {
                    paramTag.setDirection(EParamDirection.BOTH);
                }
                if (param.getName().equals("pType") || param.getName().equals("pCustom")) {
                    paramTag.setDirection(EParamDirection.OUT);
                }
            }
        }

        return procedure;
    }
}
