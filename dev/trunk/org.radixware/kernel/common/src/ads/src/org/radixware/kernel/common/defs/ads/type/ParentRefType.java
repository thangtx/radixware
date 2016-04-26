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

package org.radixware.kernel.common.defs.ads.type;

import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.type.AdsType.TypeJavaSourceSupport;
import org.radixware.kernel.common.exceptions.DefinitionError;
import org.radixware.kernel.common.types.Id;


public class ParentRefType extends AdsClassType.EntityObjectType {

    public static final class Factory {

        public static final ParentRefType newInstance(AdsEntityObjectClassDef clazz) {
            if (clazz == null) {
                return getDefault();
            } else {
                return new ParentRefType(clazz);
            }
        }

        public static final ParentRefType getDefault() {
            return defaultInstance;
        }
    }
    private static final ParentRefType defaultInstance = new ParentRefType((AdsEntityClassDef) null);

    protected ParentRefType(AdsEntityObjectClassDef clazz) {
        super(clazz);
    }

    public ParentRefType(Id entityId) {
        super(entityId);
    }

    @Override
    public String getName() {
        return getName(null, false);
    }

    private String getName(RadixObject context, boolean qualified) {
        if (source == null) {
            return "ParentRef";
        } else {
            String name = "Ref<";
            name += qualified ? source.getQualifiedName(context) : source.getName();
            name += ">";
            return name;
        }
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return getName(context, true);
    }

    @Override
    public AdsEntityObjectClassDef getSource() {
        return super.getSource();
    }

    private class ParentRefJavaSourceSupport extends UserClassJavaSourceSupport {

        ParentRefJavaSourceSupport() {
            super(ParentRefType.this);
        }

        @Override
        public char[][] getPackageNameComponents(UsagePurpose env) {
            if (source != null) {
                return super.getPackageNameComponents(env);
            } else {
                switch (env.getEnvironment()) {
                    case EXPLORER:
                    case WEB:
                    case COMMON_CLIENT:
                        return new char[][]{RADIX_EXPLORER_TYPES_PACKAGE_NAME};
                    case SERVER:
                        return new char[][]{RADIX_SERVER_TYPES_PACKAGE_NAME};
                    default:
                        throw new DefinitionError("Unsupported usage purpose: ", ParentRefType.this);
                }
            }
        }

        @Override
        public char[] getLocalTypeName(UsagePurpose env) {
            switch (env.getEnvironment()) {
                case EXPLORER:
                case WEB:
                case COMMON_CLIENT:
                    //  if (source == null) {
                    return RADIX_TYPE_REFERENCE;
//                    } else {
//                        return super.getLocalTypeName(env);
//                    }
//                    char[] simpleLocalName = super.getLocalTypeName(env);
//                    char[] suffix = AdsModelClassDef.getDefaultModelLocalClassName(EClassType.ENTITY_MODEL).toCharArray();
//                    return CharOperations.merge(simpleLocalName, suffix, '.');

                case SERVER:
                    if (source == null) {
                        return RADIX_TYPE_ENTITY;
                    } else {
                        return super.getLocalTypeName(env);
                    }
                default:
                    throw new DefinitionError("Unsupported usage purpose: ", ParentRefType.this);
            }
        }
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new ParentRefJavaSourceSupport();
    }
}
