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

package org.radixware.kernel.release.fs;

import java.io.File;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.junit.Test;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.DefinitionSearcher;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.release.fs.SimpleTranslationTest.MyLoader;
import org.radixware.kernel.starter.meta.RevisionMeta;
import org.radixware.kernel.starter.radixloader.RadixLoader.HowGetFile;
import static org.junit.Assert.*;


public class UserFuncCompileTest {

    private class AdsDefSearcher extends DefinitionSearcher<AdsDefinition> {

        public AdsDefSearcher(Definition context) {
            super(context);
        }

        @Override
        public DefinitionSearcher<AdsDefinition> findSearcher(Module module) {
            if (module instanceof AdsModule) {
                return new AdsDefSearcher(module);
            } else {
                return null;
            }
        }

        @Override
        public AdsDefinition findInsideById(Id id) {
            return ((AdsModule) getContext().getModule()).getTopContainer().findById(id);
        }
    }

    @Test
    public void userFuncCompileTest() throws IOException {
        if (System.getProperty("user.name").equals("akrylov")) {
            Branch b = Branch.Factory.newInstanceInMemory(new RfsRepositoryBranch(new ReleaseRepository() {
                private RevisionMeta meta = null;

                @Override
                protected RevisionMeta getRevisionMeta() {
                    try {
                        if (meta == null) {
                            meta = new RevisionMeta(new MyLoader(new File("/home/akrylov/ssd/radix/dev/trunk"), "org.radixware").getAccessor(), 53569, HowGetFile.fromDir(null));
                        }
                        return meta;
                    } catch (IOException | XMLStreamException ex) {
                        throw new RuntimeException(ex);
                    }
                }

                @Override
                public void processException(Throwable e) {
                    e.printStackTrace();
                }
            }));
            assertNotNull(b);
            final Id classId = Id.Factory.loadFrom("aclHS5CVJFR6JCXHCDJCMQTQRL7LU");
            final Id methodId = Id.Factory.loadFrom("mthKU4VYSKONVEINCUOCKWPQZXNIA");
//            AdsUserFuncDef uf = AdsUserFuncDef.Lookup.lookup(b, classId, methodId, Id.Factory.newInstance(EDefinitionIdPrefix.USER_FUNC_CLASS), null, null, null, null, null);
//            assertNotNull(uf);
//            Collection<AdsUserFuncDef.Lookup.DefInfo> infos = AdsUserFuncDef.Lookup.listTopLevelDefinitions(uf, EnumSet.allOf(EDefType.class));
//
//            assertFalse(infos.isEmpty());
//
//            for (AdsUserFuncDef.Lookup.DefInfo info : infos) {
//                AdsDefinition def = AdsUserFuncDef.Lookup.findTopLevelDefinition(uf, info.getId());
//                assertNotNull(def);
//                AdsDefinition def2 = new AdsDefSearcher(uf).findById(def.getId()).get();
//                assertEquals(def, def2);
//            }
        }

    }
}
