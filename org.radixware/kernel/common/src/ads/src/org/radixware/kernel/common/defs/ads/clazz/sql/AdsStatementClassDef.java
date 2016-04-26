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

import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;

/**
 * SQL Statement Class.
 * Fields - only for OUT parameters, synchronized by editor.
 * Close - static, closes all cached static prepared statements, predefined.
 * Execute - dynamic, contains IN parameters, has no result, predefined, parameters are synchronized by editor.
 */
public class AdsStatementClassDef extends AdsSqlClassDef {

    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.Statement";
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("pdcStatement_________________");

    public static final class Factory {

        public static AdsStatementClassDef loadFrom(final org.radixware.schemas.adsdef.ClassDefinition xDef) {
            return new AdsStatementClassDef(xDef);
        }

        public static AdsStatementClassDef newInstance() {
            final AdsStatementClassDef statement = new AdsStatementClassDef("NewSqlStatementClass");
            final AdsSystemMethodDef methodExecute = AdsSystemMethodDef.Factory.newExecuteStmt(statement.getClassDefType());
            final AdsSystemMethodDef methodSetExecuteBatch = AdsSystemMethodDef.Factory.newSetExecuteBatchStmt();
            final AdsSystemMethodDef methodSendBatch = AdsSystemMethodDef.Factory.newSendBatchStmt();
            statement.getMethods().getLocal().add(methodExecute);
            statement.getMethods().getLocal().add(methodSetExecuteBatch);
            statement.getMethods().getLocal().add(methodSendBatch);
            return statement;
        }
    }

    // for overwrite
    protected AdsStatementClassDef(AdsSqlClassDef source) {
        super(source);
    }

    protected AdsStatementClassDef(final org.radixware.schemas.adsdef.ClassDefinition xDef) {
        super(xDef);
    }

    protected AdsStatementClassDef(final String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_SQL_STATEMENT_CLASS), name);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.SQL_STATEMENT;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_STATEMENT;
    }

    private class Transparence extends AdsTransparence {

        public Transparence() {
            super(AdsStatementClassDef.this, AdsStatementClassDef.PLATFORM_CLASS_NAME, true);
        }

        @Override
        public void setIsExtendable(boolean isExtendable) {
            throw new IllegalStateException();
        }

        @Override
        public void setPublishedName(String publishedName) {
            throw new IllegalStateException();
        }
    }
    private Transparence transparence = new Transparence();

    @Override
    public AdsTransparence getTransparence() {
        return transparence;
    }
    private static final String ADS_STAMENENT_TYPE_TITLE = "Statement Class";
    private static final String ADS_STAMENENT_TYPES_TITLE = "Statement Classes";

    @Override
    public String getTypeTitle() {
        return ADS_STAMENENT_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ADS_STAMENENT_TYPES_TITLE;
    }
}
