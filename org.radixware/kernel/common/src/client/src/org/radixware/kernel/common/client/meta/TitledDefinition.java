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

package org.radixware.kernel.common.client.meta;

import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;

/**
 * Base class for explorer definition that can have a localized title.
 */
public abstract class TitledDefinition extends NamedDefinition {

    private final Id titleId;
    private final Id titleOwnerId;
    private String title = null;

    /**
     * Constructs an explorer definition
     * @param id definition`s  unique identifier
     * @param name definition`s  unique name
     * @param ownerId identifier of definition which is owner of this definition
     * @param titleId identifier of definition`s title
     */
    public TitledDefinition(final Id id, final String name, final Id ownerId, final Id titleId) {
        super(id, name);
        this.titleId = titleId;
        this.titleOwnerId = ownerId;
    }

    /**
     * Constructs an explorer definition
     * @param id definition`s  unique identifier
     * @param name definition`s  unique name
     * @param titleId identifier of definition`s title
     */
    public TitledDefinition(final Id id, final String name, final Id titleId) {
        super(id, name);
        this.titleId = titleId;
        this.titleOwnerId = id;
    }

    protected String getTitle(final Id definitionId, final Id titleId) {
        if (titleId != null) {
            try {
                return getDefManager().getMlStringValue(definitionId, titleId);
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get Title with id \"%s\" for %s");
                getApplication().getTracer().error(String.format(mess, titleId, getInfo()), err);
                return getApplication().getMessageProvider().translate("ExplorerItem", "<No Title>");
            }
        } else {
            return getApplication().getMessageProvider().translate("ExplorerItem", "<No Title>");
        }

    }

    /**
     * Returns definition`s localized title.
     * If this definition does not have a title this method retuns "<No Title>" string.
     * @return definitions title.
     */
    public String getTitle() {
        //if (title == null) {
         //   title = getTitle(titleOwnerId, titleId);
       // }
       // return title;
        //нельзя кэшировать заголовок, т.к. он может использоваться клиентами с разными языками
        return getTitle(titleOwnerId, titleId);
    }

    /**
     * Returns true if this definition has a not empty title; otherwise returns false.
     * @return  true if this definition has a not empty title.
     */
    public boolean hasTitle() {
        final boolean titleDefined = titleOwnerId != null && titleId != null;
        if (titleDefined) {
            try {
                final String s = getDefManager().getMlStringBundleDef(titleOwnerId).get(titleId);
                return s != null && !s.isEmpty();
            } catch (DefinitionError err) {
                final String mess = getApplication().getMessageProvider().translate("TraceMessage", "Cannot get string bundle #%s for definition %s");
                getApplication().getTracer().error(String.format(mess, titleOwnerId, getInfo()), err);
            }
        }
        return false;
    }

    private String getInfo() {
        return (getName() != null && !getName().isEmpty() ? "\"" + getName() + "\" " : "") + "#" + getId().toString();
    }

    @Override
    public String toString() {
        if (hasTitle()) {
            return String.format(getApplication().getMessageProvider().translate("DefinitionDescribtion", "%s with title \"%s\""), super.toString(), getTitle());
        }
        return super.toString();
    }

    @Override
    public String getDescription() {
        return toString();
    }
}
