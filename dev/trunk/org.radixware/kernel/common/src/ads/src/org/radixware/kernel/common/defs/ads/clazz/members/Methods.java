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

import java.util.Arrays;
import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.HierarchyIterator;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsMethodGroup;
import org.radixware.kernel.common.defs.ads.clazz.AdsWrapperClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsMethodsWriter;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.MethodDefinition;


public class Methods extends ExtendableMembers<AdsMethodDef> implements IJavaSource {

    public static final class Factory {

        public static Methods newInstance(AdsClassDef owner) {
            return new Methods(owner);
        }

        public static Methods loadFrom(AdsClassDef owner, ClassDefinition.Methods xDef) {
            return new Methods(owner, xDef);
        }
    }

    static class LocalMethods extends ClassCodeLocalDefinitions<AdsMethodDef> {

        private LocalMethods() {
            super();
        }

        @Override
        protected void onAdd(AdsMethodDef method) {
            super.onAdd(method);
            AdsMethodGroup root = getMethods().getOwnerClass().getMethodGroup();
            if (!root.isRegisteredInGroups(method)) {
                root.addMember(method);
            }
        }

        private Methods getMethods() {
            return (Methods) getContainer();
        }

        @Override
        protected void onRemove(AdsMethodDef definition) {
            super.onRemove(definition);
            getMethods().getOwnerClass().getMethodGroup().removeMember(definition);
        }
    }

    Methods(AdsClassDef context) {
        super(context, new LocalMethods());
    }

    Methods(AdsClassDef context, ClassDefinition.Methods xSet) {
        this(context);
        if (xSet != null) {
            boolean isWrapper = context instanceof AdsWrapperClassDef;
            List<MethodDefinition> list = xSet.getMethodList();
            if (list != null && !list.isEmpty()) {
                for (MethodDefinition m : list) {
                    getLocal().add(isWrapper ? new AdsWrapperMethodDef(m) : AdsMethodDef.Factory.loadFrom(m));
                }
            }
        }
    }

    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        ClassDefinition.Methods xSet = xDef.addNewMethods();

        if (saveMode == ESaveMode.API) {
            //AdsClassDef clazz = getOwnerClass();
            for (AdsMethodDef method : this.getLocal()) {
                if (method.isPublished()) {
                    //switch (method.getAccessMode()) {
                    //  case PROTECTED:
//                            if (clazz.isFinal()) {
//                                continue;
//                            }
                    // case PUBLIC:
                    method.appendTo(xSet.addNewMethod(), saveMode);
                    //     break;
                    // }
                }
            }
        } else {
            for (AdsMethodDef method : this.getLocal()) {
                method.appendTo(xSet.addNewMethod(), saveMode);
            }
        }

    }

    @Override
    public ExtendableMembers<AdsMethodDef> findInstance(AdsDefinition clazz) {
        return clazz instanceof AdsClassDef ? ((AdsClassDef) clazz).getMethods() : null;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsMethodsWriter(this, Methods.this, purpose);
            }
        };
    }

    public AdsMethodDef findBySignature(final char[] signature, EScope scope) {
        final char[] test = Arrays.copyOf(signature, signature.length);
        CharOperation.replace(test, '$', '.');
        if (scope == EScope.LOCAL) {
            for (AdsMethodDef method : getLocal()) {
                char[] sign = method.getProfile().getSignature(getOwnerClass());
                if (CharOperation.equals(test, sign)) {
                    return method;
                }
            }
            return null;
        } else {
            HierarchyIterator<ExtendableDefinitions<AdsMethodDef>> iter = newIterator(scope, HierarchyIterator.Mode.FIND_ALL);
            while (iter.hasNext()) {
                ExtendableDefinitions<AdsMethodDef> methods = iter.next().first();
                for (AdsMethodDef method : methods.get(scope)) {
                    char[] sign = method.getProfile().getSignature(getOwnerClass());
                    if (CharOperation.equals(test, sign)) {
                        return method;
                    }
                }
            }
            return null;
        }
    }

    @Override
    public String getName() {
        return "Methods";
    }
}
