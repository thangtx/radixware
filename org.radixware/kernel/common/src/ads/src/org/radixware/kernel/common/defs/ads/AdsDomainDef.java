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

package org.radixware.kernel.common.defs.ads;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ClipboardSupport.Transfer;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.IOverwritable;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.radixdoc.DomainRadixdoc;
import org.radixware.kernel.common.defs.ads.src.AdsDomainWriter;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaFileSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.DomainDefinition;
import org.radixware.schemas.radixdoc.Page;


public class AdsDomainDef extends AdsTitledDefinition implements IJavaSource, IOverwritable, IRadixdocProvider {

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsDomainWriter(this, AdsDomainDef.this, purpose);
            }

            @Override
            public EnumSet<ERuntimeEnvironmentType> getSupportedEnvironments() {
                return EnumSet.of(ERuntimeEnvironmentType.COMMON);
            }

            @Override
            public Set<CodeType> getSeparateFileTypes(ERuntimeEnvironmentType sc) {
                return sc == ERuntimeEnvironmentType.COMMON /*&& isTopLevelDefinition()*/ ? EnumSet.of(CodeType.META) : null;
            }

            @Override
            public JavaFileSupport.FileWriter getSourceFileWriter() {
                return new DomainFileWriter();
            }

            class DomainFileWriter extends JavaSourceSupport.SourceFileWriter {

                public DomainFileWriter() {
                    super();
                }
 
                private boolean w(final File p, final UsagePurpose purpose, final boolean force, final Collection<File> writtenFiles) throws IOException {
                    return super.writeDefinition(p, purpose, force, writtenFiles);
                }

                @Override
                protected boolean writeDefinition(final File packageDir, final UsagePurpose purpose, final boolean force, final Collection<File> writtenFiles) throws IOException {
                    final ArrayList<AdsDomainDef> domains = new ArrayList<>();
                    getCurrentRoot().visit(new IVisitor() {
                        @Override
                        public void accept(RadixObject radixObject) {
                            domains.add((AdsDomainDef) radixObject);
                        }
                    }, new VisitorProvider() {
                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof AdsDomainDef;
                        }
                    });
                    for (AdsDomainDef d : domains) {
                        DomainFileWriter w = (DomainFileWriter) d.getJavaSourceSupport().getSourceFileWriter();
                        if (!w.w(packageDir, purpose, force, writtenFiles)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        };
    }

    @Override
    public void afterOverwrite() {
        this.getChildDomains().clear();
    }

    @Override
    public boolean allowOverwrite() {
        return isTopLevelDefinition();
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsDomainDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DomainRadixdoc(AdsDomainDef.this, page, options);
            }
        };
    }

    @Override
    public boolean isRadixdocProvider() {
       return isTopLevelDefinition();
    }

    private class ChildDomains extends AdsDefinitions<AdsDomainDef> {

        public void loadFrom(List<DomainDefinition> children) {
            if (children != null) {
                for (DomainDefinition xDef : children) {
                    add(new AdsDomainDef(xDef));
                }
            }
        }

        public void appendTo(DomainDefinition xDef, ESaveMode saveMode) {
            if (!isEmpty()) {
                for (AdsDomainDef def : list()) {
                    def.appendTo(xDef.addNewDomain(), saveMode);
                }
            }
        }

        public ChildDomains() {
            super(AdsDomainDef.this);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static AdsDomainDef loadFrom(DomainDefinition xDef) {
            return new AdsDomainDef(xDef);
        }

        public static AdsDomainDef newInstance() {
            return new AdsDomainDef("newDomain");
        }
    }
    private final ChildDomains childDomains = new ChildDomains();
    private boolean isDeprecated = false;

    private AdsDomainDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.DOMAIN), name, null);
    }

    private void load(DomainDefinition xDef) {
        if (xDef.getDomainList() != null) {
            this.childDomains.loadFrom(xDef.getDomainList());
        }
        this.isOverwrite = xDef.getIsOverwrite();
        this.isDeprecated = xDef.getIsDeprecated();
    }

    private AdsDomainDef(DomainDefinition xDef) {
        super(xDef);
        if (xDef != null) {
            load(xDef);
        }
    }

    public AdsDomainDef getOwnerDomain() {
        for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
            if (owner instanceof AdsDomainDef) {
                return (AdsDomainDef) owner;
            }
        }
        return null;
    }

    @Override
    public boolean canChangeAccessMode() {
        return true; //To change body of generated methods, choose Tools | Templates.
    }

    public void setDeprecated(boolean deprecated) {
        if (isDeprecated == deprecated) {
            return;
        }
        isDeprecated = deprecated;
        fireNameChange();
        setEditState(EEditState.MODIFIED);

    }

    @Override
    public boolean isDeprecated() {
        if (isOwnerDeprecated()) {
            return true;
        }
        return isDeprecated || super.isDeprecated();
    }

    public boolean isOwnerDeprecated() {
        AdsDomainDef owner = getOwnerDomain();
        return owner != null && owner.isDeprecated();
    }

    public Definitions<AdsDomainDef> getChildDomains() {
        return childDomains;
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        this.appendTo(xDefRoot.addNewAdsDomainDefinition(), saveMode);
    }

    protected void appendTo(DomainDefinition xDef, ESaveMode saveMode) {
        synchronized (this) {
            super.appendTo(xDef, saveMode);
            if (isDeprecated) {
                xDef.setIsDeprecated(true);
            }
            if (childDomains != null) {
                childDomains.appendTo(xDef, saveMode);
            }
        }
    }

    @Override
    public boolean isSaveable() {
        Definition topLevel = findTopLevelDef();
        return topLevel == null || topLevel == this ? true : false;
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        if (childDomains != null) {
            childDomains.visit(visitor, provider);
        }
    }

//    @Override
//    public String getName() {
//        AdsDomainDef ownerDomain = getOwnerDomain();
//        if (ownerDomain != null) {
//            return ownerDomain.getName() + "/" + super.getName();
//        } else {
//            return super.getName();
//        }
//    }
    @Override
    public ClipboardSupport<? extends org.radixware.kernel.common.defs.ads.AdsDefinition> getClipboardSupport() {
        return new AdsClipboardSupport<AdsDomainDef>(this) {
            @Override
            protected XmlObject copyToXml() {
                DomainDefinition xDef = DomainDefinition.Factory.newInstance();
                AdsDomainDef.this.appendTo(xDef, ESaveMode.NORMAL);
                return xDef;
            }

            @Override
            protected AdsDomainDef loadFrom(XmlObject xmlObject) {
                if (xmlObject instanceof DomainDefinition) {
                    return Factory.loadFrom((DomainDefinition) xmlObject);
                } else {
                    return super.loadFrom(xmlObject);
                }
            }

            @Override
            public void paste(List<Transfer> transfers, DuplicationResolver resolver) {
                AdsDomainDef.this.getChildDomains().getClipboardSupport().paste(transfers, resolver);
            }

            @Override
            protected Method getDecoderMethod() {
                try {
                    return Factory.class.getDeclaredMethod("loadFrom", DomainDefinition.class);
                } catch (NoSuchMethodException | SecurityException ex) {
                    Logger.getLogger(AdsDomainDef.class.getName()).log(Level.SEVERE, "Broken clipboard support", ex);
                }
                return null;
            }

            @Override
            public boolean isEncodedFormatSupported() {
                return true;
            }
        };
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.DOMAIN;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.DOMAIN;
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        return collection instanceof ChildDomains || collection instanceof ModuleDefinitions;
    }
}
