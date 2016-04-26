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

package org.radixware.kernel.common.builder.check.dds;

import java.util.HashMap;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.DefinitionChecker;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.dds.*;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils;
import org.radixware.kernel.common.defs.dds.utils.DbNameUtils.CheckResult;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.utils.Utils;


public abstract class DdsDefinitionChecker<T extends DdsDefinition> extends DefinitionChecker<T> {

    private static class DbNames extends HashMap<String, Definition> {

        private static final long serialVersionUID = 0;
    }

    /**
     * Check definition database name for duplication. Must be called for not
     * overwritten definition.
     */
    public void checkForDbNameDuplication(IDdsDbDefinition dbDefinition, IProblemHandler problemHandler) {
        if (!dbDefinition.isGeneratedInDb()) {
            return;
        }

        final String dbName = dbDefinition.getDbName();

        DbNames dbNames = this.getHistory().findItemByClass(DbNames.class);
        if (dbNames == null) {
            dbNames = new DbNames();
            this.getHistory().registerItemByClass(dbNames);
        }

        final Definition oldDefinition = dbNames.get(dbName);
        if (oldDefinition != null && !Utils.equals(oldDefinition.getIdPath(), dbDefinition.getIdPath())) {
            error((Definition) dbDefinition, problemHandler, "Definition database name '" + String.valueOf(dbName) + "' is duplicated with '" + oldDefinition.getQualifiedName() + "'");
        }

        dbNames.put(dbName, (Definition) dbDefinition);
    }

    public void checkDbName(IDdsDbDefinition dbDefinition, IProblemHandler problemHandler) {
        if (!dbDefinition.isGeneratedInDb()) {
            return;
        }

        if (dbDefinition instanceof DdsTableDef) {
            final DdsTableDef table = (DdsTableDef) dbDefinition;
            if (table.findOverwritten() != null) {
                return;
            }
        }

        final String dbName = dbDefinition.getDbName();

        if (!DbNameUtils.isCorrectDbName(dbName)) {
            error((Definition) dbDefinition, problemHandler, "Illegal database name: '" + String.valueOf(dbName) + "'");
        }

        if (!(dbDefinition instanceof DdsFunctionDef)
                && !(dbDefinition instanceof DdsPlSqlObjectDef)
                && !(dbDefinition instanceof DdsParameterDef)
                && !(dbDefinition instanceof DdsTypeFieldDef)
                && !(dbDefinition instanceof DdsTriggerDef)
                && !Utils.equals(dbName.toUpperCase(), dbName)) {
            error((Definition) dbDefinition, problemHandler, "Database name must be in upper case.");
        }

        if (dbDefinition instanceof IDdsAutoDbNamedDefinition) {
            final IDdsAutoDbNamedDefinition autoDbNamedDefinition = (IDdsAutoDbNamedDefinition) dbDefinition;
            if (autoDbNamedDefinition.isAutoDbName()) {
                final String autoDbName = autoDbNamedDefinition.calcAutoDbName();
                if (!Utils.equals(dbName, autoDbName)) {
                    warning((Definition) autoDbNamedDefinition, problemHandler, "Database name doesn't equal automatically generated one");
                }
            }
        }

        if (dbDefinition instanceof Definition && DbNameUtils.isCheckable((Definition) dbDefinition)) {
            final Layer layer = ((Definition) dbDefinition).getLayer();

            final CheckResult result = DbNameUtils.checkRestriction(layer, dbName);

            if (!result.valid) {
                error((Definition) dbDefinition, problemHandler, result.message);
            }
        }
    }

    @Override
    public void check(T definition, IProblemHandler problemHandler) {
        super.check(definition, problemHandler);

        if (definition instanceof IDdsDbDefinition) {
            final IDdsDbDefinition dbDefinition = (IDdsDbDefinition) definition;
            checkDbName(dbDefinition, problemHandler);
        }
        if (definition.getDescriptionId() != null) {
            CheckUtils.checkMLStringId(definition, definition.getDescriptionId(), problemHandler, "description");
        }
    }
}
