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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsDefinition.Domains;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.utils.ModalObjectEditor;


public class AdsDefinitionDomainsEditor extends ModalObjectEditor<AdsDefinition> {

    private ChooseDomainPanel chooseDomainPanel;
    private boolean readOnly = false;

    public AdsDefinitionDomainsEditor() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 400));
    }

    @Override
    protected String getTitle() {
        return NbBundle.getMessage(AdsDefinitionDomainsEditor.class, "choose-multiple-domains-title");
    }

    @Override
    protected void applyChanges() {
        final Set<Id> selectedIds = chooseDomainPanel.getSelectedDomainIds();
        final Domains currentDomains = getObject().getDomains();
        final Id[] oldDomainIds = getObject().getDomains().getDomaindIds();

        int count = oldDomainIds.length;
        for (int i = 0; i < count;) {
            if (!selectedIds.contains(oldDomainIds[i])) {
                currentDomains.removeDomain(i);
                count--;
            } else {
                i++;
            }
        }

        Set<Id> currentDomainIds = new HashSet<Id>(Arrays.asList(currentDomains.getDomaindIds()));
        for (AdsDomainDef newDomain : chooseDomainPanel.getSelectedDomains()) {
            if (!currentDomainIds.contains(newDomain.getId())) {
                currentDomains.addDomain(newDomain);
            }
        }
    }

    @Override
    protected void afterOpen() {
        if (chooseDomainPanel != null) {
            remove(chooseDomainPanel);
        }
        chooseDomainPanel = new ChooseDomainPanel(getObject(), /*
                 * multiple selection
                 */ true, /*
                 * fill initial values from context
                 */ true, null);
        chooseDomainPanel.setReadOnly(readOnly);
        add(chooseDomainPanel, BorderLayout.CENTER);
    }

    @Override
    protected void setReadOnly(boolean readOnly) {
        if (chooseDomainPanel != null) {
            chooseDomainPanel.setReadOnly(readOnly);
        }
        this.readOnly = readOnly;
    }
}
