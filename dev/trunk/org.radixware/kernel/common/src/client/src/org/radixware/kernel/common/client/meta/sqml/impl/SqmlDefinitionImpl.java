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
import org.radixware.kernel.common.client.meta.sqml.ISqmlDefinition;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.client.env.KernelIcon;


abstract class SqmlDefinitionImpl implements ISqmlDefinition {

    private final Definition def;
    protected final IClientEnvironment environment;

    public SqmlDefinitionImpl(final IClientEnvironment environment, final Definition definition) {
        def = definition;
        this.environment = environment;
    }

    @Override
    public String getFullName() {
        if (def.getName() == null || def.getName().isEmpty()) {
            return "#" + def.getId().toString();
        }
        return def.getQualifiedName();
    }

    @Override
    public ClientIcon getIcon() {
        return KernelIcon.getInstance(def.getIcon());
    }

    @Override
    public Id getId() {
        return def.getId();
    }
    
    

    @Override
    public String getShortName() {
        if (def.getName() == null || def.getName().isEmpty()) {
            return "#" + def.getId().toString();
        }
        return def.getName();
    }

    @Override
    public final Id[] getIdPath() {
        return def.getIdPath();
    }

    @Override
    public String getModuleName() {
        return def.getModule().getQualifiedName();
    }

    protected String checkTitle(final String title) {
        return title == null || title.isEmpty() ? getShortName() : title;
    }

    @Override
    public String getDisplayableText(final EDefinitionDisplayMode mode) {
        switch (mode) {
            case SHOW_TITLES:
                return getTitle();
            case SHOW_FULL_NAMES:
                return getFullName();
            default:
                return getShortName();
        }
    }
    
    @Override
    public boolean isDeprecated() {
        return def.isDeprecated();
    }
}
