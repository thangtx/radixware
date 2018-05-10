/*
* Copyright (coffee) 2008-2016, Compass Plus Limited. All rights reserved.
*
* This Source Code Form is subject to the terms of the Mozilla Public
* License, v. 2.0. If a copy of the MPL was not distributed with this
* file, You can obtain one at http://mozilla.org/MPL/2.0/.
* This Source Code is distributed WITHOUT ANY WARRANTY; including any 
* implied warranties but not limited to warranty of MERCHANTABILITY 
* or FITNESS FOR A PARTICULAR PURPOSE.  See the Mozilla Public 
* License, v. 2.0. for more details.
*/

package org.radixware.wps;

import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.radixware.kernel.common.client.enums.EKeyboardModifier;
import org.radixware.wps.rwt.events.ButtonAndModifiers;


public class ButtonAndModifiersTest {
    
    public ButtonAndModifiersTest() {
        
    }
    
    @Test
    public void test() {
        ButtonAndModifiers btnAndModif1 = new ButtonAndModifiers(KeyEvent.VK_V, null);
        ButtonAndModifiers btnAndModif2 = new ButtonAndModifiers(KeyEvent.VK_V, null);
        Assert.assertEquals(btnAndModif1, btnAndModif2);
        
        btnAndModif1 = new ButtonAndModifiers(1, null);
        btnAndModif2 = new ButtonAndModifiers(1, null);
        Assert.assertEquals(btnAndModif1, btnAndModif2);
        
        btnAndModif1 = new ButtonAndModifiers(1, null);
        btnAndModif2 = new ButtonAndModifiers(2, null);
        Assert.assertNotSame(btnAndModif1, btnAndModif2);
        
        
        List<EKeyboardModifier> list1 = new LinkedList<>();
        list1.add(EKeyboardModifier.ALT);
        list1.add(EKeyboardModifier.SHIFT);
        
        btnAndModif1 = new ButtonAndModifiers(1, list1);
        btnAndModif2 = new ButtonAndModifiers(1, list1);
        Assert.assertEquals(btnAndModif1, btnAndModif2);
        
        btnAndModif1 = new ButtonAndModifiers(1, list1);
        btnAndModif2 = new ButtonAndModifiers(1, null);
        Assert.assertNotSame(btnAndModif1, btnAndModif2);
        
        btnAndModif1 = new ButtonAndModifiers(1, null);
        btnAndModif2 = new ButtonAndModifiers(1, list1);
        Assert.assertNotSame(btnAndModif1, btnAndModif2);
        
        btnAndModif1 = new ButtonAndModifiers(1, list1);
        btnAndModif2 = new ButtonAndModifiers(2, list1);
        Assert.assertNotSame(btnAndModif1, btnAndModif2);
        
        List<EKeyboardModifier> list2 = new LinkedList<>();
        list2.add(EKeyboardModifier.ALT);
        list2.add(EKeyboardModifier.SHIFT);
        
        btnAndModif1 = new ButtonAndModifiers(1, list1);
        btnAndModif2 = new ButtonAndModifiers(1, list2);
        Assert.assertEquals(btnAndModif1, btnAndModif2);
        
        list2.remove(EKeyboardModifier.ALT);
        
        btnAndModif1 = new ButtonAndModifiers(1, list1);
        btnAndModif2 = new ButtonAndModifiers(1, list2);
        Assert.assertNotSame(btnAndModif1, btnAndModif2);
        
        btnAndModif1 = null;
        Assert.assertNotSame(btnAndModif1, btnAndModif2);
        btnAndModif2 = null;
        Assert.assertEquals(btnAndModif1, btnAndModif2);
    }
}