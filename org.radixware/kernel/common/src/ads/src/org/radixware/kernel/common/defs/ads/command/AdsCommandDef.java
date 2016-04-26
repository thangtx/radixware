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

package org.radixware.kernel.common.defs.ads.command;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsTitledDefinition;
import org.radixware.kernel.common.defs.ads.IClientDefinition;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsScopeCommandDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.radixdoc.CommandRadixdoc;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.command.AdsCommandWriter;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsCommandType;
import org.radixware.kernel.common.defs.ads.type.AdsDefinitionType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.enums.ECommandNature;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.CommandDefinition;
import org.radixware.schemas.radixdoc.Page;


public abstract class AdsCommandDef extends AdsTitledDefinition implements IAdsTypeSource, IJavaSource, IOverwritable, IClientDefinition, IRadixdocProvider {

    @Override
    public void afterOverwrite() {
    }

    @Override
    public boolean allowOverwrite() {
        return true;
    }
    private boolean deprecated = false;

    public class CommandPresentation extends RadixObject {

        private Id iconId;
        private boolean isConfirmationRequired;
        private boolean isVisible;
        private ERuntimeEnvironmentType clientEnvironment;

        public Id getIconId() {
            return iconId;
        }

        public void setIconId(Id id) {
            this.iconId = id;
            setEditState(EEditState.MODIFIED);
        }

        public boolean getIsConfirmationRequired() {
            return isConfirmationRequired;
        }

        public void setIsConfirmationRequired(boolean isConfirmationRequired) {
            this.isConfirmationRequired = isConfirmationRequired;
            setEditState(EEditState.MODIFIED);
        }

        public boolean getIsVisible() {
            return isVisible;
        }

        public void setIsVisible(boolean isVisible) {
            this.isVisible = isVisible;
            setEditState(EEditState.MODIFIED);
        }

        protected CommandPresentation(CommandDefinition xCommand) {
            if (xCommand != null) {
                this.isVisible = xCommand.getIsVisible();
                this.isConfirmationRequired = xCommand.getIsConfirmationRequired();
                this.iconId = Id.Factory.loadFrom(xCommand.getIconId());
                if (xCommand.isSetClientEnvironment()) {
                    this.clientEnvironment = xCommand.getClientEnvironment();
                }
            } else {
                this.isVisible = true;
                this.isConfirmationRequired = true;
                this.iconId = null;
            }
            this.setContainer(AdsCommandDef.this);
        }

        public void appendTo(CommandDefinition xDef) {
            if (iconId != null) {
                xDef.setIconId(iconId.toString());
            }
            xDef.setIsVisible(isVisible);
            xDef.setIsConfirmationRequired(isConfirmationRequired);
            if (clientEnvironment != null && clientEnvironment != ERuntimeEnvironmentType.COMMON_CLIENT) {
                xDef.setClientEnvironment(clientEnvironment);
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            super.collectDependences(list);
            if (iconId != null) {
                AdsImageDef image = AdsSearcher.Factory.newImageSearcher(AdsCommandDef.this).findById(iconId).get();

                if (image != null) {
                    list.add(image);
                }
            }
        }

        private ERuntimeEnvironmentType getOwnClientEnvironment() {
            return clientEnvironment == null ? ERuntimeEnvironmentType.COMMON_CLIENT : clientEnvironment;
        }

        public ERuntimeEnvironmentType getClientEnvironment() {
            AdsDefinition def = getOwnerDef();
            if (def == null) {
                return getOwnClientEnvironment();
            } else if (def instanceof AdsEntityObjectClassDef) {
                ERuntimeEnvironmentType ce = ((AdsEntityObjectClassDef) def).getClientEnvironment();
                if (ce == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return getOwnClientEnvironment();
                } else {
                    return ce;
                }
            } else {
                return getOwnClientEnvironment();
            }
        }

        public boolean canChangeClientEnvironment() {
            AdsDefinition def = getOwnerDef();
            if (def == null) {
                return true;
            } else if (def instanceof AdsEntityObjectClassDef) {
                ERuntimeEnvironmentType ce = ((AdsEntityObjectClassDef) def).getClientEnvironment();
                if (ce == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }

        public void setClientEnvironment(ERuntimeEnvironmentType clientEnvironment) {
            if (clientEnvironment != null && clientEnvironment != this.clientEnvironment && clientEnvironment.isClientEnv()) {
                this.clientEnvironment = clientEnvironment;
                setEditState(EEditState.MODIFIED);
            }
        }
    }

    public class CommandData extends RadixObject {

        private ECommandNature nature;
        private AdsTypeDeclaration inType;
        private AdsTypeDeclaration outType;
        private boolean isImmutable = false;

        public CommandData(CommandDefinition xCommand) {
            this.nature = xCommand.getNature();
            this.inType = AdsTypeDeclaration.Factory.loadFrom(xCommand.getInType(), AdsTypeDeclaration.Factory.voidType());
            this.outType = AdsTypeDeclaration.Factory.loadFrom(xCommand.getOutType(), AdsTypeDeclaration.Factory.voidType());
            this.setContainer(AdsCommandDef.this);
            if (xCommand.isSetIsReadOnlyCommand()) {
                this.isImmutable = xCommand.getIsReadOnlyCommand();
            }
        }

        public CommandData() {
            this.nature = ECommandNature.XML_IN_OUT;
            inType = AdsTypeDeclaration.Factory.voidType();
            outType = AdsTypeDeclaration.Factory.voidType();
            this.setContainer(AdsCommandDef.this);
        }

        public boolean isReadOnlyCommand() {
            return isImmutable;
        }

        public void setReadOnlyCommand(boolean immutable) {
            if (isImmutable != immutable) {
                isImmutable = immutable;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public void collectDependences(List<Definition> list) {
            if (inType != null) {
                AdsType resolved = inType.resolve(AdsCommandDef.this).get();
                if (resolved instanceof AdsDefinitionType) {
                    Definition src = ((AdsDefinitionType) resolved).getSource();
                    if (src != null) {
                        list.add(src);

                    }
                }
            }
            if (outType != null) {
                AdsType resolved = outType.resolve(AdsCommandDef.this).get();
                if (resolved instanceof AdsDefinitionType) {
                    Definition src = ((AdsDefinitionType) resolved).getSource();
                    if (src != null) {
                        list.add(src);

                    }
                }
            }
        }

        /**
         * Returns command input type
         */
        public AdsTypeDeclaration getInType() {
            return inType;
        }

        public ECommandNature getNature() {
            return nature;
        }

        public AdsTypeDeclaration formOutType() {
            return AdsTypeDeclaration.Factory.newPlatformClass(AdsFormHandlerClassDef.PLATFORM_NEXT_REQUEST_CLASS_NAME);
        }

        /**
         * Sets command input type Returns true if type successfully changed
         * false otherwise
         *
         * @Throws {@linkplain DefinitionError} if type is not form class or xml
         * reference
         */
        public boolean setInType(AdsTypeDeclaration decl) {
            if (nature == ECommandNature.LOCAL) {
                return false;
            }

            if (decl.isBasedOn(EValType.XML) || decl.isVoid()) {
                this.inType = decl;
                if (this.outType.isBasedOn(EValType.XML) || this.outType.isVoid()) {
                    this.nature = ECommandNature.XML_IN_OUT;
                } else {
                    this.nature = ECommandNature.XML_IN_FORM_OUT;
                }
                setEditState(EEditState.MODIFIED);
                return true;
            } else if (decl.isBasedOn(EValType.USER_CLASS)) {
                AdsType type = decl.resolve(AdsCommandDef.this).get();
                if (type instanceof AdsClassType.FormHandlerType) {
                    if (this.outType.isBasedOn(EValType.JAVA_CLASS) || this.outType.isVoid()
                            || this.outType.isBasedOn(EValType.XML)) {
                        this.inType = decl;
                        this.outType = formOutType();
                        this.nature = ECommandNature.FORM_IN_OUT;
                        setEditState(EEditState.MODIFIED);
                        return true;
                    }
                } else {
                    throw new DefinitionError("Command input type must be an xml or form-handler class reference", AdsCommandDef.this);
                }
            }

            return false;
        }

        /**
         * Returns command output type
         */
        public AdsTypeDeclaration getOutType() {
            return outType;
        }

        /**
         * Sets command output type Returns true if type successfully changed
         * false otherwise
         *
         * @Throws {@linkplain DefinitionError} if given type reference is not
         * xml or java or user class linked with platform FormHandler class
         */
        public boolean setOutType(final AdsTypeDeclaration type) {
            if (nature == ECommandNature.LOCAL) {
                return false;
            }

            if (type.isBasedOn(EValType.XML) || type.isVoid()) {
                if (this.inType.isBasedOn(EValType.XML) || this.inType.isVoid()) {
                    this.nature = ECommandNature.XML_IN_OUT;
                    this.outType = type;
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
            } else if (type.isBasedOn(EValType.USER_CLASS) || type.isBasedOn(EValType.JAVA_CLASS)) {
                boolean typeIsValid = false;
                if (type.isBasedOn(EValType.USER_CLASS)) {
                    final AdsType typeSource = type.resolve(AdsCommandDef.this).get();

                    if (typeSource instanceof IPlatformClassPublisher) {
                        final IPlatformClassPublisher.IPlatformClassPublishingSupport support = ((IPlatformClassPublisher) typeSource).getPlatformClassPublishingSupport();
                        if (support != null && support.isPlatformClassPublisher() && AdsFormHandlerClassDef.PLATFORM_NEXT_REQUEST_CLASS_NAME.equals(support.getPlatformClassName())) {
                            typeIsValid = true;
                        }
                    }
                } else {
                    if (AdsFormHandlerClassDef.PLATFORM_NEXT_REQUEST_CLASS_NAME.equals(type.getExtStr())) {
                        typeIsValid = true;
                    }
                }
                if (!typeIsValid) {
                    throw new DefinitionError("Command output type must be xml or reference to platform form handler class", AdsCommandDef.this);
                }
                if (this.inType.isBasedOn(EValType.USER_CLASS)) {
                    this.outType = formOutType();
                    this.nature = ECommandNature.FORM_IN_OUT;
                    setEditState(EEditState.MODIFIED);
                    return true;
                } else if (this.inType.isBasedOn(EValType.XML) || this.inType.isVoid()) {
                    this.outType = formOutType();
                    this.nature = ECommandNature.XML_IN_FORM_OUT;
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
            }
            return false;
        }

        /**
         * Returns true if command is local
         */
        public boolean isLocal() {
            return nature == ECommandNature.LOCAL;
        }

        /**
         * Makes command local (no input and no output) and it does not send to
         * server
         */
        public void setIsLocal(boolean isLocal) {
            if (isLocal) {
                this.nature = ECommandNature.LOCAL;
            } else {
                this.nature = ECommandNature.XML_IN_OUT;
            }
            this.inType = AdsTypeDeclaration.Factory.voidType();
            this.outType = AdsTypeDeclaration.Factory.voidType();
            setEditState(EEditState.MODIFIED);
        }

        public void appendTo(CommandDefinition xDef) {
            xDef.setNature(nature);
            if (inType != null && inType != AdsTypeDeclaration.Factory.voidType()) {
                inType.appendTo(xDef.addNewInType());
            }
            if (outType != null && outType != AdsTypeDeclaration.Factory.voidType()) {
                outType.appendTo(xDef.addNewOutType());
            }
            if (isImmutable) {
                xDef.setIsReadOnlyCommand(isImmutable);
            }
        }

//        protected AdsDefinition getCommandDef() {
//            return (AdsDefinition) getOwner();
//        }
        @Override
        public boolean isReadOnly() {
            if (super.isReadOnly()) {
                return true;
            } else {
                if (!AdsCommandDef.this.getHierarchy().findOverridden().isEmpty()
                        || !AdsCommandDef.this.getHierarchy().findOverwritten().isEmpty()) {
                    return true;
                }
                return false;
            }
        }
    }
    private final CommandData data;
    private final CommandPresentation presentation;

    protected AdsCommandDef(CommandDefinition xDef) {
        super(xDef);
        this.data = new CommandData(xDef);
        this.presentation = createPresentation(xDef);
        if (xDef.isSetIsDeprecated()) {
            this.deprecated = xDef.getIsDeprecated();
        } else {
            this.deprecated = false;
        }
    }

    protected AdsCommandDef(Id id, String name) {
        super(id, name, null);
        this.data = new CommandData();
        this.presentation = createPresentation(null);
    }

    protected CommandPresentation createPresentation(CommandDefinition xDef) {
        return new CommandPresentation(xDef);
    }

    public CommandPresentation getPresentation() {
        return this.presentation;
    }

    public CommandData getData() {
        return this.data;
    }

    @Override
    public boolean isDeprecated() {
        return deprecated || super.isDeprecated();
    }

    public void setDeprecated(boolean deprecated) {
        if (this.deprecated != deprecated) {
            this.deprecated = deprecated;
            setEditState(EEditState.MODIFIED);
            fireNameChange();
        }
    }

    protected void appendTo(CommandDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        this.data.appendTo(xDef);
        this.presentation.appendTo(xDef);
        if (deprecated) {
            xDef.setIsDeprecated(true);
        }
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return AdsCommandType.Factory.newInstance(this);
    }

    public class CommandJavaSourceSupport extends JavaSourceSupport {

        public CommandJavaSourceSupport() {
            super(AdsCommandDef.this);
        }

        @Override
        public AdsCommandWriter<? extends AdsCommandDef> getCodeWriter(UsagePurpose purpose) {
            return new AdsCommandWriter<AdsCommandDef>(this, AdsCommandDef.this, purpose);
        }
    }

    @Override
    public CommandJavaSourceSupport getJavaSourceSupport() {
        return new CommandJavaSourceSupport();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.data.visit(visitor, provider);
        this.presentation.visit(visitor, provider);
    }

    public Id getHandlerId() {
        return Id.Factory.loadFrom(EDefinitionIdPrefix.ADS_CLASS_METHOD.getValue() + getId().toString());
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        return getPresentation().getClientEnvironment();
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        final String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append(' ').append(getClientEnvironment().getName()).append("</b>&nbsp;");
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        if (getTitleId() != null) {
            sb.append("<br>Title: ");
            final AdsLocalizingBundleDef bundle = findExistingLocalizingBundle();
            if (bundle != null) {
                for (EIsoLanguage lan : bundle.getLanguages()) {
                    if (getTitle(lan) != null) {
                        sb.append("<br>&nbsp;(").append(lan.toString()).append("): ").append(getTitle(lan));
                    }
                }
            }
        }
        final String inputName = getData().getInType().getName(getContainer()),
                outputName = getData().getOutType().getName(getContainer());
        final StringBuilder inType = new StringBuilder(), outType = new StringBuilder();
        if (!inputName.equals("void")) {
            sb.append("<br>Input ");
            if (getData().getInType().isBasedOn(EValType.XML)) {
                inType.append("Xml:<br>&nbsp;");
            } else {
                inType.append("Form:<br>&nbsp;");
            }
            inType.append("<a href=\"").append("\">").append(inputName).append("</a>");
            sb.append(inType);
        }
        if (!outputName.equals("void")) {
            sb.append("<br>Output ");
            if (getData().getOutType().isBasedOn(EValType.XML)) {
                outType.append("Xml:<br>&nbsp;");
            } else {
                outType.append("Form:<br>&nbsp;");
            }
            outType.append("<a href=\"").append("\">").append(outputName).append("</a>");
            sb.append(outType);
        }
        sb.append("<br>No modifications: ").append(this.getData().isReadOnlyCommand());
        sb.append("<br>Confirmation required: ").append(getPresentation().getIsConfirmationRequired());
        if (this instanceof AdsContextlessCommandDef == false) {
            sb.append("<br>Do not send request to server: ").append(this.getData().isLocal());
        }
        sb.append("<br>Show command button: ").append(getPresentation().getIsVisible());
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsCommandDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new CommandRadixdoc(getSource(), page, options);
            }
        };
    }   
}