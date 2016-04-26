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

package org.radixware.kernel.common.defs.ads.clazz.enumeration;

import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsEnumClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsEnumClassFieldDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IterableWalker;


public final class AdsEnumClassFieldWriter extends AbstractDefinitionWriter<AdsEnumClassFieldDef> {

    public AdsEnumClassFieldWriter(JavaSourceSupport support, AdsEnumClassFieldDef field, UsagePurpose usagePurpose) {
        super(support, field, usagePurpose);
    }

    @Override
    public boolean writeExecutable(CodePrinter printer) {

        writeDeclaration(printer);
        final AdsClassDef embeddedClass = def.findEmbeddedClass(ExtendableDefinitions.EScope.LOCAL_AND_OVERWRITE).get();
        if (embeddedClass != null) {
            embeddedClass.getJavaSourceSupport().getCodeWriter(usagePurpose).writeCode(printer);
        }

        return true;
    }

    private void writeDeclaration(CodePrinter printer) {
        printer.print(def.getId());

        final List<Id> idPath = new LinkedList<>();
        def.getOwnerEnumClass().getNestedClassWalker().walk(new IterableWalker.Acceptor<AdsClassDef, Void>() {

            @Override
            public void accept(AdsClassDef object) {
                idPath.add(0, object.getId());
            }
        });

        final AdsPath adsPath = new AdsPath(idPath);
        final Definition resolve = adsPath.resolve(getSupport().getCurrentRoot()).get();

        if (resolve instanceof AdsEnumClassDef) {
            final List<AdsFieldParameterDef> params = ((AdsEnumClassDef) resolve).getFieldStruct().getOrdered();

            if (!params.isEmpty()) {
                printer.print('(');
                boolean first = true;
                for (final AdsFieldParameterDef param : params) {
                    if (!first) {
                        printer.printComma();
                    } else {
                        first = false;
                    }

                    WriterUtils.writeAdsValAsStr(def.getValueByParam(param), def.getValueController(param), this, printer);
                }
                printer.print(')');
            }
        }
    }
}
