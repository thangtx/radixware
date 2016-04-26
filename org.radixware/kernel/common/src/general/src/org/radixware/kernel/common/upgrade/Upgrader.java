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

package org.radixware.kernel.common.upgrade;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.fs.IRepositoryDefinition;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.common.utils.events.RadixEventSource;


public class Upgrader {

    private static final UpgraderSearcher upgraderSearcher = new UpgraderSearcher();
    private static final Object lock = new Object();
    private static final RadixEventSource firstStepSupport = new RadixEventSource();
    private static final Set<File> upgradedFiles = new HashSet<File>();

    private static org.w3c.dom.Element findRootElement(XmlObject xmlObject) {
        final org.w3c.dom.Element result = XmlUtils.findFirstElement(xmlObject.getDomNode());
        return result;
    }

    private static final int getVersion(org.w3c.dom.Element root) throws NumberFormatException {
        final Integer formatVersion = XmlUtils.getInteger(root, "FormatVersion");
        return (formatVersion != null ? formatVersion.intValue() : 0);
    }

    private static void upgrade(IRepositoryDefinition repository, org.w3c.dom.Element root) throws XmlException, IOException {
        synchronized (lock) {
            final IRadixObjectUpgraderFactory upgraderFactory = upgraderSearcher.findUpgraderFactory(root);

            if (upgraderFactory == null) {
                return;
            }

            final int actualVersion = upgraderFactory.getActualVersion();
            final int fileVersion;
            try {
                fileVersion = getVersion(root);
            } catch (NumberFormatException cause) {
                throw new IOException("Invalid format version of file '" + repository.getPath() + "'.", cause);
            }
            if (fileVersion > actualVersion) {
                throw new IOException("Unsupported file format version. File: '" + repository.getPath() + "', version: " + fileVersion + ", supported version: " + actualVersion + ".");
            }
            if (fileVersion == actualVersion) {
                return;
            }

            firstStepSupport.fireEvent(new RadixEvent()); // to designer, to run upgrade(branch) on separate thread;

            final File file = repository.getFile();
            if (file != null) {
                upgradedFiles.add(file);
            }

            for (int i = fileVersion + 1; i <= actualVersion; i++) {
                final IRadixObjectUpgrader upgrader = upgraderSearcher.getUpgrader(upgraderFactory, i);
                upgrader.firstStep(root);
            }
        }

    }

    public static XmlObject loadFromRepository(IRepositoryDefinition repository) throws XmlException, IOException {

        try (InputStream stream = repository.getData()) {
            final XmlObject xmlObject = XmlObject.Factory.parse(stream);
            final org.w3c.dom.Element root = findRootElement(xmlObject);

            if (root != null) {
                upgrade(repository, root);
            }
            return xmlObject;
        }
    }

    public static void upgrade(final Branch branch) {
        // load whole branch, not synchronized, to prevent deadlock on lock object
        final Set<RadixObject> radixObjects = RadixObjectsUtils.collectAllInside(Collections.singleton(branch), VisitorProviderFactory.createDefaultVisitorProvider());

        synchronized (lock) {
            final List<IRadixObjectUpgrader> processedUpgraders = upgraderSearcher.getAndRemoveProcessedUpgraders();

            for (IRadixObjectUpgrader upgrader : processedUpgraders) {
                for (RadixObject radixObject : radixObjects) {
                    if (radixObject.isInBranch()) {
                        if (radixObject.isSaveable()) {
                            final File file = radixObject.getFile();
                            if (upgradedFiles.contains(file)) {
                                radixObject.setEditState(RadixObject.EEditState.MODIFIED);
                            }
                        }

                        upgrader.finalStep(radixObject);
                    }
                }
            }
            
            upgradedFiles.clear();
        }
    }

    public static void addFirstStepListener(IRadixEventListener listener) {
        firstStepSupport.addEventListener(listener);
    }

    public static void removeFirstStepListener(IRadixEventListener listener) {
        firstStepSupport.removeEventListener(listener);
    }
}
