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
import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.src.WriterUtils;
import org.radixware.kernel.common.jml.Jml.IHistory;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.license.CodeGenResult;
import org.radixware.schemas.xscml.JmlType.Item;

public class JmlTagReadLicense extends Jml.Tag {

    protected JmlTagReadLicense(final Item.ReadLicense xItem) {
        super(xItem);
        this.license = xItem.getLicense();
        this.id = xItem.getId().intValue();
    }

    public JmlTagReadLicense() {
        super(null);
    }
    private String license = null;
    private int id = 0;

    public void setId(int id) {
        this.id = id;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getId() {
        return id;
    }

    public String getLicense() {
        return license;
    }

    @Override
    public void appendTo(Item item) {
        Item.ReadLicense xReadLicense = item.addNewReadLicense();        
        xReadLicense.setId(BigInteger.valueOf(id));
        xReadLicense.setLicense(license);
    }

    @Override
    public String getDisplayName() {
        return "ReadLicense[" + license + ", " + id + "];";
    }

    @Override
    public void check(IProblemHandler problemHandler, IHistory checker) {
        if (license == null || license.isEmpty()) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "License name is not defined"));
        }
        if (id == 0) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "Read ID is not defined"));
        }
        checkTagIsInTheSeparateLine(this, problemHandler);
        final LicenseTagsCheckContext checkContext = (LicenseTagsCheckContext) checker.getHistory().get(LicenseTagsCheckContext.class);
        for (JmlTagReadLicense prevRead : checkContext.readLisenceTags) {
            if (Utils.equals(license, prevRead.getLicense()) && (id == prevRead.getId())) {
                problemHandler.accept(RadixProblem.Factory.newError(this, "Read license tag for the same license and with the same ID is already exists"));
                break;//report error only once
            }
        }
        boolean checkExists = false;
        for (Scml.Item item : getOwnerJml().getItems()) {
            if (item instanceof JmlTagCheckLicense) {
                final JmlTagCheckLicense checkTag = (JmlTagCheckLicense) item;
                if (license.equals(checkTag.getLicense())) {
                    checkExists = true;
                    break;
                }
            }
        }
        if (!checkExists) {
            problemHandler.accept(RadixProblem.Factory.newWarning(this, "There is no corresponding check tag"));
        }
        if (!getLayer().getLicenses().contains(license)) {
            problemHandler.accept(RadixProblem.Factory.newError(this, "There is no license  '" + license + "' in layer '" + getLayer().getURI() + "'"));
        }
        checkContext.readLisenceTags.add(this);
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new LicenseCodeWriter(this, purpose, JmlTagReadLicense.this) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        final LicenseCodeGenSupport licenseSupport = LicenseCodeGenSupport.getOrCreate(printer);
                        if (licenseSupport == null) {
                            return false;
                        }
                        super.writeCode(printer);
                        WriterUtils.enterHumanUnreadableBlock(printer);
                        final int currentLineNumber = getThisLineNumber(printer);
                        final CodeGenResult generatedCode = licenseSupport.generateRead(license, id, currentLineNumber, JmlTagReadLicense.this);
                        if (generatedCode != null) {
                            for (String statement : generatedCode.statements) {
                                printer.print(statement);
                            }
                            printer.println();
                        }
                        WriterUtils.leaveHumanUnreadableBlock(printer);
                        return true;
                    }
                };
            }
        };
    }

    protected static void checkTagIsInTheSeparateLine(final Scml.Tag tag, final IProblemHandler problemHandler) {
        final Scml scml = tag.getOwnerScml();
        for (int i = 0; i < scml.getItems().size(); i++) {
            if (scml.getItems().get(i) == tag) {
                int idx = i - 1;
                //check that it is new line or start of file from the left side
                while (idx >= 0) {
                    final Scml.Item prevItem = scml.getItems().get(idx);
                    if (prevItem instanceof Scml.Tag) {
                        reportSeparateLineNeeded(tag, problemHandler);
                        return;
                    } else {
                        final String text = ((Scml.Text) prevItem).getText();
                        boolean ok = false;
                        for (int j = text.length() - 1; j >= 0; j--) {
                            if (text.charAt(j) == '\n') {
                                ok = true;
                                break;
                            }
                            if (!Character.isWhitespace(text.charAt(j))) {
                                reportSeparateLineNeeded(tag, problemHandler);
                                return;
                            }
                        }
                        if (ok) {
                            break;
                        }
                    }
                    idx--;
                }
                idx = i + 1;
                //check that it is new line or start of file from the right side
                while (idx < scml.getItems().size()) {
                    final Scml.Item nextItem = scml.getItems().get(idx);
                    if (nextItem instanceof Scml.Tag) {
                        reportSeparateLineNeeded(tag, problemHandler);
                        return;
                    } else {
                        final String text = ((Scml.Text) nextItem).getText();
                        boolean ok = false;
                        for (int j = 0; j < text.length(); j++) {
                            if (text.charAt(j) == '\n') {
                                ok = true;
                                break;
                            }
                            if (!Character.isWhitespace(text.charAt(j))) {
                                reportSeparateLineNeeded(tag, problemHandler);
                                return;
                            }
                        }
                        if (ok) {
                            break;
                        }
                    }
                    idx++;
                }
            }
        }
    }

    private static void reportSeparateLineNeeded(final Scml.Tag tag, final IProblemHandler problemHandler) {
        problemHandler.accept(RadixProblem.Factory.newError(tag, "License tag should be placed in the separate line"));
    }

    protected static abstract class LicenseCodeWriter extends JmlTagWriter {

        public LicenseCodeWriter(JavaSourceSupport support, UsagePurpose usagePurpose, final Jml.Tag tag) {
            super(support, usagePurpose, tag);
        }

        protected int getThisLineNumber(CodePrinter printer) {
            return printer.getLineNumber(printer.getContents().length);
        }

        @Override
        public void writeUsage(CodePrinter printer) {
            //do nothing
        }
    }

    public static class LicenseTagsCheckContext {

        public final List<JmlTagReadLicense> readLisenceTags = new ArrayList<>();
    }
}
