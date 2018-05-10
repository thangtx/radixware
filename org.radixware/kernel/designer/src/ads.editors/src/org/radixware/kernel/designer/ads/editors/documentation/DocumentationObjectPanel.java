package org.radixware.kernel.designer.ads.editors.documentation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.doc.AdsDocDef;
import org.radixware.kernel.common.defs.ads.doc.AdsDocMapDef;
import org.radixware.kernel.common.defs.ads.doc.AdsDocTopicDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.resources.RadixWareIcons;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.general.editors.OpenInfo;
import org.radixware.kernel.designer.common.general.utils.SwingUtils;

public final class DocumentationObjectPanel extends JPanel {

    private final AdsModule module;
    private final ArrayList<AdsDocDef> docObjects = new ArrayList<>();
    private final DefaultListModel listModel = new DefaultListModel();

    public DocumentationObjectPanel(AdsModule module) {

        this.module = module;
        initComponents();
        panelEditor.setLayout(new BorderLayout());

        // list
        fillDocObjects();
        fillModel();
        list.setModel(listModel);
        list.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList list, Object docObject, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, docObject, index, isSelected, cellHasFocus);
                setIcon(((AdsDocDef) docObject).getIcon().getIcon());
                return this;
            }
        });
        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                changeAfterSelectIndex();
            }
        });

        // button
        SwingUtils.InitButton(buttonAppendMap,
                RadixWareIcons.TECHNICAL_DOCUMENTATION.MAP.getIcon(),
                "Append dita map",
                panelList,
                KeyEvent.VK_INSERT);
        SwingUtils.InitButton(buttonAppendTopic,
                RadixWareIcons.TECHNICAL_DOCUMENTATION.TOPIC.getIcon(),
                "Append dita topic",
                panelList,
                KeyEvent.VK_INSERT,
                InputEvent.CTRL_DOWN_MASK);
        SwingUtils.InitButton(buttonRemove,
                RadixWareIcons.DELETE.DELETE.getIcon(),
                "Remove",
                panelList,
                KeyEvent.VK_DELETE);
        SwingUtils.InitButton(buttonPreview,
                RadixWareIcons.EDIT.VIEW.getIcon(),
                "Generate documentation by select object in lsit",
                panelList,
                KeyEvent.VK_P,
                InputEvent.CTRL_DOWN_MASK);

        update();
    }

    public void open(OpenInfo openInfo) {
        AdsDocDef def = (AdsDocDef) openInfo.getTarget();
        afterChange(def);
    }

    public void update() {

        // если запись не выбрана (или выбрана не праильно), то мы установим курсор на первую запись
        if (getSelectIndex() == -1 && listModel.getSize() >= 0) {
            setIndex(0);
        }

        // enabled
        buttonAppendMap.setEnabled(!module.isReadOnly());
        buttonAppendTopic.setEnabled(!module.isReadOnly());
        buttonRemove.setEnabled(!module.isReadOnly() && !isEmpty());
        buttonPreview.setEnabled(!isEmpty());
    }

    private boolean isEmpty() {
        return listModel.isEmpty();
    }

    // open editor
    private void openEditor(JPanel editor) {
        panelEditor.removeAll();
        if (editor != null) {
            panelEditor.add(editor, BorderLayout.CENTER);
            editor.addPropertyChangeListener("name", new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    repaint();
                    revalidate();
                }
            });
        }
        repaint();
        revalidate();
    }

    private void openEmptyEditor() {
        openEditor(null);
    }

    private void openMapEditor(AdsDocMapDef map) {
        openEditor(new AdsDocMapEditor(map));
    }

    private void openTopicEditor(AdsDocTopicDef topic) {
        openEditor(new AdsDocTopicEditor(topic));
    }

    // TODO: !!!
    // fill
    private void fillModel() {
        listModel.clear();
        for (AdsDocDef docObject : docObjects) {
            listModel.addElement(docObject);
        }
    }

    private void fillDocObjects() {

        docObjects.clear();
        Comparator comparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return o1.toString().compareTo(o2.toString());
            }
        };

        ArrayList<AdsDocDef> maps = new ArrayList<>();
        for (AdsDefinition def : module.getDefinitions()) {
            if (def instanceof AdsDocMapDef) {
                maps.add((AdsDocDef) def);
            }
        }

        Collections.sort(maps, comparator);
        docObjects.addAll(maps);

        ArrayList<AdsDocDef> topics = new ArrayList<>();
        for (AdsDefinition def : module.getDefinitions()) {
            if (def instanceof AdsDocTopicDef) {
                topics.add((AdsDocDef) def);
            }
        }
        Collections.sort(topics, comparator);
        docObjects.addAll(topics);
    }

    // change
    private void changeAfterSelectIndex() {
        AdsDocDef selectObject = getSelectObject();

        if (selectObject == null) {
            openEmptyEditor();
        }
        if (selectObject instanceof AdsDocMapDef) {
            openMapEditor((AdsDocMapDef) selectObject);
        }
        if (selectObject instanceof AdsDocTopicDef) {
            openTopicEditor((AdsDocTopicDef) selectObject);
        }

        update();
    }

    private void afterChange(AdsDocDef currentDef) {
        afterChange(docObjects.indexOf(currentDef));
    }

    private void afterChange(int selectIndex) {
        fillModel();
        setIndex(selectIndex);
        update();
    }

    private void addObject(AdsDocDef def) {
        docObjects.add(def);
        afterChange(def);
    }

    // index
    private int getSelectIndex() {
        return list.getSelectedIndex();
    }

    private int getLastIndex() {
        return docObjects.size() - 1;
    }

    private void setIndex(Integer index) {
        if (index > getLastIndex()) {
            index = getLastIndex();
        }
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(index);
    }

    private AdsDocDef getSelectObject() {
        int index = getSelectIndex();
        if ((index < 0) || (index > docObjects.size())) {
            return null;
        } else {
            return docObjects.get(getSelectIndex());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        DocObjectPane = new javax.swing.JSplitPane();
        panelList = new javax.swing.JPanel();
        jToolBar = new javax.swing.JToolBar();
        buttonAppendMap = new javax.swing.JButton();
        buttonAppendTopic = new javax.swing.JButton();
        buttonRemove = new javax.swing.JButton();
        buttonPreview = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();
        panelEditor = new javax.swing.JPanel();

        panelList.setMaximumSize(new java.awt.Dimension(500, 32767));
        panelList.setMinimumSize(new java.awt.Dimension(100, 100));
        panelList.setPreferredSize(new java.awt.Dimension(200, 400));
        panelList.setLayout(new java.awt.BorderLayout());

        jToolBar.setFloatable(false);
        jToolBar.setRollover(true);

        buttonAppendMap.setFocusable(false);
        buttonAppendMap.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAppendMap.setLabel(org.openide.util.NbBundle.getMessage(DocumentationObjectPanel.class, "DocumentationObjectPanel.buttonAppendMap.label")); // NOI18N
        buttonAppendMap.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonAppendMap.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonAppendMap.setName("buttonAppendMap"); // NOI18N
        buttonAppendMap.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonAppendMap.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAppendMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAppendMapActionPerformed(evt);
            }
        });
        jToolBar.add(buttonAppendMap);

        buttonAppendTopic.setFocusable(false);
        buttonAppendTopic.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonAppendTopic.setLabel(org.openide.util.NbBundle.getMessage(DocumentationObjectPanel.class, "DocumentationObjectPanel.buttonAppendTopic.label")); // NOI18N
        buttonAppendTopic.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonAppendTopic.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonAppendTopic.setName("buttonAppendTopic"); // NOI18N
        buttonAppendTopic.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonAppendTopic.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonAppendTopic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAppendTopicActionPerformed(evt);
            }
        });
        jToolBar.add(buttonAppendTopic);

        buttonRemove.setFocusable(false);
        buttonRemove.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonRemove.setLabel(org.openide.util.NbBundle.getMessage(DocumentationObjectPanel.class, "DocumentationObjectPanel.buttonRemove.label")); // NOI18N
        buttonRemove.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonRemove.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonRemove.setName("buttonRemove"); // NOI18N
        buttonRemove.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonRemove.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveActionPerformed(evt);
            }
        });
        jToolBar.add(buttonRemove);

        buttonPreview.setFocusable(false);
        buttonPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        buttonPreview.setLabel(org.openide.util.NbBundle.getMessage(DocumentationObjectPanel.class, "DocumentationObjectPanel.buttonPreview.label")); // NOI18N
        buttonPreview.setMaximumSize(new java.awt.Dimension(24, 24));
        buttonPreview.setMinimumSize(new java.awt.Dimension(24, 24));
        buttonPreview.setName("buttonPreview"); // NOI18N
        buttonPreview.setPreferredSize(new java.awt.Dimension(24, 24));
        buttonPreview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        buttonPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPreviewActionPerformed(evt);
            }
        });
        jToolBar.add(buttonPreview);

        panelList.add(jToolBar, java.awt.BorderLayout.PAGE_START);

        list.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(list);

        panelList.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        DocObjectPane.setLeftComponent(panelList);

        panelEditor.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        panelEditor.setMinimumSize(new java.awt.Dimension(200, 200));
        panelEditor.setPreferredSize(new java.awt.Dimension(582, 200));

        javax.swing.GroupLayout panelEditorLayout = new javax.swing.GroupLayout(panelEditor);
        panelEditor.setLayout(panelEditorLayout);
        panelEditorLayout.setHorizontalGroup(
            panelEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 588, Short.MAX_VALUE)
        );
        panelEditorLayout.setVerticalGroup(
            panelEditorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );

        DocObjectPane.setRightComponent(panelEditor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DocObjectPane, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DocObjectPane, javax.swing.GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPreviewActionPerformed
        //RadixdocOptions options = new RadixdocOptions.Factory.
    }//GEN-LAST:event_buttonPreviewActionPerformed

    private void buttonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRemoveActionPerformed
        AdsDocDef def = docObjects.remove(getSelectIndex());
        if (DialogUtils.messageConfirmation("Delete Documentation Definition '" + def.getName() + "'?")) {
            def.delete();
            afterChange(getSelectIndex());
        }
    }//GEN-LAST:event_buttonRemoveActionPerformed

    private void buttonAppendTopicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAppendTopicActionPerformed
        addObject(AdsDocTopicDef.Factory.newInstation(module));
    }//GEN-LAST:event_buttonAppendTopicActionPerformed

    private void buttonAppendMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonAppendMapActionPerformed
        addObject(AdsDocMapDef.Factory.newInstation(module));
    }//GEN-LAST:event_buttonAppendMapActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane DocObjectPane;
    private javax.swing.JButton buttonAppendMap;
    private javax.swing.JButton buttonAppendTopic;
    private javax.swing.JButton buttonPreview;
    private javax.swing.JButton buttonRemove;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar;
    private javax.swing.JList list;
    private javax.swing.JPanel panelEditor;
    private javax.swing.JPanel panelList;
    // End of variables declaration//GEN-END:variables
}
