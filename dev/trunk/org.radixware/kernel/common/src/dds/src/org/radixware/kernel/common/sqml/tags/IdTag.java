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

package org.radixware.kernel.common.sqml.tags;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionLink;
import org.radixware.kernel.common.defs.dds.DdsPath;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;

/**
 * Тэг - идентификатор RadixWare объекта. Транслируется в идентификатор
 * RadixWare объекта, заключенный в строковые кавычки.
 */
public class IdTag extends Sqml.Tag {

    protected IdTag(Id[] ids) {
        super();
        this.ids = ids;
    }

    protected IdTag(Id[] ids, boolean isSoftRef) {
        this(ids);
        this.isSoftReference = isSoftRef;
    }
    private final Id[] ids;
    private boolean isSoftReference = false;

    /**
     * Получить путь до дефиниции.
     */
    public Id[] getPath() {
        return ids;
    }

    public boolean isSoftReference() {
        return isSoftReference;
    }

    public void setSoftReference(boolean isSoftReference) {
        if (this.isSoftReference != isSoftReference) {
            this.isSoftReference = isSoftReference;
            setEditState(EEditState.MODIFIED);
        }
    }

    public static final class Factory {

        private Factory() {
        }

        public static IdTag newInstance(final Id[] ids) {
            return new IdTag(ids);
        }

        public static IdTag newInstance(final Id[] ids, boolean isSoftRef) {
            return new IdTag(ids, isSoftRef);
        }
    }

    private class Link extends DefinitionLink<Definition> {

        @Override
        protected Definition search() {
            final ISqmlEnvironment environment = getEnvironment();
            if (environment != null) {
                return environment.findDefinitionByIds(ids, isSoftReference);
            } else {
                return null;
            }
        }
    }
    private final Link link = new Link();

    /**
     * Find target definition.
     *
     * @return definition or null if not found.
     */
    public Definition findTarget() {
        return link.find();
    }

    /**
     * Find target definition.
     *
     * @return definition.
     * @throws DefinitionNotFoundError
     */
    public Definition getTarget() {
        final Definition definition = findTarget();
        if (definition != null) {
            return definition;
        } else {
            throw new DefinitionNotFoundError(ids.length > 0 ? ids[ids.length - 1] : null);
        }
    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);

        Definition target = findTarget();
        if (target != null) {
            list.add(target);
        }
    }

    @Override
    public void collectDirectDependences(List<Definition> list) {
        if (!isSoftReference) {
            collectDependences(list);
        }
    }
    public static final String ID_TAG_TYPE_TITLE = "Definition Identifier Tag";
    public static final String ID_TAG_TYPES_TITLE = "Definition Identifier Tags";

    @Override
    public String getTypeTitle() {
        return ID_TAG_TYPE_TITLE;
    }

    @Override
    public String getTypesTitle() {
        return ID_TAG_TYPES_TITLE;
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        super.appendAdditionalToolTip(sb);
        final DdsPath ddsPath = new DdsPath(ids);
        sb.append("<br>Path: " + String.valueOf(ddsPath));
        if (isSoftReference) {
            sb.append("<br>Global reference (no dependecies required for resolution)");
        }
    }
}
