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
package org.radixware.kernel.common.defs.ads.msdl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.common.NameUtil;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.UdsExportable;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.defs.ads.src.xml.AdsXmlJavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.xml.XBeansTypeSystem;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.IAdsTypeSource;
import org.radixware.kernel.common.defs.ads.type.XmlType;
import org.radixware.kernel.common.defs.ads.xml.AbstractXmlDefinition;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.msdl.IEnumLookuper;
import org.radixware.kernel.common.msdl.RootMsdlScheme;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.XmlFormatter;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.schemas.acsImpexp.MsdlSchemesDocument;
import org.radixware.schemas.acsImpexp.UserDefinedDefs;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.MsdlDefinition;
import org.radixware.schemas.msdl.Message;
import org.radixware.schemas.msdl.MessageElementDocument;

public class AdsMsdlSchemeDef extends AbstractXmlDefinition<MsdlDefinition> implements IAdsTypeSource, IXmlDefinition, IJavaSource, UdsExportable, IEnumLookuper {

    private RootMsdlScheme rootMsdlScheme;
    private boolean isDeprecated = false;
    private String storedTargetNamespace;
    private Id runtimeId = null;

    public Id getRuntimeId() {
        synchronized (this) {
            if (runtimeId == null) {
                runtimeId = Id.Factory.newInstance(EDefinitionIdPrefix.MSDL_SCHEME);
            }
            return runtimeId;
        }
    }

    public RootMsdlScheme getRootMsdlScheme() {
        return rootMsdlScheme;
    }

    @Override
    public String getTargetNamespace() {
        if (rootMsdlScheme != null) {
            return rootMsdlScheme.getNamespace();
        }
        return storedTargetNamespace;
    }

    public void setDeprecated(boolean isDeprecated) {
        if (isDeprecated != this.isDeprecated) {
            this.isDeprecated = isDeprecated;
            setEditState(EEditState.MODIFIED);
            fireNameChange();
        }
    }

    @Override
    public Definition findEnumDef(Id id) {
        return AdsSearcher.Factory.newAdsEnumSearcher(getModule()).findById(id).get();
    }

    @Override
    public boolean isDeprecated() {
        return isDeprecated;
    }

    @Override
    public List<IXmlDefinition> getImportedDefinitions() {
        ArrayList<IXmlDefinition> defs = new ArrayList<>();
        List<String> nss = getImportedNamespaces();
        AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(this);
        for (String ns : nss) {
            final IXmlDefinition def = searcher.findByNs(ns).get();
            if (def != null) {
                defs.add(def);
            }
        }
        return defs;
    }

    @Override
    public List<String> getImportedNamespaces() {
        final ArrayList<String> nss = new ArrayList<>();
        nss.add(XmlUtils.RDX_TYPES_XMLNS);
        return nss;
    }

    @Override
    public XmlObject getXmlContent() {
        return rootMsdlScheme == null ? null : rootMsdlScheme.getMessage();
    }

    @Override
    public XmlObject getXmlDocument() {
        final MessageElementDocument xDoc = MessageElementDocument.Factory.newInstance();
        if (rootMsdlScheme != null) {
            xDoc.setMessageElement(rootMsdlScheme.getMessage());
        }
        return xDoc;
    }

    @Override
    public ERuntimeEnvironmentType getTargetEnvironment() {
        return ERuntimeEnvironmentType.COMMON;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public String getSchemaFileType() {
        return "msdl";
    }

    public static final class Factory {

        public static AdsMsdlSchemeDef loadFrom(MsdlDefinition xDef) {
            return new AdsMsdlSchemeDef(xDef);
        }

        public static AdsMsdlSchemeDef newInstance() {
            return new AdsMsdlSchemeDef("NewMsdlScheme");
        }

        public static AdsMsdlSchemeDef newInstance(final Id id, final String name) {
            return new AdsMsdlSchemeDef(id, name);
        }
    }

    private AdsMsdlSchemeDef(final String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.MSDL_SCHEME), name);
        final Message message = Message.Factory.newInstance();
        message.addNewStructure();
        message.setName(name);
        rootMsdlScheme = new RootMsdlScheme(message);
        rootMsdlScheme.setName(name);
        rootMsdlScheme.setContainer(this);
        rootMsdlScheme.setSchemeSearcher(new TemplateSchemeSearcher(this));
    }

    private AdsMsdlSchemeDef(final MsdlDefinition xDef) {
        super(xDef);
        isDeprecated = xDef.getIsDeprecated();
        storedTargetNamespace = xDef.getTargetNamespace();
    }

    private AdsMsdlSchemeDef(final Id id, final String name) {
        this(name);
        setId(id);
        //isDeprecated = xDef.getIsDeprecated();
        //storedTargetNamespace = xDef.getTargetNamespace();
    }

    @Override
    protected XBeansTypeSystem checkFixedTypeSystem(MsdlDefinition xDef) {
        if (xDef.isSetMessageElement()) {
            rootMsdlScheme = new RootMsdlScheme(xDef.getMessageElement());
            rootMsdlScheme.setName(getName());
            rootMsdlScheme.setContainer(this);
            rootMsdlScheme.setSchemeSearcher(new TemplateSchemeSearcher(this));
            return null;
        } else {
            return new XBeansTypeSystem(this, xDef.getTypeList());
        }
    }

    public void setNamespace(String namespace) {
        if (rootMsdlScheme != null) {
            rootMsdlScheme.setNamespace(namespace);
        }
    }

    @Override
    public boolean setName(String name) {
        if (rootMsdlScheme != null) {
            rootMsdlScheme.setName(name);
        }
        return super.setName(name);
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        final MsdlDefinition def = xDefRoot.addNewAdsMsdlSchemeDefinition();
        super.appendTo(def, saveMode);
        def.setTargetNamespace(getTargetNamespace());
        if (isDeprecated) {
            def.setIsDeprecated(true);
        }
        if (saveMode == ESaveMode.API) {
            final XBeansTypeSystem ts = getSchemaTypeSystem();
            if (ts != null) {
                ts.appendTo(def.addNewTypeList());
            }
        } else if (rootMsdlScheme != null) {
            def.setMessageElement(rootMsdlScheme.getMessage());
        }
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.MSDL_SCHEME;
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    private class AdsMsdlSchemeDefClipboardSupport extends ClipboardSupport<AdsMsdlSchemeDef> {

        public AdsMsdlSchemeDefClipboardSupport() {
            super(AdsMsdlSchemeDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            final MsdlDefinition msdl = MsdlDefinition.Factory.newInstance();
            msdl.setMessageElement(rootMsdlScheme.getMessage());
            msdl.setId(Id.Factory.newInstance(EDefinitionIdPrefix.MSDL_SCHEME));
            msdl.setName(AdsMsdlSchemeDef.this.getName());
            return msdl;
        }

        @Override
        protected AdsMsdlSchemeDef loadFrom(XmlObject xmlObject) {
            final MsdlDefinition msdl = (MsdlDefinition) xmlObject;
            return AdsMsdlSchemeDef.Factory.loadFrom(msdl);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsMsdlSchemeDef> getClipboardSupport() {
        return new AdsMsdlSchemeDefClipboardSupport();
    }

    @Override
    public AdsType getType(EValType typeId, String extStr) {
        return XmlType.Factory.newInstance(this, extStr);
    }

    @Override
    public AdsXmlJavaSourceSupport getJavaSourceSupport() {
        return AdsXmlJavaSourceSupport.Factory.newInstance(this);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (rootMsdlScheme != null) {
            rootMsdlScheme.visit(visitor, provider);
        }
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(ERuntimeEnvironmentType.COMMON);
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        return ERuntimeEnvironmentType.COMMON;
    }

    @Override
    public String getJavaPackageName() {
        if (isTransparent()) {
            return NameUtil.getPackageFromNamespace(this.getTargetNamespace());
        } else {
            return JavaSourceSupport.getPackageName(this, JavaSourceSupport.UsagePurpose.getPurpose(ERuntimeEnvironmentType.COMMON, JavaSourceSupport.CodeType.EXCUTABLE));
        }
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ModuleDefinitions;
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.MSDL_SCHEME;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        final RootMsdlScheme scheme = getRootMsdlScheme();
        if (scheme != null) {
            sb.append("<br>").append(scheme.getNamespace());
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
        final AdsSearcher.Factory.XmlDefinitionSearcher searcher = AdsSearcher.Factory.newXmlDefinitionSearcher(this);
        final IXmlDefinition def = searcher.findByNs(XmlUtils.RDX_TYPES_XMLNS).get();
        if (def instanceof Definition) {
            list.add((Definition) def);
        }
    }

    @Override
    public String getTypeTitle() {
        return "Msdl Schema";
    }

    @Override
    public void exportToUds(OutputStream out) throws IOException {
        final MsdlSchemesDocument xDoc = MsdlSchemesDocument.Factory.newInstance();

        final UserDefinedDefs.UserDefinedDef role = xDoc.addNewMsdlSchemes().addNewUserDefinedDef();
        role.setGuid(this.getId().toString());
        role.setTitle(this.getName());
        this.appendTo(role.addNewDefinition(), ESaveMode.NORMAL);

        role.setDescription(getDescription());

        XmlFormatter.save(xDoc, out);
    }
}
