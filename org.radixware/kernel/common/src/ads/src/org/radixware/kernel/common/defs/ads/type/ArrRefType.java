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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;


public class ArrRefType extends ParentRefType.EntityObjectType {

    public static final class Factory {

        public static final ArrRefType newInstance(AdsEntityObjectClassDef clazz) {
            if (clazz == null) {
                return getDefault();
            }
            return new ArrRefType(clazz);
        }

        public static final ArrRefType getDefault() {
            return defaultInstance;
        }
    }
    private static final ArrRefType defaultInstance = new ArrRefType(null);

    protected ArrRefType(AdsEntityObjectClassDef clazz) {
        super(clazz);
    }

    @Override
    public String getName() {
        if (source == null) {
            return "ArrRef";
        } else {
            return "ArrRef<" + super.getName() + ">";
        }
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        if (source == null) {
            return "ArrParentRef";
        } else {
            return "ArrParentRef<" + super.getQualifiedName(context) + ">";
        }
    }

    @Override
    public String getToolTip() {
        if (source != null) {
            StringBuilder b = new StringBuilder();
            b.append("<html>");
            b.append("Array of ");
            b.append(source.getQualifiedName());
            b.append("</html>");
            return source.getToolTip();
        } else {
            return "<html>Array of references</html>";
        }
    }

    private class ArrRefJavaSourceSupport extends TypeJavaSourceSupport {

        public ArrRefJavaSourceSupport() {
            super(ArrRefType.this);
        }

        @Override
        public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
            switch (env.getEnvironment()) {
                case SERVER:
                    AdsEntityObjectClassDef src = getSource();
                    if (src == null) {
                        return "ArrEntity".toCharArray();
                    } else {
                        CodePrinter printer = isHumanReadable? CodePrinter.Factory.newJavaHumanReadablePrinter(): CodePrinter.Factory.newJavaPrinter();
                        printer.print("ArrEntity<");
                        src.getType(EValType.USER_CLASS, null).getJavaSourceSupport().getCodeWriter(UsagePurpose.SERVER_EXECUTABLE).writeUsage(printer);
                        printer.print('>');
                        return printer.getContents();
                    }
                case EXPLORER:
                case COMMON_CLIENT:
                case WEB:
                    return "ArrRef".toCharArray();
                default:
                    return "???".toCharArray();
            }

        }

        @Override
        public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {

            switch (env.getEnvironment()) {
                case SERVER:
                    return JavaSourceSupport.RADIX_SERVER_TYPES_PACKAGE_NAME_ARR;
                case EXPLORER:
                case COMMON_CLIENT:
                case WEB:
                    return JavaSourceSupport.RADIX_EXPLORER_TYPES_PACKAGE_NAME_ARR;
                default:
                    return JavaSourceSupport.DEFAULT_PACKAGE;
            }
        }
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new ArrRefJavaSourceSupport();
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        super.check(referenceContext, env, problemHandler);
        if (env == ERuntimeEnvironmentType.COMMON) {
            problemHandler.accept(RadixProblem.Factory.newError(referenceContext, "ArrRef type can be used only by server or client definitions"));
        }
    }
}
