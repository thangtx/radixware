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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.radixware.kernel.common.defs.ads.build.IFlowLogger;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.svn.SVNRepositoryAdapter;
import org.radixware.kernel.common.svn.SvnConnectionType;
import org.radixware.kernel.common.svn.client.SvnPath;


class SvnDir extends SvnFile {

    List<SvnFile> children = null;

    public SvnDir(String name) {
        super(name);
    }

    public boolean applySrc(SVNRepositoryAdapter.Editor editor, String layerUrlInPrevReleaseSrc,
            String layerUrlInReleaseSrc, IFlowLogger logger, SvnConnectionType connectionType) {

        final String targetDirUrl = SvnPath.append(layerUrlInReleaseSrc, getPath());
        final String sourceUrl = SvnPath.append(layerUrlInPrevReleaseSrc, getPath());

        try {
            if (isUnderConstruction) {
                if (fromPrev) {
                    editor.copyDir(sourceUrl, targetDirUrl, sourceRevision);
                    logger.message("Under construction dir copied:    " + targetDirUrl + " from " + sourceUrl);
                    editor.closeDir();
                    return true;
                } else {
                    editor.deleteEntry(targetDirUrl, -1);
                    logger.message("Under construction dir deleted:   " + targetDirUrl);
                    return true;
                }
            }
            if (children != null && !external) {
                for (final SvnFile child : children) {
                    if (child instanceof SvnDir) {
                        editor.openDir(targetDirUrl, connectionType);
                        if (!((SvnDir) child).applySrc(editor, layerUrlInPrevReleaseSrc, layerUrlInReleaseSrc, logger, connectionType)) {
                            return false;
                        }
                        editor.closeDir();
                    }
                }
            }
            return true;
        } catch (RadixSvnException exception) {
            return logger.fatal(exception);
        }
    }

    SvnFile findChildLocal(String name) {
        if (children != null) {
            for (SvnFile file : children) {
                if (file.name.equals(name)) {
                    return file;
                }
            }
        }
        return null;
    }

    SvnFile addFileLocal(String fileName, boolean directory, boolean external) {
        if (children == null) {
            children = new ArrayList<>();
        }
        SvnFile child = directory ? new SvnDir(fileName) : new SvnFile(fileName);
        children.add(child);
        child.parent = this;
        child.external = external;
        return child;
    }

    public SvnFile addFile(String path, boolean directory, IFlowLogger logger, boolean external) {
        String fileName = path.replace("\\", "/");
        SvnDir owner = this;
        if (fileName.contains("/")) {
            String[] names = fileName.split("/");
            String[] normalNames = new String[names.length];
            int j = 0;
            for (int i = 0; i < names.length; i++) {
                if (!names[i].isEmpty()) {
                    normalNames[j] = names[i];
                    j++;
                }
            }
            if (j < 1) {
                if (logger != null) {
                    logger.error("Can not add file: Invalid file name \"" + path + "\"");
                }
                return null;
            }
            for (int i = 0; i < j; i++) {
                SvnFile sub = owner.findChildLocal(normalNames[i]);
                if (sub == null) {
                    //create dir
                    if (i < j - 1) {
                        sub = owner.addFileLocal(normalNames[i], true, external);
                        owner = (SvnDir) sub;
                    }
                } else {
                    if (i == j - 1) {
                        if (directory != (sub instanceof SvnDir)) {
                            if (logger != null) {
                                logger.error("Can not add file " + path + ". Obstruction detected");
                            }
                            return null;
                        }
                        return sub;
                    }
                    if (sub instanceof SvnDir) {
                        owner = (SvnDir) sub;
                    } else {
                        if (logger != null) {
                            logger.error("Can not add file " + path + ". " + sub.name + " is not directory");
                        }
                        return null;
                    }
                }
            }
            fileName = normalNames[j - 1];
        }
        SvnFile file = owner.findChildLocal(fileName);
        if (file != null) {
            if (directory != (file instanceof SvnDir)) {
                if (logger != null) {
                    logger.error("Can not add file " + path + ". Obstruction detected");
                }
                return null;
            } else {
                return file;
            }
        }
        return owner.addFileLocal(fileName, directory, external);
    }

    public List<SvnFile> getChildren() {
        if (children == null) {
            return Collections.emptyList();
        } else {
            return children;
        }
    }

    public SvnFile findChild(String name) {
        String fileName = name.replace("\\", "/");
        if (fileName.contains("/")) {
            String[] names = fileName.split("/");
            String[] normalNames = new String[names.length];
            int j = 0;
            for (int i = 0; i < names.length; i++) {
                if (!names[i].isEmpty()) {
                    normalNames[j] = names[i];
                    j++;
                }
            }
            if (j < 1) {
                return null;
            }
            SvnDir owner = this;
            for (int i = 0; i < j; i++) {
                SvnFile sub = owner.findChildLocal(normalNames[i]);
                if (i == j - 1) {
                    return sub;
                } else if (sub instanceof SvnDir) {
                    owner = (SvnDir) sub;
                } else {
                    return null;
                }
            }
            return null;
        } else {
            return findChildLocal(name);
        }
    }

    @Override
    public void traverse(SvnFileList.SVNFileVisitor visitor) {
        visitor.visit(this);
        if (children != null) {
            for (SvnFile file : children) {
                file.traverse(visitor);
            }
        }
        visitor.endVisit(this);
    }

    public boolean applyChildExternals(SVNRepositoryAdapter.Editor editor, String externalRootUrl, String targetRootUrl, IFlowLogger logger, SvnFileList.ChangeList changeList, SvnConnectionType connectionType) {
        if (children != null) {
            for (SvnFile file : children) {
                if (file.forceDelete) {
                    final String targetFileUrl = SvnPath.append(targetRootUrl, file.getPath());
                    try {
                        editor.deleteEntry(targetFileUrl, -1);
                        //             "Under construction dir copied:    "
                        logger.message("File deleted - not distributable: " + targetFileUrl);
                    } catch (RadixSvnException ex) {
                        return logger.fatal(ex);
                    }
                }
                if (!file.applyExternals(editor, externalRootUrl, targetRootUrl, logger, changeList, connectionType)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean applyExternals(SVNRepositoryAdapter.Editor editor, String externalRootUrl, String targetRootUrl, IFlowLogger logger, SvnFileList.ChangeList changeList, SvnConnectionType connectionType) {
        final String targetDirUrl = SvnPath.append(targetRootUrl, getPath());
        final String sourceUrl = SvnPath.append(externalRootUrl, getPath());

        if (isUnderConstruction) {
            try {
                if (fromPrev) {
                    editor.copyDir(sourceUrl, targetDirUrl, sourceRevision);
                    logger.message("Under construction dir copied:    " + targetDirUrl + " from " + sourceUrl);
                    editor.closeDir();
                    return true;

                } else {
                    editor.deleteEntry(targetDirUrl, -1);
                    logger.message("Under construction dir deleted:   " + targetDirUrl);
                    return true;
                }
            } catch (RadixSvnException ex) {
                return logger.fatal(ex);
            }
        }

        if (external) {
            if (sourceRevision >= 0) {
                if (!doNotDeleteExternal) {
                    if (parent.external) {
                        try {
                            editor.deleteEntry(targetDirUrl, -1);
                            logger.message("Dir deleted:   " + targetDirUrl);
                            return true;
                        } catch (RadixSvnException ex) {
                            return logger.fatal(ex);
                        }
                    } else {
                        return true;
                    }
                } else {
                    if (parent.external) {
                        //was copied with parent
                        try {
                            editor.openDir(targetDirUrl);
                            if (!applyChildExternals(editor, externalRootUrl, targetRootUrl, logger, changeList, connectionType)) {
                                return false;
                            }
                            editor.closeDir();
                            return true;
                        } catch (RadixSvnException ex) {
                            return logger.fatal(ex);
                        }
                    } else {
                        try {
                            editor.copyDir(sourceUrl, targetDirUrl, sourceRevision);
                            logger.message("Dir copied:    " + targetDirUrl + " from " + sourceUrl);
                            if (!applyChildExternals(editor, externalRootUrl, targetRootUrl, logger, changeList, connectionType)) {
                                return false;
                            }
                            editor.closeDir();
                            return true;
                        } catch (RadixSvnException ex) {
                            return logger.fatal(ex);
                        }
                    }
                }
            } else {
                try {
                    editor.appendDir(targetDirUrl);
                    logger.message("Dir added:     " + targetDirUrl);
                    if (!applyChildExternals(editor, externalRootUrl, targetRootUrl, logger, changeList, connectionType)) {
                        return false;
                    }
                    editor.closeDir();
                    return true;
                } catch (RadixSvnException ex) {
                    return logger.fatal(ex);
                }
            }
        } else {
            try {
                
                editor.openDir(targetDirUrl, connectionType);
                if (!applyChildExternals(editor, externalRootUrl, targetRootUrl, logger, changeList, connectionType)) {
                    editor.closeDir();
                    return false;
                }
                editor.closeDir();
                return true;
            } catch (RadixSvnException ex) {
                return logger.fatal(ex);
            }
        }
    }

    public SvnDir getScriptsDir() {
        SvnFile ddsSegment = findChild("dds");
        if (ddsSegment instanceof SvnDir) {
            SvnFile scripts = ((SvnDir) ddsSegment).findChild("scripts");
            if (scripts instanceof SvnDir) {
                return (SvnDir) scripts;
            }
        }
        return null;
    }
}
