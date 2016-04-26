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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.radixdoc.Page;

/**
 * PL/SQL type.
 *
 */
public class DdsTypeDef extends DdsPlSqlObjectDef implements IRadixdocProvider {

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DdsTypeRadixdocSupport((DdsTypeDef) getSource(), page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    private class DdsTypeFields extends DdsDefinitions<DdsTypeFieldDef> {

        public DdsTypeFields() {
            super(DdsTypeDef.this);
        }

        @Override
        protected ClipboardSupport.CanPasteResult canPaste(Transfer objectInClipboard) {
            ClipboardSupport.CanPasteResult result = super.canPaste(objectInClipboard);
            if (result != ClipboardSupport.CanPasteResult.YES) {
                return result;
            }

            return objectInClipboard.getObject() instanceof DdsTypeFieldDef ? ClipboardSupport.CanPasteResult.YES : ClipboardSupport.CanPasteResult.NO;
        }

        @Override
        public RadixIcon getIcon() {
            return DdsDefinitionIcon.TYPE_FIELD;
        }
    }
    private final DdsDefinitions<DdsTypeFieldDef> fields = new DdsTypeFields();

    /**
     * Получить список полей типа в базе данных.
     */
    public DdsDefinitions<DdsTypeFieldDef> getFields() {
        return fields;
    }
    private String dbType = "";

    /**
     * Получить имя типа в базе данных.
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

    protected DdsTypeDef(final String name) {
        super(EDefinitionIdPrefix.DDS_TYPE, name);
    }

    public DdsTypeDef(org.radixware.schemas.ddsdef.Type xType) {
        super(xType);

        this.dbType = xType.getDbType();

        if (xType.isSetFields()) {
            final List<org.radixware.schemas.ddsdef.Type.Fields.Field> xFields = xType.getFields().getFieldList();
            for (org.radixware.schemas.ddsdef.Type.Fields.Field xField : xFields) {
                final DdsTypeFieldDef field = DdsTypeFieldDef.Factory.loadFrom(xField);
                this.fields.add(field);
            }
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsTypeDef newInstance(final String name) {
            return new DdsTypeDef(name);
        }

        public static DdsTypeDef loadFrom(org.radixware.schemas.ddsdef.Type xType) {
            return new DdsTypeDef(xType);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.getFields().visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.TYPE;
    }

    private class DdsTypeClipboardSupport extends DdsPlSqlObjectClipboardSupport<DdsTypeDef> {

        public DdsTypeClipboardSupport() {
            super(DdsTypeDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Type xType = org.radixware.schemas.ddsdef.Type.Factory.newInstance();
            DdsModelWriter.writeType(DdsTypeDef.this, xType);
            return xType;
        }

        @Override
        protected DdsTypeDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Type xType = (org.radixware.schemas.ddsdef.Type) xmlObject;
            return DdsTypeDef.Factory.loadFrom(xType);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsTypeDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Type.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsTypeDef> getClipboardSupport() {
        return new DdsTypeClipboardSupport();
    }
    public static final String TYPE_TYPE_TITLE = "Type";
    public static final String TYPE_TYPES_TITLE = "Types";

    @Override
    public String getTypeTitle() {
        return TYPE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return TYPE_TYPES_TITLE;
    }
}
