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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.MethodDefinition;
import org.radixware.schemas.adsdef.ParameterDeclaration;

/**
 * Parameter set for Ads Method definition
 *
 */
public class AdsMethodParameters extends RadixObjects<MethodParameter> {

    /**
     * Parameter set factory. Provides Creation and loading of method parameters
     */
    public static final class Factory {

        public static AdsMethodParameters loadFrom(AdsMethodDef context, MethodDefinition.Parameters xParam) {
            if (xParam == null) {
                return newInstance(context);
            }
            List<ParameterDeclaration> decls = xParam.getParameterList();
            if (decls != null && decls.size() > 0) {
                AdsMethodParameters params = new AdsMethodParameters(context);
                for (ParameterDeclaration decl : decls) {
                    params.add(MethodParameter.Factory.loadFrom(params,decl));
                }
                return params;
            } else {
                return newInstance(context);
            }
        }

        public static AdsMethodParameters newInstance(AdsMethodDef context) {
            return new AdsMethodParameters(context);
        }
    }

    private AdsMethodParameters(AdsMethodDef context) {
        super(context);
    }

    public void appendTo(MethodDefinition.Parameters xDef) {
        for (MethodParameter p : this) {
            p.appendTo(xDef.addNewParameter());
        }
    }

    void printProfileString(StringBuilder b, boolean qualifiedNames) {
        printProfileString(getOwnerMethod(), qualifiedNames, this, b);
    }

    public MethodParameter findById(Id id) {
        for (MethodParameter p : this) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    boolean printCommaSeparatedTypeNames(StringBuilder b) {
        AdsMethodParameters params = this;
        int size = this.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                b.append(',');
            }
            MethodParameter p = params.get(i);
            AdsTypeDeclaration type = p.getType();
            if (type == null) {
                return false;
            }
            AdsType resolvedType = type.resolve(getOwnerMethod()).get();
            if (resolvedType == null) {
                return false;
            }
            b.append(resolvedType.getFullJavaClassName(UsagePurpose.getPurpose(getOwnerMethod().getUsageEnvironment(), CodeType.EXCUTABLE)));
        }

        return true;
    }

    private static void printProfileString(AdsDefinition context, boolean qualifiedTypeNames, AdsMethodParameters params, StringBuilder b) {
        if (params.isEmpty()) {
            return;
        }
        int size = params.size();
        AdsTypeDeclaration[] types = new AdsTypeDeclaration[size];
        String[] names = new String[size];

        for (int i = 0; i < size; i++) {
            MethodParameter p = params.get(i);
            types[i] = p.getType();
            names[i] = p.getName();
        }

        printProfileString(context, qualifiedTypeNames, types, names, params.get(size - 1).isVariable(), b);
    }

    public static void printProfileHtml(Definition context, boolean qualifiedTypeNames, AdsMethodParameters params, StringBuilder b) {
        if (params.isEmpty()) {
            return;
        }
        int size = params.size();
        AdsTypeDeclaration[] types = new AdsTypeDeclaration[size];
        String[] names = new String[size];

        for (int i = 0; i < size; i++) {
            MethodParameter p = params.get(i);
            types[i] = p.getType();
            names[i] = p.getName();
        }

        printProfileHtml(context, qualifiedTypeNames, types, names, params.get(size - 1).isVariable(), b);
    }

    private static void printProfileString(AdsDefinition context, boolean qualifiedTypeNames,
            AdsTypeDeclaration[] types, String[] names, boolean varArgs, StringBuilder b) {

        if (types.length == 0) {
            return;
        }
        boolean useGivenNames = names != null && names.length == types.length;
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                b.append(',');
                b.append(' ');
            }

            if (varArgs && i == types.length - 1) {
                b.append(qualifiedTypeNames ? getActualType(types[i], true).getQualifiedName(context) : getActualType(types[i], true).getName(context));
                b.append("...");
            } else {
                b.append(qualifiedTypeNames ? types[i].getQualifiedName(context) : types[i].getName(context));
            }
            b.append(' ');
            b.append(useGivenNames ? names[i] : "arg" + String.valueOf(i));
        }
    }

    public static void printProfileHtml(Definition context, boolean qualifiedTypeNames,
            AdsTypeDeclaration[] types, String[] names, boolean varArgs, StringBuilder b) {

        if (types.length == 0) { 
            return;
        }
        boolean useGivenNames = names != null && names.length == types.length;
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                b.append(',');
                b.append(' ');
            }
            if (i == types.length - 1 && varArgs) {
                b.append(getActualType(types[i], true).getHtmlName(context, qualifiedTypeNames));
                if (varArgs && i == types.length - 1) {
                    b.append("...");
                }
            } else {
                b.append(types[i].getHtmlName(context, qualifiedTypeNames));
            }

            b.append(' ');
            b.append("<font color=\"d3730b\">");
            b.append(useGivenNames ? names[i] : "arg" + String.valueOf(i));
            b.append("</font>");
        }
    }

    private static AdsTypeDeclaration getActualType(AdsTypeDeclaration type, boolean varArgs) {
        if (varArgs && type.isArray()) {
            return type.toArrayType(type.getArrayDimensionCount() - 1);
        }
        return type;
    }

    private AdsMethodDef getOwnerMethod() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsMethodDef) {
                return (AdsMethodDef) owner;
            }
        }
        return null;
    }
}
