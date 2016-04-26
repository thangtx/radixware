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

package org.radixware.kernel.designer.common.editors.jml.vtag;

import org.apache.commons.lang.StringEscapeUtils;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.jml.JmlTagLocalizedString;


public class VJmlTagLocalizedString<T extends JmlTagLocalizedString> extends VJmlTag<T> {

    @Override
    public String getTokenName() {
        return "tag-localizedstring";
    }

    @Override
    public String getToolTip() {
        AdsMultilingualStringDef strDef = getTag().findLocalizedString(getTag().getStringId());
        if (strDef == null) {
            return super.getToolTip();
        } else {
            return strDef.getToolTip();
        }
    }

    private String escapeHtml(String str) {
        return StringEscapeUtils.escapeHtml(str);
    }

    public VJmlTagLocalizedString(T tag) {
        super(tag);
    }
}
