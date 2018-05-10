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

package org.radixware.kernel.common.defs.uds;

import org.radixware.kernel.common.resources.icons.RadixIcon;


public class UdsDefinitionIcon extends RadixIcon {

    public static final UdsDefinitionIcon SEGMENT = new UdsDefinitionIcon("uds/segment.svg");
    public static final UdsDefinitionIcon MODULE = new UdsDefinitionIcon("uds/module.svg");
    public static final UdsDefinitionIcon USERFUNC = new UdsDefinitionIcon("uds/userfunc.svg");
    public static final UdsDefinitionIcon USERLIB = new UdsDefinitionIcon("file/load.svg");
   
    public static final UdsDefinitionIcon FILE = new UdsDefinitionIcon("file/file.svg");
    public static final UdsDefinitionIcon FOLDER = new UdsDefinitionIcon("file/folder.svg");
    public static final UdsDefinitionIcon XML_FILE = new UdsDefinitionIcon("file/xml_file.svg");

    public UdsDefinitionIcon(String name) {
        super(name);
    }
}
