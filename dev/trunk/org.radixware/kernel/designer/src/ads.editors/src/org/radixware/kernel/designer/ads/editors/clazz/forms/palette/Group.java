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

package org.radixware.kernel.designer.ads.editors.clazz.forms.palette;

import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class Group extends RadixObject {

    public static final Group GROUP_CUSTOM_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_CUSTOM_WIDGETS"));
    public static final Group GROUP_WEB_CUSTOM_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_CUSTOM_WIDGETS"), ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_RADIX_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_RADIX_WIDGETS"));
    public static final Group GROUP_LAYOUTS = new Group(NbBundle.getMessage(Group.class, "GROUP_LAYOUTS"));
    public static final Group GROUP_SPACERS = new Group(NbBundle.getMessage(Group.class, "GROUP_SPACERS"));
    public static final Group GROUP_BUTTONS = new Group(NbBundle.getMessage(Group.class, "GROUP_BUTTONS"));
    public static final Group GROUP_CONTAINERS = new Group(NbBundle.getMessage(Group.class, "GROUP_CONTAINERS"));
    public static final Group GROUP_DISPLAY_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_DISPLAY_WIDGETS"));
    public static final Group GROUP_INPUT_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_INPUT_WIDGETS"));
    public static final Group GROUP_ITEM_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_ITEM_WIDGETS"));
    public static final Group GROUP_WEB_BUTTONS = new Group(NbBundle.getMessage(Group.class, "GROUP_BUTTONS"), ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_WEB_INPUTS = new Group(NbBundle.getMessage(Group.class, "GROUP_INPUT_WIDGETS"), ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_WEB_DISPLAY = new Group(NbBundle.getMessage(Group.class, "GROUP_DISPLAY_WIDGETS"), ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_WEB_CONTAINERS = new Group(NbBundle.getMessage(Group.class, "GROUP_CONTAINERS"), ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_WEB_ITEM_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_ITEM_WIDGETS"), ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_WEB_ACTIONS = new Group("Actions", ERuntimeEnvironmentType.WEB);
    public static final Group GROUP_WEB_RADIX_WIDGETS = new Group(NbBundle.getMessage(Group.class, "GROUP_RADIX_WIDGETS"), ERuntimeEnvironmentType.WEB);
    private String title;
    private ERuntimeEnvironmentType env;

    private Group(String title) {
        this(title, ERuntimeEnvironmentType.EXPLORER);

    }

    private Group(String title, ERuntimeEnvironmentType env) {
        this.title = title;
        this.env = env;
    }

    public String getTitle() {
        return title;
    }

    public ERuntimeEnvironmentType getEnvironment() {
        return env;
    }
}