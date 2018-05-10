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

package org.radixware.kernel.common.defs.ads.src;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.ads.common.AdsCondition;
import org.radixware.kernel.common.defs.ads.common.Prop2ValueMap;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.Sqml;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsConditionWriter extends RadixObjectWriter<AdsCondition> {

    public AdsConditionWriter(JavaSourceSupport support, AdsCondition target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    private static final char[] CONDITION_META_SERVER_CLASS_NAME = CharOperations.merge(WriterUtils.PRESENTATIONS_META_SERVER_PACKAGE_NAME, "RadConditionDef".toCharArray(), '.');

    @Override
    public boolean writeCode(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                /**
                 * public RadConditionDef( final String condition, final String
                 * conditionFrom )
                 */
                printer.print("new ");
                printer.print(CONDITION_META_SERVER_CLASS_NAME);
                printer.print('(');
                WriterUtils.writeSqmlAsXmlStr(printer, checkItem(def.getWhere()));
                printer.printComma();
                WriterUtils.writeSqmlAsXmlStr(printer, def.getFrom());
                if (def.getProp2ValueMap() != null && !def.getProp2ValueMap().getItems().isEmpty()) {
                    printer.printComma();
                    printer.print("new ");
                    printer.print(CONDITION_META_SERVER_CLASS_NAME);
                    printer.print('.');
                    printer.print("Prop2ValueCondition");
                    printer.print('(');
                    List<Id> ids = new LinkedList<>();
                    List<String> values = new LinkedList<>();
                    for (Prop2ValueMap.Prop2ValMapItem item : def.getProp2ValueMap().getItems()) {
                        ids.add(item.getPropertyId());
                        values.add(item.getStringValue());
                    }
                    WriterUtils.writeIdArrayUsage(printer, ids);
                    printer.printComma();
                    WriterUtils.writeStringArrayUsage(printer, values, true, true);
                    printer.print(')');
                }
                printer.printComma();
                printer.printStringLiteral(def.getLayer().getURI());
                printer.print(')');
                return true;
            default:
                return false;
        }
    }

    @Override
    public void writeUsage(CodePrinter printer) {
        //dont use in code directly
    }
    
    private Sqml checkItem(Sqml sqml){
        if (sqml == null || sqml.getItems() == null){
            return null;
        }
        boolean isScmlEmpty = true;
        for (Scml.Item item : sqml.getItems()) {
            if (item instanceof Scml.Text) {
                final String text = ((Scml.Text) item).getText().trim();
                if (!text.isEmpty()){
                    isScmlEmpty = false;
                    break;
                }
            } else {
                isScmlEmpty = false;
                break;
            }
        }
        
        return isScmlEmpty ? null: sqml;
    }
}
