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

import org.radixware.kernel.common.resources.icons.RadixIcon;


public class RadixIconResource implements IResource {

    private final String key;
    private final RadixIcon icon;

    private static String getKey(RadixIcon icon) {
        final String resourceUri = icon.getResourceUri().replace('/', '_');
        return resourceUri;
    }

    public RadixIconResource(RadixIcon icon) {
        this(icon, getKey(icon));
    }

    public RadixIconResource(RadixIcon icon, String key) {
        this.icon = icon;
        this.key = key;
    }

//    @Override
//    public void save(OutputStream stream) throws IOException {
//        FileUtils.copyStream(icon.getClass().getClassLoader().getResourceAsStream(icon.getResourceUri()), stream);
//        stream.flush();
//    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getPath() {
        return getUri();
//        return "org.radixware/kernel/common/src/resources/src/" + icon.getResourceUri();
    }

    @Override
    public String getUri() {
        return "/" + icon.getResourceUri();
    }
}
