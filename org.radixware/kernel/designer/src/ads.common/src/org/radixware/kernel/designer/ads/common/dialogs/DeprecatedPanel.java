/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.common.dialogs;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.miginfocom.swing.MigLayout;
import org.openide.DialogDescriptor;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;

public class DeprecatedPanel extends JPanel {

    private final JButton deprecatedInfoBtn = new JButton(RadixWareIcons.DIALOG.CHOOSE.getIcon());
    private static final String DEPRECATED = "Deprecated";
    private final javax.swing.JCheckBox deprecatedCheck;
    private volatile ActionListener expirationReleaseListener;
    private volatile ActionListener deprecatedListener;
    private AdsAccessFlags flags;
    
    public DeprecatedPanel(org.radixware.kernel.designer.ads.common.dialogs.AccessPanel accessList, final AdsAccessFlags flags) {
        this(accessList.addCheckBox(DEPRECATED), flags);
    }
    
    public DeprecatedPanel(JCheckBox box) {
        this(box, null);
    }

    public DeprecatedPanel(JCheckBox box, final AdsAccessFlags flags) {
        setLayout(new MigLayout("insets 0, gap 0"));
        deprecatedCheck = box;
        
        add(deprecatedCheck);
        Dimension d = deprecatedCheck.getMaximumSize();
        deprecatedInfoBtn.setMaximumSize(new Dimension(d.height, d.height));
        d = deprecatedCheck.getMinimumSize();
        deprecatedInfoBtn.setMinimumSize(new Dimension(d.height, d.height));
        d = deprecatedCheck.getPreferredSize();
        deprecatedInfoBtn.setPreferredSize(new Dimension(d.height, d.height));
        add(deprecatedInfoBtn);
        deprecatedInfoBtn.setToolTipText("Expiration Release");
        if (flags != null) {
            setAccessFlags(flags);
        }
    }
    
    public final void setAccessFlags(final AdsAccessFlags flags) {
        this.flags = flags;
        updateText(flags.isDeprecated() ? flags.getExpirationRelease() : null);
        if (expirationReleaseListener != null) {
            deprecatedInfoBtn.removeActionListener(expirationReleaseListener);
        }
        if (deprecatedListener != null) {
            deprecatedCheck.removeActionListener(deprecatedListener);
        }
        expirationReleaseListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ReleaseNamePanel panel = new ReleaseNamePanel();
                panel.open(false, flags.getExpirationRelease(), "Expiration Release: ");
                final ModalDisplayer d = new ModalDisplayer(panel, "Expiration Release");
                final DialogDescriptor descriptor = d.getDialogDescriptor();
                descriptor.setValid(panel.isReady());
                panel.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        descriptor.setValid(panel.isReady());
                    }
                });
                if (d.showModal()) {
                    String number = panel.getText();
                    if (number.isEmpty()) {
                        number = null;
                    }
                    flags.setExpirationRelease(number);
                    updateText(number);
                }
            }
        };
        deprecatedListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateText(flags.isDeprecated() ? flags.getExpirationRelease() : null);
            }
        };
        deprecatedCheck.addActionListener(deprecatedListener);
    }

    private void updateText(String number) {
        if (number == null) {
            deprecatedCheck.setText(DEPRECATED);
        } else {
            deprecatedCheck.setText(DEPRECATED + " (" + number + ")");
        }
    }

    public void addDeprecatedItemListener(ItemListener itemListener) {
        deprecatedCheck.addItemListener(itemListener);
    }

    public void removeDeprecatedItemListener(ItemListener itemListener) {
        deprecatedCheck.removeItemListener(itemListener);
    }

    public void addExpirationReleaseActionListener(ActionListener listener) {
        deprecatedInfoBtn.addActionListener(listener);
    }

    public void removeExpirationReleaseActionListener(ActionListener listener) {
        deprecatedInfoBtn.removeActionListener(listener);
    }

    public void setDefaultListenr() {
        deprecatedInfoBtn.removeActionListener(expirationReleaseListener);
        deprecatedInfoBtn.addActionListener(expirationReleaseListener);
    }

    public void setDeprecatedEnabled(boolean enable) {
        deprecatedCheck.setEnabled(enable && flags.canChangeDeprecated() && !flags.isReadOnly());
    }

    public void setExpirationReleaseEnabled(boolean enable) {
        deprecatedInfoBtn.setEnabled((enable || deprecatedCheck.isSelected()) && flags.canChangeDeprecated() && !flags.isReadOnly());
    }

    public void setDeprecatedSelected(boolean selected) {
        deprecatedCheck.setSelected(selected);
    }
    
    public boolean isDeprecatedSelected() {
        return deprecatedCheck.isSelected();
    }
}
