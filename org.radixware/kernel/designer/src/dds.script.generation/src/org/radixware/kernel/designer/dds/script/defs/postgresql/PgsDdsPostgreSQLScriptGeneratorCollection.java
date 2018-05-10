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
package org.radixware.kernel.designer.dds.script.defs.postgresql;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsPackageDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlPartDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsTypeDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.designer.dds.script.DefinitionPair;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IDdsScriptGeneratorCollection;

public class PgsDdsPostgreSQLScriptGeneratorCollection implements IDdsScriptGeneratorCollection{
    public PgsDdsPostgreSQLScriptGeneratorCollection(){}

    @Override
    public EDatabaseType getDatabaseType() {
        return EDatabaseType.POSTGRESQL;
    }

    @Override
    public Map getScriptsCollection() {
        final Map<Class<? extends DdsDefinition>,IDdsDefinitionScriptGenerator> result = new HashMap<>();
        
        result.put(DdsViewDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsViewScriptGenerator.Factory.newInstance());
        result.put(DdsTableDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsTableScriptGenerator.Factory.newInstance());
        result.put(DdsPlSqlHeaderDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsPlSqlHeaderScriptGenerator.Factory.newInstance());
        result.put(DdsPlSqlBodyDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsPlSqlBodyScriptGenerator.Factory.newInstance());
        result.put(DdsReferenceDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsReferenceScriptGenerator.Factory.newInstance());
        result.put(DdsColumnDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsColumnScriptGenerator.Factory.newInstance());
        result.put(DdsIndexDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsIndexScriptGenerator.Factory.newInstance());
        result.put(DdsPrimaryKeyDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsPrimaryKeyScriptGenerator.Factory.newInstance());
        result.put(DdsUniqueConstraintDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsUniqueConstraintScriptGenerator.Factory.newInstance());
        result.put(DdsCheckConstraintDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsCheckConstraintScriptGenerator.Factory.newInstance());
        result.put(DdsTriggerDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsTriggerScriptGenerator.Factory.newInstance());
        result.put(DdsSequenceDef.class, org.radixware.kernel.designer.dds.script.defs.postgresql.PgsDdsSequenceScriptGenerator.Factory.newInstance());
        
        return result;
    }

    @Override
    public Comparator getOrderingComparator() {
        return new PostgreSQLCreateOrderComparator();
    }

    private static class PostgreSQLCreateOrderComparator implements Comparator<DefinitionPair> {

        private int getPriority(DefinitionPair pair) {
            final DdsDefinition definition = pair.getAnyDefinition();

            if (definition instanceof DdsSequenceDef) {
                return 1;
            }
            if (definition instanceof DdsTableDef && !(definition instanceof DdsViewDef)) {
                return 2;
            }
            if (definition instanceof DdsColumnDef) {
                DdsColumnDef column = (DdsColumnDef) definition;
                if (column.getExpression() == null) {
                    return 3;
                } else {
                    return 4;
                }
            }
            if (definition instanceof DdsIndexDef) {
                return 5;
            }
            if (definition instanceof DdsUniqueConstraintDef) {
                return 6;
            }
            if (definition instanceof DdsReferenceDef) {
                return 7;
            }
            if (definition instanceof DdsCheckConstraintDef) {
                return 8;
            }
            if (definition instanceof DdsViewDef) {
                return 9;
            }
            
            if ((definition instanceof DdsPlSqlHeaderDef) && (((DdsPlSqlHeaderDef) definition).getPlSqlObjectDef() instanceof DdsTypeDef)) {
                return 10;
            }
            if ((definition instanceof DdsPlSqlHeaderDef) && (((DdsPlSqlHeaderDef) definition).getPlSqlObjectDef() instanceof DdsPackageDef)) {
                return 11;
            }
            if ((definition instanceof DdsPlSqlBodyDef) && (((DdsPlSqlBodyDef) definition).getPlSqlObjectDef() instanceof DdsTypeDef)) {
                return 12;
            }
            if ((definition instanceof DdsPlSqlBodyDef) && (((DdsPlSqlBodyDef) definition).getPlSqlObjectDef() instanceof DdsPackageDef)) {
                return 13;
            }
            
            if (definition instanceof DdsTriggerDef) {
                return 14;
            }
            if (definition instanceof DdsModelDef) {
                return 17;
            }
            throw new IllegalStateException("Illegal object in " + this.getClass().getName() + ": " + String.valueOf(definition));
        }

        private String getComparableName(DdsDefinition def) {
            if (def instanceof DdsPlSqlPartDef) {
                return ((DdsPlSqlPartDef) def).getPlSqlObjectDef().getDbName();
            } else {
                return (def instanceof IDdsDbDefinition ? ((IDdsDbDefinition) def).getDbName() : def.getName());
            }
        }
        
        @Override
        public int compare(DefinitionPair pair1, DefinitionPair pair2) {
            final int p1 = getPriority(pair1);
            final int p2 = getPriority(pair2);
            final int result = p1 - p2;
            
            if (result == 0) {
                if (p1 == 9) {//DdsViewDef
                    //look for dependences
                    final DdsDefinition def1 = pair1.getNewDefinition();
                    final DdsDefinition def2 = pair2.getNewDefinition();
                    if (def1 instanceof DdsViewDef && def2 instanceof DdsViewDef) {
                        if (((DdsViewDef) def1).isDependsFromOtherView(def2.getId())) {
                            return 1;
                        } else if (((DdsViewDef) def2).isDependsFromOtherView(def1.getId())) {
                            return -1;                            
                        }
                    }
                }

                final DdsDefinition def1 = pair1.getAnyDefinition();
                final DdsDefinition def2 = pair2.getAnyDefinition();
                final String name1 = getComparableName(def1);
                final String name2 = getComparableName(def2);
                return name1.compareTo(name2);
            }
            return result;
        }
        
        @Override
        public String toString() {
            return "PostgreSQLCreateOrderComparator{" + '}';
        }
    }
}
