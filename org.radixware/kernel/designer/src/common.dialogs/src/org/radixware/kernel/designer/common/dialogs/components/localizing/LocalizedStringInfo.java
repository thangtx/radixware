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


package org.radixware.kernel.designer.common.dialogs.components.localizing;

import java.sql.Timestamp;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class LocalizedStringInfo implements ILocalizedStringInfo {

    private final IMultilingualStringDef string;

    public LocalizedStringInfo(IMultilingualStringDef string) {
        this.string = string;
    }

    @Override
    public String getAuthor() {
        return string.getAuthor();
    }

    @Override
    public long getCreationDate() {
        return string.getCreationDate();
    }

    @Override
    public String getLastChangeAuthor(EIsoLanguage lang) {
        return string.getLastChangeAuthor(lang);
    }

    @Override
    public Timestamp getLastChangeTime(EIsoLanguage lang) {
        return string.getLastChangeTime(lang);
    }

    @Override
    public boolean isNeedsCheck(EIsoLanguage language) {
        return string.getNeedsCheck(language);
    }

    @Override
    public String asHtml(EIsoLanguage language) {
        final StringBuilder sb = new StringBuilder(128);

        sb.append("<html><body>")
                .append("<p><b>")
                .append(isNeedsCheck(language) ? "Unchecked" : "Checked")
                .append("</b></p>");

        final String author = getAuthor();
        if (author != null && !author.isEmpty()) {
            sb.append("<p>Created by: ")
                    .append(author)
                    .append("</p>");
        }

        if (getCreationDate() > 0) {
            sb.append("<p>Created: ")
                    .append(new Timestamp(getCreationDate()))
                    .append("</p>");
        }

        final String lastChangeAuthor = getLastChangeAuthor(language);
        if (lastChangeAuthor != null) {
            sb.append("<p>Changed by: ")
                    .append(lastChangeAuthor)
                    .append("</p>");
        }

        final Timestamp lastChangeTime = getLastChangeTime(language);
        if (lastChangeTime != null) {
            sb.append("<p>Changed: ")
                    .append(lastChangeTime)
                    .append("</p>")
                    .append("</body></html>");
        }

        return sb.toString();
    }
}
