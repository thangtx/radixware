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

package org.radixware.kernel.server.arte;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.dds.DdsDefinition;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;


class DdsUtils {

    static <T extends DdsDefinition> Map<Id, T> createIdx(final Branch branch, final VisitorProvider visitorProvider) {
        final Map<Id, T> idx = new HashMap<Id, T>();
        branch.visit(new IVisitor() {
            @Override
            public void accept(final RadixObject object) {
                final T t = (T) object;
                if (!idx.containsKey(t.getId())) // was not ovverided in a higher layer
                {
                    idx.put(t.getId(), t);
                }
            }
        }, visitorProvider);
        return Collections.<Id, T>unmodifiableMap(idx);
    }
}
