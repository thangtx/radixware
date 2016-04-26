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
package org.radixware.kernel.common.defs.ads.explorerItems;

import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.common.ContextlessCommandUsage;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable.CustomViewSupport;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.radixdoc.ParagraphExpItemRadixdoc;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.exploreritems.AdsExplorerItemWriter;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomParagEditorDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AdsRwtCustomParagEditorDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef.EType;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.ParagraphDefinition;
import org.radixware.schemas.radixdoc.Page;

public class AdsParagraphExplorerItemDef extends AdsExplorerItemDef implements ICustomViewable<AdsParagraphExplorerItemDef, AdsAbstractUIDef>, ContextlessCommandUsage.IContextlessCommandsUser, IRadixdocProvider {

    public static final class Factory {

        public static final AdsParagraphExplorerItemDef newInstance() {
            return new AdsParagraphExplorerItemDef();
        }

        public static final AdsParagraphExplorerItemDef newTemporaryInstance(RadixObject container) {
            AdsParagraphExplorerItemDef item = newInstance();
            item.setContainer(container);
            return item;
        }

        public static final AdsParagraphExplorerItemDef loadFrom(ParagraphDefinition xDef) {
            return new AdsParagraphExplorerItemDef(xDef);
        }
    }
    private Id iconId;
    private Id logoId;
    private AdsParagraphModelClassDef modelClass;
    private final ExplorerItems explorerItems;
    private final ContextlessCommandUsage contextlessCommands;
    private ERuntimeEnvironmentType clientEnv;
    private boolean isRoot;
    private boolean isHidden = false;
    private boolean childrenInherited = true;
    private final ICustomViewable.CustomViewSupport<AdsParagraphExplorerItemDef, AdsAbstractUIDef> customViewSuppoort = new CustomViewSupport<AdsParagraphExplorerItemDef, AdsAbstractUIDef>(this) {
        @Override
        protected AdsAbstractUIDef createOrLoadCustomView(AdsParagraphExplorerItemDef context, ERuntimeEnvironmentType env, AbstractDialogDefinition xDef) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                if (xDef != null) {
                    return AdsCustomParagEditorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsCustomParagEditorDef.Factory.newInstance(context);
                }
            } else if (env == ERuntimeEnvironmentType.WEB) {
                if (xDef != null) {
                    return AdsRwtCustomParagEditorDef.Factory.loadFrom(context, xDef);
                } else {
                    return AdsRwtCustomParagEditorDef.Factory.newInstance(context);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet");
            }
        }
    };

    protected AdsParagraphExplorerItemDef(ParagraphDefinition xDef) {
        super(xDef);
        this.iconId = xDef.getIconId();
        this.logoId = xDef.getLogoId();
        this.modelClass = AdsParagraphModelClassDef.Factory.loadFrom(this, xDef.getModel());
        this.isRoot = xDef.getIsRoot();
        if (xDef.isSetIsHidden()) {
            this.isHidden = xDef.getIsHidden();
        }
        this.explorerItems = ExplorerItems.Factory.loadFrom(this, xDef.getChildExplorerItems());
        if (xDef.getView() != null) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.EXPLORER, xDef.getView());
        }
        if (xDef.getWebView() != null) {
            customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.WEB, xDef.getWebView());
        }
        this.contextlessCommands = ContextlessCommandUsage.Factory.loadFrom(this, xDef.getUsedContextlessCommands());
        if (xDef.getClientEnvironment() != null) {
            clientEnv = xDef.getClientEnvironment();
        } else {
            clientEnv = ERuntimeEnvironmentType.COMMON_CLIENT;
        }
        if (xDef.isSetIsChildrenInherited()) {
            childrenInherited = xDef.getIsChildrenInherited();
        }
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        if (this.isHidden != isHidden) {
            this.isHidden = isHidden;
            setEditState(EEditState.MODIFIED);
        }
    }

    protected AdsParagraphExplorerItemDef() {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.PARAGRAPH), "NewParagraph");
        this.iconId = null;
        this.logoId = null;
        this.isRoot = false;
        this.modelClass = AdsParagraphModelClassDef.Factory.newInstance(this);
        this.explorerItems = ExplorerItems.Factory.newInstance(this);
        this.contextlessCommands = ContextlessCommandUsage.Factory.newInstance(this);
        clientEnv = ERuntimeEnvironmentType.COMMON_CLIENT;

    }

    @Override
    public CustomViewSupport<AdsParagraphExplorerItemDef, AdsAbstractUIDef> getCustomViewSupport() {
        return customViewSuppoort;
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        return clientEnv;
    }

    @Override
    public void setClientEnvironment(ERuntimeEnvironmentType e) {
        if (e != this.clientEnv) {
            this.clientEnv = e;
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getIconId() {
        return iconId;
    }

    public void setIconId(Id iconId) {
        this.iconId = iconId;
        setEditState(EEditState.MODIFIED);
    }

    public Id getLogoId() {
        return logoId;
    }

    public void setLogoId(Id logoId) {
        this.logoId = logoId;
        setEditState(EEditState.MODIFIED);
    }

    public AdsParagraphModelClassDef getModel() {
        return modelClass;
    }

    @Override
    public String getTitle(EIsoLanguage language) {
        return super.getLocalizedStringValue(language, titleId);
    }

    public ExplorerItems getExplorerItems() {
        return explorerItems;
    }

    public void appendTo(ParagraphDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (iconId != null) {
            xDef.setIconId(iconId);
        }
        if (logoId != null) {
            xDef.setLogoId(logoId);
        }
        xDef.setIsRoot(isRoot);
        this.explorerItems.appendTo(xDef.addNewChildExplorerItems(), saveMode);
        this.modelClass.appendTo(xDef.addNewModel(), saveMode);
        if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
            ((AdsCustomParagEditorDef) this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER)).appendTo(xDef/*
                     * .addNewView()
                     */, saveMode);
        }
        if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
            ((AdsRwtCustomParagEditorDef) this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB)).appendTo(xDef/*
                     * .addNewView()
                     */, saveMode);
        }
        if (contextlessCommands != null && !contextlessCommands.isEmpty()) {
            xDef.setUsedContextlessCommands(contextlessCommands.getCommandIds());
        }
        if (clientEnv == ERuntimeEnvironmentType.EXPLORER || clientEnv == ERuntimeEnvironmentType.WEB) {
            xDef.setClientEnvironment(clientEnv);
        }
        if (isHidden) {
            xDef.setIsHidden(true);
        }
        if (!childrenInherited) {
            xDef.setIsChildrenInherited(false);
        }

    }

    public boolean isRoot() {
        return isRoot;
    }

    public boolean setRoot(boolean isRoot) {
        if (isInBranch() && isTopLevelDefinition()) {
            this.isRoot = isRoot;
            setEditState(EEditState.MODIFIED);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        this.contextlessCommands.visit(visitor, provider);
        this.explorerItems.visit(visitor, provider);
        this.modelClass.visit(visitor, provider);
        if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
            this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).visit(visitor, provider);
        }
        if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
            this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).visit(visitor, provider);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.PARAGRAPH_EXPLORER_ITEM;
    }

    public Id getCustomViewId() {
        return Id.Factory.changePrefix(getId(), EDefinitionIdPrefix.CUSTOM_PARAG_EDITOR);
    }

    @Override
    protected void collectDependencesImpl(boolean direct, boolean forModule, List<Definition> list) {
        if (iconId != null) {
            AdsSearcher.Factory.newImageSearcher(this).findById(iconId).save(list);
        }
        if (logoId != null) {
            AdsSearcher.Factory.newImageSearcher(this).findById(logoId).save(list);
        }
    }

    @Override
    public ClipboardSupport<AdsParagraphExplorerItemDef> getClipboardSupport() {

        return new AdsClipboardSupport<AdsParagraphExplorerItemDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                ParagraphDefinition xDef = ParagraphDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsParagraphExplorerItemDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ParagraphDefinition) {
                    return Factory.loadFrom((ParagraphDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", ParagraphDefinition.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    return null;
                }
            }
        };

    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return AdsExplorerItemWriter.Factory.newInstance(this, AdsParagraphExplorerItemDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.WEB, ERuntimeEnvironmentType.EXPLORER);
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                if (sc == null) {
                    return EnumSet.noneOf(CodeType.class);
                } else {
                    if (isTopLevelDefinition()) {
                        switch (sc) {
                            case SERVER:
                                return EnumSet.of(CodeType.META);
                            case EXPLORER:
                                if (getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT || getClientEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                                    return EnumSet.of(CodeType.META);
                                } else {
                                    return null;
                                }
                            case WEB:
                                if (getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT || getClientEnvironment() == ERuntimeEnvironmentType.WEB) {
                                    return EnumSet.of(CodeType.META);
                                } else {
                                    return null;
                                }
                            default:
                                return null;

                        }
                    } else {
                        return null;
                    }
                }
            }
        };
    }

    @Override
    public void appendTo(org.radixware.schemas.adsdef.AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        ParagraphDefinition xDef = xDefRoot.addNewAdsParagraphDefinition();
        this.appendTo(xDef, saveMode);
    }

    @Override
    public boolean isSaveable() {
        return getContainer() == null || isTopLevelDefinition();
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions || super.isSuitableContainer(collection);
    }

    public boolean isChildRefItemsAllowed() {
        AdsEditorPresentationDef epr = findOwnerEditorPresentation();
        if (epr == null) {
            return false;
        }
        DdsTableDef table = epr.getOwnerClass().findTable(this);
        if (table == null) {
            return false;
        }
        return !table.collectIncomingReferences(new IFilter<DdsReferenceDef>() {
            @Override
            public boolean isTarget(DdsReferenceDef object) {
                return object.getType() != EType.MASTER_DETAIL;
            }
        }).isEmpty();
    }

    @Override
    public boolean allowOverwrite() {
        return super.allowOverwrite();//isTopLevelDefinition(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void afterOverwrite() {
        this.modelClass = AdsParagraphModelClassDef.Factory.newInstance(this);
        this.explorerItems.children.getLocal().clear();
    }

    public boolean isParentRefItemsAllowed() {
        AdsEditorPresentationDef epr = findOwnerEditorPresentation();
        if (epr == null) {
            return false;
        }
        DdsTableDef table = epr.getOwnerClass().findTable(this);
        if (table == null) {
            return false;
        }
        return !table.collectOutgoingReferences(new IFilter<DdsReferenceDef>() {
            @Override
            public boolean isTarget(DdsReferenceDef object) {
                return object.getType() != EType.MASTER_DETAIL;
            }
        }).isEmpty();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.PARAGRAPH;
    }

    @Override
    public ContextlessCommandUsage getUsedContextlessCommands() {
        return contextlessCommands;
    }

    @Override
    public EAccess getMinimumAccess() {
        return isTopLevelDefinition() ? EAccess.DEFAULT : super.getMinimumAccess();
    }

    @Override
    public AdsDefinition findJavaPackageNameProvider() {
        if (isTopLevelDefinition()) {
            return this;
        } else {
            return super.findJavaPackageNameProvider();
        }
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsParagraphExplorerItemDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new ParagraphExpItemRadixdoc(getSource(), page, options);
            }
        };
    }

    public boolean isExplorerItemsInherited() {
//        if (!isTopLevelDefinition()) {
//            return true;
//        } else {
            return childrenInherited;
        //}
    }

    public void setExplorerItemsInherited(boolean inherit) {
        childrenInherited = inherit;
        setEditState(RadixObject.EEditState.MODIFIED);
        EntireChangesSupport.getInstance(AdsExplorerItemDef.class).fireChange(this);
    }
}
