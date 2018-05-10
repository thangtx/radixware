package org.radixware.kernel.designer.common.editors.documentation;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.HeadlessException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.Exceptions;
import org.radixware.kernel.common.defs.ads.doc.DocResources;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.common.utils.FileUtils;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.utils.SwingUtils;

public class DocResourcesEditor extends RadixObjectEditor<DocResources> {

    private final DefaultListModel<File> model = new DefaultListModel();
    private File oldFile;
    private boolean isReadOnly;

    public DocResourcesEditor(AdsModule module, EIsoLanguage lang) {
        super(module.getDocumentation().getResources(lang));
        initComponents();
        isReadOnly = module.isReadOnly();
        rebuild();
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateCorrect();
            }
        });

        // button
        list.setCellRenderer(new DefaultListCellRenderer() {
            protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList list, Object file, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, file, index, isSelected, cellHasFocus);
                renderer.setIcon(RadixWareIcons.FILE.NEW_DOCUMENT.getIcon());
                renderer.setText(FileUtils.getFileBaseName((File) file) + "." + FileUtils.getFileExt((File) file));
                return renderer;
            }
        });

        SwingUtils.InitButton(buttonAppend,
                RadixWareIcons.CREATE.ADD.getIcon(),
                "Append resource",
                this,
                KeyEvent.VK_INSERT);
        SwingUtils.InitButton(buttonRemove,
                RadixWareIcons.DELETE.DELETE.getIcon(),
                "Remove resource",
                this,
                KeyEvent.VK_DELETE);
        SwingUtils.InitButton(buttonOpen,
                RadixWareIcons.EDIT.VIEW.getIcon(),
                "Open resource",
                this,
                KeyEvent.VK_ENTER);
        list.setModel(model);

        update();
    }

    // main
    public void setSelectFile(String fileName) {

        if (fileName == null) {
            return;
        }

        for (Object obj : model.toArray()) {
            File file = (File) obj;
            if (FileUtils.getFileBaseName(file).equals(FileUtils.getFileBaseName(fileName))) {
                list.setSelectedValue(file, true);
            }
        }
    }

    private void updateCorrect() {
        firePropertyChange("file", oldFile, getSelectFile());
        oldFile = getSelectFile();
    }

    public void update() {
        // если запись не выьрана (или выбрана не праильно), то мы установим курсор на первую запись
        if (list.getSelectedIndex() == -1 && model.getSize() >= 0) {
            list.setSelectedIndex(0);
        }

        // enabled
        buttonAppend.setEnabled(!isReadOnly);
        buttonRemove.setEnabled(!isReadOnly && !isEmpty());
        boolean canEdit = Desktop.getDesktop().isSupported(Desktop.Action.EDIT);
        buttonOpen.setEnabled(!isEmpty() && canEdit);
    }

    private void rebuild() {
        // fill list
        File[] files = getFiles();
        if (files != null) {
            model.clear();
            for (File file : files) {
                model.addElement(file);
            }
        }
    }

    private boolean isEmpty() {
        return model.isEmpty();
    }

    //get
    public File getSelectFile() {
        return (File) list.getSelectedValue();
    }

    private String getNewFileName(File dir, File sourceFile) {
        String targetFileBaseName = JOptionPane.showInputDialog("Input resource name", FileUtils.getFileBaseName(sourceFile));
        if (targetFileBaseName == null) {
            return null;
        }

        String tarFileName = targetFileBaseName + "." + FileUtils.getFileExt(sourceFile);
        tarFileName = FileUtils.string2UniversalFileName(tarFileName, '-');

        //check
        final File[] listFiles = dir.listFiles();
        for (File file : listFiles) {
            if (file.getName().equals(tarFileName)) {
                DialogUtils.messageError("Dublicate resource name. Please enter a different name.");
                return getNewFileName(dir, sourceFile);
            }
        }

        return tarFileName;
    }

    private File[] getFiles() {
        File dir = getRadixObject().getDir();
        return dir.listFiles();
    }

    private void afterChange() {
        rebuild();
        update();
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jToolBar = new javax.swing.JToolBar();
        buttonAppend = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonOpen = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        jToolBar1.setRollover(true);

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        buttonAppend.setFocusable(false);
        buttonAppend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAppend.setLabel(org.openide.util.NbBundle.getMessage(DocResourcesEditor.class, "DocResourcesEditor.buttonAppend.label")); // NOI18N
        buttonAppend.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonAppend.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonAppend.setName("buttonAppend"); // NOI18N
        buttonAppend.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonAppend.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAppend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAppendActionPerformed(evt);
            }
        });
        jToolBar.add(buttonAppend);

        buttonRemove.setFocusable(false);
        buttonRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonRemove.setLabel(org.openide.util.NbBundle.getMessage(DocResourcesEditor.class, "DocResourcesEditor.buttonRemove.label")); // NOI18N
        buttonRemove.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonRemove.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonRemove.setName("buttonAppendMap"); // NOI18N
        buttonRemove.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });
        jToolBar.add(buttonRemove);

        buttonOpen.setFocusable(false);
        buttonOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonOpen.setLabel(org.openide.util.NbBundle.getMessage(DocResourcesEditor.class, "DocResourcesEditor.buttonOpen.label")); // NOI18N
        buttonOpen.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonOpen.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonOpen.setName("buttonAppendMap"); // NOI18N
        buttonOpen.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOpenActionPerformed(evt);
            }
        });
        jToolBar.add(buttonOpen);

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE)
                    .addComponent(jToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 260, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonAppendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAppendActionPerformed
        // select
        JFileChooser fileOpen = new JFileChooser();
        int ret = fileOpen.showDialog(null, "Select file");
        if (ret != JFileChooser.APPROVE_OPTION) {
            return;
        }

        // name
        File dir = getRadixObject().getDir();
        File sourceFile = fileOpen.getSelectedFile();
        String targetFileName = getNewFileName(dir, sourceFile);
        if (targetFileName == null) {
            return;
        }
        File targetFile = new File(dir, targetFileName);

        // copy
        try {
            FileUtils.copyFile(sourceFile, targetFile);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

        afterChange();
    }//GEN-LAST:event_buttonAppendActionPerformed


    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        File file = getSelectFile();
        if (DialogUtils.messageConfirmation("Delete file to '" + file.getName() + "'?")) {
            file.delete();
        }
        afterChange();
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOpenActionPerformed
        try {
            Desktop.getDesktop().edit(getSelectFile());
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }//GEN-LAST:event_buttonOpenActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonAppend;
    private javax.swing.JButton buttonOpen;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables
}
