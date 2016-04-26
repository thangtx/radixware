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

package org.radixware.kernel.designer.common.dialogs.check;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JToggleButton;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.EEventSeverity;
import org.radixware.kernel.common.utils.events.IRadixEventListener;
import org.radixware.kernel.common.utils.events.RadixEvent;
import org.radixware.kernel.designer.common.dialogs.results.ResultsToolbar;


class CheckResultsToolbar extends ResultsToolbar implements ICheckResultsFilter {

    private IRadixEventListener counterChangeListener = new IRadixEventListener() {

        @Override
        public void onEvent(RadixEvent e) {
            updateCounters();
        }
    };
    private final CheckResultsTree tree;

    public CheckResultsToolbar(CheckResultsTree tree) {
        super(tree);
        this.tree = tree;

        initButtons();

        warningsSelectButton.setSelected(true);
        errorsSelectButton.setSelected(true);

        updateCounters();
        tree.addCountersChangeListener(counterChangeListener);
    }

    @Override
    public boolean isWarningsDisplayed() {
        return warningsSelectButton.isSelected();
    }

    @Override
    public boolean isErrorsDisplayed() {
        return errorsSelectButton.isSelected();
    }

    private void updateCounters() {
        warningsSelectButton.setText("Warnings: " + tree.getDisplayedWarningsCount() + " / " + tree.getWarningsCount());
        errorsSelectButton.setText("Errors: " + tree.getDisplayedErrorsCount() + " / " + tree.getErrorsCount());
    }
    private final JToggleButton errorsSelectButton = new JToggleButton(new AbstractAction("Errors",
            RadixObjectIcon.getForSeverity(EEventSeverity.ERROR).getIcon()) {

        @Override
        public void actionPerformed(ActionEvent evt) {
            errorsButtonActionPerformed();
        }
    });
    private final JToggleButton warningsSelectButton = new JToggleButton(new AbstractAction("Warnings",
            RadixObjectIcon.getForSeverity(EEventSeverity.WARNING).getIcon()) {

        @Override
        public void actionPerformed(ActionEvent evt) {
            warningsButtonActionPerformed();
        }
    });

    private void initButtons() {
        errorsSelectButton.setToolTipText("Filter by errors");
        errorsSelectButton.setAlignmentY(1.0f);
        errorsSelectButton.setAlignmentX(1.0f);
        getAdditionalToolBar().add(errorsSelectButton);

        warningsSelectButton.setToolTipText("Filter by warnings");
        warningsSelectButton.setAlignmentY(1.0f);
        warningsSelectButton.setAlignmentX(1.0f);
        getAdditionalToolBar().add(warningsSelectButton);
    }

    private void errorsButtonActionPerformed() {
        fireFilterChanged();
    }

    private void warningsButtonActionPerformed() {
        fireFilterChanged();
    }
}
