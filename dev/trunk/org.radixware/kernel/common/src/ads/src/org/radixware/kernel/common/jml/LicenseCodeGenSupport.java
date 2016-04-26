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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.license.CodeGenResult;
import org.radixware.kernel.license.ILicenseCodeGenerator;
import org.radixware.kernel.license.LicenseCodeGenerator;

public class LicenseCodeGenSupport {

    private static final String WRITE_ERROR_PATTERN = "org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.server.pdcTrace_____________________.mthD6XIGP2SRPNRDCISABIFNQAABA(%s, org.radixware.ads.mdlPEKYFVDRVZHGZCBQQDY2NOYFOY.common.acsTNLJBZADHTNRDIPGABQAQNO6EY.aci6WAUJTYZVPORDJHCAANE2UAFXA);";

    private String className;
    private byte[] sourceHash;
    private String currentFileName;
    private final List<String> fieldDeclarations = new ArrayList<>();
    private final ILicenseCodeGenerator helper = LicenseCodeGenerator.createInstance();
    private final Map<String, String> context2correlationId = new HashMap<>();
    private final MessageDigest messageDigest;

    public LicenseCodeGenSupport() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            throw new RadixError("Unable to create SHA-1 calculator", ex);
        }
    }

    public CodeGenResult generateRead(final String license, final int id, final int lineNumber, final Jml.Tag tag) {
        if (helper == null) {
            return null;
        }
        enterContext(tag);
        final CodeGenResult result = helper.generateRead(license, id, className, lineNumber, sourceHash);
        if (result.fieldDeclarations != null) {
            fieldDeclarations.addAll(Arrays.asList(result.fieldDeclarations));
        }
        context2correlationId.put(getReadKey(license, id), result.correlationId);
        return result;
    }

    public CodeGenResult generateCheck(final String license, final int id, final Jml.Tag tag) {
        if (helper == null) {
            return null;
        }
        enterContext(tag);
        final CodeGenResult result = helper.generateCheck(context2correlationId.get(getReadKey(license, id)), WRITE_ERROR_PATTERN);
        if (result.fieldDeclarations != null) {
            fieldDeclarations.addAll(Arrays.asList(result.fieldDeclarations));
        }
        return result;
    }

    public void flushFields(final CodePrinter printer) {
        if (!fieldDeclarations.isEmpty()) {
            for (String fieldDeclaration : fieldDeclarations) {
                printer.println("@SuppressWarnings(\"unused\") " + fieldDeclaration);
            }
            fieldDeclarations.clear();
        }
    }

    public static LicenseCodeGenSupport getOrCreate(final CodePrinter codePrinter) {
        LicenseCodeGenSupport support = (LicenseCodeGenSupport) codePrinter.getProperty(LicenseCodeGenSupport.class);
        if (support == null) {
            support = new LicenseCodeGenSupport();
        }
        codePrinter.putProperty(LicenseCodeGenSupport.class, support);
        return support;
    }

    public static LicenseCodeGenSupport get(final CodePrinter codePrinter) {
        return (LicenseCodeGenSupport) codePrinter.getProperty(LicenseCodeGenSupport.class);
    }

    private void enterContext(final Jml.Tag tag) {
        className = tag.getOwnerJml().getOwnerDef().findTopLevelDef().getQualifiedName();
        final File tagFile = tag.getFile();
        if (currentFileName == null || !currentFileName.equals(tagFile.getAbsolutePath())) {
            currentFileName = tagFile.getAbsolutePath();
            try {
                sourceHash = messageDigest.digest(Files.readAllBytes(Paths.get(tagFile.toURI())));
            } catch (IOException ex) {
                sourceHash = null;
            }
        }
    }

    private String getReadKey(final String license, final int id) {
        return license + "," + id;
    }
}
