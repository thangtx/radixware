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

package org.radixware.kernel.common.defs.ads.enumeration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;
import org.radixware.kernel.common.compiler.core.lookup.LookupUtils;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.IPlatformClassPublishingSupport;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.PlatformClassPublisherChangesListener;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher.PlatformClassPublishingChangedEvent;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.IKernelCharEnum;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.IKernelStrEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.events.RadixEventSource;
import org.radixware.schemas.adsdef.EnumDefinition;


public class AdsPlatformEnumDef extends AdsEnumDef {

    public AdsPlatformEnumDef(EnumDefinition xDef) {
        super(xDef);
        this.isExtendable = xDef.getPublishing().getIsExtendable();
        this.publishedName = xDef.getPublishing().getPlatformEnum();
    }
    private String publishedName;
    private boolean isExtendable;

    public AdsPlatformEnumDef(RadixPlatformEnum sourceEnum) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.ADS_ENUMERATION), sourceEnum.name);
        this.itemType = sourceEnum.itemType;
        this.publishedName = sourceEnum.name;
        syncPublishedItems(sourceEnum);
    }

    @Override
    public boolean syncPublishedItems() {
        return syncPublishedItems(findPublishedPlatformEnum());
    }

    @Override
    protected void appendTo(EnumDefinition xDef, boolean eraseExtInfo, ESaveMode saveMode) {
        super.appendTo(xDef, eraseExtInfo, saveMode);

        if (!eraseExtInfo) {
            xDef.addNewPublishing().setIsExtendable(isExtendable);
            xDef.getPublishing().setPlatformEnum(publishedName);
        }
    }

    @Override
    public boolean isExtendable() {
        return isExtendable;
    }

    @Override
    public boolean canChangeExtensibility() {
        return true;
    }

    @Override
    public boolean setExtendable(boolean extendable) {
        if (extendable) {
            this.isExtendable = true;
        } else {
            this.isExtendable = false;
            ArrayList<AdsEnumItemDef> toRemove = new ArrayList<>();
            for (AdsEnumItemDef item : getItems().get(EScope.LOCAL)) {
                if (item instanceof VirtualPlatformItem || item instanceof OverridePlatformItem) {
                    continue;
                }
                toRemove.add(item);
            }
            for (AdsEnumItemDef item : toRemove) {
                item.delete();
            }
        }
        setEditState(EEditState.MODIFIED);
        return true;
    }

    private boolean syncPublishedItems(RadixPlatformEnum platformEnum) {
        if (isReadOnly() || platformEnum == null) {
            return false;
        } else {
            final ArrayList<AdsEnumItemDef> toRemove = new ArrayList<>();

            HashMap<String, RadixPlatformEnum.Item> name2item = new HashMap<>();
            for (RadixPlatformEnum.Item item : platformEnum.getItems()) {
                name2item.put(item.name, item);
            }

            for (AdsEnumItemDef item : getItems().getLocal()) {
                if (item instanceof VirtualPlatformItem || item instanceof OverridePlatformItem) {
                    if (name2item.get(item.getPlatformItemName()) == null) {
                        toRemove.add(item);
                    }
                }
            }

            for (AdsEnumItemDef item : toRemove) {
                this.getItems().getLocal().remove(item);
                if (isExtendable() && item instanceof OverridePlatformItem) {
                    AdsEnumItemDef reg = AdsEnumItemDef.Factory.newInstance((OverridePlatformItem) item);
                    getItems().getLocal().add(reg);
                }
            }


            HashMap<String, AdsEnumItemDef> ownname2item = new HashMap<>();
            for (AdsEnumItemDef item : getItems().getLocal()) {
                if (item instanceof VirtualPlatformItem || item instanceof OverridePlatformItem) {
                    ownname2item.put(item.getPlatformItemName(), item);
                }
            }


            for (RadixPlatformEnum.Item item : platformEnum.getItems()) {
                AdsEnumItemDef itemDef = ownname2item.get(item.name);
                if (itemDef == null) {
                    getItems().getLocal().add(new VirtualPlatformItem(item));
                } else {
                    itemDef.updateValue(item.val);
                }
            }
            return true;
        }
    }

    @Override
    public RadixPlatformEnum findPublishedPlatformEnum() {
        return publishedName == null ? null : findPublishedPlatformEnum(publishedName);
    }

    private RadixPlatformEnum findPublishedPlatformEnum(String className) {
        return findPlatformEnum(className);
    }
    private RadixPlatformEnum platformEnum;

    public static RadixPlatformEnum findPlatformEnum(RadixObject context, String publishedName, EValType itemType) {

        if (context == null || context.getLayer() == null) {
            return null;
        }

        final byte[] bytes = LookupUtils.getClassFileBytes(context.getLayer(), publishedName, ERuntimeEnvironmentType.COMMON);

        if (bytes == null) {
            return null;
        }

        Class c = loadClass(bytes);

        if (itemType == null) {
            //try to determine item type by class
            Class[] ifaces = c.getInterfaces();
            if (ifaces != null) {
                for (final Class iface : ifaces) {
                    if (IKernelIntEnum.class.getName().equals(iface.getName())) {
                        itemType = EValType.INT;
                        break;
                    }
                    if (IKernelStrEnum.class.getName().equals(iface.getName())) {
                        itemType = EValType.STR;
                        break;
                    }
                    if (IKernelCharEnum.class.getName().equals(iface.getName())) {
                        itemType = EValType.CHAR;
                        break;
                    }
                }
            }
            if (itemType == null) {
                return null;
            }
        }

        RadixPlatformEnum platformEnum = new RadixPlatformEnum(publishedName, itemType);
        if (c != null) {

            try {
                @SuppressWarnings("unchecked")
                Method valueGetter = c.getMethod("getValue", (Class<?>[]) null);
                if (valueGetter != null) {
                    Field[] fields = c.getFields();
                    for (int i = 0; i < fields.length; i++) {
                        Field f = fields[i];
                        f.getModifiers();
                        if ((f.getModifiers() & ClassFileConstants.AccEnum) == 0) {
                            continue;
                        }
                        try {
                            String fieldName = String.valueOf(f.getName());
                            Field rf = c.getField(fieldName);
                            if (rf != null) {
                                Object val = valueGetter.invoke(rf.get(null), (Object[]) null);
                                platformEnum.addItem(fieldName, ValAsStr.Factory.newInstance(val, itemType));
                            }
                        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException | SecurityException ex) {
                            Logger.getLogger(AdsPlatformEnumDef.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(AdsPlatformEnumDef.class.getName()).log(Level.FINE, ex.getMessage(), ex);
            }
        }


        return platformEnum;
    }

    private RadixPlatformEnum findPlatformEnum(String publishedName) {
        if (platformEnum != null) {
            return platformEnum;
        }
        return platformEnum = findPlatformEnum(this, publishedName, itemType);
    }

    private static class CL extends ClassLoader {

        private byte[] classBytes;

        private CL(byte[] classBytes) {
            super(PlatformLib.class.getClassLoader());
            this.classBytes = classBytes;
        }

        public Class<?> get() {
            return defineClass(null, classBytes, 0, classBytes.length);
        }
    }

    protected static Class loadClass(byte[] classBytes) {
        CL cl = new CL(classBytes);

        try {
            return cl.get();
        } catch (Throwable ex) {
            return null;
        }
    }

    @Override
    public boolean isPlatformEnumPublisher() {
        return true;
    }

    @Override
    public String getPublishedPlatformEnumName() {
        return publishedName;
    }

    @Override
    public boolean setPublishedPlatformEnumName(String enumClassName) {
        synchronized (this) {
            if (isReadOnly()) {
                return false;
            }
            AdsEnumDef ovr = getHierarchy().findOverwritten().get();
            if (ovr != null) {
                return false;
            }

            RadixPlatformEnum e = findPublishedPlatformEnum(enumClassName);
            if (e == null) {
                return false;
            }
            publishedName = enumClassName;

            syncPublishedItems(e);
            setEditState(EEditState.MODIFIED);
            if (publishingSupport != null) {
                publishingSupport.fireChange();
            }
            return true;
        }
    }

    private class PublishingSupport implements IPlatformClassPublishingSupport {

        @Override
        public String getPlatformClassName() {
            return getPublishedPlatformEnumName();
        }
        private RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> changeSupport = null;

        @Override
        public RadixEventSource<PlatformClassPublisherChangesListener, PlatformClassPublishingChangedEvent> getPlatformClassPublishingChengesSupport() {
            synchronized (this) {
                if (changeSupport == null) {
                    changeSupport = new RadixEventSource<>();
                }
                return changeSupport;
            }
        }

        private void fireChange() {
            synchronized (this) {
                if (changeSupport != null) {
                    changeSupport.fireEvent(new PlatformClassPublishingChangedEvent(AdsPlatformEnumDef.this));
                }
            }
        }

        @Override
        public boolean isExtendablePublishing() {
            return AdsPlatformEnumDef.this.isExtendable();
        }

        @Override
        public boolean isPlatformClassPublisher() {
            return true;
        }
    }
    private PublishingSupport publishingSupport = null;

    @Override
    public IPlatformClassPublishingSupport getPlatformClassPublishingSupport() {
        synchronized (this) {
            if (publishingSupport == null) {
                publishingSupport = new PublishingSupport();
            }
            return publishingSupport;
        }
    }
}