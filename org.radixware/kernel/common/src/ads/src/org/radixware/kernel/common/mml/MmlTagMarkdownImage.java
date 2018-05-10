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

import java.io.File;
import java.text.MessageFormat;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.schemas.xscml.MmlType;

public class MmlTagMarkdownImage extends MmlTagMarkdownResource {

    public MmlTagMarkdownImage(MmlType.Item.MarkdownImage item) {
        super(item);
    }

    public MmlTagMarkdownImage(AdsModule module, EIsoLanguage lang) {
        super(module, lang);
    }

    @Override
    public void appendTo(MmlType.Item item) {
        MmlType.Item.MarkdownImage image = item.addNewMarkdownImage();
        appendTo(image);
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag image={0}]", fileName);
    }

    @Override
    public String getDisplayName() {
        if (getOwnerMml() == null) {
            return "Unknown";
        }
        return "![" + title + "]" + "(" + rememberDisplayName(fileName) + ")";
    }

    public String getMarkdown(MarkdownGenerationContext context) {
        return "![" + title + "]("
                + context.getTechDocDir().toPath().relativize(context.getResourceDir().toPath()).toString() + File.separator
                + layerUri + File.separator
                + moduleName + File.separator
                + context.getLanguage().getValue() + File.separator
                + fileName + ")";
    }

}
