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

package org.radixware.kernel.common.defs.ads.clazz;

import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition.ESaveMode;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.AdsDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.NestedClasses;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportClassDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.ModuleDefinitions;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.radixdoc.ClassRadixdoc;
import org.radixware.kernel.common.defs.ads.radixdoc.DynamicClassRadixdoc;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration.TypeArgument;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.AccessRules.Transparence;
import org.radixware.schemas.adsdef.ClassDefinition;
import org.radixware.schemas.radixdoc.ContentContainer;
import org.radixware.schemas.radixdoc.Page;
import org.radixware.schemas.radixdoc.Table;


public class AdsDynamicClassDef extends AdsClassDef implements IPlatformClassPublisher {

    private class ClassTransparence extends AdsTransparence {

        AdsTransparence src;

        public ClassTransparence(AdsClassDef context, Transparence xDef) {
            super(context, xDef);
        }

        public ClassTransparence(AdsClassDef context, ClassTransparence src) {
            super(context, src.getPublishedName(), src.isExtendable());
        }

        public ClassTransparence(AdsClassDef context) {
            super(context, null, false);
        }

        @Override
        public void setPublishedName(String publishedName) {
            Id predefinedId = null;
            if (AdsEntityClassDef.PLATFORM_CLASS_NAME.equals(publishedName)) {
                predefinedId = AdsEntityClassDef.PREDEFINED_ID;
            } else if (AdsEntityGroupClassDef.PLATFORM_CLASS_NAME.equals(publishedName)) {
                predefinedId = AdsEntityGroupClassDef.PREDEFINED_ID;
            } else if (AdsReportClassDef.PLATFORM_CLASS_NAME.equals(publishedName)) {
                predefinedId = AdsReportClassDef.PREDEFINED_ID;
            } else if (AdsCursorClassDef.PLATFORM_CLASS_NAME.equals(publishedName)) {
                predefinedId = AdsCursorClassDef.PREDEFINED_ID;
            } else if (AdsAlgoClassDef.PLATFORM_CLASS_NAME.equals(publishedName)) {
                predefinedId = AdsAlgoClassDef.PREDEFINED_ID;
            } else if (AdsStatementClassDef.PLATFORM_CLASS_NAME.equals(publishedName)) {
                predefinedId = AdsStatementClassDef.PREDEFINED_ID;
            }
            if (predefinedId != null) {
                if (getOwnerClass().setNewId(predefinedId)) {
                    super.setPublishedName(publishedName);
                }
            } else {
                super.setPublishedName(publishedName);
            }
        }

        AdsDynamicClassDef getOwnerClass() {
            return (AdsDynamicClassDef) getContainer();
        }
    }

    public static class EnvironmentChangedEvent extends RadixEvent {

        public final AdsDynamicClassDef source;

        private EnvironmentChangedEvent(AdsDynamicClassDef source) {
            this.source = source;
        }
    }

    public interface EnvironmentListener extends IRadixEventListener<EnvironmentChangedEvent> {
    }

    public static class EnvironmentChangeSupport extends RadixEventSource<EnvironmentListener, EnvironmentChangedEvent> {
    }

    public static class Factory {

        public static AdsDynamicClassDef loadFrom(ClassDefinition classDef) {
            return new AdsDynamicClassDef(classDef);
        }

        public static AdsDynamicClassDef newInstance(ERuntimeEnvironmentType env) {
            return new AdsDynamicClassDef("NewDynamicClass", env);
        }
    }
    protected ERuntimeEnvironmentType environment;
    private boolean isDual = false;
    private EnvironmentChangeSupport envChangeSupport = null;
    private final Object defLock = new Object();

    protected AdsDynamicClassDef(ClassDefinition xDef) {
        this(xDef.getId(), xDef);
    }

    protected AdsDynamicClassDef(Id id, ClassDefinition xDef) {
        super(id, xDef);
        this.transparence = new ClassTransparence(this, xDef.isSetAccessRules() ? xDef.getAccessRules().getTransparence() : null);

        // assert xDef.getEnvironment() != null;
        this.environment = xDef.getEnvironment() == null ? ERuntimeEnvironmentType.COMMON : xDef.getEnvironment();
        if (xDef.isSetIsDual()) {
            isDual = xDef.getIsDual();
        }
    }

    protected AdsDynamicClassDef(String name, ERuntimeEnvironmentType env) {
        this(EDefinitionIdPrefix.ADS_DYNAMIC_CLASS, name, env);
    }

    /**
     * Override creation constructor
     */
    protected AdsDynamicClassDef(AdsDynamicClassDef source) {
        super(source);
        this.transparence = new ClassTransparence(this, source.transparence);
        this.environment = source.environment;
        this.isDual = source.isDual;
    }

    protected AdsDynamicClassDef(Id id, String name, ERuntimeEnvironmentType env) {
        super(id, name);
        this.transparence = new ClassTransparence(this);
        assert env != null;
        this.environment = env;
    }

    protected AdsDynamicClassDef(EDefinitionIdPrefix prefix, String name, ERuntimeEnvironmentType env) {
        this(Id.Factory.newInstance(prefix), name, env);
    }

    @Override
    public boolean isDual() {
        return environment == ERuntimeEnvironmentType.COMMON_CLIENT ? isDual : false;
    }

    @Override
    public EClassType getClassDefType() {
        return EClassType.DYNAMIC;
    }
    private final ClassTransparence transparence;

    @Override
    public AdsTransparence getTransparence() {
        return transparence;
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.CLASS_DYNAMIC;
    }
    private PlatformClassPublishingSupport publishingSupport = null;

    @Override
    public IPlatformClassPublishingSupport getPlatformClassPublishingSupport() {
        synchronized (this) {
            if (publishingSupport == null) {
                publishingSupport = new PlatformClassPublishingSupport(this);
            }
            return publishingSupport;
        }
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        if (isNested() && !getOwnerClass().getUsageEnvironment().isClientEnv()) {
            return super.getUsageEnvironment();
        }
        return environment;
    }

    @Override
    public boolean setUsageEnvironment(ERuntimeEnvironmentType env) {
        if (env == this.environment) {
            return false;
        }

        this.environment = env;
        setEditState(EEditState.MODIFIED);
        synchronized (this) {
            if (this.envChangeSupport != null) {
                this.envChangeSupport.fireEvent(new EnvironmentChangedEvent(this));
            }
        }
        return true;
    }

    @Override
    public Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return EnumSet.of(environment);
    }

    @Override
    public boolean isSaveable() {
        return !isNested();
    }

    @Override
    public String getTypeTitle() {
        return environment.getName() + " Dynamic Class";
    }

    @Override
    public void appendTo(ClassDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        xDef.setEnvironment(environment);
        if (isDual) {
            xDef.setIsDual(isDual);
        }
    }

    @Override
    protected void insertToolTipPrefix(StringBuilder sb) {
        super.insertToolTipPrefix(sb);
        String access = getDefaultAccess().getName().toUpperCase().charAt(0) + getDefaultAccess().getName().substring(1, getDefaultAccess().getName().length());
        sb.append("<b>").append(access).append("</b>&nbsp;");
    }

    @Override
    public boolean isTitleSupported() {
        AdsTransparence t = getTransparence();
        if (t != null && t.isTransparent() && !t.isExtendable()) {
            return false;
        } else {
            return super.isTitleSupported();
        }
    }

    @Override
    public AdsClassDef getOwnerClass() {
        return RadixObjectsUtils.findContainer(getContainer(), AdsClassDef.class);
    }

    @Override
    public boolean isSuitableContainer(AdsDefinitions collection) {
        if (collection instanceof NestedClasses.LocalClasses) {
            final AdsClassDef ownClass = RadixObjectsUtils.findContainer(collection, AdsClassDef.class);

            if (ownClass != null && ownClass.isAnonymous()) {
                return false;
            }

            RadixObject container = ownClass;
            while (container != null && !(container instanceof AdsModule)) {
                if (container == this) {
                    return false;
                }
                container = container.getContainer();
            }
            return true;
        }
        return collection instanceof ModuleDefinitions;
    }

    @Override
    public boolean isNested() {
        return getOwnerClass() != null;
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return isNested() ? getName() : super.getQualifiedName(context);
    }

    public void setDual(boolean isDual) {
        if (this.isDual != isDual) {
            this.isDual = isDual;
            setEditState(EEditState.MODIFIED);
        }
    }

    public EnvironmentChangeSupport getEnvironmentChangeSupport() {
        synchronized (defLock) {
            if (envChangeSupport == null) {
                envChangeSupport = new EnvironmentChangeSupport();
            }
            return envChangeSupport;
        }
    }

    private boolean setNewId(Id newId) {
        AdsModule module = null;
        if (getModule() != null) {
            if (getModule().getDefinitions().findById(newId) == null) {
                module = getModule();
                delete();
            } else {
                return false;
            }
        }
        setId(newId);
        if (module != null) {
            module.getDefinitions().add(this);
        }
        return true;
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport<AdsClassDef>(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DynamicClassRadixdoc(getSource(), page, options);
            }
        };
    }
}