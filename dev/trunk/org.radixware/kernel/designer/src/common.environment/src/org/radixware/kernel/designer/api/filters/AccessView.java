package org.radixware.kernel.designer.api.filters;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.GenericComboBox;

public class AccessView extends GenericComboBox.PopupView {

        private final JPanel panel = new JPanel();
        private final JCheckBox chbPublic = new JCheckBox("public");
        private final JCheckBox chbProtected = new JCheckBox("protected");
        private final JCheckBox chbDefault = new JCheckBox("default");
        private final JCheckBox chbPrivate = new JCheckBox("private");
        private final JCheckBox chbPublished = new JCheckBox("only published");
        private final ItemListener listener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {

                if (inUpdate) {
                    return;
                }

                EAccess access = null;
                if (e.getSource() == chbPublic) {
                    access = EAccess.PUBLIC;
                } else if (e.getSource() == chbProtected) {
                    access = EAccess.PROTECTED;
                } else if (e.getSource() == chbDefault) {
                    access = EAccess.DEFAULT;
                } else if (e.getSource() == chbPrivate) {
                    access = EAccess.PRIVATE;
                } else if (e.getSource() == chbPublished) {
                    model.onlyPublished = chbPublished.isSelected();
                }

                if (access != null) {

                    if (access.isLess(EAccess.PROTECTED)) {
                        final boolean bool = chbPrivate.isSelected() || chbDefault.isSelected();

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (bool) {
                                    chbPublished.setSelected(false);
                                }
                                chbPublished.setEnabled(!bool);
                            }
                        });
                    }
                    if (((JCheckBox) e.getItem()).isSelected()) {
                        model.accessLevel.add(access);
                    } else {
                        model.accessLevel.remove(access);
                    }
                }

                changes = true;
            }
        };
        private AccessModel model;
        private boolean changes = false;
        private boolean inUpdate = false;

        public AccessView() {
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(chbPublished);
            panel.add(chbPublic);
            panel.add(chbProtected);
            panel.add(chbDefault);
            panel.add(chbPrivate);

            model = new AccessModel();

            update();

            chbPublic.addItemListener(listener);
            chbProtected.addItemListener(listener);
            chbDefault.addItemListener(listener);
            chbPrivate.addItemListener(listener);
            chbPublished.addItemListener(listener);
        }

        @Override
        public final void update() {
            inUpdate = true;

            chbPublic.setSelected(model.accessLevel.contains(EAccess.PUBLIC));
            chbProtected.setSelected(model.accessLevel.contains(EAccess.PROTECTED));
            chbDefault.setSelected(model.accessLevel.contains(EAccess.DEFAULT));
            chbPrivate.setSelected(model.accessLevel.contains(EAccess.PRIVATE));
            chbPublished.setSelected(model.onlyPublished);

            changes = false;
            inUpdate = false;
        }

        @Override
        public JComponent getComponent() {
            return panel;
        }

        @Override
        public GenericComboBox.Model getModel() {
            return model;
        }

        public boolean isChanged() {
            return changes;
        }
    }