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

package org.radixware.kernel.explorer.tester.tests;

import com.trolltech.qt.gui.QWidget;
import org.radixware.kernel.common.client.meta.IExplorerItemsHolder;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItemDef;
import org.radixware.kernel.common.client.meta.explorerItems.RadExplorerItems;
import org.radixware.kernel.common.client.models.items.EditorPageModelItem;
import org.radixware.kernel.common.client.views.IEditorPageView;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.explorer.tester.TesterConstants;
import org.radixware.kernel.explorer.tester.TesterEngine;
import org.radixware.kernel.explorer.tester.TesterEnvironment;
import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.providers.EmbeddedViewProvider;
import org.radixware.kernel.explorer.tester.providers.TestsProvider;
import org.radixware.kernel.explorer.views.Editor;
import org.radixware.kernel.explorer.views.IExplorerView;
import org.radixware.kernel.explorer.views.StandardEditorPage;
import org.radixware.kernel.explorer.views.selector.Selector;
import org.radixware.kernel.explorer.widgets.EmbeddedView;


public class TabTest implements ITest {

    private boolean interrupted = false;
    private TabTestResult testResult;
    private EditorPageModelItem page;
    private IExplorerView nextTestObject;
    private final boolean testEmbedded;

    public TabTest(EditorPageModelItem page, final boolean testEmbedded) {
        this.page = page;
        this.testResult = new TabTestResult(page.getEnvironment(), page);
        this.testResult.operation = TesterConstants.TEST_PAGE.getTitle();
        this.testEmbedded = testEmbedded;
    }

    @Override
    public TestsProvider createChildTestsProvider(TestsProvider parentProvider, TestsOptions options) {
        if (nextTestObject != null) {

            return new EmbeddedViewProvider(page.getEnvironment(), parentProvider, nextTestObject);
        }
        return null;
    }

    @Override
    public void execute(TestsOptions options, TesterEnvironment env) {
        try {
            if (page.isEnabled() && page.isVisible()) {
                if (!interrupted) {
                    page.setFocused();
                    testResult.result = TesterConstants.RESULT_PAGE_SCS.getTitle();
                    this.nextTestObject = calculateNextTestObject(options);
                }
            } else if (!page.isVisible()) {
                testResult.result = TesterConstants.RESULT_PAGE_INVISIBLE.getTitle();
            } else if (!page.isEnabled()) {
                testResult.result = TesterConstants.RESULT_PAGE_DISABLED.getTitle();
            }
        } catch (Throwable ex) {
            TesterEngine.processThrowable(page.getEnvironment(), ex, testResult);
        }
    }

    private IExplorerView calculateNextTestObject(TestsOptions options) {
        IEditorPageView pageView = page.getView();
        if (pageView==null){
            pageView = page.createView();
        }
        final RadExplorerItems childItems;
        if (page.getOwner().getDefinition() instanceof IExplorerItemsHolder){
            childItems = ((IExplorerItemsHolder)page.getOwner().getDefinition()).getChildrenExplorerItems();
        }else{
            childItems = null;
        }
        if (pageView instanceof StandardEditorPage) {
            StandardEditorPage asStandart = (StandardEditorPage) pageView;
            if (asStandart.getWidget() instanceof EmbeddedView) {
                EmbeddedView asView = (EmbeddedView) asStandart.getWidget();
                if (asView.getExplorerItemId() != null) {
                    RadExplorerItemDef itemDef = 
                        childItems==null ? null : childItems.findExplorerItem(asView.getExplorerItemId());
                    if (testEmbedded || itemDef == null || !itemDef.isVisible()) {
                        return findViewForTest(asView, options);
                    }
                }
            }
        } else if (pageView.getClass().getSimpleName().startsWith(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE.getValue())) {
            EmbeddedView view = (EmbeddedView) ((QWidget)pageView).findChild(EmbeddedView.class);
            if (view != null && view.getExplorerItemId() != null) {
                RadExplorerItemDef itemDef = 
                        childItems==null ? null : childItems.findExplorerItem(view.getExplorerItemId());
                if (testEmbedded || itemDef == null || !itemDef.isVisible()) {
                    return findViewForTest(view, options);
                }
            }
        }
        return null;
    }

    private IExplorerView findViewForTest(EmbeddedView view, TestsOptions options) {
        Selector selectorChild = (Selector) view.findChild(Selector.class);
        Editor editorChild = (Editor) view.findChild(Editor.class);

        if (selectorChild != null) {
            return selectorChild;
        }
        if (editorChild != null) {
            return editorChild;
        }
        return null;
    }

    @Override
    public TestResult getTestResult() {
        return testResult;
    }

    @Override
    public void interrupt() {
        interrupted = true;
        page.getEnvironment().getEasSession().breakRequest();
    }
}
