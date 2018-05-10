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
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsPin;
import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsProgramObject;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;

public class JmlTagPin extends Jml.Tag {

    private Id id;

    public JmlTagPin(Id id) {
        super(null);
        this.id = id;
    }

    JmlTagPin(JmlType.Item.Pin xPin) {
        super(xPin);
        id = Id.Factory.loadFrom(xPin.getId());
    }

    public Id getId() {
        return id;
    }

    @Override
    public void appendTo(Item item) {
        Item.Pin xPin = item.addNewPin();
        appendTo(xPin);
        xPin.setId(id.toString());
    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag exit = {0}]", id.toString());
    }

    AdsPin findPin() {
        if (getOwnerJml().getOwnerDef() instanceof AdsProgramObject) {
            AdsProgramObject node = (AdsProgramObject) getOwnerJml().getOwnerDef();
            List<AdsPin> pins = node.getPins();
            for (int i = 0; i < pins.size(); i++) {
                AdsPin pin = pins.get(i);
                if (pin.getId().equals(id)) {
                    return pin;
                }
            }
        }
        return null;
    }

    @Override
    public String getDisplayName() {
        AdsPin pin = findPin();
        if (pin != null) {
            return rememberDisplayName(pin.toString());
        } else {
            return "unknown exit [" + restoreDisplayName(id.toString()) + "]";
        }
    }

    @Override
    public String getTypeTitle() {
        AdsPin pin = findPin();
        if (pin != null) {
            String name = "";
            for (char ch : getDisplayName().toCharArray()) {
                name += "&#" + Long.valueOf(ch);
            }
            return pin.getTypeTitle() + "'" + name + "'";
        } else {
            return "unknown exit [" + id.toString() + "]";
        }
        /*
         String name = getDisplayName();
         for (char ch: getDisplayName().toCharArray()) {
         name += "&#" + Long.valueOf(ch);
         if ('<' == ch)
         name += "&lt";
         else if ('>' == ch)
         name += "&gt";
         else
         name += ch;
         }
         return "Exit \'" + name + "\'";
         */
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new JmlTagWriter(this, purpose, JmlTagPin.this) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        try {
                            int index = indexOfPin();
                            if (index >= 0) {
                                printer.print(String.valueOf(index));
                                return true;
                            } else {
                                return false;
                            }
                        } finally {
                            WriterUtils.leaveHumanUnreadableBlock(printer);
                        }
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                    }
                };
            }
        };
    }

    private int indexOfPin() {
        if (getOwnerJml().getOwnerDef() instanceof AdsProgramObject) {
            AdsProgramObject node = (AdsProgramObject) getOwnerJml().getOwnerDef();
            List<AdsPin> pins = node.getSourcePins();
            for (int i = 0; i < pins.size(); i++) {
                AdsPin pin = pins.get(i);
                if (pin.getId().equals(getId())) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        if (indexOfPin() < 0) {
            error(problemHandler, MessageFormat.format("Referenced exit is not found: {0}", getId()));
        }
    }
}
