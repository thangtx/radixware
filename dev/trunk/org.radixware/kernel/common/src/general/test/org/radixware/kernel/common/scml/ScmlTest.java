/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.scml;

import org.junit.Test;
import org.junit.Assert;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;

/**
 *
 * @author achernomyrdin
 */
public class ScmlTest {
    public static final String TEST_STRING = "test string";
    
    @Test
    public void basicTest() {
        final TestScml      container = new TestScml();
        final Scml.Text     text1 = Scml.Text.Factory.newInstance("my text1"), text2 = Scml.Text.Factory.newInstance("my text2");
        
        Assert.assertEquals(container.getItems().size(),0);
        
        container.getItems().add(text1);
        container.getItems().add(text2);
        Assert.assertEquals(container.getItems().size(),2);
        Assert.assertEquals(container.getItems().get(0),text1);
        Assert.assertEquals(container.getItems().get(1),text2);
        
        container.setText(TEST_STRING);
        Assert.assertEquals(container.getItems().size(),1);
        Assert.assertEquals(container.getItems().get(0).toString(),TEST_STRING);

        container.getItems().clear();
        container.getItems().add(text1);
        container.setText(TEST_STRING);
        Assert.assertEquals(container.getItems().size(),1);
        Assert.assertEquals(container.getItems().get(0).toString(),TEST_STRING);
        
        container.getItems().clear();
        container.getItems().add(text1);
        container.getItems().add(text2);
        
        for (Scml.Item item : container.getItems()) {
            Assert.assertTrue(item instanceof Scml.Text);
            Assert.assertTrue(item.equals(text1) || item.equals(text2));
        }
        
        Assert.assertEquals(container.toString(),TEST_STRING+"my text2");    // Side effect of the setText implementation: "test string" instead of "my text1"!

        final boolean[]   visited = new boolean[]{false,false};
        container.visitAll(new IVisitor() {
                @Override
                public void accept(RadixObject item) {
                    if (item == text1) {
                        visited[0] = true;
                    }
                    else if (item == text2) {
                        visited[1] = true;
                    }
                }
            }
        );
        Assert.assertTrue(visited[0]);
        Assert.assertTrue(visited[1]);
    }
}

class TestScml extends Scml {
    public void setText(final String code) {
        super.setText(code);
    }
    
}
