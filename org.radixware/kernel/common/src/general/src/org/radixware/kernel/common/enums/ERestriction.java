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

public enum ERestriction implements IKernelIntEnum {

    CALC_STATISTIC(0x4000000),
    MULTIPLE_CREATE(0x2000000),
    MULTIPLE_DELETE(0x1000000),
    SELECT_ALL(0x800000),
    MULTIPLE_SELECTION(0x400000),
    NOT_READ_ONLY_COMMANDS(0x200000),
    ANY_PAGES(0x100000),
    VIEW(0x80000),
    CONTEXTLESS_USAGE(0x40000),
    ACCESS(0x10000),
    ANY_CHILD(0x20000),
    ANY_COMMAND(0x10),
    CHANGE_POSITION(0x8000),
    COPY(0x1000),
    CREATE(0x1),
    DELETE(0x2),
    DELETE_ALL(0x20),
    EDITOR(0x800),
    INSERT_ALL_INTO_TREE(0x8),
    INSERT_INTO_TREE(0x40),
    MOVE(0x2000),
    MULTIPLE_COPY(0x4000),
    RUN_EDITOR(0x400),
    TRANSFER_IN(0x80),
    TRANSFER_OUT(0x100),
    TRANSFER_OUT_ALL(0x200),
    UPDATE(0x4);
    private Long val;

    private ERestriction(final long val) {
        this.val = Long.valueOf(val);
    }

    @Override
    public Long getValue() {
        return val;
    }

    @Override
    public String getName() {
        return this.name();
    }

    public static ERestriction getForValue(final long val) {
        for (ERestriction e : ERestriction.values()) {
            if (e.val.longValue() == val) {
                return e;
            }
        }
        throw new NoConstItemWithSuchValueError("ERestriction has no item with value: " + String.valueOf(val), val);
    }

    public static final long toBitField(final EnumSet<ERestriction> enumSet) {
        long result = 0;
        for (ERestriction e : enumSet) {
            result |= e.val.longValue();
        }
        return result;
    }

    public static final EnumSet<ERestriction> fromBitField(final long field) {
        final EnumSet<ERestriction> enumSet = EnumSet.noneOf(ERestriction.class);

        for (ERestriction e : values()) {
            if ((field & e.val.longValue()) != 0L) {
                enumSet.add(e);
            }
        }

        return enumSet;
    }

    @Override
    public boolean isInDomain(final Id id) {
        return false;
    }

    @Override
    public boolean isInDomains(final List<Id> ids) {
        return false;
    }

    public String getAsStr() {
        switch (this) {
            case NOT_READ_ONLY_COMMANDS:
                return "Not read only commands";
            case ANY_PAGES:
                return "Any pages";
            case VIEW:
                return "View";
            case CONTEXTLESS_USAGE:
                return "Contextless usage";
            case ACCESS:
                return "Access";
            case ANY_CHILD:
                return "Any child";
            case ANY_COMMAND:
                return "Any commands";
            case CHANGE_POSITION:
                return "Change position";
            case COPY:
                return "Copy";
            case CREATE:
                return "Create";
            case DELETE:
                return "Delete";
            case DELETE_ALL:
                return "Delete all";
            case EDITOR:
                return "Editor under selector";
            case INSERT_ALL_INTO_TREE:
                return "Insert all into tree";
            case INSERT_INTO_TREE:
                return "Insert into tree";
            case MOVE:
                return "Move";
            case MULTIPLE_COPY:
                return "Multiple copy";
            case RUN_EDITOR:
                return "Run editor";
            case TRANSFER_IN:
                return "Transfer in";
            case TRANSFER_OUT:
                return "Transfer out";
            case TRANSFER_OUT_ALL:
                return "Transfer out all";
            case UPDATE:
                return "Update";
            case MULTIPLE_SELECTION:
                return "Multiple selection";
            case SELECT_ALL:
                return "Select all";
            case MULTIPLE_DELETE:
                return "Multiple delete";
            case MULTIPLE_CREATE:
                return "Multiple create";
            case CALC_STATISTIC:
                return "Calculate selection statistic";
            default:
                return "not defined";
        }
    }
}