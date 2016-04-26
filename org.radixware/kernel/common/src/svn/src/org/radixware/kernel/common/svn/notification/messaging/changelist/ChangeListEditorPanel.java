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

package org.radixware.kernel.common.svn.notification.messaging.changelist;

import java.awt.Color;
import java.awt.Component;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.radixware.kernel.common.dialogs.IDialogStyler;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchValueError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;


public class ChangeListEditorPanel extends javax.swing.JPanel {

    public static abstract class MessageElement {

        private Map<EIsoLanguage, String> messages = new EnumMap<>(EIsoLanguage.class);

        public abstract String getDisplayName();

        public String getMessage(EIsoLanguage lang) {
            String result = messages.get(lang);
            return result == null ? "" : result;
        }

        public void setMessage(EIsoLanguage lang, String message) {
            messages.put(lang, message == null ? "" : message);
        }

        protected abstract List<Layer> getLayers();

        @Override
        public String toString() {
            return getDisplayName();
        }

        public Set<EIsoLanguage> availableLanguages() {
            return messages.keySet();
        }

        public boolean isReadOnly() {
            return false;
        }
    }

    private class LayerListElement {

        private final Layer layer;

        private LayerListElement(Layer layer) {
            this.layer = layer;
        }

        @Override
        public String toString() {
            return layer.getName();
        }
    }

    private class LayerListModel implements ListModel {

        private List<LayerListElement> layers;

        public LayerListModel(List<Layer> layers) {
            if (layers == null || layers.isEmpty()) {
                this.layers = Collections.emptyList();
            } else {
                this.layers = new ArrayList<LayerListElement>(layers.size());
                Branch.LayerSorter sorter = layers.get(0).getBranch().new LayerSorter(layers);
                for (Layer l : sorter.getInOrder()) {
                    this.layers.add(new LayerListElement(l));
                }
            }
        }

        void findAndSelectLayer(Layer layer) {
            int index2 = -1;
            for (int i = 0; i < layers.size(); i++) {
                final LayerListElement e = layers.get(i);
                if (e.layer == layer) {
                    index2 = i;
                    break;
                }
            }

            if (index2 >= 0) {
                final int index = index2;
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lstLayers.setSelectedIndex(index);
                        lstLayers.ensureIndexIsVisible(index);
                    }
                });
            }
        }

        @Override
        public int getSize() {
            return this.layers.size();
        }

        @Override
        public Object getElementAt(int index) {
            return layers.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
    private final Object lock = new Object();
    private IDialogStyler styler;

    /** Creates new form ChangeListEditorPanel */
    public ChangeListEditorPanel() {
        this(null);
    }

    public ChangeListEditorPanel(IDialogStyler styler) {
        initComponents();
        this.styler = styler;
        edFileChanges.setContentType("text/html");
        lstLayers.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
                    if (lstLayers.isSelectedIndex(i)) {
                        final int index = i;
                        SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                setSelectedLayer(((LayerListElement) lstLayers.getModel().getElementAt(index)).layer);
                            }
                        });
                        break;
                    }
                }
            }
        });
        lstMessageElement.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateWithCurrentMessageElement();
            }
        });
        lstMessageElement.setCellRenderer(renderer);
        lstLayers.setCellRenderer(renderer);

    }

    private void setSelectedLayer(Layer layer) {
        synchronized (lock) {
            if (layer != null) {
                selectedLayer = layer;
                selectedChangeList = layerInfo.get(layer);
                if (selectedChangeList != null) {
                    fillFileChanges();
                }
            } else {
                edFileChanges.setText("");
            }
        }
    }

    private void fillFileChanges() {
        edFileChanges.setText(selectedChangeList.getFileChangesHtml());
        edFileChanges.moveCaretPosition(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        tbPages = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstMessageElement = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstLayers = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        edFileChanges = new javax.swing.JEditorPane();
        jLabel2 = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        jSplitPane2.setDividerLocation(200);
        jSplitPane2.setDividerSize(6);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Message"));

        jSplitPane3.setDividerLocation(150);
        jSplitPane3.setDividerSize(6);
        jSplitPane3.setRightComponent(tbPages);

        jScrollPane2.setViewportView(lstMessageElement);

        jSplitPane3.setLeftComponent(jScrollPane2);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSplitPane2.setTopComponent(jPanel1);

        jSplitPane1.setDividerLocation(152);
        jSplitPane1.setDividerSize(6);

        jScrollPane1.setViewportView(lstLayers);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane3.setViewportView(edFileChanges);

        jSplitPane1.setRightComponent(jScrollPane3);

        jLabel2.setText("File changes:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE))
        );

        jSplitPane2.setRightComponent(jPanel2);

        add(jSplitPane2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane edFileChanges;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JList lstLayers;
    private javax.swing.JList lstMessageElement;
    private javax.swing.JTabbedPane tbPages;
    // End of variables declaration//GEN-END:variables
    private Map<Layer, ChangeList> layerInfo;
    private ChangeList selectedChangeList = null;
    private Layer selectedLayer = null;

    private class Renderer implements ListCellRenderer {

        private final DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        private Color defaultColor = null;

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (defaultColor == null) {
                defaultColor = c.getForeground();
            }
            c.setForeground(defaultColor);

            if (value instanceof LayerListElement) {
                ChangeList info = layerInfo.get(((LayerListElement) value).layer);
                if (info.hasAnyFileChanges()) {
                    c.setForeground(Color.BLUE);
                }
            } else if (value instanceof MessageElement) {
                for (Map.Entry<EIsoLanguage, String> m : ((MessageElement) value).messages.entrySet()) {
                    if (m.getValue() != null && !m.getValue().isEmpty()) {
                        c.setForeground(Color.BLUE);
                        break;
                    }
                }
            }
            return c;
        }
    }
    private final Renderer renderer = new Renderer();

    private class MessageElementListModel implements ListModel {

        private final List<MessageElement> list;

        public MessageElementListModel(List<MessageElement> list) {
            this.list = list;
        }

        @Override
        public int getSize() {
            return list.size();
        }

        @Override
        public Object getElementAt(int index) {
            return list.get(index);
        }

        @Override
        public void addListDataListener(ListDataListener l) {
        }

        @Override
        public void removeListDataListener(ListDataListener l) {
        }
    }
    private final List<MessagePanel> panels = new LinkedList<>();

    public void open(Map<Layer, ChangeList> layerInfo, List<MessageElement> messages, Set<EIsoLanguage> availableLanguages) {
        tbPages.removeAll();

        Map<Layer, MessageElement> layer2MessageElement = new HashMap<>();
        List<MessageElement> lost = new LinkedList<>();
        for (MessageElement e : messages) {
            List<Layer> layers = e.getLayers();
            if (layers == null || layers.isEmpty()) {
                lost.add(e);
            } else {
                layers = layers.get(0).getBranch().new LayerSorter(layers).getInOrder();
                Layer bottom = layers.get(0);
                if (layer2MessageElement.containsKey(bottom)) {
                    lost.add(e);
                } else {
                    layer2MessageElement.put(bottom, e);
                }
            }
        }
        List<MessageElement> sortedMessages = new ArrayList<>(messages.size());
        if (!layer2MessageElement.isEmpty()) {
            List<Layer> list = new ArrayList<>(layer2MessageElement.keySet());
            List<Layer> layersInOrder = list.get(0).getBranch().new LayerSorter(list).getInOrder();
            for (Layer l : layersInOrder) {
                sortedMessages.add(layer2MessageElement.get(l));
            }
        }
        for (MessageElement e : lost) {
            sortedMessages.add(e);
        }


        lstMessageElement.setModel(new MessageElementListModel(sortedMessages));

        List<EIsoLanguage> langList = new ArrayList<>(availableLanguages);

        Locale locale = Locale.getDefault();

        EIsoLanguage lang;
        try {
            lang = EIsoLanguage.getForValue(locale.getLanguage());
        } catch (NoConstItemWithSuchValueError e) {
            lang = EIsoLanguage.ENGLISH;
        }

        if (langList.contains(lang)) {
            langList.remove(lang);
        }

        langList.add(0, lang);

        if (messages != null) {
            for (MessageElement e : messages) {
                for (EIsoLanguage l : e.availableLanguages()) {
                    if (!langList.contains(l)) {
                        langList.add(l);
                    }
                }
            }
        }
        for (EIsoLanguage l : langList) {
            MessagePanel p = new MessagePanel(styler, l);
            tbPages.addTab(l.getName(), p);
            panels.add(p);
        }
        this.layerInfo = layerInfo == null ? new HashMap<Layer, ChangeList>() : layerInfo;

        final LayerListModel listModel = new LayerListModel(new ArrayList<>(this.layerInfo.keySet()));

        if (!this.layerInfo.isEmpty()) {
            selectedLayer = new ArrayList<>(this.layerInfo.keySet()).get(0);
        } else {
            if (!listModel.layers.isEmpty()) {
                selectedLayer = listModel.layers.get(0).layer;
            } else {
                selectedLayer = null;
            }
        }
        this.lstLayers.setModel(listModel);
        if (this.layerInfo.size() > 0 && selectedLayer != null) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    setSelectedLayer(selectedLayer);
                    listModel.findAndSelectLayer(selectedLayer);
                }
            });
        } else {
            setSelectedLayer(null);
        }
        lstMessageElement.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                updateWithCurrentMessageElement();
            }
        });
        if (lstMessageElement.getModel().getSize() > 0) {
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    lstMessageElement.setSelectedIndex(0);
                }
            });
        }
    }

    private void updateWithCurrentMessageElement() {
        MessageElement e = (MessageElement) lstMessageElement.getSelectedValue();
        if (e != null) {
            for (MessagePanel p : panels) {
                p.open(e);
            }
        }
    }
}
