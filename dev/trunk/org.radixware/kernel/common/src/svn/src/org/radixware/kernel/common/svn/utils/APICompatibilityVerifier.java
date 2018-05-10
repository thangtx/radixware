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
package org.radixware.kernel.common.svn.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.radixware.kernel.common.dialogs.AuthenticationCancelledException;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVN;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.client.SvnEntry;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.schemas.product.BinaryCompatibilityOptions;
import org.radixware.schemas.product.LayerDocument;

public class APICompatibilityVerifier {

    public static void main(String[] args) throws RadixSvnException, AuthenticationCancelledException {

        if (args.length < 5) {
            System.err.println("Usage:");
            System.err.println(" <svn repository url> <svn user name> <svn keyfile path> <product release path (inside repository)> <radix release path (inside repository)>");
            return;
        }

        String repositoryURL = args[0];
        String svnUserName = args[1];
        String keyFilePath = args[2];
        final String productReleaseUrl = args[3];
        final String radixReleaseUrl = args[4];

        SVNRepositoryAdapter repository = new SVNRepositoryAdapter.Builder().addSSH(svnUserName, keyFilePath, null).build(repositoryURL, "/");

        final List<String> candidates = new LinkedList<>();
        repository.getDir(productReleaseUrl, -1, new SVNRepositoryAdapter.EntryHandler() {

            @Override
            public void accept(SvnEntry svnde) throws RadixSvnException {
                if (svnde.getKind() == SvnEntry.Kind.DIRECTORY) {
                    if ("org.radixware".equals(svnde.getName())) {
                        return;
                    }
                    candidates.add(SvnPath.append(productReleaseUrl, svnde.getName()));
                }
            }
        });

        final List<String> layers = new LinkedList<>();
        for (final String candidate : candidates) {

            repository.getDir(candidate, -1, new SVNRepositoryAdapter.EntryHandler() {

                @Override
                public void accept(SvnEntry svnde) throws RadixSvnException {
                    if (svnde.getKind() == SvnEntry.Kind.FILE && "layer.xml".equals(svnde.getName())) {
                        layers.add(candidate);
                    }
                }
            });
        }
        List<String> layerPathList = layers;

        layerPathList.add(SvnPath.append(radixReleaseUrl, "org.radixware"));

        final List<String> errorsSummary = new LinkedList<>();

        final Map<String, List<String>> layer2PackageMap = new HashMap<>();
        for (String layerPath : layerPathList) {
            try {

                final String layerXmlFile = SvnPath.append(layerPath, "layer.xml");
                byte[] bytes = SVN.getFile(repository, layerXmlFile, -1);
                if (bytes == null) {
                    continue;
                }
                LayerDocument xDoc = LayerDocument.Factory.parse(new ByteArrayInputStream(bytes));
                BinaryCompatibilityOptions xOpts = xDoc.getLayer().getBinaryCompatibilityOptions();
                if (xOpts != null && xOpts.getIgnorePackages() != null) {
                    for (BinaryCompatibilityOptions.IgnorePackages.Package xPkg : xOpts.getIgnorePackages().getPackageList()) {
                        List<String> list = layer2PackageMap.get(xDoc.getLayer().getUri());
                        if (list == null) {
                            list = new LinkedList<>();
                            layer2PackageMap.put(xDoc.getLayer().getUri(), list);
                        }
                        list.add(xPkg.getValue());
                    }
                }
            } catch (Throwable e) {
            }
        }

        if (layer2PackageMap.isEmpty()) {
            System.out.println("No exceptional packages detected. Will report all errors");
        } else {
            System.out.println("Following exceptional package name prefixes detected (grouped by layer): ");
            for (Map.Entry<String, List<String>> e : layer2PackageMap.entrySet()) {
                System.out.println(e.getKey());
                for (String s : e.getValue()) {
                    System.out.println("   " + s);
                }
            }
            System.out.println("Errors on binding with classes, whose package name starts from on of names listed above, will be treated as warnings and may only appears in output");
        }

        final BranchHolderParams branchParams = new BranchHolderParams(repository, layerPathList, null, null, true, null);
        PatchClassFileLinkageVerifier verifier = new PatchClassFileLinkageVerifier(branchParams, false) {
            @Override
            public void error(Exception e) {
                e.printStackTrace(System.err);
                ByteArrayOutputStream bout = new ByteArrayOutputStream();
                PrintStream ps;
                try {
                    ps = new PrintStream(bout, false, "UTF-8");
                    e.printStackTrace(ps);
                    errorsSummary.add(new String(bout.toByteArray(), "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                }
            }

            protected boolean isWarningWhenClassMissing(String className) {
                for (String layer : layer2PackageMap.keySet()) {
                    List<String> exceptions = layer2PackageMap.get(layer);
                    for (String e : exceptions) {
                        if (className.startsWith(e)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public void error(String message) {
                System.err.println(message);
                errorsSummary.add(message);
            }

            @Override
            public void message(String message) {
                System.out.println(message);
            }
        };
        if (!verifier.verify()) {
            System.err.println("VALIDATION FAILED");
            System.err.println("Validated layers are:");
            for (String layer : layers) {
                System.err.println("    " + layer);
            }
            System.err.println("Following errors were found:");
            for (String error : errorsSummary) {
                System.err.println(error);
            }
            System.exit(1);
        }
        System.err.println("VALIDATION SUCCESSFULL");
    }
}
