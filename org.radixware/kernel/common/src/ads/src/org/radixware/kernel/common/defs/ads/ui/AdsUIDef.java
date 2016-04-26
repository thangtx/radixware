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
package org.radixware.kernel.common.defs.ads.ui;

import java.util.Set;
import java.util.List;
import java.util.EnumSet;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObjectInitializationPolicy;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.ui.generation.AdsUIWriter;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;

public abstract class AdsUIDef extends AdsAbstractUIDef<AdsUIDef> implements IJavaSource, IAdsTypeSource {

    private AdsWidgetDef widget;

    public AdsUIDef(Id id, String name) {
        super(id, name);
        this.widget = new AdsWidgetDef(this, getClassName());
    }

    public AdsUIDef(Id id, String name, AbstractDialogDefinition xDef) {
        super(id, xDef);
        setName(name);
        
        if (!RadixObjectInitializationPolicy.get().isRuntime()){
            if (xDef.getUi() == null) {
                widget = new AdsWidgetDef(this, getClassName());
            } else {
                widget = new AdsWidgetDef(this, xDef.getUi().getWidget());
                if (!getClassName().equals(widget.getClassName())) {
                    widget.className = getClassName();
                }
            }
        }
    }

    @Override
    public AdsWidgetDef getWidget() {
        AdsUIDef ovr = (AdsUIDef) getHierarchy().findOverwritten().get();
        if (ovr != null) {
            return ovr.getWidget();
        }
        return widget;
    }

    @Override
    public void afterOverwrite() {
        this.widget.getWidgets().clear();
        if (this.widget.getLayout() != null) {
            this.widget.getLayout().getItems().clear();
        }
        getConnections().clear();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_FORM_HANDLER;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (widget!=null){
            widget.visit(visitor, provider);
        }

    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        if (widget != null) {
            list.add(widget);
        }
    }

    @Override
    protected void appendTo(AbstractDialogDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (xDef.getUi() == null) {
            xDef.addNewUi();
        }
        if (widget != null) {
            widget.appendTo(xDef.getUi().addNewWidget());
        }

    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        if (id.getPrefix() == EDefinitionIdPrefix.WIDGET) {
            AdsWidgetDef widget = getWidget();
            if (widget != null) {
                return widget.findComponentDefinition(id);
            } else {
                return null;//TODO:
            }
        }
        return super.findComponentDefinition(id);
    }

    protected class UIJavaSourceSupport extends JavaSourceSupport {

        public UIJavaSourceSupport() {
            super(AdsUIDef.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new AdsUIWriter(this, AdsUIDef.this, purpose);
        }

        @Override
        public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
            return EnumSet.of(/*
                     * ESystemComponent.COMMON,
                     */ERuntimeEnvironmentType.EXPLORER);
        }

        @Override
        public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
            return sc == ERuntimeEnvironmentType.EXPLORER ? EnumSet.of(CodeType.EXCUTABLE) : null;
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new UIJavaSourceSupport();
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.EXPLORER);
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.DEFAULT;
    }

    @Override
    public boolean canChangeAccessMode() {
        return true;
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.EXPLORER;
    }

    @Override
    public String getTypeTitle() {
        return super.getTypeTitle() + " for " + getUsageEnvironment().getName();
    }
}
