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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef.Parameter;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.enums.EValType;


@RadixObjectCheckerRegistration
public class AdsFilterParameterChecker extends AdsDefinitionChecker<AdsFilterDef.Parameter> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsFilterDef.Parameter.class;
    }

    @Override
    public void check(Parameter definition, IProblemHandler problemHandler) {
        super.check(definition, problemHandler);
        CheckUtils.checkMLStringId(definition, definition.getTitleId(), problemHandler, "title");
        if (definition.getType() != null) {
            definition.getType().check(definition, problemHandler);
            if (!definition.isTypeAllowed(definition.getType().getTypeId())) {
                final EValType type = definition.getType().getTypeId();
                if (type != null) {
                    error(definition, problemHandler, "Invalid type for filter parameter: " + type.getName());
                } else {
                    error(definition, problemHandler, "Invalid type for filter parameter: UNDEFINED");
                }
            }
            final AdsType type = definition.getType().resolve(definition).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(definition, problemHandler));
            if (type instanceof AdsDefinitionType) {
                final Definition def = ((AdsDefinitionType) type).getSource();
                if (def instanceof AdsDefinition) {
                    AdsUtils.checkAccessibility(definition, (AdsDefinition) def, false, problemHandler);
                    CheckUtils.checkExportedApiDatails(definition, (AdsDefinition) def, problemHandler);
                    if (def instanceof AdsEnumDef && definition.getDefaultValue() != null) {
                        final AdsEnumDef enumeration = (AdsEnumDef) def;
                        final AdsEnumItemDef item = enumeration.getItems().findByValue(definition.getDefaultValue().toString(), ExtendableDefinitions.EScope.ALL);
                        if (item == null) {
                            error(definition, problemHandler, "Invalid default value (constant for value " + definition.getDefaultValue().toString() + " not found in " + enumeration.getQualifiedName() + ")");
                        }
                    }
                }
            }
        } else {
            error(definition, problemHandler, "Parameter type is undefined");
        }

        final Parameter.ParameterEditOptions options = definition.getEditOptions();
        if (options != null) {
            AdsEnumDef referencedEnum = null;
            if (definition.getType() != null) {

                final AdsType type = definition.getType().resolve(definition).get(AdsDefinitionChecker.<AdsType>getSearchDuplicatesChecker(definition, problemHandler));

                if (type instanceof AdsEnumType) {
                    referencedEnum = ((AdsEnumType) type).getSource();
                }
            }
            EditOptionsChecker.Factory.newInstance(definition, definition.getType(), referencedEnum).check(options, problemHandler);

        }
    }
}
