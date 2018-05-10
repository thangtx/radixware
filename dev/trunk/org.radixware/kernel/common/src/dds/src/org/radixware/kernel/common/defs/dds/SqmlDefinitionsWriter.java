/*
* Copyright (c) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.kernel.common.defs.dds;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.schemas.sqmldef.*;


public class SqmlDefinitionsWriter {
    private final Module module;
    
    public SqmlDefinitionsWriter(final Module module){
        this.module = module;
    }
    
    public final Module getModule(){
        return module;
    }               
            
    public final String write() throws IOException{
        final File indexFile = new File(module.getDirectory(), FileUtils.SQML_DEFINITIONS_XML_FILE_NAME);
        final SqmlModuleDocument xDoc = SqmlModuleDocument.Factory.newInstance();            

        final SqmlModule  xModule =  xDoc.addNewSqmlModule();
        final Map<Id,RadixObject> sqmlDefsById = new TreeMap<>();
        module.visit(new IVisitor() {
            @Override
            public void accept(final RadixObject radixObject) {
                if (radixObject instanceof Definition){
                    //sorting definitions before write them to xml
                    sqmlDefsById.put(((Definition)radixObject).getId(), radixObject);
                }
            }
        },new VisitorProvider() {

            @Override
            public boolean isTarget(final RadixObject radixObject) {
                return SqmlDefinitionsWriter.this.isSqmlDefinition(radixObject);
            }

            @Override
            public boolean isContainer(final RadixObject radixObject) {
                return SqmlDefinitionsWriter.this.isSqmlDefinitionsContainer(radixObject);
            }
        });

        if (sqmlDefsById.isEmpty()){
            if (indexFile.exists()){
                FileUtils.deleteFile(indexFile);
            }
            return null;
        }
        
        for (RadixObject radixObject: sqmlDefsById.values()){
            writeRadixObject(radixObject, xModule);
        }

        xModule.setId(module.getId());
        xModule.setQualifiedName(module.getQualifiedName());

        final boolean exist = indexFile.exists();
        try(final OutputStream out = FileUtils.getOutputStreamNoLock(indexFile)){
            XmlFormatter.save(xDoc, out);
        }
        if (exist) {
            return "[dist] SQML definitions index file updated: " + indexFile.getAbsolutePath();               
        } else {
            return "[dist] SQML definitions index file created: " + indexFile.getAbsolutePath();
        }
    }

    protected boolean writeRadixObject(final RadixObject radixObject, final SqmlModule  xModule){
        if (radixObject instanceof DdsTableDef){
            writeDdsTable((DdsTableDef)radixObject, xModule.addNewTableDef());
            return true;
        }else if (radixObject instanceof DdsPackageDef){
            writeDdsPackageDef((DdsPackageDef)radixObject, xModule.addNewPackageDef());
            return true;
        }
        return false;
    }

    protected boolean isSqmlDefinition(final RadixObject radixObject) {
        return radixObject instanceof DdsTableDef 
                   || radixObject instanceof DdsPackageDef;
    }

    protected boolean isSqmlDefinitionsContainer(final RadixObject radixObject) {
        return true;
    }   

    private void writeDdsPackageDef(final DdsPackageDef packageDef, final SqmlPackageDef xPackageDef){
        writeDefinition(packageDef, xPackageDef);
        DdsFunctionDef function;
        for (DdsPlSqlObjectItemDef sqlItem : packageDef.getHeader().getItems()) {
            if (sqlItem instanceof DdsPrototypeDef) {
                function = ((DdsPrototypeDef) sqlItem).findFunction();
                if (function != null && function.isPublic()) {
                    writeDdsFuction(function, xPackageDef.addNewFunction());
                }
            }
        }
    }

    private void writeDdsFuction(final DdsFunctionDef function, final SqmlFunctionDef xFunction){
        writeDefinition(function, xFunction);
        final DdsDefinitions<DdsParameterDef> params = function.getParameters();
        if (params!=null && !params.isEmpty()){
            for (DdsParameterDef parameter: params){
                writeDdsFunctionParameter(parameter, xFunction.addNewParameter());
            }
        }
        xFunction.setResultDbType(function.getResultDbType());
        if (function.getPurityLevel().isWNDS()){
            xFunction.setIsWNDS(true);
        }
    }

    private void writeDdsFunctionParameter(final DdsParameterDef parameter, final SqmlFunctionParamDef xParameter){
        writeDefinition(parameter, xParameter);
        xParameter.setDbName(parameter.getDbName());
        xParameter.setDbType(parameter.getDbType());
        if (parameter.getValType()!=null){
            xParameter.setValType(parameter.getValType());
        }
        final String defaulVal = parameter.getDefaultVal();
        if (defaulVal!=null && !defaulVal.isEmpty()){
            xParameter.setDefaultValue(defaulVal);
        }
        xParameter.setDirection(parameter.getDirection());
    }

    private void writeDdsTable(final DdsTableDef tableDef, final SqmlTableDef xTableDef){
        writeDefinition(tableDef, xTableDef);
        {//Indexes
            final SqmlTableDef.Indexes xIndices = xTableDef.addNewIndexes();            
            final DdsPrimaryKeyDef primaryKey = tableDef.getPrimaryKey();
            writeTableIndex(primaryKey, xIndices.addNewIndex(), true);            
            final Collection<DdsIndexDef> indices = tableDef.getIndices().get(ExtendableDefinitions.EScope.ALL);
            for (DdsIndexDef index:  indices){
                if (!primaryKey.getId().equals(index.getId()) && index.isSecondaryKey()){
                    writeTableIndex(index, xIndices.addNewIndex(), false);
                }
            }
        }
        final DdsReferenceDef masterReference = tableDef.findMasterReference();
        {//Outgoing references
            final List<DdsReferenceDef> refDefs = new ArrayList<>(tableDef.collectOutgoingReferences());
            Collections.sort(refDefs, new Comparator<DdsReferenceDef>(){
                @Override
                public int compare(DdsReferenceDef o1, DdsReferenceDef o2) {
                    return o1.getId().toString().compareTo(o2.getId().toString());
                }                
            });
            final Id masterTableId = masterReference==null ? null : masterReference.getParentTableId();
            SqmlTableDef.OutgoingReferences xRefs = null;            
            for (DdsReferenceDef ddsReference : refDefs) {
                if (masterTableId==null || !masterTableId.equals(ddsReference.getParentTableId())){
                    if (xRefs==null){
                        xRefs = xTableDef.addNewOutgoingReferences();
                    }
                    writeOutgoingTableReference(ddsReference, xRefs.addNewReference());
                }
            }                
        }
        {//Table columns
            final List<DdsColumnDef> ddsColumns = tableDef.getColumns().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE);
            final SqmlTableDef.Columns xColumns = xTableDef.addNewColumns();
            for (DdsColumnDef ddsColumn : ddsColumns) {
                if (!ddsColumn.isHidden()) {
                    writeDdsColumnDef(ddsColumn, xColumns.addNewColumn());
                }
            }
        }
        if (masterReference!=null){
            writeDefinition(masterReference, xTableDef.addNewReferenceToMasterTable());
            xTableDef.setMasterTableId(masterReference.getParentTableId());
        }            
    }

    protected final void writeTableIndex(final DdsIndexDef index, final SqmlIndexDef xIndex, final boolean primaryKey){
        writeDefinition(index, xIndex);            
        if (primaryKey){
            xIndex.setIsPrimaryKey(true);
        }
        final DdsIndexDef.ColumnsInfo columnsInfo = index.getColumnsInfo();
        if (columnsInfo!=null && columnsInfo.size()>0){
            final List<Id> columns = new LinkedList<>();
            for (DdsIndexDef.ColumnInfo columnInfo: columnsInfo){
                columns.add(columnInfo.getColumnId());
            }
            xIndex.setIndexColumns(columns);
        }
    }

    private void writeOutgoingTableReference(final DdsReferenceDef reference, final SqmlOutgoingReferenceDef xReference){
        writeDefinition(reference, xReference);
        xReference.setReferencedTableId(reference.getParentTableId());
        final DdsReferenceDef.ColumnsInfoItems columnsInfo = reference.getColumnsInfo();
        if (columnsInfo!=null && columnsInfo.size()>0){
            final List<String> childColumnNames = new LinkedList<>();
            for (DdsReferenceDef.ColumnsInfoItem columnInfo: columnsInfo){
                childColumnNames.add(columnInfo.findChildColumn() == null ? "#" + columnInfo.getChildColumnId().toString() :  columnInfo.findChildColumn().getName());
            }
            xReference.setChildColumnNames(childColumnNames);
        }
    }

    private void writeDdsColumnDef(final DdsColumnDef column, final SqmlTableColumnDef xColumn){  
        writeDefinition(column, xColumn);
        if (column.getValType()!=null){
            xColumn.setValType(column.getValType());
        }
        xColumn.setLength(column.getLength());
        xColumn.setPrecision(column.getPrecision());
        if (column.isNotNull()){
            xColumn.setNotNull(true);
        }
    }

    protected final void writeDefinition(final Definition def, final SqmlDefinition xDef){
        xDef.setId(def.getId());
        xDef.setName(def.getName());
        if (def.getModule()!=module){
            xDef.setModuleName(def.getModule().getQualifiedName());
        }
        if (def.isDeprecated()){
            xDef.setIsDeprecated(true);
        }
    }                
}  
