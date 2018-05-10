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

import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EAadcTransformColumnSourceType;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.product.AadcTransformColumn;

public class DdsAadcTransformColumn extends RadixObject {
    private Id targetId;
    private String targetDbName;
    private EAadcTransformColumnSourceType type;
    private String source;

    public DdsAadcTransformColumn() {
        this.type = EAadcTransformColumnSourceType.COLUMN;
    }

    protected DdsAadcTransformColumn(AadcTransformColumn xAadcTransformColumn) {
        this.targetId = xAadcTransformColumn.getTargetId();
        this.targetDbName = xAadcTransformColumn.getTargetName();
        this.type = xAadcTransformColumn.getSourceType();
        if (xAadcTransformColumn.isSetSource()){
            this.source = xAadcTransformColumn.getSource();
        }
    }
    
    public void loadFrom(DdsAadcTransformColumn aadcTransformColumn) {
        setTargetId(aadcTransformColumn.getTargetId());
        setTargetDbName(aadcTransformColumn.getTargetDbName());
        setType(aadcTransformColumn.getType());
        setSource(aadcTransformColumn.getSource());
    }

    protected void appendTo(AadcTransformColumn xAadcTransformColumn) {
        xAadcTransformColumn.setTargetId(targetId);
        xAadcTransformColumn.setTargetName(targetDbName);
        xAadcTransformColumn.setSourceType(type);
        if (source != null && !source.isEmpty()){
            xAadcTransformColumn.setSource(source);
        }
    }

    public Id getTargetId() {
        return targetId;
    }

    public void setTargetId(Id targetId) {
        if (!Utils.equals(this.targetId, targetId)) {
            this.targetId = targetId;
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getTargetDbName() {
        return targetDbName;
    }

    public void setTargetDbName(String targetDbName) {
        if (!Utils.equals(this.targetDbName, targetDbName)) {
            this.targetDbName = targetDbName;
            setEditState(EEditState.MODIFIED);
        }
    }

    public EAadcTransformColumnSourceType getType() {
        return type;
    }

    public void setType(EAadcTransformColumnSourceType type) {
        if (!Utils.equals(this.type, type)) {
            this.type = type;
            setEditState(EEditState.MODIFIED);
        }
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        if (!Utils.equals(this.source, source)) {
            this.source = source;
            setEditState(EEditState.MODIFIED);
        }
    }
    
    public DdsAadcTransformTable getOwnerAadcTransformTable() {        
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof DdsAadcTransformTable) {
                return (DdsAadcTransformTable) owner;
            }
        }
        return null;
    }
    
    public DdsColumnDef findColumn() {
        return findColumn(getLayer());
    }
    
    public DdsColumnDef findColumn(Layer layer) {
        DdsAadcTransformTable container = getOwnerAadcTransformTable();
        if (container != null) {
            DdsTableDef table = container.findTable(layer);
            if (table != null) {
                return table.getColumns().findById(targetId, ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Target Column Name=");
        sb.append(targetDbName).append(", type=").append(type);
        if (source != null && !source.isEmpty()) {
            switch (type) {
                case COLUMN:
                    sb.append(", Source Column Name=");
                    break;
                case CONST:
                    sb.append(", Value=");
                    break;
                case EXPRESSION:
                    sb.append(", OGG Expression=");
            }
            sb.append(source);
        }
        return sb.toString();
    }

//    @Override
//    public ClipboardSupport<DdsAadcTransformColumn> getClipboardSupport() {
//        return new DdsAadcTransformColumnClipboardSupport();
//    }
//    
//    
//    private class DdsAadcTransformColumnClipboardSupport extends ClipboardSupport<DdsAadcTransformColumn> {
//
//        public DdsAadcTransformColumnClipboardSupport() {
//            super(DdsAadcTransformColumn.this);
//        }
//
//        @Override
//        protected XmlObject copyToXml() {
//            AadcTransformColumn xAadcTransformColumn = AadcTransformColumn.Factory.newInstance();
//            DdsAadcTransformColumn.this.appendTo(xAadcTransformColumn);
//            return xAadcTransformColumn;
//        }
//
//        @Override
//        protected DdsAadcTransformColumn loadFrom(XmlObject xmlObject) {
//            AadcTransformColumn xAadcTransformColumn = (AadcTransformColumn) xmlObject;
//            return loadFrom(xAadcTransformColumn);
//        }
//    }

}
