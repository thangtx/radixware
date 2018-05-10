/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.design.msdleditor;

import org.radixware.kernel.common.msdl.MsdlField;
import org.radixware.kernel.common.msdl.enums.EEncoding;
import org.radixware.kernel.common.msdl.fields.AbstractFieldModel;
import org.radixware.kernel.common.msdl.fields.StructureFieldModel;
import org.radixware.schemas.msdl.SimpleField;

/**
 *
 * @author npopov
 */
public abstract class AbstractMsdlPanel extends AbstractEditItem {
    
    private AbstractFieldModel editField;

    public void open(AbstractFieldModel field, MsdlField containerField) {
        editField = field;
        super.open(containerField);
    }

    @Override
    public void open(MsdlField field) {
        throw new UnsupportedOperationException("Not implemented in this class, use open(AbstractFieldModel, MsdlField) instead");
    }
    
    
    protected void save() {
        final boolean fireChangeEvent = needFireChangeEvent();
        doSave();
        editField.setModified(fireChangeEvent);
    }
     
    protected boolean needFireChangeEvent() {
        if (this instanceof IEncodingField && (editField.getField() instanceof SimpleField || editField instanceof StructureFieldModel)) {
            EEncoding newEncoding = ((IEncodingField) this).getEncoding();
            if (newEncoding == null) {
                return false;
            }
            EEncoding oldEncoding;
            if (editField.getField() instanceof SimpleField) {
                oldEncoding = EEncoding.getInstance(((SimpleField) editField.getField()).getEncoding());
            } else {
                oldEncoding = EEncoding.getInstance(editField.getEncoding(true));
            }
            
            if ((editField.isRootMsdlScheme() && oldEncoding != newEncoding) || 
                    EEncoding.isElement(oldEncoding) != EEncoding.isElement(newEncoding)) {
                return true;
            }
        }
        return false;
    }
    
    protected abstract void doSave();

    
    public interface IEncodingField {
        
        public  EEncoding getEncoding();
        
    }
}
