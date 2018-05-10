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
package org.radixware.kernel.common.defs.uds.files;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.uds.IUdsFileDefinition;
import org.radixware.kernel.common.defs.uds.UdsDefinitionIcon;
import org.radixware.kernel.common.defs.uds.userfunc.UdsDummyUserFuncDef;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.XmlFormatter;



public class UdsXmlFile extends UdsFile {
    private final UdsFileDefinitions udsDefinitions;
    
    public UdsXmlFile(String name) {
        super(name);
        udsDefinitions = new UdsFileDefinitions();
    }



    @Override
    public void save() throws IOException {
        File file = getFile();
        try {
            XmlObject xmlObject = XmlObject.Factory.parse(file);
            UdsFileDefinitions defenitions = getUdsDefinitions();
            for (RadixObject def : defenitions) {
                if (def instanceof IUdsFileDefinition) {
                    IUdsFileDefinition uf = (IUdsFileDefinition) def;
                    XmlObject[] objects = xmlObject.selectPath(uf.getXPath());
                    if (objects != null && objects.length > 0){
                        uf.appendTo(objects[0]);
                    }
                }
            }
            
            
            XmlFormatter.save(xmlObject, file);
            setFileLastModifiedTime(file.lastModified());
        } catch (XmlException ex) {
            Logger.getLogger(UdsDummyUserFuncDef.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setEditState(EEditState.NONE);
    }

//    private Map<UdsDefinition, Element> findElments(XmlObject obj, Map<String, UdsDefinition> defenitions) {
//        Element e = XmlUtils.findFirstElement(obj.getDomNode());
//        Map<UdsDefinition, Element> elements = new HashMap<>();
//        findNodes(e, defenitions, elements);
//        return elements;
//    }
//
//    private void findNodes(final Element node, Map<String, UdsDefinition> defenitions, Map<UdsDefinition, Element> result) {
//        String xPath = UdsUtils.getXPath(node);
//        if (defenitions.isEmpty()){
//            return;
//        }
//        if (defenitions.containsKey(xPath)){
//            UdsDefinition def = defenitions.get(xPath);
//            defenitions.remove(xPath);
//            result.put(def, node);
//        }
//        for (Element child : XmlUtils.getChildElements(node)) {
//            findNodes(child, defenitions, result);
//        }
//    }

    public UdsFileDefinitions getUdsDefinitions() {
            return udsDefinitions;
    }

    public class UdsFileDefinitions extends UdsFileRadixObjects {

        public UdsFileDefinitions() {
            super(UdsXmlFile.this);
        }
       
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getUdsDefinitions().visit(visitor, provider);
    }

    @Override
    public boolean isSaveable() {
        return true;
    }
    
    @Override
    public AdsDefinition findById(Id id) {
        return getUdsDefinitions().findById(id);
    }
    
    @Override
    public RadixIcon getIcon() {
        return UdsDefinitionIcon.XML_FILE;
    }
}
