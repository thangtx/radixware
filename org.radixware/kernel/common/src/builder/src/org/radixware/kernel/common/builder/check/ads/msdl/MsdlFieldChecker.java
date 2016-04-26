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
package org.radixware.kernel.common.builder.check.ads.msdl;

import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.common.RadixObjectChecker;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.msdl.EFieldType;
import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.types.Id;

@RadixObjectCheckerRegistration
public class MsdlFieldChecker extends RadixObjectChecker<MsdlField> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return MsdlField.class;
    }

    @Override
    public void check(MsdlField field, IProblemHandler problemHandler) {
        super.check(field, problemHandler);
        field.check(problemHandler);
        Id enumId = field.getReferncedEnumId();
        if (enumId != null) {
            Definition possibleEnum = field.findReferencedEnum();
            if (possibleEnum instanceof AdsEnumDef) {
                final AdsEnumDef enumeration = (AdsEnumDef) possibleEnum;
                if (field.getType() == EFieldType.INT && enumeration.getItemType() != EValType.INT) {
                    error(field, problemHandler, "Invalid type of referenced enumeration " + enumeration.getQualifiedName() + ": " + enumeration.getItemType().getName());
                }
                if (field.getType() == EFieldType.STR && enumeration.getItemType() == EValType.INT) {
                    error(field, problemHandler, "Invalid type of referenced enumeration " + enumeration.getQualifiedName() + ": " + enumeration.getItemType().getName());
                }
                AdsUtils.checkAccessibility(field, enumeration, false, problemHandler);
                CheckUtils.checkExportedApiDatails(field, enumeration, problemHandler);
            } else {
                error(field, problemHandler, "Can not find referenced enumeration #" + enumId);
            }
        }
    }
}
