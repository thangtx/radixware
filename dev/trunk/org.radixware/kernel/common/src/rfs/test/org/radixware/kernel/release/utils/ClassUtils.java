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

package org.radixware.kernel.release.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.build.Make;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsPresentationEntityAdapterClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDetailRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsDynamicPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateColumnPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsInnateRefPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsParentPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsUserPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsEditorPresentationDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeType;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.dds.DdsColumnDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;


import org.radixware.kernel.release.utils.TestFlowLogger;
import static org.junit.Assert.*;


public abstract class ClassUtils extends TableUtils {

    private static int nameIndex = 0;

    private static String newName(String basis) {
        return basis + "#" + String.valueOf(nameIndex++);
    }

    public ClassUtils(int layersCount, int modulesCount, boolean enumsReqiured) throws IOException {
        super(layersCount, modulesCount, enumsReqiured);
    }

    public AdsEntityClassDef createAec(DdsTableDef table) {
        return createAec(table, null);
    }

    public AdsEntityClassDef createAec(DdsTableDef table, AdsDefinition[] depends) {
        AdsModule module = chooseAdsModule(table, depends);

        AdsEntityClassDef clazz = AdsEntityClassDef.Factory.newInstance(table);
        clazz.setName(newName("aec"));
        //clazz.associateWithTable(table);

        module.getDefinitions().add(clazz);

        module.getDependences().add(table.getModule());
        if (depends != null) {
            for (int i = 0; i < depends.length; i++) {
                module.getDependences().add(depends[i].getModule());
                assertEquals(depends[i], AdsSearcher.Factory.newAdsDefinitionSearcher(clazz).findById(depends[i].getId()));
            }
        }

        return clazz;
    }

    public AdsEntityGroupClassDef createAgc(AdsEntityClassDef entity) {
        AdsEntityGroupClassDef group = AdsEntityGroupClassDef.Factory.newInstance(entity);
        group.setName(newName("aec_group"));
        assertTrue(group.setBasis(entity));

        AdsModule module = chooseAdsModuleInContextLayer(entity);

        module.getDefinitions().add(group);


        module.getDependences().add(entity.getModule());

        entity.getModule().getDependences().add(module);


        return group;
    }

    public AdsApplicationClassDef createAcl(AdsEntityObjectClassDef base, int layer) {
        AdsModule module = chooseAdsModule(layer);

        AdsApplicationClassDef acl = AdsApplicationClassDef.Factory.newInstance(base);
        acl.setName(newName(base.getName() + ".acl_in_layer" + String.valueOf(layer)));

        module.getDefinitions().add(acl);

        module.getDependences().add(base.getModule());
        return acl;
    }

    public ArrayList<AdsApplicationClassDef> createAcls(AdsEntityObjectClassDef entity, int aclCount) {

        ArrayList<AdsApplicationClassDef> acls = new ArrayList<AdsApplicationClassDef>(aclCount);
        List<AdsEntityObjectClassDef> useClasses = new ArrayList<AdsEntityObjectClassDef>();

        useClasses.add(entity);

        for (int a = 0; a < aclCount; a++) {
            AdsModule module = chooseAdsModule(entity);

            int tryesCount = useClasses.size() * 2 + 1;

            AdsEntityObjectClassDef ancestor = null;
            while (tryesCount > 0) {
                AdsEntityObjectClassDef candidate = useClasses.get(random.nextInt(useClasses.size()));
                if (candidate.getModule().getSegment().getLayer().isHigherThan(module.getSegment().getLayer())) {
                    tryesCount--;
                } else {
                    ancestor = candidate;
                    break;
                }
            }

            if (ancestor == null) {
                ancestor = entity;
            }

            if (module != ancestor.getModule()) {
                module.getDependences().add(ancestor.getModule());
                assertNotNull(AdsSearcher.Factory.newAdsDefinitionSearcher(ancestor).findById(ancestor.getId()));
            }

            AdsApplicationClassDef acl = AdsApplicationClassDef.Factory.newInstance(ancestor);

            acl.setName(entity.getName() + ".acl" + String.valueOf(a));

            module.getDefinitions().add(acl);

            assertNotNull(acl.findBasis());
            assertNotNull(acl.findRootBasis());
            assertNotNull(acl.findTable(acl));
            assertEquals(ancestor, acl.findBasis());
            assertEquals(entity.findRootBasis(), acl.findRootBasis());
            assertEquals(entity.findTable(acl), acl.findTable(acl));

            useClasses.add(acl);
            acls.add(acl);
        }
        return acls;
    }

    public AdsPresentationEntityAdapterClassDef createAdapterClass(AdsEntityObjectClassDef entityClass) {
        assertNotNull(entityClass);
        if (entityClass.findPresentationAdapter() == null) {
            AdsEntityObjectClassDef basis = entityClass.findBasis();
            if (basis != null) {
                createAdapterClass(basis);
                if (basis.findPresentationAdapter() == null) {
                    fail("Adapter must be created but not found");
                }
            }
            AdsPresentationEntityAdapterClassDef adapter = AdsPresentationEntityAdapterClassDef.Factory.newInstance(entityClass);
            adapter.setName(newName(entityClass.getName() + ".adapter"));
            assertNotNull(entityClass.getModule());
            assertTrue(adapter.setBasis(entityClass));

            AdsModule module = chooseAdsModuleInContextLayer(entityClass);

            module.getDefinitions().add(adapter);

            entityClass.getModule().getDependences().add(module);
            module.getDependences().add(entityClass.getModule());

            assertNotNull(entityClass.findPresentationAdapter());
            assertEquals(adapter, entityClass.findPresentationAdapter());

            assertNotNull(adapter.findBasis());
            assertEquals(entityClass, adapter.findBasis());
            assertEquals(adapter.findTable(adapter), entityClass.findTable(adapter));

            return adapter;
        } else {
            return entityClass.findPresentationAdapter();
        }
    }

    public AdsInnateColumnPropertyDef createInnateColumnProperty(AdsEntityObjectClassDef clazz, DdsColumnDef c) {
        AdsInnateColumnPropertyDef p = AdsInnateColumnPropertyDef.Factory.newInstance(c);


        assertTrue(chooseOrCreatePropertyGroup(clazz, p));


        return p;
    }

    public AdsInnateRefPropertyDef createInnateRefPropertyDef(AdsEntityObjectClassDef clazz, DdsReferenceDef ref) {
        clazz.getModule().getDependences().add(ref.findChildTable(clazz).getModule());

        AdsInnateRefPropertyDef p = AdsInnateRefPropertyDef.Factory.newInstance(ref);

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));


        return p;
    }

    public AdsDetailColumnPropertyDef createDetailColumnProperty(AdsEntityObjectClassDef clazz, DdsReferenceDef ref, DdsColumnDef c) {
        AdsDetailColumnPropertyDef p = AdsDetailColumnPropertyDef.Factory.newInstance(ref, c);



        assertTrue(chooseOrCreatePropertyGroup(clazz, p));

        return p;
    }

    public AdsDetailRefPropertyDef createDetailRefPropertyDef(AdsEntityObjectClassDef clazz, DdsReferenceDef detailRef, DdsReferenceDef parentRef) {
        AdsDetailRefPropertyDef p = AdsDetailRefPropertyDef.Factory.newInstance(detailRef, parentRef);

        //assertTrue(p.associateWith(detailRef, parentRef));

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));


        return p;
    }

    public AdsDynamicPropertyDef createDynamicPropertyDef(AdsClassDef clazz, String name) {
        AdsDynamicPropertyDef p = AdsDynamicPropertyDef.Factory.newInstance();
        p.setName(name);
        assertTrue(chooseOrCreatePropertyGroup(clazz, p));
        return p;
    }

    public AdsParentPropertyDef createParentPropertyDef(AdsClassDef clazz, ArrayList<AdsPropertyDef> parentPath, AdsPropertyDef finalProp) {
        AdsParentPropertyDef p = AdsParentPropertyDef.Factory.newInstance(parentPath, finalProp);

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));

        return p;
    }

    public AdsUserPropertyDef createUserPropertyDef(AdsEntityObjectClassDef clazz, EValType valType) {
        AdsUserPropertyDef p = AdsUserPropertyDef.Factory.newInstance();

        p.getValue().setType(AdsTypeDeclaration.Factory.newInstance(valType));

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));

        return p;
    }

    public AdsUserPropertyDef createUserRefPropertyDef(AdsEntityObjectClassDef clazz, AdsEntityObjectClassDef type) {
        AdsUserPropertyDef p = AdsUserPropertyDef.Factory.newInstance();

        p.getValue().setType(AdsTypeDeclaration.Factory.newParentRef(type));

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));

        return p;
    }

    public ClassUtils(TestUtils source) {
        super(source);
    }

    public AdsUserPropertyDef createUserArrRefPropertyDef(AdsEntityObjectClassDef clazz, AdsEntityObjectClassDef type) {
        AdsUserPropertyDef p = AdsUserPropertyDef.Factory.newInstance();

        p.getValue().setType(AdsTypeDeclaration.Factory.newArrRef(type));

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));



        return p;
    }

    public AdsUserPropertyDef createUserObjectPropertyDef(AdsEntityObjectClassDef clazz, AdsEntityObjectClassDef type) {
        AdsUserPropertyDef p = AdsUserPropertyDef.Factory.newInstance();

        p.getValue().setType(AdsTypeDeclaration.Factory.newEntityObject(type));

        assertTrue(chooseOrCreatePropertyGroup(clazz, p));
        return p;
    }

    public AdsSelectorPresentationDef createSelectorPresentation(AdsEntityObjectClassDef clazz) {
        AdsSelectorPresentationDef spr = AdsSelectorPresentationDef.Factory.newInstance();
        spr.setName("spr");
        clazz.getPresentations().getSelectorPresentations().getLocal().add(spr);
        return spr;
    }

    public AdsEditorPresentationDef createEditorPresentation(AdsEntityObjectClassDef clazz) {
        AdsEditorPresentationDef spr = AdsEditorPresentationDef.Factory.newInstance();
        spr.setName("epr");
        clazz.getPresentations().getEditorPresentations().getLocal().add(spr);
        return spr;
    }

    public AdsXmlSchemeDef createXmlSchemeDef(AdsModule module, String publishedUrl) {
        AdsXmlSchemeDef xml = AdsXmlSchemeDef.Factory.newInstance(publishedUrl);
        module.getDefinitions().add(xml);
        return xml;
    }

    private static final class PH implements IProblemHandler {

        private final ArrayList<RadixProblem> errors = new ArrayList<RadixProblem>();

        @Override
        public void accept(RadixProblem problem) {
            if (problem.getSeverity() == RadixProblem.ESeverity.ERROR) {
                errors.add(problem);
            }
        }

        public void assertProblemCount(int count) {
            if (errors.size() != count) {
                for (RadixProblem p : errors) {
                    System.out.println(p.getMessage());
                }
                fail("Unexpected problem count: " + errors.size() + " (expected " + count + ")");
            }
        }
    }

    public void printAndCompile(IJavaSource src, ERuntimeEnvironmentType env) {
        printAndCompile(src, env, 0);
    }

    public void printAndCompile(IJavaSource src, ERuntimeEnvironmentType env, int expectedProblems) {
        try {
            ((AdsDefinition) src).save();
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        for (CodeType ct : src.getJavaSourceSupport().getSeparateFileTypes(env)) {
            CodePrinter printer = CodePrinter.Factory.newJavaPrinter();
            src.getJavaSourceSupport().getCodeWriter(UsagePurpose.getPurpose(env, ct)).writeCode(printer);
            System.out.println(printer.toString());
            // compilation
            enablePlatformLibs();
            PH ph = new PH();

            new Make(new TestFlowLogger()).compile((AdsDefinition) src, env, ph, null, false, false);
            ph.assertProblemCount(expectedProblems);
        }
    }
}
