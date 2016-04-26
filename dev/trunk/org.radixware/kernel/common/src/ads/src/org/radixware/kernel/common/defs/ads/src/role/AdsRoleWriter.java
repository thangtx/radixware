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

package org.radixware.kernel.common.defs.ads.src.role;

import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.src.AbstractDefinitionWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.utils.CharOperations;


public class AdsRoleWriter extends AbstractDefinitionWriter<AdsRoleDef> {

    private static final char[] ROLE_PACKAGE_NAME = "org.radixware.kernel.server.meta.roles".toCharArray();
    private static final char[] ROLE_CLASS_NAME = CharOperations.merge(ROLE_PACKAGE_NAME, "RadRoleDef".toCharArray(), '.');
    private static final char[] ROLE_RESOURCE_CLASS_NAME = CharOperations.merge(ROLE_PACKAGE_NAME, "RadRoleResource".toCharArray(), '.');

    public AdsRoleWriter(JavaSourceSupport support, AdsRoleDef target, UsagePurpose usagePurpose) {
        super(support, target, usagePurpose);
    }

    @Override
    public boolean writeCode(CodePrinter printer) {
        if (def.isAppRole()) {
            WriterUtils.writeUserDefinitionHeader(printer, def);
        }
        return super.writeCode(printer);
    }

    /**
     * public RadRoleDef( final Id id, final String name, final Id titleId,
     * final Id[] ancestorIds, final Id[] apFamilyIds, final RadRoleResource[]
     * resources)
     *
     *
     */
    @Override
    protected boolean writeMeta(CodePrinter printer) {
        switch (usagePurpose.getEnvironment()) {
            case SERVER:
                WriterUtils.writePackageDeclaration(printer, def, usagePurpose);

                printer.println();
                WriterUtils.writeMetaShareabilityAnnotation(printer, def);
                printer.println();
                printer.print("public final class ");
                //writeUsage(printer);
                printer.print(def.getRuntimeId());
                printer.print(JavaSourceSupport.META_CLASS_SUFFIX);
                printer.enterBlock(1);
                printer.println('{');
                WriterUtils.writeServerArteAccessMethodDeclaration(def, JavaSourceSupport.CodeType.META, printer);
                printer.print("public static final ");
                printer.print(ROLE_CLASS_NAME);
                printer.print(" rdxMeta = new ");
                printer.print(ROLE_CLASS_NAME);
                printer.print('(');
                WriterUtils.writeReleaseAccessorInMetaClass(printer);
                printer.printComma();
                WriterUtils.writeIdUsage(printer, def.getId());
                printer.printComma();
                printer.enterBlock(5);
                printer.println();
                printer.printStringLiteral(def.getName());
                printer.printComma();
                printer.println();
                printer.printStringLiteral(def.getDescription());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdUsage(printer, def.getTitleId());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdArrayUsage(printer, def.collectOverwriteAncestors());
                printer.printComma();
                printer.println();
                WriterUtils.writeIdArrayUsage(printer, def.collectOverwriteAPF());
                printer.printComma();
                printer.println();
                new WriterUtils.SameObjectArrayWriter<AdsRoleDef.Resource>(ROLE_RESOURCE_CLASS_NAME) {

                    @Override
                    public void writeItemConstructorParams(CodePrinter printer, AdsRoleDef.Resource item) {
                        //public RadRoleResource(final EDrcResourceType type, final Id defId, final Id subDefId, final Restrictions restrictions)
                        WriterUtils.writeEnumFieldInvocation(printer, item.type);
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, item.defId);
                        printer.printComma();
                        WriterUtils.writeIdUsage(printer, item.subDefId);
                        printer.printComma();
                        writeCode(printer, item.restrictions);
                    }
                }.write(printer, def.collectOverwriteResource());
                printer.printComma();
                printer.println();
                printer.print(def.isAbstract());
                printer.printComma();
                printer.println();
                printer.print(def.isDeprecated());

                printer.leaveBlock(5);
                printer.println(')');
                printer.leaveBlock(1);
                printer.printlnSemicolon();
                printer.println('}');
                return true;
            default:
                return false;
        }
    }
}
