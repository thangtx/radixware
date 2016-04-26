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

package org.radixware.wps.sqml;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.client.env.SqmlDefinitionsFactory;
import org.radixware.kernel.common.client.meta.sqml.*;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;


public class WpsSqmlDefinitionsFactory implements SqmlDefinitionsFactory {

    @Override
    public ISqmlDefinitions newSqmlDefinitions(Branch branch) {
        return new ISqmlDefinitions() {

            @Override
            public ISqmlTableDef findTableById(Id tableId) {
                return null;
            }

            @Override
            public ISqmlEnumDef findEnumById(Id enumId) {
                return null;
            }

            @Override
            public ISqmlFunctionDef findFunctionById(Id functionId) {
                return null;
            }

            @Override
            public ISqmlPackageDef findPackageById(Id packageId) {
                return null;
            }

            @Override
            public ISqmlDefinition findDefinitionByIdPath(List<Id> ids) {
                return null;
            }

            @Override
            public Collection<ISqmlTableDef> getTables() {
                return Collections.emptyList();
            }

            @Override
            public Collection<ISqmlEnumDef> getEnums() {
                return Collections.emptyList();
            }

            @Override
            public Collection<ISqmlDomainDef> getDomains() {
                return Collections.emptyList();
            }                        

            @Override
            public Collection<ISqmlPackageDef> getPackages() {
                return Collections.emptyList();
            }

            @Override
            public Collection<ISqmlEventCodeDef> getEventCodes() {
                return Collections.emptyList();
            }
           
            @Override
            public ISqmlBrokenDefinition createBrokenDefinition(Id id, String typeName) {
                return null;
            }

            @Override
            public ISqmlEventCodeDef findEventCodeById(Id id) {
                return null;
            }
        };
    }
}
