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

package org.radixware.kernel.common.builder.check.ads.clazz.presentations;

import java.text.MessageFormat;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsObjectTitleFormatDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import org.radixware.kernel.common.builder.check.ads.MessageFormatValidator;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;


@RadixObjectCheckerRegistration
public class AdsObjectTitleFormatChecker extends AdsDefinitionChecker<AdsObjectTitleFormatDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsObjectTitleFormatDef.class;
    }
    private static final String INVALID_FORMAT_ERROR = "Invalid title item format value";

    @Override
    public void check(AdsObjectTitleFormatDef format, IProblemHandler problemHandler) {
        super.check(format, problemHandler);
        CheckUtils.checkMLStringId(format, format.getNullValTitleId(), problemHandler, "title for <null> value");
        for (AdsObjectTitleFormatDef.TitleItem item : format.getItems()) {
            AdsPropertyDef prop = item.findProperty();
            if (prop == null) {
                final int idx = format.getItems().indexOf(item) + 1;
                final Id propId = item.getPropertyId();
                error(format, problemHandler, MessageFormat.format("Cannot find property referenced from title format item N{0}: #{1}", idx, propId));
            } else {
                AdsUtils.checkAccessibility(format, prop, false, problemHandler);
               // CheckUtils.checkExportedApiDatails(format, prop, problemHandler);
                EValType type = prop.getValue().getType().getTypeId();
                switch (type) {
                    case INT:
                    case NUM:
                    case STR:
                    case CHAR:
                    case DATE_TIME:
                    case CLOB:
                    case OBJECT:
                    case PARENT_REF:
                        if (item.isMultilingual()) {
                            CheckUtils.checkMLStringId(format, item.getPatternId(), problemHandler, "title format item pattern");
                            AdsLocalizingBundleDef bundle = format.findExistingLocalizingBundle();
                            if (bundle != null) {
                                AdsMultilingualStringDef string = bundle.getStrings().findById(item.getPatternId(), EScope.LOCAL_AND_OVERWRITE).get();
                                if (string != null) {
                                    List<AdsMultilingualStringDef.StringStorage> formats = string.getValues(EScope.LOCAL_AND_OVERWRITE);
                                    for (AdsMultilingualStringDef.StringStorage e : formats) {
                                        if (!MessageFormatValidator.formatIsValid(type, e.getValue())) {
                                            error(item, problemHandler, INVALID_FORMAT_ERROR + " in translation to language " + e.getLanguage().getName());
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!MessageFormatValidator.formatIsValid(type, item.getPattern())) {
                                error(item, problemHandler, INVALID_FORMAT_ERROR);
                            }
                        }
                        break;
                    default:
                        error(item, problemHandler, "Unsupported property type in title format item: " + type.getName());
                }
            }
        }
    }
}
