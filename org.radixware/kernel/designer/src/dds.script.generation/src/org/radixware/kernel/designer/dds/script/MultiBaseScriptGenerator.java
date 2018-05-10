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

package org.radixware.kernel.designer.dds.script;

import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.scml.CodePrinter;


class MultiBaseScriptGenerator<T extends DdsDefinition> implements IDdsDefinitionScriptGenerator<T>{
    private final Map<EDatabaseType,IDdsDefinitionScriptGenerator<T>> components = new HashMap<>();
    
    public MultiBaseScriptGenerator() {
    }

    public MultiBaseScriptGenerator(final EDatabaseType database, final IDdsDefinitionScriptGenerator<T> generator) {
        internalAdd(database,generator);
    }
    
    public void add(final EDatabaseType database, final IDdsDefinitionScriptGenerator<T> generator) {
        internalAdd(database,generator);
    }

    @Override
    public boolean isModifiedToDrop(final DdsDefinition oldDefinition, final DdsDefinition newDefinition) {
        return components.get(EDatabaseType.ORACLE).isModifiedToDrop((T)oldDefinition,(T)newDefinition);
    }

    @Override
    public void getDropScript(final CodePrinter cp, final DdsDefinition definition) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("DDS definition can't be null");
        }
        else if (components.size() > 1) {   // Multibase
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                if (cp.getProperty(CodePrinter.DATABASE_TYPE) == null || cp.getProperty(CodePrinter.DATABASE_TYPE) == item.getKey()) {
                    final CodePrinter specific = CodePrinter.Factory.newSqlPrinter(cp,item.getKey());

                    specific.print("\n#IF DB_TYPE == \"").print(item.getKey().toString()).println("\" THEN");
                    item.getValue().getDropScript(specific,(T)definition);
                    specific.println("\n#ENDIF");
                }
            }
        }
        else {
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                item.getValue().getDropScript(cp,(T)definition);
            }
        }
    }

    @Override
    public void getCreateScript(final CodePrinter cp, final DdsDefinition definition, final IScriptGenerationHandler handler) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("DDS definition can't be null");
        }
        else if (components.size() > 1) {   // Multibase
            if (cp.getProperty(CodePrinter.DATABASE_TYPE) == null) {
                for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                    final CodePrinter specific = CodePrinter.Factory.newSqlPrinter(cp,item.getKey());

                    specific.print("\n#IF DB_TYPE == \"").print(item.getKey().toString()).println("\" THEN");
                    item.getValue().getCreateScript(specific,(T)definition,handler);
                    specific.println("\n#ENDIF");
                }
            }
            else {
                components.get((EDatabaseType)cp.getProperty(CodePrinter.DATABASE_TYPE)).getCreateScript(cp,(T)definition,handler);
            }
        }
        else {
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                item.getValue().getCreateScript(cp,(T)definition,handler);
            }
        }
    }

    @Override
    public void getAlterScript(final CodePrinter cp, final DdsDefinition oldDefinition, final DdsDefinition newDefinition) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (oldDefinition == null) {
            throw new IllegalArgumentException("Old DDS definition can't be null");
        }
        else if (newDefinition == null) {
            throw new IllegalArgumentException("New DDS definition can't be null");
        }
        else if (components.size() > 1) {   // Multibase
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                if (cp.getProperty(CodePrinter.DATABASE_TYPE) == null || cp.getProperty(CodePrinter.DATABASE_TYPE) == item.getKey()) {
                    final CodePrinter specific = CodePrinter.Factory.newSqlPrinter(cp,item.getKey());

                    specific.print("\n#IF DB_TYPE == \"").print(item.getKey().toString()).println("\" THEN");
                    item.getValue().getAlterScript(specific,(T)oldDefinition,(T)newDefinition);
                    specific.println("\n#ENDIF");
                }
            }
        }
        else {
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                item.getValue().getAlterScript(cp,(T)oldDefinition,(T)newDefinition);
            }
        }
    }

    @Override
    public void getRunRoleScript(final CodePrinter cp, final DdsDefinition definition) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("DDS definition can't be null");
        }
        else if (components.size() > 1) {   // Multibase
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                if (cp.getProperty(CodePrinter.DATABASE_TYPE) == null || cp.getProperty(CodePrinter.DATABASE_TYPE) == item.getKey()) {
                    final CodePrinter specific = CodePrinter.Factory.newSqlPrinter(cp,item.getKey());

                    specific.print("\n#IF DB_TYPE == \"").print(item.getKey().toString()).println("\" THEN");
                    item.getValue().getRunRoleScript(specific,(T)definition);
                    specific.println("\n#ENDIF");
                }
            }
        }
        else {
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                item.getValue().getRunRoleScript(cp,(T)definition);
            }
        }
    }    

    @Override
    public void getReCreateScript(final CodePrinter cp, final T definition, final boolean storeData) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("DDS definition can't be null");
        }
        else if (components.size() > 1) {   // Multibase
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                if (cp.getProperty(CodePrinter.DATABASE_TYPE) == null || cp.getProperty(CodePrinter.DATABASE_TYPE) == item.getKey()) {
                    final CodePrinter specific = CodePrinter.Factory.newSqlPrinter(cp,item.getKey());

                    specific.print("\n#IF DB_TYPE == \"").print(item.getKey().toString()).println("\" THEN");
                    item.getValue().getReCreateScript(specific,(T)definition,storeData);
                    specific.println("\n#ENDIF");
                }
            }
        }
        else {
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                item.getValue().getReCreateScript(cp,(T)definition,storeData);
            }
        }
    }

    @Override
    public void getEnableDisableScript(final CodePrinter cp, final T definition, final boolean enable) {
        if (cp == null) {
            throw new IllegalArgumentException("Code printer can't be null");
        }
        else if (definition == null) {
            throw new IllegalArgumentException("DDS definition can't be null");
        }
        else if (components.size() > 1) {   // Multibase
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                if (cp.getProperty(CodePrinter.DATABASE_TYPE) == null || cp.getProperty(CodePrinter.DATABASE_TYPE) == item.getKey()) {
                    final CodePrinter specific = CodePrinter.Factory.newSqlPrinter(cp,item.getKey());

                    specific.print("\n#IF DB_TYPE == \"").print(item.getKey().toString()).println("\" THEN");
                    item.getValue().getEnableDisableScript(specific,(T)definition,enable);
                    specific.println("\n#ENDIF");
                }
            }
        }
        else {
            for (Map.Entry<EDatabaseType, IDdsDefinitionScriptGenerator<T>> item : components.entrySet()) {
                item.getValue().getEnableDisableScript(cp,(T)definition,enable);
            }
        }
    }

    private void internalAdd(final EDatabaseType database, final IDdsDefinitionScriptGenerator<T> generator) {
        if (database == null) {
            throw new IllegalArgumentException("Database type can't be null");
        }
        else if (generator == null) {
            throw new IllegalArgumentException("Generator can't be null");
        }
        else if (components.containsKey(database)) {
            throw new IllegalArgumentException("Database type ["+database+"] already registered in the list!");
        }
        else {
            components.put(database,generator);
        }
    }
}
