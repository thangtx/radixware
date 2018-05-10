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
package org.radixware.kernel.designer.environment.merge;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlbeans.XmlException;
import org.radixware.kernel.common.constants.FileNames;
import org.radixware.kernel.common.defs.dds.DdsModelManager;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.Reference;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.tree.actions.dds.DdsModuleCaptureAction;
import org.radixware.schemas.ddsdef.ModelDocument;

public class DdsMergeChangesOptions extends AbstractMergeChangesOptions {

    public void captureDestinationModules() {

        try {
            final StringBuilder message = new StringBuilder();
            boolean isFirst = true;
            Reference<Branch> cacheBranch = new Reference<>();
            for (DdsMergeChangesItemWrapper wrapper : wrappers) {
                if (!wrapper.isCaptured() && wrapper.isDone() && !wrapper.isMlb()) {

                    if (isFirst) {
                        isFirst = false;
                    } else {
                        message.append(", ");
                    }
                    message.append(wrapper.getWrapperName());
                    wrapper.setCaptured();

                    File fixedModelFile = wrapper.getDestFile();
                    
                    final DdsModuleCaptureAction.Cookie Cookie = new DdsModuleCaptureAction.Cookie(DdsModelManager.Support.loadAndGetDdsModule(fixedModelFile, cacheBranch));
                    Cookie.run();
                    
                    if (!wrapper.isNewModel()) {//added model.xml by coping
                        
                        final File modifyFile = new File(fixedModelFile.getParentFile().getAbsolutePath() + "/" + FileNames.DDS_MODEL_MODIFIED_XML);
                        final ModelDocument modelDocument = ModelDocument.Factory.parse(modifyFile);
                        final ModelDocument.Model model = modelDocument.getModel();
                        String oldEditor = model.getEditor();
                        String oldStation = model.getStation();
                        String oldFilePath = model.getFilePath();

                        byte[] oldBytes = wrapper.getNewModelXmlVersion();
                        //final ModelDocument.Model newModel = 

                        if (oldBytes!=null){ //new module - RADIX-11623
                            final ModelDocument doc = ModelDocument.Factory.parse(new ByteArrayInputStream(oldBytes));
                            final ModelDocument.Model model2 = doc.getModel();
                            model2.setEditor(oldEditor);
                            model2.setStation(oldStation);
                            model2.setFilePath(oldFilePath);
                            doc.save(modifyFile);
                        }

                        
                    }
                }
            }
            if (!isFirst) {
                DialogUtils.messageInformation("The following module(s) will be captured in the brunch \'"
                        + getToBranchShortName() + "\': " + message
                        + "\nDo not forget to fix them and execute changes scripts.");
            }

        } catch (IOException | XmlException ex) {
            MergeUtils.messageError(ex);
        }

    }

    private final List<DdsModule> natureDefList;

    public DdsMergeChangesOptions(final List<DdsModule> srcModules) {
        this.srcModules = srcModules;
        natureDefList = new ArrayList(srcModules);
    }

    public List<DdsModule> getNatureDefList() {
        return natureDefList;
    }

    private List<File> incorrectFiles;

    public List<File> getIncorrectFiles() {
        return incorrectFiles;
    }

    public void setIncorrectFiles(final List<File> incorrectFiles) {
        this.incorrectFiles = incorrectFiles;
    }

    private final List<DdsModule> srcModules;
    private List<DdsMergeChangesItemWrapper> wrappers;

    public List<DdsMergeChangesItemWrapper> getList() {
        return wrappers;
    }

    public void setWrappers(final List<DdsMergeChangesItemWrapper> wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public String getFromBranchShortName() {
        return srcModules == null || srcModules.isEmpty() ? "Not defined" : srcModules.get(0).getSegment().getLayer().getBranch().getFile().getParentFile().getName();
    }

    @Override
    public String getToBranchShortName() {

        return this.getToBranchFile() == null ? "Not defined" : this.getToBranchFile().getName();
    }

    @Override
    public String getNameByIndex(int index) {
        return wrappers.get(index).getWrapperName();
    }

    @Override
    public String getFromPathByIndex(int index) {
        try {
            return wrappers.get(index).getFromPath();
        } catch (Exception ex) {
            MergeUtils.messageError(ex);
            return null;
        }
    }

    @Override
    public String getToPathByIndex(int index) {
        try {
            return wrappers.get(index).getToPath();
        } catch (Exception ex) {
            MergeUtils.messageError(ex);
            return null;
        }
    }
}
