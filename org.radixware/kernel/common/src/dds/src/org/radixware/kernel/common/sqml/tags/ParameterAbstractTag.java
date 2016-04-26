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
import org.radixware.kernel.common.defs.IParameterDef;
import org.radixware.kernel.common.exceptions.DefinitionNotFoundError;
import org.radixware.kernel.common.sqml.ISqmlEnvironment;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.utils.Utils;

/**
 * Тэг со ссылкой на параметр.
 */
public abstract class ParameterAbstractTag extends Sqml.Tag {

    protected ParameterAbstractTag() {
        super();
    }
    private Id parameterId = null;

    /**
     * Get identified of used parameter.
     */
    public Id getParameterId() {
        return parameterId;
    }

    public void setParameterId(Id parameterId) {
        if (!Utils.equals(this.parameterId, parameterId)) {
            this.parameterId = parameterId;
            setEditState(EEditState.MODIFIED);
        }
    }

    /**
     * Find parameter.
     * @return parameter or null if not found.
     */
    public IParameterDef findParameter() {
        final ISqmlEnvironment environment = getEnvironment();
        if (environment != null) {
            return environment.findParameterById(parameterId);
        } else {
            return null;
        }
    }

    /**
     * Find parameter.
     * @throw DefinitionNotFoundError if not found.
     */
    public IParameterDef getParameter() {
        final IParameterDef parameter = findParameter();
        if (parameter == null) {
            throw new DefinitionNotFoundError(parameterId);
        }
        return parameter;
    }

    @Override
    public void collectDependences(final List<Definition> list) {
        super.collectDependences(list);

        final IParameterDef parameter = findParameter();
        if (parameter != null) {
            final Definition definition = parameter.getDefinition();
            if (definition != null) {
                list.add(definition);
            }
        }
    }

    @Override
    protected void appendAdditionalToolTip(StringBuilder sb) {
        sb.append("<br>Parameter Id: " + String.valueOf(getParameterId()));
    }
}
