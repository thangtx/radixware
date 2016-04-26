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
package org.radixware.kernel.common.defs.ads.xslt;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.defs.ads.xml.IPureXmlDefinition;
import org.radixware.kernel.common.enums.EDefType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.schemas.adsdef.AdsDefinitionElementType;
import org.radixware.schemas.adsdef.XsltDefinition;
import org.radixware.schemas.commondef.DescribedDefinition;

public class AdsXsltDef extends AdsDefinition implements IPureXmlDefinition {

    protected AdsXsltDef(String name) {
        super(Id.Factory.newInstance(EDefinitionIdPrefix.XSLT_TRANSFORM), name);
        content = getDefaultTransformFileContent();
    }

    protected AdsXsltDef(Id id) {
        super(id);
        content = getDefaultTransformFileContent();
    }

    protected AdsXsltDef(Id id, String name) {
        super(id, name);
        content = getDefaultTransformFileContent();
    }

    @Override
    public EDefType getDefinitionType() {
        return EDefType.XSLT;
    }

    protected AdsXsltDef(org.radixware.schemas.commondef.Definition xDef) {
        super(xDef.getId());
        this.isOverwrite = xDef.getIsOverwrite();
        content = getDefaultTransformFileContent();
    }

    protected AdsXsltDef(DescribedDefinition xDef) {
        super(xDef.getId(), xDef.getName());
        this.isOverwrite = xDef.getIsOverwrite();
        content = getDefaultTransformFileContent();
    }

    protected AdsXsltDef(XsltDefinition xDef) {
        super(xDef.getId(), xDef.getName());
        this.isOverwrite = xDef.getIsOverwrite();
        content = xDef.getXslt();
    }

    @Override
    public void appendTo(AdsDefinitionElementType xDefRoot, ESaveMode saveMode) {
        XsltDefinition def = xDefRoot.addNewAdsXsltSchemeDefinition();
        super.appendTo(def, saveMode);
        if (saveMode != ESaveMode.API) {
            def.setXslt(content);
        }
    }

    @Override
    public String getXmlText() {
        return content;
    }

    public static final class Factory {

        public static AdsXsltDef loadFrom(XsltDefinition xDef) {
            return new AdsXsltDef(xDef);
        }

        public static AdsXsltDef newInstance() {
            return new AdsXsltDef("NewXSLTTransformation");
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private class AdsXsltDefClipboardSupport extends ClipboardSupport<AdsXsltDef> {

        public AdsXsltDefClipboardSupport() {
            super(AdsXsltDef.this);
        }

        @Override
        protected XmlObject copyToXml() {
            XsltDefinition xslt = XsltDefinition.Factory.newInstance();
            xslt.setXslt(content);
            xslt.setId(Id.Factory.newInstance(EDefinitionIdPrefix.XSLT_TRANSFORM));
            xslt.setName(AdsXsltDef.this.getName());
            return xslt;
        }

        @Override
        protected AdsXsltDef loadFrom(XmlObject xmlObject) {
            XsltDefinition xslt = (XsltDefinition) xmlObject;
            return AdsXsltDef.Factory.loadFrom(xslt);
        }
    }

    @Override
    public ClipboardSupport<? extends AdsXsltDef> getClipboardSupport() {
        return new AdsXsltDefClipboardSupport();
    }

    @Override
    public RadixIcon getIcon() {
        return AdsDefinitionIcon.XML_TRANSFORMATION;
    }

    private String getDefaultTransformFileContent() {
        try {
            InputStream is = getClass().getResourceAsStream("initxslt.xml");
            return FileUtils.readTextStream(is, FileUtils.XML_ENCODING);
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
        }
        return null;
    }
}
