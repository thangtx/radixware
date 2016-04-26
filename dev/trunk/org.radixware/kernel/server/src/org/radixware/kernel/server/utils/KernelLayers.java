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

package org.radixware.kernel.server.utils; 

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.server.Server;
import org.radixware.kernel.starter.meta.LayerMeta;
import org.radixware.kernel.starter.radixloader.RadixClassLoader;


public class KernelLayers {

    public static List<Class<?>> classListFromTopToBottomLayer(final String classRelativeName) {
        final List<Class<?>> lstFromBottom = classListFromBottomToTopLayer(classRelativeName);
        final List<Class<?>> lstFromTop = new LinkedList<>(lstFromBottom);
        Collections.reverse(lstFromTop);
        return Collections.unmodifiableList(lstFromTop);

    }

    public static List<Class<?>> classListFromBottomToTopLayer(final String classRelativeName) {
        final List<Class<?>> lst = new LinkedList<>();
        final RadixClassLoader cl = getServerClassLoader();
        if (cl != null) {
            final List<LayerMeta> layers = cl.getRevisionMeta().getAllLayersSortedFromBottom();
            for (LayerMeta layer : layers) { // searching from bottom layer to top
                final Class<?> c;
                try {
                    c = cl.loadClass(layer.getUri() + ".kernel." + classRelativeName);
                    lst.add(c);
                } catch (ClassNotFoundException e) {
                    continue;
                }
            }
        }
        return Collections.unmodifiableList(lst);
    }

    private static RadixClassLoader getServerClassLoader() {
        try {
            return (RadixClassLoader) Server.class.getClassLoader();
        } catch (ClassCastException e) {
            //do not throw to let unit-tests work
            //throw new RadixError("Server should be loaded by starter", e);
            return null;
        }
    }
}
