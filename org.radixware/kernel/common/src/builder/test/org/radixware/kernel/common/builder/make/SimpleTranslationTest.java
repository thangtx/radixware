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

package org.radixware.kernel.common.builder.make;

import org.radixware.kernel.common.builder.utils.DefaultBuildEnvironment;
import org.junit.Test;
import static org.junit.Assert.*;
import org.radixware.kernel.common.builder.BuildActionExecutor;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserMethodDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.scml.Scml;


public class SimpleTranslationTest {

    @Test
    public void buildTest() throws Exception {
        ClassUtils utils = new ClassUtils(2, 1, false) {

            @Override
            public void executeTest() throws Exception {
                AdsModule m = chooseAdsModule(0);

                AdsDynamicClassDef clazz1 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                AdsDynamicClassDef clazz2 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                AdsDynamicClassDef clazz3 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);

                clazz1.setName("clazz1");
                clazz2.setName("clazz2");
                clazz3.setName("clazz3");

                m.getDefinitions().add(clazz1);
                m.getDefinitions().add(clazz2);
                m.getDefinitions().add(clazz3);

                AdsModule m2 = chooseAdsModule(1);

                AdsDynamicClassDef clazz4 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                AdsDynamicClassDef clazz5 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);
                AdsDynamicClassDef clazz6 = AdsDynamicClassDef.Factory.newInstance(ERuntimeEnvironmentType.SERVER);


                clazz4.setName("clazz4");
                clazz5.setName("clazz5");
                clazz6.setName("clazz6");

                m2.getDefinitions().add(clazz4);
                m2.getDefinitions().add(clazz5);
                m2.getDefinitions().add(clazz6);

                m2.getDependences().add(m);

                clazz4.getInheritance().setSuperClass(clazz1);
                clazz5.getInheritance().setSuperClass(clazz2);
                clazz6.getInheritance().setSuperClass(clazz3);


                AdsUserMethodDef method = AdsUserMethodDef.Factory.newInstance();


                clazz6.getMethods().getLocal().add(method);
                method.getSource().getItems().clear();
                method.getSource().getItems().add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(clazz1)));
                method.getSource().getItems().add(Scml.Text.Factory.newInstance(" v = null;\n"));


                enablePlatformLibs();
                BuildActionExecutor executor = new BuildActionExecutor(new DefaultBuildEnvironment());

                executor.execute(branch);

                assertFalse(executor.wasErrors());

            }
        };
        utils.test();
    }

    @Test
    public void userFuncCompileTest() {
    }
}
