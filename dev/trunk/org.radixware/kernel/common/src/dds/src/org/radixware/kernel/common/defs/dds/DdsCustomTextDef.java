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

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * Метаинформация о произвольном SQML выражении в коде {@link DdsPlSqlObjectDef PL/SQL объекта}.
 */
public class DdsCustomTextDef extends DdsPlSqlObjectItemDef {

    private final Sqml sqml = new DdsSqml(this);

    /**
     * Получить SQML выражение.
     * Транслируется и подставляется во время генерации скрипта PL/SQL объекта.
     */
    public Sqml getText() {
        return sqml;
    }

    protected DdsCustomTextDef(final String name) {
        super(EDefinitionIdPrefix.DDS_CUSTOM_TEXT, name);
    }

    public DdsCustomTextDef(org.radixware.schemas.ddsdef.PlSqlCustomText xCustomText) {
        super(xCustomText);
        this.sqml.loadFrom(xCustomText.getText());
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsCustomTextDef newInstance(final String name) {
            return new DdsCustomTextDef(name);
        }

        public static DdsCustomTextDef loadFrom(org.radixware.schemas.ddsdef.PlSqlCustomText xCustomText) {
            return new DdsCustomTextDef(xCustomText);
        }
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        sqml.visit(visitor, provider);
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.SQML;
    }

    private class DdsCustomTextClipboardSupport extends DdsClipboardSupport<DdsCustomTextDef> {

        public DdsCustomTextClipboardSupport() {
            super(DdsCustomTextDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.ddsdef.PlSqlCustomText xCustomText = org.radixware.schemas.ddsdef.PlSqlCustomText.Factory.newInstance();
            DdsModelWriter.writeCustomText(DdsCustomTextDef.this, xCustomText);
            return xCustomText;
        }

        @Override
        protected DdsCustomTextDef loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.ddsdef.PlSqlCustomText xCustomText = (org.radixware.schemas.ddsdef.PlSqlCustomText) xmlObject;
            return DdsCustomTextDef.Factory.loadFrom(xCustomText);
        }
    }

    @Override
    public ClipboardSupport<? extends DdsCustomTextDef> getClipboardSupport() {
        return new DdsCustomTextClipboardSupport();
    }
    public static final String CUSTOM_TEXT_TYPE_TITLE = "Custom Text";
    public static final String CUSTOM_TEXT_TYPES_TITLE = "Custom Texts";

    @Override
    public String getTypeTitle() {
        return CUSTOM_TEXT_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return CUSTOM_TEXT_TYPES_TITLE;
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.FREE;
    }
}
