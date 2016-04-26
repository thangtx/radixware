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
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.ExtendableMembers;
import org.radixware.kernel.common.defs.ads.command.AdsCommandModelClassDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsNestedClassesWriter;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.ClassDefinition.InnerClasses;


public class NestedClasses extends ExtendableMembers<AdsClassDef> implements IJavaSource {

    public static final class Factory {

        private Factory() {
        }

        public static NestedClasses newInstance(AdsClassDef owner) {
            return new NestedClasses(owner);
        }

        public static NestedClasses loadFrom(AdsClassDef owner, InnerClasses classes) {
            return new NestedClasses(owner, classes);
        }
    }

    public static class LocalClasses extends ClassCodeLocalDefinitions<AdsClassDef> {

        private LocalClasses() {
            super();
        }
    }

    protected NestedClasses(AdsClassDef context) {
        super(context, new NestedClasses.LocalClasses());
    }

    protected NestedClasses(AdsClassDef owner, InnerClasses classes) {
        this(owner);

        if (classes != null) {
            final List<ClassDefinition> list = classes.getClazzList();
            if (list != null && !list.isEmpty()) {
                for (final ClassDefinition c : list) {
                    if (c.getType() == EClassType.COMMAND_MODEL) {
                        if (owner instanceof AdsModelClassDef) {
                            getLocal().add(new AdsCommandModelClassDef((AdsModelClassDef) owner, c));
                        }
                        continue;
                    }
                    getLocal().add(AdsClassDef.Factory.loadFrom(c));
                }
            }
        }
    }

    @Override
    public ExtendableDefinitions<AdsClassDef> findInstance(AdsDefinition clazz) {
        return clazz instanceof AdsClassDef ? ((AdsClassDef) clazz).getNestedClasses() : null;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsNestedClassesWriter(this, NestedClasses.this, purpose);
            }
        };
    }

    @Override
    public final LocalClasses getLocal() {
        return (LocalClasses) super.getLocal();
    }

    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        if (!getLocal().isEmpty()) {
            final InnerClasses nestedClasses = xDef.addNewInnerClasses();
            for (final AdsClassDef dynamicClass : getLocal()) {
                dynamicClass.appendTo(nestedClasses.addNewClazz(), saveMode);
            }
        }
    }

    @Override
    public String getName() {
        return "Nested classes";
    }
}
