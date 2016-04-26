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
package org.radixware.kernel.designer.dds.script.defs;

import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;

/**
 * {@linkplain DdsSequenceDef} Script Generator
 */
public class DdsSequenceScriptGenerator implements IDdsDefinitionScriptGenerator<DdsSequenceDef> {

    protected DdsSequenceScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsSequenceDef sqn) {
        cp.print("drop sequence ");
        cp.print(sqn.getDbName());
        cp.printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsSequenceDef oldSqn, DdsSequenceDef newSqn) {
        return false;
    }

    enum ESqnScriptType {

        CREATE, ALTER
    }

    private void getScript(CodePrinter cp, DdsSequenceDef sqn, ESqnScriptType mode) {
        final boolean isAlter = (mode == ESqnScriptType.ALTER);

        cp.print(isAlter ? "alter" : "create");
        cp.print(" sequence ");
        cp.print(sqn.getDbName());

        // increment by
        Long incrementBy = sqn.getIncrementBy();
        if (isAlter && incrementBy == null) {
            incrementBy = 1L;
        }
        if (incrementBy != null) {
            cp.print("\n\t");
            cp.print("increment by ");
            cp.print(incrementBy);
        }

        // start with
        if (!isAlter) {
            Long startWith = sqn.getStartWith();
            if (startWith != null) {
                cp.print("\n\t");
                cp.print("start with ");
                cp.print(startWith);
            }
        }

        // max value
        final Long maxValue = sqn.getMaxValue();
        if (maxValue != null) {
            cp.print("\n\t");
            cp.print("maxvalue ");
            cp.print(maxValue);
        } else if (isAlter) {
            cp.print("\n\t");
            cp.print("nomaxvalue");
        }

        final Long minValue = sqn.getMinValue();
        if (minValue != null) {
            cp.print("\n\t");
            cp.print("minvalue ");
            cp.print(minValue);
        } else if (isAlter) {
            cp.print("\n\t");
            cp.print("nominvalue");
        }

        if (sqn.isCycled()) {
            cp.print("\n\t");
            cp.print("cycle");
        } else if (isAlter) {
            cp.print("\n\t");
            cp.print("nocycle");
        }

        final Long cache = sqn.getCache();
        if (cache == null) {
            cp.print("\n\t");
            cp.print("nocache");
        } else if (isAlter || !cache.equals(20L)) {
            cp.print("\n\t");
            cp.print("cache ");
            cp.print(cache);
        }

        if (sqn.isOrdered()) {
            cp.print("\n\t");
            cp.print("order");
        } else if (isAlter) {
            cp.print("\n\t");
            cp.print("noorder");
        }

        cp.printCommandSeparator();
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsSequenceDef sqn, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(sqn, cp);
        }

        getScript(cp, sqn, ESqnScriptType.CREATE);
        getRunRoleScript(cp, sqn);
    }

    public boolean isStructureEquals(DdsSequenceDef oldSqn, DdsSequenceDef newSqn) {
        if (oldSqn.isCycled() != newSqn.isCycled()) {
            return false;
        }
        if (oldSqn.isOrdered() != newSqn.isOrdered()) {
            return false;
        }
        if (!Utils.equals(oldSqn.getCache(), newSqn.getCache())) {
            return false;
        }
        if (!Utils.equals(oldSqn.getIncrementBy(), newSqn.getIncrementBy())) {
            return false;
        }
        if (!Utils.equals(oldSqn.getMaxValue(), newSqn.getMaxValue())) {
            return false;
        }
        if (!Utils.equals(oldSqn.getMinValue(), newSqn.getMinValue())) {
            return false;
        }
        return true;
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsSequenceDef oldSqn, DdsSequenceDef newSqn) {
        final String oldDbName = oldSqn.getDbName();
        final String newDbName = newSqn.getDbName();
        boolean rename = !oldDbName.equals(newDbName);
        if (rename) {
            cp.print("rename ");
            cp.print(oldDbName);
            cp.print(" to ");
            cp.print(newDbName);
            cp.printCommandSeparator();
        }

        if (!isStructureEquals(oldSqn, newSqn)) {
            getScript(cp, newSqn, ESqnScriptType.ALTER);
        }
        if (rename) {
            getRunRoleScript(cp, newSqn);
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter cp, DdsSequenceDef sqn) {
        cp.print("grant select on ");
        cp.print(sqn.getDbName());
        cp.print(" to &USER&_RUN_ROLE");
        cp.printCommandSeparator();
    }

    public static final class Factory {

        private Factory() {
        }

        public static DdsSequenceScriptGenerator newInstance() {
            return new DdsSequenceScriptGenerator();
        }
    }
}
