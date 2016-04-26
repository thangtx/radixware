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

package org.radixware.kernel.common.defs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.common.defs.localization.ILocalizingBundleDef;
import org.radixware.kernel.common.defs.localization.IMultilingualStringDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;

/**
 * <p>Base interface for all objects of DDS and ADS.</p>
 *
 */
public abstract class Definition extends RadixObject implements IDescribable, IDefinition, ILocalizedDescribable {

    public static final String DEFINITION_TYPE_TITLE = "Definition";
    public static final String DEFINITION_TYPES_TITLE = "Definitions";
    private Id id;
    private final Object pathLinkLock = new Object();

    private static final class PathLink {

        private final Id[] path;

        public PathLink(Id[] path) {
            this.path = path;
        }

        @Override
        public boolean equals(Object obj) {
            if (super.equals(obj)) {
                return true;
            } else {
                if (obj instanceof PathLink) {
                    PathLink other = (PathLink) obj;
                    if (path.length != other.path.length) {
                        return false;
                    }
                    for (int i = 0; i < path.length; i++) {
                        if (path[i] != other.path[i]) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    return false;
                }
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + java.util.Arrays.deepHashCode(this.path);
            return hash;
        }
    }
    private Map<PathLink, List<Definition>> resolvedPathes = null;
    private long changeTrackerToken = -1;

    private boolean pathCacheIsActual() {
        return changeTrackerToken == RadixObject.changeTrackingToken();
    }

    public final List<Definition> checkPathIsKnown(DefinitionPath path) {
        if (RadixObject.isChangeTrackingEnabled()) {
            synchronized (pathLinkLock) {
                resolvedPathes = null;
            }
            return null;
        }
        synchronized (pathLinkLock) {
            if (resolvedPathes == null) {
                return null;
            }
            if (pathCacheIsActual()) {
                PathLink pl = new PathLink(path.asArray());
                return resolvedPathes.get(pl);
            } else {
                resolvedPathes = null;
                return null;
            }
        }
    }

    public final List<Definition> checkPathIsKnown(Id[] path) {
        if (RadixObject.isChangeTrackingEnabled()) {
            synchronized (pathLinkLock) {
                resolvedPathes = null;
            }
            return null;
        }
        synchronized (pathLinkLock) {
            if (resolvedPathes == null) {
                return null;
            }
            if (pathCacheIsActual()) {
                PathLink pl = new PathLink(path);
                return resolvedPathes.get(pl);
            } else {
                resolvedPathes = null;
                return null;
            }
        }
    }

    public final void rememberPath(DefinitionPath path, List<Definition> ref) {
        if (RadixObject.isChangeTrackingEnabled()) {
            synchronized (pathLinkLock) {
                resolvedPathes = null;
            }
            return;
        }
        synchronized (pathLinkLock) {
            if (resolvedPathes == null || !pathCacheIsActual()) {
                resolvedPathes = new HashMap<>(3);
                changeTrackerToken = RadixObject.changeTrackingToken();
            }

            resolvedPathes.put(new PathLink(path.asArray()), ref);
        }
    }

    public final void rememberPath(Id[] path, List<Definition> ref) {
        if (RadixObject.isChangeTrackingEnabled()) {
            synchronized (pathLinkLock) {
                resolvedPathes = null;
            }
            return;
        }
        synchronized (pathLinkLock) {
            if (resolvedPathes == null || !pathCacheIsActual()) {
                resolvedPathes = new HashMap<>(3);
                changeTrackerToken = RadixObject.changeTrackingToken();
            }
            resolvedPathes.put(new PathLink(path), ref);
        }
    }

    protected Definition(Id id) {
        super();
        this.id = id;
    }

    protected Definition(Id id, String name) {
        super(name);
        this.id = id;
    }

    protected Definition(Id id, String name, String description) {
        super(name);
        this.id = id;
        this.description = (description != null ? description : "");
    }

    protected Definition(Definition src) {
        super();
        this.id = src.id;
    }

    protected Definition(org.radixware.schemas.commondef.Definition xDefinition) {
        this(xDefinition.getId());
    }

    /**
     * Get unique identifier for RadixWare object.
     */
    @Override
    public Id getId() {
        if (id == null) {
            throw new DefinitionError("Undefined Id", this);
        }
        return id;
    }

    /**
     * Set definition indentifier.
     *
     * @return
     */
    protected boolean setId(Id newId) {
        if (newId == null) {
            throw new DefinitionError("Atempt to set definition identifier to null.", this);
        }

        if (!Utils.equals(id, newId)) {
            if (getContainer() != null) {
                throw new DefinitionError("Atempt to change identifier of definition that have owner.", this);
            }
            this.id = newId;
            return true;
        }
        return false;
    }
    private String description = "";

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        if (description == null) {
            throw new DefinitionError("Definition description must not be null.", this);
        }
        if (!Utils.equals(this.description, description)) {
            this.description = description;
            this.setEditState(EEditState.MODIFIED);
        }
    }
    /**
     * Get dependence provider. Some definitions enlarges module dependencies by
     * own rules.
     */
    private final DefaultDependenceProvider depProvider = new DefaultDependenceProvider(this);

    public IDependenceProvider getDependenceProvider() {
        return depProvider;
    }

    @Override
    public String toString() {
        return super.toString() + "; #" + String.valueOf(id);
    }

    /**
     * Returns id sequence containing all container ids from top level
     * definition to current
     */
    public Id[] getIdPath() {
        final ArrayList<Id> path = new ArrayList<>(5);
        Definition def = this;
        while (def != null) {
            path.add(def.getId());
            def = def.getOwnerDefinition();
            if (def instanceof Module) {
                break;
            }
        }
        final Id[] pathArr = new Id[path.size()];
        for (int i = path.size() - 1, j = 0; i >= 0; i--, j++) {
            pathArr[j] = path.get(i);
        }
        return pathArr;
    }

    public boolean isPublished() {
        return true;
    }

    public boolean isFinal() {
        return false;
    }

    public boolean isDeprecated() {
        final Module module = getModule();
        return module != null ? module.isDeprecated() : false;
    }

    public IMultilingualStringDef findLocalizedString(Id stringId) {
        ILocalizingBundleDef bundle = findExistingLocalizingBundle();
        if (bundle != null) {
            return (IMultilingualStringDef) bundle.getStrings().findById(stringId, ExtendableDefinitions.EScope.ALL).get();
        }
        return null;
    }

    public ILocalizingBundleDef findExistingLocalizingBundle() {
        return null;
    }

    public ILocalizingBundleDef findLocalizingBundle() {
        return null;
    }

    @Override
    public Definition getDescriptionLocation() {
        return this;
    }

    @Override
    public Id getDescriptionId() {
        return null;
    }

    @Override
    public void setDescriptionId(Id id) {
    }

    @Override
    public String getDescription(EIsoLanguage language) {
        return null;
    }

    @Override
    public boolean setDescription(EIsoLanguage language, String description) {
        
        return false;
    }

    public String getLocalizedStringValue(EIsoLanguage language, Id stringId) {
        return null;
    }
}
