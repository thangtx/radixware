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

package org.radixware.kernel.designer.ads.editors.presentations;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.designer.ads.editors.exploreritems.UsedContextlessCommandsListView;
import org.radixware.kernel.designer.common.annotations.registrators.EditorFactoryRegistration;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.IEditorFactory;
import org.radixware.kernel.designer.common.general.editors.IRadixObjectEditor;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;


public class AdsEditorPresentationEditor extends RadixObjectEditor<AdsEditorPresentationDef> {

    @EditorFactoryRegistration
    public static final class Factory implements IEditorFactory<AdsEditorPresentationDef> {

        @Override
        public IRadixObjectEditor<AdsEditorPresentationDef> newInstance(AdsEditorPresentationDef ep) {
            return new AdsEditorPresentationEditor(ep);
        }
    }    

    private JTabbedPane mainPane = new JTabbedPane();
    private InheritableObjectTitleFormatPanel objectTitleFormat = new InheritableObjectTitleFormatPanel();
    private JScrollPane otfScroll = new JScrollPane();
    private CommonPropertiesPanel commonProps = new CommonPropertiesPanel();
//    private InheritableForbiddenPropertiesPanel forbiddenProps = new InheritableForbiddenPropertiesPanel();
    private JScrollPane scroll = new JScrollPane();
    private InheritableRestrictionsPanel restrictionsPanel = new InheritableRestrictionsPanel();
    private JScrollPane rScroll = new JScrollPane();
    private UsedContextlessCommandsListView contextlessCommandsView = new UsedContextlessCommandsListView();
 //   private ExplorerItemOrderPanel orderPanel = new ExplorerItemOrderPanel();

    private final PropertyPresentationAttributesPanel propertyRestrictionsPanel = new PropertyPresentationAttributesPanel();

    public AdsEditorPresentationEditor(AdsEditorPresentationDef ep) {
        super(ep);

        setLayout(new BorderLayout());

        JPanel content = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        content.setLayout(gbl);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        gbl.setConstraints(commonProps, c);
        content.add(commonProps);

        propertyRestrictionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 12, 4, 12));
        final int propertyRestrictionsPanelHeight = propertyRestrictionsPanel.getRowHeight() * 7;
        propertyRestrictionsPanel.setPreferredSize(new Dimension(0, propertyRestrictionsPanelHeight));
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        content.add(propertyRestrictionsPanel, c);

        c.gridy = 2;
        gbl.setConstraints(contextlessCommandsView, c);
        content.add(contextlessCommandsView);

       /* c.gridy = 3;
        gbl.setConstraints(orderPanel, c);
        content.add(orderPanel);*/

        javax.swing.JPanel additional = new javax.swing.JPanel();
        additional.setLayout(new BorderLayout());
        additional.add(content, BorderLayout.NORTH);

        scroll.setViewportView(additional);
        mainPane.add(NbBundle.getMessage(AdsEditorPresentationEditor.class, "SelectorTabs-General"), scroll);
        otfScroll = new javax.swing.JScrollPane(objectTitleFormat);
        mainPane.add(objectTitleFormat.getName(), otfScroll);
        rScroll = new javax.swing.JScrollPane(restrictionsPanel);
        mainPane.add(restrictionsPanel.getName(), rScroll);

        add(mainPane, BorderLayout.CENTER);

        commonProps.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                AdsEditorPresentationEditor.this.update();
            }
        });
    }

    public AdsEditorPresentationDef getEditorPresentation() {
        return getRadixObject();
    }

    @Override
    public boolean open(OpenInfo info) {

        final AdsEditorPresentationDef ep = getEditorPresentation();

        commonProps.open(ep);
        propertyRestrictionsPanel.open(ep);
      //  orderPanel.open(ep);

        contextlessCommandsView.open(ep);
        objectTitleFormat.open(ep);
        restrictionsPanel.open(ep);

        RadixObject target = info.getTarget();
        if (target.equals(ep.getRestrictions())) {
            mainPane.setSelectedComponent(rScroll);
        } else if (target.equals(ep.getObjectTitleFormat())) {
            mainPane.setSelectedComponent(otfScroll);
        } else {
            mainPane.setSelectedComponent(scroll);
        }

        return super.open(info);
    }

    @Override
    public void update() {
        commonProps.update();
        propertyRestrictionsPanel.update();
        contextlessCommandsView.update();
        objectTitleFormat.update();
        restrictionsPanel.update();
      //  orderPanel.update();
    }
}
