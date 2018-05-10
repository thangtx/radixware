package org.radixware.kernel.designer.common.editors.mml.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.mml.MmlTagMarkdownResource;
import org.radixware.kernel.designer.common.dialogs.scmlnb.tags.TagEditor;
import org.radixware.kernel.designer.common.editors.documentation.DocResourcesEditor;

public class MarkdownRefTagEditor<T extends MmlTagMarkdownResource> extends TagEditor<T> {

    private DocResourcesEditor editor;

    public MarkdownRefTagEditor() {
        initComponents();
    }

    @Override
    public void afterOpen() {

        AdsModule module = (AdsModule) getTag().getModule();

        // editor
        editor = new DocResourcesEditor(module, getTag().getLanguage());
        FilePanel.add(editor);
        editor.addPropertyChangeListener("file", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                updateOkState();
            }
        });
        editor.setSelectFile(getTag().getFileName());

        // title
        textTitle.setText(getTag().getTitle());

        updateOkState();
    }

    @Override
    public void applyChanges() {
        getTag().setTitle(textTitle.getText());
        getTag().setFileName(editor.getSelectFile().getName());
        updateOkState();
    }

    @Override
    protected boolean tagDefined() {
        String title = textTitle.getText();
        File file = editor.getSelectFile();

        return !title.isEmpty() && file != null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        final boolean editable = !readOnly;
        textTitle.setEditable(editable);
    }

    @Override
    public String getTitle() {
        return "Reference Tag";
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        FilePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        textTitle = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(360, 260));
        setPreferredSize(new java.awt.Dimension(360, 260));

        jLabel1.setText(org.openide.util.NbBundle.getMessage(MarkdownRefTagEditor.class, "MarkdownRefTagEditor.jLabel1.text")); // NOI18N

        FilePanel.setLayout(new java.awt.BorderLayout());

        jLabel3.setText(org.openide.util.NbBundle.getMessage(MarkdownRefTagEditor.class, "MarkdownRefTagEditor.jLabel3.text")); // NOI18N
        FilePanel.add(jLabel3, java.awt.BorderLayout.PAGE_START);

        textTitle.setText(org.openide.util.NbBundle.getMessage(MarkdownRefTagEditor.class, "MarkdownRefTagEditor.textTitle.text")); // NOI18N
        textTitle.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textTitleKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(textTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 318, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(textTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(FilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    private void textTitleKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textTitleKeyReleased
        updateOkState();
    }//GEN-LAST:event_textTitleKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel FilePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField textTitle;
    // End of variables declaration//GEN-END:variables

}
