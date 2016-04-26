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
package org.radixware.kernel.designer.environment.actions;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.xmlbeans.XmlException;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.check.RadixProblemRegistry;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.EValueType;
import org.radixware.kernel.common.defs.ads.ITransparency;
import org.radixware.kernel.common.defs.ads.clazz.*;
import org.radixware.kernel.common.defs.ads.clazz.algo.AdsAlgoStartMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsApplicationClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityModelClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.kernel.common.defs.ads.clazz.presentation.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsSelectorPresentationDef.SelectorColumn;
import org.radixware.kernel.common.defs.ads.clazz.presentation.EditorPages.OrderedPage;
import org.radixware.kernel.common.defs.ads.clazz.presentation.editmask.EditMask;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsCursorClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsSqlClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.AdsStatementClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportFormattedCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportPropertyCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSpecialCell;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsReportSummaryCell;
import org.radixware.kernel.common.defs.ads.common.JavaSignatures;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.explorerItems.AdsSelectorExplorerItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsLocalizingBundleDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsImageDef;
import org.radixware.kernel.common.defs.ads.module.AdsModule;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.module.AdsSearcher;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass.Method;
import org.radixware.kernel.common.defs.ads.type.*;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomDialogDef;
import org.radixware.kernel.common.defs.ads.ui.AdsCustomPageEditorDef;
import org.radixware.kernel.common.defs.ads.ui.AdsUIConnection;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.defs.dds.DdsColumnTemplateDef;
import org.radixware.kernel.common.defs.dds.DdsFunctionDef;
import org.radixware.kernel.common.defs.dds.DdsModelDef;
import org.radixware.kernel.common.defs.dds.DdsModule;
import org.radixware.kernel.common.defs.dds.DdsParameterDef;
import org.radixware.kernel.common.defs.dds.DdsReferenceDef;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.defs.dds.DdsTriggerDef;
import org.radixware.kernel.common.defs.value.ValAsStr;
import org.radixware.kernel.common.enums.*;
import org.radixware.kernel.common.exceptions.RadixError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.exceptions.RadixPublishedException;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagId;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.repository.dds.DdsSegment;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.EntityRefValueTag;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.RadixObjectsUtils;
import org.radixware.kernel.common.utils.XmlUtils;
import org.radixware.kernel.designer.common.dialogs.check.CheckResultsTopComponent;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;
import org.radixware.kernel.designer.common.general.editors.EditorsManager;
import org.radixware.kernel.designer.common.general.filesystem.RadixFileUtil;
import org.radixware.kernel.designer.dds.script.ScriptGenerator;
import org.radixware.schemas.adsdef.AdsDefinitionDocument;
import org.radixware.schemas.adsdef.MethodDefinition;
import org.radixware.schemas.xscml.TypeDeclaration;

public final class SelfCheckAction implements ActionListener {

    private static class Collector implements IVisitor {

        final List<RadixObject> radixObjects = new ArrayList<RadixObject>(200000);

        @Override
        public void accept(RadixObject radixObject) {
            radixObjects.add(radixObject);
        }
    }

    private void collectImages(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Map<Class, RadixObject> classes = new HashMap<Class, RadixObject>();
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Definition) {
                classes.put(radixObject.getClass(), radixObject);
            }
        }
        final List<String> results = new ArrayList<String>();
        for (Entry<Class, RadixObject> entry : classes.entrySet()) {
            final Class cl = entry.getKey();
            final String line = cl.getName() + " - " + entry.getValue().getTypeTitle();
            results.add(line);
        }
        Collections.sort(results);
        for (String result : results) {
            sb.append(result);
            sb.append('\n');
        }
    }

    private void findFunctionParameterObject(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof DdsParameterDef) {
                DdsParameterDef param = (DdsParameterDef) radixObject;
                if (param.getValType() == EValType.OBJECT) {
                    sb.append(param.getQualifiedName() + "\n");
                }
            }
            if (radixObject instanceof DdsFunctionDef) {
                DdsFunctionDef func = (DdsFunctionDef) radixObject;
                if (func.getResultValType() == EValType.OBJECT) {
                    sb.append(func.getQualifiedName() + "\n");
                }
            }
        }
    }

    private void findNonNullEditMask(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            EditMask em = null;
            if (radixObject instanceof EditOptions) {
                em = ((EditOptions) radixObject).getEditMask();
            }
            if (em != null) {
                sb.append(em.getOwnerDefinition().getQualifiedName() + "\n");
            }
        }
    }

    private void findDynamicPropsInPresentationClasses(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) radixObject;
                switch (prop.getNature()) {
                    case DYNAMIC:
                        AdsClassDef clazz = prop.getOwnerClass();
                        if (clazz.getUsageEnvironment() == ERuntimeEnvironmentType.EXPLORER) {
                            sb.append(clazz.getQualifiedName());
                            sb.append("\n");
                        }
                        break;
                }
            }
        }
    }

    private void findLargeMethod(final List<RadixObject> radixObjects, final StringBuilder sb) {
        int maxSize = 0;
        Scml result = null;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Scml) {
                int size = 0;
                final Scml scml = (Scml) radixObject;
                for (Scml.Item item : scml.getItems()) {
                    if (item instanceof Scml.Text) {
                        size += ((Scml.Text) item).getText().length();
                    } else {
                        size += 1000;
                    }
                }
                if (size > maxSize) {
                    result = scml;
                    maxSize = size;
                }
            }
        }
        sb.append(result.getQualifiedName());
    }

    private void synchronizeAllAlgoClasses(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsAlgoStartMethodDef) {
                final AdsAlgoStartMethodDef algMethod = (AdsAlgoStartMethodDef) radixObject;
                algMethod.update();
                sb.append(algMethod.getQualifiedName() + "\n");
            }
        }
    }

    private static ERadixIconType getImageType(InputStream inputStream) throws IOException {
        final String SVG_SIGNATURE = "<?xml ";
        final String PNG_SIGNATURE = "PNG"; //?PNG
        final String GIF_SIGNATURE = "GIF";
        final String JPG_SIGNATURE = "JFIF"; //���� JFIF
        final String BMP_SIGNATURE = "BM6";
        StringBuilder str = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            char c = (char) inputStream.read();
            str.append(c);
        }
        String s = str.toString();
        if (s.startsWith(SVG_SIGNATURE)) {
            return ERadixIconType.SVG;
        }
        if (s.startsWith(PNG_SIGNATURE, 1)) {
            return ERadixIconType.PNG;
        }
        if (s.startsWith(GIF_SIGNATURE)) {
            return ERadixIconType.GIF;
        }
        if (s.startsWith(JPG_SIGNATURE, 6)) {
            return ERadixIconType.JPG;
        }

        return ERadixIconType.UNKNOWN;
    }

    public static ERadixIconType getImageType(File file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            return getImageType(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

//    private void renameAllImages(final List<RadixObject> radixObjects, final StringBuilder sb) {
//        int count = 0;
//        for (RadixObject radixObject : radixObjects) {
//            if (radixObject instanceof AdsModule) {
//                final AdsModule module = (AdsModule) radixObject;
//                final List<AdsImageDef> imageDefs = module.getImages().loadAll();
//                for (AdsImageDef imageDef : imageDefs) {
//                    final File file = imageDef.getImageDataFile();
//                    try {
//                        final EImageType imageType = getImageType(file);
//                        final String ext = imageType.name().toLowerCase();
//                        FileUtils.changeFileExt(file, ext);
//                        count++;
//                    } catch (IOException cause) {
//                        DialogUtils.messageError(cause);
//                    }
//                }
//            }
//        }
//        sb.append(count);
//    }
    private void collectWithErrorNames(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Set<File> files = new HashSet<File>();
        for (RadixObject radixObject : radixObjects) {
            if (radixObject.isSaveable()) {
                if (radixObject instanceof AdsLocalizingBundleDef || radixObject instanceof AdsImageDef || radixObject instanceof Module) {
                    continue;
                }
                final String name = radixObject.getName();
                final File dir = radixObject.getFile().getParentFile();
                final File newFile = new File(dir, name + ".xml");

                if (!RadixObjectsUtils.isCorrectName(name) || files.contains(newFile)) {
                    sb.append(radixObject.getTypeTitle() + " " + radixObject.getQualifiedName());
                    sb.append('\n');
                } else {
                    files.add(newFile);
                }
            }
        }
    }

//    private void formatXmlFiles(final List<RadixObject> radixObjects, final StringBuilder sb) {
//        for (RadixObject radixObject : radixObjects) {
//            if (radixObject.isSaveable()) {
//                final File file = radixObject.getFile();
//                try {
//                    if (file.isFile()) {
//                        final String s = FileUtils.readTextFile(file, FileUtils.XML_ENCODING);
//                        final XmlFormatter formatter = new XmlFormatter(new FileOutputStream(file));
//                        try {
//                            formatter.write(s, 0, s.length());
//                        } finally {
//                            formatter.close();
//                        }
//                    }
//                } catch (IOException error) {
//                    DialogUtils.messageError(error);
//                }
//            }
//        }
//    }
    private class Group {

        String name;
        String ownerName = null;
        List<Id> memberIds = new ArrayList<Id>();
        HashMap<String, Group> children = new HashMap<String, Group>();

        void createGroup(AdsPropertyGroup owner) {
            AdsPropertyGroup g = AdsPropertyGroup.Factory.newInstance(name);
            owner.getChildGroups().add(g);
            for (Id id : memberIds) {
                AdsPropertyDef prop = g.getOwnerClass().getProperties().findById(id, EScope.LOCAL).get();
                if (prop != null) {
                    g.addMember(prop);
                }
            }
            for (Group c : children.values()) {
                c.createGroup(g);
            }
        }

        void createGroup(AdsMethodGroup owner) {
            AdsMethodGroup g = AdsMethodGroup.Factory.newInstance(name);
            owner.getChildGroups().add(g);
            for (Id id : memberIds) {
                AdsMethodDef prop = g.getOwnerClass().getMethods().findById(id, EScope.LOCAL).get();
                if (prop != null) {
                    g.addMember(prop);
                }
            }
            for (Group c : children.values()) {
                c.createGroup(g);
            }
        }
    }

    private void collectPropGroups(AdsPropertyGroup root, Collection<AdsPropertyGroup> list) {
        for (AdsPropertyGroup g : root.getChildGroups()) {
            list.add(g);
            collectPropGroups(g, list);
        }
    }

    private void collectMethodGroups(AdsMethodGroup root, Collection<AdsMethodGroup> list) {
        for (AdsMethodGroup g : root.getChildGroups()) {
            list.add(g);
            collectMethodGroups(g, list);
        }
    }

    private void restorePropGroups(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsClassDef) {

                AdsClassDef clazz = (AdsClassDef) obj;

                ArrayList<AdsPropertyGroup> propGroups = new ArrayList<AdsPropertyGroup>();
                collectPropGroups(clazz.getPropertyGroup(), propGroups);
                HashMap<String, Group> propGroupsMap = new HashMap<String, Group>();

                for (AdsPropertyGroup group : propGroups) {
                    Group g = propGroupsMap.get(group.getName());
                    if (g == null) {
                        g = new Group();
                        g.name = group.getName();
                        propGroupsMap.put(g.name, g);
                    }

                    if (group.getChildGroups().isEmpty()) {//own props
                        for (Id id : group.getMemberIds()) {
                            if (!g.memberIds.contains(id)) {
                                g.memberIds.add(id);
                            }
                        }
                    } else if (group.getChildGroups().size() == 1) {
                        AdsPropertyGroup cg = group.getChildGroups().get(0);
                        Group child = propGroupsMap.get(cg.getName());
                        if (child == null) {
                            child = new Group();
                            child.name = cg.getName();

                            propGroupsMap.put(child.name, child);
                        }
                        child.ownerName = group.getName();
                        for (Id id : group.getMemberIds()) {
                            if (!child.memberIds.contains(id)) {
                                child.memberIds.add(id);
                            }
                        }
                    }
                }
                for (Group g : propGroupsMap.values()) {
                    if (g.ownerName != null) {
                        Group owner = propGroupsMap.get(g.ownerName);
                        if (owner != null) {
                            Group c = owner.children.get(g.name);
                            if (c == null) {
                                owner.children.put(g.name, g);
                            } else {
                                for (Id id : g.memberIds) {
                                    if (!c.memberIds.contains(id)) {
                                        c.memberIds.add(id);

                                    }
                                }
                            }
                        } else {
                            g.ownerName = null;
                        }
                    }
                }

                clazz.getPropertyGroup().getChildGroups().clear();

                for (Group g : propGroupsMap.values()) {
                    if (g.ownerName == null) {
                        g.createGroup(clazz.getPropertyGroup());
                    }
                }

                ArrayList<AdsMethodGroup> methodGroups = new ArrayList<AdsMethodGroup>();
                collectMethodGroups(clazz.getMethodGroup(), methodGroups);
                HashMap<String, Group> methodGroupsMap = new HashMap<String, Group>();

                for (AdsMethodGroup group : methodGroups) {
                    Group g = methodGroupsMap.get(group.getName());
                    if (g == null) {
                        g = new Group();
                        g.name = group.getName();
                        methodGroupsMap.put(g.name, g);
                    }

                    if (group.getChildGroups().isEmpty()) {//own props
                        for (Id id : group.getMemberIds()) {
                            if (!g.memberIds.contains(id)) {
                                g.memberIds.add(id);
                            }
                        }
                    } else if (group.getChildGroups().size() == 1) {
                        AdsMethodGroup cg = group.getChildGroups().get(0);
                        Group child = methodGroupsMap.get(cg.getName());
                        if (child == null) {
                            child = new Group();
                            child.name = cg.getName();

                            methodGroupsMap.put(child.name, child);
                        }
                        child.ownerName = group.getName();
                        for (Id id : group.getMemberIds()) {
                            if (!child.memberIds.contains(id)) {
                                child.memberIds.add(id);
                            }
                        }
                    }
                }
                for (Group g : methodGroupsMap.values()) {
                    if (g.ownerName != null) {
                        Group owner = methodGroupsMap.get(g.ownerName);
                        if (owner != null) {
                            Group c = owner.children.get(g.name);
                            if (c == null) {
                                owner.children.put(g.name, g);
                            } else {
                                for (Id id : g.memberIds) {
                                    if (!c.memberIds.contains(id)) {
                                        c.memberIds.add(id);

                                    }
                                }
                            }
                        } else {
                            g.ownerName = null;
                        }
                    }
                }

                clazz.getMethodGroup().getChildGroups().clear();

                for (Group g : methodGroupsMap.values()) {
                    if (g.ownerName == null) {
                        g.createGroup(clazz.getMethodGroup());
                    }
                }

            }
        }
    }

    private void findStrangePresentations(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsPresentationDef) {
                final AdsDefinition def = (AdsDefinition) radixObject;
                final AdsDefinition overridden = def.getHierarchy().findOverridden().get();
                final AdsDefinition overwritten = def.getHierarchy().findOverwritten().get();
                if (overridden != null || overwritten != null) {
                    sb.append(def.getQualifiedName() + "\n");
                }
            }
        }
    }

    private void resaveAll(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject.isSaveable()) {
                try {
                    radixObject.save();
                } catch (IOException error) {
                    DialogUtils.messageError(error);
                }
            }
        }
    }

    private void updateRefMaps(final List<RadixObject> radixObjects, final StringBuilder sb) {
        FieldRefUpdater updater = new FieldRefUpdater();
        for (RadixObject radixObject : radixObjects) {
            updater.update(radixObject, sb);
        }
    }

    private void restoreDependencies(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final List<Module> modules = new ArrayList<Module>();

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Module) {
                modules.add((Module) radixObject);
            }
        }
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsModule) {
                AdsModule adsModule = (AdsModule) radixObject;
                if (adsModule.getSegment().getLayer().getURI().equals("com.tranzaxis")) {
                    adsModule.getDependences().clear();
                    for (Module module : modules) {
                        adsModule.getDependences().add(module);
                    }
                    try {
                        adsModule.saveDescription();
                    } catch (IOException cause) {
                        DialogUtils.messageError(cause);
                    }
                }
            }
        }
    }
    private static final Logger log = Logger.getLogger(SelfCheckAction.class.getName());

    private void testLoggers(final List<RadixObject> radixObjects, final StringBuilder sb) {
        try {
            throw new RadixError("Test2");
        } catch (RadixError er) {
            log.log(Level.WARNING, "test", er);
        }

    }

    private void checkTags(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof EntityRefValueTag) {
                sb.append(radixObject.getQualifiedName() + "\n");
            }
        }
    }

    private AdsTypeDeclaration getNewType(RadixObject obj, AdsTypeDeclaration oldType, StringBuilder sb) {
        if (oldType.getTypeId() == EValType.JAVA_CLASS) {
            String clazz = oldType.getExtStr();
            if (clazz.indexOf(".") < 0) {
                String newClazz = null;

                if ("Object".equals(clazz)) {
                    newClazz = java.lang.Object.class.getName();
                } else if ("List".equals(clazz)) {
                    newClazz = java.util.List.class.getName();
                } else if ("SortedMap".equals(clazz)) {
                    newClazz = java.util.SortedMap.class.getName();
                } else if ("HashMap".equals(clazz)) {
                    newClazz = java.util.HashMap.class.getName();
                } else if ("String".equals(clazz)) {
                    return AdsTypeDeclaration.Factory.newInstance(EValType.STR);
                } else if ("Integer".equals(clazz)) {
                    newClazz = Integer.class.getName();
                } else if ("Vector".equals(clazz)) {
                    newClazz = Vector.class.getName();
                } else if ("Map".equals(clazz)) {
                    newClazz = Map.class.getName();
                } else if ("Throwable".equals(clazz)) {
                    newClazz = Throwable.class.getName();
                } else if ("RandomAccessFile".equals(clazz)) {
                    newClazz = RandomAccessFile.class.getName();
                } else if ("File".equals(clazz)) {
                    newClazz = File.class.getName();
                } else if ("ByteBuffer".equals(clazz)) {
                    newClazz = ByteBuffer.class.getName();
                } else if ("Long".equals(clazz)) {
                    return AdsTypeDeclaration.Factory.newInstance(EValType.INT).toArrayType(oldType.getArrayDimensionCount());
                } else if ("Boolean".equals(clazz)) {
                    return AdsTypeDeclaration.Factory.newInstance(EValType.BOOL).toArrayType(oldType.getArrayDimensionCount());
                } else if ("TreeMap".equals(clazz)) {
                    newClazz = TreeMap.class.getName();
                } else if ("InputStream".equals(clazz)) {
                    newClazz = InputStream.class.getName();
                } else if ("SortedSet".equals(clazz)) {
                    newClazz = SortedSet.class.getName();
                } else if ("Class".equals(clazz)) {
                    newClazz = Class.class.getName();
                } else if ("Double".equals(clazz)) {
                    newClazz = Double.class.getName();
                } else if ("byte".equals(clazz) || "byte ".equals(clazz)) {
                    return AdsTypeDeclaration.Factory.newPrimitiveType("byte").toArrayType(oldType.getArrayDimensionCount());
                } else if ("InputStreamReader".equals(clazz)) {
                    newClazz = InputStreamReader.class.getName();
                } else if ("Collection".equals(clazz)) {
                    newClazz = Collection.class.getName();
                } else if ("QCalendarWidget".equals(clazz) || "QAbstractButton".equals(clazz) || "QIcon".equals(clazz) || "QMenu".equals(clazz) || "QAction".equals(clazz) || "QPainter".equals(clazz) || "QLineEdit".equals(clazz) || "QTreeWidgetItem".equals(clazz) || "QTableWidget".equals(clazz) || "QStackedWidget".equals(clazz) || "QToolButton".equals(clazz) || "QTabWidget".equals(clazz) || "QToolBar".equals(clazz) || "QTableWidgetItem".equals(clazz) || "QWidget".equals(clazz)) {
                    newClazz = "com.trolltech.qt.gui." + clazz;
                } else if ("QPoint".equals(clazz)) {
                    newClazz = "com.trolltech.qt.core." + clazz;
                } else if ("StringBuilder".equals(clazz)) {
                    newClazz = StringBuilder.class.getName();
                } else if ("List<Range>".equals(clazz) || "List <Range>".equals(clazz)) {
                    AdsTypeDeclaration.TypeArguments args = AdsTypeDeclaration.TypeArguments.Factory.newInstance(null);
                    args.add(AdsTypeDeclaration.TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newPlatformClass("Range")));
                    return AdsTypeDeclaration.Factory.newPlatformClass(java.util.List.class.getName()).toGenericType(args).toArrayType(oldType.getArrayDimensionCount());
                } else {
                    if (obj instanceof AdsClassMember) {
                        AdsClassDef oc = ((AdsClassMember) obj).getOwnerClass();
                        if (oc != null) {
                            Jml header = oc.getHeader().ensureFirst();
                            for (Scml.Item item : header.getItems()) {
                                if (item instanceof Scml.Text) {
                                    String text = ((Scml.Text) item).getText();
                                    int index = text.indexOf("import ");
                                    while (index >= 0) {
                                        int semicolon = text.indexOf(";", index);
                                        if (semicolon >= 0) {
                                            String className = text.substring(index + 6, semicolon);
                                            if (className.endsWith("." + clazz)) {
                                                newClazz = className.trim();
                                            }
                                        } else {
                                            break;
                                        }
                                        index = text.indexOf("import ", semicolon);
                                    }
                                }
                            }
                        }
                    }
                }

                if (newClazz != null) {
                    AdsTypeDeclaration result = null;
                    IPlatformClassPublisher pub = ((AdsSegment) obj.getModule().getSegment()).getBuildPath().getPlatformPublishers().findPublisherByName(newClazz);
                    if (pub != null) {
                        IPlatformClassPublisher.IPlatformClassPublishingSupport support = pub.getPlatformClassPublishingSupport();
                        if (pub instanceof IAdsTypeSource && support != null && support.isPlatformClassPublisher() && !support.isExtendablePublishing()) {
                            obj.getModule().getDependences().add(((AdsDefinition) pub).getModule());
                            result = AdsTypeDeclaration.Factory.newInstance((IAdsTypeSource) pub);
                        }
                    }
                    if (result == null) {
                        result = AdsTypeDeclaration.Factory.newPlatformClass(newClazz);
                    }
                    String oldTypeStr = oldType.getQualifiedName(obj);

                    if (oldType.getGenericArguments() != null && oldType.getGenericArguments().getArgumentList() != null) {
                        for (AdsTypeDeclaration.TypeArgument a : oldType.getGenericArguments().getArgumentList()) {
                            AdsTypeDeclaration decl = getNewType(obj, a.getType(), sb);
                            if (decl != null) {
                                a.setType(decl);
                            }
                        }
                        result = result.toGenericType(oldType.getGenericArguments());
                    }
                    result = result.toArrayType(oldType.getArrayDimensionCount());
                    sb.append("------> " + oldTypeStr + "------" + result.getQualifiedName(obj) + " at " + obj.getQualifiedName() + "\n");
                    return result;
                } else {
                    sb.append(oldType.getQualifiedName(obj));
                    sb.append(" at ");
                    sb.append(obj.getQualifiedName());
                    sb.append("\n");
                }
            }
        }
        return null;
    }

    private void checkJavaClasses(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) radixObject;
                AdsTypeDeclaration decl = getNewType(prop, prop.getValue().getType(), sb);
                if (decl != null) {
                    prop.getValue().setType(decl);
                }

            } else if (radixObject instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) radixObject;
                if (!method.isConstructor()) {
                    AdsTypeDeclaration decl = getNewType(method, method.getProfile().getReturnValue().getType(), sb);
                    if (decl != null) {
                        method.getProfile().getReturnValue().setType(decl);
                    }

                }
                for (MethodParameter p : method.getProfile().getParametersList()) {
                    AdsTypeDeclaration decl = getNewType(method, p.getType(), sb);
                    if (decl != null) {
                        p.setType(decl);
                    }

                }
            }
        }
    }

    private static final class VisitChecker implements IVisitor {

        final Map<RadixObject, String> object2Stack = new HashMap<RadixObject, String>();
        final StringBuilder sb;
        boolean found = false;

        public VisitChecker(StringBuilder sb) {
            this.sb = sb;
        }

        @Override
        public void accept(RadixObject radixObject) {
            if (found) {
                return;
            }
            if (radixObject instanceof AdsTypeDeclaration) {
                return;
            }

            final RadixObjectError error = new RadixObjectError("Attempt to visit object twice", radixObject);
            final String newStack = null;//ExceptionTextFormatter.exceptionStackToString(error);

            final String oldStack = object2Stack.get(radixObject);
            if (object2Stack.containsKey(radixObject)) {
                sb.append("Old Stack:\n" + oldStack + "\n\n" + "NewStack:\n" + newStack);
                found = true;
            } else {
                object2Stack.put(radixObject, newStack);
            }
        }
    }

    private void checkVisit(final StringBuilder sb) {
        final VisitChecker visitChecker = new VisitChecker(sb);
        for (Branch branch : RadixFileUtil.getOpenedBranches()) {
            branch.visit(visitChecker, VisitorProviderFactory.createDefaultVisitorProvider());
        }
    }

    private void viewDependenciesTable(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Map<Class, Set<Class>> class2classes = new HashMap<Class, Set<Class>>();

        for (RadixObject radixObject : radixObjects) {
            final List<Definition> defs = new ArrayList<Definition>();
            radixObject.collectDependences(defs);
            for (Definition def : defs) {
                Set<Class> classes = class2classes.get(def.getClass());
                if (classes == null) {
                    classes = new HashSet<Class>();
                    class2classes.put(def.getClass(), classes);
                }

                classes.add(radixObject.getClass());
            }
        }

        for (Map.Entry<Class, Set<Class>> entry : class2classes.entrySet()) {
            final Class cl = entry.getKey();
            final Set<Class> classes = entry.getValue();
            if (!classes.isEmpty()) {
                sb.append(cl.getName() + "\n");
                for (Class used : classes) {
                    sb.append("  " + used.getName() + "\n");
                }
            }
        }
    }

    private void measureCollectDependenciesSpeed(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Map<Class, List<Long>> class2times = new HashMap<Class, List<Long>>();

        for (RadixObject radixObject : radixObjects) {
            List<Long> times = class2times.get(radixObject.getClass());
            if (times == null) {
                times = new ArrayList<Long>();
                class2times.put(radixObject.getClass(), times);
            }

            final List<Definition> defs = new ArrayList<Definition>();
            long t = System.nanoTime();
            radixObject.collectDependences(defs);
            t = System.nanoTime() - t;
            times.add(Long.valueOf(t));
        }

        for (Map.Entry<Class, List<Long>> entry : class2times.entrySet()) {
            final Class cl = entry.getKey();
            final List<Long> times = entry.getValue();
            long sum = 0;
            for (Long t : times) {
                sum += t;
            }
            final int size = times.size();
            sum = sum / size;
            if (sum > 100000 || size > 10000) { // 0.1 msec or 10 000 times
                sb.append(sum);
                sb.append(" ns, ");
                sb.append(size);
                sb.append(" times: ");
                sb.append(cl.getName());
                sb.append('\n');
            }
        }
    }

    private void getClassCatalogsContainers(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Set<Class> containers = new HashSet<Class>();
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsClassCatalogDef) {
                for (RadixObject container = radixObject.getContainer(); container != null; container = container.getContainer()) {
                    containers.add(container.getClass());
                }
            }
        }
        for (Class cl : containers) {
            sb.append(cl.getName() + "\n");
        }
    }

    private void findScmlItems(final List<RadixObject> radixObjects, final StringBuilder sb) {
        int items = 0;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Scml.Item) {
                items++;
            }
        }
        sb.append(radixObjects.size() + "/" + items);
    }

//    private void resetAccessMode(final List<RadixObject> radixObjects, final StringBuilder sb) {
//        int items = 0;
//        for (RadixObject radixObject : radixObjects) {
//            if (radixObject instanceof AdsDefinition && ((AdsDefinition) radixObject).canChangeAccessMode() && ((AdsDefinition) radixObject).getAccessMode() == EAccess.PUBLIC) {
//                ((AdsDefinition) radixObject).setAccessMode(EAccess.PUBLISHED);
//                sb.append(radixObject.getQualifiedName() + "\n");
//            }
//        }
//        for (RadixObject radixObject : radixObjects) {
//            if (radixObject instanceof AdsMethodDef) {
//                AdsMethodDef m = (AdsMethodDef) radixObject;
//                AdsMethodDef root = null;
//                AdsMethodDef ovr = m.getHierarchy().findOverridden();
//                while (ovr != null) {
//                    root = ovr;
//                    ovr = ovr.getHierarchy().findOverridden();
//                }
//                if (root != null) {
//                    m.setAccessMode(root.getAccessMode());
//                    sb.append(m.getQualifiedName() + "\n");
//                }
//            }
//        }
//
//    }
    private void findFieldRefs(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            final List<Definition> defs = new ArrayList<Definition>();
            radixObject.collectDependences(defs);
            for (Definition def : defs) {
                if ((def instanceof AdsFieldPropertyDef) && !(def instanceof AdsFieldRefPropertyDef)) {
                    final Layer layer = def.getModule().getSegment().getLayer();
                    if (layer.getURI().equals("org.radixware") && layer != radixObject.getModule().getSegment().getLayer()) {
                        sb.append(def.getQualifiedName() + "\n");
                    }
                }
            }
        }
    }

    private void resetCursors(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Id closeMethodId = Id.Factory.loadFrom("mth_stmt_close_______________");

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsSqlClassDef) {
                final AdsSqlClassDef sqlClass = (AdsSqlClassDef) radixObject;
                sqlClass.setCacheSize(10);

                if (radixObject instanceof AdsCursorClassDef) {
                    final AdsCursorClassDef cursor = (AdsCursorClassDef) radixObject;
                    cursor.setDbReadOnly(true);
                    cursor.setUniDirect(true);
                }

                final AdsMethodDef closeMethod = sqlClass.getMethods().findById(closeMethodId, EScope.LOCAL).get();
                if (closeMethod != null) {
                    closeMethod.delete();
                }

                for (final AdsMethodDef method : sqlClass.getMethods().get(EScope.LOCAL)) {
                    if (method.isConstructor()) {
                        method.delete();
                        break;
                    }
                }

                sb.append(sqlClass.getQualifiedName());
                sb.append("\n");
            }
        }
    }

    private void openMethods(final List<RadixObject> radixObjects, final StringBuilder sb) {
        int i = 0;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsMethodDef) {
                if (i % 6 == 0) {
                    EditorsManager.getDefault().open(radixObject);
                }
                i++;
            }
        }
    }

//    private void removePubs(final List<RadixObject> radixObjects, final StringBuilder sb) {
//        int i = 0;
//        for (RadixObject radixObject : radixObjects) {
//            if (radixObject instanceof AdsTransparentMethodDef) {
//                RadixPlatformClass.Method m = ((AdsTransparentMethodDef) radixObject).findPublishedMethod();
//                if (m != null) {
//                    if (m.getAccess() == EAccess.PRIVATE) {
//                        radixObject.delete();
//                    } else {
//                        if (m.getAccess() != ((AdsTransparentMethodDef) radixObject).getAccessMode()) {
//                            if (((AdsTransparentMethodDef) radixObject).getAccessMode() == EAccess.PUBLISHED && m.getAccess() == EAccess.PUBLIC) {
//                            } else {
//                                ((AdsTransparentMethodDef) radixObject).setAccessMode(m.getAccess());
//                            }
//                        }
//                    }
//                    sb.append(radixObject.getQualifiedName() + "\n");
//                }
//                i++;
//            }
//        }
//    }
    private void updatePubs(final List<RadixObject> radixObjects, final StringBuilder sb) {

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsTransparentMethodDef) {
                RadixPlatformClass.Method m = ((AdsTransparentMethodDef) radixObject).findPublishedMethod();
                if (m != null) {
                    if (m.isStatic() != ((AdsTransparentMethodDef) radixObject).getProfile().getAccessFlags().isStatic()) {
                        ((AdsTransparentMethodDef) radixObject).getProfile().getAccessFlags().setStatic(m.isStatic());
                        sb.append(radixObject.getQualifiedName() + "\n");
                    }
                }

            }
        }
    }

    private void updatePps(final List<RadixObject> radixObjects, final StringBuilder sb) {

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof PropertyPresentation) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                sb.append("Unset presentable:" + radixObject.getQualifiedName() + "\n");

                ((PropertyPresentation) radixObject).setPresentable(false);
            }
        }
        final ArrayList<AdsEditorPageDef> invisiblePages = new ArrayList<AdsEditorPageDef>();
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsEditorPageDef) {
                AdsEditorPageDef page = (AdsEditorPageDef) radixObject;

                PropertyUsageSupport support = page.getUsedProperties();
                for (Id id : support.getIds()) {
                    PropertyUsageSupport.PropertyRef ref = support.getReference(id);

                    AdsDefinition pc = ref.findProperty();

                    if (pc.isReadOnly()) {
                        continue;
                    }
                    if (pc instanceof AdsPropertyDef && pc instanceof IAdsPresentableProperty) {
                        IAdsPresentableProperty prop = (IAdsPresentableProperty) pc;
                        ServerPresentationSupport ps = prop.getPresentationSupport();
                        if (ps != null && ps.getPresentation() != null) {
                            sb.append("Set presentable:" + pc.getQualifiedName() + "\n");
                            ps.getPresentation().setPresentable(true);
                        }
                    }
                }
            }
        }

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof SelectorColumn) {
                AdsPropertyDef pc = ((SelectorColumn) radixObject).findProperty();
                if (pc.isReadOnly()) {
                    continue;
                }
                if (pc instanceof IAdsPresentableProperty) {
                    IAdsPresentableProperty prop = (IAdsPresentableProperty) pc;
                    ServerPresentationSupport ps = prop.getPresentationSupport();
                    if (ps != null && ps.getPresentation() != null) {
                        sb.append("Set presentable:" + pc.getQualifiedName() + "\n");
                        ps.getPresentation().setPresentable(true);
                    }
                }
            }
        }

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof OrderedPage) {
                OrderedPage op = (OrderedPage) radixObject;
                AdsEditorPageDef page = op.findPage();
                if (page != null) {
                    if (invisiblePages.contains(page)) {
                        if (op.isReadOnly()) {
                            continue;
                        }
                        ;
                        sb.append("Deleted order:" + op.getQualifiedName() + "\n");
                        op.delete();

                    }
                }
            }
        }
        for (AdsEditorPageDef page : invisiblePages) {
            if (page.isInBranch() && !page.isReadOnly()) {
                sb.append("Deleted page:" + page.getQualifiedName() + "\n");
                page.delete();
            }
        }
    }

    private void updatePps2(final List<RadixObject> radixObjects, final StringBuilder sb) {

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof PropertyPresentation) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                AdsPropertyDef prop = ((PropertyPresentation) radixObject).getOwnerProperty();
                if (prop != null) {
                    AdsClassDef clazz = prop.getOwnerClass();
                    if (clazz instanceof AdsFormHandlerClassDef) {
                        sb.append("Set presentable:" + radixObject.getQualifiedName() + "\n");
                        ((PropertyPresentation) radixObject).setPresentable(true);
                    }
                }
            }
        }
    }

    private void removePropPresentationProps(final List<RadixObject> radixObjects, final StringBuilder sb) {
        int count = 0;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Jml) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                for (int i = 0; i < ((Jml) radixObject).getItems().size(); i++) {
                    Scml.Item item = ((Jml) radixObject).getItems().get(i);
                    if (item instanceof JmlTagId) {
                        Definition def = ((JmlTagId) item).resolve(((Jml) radixObject).getOwnerDef());
                        if (def instanceof AdsPropertyPresentationPropertyDef) {
                            IModelPublishableProperty prop = ((AdsPropertyPresentationPropertyDef) def).findServerSideProperty();
                            if (prop instanceof AdsPropertyDef) {
                                ((JmlTagId) item).setPath(new AdsPath((AdsPropertyDef) prop));
                                if (((AdsPropertyDef) prop).getAccessMode() == EAccess.PRIVATE) {
                                    ((AdsPropertyDef) prop).setAccessMode(EAccess.PROTECTED);
                                }
                                count++;
                                sb.append(def.getQualifiedName() + " --> " + ((AdsPropertyDef) prop).getQualifiedName() + "\n");
                            }
                        }
                    }
                }
            }
        }
        sb.append("Total:" + count + "\n");

        count = 0;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsPropertyPresentationPropertyDef) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                AdsPropertyPresentationPropertyDef prop = (AdsPropertyPresentationPropertyDef) radixObject;
                if (prop.isGetterDefined(EScope.LOCAL) || prop.isSetterDefined(EScope.LOCAL)) {
                    continue;
                }

                if (prop.getOwnerClass().getClassDefType() != EClassType.ENTITY_MODEL && prop.getOwnerClass().getClassDefType() != EClassType.GROUP_MODEL) {
                    continue;
                }

                prop.delete();
                count++;
                sb.append("Deleted:" + radixObject.getQualifiedName() + "\n");
            }
        }
        sb.append("Total:" + count + "\n");
        count = 0;

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof Jml) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                for (int i = 0; i < ((Jml) radixObject).getItems().size(); i++) {
                    Scml.Item item = ((Jml) radixObject).getItems().get(i);
                    if (item instanceof JmlTagTypeDeclaration) {
                        AdsType type = ((JmlTagTypeDeclaration) item).getType().resolve(((Jml) radixObject).getOwnerDef()).get();
                        if (type instanceof AdsClassType) {
                            AdsClassDef cd = ((AdsClassType) type).getSource();
                            if (cd instanceof AdsEntityModelClassDef) {
                                if (cd.getProperties().getLocal().isEmpty() && cd.getMethods().getLocal().isEmpty()) {
                                    if (cd.getHeader().ensureFirst().getItems().isEmpty() && cd.getBody().ensureFirst().getItems().isEmpty()) {
                                        if (cd.getInheritance().getInerfaceRefList(EScope.LOCAL).isEmpty()) {
                                            ((JmlTagTypeDeclaration) item).setType(AdsTypeDeclaration.Factory.newInstance(((AdsEntityModelClassDef) cd).findServerSideClasDef()));
                                            count++;
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }
        sb.append("Total:" + count + "\n");
        count = 0;

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsPresentationDef) {
                AdsModelClassDef clazz = ((AdsPresentationDef) radixObject).getModel();
                if (clazz.getProperties().getLocal().isEmpty() && clazz.getMethods().getLocal().isEmpty()) {
                    if (clazz.getHeader().ensureFirst().getItems().isEmpty() && clazz.getBody().ensureFirst().getItems().isEmpty()) {
                        if (clazz.getInheritance().getInerfaceRefList(EScope.LOCAL).isEmpty()) {
                            ((AdsPresentationDef) radixObject).setUseDefaultModel(true);
                            sb.append("Model Deleted:" + radixObject.getQualifiedName() + "\n");
                            count++;
                        }
                    }
                }
            }
        }
        sb.append("Total:" + count + "\n");

        count = 0;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsPropertyPresentationPropertyDef) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                if (!radixObject.isInBranch()) {
                    continue;

                }
                AdsPropertyPresentationPropertyDef prop = (AdsPropertyPresentationPropertyDef) radixObject;

                if (prop.isOverride()) {
                    if (prop.getHierarchy().findOverridden() == null) {
                        prop.setOverride(false);
                        count++;
                        sb.append("Unset override:" + radixObject.getQualifiedName() + "\n");
                    }
                }
            }
        }
        sb.append("Total:" + count + "\n");
    }

    private void updateEIS(final List<RadixObject> radixObjects, final StringBuilder sb) {
        int count = 0;
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsSelectorExplorerItemDef) {
                if (radixObject.isReadOnly()) {
                    continue;
                }
                AdsSelectorExplorerItemDef ei = (AdsSelectorExplorerItemDef) radixObject;
                ei.getOwnRestrictions().allow(ERestriction.ACCESS);
                count++;
                sb.append("Updated:" + radixObject.getQualifiedName() + "\n");
            }
        }
        sb.append("Total:" + count + "\n");
    }

    private void updatePubMethodRefs(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final boolean IS_PATCH_MODE = false;
        int count = 0;
        final List<AdsClassDef> transparentClasses = new ArrayList<AdsClassDef>();

        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsClassDef) {
                AdsClassDef clazz = (AdsClassDef) radixObject;
                AdsTransparence t = clazz.getTransparence();
                if (t != null && t.isTransparent()) {
                    transparentClasses.add(clazz);
                }
            }
        }
        final HashMap<AdsMethodDef, AdsMethodDef> methodReplaceMap = new HashMap<AdsMethodDef, AdsMethodDef>();
        for (AdsClassDef clazz : transparentClasses) {
            List<AdsMethodDef> methods = clazz.getMethods().get(EScope.ALL, new IFilter<AdsMethodDef>() {
                @Override
                public boolean isTarget(AdsMethodDef radixObject) {
                    return radixObject instanceof AdsTransparentMethodDef;
                }
            });
            List<AdsMethodDef> ownMethods = new ArrayList<AdsMethodDef>();
            for (int i = 0; i < methods.size();) {
                AdsMethodDef method = methods.get(i);
                if (method.getOwnerClass() == clazz) {
                    ownMethods.add(method);
                    methods.remove(i);
                } else {
                    i++;
                }
            }

            List<AdsMethodDef> deletedOwnMethods = new ArrayList<AdsMethodDef>();
            for (AdsMethodDef ownMethod : ownMethods) {
                if (ownMethod.isConstructor()) {
                    continue;
                }
                String ownsig = new String(JavaSignatures.generateSignature(clazz, ownMethod, false));
                for (AdsMethodDef method : ownMethods) {
                    if (method == ownMethod || deletedOwnMethods.contains(method)) {
                        continue;
                    }
                    if (method.getId() != ownMethod.getId() && method.getName().equals(ownMethod.getName())) {
                        String sig = new String(JavaSignatures.generateSignature(clazz, ownMethod, false));
                        if (sig.equals(ownsig)) {
                            sb.append("\nDuplicate methods in same class: ").append(ownMethod.getQualifiedName()).append("  -> ").append(method.getQualifiedName());
                            methodReplaceMap.put(ownMethod, method);
                            deletedOwnMethods.add(ownMethod);
                            break;
                        }
                    }
                }
            }
            for (AdsMethodDef method : deletedOwnMethods) {
                ownMethods.remove(method);
            }

            for (AdsMethodDef ownMethod : ownMethods) {
                if (ownMethod.isConstructor()) {
                    continue;
                }
                String ownsig = new String(JavaSignatures.generateSignature(clazz, ownMethod, false));
                for (AdsMethodDef method : methods) {
                    if (method.getId() != ownMethod.getId() && method.getName().equals(ownMethod.getName())) {
                        String sig = new String(JavaSignatures.generateSignature(clazz, ownMethod, false));
                        if (sig.equals(ownsig)) {
                            sb.append("\nFirst chance replacement found: ").append(ownMethod.getQualifiedName()).append("  -> ").append(method.getQualifiedName());
                            methodReplaceMap.put(ownMethod, method);
                            break;
                        }
                    }
                }
            }
        }

        class Method2Id {

            final AdsMethodDef method;
            final Id id;

            public Method2Id(AdsMethodDef method, Id id) {
                this.method = method;
                this.id = id;
            }
        }
        class Method2Replacer {

            final AdsMethodDef method;
            final AdsMethodDef replacer;

            public Method2Replacer(AdsMethodDef method, AdsMethodDef replacer) {
                this.method = method;
                this.replacer = replacer;
            }
        }
        final List<Method2Id> toChangeId = new ArrayList<Method2Id>();
        final List<Method2Replacer> toDelete = new ArrayList<Method2Replacer>();

        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) obj;
                AdsMethodDef replacement = getFinalReplacementForMethod(method, methodReplaceMap);
                if (replacement != null) {
                    sb.append("\nWill be deleted with substitution: ").append(method.getQualifiedName()).append("  -> ").append(replacement.getQualifiedName());
                    toDelete.add(new Method2Replacer(method, replacement));
                } else {//no direct replacement,possible id change
                    boolean found = false;
                    AdsMethodDef ovr = method.getHierarchy().findOverwritten().get();
                    while (ovr != null) {
                        replacement = getFinalReplacementForMethod(ovr, methodReplaceMap);
                        if (replacement != null) {
                            sb.append("\nId will be changed: ").append(method.getQualifiedName()).append("  -> ").append(replacement.getQualifiedName());
                            toChangeId.add(new Method2Id(method, replacement.getId()));
                            found = true;
                            break;
                        }
                        ovr = ovr.getHierarchy().findOverwritten().get();
                    }
                    if (!found) {
                        ovr = method.getHierarchy().findOverridden().get();
                        while (ovr != null) {
                            replacement = getFinalReplacementForMethod(ovr, methodReplaceMap);
                            if (replacement != null) {
                                sb.append("\nId will be changed: ").append(method.getQualifiedName()).append("  -> ").append(replacement.getQualifiedName());
                                toChangeId.add(new Method2Id(method, replacement.getId()));
                                found = true;
                                break;
                            }
                            ovr = ovr.getHierarchy().findOverridden().get();
                        }
                    }
                }
            }
        }

        for (RadixObject obj : radixObjects) {
            if (obj instanceof JmlTagId) {
                JmlTagId idTag = (JmlTagId) obj;
                AdsDefinition container = idTag.getOwnerJml().getOwnerDef();
                if (container.isReadOnly()) {
                    continue;
                }
                Definition def = idTag.resolve(container);
                if (def instanceof AdsMethodDef) {
                    for (Method2Replacer method : toDelete) {
                        if (method.method == def) {
                            idTag.setPath(new AdsPath(method.replacer.getIdPath()));
                            sb.append("\nUpdate reference to removed method: ").append(method.method.getQualifiedName()).append("  -> ").append(method.replacer.getQualifiedName());
                        }
                    }
                    for (Method2Id method2Id : toChangeId) {
                        if (method2Id.method == def) {
                            Id[] path = method2Id.method.getIdPath();
                            path[path.length - 1] = method2Id.id;
                            idTag.setPath(new AdsPath(path));
                            sb.append("\nUpdate reference to new id of method : ").append(method2Id.method.getQualifiedName()).append("  -> ").append(method2Id.id);
                        }
                    }
                }
            }
        }

        for (Method2Replacer method : toDelete) {
            if (!IS_PATCH_MODE) {
                if (method.method.getOwnerClass().isReadOnly()) {
                    continue;
                }
                method.method.delete();

            }
        }
        for (Method2Id method2Id : toChangeId) {
            Id id = method2Id.method.getId();
            if (id != method2Id.id) {
                if (method2Id.method.getOwnerClass().isReadOnly()) {
                    continue;
                }
                method2Id.method.supportForSelfCheckOnPublishing(method2Id.id);
            }
        }

    }

    private void addBatchMethodsToStatements(List<RadixObject> radixObjects, StringBuilder sb) {
        for (RadixObject radixObject : radixObjects) {
            if (radixObject instanceof AdsStatementClassDef) {
                boolean hasSendBatchMethod = false;
                boolean hasSetExecuteBatchMethod = false;
                final Definitions<AdsMethodDef> methods = ((AdsStatementClassDef) radixObject).getMethods().getLocal();
                for (AdsMethodDef method : methods) {
                    if (AdsSystemMethodDef.ID_STMT_SEND_BATCH.toString().equals(method.getId().toString())) {
                        hasSendBatchMethod = true;
                    }
                    if (AdsSystemMethodDef.ID_STMT_SET_EXECUTE_BATCH.toString().equals(method.getId().toString())) {
                        hasSetExecuteBatchMethod = true;
                    }
                }
                if (!hasSendBatchMethod) {
                    methods.add(AdsSystemMethodDef.Factory.newSendBatchStmt());
                }
                if (!hasSetExecuteBatchMethod) {
                    methods.add(AdsSystemMethodDef.Factory.newSetExecuteBatchStmt());
                }
                if (!hasSendBatchMethod || !hasSetExecuteBatchMethod) {
                    sb.append("Add ");
                    if (!hasSendBatchMethod) {
                        sb.append("sendBatch() ");
                    }
                    if (!hasSetExecuteBatchMethod) {
                        sb.append("setExecuteBatch(int size) ");
                    }
                    sb.append("to ");
                    sb.append(radixObject.getQualifiedName());
                    sb.append('\n');
                }
            }
        }
    }

    private AdsMethodDef getFinalReplacementForMethod(AdsMethodDef method, HashMap<AdsMethodDef, AdsMethodDef> map) {
        AdsMethodDef candidate = map.get(method);
        AdsMethodDef result = candidate;
        while (candidate != null) {
            candidate = map.get(candidate);
            if (candidate != null) {
                result = candidate;
            }
        }
        return result;
    }

    private void fixTx(final List<RadixObject> radixObjects, final StringBuilder sb) {
        HashMap<String, String[]> idsMap = new HashMap<String, String[]>();
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthLVFY4BTKLZHJPB2NX276PVXK4I", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthTTUQQ3QNAVENTCMAW45HZ7Q4UE"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthX2P23MQKBBBDFDMXNASUG6WBPU", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mth3IC5KWE4XJHVNALB7QRHNEB63Q"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthEB5KDZILYBDDJEUPOU4XHQ7SMA", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthBN46PMVUBFADVFFMPLGSSR7ZRM"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthCLIGGMVKDVFJLJCSWDATVFA52U", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthBN46PMVUBFADVFFMPLGSSR7ZRM"});

        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthPCQZDOTRIZFVFD3JBYMAA4BGZQ", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthCLIGGMVKDVFJLJCSWDATVFA52U"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthJ3QTHDECSJHVNIEHKJFH6H4HUU", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthZXLWC3AQT5ARLORV4S2MZR3MGI"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthJ67ZNNAFXRCQ7G5ART5L6CUARM", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthGMLQSMXV7VFUPJKCIEWSHHVOUU"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthN2LXT7HF45HXJOVQVKF23JLV4A", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthQJLQGY6YVNGKTJDKPGGI2WNZGU"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mth4GPGYP4CTBGCHNSLDVX73DR2XQ", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthTY5EMSI2EBDPLL7SNAHV7FO6D4"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthKLUYMMPA4BGZZL573XZ43EE6MA", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthDJZSTHREHVDTFB323J3RXPJYNY"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthOB5GAKAOWNBGZPCXC2NLGEPMMA", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthHUS5XHSNB5E3RB6FMGZBODKOSM"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthT4WNM265GBB4PMT7TNAUDNSFMM", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mth3HBB5FVQXRC4LP2ITXM6TXVDYI"});

        idsMap.put("adc3T4OO4IRSBBVFBEUYJMJ7V4TMU:mth3OMNUZNFDZHGRPOPA5NSXLFAFE", new String[]{"adc577BIS4VOZF6LHBG5OV7JJ5SVY", "mthCKIIJQS3LBB6ZNOGGM25KC5HOY"});
        idsMap.put("adcFSI3GFO2XVCRJEEMXETCN54GMM:mthCOKKAU5LQZCOZBPLH65O5SCKVM", new String[]{"adcFSI3GFO2XVCRJEEMXETCN54GMM", "mthMPPPCYFVEJBDZJC3LNBBMWMTR4"});
        idsMap.put("adcZ3N2ZQF7BVGB7NZZIGQP5RUK5I:mth42V26UI2YBB2BCWRCBY3VSEJQA", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthFLL26J774NG2NALGUR4MNYV42I"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthB3XKNLDRTBCVDDKNOFUAEH6NDA", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthDIJBS4PRMZCI7BX2VH6YVINVOM"});

        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mth3WTZJXNRR5CBBCVGBH2VMHDKAU", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthI4ZEAFBPYRGELAKWN3W2VE4SA4"});
        idsMap.put("adcZ3N2ZQF7BVGB7NZZIGQP5RUK5I:mthB6C47REVIRGNFKWZQ3VBHVXMXQ", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthYJXQ7ZHE2FGB5D2NE6CSGFKRUI"});
        idsMap.put("pdcEntity____________________:mthX5SVRECDLNETTJYVB5HABH6PRA", new String[]{"pdcEntity____________________", "mthJ66JVZJF57NRDIMQAAMPGXSZKU"});

        idsMap.put("adcJY23D47GDFBFTLU26GY5RGU7KY:mthGLICEILXEJGC3EELS2GI7MWCA4", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mth4OEHUJ3KFRBJRDYQW3W6NUUVCM"});
        idsMap.put("adcB3YNBRATAVEHRM3YZIEXM5HH2A:mthP2BRYOIGMBAIVGOFUGUPVHG4VU", new String[]{"adcXI56W3OPAJCYDNL367FELXOZ7U", "mthU74LOQKORBCUJAPMCQLCI3M62E"});
        idsMap.put("adcZY6KPQ2FFZBPBIHXAJHHDYRGMI:mth74E2Q22SJZFNHCCYW4Y3XKJESA", new String[]{"adc577BIS4VOZF6LHBG5OV7JJ5SVY", "mthCKIIJQS3LBB6ZNOGGM25KC5HOY"});
        idsMap.put("adcZY6KPQ2FFZBPBIHXAJHHDYRGMI:mthLM4GLZOOX5EAFHTM6QRNU27RAY", new String[]{"adc577BIS4VOZF6LHBG5OV7JJ5SVY", "mthKG22PYUP5FCCNHMIGNJPEBDR7M"});
        idsMap.put("adcZGJT7DMN2BCOBGRK4YCNJ3YBMI:mthSLF3YACGBNESXNI7LJR532EVDI", new String[]{"adc577BIS4VOZF6LHBG5OV7JJ5SVY", "mthKG22PYUP5FCCNHMIGNJPEBDR7M"});

        idsMap.put("adcZGJT7DMN2BCOBGRK4YCNJ3YBMI:mthU2DXDI5XKZEKHPQ3D4QY7DHFT4", new String[]{"adc577BIS4VOZF6LHBG5OV7JJ5SVY", "mth6VN6K4GXE5HMJJ6YNTFQUI35FM"});

        idsMap.put("adc5H7P5FMQSNAEBGKNU3ESVQAEEA:mthNZW3DYMSYFHOXOGW66HNOFSYBM", new String[]{"aicU2MBT4JOMBE4BHSE7K3Q2VLSEU", "mthHNVSWEIEA5FABGLZ2CHU42TCPU"});
        idsMap.put("adcN4R32FFJGJBATPHMG5SHO2SUVI:mthSBGPCQTWRBEGJFS4FEZVL3GGKI", new String[]{"adc577BIS4VOZF6LHBG5OV7JJ5SVY", "mthKG22PYUP5FCCNHMIGNJPEBDR7M"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA.mthLYC5FVT27ZGPHGNIIZJBILXRM4", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthX6SZR26JYFD2LFEEKBH3R2UL5Y"});

        idsMap.put("adcKWGD253MUFA5RLWGKSXXEAWF5Q:mth57UIRN3DRZBFHOJ5FB3WEPDGTY", new String[]{"adcYPRT23KRRVAX7AN3727VBD5VI4", "mthZBTI63XF4RBXJJXOH54ZV3GT6I"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthJVJ54EALTNBKBPV4LVWY24O6QI", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthYJXQ7ZHE2FGB5D2NE6CSGFKRUI"});
        idsMap.put("axcVJ2GJCJE2BC6ZMPAVTRQ7GT5AI:mthLDGWFISBQBB3PADPX4VFQ6RFK4", new String[]{"axc6XYNZ22YSZEKDKWH2SWRGBTC6I", "mthAIARN4PFVBD6HJZSJNZIXYNYAY"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthLYC5FVT27ZGPHGNIIZJBILXRM4", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthX6SZR26JYFD2LFEEKBH3R2UL5Y"});
        idsMap.put("adcA47XSJRXLVBWTMDZBEL4EBYWTE:mthFZLJ4UIAORAMLKGVHBSQKDCTZA", new String[]{"adc4XUXHLLOJRC7ZGMB7CYL7MHD4A", "mth3B7TBENHGNCUBE7KBK75KHSC6Q"});
        idsMap.put("adc2BBVYOFJVBDTDPNKMVD7H7WLQA:mthPNTGXK5BHFDS5HTZCUNJ7WABHA", new String[]{"adc24D3IJUTHVH7HJZFW2PSATWURU", "mthZ2QY4DNIOFCUPEMIZT4AEJWQ3M"});
        idsMap.put("adc2XZZ4SGN5BH3VDXQ572ZFSCENI:mthX5TKEMWDTVFWHM2ZHNHSKVCIXQ", new String[]{"adcRWKREWEWMBEALFKQUBWVPQ3XXY", "mth77RLCTER3JBADAC6OBZCKCY6ZA"});

        idsMap.put("aecTHH2BU632LNRDCKSABIFNQAABA:eprXIURWV4K2PNRDCKTABIFNQAABA:aemXIURWV4K2PNRDCKTABIFNQAABA:mthN2LXT7HF45HXJOVQVKF23JLV4A", new String[]{"aecTHH2BU632LNRDCKSABIFNQAABA", "eprXIURWV4K2PNRDCKTABIFNQAABA", "aemXIURWV4K2PNRDCKTABIFNQAABA", "mth3HBB5FVQXRC4LP2ITXM6TXVDYI"});

        HashMap<Id, Id> ecmids = new HashMap<Id, Id>();
        //mthN2LXT7HF45HXJOVQVKF23JLV4A
        ecmids.put(Id.Factory.loadFrom("mthPCQZDOTRIZFVFD3JBYMAA4BGZQ"), Id.Factory.loadFrom("mthCLIGGMVKDVFJLJCSWDATVFA52U"));
        ecmids.put(Id.Factory.loadFrom("mthN2LXT7HF45HXJOVQVKF23JLV4A"), Id.Factory.loadFrom("mthQJLQGY6YVNGKTJDKPGGI2WNZGU"));
        ecmids.put(Id.Factory.loadFrom("mthB3XKNLDRTBCVDDKNOFUAEH6NDA"), Id.Factory.loadFrom("mthDIJBS4PRMZCI7BX2VH6YVINVOM"));
        ecmids.put(Id.Factory.loadFrom("mthMAUFR3O63FHBBDHGFFZD2KF3EY"), Id.Factory.loadFrom("mthCLIGGMVKDVFJLJCSWDATVFA52U"));
        ecmids.put(Id.Factory.loadFrom("mthJVJ54EALTNBKBPV4LVWY24O6QI"), Id.Factory.loadFrom("mthYJXQ7ZHE2FGB5D2NE6CSGFKRUI"));
        ecmids.put(Id.Factory.loadFrom("mthFZLJ4UIAORAMLKGVHBSQKDCTZA"), Id.Factory.loadFrom("mth3B7TBENHGNCUBE7KBK75KHSC6Q"));
        ecmids.put(Id.Factory.loadFrom("mthMI3LNYL2PBECTM5NAAYQJU5O64"), Id.Factory.loadFrom("mthZ5K4Z5QS4BAMFBGYAWR2CGCKYI"));

        //ecmids.put(Id.Factory.loadFrom("mthN2LXT7HF45HXJOVQVKF23JLV4A"), Id.Factory.loadFrom("mth3HBB5FVQXRC4LP2ITXM6TXVDYI"));
        //mthQJLQGY6YVNGKTJDKPGGI2WNZGU
        for (RadixObject obj : radixObjects) {
            if (obj instanceof JmlTagId) {
                JmlTagId tag = (JmlTagId) obj;
                AdsPath path = tag.getPath();

                Id[] ids = path.asArray();
                boolean first = true;
                StringBuilder match = new StringBuilder();
                for (Id id : ids) {
                    if (first) {
                        first = false;
                    } else {
                        match.append(':');
                    }
                    match.append(id.toString());
                }
                String[] repl = idsMap.get(match.toString());
                if (repl != null) {
                    Id[] newIds = new Id[repl.length];
                    for (int i = 0; i < repl.length; i++) {
                        newIds[i] = Id.Factory.loadFrom(repl[i]);
                    }
                    tag.setPath(new AdsPath(newIds));
                }
            } else if (obj instanceof AdsMethodDef) {
                AdsMethodDef m = (AdsMethodDef) obj;
                if (m.getOwnerClass().getClassDefType() == EClassType.ENTITY || m.getOwnerClass().getClassDefType() == EClassType.APPLICATION | m.getOwnerClass().getClassDefType() == EClassType.DYNAMIC || m.getOwnerClass().getClassDefType() == EClassType.GROUP_MODEL || m.getOwnerClass().getClassDefType() == EClassType.ENTITY_MODEL || m.getOwnerClass().getClassDefType() == EClassType.FILTER_MODEL) {
                    Id repl = ecmids.get(m.getId());
                    if (repl != null) {
                        m.supportForSelfCheckOnPublishing(repl);
                    }
                }
            }
        }
    }
    private static final char[] NEW = new char[]{'w', 'e', 'n'};

    private void collectJmls(final List<RadixObject> radixObjects, final StringBuilder sb) {
        File folder = new File("/home/akrylov/Desktop/jml");
        folder.mkdirs();
        int index = 1;
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsUserMethodDef) {
                try {
                    AdsUserMethodDef method = (AdsUserMethodDef) obj;
                    Jml jml = method.getSource();
                    if (jml.getItems().size() > 100) {
                        AdsDefinitionDocument xDoc = AdsDefinitionDocument.Factory.newInstance();
                        org.radixware.schemas.adsdef.ClassDefinition xClass = xDoc.addNewAdsDefinition().addNewAdsClassDefinition();
                        xClass.setName(method.getOwnerClass().getName());
                        xClass.setId(method.getOwnerClass().getId());
                        MethodDefinition methodDef = xClass.addNewMethods().addNewMethod();
                        method.appendTo(methodDef, AdsDefinition.ESaveMode.NORMAL);
                        XmlUtils.saveXmlPretty(xDoc, new File(folder, "jml_src" + String.valueOf(index) + ".properties"));
                        index++;
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private void convertDeclsToConstructors(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof JmlTagTypeDeclaration) {
                JmlTagTypeDeclaration decl = (JmlTagTypeDeclaration) obj;
                AdsType type = decl.getType().resolve(decl.getOwnerDefinition()).get();
                if (type instanceof AdsClassType) {
                    AdsClassDef clazz = ((AdsClassType) type).getSource();
                    if (clazz == null) {
                        continue;
                    }
                    Collection<AdsMethodDef> constructors = clazz.getConstructors();
                    if (!constructors.isEmpty()) {
                        int index = decl.getOwnerJml().getItems().indexOf(decl);
                        if (index > 0) {
                            Scml.Item prev = decl.getOwnerJml().getItems().get(index - 1);
                            if (prev instanceof Scml.Text) {
                                char[] content = ((Scml.Text) prev).getText().toCharArray();
                                boolean isNew = false;
                                int match = 0;
                                for (int i = content.length - 1; i >= 0; i--) {
                                    char c = content[i];
                                    if (c == ' ') {
                                        continue;
                                    }
                                    if (c == NEW[match]) {
                                        match++;
                                        if (match == 3) {
                                            isNew = true;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                                if (isNew) {
                                    Jml jml = decl.getOwnerJml();
                                    if (constructors.size() == 1) {
                                        jml.getItems().remove(index);
                                        jml.getItems().add(index, JmlTagInvocation.Factory.newInstance(constructors.iterator().next()));
                                        sb.append(jml.getOwnerDef()).append(": " + "Reference to class ").append(clazz.getQualifiedName()).append(" replaced with single constructor\n");
                                    } else {
                                        int argCount = computeArgumentCount(jml, index + 1);
                                        if (argCount >= 0) {
                                            List<AdsMethodDef> candidates = new ArrayList<AdsMethodDef>();
                                            for (AdsMethodDef m : constructors) {
                                                if (m.getProfile().getParametersList().size() == argCount) {
                                                    candidates.add(m);
                                                }
                                            }
                                            if (candidates.size() == 1) {
                                                jml.getItems().remove(index);
                                                jml.getItems().add(index, JmlTagInvocation.Factory.newInstance(candidates.get(0)));
                                                sb.append(jml.getOwnerDef()).append(": " + "Reference to class ").append(clazz.getQualifiedName()).append(" replaced with constructor with argument count ").append(argCount).append("\n");
                                            } else if (candidates.size() > 1) {
                                                sb.append(jml.getOwnerDef()).append(": " + "More than one constructor of ").append(clazz.getQualifiedName()).append("with argument count ").append(argCount).append(" found. Skip replacement\n");
                                            } else {
                                                sb.append(jml.getOwnerDef()).append(": " + "No constructor of ").append(clazz.getQualifiedName()).append("with argument count ").append(argCount).append(" found. Skip replacement\n");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private int computeArgumentCount(Jml jml, int startFrom) {
        int count = jml.getItems().size();
        int dept = 0;
        int paramCount = 0;
        boolean paramStart = false;
        boolean inStringLiteral = false;
        boolean inCharLiteral = false;

        for (int i = startFrom; i < count; i++) {
            Scml.Item item = jml.getItems().get(i);
            if (item instanceof Scml.Text) {
                String str = ((Scml.Text) item).getText();
                char prev = (char) -1;
                for (int c = 0; c < str.length(); c++) {
                    char s = str.charAt(c);
                    switch (s) {
                        case '(':
                            if (!(inStringLiteral || inCharLiteral)) {
                                dept++;
                                if (dept == 1) {
                                    paramStart = true;
                                }
                            }
                            break;
                        case ')':
                            if (!(inStringLiteral || inCharLiteral)) {
                                dept--;
                                if (dept < 0) {
                                    return -1;
                                } else if (dept == 0) {
                                    return paramCount;
                                }
                            }
                            break;
                        case ',':
                            if (!(inStringLiteral || inCharLiteral)) {
                                if (dept == 1) {
                                    paramStart = true;
                                }
                            }
                            break;
                        case '"':
                            if (inStringLiteral) {
                                if (prev != '\\') {
                                    inStringLiteral = false;
                                }
                            } else {
                                if (!inCharLiteral) {
                                    inStringLiteral = true;
                                }
                            }
                            if (dept == 1 && paramStart) {
                                paramCount++;
                                paramStart = false;
                            }
                            break;
                        case '\'':
                            if (inCharLiteral) {
                                if (prev != '\\') {
                                    inCharLiteral = false;
                                }
                            } else {
                                if (!inStringLiteral) {
                                    inCharLiteral = true;
                                }
                            }
                            if (dept == 1 && paramStart) {
                                paramCount++;
                                paramStart = false;
                            }
                            break;
                        default:
                            if (dept == 1 && paramStart) {
                                paramCount++;
                                paramStart = false;
                            }
                    }
                }
            } else {
                if (dept == 1 && paramStart) {
                    paramCount++;
                    paramStart = false;
                }
                continue;
            }
        }
        if (dept != 0) {
            return -1;
        }

        return paramCount;
    }

    private void fixAutoDbTypes(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject ro : radixObjects) {
            if (ro instanceof DdsColumnTemplateDef) {
                final DdsColumnTemplateDef col = (DdsColumnTemplateDef) ro;
                final EValType valType = col.getValType();
                final String dbType = col.getDbType().toUpperCase();
                if ((valType == EValType.STR || valType.isArrayType()) && dbType.equals("VARCHAR2(4000 CHAR)")) {
                    col.setDbType("VARCHAR2(4000 char)");
                    col.setLength(4000);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.STR && dbType.equals("VARCHAR2(50 CHAR)")) {
                    col.setDbType("VARCHAR2(50 char)");
                    col.setLength(50);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.DATE_TIME && dbType.equals("TIMESTAMP")) {
                    col.setDbType("TIMESTAMP(6)");
                    col.setLength(0);
                    col.setPrecision(6);
                    col.setAutoDbType(true);
                } else if (valType == EValType.DATE_TIME && dbType.equals("TIMESTAMP(6)")) {
                    col.setDbType(dbType);
                    col.setLength(0);
                    col.setPrecision(6);
                    col.setAutoDbType(true);
                } else if (valType == EValType.DATE_TIME && dbType.equals("DATE")) {
                    col.setDbType(dbType);
                    col.setLength(0);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType.isArrayType() && dbType.equals("CLOB")) {
                    col.setDbType(dbType);
                    col.setLength(0);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.BIN && dbType.equals("RAW(2000)")) {
                    col.setDbType(dbType);
                    col.setLength(2000);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.NUM && dbType.equals("NUMBER")) {
                    col.setDbType(dbType);
                    col.setLength(0);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.INT && dbType.equals("NUMBER(2,0)")) {
                    col.setDbType(dbType);
                    col.setLength(2);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.INT && dbType.equals("NUMBER(1,0)")) {
                    col.setDbType(dbType);
                    col.setLength(1);
                    col.setPrecision(0);
                    col.setAutoDbType(true);
                } else if (valType == EValType.BOOL || valType == EValType.CHAR) {
                    col.setLength(1);
                    col.setPrecision(1);
                } else if (valType == EValType.INT || valType == EValType.BIN || valType == EValType.STR || valType.isArrayType()) {
                    col.setPrecision(0);
                } else if (valType == EValType.BLOB || valType == EValType.CLOB || valType == EValType.NATIVE_DB_TYPE) {
                    col.setLength(0);
                    col.setPrecision(0);
                }
            }
        }
    }

    private void updateEnum(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsEnumDef) {
                AdsEnumDef enumDef = (AdsEnumDef) obj;
                AdsLocalizingBundleDef bundle = enumDef.findExistingLocalizingBundle();
                if (bundle != null) {
                    if (enumDef.getId().toString().equals("acsUOT6QZNAXLNRDISQAAAAAAAAAA")) {
                        for (AdsEnumItemDef item : enumDef.getItems().get(EScope.LOCAL)) {
                            Id titleId = item.getTitleId();
                            String itemValue = (String) item.getValue().toObject(EValType.STR);
                            AdsMultilingualStringDef string = bundle.getStrings().findById(titleId, EScope.LOCAL).get();
                            for (AdsMultilingualStringDef.StringStorage ss : string.getValues(EScope.LOCAL)) {
                                string.setValue(ss.getLanguage(), itemValue + " - " + ss.getValue());
                            }
                        }
                    }
                }
            }
        }
    }

    private void correctOldExplorerClassRefs(final List<RadixObject> radixObjects, final StringBuilder sb) {
        AdsClassDef staticApp = null;//AdsSearcher.Factory.newAdsClassSearcher(method).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ"));
        if (!radixObjects.isEmpty()) {
            Branch b = radixObjects.get(0).getBranch();
            if (b != null) {
                RadixObject res = b.find(new VisitorProvider() {
                    @Override
                    public boolean isTarget(RadixObject radixObject) {
                        return radixObject instanceof AdsClassDef && ((AdsClassDef) radixObject).getId() == Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ");
                    }
                });
                if (res instanceof AdsClassDef) {
                    staticApp = (AdsClassDef) res;
                }
            }
        }
        final Map<String, String> replaceMap = new HashMap<String, String>();
        replaceMap.put("org.radixware.kernel.explorer.models.EntityModel", "org.radixware.kernel.common.client.models.EntityModel");
        replaceMap.put("org.radixware.kernel.explorer.models.GroupModel", "org.radixware.kernel.common.client.models.GroupModel");
        replaceMap.put("org.radixware.kernel.explorer.models.Model", "org.radixware.kernel.common.client.models.Model");
        replaceMap.put("org.radixware.kernel.explorer.models.PropEditorModel", "org.radixware.kernel.common.client.models.PropEditorModel");
        replaceMap.put("org.radixware.kernel.explorer.widgets.IWidget", "org.radixware.kernel.common.client.widgets.IModelWidget");
        replaceMap.put("org.radixware.kernel.explorer.views.IView", "org.radixware.kernel.common.client.views.IView");
        replaceMap.put("org.radixware.kernel.explorer.tree.ExplorerItemView", "org.radixware.kernel.common.client.views.IExplorerItemView");
        replaceMap.put("org.radixware.kernel.explorer.models.items.properties.Property", "org.radixware.kernel.common.client.models.items.properties.Property");
        replaceMap.put("org.radixware.kernel.explorer.models.items.ModelItem", "org.radixware.kernel.common.client.models.items.ModelItem");
        replaceMap.put("org.radixware.kernel.explorer.models.items.SelectorColumnModelItem", "org.radixware.kernel.common.client.models.items.SelectorColumnModelItem");
        replaceMap.put("org.radixware.kernel.explorer.types.Pid", "org.radixware.kernel.common.client.types.Pid");
        replaceMap.put("org.radixware.kernel.explorer.types.Pid", "org.radixware.kernel.common.client.types.Pid");
        replaceMap.put("org.radixware.kernel.explorer.types.InstantiatableClass", "org.radixware.kernel.common.client.types.InstantiatableClass");
        replaceMap.put("org.radixware.kernel.explorer.types.GroupRestrictions", "org.radixware.kernel.common.client.types.GroupRestrictions");
        replaceMap.put("org.radixware.kernel.explorer.env.session.RequestHandle", "org.radixware.kernel.common.client.eas.RequestHandle");
        replaceMap.put("org.radixware.kernel.explorer.env.session.CommandRequestHandle", "org.radixware.kernel.common.client.eas.CommandRequestHandle");
        replaceMap.put("org.radixware.kernel.explorer.exceptions.ModelException", "org.radixware.kernel.common.client.exceptions.ModelException");
        replaceMap.put("org.radixware.kernel.explorer.exceptions.PropertyIsMandatoryException", "org.radixware.kernel.common.client.exceptions.PropertyIsMandatoryException");
        replaceMap.put("org.radixware.kernel.explorer.exceptions.InvalidPropertyValueException", "org.radixware.kernel.common.client.exceptions.InvalidPropertyValueException");
        replaceMap.put("org.radixware.kernel.explorer.models.items.properties.style.IPropertyStyle", "org.radixware.kernel.common.client.models.items.properties.style.IPropertyStyle");
        replaceMap.put("org.radixware.kernel.explorer.env.Environment", "org.radixware.kernel.explorer.env.Application");

        for (RadixObject obj : radixObjects) {
            if (!obj.isInBranch()) {
                continue;
            }
//            if (obj instanceof AdsMethodDef && ((AdsMethodDef) obj).getId() == Id.Factory.loadFrom("mthZ4ODIBIQHVH7VKXGC3X3DCWSSA")) {
//                ((AdsMethodDef) obj).setAccessMode(EAccess.PUBLIC);
//            } else

            if (obj instanceof AdsUIConnection) {
                AdsUIConnection c = (AdsUIConnection) obj;
                String signalStr = c.getSignal();

                boolean sm = false;
                if (signalStr != null) {
                    for (Map.Entry<String, String> e : replaceMap.entrySet()) {
                        String newString = signalStr.replace(e.getKey(), e.getValue());
                        if (newString != signalStr) {
                            sm = true;
                            signalStr = newString;
                        }
                    }

                }
                if (sm) {
                    sb.append("Signal modified\n");
                }
                c.setSignal(signalStr);
                AdsUISignalDef signal = c.getUISignal();
                if (signal != null) {
                    List<AdsTypeDeclaration> decls = signal.getTypes().list();
                    List<AdsTypeDeclaration> result = new ArrayList<AdsTypeDeclaration>();
                    for (AdsTypeDeclaration decl : decls) {
                        TypeDeclaration xDecl = TypeDeclaration.Factory.newInstance();
                        decl.appendTo(xDecl);
                        String asString = xDecl.xmlText();
                        boolean modified = false;
                        for (Map.Entry<String, String> e : replaceMap.entrySet()) {
                            String newString = asString.replace(e.getKey(), e.getValue());
                            if (newString != asString) {
                                modified = true;
                                asString = newString;
                            }
                        }
                        if (modified) {
                            try {
                                xDecl = TypeDeclaration.Factory.parse(asString);
                                decl = AdsTypeDeclaration.Factory.loadFrom(xDecl);

                                sb.append("Updated: ").append(obj.getQualifiedName()).append('\n');
                            } catch (XmlException ex) {
                                sb.append("********** ERROR *********\n");
                                sb.append(ex.getMessage());
                                sb.append("\n********** ***** *********\n");
                            }
                        }
                        result.add(decl);
                    }
                    signal.getTypes().clear();
                    for (AdsTypeDeclaration decl : result) {
                        signal.getTypes().add(decl);
                    }
                }
            }
            if (obj instanceof Value || obj instanceof ThrowsListItem) {
                AdsTypeDeclaration decl;
                if (obj instanceof Value) {
                    decl = ((Value) obj).getType();
                } else {
                    decl = ((ThrowsListItem) obj).getException();
                }
                if (decl != null && (decl.getTypeId() == EValType.JAVA_CLASS || decl.getTypeId() == EValType.JAVA_TYPE)) {
                    TypeDeclaration xDecl = TypeDeclaration.Factory.newInstance();
                    decl.appendTo(xDecl);
                    String asString = xDecl.xmlText();
                    boolean modified = false;
                    for (Map.Entry<String, String> e : replaceMap.entrySet()) {
                        String newString = asString.replace(e.getKey(), e.getValue());
                        if (newString != asString) {
                            modified = true;
                            asString = newString;
                        }
                    }
                    if (modified) {
                        try {
                            xDecl = TypeDeclaration.Factory.parse(asString);
                            decl = AdsTypeDeclaration.Factory.loadFrom(xDecl);
                            if (obj instanceof Value) {
                                ((Value) obj).setType(decl);
                            } else {
                                AdsMethodThrowsList tl = ((ThrowsListItem) obj).getOwnerMethod().getProfile().getThrowsList();

                                ThrowsListItem newItem = ThrowsListItem.Factory.newInstance(decl);
                                newItem.setDescription(((ThrowsListItem) obj).getDescription());
                                tl.remove((ThrowsListItem) obj);
                                tl.add(newItem);
                            }
                            sb.append("Updated: ").append(obj.getQualifiedName()).append('\n');
                        } catch (XmlException ex) {
                            sb.append("********** ERROR *********\n");
                            sb.append(ex.getMessage());
                            sb.append("\n********** ***** *********\n");
                        }

                    }
                }
            } else if (obj instanceof JmlTagInvocation) {
                //Referenced subdefinition of Radix::Explorer.Views::IView is not found #aicU2MBT4JOMBE4BHSE7K3Q2VLSEU.mthNPPMK5NF75FN7FYSELQUH3WQ7A
                JmlTagInvocation invoke1 = (JmlTagInvocation) obj;
                Jml jml = invoke1.getOwnerJml();
                if (jml == null) {
                    continue;
                }
                Id[] ids = invoke1.getPath().asArray();
                if (ids.length == 2) {
                    if (ids[0] == Id.Factory.loadFrom("aicU2MBT4JOMBE4BHSE7K3Q2VLSEU") && ids[1] == Id.Factory.loadFrom("mthNPPMK5NF75FN7FYSELQUH3WQ7A")) {
                        int index = invoke1.getOwnerJml().getItems().indexOf(invoke1);
                        if (index > 0) {
                            Scml.Item prev = invoke1.getOwnerJml().getItems().get(index - 1);
                            if (prev instanceof Scml.Text && prev.toString().trim().equals("().")) {
                                if (index > 1) {
                                    Scml.Item pprev = invoke1.getOwnerJml().getItems().get(index - 2);
                                    if (pprev instanceof JmlTagInvocation) {
                                        JmlTagInvocation invoke2 = (JmlTagInvocation) pprev;
                                        Id[] ids2 = invoke2.getPath().asArray();
                                        if (ids2.length == 2 && ids2[0] == Id.Factory.loadFrom("adc24D3IJUTHVH7HJZFW2PSATWURU") && ids2[1] == Id.Factory.loadFrom("mthTTUQQ3QNAVENTCMAW45HZ7Q4UE")) {
                                            jml.getItems().remove(index);
                                            ((Scml.Text) prev).setText("()).asQWidget");
                                            jml.getItems().add(index - 2, Scml.Text.Factory.newInstance("((org.radixware.kernel.explorer.views.IExplorerView)"));
                                        }
                                    }
                                }
                            }
                        }
                    } else if (ids[0] == Id.Factory.loadFrom("adcBQWVU3OGYFHPXEZH5ND56A3YBU") && ids[1] == Id.Factory.loadFrom("mthFWNU3ODV7ZFDJBZBNWRH4HVGMM")) {
                        int index = jml.getItems().indexOf(invoke1);
                        jml.getItems().add(index, Scml.Text.Factory.newInstance("acceptDialog"));
                        jml.getItems().remove(invoke1);
                    } else if (ids[0] == Id.Factory.loadFrom("adcBQWVU3OGYFHPXEZH5ND56A3YBU") && ids[1] == Id.Factory.loadFrom("mthMFAHW7D5NZAGPEZKTZBML4PQJA")) {
                        int index = jml.getItems().indexOf(invoke1);
                        jml.getItems().add(index, Scml.Text.Factory.newInstance("rejectDialog"));
                        jml.getItems().remove(invoke1);
                    } else if (ids[0] == Id.Factory.loadFrom("apcMWRMQFMQR5FSZEYESOHBVLF6CU") && ids[1] == Id.Factory.loadFrom("mthF2URRU4KWNDCNNWW6IB4SBLPLQ")) {//ExplorerUtils.getSaveXmlFileName()
                        int index = jml.getItems().indexOf(invoke1);
                        if (index + 1 < jml.getItems().size()) {
                            Scml.Item next = jml.getItems().get(index + 1);
                            if (next instanceof Scml.Text) {
                                String text = next.toString().trim().replace(" ", "").replace("\n", "");
                                if (text.startsWith("(") && !text.startsWith("((")) {
                                    int lpp = next.toString().indexOf("(");
                                    jml.getItems().add(index + 1, Scml.Text.Factory.newInstance("((com.trolltech.qt.gui.QWidget)"));
                                    jml.getItems().add(index + 2, Scml.Text.Factory.newInstance(next.toString().substring(lpp + 1)));
                                    jml.getItems().remove(next);
                                }
                            }
                        }
                    } else if ((ids[0] == Id.Factory.loadFrom("adc2BBVYOFJVBDTDPNKMVD7H7WLQA") && ids[1] == Id.Factory.loadFrom("mthZ2ZROAGPG5FDRBWDS5INBAJ7HE"))
                            || (ids[0] == Id.Factory.loadFrom("adc2BBVYOFJVBDTDPNKMVD7H7WLQA") && ids[1] == Id.Factory.loadFrom("mthATRXOJCPFZDKLL4QS37HGEVT3A"))
                            || (ids[0] == Id.Factory.loadFrom("adcZ3N2ZQF7BVGB7NZZIGQP5RUK5I") && ids[1] == Id.Factory.loadFrom("mth2WYIGFEZYRF37MLJQKURTWGEDQ"))
                            || (ids[0] == Id.Factory.loadFrom("adcDHI5YQVUWZFTXMXT2QKFPDDVRY") && ids[1] == Id.Factory.loadFrom("mthFFPTCDEJQNE5NDZHVT5UZCN5BA"))
                            || (ids[0] == Id.Factory.loadFrom("apcHAM7TPVGPBGGTGN2GIM45SJTC4") && ids[1] == Id.Factory.loadFrom("mthCWJWTR2KFBEM3OLHUBLBBALFQM"))
                            || (ids[0] == Id.Factory.loadFrom("apcHAM7TPVGPBGGTGN2GIM45SJTC4") && ids[1] == Id.Factory.loadFrom("mthOYZ7DN62PRGH5NSP6L3YTUP7PE"))
                            || (ids[0] == Id.Factory.loadFrom("adcXOOE64LGCZBBRE5I2ZRWXERCRM") && ids[1] == Id.Factory.loadFrom("mthRNVUIO6D3BCCDFYYP7QR45ABHQ"))) {

                        boolean addComma = ids[1] != Id.Factory.loadFrom("mthCWJWTR2KFBEM3OLHUBLBBALFQM") && ids[1] != Id.Factory.loadFrom("mthOYZ7DN62PRGH5NSP6L3YTUP7PE");
                        int index = jml.getItems().indexOf(invoke1);
                        if (index + 1 < jml.getItems().size()) {
                            Scml.Item next = jml.getItems().get(index + 1);
                            if (next instanceof Scml.Text) {
                                String text = next.toString();
                                if (text.trim().startsWith("(")) {
                                    int lparenPos = text.indexOf("(");
                                    String text2check = text.replace("\n", " ");
                                    text2check = text2check.replace(" ", "");
                                    if (text2check.startsWith("(Environment")) {
                                        continue;
                                    } else if (text2check.equals("(")) {

                                        if (jml.getItems().size() > index + 2) {
                                            Scml.Item next2 = jml.getItems().get(index + 2);
                                            if (next2 instanceof JmlTagTypeDeclaration) {
                                                AdsTypeDeclaration decl = ((JmlTagTypeDeclaration) next2).getType();
                                                if (decl.getPath() != null) {
                                                    ids = decl.getPath().asArray();
                                                    if (ids.length == 1 && ids[0] == Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")) {
                                                        continue;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    AdsDefinition jmlDef = jml.getOwnerDef();
                                    if (jmlDef instanceof AdsMethodDef && ((AdsMethodDef) jmlDef).getOwnerClass() instanceof AdsModelClassDef) {
                                        String insert = "(Environment";
                                        if (addComma) {
                                            insert += ",";
                                        }

                                        jml.getItems().add(index + 1, Scml.Text.Factory.newInstance(insert));
                                        if (text.length() > lparenPos + 1) {
                                            jml.getItems().add(index + 2, Scml.Text.Factory.newInstance(text.substring(lparenPos + 1)));
                                        }
                                        jml.getItems().remove(next);
                                        sb.append("openContextlessModel invocation change\n");
                                    } else {
                                        AdsClassDef env = AdsSearcher.Factory.newAdsClassSearcher(jml.getOwnerDef()).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")).get();
                                        if (env == null) {
                                            jml.getModule().getDependences().add(staticApp.getModule());
                                            env = AdsSearcher.Factory.newAdsClassSearcher(jml.getOwnerDef()).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")).get();
                                        }
                                        if (env != null) {
                                            jml.getItems().add(index + 1, Scml.Text.Factory.newInstance("("));
                                            jml.getItems().add(index + 2, new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(env)));
                                            String insert = ".Instance.Environment";
                                            if (addComma) {
                                                insert += ",";
                                            }
                                            jml.getItems().add(index + 3, Scml.Text.Factory.newInstance(insert));
                                            if (text.length() > lparenPos + 1) {
                                                jml.getItems().add(index + 4, Scml.Text.Factory.newInstance(text.substring(lparenPos + 1)));
                                            }
                                            jml.getItems().remove(next);
                                            sb.append("openContextlessModel invocation change\n");
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
                if (!invoke1.isInBranch()) {
                    continue;
                }
                Definition def = invoke1.resolve(jml.getOwnerDef());
                if (def instanceof AdsMethodDef) {
                    AdsMethodDef method = (AdsMethodDef) def;
                    if (method.isConstructor()) {
                        AdsClassDef createdClass = method.getOwnerClass();
                        if (staticApp != null) {
                            if (createdClass.getId() == Id.Factory.loadFrom("adcAYRYJOCRLBGQ7KGSLD7TBOKI4U")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcYWRWEDDK4ZEBXIKA3NCLFBXRVI")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcZOESDNDCUJD47OKTZ5J2R4BZDE")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcVEPIYKNGVZDBZGETSNUYSHAUMU")
                                    || createdClass.getId() == Id.Factory.loadFrom("adc3T4OO4IRSBBVFBEUYJMJ7V4TMU")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcN4R32FFJGJBATPHMG5SHO2SUVI")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcZY6KPQ2FFZBPBIHXAJHHDYRGMI")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcZGJT7DMN2BCOBGRK4YCNJ3YBMI")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcYAXYNEWENVDHHBKDWTNAPW3ZTY")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcA6YJ4O5BRBCNBKXXLMKZE7IZZU")
                                    || createdClass.getId() == Id.Factory.loadFrom("adcFSI3GFO2XVCRJEEMXETCN54GMM")) {
                                boolean addComma = createdClass.getId() != Id.Factory.loadFrom("adcA6YJ4O5BRBCNBKXXLMKZE7IZZU") && createdClass.getId() != Id.Factory.loadFrom("adcFSI3GFO2XVCRJEEMXETCN54GMM");
                                int index = jml.getItems().indexOf(invoke1);
                                if (index + 1 < jml.getItems().size()) {
                                    Scml.Item next = jml.getItems().get(index + 1);
                                    if (next instanceof Scml.Text) {
                                        String text = next.toString();
                                        if (text.trim().startsWith("(")) {
                                            String text2check = text.replace("\n", " ");
                                            text2check = text2check.replace(" ", "");
                                            if (text2check.startsWith("(Environment")) {
                                                continue;
                                            } else if (text2check.equals("(")) {
                                                if (jml.getItems().size() > index + 2) {
                                                    Scml.Item next2 = jml.getItems().get(index + 2);
                                                    if (next2 instanceof JmlTagTypeDeclaration) {
                                                        AdsTypeDeclaration decl = ((JmlTagTypeDeclaration) next2).getType();
                                                        if (decl.getPath() != null) {
                                                            ids = decl.getPath().asArray();
                                                            if (ids.length == 1 && ids[0] == Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")) {
                                                                continue;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            int lparen = text.indexOf("(");
                                            AdsDefinition jmlDef = jml.getOwnerDef();
                                            if (jmlDef instanceof AdsMethodDef && ((AdsMethodDef) jmlDef).getOwnerClass() instanceof AdsModelClassDef) {
                                                String ttr = "(Environment";
                                                if (addComma) {
                                                    ttr += ",";
                                                }
                                                jml.getItems().add(index + 1, Scml.Text.Factory.newInstance(ttr));
                                                if (text.length() > lparen + 1) {
                                                    jml.getItems().add(index + 2, Scml.Text.Factory.newInstance(text.substring(lparen + 1)));
                                                }
                                                jml.getItems().remove(next);
                                                sb.append("ValEditor constructor invocation change\n");
                                            } else {
                                                AdsClassDef env = AdsSearcher.Factory.newAdsClassSearcher(method).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")).get();
                                                if (env == null) {
                                                    method.getModule().getDependences().add(staticApp.getModule());
                                                    env = AdsSearcher.Factory.newAdsClassSearcher(method).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")).get();
                                                }
                                                if (env != null) {
                                                    jml.getItems().add(index + 1, Scml.Text.Factory.newInstance("("));
                                                    jml.getItems().add(index + 2, new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(env)));
                                                    String ttr = ".Instance.Environment";
                                                    if (addComma) {
                                                        ttr += ",";
                                                    }
                                                    jml.getItems().add(index + 3, Scml.Text.Factory.newInstance(ttr));

                                                    if (text.length() > lparen + 1) {
                                                        jml.getItems().add(index + 4, Scml.Text.Factory.newInstance(text.substring(lparen + 1)));
                                                    }
                                                    jml.getItems().remove(next);
                                                    sb.append("ValEditor constructor invocation change\n");
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (obj instanceof JmlTagTypeDeclaration) {
                Jml jml = ((JmlTagTypeDeclaration) obj).getOwnerJml();
                if (jml == null) {
                    continue;
                }
                JmlTagTypeDeclaration tag = (JmlTagTypeDeclaration) obj;
                AdsTypeDeclaration type = ((JmlTagTypeDeclaration) obj).getType();
                AdsType resolvedType = type.resolve(obj.getOwnerDefinition()).get();
                if (resolvedType instanceof AdsClassType) {
                    AdsClassDef clazz = ((AdsClassType) resolvedType).getSource();

                    if (clazz != null && clazz.getId() == Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")) {
                        int index = jml.getItems().indexOf(tag);
                        if (index + 1 < jml.getItems().size()) {
                            Scml.Item next = jml.getItems().get(index + 1);
                            if (next instanceof Scml.Text) {
                                String text = next.toString();
                                if (text.startsWith(".processException(e);") || text.startsWith(".processException(ex);") || text.startsWith(".processException(er);") || text.startsWith(".processException( e );")) {
                                    Scml.Text newText = Scml.Text.Factory.newInstance(".Instance.Environment" + text);
                                    jml.getItems().add(index + 1, newText);
                                    jml.getItems().remove(next);
                                    sb.append("\n===============================================================\n");
                                    sb.append(text);
                                    sb.append("\n------------------------------------------------------------\n");
                                    sb.append(newText.toString());
                                    sb.append("\n===============================================================\n");
                                } else if (text.startsWith(".clipboard.")) {
                                    Scml.Text newText = Scml.Text.Factory.newInstance(".Instance.Environment.C" + text.substring(2));
                                    jml.getItems().add(index + 1, newText);
                                    jml.getItems().remove(next);
                                    sb.append("\n===============================================================\n");
                                    sb.append(text);
                                    sb.append("\n------------------------------------------------------------\n");
                                    sb.append(newText.toString());
                                    sb.append("\n===============================================================\n");
                                } else if (text.startsWith(".tracer.")) {
                                    Scml.Text newText = Scml.Text.Factory.newInstance(".Instance.Environment.T" + text.substring(2));
                                    jml.getItems().add(index + 1, newText);
                                    jml.getItems().remove(next);
                                    sb.append("\n===============================================================\n");
                                    sb.append(text);
                                    sb.append("\n------------------------------------------------------------\n");
                                    sb.append(newText.toString());
                                    sb.append("\n===============================================================\n");
                                } else if (text.startsWith(".session.")) {
                                    Scml.Text newText = Scml.Text.Factory.newInstance(".Instance.Environment.EasS" + text.substring(2));
                                    jml.getItems().add(index + 1, newText);
                                    jml.getItems().remove(next);
                                    sb.append("\n===============================================================\n");
                                    sb.append(text);
                                    sb.append("\n------------------------------------------------------------\n");
                                    sb.append(newText.toString());
                                    sb.append("\n===============================================================\n");
                                } else if (text.startsWith(".defManager.")) {
                                    Scml.Text newText = Scml.Text.Factory.newInstance(".Instance.Environment.D" + text.substring(2));
                                    jml.getItems().add(index + 1, newText);
                                    jml.getItems().remove(next);
                                    sb.append("\n===============================================================\n");
                                    sb.append(text);
                                    sb.append("\n------------------------------------------------------------\n");
                                    sb.append(newText.toString());
                                    sb.append("\n===============================================================\n");
                                }
                            }
                        }
                    }
                } else if (resolvedType instanceof AdsDefinitionType) {
                    Definition def = ((AdsDefinitionType) resolvedType).getSource();
                    if (def instanceof AdsCustomDialogDef) {
                        int index = jml.getItems().indexOf(tag);
                        if (index > 0) {
                            Scml.Item prev = jml.getItems().get(index - 1);
                            if (prev instanceof Scml.Text) {
                                String text = prev.toString();
                                if (text.trim().endsWith("new")) {
                                    if (index + 1 < jml.getItems().size()) {
                                        Scml.Item next = jml.getItems().get(index + 1);
                                        if (next instanceof Scml.Text) {
                                            text = next.toString();
                                            if (text.trim().startsWith("()")) {
                                                int lparenPos = text.indexOf("(");
                                                AdsDefinition jmlDef = jml.getOwnerDef();
                                                if (jmlDef instanceof AdsMethodDef && ((AdsMethodDef) jmlDef).getOwnerClass() instanceof AdsModelClassDef) {
                                                    jml.getItems().add(index + 1, Scml.Text.Factory.newInstance("(Environment"));
                                                    if (text.length() > lparenPos + 1) {
                                                        jml.getItems().add(index + 2, Scml.Text.Factory.newInstance(text.substring(lparenPos + 1)));
                                                    }
                                                    jml.getItems().remove(next);
                                                    sb.append("Custom dialog creation invocation change\n");
                                                } else {
                                                    AdsClassDef env = AdsSearcher.Factory.newAdsClassSearcher(jml.getOwnerDef()).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")).get();
                                                    if (env == null) {
                                                        jml.getModule().getDependences().add(staticApp.getModule());
                                                        env = AdsSearcher.Factory.newAdsClassSearcher(jml.getOwnerDef()).findById(Id.Factory.loadFrom("adcGPNNISYF4NG5NC4NX5ITZORVYQ")).get();
                                                    }
                                                    if (env != null) {
                                                        jml.getItems().add(index + 1, Scml.Text.Factory.newInstance("("));
                                                        jml.getItems().add(index + 2, new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(env)));
                                                        jml.getItems().add(index + 3, Scml.Text.Factory.newInstance(".Instance.Environment"));
                                                        if (text.length() > lparenPos + 1) {
                                                            jml.getItems().add(index + 4, Scml.Text.Factory.newInstance(text.substring(lparenPos + 1)));
                                                        }
                                                        jml.getItems().remove(next);
                                                        sb.append("Custom dialog creation invocation change\n");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (obj instanceof Scml.Text) {

                String orig = ((Scml.Text) obj).toString();
                String text = orig;
                //String result = null;
                if (text.startsWith(".id")) {

                    if (text.length() == 3) {
                        text = ".Id";
                    } else if (!Character.isJavaIdentifierPart(text.charAt(3))) {
                        text = ".Id" + text.substring(3);
                    }

                }

                if (text.contains("getGroupView().setUpdatesEnabled(")) {
                    text = text.replace("this.getGroupView()", "getGroupView()");
                    text = text.replace("getGroupView().setUpdatesEnabled(", "((com.trolltech.qt.gui.QWidget)getGroupView()).setUpdatesEnabled(");
                }
                if (text.contains("getGroupView().blockRedraw(")) {
                    text = text.replace("this.getGroupView()", "getGroupView()");
                    text = text.replace("getGroupView().blockRedraw(", "((org.radixware.kernel.common.client.widgets.IBlocakbleWidget)getGroupView()).blockRedraw(");
                }
                if (text.contains("getGroupView().unblockRedraw(")) {
                    text = text.replace("this.getGroupView()", "getGroupView()");
                    text = text.replace("getGroupView().unblockRedraw(", "((org.radixware.kernel.common.client.widgets.IBlocakbleWidget)getGroupView()).unblockRedraw(");
                }
                if (text.contains("getGroupView().addAction(")) {
                    text = text.replace("this.getGroupView()", "getGroupView()");
                    text = text.replace("getGroupView().addAction(", "((com.trolltech.qt.gui.QWidget)getGroupView()).addAction(");
                }
                if (text.contains("new QBrush(settings.background)")) {
                    text = text.replace("new QBrush(settings.background)", "new QBrush(new com.trolltech.qt.gui.QColor(settings.background))");
                }
                if (text.contains("new QBrush(settings.foreground)")) {
                    text = text.replace("new QBrush(settings.foreground)", "new QBrush(new com.trolltech.qt.gui.QColor(settings.foreground))");
                }
                if (text.contains(".getSaveFileName(") && !text.contains(".getSaveFileName((")) {
                    text = text.replace(".getSaveFileName(", ".getSaveFileName((com.trolltech.qt.gui.QWidget)");
                }
                if (text.contains(".getOpenFileName(") && !text.contains(".getOpenFileName((")) {
                    text = text.replace(".getOpenFileName(", ".getOpenFileName((com.trolltech.qt.gui.QWidget)");
                }
                if (text.contains(".reject();")) {
                    text = text.replace(".reject();", ".rejectDialog();");
                }
                if (text.contains(".styleSheet")) {
                    text = text.replace(".styleSheet", ".Stylesheet");
                }

                if (text.contains("getGroupView().actions.")) {
                    int index = text.indexOf("getGroupView().actions.");
                    while (index >= 0) {
                        String add = "";
                        if (index >= ".insertAction(".length() && text.substring(index - ".insertAction(".length(), index).equals(".insertAction(")) {
                            add = "(QAction)";
                        } else if (index >= ".insertWidget(".length() && text.substring(index - ".insertWidget(".length(), index).equals(".insertWidget(")) {
                            add = "(QAction)";
                        }

                        text = text.substring(0, index) + add + "getGroupView().Actions." + Character.toUpperCase(text.charAt(index + "getGroupView().actions.".length())) + text.substring(index + "getGroupView().actions.".length() + 1);
                        index = text.indexOf("getGroupView().actions.");
                    }
                } else if (text.contains(".actions.")) {
                    int index = text.indexOf(".actions.");
                    while (index >= 0) {

                        text = text.substring(0, index) + ".Actions." + Character.toUpperCase(text.charAt(index + ".actions.".length())) + text.substring(index + ".actions.".length() + 1);
                        index = text.indexOf("getGroupView().actions.");
                    }
                }
                if (text.contains(".accept();")) {
                    text = text.replace(".accept();", ".acceptDialog();");
                }

                if (text.contains(".id")) {
                    int index = text.indexOf(".id");
                    while (index >= 0) {
                        if (index + 3 < text.length()) {
                            char c = text.charAt(index + 3);
                            if (!Character.isJavaIdentifierPart(c)) {
                                text = text.substring(0, index) + ".Id" + text.substring(index + 3);
                            }
                        } else {
                            break;
                        }
                        index = text.indexOf(".id", index + 1);
                    }
                }
                if (text.contains(".owner")) {
                    int index = text.indexOf(".owner");
                    while (index >= 0) {
                        if (index + 6 < text.length()) {
                            char c = text.charAt(index + 6);
                            if (!Character.isJavaIdentifierPart(c)) {
                                text = text.substring(0, index) + ".Owner" + text.substring(index + 6);
                            }
                        } else {
                            break;
                        }
                        index = text.indexOf(".owner", index + 1);
                    }
                }
                if (text.contains(".def")) {
                    int index = text.indexOf(".def");
                    while (index >= 0) {
                        if (index + 4 < text.length()) {
                            char c = text.charAt(index + 4);
                            if (!Character.isJavaIdentifierPart(c)) {
                                text = text.substring(0, index) + ".Definition" + text.substring(index + 4);
                            }
                        } else {
                            break;
                        }
                        index = text.indexOf(".def", index + 1);
                    }
                }
                if (!text.equals(orig)) {
                    Scml jml = ((Scml.Text) obj).getOwnerScml();
                    int index = jml.getItems().indexOf((Scml.Text) obj);
                    if (index >= 0) {
                        jml.getItems().add(index, Scml.Text.Factory.newInstance(text));
                        jml.getItems().remove((Scml.Text) obj);
                    }
                    sb.append("\n===============================================================\n");
                    sb.append(orig);
                    sb.append("\n------------------------------------------------------------\n");
                    sb.append(text);
                    sb.append("\n===============================================================\n");
                }
            }
        }
    }

    private void secondPassForOlExplorerClassRefs(final List<RadixObject> radixObjects, final StringBuilder sb) {
        //.onSetCurrentEntity.connect(this,
        AdsClassDef iSelector = (AdsClassDef) radixObjects.get(0).getBranch().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsClassDef && ((AdsClassDef) radixObject).getId() == Id.Factory.loadFrom("aic4XUXMXRWTREMHAT5S7SM55XC6A");
            }
        });

        AdsClassDef eModel = (AdsClassDef) radixObjects.get(0).getBranch().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsClassDef && ((AdsClassDef) radixObject).getId() == Id.Factory.loadFrom("adc2BBVYOFJVBDTDPNKMVD7H7WLQA");
            }
        });
        for (RadixObject obj : radixObjects) {
            if (obj instanceof Jml) {
                AdsMethodDef onSetCurrentEntity = null;
                AdsMethodDef onLeaveCurrentEntity = null;

                Scml scml = (Jml) obj;

                Scml.Item text1 = null;
                Scml.Item text2 = null;
                Scml.Item text3 = null;
                Scml.Item text4 = null;
                String match = null;
                for (int i = 0; i < scml.getItems().size(); i++) {
                    Scml.Item item = scml.getItems().get(i);
                    if (item instanceof Scml.Text) {
                        String text = item.toString();
                        if (text.contains(".onSetCurrentEntity.connect(this")) {
                            if (match == null) {
                                match = ".onSetCurrentEntity.connect(this";
                            }
                            text1 = item;
                            for (int j = i + 1; j < scml.getItems().size(); j++) {
                                item = scml.getItems().get(j);
                                if (item instanceof JmlTagId) {
                                    JmlTagId idTag = ((JmlTagId) item);
                                    Definition def = idTag.resolve((AdsDefinition) scml.getOwnerDefinition());
                                    if (def instanceof AdsMethodDef) {
                                        onSetCurrentEntity = (AdsMethodDef) def;
                                    }
                                } else if (item instanceof Scml.Text && onSetCurrentEntity != null) {
                                    if (item.toString().contains(");")) {
                                        text2 = item;
                                        break;
                                    }
                                }
                            }
                        } else if (text.contains(".onLeaveCurrentEntity.connect(this")) {
                            text3 = item;
                            if (match == null) {
                                match = ".onLeaveCurrentEntity.connect(this";
                            }
                            for (int j = i + 1; j < scml.getItems().size(); j++) {
                                item = scml.getItems().get(j);
                                if (item instanceof JmlTagId) {
                                    JmlTagId idTag = ((JmlTagId) item);
                                    Definition def = idTag.resolve((AdsDefinition) scml.getOwnerDefinition());
                                    if (def instanceof AdsMethodDef) {
                                        onLeaveCurrentEntity = (AdsMethodDef) def;
                                    }
                                } else if (item instanceof Scml.Text && onLeaveCurrentEntity != null) {
                                    if (item.toString().contains(");")) {
                                        text4 = item;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                //1 remove tags;

                if (text1 != null || text3 != null) {
                    int insertTo;
                    int index;
                    Scml.Item start;
                    if (text1 != null) {
                        insertTo = scml.getItems().indexOf(text1);
                        index = text1.toString().indexOf(".onSetCurrentEntity.c");
                        start = text1;
                    } else {
                        insertTo = scml.getItems().indexOf(text3);
                        index = text3.toString().indexOf(".onLeaveCurrentEntity.c");
                        start = text3;
                    }

                    List<Scml.Item> items = new LinkedList<Scml.Item>();
                    if (index > 0) {
                        items.add(Scml.Text.Factory.newInstance(start.toString().substring(0, index)
                                + ".addCurrentEntityHandler( new "));
                    } else {
                        items.add(Scml.Text.Factory.newInstance(".addCurrentEntityHandler( new "));
                    }
                    if (iSelector != null) {
                        items.add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(iSelector)));
                    } else {
                        items.add(Scml.Text.Factory.newInstance("org.radixware.kernel.common.client.views.ISelector"));
                    }

                    items.add(Scml.Text.Factory.newInstance(".CurrentEntityHandler(){\n    @Override\n    public void onSetCurrentEntity("));

                    if (eModel != null) {
                        items.add(new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(eModel)));
                    } else {
                        items.add(Scml.Text.Factory.newInstance("org.radixware.kernel.common.client.models.EntityModel"));
                    }
                    items.add(Scml.Text.Factory.newInstance(" entity){\n        "));
                    if (onSetCurrentEntity != null) {
                        items.add(JmlTagInvocation.Factory.newInstance(onSetCurrentEntity));
                        items.add(Scml.Text.Factory.newInstance("();\n    }\n"));
                    } else {
                        items.add(Scml.Text.Factory.newInstance("//Do nothing\n"));
                        items.add(Scml.Text.Factory.newInstance("}\n"));
                    }

                    items.add(Scml.Text.Factory.newInstance("\n    @Override\n    public void onLeaveCurrentEntity(){\n        "));

                    if (onLeaveCurrentEntity != null) {
                        items.add(JmlTagInvocation.Factory.newInstance(onLeaveCurrentEntity));
                        items.add(Scml.Text.Factory.newInstance("();\n    }\n"));
                    } else {
                        items.add(Scml.Text.Factory.newInstance("//Do nothing\n"));
                        items.add(Scml.Text.Factory.newInstance("    }\n"));
                    }
                    items.add(Scml.Text.Factory.newInstance("\n    });\n"));

                    Scml.Item last = text4 == null ? text2 : text4;
                    index = last.toString().indexOf(");");
                    if (index + 2 < last.toString().length()) {
                        items.add(Scml.Text.Factory.newInstance(last.toString().substring(index + 2)));
                    }

                    for (int i = 0; i < items.size(); i++) {
                        scml.getItems().add(insertTo + i, items.get(i));
                    }
                    int indexToRemove = scml.getItems().indexOf(start);

                    while (true) {
                        Scml.Item item = scml.getItems().get(indexToRemove);
                        scml.getItems().remove(item);
                        if (item == last) {
                            break;
                        }
                    }
                    if (iSelector != null) {
                        scml.getModule().getDependences().add(iSelector.getModule());
                    }

                }
            }
        }
    }

    private AdsMethodDef findMethod(RadixObject obj) {
        while (obj != null) {
            if (obj instanceof AdsMethodDef) {
                return (AdsMethodDef) obj;
            }
            obj = obj.getContainer();
        }
        return null;
    }

    private void thirdPassForOlExplorerClassRefs(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof Scml.Text) {
                if (obj.toString().endsWith("ic = dm.getImage(")) {
                    AdsMethodDef method = findMethod(obj);
                    if (method != null && method.getId() == Id.Factory.loadFrom("mthWSXAGZUIPBFT7H4SA5CGSWMBB4")) {
                        Scml scml = ((Scml.Text) obj).getOwnerScml();
                        int index = scml.getItems().indexOf((Scml.Text) obj);
                        if (index + 2 < scml.getItems().size()) {
                            int start = obj.toString().indexOf("dm.getImage(");
                            scml.getItems().add(index, Scml.Text.Factory.newInstance(obj.toString().substring(0, start)));
                            scml.getItems().add(index + 2, Scml.Text.Factory.newInstance("dm.getImage()"));
                        }
                    }
                }
            }
        }
    }

    private void updateCustomEditorPageIds(final List<RadixObject> radixObjects, final StringBuilder sb) {
        class EditorPageInfo {

            Id oldId;
            Id newId;
            AdsCustomPageEditorDef view;

            public EditorPageInfo(AdsCustomPageEditorDef view) {
                this.view = view;
                this.oldId = view.getId();
                this.newId = Id.Factory.newInstance(EDefinitionIdPrefix.CUSTOM_EDITOR_PAGE);
            }
        }
        final Map<Id, EditorPageInfo> map = new HashMap<Id, EditorPageInfo>();
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsCustomPageEditorDef) {
                EditorPageInfo info = new EditorPageInfo((AdsCustomPageEditorDef) obj);
                map.put(info.oldId, info);
            }
        }

        for (RadixObject obj : radixObjects) {
            if (obj instanceof JmlTagId) {
                JmlTagId id = (JmlTagId) obj;
                Definition def = id.resolve(id.getOwnerDefinition());
                AdsCustomPageEditorDef inView = null;
                while (def != null) {
                    if (def instanceof AdsCustomPageEditorDef) {
                        inView = (AdsCustomPageEditorDef) def;
                        break;
                    }
                    def = def.getOwnerDefinition();
                }
                if (inView != null) {
                    EditorPageInfo info = map.get(inView.getId());
                    if (info.view == inView) {
                        Id[] ids = id.getPath().asArray();
                        for (int i = 0; i < ids.length; i++) {
                            if (ids[i] == info.oldId) {
                                ids[i] = info.newId;
                                break;
                            }
                        }
                        id.setPath(new AdsPath(ids));
                        sb.append("Reference updage: id tag in ").
                                append(id.getOwnerJml().getQualifiedName()).
                                append(" usage of ").
                                append(inView.getQualifiedName()).
                                append(", id replacement: ").
                                append(info.oldId).append(" ->").
                                append(info.newId).append("\n");
                    } else {
                        sb.append("!!!Editor page mismatch: found").
                                append(inView.getQualifiedName()).
                                append(" in cache ").
                                append(info.view.getQualifiedName()).
                                append("\n");
                    }
                }
            }
        }
        for (EditorPageInfo info : map.values()) {
            info.view.udpateIdDueToPageInheritanceBugFix(info.newId);
            sb.append("View updage: ").
                    append(info.view.getQualifiedName()).
                    append(", id replacement: ").
                    append(info.oldId).append(" ->").
                    append(info.newId).append("\n");
        }
    }

    private void getBackUfClassCatalogs(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Id UF_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");
        final Id UFC_ID = Id.Factory.loadFrom("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM");
        final Id ALL_ID = Id.Factory.loadFrom("eccZMCDFRDL4DOBDIEAAALOMT5GDM");
        AdsClassCatalogDef srcClassCatalog = (AdsClassCatalogDef) radixObjects.get(0).getBranch().find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return radixObject instanceof AdsClassCatalogDef && ((AdsClassCatalogDef) radixObject).getOwnerClass().getId() == UFC_ID;
            }
        });
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsApplicationClassDef && ((AdsApplicationClassDef) obj).getEntityId() == UF_ID) {
                AdsApplicationClassDef clazz = (AdsApplicationClassDef) obj;
                if (!clazz.getAccessFlags().isAbstract()) {
                    AdsClassCatalogDef cc = clazz.getPresentations().getClassCatalogs().findById(UF_ID, EScope.LOCAL).get();
                    if (cc == null) {
                        cc = (AdsClassCatalogDef) clazz.getPresentations().getClassCatalogs().override(srcClassCatalog);
                        if (cc instanceof AdsClassCatalogDef.Virtual) {
                            ((AdsClassCatalogDef.Virtual) cc).defineClassReference();
                        }
                        sb.append(cc.getQualifiedName()).append("\n");
                    }
                }
            }
        }
    }

    private void lookupUfProps(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Id UF_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");
        int nullCount = 0;
        int totalCount = 0;
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsUIConnection) {
                AdsUIConnection c = (AdsUIConnection) obj;
                if (c.getSlot() == null) {
                }
            } else if (obj instanceof AdsUserPropertyDef) {
                AdsUserPropertyDef prop = ((AdsUserPropertyDef) obj);
                AdsTypeDeclaration decl = prop.getValue().getType();
                if (decl != null && decl.getTypeId() == EValType.OBJECT) {
                    AdsType type = decl.resolve(prop).get();
                    if (type instanceof AdsDefinitionType && ((AdsDefinitionType) type).getSource() instanceof AdsApplicationClassDef) {
                        AdsApplicationClassDef clazz = (AdsApplicationClassDef) ((AdsDefinitionType) type).getSource();
                        if (clazz.getEntityId() == UF_ID) {
                            ObjectPropertyPresentation pps = (ObjectPropertyPresentation) prop.getPresentationSupport().getPresentation();
                            if (pps.getCreationClassCatalogId() == null) {
                                nullCount++;
                            } else {
                                AdsClassCatalogDef def = pps.findCreationClassCatalog().get();
                                if (def != null) {
                                    sb.append(def.getQualifiedName()).append("\n");
                                }
                                pps.setCreationClassCatalogId(null);
                            }
                            totalCount++;
                        }
                    }
                }
            }
        }
        sb.append(nullCount).append("\n").append(totalCount);
    }

    private void updateInitVals(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) obj;
                if (prop.getValue().getInitial() != null) {
                    if (prop.getValue().getInitial().getValueType() == EValueType.VAL_AS_STR) {
                        ValAsStr val = prop.getValue().getInitial().getValAsStr();
                        String stringReprentation = val.toString();
                        if ("NULL".equalsIgnoreCase(stringReprentation)) {
                            prop.getValue().setInitial(AdsValAsStr.NULL_VALUE);
                            sb.append("Updated: ").append(prop.getQualifiedName()).append("\n");
                        }
                    }
                }
            }
        }
    }

    private void updateModelMethods(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject object : radixObjects) {
            if (object instanceof AdsMethodDef || object instanceof AdsPropertyDef) {
                AdsClassDef clazz = ((AdsClassMember) object).getOwnerClass();
                if (clazz instanceof AdsModelClassDef) {
                    if (object instanceof AdsPropertyDef) {
                        ((AdsPropertyDef) object).setUsageEnvironment(ERuntimeEnvironmentType.EXPLORER);
                    } else if (object instanceof AdsMethodDef) {
                        ((AdsMethodDef) object).setUsageEnvironment(ERuntimeEnvironmentType.EXPLORER);
                    }
                }
            } else if (object instanceof AdsModelClassDef) {
                AdsModelClassDef clazz = (AdsModelClassDef) object;
                clazz.getHeader().ensureFirst().setUsageEnvironment(ERuntimeEnvironmentType.EXPLORER);
                clazz.getBody().ensureFirst().setUsageEnvironment(ERuntimeEnvironmentType.EXPLORER);
            }
        }
    }
    private final Id TEST_CASE_ID = Id.Factory.loadFrom("tblHW4OSVMS27NRDISQAAAAAAAAAA");
    private final Id USER_FUNC_ID = Id.Factory.loadFrom("tblJ6SOXKD3ZHOBDCMTAALOMT5GDM");

    private void updateEnvironment1_2_13(final List<RadixObject> radixObjects, final StringBuilder sb) {
        List<AdsEditorPresentationDef> presentations = new LinkedList<AdsEditorPresentationDef>();
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsEntityObjectClassDef) {
                AdsEntityObjectClassDef clazz = (AdsEntityObjectClassDef) obj;
                if (clazz.getEntityId() == TEST_CASE_ID) {
                    clazz.setClientEnvironment(ERuntimeEnvironmentType.EXPLORER);
                    sb.append("Test case update:  ").append(clazz.getQualifiedName()).append("\n");
                } else if (clazz.getEntityId() == USER_FUNC_ID) {
                    clazz.setClientEnvironment(ERuntimeEnvironmentType.EXPLORER);
                    sb.append("User func update:  ").append(clazz.getQualifiedName()).append("\n");
                }
            } else if (obj instanceof AdsEditorPresentationDef) {
                presentations.add((AdsEditorPresentationDef) obj);
            }
        }

        int iteration = 1;
        int updateCount = 0;
        do {
            updateCount = 0;
            sb.append("------------------ ITERATION #").append(iteration).append("---------------------------------------------------\n");
            for (AdsEditorPresentationDef epr : presentations) {
                if (epr.getClientEnvironment() == ERuntimeEnvironmentType.COMMON_CLIENT) {
                    AdsEditorPresentationDef base = epr.findBaseEditorPresentation().get();
                    if (base != null && base.getClientEnvironment() != ERuntimeEnvironmentType.COMMON_CLIENT) {
                        epr.setClientEnvironment(base.getClientEnvironment());
                        sb.append("Editor presentation updated: ").append(epr.getQualifiedName()).append("\n");
                        updateCount++;
                    }
                }
            }
            iteration++;
        } while (updateCount > 0);
    }

    private void changeStandardButton2DialogButtonType(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof MethodParameter) {
                MethodParameter p = (MethodParameter) obj;
                AdsTypeDeclaration typeDecl = p.getType();
                if (typeDecl != null && typeDecl.getTypeId() == EValType.JAVA_CLASS) {
                    String className = typeDecl.getExtStr();
                    if ("org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton".equals(className)) {
                        AdsTypeDeclaration newDecl = AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.enums.EDialogButtonType");
                        p.setType(newDecl);
                        sb.append(p.getOwnerMethod().getQualifiedName()).append("\n");
                    }
                }
            } else if (obj instanceof AdsUIConnection) {
                AdsUIConnection c = (AdsUIConnection) obj;
                String signal = c.getSignal();

                if (signal != null && signal.contains("org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton")) {
                    while (signal.contains("org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton")) {
                        signal = signal.replace("org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton", "org.radixware.kernel.common.enums.EDialogButtonType");
                    }
                    c.setSignal(signal);
                    sb.append(c.getQualifiedName()).append("\n");
                }
            } else if (obj instanceof AdsReportFormattedCell) {
                AdsReportFormattedCell cell = (AdsReportFormattedCell) obj;
                if (cell.getPattern() != null && !cell.getPattern().isEmpty()) {
                    sb.append(cell.getQualifiedName()).append("\n");
                    sb.append(cell.getPattern()).append(" --> ");
                    switch (cell.getCellType()) {
                        case PROPERTY:
                            AdsReportPropertyCell propCell = (AdsReportPropertyCell) cell;
                            AdsPropertyDef prop = propCell.findProperty();
                            if (prop != null && prop.getValue() != null) {
                                EValType propType = prop.getValue().getType().getTypeId();
                                if (propType == EValType.DATE_TIME) {
                                    setDateTimeFormat(propCell, sb);
                                } else if (propType == EValType.NUM) {
                                    setNumFormat(propCell, sb);
                                } else {
                                    propCell.getFormat().setPattern(null);
                                    sb.append("null").append("\n");
                                }
                            }
                            break;
                        case SUMMARY:
                            AdsReportSummaryCell summaryCell = (AdsReportSummaryCell) cell;
                            prop = summaryCell.findProperty();
                            if (prop != null && prop.getValue() != null) {
                                EValType propType = prop.getValue().getType().getTypeId();
                                if (propType == EValType.NUM) {
                                    setNumFormat(summaryCell, sb);
                                } else {
                                    summaryCell.getFormat().setPattern(null);
                                    sb.append("null").append("\n");
                                }
                            }
                            break;
                        case SPECIAL:
                            AdsReportSpecialCell specialCell = (AdsReportSpecialCell) cell;
                            EReportSpecialCellType specialCellType = specialCell.getSpecialType();
                            if (specialCellType == EReportSpecialCellType.GENERATION_TIME) {
                                setDateTimeFormat(specialCell, sb);
                            } else {
                                specialCell.getFormat().setPattern(null);
                                sb.append("null").append("\n");
                            }
                            break;
                    }

                }
            }
        }
    }

    private void setNumFormat(AdsReportFormattedCell cell, final StringBuilder sb) {
        String pattern = cell.getPattern();
        if (pattern.startsWith("{") && pattern.endsWith("}")) {
            String[] arrStr = pattern.split(",");
            int len = arrStr.length - 1;
            String newPattern = arrStr[len].substring(0, arrStr[len].length() - 1);
            if (newPattern.contains(".")) {
                int index = newPattern.indexOf(".");
                int precission = newPattern.length() - index - 1;
                sb.append("precission = ").append(precission).append("\n");
                cell.getFormat().setPrecission(precission);
            } else {
                sb.append("precission = 0").append("\n");
                cell.getFormat().setPrecission(0);
            }
            if (arrStr.length > 3) {
                cell.getFormat().setTriadDelimeterType(ETriadDelimeterType.SPECIFIED);
                cell.getFormat().setTriadDelimeter(",");
                sb.append("TriadDelimeter = ").append(",").append("\n");
            }
            cell.getFormat().setUseDefaultFormat(false);
            cell.getFormat().setPattern(null);
        }
    }

    private void setDateTimeFormat(AdsReportFormattedCell cell, final StringBuilder sb) {
        String pattern = cell.getPattern();
        pattern.split(",");
        if (pattern.startsWith("{") && pattern.endsWith("}")) {
            String[] arrStr = pattern.split(",");
            int len = arrStr.length - 1;
            String newPattern = arrStr[len].substring(0, arrStr[len].length() - 1);
            if (newPattern.trim().equals("short") && len >= 1) {
                cell.getFormat().setPattern(null);
                String style = arrStr[1].trim();
                if (style.equals("time")) {
                    cell.getFormat().setTimeStyle(EDateTimeStyle.SHORT);
                    sb.append(style).append(EDateTimeStyle.SHORT).append("\n");
                } else if (style.equals("date")) {
                    cell.getFormat().setDateStyle(EDateTimeStyle.SHORT);
                    sb.append(style).append(EDateTimeStyle.SHORT).append("\n");
                }
            } else {
                cell.getFormat().setPattern(newPattern.trim());
                cell.getFormat().setDateStyle(EDateTimeStyle.CUSTOM);
                cell.getFormat().setTimeStyle(EDateTimeStyle.CUSTOM);
                sb.append(newPattern).append("\n");
            }
            cell.getFormat().setUseDefaultFormat(false);

        }
    }

    class CheckManager {

        private JPanel panel;
        private JList<AbstractFixer> fixersList;

        public CheckManager(AbstractFixer... fixers) {
            this.panel = new JPanel(new BorderLayout());

            fixersList = new JList<>(fixers);
            fixersList.addListSelectionListener(new ListSelectionListener() {
                private JPanel currPanel;

                @Override
                public void valueChanged(ListSelectionEvent e) {
                    final AbstractFixer fixer = fixersList.getSelectedValue();
                    if (fixer != null) {
                        if (currPanel != null) {
                            CheckManager.this.panel.remove(currPanel);
                        }
                        final JPanel panel = fixer.getPanel();
                        if (panel != null) {
                            CheckManager.this.panel.add(panel, BorderLayout.LINE_END);
                        }
                        currPanel = panel;
                    }
                    if (currPanel != null) {
                        currPanel.revalidate();
                        currPanel.repaint();
                    } else {
                        panel.revalidate();
                        panel.repaint();
                    }
                }
            });
            panel.add(new JScrollPane(fixersList), BorderLayout.CENTER);
        }

        public void show(List<RadixObject> radixObjects, StringBuilder sb) {
            final ModalDisplayer displayer = new ModalDisplayer(panel);
            displayer.setTitle("Select check action");

            if (displayer.showModal()) {
                try {
                    fixersList.getSelectedValue().fix(radixObjects, sb);
                } catch (Exception e) {
                    Logger.getLogger(CheckManager.class.getName()).log(Level.WARNING, "", e);
                }
            }
        }
    }

    private static abstract class AbstractFixer {

        public abstract void fix(List<RadixObject> radixObjects, StringBuilder sb);

        public String getName() {
            return "<fixer>";
        }

        @Override
        public String toString() {
            return getName();
        }

        public JPanel getPanel() {
            return null;
        }
    }

    private static class WebViewFinder extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject radixObject : radixObjects) {
                if (radixObject instanceof AdsAbstractUIDef) {
                    final AdsAbstractUIDef cls = (AdsAbstractUIDef) radixObject;

                    final AdsDefinition entity = cls.findTopLevelDef();

                    if (cls.getUsageEnvironment() == ERuntimeEnvironmentType.WEB) {
                        sb.append("WebView: ").append(cls.getQualifiedName())
                                .append(" : ").append(entity != null ? entity.getQualifiedName() : "").append("\n");
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "WebViewFinder";
        }
    }

    private static class ExtendablePublishedFinder extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject radixObject : radixObjects) {
                if (radixObject instanceof ITransparency && AdsTransparence.isTransparent((ITransparency) radixObject, true)) {
                    sb.append(radixObject.getQualifiedName()).append("\n");
                }
            }
        }

        @Override
        public String getName() {
            return "ExtendablePublishedFinder";
        }
    }

    private static class InnerClassFinder extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject radixObject : radixObjects) {
                if (radixObject instanceof AdsClassDef) {
                    final AdsClassDef cls = (AdsClassDef) radixObject;

                    if (cls.isNested()) {
                        sb.append("Inner Class: ").append(cls.getQualifiedName()).append("\n");
                    }
                }

                if (radixObject instanceof AdsModule) {
                    AdsModule module = (AdsModule) radixObject;

                    if (!module.getServicesCatalog().isEmpty()) {
                        sb.append("Module has services: ").append(module.getQualifiedName()).append("\n");
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "InnerClassFinder";
        }
    }

    private static class SyncPublishedMethodsFixer extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (final RadixObject radixObject : radixObjects) {
                if (radixObject instanceof AdsClassDef) {
                    final AdsClassDef cls = (AdsClassDef) radixObject;

                    if (AdsTransparence.isTransparent(cls)) {
                        final RadixPlatformClass platformClass = findClass(cls, cls.getTransparence().getPublishedName());

                        for (final AdsMethodDef method : cls.getMethods().get(EScope.LOCAL)) {
                            if (AdsTransparence.isTransparent(method)) {
                                final Method publishMethod = findMethod(platformClass, method);

                                if (publishMethod != null) {

                                    if (publishMethod.isFinal() != method.getAccessFlags().isFinal()) {
                                        method.getAccessFlags().setFinal(publishMethod.isFinal());

                                        sb.append("Set final = ").append(publishMethod.isFinal()).append(" for ").append(method.getQualifiedName()).append("\n");
                                    }

                                    if (publishMethod.isAbstract() != method.getAccessFlags().isAbstract()) {
                                        method.getAccessFlags().setAbstract(publishMethod.isAbstract());

                                        sb.append("Set abstract = ").append(publishMethod.isFinal()).append(" for ").append(method.getQualifiedName()).append("\n");
                                    }

                                    if (publishMethod.isStatic() != method.getAccessFlags().isStatic()) {
                                        method.getAccessFlags().setStatic(publishMethod.isStatic());

                                        sb.append("Set static = ").append(publishMethod.isFinal()).append(" for ").append(method.getQualifiedName()).append("\n");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private Method findMethod(RadixPlatformClass platformClass, AdsMethodDef method) {
            Method publishMethod = null;
            while (platformClass != null) {
                publishMethod = platformClass.findMethodByProfile(method);
                if (publishMethod != null) {
                    break;
                }
                final AdsTypeDeclaration decl = platformClass.getSuperclass();
                if (decl != null) {
                    platformClass = findClass(method, decl.getExtStr());
                } else {
                    break;
                }
            }

            return publishMethod;
        }

        private RadixPlatformClass findClass(AdsDefinition cls, String name) {
            return ((AdsSegment) cls.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(cls.getUsageEnvironment()).findPlatformClass(name);
        }
    }

    private static class FindAutoDbObject extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject radixObject : radixObjects) {
                if (radixObject instanceof DdsTriggerDef) {
                    DdsTriggerDef trigger = (DdsTriggerDef) radixObject;
                    if (trigger.getType() == DdsTriggerDef.EType.NONE) {
                        sb.append(trigger.getQualifiedName());
                        sb.append(" -> ");
                        sb.append(trigger.getDbName());
                        sb.append('\n');
                    }
                }
            }
        }
    }

    private static class CLassCatalogDuplicationFixer extends AbstractFixer {

        private static class TopicItem {

            final AdsClassCatalogDef.Topic source;
            boolean difName = false;

            public TopicItem(AdsClassCatalogDef.Topic source) {
                this.source = source;
            }

            @Override
            public int hashCode() {
                int hash = 7;

                hash = 23 * hash + Objects.hashCode(source.getId());
                hash = 23 * hash + Objects.hashCode(source.getTitle(EIsoLanguage.ENGLISH));
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final TopicItem other = (TopicItem) obj;
                if (!Objects.equals(this.source, other.source)) {
                    if (!Objects.equals(this.source.getId(), other.source.getId())) {
                        return false;
                    }
                    if (Objects.equals(this.source.getTitle(EIsoLanguage.ENGLISH), other.source.getTitle(EIsoLanguage.ENGLISH))) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }

        private static class TopicFinder {

            final AdsClassCatalogDef cc;
            Set<Id> allCollectedTopicIds = new HashSet<>();
            Set<TopicItem> topics = new HashSet<>();
            List<TopicItem> badTopics = new ArrayList<>();
            private Inheritance.ClassHierarchySupport hSupport = new Inheritance.ClassHierarchySupport();

            public TopicFinder(AdsClassCatalogDef cc) {
                this.cc = cc;
            }

            void find() {
                final AdsEntityObjectClassDef superClassRef = findUpperSuperClass(cc.getOwnerClass());
                collectSubTopicsAndRefs(superClassRef);
            }

            private AdsEntityObjectClassDef findUpperSuperClass(AdsEntityObjectClassDef result) {
                AdsClassDef superClassRef = result.getInheritance().findSuperClass().get();
                while (superClassRef != null
                        && superClassRef instanceof AdsEntityObjectClassDef) {
                    return findUpperSuperClass((AdsEntityObjectClassDef) superClassRef);
                }
                return result;
            }

            private void collectSubTopicsAndRefs(AdsEntityObjectClassDef ownerClass) {
                final AdsClassCatalogDef classCatalog = ownerClass.getPresentations().getClassCatalogs().findById(cc.getId(), EScope.LOCAL).get();
                if (classCatalog != null) {
                    for (AdsClassCatalogDef.Topic topic : classCatalog.getTopicList()) {
                        TopicItem topicItem = new TopicItem(topic);
                        if (topics.contains(topicItem)) {
                            badTopics.add(topicItem);
                        } else {
                            if (allCollectedTopicIds.contains(topic.getId())) {
                                badTopics.add(topicItem);
                                topicItem.difName = true;
                            } else {
                                allCollectedTopicIds.add(topic.getId());
                                topics.add(topicItem);
                            }
                        }
                    }
                }
                final Collection<AdsClassDef> subClasses = hSupport.findDirectSubclasses(ownerClass);
                for (AdsClassDef subClass : subClasses) {
                    assert (subClass instanceof AdsEntityObjectClassDef);
                    collectSubTopicsAndRefs((AdsEntityObjectClassDef) subClass);
                }
            }
        }
        private JPanel panel = new JPanel();
        private JCheckBox fix;

        public CLassCatalogDuplicationFixer() {
            fix = new JCheckBox("Fix");
            panel.add(fix);
        }

        @Override
        public void fix(final List<RadixObject> radixObjects, final StringBuilder sb) {
            final Object lock = new Object();
            final ExecutorService executor = Executors.newSingleThreadExecutor();

            executor.execute(new Runnable() {
                @Override
                public void run() {
                    final Set<AdsClassCatalogDef> used = new HashSet<>();
                    final List<TopicFinder> finders = new ArrayList<>();

                    for (RadixObject radixObject : radixObjects) {

                        if (radixObject instanceof AdsClassCatalogDef) {
                            final AdsClassCatalogDef cc = (AdsClassCatalogDef) radixObject;

//                            if (used.contains(cc) || !cc.getHierarchy().findOverridden().isEmpty() || !cc.getHierarchy().findOverwritten().isEmpty()) {
//                                continue;
//                            }
                            if (!"TestCase".equals(cc.getOwnerClass().getName())) {
                                continue;
                            }
                            if (!"Testing".equals(cc.getModule().getName())) {
                                continue;
                            }
                            if (!"org.radixware".equals(cc.getLayer().getURI())) {
                                continue;
                            }

//                            used.add(cc);
                            TopicFinder finder = new TopicFinder(cc);
                            finder.find();

                            if (!finder.badTopics.isEmpty()) {
                                sb.append("Found ").append(finder.badTopics.size()).append(" invalid topics in ").append(cc.getQualifiedName()).append("\n");
                                sb.append("================================================\n");
                                for (final TopicItem topicItem : finder.badTopics) {
                                    writeTopicItem(topicItem, sb);
                                }

                                finders.add(finder);
                            }
                        }
                    }

                    if (fix.isSelected()) {
                        resolve(finders, sb);
                    }

                    synchronized (lock) {
                        lock.notifyAll();
                    }
                }
            });

            synchronized (lock) {
                try {
                    System.out.println("Wait fix task...");
                    lock.wait();
                    System.out.println("Fix task complete");
                } catch (InterruptedException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        private void writeTopicItem(TopicItem topicItem, StringBuilder sb) {
            sb.append(topicItem.source.getOwnerClass().getQualifiedName()).append(" : ");
            sb.append(topicItem.source.getOwnerClassCatalog().getName());
            sb.append(" {");
            sb.append(topicItem.source.getTitle(EIsoLanguage.ENGLISH));
            sb.append(" : ");
            sb.append(topicItem.source.getId());
            sb.append("}");

            if (topicItem.difName) {
                sb.append("\t\t match id only");
            }
            sb.append("\n");
        }

        private void resolve(List<TopicFinder> finders, final StringBuilder sb) {
            for (TopicFinder finder : finders) {
                sb.append("\nFix topics ").append("\n");
                sb.append("================================================\n");

                for (TopicItem topicItem : finder.badTopics) {
                    final AdsClassCatalogDef.Topic topic = topicItem.source;
                    final SearchResult<AdsClassCatalogDef> overridden = topic.getOwnerClassCatalog().getHierarchy().findOverridden();
                    if (!overridden.isEmpty()) {
                        final AdsClassCatalogDef overcc = overridden.get();
                        final AdsClassCatalogDef.Topic baseTopic = overcc.getTopics().findById(topic.getId());
                        if (baseTopic != null) {
                            sb.append("Delete... ");
                            writeTopicItem(topicItem, sb);
                            topic.delete();
                        } else if ("org.radixware".equals(overcc.getLayer().getURI())) {
//                                sb.append("Skip... ");
//                                writeTopicItem(topicItem, sb);
//                                continue;
                            final AdsEntityObjectClassDef ownerClass = topic.getOwnerClass();
                            if ("Tx::Testing::TestCase.Tx".equals(ownerClass.getQualifiedName())) {
                                for (final TopicItem tp : finder.topics) {
                                    if (tp.equals(topicItem)) {

                                        if ("ValeCard::Acquiring.Lyra.Pos.Testing::TestCase.LyraPos".equals(tp.source.getOwnerClass().getQualifiedName())) {
                                            sb.append("Delete... ");
                                            writeTopicItem(tp, sb);
                                            tp.source.delete();
                                        }
                                        break;
                                    }
                                }
                            } else if ("ValeCard::Acquiring.Lyra.Pos.Testing::TestCase.LyraPos".equals(ownerClass.getQualifiedName())) {
                                for (final TopicItem tp : finder.topics) {
                                    if (tp.equals(topicItem)) {

                                        if ("Tx::Testing::TestCase.Tx".equals(tp.source.getOwnerClass().getQualifiedName())) {
                                            sb.append("Delete... ");
                                            writeTopicItem(topicItem, sb);
                                            topic.delete();
                                        }
                                        break;
                                    }
                                }
                            }
                        } else {
                            sb.append("Copy to ").append(overcc.getQualifiedName()).append(" ");
                            writeTopicItem(topicItem, sb);

                            final String enTitle = topic.getTitle(EIsoLanguage.ENGLISH);
                            final String ruTitle = topic.getTitle(EIsoLanguage.RUSSIAN);
                            final AdsClassCatalogDef.Topic copy = topic.getClipboardSupport().copy();
                            overcc.getTopics().add(copy);

                            topic.delete();

                            copy.setTitle(EIsoLanguage.ENGLISH, enTitle);
                            copy.setTitle(EIsoLanguage.RUSSIAN, ruTitle);
                        }
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "CLass catalog duplication fixer";
        }

        @Override
        public JPanel getPanel() {
            return panel;
        }
    }

    private static class CheckDefinitionDescription extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {

            int russian = 0, english = 0;

            for (final RadixObject radixObject : radixObjects) {
                if (radixObject instanceof AdsDefinition) {
                    final AdsDefinition definition = (AdsDefinition) radixObject;

                    final String oldDescription = definition.getDescription();
                    if (definition.getDescriptionId() == null
                            && oldDescription != null && !oldDescription.isEmpty()) {

                        final EIsoLanguage language = getLanguage(oldDescription);
                        sb.append(definition.getQualifiedName()).append("( ").append(language.getName()).append(" ): '").append(definition.getDescription()).append("'\n");
                        definition.setDescription(language, oldDescription);

                        if (language == EIsoLanguage.RUSSIAN) {
                            ++russian;
                        } else {
                            ++english;
                        }
                    }
                }
            }

            sb.append("Total russians: ").append(russian).append(" \nTotal english: ").append(english);
        }

        EIsoLanguage getLanguage(String description) {
            for (char c : description.toCharArray()) {
                if (isRusChar(c)) {
                    return EIsoLanguage.RUSSIAN;
                }
            }

            return EIsoLanguage.ENGLISH;
        }

        boolean isRusChar(char ch) {
            return 'а' <= ch && ch <= 'я' || 'А' <= ch && ch <= 'Я';
        }

        @Override
        public String getName() {
            return "CheckDefinitionDescription";
        }
    }

    private static class OldStyleSignalFinder extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject obj : radixObjects) {
                if (obj instanceof AdsUIConnection) {
                    AdsUIConnection c = (AdsUIConnection) obj;
                    if (c.getSignal() != null && c.getSignal().startsWith("rowSelectionChanged(org.radixware.wps.rwt.Grid.Row")
                            && "addSelectionListener".equals(c.getRegistrator())) {
                        c.setRegistrator("addCurrentRowListener");
                        c.setSignal("currentRowChanged(org.radixware.wps.rwt.Grid.Row oldSelectedRow ,org.radixware.wps.rwt.Grid.Row newSelectedRow)");
                        sb.append(c.getQualifiedName()).append("\n");
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "Old Style Signal Fixer"; //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class VarargsFinder extends AbstractFixer {

        private boolean repair;

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject radixObject : radixObjects) {
                if (radixObject instanceof AdsMethodDef) {
                    final AdsMethodDef method = (AdsMethodDef) radixObject;

                    final AdsMethodDef.Profile profile = method.getProfile();

                    for (final MethodParameter parameter : profile.getParametersList()) {
                        if (parameter.isVariable()) {
                            if (!parameter.getType().isArray()) {

                                if (repair) {
                                    parameter.setType(parameter.getType().toArrayType(1));
                                    sb.append("Repair --> ");
                                } else {
                                    sb.append("Error --> ");
                                }
                            }
                            sb.append(method.getQualifiedName()).append(" { ")
                                    .append(parameter.getType().getQualifiedName(radixObject)).append(" : ").append(parameter.getName()).append(" }\n");
                        }
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "VarargsFinder";
        }

        @Override
        public JPanel getPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            final JCheckBox checkBox = new JCheckBox("Fix");
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    repair = checkBox.isSelected();
                }
            });
            panel.add(checkBox, BorderLayout.PAGE_START);

            return panel;
        }
    }

    private static class DefinitionFinder extends AbstractFixer {

        private boolean repair;

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject radixObject : radixObjects) {
                if (radixObject != null && radixObject.getClass().getSimpleName().endsWith(txt.getText())) {
                    sb.append(radixObject.getQualifiedName()).append(System.lineSeparator());
                }
            }
        }

        @Override
        public String getName() {
            return "DefinitionFinder";
        }
        final JTextField txt = new JTextField();

        @Override
        public JPanel getPanel() {
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            constraints.gridx = 0;
            constraints.gridy = 0;

            panel.add(new JLabel("Definition type name"), constraints);

            txt.setMinimumSize(new Dimension(200, 0));
            txt.setPreferredSize(new Dimension(200, 24));

            constraints = new GridBagConstraints();

            constraints.anchor = GridBagConstraints.LINE_START;
            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.weightx = 1;

            panel.add(txt, constraints);

            constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.FIRST_LINE_START;
            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.weighty = 1;

            panel.add(new JLabel(), constraints);

            return panel;
        }
    }

    private static class Scriptgenerator extends AbstractFixer {

        private boolean hasOwnModifiedModules(DdsSegment segment) {
            for (DdsModule module : segment.getModules()) {

                final DdsModelDef modifiedModel;
                try {
                    modifiedModel = module.getModelManager().getModifiedModel();
                    if (modifiedModel != null && modifiedModel.getModifierInfo().isOwn()) {
                        return true;
                    }
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
            return false;
        }

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            final DdsSegment segment = (DdsSegment) radixObjects.get(0).getLayer().getDds();

//            if (hasOwnModifiedModules(segment)) {
            final ScriptGenerator scriptGenerator = ScriptGenerator.Factory.newCreationInstance(segment);
            final CodePrinter cp = CodePrinter.Factory.newSqlPrinter();
            scriptGenerator.generateModificationScript(cp);
            final String installScript = cp.toString();
            sb.append(installScript);
//            } else {
//                sb.append("Empty");
//            }
        }

        @Override
        public String getName() {
            return "Scriptgenerator";
        }
    }

    private void fixMethodName(final List<RadixObject> radixObjects, final StringBuilder sb, final String publishedMethodId, final String publishedMethodOldName) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsMethodDef) {
                System.out.println(obj.getName() + obj.getQualifiedName());
                if (publishedMethodOldName.equals(obj.getName())) {
                    AdsMethodDef method = ((AdsMethodDef) obj);
                    while (method != null) {
                        AdsMethodDef ovr = method.getHierarchy().findOverwritten().get();
                        if (ovr == null) {
                            ovr = ((AdsMethodDef) obj).getHierarchy().findOverridden().get();
                        }
                        if (ovr != null && ovr.getId().toString().equals(publishedMethodId)) {
                            sb.append("renaming method " + obj.getQualifiedName() + " \n");
                            obj.setName(ovr.getName());
                            break;
                        } else {
                            method = ovr;
                        }
                    }
                }
            }
        }
    }

    private static class DdsRefAutoDbNameUpdater extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            for (RadixObject obj : radixObjects) {
                if (obj instanceof DdsReferenceDef) {
                    DdsReferenceDef ref = (DdsReferenceDef) obj;
                    if (!ref.getOwnerModel().isReadOnly() && ref.isAutoDbName()) {
                        String oldDbName = ref.getDbName();
                        ref.setDbName(ref.calcAutoDbName());
                        sb.append(ref.getQualifiedName()).append(": ").append(oldDbName).append(" => ").append(ref.getDbName()).append("\n");
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "Update reference DB names"; //To change body of generated methods, choose Tools | Templates.
        }
    }

    private static class UseAsUserPropertyObjectFinder extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {

            for (final RadixObject radixObject : radixObjects) {
                if (radixObject instanceof DdsTableDef) {
                    final DdsTableDef table = (DdsTableDef) radixObject;

                    if (table.getExtOptions().contains(EDdsTableExtOption.USE_AS_USER_PROPERTIES_OBJECT)) {
                        sb.append(table.getQualifiedName()).append(System.lineSeparator());
                    }
                }
            }
        }

        @Override
        public String getName() {
            return "UseAsUserPropertyObjectFinder";
        }
    }

    private void process(final List<RadixObject> radixObjects, final StringBuilder sb) {
        //collectImages(radixObjects, sb);
        //findFunctionParameterObject(radixObjects, sb);
        //findOracleHintTagUsages(radixObjects, sb);
        //findNonNullEditMask(radixObjects, sb);
        //synchronizeAllSqlClasses(radixObjects, sb);
        //synchronizeAllAlgoClasses(radixObjects, sb);
        //renameAllImages(radixObjects, sb);
        //collectWithErrorNames(radixObjects, sb);
        //formatXmlFiles(radixObjects, sb);
        //resaveAll(radixObjects, sb);
        //updateTitleIds(radixObjects, sb);
        //updateRefMaps(radixObjects, sb);
        //restoreDependencies(radixObjects, sb);
        //testLoggers(radixObjects, sb);
        //checkTags(radixObjects, sb);
        //radixObjects.get(0).visit(new Collector(), VisitorProviderFactory.createDefaultVisitorProvider());
        //required for distribution
        //
        //checkVisit(sb);
        //new RBSFix().exec(radixObjects, sb);
        //restorePropGroups(radixObjects, sb);
        //checkJavaClasses(radixObjects, sb);
        // measureCollectDependenciesSpeed(radixObjects, sb);
        //getClassCatalogsContainers(radixObjects, sb);
        //findPropWithAlias(radixObjects, sb);
        //openAllEditors(radixObjects, sb);
        //findScmlItems(radixObjects, sb);
        //resetCursors(radixObjects, sb);
        //openMethods(radixObjects, sb);
        // resetAccessMode(radixObjects, sb);
        //updatePubs(radixObjects, sb);
        //updatePps2(radixObjects, sb);
        //removePropPresentationProps(radixObjects, sb);
        //updateEIS(radixObjects, sb);
        //updatePubMethodRefs(radixObjects, sb);
        //fixTx(radixObjects, sb);
        //convertDeclsToConstructors(radixObjects, sb);
        //collectJmls(radixObjects, sb);
        //findContextlessEnabledSps(radixObjects, sb);
        //updateEnum(radixObjects, sb);
        //   correctOldExplorerClassRefs(radixObjects, sb);
        //updateCustomEditorPageIds(radixObjects, sb);
        //getBackUfClassCatalogs(radixObjects, sb);
        //lookupUfProps(radixObjects, sb);
        //  updateInitVals(radixObjects, sb);
//        updateModelMethods(radixObjects, sb);
        //addBatchMethodsToStatements(radixObjects, sb);
        // updateEnvironment1_2_13(radixObjects, sb);
//        changeStandardButton2DialogButtonType(radixObjects, sb);
//        fixMethodName(radixObjects, sb, "mthQJLQGY6YVNGKTJDKPGGI2WNZGU", "afterOpenView");

        final CheckManager manager = new CheckManager(
                new InnerClassFinder(),
                new CheckDefinitionDescription(),
                new WebViewFinder(),
                new ExtendablePublishedFinder(),
                new CLassCatalogDuplicationFixer(),
                new VarargsFinder(),
                new DefinitionFinder(),
                new OldStyleSignalFinder(),
                new DdsRefAutoDbNameUpdater(),
                new Scriptgenerator(),
                new UseAsUserPropertyObjectFinder(),
                new CursorLeaksFinder()
        );

        manager.show(radixObjects, sb);

//        new CheckDefinitionDescription(radixObjects, sb).fix();
//        new FindAutoDbObject(radixObjects, sb).fix();
//        new SyncPublishedMethodsFixer(radixObjects, sb).fix();
//        new InnerClassFinder(radixObjects, sb).fix();
    }

    private void scan(RadixObject top) {
        final Collector collector = new Collector();
        top.visit(collector, VisitorProviderFactory.createDefaultVisitorProvider());
        final StringBuilder sb = new StringBuilder();
        process(collector.radixObjects, sb);
        collector.radixObjects.clear();
        //    top.visit(collector, VisitorProviderFactory.createDefaultVisitorProvider());
        //  secondPassForOlExplorerClassRefs(collector.radixObjects, sb);

        final String result = sb.toString();

        if (result != null && !result.isEmpty()) {
            DialogUtils.showText(sb.toString(), "Result", "txt");

        } else {
            DialogUtils.messageInformation("Nothing found.");

        }
    }

    private void updateCommandHandlers(final List<RadixObject> radixObjects, final StringBuilder sb) {
        final Id afterInitId = Id.Factory.loadFrom("mthD77F2HBL2FGSVPJHR3SEAMPWMA");
        final Id beforeInitId = Id.Factory.loadFrom("mthGZDY76GQDVEENNFB33JRO5ADCU");
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsCommandHandlerMethodDef && ((AdsCommandHandlerMethodDef) obj).getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                try {
                    ((AdsCommandHandlerMethodDef) obj).updateProfile();
                    sb.append(obj.getQualifiedName() + "\n");
                } catch (RadixPublishedException ex) {
                    DialogUtils.messageError(ex);
                }
            } else if (obj instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) obj;
                if (method.getOwnerClass().getClassDefType() == EClassType.APPLICATION || method.getOwnerClass().getClassDefType() == EClassType.ENTITY) {
                    if (method.getId() == afterInitId) {
                        AdsMethodDef.Profile profile = method.getProfile();
                        if (profile.getParametersList().size() == 1) {
                            profile.getParametersList().add(MethodParameter.Factory.newInstance("phase", AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.enums.EEntityInitializationPhase")));
                        }
                        sb.append(method.getQualifiedName() + "\n");
                    } else if (method.getId() == beforeInitId) {
                        AdsMethodDef.Profile profile = method.getProfile();
                        if (profile.getParametersList().size() < 3) {
                            profile.getParametersList().add(MethodParameter.Factory.newInstance("phase", AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.common.enums.EEntityInitializationPhase")));
                        }
                        profile.getParametersList().get(0).setType(AdsTypeDeclaration.Factory.newPlatformClass("org.radixware.kernel.server.types.PropValHandlersByIdMap"));
                        profile.getParametersList().get(0).setName("initPropValHandlersById");
                        sb.append(method.getQualifiedName() + "\n");
                    }
                }
            }
        }
    }

    private void findContextlessEnabledSps(final List<RadixObject> radixObjects, final StringBuilder sb) {
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsSelectorPresentationDef) {
                final AdsSelectorPresentationDef res = (AdsSelectorPresentationDef) obj;
                if (!res.isRestrictionsInherited()) {
                    if (!res.getRestrictions().isDenied(ERestriction.CONTEXTLESS_USAGE)) {
                        sb.append(obj.getQualifiedName() + "\n");
                        res.getRestrictions().deny(ERestriction.CONTEXTLESS_USAGE);
                    }
                }
            }
        }
    }

    private static class CursorLeaksFinder extends AbstractFixer {

        @Override
        public void fix(List<RadixObject> radixObjects, StringBuilder sb) {
            collectAllNonStaticCursors(radixObjects, sb);
        }

        @Override
        public String getName() {
            return "Look for curors leaks";
        }
    }

    private static void collectAllNonStaticCursors(final List<RadixObject> radixObjects, final StringBuilder sb) {
        sb.append("Take a look at check results window");
        final Id sqlBlockId = Id.Factory.loadFrom("pdcSqlBlock__________________");
        final Id sqlResultSetId = Id.Factory.loadFrom("pdcResultSet_________________");
        final Id sqlStatementId = Id.Factory.loadFrom("pdcSqlStatement______________");
        final Id sqlSubQueryId = Id.Factory.loadFrom("adc7WQ6UZQBWVF5RAHMTI3ZB5K5UM");

        RadixProblemRegistry.getDefault().clear(radixObjects);
        for (RadixObject obj : radixObjects) {
            if (obj instanceof AdsPropertyDef) {
                AdsPropertyDef prop = (AdsPropertyDef) obj;
                boolean register = false;
                if (!prop.getAccessFlags().isStatic()) {
                    AdsTypeDeclaration decl = prop.getValue().getType();
                    if (decl != null && decl.getPath() != null && (decl.getPath().asList().contains(sqlBlockId)
                            || decl.getPath().asList().contains(sqlResultSetId)
                            || decl.getPath().asList().contains(sqlStatementId)
                            || decl.getPath().asList().contains(sqlSubQueryId))) {
                        register = true;
                    } else {
                        if ("java.sql.Statement".equals(decl.getExtStr())) {
                            register = true;
                        }
                    }

                }
                if (register) {
                    RadixProblemRegistry.getDefault().accept(RadixProblem.Factory.newError(prop, "Cursor leak suspeced"));
                }
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final CheckResultsTopComponent checkResults = CheckResultsTopComponent.findInstance();

                if (!checkResults.isEmpty()) {
                    checkResults.open();
                    checkResults.requestVisible();
                    checkResults.requestActive();
                } else {
                    //status = "Check complete for " + (System.currentTimeMillis() - startTimeMillis) + " msec.";

                }

            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final RadixObject radixObject = WindowManager.getDefault().getRegistry().getActivated().getLookup().lookup(RadixObject.class);
        if (radixObject == null) {
            DialogUtils.messageError("There is no object selected.");
            return;
        }
        if (!DialogUtils.messageConfirmation(
                "Execute Self Check action for\n"
                + radixObject.getTypeTitle() + " '" + radixObject.getQualifiedName() + "'?\n"
                + "This action designed for internal usage!!!")) {
            return;
        }

        scan(radixObject);
    }
}
