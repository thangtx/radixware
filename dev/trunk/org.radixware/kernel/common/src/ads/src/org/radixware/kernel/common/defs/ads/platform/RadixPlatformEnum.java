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

package org.radixware.kernel.common.defs.ads.platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.EValType;


public class RadixPlatformEnum {

    public class Item {

        public transient final String name;
        public transient final ValAsStr val;

        public Item(final String name, final ValAsStr val) {
            this.name = name;
            this.val = val;
        }
    }
    public final transient EValType itemType;
    public final transient String name;
    private transient List<Item> items = null;

    public RadixPlatformEnum(final String name, final EValType itemType) {
        this.itemType = itemType;
        this.name = name.replace('/', '.').replace('$', '.');
    }

    public Item addItem(final String name, final ValAsStr val) {
        synchronized (this) {
            final Item item = new Item(name, val);
            if (items == null) {
                items = new ArrayList<Item>();
            }
            items.add(item);
            return item;
        }
    }

    public List<Item> getItems() {
        synchronized (this) {
            if (items == null) {
                return Collections.emptyList();
            } else {
                return new ArrayList<Item>(items);
            }
        }
    }
    private static final char[] ENUM_CLASS_NAME = "java/lang/Enum".toCharArray();

    public static final EValType getPlatformEnumType(final IBinaryType type) {
        if (type == null) {
            return null;
        } else {
            if (!CharOperation.equals(type.getSuperclassName(), ENUM_CLASS_NAME)) {
                return null;
            }

            final char[][] interfaceNames = type.getInterfaceNames();
            if (interfaceNames == null) {
                return null;
            }

            EValType itemType = null;
            for (int i = 0; i < interfaceNames.length; i++) {
                final char[] ifaceName = new char[interfaceNames[i].length];
                System.arraycopy(interfaceNames[i], 0, ifaceName, 0, ifaceName.length);
                CharOperation.replace(ifaceName, '/', '.');

                if (CharOperation.equals(WriterUtils.IKERNEL_CHAR_ENUM_CLASS_NAME, ifaceName)) {
                    itemType = EValType.CHAR;
                    break;

                } else if (CharOperation.equals(WriterUtils.IKERNEL_INT_ENUM_CLASS_NAME, ifaceName)) {
                    itemType = EValType.INT;
                    break;

                } else if (CharOperation.equals(WriterUtils.IKERNEL_STR_ENUM_CLASS_NAME, ifaceName)) {
                    itemType = EValType.STR;
                    break;

                }
            }
            return itemType;
        }
    }
}