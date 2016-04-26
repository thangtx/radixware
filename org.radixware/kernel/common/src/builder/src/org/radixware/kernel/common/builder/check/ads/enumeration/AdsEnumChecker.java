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

package org.radixware.kernel.common.builder.check.ads.enumeration;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.enumeration.ValueRanges;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformEnum;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.builder.check.ads.AdsDefinitionChecker;
import org.radixware.kernel.common.builder.check.ads.CheckUtils;
import static org.radixware.kernel.common.builder.check.common.RadixObjectChecker.error;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.builder.check.common.RadixObjectCheckerRegistration;
import org.radixware.kernel.common.defs.value.ValAsStr;


@RadixObjectCheckerRegistration
public class AdsEnumChecker extends AdsDefinitionChecker<AdsEnumDef> {

    @Override
    public Class<? extends RadixObject> getSupportedClass() {
        return AdsEnumDef.class;
    }

    @Override
    public void check(AdsEnumDef enumeration, IProblemHandler problemHandler) {
        super.check(enumeration, problemHandler);
        CheckUtils.checkMLStringId(enumeration, enumeration.getTitleId(), problemHandler, "title");

        if (enumeration.isPlatformEnumPublisher()) {
            final RadixPlatformEnum platformEnum = enumeration.findPublishedPlatformEnum();
            if (platformEnum == null) {
                error(enumeration, problemHandler, MessageFormat.format("Published platform enum {0} can not be found", enumeration.getPublishedPlatformEnumName()));
            } else {
                final HashMap<String, RadixPlatformEnum.Item> platformItems = new HashMap<String, RadixPlatformEnum.Item>();
                for (RadixPlatformEnum.Item item : platformEnum.getItems()) {
                    platformItems.put(item.name, item);
                }
                final List<String> foundNames = new ArrayList<String>();
                for (AdsEnumItemDef item : enumeration.getItems().getLocal()) {
                    if (item.isOverwrite() || item.isVirtual()) {
                        final RadixPlatformEnum.Item pi = platformItems.get(item.getPlatformItemName());
                        if (pi == null) {
                            error(item, problemHandler, MessageFormat.format("Platform enumeration item {0} referenced from {1} can not be found", item.getPlatformItemName(), item.getName()));
                        } else {
                            if (!Utils.equals(item.getValue(), pi.val)) {
                                error(item, problemHandler, MessageFormat.format("Value of item {0} differs from value of wrapped platform item {1}", item.getName(), item.getPlatformItemName()));
                            }
                            foundNames.add(item.getPlatformItemName());
                        }
                    } else {
                        if (!enumeration.isExtendable()) {
                            error(item, problemHandler, MessageFormat.format("Item {0} is not a platform enumeration item wrapper or overwrite", item.getName()));
                        }
                    }
                }
                for (final String name : foundNames) {
                    platformItems.remove(name);
                }
                for (final String name : platformItems.keySet()) {
                    String messageStr = MessageFormat.format("{0}", name);
                        if (!enumeration.isWarningSuppressed(AdsEnumDef.Problems.PLATFORM_ENUM_ITEM_IS_NOT_PUBLISHED)) {
                            warning(enumeration, problemHandler, AdsEnumDef.Problems.PLATFORM_ENUM_ITEM_IS_NOT_PUBLISHED, messageStr);
                        }
                }
            }
        } else {

            final AdsEnumDef overwritten = enumeration.getHierarchy().findOverwritten().get();

            final List<AdsEnumDef> ovrs = new ArrayList<AdsEnumDef>();
            AdsEnumDef ovre = overwritten;
            while (ovre != null) {
                ovrs.add(ovre);
                ovre = ovre.getHierarchy().findOverwritten().get();
            }
            if (!ovrs.isEmpty()) {
                final List<ValueRanges> checkRanges = new LinkedList<>();
                for (final AdsEnumDef ovr : ovrs) {
                    final ValueRanges ranges = ovr.getValueRanges();
                    if (!ranges.isEmpty() || ranges.getRegexp() != null) {
                        checkRanges.add(ranges);
                    }
                }

                final List<AdsEnumItemDef> locals = enumeration.getItems().getLocal().list();


                final String[] message = new String[1];
                for (AdsEnumItemDef item : locals) {
                    if (item.isOverwrite()) {
                        final AdsEnumItemDef ovr = item.getHierarchy().findOverwritten().get();
                        if (ovr == null) {
                            error(enumeration, problemHandler, MessageFormat.format("Overwritten item for {0} can not be found", item.getName()));
                        } else {
                            if (!Utils.equals(item.getValue(), ovr.getValue())) {
                                error(item, problemHandler, "Item value is not the same with value of overridden item");
                            }
                        }
                    } else if (item.isVirtual()) {
                        error(enumeration, problemHandler, MessageFormat.format("Virtual items are not allowed in regular enumerations: {0}", item.getName()));
                    } else {
                        if (checkRanges.isEmpty()) {//no element addition allowed
                            error(item, problemHandler, MessageFormat.format("Additional elements are not allowed for enumeration {0}, because no value ranges defined in overwritten enumeration(s). Remove this element", enumeration.getQualifiedName()));
                        } else {
                            for (final ValueRanges ranges : checkRanges) {
                                if (!ranges.checkValueAgainstRanges(item.getValue(), message, false)) {
                                    error(item, problemHandler, MessageFormat.format("Value of item {0} is out of restrictions defined by overwritten enumeration. Problem in " + message[0], item.getName()));
                                }
                            }
                        }
                    }
                }
            }
        }

        final ValueRanges ownRanges = enumeration.getValueRanges();

        if (!ownRanges.isEmpty() || ownRanges.getRegexp() != null) {
            final String[] message = new String[1];
            for (AdsEnumItemDef item : enumeration.getItems().getLocal()) {
                if (!item.isOverwrite() || item.isPlatformItemPublisher()) {
                    if (!ownRanges.checkValueAgainstRanges(item.getValue(), message, true)) {
                        error(item, problemHandler, "Item value is not in range, reserved for use in this enumeration. Matched by " + message[0]);
                    }
                }
            }
        }
//        else {
//            if (!enumeration.isFinal()) {
//                warning(enumeration, problemHandler, "Non-final enumeration must define at least one value range or regular expression (string based enums) for correct overwrite in higher layers");
//            }
//        }
        final HashMap<String, AdsEnumItemDef> name2item = new HashMap<>();

        final List<AdsEnumItemDef> allItems = enumeration.getItems().get(EScope.ALL);
        for (AdsEnumItemDef item : allItems) {
            final AdsEnumItemDef ex = name2item.get(item.getName());
            if (ex != null) {
                error(enumeration, problemHandler, MessageFormat.format("Item name duplication: {0} and {1}", item.getQualifiedName(), ex.getQualifiedName()));
            } else {
                name2item.put(item.getName(), item);
            }
        }
        final HashMap<ValAsStr, AdsEnumItemDef> val2item = new HashMap<>();
        for (AdsEnumItemDef item : allItems) {
            AdsEnumItemDef ex = val2item.get(item.getValue());
            if (ex != null) {
                error(enumeration, problemHandler, MessageFormat.format("Item value duplication: {0} and {1}", item.getQualifiedName(), ex.getQualifiedName()));
            } else {
                val2item.put(item.getValue(), item);
            }
        }
        final List<Id> ids = enumeration.getViewOrder().getOrderedItemIds();
        for (Id id : ids) {
            if (enumeration.getItems().findById(id, EScope.ALL).get() == null) {
                error(enumeration, problemHandler, "Unknown item in view order");
            }
        }
        if (allItems.isEmpty()) {
            error(enumeration, problemHandler, "Empty enumeration");
        }

        if (enumeration.isIdEnum()) {
            List<String> rejects = new LinkedList<String>();
            if (!enumeration.canBeIdEnum(rejects)) {
                for (String r : rejects) {
                    error(enumeration, problemHandler, r);
                }
            }
        }
    }
}
