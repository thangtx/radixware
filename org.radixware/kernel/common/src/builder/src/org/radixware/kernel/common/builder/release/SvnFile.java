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
package org.radixware.kernel.common.builder.release;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.build.directory.DigestWriter;
import org.radixware.kernel.common.build.directory.DirectoryFileSigner;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnConnectionType;
import org.radixware.kernel.common.svn.client.SvnPath;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.common.utils.Hex;

class SvnFile {

    protected SvnDir parent;
    protected final String name;
    boolean external = false;
    boolean forceDelete = false;
    boolean doNotDeleteExternal = false;
    byte[] remoteDigest;
    byte[] localDigest;
    long sourceRevision = -1;
    File sourceFile;
    byte[] sourceBytes = null;
    boolean replaced = false;
    Id moduleId;
    boolean isUnderConstruction = false;
    boolean fromPrev = false;

    SvnFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        if (parent != null) {
            return SvnPath.append(parent.getPath(), name);
        } else {
            return "";
        }
    }

    public String getParentPath() {
        if (parent != null) {
            return parent.getPath();
        } else {
            return "";
        }
    }

    public void traverse(SvnFileList.SVNFileVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }

    @Override
    public String toString() {
        return getPath();
    }

    private String describe() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nParent:              ").append(parent == null ? "<no>" : parent.getPath());
        sb.append("\nName:                ").append(name);
        sb.append("\nExternal:            ").append(external);
        sb.append("\nForce delete:        ").append(forceDelete);
        sb.append("\nNot delete external: ").append(doNotDeleteExternal);
        sb.append("\nRemote digest:       ").append(remoteDigest == null ? "<no>" : Hex.encode(remoteDigest));
        sb.append("\nLocal digest:        ").append(localDigest == null ? "<no>" : Hex.encode(localDigest));
        sb.append("\nSource bytes:        ").append(sourceBytes == null ? "<no>" : String.valueOf(sourceBytes.length));
        sb.append("\nReplaced:            ").append(replaced);
        sb.append("\nModule Id:           ").append(moduleId);
        sb.append("\nUnder construction:  ").append(isUnderConstruction);
        sb.append("\nFrom prev:           ").append(fromPrev);
        return sb.toString();
    }

    public void setExternalRevisionNumber(long number) {
        sourceRevision = number;
        SvnDir dir = parent;
        while (dir != null) {
            if (dir.external) {
                dir.sourceRevision = number;
            } else {
                break;
            }
            dir = dir.parent;
        }
    }

    protected void unregister() {
        if (parent != null) {
            parent.getChildren().remove(this);
        }
    }

    public void setDoNotDeleteExternal(boolean doNotDelete) {
        doNotDeleteExternal = doNotDelete;
        SvnDir dir = parent;
        while (dir != null) {
            if (dir.external) {
                dir.doNotDeleteExternal = doNotDelete;
            } else {
                break;
            }
            dir = dir.parent;
        }
    }

    public boolean applyExternals(SVNRepositoryAdapter.Editor editor, String externalRootUrl, String targetRootUrl, IFlowLogger logger, SvnFileList.ChangeList changeList, SvnConnectionType connectionType) {
        if (external) {
            if (sourceFile == null && sourceBytes == null) {
                //was in previous release only
                if (doNotDeleteExternal) {
                    if (parent.external) {
                        return true; //was copied with parent
                    } else {
                        //should be copied from perviious release
                        String sourceUrl = SvnPath.append(externalRootUrl, getPath());
                        String targetUrl = SvnPath.append(targetRootUrl, getPath());
                        try {
                            editor.copyFile(sourceUrl, targetUrl, sourceRevision);
                            logger.message("File copied:  " + targetUrl + " from " + sourceUrl);
                            return true;
                        } catch (RadixSvnException ex) {
                            return logger.fatal(ex);
                        }
                    }
                } else {
                    //file was in previous release but missing in current release externals
                    if (parent.external) {
                        //was copied with parent and should be deleted
                        String targetUrl = SvnPath.append(targetRootUrl, getPath());
                        try {
                            editor.deleteEntry(targetUrl, -1);
                            logger.message("File deleted:  " + targetUrl);
                            return true;
                        } catch (RadixSvnException ex) {
                            return logger.fatal(ex);
                        }
                    } else {
                        //was not copied. Skip
                        return true;
                    }
                }
            } else {
                InputStream stream = null;
                File temporaryFile = null;
                try {
                    if (sourceFile != null) {
                        //infer digest information into jar before append to svn
                        File fileToRead;
                        if (sourceFile.getName().toLowerCase().endsWith(".jar") && localDigest != null) {
                            byte[] justComputedDigest = null;
                            try {
                                justComputedDigest = DirectoryFileSigner.calcFileDigest(sourceFile);
                            } catch (NoSuchAlgorithmException ex) {
                                Logger.getLogger(SvnFile.class.getName()).log(Level.SEVERE, "Houston, we have a problem", ex);
                            }
                            if (justComputedDigest != null) {
                                if (!Arrays.equals(justComputedDigest, localDigest)) {
                                    Logger.getLogger(SvnFile.class.getName()).log(Level.SEVERE, "Computed and precomputed digest mismatch."
                                            + "\nLocal file: " + sourceFile.getPath()
                                            + "\n Svn file state: " + this.describe());
                                }
                            } else {
                                justComputedDigest = localDigest;
                            }
                            File signedFile = DigestWriter.writeDigestToFile(sourceFile, justComputedDigest, false);
                            if (signedFile != null) {
                                fileToRead = signedFile;
                                temporaryFile = fileToRead;
                            } else {
                                fileToRead = sourceFile;
                            }
                        } else {
                            fileToRead = sourceFile;
                        }
                        stream = new FileInputStream(fileToRead);
                    } else {
                        stream = new ByteArrayInputStream(sourceBytes);
                    }
                    try {
                        String targetUrl = SvnPath.append(targetRootUrl, getPath());
                        if (replaced) {
                            if (parent.external) {
                                //was copied with parent
                                editor.deleteEntry(targetUrl, -1);
                            }
                        }
                        /*!*/ editor.appendFile(targetUrl, stream);
                        logger.message((replaced ? "File replaced: " : "File added:    ") + targetUrl);
                        return true;
                    } catch (RadixSvnException ex) {
                        return logger.fatal(ex);
                    }
                } catch (IOException ex) {
                    return logger.fatal(ex);
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(getClass().getName()).log(Level.FINE, ex.getMessage(), ex);
                        }
                    }
                    if (temporaryFile != null) {
                        FileUtils.deleteFile(temporaryFile);
                    }
                }
            }
        } else {
            return true;
        }
    }

}
