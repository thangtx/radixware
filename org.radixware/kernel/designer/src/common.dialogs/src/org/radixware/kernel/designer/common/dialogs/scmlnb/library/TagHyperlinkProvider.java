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

package org.radixware.kernel.designer.common.dialogs.scmlnb.library;

import java.util.EnumSet;
import java.util.Set;
import javax.swing.text.Document;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkProviderExt;
import org.netbeans.lib.editor.hyperlink.spi.HyperlinkType;

/**
 * Provides tag's GoTo support
 */
public class TagHyperlinkProvider implements HyperlinkProviderExt {

    private int startOffset;
    private int endOffset;

    public TagHyperlinkProvider() {
    }

    @Override
    public boolean isHyperlinkPoint(Document doc, int offset, HyperlinkType type) {
        TagMapper tm = TagMapper.getInstance(doc);
        if (tm == null) {
            //System.out.println("Cant find TagMapper for given document");
            return false;
        }
        TagBounds tagBounds = tm.findContainingBounds(offset);
        if (tagBounds == null || !tagBounds.getVTag().hasGoToTargets()) {
            return false;
        }
        startOffset = tagBounds.getBeginOffset();
        endOffset = tagBounds.getEndOffset();
        return true;
    }

    @Override
    public int[] getHyperlinkSpan(Document doc, int offset, HyperlinkType type) {
        return new int[]{startOffset, endOffset};
    }

    @Override
    public void performClickAction(Document doc, int offset, HyperlinkType type) {
        TagMapper tm = TagMapper.getInstance(doc);
        tm.findContainingBounds(offset).getVTag().goToObject();
    }

    @Override
    public Set<HyperlinkType> getSupportedHyperlinkTypes() {
        return EnumSet.of(HyperlinkType.GO_TO_DECLARATION);
    }

    @Override
    public String getTooltipText(Document doc, int offset, HyperlinkType type) {
        TagMapper tm = TagMapper.getInstance(doc);
        return tm.findContainingBounds(offset).getVTag().getGoToTooltip();
    }
}
