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
package org.radixware.kernel.common.defs.ads.ui.rwt;

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.SearchResult;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.ui.rwt.AdsRwtUICodeWriter;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsWidgetDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.adsdef.AbstractDialogDefinition;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;

public abstract class AdsRwtUIDef extends AdsAbstractUIDef<AdsRwtUIDef> implements IJavaSource {

    private final AdsRwtWidgetDef widget;
    private String customCode;

    protected AdsRwtUIDef(Id id, String name) {
        super(id, name);
        if (name != null) {
            setName(name);
        }
        this.widget = new AdsRwtWidgetDef(this, getClassName());
    }

    protected AdsRwtUIDef(Id id, AbstractDialogDefinition xDef) {
        this(id, null, xDef);
    }

    protected AdsRwtUIDef(Id id, String name, AbstractDialogDefinition xDef) {
        super(id, xDef);
        if (name != null) {
            setName(name);
        }
        if (xDef.getUi() != null) {
            if (xDef.getUi().getWidget() != null) {
                this.widget = new AdsRwtWidgetDef(this, xDef.getUi().getWidget());
            } else {
                this.widget = new AdsRwtWidgetDef(this, getClassName());
            }
            customCode = xDef.getUi().getCustomCode();
        } else {
            this.widget = new AdsRwtWidgetDef(this, getClassName());
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.WEB;
    }

    @Override
    protected void appendTo(AbstractDialogDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (xDef.getUi() == null) {
            xDef.addNewUi();
        }
        widget.appendTo(xDef.getUi().addNewWidget());
        if (customCode != null && !customCode.isEmpty()) {
            xDef.getUi().setCustomCode(customCode);
        }
    }

    @Override
    public AdsRwtWidgetDef getWidget() {
        AdsRwtUIDef ovr = (AdsRwtUIDef) getHierarchy().findOverwritten().get();
        if (ovr != null) {
            return ovr.getWidget();
        }
        return widget;
    }

    @Override
    public SearchResult<? extends AdsDefinition> findComponentDefinition(Id id) {
        if (id.getPrefix() == EDefinitionIdPrefix.WIDGET) {
            final AdsRwtWidgetDef widget = getWidget();
            if (widget != null) {
                return widget.findComponentDefinition(id);
            } else {
                return null;//TODO:
            }
        }
        return super.findComponentDefinition(id);
    }

    @Override
    public void afterOverwrite() {
        widget.getWidgets().clear();
        getConnections().clear();
    }

    @Override
    public abstract void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode);

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsRwtUICodeWriter(this, AdsRwtUIDef.this, purpose);
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                if (sc == ERuntimeEnvironmentType.WEB) {
                    String js = getJsCode();
                    if (js == null || js.isEmpty()) {
                        return EnumSet.of(CodeType.EXCUTABLE);
                    } else {
                        return EnumSet.of(CodeType.EXCUTABLE, CodeType.ADDON);
                    }
                } else {
                    return EnumSet.noneOf(CodeType.class);
                }
            }

            @Override
            public boolean isSeparateFilesRequired(ERuntimeEnvironmentType sc) {
                return sc == ERuntimeEnvironmentType.WEB;
            }

            @Override
            public String getSourceFileExtension(UsagePurpose purpose) {
                if (purpose == UsagePurpose.WEB_ADDON) {
                    return "js";
                } else {
                    return super.getSourceFileExtension(purpose); //To change body of generated methods, choose Tools | Templates.
                }
            }
        };
    }

    public abstract char[] getSuperClassName();

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.WEB);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        widget.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_FORM_HANDLER;
    }

    public String getJsCode() {
        return customCode == null ? "" : customCode;
    }

    public void setJsCode(String code) {
        if (!Utils.equals(customCode, code)) {
            this.customCode = code;
            setEditState(EEditState.MODIFIED);
        }
    }
}
