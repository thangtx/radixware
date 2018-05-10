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

package org.radixware.wps;

import static org.junit.Assert.*;
import org.junit.*;
import org.radixware.kernel.common.client.env.LocaleManager;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.enums.EIsoLanguage;


public class LanguageManagerTest {

    public LanguageManagerTest() {
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
     * Test of getTranslations method, of class LanguageManager.
     */
    @Test
    public void testGetTranslations() {

        EIsoLanguage lang = null;
        WpsLocaleManager instance = new WpsLocaleManager(null);
        WpsLocaleManager.TranslationSet result = instance.getTranslations(EIsoLanguage.RUSSIAN);
        assertNotNull(result);

        assertEquals("Имя пользователя:", instance.translate(EIsoLanguage.RUSSIAN, "LogonDialog", "User name:"));
        assertEquals("Изменение пароля к учетной записи '%s'", instance.translate(EIsoLanguage.RUSSIAN, "ChangePasswordDialog", "Change Password for '%s' Account"));
        assertEquals("<нет>", instance.translate(EIsoLanguage.RUSSIAN, "Selector", "<None>"));
        assertNotSame("Your connection is not secure. Do you want to continue?", instance.translate(EIsoLanguage.RUSSIAN, "EnvironmentAction", "Your connection is not secure. Do you want to continue?"));


    }

    @Test
    public void testMessageBoxButtons() {
        WpsLocaleManager instance = new WpsLocaleManager(null);
        WpsLocaleManager.TranslationSet result = instance.getTranslations(EIsoLanguage.RUSSIAN);
        assertNotNull(result);
        for (EDialogButtonType b : EDialogButtonType.values()) {
            if (b.getValue().isEmpty()) {
                continue;
            };
            String translation = instance.translateImpl(EIsoLanguage.RUSSIAN, "ExplorerDialog", b.getValue());

            assertNotNull(b.getValue(), translation);
            System.out.println(b.getValue() + " -> " + translation);
        }
    }
}
