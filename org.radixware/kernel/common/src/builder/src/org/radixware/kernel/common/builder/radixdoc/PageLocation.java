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

package org.radixware.kernel.common.builder.radixdoc;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.radixware.kernel.common.conventions.RadixdocConventions;
import org.radixware.kernel.common.radixdoc.IReferenceResolver;


final class PageLocation {

    public static String concatUrl(String... parts) {
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

    private final Path modulePath;
    private final String provider;
    private final String name;

    PageLocation(Path modulePath, String provider, String name) {
        this.modulePath = modulePath;
        this.provider = provider;
        this.name = name;
    }

    Path getModulePath() {
        return modulePath;
    }

    Path getProviderPath() {
        return Paths.get(getModulePath().toString(), provider);
    }

    Path getOutputPath() {
        return getModulePath().resolve(RadixdocConventions.RADIXDOC_ZIP_FILE);
    }

    String getReference() {
        String uri = Paths.get(".").toUri().relativize(getProviderPath().toUri()).toString();
        return name != null ? uri + IReferenceResolver.PATH_DELIMITER + name :  uri;
    }
    
    @Override
    public String toString() {
        return getReference();
    }

    public String getName() {
        return name;
    }
}
