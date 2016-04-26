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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.types.Id;

/**
 * Страница редактора, для которой задан кастомный диалог.
 *
 */
public final class RadCustomEditorPageDef extends RadEditorPageDef {

    private final Id customDialogId;
    private final List<Id> propertyIds;

    public RadCustomEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            //Идентификаторы вытащенных на эту страницу свойств
            final Id[] properties,
            final Id customDialogId) {
        this(id, name, titleOwnerId, titleId, iconId, arrToList(properties), customDialogId, null);
    }
    
    private RadCustomEditorPageDef(final Id id,
            final String name,
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            //Идентификаторы вытащенных на эту страницу свойств
            final List<Id> properties,
            final Id customDialogId,
            final List<RadEditorPageDef> childPages) {
        super(id, name, titleOwnerId, titleId, iconId, childPages);
        propertyIds = properties;
        this.customDialogId = customDialogId;        
    }
    
    private static List<Id> arrToList(final Id[] arr){
        final ArrayList<Id> result = new ArrayList<>();
        if (arr!=null){
            Collections.addAll(result, arr);
        }
        return result;
    }

    public Id getCustomDialogId() {
        return customDialogId;
    }

    public List<Id> getPropertyIds() {
        return Collections.unmodifiableList(propertyIds);
    }
    
    @Override
    RadCustomEditorPageDef createCopyWithSubPages(final List<RadEditorPageDef> childPages){
        return new RadCustomEditorPageDef(getId(),
                                          getName(),
                                          classId,
                                          titleId,
                                          iconId,
                                          propertyIds,
                                          customDialogId,
                                          childPages);
    }
}
