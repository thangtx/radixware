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

package org.radixware.kernel.explorer.editors.sqmleditor.sqmltags;

import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.utils.XmlObjectProcessor;
import org.radixware.kernel.explorer.editors.xscmleditor.TagInfo;
import org.radixware.schemas.xscml.Sqml;


public class SqmlTag_UnknownTag extends SqmlTag {

    private XmlObject xmlObj;
    private static final String PATH = "org.radixware.explorer/S_E/SYNTAX_SQML/SQML_UNKNOWN_TAG";

    public SqmlTag_UnknownTag(final IClientEnvironment environment,final long pos,final XmlObject xmlObj) {
        super(environment, pos);
        this.xmlObj = xmlObj;
        final XmlObject obj = XmlObjectProcessor.getXmlObjectFirstChild(xmlObj);
        if (obj != null && (obj.getClass() != null)) {
            setDisplayedInfo("???" + obj.getClass().getSimpleName() + "???");
        }
    }

    public SqmlTag_UnknownTag(final IClientEnvironment environment,final SqmlTag source) {
        super(environment, source);
    }

    private void setDisplayedInfo(final String name) {
        fullName = name;
        super.setDisplayedInfo("", name);
    }

    @Override
    public TagInfo copy() {
        final SqmlTag_UnknownTag res = new SqmlTag_UnknownTag(environment, this);
        res.xmlObj = this.xmlObj;
        return res;
    }

    @Override
    public void addTagToSqml(final XmlObject itemTag) {
        final Sqml.Item item = (Sqml.Item) itemTag;
        item.set(xmlObj);
    }

    @Override
    protected String getSettingsPath() {
        return PATH;
    }
}