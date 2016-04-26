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

import java.util.List;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.models.EntityModel;
import org.radixware.kernel.common.client.models.Model;
import org.radixware.kernel.common.client.tree.ExplorerItemView;
import org.radixware.kernel.common.client.tree.IExplorerTree;
import org.radixware.kernel.common.client.tree.nodes.IExplorerTreeNode;

import org.radixware.kernel.common.client.models.ITestableModel;

import org.radixware.kernel.explorer.tester.TestsOptions;
import org.radixware.kernel.explorer.tester.tests.CloseViewTest;
import org.radixware.kernel.explorer.tester.tests.CustomTest;
import org.radixware.kernel.explorer.tester.tests.ITest;
import org.radixware.kernel.explorer.tester.tests.OpenViewTest;
import org.radixware.kernel.explorer.tester.tests.PropDialogsSingleEntityTest;
import org.radixware.kernel.explorer.tree.ExplorerTree;

import org.radixware.kernel.explorer.views.IExplorerView;


public class ExplorerTreeTests extends TestsProvider {

    ExplorerTree testTree;
    IExplorerTreeNode currentNode, rootNode;

    public ExplorerTreeTests(IClientEnvironment environment, TestsProvider provider) {
        super(environment, provider);
    }
    private boolean customTestPassed = false;
    private boolean propDialogsPassed = false;

    private ITest getNextSpecificTest(TestsOptions options) {
        if (currentNode.getView().getModel() instanceof EntityModel && options.testPropDialog) {
            if (!propDialogsPassed) {
                propDialogsPassed = true;
                return new PropDialogsSingleEntityTest((EntityModel) currentNode.getView().getModel());
            } else {
                propDialogsPassed = false;
                return new CloseViewTest(getEnvironment(),
                        (IExplorerView) currentNode.getView().getModel().getView());
            }
        }        
        return new CloseViewTest(getEnvironment(), (IExplorerView) currentNode.getView().getModel().getView());
    }

    @Override
    public ITest createNextTest(TestsOptions options) {
        if (currentNode != null
                && currentNode.isValid() && currentNode.getView().getModel().getView() != null) {
            if (currentNode.getView().getModel() instanceof ITestableModel) {
                if (!customTestPassed) {
                    customTestPassed = true;
                    return new CustomTest(getEnvironment(), (ITestableModel) currentNode.getView().getModel());
                } else {
                    return getNextSpecificTest(options);
                }
            }
            return getNextSpecificTest(options);
        }
        if (testTree == null) {
            final IExplorerTreeNode curNode = getEnvironment().getTreeManager().getCurrentTree().getCurrent();
            testTree = (ExplorerTree)getEnvironment().getTreeManager().openSubTree(curNode, null);
            rootNode = testTree.getRootNodes().get(0);
            rootNode.getView().removeChoosenEntities();
            currentNode = rootNode;
        } else {
            if (currentNode.getChildNodes().isEmpty()) {
                currentNode = findSibling(currentNode);
            } else {
                //Идем вниз
                boolean gotNode = false;
                int i = 0;
                while (!gotNode && i < currentNode.getChildNodes().size()) {
                    currentNode = currentNode.getChildNodes().get(i);
                    if (isRecursive(currentNode)) {
                        i++;
                    } else {
                        gotNode = true;
                    }
                }
                if (!gotNode) {
                    currentNode = findSibling(currentNode);
                }
            }
        }

        if (currentNode != null) {            
            return new OpenViewTest(currentNode);
        } else {
            rootNode = null;
            testTree.closeModel(true);
            testTree = null;
            return null;
        }
    }

    private IExplorerTreeNode findSibling(IExplorerTreeNode node) {
        IExplorerTreeNode siblingNode = null, prevNode;
        while ((siblingNode == null || isRecursive(siblingNode)) && node != null && node != rootNode) {
            prevNode = siblingNode == null ? node : siblingNode;
            siblingNode = getSiblingNode(prevNode);
            if (siblingNode == null) //идем наверх
            {
                prevNode = node;
                node = node.getParentNode();
            }
            if (prevNode.isValid() && prevNode.getView()!=null){
                if (prevNode.getView().isChoosenObject()){
                    testTree.removeNode(prevNode);
                }
                else{
                    prevNode.getView().getModel().clean();
                }
            }                        
        }
        return siblingNode;
    }
    
    private boolean isRecursive(IExplorerTreeNode node) {
        if (node != null) {
            ExplorerItemView view = (ExplorerItemView) node.getView();
            final String viewPrefix = view.getModel().getConfigStoreGroupName();

            List<Model> parentModels = view.getParentModels();
            boolean isRecursive = false;
            int i = 0;
            while (!isRecursive && i < parentModels.size()) {
                Model model = parentModels.get(i);

                String prefix = model.getConfigStoreGroupName();
                if (prefix.equals(viewPrefix)) {
                    isRecursive = true;
                } else {
                    i++;
                }
            }
            return isRecursive;
        }

        return false;
    }

    private IExplorerTreeNode getSiblingNode(final IExplorerTreeNode node) {
        final IExplorerTreeNode parentNode = node.getParentNode();
        if (parentNode != null) {
            final List<IExplorerTreeNode> childNodes = parentNode.getChildNodes();
            int currentNodeIndex = -1;
            for (int i = 0; currentNodeIndex == -1 && i < childNodes.size(); i++) {
                if (childNodes.get(i) == node) {
                    currentNodeIndex = i;                    
                }
            }
            if (currentNodeIndex + 1 < childNodes.size()) {
                return childNodes.get(currentNodeIndex + 1);
            }
        }
        return null;
    }
}
