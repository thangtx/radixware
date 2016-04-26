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
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;

/**
 * SQL Procedure Class Definition.
 */
public class AdsProcedureClassDef extends AdsSqlClassDef {

    public static final class Factory {

        public static AdsProcedureClassDef loadFrom(org.radixware.schemas.adsdef.ClassDefinition xClassDef) {
            return new AdsProcedureClassDef(xClassDef);
        }

        public static AdsProcedureClassDef newInstance() {
            final AdsProcedureClassDef procedure = new AdsProcedureClassDef("NewSqlProcedureClass");
            final AdsSystemMethodDef methodExecute = AdsSystemMethodDef.Factory.newExecuteStmt(procedure.getClassDefType());
            procedure.getMethods().getLocal().add(methodExecute);
            return procedure;
        }
    }

    protected AdsProcedureClassDef(org.radixware.schemas.adsdef.ClassDefinition xClassDef) {
        super(xClassDef);
    }

    protected AdsProcedureClassDef(final String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_SQL_PROCEDURE_CLASS), name);
    }

    protected AdsProcedureClassDef(AdsProcedureClassDef source) {
        super(source);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.SQL_PROCEDURE;
    }
    private static final String ADS_PROCEDURE_TYPE_TITLE = "Procedure Class";
    private static final String ADS_PROCEDURE_TYPES_TITLE = "Procedure Classes";

    @Override
    public String getTypeTitle() {
        return ADS_PROCEDURE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ADS_PROCEDURE_TYPES_TITLE;
    }

    private class Transparence extends AdsTransparence {

        public Transparence() {
            super(AdsProcedureClassDef.this, AdsProcedureClassDef.PLATFORM_CLASS_NAME, true);
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

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_PROCEDURE;
    }
}
