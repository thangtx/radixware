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
package org.radixware.kernel.common.sqml.translate;

import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;

public interface ISqmlPreprocessorConfig {

    public PreprocessorParameter getParameter(final Id paramId);

    /**
     * @return value of the specified property
     */
    public Object getProperty(final String propertyName);

    /**
     * @return database type name (e.g. ORACLE)
     */
    public String getDbTypeName();

    /**
     * @return null for any version
     */
    public String getDbVersion();

    public Sqml copy(final Sqml sqml);

    /**
     * If source sqml doesn't contain any preprocessing tags, preprocessor can
     * return source sqml itself (if alwaysCreateCopy() == false), or make a
     * copy (otherwise).
     */
    public boolean alwaysCreateCopy();
    
    public static interface PreprocessorParameter {

        public Object getValue();

        public Id getId();

        public String getName();
    }
}
