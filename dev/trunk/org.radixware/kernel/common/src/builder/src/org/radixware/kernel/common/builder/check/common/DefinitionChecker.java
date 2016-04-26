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

package org.radixware.kernel.common.builder.check.common;

import java.util.HashMap;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.types.Id;


public abstract class DefinitionChecker<T extends Definition> extends RadixObjectChecker<T> {

    private static class Ids extends HashMap<Id, Definition> {
    }

    private boolean isCheckForIdDuplicationRequired(T definition) {
        if (definition instanceof IOverwritable) {
            final IOverwritable overwritable = (IOverwritable) definition;
            if (overwritable.isOverwrite()) {
                return false;
            }
        }

        if ((definition instanceof AdsDefinition)) {
            if (!(definition.getOwnerDefinition() instanceof Module)) {
                return false; // otherwise too many duplications (alter migration from DBP2), remove when be ready :-(
            }
            if (((AdsDefinition) definition).isIdInherited()) {
                return false;
            }
        }
       

        if (definition instanceof IAdsClassMember) {
            final IAdsClassMember adsClassMember = (IAdsClassMember) definition;
            if (adsClassMember.getOwnerClass() instanceof AdsModelClassDef) { // generated with same id
                return false;
            }
        }

        if (definition instanceof Module) { // checked by loading
            return false;
        }

        if (definition instanceof AdsInnateColumnPropertyDef) { // generated with some id
            return false;
        }

        if (definition instanceof AdsMethodDef) {
            final AdsMethodDef method = (AdsMethodDef) definition;
            if (method.isOverride()) {
                return false;
            }
        }

        if (definition instanceof AdsPropertyDef) {
            final AdsPropertyDef property = (AdsPropertyDef) definition;
            if (property.isOverride()) {
                return false;
            }
        }

        if (definition instanceof AdsSystemMethodDef) { // fixed id
            return false;
        }

        if ((definition instanceof IOverridable) && (definition instanceof AdsDefinition)) {
            final AdsDefinition def = (AdsDefinition) definition;
            if (def.getHierarchy().findOverridden() != null) {
                return false;
            }
        }

        if ((definition instanceof IOverwritable) && (definition instanceof AdsDefinition)) {
            final AdsDefinition def = (AdsDefinition) definition;
            if (def.getHierarchy().findOverwritten().get() != null) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check definition identifier for duplication. Must be called for not
     * overwritten definition.
     */
    private void checkForIdDuplication(T definition, IProblemHandler problemHandler) {
        if (isCheckForIdDuplicationRequired(definition)) {
            Ids ids = this.getHistory().findItemByClass(Ids.class);
            if (ids == null) {
                ids = new Ids();
                this.getHistory().registerItemByClass(ids);
            }

            final Id id = definition.getId();
            final Definition oldDefinition = ids.get(id);
            if (oldDefinition != null && oldDefinition != definition) {
                warning(definition, problemHandler, "Identifier #" + String.valueOf(definition.getId()) + " is duplicated with " + oldDefinition.getTypeTitle() + " '" + oldDefinition.getQualifiedName() + "'");
            }

            ids.put(id, definition);
        } else {
            // check for duplication in the same collection
            if (definition.getContainer() instanceof Definitions) {
                final Definitions definitions = (Definitions) (definition.getContainer());
                if (definitions != null) {
                    final Definition oldDefinition = definitions.findById(definition.getId());
                    if (oldDefinition != null && oldDefinition != definition) {
                        error(definition, problemHandler, "Identifier #" + String.valueOf(definition.getId()) + " is duplicated in the same collection with " + oldDefinition.getTypeTitle() + " '" + oldDefinition.getName() + "'");
                    }
                }
            }
        }
    }

    @Override
    public void check(T definition, IProblemHandler problemHandler) {
        super.check(definition, problemHandler);

        checkForIdDuplication(definition, problemHandler);
    }
}
