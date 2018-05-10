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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;

import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.server.arte.Arte;
import org.radixware.kernel.server.arte.Release;
import org.radixware.kernel.common.types.MultilingualString;
import org.radixware.kernel.server.types.Restrictions;

public class RadParagraphExplorerItemDef extends RadExplorerItemDef {

    private final List<RadExplorerItemDef> children;
    private final Id titleId;
    private final String layerUri;
    private final boolean isHidden;
    private final Map<Id, RadExplorerItemDef> eiById = new HashMap<>();

    private final RadParagraphExplorerItemDef base;

    public RadParagraphExplorerItemDef(
            final Arte arte,
            final Id id,
            final Id ownerDefId,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base) {
        this(arte, id, ownerDefId, titleId, layerUri, children, isHidden, base, null);
    }

    public RadParagraphExplorerItemDef(
            final Arte arte,
            final Id id,
            final Id ownerDefId,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base,
            final Id[] explicitelyInheritedChildren) {
        this(arte == null ? null : arte.getDefManager().getReleaseCache().getRelease(), id, ownerDefId, titleId, layerUri, children, isHidden, base, explicitelyInheritedChildren);
    }

    public RadParagraphExplorerItemDef(
            final Release release,
            final Id id,
            final Id ownerDefId,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base) {
        this(release, id, ownerDefId, titleId, layerUri, children, isHidden, base, null);
    }

    public RadParagraphExplorerItemDef(
            final Release release,
            final Id id,
            final Id ownerDefId,
            final Id titleId,
            final String layerUri,
            final RadExplorerItemDef[] children,
            final boolean isHidden,
            final RadParagraphExplorerItemDef base,
            final Id[] explicitelyInheritedChildren) {
        super(id, ownerDefId);
        this.titleId = titleId;
        final List<RadExplorerItemDef> childrensList;
        if (children != null) {
            if (base == null || base.children.isEmpty()) {
                childrensList = Collections.unmodifiableList(Arrays.asList(children));
            } else {
                final List<RadExplorerItemDef> allChildren = new LinkedList<>(base.children);
                allChildren.addAll(Arrays.asList(children));
                childrensList = Collections.unmodifiableList(allChildren);
            }
        } else {
            if (base == null) {
                childrensList = Collections.emptyList();
            } else {
                childrensList = base.children;
            }
        }
        this.base = base;

        if (explicitelyInheritedChildren != null && explicitelyInheritedChildren.length > 0 && base != null) {
            this.children = RadExplorerItemDef.addInherited(childrensList, base, explicitelyInheritedChildren);
        } else {
            this.children = childrensList;
        }

        this.layerUri = layerUri;
        this.isHidden = isHidden;

        link(release);
    }

    public boolean isHidden() {
        return isHidden;
    }

    protected RadParagraphExplorerItemDef getBase() {
        return base;
    }

    public String getLayerUri() {
        return layerUri;
    }

    public String getTitle(final Arte arte) {
        return MultilingualString.get(arte, getOwnerDefId(), titleId);
    }

    @Override
    protected final void link(final Release release) {
        super.link(release);
        for (RadExplorerItemDef c : children) {
            c.link(release);
        }
    }

    public final RadExplorerItemDef findChildExplorerItemById(final Id eiId) {
        //XXX: remove synchronization
        synchronized (eiById) {
            RadExplorerItemDef ei = eiById.get(eiId);
            if (ei != null) // already indexed
            {
                return ei;
            }
            // search
            ei = RadExplorerItemDef.findEiById(getChildren(), eiId, new ArrayList<RadExplorerItemDef>());
            if (ei == null) {
                throw new DefinitionNotFoundError(eiId);
            }
            eiById.put(eiId, ei);
            return ei;
        }
    }

    public List<RadExplorerItemDef> getChildren() {
        return children;
    }

    public final RadEditorPresentationDef getDefaultEditorPresentation() {
        return null;
    }

    public final RadSelectorPresentationDef getDefaultSelectorPresentation() {
        return null;
    }

    @Override
    @Deprecated
    public boolean isCommandEnabled(final Id cmdId) {
        return false;
    }
    
    @Override    
    public boolean isCommandEnabled(final Id cmdId, final boolean isReadOnly) {
        return false;
    }

    @Override
    public Restrictions getRestrictions() {
        return Restrictions.ZERO;
    }

    @Override
    public DdsReferenceDef getContextReference() {
        return null;
    }
}
