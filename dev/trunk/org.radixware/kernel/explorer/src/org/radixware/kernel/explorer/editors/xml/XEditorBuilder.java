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

package org.radixware.kernel.explorer.editors.xml;

import javax.xml.namespace.QName;
import org.apache.xmlbeans.SchemaAnnotation;
import org.apache.xmlbeans.SchemaLocalElement;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.client.meta.mask.EditMask;
import org.radixware.kernel.common.client.meta.mask.EditMaskConstSet;
import org.radixware.kernel.common.client.meta.mask.EditMaskDateTime;
import org.radixware.kernel.common.client.meta.mask.EditMaskInt;
import org.radixware.kernel.common.client.meta.mask.EditMaskList;
import org.radixware.kernel.common.client.meta.mask.EditMaskNum;
import org.radixware.kernel.common.client.meta.mask.EditMaskStr;
import org.radixware.kernel.common.client.meta.mask.EditMaskBool;
import org.radixware.kernel.common.client.meta.mask.EditMaskFilePath;
import org.radixware.kernel.common.client.meta.mask.EditMaskTimeInterval;


import org.radixware.kernel.common.types.Id;


import org.radixware.kernel.explorer.editors.valeditors.*;

public class XEditorBuilder {

    static public final String EDIT_MASK_TAG = "EditMask";
    static public final String CLASS_TAG = "class";
    static public final String CLASS_ID_ATTR = "classId";
    static public final String PRESENT_TAG = "presentation";
    static private final String POINT = ".";

    public static void setupEditorWithPresentation(ValEditor editor, XmlObject presentation) {
        if (presentation != null
                && editor != null) {
            XmlObject prop;
            // isMemo
            if (editor instanceof ValStrEditor) {
                prop = XElementTools.getAttribute(presentation, "IsMemo");
                if (prop != null) {
                    ValStrEditor e = (ValStrEditor) editor;
                    if (prop.getDomNode().getNodeValue() != null
                            && prop.getDomNode().getNodeValue().equals("true")) {
                        e.addMemo();
                    }
                }
            }
            //isReadonly
            prop = XElementTools.getAttribute(presentation, "IsReadonly");
            if (prop != null) {
                String v = prop.getDomNode().getNodeValue();
                if (v != null) {
                    if (v.equals(Boolean.toString(true))) {
                        editor.setReadOnly(true);
                    } else {
                        editor.setReadOnly(false);
                    }
                }
            }
            //isNotNull
            prop = XElementTools.getAttribute(presentation, "IsNotNull");
            if (prop != null) {
                String v = prop.getDomNode().getNodeValue();
                if (v != null) {
                    editor.setMandatory(Boolean.valueOf(v));
                }
            }
        }
    }

    public static Id getConstSetID(SchemaAnnotation a) {
        if (a != null) {
            XmlObject[] inf = a.getApplicationInformation();
            for (XmlObject x : inf) {
                if (x.getDomNode().getLocalName().equals("appinfo")) {
                    XmlCursor k = x.newCursor();
                    if (k.toFirstChild()
                            && k.getObject().getDomNode().getLocalName().equals(CLASS_TAG)) {//<class>
                        String val = k.getAttributeText(new QName(CLASS_ID_ATTR));
                        if (val!=null){
                            return Id.Factory.loadFrom(val);
                        }
                        x = k.getObject();
                        val = k.getTextValue();
                        if (val != null
                                && !val.isEmpty()) {
                            int point = val.lastIndexOf(POINT);
                            if (point != -1) {
                                return Id.Factory.loadFrom(val.substring(point + 1));
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static XmlObject getPresentationTag(SchemaAnnotation sa) {
        if (sa != null) {
            XmlObject[] inf = sa.getApplicationInformation();
            for (XmlObject x : inf) {
                if (x.getDomNode().getLocalName().equals("appinfo")) {
                    XmlCursor k = x.newCursor();
                    if (k.toChild(PRESENT_TAG)) {
                        return k.getObject();
                    }
                }
            }
        }
        return null;
    }

    public static ValConstSetEditor getConstSetEditor(IClientEnvironment environment, RadEnumPresentationDef def) {
        EditMaskConstSet mask = new EditMaskConstSet(def.getId(), null, null, null);
        ValConstSetEditor edit = new ValConstSetEditor(environment, null, mask, false, false);
        return edit;
    }

    public static EditMask getMaskFromPresentation(XmlObject presentation) {
        if (presentation != null) {
            XmlCursor k = presentation.newCursor();
            if (k.toChild("editMask")) {
                XmlObject mask = k.getObject();
                k = mask.newCursor();
                if (k.toChild(EDIT_MASK_TAG)) {
                    mask = k.getObject();
                    k = mask.newCursor();
                    if (k.toFirstChild()) {
                        String maskText = k.xmlText();
                        try {
                            EditMask result = EditMask.loadFrom(maskText);
                            return result;
                        } catch (XmlException xmle) {
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static SchemaAnnotation getElementAnnotation(XTreeElement element) {
//        XmlCursor cr = doc.newCursor();
//        cr.toParent();
//        if (cr.getObject() != null) {
        if (element.parent() != null) {
            //XmlObject parent = cr.getObject();
            SchemaType t = ((XTreeElement) element.parent()).getSchemaType();
            SchemaParticle c = t.getContentModel();
            if (c != null) {
                SchemaParticle[] parties = c.getParticleChildren();
                String local = element.getNode().getDomNode().getLocalName();
                return findParticle(parties, local);
            }
        }
        return null;
    }

    private static SchemaAnnotation findParticle(SchemaParticle[] p, String local) {
        if (p != null) {
            for (int i = 0; i <= p.length - 1; i++) {
                if (p[i].getParticleType() == SchemaParticle.ELEMENT) {
                    if (p[i].getName().getLocalPart().equals(local)) {
                        SchemaLocalElement l = (SchemaLocalElement) p[i];
                        SchemaAnnotation a = l.getAnnotation();
                        if (a != null) {
                            return a;
                        } else {
                            SchemaType localElementType = l.getType();
                            if (localElementType != null) {
                                return localElementType.getAnnotation();
                            }
                        }
                    }
                } else {
                    SchemaAnnotation res = findParticle(p[i].getParticleChildren(), local);
                    if (res != null) {
                        return res;
                    }
                }
            }
        }
        return null;
    }

    public static String getDocumentation(SchemaAnnotation a) {
        if (a != null) {
            XmlObject[] inf = a.getUserInformation();
            for (XmlObject x : inf) {
                if (x.getDomNode().getLocalName().equals("documentation")) {
                    XmlCursor k = x.newCursor();
                    String text = k.getTextValue();
                    return text;
                }
            }
        }
        return "";
    }

    static public ValEditor getEditorByMask(IClientEnvironment environment, EditMask mask) {
        
        ValEditor edit = null;
        if (mask != null) {
            if (mask instanceof EditMaskStr) {
                edit = new ValStrEditor(environment, null, (EditMaskStr) mask, false, false);
            }
            if (mask instanceof EditMaskInt) {
                edit = new ValIntEditor(environment, null, (EditMaskInt) mask, false, false);
            }
            if (mask instanceof EditMaskNum) {
                edit = new ValNumEditor(environment, null, (EditMaskNum) mask, false, false);
            }
            if (mask instanceof EditMaskDateTime) {
                edit = new ValDateTimeEditor(environment, null, (EditMaskDateTime) mask, false, false);
            }
            if (mask instanceof EditMaskTimeInterval) {
                edit = new ValTimeIntervalEditor(environment, null, (EditMaskTimeInterval) mask, false, false);
            }
            if (mask instanceof EditMaskList) {
                edit = new ValListEditor(environment, null, (EditMaskList) mask, false, false);
            }
            if (mask instanceof EditMaskConstSet) {
                edit = new ValConstSetEditor(environment, null, (EditMaskConstSet) mask, false, false);
            }
            if (mask instanceof EditMaskBool) {
                edit = new AdvancedValBoolEditor(environment, null, (EditMaskBool) mask, false, false);
            }
            if (mask instanceof EditMaskFilePath) {
                edit = new ValFilePathEditor(environment, null, (EditMaskFilePath) mask, false, false);
            }
        }
        return edit==null ? new ValStrEditor(environment, null, new EditMaskStr(), false, false) : edit;
    }
}
