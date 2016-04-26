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

package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsEventCodeDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;


public class JmlTagEventCode extends JmlTagLocalizedString {

    JmlTagEventCode(Id stringId, EType type) {
        super(stringId, type);
    }

    JmlTagEventCode(JmlType.Item.EventCode xEventCode) {
        super(Id.Factory.loadFrom(xEventCode.getStringId()), EType.getForValue(xEventCode.getType()));
    }

    JmlTagEventCode(JmlTagEventCode src) {
        super(src);
    }

    @Override
    public void appendTo(Item item) {
        Item.EventCode ev = item.addNewEventCode();
        ev.setStringId(stringId.toString());
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag event code={0}.{1}]", getBundleId(), stringId);
    }

    @Override
    public String getDisplayName() {
        AdsMultilingualStringDef string = findString();
        if (string != null) {
            String value = string.getValue(EIsoLanguage.ENGLISH);
            if (value == null) {
                value = "";
            }
            int index = value.indexOf("\n");
            if (index >= 0) {
                value = value.substring(0, index) + "...";
            }
            return MessageFormat.format("eventCode[\"{0}\"]", value.replace("\"", "\\\""));
        } else {
            return MessageFormat.format("eventCode[\"!!!String not found: {0}\"]", stringId.toString());
        }
    }
    
    public EEventSeverity getEventSeverity() {
        final AdsMultilingualStringDef event = findString();
        if (event instanceof AdsEventCodeDef) {
            return ((AdsEventCodeDef)event).getEventSeverity();
        }
        return null;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new CodeWriter(this, purpose) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        StringBuilder b = new StringBuilder(100);
                        b.append(getBundleId());
                        b.append('-');
                        b.append(stringId.toString());
                        printer.printStringLiteral(b.toString());
                        return true;
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }
    private static final Id eventSourceId = Id.Factory.loadFrom("acsTNLJBZADHTNRDIPGABQAQNO6EY");

    @SuppressWarnings("unchecked")
    public static void checkForEventDuplication(AdsEventCodeDef evCode, RadixObject context, IProblemHandler problemHandler, Jml.IHistory h) {

        AdsEnumDef eventSourceEnum = AdsSearcher.Factory.newAdsEnumSearcher(context.getModule()).findById(eventSourceId).get();
        if (eventSourceEnum != null) {
            AdsEnumItemDef item = eventSourceEnum.getItems().findByValue(evCode.getEventSource(), EScope.ALL);
            if (item == null) {
                problemHandler.accept(RadixProblem.Factory.newError(context, "Unknown event source \"" + evCode.getEventSource()));
            }
        }


        Object obj = h.getHistory().get("EVENT_CODE_MESSAGE_MAP");
        Map<String, RadixObject> knownMessages = null;

        if (obj instanceof Map) {

            knownMessages = (Map<String, RadixObject>) obj;
        }
        if (knownMessages == null) {
            knownMessages = new HashMap<>();
            h.getHistory().put("EVENT_CODE_MESSAGE_MAP", knownMessages);
        }


        for (EIsoLanguage l : evCode.getLanguages()) {
            String text = evCode.getValue(l) + "|" + String.valueOf(evCode.getEventSource()) + "|" + String.valueOf(evCode.getEventSeverity());
            RadixObject ec = knownMessages.get(text);
            if (ec != null && ec != context) {

                String location;
                if (ec.getOwnerDefinition() != null) {
                    location = ec.getOwnerDefinition().getQualifiedName();
                } else {
                    location = ec.getQualifiedName();
                }
                problemHandler.accept(RadixProblem.Factory.newWarning(context, "Event message for language \"" + l.getName() + "\" duplicates message from " + location));
            } else {
                knownMessages.put(text, context);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        super.check(problemHandler, h);
        AdsMultilingualStringDef string = findLocalizedString(stringId);
        if (string != null) {
            if (!(string instanceof AdsEventCodeDef)) {
                error(problemHandler, "Event code tag should refer to event code multilingual string");
            } else {
                EEventSeverity s = ((AdsEventCodeDef) string).getEventSeverity();
                if (s == null) {
                    error(problemHandler, "Undefined event severity");
                } else {
                    checkForEventDuplication((AdsEventCodeDef) string, this, problemHandler, h);
                }
            }
        }
    }
}
