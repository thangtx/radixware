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

package org.radixware.kernel.common.client.meta.sqml.impl;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.enums.EDefinitionDisplayMode;
import org.radixware.kernel.common.client.env.ClientIcon;
import org.radixware.kernel.common.client.meta.sqml.ISqmlBrokenDefinition;
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;


class SqmlBrokenDefinitionImpl extends SqmlDefinitionImpl implements ISqmlBrokenDefinition {

    private final ISqmlDefinition owner;
    private final Id targetId;
    private final String typeName;

    public SqmlBrokenDefinitionImpl(final IClientEnvironment environment, final Definition ownerDefinition, final Id definitionId, final String typeName) {
        super(environment, null);
        this.typeName = typeName;
        this.owner = ownerDefinition==null ? null : new SqmlDefinitionImpl(environment, ownerDefinition) {
                                                                                            @Override
                                                                                            public String getTitle() {
                                                                                                return super.getShortName();
                                                                                            }
                                                                                        };
        targetId = definitionId;
    }

    public SqmlBrokenDefinitionImpl(final IClientEnvironment environment, final Definition ownerDefinition, final Id definitionId) {
        this(environment,ownerDefinition,definitionId,null);
    }

    public String getTitle() {
        return getShortName();
    }

    @Override
    public String getDefinitionTypeName() {
        return typeName;
    }

    @Override
    public ISqmlDefinition getOwnerDefinition() {
        return owner;
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        return getShortName();
    }

    @Override
    public String getFullName() {
        return owner.getFullName() + ":" + targetId.toString();
    }

    @Override
    public ClientIcon getIcon() {
        return null;
    }

    @Override
    public Id getId() {
        return targetId;
    }

    @Override
    public String getShortName() {
        return "#" + targetId.toString();
    }
}
