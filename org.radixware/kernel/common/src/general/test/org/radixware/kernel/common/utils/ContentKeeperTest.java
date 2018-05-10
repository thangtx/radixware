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
package org.radixware.kernel.common.utils;


import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class ContentKeeperTest {
	@Test
	public void xlsContentKeeperTest() throws IOException {
		try(final InputStream is = this.getClass().getResourceAsStream("test.xls")) {
			final XLSContentKeeper ck = new XLSContentKeeper(is);
			
			Assert.assertEquals(ck.getRowCount(),2);
			Assert.assertEquals(ck.getColumnCount(),3);
			Assert.assertEquals(ck.getCell(0,0),1.0);
			Assert.assertEquals(ck.getCell(0,1),"test1");
			Assert.assertTrue(ck.getCell(0,2) instanceof Date);
			
			try{ck.getCell(666,0);
				Assert.fail("Mandatory exception was not detected (row out of range)");
			} catch (IllegalArgumentException exc) {
			}
			try{ck.getCell(0,666);
				Assert.fail("Mandatory exception was not detected (column out of range)");
			} catch (IllegalArgumentException exc) {
			}
		}
	}

	@Test
	public void xlsxContentKeeperTest() throws IOException {
		try(final InputStream is = this.getClass().getResourceAsStream("test.xlsx")) {
			final XLSXContentKeeper ck = new XLSXContentKeeper(is);
			
			Assert.assertEquals(ck.getRowCount(),2);
			Assert.assertEquals(ck.getColumnCount(),3);
			Assert.assertEquals(ck.getCell(0,0),1.0);
			Assert.assertEquals(ck.getCell(0,1),"test1");
			Assert.assertTrue(ck.getCell(0,2) instanceof Date);
			
			try{ck.getCell(666,0);
				Assert.fail("Mandatory exception was not detected (row out of range)");
			} catch (IllegalArgumentException exc) {
			}
			try{ck.getCell(0,666);
				Assert.fail("Mandatory exception was not detected (column out of range)");
			} catch (IllegalArgumentException exc) {
			}
		}
	}
}
