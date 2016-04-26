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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport;

import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.types.Id;

import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.profiling.AdsProfileSupport.IProfileable;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.ads.src.command.AdsCommandWriter;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;

import org.radixware.kernel.common.defs.ads.src.command.AdsContextlessCommandWriter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.jml.IJmlSource;

import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.LineMatcher.ILocationDescriptor;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.ContextlessCommandDefinition;


public class AdsContextlessCommandDef extends AdsCommandDef implements IJavaSource, IJmlSource, IProfileable {

    @Override
    public EDefType getDefinitionType() {
        return EDefType.CONTEXTLESS_COMMAND; 
    }

    @Override
    public AdsProfileSupport getProfileSupport() {
        return profileSupport;
    }

    @Override
    public boolean isProfileable() {
        return true;
    }

    public static class Factory {

        public static final AdsContextlessCommandDef loadFrom(ContextlessCommandDefinition xDef) {
            return new AdsContextlessCommandDef(xDef);
        }

        public static AdsContextlessCommandDef newInstance() {
            return new AdsContextlessCommandDef("NewContextlessCommand");
        }
    }
    private Jml source;
    private final AdsProfileSupport profileSupport = new AdsProfileSupport(this);

    private AdsContextlessCommandDef(ContextlessCommandDefinition xDef) {
        super(xDef);
        this.source = Jml.Factory.loadFrom(this, xDef.getSource(), "Source");
        if (source == null) {
            source = Jml.Factory.newInstance(this, "Source");
        }
        if (xDef.getProfileInfo() != null) {
            this.profileSupport.loadFrom(xDef.getProfileInfo());
        }
    }

    private AdsContextlessCommandDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.CONTEXTLESS_COMMAND), name);
        this.source = Jml.Factory.newInstance(this, "Source");
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        ContextlessCommandDefinition xDef = xDefRoot.addNewAdsContextlessCommandDefinition();
        appendTo(xDef, saveMode);
    }

    protected void appendTo(ContextlessCommandDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        this.source.appendTo(xDef.addNewSource(), saveMode);
        if (profileSupport.isProfiled()) {
            profileSupport.appendTo(xDef.addNewProfileInfo());
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CONTEXTLESS_COMMAND;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public Jml getSource() {
        return source;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        source.visit(visitor, provider);
    }

    @Override
    public Jml getSource(String name) {
        return source;
    }

    @Override
    public CommandJavaSourceSupport getJavaSourceSupport() {
        return new CommandJavaSourceSupport() {

            @Override
            public AdsCommandWriter<AdsContextlessCommandDef> getCodeWriter(UsagePurpose purpose) {
                return new AdsContextlessCommandWriter(this, AdsContextlessCommandDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.COMMON_CLIENT);
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                return sc == ERuntimeEnvironmentType.SERVER || sc == ERuntimeEnvironmentType.COMMON_CLIENT ? EnumSet.of(CodeType.EXCUTABLE, CodeType.META) : null;
            }
        };
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.SERVER, ERuntimeEnvironmentType.EXPLORER, ERuntimeEnvironmentType.COMMON_CLIENT, ERuntimeEnvironmentType.WEB);
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.SERVER;
    }

    @Override
    public ClipboardSupport<AdsContextlessCommandDef> getClipboardSupport() {
        return new AdsClipboardSupport<AdsContextlessCommandDef>(this) {

            @Override
            protected XmlObject copyToXml() {
                ContextlessCommandDefinition xDef = ContextlessCommandDefinition.Factory.newInstance();
                appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsContextlessCommandDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof ContextlessCommandDefinition) {
                    return Factory.loadFrom((ContextlessCommandDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }
        };
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;
    }

    @Override
    public EAccess getMinimumAccess() {
        return EAccess.DEFAULT;
    }

    @Override
    public AdsDefinition getAdsDefinition() {
        return this;
    }

    public ILocationDescriptor getLocationDescriptor() {
        return source.getLocationDescriptor();
    }

    @Override
    public boolean isFinal() {
        return true;
    }

    @Override
    public boolean canChangeFinality() {
        return false;
    }
    
    public Profile computeProfile(){
        return new Profile(this);
    }
    
    public class Profile {
        public final String ACCESS_FlAGS = "public static final";
        public final String FUNCTION_NAME = "execute";
        public final String ARTE_PARAMETER_TYPE = new String(WriterUtils.ARTE_CLASS_NAME);
        public final String ARTE_PARAMETER_NAME = "arte";
        public final String INPUT_PARAMETER_NAME = "input";
        public final String EXEPTION = "org.radixware.kernel.common.exceptions.AppException";
        
        private final AdsTypeDeclaration inType;
        private final AdsTypeDeclaration outType;
        
        Profile(AdsContextlessCommandDef comand){
            inType = comand.getData().getInType();
            outType = comand.getData().getOutType();
        }
        
        
        public String getProfileHtml(){
            return print(true);
        }
        
        private String getHTMLParameterName(String name){
            return "<font color=\"d3730b\">" + name + "</font>";
        }
        
        private String print(boolean isHtml){
            StringBuilder s  = new StringBuilder();
            if (isHtml){
                s.append(outType.getHtmlName(AdsContextlessCommandDef.this,false));
            } else {
                s.append(outType.getQualifiedName(AdsContextlessCommandDef.this));
            }
            s.append(" ");
            s.append(FUNCTION_NAME);
            s.append("(");
            s.append(ARTE_PARAMETER_TYPE);
            s.append(" ");
            if (isHtml){
                s.append(getHTMLParameterName(ARTE_PARAMETER_NAME));
            } else {
                s.append(ARTE_PARAMETER_NAME);
            }
            if (inType != AdsTypeDeclaration.Factory.voidType()) {
                s.append(", ");
                if (isHtml) {
                    s.append(inType.getHtmlName(AdsContextlessCommandDef.this, false));
                    s.append(" ");
                    s.append(getHTMLParameterName(INPUT_PARAMETER_NAME));
                } else {
                    s.append(inType.getQualifiedName(AdsContextlessCommandDef.this));
                    s.append(" ");
                    s.append(INPUT_PARAMETER_NAME);
                }
            }
            s.append(")");
            s.append(" throws ");
            s.append(EXEPTION);
            return s.toString();
        }
        
        @Override
        public String toString() {
            return print(false);
        }
        
        
    }
}
