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

package org.radixware.kernel.common.radixdoc;

import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;


public abstract class RadixdocSupport<T extends RadixObject> implements IDocumentor {
    public static final String RADIXDOC_XML_FILE = RadixdocConventions.RADIXDOC_XML_FILE;

    private final T source;

    public RadixdocSupport(T source) {
        this.source = source;
    }

    public final T getSource() {
        return source;
    }

    public String getIdentifier() {
        if (source instanceof Definition) {
            return ((Definition)source).getId().toString();
        }
        return source.getQualifiedName();
    }
}
