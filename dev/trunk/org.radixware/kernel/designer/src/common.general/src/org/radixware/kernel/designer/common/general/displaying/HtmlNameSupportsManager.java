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

package org.radixware.kernel.designer.common.general.displaying;

import org.radixware.kernel.common.defs.RadixObject;

/**
 * Manager for definition html name supports. Allows to display definition
 * display name in html kind and listen it for changes. All html name supports
 * are registered in layer.xml files.
 *
 */
public class HtmlNameSupportsManager {

    private static final HtmlNameSupportFactoriesManager htmlNameSupportFactoriesManager = new HtmlNameSupportFactoriesManager();

    /**
     * Find editor for definition.
     *
     * @return definition editor or null if none.
     */
    public static HtmlNameSupport newInstance(RadixObject radixObject) {
        final IHtmlNameSupportFactory factory = htmlNameSupportFactoriesManager.find(radixObject);
        if (factory != null) {
            return factory.newInstance(radixObject);
        } else {
            return new HtmlNameSupport(radixObject);
        }
    }
}
