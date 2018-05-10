/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixeare.kernel.designer.ads.build.release.scripts;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.builder.release.ScriptInfo;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.svn.RadixSvnException;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.kernel.designer.dds.editors.DefaultSqlModalEditorCfg;
import org.radixware.kernel.designer.dds.editors.SqlModalEditor;

/**
 *
 * @author avoloshchuk
 */
public class ScriptsCheckPanel extends javax.swing.JPanel {
    private final JLabel scriptColLabel = new JLabel("Script");
    private final JLabel compatibleColLabel = new JLabel("Backward Compatible");
    private final JLabel lastModifierColLabel = new JLabel("Last Modified by User");
    private final List<ScriptInfo> scriptsInfo;

    /**
     * Creates new form ScriptsCheckPanel
     */
    public ScriptsCheckPanel(List<ScriptInfo> infos) throws RadixSvnException, URISyntaxException {
        initComponents();
        setLayout(new MigLayout("fillx, insets 0", "[grow][grow][grow][shrink]"));//, hidemode 2
        add(scriptColLabel);
        add(compatibleColLabel, "align center");
        add(lastModifierColLabel, "align center, wrap");
        this.scriptsInfo = infos;
        for (ScriptInfo info : scriptsInfo){
            createScriptInfoPanel(info);
        }
    }
    
    private void createScriptInfoPanel(final ScriptInfo scriptInfo) throws RadixSvnException, URISyntaxException {
        final JLabel nameLabel = new JLabel(scriptInfo.getFileBaseName());
        final JCheckBox compatibleCheckBox = new JCheckBox();
        final JLabel lastModifierLabel = new JLabel(scriptInfo.getLastModifer());
        final JButton editButton = new JButton(RadixWareIcons.EDIT.EDIT.getIcon());
        add(nameLabel);
        add(compatibleCheckBox, "alignx center");
        add(lastModifierLabel, "alignx center");
        add(editButton, "shrinkx, wrap");
        if (scriptInfo.isBackwardCompatible()) {
            compatibleCheckBox.setSelected(true);
            compatibleCheckBox.setToolTipText("Compatible");
        } else {
            nameLabel.setForeground(Color.red);
            compatibleCheckBox.setToolTipText("Incompatible");
        }
        compatibleCheckBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                scriptInfo.setBackwardCompatible(compatibleCheckBox.isSelected());
                if (!compatibleCheckBox.isSelected()) {
                    nameLabel.setForeground(Color.red);
                    compatibleCheckBox.setToolTipText("Incompatible");
                } else {
                    nameLabel.setForeground(Color.BLACK);
                    compatibleCheckBox.setToolTipText("Compatible");
                }
            }
        });
        editButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultSqlModalEditorCfg cfg = new DefaultSqlModalEditorCfg() {

                    @Override
                    public String getSql() {
                        return scriptInfo.getContent();
                    }

                    @Override
                    public void setSql(String sql) {
                        Boolean compatible = getCompatible();
                        if (!Utils.equals(compatible, scriptInfo.isBackwardCompatible())) {
                            scriptInfo.setBackwardCompatible(compatible);
                            compatibleCheckBox.setSelected(compatible);
                        }
                        if (Utils.equals(sql, getSql())) {
                            return;
                        }
                        scriptInfo.setContent(sql);
                    }

                    @Override
                    public String getTitle() {
                        return scriptInfo.getFileBaseName();
                    }

                };
                cfg.setCompatible(scriptInfo.isBackwardCompatible());
                SqlModalEditor.editSql(cfg);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
