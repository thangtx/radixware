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

package org.radixware.kernel.designer.common.dialogs.choosedomain;

import java.awt.event.ActionEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.AbstractAction;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

/**
 * Utility class for choosing domains.
 */
public class ChooseDomain {

    private ChooseDomain() {
    }

    private static List<AdsDomainDef> chooseDomainImpl(RadixObject context, boolean isMultipleSelectionAllowed, boolean fillValuesFromContext) {
        String bundleKey;
        if (isMultipleSelectionAllowed) {
            bundleKey = "choose-multiple-domains-title";
        } else {
            bundleKey = "choose-single-domain-title";
        }

        String title = NbBundle.getMessage(ChooseDomain.class, bundleKey);

        final ApplyAction applyAction = new ApplyAction();
        ChooseDomainPanel choosePanel = new ChooseDomainPanel(context, isMultipleSelectionAllowed, fillValuesFromContext, applyAction);
        ModalDisplayer md = new ModalDisplayer(choosePanel, title);
        applyAction.setDisplayerToApply(md);

        List<AdsDomainDef> result = null;
        if (md.showModal()) {
            result = new LinkedList<AdsDomainDef>(choosePanel.getSelectedDomains());
        }
        return result;
    }

    private static class ApplyAction extends AbstractAction {

        private ModalDisplayer displayerToApply;

        public void setDisplayerToApply(ModalDisplayer displayerToApply) {
            this.displayerToApply = displayerToApply;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (displayerToApply != null) {
                displayerToApply.close(true);
            }
        }
    }

    public static final AdsDomainDef chooseDomain(RadixObject context) {
        List<AdsDomainDef> result = chooseDomainImpl(context, false, false);
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public static final List<AdsDomainDef> chooseDomains(AdsDefinition context) {
        List<AdsDomainDef> result = chooseDomainImpl(context, true, false);
        return result;
    }
}
