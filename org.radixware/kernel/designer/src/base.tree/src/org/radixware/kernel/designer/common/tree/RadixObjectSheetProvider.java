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

package org.radixware.kernel.designer.common.tree;

import java.lang.reflect.InvocationTargetException;
import org.openide.nodes.Node.Property;
import org.openide.nodes.Sheet;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;


public class RadixObjectSheetProvider {

    private static abstract class ReadOnlyProperty extends Property<String> {

        private final String name;

        public ReadOnlyProperty(final String name) {
            super(String.class);
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean canRead() {
            return true;
        }

        @Override
        public boolean canWrite() {
            return false;
        }

        @Override
        public void setValue(String val) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            throw new IllegalStateException();
        }
    }

    public static Sheet createSheet(final RadixObject radixObject) {
        final Sheet sheet = Sheet.createDefault();
        final Sheet.Set set = Sheet.createPropertiesSet();
        final ReadOnlyProperty propType = new ReadOnlyProperty("Type") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return radixObject.getTypeTitle();
            }
        };

        set.put(propType);
        final ReadOnlyProperty propName = new ReadOnlyProperty("Name") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return radixObject.getName();
            }
        };

        set.put(propName);
        final ReadOnlyProperty propQualifiedName = new ReadOnlyProperty("Qualified Name") {

            @Override
            public String getValue() throws IllegalAccessException, InvocationTargetException {
                return radixObject.getQualifiedName();
            }
        };

        set.put(propQualifiedName);
        if (radixObject instanceof Definition) {
            final ReadOnlyProperty propId = new ReadOnlyProperty("Id") {

                @Override
                public String getValue() throws IllegalAccessException, InvocationTargetException {
                    return ((Definition) radixObject).getId().toString();
                }
            };

            set.put(propId);
        }

        sheet.put(set);
        return sheet;
    }
}
