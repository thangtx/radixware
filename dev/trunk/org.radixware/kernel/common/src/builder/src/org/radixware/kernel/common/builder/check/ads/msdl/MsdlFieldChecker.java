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
package org.radixware.kernel.common.builder.check.ads.msdl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.msdl.fields.ChoiceFieldModel;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.msdl.ChoiceField;

@RadixObjectCheckerRegistration
public class MsdlFieldChecker extends RadixObjectChecker<MsdlField> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return MsdlField.class;
    }

    @Override
    public void check(MsdlField field, IProblemHandler problemHandler) {
        super.check(field, problemHandler);
        field.check(problemHandler);
        Id enumId = field.getReferncedEnumId();
        if (enumId != null) {
            Definition possibleEnum = field.findReferencedEnum();
            if (possibleEnum instanceof AdsEnumDef) {
                final AdsEnumDef enumeration = (AdsEnumDef) possibleEnum;
                if (field.getType() == EFieldType.INT && enumeration.getItemType() != EValType.INT) {
                    error(field, problemHandler, "Invalid type of referenced enumeration " + enumeration.getQualifiedName() + ": " + enumeration.getItemType().getName());
                }
                if (field.getType() == EFieldType.STR && enumeration.getItemType() == EValType.INT) {
                    error(field, problemHandler, "Invalid type of referenced enumeration " + enumeration.getQualifiedName() + ": " + enumeration.getItemType().getName());
                }
                AdsUtils.checkAccessibility(field, enumeration, false, problemHandler);
                CheckUtils.checkExportedApiDatails(field, enumeration, problemHandler);
            } else {
                error(field, problemHandler, "Can not find referenced enumeration #" + enumId);
            }
            
            checkEnumFieldNameUniqueness(field, problemHandler);
        }
        
        checkPreprocessorMethods(field, problemHandler);
        
        if (getCheckOptions().isCheckDocumentation()) {
            if (field.getParentMsdlField() != null && Utils.emptyOrNull(field.getDescription())) {
                warning(field, problemHandler, "Field '" + field.getName() + "' has no description");
            }
        }
    }
    
    
    private void checkPreprocessorMethods(MsdlField field, IProblemHandler problemHandler) {
        MsdlFieldCheckHistory hist = getCheckHistory(field);
        final Set<Id> alreadyChecked = hist.checkedPreprocessorMethods;
        final Set<Id> methodsToCheck = new HashSet<>();
        if (field.getModel().getField().getParseFunctionName() != null) {
            methodsToCheck.add(Id.Factory.loadFrom(field.getModel().getField().getParseFunctionName()));
        }
        if (field.getModel().getField().getMergeFunctionName() != null) {
            methodsToCheck.add(Id.Factory.loadFrom(field.getModel().getField().getMergeFunctionName()));
        }
        if (field.getModel() instanceof ChoiceFieldModel) {
            final String selectorMethodName = ((ChoiceField) field.getModel().getField()).getSelectorAdvisorFunctionName();
            if (selectorMethodName != null) {
                methodsToCheck.add(Id.Factory.loadFrom(selectorMethodName));
            }
        }
        
        methodsToCheck.removeAll(alreadyChecked);

        if (!methodsToCheck.isEmpty()) {
            AdsClassDef adsClassDef = null;
            final String preprocessorClassId = field.getRootMsdlScheme().getPreprocessorClassGuid();
            if (preprocessorClassId != null && !preprocessorClassId.isEmpty()) {
                final Definition def = field.findDefinition(Id.Factory.loadFrom(preprocessorClassId));
                if (def instanceof AdsClassDef) {
                    adsClassDef = (AdsClassDef) def;
                    for (Id methodId : methodsToCheck) {
                        if (adsClassDef.findComponentDefinition(methodId).get() == null) {
                            error(field, problemHandler, "Can not find referenced MSDL preprocessor method #" + methodId);
                        }
                    }
                }
            }
            if (adsClassDef == null && preprocessorClassId != null) {
                error(field, problemHandler, "MSDL preprocessor method specified, but preprocessor class not found: " + preprocessorClassId);
            }
            alreadyChecked.addAll(methodsToCheck);
        }
    }
    
    private void checkEnumFieldNameUniqueness(MsdlField field, IProblemHandler problemHandler) {
        MsdlFieldCheckHistory hist = getCheckHistory(field);
        if (hist.fieldName2QName.containsKey(field.getName())) {
            error(field, problemHandler, "Found enum based fields with same name: " + field.getQualifiedName() + " and " + hist.fieldName2QName.get(field.getName()));
        } else {
            hist.fieldName2QName.put(field.getName(), field.getQualifiedName());
        }
    }
    
    private MsdlFieldCheckHistory getCheckHistory(final MsdlField field) {
        MsdlFieldCheckHistory hist = getHistory().findItemByClass(MsdlFieldCheckHistory.class);
        if (hist == null) {
            hist = new MsdlFieldCheckHistory();
            getHistory().registerItemByClass(hist);
        }
        if (hist.curScheme != field.getRootMsdlScheme()) {
            hist.curScheme = field.getRootMsdlScheme();
            hist.fieldName2QName.clear();
            hist.checkedPreprocessorMethods.clear();
        }
        return hist;
    }
    
    
    private static class MsdlFieldCheckHistory {
        
        public RootMsdlScheme curScheme;
        public final Map<String, String> fieldName2QName = new HashMap<>();
        public final Set<Id> checkedPreprocessorMethods = new HashSet<>();
    }
}
