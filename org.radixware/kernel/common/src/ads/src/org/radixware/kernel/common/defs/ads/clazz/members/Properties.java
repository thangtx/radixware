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
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.AdsWrapperClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsPropertiesWriter;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.PropertyDefinition;


public class Properties extends ExtendableMembers<AdsPropertyDef> implements IJavaSource {

    public static final class Factory {

        public static final Properties newInstance(AdsClassDef owner) {
            return new Properties(owner);
        }

        public static final Properties loadFrom(AdsClassDef owner, ClassDefinition.Properties xDef) {
            return new Properties(owner, xDef);
        }
    }

    static class LocalProperties extends ClassCodeLocalDefinitions<AdsPropertyDef> {

        private LocalProperties() {
            super();
        }

        @Override
        protected void onAdd(AdsPropertyDef prop) {
            super.onAdd(prop);
            AdsPropertyGroup root = getProperties().getOwnerClass().getPropertyGroup();
            if (!root.isRegisteredInGroups(prop)) {
                root.addMember(prop);
            }
            if(prop.getNature()== EPropNature.SQL_CLASS_PARAMETER && getProperties().getOwnerClass() instanceof AdsReportClassDef){
                ((AdsReportClassDef)getProperties().getOwnerClass()).updateMethodsParams();
            }
        }

        private Properties getProperties() {
            return (Properties) getContainer();
        }

        @Override
        protected void onRemove(AdsPropertyDef definition) {
            super.onRemove(definition);
            getProperties().getOwnerClass().getPropertyGroup().removeMember(definition);
            if(definition.getNature()== EPropNature.SQL_CLASS_PARAMETER && getProperties().getOwnerClass() instanceof AdsReportClassDef){
                ((AdsReportClassDef)getProperties().getOwnerClass()).updateMethodsParams();
            }
        }
    }

    Properties(AdsClassDef context) {
        super(context, new LocalProperties());
    }

    Properties(AdsClassDef context, ClassDefinition.Properties xSet) {
        this(context);
        if (xSet != null) {
            //   boolean isWrapper = context instanceof AdsWrapperClassDef;
            List<PropertyDefinition> list = xSet.getPropertyList();
            if (list != null && !list.isEmpty()) {
                for (PropertyDefinition m : list) {
                    AdsPropertyDef prop =/* isWrapper ? new AdsWrapperPropertyDef(m) :*/ AdsPropertyDef.Factory.loadFrom(m);
                    if (prop != null) {
                        getLocal().add(prop);
                    }
                }
            }
        }
    }

    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        ClassDefinition.Properties xSet = xDef.addNewProperties();
        if (saveMode == ESaveMode.API) {
            // AdsClassDef clazz = getOwnerClass();
            for (AdsPropertyDef prop : this.getLocal()) {
                if (prop.isPublished()) {
//                    switch (prop.getAccessMode()) {
//                        case PROTECTED:
//                            if (clazz.isFinal()) {
//                                continue;
//                            }
//                        case PUBLIC:
                    prop.appendTo(xSet.addNewProperty(), saveMode);
//                            break;
//                    }
                }
            }
        } else {
            for (AdsPropertyDef prop : this.getLocal()) {
                prop.appendTo(xSet.addNewProperty(), saveMode);
            }
        }
    }

    @Override
    public ExtendableMembers<AdsPropertyDef> findInstance(AdsDefinition clazz) {
        return clazz instanceof AdsClassDef ? ((AdsClassDef) clazz).getProperties() : null;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsPropertiesWriter(this, Properties.this, purpose);
            }
        };
    }

//    /**
//     *
//     * @deprecated relevance?
//     */
//    @Deprecated
//    public AdsPropertyDef findByName(final char[] signature, EScope scope) {
//        HierarchyIterator<ExtendableDefinitions<AdsPropertyDef>> iter = newIterator(scope);
//        final char[] test = Arrays.copyOf(signature, signature.length);
//        CharOperation.replace(test, '$', '.');
//        while (iter.hasNext()) {
//            ExtendableDefinitions<AdsPropertyDef> fields = iter.next();
//            for (AdsPropertyDef field : fields.get(scope)) {
//                char[] sign = field.getProfile().getName().toCharArray();
//                if (CharOperation.equals(test, sign)) {
//                    return field;
//                }
//            }
//        }
//        return null;
//    }
    public AdsPropertyDef findBySignature(final char[] signature, EScope scope) {
        final HierarchyIterator<ExtendableDefinitions<AdsPropertyDef>> iter = newIterator(scope, HierarchyIterator.Mode.FIND_ALL);
        final char[] test = Arrays.copyOf(signature, signature.length);
        CharOperation.replace(test, '$', '.');
        CharOperation.replace(test, '/', '.');
        while (iter.hasNext()) {
            ExtendableDefinitions<AdsPropertyDef> fields = iter.next().first();
            for (AdsPropertyDef field : fields.get(scope)) {
                char[] sign = field.getProfile().getSignature(getOwnerClass());
                if (CharOperation.equals(test, sign)) {
                    return field;
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "Properties";
    }
}
