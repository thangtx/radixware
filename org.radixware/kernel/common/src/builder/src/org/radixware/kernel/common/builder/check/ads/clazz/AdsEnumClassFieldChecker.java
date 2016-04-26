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

/*
 * 9/16/11 3:07 PM
 */
package org.radixware.kernel.common.builder.check.ads.clazz;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.Definitions;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.WrongFormatError;


@RadixObjectCheckerRegistration
public class AdsEnumClassFieldChecker extends AdsDefinitionChecker<AdsEnumClassFieldDef> {

    public AdsEnumClassFieldChecker() {
        super();
    }

    @Override
    public Class<AdsEnumClassFieldDef> getSupportedClass() {
        return AdsEnumClassFieldDef.class;
    }

    @Override
    public void check(AdsEnumClassFieldDef field, IProblemHandler problemHandler) {
        super.check(field, problemHandler);
        valuesCheck(field, problemHandler);
    }

    private void valuesCheck(AdsEnumClassFieldDef field, IProblemHandler problemHandler) {
        final AdsEnumClassDef def = field.getOwnerEnumClass();

        final Definitions<AdsFieldParameterDef> localParams = def.getFieldStruct().getLocal();
        final Definitions<AdsEnumClassFieldDef> localFields = def.getFields().getLocal();

        for (final AdsFieldParameterDef param : def.getFieldStruct().get(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE)) {
            final AdsValAsStr value = field.getValueByParam(param);
            if (value == AdsValAsStr.NULL_VALUE) {
                warning(field, problemHandler, "Value for parameter '" + param.getName() + "' not defined");

                if (localParams.contains(param) && !localFields.contains(field)) {
                    warning(field, problemHandler, "Required to rewrite field '" + field.getName() + "' to set value for parameter '" + param.getName() + "'");
                }
            }

            final EValType type = param.getValue().getType().getTypeId();
            switch (type) {
                case JAVA_TYPE:
                case JAVA_CLASS:
                case USER_CLASS:
                    break;
                default:
                    try {
                        if (value.getValueType() == AdsValAsStr.EValueType.VAL_AS_STR) {
                            value.getValAsStr().toObject(type);
                        }
                    } catch (WrongFormatError e) {
                        error(field, problemHandler, "Value of field '" + field.getName() + "' for parameter '" + param.getName() + "' have wrong format");
                    } catch (Exception e) {
                        Logger.getLogger(AdsEnumClassFieldChecker.class.getName()).log(Level.WARNING, null, e);
                    }
            }
        }
    }
}