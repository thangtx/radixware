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
package org.radixware.kernel.common.dialogs.chooseobject;

import javax.swing.Icon;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.defs.RadixObject;

/**
 *
 * @author dlastochkin
 */
public class SelectableObjectWrapper {
    public static ISelectableObject getRadixObjectWrap(final RadixObject object) {
        return new ISelectableObject() {

            @Override
            public String getTitle() {
                return object.getName();
            }

            @Override
            public Object getUserObject() {
                return object;
            }

            @Override
            public Icon getIcon() {
                return object.getIcon().getIcon();
            }

            @Override
            public String getLocation() {
                return object.getOwnerForQualifedName() == null ? "" : object.getOwnerForQualifedName().getQualifiedName();
            }                        

            @Override
            public Icon getLocationIcon() {
                return object.getOwnerForQualifedName() == null ? null : object.getOwnerForQualifedName().getIcon().getIcon();
            }
        };
    }
}
