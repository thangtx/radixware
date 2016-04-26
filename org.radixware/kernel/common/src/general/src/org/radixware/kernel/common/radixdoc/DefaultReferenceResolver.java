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

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import static org.radixware.kernel.common.radixdoc.IReferenceResolver.PATH_DELIMITER;
import org.radixware.kernel.common.types.Id;

public final class DefaultReferenceResolver implements IReferenceResolver {

    private static String concatPath(String... parts) {
        final StringBuilder builder = new StringBuilder();

        boolean first = true;
        for (String part : parts) {
            if (first) {
                first = false;
            } else {
                builder.append("/");
            }
            builder.append(part);
        }

        return builder.toString();
    }

    @Override
    public String resolve(RadixObject from, RadixObject target) {
        final IRadixdocProvider provider = findProvider(target);

        if (provider != null) {
            if (provider == from) {
                return "#" + getIdentifier(target);
            }

            final Module module = target.getModule();
            if (provider == target) {
                return concatPath(module.getLayer().getURI(), module.getSegmentType().getValue(),
                        module.getName(), getIdentifier(target));
            } else {
                return concatPath(module.getLayer().getURI(), module.getSegmentType().getValue(),
                        module.getName(), getIdentifier((RadixObject) provider)) + PATH_DELIMITER + getIdentifier(target);
            }
        }

        return "";
    }

    public IRadixdocProvider findProvider(RadixObject target) {
        RadixObject provider = target;
        while (provider != null) {
            if (provider instanceof IRadixdocProvider && ((IRadixdocProvider) provider).isRadixdocProvider()) {
                return (IRadixdocProvider) provider;
            }
            provider = provider.getContainer();
        }
        return null;
    }

    @Override
    public String getIdentifier(RadixObject target) {
        if (target instanceof Definition) {
            StringBuilder pageName = new StringBuilder();
            boolean first = true;

            for (Id id : ((Definition) target).getIdPath()) {
                if (first) {
                    first = false;
                } else {
                    pageName.append("-");
                }
                pageName.append(id.toString());
            }

            return pageName.toString();
        }
        return target.getQualifiedName();
    }
}
