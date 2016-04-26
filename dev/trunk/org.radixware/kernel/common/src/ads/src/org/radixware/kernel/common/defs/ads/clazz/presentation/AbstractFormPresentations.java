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

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.IModalDisplayable;
import org.radixware.kernel.common.defs.ads.clazz.AbstractFormModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsModelClassDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.clazz.ClassHierarchyIterator;
import org.radixware.kernel.common.defs.ads.clazz.IAdsFormPresentableClass;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.IEditorPagesContainer;
import org.radixware.kernel.common.defs.ads.common.ICustomViewable;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.presentation.AdsClassPresentationsWriter;
import org.radixware.kernel.common.defs.ads.ui.AbstractCustomFormDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.rwt.AbstractRwtCustomFormDialogDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EPresentationAttrInheritance;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.EXPLORER;
import static org.radixware.kernel.common.enums.ERuntimeEnvironmentType.WEB;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.adsdef.Form;
import org.radixware.schemas.adsdef.Size;

public abstract class AbstractFormPresentations<T extends AdsClassDef & IAdsFormPresentableClass> extends ClassPresentations implements ICustomViewable<AbstractFormPresentations, AdsAbstractUIDef>, IEditorPagesContainer, IModalDisplayable {

    private EnumSet<EPresentationAttrInheritance> inheritanceMask;
    private EditorPages editorPages;
    private Id iconId = null;
    private List<Id> commandsOrder = null;
    private AbstractFormModelClassDef modelClass;
    private final ModialViewSizeInfo explorerDialogSize = new ModialViewSizeInfo(this);
    private final ModialViewSizeInfo webDialogSize = new ModialViewSizeInfo(this);
    private final transient ICustomViewable.CustomViewSupport<AbstractFormPresentations, AdsAbstractUIDef> customViewSuppoort = new CustomViewSupport<AbstractFormPresentations, AdsAbstractUIDef>(this) {
        @Override
        protected AdsAbstractUIDef createOrLoadCustomView(final AbstractFormPresentations context, ERuntimeEnvironmentType env, final AbstractDialogDefinition xDef) {
            if (env == ERuntimeEnvironmentType.EXPLORER) {
                if (xDef == null) {
                    return AbstractCustomFormDialogDef.Factory.newInstance(context);
                } else {
                    return AbstractCustomFormDialogDef.Factory.loadFrom(context, xDef);
                }
            } else if (env == ERuntimeEnvironmentType.WEB) {
                if (xDef == null) {
                    return AbstractRwtCustomFormDialogDef.Factory.newInstance(context);
                } else {
                    return AbstractRwtCustomFormDialogDef.Factory.loadFrom(context, xDef);
                }
            } else {
                throw new UnsupportedOperationException("Not supported yet");
            }
        }

        @Override
        public boolean isUseCustomView(ERuntimeEnvironmentType env) {
            final AdsClassDef clazz = getOwnerClass();
            if (clazz == null) {
                return super.isUseCustomView(env);
            } else {
                final ERuntimeEnvironmentType cenv = clazz.getClientEnvironment();
                if (cenv == env || cenv == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    return super.isUseCustomView(env);
                } else {
                    return false;
                }
            }
        }

        @Override
        public AdsAbstractUIDef getCustomView(ERuntimeEnvironmentType env) {
            if (isUseCustomView(env)) {
                return super.getCustomView(env);
            } else {
                return null;
            }
        }
    };

    @Override
    public ModialViewSizeInfo getModialViewSizeInfo(ERuntimeEnvironmentType env) {
        switch (env) {
            case EXPLORER:
                return explorerDialogSize;
            case WEB:
                return webDialogSize;
            default:
                return null;
        }
    }

    @Override
    public CustomViewSupport<AbstractFormPresentations, AdsAbstractUIDef> getCustomViewSupport() {
        return customViewSuppoort;
    }

    public Set<EPresentationAttrInheritance> getInheritanceMask() {
        return EnumSet.copyOf(inheritanceMask);
    }

    @SuppressWarnings("unchecked")
    protected AbstractFormPresentations(final T owner, final ClassDefinition.Presentations xDef) {
        super(owner, xDef);
        final Form form;
        if (xDef == null) {
            form = null;
        } else {
            form = xDef.getForm();
            this.iconId = Id.Factory.loadFrom(xDef.getIconId());
        }
        if (form == null) {
            this.inheritanceMask = EnumSet.noneOf(EPresentationAttrInheritance.class);
            this.editorPages = EditorPages.Factory.newInstance(owner);
            if (owner.isModelRequred()) {
                this.modelClass = AbstractFormModelClassDef.Factory.newInstance(owner);
            }
        } else {
            this.webDialogSize.loadFrom(form.getWebDialogSize());
            this.explorerDialogSize.loadFrom(form.getDialogSize());
            this.inheritanceMask = EPresentationAttrInheritance.fromBitField(form.getInheritanceMask());
            this.editorPages = EditorPages.Factory.loadFrom(owner, form.getEditorPages());
            if (owner.isModelRequred()) {
                if (form.getModel() != null) {//old style definition
                    this.modelClass = AbstractFormModelClassDef.Factory.loadFrom(owner, form.getModel());
                } else {
                    this.modelClass = AbstractFormModelClassDef.Factory.newInstance(owner);
                }
            }

            if (form.getView() != null) {
                customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.EXPLORER, form.getView());
            }
            if (form.getWebView() != null) {
                customViewSuppoort.loadCustomView(ERuntimeEnvironmentType.WEB, form.getWebView());
            }
            if (form.getCommands() != null) {
                this.commandsOrder = new ArrayList<Id>();
                this.commandsOrder.addAll(form.getCommands());
            }
        }
    }

    public List<Id> getCommandsOrder() {
        synchronized (this) {
            if (commandsOrder == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<Id>(commandsOrder);
            }
        }
    }

    public void setCommandsOrder(final List<Id> ids) {
        synchronized (this) {
            this.commandsOrder = new ArrayList<Id>(ids);
            setEditState(EEditState.MODIFIED);
        }
    }

    public Id getIconId() {
        synchronized (this) {
            return findAttributeOwner(EPresentationAttrInheritance.ICON).iconId;
        }
    }

    public boolean setIconId(final Id id) {
        synchronized (this) {
            if (isIconInherited()) {
                return false;
            }
            this.iconId = id;
            setEditState(EEditState.MODIFIED);
            return true;
        }
    }

    public boolean isIconInherited() {
        synchronized (this) {
            return inheritanceMask.contains(EPresentationAttrInheritance.ICON);
        }
    }

    public boolean setIconInherited(final boolean inherit) {
        synchronized (this) {
            if (inherit) {
                if (!isIconInherited()) {
                    inheritanceMask.add(EPresentationAttrInheritance.ICON);
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
            } else {
                if (isIconInherited()) {
                    inheritanceMask.remove(EPresentationAttrInheritance.ICON);
                    setEditState(EEditState.MODIFIED);
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public void appendTo(final ClassDefinition.Presentations xDef, final ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        final Form form = xDef.addNewForm();
        form.setInheritanceMask(EPresentationAttrInheritance.toBitField(inheritanceMask));
        if (saveMode == ESaveMode.NORMAL || (saveMode == ESaveMode.API && !getOwnerClass().isFinal())) {
            if (!isEditorPagesInherited()) {
                this.editorPages.appendTo(form.addNewEditorPages(), saveMode);
            }
            if (!isCustomViewInherited()) {
                if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
                    ((AbstractCustomFormDialogDef) customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER)).appendTo(form/*
                             * .addNewView()
                             */, saveMode);
                }
                if (customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
                    ((AbstractRwtCustomFormDialogDef) customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB)).appendTo(form/*
                             * .addNewView()
                             */, saveMode);
                }
            }
        }
        if (modelClass != null) {
            modelClass.appendTo(form.addNewModel(), saveMode);
        }

        if (saveMode == ESaveMode.NORMAL) {
            if (commandsOrder != null && !commandsOrder.isEmpty()) {
                form.setCommands(commandsOrder);
            }
            Size xSize = Size.Factory.newInstance();
            if (this.webDialogSize.appendTo(xSize)) {
                form.setWebDialogSize(xSize);
            }
            xSize = Size.Factory.newInstance();
            if (this.explorerDialogSize.appendTo(xSize)) {
                form.setDialogSize(xSize);
            }
        }

        if (iconId != null) {
            xDef.setIconId(iconId.toString());
        }
    }

    public boolean isEditorPagesInherited() {
        synchronized (this) {
            return inheritanceMask.contains(EPresentationAttrInheritance.PAGES);
        }
    }

    public boolean setEditorPagesInherited(final boolean inherit) {
        synchronized (this) {
            if (inherit) {
                if (!isEditorPagesInherited()) {
                    inheritanceMask.add(EPresentationAttrInheritance.PAGES);
                    setEditState(EEditState.MODIFIED);
                    fireEditorPagesInheritanceChange();
                    return true;
                }
            } else {
                if (isEditorPagesInherited()) {
                    inheritanceMask.remove(EPresentationAttrInheritance.PAGES);
                    setEditState(EEditState.MODIFIED);
                    fireEditorPagesInheritanceChange();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public EditorPages getEditorPages() {
        synchronized (this) {
            return findAttributeOwner(EPresentationAttrInheritance.PAGES).editorPages;
        }
    }

    public AbstractFormModelClassDef getModel() {
        return modelClass;
    }

    public boolean isCustomViewInherited() {
        synchronized (this) {
            return inheritanceMask.contains(EPresentationAttrInheritance.CUSTOM_DIALOG);
        }
    }

    public boolean setCustomViewInherited(final boolean inherit) {
        synchronized (this) {
            if (inherit) {
                if (!isCustomViewInherited()) {
                    inheritanceMask.add(EPresentationAttrInheritance.CUSTOM_DIALOG);
                    setEditState(EEditState.MODIFIED);
                    fireCustomViewInheritanceChange();
                    return true;
                }
            } else {
                if (isCustomViewInherited()) {
                    inheritanceMask.remove(EPresentationAttrInheritance.CUSTOM_DIALOG);
                    setEditState(EEditState.MODIFIED);
                    fireCustomViewInheritanceChange();
                    return true;
                }
            }
            return false;
        }
    }

//    public Id getCustomViewId() {
//        synchronized (this) {
//            final AbstractFormPresentations owner = findAttributeOwner(EPresentationAttrInheritance.CUSTOM_DIALOG);
//            return Id.Factory.changePrefix(owner.getOwnerClass().getId(), getCustomViewIdPrefix());
//        }
//    }
    public abstract EDefinitionIdPrefix getCustomViewIdPrefix();

    @Override
    public void visitChildren(final IVisitor visitor, final VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (modelClass != null) {
            modelClass.visit(visitor, provider);
        }
        this.editorPages.visit(visitor, provider);

        if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.EXPLORER)) {
            this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.EXPLORER).visit(visitor, provider);
        }
        if (this.customViewSuppoort.isUseCustomView(ERuntimeEnvironmentType.WEB)) {
            this.customViewSuppoort.getCustomView(ERuntimeEnvironmentType.WEB).visit(visitor, provider);
        }

    }

    protected AbstractFormPresentations findAttributeOwner(final EPresentationAttrInheritance attribute) {
        if (inheritanceMask.contains(attribute)) {
            final ClassHierarchyIterator iter = new ClassHierarchyIterator(getOwnerClass(), EScope.ALL);

            AdsClassDef next = iter.next().first();
            assert next == getOwnerClass();

            while (iter.hasNext()) {
                next = iter.next().first();
                if (next instanceof IAdsFormPresentableClass) {
                    final AbstractFormPresentations prs = ((IAdsFormPresentableClass) next).getPresentations();
                    if (!prs.inheritanceMask.contains(attribute)) {
                        return prs;
                    }
                } else {
                    break;
                }
            }
            return this;
        } else {
            return this;
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(final UsagePurpose purpose) {
                return AdsClassPresentationsWriter.Factory.newInstance(AbstractFormPresentations.this, this, purpose);
            }
        };
    }
    private transient RadixEventSource epSupport = null;

    public RadixEventSource getEditorPagesInheritanceChangesSupport() {
        synchronized (this) {
            if (epSupport == null) {
                this.epSupport = new RadixEventSource();
            }
            return epSupport;
        }
    }

    @SuppressWarnings("unchecked")
    private void fireEditorPagesInheritanceChange() {
        if (epSupport != null) {
            epSupport.fireEvent(new RadixEvent());
        }
    }
    private transient RadixEventSource cvInheritance = null;

    public RadixEventSource getCustomViewInheritanceChangesSupport() {
        synchronized (this) {
            if (cvInheritance == null) {
                cvInheritance = new RadixEventSource();
            }
            return cvInheritance;
        }
    }

    @SuppressWarnings("unchecked")
    private void fireCustomViewInheritanceChange() {
        synchronized (this) {
            if (cvInheritance != null) {
                cvInheritance.fireEvent(new RadixEvent());
            }
        }
    }

    @Override
    public ERuntimeEnvironmentType getClientEnvironment() {
        return getOwnerClass().getClientEnvironment();
    }

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        AdsAbstractUIDef def = getCustomViewSupport().getCustomView(WEB);
        if (def != null) {
            def.afterOverwrite();
        }
        def = getCustomViewSupport().getCustomView(EXPLORER);
        if (def != null) {
            def.afterOverwrite();
        }
        if (modelClass != null) {
            modelClass.afterOverwrite();
        }
    }

}
