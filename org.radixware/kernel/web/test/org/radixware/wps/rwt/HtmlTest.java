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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.html.Div;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.html.Html;


public class HtmlTest {

    public HtmlTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of add method, of class Html.
     */
    @Test
    public void testAdd() {
        Html div = new Div();
        assertNotNull(div);
        div.setCss("overflow", "hidden");
        div.setCss("width", "100%");
        Html table = new Html("table");
        div.add(table);
        Html row = new Html("tr");
        Html cell = new Html("td");
        table.add(row);
        row.add(cell);
        printState(div);
        //changes traking
        div = new Div();
        printState(div);
        assertNotNull(div);
        div.setCss("overflow", "hidden");
        div.setCss("width", "100%");
        printState(div);
        table = new Html("table");
        div.add(table);
        printState(div);
        row = new Html("tr");
        cell = new Html("td");
        printState(div);
        table.add(row);
        row.add(cell);
        printState(div);
        cell.setAttr("colspan", 2);
        printState(div);
        cell.setInnerText("Hello world");
        printState(div);
    }

    private void printState(Html html) {
        //  WpsEnvironment e = new MocWpsEnvironment();
        System.out.println("======================== HTML element state report start =========================");
        final StringBuilder builder = new StringBuilder();
        //     html.saveChanges(e.getMainWindow(), builder);
        System.out.println(builder);
        //    html.rendered(e.getMainWindow());
        System.out.println("======================== HTML element state report end =========================");
    }

    /**
     * Test of remove method, of class Html.
     */
    @Test
    public void testRemove() {
    }

    /**
     * Test of children method, of class Html.
     */
    @Test
    public void testChildren() {
    }

    /**
     * Test of setCss method, of class Html.
     */
    @Test
    public void testSetCss() {
    }

    /**
     * Test of getCss method, of class Html.
     */
    @Test
    public void testGetCss() {
    }

    /**
     * Test of isModified method, of class Html.
     */
    @Test
    public void testIsModified() {
    }

    /**
     * Test of toString method, of class Html.
     */
    @Test
    public void testToString() {
    }

    /**
     * Test of getStyle method, of class Html.
     */
    @Test
    public void testGetStyle() {
    }
}
