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

import java.math.BigInteger;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.jml.Jml.IHistory;
import org.radixware.kernel.common.jml.JmlTagReadLicense.LicenseCodeWriter;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.license.CodeGenResult;
import org.radixware.schemas.xscml.JmlType.Item;

public class JmlTagCheckLicense extends Jml.Tag {

    private String license = null;
    private int id = 0;

    protected JmlTagCheckLicense(Item.CheckLicense xItem) {
        this.license = xItem.getLicense();
        this.id = xItem.getId().intValue();
    }

    public JmlTagCheckLicense() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getLicense() {
        return license;
    }

    public int getId() {
        return id;
    }

    @Override
    public void appendTo(Item item) {
        Item.CheckLicense xCheckLicense = item.addNewCheckLicense();
        xCheckLicense.setId(BigInteger.valueOf(id));
        xCheckLicense.setLicense(license);
    }

    @Override
    public String getDisplayName() {
        return "CheckLicense[" + license + ", " + id + "];";
    }

    @Override
    public void check(IProblemHandler problemHandler, IHistory checker) {
        if (license == null || license.isEmpty()) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "License name is not defined"));
        }
        if (id == 0) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "Check ID is not defined"));
        }
        JmlTagReadLicense.checkTagIsInTheSeparateLine(this, problemHandler);
        checkCorrespondingReadIsPresent(problemHandler, checker);
    }

    private void checkCorrespondingReadIsPresent(final IProblemHandler problemHandler, final IHistory checker) {
        boolean correspondingReadFound = false;
        for (JmlTagReadLicense readTag : ((JmlTagReadLicense.LicenseTagsCheckContext) checker.getHistory().get(JmlTagReadLicense.LicenseTagsCheckContext.class)).readLisenceTags) {
            if (Utils.equals(license, readTag.getLicense()) && id == readTag.getId()) {
                correspondingReadFound = true;
                break;
            }
        }
        if (!correspondingReadFound) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "There is no corresponding read license tag"));
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {

            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new LicenseCodeWriter(this, purpose, JmlTagCheckLicense.this) {

                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        final LicenseCodeGenSupport licenseSupport = LicenseCodeGenSupport.getOrCreate(printer);
                        if (licenseSupport == null) {
                            return false;
                        }
                        final CodeGenResult generatedCode = licenseSupport.generateCheck(license, id, JmlTagCheckLicense.this);
                        if (generatedCode != null) {
                            for (String statement : generatedCode.statements) {
                                printer.print(statement);
                            }
                            printer.println();
                        }

                        return true;
                    }
                };
            }
        };
    }
}
