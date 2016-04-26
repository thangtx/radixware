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

/*
 * AdsXmlSchemePublishingWizardStep1Panel.java
 *
 * Created on Jan 14, 2011, 3:20:56 PM
 */

package org.radixware.kernel.designer.ads.editors.creation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.openide.util.ChangeSupport;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinitionIcon;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.designer.common.dialogs.chooseobject.AbstractItemRenderer;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;


public class AdsXmlSchemePublishingWizardStep1Panel extends javax.swing.JPanel {

    private class XsdItem implements Comparable<XsdItem> {

        String name;
        String dir;
        String namespace;
        ERuntimeEnvironmentType environment = ERuntimeEnvironmentType.COMMON;

        XsdItem(final File file, final String layerName){
            this.name = file.getName();
            String dirstr = file.getParent();
            int index = dirstr.indexOf(layerName);
            this.dir = dirstr.substring(index);

            if (dirstr.contains("explorer")){
                this.environment = ERuntimeEnvironmentType.EXPLORER;
            } else if (dirstr.contains("server")){
                this.environment = ERuntimeEnvironmentType.SERVER;
            }

            this.namespace = "";
            try {
                XmlObject asXml = XmlObject.Factory.parse(file);
                this.namespace = XmlUtils.getTargetNamespace(asXml);
            } catch (XmlException ex){
                DialogUtils.messageError(ex);
            } catch (IOException ex1){
                DialogUtils.messageError(ex1);
            }
        }

        @Override
        public int compareTo(XsdItem o) {
            return this.name.compareTo(o.name);
        }

        @Override
        public String toString() {
            return this.namespace;
        }

    }

    /** Creates new form AdsXmlSchemePublishingWizardStep1Panel */
    public AdsXmlSchemePublishingWizardStep1Panel() {
        initComponents();
        list.setCellRenderer(new AbstractItemRenderer(list) {

            @Override
            public String getObjectName(Object object) {
                if (object instanceof XsdItem){
                    return ((XsdItem) object).name;
                }
                return "";
            }

            @Override
            public String getObjectLocation(Object object) {
                if (object instanceof XsdItem){
                    return ((XsdItem) object).dir;
                }
                return "";
            }

            @Override
            public RadixIcon getObjectIcon(Object object) {
                return AdsDefinitionIcon.XML_SCHEME;
            }

            @Override
            public RadixIcon getObjectLocationIcon(Object object) {
                return null;
            }
        });
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                AdsXmlSchemePublishingWizardStep1Panel.this.changeSupport.fireChange(); 
            }
        }); 
    }

    private ChangeSupport changeSupport = new ChangeSupport(this);

    public void addChangeListener(ChangeListener listener){
        changeSupport.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener){
        changeSupport.removeChangeListener(listener); 
    }

    private class XsdDirFilter implements FileFilter{

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && pathname.getName().equals("xsd");
        }

    };

    private class NotXsdDirFilter implements FileFilter{

        @Override
        public boolean accept(File pathname) {
            return pathname.isDirectory() && !pathname.getName().equals("xsd");
        }

    }

    private class SchemeFilter implements FileFilter {

        @Override
        public boolean accept(File pathname) {
            return pathname.isFile() && 
                     (pathname.getName().endsWith(".xsd") || pathname.getName().endsWith(".wsdl"));
        }

    }

    public void open(AdsXmlSchemePublishingCreature creature){
        DefaultListModel model = new DefaultListModel();
        list.setModel(model);
        Module module = creature.getModule();
        File layerDir = module.getSegment().getLayer().getDirectory();
        if (layerDir != null){
            List<File> xsdList = getXsdDirList(layerDir);
            String layerName = layerDir.getName();
            List<XsdItem> forModel = new ArrayList<XsdItem>();
            for (File f : xsdList){
                forModel.add(new XsdItem(f, layerName));
            }
            Collections.sort(forModel);
            for (XsdItem item : forModel){
                model.addElement(item); 
            }
        }
    }

    private List<File> getXsdDirList(File layerDir){
        List<File> result = new ArrayList<File>();
        File[] xsdDirList = layerDir.listFiles(new XsdDirFilter());
        if (xsdDirList.length > 0){
            result.addAll(Arrays.asList(xsdDirList[0].listFiles(new SchemeFilter())));
        }
        File[] other = layerDir.listFiles(new NotXsdDirFilter());
        for (File f : other){
            if (!f.getName().equals("test")){
                result.addAll(getXsdDirList(f));
            }
        }
        return result;
    }

    public String getPublishingUrl(){
        return list.getSelectedValue().toString();
    }

    public ERuntimeEnvironmentType getTargetEnvironment(){
        Object value = list.getSelectedValue();
        if (value != null && value instanceof XsdItem){
            return ((XsdItem) value).environment;
        }
        return ERuntimeEnvironmentType.COMMON;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        list = new javax.swing.JList();

        jScrollPane1.setViewportView(list);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList list;
    // End of variables declaration//GEN-END:variables

    public boolean isComplete(){
        return list.getSelectedIndex() > -1 && list.getSelectedIndex() < list.getModel().getSize();
    }

}
