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

/*
 * 10/25/11 5:01 PM
 */
package org.radixware.kernel.designer.ads.editors.clazz.forms.palette.items;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.ui.AdsMetaInfo;
import org.radixware.kernel.designer.ads.editors.clazz.forms.palette.*;

public class CommandToolButtonItem extends ToolButtonItem {

    public static final CommandToolButtonItem DEFAULT = new CommandToolButtonItem();

    

    public CommandToolButtonItem() {
        super(Group.GROUP_RADIX_WIDGETS,
                NbBundle.getMessage(CommandToolButtonItem.class, "Command_Tool_Button"),
                AdsMetaInfo.COMMAND_TOOL_BUTTON_CLASS,
                new ToolButtonPainter(),
                new ToolButtonPropertyCollector());
    }
}
