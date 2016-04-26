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

package org.radixware.kernel.common.defs.ads.clazz.sql;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.radixdoc.SqlClassRadixdoc;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

/**
 * ADS SQL Cursor Class Definition.
 *
 */
public class AdsCursorClassDef extends AdsSqlClassDef {

    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.Cursor";
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("pdcCursor____________________");

    public static class Factory {

        public static AdsCursorClassDef loadFrom(final org.radixware.schemas.adsdef.ClassDefinition xClassDef) {
            return new AdsCursorClassDef(xClassDef);
        }

        public static AdsCursorClassDef newInstance() {
            final AdsCursorClassDef cursor = new AdsCursorClassDef("NewSqlCursorClass");
            final AdsSystemMethodDef methodOpen = AdsSystemMethodDef.Factory.newExecuteStmt(cursor.getClassDefType());
            cursor.getMethods().getLocal().add(methodOpen);
            return cursor;
        }
    }
    private boolean uniDirect = true;

    protected AdsCursorClassDef(final org.radixware.schemas.adsdef.ClassDefinition xClassDef) {
        super(xClassDef);
        final org.radixware.schemas.adsdef.SqlStatement xStatement = xClassDef.getSql();
        if (xStatement != null) {
            if (xStatement.isSetIsUniDirect()) {
                uniDirect = xStatement.getIsUniDirect();
            }
            if (!xStatement.isSetIsReadOnly()) {
                setDbReadOnly(true);
            }
        }
    }

    @Override
    public void appendTo(final org.radixware.schemas.adsdef.ClassDefinition xClassDef, ESaveMode saveMode) {
        super.appendTo(xClassDef, saveMode);
        final org.radixware.schemas.adsdef.SqlStatement xStatement = xClassDef.getSql();
        if (saveMode == ESaveMode.NORMAL) {
            if (!uniDirect) {
                xStatement.setIsUniDirect(uniDirect);
            }
        }
    }

    protected AdsCursorClassDef(final String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_SQL_CURSOR_CLASS), name);
    }

    public AdsCursorClassDef(AdsCursorClassDef source) {
        super(source);
    }

    /**
     * @return true if cursor SQL query is not modified database.
     */
    @Override
    public boolean isDbReadOnly() {
        return super.isDbReadOnly();
    }

    /**
     * For runtime optimization.
     *
     * @return true if cursor is unidirectional, false otherwise.
     */
    public boolean isUniDirect() {
        return uniDirect;
    }

    @Override
    public void setDbReadOnly(boolean dbReadOnly) {
        super.setDbReadOnly(dbReadOnly);
    }

    public void setUniDirect(boolean uniDirect) {
        if (!Utils.equals(this.uniDirect, uniDirect)) {
            this.uniDirect = uniDirect;
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.SQL_CURSOR;
    }
    private static final String ADS_CURSOR_TYPE_TITLE = "Cursor Class";
    private static final String ADS_CURSOR_TYPES_TITLE = "Cursor Classes";

    @Override
    public String getTypeTitle() {
        return ADS_CURSOR_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ADS_CURSOR_TYPES_TITLE;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_CURSOR;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new SqlClassRadixdoc(getSource(), page, options) {
                    @Override
                    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
                        super.writeClassDefInfo(overview, overviewTable);
                        AdsCursorClassDef cursorDef = (AdsCursorClassDef) source;
                        getClassWriter().addStr2BoolRow(overviewTable, "Unidirectional", cursorDef.isUniDirect());
                    }
                };
            }
        };
    }
}
