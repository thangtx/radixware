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

package org.radixware.kernel.common.enums;

import java.util.EnumSet;
import java.util.List;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.types.IKernelIntEnum;
import org.radixware.kernel.common.types.Id;


public enum EClassType implements IKernelIntEnum {

    ALGORITHM(7, 0),
    APPLICATION(2, 0),
    CUSTOM_WDGET_MODEL(23, 3),
    DIALOG_MODEL(16, 3),
    DYNAMIC(3, 0),
    ENTITY(1, 0),
    ENTITY_GROUP(11, 0),
    ENTITY_MODEL(15, 3),
    ENUMERATION(24, 0),
    EXCEPTION(8, 0),
    FORM_HANDLER(19, 0),
    FORM_MODEL(18, 3),
    FILTER_MODEL(21, 3),
    GROUP_MODEL(6, 3),
    INTERFACE(5, 0),
    PARAGRAPH_MODEL(13, 3),
    PRESENTATION_ENTITY_ADAPTER(20, 0),
    PROP_EDITOR_MODEL(17, 3),
    REPORT(12, 0),
    REPORT_MODEL(22, 3),
    SQL_CURSOR(4, 0),
    SQL_PROCEDURE(10, 0),
    SQL_STATEMENT(9, 0),
    COMMAND_MODEL(31, 3);
    private static final int BIT_SERVER_CLASS = 0;
    private static final int BIT_CLIENT_CLASS = 1;
    private static final int BIT_MODEL_CLASS = 2;
    private final Long val;
    private final int bits;

    private EClassType(long val, int bits) {
        this.val = Long.valueOf(val);
        this.bits = bits;
    }

    @Override
    public Long getValue() {
        return val;
    }

    public boolean isClientClass() {
        return clientSideTypes.contains(this);
    }

    public boolean isModelClass() {
        return modelTypes.contains(this);
    }

    public boolean isServerClass() {
        return serverSideTypes.contains(this);
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static EClassType getForValue(final Long val) {
        for (EClassType e : EClassType.values()) {
            if (e.val.equals(val)) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("EClassType has no item with value: " + String.valueOf(val), val);
    }
    private static final EnumSet<EClassType> clientSideTypes = EnumSet.noneOf(EClassType.class);
    private static final EnumSet<EClassType> serverSideTypes = EnumSet.noneOf(EClassType.class);
    private static final EnumSet<EClassType> modelTypes = EnumSet.noneOf(EClassType.class);

    static {
        serverSideTypes.add(ALGORITHM);
        serverSideTypes.add(APPLICATION);
        modelTypes.add(DIALOG_MODEL);
        serverSideTypes.add(DYNAMIC);
        serverSideTypes.add(ENTITY);
        serverSideTypes.add(ENTITY_GROUP);
        modelTypes.add(ENTITY_MODEL);
        serverSideTypes.add(EXCEPTION);
        serverSideTypes.add(FORM_HANDLER);
        modelTypes.add(FORM_MODEL);
        modelTypes.add(REPORT_MODEL);
        modelTypes.add(GROUP_MODEL);
        serverSideTypes.add(INTERFACE);
        modelTypes.add(PARAGRAPH_MODEL);
        serverSideTypes.add(PRESENTATION_ENTITY_ADAPTER);
        modelTypes.add(PROP_EDITOR_MODEL);
        serverSideTypes.add(REPORT);
        serverSideTypes.add(SQL_CURSOR);
        serverSideTypes.add(SQL_PROCEDURE);
        serverSideTypes.add(SQL_STATEMENT);
        modelTypes.add(COMMAND_MODEL);
        clientSideTypes.add(COMMAND_MODEL);
    }

    public static final EnumSet<EClassType> clientSideTypes() {
        return EnumSet.copyOf(clientSideTypes);
    }

    public static final EnumSet<EClassType> serverSideTypes() {
        return EnumSet.copyOf(serverSideTypes);
    }

    public static final EnumSet<EClassType> modelTypes() {
        return EnumSet.copyOf(modelTypes);
    }

    @Override
    public boolean isInDomain(Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(List<Id> ids) {
        return false;
    }
}
