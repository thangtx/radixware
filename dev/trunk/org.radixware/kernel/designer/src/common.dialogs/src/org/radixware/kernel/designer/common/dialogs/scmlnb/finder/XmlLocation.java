/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.designer.common.dialogs.scmlnb.finder;

/**
 *
 * @author ilevandovskiy
 */
public class XmlLocation {
    private final int matcherStart;

    public XmlLocation(int matcherStart) {
        this.matcherStart = matcherStart;
    }
    
    public int getMatcherStart(){
        return matcherStart;
    }
}
