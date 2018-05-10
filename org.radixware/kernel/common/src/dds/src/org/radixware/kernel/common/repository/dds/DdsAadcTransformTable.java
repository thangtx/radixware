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

package org.radixware.kernel.common.repository.dds;

import java.util.List;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.utils.ScriptsUtils;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.product.AadcTransformColumn;
import org.radixware.schemas.product.AadcTransformTable;

public class DdsAadcTransformTable extends RadixObject {
    private Id targetId;
    private String targetDbName;
    private String sourceDbName;
    private final DdsAadcTransformColumns  columns = new DdsAadcTransformColumns(this);
    
    public DdsAadcTransformTable() {
    }
    
    protected DdsAadcTransformTable(AadcTransformTable xAadcTransformTable) {
        this.targetId = xAadcTransformTable.getTargetId();
        this.targetDbName = xAadcTransformTable.getTargetName();
        if (xAadcTransformTable.isSetSourceName()){
            this.sourceDbName = xAadcTransformTable.getSourceName();
        }
        columns.clear();
        List<AadcTransformColumn> columnsList = xAadcTransformTable.getColumnsList();
        if (columnsList != null && !columnsList.isEmpty()) {
            for (AadcTransformColumn xColumn : columnsList) {
                final DdsAadcTransformColumn column = new DdsAadcTransformColumn(xColumn);
                columns.add(column);
            }
        }
    }
    
    public void loadFrom(DdsAadcTransformTable aadcTransformTable) {
        setTargetId(aadcTransformTable.getTargetId());
        setTargetDbName(aadcTransformTable.getTargetDbName());
        setSourceDbName(aadcTransformTable.getSourceDbName());
        DdsAadcTransformColumns columnsList = aadcTransformTable.getColumns();
        columns.clear();
        if (columnsList != null && !columnsList.isEmpty()) {
            for (DdsAadcTransformColumn xColumn : columnsList) {
                final DdsAadcTransformColumn column = new DdsAadcTransformColumn();
                column.loadFrom(xColumn);
                columns.add(column);
            }
        }
        
    }
    
    protected void appendTo(AadcTransformTable xAadcTransformTable) {
        xAadcTransformTable.setTargetId(targetId);
        xAadcTransformTable.setTargetName(targetDbName);
        if (sourceDbName!= null && !sourceDbName.isEmpty()){
            xAadcTransformTable.setSourceName(sourceDbName);
        }
        if (!columns.isEmpty()){
            for (DdsAadcTransformColumn column: columns){
                column.appendTo(xAadcTransformTable.addNewColumns());
            }
        }
    }

    public Id getTargetId() {
        return targetId;
    }

    public void setTargetId(Id targetId) {
        if (!Utils.equals(this.targetId, targetId)) {
            this.targetId = targetId;
        }
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        if (!Utils.equals(this.targetDbName, targetDbName)) {
            this.targetDbName = targetDbName;
        }
    }

    public String getSourceDbName() {
        return sourceDbName;
    }

    public void setSourceDbName(String sourceDbName) {
        if (!Utils.equals(this.sourceDbName, sourceDbName)) {
            this.sourceDbName = sourceDbName;
        }
    }
    
    public DdsTableDef findTable() {
        return findTable(getLayer());
    }
    
    public DdsTableDef findTable(Layer layer) {
        if (layer != null) {
            DdsDefinition def = ScriptsUtils.findTopLevelDdsDefById(layer, targetId);
            if (def instanceof DdsTableDef) {
                return (DdsTableDef) def;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Target Table Name=");
        sb.append(targetDbName);
        if (sourceDbName != null && !sourceDbName.isEmpty()) {
            sb.append(", Source Table Name=").append(sourceDbName);
        }
        return sb.toString();
    }

    public DdsAadcTransformColumns getColumns() {
        return columns;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        columns.visit(visitor, provider);
    }
    
    public class DdsAadcTransformColumns extends RadixObjects<DdsAadcTransformColumn>{

        public DdsAadcTransformColumns(DdsAadcTransformTable container) {
            super(container);
        }
    }
    
}
