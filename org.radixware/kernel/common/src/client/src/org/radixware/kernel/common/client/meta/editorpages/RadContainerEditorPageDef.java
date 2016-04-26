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

package org.radixware.kernel.common.client.meta.editorpages;

import java.util.List;
import org.radixware.kernel.common.types.Id;

/**
 * Страница редактора, содержащая дочерний элемент проводника
 * Может использоваться только в презентации редактора
 *
 */
public final class RadContainerEditorPageDef extends RadEditorPageDef {

    final Id explorerItemId;

    public RadContainerEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final Id explorerItemId) {
        this(id, name, titleOwnerId, titleId, iconId, explorerItemId, null);
    }
    
    private RadContainerEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final Id explorerItemId,
            final List<RadEditorPageDef> childPages) {
        super(id, name, titleOwnerId, titleId, iconId, childPages);
        this.explorerItemId = explorerItemId;
    }

    public Id getExplorerItemId() {
        return explorerItemId;
    }

    public boolean hasOwnTitle() {
        return titleId != null;
    }

    public boolean hasOwnIcon() {
        return iconId != null;
    }
    
    @Override
    RadContainerEditorPageDef createCopyWithSubPages(final List<RadEditorPageDef> childPages){
        return new RadContainerEditorPageDef(getId(),
                                             getName(),
                                             classId,
                                             titleId,
                                             iconId,
                                             explorerItemId,
                                             childPages);
    }
}
