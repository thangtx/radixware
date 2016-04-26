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

package org.radixware.kernel.server.meta.clazzes;

import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.meta.RadDefinition;

public class RadMethodDef extends RadDefinition {

    private static final byte ACC_STATIC = 0x01;
    private static final byte ACC_ABSTRACT = 0x02;
    private final byte accStore;
    private final EAccess access;
    private final Parameter[] params;
    private final EValType returnType;
    private static final Parameter[] NO_PARAMS = new Parameter[0];

    public RadMethodDef(
            final Id id,
            final String name,
            final boolean isStatic,
            final boolean isAbstract,
            final EAccess access) {
        this(id, name, isStatic, isAbstract, access, null, EValType.JAVA_TYPE);
    }

    public RadMethodDef(
            final Id id,
            final String name,
            final boolean isStatic,
            final boolean isAbstract,
            final EAccess access, Parameter[] params,
            final EValType returnType) {
        super(id, name);
        byte acc = 0;
        if (isStatic) {
            acc &= ACC_STATIC;
        }
        if (isAbstract) {
            acc &= ACC_ABSTRACT;
        }
        this.accStore = acc;
        this.access = access;
        this.params = params;
        this.returnType = returnType;
    }

    public boolean isStatic() {
        return (accStore & ACC_STATIC) != 0;
    }

    public boolean isAbstract() {
        return (accStore & ACC_ABSTRACT) != 0;
    }

    public EAccess getAccess() {
        return access;
    }

    public Parameter[] getParams() {
        return params == null ? NO_PARAMS : params;
    }

    public EValType getReturnType() {
        return returnType;
    }

    public static class Parameter {

        public final String name;
        public final EValType valType;
        public final Id id;

        public Parameter(String name, EValType valType, Id id) {
            this.name = name;
            this.valType = valType;
            this.id = id;
        }

        public Parameter(String name, EValType valType) {
            this.name = name;
            this.valType = valType;
            this.id = Id.Factory.newInstance(EDefinitionIdPrefix.ADS_METHOD_PARAMETER);
        }
    }
}