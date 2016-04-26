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

package org.radixware.kernel.common.utils.dist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.xmlbeans.XmlException;
import org.radixware.schemas.product.Patch;
import org.radixware.schemas.product.PatchDocument;
import org.radixware.schemas.product.Upgrade;
import org.radixware.schemas.product.UpgradeDocument;


public class DistributionUtils {

    public static final String UPGRADE_XML_FILE_NAME = "upgrade.xml";
    public static final String PATCH_XML_FILE_NAME = "patch.xml";

    public static class UpgradeInfo {

        public final EUpgradeFileKind upgradeKind;
        public final String baseReleaseVersion;
        public final String patchOrReleaseNumber;
        public final String clientURI;
        public final String description;
        public final Long distribVariant;
        public final Long prevDistribVariant;
        public final Long distribNumber;

        private UpgradeInfo(Patch xDef) {
            this.upgradeKind = EUpgradeFileKind.PATCH;
            this.baseReleaseVersion = xDef.getBaseRelease();
            this.patchOrReleaseNumber = xDef.getNumber();
            this.clientURI = xDef.getClientUri();
            this.description = null;
            this.distribVariant = null;
            this.prevDistribVariant = null;
            this.distribNumber = null;
        }

        private UpgradeInfo(Upgrade xDef) {
            this.upgradeKind = EUpgradeFileKind.UPGRADE;
            this.baseReleaseVersion = xDef.getPrevReleaseNumber();
            this.patchOrReleaseNumber = xDef.getReleaseNumber();
            this.clientURI = xDef.getClientUri();
            this.description = xDef.getDescription();
            this.distribVariant = xDef.getDistribVariant();
            this.prevDistribVariant = xDef.getPrevDistribVariant();
            this.distribNumber = xDef.getNumber();
        }

        private UpgradeInfo(EUpgradeFileKind upgradeKind) {
            this.upgradeKind = upgradeKind;
            this.baseReleaseVersion = null;
            this.patchOrReleaseNumber = null;
            this.clientURI = null;
            this.description = null;
            this.distribVariant = null;
            this.prevDistribVariant = null;
            this.distribNumber = null;
        }

        public String getShortDescription() {
            StringBuilder report = new StringBuilder();
            switch (upgradeKind) {
                case PATCH:
                    report.append("Patch #").
                            append(patchOrReleaseNumber).
                            append(" of release ").
                            append(baseReleaseVersion).
                            append(" for client ").
                            append(clientURI);
                    break;
                case UPGRADE:
                    report.append("Upgrade by distributive #").
                            append(distribNumber);
                    if (distribVariant != null) {
                        report.append("v").
                                append(distribVariant);
                    }
                    report.append(" from release ").
                            append(baseReleaseVersion).
                            append(" to release ").
                            append(patchOrReleaseNumber);
                    break;
                default:
                    report.append("<Invalid Upgrade>");
            }
            return report.toString();
        }
    }
    public static final UpgradeInfo INVALID_UPGRADE = new UpgradeInfo(EUpgradeFileKind.INVALID);

    public enum EUpgradeFileKind {

        UPGRADE,
        PATCH,
        INVALID
    }

    public static EUpgradeFileKind getUpgradeFileKind(File file) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
            ZipEntry e = zip.getEntry(UPGRADE_XML_FILE_NAME);
            if (e != null && !e.isDirectory()) {
                return EUpgradeFileKind.UPGRADE;
            }
            e = zip.getEntry(PATCH_XML_FILE_NAME);
            if (e != null && !e.isDirectory()) {
                return EUpgradeFileKind.PATCH;
            }
            return EUpgradeFileKind.INVALID;
        } catch (IOException ex) {
            return EUpgradeFileKind.INVALID;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException ex) {
                    Logger.getLogger(DistributionUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    public static UpgradeInfo getUpgradeFileDescription(File file) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
            ZipEntry e = zip.getEntry(UPGRADE_XML_FILE_NAME);
            if (e != null && !e.isDirectory()) {
                InputStream stream = null;
                try {
                    stream = zip.getInputStream(e);
                    return getNormalUpgradeDescription(stream);
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
            }
            e = zip.getEntry(PATCH_XML_FILE_NAME);
            if (e != null && !e.isDirectory()) {
                InputStream stream = null;
                try {
                    stream = zip.getInputStream(e);
                    return getPatchDescription(stream);
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
            }
            return INVALID_UPGRADE;
        } catch (IOException ex) {
            return INVALID_UPGRADE;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException ex) {
                    Logger.getLogger(DistributionUtils.class.getName()).log(Level.FINE, ex.getMessage(), ex);
                }
            }
        }
    }

    private static UpgradeInfo getPatchDescription(InputStream stream) {
        try {
            PatchDocument xDoc = PatchDocument.Factory.parse(stream);
            Patch xDef = xDoc.getPatch();
            if (xDef == null) {
                return INVALID_UPGRADE;
            }
            return new UpgradeInfo(xDef);
        } catch (XmlException ex) {
            return INVALID_UPGRADE;
        } catch (IOException ex) {
            return INVALID_UPGRADE;
        }
    }

    private static UpgradeInfo getNormalUpgradeDescription(InputStream stream) {
        try {
            UpgradeDocument xDoc = UpgradeDocument.Factory.parse(stream);
            Upgrade xDef = xDoc.getUpgrade();
            if (xDef == null) {
                return INVALID_UPGRADE;
            }
            return new UpgradeInfo(xDef);
        } catch (XmlException ex) {
            return INVALID_UPGRADE;
        } catch (IOException ex) {
            return INVALID_UPGRADE;
        }
    }
}
