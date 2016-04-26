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

package org.radixware.kernel.designer.environment.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.awt.StatusDisplayer;
import org.openide.util.RequestProcessor;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.common.general.utils.RadixMutex;

public final class RenameImagesAction implements ActionListener {

    private static boolean isUnnamed(AdsImageDef image) {
        final String name = image.getName();
        return (name.isEmpty() || "Untitled".equalsIgnoreCase(name) || "<Untitled>".equalsIgnoreCase(name));
    }

    private void doRenameImages(final Set<RadixObject> radixObjects) {
        final Map<AdsImageDef, List<String>> image2Words = new HashMap();
        for (RadixObject radixObject : radixObjects) {
            final Definition definition = radixObject.getDefinition();
            if (definition != null) {
                final String name = definition.getName();
                if (name != null && !name.isEmpty()) {
                    final List<Definition> usedRadixObjects = new ArrayList();
                    radixObject.collectDependences(usedRadixObjects);
                    for (RadixObject used : usedRadixObjects) {
                        if (used instanceof AdsImageDef) {
                            final AdsImageDef image = (AdsImageDef) used;
                            if (!image.isReadOnly()) {
                                List<String> words = image2Words.get(image);
                                if (words == null) {
                                    words = new ArrayList();
                                    image2Words.put(image, words);
                                }
                                words.add(name);
                            }
                        }
                    }
                }
            }
        }

        for (Map.Entry<AdsImageDef, List<String>> entry : image2Words.entrySet()) {
            final AdsImageDef image = entry.getKey();
            final List<String> words = entry.getValue();
            final Map<String, Integer> word2Count = new HashMap();
            int maxCount = 0;
            for (String word : words) {
                Integer count = word2Count.get(word);
                if (count != null) {
                    count++;
                } else {
                    count = 1;
                }
                word2Count.put(word, count);
                maxCount = Math.max(count.intValue(), maxCount);
            }
            boolean first = true;
            final List<String> keywords = new ArrayList();
            for (int i = maxCount; i > 0; i--) {
                for (Map.Entry<String, Integer> entry2 : word2Count.entrySet()) {
                    final String word = entry2.getKey();
                    final int count = entry2.getValue().intValue();
                    if (count == i) {
                        if (first) {
                            if (isUnnamed(image)) {
                                image.setName(word);
                            }
                            first = false;
                        } else {
                            keywords.add(word);
                        }
                    }
                }
            }
            if (image.getKeywords().isEmpty() && !keywords.isEmpty()) {
                image.setKeywords(keywords);
            }
        }
    }

    private void renameImages(final Collection<? extends RadixObject> context) {
        final ProgressHandle progressHandle = ProgressHandleFactory.createHandle("Images renaming");

        progressHandle.start();
        try {
            RadixMutex.writeAccess(new Runnable() {

                @Override
                public void run() {
                    final Set<RadixObject> radixObjects = RadixObjectsUtils.collectAllInside(context, VisitorProviderFactory.createDefaultVisitorProvider());
                    doRenameImages(radixObjects);
                }
            });
        } finally {
            progressHandle.finish();
        }

        StatusDisplayer.getDefault().setStatusText("Images renamed.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final Collection<Branch> branches = RadixFileUtil.getOpenedBranches();
        if (branches.isEmpty()) {
            DialogUtils.messageError("There are no opened branches.");
            return;
        }

        if (!DialogUtils.messageConfirmation("Rename images?")) {
            return;
        }

        RequestProcessor.getDefault().post(new Runnable() {

            @Override
            public void run() {
                renameImages(branches);
            }
        });
    }
}
