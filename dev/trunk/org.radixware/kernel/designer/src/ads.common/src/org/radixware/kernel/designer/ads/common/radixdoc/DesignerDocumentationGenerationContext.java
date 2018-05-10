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
package org.radixware.kernel.designer.ads.common.radixdoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.radixware.kernel.common.components.ICancellable;
import org.radixware.kernel.common.components.IProgressHandle;
import org.radixware.kernel.common.defs.ads.build.Cancellable;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.doc.DocTopicBody;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.mml.Mml;
import org.radixware.kernel.common.radixdoc.IDocLogger;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.build.ProgressHandleFactoryDelegate;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.radixdoc.generator.RadixdocGenerationContext;
import org.radixware.kernel.radixdoc.html.FileProvider;
import org.radixware.kernel.radixdoc.xmlexport.DefinitionDocInfo;
import org.radixware.schemas.radixdoc.ModuleDocItem;

public abstract class DesignerDocumentationGenerationContext extends RadixdocGenerationContext {

    private final IDocLogger logger = new DocLogger();
    protected IProgressHandle progress;
    private final Set<Id> radixDocXmlModules = new HashSet<>();
    private final Set<Id> reportedDefIds = new HashSet<>();

    protected final ICancellable cancellable = new ICancellable() {

        private volatile boolean cancel = false;

        @Override
        public boolean cancel() {
            return cancel = true;
        }

        @Override
        public boolean wasCancelled() {
            return cancel;
        }
    };

    protected DesignerDocumentationGenerationContext(FileProvider fileProvider, String topLayerUri) {
        super(fileProvider, topLayerUri);
    }

    protected DesignerDocumentationGenerationContext(FileProvider fileProvider, List<String> layerList) {
        super(fileProvider, layerList);
    }

    protected void createProgress(String title) {
        ProgressHandleFactoryDelegate factory = new ProgressHandleFactoryDelegate();
        Cancellable nbCancellable = new Cancellable() {

            @Override
            public boolean cancel() {
                return cancellable.cancel();
            }

            @Override
            public boolean wasCancelled() {
                return cancellable.wasCancelled();
            }
        };
        progress = factory.createHandle(title, nbCancellable);
    }

    @Override
    protected void addModuleInfo(ModuleDocItem moduleDocItem) {
        radixDocXmlModules.add(moduleDocItem.getId());
    }

    @Override
    public DefinitionDocInfo getDefinitionDocInfo(final Id definitionId) {
        if (defDocInfoCache == null) {
            if (!prepareDefDocInfoCache()) {
                return null;
            }
        }

        DefinitionDocInfo docInfo = getDefDocInfoFromCache(definitionId, true);

        if (docInfo == null) {
            Definition def = null;
            if (!reportedDefIds.contains(definitionId)) {
                final List<Branch> branches = new ArrayList<>(RadixFileUtil.getOpenedBranches());
                if (branches == null || branches.isEmpty()) {
                    return null;
                }

                for (Branch branch : branches) {
                    def = (Definition) branch.find(new VisitorProvider() {

                        @Override
                        public boolean isTarget(RadixObject radixObject) {
                            return radixObject instanceof Definition && ((Definition) radixObject).getId() == definitionId;
                        }
                    });
                }

                if (def != null && def.getModule() != null) {
                    if (radixDocXmlModules.contains(def.getModule().getId())) {
                        logger.put(EEventSeverity.WARNING, "Definition '" + def.getQualifiedName() + "' is not found in 'radixdoc.xml'");
                        reportedDefIds.add(definitionId);
                    } else {
                        logger.put(EEventSeverity.WARNING, "Missing radixdoc.xml for module '" + def.getModule().getQualifiedName() + "'");
                        reportedDefIds.add(definitionId);
                    }
                }
            }

            return null;
        }

        DefinitionDocInfo baseDocInfo = getDefDocInfoFromCache(definitionId, false);
        if (!baseDocInfo.equals(docInfo)) {
            docInfo.setBaseLayerName(baseDocInfo.getOwnLayerName());
        }

        return docInfo;
    }

    @Override
    public Mml getMml(final Id definitionId, EIsoLanguage lang) {
        if (mmlMapCache == null || mmlMapCache.get(lang) == null) {
            if (!prepareTopicBodyCache(lang)) {
                return null;
            }
        }

        Map<String, Mml> mmlMap = mmlMapCache.get(lang);
        String fileName = "body" + "/"
                + lang.getValue() + "/"
                + DocTopicBody.FILE_SIMPLE_PREFIX + definitionId.toString() + ".xml";
        Mml mml = mmlMap.get(fileName);
        return mml;
    }

    @Override
    public IDocLogger getDocLogger() {
        return logger;
    }

    @Override
    public IProgressHandle getProgressHandler() {
        return progress;
    }

    @Override
    public ICancellable getCancellable() {
        return cancellable;
    }
}
