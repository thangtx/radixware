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

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import org.radixware.kernel.common.defs.dds.DdsCheckConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsIndexDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlBodyDef;
import org.radixware.kernel.common.defs.dds.DdsPlSqlHeaderDef;
import org.radixware.kernel.common.defs.dds.DdsPrimaryKeyDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.dds.DdsUniqueConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.enums.EDatabaseType;
import org.radixware.kernel.common.exceptions.DefinitionError;

class MultiBaseScriptGeneratorRepo {
    private static final ServiceLoader<IDdsScriptGeneratorCollection> loader = ServiceLoader.load(IDdsScriptGeneratorCollection.class,MultiBaseScriptGeneratorRepo.class.getClassLoader());
    private static final Set<Class<? extends DdsDefinition>> generators = new HashSet<Class<? extends DdsDefinition>>(){{
                                                                add(DdsViewDef.class);              add(DdsTableDef.class);
                                                                add(DdsPlSqlHeaderDef.class);       add(DdsPlSqlBodyDef.class);
                                                                add(DdsReferenceDef.class);         add(DdsColumnDef.class);
                                                                add(DdsIndexDef.class);             add(DdsPrimaryKeyDef.class);
                                                                add(DdsUniqueConstraintDef.class);  add(DdsCheckConstraintDef.class);
                                                                add(DdsTriggerDef.class);           add(DdsSequenceDef.class);
                                                                add(DdsModelDef.class);
                                                                }};
    
    private final HashMap<Class<? extends DdsDefinition>, IDdsDefinitionScriptGenerator> ddsDefinitionClass2Generator = new HashMap<>();
    private final Map<EDatabaseType,Comparator<DefinitionPair>> ddsOrderingComparator = new HashMap<>();
    
    public MultiBaseScriptGeneratorRepo() {
        final Map<EDatabaseType,Map<Class<? extends DdsDefinition>,IDdsDefinitionScriptGenerator<?>>> availableGenerators = new EnumMap<>(EDatabaseType.class);
        
        for (IDdsScriptGeneratorCollection item : loader) {
            availableGenerators.put(item.getDatabaseType(),item.getScriptsCollection());
            ddsOrderingComparator.put(item.getDatabaseType(),item.getOrderingComparator());
        }
        for (Class<? extends DdsDefinition> item : generators) {
            final MultiBaseScriptGenerator gen = new MultiBaseScriptGenerator();
            
            for (Map.Entry<EDatabaseType,Map<Class<? extends DdsDefinition>,IDdsDefinitionScriptGenerator<?>>> entity : availableGenerators.entrySet()) {
                if (entity.getValue().containsKey(item)) {
                    gen.add(entity.getKey(),entity.getValue().get(item));
                }
            }
            registerScriptGenerator(item,gen);
        }
    }

    public final <T extends DdsDefinition> IDdsDefinitionScriptGenerator<T> getDefinitionScriptGenerator(final Class<T> definition) {
        if (definition == null) {
            throw new IllegalArgumentException("Definition class can't be null");
        }
        else if (!this.ddsDefinitionClass2Generator.containsKey(definition)) {
            throw new DefinitionError("Unsupported definition type ["+definition.getName()+"] in MultiBaseScriptGeneratorRepo");
        }
        else {
            return this.ddsDefinitionClass2Generator.get(definition);
        }
    }
    
    public final Map<EDatabaseType,Comparator<DefinitionPair>> getOrderingComparators() {
        return ddsOrderingComparator;
    }

    private <T extends DdsDefinition> void registerScriptGenerator(Class<T> ddsDefinitionClass, IDdsDefinitionScriptGenerator<T> definitionScriptGenerator) {
        ddsDefinitionClass2Generator.put(ddsDefinitionClass, definitionScriptGenerator);
    }
}
