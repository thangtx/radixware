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

import java.awt.datatransfer.Transferable;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.ClipboardSupport.DuplicationResolver.Resolution;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.types.Id;

/**
 * Generic container for subdefinitions with type T. Supports subset of list
 * operations on definitions: <ul> <li>add (with ownership check)</li>
 * <li>remove (by index or by pointer)</li> <li>clear</li> <li>quick search by
 * identifier</li> </ul> Also this container provides notification support with
 * listeners mechanism and Vistor support
 *
 */
public class Definitions<T extends Definition> extends RadixObjects<T> {

    private Map<Id, T> id2definition = null; // definition hash by identifier.
    private final Object id2definitionLock = new Object();

    protected Definitions(RadixObject container) {
        super(container);
    }

    protected Definitions() { // for extendable definitions
        super();
    }

    @Override
    protected void onAdd(T definition) {
        synchronized (id2definitionLock) {
            if (id2definition != null) {
                id2definition.put(definition.getId(), definition);
            }
        }
    }

    @Override
    protected void onRemove(T definition) {
        synchronized (id2definitionLock) {
            if (id2definition != null) {
                id2definition.remove(definition.getId());
            }
        }
    }

    @Override
    protected void onClear() {
        synchronized (id2definitionLock) {
            if (id2definition != null) {
                id2definition = null;
            }
        }
    }

    /**
     * Returns definition for given id (if any) or null
     */
    public T findById(Id id) {
        if (id == null || isEmpty()) {
            return null;
        }

        synchronized (id2definitionLock) {
            if (id2definition == null) {
                id2definition = new HashMap<>(size());// * 2 + 11);
                for (T def : this) {
                    id2definition.put(def.getId(), def);
                }
            }
            return id2definition.get(id);
        }
    }

    public final boolean containsId(Id id) {
        if (id == null || isEmpty()) {
            return false;
        }

        synchronized (id2definitionLock) {
            if (id2definition == null) {
                id2definition = new HashMap<>(size());// * 2 + 11);
                for (T def : this) {
                    id2definition.put(def.getId(), def);
                }
            }
            return id2definition.get(id) != null;
        }
    }

    /**
     * Returns definition for given id (if any)
     *
     * @throws DefinitionNotFoundError
     */
    public T getById(Id id) {
        T res = findById(id);
        if (res == null) {
            throw new DefinitionNotFoundError(id);
        }
        return res;
    }

    public void unregister(T definition) {
        if (contains(definition)) {
            if (id2definition != null) {
                id2definition.remove(definition.getId());
            }
        }
    }

    public void register(T definition) {
        if (contains(definition)) {
            if (id2definition != null) {
                id2definition.put(definition.getId(), definition);
            }
        }
    }

    @Override
    public <R extends T> R overwrite(R sourceDef) {
        synchronized (this) {
            if (sourceDef == null) {
                throw new DefinitionError("Source for overwrite must not be null.", this);
            }

            if (!(sourceDef instanceof IOverwritable)) {
                throw new DefinitionError("Attempt to overwrite definition that not IOverwritable.", sourceDef);
            }
            IOverwritable source = (IOverwritable) sourceDef;

            if (!source.allowOverwrite()) {
                throw new DefinitionError("Overwrite is not allowed", sourceDef);
            }

            if (!(source instanceof Definition)) {
                throw new DefinitionError("Only definition might be overwritten.", sourceDef);
            }
            Definition owner = getOwnerDefinition();

            Definition sourceOwner = sourceDef.getOwnerDefinition();

            if (owner == null) {
                throw new DefinitionError("Can not overwrite contextless definition.", sourceDef);
            }
            if (!owner.getId().equals(sourceOwner.getId())) {
                throw new DefinitionError("Overwritten definition should be in overwritten container.", sourceDef);
            }
            if (!owner.getModule().getSegment().getLayer().isHigherThan(sourceDef.getModule().getSegment().getLayer())) {
                throw new DefinitionError("Overwritten definition container should be in base layer.", sourceDef);
            }
            if (findById(sourceDef.getId()) != null) {
                throw new DefinitionError("Definition is already overwritten.", sourceDef);
            }


            Transferable t = sourceDef.getClipboardSupport().createTransferable(ETransferType.COPY);
            this.getClipboardSupport().paste(t, new ClipboardSupport.DuplicationResolver() {

                @Override
                public Resolution resolveDuplication(RadixObject newObject, RadixObject oldObject) {
                    return Resolution.CANCEL;
                }
            });

            T overwritten = getById(sourceDef.getId());
            if (overwritten != null) {
                ((IOverwritable) overwritten).setOverwrite(true);
                ((IOverwritable) overwritten).afterOverwrite();
            }
            return (R) overwritten;
        }
    }

    protected class DefinitionsClipboardSupport extends RadixObjectsClipboardSupport {

        @Override
        protected RadixObject findDuplicate(RadixObject objectInClipboard) {
            if (objectInClipboard instanceof Definition) {
                Definition def = (Definition) objectInClipboard;
                return findById(def.getId());
            } else {
                return null;
            }
        }
    }

    @Override
    public ClipboardSupport<? extends RadixObject> getClipboardSupport() {
        return new DefinitionsClipboardSupport();
    }
}
