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
package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.enums.EDeleteMode;
import org.radixware.kernel.common.enums.EDdsConstraintDbOption;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.schemas.ddsdef.ModelDocument;
import org.radixware.schemas.ddsdef.ModelDocument.Model;
import org.radixware.kernel.common.sqml.Sqml;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.check.RadixProblem;

import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.value.RadixDefaultValue;
import org.radixware.kernel.common.enums.EOrder;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDdsTableExtOption;
import org.radixware.kernel.common.enums.EParamDirection;
import org.radixware.kernel.common.utils.XmlFormatter;

/**
 * Выгрузчик {@link DdsModelDef DDS модели} в XML файл.
 *
 */
class DdsModelWriter {

    // there are no instances needed
    private DdsModelWriter() {
    }

    /**
     * Save model into the specified file.
     *
     * @throws IOException
     */
    public static void writeModel(DdsModelDef ddsModel, File file) throws IOException {
        final ModelDocument modelDocument = ModelDocument.Factory.newInstance();
        final Model model = modelDocument.addNewModel();
        writeModel(ddsModel, model);

        //XmlOptions xmlOptions = new XmlOptions();
        //xmlOptions = xmlOptions.setSavePrettyPrint(); // commented, so data damaged :-( 
        XmlFormatter.save(modelDocument, file);
    }

    public static void writeModel(DdsModelDef ddsModel, Model xModel) {
        writeModel(ddsModel, xModel, false);
    }

    public static void writeModel(DdsModelDef ddsModel, Model xModel, boolean forceModifiedMode) {
        //final String idAsStr = .toString();
        xModel.setId(ddsModel.getId());

        final String dbNamePrefix = ddsModel.getDbAttributes().getDbNamePrefix();
        if (dbNamePrefix != null && !dbNamePrefix.isEmpty()) {
            xModel.setDbNamePrefix(dbNamePrefix);
        }

        final String editor = ddsModel.getModifierInfo().getEditor();
        if (editor != null && !editor.isEmpty()) {
            xModel.setEditor(editor);
        }

        final String station = ddsModel.getModifierInfo().getStation();
        if (station != null && !station.isEmpty()) {
            xModel.setStation(station);
        }

        final String filePath = ddsModel.getModifierInfo().getFilePath();
        if (filePath != null && !filePath.isEmpty()) {
            xModel.setFilePath(filePath);
        }

        final String tablespaceForIndices = ddsModel.getDbAttributes().getDefaultTablespaceForIndices();
        if (tablespaceForIndices != null && !tablespaceForIndices.isEmpty()) {
            xModel.setTablespaceForIndices(tablespaceForIndices);
        }

        final String tablespaceForTables = ddsModel.getDbAttributes().getDefaultTablespaceForTables();
        if (tablespaceForTables != null && !tablespaceForTables.isEmpty()) {
            xModel.setTablespaceForTables(tablespaceForTables);
        }

        final Sqml beginScript = ddsModel.getBeginScript();
        if (!beginScript.getItems().isEmpty()) {
            final org.radixware.schemas.xscml.Sqml xBeginScript = xModel.addNewBeginScript();
            beginScript.appendTo(xBeginScript);
        }

        final Sqml endScript = ddsModel.getEndScript();
        if (!endScript.getItems().isEmpty()) {
            final org.radixware.schemas.xscml.Sqml xEndScript = xModel.addNewEndScript();
            endScript.appendTo(xEndScript);
        }

        final RadixObjects<DdsAccessPartitionFamilyDef> apfs = ddsModel.getAccessPartitionFamilies();
        if (!apfs.isEmpty()) {
            final Model.AccessPartitionFamilies xApfs = xModel.addNewAccessPartitionFamilies();
            for (DdsAccessPartitionFamilyDef apf : apfs) {
                final org.radixware.schemas.ddsdef.AccessPartitionFamily xApf = xApfs.addNewAccessPartitionFamily();
                writeApf(apf, xApf);
            }
        }

        final DdsDefinitions<DdsExtTableDef> extTables = ddsModel.getExtTables();
        if (!extTables.isEmpty()) {
            final Model.ExtTables xExtTables = xModel.addNewExtTables();
            for (DdsExtTableDef extTable : extTables) {
                final org.radixware.schemas.ddsdef.ExtTable xExtTable = xExtTables.addNewExtTable();
                writeExtTable(extTable, xExtTable);
            }
        }

        final DdsDefinitions<DdsLabelDef> labels = ddsModel.getLabels();
        if (!labels.isEmpty()) {
            final Model.Labels xLabels = xModel.addNewLabels();
            for (DdsLabelDef label : labels) {
                final org.radixware.schemas.ddsdef.Label xLabel = xLabels.addNewLabel();
                writeLabel(label, xLabel);
            }
        }

        final DdsDefinitions<DdsPackageDef> pkgs = ddsModel.getPackages();
        if (!pkgs.isEmpty()) {
            final Model.Packages xPkgs = xModel.addNewPackages();
            for (DdsPackageDef pkg : pkgs) {
                final org.radixware.schemas.ddsdef.Package xPkg = xPkgs.addNewPackage();
                writePackage(pkg, xPkg);
            }
        }

        final DdsDefinitions<DdsReferenceDef> references = ddsModel.getReferences();
        if (!references.isEmpty()) {
            final Model.References xReferences = xModel.addNewReferences();
            for (DdsReferenceDef reference : references) {
                final org.radixware.schemas.ddsdef.Reference xReference = xReferences.addNewReference();
                writeReference(reference, xReference);
            }
        }

        final DdsDefinitions<DdsSequenceDef> Sequences = ddsModel.getSequences();
        if (!Sequences.isEmpty()) {
            final Model.Sequences xSequences = xModel.addNewSequences();
            for (DdsSequenceDef sequence : Sequences) {
                final org.radixware.schemas.ddsdef.Sequence xSequence = xSequences.addNewSequence();
                writeSequence(sequence, xSequence);
            }
        }

        final DdsDefinitions<DdsColumnTemplateDef> columnTemplates = ddsModel.getColumnTemplates();
        if (!columnTemplates.isEmpty()) {
            final Model.ColumnTemplates xColumnTemplates = xModel.addNewColumnTemplates();
            for (DdsColumnTemplateDef columnTemplate : columnTemplates) {
                final org.radixware.schemas.ddsdef.ColumnTemplate xColumnTemplate = xColumnTemplates.addNewColumnTemplate();
                writeColumnTemplate(columnTemplate, xColumnTemplate);
            }
        }

        final DdsDefinitions<DdsTableDef> tables = ddsModel.getTables();
        if (!tables.isEmpty()) {
            final Model.Tables xTables = xModel.addNewTables();
            for (DdsTableDef table : tables) {
                final org.radixware.schemas.ddsdef.Table xTable = xTables.addNewTable();
                writeTable(table, xTable);
            }
        }

        final DdsDefinitions<DdsTypeDef> types = ddsModel.getTypes();
        if (!types.isEmpty()) {
            final Model.Types xTypes = xModel.addNewTypes();
            for (DdsTypeDef type : types) {
                final org.radixware.schemas.ddsdef.Type xType = xTypes.addNewType();
                writeType(type, xType);
            }
        }

        final DdsDefinitions<DdsViewDef> views = ddsModel.getViews();
        if (!views.isEmpty()) {
            final Model.Views xViews = xModel.addNewViews();
            for (DdsViewDef view : views) {
                final org.radixware.schemas.ddsdef.View xView = xViews.addNewView();
                writeView(view, xView);
            }
        }
        try {
            if (forceModifiedMode || ddsModel.getModule().getModelManager().getModifiedModel() == ddsModel) {
                if (forceModifiedMode) {
                    ddsModel.getStringBundle().getStrings();
                }
                ddsModel.getStringBundle().appendTo(xModel.addNewStringBundle());
            }
        } catch (IOException ex) {
        }
    }

    private static void writeIdAndName(DdsDefinition namedDef, org.radixware.schemas.commondef.NamedDefinition xNamedDef) {
        //final String idAsStr = namedDef.getId().toString();
        xNamedDef.setId(namedDef.getId());

        final String name = namedDef.getName();
        xNamedDef.setName(name);
    }

    private static void writeIdNameDescription(DdsDefinition describedDef, org.radixware.schemas.commondef.DescribedDefinition xDescribedDef) {
        writeIdAndName(describedDef, xDescribedDef);
        final String description = describedDef.getDescription();
        if (description != null && !description.isEmpty()) {
            xDescribedDef.setDescription(description);
        }
        final Id descriptionId = describedDef.getDescriptionId();
        if (descriptionId != null) {
            xDescribedDef.setDescriptionId(descriptionId);
        }
    }

    public static void writeApf(DdsAccessPartitionFamilyDef apf, org.radixware.schemas.ddsdef.AccessPartitionFamily xApf) {
        xApf.setId(apf.getId());

        final Id headId = apf.getHeadId();
        xApf.setHeadId(headId.toString());

        final Id parentFamilyReferenceId = apf.getParentFamilyReferenceId();
        if (parentFamilyReferenceId != null) {
            xApf.setParentFamilyReferenceId(parentFamilyReferenceId.toString());
        }
    }

    private static void writePlacement(DdsDefinitionPlacement placement, org.radixware.schemas.ddsdef.Placement xPlacement) {
        final int posX = placement.getPosX();
        xPlacement.setPosX(posX);

        final int posY = placement.getPosY();
        xPlacement.setPosY(posY);
    }

    public static void writeExtTable(DdsExtTableDef extTable, org.radixware.schemas.ddsdef.ExtTable xExtTable) {
        xExtTable.setId(extTable.getId());

        final Id tableId = extTable.getTableId();
        xExtTable.setTableId(tableId.toString());

        writePlacement(extTable.getPlacement(), xExtTable.addNewPlacement());
    }

    public static void writeLabel(DdsLabelDef label, org.radixware.schemas.ddsdef.Label xLabel) {
        xLabel.setId(label.getId());

        xLabel.addNewFont();

        final boolean bold = label.getFont().isBold();
        xLabel.getFont().setBold(bold);

        final boolean italic = label.getFont().isItalic();
        xLabel.getFont().setItalic(italic);

        final int fontSize = label.getFont().getSizePx();
        xLabel.getFont().setSize(fontSize);

        final String text = label.getText();
        xLabel.setText(text);

        writePlacement(label.getPlacement(), xLabel.addNewPlacement());

//        final int width = label.getWidth();
//        xLabel.getPlacement().setWidth(width);
//
//        final int height = label.getHeight();
//        xLabel.getPlacement().setHeight(height);
    }

    public static void writeParameter(DdsParameterDef param, org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xParam) {
        writeIdNameDescription(param, xParam);

        final String dbType = param.getDbType();
        if (dbType != null && !dbType.isEmpty()) {
            xParam.setDbType(dbType);
        }

        final EParamDirection direction = param.getDirection();
        xParam.setDirection(direction);

        final EValType valType = param.getValType();
        xParam.setValType(valType);

        final String defaultVal = param.getDefaultVal();
        if (defaultVal == null) {
            xParam.setNilDefaultVal();
        } else if (!defaultVal.isEmpty()) {
            xParam.setDefaultVal(defaultVal);
        }
    }

    public static void writeFunction(DdsFunctionDef function, org.radixware.schemas.ddsdef.PlSqlFunction xFunction) {
        writeIdNameDescription(function, xFunction);

        if (!function.generatedInDb) {
            xFunction.setGenerateInDb(function.generatedInDb);
        }

        final org.radixware.schemas.xscml.Sqml xBody = xFunction.addNewBody();
        final Sqml body = function.getBody();
        body.appendTo(xBody);

        final String resultDbType = function.getResultDbType();
        if (resultDbType != null && !resultDbType.isEmpty()) {
            xFunction.setResultDbType(resultDbType);
        }

        boolean deterministic = function.isDeterministic();
        xFunction.setDeterministic(deterministic);

        boolean cacheResult = function.isCachedResult();
        xFunction.setCacheResult(cacheResult);

        final int purityLevelMask = function.getPurityLevel().toBitMask();
        xFunction.setPurityLevelMask(purityLevelMask);

        EValType resultValType = function.getResultValType();
        if (resultValType != null) {
            xFunction.setResultValType(resultValType);
        }
        DdsFunctionDef.ReliesOnTablesInfo reliesOnTables = function.getReliesOnTablesInfo();
        final int reliesOnTableIdCount = reliesOnTables.size();
        if (reliesOnTableIdCount > 0) {
            List<String> reliesOnTableIdsAsStr = new ArrayList<String>(reliesOnTableIdCount);
            for (DdsFunctionDef.ReliesOnTableInfo reliesOnTableInfo : reliesOnTables) {
                Id reliesOnTableId = reliesOnTableInfo.getTableId();
                reliesOnTableIdsAsStr.add(reliesOnTableId.toString());
            }
            xFunction.setReliesOnTableIds(reliesOnTableIdsAsStr);
        }

        if (!function.getParameters().isEmpty()) {
            org.radixware.schemas.ddsdef.PlSqlFunction.Params xParams = xFunction.addNewParams();
            for (DdsParameterDef param : function.getParameters()) {
                org.radixware.schemas.ddsdef.PlSqlFunction.Params.Param xParam = xParams.addNewParam();
                writeParameter(param, xParam);
            }
        }
        if (function.isDeprecated()) {
            xFunction.setDeprecated(true);
        }
    }

    public static void writePrototype(DdsPrototypeDef prototype, org.radixware.schemas.ddsdef.PlSqlPrototype xPrototype) {
        //    final String idAsStr = prototype.getId().toString();
        xPrototype.setId(prototype.getId());

        final Id functionId = prototype.getFunctionId();
        xPrototype.setFunctionId(functionId.toString());
        if (prototype.isDeprecated()) {
            xPrototype.setDeprecated(true);
        }
    }

    public static void writeCustomText(DdsCustomTextDef customText, org.radixware.schemas.ddsdef.PlSqlCustomText xCustomText) {
        writeIdAndName(customText, xCustomText);

        final org.radixware.schemas.xscml.Sqml xText = xCustomText.addNewText();
        final Sqml text = customText.getText();
        text.appendTo(xText);
    }

    private static void writePlSqlObject(DdsPlSqlObjectDef plSqlObject, org.radixware.schemas.ddsdef.PlSqlObject xPlSqlObject) {
        writeIdNameDescription(plSqlObject, xPlSqlObject);

        final int purityLevelMask = plSqlObject.getPurityLevel().toBitMask();
        xPlSqlObject.setPurityLevelMask(purityLevelMask);

        if (!plSqlObject.getHeader().getItems().isEmpty()) {
            xPlSqlObject.addNewHeader();
            for (DdsPlSqlObjectItemDef item : plSqlObject.getHeader().getItems()) {
                org.radixware.schemas.ddsdef.PlSqlHeader.Item xItem = xPlSqlObject.getHeader().addNewItem();
                if (item instanceof DdsPrototypeDef) {
                    DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                    writePrototype(prototype, xItem.addNewPrototype());
                } else if (item instanceof DdsCustomTextDef) {
                    DdsCustomTextDef customText = (DdsCustomTextDef) item;
                    writeCustomText(customText, xItem.addNewCustomText());
                }
            }
        }

        if (!plSqlObject.getBody().getItems().isEmpty()) {
            xPlSqlObject.addNewBody();
            for (DdsPlSqlObjectItemDef item : plSqlObject.getBody().getItems()) {
                org.radixware.schemas.ddsdef.PlSqlBody.Item xItem = xPlSqlObject.getBody().addNewItem();
                if (item instanceof DdsFunctionDef) {
                    DdsFunctionDef function = (DdsFunctionDef) item;
                    writeFunction(function, xItem.addNewFunction());
                } else if (item instanceof DdsPrototypeDef) {
                    DdsPrototypeDef prototype = (DdsPrototypeDef) item;
                    writePrototype(prototype, xItem.addNewPrototype());
                } else if (item instanceof DdsCustomTextDef) {
                    DdsCustomTextDef customText = (DdsCustomTextDef) item;
                    writeCustomText(customText, xItem.addNewCustomText());
                }
            }
        }
        if (plSqlObject.isDeprecated()) {
            xPlSqlObject.setDeprecated(true);
        }
    }

    public static void writePackage(DdsPackageDef pkg, org.radixware.schemas.ddsdef.Package xPkg) {
        writePlSqlObject(pkg, xPkg);
    }

    public static void writeTypeField(DdsTypeFieldDef field, org.radixware.schemas.ddsdef.Type.Fields.Field xField) {
        writeIdNameDescription(field, xField);

        final String dbType = field.getDbType();
        xField.setDbType(dbType);
    }

    public static void writeType(DdsTypeDef type, org.radixware.schemas.ddsdef.Type xType) {
        writePlSqlObject(type, xType);

        final String dbType = type.getDbType();
        xType.setDbType(dbType);

        final DdsDefinitions<DdsTypeFieldDef> fields = type.getFields();
        if (!fields.isEmpty()) {
            org.radixware.schemas.ddsdef.Type.Fields xFields = xType.addNewFields();
            for (DdsTypeFieldDef field : fields) {
                org.radixware.schemas.ddsdef.Type.Fields.Field xField = xFields.addNewField();
                writeTypeField(field, xField);
            }
        }
    }

    private static void writeConstraint(DdsConstraintDef constraint, org.radixware.schemas.ddsdef.Constraint xConstraint) {
        xConstraint.setId(constraint.getId());

        if (!constraint.isAutoDbName()) {
            xConstraint.setAutoDbName(constraint.isAutoDbName());
        }
        xConstraint.setDbName(constraint.getDbName());

        if (constraint.getDbOptions().contains(EDdsConstraintDbOption.RELY)) {
            xConstraint.setRely(true);
        }
        if (constraint.getDbOptions().contains(EDdsConstraintDbOption.DISABLE)) {
            xConstraint.setDisable(true);
        }
        if (constraint.getDbOptions().contains(EDdsConstraintDbOption.NOVALIDATE)) {
            xConstraint.setNovalidate(true);
        }
        if (constraint.getDbOptions().contains(EDdsConstraintDbOption.DEFERRABLE)) {
            xConstraint.setDeferrable(true);
        }
        if (constraint.getDbOptions().contains(EDdsConstraintDbOption.INITIALLY_DEFERRED)) {
            xConstraint.setInitiallyDeferred(true);
        }
    }

    public static void writeReference(DdsReferenceDef reference, org.radixware.schemas.ddsdef.Reference xReference) {
        writeConstraint(reference, xReference);

        DdsReferenceDef.EType type = reference.getType();
        xReference.setType(org.radixware.schemas.ddsdef.Reference.Type.Enum.forString(type.toString()));

        if (!reference.generatedInDb) {
            xReference.setGenerateInDb(reference.generatedInDb);
        }

        final Id childTableId = reference.getChildTableId();
        xReference.setChildTableId(childTableId.toString());

        final Id parentTableId = reference.getParentTableId();
        xReference.setParentTableId(parentTableId.toString());

        final Id extChildTableId = reference.getExtChildTableId();
        if (extChildTableId != null) {
            xReference.setExtChildTableId(extChildTableId.toString());
        }

        final Id extParentTableId = reference.getExtParentTableId();
        if (extParentTableId != null) {
            xReference.setExtParentTableId(extParentTableId.toString());
        }

        final boolean confirmDelete = reference.isConfirmDelete();
        xReference.setConfirmDelete(confirmDelete);

        EDeleteMode deleteMode = reference.getDeleteMode();

        xReference.setDeleteMode(org.radixware.schemas.ddsdef.Reference.DeleteMode.Enum.forString(deleteMode.toString()));

        DdsReferenceDef.ERestrictCheckMode restrictCheckMode = reference.getRestrictCheckMode();

        xReference.setRestrictCheckMode(org.radixware.schemas.ddsdef.Reference.RestrictCheckMode.Enum.forString(restrictCheckMode.toString()));

        final Id parentUniqueConstraintId = reference.getParentUnuqueConstraintId();
        if (parentUniqueConstraintId != null) {
            xReference.setParentUniqueConstraintId(parentUniqueConstraintId.toString());
        }
        final RadixObjects<DdsReferenceDef.ColumnsInfoItem> items = reference.getColumnsInfo();
        if (!items.isEmpty()) {
            org.radixware.schemas.ddsdef.Reference.Columns xColumns = xReference.addNewColumns();
            for (DdsReferenceDef.ColumnsInfoItem item : items) {
                org.radixware.schemas.ddsdef.Reference.Columns.Item xItem = xColumns.addNewItem();
                final Id childColumnId = item.getChildColumnId();
                xItem.setChildColumnId(childColumnId != null ? childColumnId.toString() : "");
                final Id parentColumnId = item.getParentColumnId();
                xItem.setParentColumnId(parentColumnId != null ? parentColumnId.toString() : "");
            }
        }
        
        RadixProblem.WarningSuppressionSupport wss = reference.getWarningSuppressionSupport(false);
        if (wss != null && !wss.isEmpty()) {

            int[] warnings = wss.getSuppressedWarnings();
            List<Integer> list = new ArrayList<>(warnings.length);
            for (int w : warnings) {
                list.add(w);
            }
            xReference.setSuppressedWarnings(list);
        }
    }

    public static void writeSequence(DdsSequenceDef sequence, org.radixware.schemas.ddsdef.Sequence xSequence) {
        writeIdAndName(sequence, xSequence);

        final String dbName = sequence.getDbName();
        xSequence.setDbName(dbName);

        if (!sequence.isAutoDbName()) {
            xSequence.setAutoDbName(false);
        }
        final Long startWith = sequence.getStartWith();
        if (startWith != null) {
            xSequence.setStartWith(startWith.longValue());
        }
        final Long incrementBy = sequence.getIncrementBy();
        if (incrementBy != null) {
            xSequence.setIncrementBy(incrementBy.longValue());
        }
        boolean cycle = sequence.isCycled();
        xSequence.setCycle(cycle);

        final Long minValue = sequence.getMinValue();
        if (minValue != null) {
            xSequence.setMinValue(minValue.longValue());
        }
        final Long maxValue = sequence.getMaxValue();
        if (maxValue != null) {
            xSequence.setMaxValue(maxValue.longValue());
        }
        boolean order = sequence.isOrdered();
        xSequence.setOrder(order);

        final Long cache = sequence.getCache();
        if (cache != null) {
            xSequence.setCache(cache.longValue());
        }
        writePlacement(sequence.getPlacement(), xSequence.addNewPlacement());
        
        RadixProblem.WarningSuppressionSupport wss = sequence.getWarningSuppressionSupport(false);
        if (wss != null && !wss.isEmpty()) {

            int[] warnings = wss.getSuppressedWarnings();
            List<Integer> list = new ArrayList<Integer>(warnings.length);
            for (int w : warnings) {
                list.add(Integer.valueOf(w));
            }
            xSequence.setSuppressedWarnings(list);
        }
    }

    public static void writeColumnTemplate(DdsColumnTemplateDef columnTemplate, org.radixware.schemas.ddsdef.ColumnTemplate xColumnTemplate) {
        writeIdNameDescription(columnTemplate, xColumnTemplate);

        EValType valType = columnTemplate.getValType();
        xColumnTemplate.setValType(valType);

        final int length = columnTemplate.getLength();
        xColumnTemplate.setLength(length);

        final int precision = columnTemplate.getPrecision();
        if (precision != 0) {
            xColumnTemplate.setPrecision(precision);
        }

        final boolean autoDbType = columnTemplate.isAutoDbType();
        if (!autoDbType) {
            xColumnTemplate.setAutoDbType(autoDbType);
        }

        final Id nativeDbTypeId = columnTemplate.getNativeDbTypeId();
        if (nativeDbTypeId != null) {
            xColumnTemplate.setNativeDbTypeId(nativeDbTypeId.toString());
        }

        final String dbType = columnTemplate.getDbType();
        xColumnTemplate.setDbType(dbType);

        if (columnTemplate.isDeprecated()) {
            xColumnTemplate.setDeprecated(true);
        }
    }

    public static void writeColumn(DdsColumnDef column, org.radixware.schemas.ddsdef.Column xColumn) {
        writeColumnTemplate(column, xColumn);

        final boolean notNull = column.isNotNull();
        xColumn.setNotNull(notNull);

        if (!column.generatedInDb) {
            xColumn.setGenerateInDb(column.generatedInDb);
        }

        final String dbName = column.getDbName();
        xColumn.setDbName(dbName);

        final boolean autoDbName = column.isAutoDbName();
        if (!autoDbName) {
            xColumn.setAutoDbName(autoDbName);
        }

        final Id sequenceId = column.getSequenceId();
        if (sequenceId != null) {
            xColumn.setSequenceId(sequenceId.toString());
        }

        final Id templateId = column.getTemplateId();
        if (templateId != null) {
            xColumn.setTemplateId(templateId.toString());
        }

        final RadixDefaultValue defaultValue = column.getDefaultValue();
        if (defaultValue != null) {
            column.getDefaultValue().appendTo(xColumn.addNewDefaultVal());
        }

        final DdsCheckConstraintDef checkConstraint = column.getCheckConstraint();
        if (checkConstraint != null) {
            org.radixware.schemas.ddsdef.Column.CheckConstraint xCheckConstraint = xColumn.addNewCheckConstraint();
            writeConstraint(checkConstraint, xCheckConstraint);

            org.radixware.schemas.xscml.Sqml xCondition = xCheckConstraint.addNewCondition();
            checkConstraint.getCondition().appendTo(xCondition);
        }

        column.getAuditInfo().appendTo(xColumn);

        final boolean hidden = column.isHidden();
        if (hidden) {
            xColumn.setHidden(hidden);
        }

        final List<ValAsStr> initialValues = column.getInitialValues();
        if (!initialValues.isEmpty()) {
            org.radixware.schemas.ddsdef.Column.InitialValues xInitialValues = xColumn.addNewInitialValues();
            for (ValAsStr initialValue : initialValues) {
                if (initialValue != null) {
                    xInitialValues.addValue(initialValue.toString());
                } else {
                    xInitialValues.addValue(""); // зачем - см. восстановление.
                }
            }
        }

        final Sqml sqml = column.getExpression();
        if (sqml != null) {
            org.radixware.schemas.xscml.Sqml xSqml = xColumn.addNewExpression();
            sqml.appendTo(xSqml);
        }
    }

    public static void writeUniqueConstraint(DdsUniqueConstraintDef uc, org.radixware.schemas.ddsdef.Index.UniqueConstraint xUc) {
        writeConstraint(uc, xUc);
    }

    public static void writeIndex(DdsIndexDef index, org.radixware.schemas.ddsdef.Index xIndex) {
        writeIdAndName(index, xIndex);

        final String dbName = index.getDbName();
        xIndex.setDbName(dbName);

        final boolean autoDbName = index.isAutoDbName();
        if (!autoDbName) {
            xIndex.setAutoDbName(autoDbName);
        }

        if (!index.generatedInDb) {
            xIndex.setGenerateInDb(index.generatedInDb);
        }

        final String tablespace = index.getTablespaceDbName();
        if (tablespace != null && !tablespace.isEmpty()) {
            xIndex.setTablespace(tablespace);
        }

        final DdsUniqueConstraintDef uc = index.getUniqueConstraint();
        if (uc != null) {
            final org.radixware.schemas.ddsdef.Index.UniqueConstraint xUc = xIndex.addNewUniqueConstraint();
            writeUniqueConstraint(uc, xUc);
        }

        if (index.getDbOptions().contains(DdsIndexDef.EDbOption.UNIQUE)) {
            xIndex.setUnique(true);
        }
        if (index.getDbOptions().contains(DdsIndexDef.EDbOption.INVISIBLE)) {
            xIndex.setInvisible(true);
        }
        if (index.getDbOptions().contains(DdsIndexDef.EDbOption.BITMAP)) {
            xIndex.setBitmap(true);
        }
        if (index.getDbOptions().contains(DdsIndexDef.EDbOption.LOCAL)) {
            xIndex.setLocal(true);
        }
        if (index.getDbOptions().contains(DdsIndexDef.EDbOption.NOLOGGING)) {
            xIndex.setNologging(true);
        }
        if (index.getDbOptions().contains(DdsIndexDef.EDbOption.REVERSE)) {
            xIndex.setReverse(true);
        }
        RadixObjects<DdsIndexDef.ColumnInfo> columns = index.getColumnsInfo();
        if (!columns.isEmpty()) {
            org.radixware.schemas.ddsdef.Index.Columns xColumns = xIndex.addNewColumns();
            for (DdsIndexDef.ColumnInfo columnInfo : columns) {
                org.radixware.schemas.ddsdef.Index.Columns.Column xColumn = xColumns.addNewColumn();

                final Id columnId = columnInfo.getColumnId();
                xColumn.setColumnId(columnId.toString());

                final EOrder order = columnInfo.getOrder();
                xColumn.setOrder(order);
            }
        }

        if (index.isDeprecated()) {
            xIndex.setDeprecated(true);
        }
    }

    public static void writeTrigger(DdsTriggerDef trigger, org.radixware.schemas.ddsdef.Trigger xTrigger) {
        writeIdNameDescription(trigger, xTrigger);

        if (trigger.isOverwrite()) {
            xTrigger.setIsOverwrite(true);
        }

        final String dbName = trigger.getDbName();
        xTrigger.setDbName(dbName);

        final boolean autoDbName = trigger.isAutoDbName();
        if (!autoDbName) {
            xTrigger.setAutoDbName(autoDbName);
        }

        final DdsTriggerDef.EType type = trigger.getType();
        if (type != DdsTriggerDef.EType.NONE) {
            final org.radixware.schemas.ddsdef.Trigger.Type.Enum xType = org.radixware.schemas.ddsdef.Trigger.Type.Enum.forString(type.toString());
            xTrigger.setType(xType);
        }

        final DdsTriggerDef.EActuationTime actuationTime = trigger.getActuationTime();
        final org.radixware.schemas.ddsdef.Trigger.ActuationTime.Enum xActuationTime = org.radixware.schemas.ddsdef.Trigger.ActuationTime.Enum.forString(actuationTime.toString());
        xTrigger.setActuationTime(xActuationTime);

        long triggeringEventMask = 0;
        if (trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_DELETE)) {
            triggeringEventMask += 1;
        }
        if (trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_INSERT)) {
            triggeringEventMask += 2;
        }
        if (trigger.getTriggeringEvents().contains(DdsTriggerDef.ETriggeringEvent.ON_UPDATE)) {
            triggeringEventMask += 4;
        }
        xTrigger.setTriggeringEventMask(triggeringEventMask);

        final int columnsCount = trigger.getColumnsInfo().size();
        if (columnsCount > 0) {
            List<String> columnIdsAsStr = new ArrayList<String>(columnsCount);
            for (DdsTriggerDef.ColumnInfo columnInfo : trigger.getColumnsInfo()) {
                Id columnId = columnInfo.getColumnId();
                columnIdsAsStr.add(columnId.toString());
            }
            xTrigger.setColumnIds(columnIdsAsStr);
        }
        final boolean forEachRow = trigger.isForEachRow();
        if (!forEachRow) {
            xTrigger.setForEachRow(forEachRow);
        }

        final boolean disabled = trigger.isDisabled();
        if (disabled) {
            xTrigger.setDisabled(disabled);
        }

        final Sqml body = trigger.getBody();
        final org.radixware.schemas.xscml.Sqml xSqml = xTrigger.addNewBody();
        body.appendTo(xSqml);
        if (trigger.isDeprecated()) {
            xTrigger.setDeprecated(true);
        }
    }

    public static void writeTable(DdsTableDef table, org.radixware.schemas.ddsdef.Table xTable) {
        writeIdNameDescription(table, xTable);

        if (table.isOverwrite()) {
            xTable.setIsOverwrite(true);
        }

        final String dbName = table.getDbName();
        xTable.setDbName(dbName);

        final boolean autoDbName = table.isAutoDbName();
        if (!autoDbName) {
            xTable.setAutoDbName(autoDbName);
        }

        DdsIndexDef primaryKey = table.getPrimaryKey();
        writeIndex(primaryKey, xTable.addNewPrimaryKey());

        if (!table.generatedInDb) {
            xTable.setGenerateInDb(table.generatedInDb);
        }

        final boolean hidden = table.isHidden();
        if (hidden) {
            xTable.setHidden(hidden);
        }

        table.getAuditInfo().appendTo(xTable);

        long userExtMask = 0;
        for (EDdsTableExtOption extOption : table.getExtOptions()) {
            userExtMask |= extOption.getValue();
        }
        if (userExtMask != 0) {
            xTable.setUserExtMask(userExtMask);
        }

        writePlacement(table.getPlacement(), xTable.addNewPlacement());

        final Definitions<DdsColumnDef> columns = table.getColumns().getLocal();
        if (!columns.isEmpty()) {
            final org.radixware.schemas.ddsdef.Table.Columns xColumns = xTable.addNewColumns();
            for (DdsColumnDef column : columns) {
                final org.radixware.schemas.ddsdef.Column xColumn = xColumns.addNewColumn();
                writeColumn(column, xColumn);
            }
        }

        final String tablespace = table.getTablespace();
        if (tablespace != null) {
            xTable.setTablespace(tablespace);
        }

        final Sqml partition = table.getPartition();
        if (!partition.getItems().isEmpty()) {
            final org.radixware.schemas.xscml.Sqml xPartition = xTable.addNewPartition();
            partition.appendTo(xPartition);
        }

        if (table.isGlobalTemporary()) {
            xTable.setGlobalTemporary(true);
        }

        if (table.isOnCommitPreserveRows()) {
            xTable.setOnCommitPreserveRows(true);
        }

        final Definitions<DdsIndexDef> indices = table.getIndices().getLocal();
        if (!indices.isEmpty()) {
            final org.radixware.schemas.ddsdef.Table.Indices xIndices = xTable.addNewIndices();
            for (DdsIndexDef index : indices) {
                final org.radixware.schemas.ddsdef.Index xIndex = xIndices.addNewIndex();
                writeIndex(index, xIndex);
            }
        }

        final Definitions<DdsTriggerDef> triggers = table.getTriggers().getLocal();
        if (!triggers.isEmpty()) {
            final org.radixware.schemas.ddsdef.Table.Triggers xTriggers = xTable.addNewTriggers();
            for (DdsTriggerDef trigger : triggers) {
                final org.radixware.schemas.ddsdef.Trigger xTrigger = xTriggers.addNewTrigger();
                writeTrigger(trigger, xTrigger);
            }
        }
        if (table.isDeprecated()) {
            xTable.setDeprecated(true);
        }
    }

    public static void writeView(DdsViewDef view, org.radixware.schemas.ddsdef.View xView) {
        writeTable(view, xView);

        final Sqml query = view.getSqml();
        final org.radixware.schemas.xscml.Sqml xQuery = xView.addNewQuery();
        query.appendTo(xQuery);

        xView.setDistinct(view.isDistinct());
        xView.setWithOption(view.getWithOption());
        for (Id id : view.getUsedTables().getUsedTableIds()) {
            xView.ensureUsedTables().getTableList().add(id);
        }
    }
}
