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

import java.util.Map;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.utils.CharOperations;

/**
 * Marker interface for all definitions wich can looks like java type definition
 *
 */
public abstract class AdsType implements IJavaSource {

    public static abstract class TypeJavaSourceSupport extends JavaSourceSupport {

        public static class TypeCodeWriter extends CodeWriter {

            protected TypeCodeWriter(TypeJavaSourceSupport support, UsagePurpose usagePurpose) {
                super(support, usagePurpose);
            }

            @Override
            public boolean writeCode(CodePrinter printer) {
                printer.print(((TypeJavaSourceSupport)getSupport()).getQualifiedTypeName(usagePurpose, printer instanceof IHumanReadablePrinter));
                return true;
            }

            @Override
            public void writeUsage(CodePrinter printer) {
                writeCode(printer);
            }
        }

        protected TypeJavaSourceSupport(final AdsType type) {
            super(type);
        }

        public abstract char[][] getPackageNameComponents(UsagePurpose purpose, boolean isHumanReadable);

        public final char[] getQualifiedPackageName(UsagePurpose purpose, boolean isHumanReadable) {
            return getQualifiedPackageName(purpose, '.', isHumanReadable);
        }

        public final char[] getQualifiedPackageName(UsagePurpose purpose, char separator, boolean isHumanReadable) {
            char[][] packageName = getPackageNameComponents(purpose, isHumanReadable);
            return CharOperations.merge(packageName, separator);
        }

        public char[] getQualifiedTypeName(UsagePurpose purpose, char separator, boolean isHumanReadable) {
            char[] packageName = getQualifiedPackageName(purpose, separator, isHumanReadable);
            if (packageName != null && packageName.length > 0) {
                char[] localName = getLocalTypeName(purpose, isHumanReadable);
                char[] res = new char[packageName.length + localName.length + 1];
                System.arraycopy(packageName, 0, res, 0, packageName.length);
                res[packageName.length] = separator;
                System.arraycopy(localName, 0, res, packageName.length + 1, localName.length);
                return res;
            } else {
                return getLocalTypeName(purpose, isHumanReadable);
            }
        }

        public char[] getQualifiedTypeName(UsagePurpose purpose, boolean isHumanReadable) {
            return getQualifiedTypeName(purpose, '.', isHumanReadable);
        }

        public abstract char[] getLocalTypeName(UsagePurpose purpose, boolean isHumanReadable);

        @Override
        public TypeCodeWriter getCodeWriter(UsagePurpose purpose) {
            return new TypeCodeWriter(this, purpose);
        }
    }

    public char[] getFullJavaClassName(JavaSourceSupport.UsagePurpose purpose) {
        return getJavaSourceSupport().getQualifiedTypeName(purpose, false);
    }

    @Override
    public abstract TypeJavaSourceSupport getJavaSourceSupport();

    /**
     * Returns display name for type definition
     */
    public abstract String getName();

    public abstract String getQualifiedName(RadixObject context);

    //  public abstract JavaSourceSupport getJavaSourceSupport();
    public String getToolTip() {
        return "";
    }

    public final void check(RadixObject referenceContext, IProblemHandler problemHandler, Map<Object, Object> checkHistory) {
        ERuntimeEnvironmentType environment = null;
        RadixObject obj = referenceContext;
        while (obj != null) {
            if (obj instanceof IEnvDependent) {
                IEnvDependent def = (IEnvDependent) obj;
                environment = def.getUsageEnvironment();
                break;
            } else {
                obj = obj.getContainer();
            }
        }
        if (environment == null) {
            error(referenceContext, problemHandler, "Can not determine type usage purpose");
        } else {
            check(referenceContext, environment, problemHandler, checkHistory);
        }
    }

    protected void error(RadixObject context, IProblemHandler problemHandler, String message) {
        if (problemHandler != null) {
            problemHandler.accept(RadixProblem.Factory.newError(context, message));
        }
    }

    protected void warning(RadixObject context, IProblemHandler problemHandler, String message) {
        if (problemHandler != null) {
            problemHandler.accept(RadixProblem.Factory.newWarning(context, message));
        }
    }

    public boolean isSubclassOf(AdsType type) {
        return false;
    }

    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType environment, IProblemHandler problemHandler, Map<Object, Object> checkHistory) {
        check(referenceContext, environment, problemHandler);
    }
    
    protected abstract void check(RadixObject referenceContext, ERuntimeEnvironmentType environment, IProblemHandler problemHandler);
}
