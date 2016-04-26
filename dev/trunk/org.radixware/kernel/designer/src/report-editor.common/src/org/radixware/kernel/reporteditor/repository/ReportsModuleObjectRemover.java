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

package org.radixware.kernel.reporteditor.repository;

import org.radixware.kernel.common.userreport.repository.ReportsModule;
import org.radixware.kernel.designer.common.annotations.registrators.ObjectRemoverFactoryRegistration;
import org.radixware.kernel.designer.common.general.nodes.operation.IObjectRemoverFactory;
import org.radixware.kernel.designer.common.general.nodes.operation.ObjectRemover;


public class ReportsModuleObjectRemover extends ObjectRemover {

    private final ReportsModule module;

    public ReportsModuleObjectRemover(final ReportsModule module) {
        this.module = module;
    }

    @Override
    public void removeObject() {
        module.removeFromDb();
    }

    @ObjectRemoverFactoryRegistration
    public static final class Factory implements IObjectRemoverFactory<ReportsModule> {

        @Override
        public ObjectRemover newInstance(final ReportsModule obj) {
            return new ReportsModuleObjectRemover(obj);
        }
    }
}
