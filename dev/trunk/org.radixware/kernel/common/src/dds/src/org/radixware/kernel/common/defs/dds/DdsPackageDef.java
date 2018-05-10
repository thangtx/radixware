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
package org.radixware.kernel.common.defs.dds;

import org.radixware.kernel.common.defs.dds.radixdoc.DdsPackageRadixdocSupport;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EDocGroup;
import org.radixware.kernel.common.radixdoc.DocumentOptions;
import org.radixware.kernel.common.radixdoc.IRadixdocPage;
import org.radixware.kernel.common.radixdoc.IRadixdocProvider;
import org.radixware.kernel.common.radixdoc.RadixdocSupport;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.schemas.radixdoc.Page;

/**
 * Метаинформация о пакете в базе данных.
 *
 */
public class DdsPackageDef extends DdsPlSqlObjectDef implements IRadixdocProvider {

    protected DdsPackageDef(final String name) {
        super(EDefinitionIdPrefix.DDS_PACKAGE, name);
    }

    public DdsPackageDef(org.radixware.schemas.ddsdef.Package xPackage) {
        super(xPackage);
    }

    @Override
    public RadixdocSupport<? extends RadixObject> getRadixdocSupport() {
        return new RadixdocSupport(this) {
            @Override
            public IRadixdocPage document(Page page, DocumentOptions options) {
                return new DdsPackageRadixdocSupport((DdsPackageDef) getSource(), page, options);
            }
        };
    }

    @Override
    public boolean needsDocumentation() {
        return true;
    }

    @Override
    public boolean isRadixdocProvider() {
        return true;
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsPackageDef newInstance(final String name) {
            return new DdsPackageDef(name);
        }

        public static DdsPackageDef loadFrom(org.radixware.schemas.ddsdef.Package xPackage) {
            return new DdsPackageDef(xPackage);
        }
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.PACKAGE;
    }

    private class DdsPackageClipboardSupport extends DdsPlSqlObjectClipboardSupport<DdsPackageDef> {

        public DdsPackageClipboardSupport() {
            super(DdsPackageDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.Package xPackage = org.radixware.schemas.ddsdef.Package.Factory.newInstance();
            DdsModelWriter.writePackage(DdsPackageDef.this, xPackage);
            return xPackage;
        }

        @Override
        protected DdsPackageDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.Package xPackage = (org.radixware.schemas.ddsdef.Package) xmlObject;
            return DdsPackageDef.Factory.loadFrom(xPackage);
        }

        @Override
        protected Method getDecoderMethod() {
            try {
                return DdsPackageDef.Factory.class.getDeclaredMethod("loadFrom", org.radixware.schemas.ddsdef.Package.class);
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
            }
            return null;
        }
    }

    @Override
    public ClipboardSupport<? extends DdsPackageDef> getClipboardSupport() {
        return new DdsPackageClipboardSupport();
    }
    public static final String PACKAGE_TYPE_TITLE = "Package";
    public static final String PACKAGE_TYPES_TITLE = "Packages";

    @Override
    public String getTypeTitle() {
        return PACKAGE_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return PACKAGE_TYPES_TITLE;
    }

    @Override
    public EDocGroup getDocGroup() {
        return EDocGroup.PACKAGE;
    }
}
