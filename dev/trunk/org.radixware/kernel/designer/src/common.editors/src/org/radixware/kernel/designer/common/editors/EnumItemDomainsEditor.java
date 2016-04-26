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

package org.radixware.kernel.designer.common.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import org.radixware.kernel.common.components.ExtendableTextField;
import org.radixware.kernel.common.defs.ads.AdsDomainDef;
import org.radixware.kernel.common.defs.ads.common.AdsUtils;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.designer.common.dialogs.choosedomain.AdsDefinitionDomainsEditor;


public final class EnumItemDomainsEditor extends ExtendableTextField {

    private AdsEnumItemDef item;
    private final JButton chooseDomainsButton;

    public EnumItemDomainsEditor(final Runnable runnable) {
        super(true);

        chooseDomainsButton = addButton();
        chooseDomainsButton.setIcon(RadixWareIcons.ARROW.GO_TO_OBJECT.getIcon(13, 13));
        chooseDomainsButton.setToolTipText("Configure Domains");

        final ActionListener chooseButton = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                final AdsDefinitionDomainsEditor editor = new AdsDefinitionDomainsEditor();
                editor.open(getItem(), null);
                if (editor.showModal()) {
                    if (runnable != null) {
                        runnable.run();
                    }

                    if (item != null) {
                        setValue(getDomainListAsStr(item));
                    }
                }
            }
        };
        chooseDomainsButton.addActionListener(chooseButton);
    }

    public void open(final AdsEnumItemDef item) {
        if (item != null) {
            this.item = item;
            chooseDomainsButton.setEnabled(!item.isReadOnly());
            setValue(getDomainListAsStr(item));
        }
    }

    public AdsEnumItemDef getItem() {
        return item;
    }

    public static String getDomainListAsStr(AdsEnumItemDef item) {
        Collection<Id> domainIds = item.getDomainIds();
        if (domainIds == null || domainIds.isEmpty()) {
            return "<Not Defined>";
        }
        String sNames = "";
        List<AdsPath> pathes = item.getDomains().getUsedDomainPathes();
        for (AdsPath path : pathes) {
            Id idLst[] = path.asArray();
            if (idLst.length > 0) {
                AdsDomainDef def = (AdsDomainDef) AdsUtils.findTopLevelDefById(item.getModule().getSegment().getLayer(), idLst[0]);
                String sName;
                if (def != null) {
                    def = (AdsDomainDef) def.findComponentDefinition(idLst[idLst.length - 1]).get();
                    sName = def == null ? path.toString() : def.getQualifiedName();
                } else {
                    sName = path.toString();
                }

                if (sNames.isEmpty()) {
                    sNames = sName;
                } else {
                    sNames += ", " + sName;
                }
            }
        }
        return sNames;

    }
}
