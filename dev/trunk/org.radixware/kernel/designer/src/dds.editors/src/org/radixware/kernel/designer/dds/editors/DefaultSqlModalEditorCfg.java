package org.radixware.kernel.designer.dds.editors;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;
import org.radixware.kernel.common.dialogs.db.DdsScriptUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;

public abstract class DefaultSqlModalEditorCfg implements SqlModalEditor.ICfg{
        private final JRadioButton chCompatibleBox = new JRadioButton();
        private final JRadioButton chIncompatibleBox = new JRadioButton();

        public DefaultSqlModalEditorCfg() {
            //this.chCompatibleBox.setSelected(modificationInfo.isBackwardCompatible());
            this.chCompatibleBox.setText("Compatible");
            this.chIncompatibleBox.setText("Incompatible");
            this.chCompatibleBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chCompatibleBox.isSelected()) {
                        chIncompatibleBox.setSelected(false);
                    }

                }
            });
            this.chIncompatibleBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (chIncompatibleBox.isSelected()) {
                        if (!DdsScriptUtils.showCompatibleWarning()){
                            chIncompatibleBox.setSelected(false);
                        } else {
                            chCompatibleBox.setSelected(false);
                        }
                    }
                }
            });
        }
        
        public void setCompatible(boolean isCompatible){
            if (isCompatible){
                chCompatibleBox.setSelected(true);
            } else {
                chIncompatibleBox.setSelected(true);
            }
        }
        
        public Boolean getCompatible(){
            if (chCompatibleBox.isSelected()) {
                return true;
            } else if (chIncompatibleBox.isSelected()) {
                return false;
            }
            return null;
        }

        @Override
        public boolean canCloseEditor() {
            return chCompatibleBox.isSelected() || chIncompatibleBox.isSelected();
        }

        @Override
        public JPanel getAdditionalPanel() {
            final JPanel panel = new JPanel();
            panel.setBorder(new TitledBorder("Backward compatibility"));
            panel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
            panel.add(chCompatibleBox);
            panel.add(chIncompatibleBox);
            return panel;
        }

        @Override
        public void showClosingProblems() {
            DialogUtils.messageError("One of script compatibility options must be choosen");
        }
    
}
