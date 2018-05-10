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
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.exceptions.DefinitionError;


public class ObjectType extends AdsClassType.EntityObjectType {

    public static final class Factory {

        public static ObjectType newInstance(AdsEntityObjectClassDef clazz) {
            if (clazz == null) {
                return getDefault();
            }
            return new ObjectType(clazz);
        }

        public static ObjectType getDefault() {
            return defaultInstance;
        }
    }
    private static final ObjectType defaultInstance = new ObjectType(null);

    public ObjectType(AdsEntityObjectClassDef clazz) {
        super(clazz);
    }

    @Override
    public AdsEntityObjectClassDef getSource() {
        return (AdsEntityObjectClassDef) source;
    }

    @Override
    public String getName() {
        return getName(null, false);
    }

    private String getName(RadixObject context, boolean qualified) {
        String name = "Object";
        if (source == null) {
            return name;
        } else {
            name += "<";
            name += qualified ? source.getQualifiedName(context) : source.getName();
            name += ">";
            return name;
        }
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return getName(context, true);
    }

    private class ObjectJavaSourceSupport extends UserClassJavaSourceSupport {

        ObjectJavaSourceSupport() {
            super(ObjectType.this);
        }

        @Override
        public char[][] getPackageNameComponents(UsagePurpose env, boolean isHumanReadable) {
            if (source != null) {
                return super.getPackageNameComponents(env, isHumanReadable);
            } else {
                switch (env.getEnvironment()) {
                    case EXPLORER:
                    case COMMON_CLIENT:
                    case WEB:
                        return new char[][]{RADIX_EXPLORER_TYPES_PACKAGE_NAME};
                    case SERVER:
                        return new char[][]{RADIX_SERVER_TYPES_PACKAGE_NAME};
                    default:
                        return new char[][]{{'?', '?', '?'}};
                }
            }
        }

        @Override
        public char[] getLocalTypeName(UsagePurpose env, boolean isHumanReadable) {
            switch (env.getEnvironment()) {
                case EXPLORER:
                case COMMON_CLIENT:
                case WEB:
                    //if (source == null) {
                    return RADIX_TYPE_REFERENCE;
                //}else
                //  return super.getLocalTypeName(env);
//                    char[] simpleLocalName = super.getLocalTypeName(env);
//                    char[] suffix = AdsModelClassDef.getDefaultModelLocalClassName(EClassType.ENTITY_MODEL).toCharArray();
//                    return CharOperations.merge(simpleLocalName, suffix, '.');
                case SERVER:
                    if (source == null) {
                        return RADIX_TYPE_REFERENCE;
                    } else {
                        return super.getLocalTypeName(env, isHumanReadable);
                    }
                default:
                    throw new DefinitionError("Unsupported usage purpose: ", ObjectType.this);
            }
        }
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new ObjectJavaSourceSupport();
    }
}
