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
package org.radixware.kernel.common.defs.ads.clazz.form;

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.IAdsPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AbstractFormPresentations;
import org.radixware.kernel.common.defs.ads.clazz.presentation.ClassPresentations;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassRadixdoc;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;

public class AdsFormHandlerClassDef extends AdsClassDef implements IAdsFormPresentableClass {

    public static final int FORMAT_VERSION = IAdsPresentableClass.FORMAT_VERSION;
    public static final Id PREDEFINED_ID = Id.Factory.loadFrom("adcJJJGZQRERVCAPNT2A3LNCCWYPI");

    @Override
    public long getFormatVersion() {
        return FORMAT_VERSION;
    }
    public static final String PLATFORM_CLASS_NAME = "org.radixware.kernel.server.types.FormHandler";
    public static final String PLATFORM_NEXT_REQUEST_CLASS_NAME = "org.radixware.kernel.server.types.FormHandler.NextDialogsRequest";

    @Override
    public boolean isModelRequred() {
        return true;
    }

    public static final class Factory {

        private Factory() {
            super();
        }

        public static AdsFormHandlerClassDef loadFrom(final ClassDefinition classDef) {
            return new AdsFormHandlerClassDef(classDef);
        }

        public static AdsFormHandlerClassDef newInstance() {
            return new AdsFormHandlerClassDef("NewFormHandlerClass");
        }
    }
    private final transient AbstractFormPresentations presentations;
    private ERuntimeEnvironmentType clientEnvironment = null;

    private AdsFormHandlerClassDef(final ClassDefinition xDef) {
        super(xDef);
        this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.loadFrom(this, xDef.getPresentations());
        if (xDef.isSetClientEnvironment()) {
            this.clientEnvironment = xDef.getClientEnvironment();

        }
    }

    private AdsFormHandlerClassDef(final String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_FORM_HANDLER_CLASS), name);
        this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.newInstance(this);
    }

    private AdsFormHandlerClassDef(final AdsFormHandlerClassDef source) {
        super(source);
        this.presentations = (AbstractFormPresentations) ClassPresentations.Factory.newInstance(source);
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.FORM_HANDLER;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_FORM_HANDLER;
    }

    @Override
    public AbstractFormPresentations getPresentations() {
        return (AbstractFormPresentations) presentations;
    }

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
    }

    @Override
    public void appendTo(final ClassDefinition xDef, final ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        this.presentations.appendTo(xDef.addNewPresentations(), saveMode);
        if (this.clientEnvironment != null && this.clientEnvironment != ERuntimeEnvironmentType.COMMON_CLIENT) {
            xDef.setClientEnvironment(clientEnvironment);
        }
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(final Id id) {
        try {
            final EDefinitionIdPrefix prefix = id.getPrefix();
            if (prefix == null) {
                return SearchResult.empty();
            }
            switch (prefix) {
                case EDITOR_PAGE:
                    return getPresentations().getEditorPages().findById(id, EScope.ALL);
                case ADS_FORM_MODEL_CLASS:
                    AdsModelClassDef model = getPresentations().getModel();
                    if (model != null && model.getId() == id) {
                        return SearchResult.single(model);
                    } else {
                        return SearchResult.empty();
                    }
                default:
                    return super.findComponentDefinition(id);
            }
        } catch (NoConstItemWithSuchValueError e) {
            return super.findComponentDefinition(id);
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
    }

    @SuppressWarnings("unchecked")
    public void setClientEnvironment(ERuntimeEnvironmentType env) {
        if (env != this.clientEnvironment && env != null && env.isClientEnv()) {
            this.clientEnvironment = env;
            this.presentations.getCustomViewSupport().getChangeSupport().fireEvent(new ICustomViewable.CustomViewChangedEvent());
            setEditState(EEditState.MODIFIED);
        }
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        final String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ClassRadixdoc(getSource(), page, options) {
                    @Override
                    protected void writeClassDefInfo(ContentContainer overview, Table overviewTable) {
                        AdsFormHandlerClassDef handler = (AdsFormHandlerClassDef) source;
                        Table presTable = getClassWriter().setBlockCollapsibleAndAddTable(overview.addNewBlock(), "Presentations");
                        AbstractFormPresentations pres = handler.getPresentations();
                        getClassWriter().addStr2BoolRow(presTable, "Inherit Editor Pages", pres.isEditorPagesInherited());
                        getClassWriter().addStr2BoolRow(presTable, "Inherit Custom View", pres.isCustomViewInherited());
                        getClassWriter().addStr2BoolRow(presTable, "Use custom view for explorer", pres.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.EXPLORER));
                        getClassWriter().addStr2BoolRow(presTable, "Use custom view for web", pres.getCustomViewSupport().isUseCustomView(ERuntimeEnvironmentType.WEB));
                    }
                };
            }
        };
    }

    @Override
    protected void onSuperClassChanged(AdsTypeDeclaration newSuperClass) {
        if (newSuperClass == null) {
            return;
        }
        AdsType superType = newSuperClass.resolve(this).get();
        if (superType instanceof AdsClassType) {
            AdsClassDef superClass = ((AdsClassType) superType).getSource();
            if (superClass.getClassDefType() == EClassType.FORM_HANDLER) {
                this.getPresentations().setIconInherited(true);
                this.getPresentations().setEditorPagesInherited(true);
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getDocEnvironment() {
        return ERuntimeEnvironmentType.COMMON_CLIENT;
    }
    
}
