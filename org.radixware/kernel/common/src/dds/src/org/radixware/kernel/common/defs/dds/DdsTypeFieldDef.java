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

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;

/**
 * PL/SQL type field.
 *
 */
public class DdsTypeFieldDef extends DdsDefinition implements IDdsDbDefinition {

    /**
     * Get owner PL/SQL type
     */
    public DdsTypeDef getOwnerType() {
        return (DdsTypeDef) getOwnerDefinition();
    }

    @Override
    public String getDbName() {
        return getName();
    }
    private String dbType = "";

    /**
     * Получить тип поля PL/SQL типа в базе данных, for example: VARCHAR2(100
     * char).
     */
    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        if (!Utils.equals(this.dbType, dbType)) {
            this.dbType = dbType;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public boolean isGeneratedInDb() {
        final DdsTypeDef ownerType = getOwnerType();
        return ownerType.isGeneratedInDb();
    }

    protected DdsTypeFieldDef(final String name) {
        super(EDefinitionIdPrefix.DDS_TYPE_FIELD, name);
    }

    public DdsTypeFieldDef(org.radixware.schemas.ddsdef.Type.Fields.Field xField) {
        super(xField);
        this.dbType = xField.getDbType();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsTypeFieldDef newInstance(final String name) {
            return new DdsTypeFieldDef(name);
        }

        public static DdsTypeFieldDef loadFrom(org.radixware.schemas.ddsdef.Type.Fields.Field xField) {
            return new DdsTypeFieldDef(xField);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.TYPE_FIELD;
    }

    @Override
    public String toString() {
        return super.toString()
                + "; DbType: " + String.valueOf(dbType);
    }

    private class DdsTypeFieldClipboardSupport extends DdsClipboardSupport<DdsTypeFieldDef> {

        public DdsTypeFieldClipboardSupport() {
            super(DdsTypeFieldDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Type.Fields.Field xField = org.radixware.schemas.ddsdef.Type.Fields.Field.Factory.newInstance();
            DdsModelWriter.writeTypeField(DdsTypeFieldDef.this, xField);
            return xField;
        }

        @Override
        protected DdsTypeFieldDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Type.Fields.Field xField = (org.radixware.schemas.ddsdef.Type.Fields.Field) xmlObject;
            return DdsTypeFieldDef.Factory.loadFrom(xField);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsTypeFieldDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Type.Fields.Field.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsTypeFieldDef> getClipboardSupport() {
        return new DdsTypeFieldClipboardSupport();
    }
}
