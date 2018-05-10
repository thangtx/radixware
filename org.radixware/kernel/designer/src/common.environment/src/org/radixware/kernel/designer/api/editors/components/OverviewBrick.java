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
package org.radixware.kernel.designer.api.editors.components;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.defs.IDescribable;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.ITransparency;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.localization.ILocalizedDescribable;
import org.radixware.kernel.designer.api.ApiFilter;
import org.radixware.kernel.designer.api.editors.Brick;
import org.radixware.kernel.designer.api.editors.BrickFactory;
import org.radixware.kernel.designer.api.editors.OpenMode;
import org.radixware.kernel.designer.api.editors.SimpleBrick;
import org.radixware.kernel.designer.common.dialogs.components.ScalableRuler;
import org.radixware.kernel.designer.common.dialogs.components.description.DescriptionEditor;
import org.radixware.kernel.designer.common.dialogs.components.description.MixedDescriptionWrapper;
import org.radixware.kernel.designer.common.dialogs.names.NamingService;

public class OverviewBrick<T extends RadixObject> extends Brick<T> {

    private static final float FONT_SIZE_FACTOR_H1 = 1.2f;
    private static final float FONT_SIZE_FACTOR_H2 = 1.0f;
    protected static final String OVERVIEW = "overview";
    protected static final String NAME = "name";
    protected static final String LOCATION = "location";
    protected static final String DESCRIPTION = "description";
    protected static final String TOOLBAR = "toolbar";

    //
    private final BrickFactory factory;

    public OverviewBrick(T object, GridBagConstraints constraints, BrickFactory factory) {
        super(object, constraints, OVERVIEW);

        this.factory = factory;
    }

    @Override
    protected void beforeBuild(OpenMode mode, ApiFilter filter) {
        if (mode != OpenMode.EMBEDDED) {
            buildName();
            buildLocation();
        }
        buildAccess(mode);
        buildToolbar();
        buildHierarchy();
        buildDescription();
    }

    private void buildName() {
        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        final JLabel lblName = new JLabel();
        final Font font = lblName.getFont();
        final ScalableRuler ruler = new ScalableRuler(12, font.getSize());

        lblName.setIcon(getSource().getIcon().getIcon(ruler.scale(16), ruler.scale(20)));
        lblName.setText(NamingService.getDefault().getHtmlName(getSource(), getSource().getTypeTitle() + " '", "'"));
        lblName.setFont(font.deriveFont(Font.BOLD, font.getSize() * FONT_SIZE_FACTOR_H1));
        panel.add(lblName);

        if (getSource() instanceof ITransparency) {
            ITransparency transparency = (ITransparency) getSource();
            if (AdsTransparence.isTransparent(transparency)) {
                final JLabel lblWrapped = new JLabel();
                lblWrapped.setText("is wrapper of " + transparency.getTransparence().getPublishedName() + " ");
                lblWrapped.setFont(font.deriveFont(Font.ITALIC, font.getSize() * FONT_SIZE_FACTOR_H1));

                panel.add(lblWrapped);
            }
        }

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets.left = 16;
        getBricks().add(new SimpleBrick(source, panel, constraints, NAME, null));

    }

    private void buildLocation() {
        buildLocation(getLocation());
    }

    protected RadixObject getLocation() {
        RadixObject location = getSource().getOwnerDefinition();
        
        if (location == null) {
            location = getSource().getLayer();
        }
        
        return location;
    }

    private void buildLocation(RadixObject location) {
        if (location != null) {

            final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            final Font font = panel.getFont();

            final JLabel lblOwner = new RefLabel(location);
            lblOwner.setText(location.getTypeTitle() + " " + location.getName());
            lblOwner.setFont(font.deriveFont(font.getSize() * FONT_SIZE_FACTOR_H2));

            final JLabel lblLocation = new JLabel("Location:");
            lblLocation.setFont(font.deriveFont(font.getSize() * FONT_SIZE_FACTOR_H2));
            panel.add(lblLocation);
            panel.add(Box.createHorizontalStrut(4));
            panel.add(lblOwner);

            final GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.insets.left = 20;
            getBricks().add(new SimpleBrick(source, panel, constraints, LOCATION, null));
        }
    }

    protected void buildDescription() {
        if (getSource() instanceof IDescribable || getSource() instanceof ILocalizedDescribable) {
            final DescriptionEditor descriptionEditor = new DescriptionEditor();

            final MixedDescriptionWrapper wrapper = MixedDescriptionWrapper.Factory.tryNewInstance(getSource());

            if (wrapper != null) {
                descriptionEditor.open(wrapper);
            } else {
                descriptionEditor.tryOpen(getSource());
            }

            final GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;

            getBricks().add(new SimpleBrick(source, descriptionEditor, constraints, DESCRIPTION, null));
        }
    }

    protected void buildToolbar() {
    }

    private void buildAccess(OpenMode mode) {
        if (getSource() instanceof AdsDefinition) {
            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.FIRST_LINE_START;
            if (mode != OpenMode.EMBEDDED) {
                c.insets.left = 20;
            }
            getBricks().add(new AccessBrick((AdsDefinition) source, c));
        }
    }

    protected void buildHierarchy() {
        if (hasHierarchy(getSource())) {

            final GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.LINE_START;
            getBricks().add(factory.create(BrickFactory.HIERARCHY, getSource(), c));
        }
    }

    protected boolean hasHierarchy(RadixObject object) {
        return HierarchyBrick.hasHierarchy(object);
    }
}
