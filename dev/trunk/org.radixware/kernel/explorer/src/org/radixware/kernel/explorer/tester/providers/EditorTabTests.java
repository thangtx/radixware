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

package org.radixware.kernel.explorer.tester.providers;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.editorpages.RadEditorPages;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.IContext;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.types.Pid;
import org.radixware.kernel.common.client.views.IExplorerItemView;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.tester.tests.TabTest;

import org.radixware.kernel.explorer.views.Editor;


public class EditorTabTests extends TestsProvider {

    private List<EditorPageModelItem> pages;
    private boolean testEmbedded;

    public EditorTabTests(IClientEnvironment environment, TestsProvider provider, Editor editor) {
        super(environment, provider);
        EntityModel model = editor.getEntityModel();
        if (model != null && model.getEditorPresentationDef() != null) {
            this.pages = getPagesOrderForTest(model);
        }
        if (editor.getEntityModel().isExists()) {
            final IExplorerItemView eiView = editor.getModel().findNearestExplorerItemView();
            final Pid pid = editor.getEntityModel().getPid();
            final List<IExplorerItemView> choosenEntities = eiView.getChoosenEntities(pid.getTableId());
            testEmbedded = !(editor.getEntityModel().getContext() instanceof IContext.ChoosenEntityEditing);
            for (IExplorerItemView choosen : choosenEntities) {
                if (choosen.getChoosenEntityInfo().pid.equals(pid)) {
                    testEmbedded = false;
                }
            }
        } else {
            testEmbedded = true;
        }
    }

    public EditorTabTests(IClientEnvironment environment, TestsProvider provider, List<EditorPageModelItem> pages) {
        super(environment, provider);
        this.pages = pages;
        testEmbedded = ((EditorTabTests) provider).testEmbedded;
    }

    private List<EditorPageModelItem> getPagesOrderForTest(EntityModel model) {
        List<EditorPageModelItem> result = new LinkedList<EditorPageModelItem>();
        RadEditorPages radPages = model.getEditorPresentationDef().getEditorPages();
        Collection<Id> allPages = radPages.getAllPagesIds();

        for (Id pId : allPages) {
            EditorPageModelItem item = model.getEditorPage(pId);
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }
    private int lastTested = -1;
    private Set<Id> testedPages = new HashSet<Id>();

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (pages != null && !pages.isEmpty()) {
            if (options.pagesCount != -1) {
                int testedCount = testedPages.size();
                if (testedCount == options.pagesCount) {
                    return null;
                }
            }
            lastTested++;
            if (lastTested < pages.size()) {
                EditorPageModelItem item = pages.get(lastTested);
                testedPages.add(item.getId());
                return new TabTest(item, testEmbedded);
            }
        }
        return null;
    }
}
