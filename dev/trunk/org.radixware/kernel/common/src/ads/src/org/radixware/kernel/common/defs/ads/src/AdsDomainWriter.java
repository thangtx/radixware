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

import java.util.ArrayList;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.meta.RadDomainDef;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.types.Id;


public class AdsDomainWriter extends AbstractDefinitionWriter<AdsDomainDef> {

    public AdsDomainWriter(JavaSourceSupport support, AdsDomainDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }
    private static final char[] DOMAIN_CLASS_NAME = RadDomainDef.class.getName().toCharArray();

    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case COMMON:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);
                
                WriterUtils.writeMetaShareabilityAnnotation(printer, def);
                printer.println();
                
                printer.print("public final class ");
                printer.print(def.getId());
                printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
                printer.println("{");
                printer.enterBlock();
                printer.print("public static final ");
                printer.print(DOMAIN_CLASS_NAME);
                printer.print(" rdxMeta = new ");
                printer.print(DOMAIN_CLASS_NAME);
                printer.print("(");
                /**
                 * public RadDomainDef(
                final Id           id,
                final Id           ownerDomainId,
                final Id[]         innerDomainIds,
                final String       name,
                final Id           titleOwnerId,
                final Id           titleId
                )
                 */
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                AdsDomainDef owner = def.getOwnerDomain();
                if (owner != null) {
                    WriterUtils.writeIdUsage(printer, owner.getId());
                } else {
                    WriterUtils.writeNull(printer);
                }
                printer.printComma();
                ArrayList<Id> ids = new ArrayList<Id>();
                AdsDomainDef domain = def;
                while (domain != null) {
                    for (AdsDomainDef child : domain.getChildDomains()) {
                        ids.add(child.getId());
                    }
                    domain = (AdsDomainDef) domain.getHierarchy().findOverwritten().get();
                }
                WriterUtils.writeIdArrayUsage(printer, ids);
                printer.printComma();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.findTopLevelDef().getId());
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getTitleId());
                printer.print(")");
                printer.printlnSemicolon();

                printer.leaveBlock();
                printer.println("}");
                return true;
            default:
                return false;
        }
    }
}
