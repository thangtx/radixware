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
package org.radixware.kernel.designer.ads.editors.xml;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.RadixWareIcons;

public class TreeNodePanel extends JPanel {

    private final Map<EIsoLanguage, JLabel> lang2IconMap = new HashMap<>();
    private final Map<EIsoLanguage, Component> lang2StrutMap = new HashMap<>();

    private final JLabel attributesLabel = new JLabel();
    private final Component attributesStrut = Box.createHorizontalStrut(5);

    private final JLabel warningIcon = new JLabel(RadixWareIcons.XML.WARNING.getIcon());
    private final Component warningStrut = Box.createHorizontalStrut(20);

    public TreeNodePanel(Component nodeTextLabel, List<EIsoLanguage> langs) {
        attributesLabel.setForeground(XmlTreeCellRenderer.SERVICE_NODE);
        attributesLabel.setVisible(false);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setOpaque(false);
        this.add(nodeTextLabel);

        this.add(attributesStrut);
        this.add(attributesLabel);

        this.add(warningStrut);
        this.add(warningIcon);

        for (EIsoLanguage lang : langs) {
            JLabel langLbl = new JLabel(RadixWareIcons.LANGS.getForLang(lang).getIcon());
            Component strut = Box.createHorizontalStrut(5);

            this.add(strut);
            this.add(langLbl);

            lang2StrutMap.put(lang, strut);
            lang2IconMap.put(lang, langLbl);
        }

    }

    public void setWarningIconVisible(boolean visible) {
        warningStrut.setVisible(visible);
        warningIcon.setVisible(visible);
    }

    public void setLangIconVisible(EIsoLanguage lang, boolean visible) {
        lang2IconMap.get(lang).setVisible(visible);
        lang2StrutMap.get(lang).setVisible(visible);
    }

    public void setIconsVisible(boolean visible) {
        warningStrut.setVisible(visible);
        warningIcon.setVisible(visible);
        for (EIsoLanguage lang : lang2IconMap.keySet()) {
            setLangIconVisible(lang, visible);
        }
    }

    public void setAttributesLabelVisible(boolean visible) {
        attributesStrut.setVisible(visible);
        attributesLabel.setVisible(visible);
    }

    public void setAttributesLabelText(String text) {
        attributesLabel.setText(text);
    }
}
