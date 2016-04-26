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

import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.meta.TitledDefinition;
import org.radixware.kernel.common.client.models.ModelWithPages;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.types.Id;

/**
 * Базовый класс страницы редактирования.
 *
 */
public abstract class RadEditorPageDef extends TitledDefinition {

    protected final Id iconId;
    protected final Id titleId;
    protected final Id classId;
    private final List<RadEditorPageDef> childPages;
    public final boolean isVisible;

    public RadEditorPageDef(final Id id,
            final String name,
            //идентификатор класса для презентации редактора или идентификатор формы:
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId) {
        this(id,name,titleOwnerId,titleId,iconId,null);
    }
    
    public RadEditorPageDef(final Id id,
            final String name,
            //идентификатор класса для презентации редактора или идентификатор формы:
            final Id titleOwnerId,
            final Id titleId,
            final Id iconId,
            final List<RadEditorPageDef> childPages) {
        super(id, name, titleOwnerId, titleId);
        this.iconId = iconId;
        this.classId = titleOwnerId;
        this.titleId = titleId;
        this.isVisible = true;
        this.childPages = childPages;
    }

    public Icon getIcon() {
        if (iconId == null) {
            return null;
        }
        return getIcon(iconId);
    }

    public final EditorPageModelItem newModelItem(ModelWithPages owner) {
        return new EditorPageModelItem(owner, this);
    }
    
    public final List<RadEditorPageDef> getSubPages(){
        if (childPages==null){
            return Collections.emptyList();
        }else{
            return Collections.unmodifiableList(childPages);
        }
    }
    
    public final boolean hasSubPages(){
        return childPages!=null && !childPages.isEmpty();
    }

    @Override
    public String getDescription() {
        final String desc = getApplication().getMessageProvider().translate("DefinitionDescribtion", "editor page %s, owner definition is #%s");
        return String.format(desc, super.getDescription(), classId);
    }
    
    abstract RadEditorPageDef createCopyWithSubPages(final List<RadEditorPageDef> childPages);
}
