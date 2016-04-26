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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.util.ServiceLoader;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;


public interface IMoveTransformationFactory {

    public static class Lookup {

        public static IMoveTransformationFactory getFactory(Definition movedDef) {
            ServiceLoader<IMoveTransformationFactory> loader = ServiceLoader.load(IMoveTransformationFactory.class);
            for (IMoveTransformationFactory f : loader) {
                if (f.matches(movedDef)) {
                    return f;
                }
            }
            return new MoveTransformationFactory();
        }
    }

    public IMoveTransformation findTransformation(final RadixObject externalRadixObject, Definition movedDef, final RadixObject destination);

    public boolean matches(Definition def);
}
