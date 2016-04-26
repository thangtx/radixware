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

package org.radixware.kernel.utils.keyStoreAdmin.utils;

import java.awt.Component;
import java.net.URL;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.radixware.kernel.common.ssl.KeystoreDesKeyEntry;
import org.radixware.kernel.common.ssl.KeystoreEntry;
import org.radixware.kernel.common.ssl.KeystoreRsaKeyEntry;
import org.radixware.kernel.common.ssl.KeystoreTrustedCertificateEntry;
import org.radixware.kernel.common.utils.Hex;
import org.radixware.kernel.utils.keyStoreAdmin.KeyStoreAdminException;



public class KeyStoreEntryTreeRenderer extends DefaultTreeCellRenderer{
    private final int ICON_WIDTH = 16;
    private final int ICON_HEIGHT = 16;
    private ImageIcon certificateIcon = null;
    private ImageIcon trustedCertificateIcon = null;
    private ImageIcon keyIcon = null;
    private ImageIcon certificateChainIcon = null;
    private ImageIcon keystoreIcon = null;

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus){

        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        Object object = node.getUserObject();
        if (object instanceof KeystoreRsaKeyEntry){
            setText(rsaKeyEntryToString((KeystoreRsaKeyEntry)object));
            setIcon(getKeyIcon());
        } else if (object instanceof KeystoreTrustedCertificateEntry){
            setText(trustedCertificateEntryToString((KeystoreTrustedCertificateEntry)object));
            setIcon(getTrustedCertificateIcon());
        } else if (object instanceof KeystoreDesKeyEntry){
            setText(desKeyEntryToString((KeystoreDesKeyEntry)object));
            setIcon(getKeyIcon());
        } else if (object instanceof RSAPrivateKey){
            setText(privateKeyToString((RSAPrivateKey)object));
        } else if (object instanceof Certificate[]){
            setText(certificateChainToString((Certificate[])object));
            setIcon(getCertificateChainIcon());
        } else if (object instanceof X509Certificate){
            String certificateTitle = "Certificate";
            DefaultMutableTreeNode root = (DefaultMutableTreeNode)tree.getModel().getRoot();
            TreePath path = tree.getPathForRow(row);
            if (path!=null){
                DefaultMutableTreeNode parent = KeyStoreEntryTreeRenderer.getParentKeyStoreEntryNode(root, path);
                if (parent.getUserObject() instanceof KeystoreTrustedCertificateEntry)
                    certificateTitle = "Trusted certificate";
                else if (parent.getUserObject() instanceof KeystoreRsaKeyEntry){
                    DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                    if (currentNode.getParent().getIndex(currentNode)==0)
                        certificateTitle = "Own certificate";
                    else
                        certificateTitle = "CA certificate";
                }
            }
            setText(x509certificateToString((X509Certificate)object, certificateTitle));
            setIcon(getCertificateIcon());
        } else if (object instanceof KeyStoreEntryTreeRenderer.KeystoreTreeItem){
            setText(((KeyStoreEntryTreeRenderer.KeystoreTreeItem)object).getName());
            setIcon(getKeystoreIcon());
        } else if (object instanceof String){
            setText((String)object);
        }

        return this;
    }

    private String privateKeyToString(RSAPrivateKey key){
        return "Private key (format: "+key.getFormat()+", length: "+Integer.toString(key.getModulus().toString(16).length()/2*8)+")";
    }

    private String certificateChainToString(Certificate[] chain){
        return "Certificate chain ("+(chain!=null ? Integer.toString(chain.length) : "null")+" certificate"+(chain.length==1 ? "" : "(s)")+")";
    }

    private String x509certificateToString(X509Certificate certificate, String certificateTitle){
        return certificateTitle+" (serial number: "+certificate.getSerialNumber().toString()+")";
    }

    private String rsaKeyEntryToString(KeystoreRsaKeyEntry entry){
        int keyLength = 0;
        String algorithm = "";
        if (entry.getCertificateChain().length>0){
            PublicKey publicKey = (PublicKey)entry.getCertificateChain()[0].getPublicKey();
            algorithm = publicKey.getAlgorithm();
            if (publicKey instanceof RSAPublicKey) //avoid exception if key algorithm is other than RSA
                keyLength = ((RSAPublicKey)publicKey).getModulus().toString(16).length()/2*8;
        }
        return entry.getAlias()+" (RSA key entry"+(algorithm.length()>0 ? ", algorithm: "+algorithm : "")+(keyLength==0 ? "" : ", length: "+Integer.toString(keyLength))+")";
    }
    
    private String desKeyEntryToString(KeystoreDesKeyEntry entry){
        String sCheckValue = null;
        if (entry.getKey()!=null){
            try{
                final byte[] checkValue = Crypto.calc3desKeyCheckValue(entry.getKey());
                sCheckValue = Hex.encode(checkValue).substring(0, 6);
            } catch (KeyStoreAdminException e){
            }
        }
        return entry.getAlias()+" (3DES key entry)"+(sCheckValue!=null ? ", check value: "+sCheckValue : "");
    }

    private String trustedCertificateEntryToString(KeystoreTrustedCertificateEntry entry){
        return entry.getAlias()+" (trusted certificate entry)";
    }

    public static DefaultMutableTreeNode getParentKeyStoreEntryNode(DefaultMutableTreeNode root, TreePath path){
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode)path.getLastPathComponent();
        while (parent!=root && !(parent.getUserObject() instanceof KeystoreEntry)){
            path = path.getParentPath();
            parent = (DefaultMutableTreeNode)path.getLastPathComponent();
        }
        return parent;
    }

    private ImageIcon getCertificateIcon(){
        if (certificateIcon==null)
            certificateIcon = getResourceIcon("/org/radixware/kernel/utils/keyStoreAdmin/resources/img/certificate.png");
        return certificateIcon;
    }

    private ImageIcon getTrustedCertificateIcon(){
        if (trustedCertificateIcon==null)
            trustedCertificateIcon = getResourceIcon("/org/radixware/kernel/utils/keyStoreAdmin/resources/img/trusted certificate.png");
        return trustedCertificateIcon;
    }

    private ImageIcon getKeyIcon(){
        if (keyIcon==null)
            keyIcon = getResourceIcon("/org/radixware/kernel/utils/keyStoreAdmin/resources/img/keypair.png");
        return keyIcon;
    }

    private ImageIcon getCertificateChainIcon(){
        if (certificateChainIcon==null)
            certificateChainIcon = getResourceIcon("/org/radixware/kernel/utils/keyStoreAdmin/resources/img/certificate chain.png");
        return certificateChainIcon;
    }

    private ImageIcon getKeystoreIcon(){
        if (keystoreIcon==null)
            keystoreIcon = getResourceIcon("/org/radixware/kernel/utils/keyStoreAdmin/resources/img/keystore.png");
        return keystoreIcon;
    }

    private ImageIcon getResourceIcon(String resourcePath){
        URL url = getClass().getResource(resourcePath);
        if (url==null)
            return null;
        ImageIcon tmpIcon = new ImageIcon(url);
        return new ImageIcon(tmpIcon.getImage().getScaledInstance(ICON_WIDTH, ICON_HEIGHT, 0));
    }

    public static class KeystoreTreeItem{
        private final String name;

        public KeystoreTreeItem(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }
    }
}