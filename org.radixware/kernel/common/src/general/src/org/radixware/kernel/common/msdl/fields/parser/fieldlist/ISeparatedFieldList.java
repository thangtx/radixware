/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.msdl.fields.parser.fieldlist;

/**
 *
 * @author npopov
 */
public interface ISeparatedFieldList extends IFieldList {
    
    boolean hasSameShield(ISeparatedFieldList other);
    
    boolean hasSameSeparators(ISeparatedFieldList other);
    
    Byte getShield();
    
    byte[] getSeparators();
    
}
