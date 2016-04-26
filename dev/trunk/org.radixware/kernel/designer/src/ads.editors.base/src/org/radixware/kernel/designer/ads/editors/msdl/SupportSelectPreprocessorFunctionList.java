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
package org.radixware.kernel.designer.ads.editors.msdl;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.Methods;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.Profile;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodParameters;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodReturnValue;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.msdl.AdsMsdlSchemeDef;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.msdl.ChoiceField;

public class SupportSelectPreprocessorFunctionList {

    AdsClassDef clazz = null;
    MsdlField needField = null;
    boolean isChoice = false;

    public SupportSelectPreprocessorFunctionList(MsdlField field) {
        RootMsdlScheme scheme = field.getFieldModel().getRootMsdlScheme();
        needField = field;
        if (scheme.getPreprocessorClassGuid() != null) {
            Id ident = Id.Factory.loadFrom(scheme.getPreprocessorClassGuid());
            clazz = (AdsClassDef) AdsSearcher.Factory.newAdsDefinitionSearcher(((AdsMsdlSchemeDef) scheme.getOwnerDefinition())).findById(ident).get();
        }
        if (field.getFieldModel().getField() instanceof ChoiceField) {
            isChoice = true;
        }
    }

    public ArrayList<PreprocessorFunctionItem> getFunctionList() {
        ArrayList res = new ArrayList<PreprocessorFunctionItem>();
        Methods methods = clazz.getMethods();
        for (AdsMethodDef m : methods.get(EScope.ALL)) {
            if (m.getProfile().getAccessFlags().isStatic()) {
                appendFunction(res, m);
            }
        }
        return res;
    }

    public boolean isSetClass() {
        return clazz != null;
    }

    private void appendFunction(ArrayList res, AdsMethodDef m) {
        int possibleMethodRole = getMethodRole(m);
        if (possibleMethodRole >= 0) {
            PreprocessorFunctionItem fi = new PreprocessorFunctionItem(m.getName(), m.getId().toString());
            if (possibleMethodRole == 1) {
                fi.isAdvisor = true;
            }
            res.add(fi);
        }
    }

    //contract: 0: parser-merger,1- selector advisor
    private int getMethodRole(AdsMethodDef method) {
        Profile profile = method.getProfile();
        AdsTypeDeclaration returnType = profile.getReturnValue().getType();
        if (returnType == null) {
            return -1;
        }
        boolean isParserMerger = returnType.isBasedOn(EValType.JAVA_TYPE) && "byte".equals(returnType.getExtStr()) && returnType.isArray();
        boolean isSelectorAdvisor = returnType.getTypeId() == EValType.STR || (returnType.isBasedOn(EValType.JAVA_CLASS) && "java.lang.String".equals(returnType.getExtStr()));
        if (isParserMerger || isSelectorAdvisor) {
            AdsMethodParameters params = profile.getParametersList();
            if (params.size() == 1) {
                AdsTypeDeclaration param = params.iterator().next().getType();
                if (!(param.isBasedOn(EValType.JAVA_TYPE) && param.isArray() && "byte".equals(param.getExtStr()))) {
                    return -1;
                }
            }
        }
        if (isParserMerger) {
            return 0;
        }
        if (isSelectorAdvisor && isChoice) {
            return 1;
        }
        return -1;
    }
}
