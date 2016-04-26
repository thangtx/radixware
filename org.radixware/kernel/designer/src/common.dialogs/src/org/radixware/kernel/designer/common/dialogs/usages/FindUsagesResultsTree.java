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

package org.radixware.kernel.designer.common.dialogs.usages;

import java.util.Collection;
import javax.swing.Icon;
import org.radixware.kernel.common.components.FilteredTreeView;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.designer.common.dialogs.results.IResultsFilter;
import org.radixware.kernel.designer.common.dialogs.results.ResultsTree;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


class FindUsagesResultsTree extends ResultsTree {

    private class Head extends ResultsTree.RadixObjectItem {

        public Head(RadixObject radixObject) {
            super(radixObject);
        }
    }

    private class UsageWrapper {

        private final RadixObject displayedHead;
        private final RadixObject source;

        public UsageWrapper(RadixObject displayedHead, RadixObject source) {
            this.displayedHead = displayedHead;
            this.source = source;
        }

        public RadixObject getSource() {
            return source;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof UsageWrapper) {
                final UsageWrapper usageWrapper = (UsageWrapper) obj;
                return usageWrapper.displayedHead == displayedHead && usageWrapper.source == source;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return displayedHead.hashCode() + source.hashCode();
        }
    }

    private class Usage extends FilteredTreeView.Item {

        public Usage(UsageWrapper usageWrapper) {
            super(usageWrapper);

            final RadixObject source = usageWrapper.getSource();
            final RadixObject displayed = getDisplayedObject(source);
            String suffix = source.getTypeTitle().toLowerCase();
            String[] words = suffix.split(" ");
            if (words.length > 2) {
                suffix = words[words.length - 2] + " " + words[words.length - 1];
            }

            if (source instanceof Scml.Tag) {
                //compute line;
                Scml scml = ((Scml.Tag) source).getOwnerScml();
                int line = 1;
                for (Scml.Item item : scml.getItems()) {
                    if (item == source) {
                        break;
                    }
                    if (item instanceof Scml.Text) {
                        String text = ((Scml.Text) item).getText();
                        int index = text.indexOf("\n");
                        while (index > 0) {
                            line++;
                            index = text.indexOf("\n", index + 1);
                        }
                    }
                }
                suffix += " at line " + line;
            }


            final String qualifiedName = displayed.getQualifiedName() + " - " + suffix;
            final Icon icon = displayed.getIcon().getIcon();

            setDisplayName(qualifiedName);
            setIcon(icon);
        }

        private RadixObject getDisplayedObject(RadixObject source) {
            RadixObject obj = source;
            while (obj != null) {
                if (obj instanceof Definition) {
                    return obj;
                }
                obj = obj.getContainer();
            }
            return source;
        }

        @Override
        public String getToolTipText() {
            final RadixObject source = getSource();
            final RadixObject displayed = getDisplayedObject(source);
            return displayed.getToolTip();
        }

        public RadixObject getSource() {
            final UsageWrapper usageWrapper = (UsageWrapper) getUserObject();
            return usageWrapper.getSource();
        }

        @Override
        public boolean isLeaf() {
            return true;
        }
    }
    private IResultsFilter filter = null;

    public FindUsagesResultsTree() {
        super();
    }

    public void add(RadixObject headSource, RadixObject usageSource) {
        final RadixObject headDisplayed = getDisplayedObject(headSource);
        final UsageWrapper usageWrapper = new UsageWrapper(headDisplayed, usageSource);
        // do not display twice
        if (findItemByUserObject(usageWrapper) != null) {
            return;
        }

        final boolean needAddToTree;
        Head head = (Head) findItemByUserObject(headDisplayed);
        if (head == null) {
            head = new Head(headDisplayed);
            needAddToTree = true;
        } else {
            needAddToTree = false;
        }

        final Usage usage = new Usage(usageWrapper);
        head.add(usage);
        applyFilter(head);

        if (needAddToTree) {
            getRoot().add(head);
        }
    }

    private void applyFilter(Head head) {
        final String pattern = (filter != null ? filter.getText().toLowerCase() : "");
        final Collection<? extends RadixObject> rootObjects = (filter != null ? filter.getRootObjects() : null);

        final RadixObject radixObject = head.getRadixObject();
        final boolean headSatisfiedToRoots = isSatisfiedToRoot(radixObject, rootObjects);
//        if (!headSatisfiedToRoots) {
//            head.setVisible(false);
//            return;
//        }

        //boolean headSatistiedToText = pattern.isEmpty() || head.getDisplayName().toLowerCase().contains(pattern);
        boolean someItemDisplayed = false;

        for (Item child : head.getChildren()) {
            final Usage usage = (Usage) child;
            final RadixObject source = usage.getSource();

            final boolean itemSatistiedToText = pattern.isEmpty() || usage.getDisplayName().toLowerCase().contains(pattern);
            if (!itemSatistiedToText) {
                usage.setVisible(false);
                continue;
            }

            final boolean itemSatisfiedToRoot = (rootObjects != null && !rootObjects.isEmpty() && headSatisfiedToRoots) || isSatisfiedToRoot(source, rootObjects);
            if (!itemSatisfiedToRoot) {
                usage.setVisible(false);
                continue;
            }

            usage.setVisible(true);
            someItemDisplayed = true;
        }

        head.setVisible(someItemDisplayed);
    }

    protected void setFilter(IResultsFilter filter) {
        if (this.filter != filter) {
            this.filter = filter;
            onFilterChanged();
        }
    }

    @Override
    protected void onFilterChanged() {
        for (Item child : getRoot().getChildren()) {
            final Head head = (Head) child;
            applyFilter(head);
        }
    }

    @Override
    public void goToLastSelectedObject() {
        final Object userObject = getLastSelectedUserObject();
        if (userObject instanceof RadixObject) {
            final RadixObject radixObject = (RadixObject) userObject;
            DialogUtils.goToObject(radixObject);
        } else if (userObject instanceof UsageWrapper) {
            final UsageWrapper usageWrapper = (UsageWrapper) userObject;
            final RadixObject source = usageWrapper.getSource();
            DialogUtils.goToObject(source, new OpenInfo(source));
        }
    }

    @Override
    public boolean canFindUsagesForSelectedObject() {
        final Object userObject = getLastSelectedUserObject();
        if (userObject instanceof Definition) {
            return true;
        } else if (userObject instanceof UsageWrapper) {
            final UsageWrapper usageWrapper = (UsageWrapper) userObject;
            final RadixObject source = usageWrapper.getSource();
            if (source instanceof Definition) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void findUsagesOfLastSelectedObject() {
        final Object userObject = getLastSelectedUserObject();
        if (userObject instanceof Definition) {
            final Definition def = (Definition) userObject;
            final FindUsagesCfg cfg = FindUsagesCfgPanel.askCfg(def);
            if (cfg != null) {
                FindUsages.search(cfg);
            }
        } else if (userObject instanceof UsageWrapper) {
            final UsageWrapper usageWrapper = (UsageWrapper) userObject;
            final RadixObject source = usageWrapper.getSource();
            if (source instanceof Definition) {
                final Definition def = (Definition) source;
                final FindUsagesCfg cfg = FindUsagesCfgPanel.askCfg(def);
                if (cfg != null) {
                    FindUsages.search(cfg);
                }
            }
        }
    }
}
