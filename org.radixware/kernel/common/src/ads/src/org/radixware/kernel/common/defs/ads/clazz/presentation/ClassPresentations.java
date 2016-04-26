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

package org.radixware.kernel.common.defs.ads.clazz.presentation;

import java.util.Collection;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;

import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsClassMember;
import org.radixware.kernel.common.defs.IOverridable;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsClassPresentationsWriter;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef.MultilingualStringInfo;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;


public abstract class ClassPresentations extends RadixObject implements IAdsClassMember, IJavaSource, ILocalizedDef, IOverridable, IOverwritable {

    public static final int FORMAT_VERSION = 1;

    public static final class Factory {

        private Factory() {
            super();
        }

        public static ClassPresentations loadFrom(final AdsClassDef context, final ClassDefinition.Presentations xDef) {
            if (xDef == null) {
                return newInstance(context);
            }
            if (context instanceof AdsEntityClassDef) {
                return new EntityPresentations((AdsEntityClassDef) context, xDef);
            } else if (context instanceof AdsApplicationClassDef) {
                return new EntityObjectPresentations((AdsApplicationClassDef) context, xDef);
            } else if (context instanceof AdsEntityGroupClassDef) {
                return new EntityGroupPresentations((AdsEntityGroupClassDef) context, xDef);
            } else if (context instanceof AdsFormHandlerClassDef) {
                return new FormPresentations((AdsFormHandlerClassDef) context, xDef);
            } else if (context instanceof AdsReportClassDef) {
                return new ReportPresentations((AdsReportClassDef) context, xDef);
            } else if (context instanceof AdsAlgoClassDef) {
                return new AlgoClassPresentations((AdsAlgoClassDef) context, xDef);
            } else {
                return null;
            }
        }

        public static ClassPresentations newInstance(final AdsClassDef context) {
            if (context instanceof AdsEntityClassDef) {
                return new EntityPresentations((AdsEntityClassDef) context);
            } else if (context instanceof AdsApplicationClassDef) {
                return new EntityObjectPresentations((AdsApplicationClassDef) context);
            } else if (context instanceof AdsEntityGroupClassDef) {
                return new EntityGroupPresentations((AdsEntityGroupClassDef) context);
            } else if (context instanceof AdsFormHandlerClassDef) {
                return new FormPresentations((AdsFormHandlerClassDef) context, null);
            } else if (context instanceof AdsReportClassDef) {
                return new ReportPresentations((AdsReportClassDef) context, null);
            } else if (context instanceof AdsAlgoClassDef) {
                return new AlgoClassPresentations((AdsAlgoClassDef) context, null);
            } else {
                return null;
            }
        }
    }

    @Override
    public AdsClassDef getOwnerClass() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsClassDef) {
                return (AdsClassDef) owner;
            }
        }
        return null;
    }

    public void appendTo(final ClassDefinition.Presentations xDef, final ESaveMode saveMode) {
        commands.appendTo(xDef, saveMode);
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.commands.visit(visitor, provider);
    }

    @Override
    public String getName() {
        return "Presentations";
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PRESENTATION_SET;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(final UsagePurpose purpose) {
                return AdsClassPresentationsWriter.Factory.newInstance(ClassPresentations.this, this, purpose);
            }
        };
    }
    private final transient Commands commands;

    protected ClassPresentations(final AdsClassDef ownerClass, final ClassDefinition.Presentations xDef) {
        super();
        setContainer(ownerClass);
        this.commands = new Commands(this, xDef);
    }

    protected ClassPresentations(final AdsClassDef ownerClass) {
        super();
        setContainer(ownerClass);
        this.commands = new Commands(this, null);
    }

    public ExtendableDefinitions<AdsScopeCommandDef> getCommands() {
        return commands;
    }

    @Override
    public void collectUsedMlStringIds(final Collection<MultilingualStringInfo> ids) {
        //do nothing
    }

    @Override
    public AdsMultilingualStringDef findLocalizedString(final Id stringId) {
        return getOwnerClass().findLocalizedString(stringId);
    }

    protected void cleanup() {
        getCommands().getLocal().clear();
    }

    @Override
    public void afterOverride() {
        cleanup();
    }

    @Override
    public void afterOverwrite() {
        cleanup();
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }

    @Override
    public boolean isOverwrite() {
        return false;
    }

    @Override
    public boolean setOverwrite(final boolean override) {
        return false;
    }

    @Override
    protected boolean isQualifiedNamePart() {
        return false;
    }
}
