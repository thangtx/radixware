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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.presentation.PropertyPresentation;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class ColumnInfo {

    private Id columnId;
    private AdsTablePropertyDef property;

    ColumnInfo(AdsTablePropertyDef property, DdsColumnDef column) {
        this.property = property;
        if (column != null) {
            this.columnId = column.getId();
            property.getValue().setType(AdsTypeDeclaration.Factory.newInstance(column.getValType()));
        } else {
            this.columnId = null;
            property.getValue().setType(AdsTypeDeclaration.UNDEFINED);
        }
    }

    ColumnInfo(AdsTablePropertyDef property, Id columnId) {
        this.property = property;
        this.columnId = columnId;
    }

    ColumnInfo(AdsTablePropertyDef property, ColumnInfo source) {
        this.property = property;
        this.columnId = source.columnId;
    }

    public Id getColumnId() {
        return columnId;
    }

    public void setColumnId(Id columnId) {
        if (property instanceof AdsInnateColumnPropertyDef) {
            if (!((AdsInnateColumnPropertyDef) property).changeColumn(columnId)) {
                return;
            }
        }
        this.columnId = columnId;
        DdsColumnDef c = findColumn();
        if (c != null) {
            property.getValue().setType(AdsTypeDeclaration.Factory.newInstance(c.getValType()));
            if (c.isNotNull()) {
                ServerPresentationSupport support = property.getPresentationSupport();
                if (support != null) {
                    PropertyPresentation p = support.getPresentation();
                    if (p != null) {
                        p.getEditOptions().setNotNull(true);
                    }
                }
            }
            ValAsStr defaultVal = null;
            if (c.getDefaultValue() != null) {
                try {
                    defaultVal = c.getDefaultValue().getValAsStr();
                } catch (RadixError e) {
                    Logger.getLogger(ColumnInfo.class.getName()).log(Level.SEVERE, "Error on conversion of default value of column " + c.getQualifiedName(), e);
                }
            }
            if (defaultVal != null) {
                property.getValue().setInitial(ValAsStr.Factory.newCopy(defaultVal));
            }
        }
    }

    protected void appendAdditionalToolTip(StringBuilder sb) {
        DdsColumnDef column = findColumn();
        if (column != null) {
            Utils.appendReferenceToolTipHtml(column, "Table Column", sb);
        } else {
            sb.append("<b><font color=\"#FF0000\">Base table column is not found</font></b>");
        }
    }

    public DdsColumnDef findColumn() {
        DdsTableDef table = findColumnTable();//property.findTable();

        if (table != null) {
            return table.getColumns().findById(columnId, EScope.LOCAL_AND_OVERWRITE).get();
        } else {
            return null;
        }
    }

    public DdsTableDef findColumnTable() {
        DdsTableDef table = property.findTable();
        if (table != null) {
            if (property instanceof DetailProperty) {
                DdsReferenceDef ref = ((DetailProperty) property).getDetailReferenceInfo().findDetailReference();
                if (ref != null) {
                    return ref.findChildTable(property);
                } else {
                    return null;
                }
            } else {
                return table;
            }
        } else {
            return null;
        }
    }

    public boolean isPrimaryKey() {
        DdsColumnDef column = findColumn();
        if (column == null) {
            return false;
        } else {
            return column.isPrimaryKey();
        }
    }

    public void appendTo(PropertyDefinition xProp) {
        if (columnId != null) {
            if (!(property instanceof AdsInnateColumnPropertyDef)) {
                xProp.setOriginalPropertyId(columnId);
            }
        }
    }
}
