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

package org.radixware.kernel.designer.common.dialogs.choosetype;


public enum ETypeWizardMode {

    CHOOSE_TYPE_NATURE,
    CHOOSE_RADIX_TYPE,
    CHOOSE_ITEM_ARRAY_TYPE,
    CHOOSE_GENERIC_TYPE,
    CHOOSE_XML_TYPE,
    CHOOSE_JAVA_CLASS,
    CHOOSE_JAVA_TYPE,
    CHOOSE_RADIX_CLASS,
    CHOOSE_RADIX_ENUM,
    CHOOSE_RADIX_MODEL,
    EDIT_GENERIC_PARAMETERS,
    REFINE_TYPE;

    public static ETypeWizardMode getByTypeNature(ETypeNature nature) {
        switch (nature) {
            case JAVA_CLASS:
                return ETypeWizardMode.CHOOSE_JAVA_CLASS;
            case JAVA_PRIMITIVE:
                return ETypeWizardMode.CHOOSE_JAVA_TYPE;
            case RADIX_XML:
                return ETypeWizardMode.CHOOSE_XML_TYPE;
            case RADIX_CLASS:
                return ETypeWizardMode.CHOOSE_RADIX_CLASS;
            case RADIX_ENUM:
                return ETypeWizardMode.CHOOSE_RADIX_ENUM;
//            case RADIX_MODEL:
//                return ETypeWizardMode.CHOOSE_RADIX_MODEL;
            case JAVA_ARRAY:
                return ETypeWizardMode.CHOOSE_ITEM_ARRAY_TYPE;
            case TYPE_PARAMETER:
                return ETypeWizardMode.CHOOSE_GENERIC_TYPE;
            case RADIX_TYPE:
                return ETypeWizardMode.CHOOSE_RADIX_TYPE;
            default:
                return ETypeWizardMode.CHOOSE_TYPE_NATURE;
        }
    }
}