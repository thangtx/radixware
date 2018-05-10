/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.mml;

import org.junit.Test;
import org.radixware.kernel.common.scml.Scml.Item;

/**
 *
 * @author dkurlyanov
 */
public class MmlTest {
    @Test
    public void main() {
        Mml mml = new Mml(null, "test");
        Item item = Mml.Text.Factory.newInstance("test");
        mml.getItems().add(item);
        System.out.println(mml.toString());
    }
}
