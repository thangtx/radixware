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

package org.radixware.kernel.common.design.msdleditor.piece;

import org.radixware.schemas.msdl.EmbeddedLenDef;
import org.radixware.schemas.msdl.FixedLenDef;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.UnitPanel;
import org.radixware.kernel.common.design.msdleditor.field.panel.simple.ExtHexPanel;
import org.radixware.kernel.common.design.msdleditor.enums.EUnit;
import org.radixware.kernel.common.msdl.fields.parser.SmioCoder;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.exceptions.SmioException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlException;
import javax.swing.JOptionPane;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.radixware.kernel.common.msdl.enums.EEncoding;

/**
 * Delegate, used to get and set padding and units to and from padding and unit
 * widgets and to appropriate XSD-specified field definitions.
 *
 */
public class PadFiller {

    private final UnitPanel unitPanel;
    private final ExtHexPanel padPanel;
    private ICoderProvider cp;
    private final AbstractFieldModel field;
    /**
     * As FixedLenDef and EmbeddedLenDef have no direct predecessor, we have to
     * hold each type value here in order to accomplish our mission
     */
    private FixedLenDef fld;
    private EmbeddedLenDef eld;
    
    public interface ICoderProvider {
        SmioCoder getCoder();
    }

    public PadFiller(XmlObject PT, UnitPanel UP, ExtHexPanel HP, ICoderProvider CP, AbstractFieldModel F) throws SmioException {
        if (PT instanceof EmbeddedLenDef) {
            eld = (EmbeddedLenDef) PT;
            fld = null;
        } else if (PT instanceof FixedLenDef) {
            eld = null;
            fld = (FixedLenDef) PT;
        } else {
            throw new SmioException("PadFiller used in the wrong widget type");
        }

        unitPanel = UP;
        padPanel = HP;
        cp = CP;
        field = F;
    }

    public void fillWidgets() {
        String viewTypeStr = fld != null ? fld.getPadViewType() : eld.getPadViewType();
        EEncoding viewType = EEncoding.getInstanceForHexViewType(viewTypeStr);
        EEncoding parentViewType = EEncoding.getInstanceForHexViewType(field.getPadViewType(true));
        if (unitPanel.getViewedUnit() == EUnit.CHAR) {
            byte[] val1 = null;

            /*
             * Why one can't say if(null) in Java?!
             */
            String ch = fld != null ? fld.getPadChar() : eld.getPadChar();
            if (cp.getCoder() != null) {
                if (ch != null) {
                    try {
                        val1 = cp.getCoder().encode(ch);
                    } catch (SmioException ex) {
                        Logger.getLogger(field.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                byte[] val2 = null;
                String ch1 = field.getPadChar();
                if (ch1 != null) {
                    try {
                        val2 = cp.getCoder().encode(ch1);
                    } catch (SmioException ex) {
                        Logger.getLogger(field.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                padPanel.setValue(val1, val2, viewType, parentViewType);
            }
        } else {
            padPanel.setValue(fld != null ? fld.getPadBin() : eld.getPadBin(), field.getPadBin(), viewType, parentViewType);
        }
    }

    /*
     * I know, that one should be brutaly killed for such a code, but it's my
     * legacy, and I haven't manage to fix it yet
     */
    public void fillDefinition() {
        byte[] arr = padPanel.getValue();
        if (arr != null && arr.length > 0) {
            if (unitPanel.getViewedUnit() == EUnit.CHAR) {
                if (cp.getCoder() != null) {
                    try {
                        String ch = cp.getCoder().decode(ByteBuffer.wrap(arr));
                        EmbeddedLenDef ld = EmbeddedLenDef.Factory.newInstance();
                        ld.setPadChar(ch);
                        XmlObject.Factory.parse(ld.xmlText());
                        setPadChar(ch);
                    } catch (XmlException | SmioException | CharacterCodingException ex) {
                        JOptionPane.showMessageDialog(null, "Wrong pad");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Encoding not defined");
                }
            } else {
                setPadBin(arr);
            }
        } else {
            if (unitPanel.getViewedUnit() == EUnit.CHAR) {
                setPadChar(null);
            } else {
                setPadBin(null);
            }
        }
        setViewEncoding(padPanel.getViewEncoding());
    }

    /*
     * The following two functions could have been written in a single line each
     * if it was C/C++, not Java.
     */
    private void setPadChar(String ch) {
        if (fld != null) {
            fld.setPadChar(ch);
        } else {
            eld.setPadChar(ch);
        }
    }

    private void setPadBin(byte[] arr) {
        if (fld != null) {
            fld.setPadBin(arr);
        } else {
            eld.setPadBin(arr);
        }
    }
    
    private void setViewEncoding(EEncoding viewType) {
        if(viewType == null) {
            return;
        }
        if (fld != null) {
            fld.setPadViewType(viewType.getValue());
        } else {
            eld.setPadViewType(viewType.getValue());
        }
    }
}
