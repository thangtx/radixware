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

package org.radixware.kernel.explorer.macros;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;


public final class ModelDescriptor {

    public final static class Factory {

        private Factory() {
        }

        public static ModelDescriptor newInstance(final Model model) {
            return new ModelDescriptor(model);
        }
    }
    private final Id definitionId;
    private final String className;
    private final Pid entityObjectPid;
    private final IClientEnvironment environment;

    private ModelDescriptor(final Model model) {
        this.environment = model.getEnvironment();
        definitionId = model.getDefinition().getId();
        className = model.getClass().getName();
        if (model instanceof EntityModel) {
            entityObjectPid = ((EntityModel) model).getPid();
        } else {
            entityObjectPid = null;
        }
    }

    public IClientEnvironment environment() {
        return environment;
    }

    public boolean isSameModel(final Model model) {
        if (!className.equals(model.getClass().getName())) {
            return false;
        }
        if (!definitionId.equals(model.getDefinition().getId())) {
            return false;
        }
        if ((model instanceof EntityModel) && !Utils.equals(entityObjectPid, ((EntityModel) model).getPid())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (entityObjectPid == null) {
            return className + "; #" + definitionId.toString();
        } else {
            return className + "; #" + definitionId.toString() + "; pid=" + entityObjectPid.toString();
        }
    }
}
