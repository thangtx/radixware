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

package org.radixware.kernel.designer.environment;

import org.openide.filesystems.FileSystem;
import org.openide.filesystems.MultiFileSystem;
import org.openide.filesystems.XMLFileSystem;
import org.openide.util.Exceptions;
import org.openide.util.lookup.ServiceProvider;
import org.xml.sax.SAXException;


@ServiceProvider(service = FileSystem.class)
public class DynamicFs extends MultiFileSystem {

    private static DynamicFs INSTANCE;

    public DynamicFs() {
        // will be created on startup, exactly once
        INSTANCE = this;        
        setPropagateMasks(true); // permit *_hidden masks to be used
    }

    static boolean hasContent() {
        return INSTANCE.getDelegates().length > 0;
    }

    static void enable() {
        if (!hasContent()) {
            try {
                INSTANCE.setDelegates(new XMLFileSystem(
                        DynamicFs.class.getResource(
                        "development.xml")));
            } catch (SAXException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    static void disable() {
        INSTANCE.setDelegates();
    }
}
