/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.ads.editors.documentation;

import javax.swing.JTextField;
import org.radixware.kernel.common.defs.ads.doc.AdsDocDef;
import org.radixware.kernel.designer.common.editors.RadixObjectEditor;
import org.radixware.kernel.designer.common.general.utils.SwingUtils;

/**
 *
 * @author dkurlyanov
 */
public abstract class AdsDocEditor<T extends AdsDocDef> extends RadixObjectEditor<T> {

    protected AdsDocEditor(T radixObject) {
        super(radixObject);
        this.addPropertyChangeListener("name", null);
    }

    protected void changeName(JTextField textField, String name) {
        getRadixObject().setName(textField.getText());
        SwingUtils.checkValidName(textField, getRadixObject());
        setName(getName());   // кастыль для того чтобы стробнуть PropertyChangeListener(name)
    }
}
