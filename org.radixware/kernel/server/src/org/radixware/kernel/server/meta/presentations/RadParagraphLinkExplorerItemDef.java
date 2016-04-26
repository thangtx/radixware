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

package org.radixware.kernel.server.meta.presentations;

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Release;

public final class RadParagraphLinkExplorerItemDef extends RadParagraphExplorerItemDef {

    private final Id paragraphId;
    private final int inheritanceMask;
    private RadParagraphExplorerItemDef paragraph = null;

    public RadParagraphLinkExplorerItemDef(
            final Id id,
            final Id ownerDefId,
            final Id titleId,
            final Id paragraphId,
            final int inheritanceMask) {
        super((Release) null, id, ownerDefId, titleId, null, null, false, null); // arte parameter is null but the definition will be linked by owner ExplorerItem
        this.paragraphId = paragraphId;
        this.inheritanceMask = inheritanceMask;
    }

    @Override
    public void link() {
        super.link();
        getParagraph();
    }

    private RadParagraphExplorerItemDef getParagraph() {
        if (paragraph == null) {
            paragraph = getRelease().getExplorerParagraphDef(paragraphId);
        }
        return paragraph;
    }

    @Override
    public final List<RadExplorerItemDef> getChildren() {
        try {
            return getParagraph().getChildren();
        } catch (DefinitionNotFoundError e) {
            return Collections.emptyList();//RADIX-4918
        }
    }

    @Override
    public String getTitle(final Arte arte) {
        if ((inheritanceMask & CInheritance.TITLES) == 0) // не наследуется 
        {
            return super.getTitle(arte);
        }
        return getParagraph().getTitle(arte);
    }

    static private final class CInheritance {

        static public final int TITLES = 32;
    }
}
