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

import org.radixware.kernel.common.builder.test.TestBranchFactory;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsPropertyGroup;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.dds.DdsDefinition;

import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;

import org.radixware.kernel.common.utils.FileUtils;
import static org.junit.Assert.*;


public abstract class TestUtils {

    protected Random random;
    public final Branch branch;
    public final Layer[] layers;
    public final List<AdsModule> adsModules = new ArrayList<AdsModule>();
    public final List<DdsModule> ddsModules = new ArrayList<DdsModule>();
    private static EValType[] enumTypes = new EValType[]{EValType.INT, EValType.CHAR, EValType.STR};

    public TestUtils(TestUtils source) {
        try {
            source.saveAll();
            File copyofBranch = cloneTemporaryFile(source.branch.getDirectory());

            this.branch = Branch.Factory.loadFromDir(copyofBranch);
            layers = new Layer[branch.getLayers().getInOrder().size()];
            branch.getLayers().getInOrder().toArray(layers);
            this.branch.visit(new IVisitor() {

                @Override
                public void accept(RadixObject radixObject) {
                    if (radixObject instanceof AdsModule) {
                        adsModules.add((AdsModule) radixObject);
                    } else if (radixObject instanceof DdsModule) {
                        ddsModules.add((DdsModule) radixObject);
                    }
                }
            }, new VisitorProvider() {

                @Override
                public boolean isTarget(RadixObject radixObject) {
                    return radixObject instanceof Module;
                }
            });
            this.random = new Random(1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AdsEnumDef findAdsEnumDef(final Id id) {
        RadixObject obj = branch.find(new VisitorProvider() {

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef) {
                    return ((AdsEnumDef) object).getId() == id;
                } else {
                    return false;
                }
            }
        });
        if (obj instanceof AdsEnumDef) {
            return (AdsEnumDef) obj;
        } else {
            return null;
        }
    }

    public TestUtils(int layersCount, int modulesPerLayer, boolean enumsRequired) {
        try {

            layers = new Layer[layersCount];

            branch = TestBranchFactory.newEmptyInstance();
            random = new Random(1);

            for (int i = 0; i < layersCount; i++) {
                List<EIsoLanguage> list = new ArrayList<EIsoLanguage>();
                list.add(EIsoLanguage.ENGLISH);
                for (int j = i; j < layersCount + i; j++) {
                    if (j + i < EIsoLanguage.values().length) {
                        list.add(EIsoLanguage.values()[j]);
                    }
                }
                layers[i] = branch.getLayers().addNew("layer" + String.valueOf(i) + ".url", "Layer" + String.valueOf(i), "", "TestCompany", new ArrayList(list));
            }
            int i = 0;
            for (Layer l : layers) {
                for (int m = 0; m < modulesPerLayer; m++) {
                    DdsModule dds = DdsModule.Factory.getDefault().newInstance("ddsm" + "." + String.valueOf(i) + "." + String.valueOf(m));
                    l.getDds().getModules().add(dds);
                    ddsModules.add(dds);

                    AdsModule ads = AdsModule.Factory.getDefault().newInstance("adsm" + "." + String.valueOf(i) + "." + String.valueOf(m));
                    l.getAds().getModules().add(ads);
                    adsModules.add(ads);

                    if (enumsRequired) {
                        for (int e = 0; e < 2; e++) {
                            System.out.print("adding ads enum...");
                            AdsEnumDef enumDef = AdsEnumDef.Factory.newInstance();
                            enumDef.setName("adsenum" + e * i);
                            enumDef.setItemType(enumTypes[random.nextInt(3)]);
                            ads.getDefinitions().add(enumDef);
                            System.out.println(" done");
                        }
                    }
                }
                i++;
            }
        } catch (IOException cause) {
            throw new RadixError("Unable to create temp branch.", cause);
        }
    }

    public void testVisit(final ArrayList<Id> ids) {
        final ArrayList<Id> idsToFind = new ArrayList<Id>(ids);
        branch.visit(new IVisitor() {

            @Override
            public void accept(RadixObject object) {
                System.out.println("Visited: " + object.getQualifiedName());
                if (object instanceof Definition) {
                    Id id = ((Definition) object).getId();
                    if (id == null) {
                        fail("null id : " + object.getClass().getName() + " : " + object.getQualifiedName());
                    }
                    if (idsToFind.contains(id)) {
                        idsToFind.remove(id);
                    }
                }
            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        if (!idsToFind.isEmpty()) {
            StringBuilder b = new StringBuilder();
            for (Id id : idsToFind) {
                b.append(id.toString());
                b.append("\n");
            }
            fail("Not found: \n" + b.toString());
        }
    }

    public abstract void executeTest() throws Exception;

    public void test() throws Exception {
        try {
            executeTest();
        } finally {
            done();
//            FileUtils.deleteDirectory(branch.getDirectory());
//            assertFalse(branch.getDirectory().exists());
//            for (TestUtils clone : clones) {
//                clone.done();
//            }
        }
    }

    public Layer chooseLayer(Layer layer) {
        int layerIndex = -1;
        for (int i = 0; i < layers.length; i++) {
            if (layers[i] == layer) {
                layerIndex = i;
                break;
            }
        }
        if (layerIndex < 0) {
            return layer;
        }

        return layerIndex == layers.length - 1 ? layers[layers.length - 1] : layers[layerIndex + random.nextInt(layers.length - layerIndex - 1)];
    }

    public Layer chooseLayerExact(Layer[] layers_list) {
        int layerIndex = -1;

        for (int i = 0; i < layers.length; i++) {
            for (int l = 0; l < layers_list.length; l++) {
                if (layers_list[l] == layers[i]) {
                    if (i > layerIndex) {
                        layerIndex = i;
                    }
                    break;
                }
            }

        }
        if (layerIndex < 0) {
            fail("cannot choose layer");
        }
        return layers[layerIndex];
    }

    public Layer chooseLayer(Layer[] layers_list) {
        int layerIndex = -1;

        for (int i = 0; i < layers.length; i++) {
            for (int l = 0; l < layers_list.length; l++) {
                if (layers_list[l] == layers[i]) {
                    if (i > layerIndex) {
                        layerIndex = i;
                    }
                    break;
                }
            }

        }
        if (layerIndex < 0) {
            fail("cannot choose layer");
        }
        return layerIndex == layers.length - 1 ? layers[layers.length - 1] : layers[layerIndex + random.nextInt(layers.length - layerIndex - 1)];
    }

    public DdsModule chooseDdsModule() {
        Layer l = layers[random.nextInt(layers.length)];
        @SuppressWarnings("unchecked")
        List<? extends Module> ms = l.getDds().getModules().list();
        assertFalse(ms.isEmpty());
        return (DdsModule) ms.get(random.nextInt(ms.size()));
    }

    public DdsModule chooseDdsModule(int layer) {
        Layer l = layers[layer];
        @SuppressWarnings("unchecked")
        List<? extends Module> ms = l.getDds().getModules().list();
        assertFalse(ms.isEmpty());
        return (DdsModule) ms.get(random.nextInt(ms.size()));
    }

    public DdsModule lowestDdsModule() {
        Layer l = layers[0];
        @SuppressWarnings("unchecked")
        List<? extends Module> ms = l.getDds().getModules().list();
        assertFalse(ms.isEmpty());
        return (DdsModule) ms.get(random.nextInt(ms.size()));
    }

    public DdsModule chooseDdsModule(DdsDefinition context) {
        Layer l = chooseLayer(context.getModule().getSegment().getLayer());
        @SuppressWarnings("unchecked")
        List<? extends Module> ms = l.getDds().getModules().list();
        assertFalse(ms.isEmpty());
        return (DdsModule) ms.get(random.nextInt(ms.size()));
    }

    public AdsModule chooseAdsModule(AdsDefinition context) {
        Layer layer = chooseLayer(context.getModule().getSegment().getLayer());
        @SuppressWarnings("unchecked")
        List<? extends Module> modules = layer.getAds().getModules().list();

        if (modules.isEmpty()) {
            AdsModule p = AdsModule.Factory.getDefault().newInstance("aaa");
            layer.getAds().getModules().add(p);
            modules = layer.getAds().getModules().list();
        }
        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public AdsModule chooseAdsModule(DdsDefinition context) {
        Layer layer = chooseLayer(context.getModule().getSegment().getLayer());

        List<? extends Module> modules = layer.getAds().getModules().list();

        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public AdsModule chooseAdsModule(int layerIdx) {
        Layer layer = layers[layerIdx];

        List<? extends Module> modules = layer.getAds().getModules().list();

        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public AdsModule chooseAdsModuleInSameLayer(AdsDefinition def) {
        Layer layer = def.getModule().getSegment().getLayer();

        List<? extends Module> modules = layer.getAds().getModules().list();

        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public AdsModule overwriteAdsModule(AdsModule module, Layer layer) {
        @SuppressWarnings("unchecked")
        AdsModule m = ((AdsSegment) layer.getAds()).getModules().overwrite(module);
        adsModules.add(m);
        return m;
    }

    public AdsModule chooseAdsModule(DdsDefinition context, AdsDefinition[] context2) {
        Layer layer = null;
        if (context2 != null) {
            Layer[] addContext = new Layer[context2.length + 1];
            addContext[0] = context.getModule().getSegment().getLayer();
            for (int i = 1; i < context2.length; i++) {
                addContext[i] = context2[i].getModule().getSegment().getLayer();
            }
            layer = chooseLayerExact(addContext);
        } else {
            layer = context.getModule().getSegment().getLayer();
        }

        List<? extends Module> modules = layer.getAds().getModules().list();

        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public AdsModule chooseAdsModuleInContextLayer(AdsDefinition context) {
        Layer layer = context.getModule().getSegment().getLayer();

        List<? extends Module> modules = layer.getAds().getModules().list();

        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public AdsModule chooseAdsModuleInContextLayer(DdsDefinition context) {
        Layer layer = context.getModule().getSegment().getLayer();

        List<? extends Module> modules = layer.getAds().getModules().list();

        return (AdsModule) modules.get(random.nextInt(modules.size()));
    }

    public Definition findDefinition(Module context, final Id id) {
        System.out.print("Looking for definition with id " + id.toString() + "... ");
        return (Definition) context.getSegment().getLayer().find(new VisitorProvider() {

            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsDefinition && ((AdsDefinition) object).getId().equals(id)) {
                    System.out.println(" Found: " + ((AdsDefinition) object).getQualifiedName());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public AdsEnumDef findAdsEnum(Module context) {
        System.out.print("Looking for definition ads enum from module " + context.getQualifiedName() + "... ");
        return (AdsEnumDef) context.getSegment().getLayer().find(new VisitorProvider() {

            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef) {
                    System.out.println(" Found: " + ((AdsDefinition) object).getQualifiedName());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public AdsEnumDef findAdsEnum(Module context, final EValType itemType) {
        System.out.print("Looking for definition ads enum from module " + context.getQualifiedName() + "... ");
        return (AdsEnumDef) context.getSegment().getLayer().find(new VisitorProvider() {

            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsEnumDef && ((AdsEnumDef) object).getItemType() == itemType) {
                    System.out.println(" Found: " + ((AdsDefinition) object).getQualifiedName());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public AdsDefinition findAdsDefinition(final Id id) {
        System.out.print("Looking for definition with id " + id.toString() + "... ");
        return (AdsDefinition) branch.find(new VisitorProvider() {

            @Override
            public boolean isContainer(RadixObject object) {
                return true;
            }

            @Override
            public boolean isTarget(RadixObject object) {
                if (object instanceof AdsDefinition && ((AdsDefinition) object).getId().equals(id)) {
                    System.out.println(" Found: " + ((AdsDefinition) object).getQualifiedName());
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public AdsPropertyGroup chooseOrCreatePropertyGroup(AdsClassDef clazz) {
        System.out.print("Looking for property in class " + clazz.getQualifiedName() + "... ");

        AdsPropertyGroup targetGroup = clazz.getPropertyGroup();

        final ArrayList<AdsPropertyGroup> groups = new ArrayList<AdsPropertyGroup>();
        clazz.visit(new IVisitor() {

            @Override
            public void accept(RadixObject object) {
                if (object instanceof AdsPropertyGroup) {
                    groups.add((AdsPropertyGroup) object);
                }

            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());

        targetGroup = groups.get(random.nextInt(groups.size()));

        if (random.nextBoolean()) {
            int count = random.nextInt(5);

            for (int i = 0; i < count; i++) {
                AdsPropertyGroup child = AdsPropertyGroup.Factory.newInstance("pg" + i);
                targetGroup.getChildGroups().add(child);
                if (random.nextBoolean()) {
                    targetGroup = child;
                }
            }
        }


        System.out.println("Group found: " + targetGroup.getQualifiedName());
        return targetGroup;
    }

    public boolean chooseOrCreatePropertyGroup(AdsClassDef clazz, AdsPropertyDef target) {
        clazz.getProperties().getLocal().add(target);
        chooseOrCreatePropertyGroup(clazz).addMember(target);
        return true;
    }

    public AdsPropertyDef overrideProperty(AdsClassDef clazz, AdsPropertyDef target) {
        AdsPropertyDef res = clazz.getProperties().override(target);
        chooseOrCreatePropertyGroup(clazz).addMember(res);
        return res;
    }

    public ArrayList<DdsReferenceDef> findReferencesToParent(final DdsTableDef table) {
        System.out.print("Collecting parent references of table " + table.getQualifiedName() + " ... ");
        final ArrayList<DdsReferenceDef> res = new ArrayList<DdsReferenceDef>();

        assertNotNull(table);
        final Layer contextLayer = table.getModule().getSegment().getLayer();
        branch.visit(
                new IVisitor() {

                    @Override
                    public void accept(RadixObject object) {
                        if (object.getModule() == null) {
                            return;
                        }
                        Layer layer = object.getModule().getSegment().getLayer();
                        if (layer == contextLayer || contextLayer.isHigherThan(layer)) {
                            if (object instanceof DdsReferenceDef) {
                                DdsReferenceDef ref = (DdsReferenceDef) object;
                                if (ref.findChildTable(object) == table && ref.getType() == DdsReferenceDef.EType.LINK) {
                                    res.add(ref);
                                }

                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
        System.out.println(" " + res.size() + " found ");
        return res;
    }

    public ArrayList<DdsReferenceDef> findReferencesToDetails(final DdsTableDef table) {
        System.out.print("Collecting detail references of table " + table.getQualifiedName() + " ... ");
        final ArrayList<DdsReferenceDef> res = new ArrayList<DdsReferenceDef>();
        assertNotNull(table);
        final Layer contextLayer = table.getModule().getSegment().getLayer();
        branch.visit(
                new IVisitor() {

                    @Override
                    public void accept(RadixObject object) {
                        if (object.getModule() == null) {
                            return;
                        }
                        Layer layer = object.getModule().getSegment().getLayer();
                        if (layer == contextLayer || contextLayer.isHigherThan(layer)) {
                            if (object instanceof DdsReferenceDef) {
                                DdsReferenceDef ref = (DdsReferenceDef) object;
                                if (ref.findParentTable(object) == table && ref.getType() == DdsReferenceDef.EType.MASTER_DETAIL) {
                                    res.add(ref);
                                }

                            }
                        }
                    }
                }, VisitorProviderFactory.createDefaultVisitorProvider());
        System.out.println(" " + res.size() + " found ");
        return res;
    }

    public AdsEntityClassDef findReferenceTargetClass(DdsReferenceDef ref, AdsClassDef context) {
        Id handlerClassId = Id.Factory.changePrefix(ref.findParentTable(context).getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS);
        Definition def = findDefinition(context.getModule(), handlerClassId);
        if (def instanceof AdsEntityClassDef) {
            context.getModule().getDependences().add(((AdsEntityClassDef) def).getModule());
            return (AdsEntityClassDef) def;
        } else {
            return null;
        }

    }

//    public DdsEnumDef findDdsEnumByType(Module context, final EValType valType) {
//        final ArrayList<DdsEnumDef> list = new ArrayList<DdsEnumDef>();
//        final Layer contextLayer = context.getModule().getSegment().getLayer();
//        branch.visit(new IVisitor() {
//
//            @Override
//            public void accept(RadixObject object) {
//                if (object.getModule() == null) {
//                    return;
//                }
//                Layer layer = object.getModule().getSegment().getLayer();
//                if (layer == contextLayer || contextLayer.isHigherThan(layer)) {
//
//                    if (!list.isEmpty()) {
//                        return;
//                    }
//
//                    if (object instanceof DdsEnumDef && ((DdsEnumDef) object).getItemType() == valType) {
//                        list.add((DdsEnumDef) object);
//                    }
//                }
//
//            }
//        }, VisitorProviderFactory.createDefaultVisitorProvider());
//        return list.isEmpty() ? null : list.get(0);
//    }
    public void saveAll() {
        try {
            for (DdsModule module : ddsModules) {
                module.getModelManager().getModel().save();
            }
            for (int i = 0; i < adsModules.size(); i++) {
                AdsModule module = adsModules.get(i);
                module.save();
                List<AdsDefinition> defs = module.getDefinitions().list();
                for (int j = 0; j < defs.size(); j++) {
                    AdsDefinition def = defs.get(j);
                    def.save();
                }
            }
        } catch (IOException ex) {
            fail(ex.getMessage());
        }
    }

    public HashMap<Id, AdsDefinition> collectAdsTopLevelDefs() {
        final HashMap<Id, AdsDefinition> adsDefs = new HashMap<Id, AdsDefinition>();
        branch.visit(new IVisitor() {

            @Override
            public void accept(RadixObject object) {
                if (object instanceof AdsDefinition && ((AdsDefinition) object).isTopLevelDefinition()) {
                    adsDefs.put(((AdsDefinition) object).getId(), (AdsDefinition) object);
                }

            }
        }, VisitorProviderFactory.createDefaultVisitorProvider());
        return adsDefs;
    }

    public TestUtils saveAndLoad() throws Exception {
        saveAll();
        final HashMap<Id, AdsDefinition> defs = this.collectAdsTopLevelDefs();
        final HashMap<Id, AdsModule> modules = new HashMap<Id, AdsModule>();

        for (AdsModule module : this.adsModules) {
            modules.put(module.getId(), module);
        }

        TestUtils testUtils = new TestUtils(this) {

            @Override
            public void executeTest() {
                saveAll();
                assertTrue(modules.size() <= this.adsModules.size());
                for (AdsModule module : this.adsModules) {
                    AdsModule old = modules.get(module.getId());
                    assertEquals(old.getDependences().size(), module.getDependences().size());
                }

                HashMap<Id, AdsDefinition> loadedDefs = this.collectAdsTopLevelDefs();

                if (defs.size() != loadedDefs.size()) {
                    for (AdsDefinition def : defs.values()) {
                        AdsDefinition another = loadedDefs.get(def.getId());
                        if (another == null) {
                            fail("Not loaded " + def.getQualifiedName() + " " + def.getId());
                        }

                    }

                    fail("Not all loaded");
                }

                for (Id id : defs.keySet()) {
                    try {
                        AdsDefinition source = defs.get(id);
                        AdsDefinition loaded = loadedDefs.get(id);
                        assertNotNull(source);
                        assertNotNull(loaded);
                        File sourceFile = source.getModule().getDefinitions().getSourceFile(source, AdsDefinition.ESaveMode.NORMAL);
                        File loadedFile = loaded.getModule().getDefinitions().getSourceFile(source, AdsDefinition.ESaveMode.NORMAL);
                        String sourceContent = FileUtils.readTextFile(sourceFile, FileUtils.XML_ENCODING);
                        String loadedContent = FileUtils.readTextFile(loadedFile, FileUtils.XML_ENCODING);
                        assertEquals(sourceContent, loadedContent);
                    } catch (IOException ex) {
                        fail(id.toString() + " : " + ex.getMessage());
                    }

                }

            }
        };
        testUtils.executeTest();
        this.clones.add(testUtils);
        return testUtils;
    }
    private ArrayList<TestUtils> clones = new ArrayList<TestUtils>();

    public void done() {
        for (Layer l : branch.getLayers()) {
            AdsSegment s = (AdsSegment) l.getAds();
            for (ERuntimeEnvironmentType sx : ERuntimeEnvironmentType.values()) {
                s.getBuildPath().getPlatformLibs().getKernelLib(sx).cleanup();
            }
        }
        FileUtils.deleteDirectory(branch.getDirectory());
        assertFalse(branch.getDirectory().exists());
        for (TestUtils clone : clones) {
            clone.done();
        }
    }

    public static final File cloneTemporaryFile(File sourceLocation) throws IOException {
        File f = File.createTempFile("cloneof_" + sourceLocation.getName(), "");
        if (f.exists() && sourceLocation.isDirectory()) {
            f.delete();
            f.mkdirs();
        }
        FileUtils.copyDirectory(sourceLocation, f);
        return f;
    }

    private static void copyJars(File srcDir, File destDir) {
        if (srcDir.exists()) {
            File[] files = srcDir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".jar");
                }
            });
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File jar = files[i];
                    if (jar.exists()) {
                        if (!destDir.exists()) {
                            destDir.mkdirs();
                        }
                        try {
                            FileUtils.copyFile(jar, new File(destDir, jar.getName()));
                        } catch (IOException ex) {
                            fail(ex.getMessage());
                        }
                    }
                }
            }
        }
    }

    public final void enablePlatformLibs() {
        enablePlatformLibs(layers[0]);
    }

    public static final void enablePlatformLibs(Layer layer) {
        String classPath = System.getProperty("java.class.path");
        String[] classPathComponents = classPath.split(File.pathSeparator);
        String commonBin = "kernel" + File.separator + "common" + File.separator + "bin";
        String commonLib = "kernel" + File.separator + "common" + File.separator + "lib";
        String serverBin = "kernel" + File.separator + "server" + File.separator + "bin";
        String serverLib = "kernel" + File.separator + "server" + File.separator + "lib";
        String explorerBin = "kernel" + File.separator + "explorer" + File.separator + "bin";
        String explorerLib = "kernel" + File.separator + "explorer" + File.separator + "lib";
        String match = "kernel" + File.separator + "common";
        for (String s : classPathComponents) {
            int index = s.indexOf(match);
            if (index >= 0) {
                String pathPrefix = s.substring(0, index);
                File commonBinDir = new File(pathPrefix + commonBin);
                copyJars(commonBinDir, new File(layer.getDirectory(), "kernel" + File.separator + "common" + File.separator + "bin"));

                File commonLibDir = new File(pathPrefix + commonLib);
                copyJars(commonLibDir, new File(layer.getDirectory(), "kernel" + File.separator + "common" + File.separator + "lib"));

                File serverBinDir = new File(pathPrefix + serverBin);
                copyJars(serverBinDir, new File(layer.getDirectory(), "kernel" + File.separator + "server" + File.separator + "bin"));


                File serverLibDir = new File(pathPrefix + serverLib);
                copyJars(serverLibDir, new File(layer.getDirectory(), "kernel" + File.separator + "server" + File.separator + "lib"));

                File explorerBinDir = new File(pathPrefix + explorerBin);
                copyJars(explorerBinDir, new File(layer.getDirectory(), "kernel" + File.separator + "explorer" + File.separator + "bin"));


                File explorerLibDir = new File(pathPrefix + explorerLib);
                copyJars(explorerLibDir, new File(layer.getDirectory(), "kernel" + File.separator + "explorer" + File.separator + "lib"));

                for (ERuntimeEnvironmentType e : ERuntimeEnvironmentType.values()) {
                    ((AdsSegment) layer.getAds()).getBuildPath().getPlatformLibs().getKernelLib(e).cleanup();
                }
                return;

            }
        }

    }
}
