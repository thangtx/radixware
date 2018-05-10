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
package org.radixware.kernel.common.defs;

import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;

/**
 * Factory for {@linkplain VisitorProvider}. Defines some standard visitor
 * providers.
 *
 */
public class VisitorProviderFactory {

    private VisitorProviderFactory() {
    }

    private static final class DefaultVisitorProvider extends VisitorProvider {

        @Override
        public boolean isContainer(RadixObject obj) {
            return true;
        }

        @Override
        public boolean isTarget(RadixObject obj) {
            return true;
        }
    }

    private static final class CheckVisitorProvider extends VisitorProvider {

        @Override
        public boolean isContainer(RadixObject obj) {
            if (obj instanceof Scml) { // impossible to check tags separately because tags in comments must be ignored
                return false;
            }
            return true;
        }

        @Override
        public boolean isTarget(RadixObject obj) {
            return true;
        }
    }

    /**
     * Create visitor provider that doesn't filter definitions.
     */
    public static VisitorProvider createDefaultVisitorProvider() {
        return new DefaultVisitorProvider();
    }

    public static VisitorProvider createDefinitionVisitorProvider() {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof Definition;
            }
        };
    }

    public static VisitorProvider createDefinitionVisitorProvider(final Id id) {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                if (radixObject instanceof Definition) {
                    return ((Definition) radixObject).getId().equals(id);
                }
                return false;
            }
        };
    }

    public static VisitorProvider createCheckVisitorProvider() {
        return new CheckVisitorProvider();
    }

    public static VisitorProvider createEnumVisitorProvider() {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof IEnumDef);
            }
        };
    }

    public static VisitorProvider createEnumItemVisitorProvider() {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof IEnumDef.IItem);
            }
        };
    }

    public static VisitorProvider createEmptyVisitorProvider() {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return false;
            }

            @Override
            public boolean isContainer(RadixObject radixObject) {
                return false;
            }
        };
    }

    public static VisitorProvider createModuleVisitorProvider() {
        return new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof Module;
            }

            @Override
            public boolean isContainer(RadixObject radixObject) {
                return !(radixObject instanceof Module);
            }
        };
    }
}
