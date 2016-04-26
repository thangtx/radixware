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

import java.io.IOException;
import java.util.Collection;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsConstraintDef;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsPlSqlObjectDef;
import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsViewDef;
import org.radixware.kernel.common.defs.dds.IDdsDbDefinition;
import org.radixware.kernel.common.defs.dds.IDdsTableItemDef;
import org.radixware.kernel.common.defs.dds.providers.DdsVisitorProvider;
import org.radixware.kernel.common.repository.dds.DdsSegment;


public class ScriptDefinitionsCollector {

    private static class VisitorProvider extends DdsVisitorProvider {

        @Override
        public boolean isTarget(RadixObject object) {
            if (object instanceof IDdsDbDefinition) {
                IDdsDbDefinition dbDefinition = (IDdsDbDefinition) object;
                if (!dbDefinition.isGeneratedInDb()) {
                    return false;
                }
            }

            if (object instanceof DdsColumnDef) {
                final DdsColumnDef column = (DdsColumnDef) object;
                if (column.getOwnerTable() instanceof DdsViewDef) {
                    return false;
                }
            }

            if (object instanceof DdsTableDef) {
                DdsTableDef table = (DdsTableDef) object;
                if (table.isOverwrite()) {
                    return false;
                }
            }

            return (object instanceof DdsModelDef) ||
                    (object instanceof DdsTableDef) ||
                    (object instanceof IDdsTableItemDef) ||
                    (object instanceof DdsConstraintDef) ||
                    (object instanceof DdsSequenceDef) ||
                    (object instanceof DdsPlSqlObjectDef);
        }
    }

    private static class Visitor implements IVisitor {

        private final Collection<DdsDefinition> scriptDefinitions;

        public Visitor(Collection<DdsDefinition> scriptDefinitions) {
            this.scriptDefinitions = scriptDefinitions;
        }

        @Override
        public void accept(RadixObject radixObject) {
            DdsDefinition ddsDefinition = (DdsDefinition) radixObject;
            scriptDefinitions.add(ddsDefinition);
        }
    }

    /**
     * Собрать дефиниции в пределах указанного контекста.
     */
    public static void collect(RadixObject context, Collection<DdsDefinition> scriptDefinitions) {
        VisitorProvider provider = new ScriptDefinitionsCollector.VisitorProvider();
        Visitor visitor = new ScriptDefinitionsCollector.Visitor(scriptDefinitions);
        context.visit(visitor, provider);
    }

    /**
     * Собрать модифицированные и захваченные дефиниции DDS моделей указанного модуля.
     */
    public static void collect(DdsModule module, Collection<DdsDefinition> fixedScriptDefinitions, Collection<DdsDefinition> modifiedScriptDefinitions) throws IOException {
        DdsModelDef model = module.getModelManager().getModel();
        DdsModelDef fixedModel = module.getModelManager().getFixedModel();
        if (model != null && fixedModel != null && model != fixedModel) {
            // собираются только дефиниции захваченных моделей, у остальных ничего не поменялось, скрипт модификаций будет пустой
            // если SQML ссылается на переименованную дефиниции, будет ошибка проверки (sql устареет), прийдется захватить.
            collect(fixedModel, fixedScriptDefinitions);
            collect(model, modifiedScriptDefinitions);
        }
    }

    /**
     * Собрать модифицированные и захваченные дефиниции DDS моделей указанного сегмента (без подсегментов).
     */
    public static void collect(DdsSegment segment, Collection<DdsDefinition> fixedScriptDefinitions, Collection<DdsDefinition> modifiedScriptDefinitions) throws IOException {
        for (DdsModule ddsModule : segment.getModules()) {
            collect(ddsModule, fixedScriptDefinitions, modifiedScriptDefinitions);
        }
//        Layer baseLayer = segment.getLayer().findPrevLayer();
//        if (baseLayer != null) {
//            DdsSegment baseSegment = (DdsSegment) baseLayer.getDds();
//            collect(baseSegment, fixedScriptDefinitions, modifiedScriptDefinitions);
//        }
    }
}
