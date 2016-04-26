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

package org.radixware.kernel.common.client.meta.sqml;

import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.types.Id;

/**
 * Интерфейс для доступа к дефинициям sqml-редактора
 */
public interface ISqmlDefinitions {

    ISqmlTableDef findTableById(final Id tableId);

    ISqmlEnumDef findEnumById(final Id enumId);

    ISqmlFunctionDef findFunctionById(final Id functionId);

    ISqmlPackageDef findPackageById(final Id packageId);

    ISqmlDefinition findDefinitionByIdPath(final List<Id> ids);

    Collection<ISqmlTableDef> getTables();

    Collection<ISqmlEnumDef> getEnums();
    
    Collection<ISqmlDomainDef> getDomains();

    Collection<ISqmlPackageDef> getPackages();
    
    Collection<ISqmlEventCodeDef> getEventCodes();
    
    ISqmlEventCodeDef findEventCodeById(Id id);

    ISqmlBrokenDefinition createBrokenDefinition(final Id id, final String typeName);        
}
