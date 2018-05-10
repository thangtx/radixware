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

package org.radixware.kernel.common.repository.dds;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.dds.DdsDefinitionIcon;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.dds.DdsUpdateInfo.BaseLayerInfo;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.common.utils.XmlFormatter;

/**
 * Metainformation about DDS segment scrips folder and scripts.xml file.
 *
 */
public class DdsScripts extends RadixObject implements IDirectoryRadixObject {

    private final DdsDatabaseScripts oracleScripts;
    public static final String SCRIPTS_DIR_NAME = "scripts";
    public static final String SCRIPTS_XML_FILE_NAME = "scripts.xml";

    protected DdsScripts(DdsSegment ownerSegment) {
        super();
        setContainer(ownerSegment);
        oracleScripts = DdsDatabaseScripts.Factory.newOracleScripts(this);
    }

    @Override
    public String getName() {
        return "Scripts";
    }

    @Override
    public RadixIcon getIcon() {
        return DdsDefinitionIcon.SQL_SCRIPTS;
    }

    public DdsSegment getOwnerSegment() {
        return (DdsSegment) getContainer();
    }

    public static File calcScriptsDirectory(DdsSegment segment) {
        final File segmentDir = segment.getDirectory();
        if (segmentDir != null) {
            return new File(segmentDir, SCRIPTS_DIR_NAME);
        } else {
            return null;
        }
    }

    public static File calcScriptsXmlFile(DdsSegment segment) {
        final File dir = calcScriptsDirectory(segment);
        if (dir != null) {
            return new File(dir, SCRIPTS_XML_FILE_NAME);
        } else {
            return null;
        }
    }

    @Override
    public File getDirectory() {
        final DdsSegment segment = getOwnerSegment();
        if (segment != null) {
            return calcScriptsDirectory(segment);
        } else {
            return null;
        }
    }

    /**
     * @return scripts.xml file or null if segment is not in branch.
     */
    @Override
    public File getFile() {
        final DdsSegment segment = getOwnerSegment();
        if (segment != null) {
            return calcScriptsXmlFile(segment);
        } else {
            return null;
        }
    }

    @Override
    public boolean isSaveable() {
        return true;
    }

    public DdsDatabaseScripts getDbScripts() {
        return oracleScripts;
    }

    @Deprecated
    public DdsDatabaseScripts getOracleScripts() {
        return getDbScripts();
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getDbScripts().visit(visitor, provider);
        getUpdatesInfo().visit(visitor, provider);
    }

    public static class UpdatesInfo extends RadixObjects<DdsUpdateInfo> {

        protected UpdatesInfo(DdsScripts ownerScripts) {
            super(ownerScripts);
        }

        public DdsScripts getOwnerScripts() {
            return (DdsScripts) getContainer();
        }

        @Override
        public boolean isSaveable() {
            return true;
        }

        public DdsUpdateInfo findByUpdateFileName(String fileName) {
            for (DdsUpdateInfo info : this) {
                if (Utils.equals(info.getUpdateFileName(), fileName)) {
                    return info;
                }
            }
            return null;
        }
        
        public DdsUpdateInfo registerNew(DdsScript updateScript, boolean isBackwardCompatible, DdsUpdateInfo reverseFor) { //TODO: убрать
            return registerNew(updateScript, isBackwardCompatible, null, reverseFor);
        }

        public DdsUpdateInfo registerNew(DdsScript updateScript, boolean isBackwardCompatible, DdsAadcTransform transform, DdsUpdateInfo reverseFor) {
            assert updateScript != null;

            final DdsUpdateInfo updateInfo = new DdsUpdateInfo();
            if (transform != null) {
                updateInfo.getDdsAadcTransform().loadFrom(transform);
            }
            add(updateInfo);

            final File updateFile = updateScript.getFile();
            assert (updateFile != null);

            final String updateFileName = updateFile.getName();
            updateInfo.setUpdateFileName(updateFileName);
            updateInfo.setBackwardCompatible(isBackwardCompatible);
            if (reverseFor != null) {
                updateInfo.setReverse(true);
                updateInfo.setStraightUpdateFileName(reverseFor.getUpdateFileName());
            }

            final DdsSegment segment = getOwnerScripts().getOwnerSegment();

            final Layer root = segment.getLayer();
            for (Layer prevLayer : root.listBaseLayers()) {
                if (prevLayer != root) {
                    final BaseLayerInfo baseLayerInfo = new BaseLayerInfo();
                    updateInfo.getBaseLayersInfo().add(baseLayerInfo);

                    final String uri = prevLayer.getURI();
                    baseLayerInfo.setUri(uri);

                    final String releaseNumber;
                    if (prevLayer.isReadOnly()) {  // RADIX-2695
                        releaseNumber = prevLayer.getReleaseNumber();
                    } else {
                        final Layer layer = segment.getLayer();
                        final String lastReleaseVersionInBranchXml = layer.getBranch().getLastReleaseVersion();
                        if (lastReleaseVersionInBranchXml==null || lastReleaseVersionInBranchXml.isEmpty()){ //RADIX-10388 - все слои имеют один и тот же номер релиза
                                                                                                             //по офшуту еще ни разу не отрезали              
                            releaseNumber = prevLayer.getReleaseNumber();
                        }
                        else{
                            releaseNumber = lastReleaseVersionInBranchXml;
                        }
                    }

                    baseLayerInfo.setReleaseNumber(releaseNumber);

                    final DdsSegment prevSegment = ((DdsSegment) prevLayer.getDds());
                    final DdsScript lastUpdateScript = prevSegment.getScripts().getDbScripts().getUpgradeScripts().findLastByNumberedName(reverseFor != null);
                    if (lastUpdateScript != null) {
                        final File lastUpdateFile = lastUpdateScript.getFile();
                        final String lastUpdateFileName = lastUpdateFile.getName();
                        baseLayerInfo.setLastUpdateFileName(lastUpdateFileName);
                    }
                }
            }



            return updateInfo;
        }
    }
    private final UpdatesInfo updatesInfo = new UpdatesInfo(this);

    public UpdatesInfo getUpdatesInfo() {
        return updatesInfo;
    }

    protected void loadFrom(org.radixware.schemas.product.Scripts xScripts) {
        updatesInfo.clear();
        final List<org.radixware.schemas.product.Scripts.Script> xScriptsList = xScripts.getScriptList();
        if (xScriptsList != null) {
            for (org.radixware.schemas.product.Scripts.Script xScript : xScriptsList) {
                final DdsUpdateInfo updateInfo = new DdsUpdateInfo();
                updateInfo.loadFrom(xScript);
                updatesInfo.add(updateInfo);
            }
        }
        setEditState(EEditState.NONE);
        updatesInfo.setEditState(EEditState.NONE);
    }
    private long fileLastModifiedTime = 0L;

    public void reload() throws IOException {
        final File file = getFile();

        final String ERROR_MESS = "Unable to load scripts from " + String.valueOf(file);
        final long fileTime;

        if (file != null && file.exists()) {
            fileTime = file.lastModified();
            try {
                final org.radixware.schemas.product.Scripts xScripts = org.radixware.schemas.product.ScriptsDocument.Factory.parse(file).getScripts();
                loadFrom(xScripts);
            } catch (XmlException | IllegalArgumentException | RadixError e) {
                throw new IOException(ERROR_MESS, e);
            }
        } else {
            updatesInfo.clear();
            fileTime = 0L;
        }

        fileLastModifiedTime = fileTime;

        setEditState(EEditState.NONE);
        updatesInfo.setEditState(EEditState.NONE);
    }

    public long getFileLastModifiedTime() {
        synchronized (this) {
            return fileLastModifiedTime;
        }
    }

    protected void appendTo(org.radixware.schemas.product.Scripts xScripts) {
        for (DdsUpdateInfo updateInfo : getUpdatesInfo()) {
            org.radixware.schemas.product.Scripts.Script xScript = xScripts.addNewScript();
            updateInfo.appendTo(xScript);
        }
    }

    @Override
    public void save() throws IOException {
        final File dir = getDirectory();
        assert (dir != null);

        //FileUtils.mkDirs(dir);

        final File file = getFile();
        assert file != null;

        synchronized (this) {
            final org.radixware.schemas.product.ScriptsDocument xScriptsDocument = org.radixware.schemas.product.ScriptsDocument.Factory.newInstance();
            final org.radixware.schemas.product.Scripts xScripts = xScriptsDocument.addNewScripts();

            appendTo(xScripts);

            XmlFormatter.save(xScriptsDocument, file);

            fileLastModifiedTime = file.lastModified();
        }

        setEditState(EEditState.NONE);
        updatesInfo.setEditState(EEditState.NONE);
    }

    @Override
    public String getTypesTitle() {
        return super.getTypeTitle(); // prevent scriptses
    }
}
