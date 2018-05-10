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
package org.radixware.kernel.common.conventions;

import org.radixware.kernel.common.types.Id;

public final class RadixdocConventions {

    private RadixdocConventions() {
    }

    public static final String TECHDOC_BODY_DIR_NAME = "body";
    public static final String TECHDOC_RESOURCES_DIR_NAME = "resources";
    public static final String RADIXDOC_XML_FILE = "radixdoc.xml";
    public static final String RADIXDOC_2_XML_FILE = "radixdoc_2.xml";

    public static final String RADIXDOC_ZIP_FILE = "radixdoc.zip";

    public static final String RADIXDOC_DECORATIONS_PATH = "etc/DocDecorations";

    public static final String RADIXDOC_VERSION_DELIMITER = ".";

    public static final String RADIXDOC_2_NEW_LINE_MARK = "{newLine}";
    public static final String RADIXDOC_2_BEGIN_XREF_MARK = "{bXref}";
    public static final String RADIXDOC_2_END_XREF_MARK = "{eXref}";

    public static final String LOCALIZING_MODULE_PATH = "org.radixware/ads/Utils";
    public static final Id LOCALIZING_MODULE_ID = Id.Factory.loadFrom("mdlXJFKJCKY3VEG7N7IXU3BOT6BMI");
    public static final Id LOCALIZING_BUNDLE_ID = Id.Factory.loadFrom("mlbadcLPVM3UU5CNFNVCAN4XN675OICU");
    public static final Id LOCALIZING_DEF_ID = Id.Factory.loadFrom("adcLPVM3UU5CNFNVCAN4XN675OICU");
}
