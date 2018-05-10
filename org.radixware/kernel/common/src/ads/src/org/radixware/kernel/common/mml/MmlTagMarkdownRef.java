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
package org.radixware.kernel.common.mml;

import java.text.MessageFormat;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.schemas.xscml.MmlType;

public class MmlTagMarkdownRef extends MmlTagMarkdownResource {

    public MmlTagMarkdownRef(MmlType.Item.MarkdownRef markdownRef) {
        super(markdownRef);
    }

    public MmlTagMarkdownRef(AdsModule module, EIsoLanguage lang) {
        super(module, lang);
    }

    @Override
    public void appendTo(MmlType.Item item) {
        MmlType.Item.MarkdownRef ref = item.addNewMarkdownRef();
        appendTo(ref);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag ref={0}]", fileName);
    }
}
