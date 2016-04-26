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

package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.components.FilteredTreeView;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.designer.common.dialogs.results.IResultsFilter;
import org.radixware.kernel.designer.common.dialogs.results.ResultsTree;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;


public class FindInSourcesResultsTree extends ResultsTree {

    private IResultsFilter filter;

    private class Head extends RadixObjectItem {

        public Head(RadixObject radixObject) {
            super(radixObject);
        }
    }

    private class OccurenceItem extends FilteredTreeView.Item {

        public OccurenceItem(IOccurrence occurrence) {
            super(occurrence);
            setDisplayName(occurrence.getDisplayText());
        }

        @Override
        public boolean isLeaf() {
            return true;
        }

        public IOccurrence getPatternOccurence() {
            return (IOccurrence) super.getUserObject();
        }

        public Head getHead() {
            return (Head) super.getParent();
        }
    }

    public void add(IOccurrence occurrence) {
        final RadixObject headObject = occurrence.getOwnerObject();

        Head head = (Head) findItemByUserObject(headObject);
        boolean needAddToTree;
        if (head == null) {
            head = new Head(headObject);
            needAddToTree = true;
        } else {
            needAddToTree = false;
        }

        final OccurenceItem occurrenceItem = new OccurenceItem(occurrence);
        head.add(occurrenceItem);

        applyFilter(head);

        if (needAddToTree) {
            getRoot().add(head);
        }
    }

    @Override
    public void goToLastSelectedObject() {
        try {
            if (getLastSelectedUserObject() instanceof Definition) {
                EditorsManager.getDefault().open((Definition) getLastSelectedUserObject());
            } else if (getLastSelectedUserObject() instanceof IOccurrence) {
                try {
                    ((IOccurrence) getLastSelectedUserObject()).goToObject();
                } catch (RuntimeException ex) {
                    try {
                        EditorsManager.getDefault().open(((IOccurrence) getLastSelectedUserObject()).getOwnerObject());
                    } catch (RuntimeException ex1) {
                       Logger.getLogger(getClass().getName()).log(Level.FINE, ex1.getMessage(), ex1);
                    }
                    throw ex;
                }
            }
        } catch (RuntimeException ex) {
            DialogUtils.messageError(new RuntimeException("Unable to open referenced location", ex));
        }
    }

    private void applyFilter(Head head) {
        final String pattern = (filter != null ? filter.getText().toLowerCase() : "");
        final Collection<? extends RadixObject> rootObjects = (filter != null ? filter.getRootObjects() : null);

        final RadixObject radixObject = head.getRadixObject();
        final boolean headSatisfiedToRoots = isSatisfiedToRoot(radixObject, rootObjects);

        boolean headSatisfiedToText = pattern.isEmpty() || head.getDisplayName().toLowerCase().contains(pattern);
        boolean someItemDisplayed = false;

        for (Item child : head.getChildren()) {
            final OccurenceItem occurence = (OccurenceItem) child;
            final RadixObject source = occurence.getHead().getRadixObject();

            final boolean itemSatisfiedToText = (!pattern.isEmpty() && (headSatisfiedToText || occurence.getDisplayName().toLowerCase().contains(pattern.toLowerCase()))) || pattern.isEmpty();
            if (!itemSatisfiedToText) {
                occurence.setVisible(false);
                continue;
            }

            final boolean itemSatisfiedToRoot = (rootObjects != null && !rootObjects.isEmpty() && headSatisfiedToRoots) || isSatisfiedToRoot(source, rootObjects);
            if (!itemSatisfiedToRoot) {
                occurence.setVisible(false);
                continue;
            }

            occurence.setVisible(true);
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
}
