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
package org.radixware.kernel.designer.dds.script;

import java.util.Comparator;
import java.util.Map;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.enums.EDatabaseType;

/**
 * <p>This interface describes a collection of script generators for the given database</p>
 * @param <T> 
 */
public interface IDdsScriptGeneratorCollection<T extends DdsDefinition> {
    /**
     * <p>Get database type these scripts are used for</p>
     * @return database type. Can't be null
     */
    EDatabaseType getDatabaseType();
    
    /**
     * <p>Get collection of the scripts for the given database</p>
     * @return script collection
     */
    Map<Class<T>,IDdsDefinitionScriptGenerator<T>> getScriptsCollection();
    

    /**
     * <p>Get ordering comparator to sort database definitions in the valid order</p>
     * @return 
     */
    Comparator<DefinitionPair> getOrderingComparator();
}
