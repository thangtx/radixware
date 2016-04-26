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

package org.radixware.kernel.designer.common.general.license;

import java.awt.Dimension;
import java.beans.PropertyChangeListener;
import java.util.prefs.Preferences;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;


public class LicenseServerOptionsController extends OptionsPanelController {

    private static final String LICENSE_SERVER_ADDRESS = "org.radixware.license.server.address";
    private static final String LICENSE_SERVER_DOWNLOAD_ENABLED = "org.radixware.license.server.enable.download";
    private final JPanel panel;
    private final JTextField tfLicense;
    private final JCheckBox cbUseLicServer;

    public LicenseServerOptionsController() {
        panel = new JPanel(new MigLayout("fill", "[][grow]", "[shrink][shrink][grow]"));
        panel.add(new JLabel("License server address:"));
        tfLicense = new JTextField();
        tfLicense.setMaximumSize(new Dimension(Integer.MAX_VALUE, tfLicense.getPreferredSize().height));
        tfLicense.setMinimumSize(new Dimension(280, tfLicense.getPreferredSize().height));
        panel.add(tfLicense, "growx, wrap");
        cbUseLicServer = new JCheckBox("Use license server to obtain license list");
        panel.add(cbUseLicServer);
    }

    @Override
    public void update() {
        tfLicense.setText(getLicenseServerAddress());
        cbUseLicServer.setSelected(isDownloadFromLicenseServerEnabled());
    }

    @Override
    public void applyChanges() {
        Preferences.userNodeForPackage(LicenseServerOptionsController.class).put(LICENSE_SERVER_ADDRESS, tfLicense.getText());
        Preferences.userNodeForPackage(LicenseServerOptionsController.class).put(LICENSE_SERVER_DOWNLOAD_ENABLED, Boolean.toString(cbUseLicServer.isSelected()));
    }

    @Override
    public void cancel() {
        //do nothing
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isChanged() {
        return true;
    }

    @Override
    public JComponent getComponent(Lookup masterLookup) {
        return panel;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return null;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener l) {
        //do nothing
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener l) {
        //do nothing
    }

    public static boolean isDownloadFromLicenseServerEnabled() {
        return Boolean.valueOf(Preferences.userNodeForPackage(LicenseServerOptionsController.class).get(LICENSE_SERVER_DOWNLOAD_ENABLED, "false"));
    }

    public static String getLicenseServerAddress() {
        return Preferences.userNodeForPackage(LicenseServerOptionsController.class).get(LICENSE_SERVER_ADDRESS, null);
    }
}
