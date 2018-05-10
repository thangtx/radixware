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
package org.radixware.kernel.designer.dds.script.defs.postgresenterprise;

import org.radixware.kernel.common.defs.dds.DdsSequenceDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.dds.script.DdsScriptGeneratorUtils;
import org.radixware.kernel.designer.dds.script.IDdsDefinitionScriptGenerator;
import org.radixware.kernel.designer.dds.script.IScriptGenerationHandler;
import org.radixware.kernel.designer.dds.script.DdsScriptInternalUtils;

/**
 * {@linkplain DdsSequenceDef} Script Generator
 */
public class EdbDdsSequenceScriptGenerator implements IDdsDefinitionScriptGenerator<DdsSequenceDef> {

    protected EdbDdsSequenceScriptGenerator() {
    }

    @Override
    public void getDropScript(CodePrinter cp, DdsSequenceDef sqn) {
        cp.print("drop sequence ").print(sqn.getDbName()).printCommandSeparator();
    }

    @Override
    public boolean isModifiedToDrop(DdsSequenceDef oldSqn, DdsSequenceDef newSqn) {
        return false;
    }

    @Override
    public void getReCreateScript(CodePrinter printer, DdsSequenceDef definition, boolean storeData) {
    }

    @Override
    public void getEnableDisableScript(CodePrinter cp, DdsSequenceDef definition, boolean enable) {
    }

    @Override
    public void getCreateScript(CodePrinter cp, DdsSequenceDef sqn, IScriptGenerationHandler handler) {
        if (handler != null) {
            handler.onGenerationStarted(sqn, cp);
        }

        cp.print("create sequence ").print(sqn.getDbName());

        // start with
        Long startWith = sqn.getStartWith();
        if (startWith != null) {
            cp.print("\n\tstart with ").print(startWith);
        }

        // increment by
        Long incrementBy = sqn.getIncrementBy();
        if (incrementBy != null) {
            cp.print("\n\tincrement by ").print(incrementBy);
        }

        // min value
        final Long minValue = sqn.getMinValue();
        if (minValue != null) {
            cp.print("\n\tminvalue ").print(minValue);
        }

        
        // max value
        final Long maxValue = sqn.getMaxValue();
        if (maxValue != null) {
            cp.print("\n\tmaxvalue ").print(maxValue);
        }

        if (sqn.isCycled()) {
            cp.print("\n\tcycle");
        }

        final Long cache = sqn.getCache();
        if (cache == null) {
            cp.print("\n\tnocache");
        } else {
            cp.print("\n\tcache ").print(cache);
        }

        cp.printCommandSeparator();
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
        return Utils.equals(oldSqn.getMinValue(), newSqn.getMinValue());
    }

    @Override
    public void getAlterScript(CodePrinter cp, DdsSequenceDef oldSqn, DdsSequenceDef newSqn) {
        final String oldDbName = oldSqn.getDbName();
        final String newDbName = newSqn.getDbName();
        final boolean rename = !oldDbName.equals(newDbName);
        
        if (rename) {
            cp.print("alter sequence ").print(oldDbName).print(" rename to ").print(newDbName).printCommandSeparator();
        }

        if (!isStructureEquals(oldSqn, newSqn)) {
            cp.print("alter sequence ").print(newSqn.getDbName());

            // increment by
            Long incrementBy = newSqn.getIncrementBy();
            cp.print("\n\tincrement by ").print(DdsScriptInternalUtils.nvl(incrementBy,1L));

            // min value
            final Long minValue = newSqn.getMinValue();
            if (minValue != null) {
                cp.print("\n\tminvalue ").print(minValue);
            } else {
                cp.print("\n\tnominvalue");
            }
            
            // max value
            final Long maxValue = newSqn.getMaxValue();
            if (maxValue != null) {
                cp.print("\n\tmaxvalue ").print(maxValue);
            } else {
                cp.print("\n\tnomaxvalue");
            }


            if (newSqn.isCycled()) {
                cp.print("\n\tcycle");
            } else {
                cp.print("\n\tnocycle");
            }

            final Long cache = newSqn.getCache();
            if (cache == null) {
                cp.print("\n\tnocache");
            } else {
                cp.print("\n\tcache ").print(cache);
            }
            cp.printCommandSeparator();
        }
        if (rename) {
            getRunRoleScript(cp, newSqn);
        }
    }

    @Override
    public void getRunRoleScript(CodePrinter cp, DdsSequenceDef sqn) {
        cp.print("grant select on ").print(sqn.getDbName()).print(" to &USER&_RUN_ROLE").printCommandSeparator();
    }

    public static final class Factory {

        private Factory() {
        }

        public static EdbDdsSequenceScriptGenerator newInstance() {
            return new EdbDdsSequenceScriptGenerator();
        }
    }
}
