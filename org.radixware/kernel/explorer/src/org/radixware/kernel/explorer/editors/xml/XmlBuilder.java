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

import java.util.ArrayList;

import org.apache.xmlbeans.SchemaAttributeModel;
import org.apache.xmlbeans.SchemaLocalAttribute;
import org.apache.xmlbeans.SchemaParticle;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

public class XmlBuilder {

    public XmlBuilder() {
    }

    public void buildNodeContent(XmlObject doc, ArrayList<Integer> inserts) {
        SchemaParticle model = doc.schemaType().getContentModel();
        buildNodeContent(doc, model, inserts);
    }

    public void buildNodeContent(XmlObject doc, SchemaParticle model, ArrayList<Integer> inserts) {
        if (model != null) {
            int l = inserts.size();
            int minO = model.getIntMinOccurs();
            if (minO != 0) {
                int type = model.getParticleType();
                switch (type) {
                    case SchemaParticle.ELEMENT: {
                        XmlCursor c = doc.newCursor();
                        c.toEndToken();
                        insertElement(null, doc, c, model, inserts);
                    }
                    break;
                    case SchemaParticle.CHOICE: {
                        if (model.getParticleChildren() != null) {
                            SchemaParticle part = model.getParticleChild(0);
                            buildNodeContent(doc, part, inserts);
                        }
                    }
                    break;
                    case SchemaParticle.ALL:
                    case SchemaParticle.SEQUENCE: {
                        SchemaParticle[] parties = model.getParticleChildren();
                        if (parties != null) {
                            for (SchemaParticle p : parties) {
                                buildNodeContent(doc, p, inserts);
                            }
                        }
                    }
                    break;
                }
            }
            if (inserts.size() > l) {
                int cur = inserts.size();
                int i = 1;
                while (i <= cur - l
                        && inserts.size() >= l) {
                    inserts.remove(inserts.size() - 1);
                    i++;
                }
            }
        }
    }

    public void buildNodeContent(ArrayList<XmlObject> s, XmlObject doc, XmlCursor pos, SchemaParticle model, ArrayList<Integer> inserts) {
        if (model != null) {
            int l = inserts.size();
            int minO = model.getIntMinOccurs();
            if (minO != 0 || pos != null) {
                int type = model.getParticleType();
                switch (type) {
                    case SchemaParticle.ELEMENT: {
                        insertElement(s, doc, pos, model, inserts);
                    }
                    break;
                    case SchemaParticle.CHOICE: {
                        if (model.getParticleChildren() != null) {
                            SchemaParticle part = model.getParticleChild(0);
                            buildNodeContent(s, doc, pos, part, inserts);
                        }
                    }
                    break;
                    case SchemaParticle.ALL:
                    case SchemaParticle.SEQUENCE: {
                        SchemaParticle[] parties = model.getParticleChildren();
                        if (parties != null) {
                            for (SchemaParticle p : parties) {
                                buildNodeContent(s, doc, pos, p, inserts);
                            }
                        }
                    }
                    break;
                }
            }
            if (inserts.size() > l) {
                int cur = inserts.size();
                int i = 1;
                while (i <= cur - l
                        && inserts.size() >= l) {
                    inserts.remove(inserts.size() - 1);
                    i++;
                }
            }
        }
    }

    private void insertElement(ArrayList<XmlObject> s, XmlObject doc, XmlCursor c, SchemaParticle model, ArrayList<Integer> inserts) {
        if (!inserts.contains(model.getType().hashCode())) {
            c.insertElement(model.getName());
            c = doc.newCursor();
            c.toChild(model.getName());
            XmlObject inserted = c.getObject();
            Integer hc = inserted.schemaType().hashCode();
            inserts.add(hc);
            buildAttributes(inserted);
            buildNodeContent(inserted, inserts);
            if (s != null) {
                s.add(inserted);
            }
        }
    }

    public void buildAttributes(XmlObject node) {
        SchemaAttributeModel attrs = node.schemaType().getAttributeModel();
        if (attrs != null) {
            SchemaLocalAttribute[] all = attrs.getAttributes();
            for (SchemaLocalAttribute a : all) {
                if (a.getUse() != SchemaLocalAttribute.PROHIBITED) {
                    if (a.getUse() == SchemaLocalAttribute.REQUIRED) {
                        XmlCursor c = node.newCursor();
                        c.toEndToken();
                        if (a.isFixed() || a.isDefault()) {
                            c.insertAttributeWithValue(a.getName(), a.getDefaultValue().getStringValue());
                        } else {
                            c.insertAttributeWithValue(a.getName(), XEditorTools.getInitialValue(a.getType()));
                        }
                    }
                }
            }
        }
    }
}
