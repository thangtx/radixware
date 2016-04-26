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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.AdsDefinition;

import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;

import org.radixware.kernel.common.defs.ads.clazz.algo.object.AdsAppObject.Prop;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodDef.MethodValue;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsPropertyDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsSystemMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsTransparentMethodDef;
import org.radixware.kernel.common.defs.ads.clazz.members.MethodParameter;
import org.radixware.kernel.common.defs.ads.clazz.members.PropertyValue;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumItemDef;
import org.radixware.kernel.common.defs.ads.localization.AdsMultilingualStringDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.rights.AdsRoleDef;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.defs.ads.xml.AdsXmlSchemeDef;
import org.radixware.kernel.common.defs.localization.ILocalizedDef;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;
import org.radixware.kernel.common.jml.JmlTagTypeDeclaration;
import org.radixware.kernel.common.repository.Branch;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.sqml.tags.ConstValueTag;
import org.radixware.kernel.common.sqml.tags.PropSqlNameTag;
import org.radixware.kernel.common.types.Id;


public class RBSFix {

    public void exec(final List<RadixObject> objects, StringBuilder report) {
        fixParentPropSqlNameTagInParentRefPropCondition(objects, report);
        fixSystemMethodArgumentNames(objects, report);
        fixConstSetDefUsage(objects, report);
        fixUserFuncGetMethodId(objects, report);
        fixEnumItemValueTag(objects, report);
        fixUnfoundStrings(objects, report);
        fixOverridingMethods(objects, report);
        fixJmlTypeRefs(objects, report);
        fixTransparentMethods(objects, report);
        fixClercRole(objects, report);
        for (RadixObject obj : objects) {
            if (obj instanceof AdsXmlSchemeDef) {
                AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) obj;
                if (scheme.getId().toString().equals("xsdE6MW5W5QFJBDFB7CLJPPF6KLIE") || scheme.getId().toString().equals("xsdMY6ZWGXC3FGE3HRGYOGNXDXSGQ")) {

                    scheme.setTargetEnvironment(ERuntimeEnvironmentType.COMMON);
                }
            } else if (obj instanceof Prop) {
                Prop var = (Prop) obj;
                String extStr = var.getType().getExtStr();
                String newExtStr = null;
                if ("com.compassplus.dbp.enums.CDwfFormClerkAutoSelect".equals(extStr)) {
                    newExtStr = "org.radixware.kernel.common.enums.EDwfFormClerkAutoSelect";
                }
                if (newExtStr != null) {
                    var.setType(AdsTypeDeclaration.Factory.newPlatformClass(newExtStr));
                    report.append("Change :" + extStr + " -> " + newExtStr + "\n");
                }
            } else if (obj instanceof Jml.Text) {
                String java = ((Jml.Text) obj).getText();
                java = java.replace("import com.compassplus.dbp.types.ArrStr;", "");
                java = java.replace("import com.compassplus.dbp.types.ArrBool;", "");
                java = java.replace("dbuMeta.", "rdxMeta.");
                java = java.replace("dacMeta.", "rdxMeta.");
                java = java.replace("com.compassplus.dbp.meta.dbu.TDbuEditorPresentation", "org.radixware.kernel.server.meta.presentations.RadEditorPresentationDef");
                java = java.replace("import com.compassplus.dbp.arte.JobQueue;", "import org.radixware.kernel.server.arte.JobQueue;");

                java = java.replace("import com.compassplus.dbp.meta.drc.TDrcRole;", "import org.radixware.kernel.server.meta.roles.RadRoleDef");
                java = java.replace("import com.compassplus.dbp.meta.dbp.TDbpProject;", "");
                java = java.replace("import com.compassplus.dbp.arte.Rights;", "import org.radixware.kernel.server.arte.Rights;");
                java = java.replace("import com.compassplus.dbp.meta.dbp.TDbpMainProject.AccessPartitionFamily;", "import  org.radixware.kernel.common.defs.dds.DdsAccessPartitionFamilyDef;");
                java = java.replace("TDrcRole", "RadRoleDef");
                java = java.replace("import com.compassplus.dbp.types.StringEnum;", "import org.radixware.kernel.common.types.IKernelStrEnum;");

                java = java.replace("com.compassplus.dbp.meta.dac.TDacUserProperty", "org.radixware.kernel.server.meta.clazzes.RadUserPropDef");

                java = java.replace("import com.compassplus.product.twrbs.enums.CryptoKeyKind", "com.tranzaxis.kernel.common.enums.ECryptoKeyKind");
                java = java.replace("import com.compassplus.product.twrbs.enums.CryptoKeyExtForm", "com.tranzaxis.kernel.common.enums.ECryptoKeyExtForm");

                java = java.replace(".getPresentationClassDef(", ".getClassPresentationDef(");

                int index = java.indexOf(".getCustomView$$$");
                while (index >= 0) {
                    int last = java.indexOf("(", index);
                    if (last >= 0) {
                        java = java.substring(0, index) + ".getCustomView" + java.substring(last);
                    }
                    index = java.indexOf(".getCustomView$$$", index + 1);
                }


                if (!java.equals(((Jml.Text) obj).getText())) {
                    ((Jml.Text) obj).setText(java);
                }
            } else if (obj instanceof MethodValue) {
                MethodValue val = (MethodValue) obj;
                AdsTypeDeclaration decl = val.getType();
                if (decl.getTypeId() == EValType.JAVA_CLASS) {
                    String extStr = decl.getExtStr();
                    if ("com.compassplus.dbp.types.Bin".equals(extStr)) {
                        AdsTypeDeclaration newDecl = AdsTypeDeclaration.Factory.newInstance(EValType.BIN);
                        val.setType(newDecl);
                    }
                } else if (decl.getPath() != null && decl.getPath().asList().size() == 1 && "xsdY5AELJNM7FG77FZD4ATT7SFMXA".equals(decl.getPath().asList().get(0).toString())) {
                    if (decl.getExtStr() != null && decl.getExtStr().startsWith("DbpSqml")) {
                        AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) findBranch(obj).find(new VisitorProvider() {
                            @Override
                            public boolean isTarget(RadixObject radixObject) {
                                return radixObject instanceof AdsXmlSchemeDef && ((AdsXmlSchemeDef) radixObject).getId().toString().equals("xsdY5AELJNM7FG77FZD4ATT7SFMXA");
                            }
                        });
                        val.setType(AdsTypeDeclaration.Factory.newXml(scheme, decl.getExtStr().substring(3)));
                    }
                }
            } else if (obj instanceof JmlTagTypeDeclaration) {
                JmlTagTypeDeclaration inv = (JmlTagTypeDeclaration) obj;

                Jml jml = inv.getOwnerJml();
                if (jml == null) {
                    continue;
                }
                ;
                AdsType type = inv.getType().resolve(jml.getOwnerDef()).get();
                if (type instanceof AdsEnumType) {
                    AdsEnumDef def = ((AdsEnumType) type).getSource();

                    if (def != null && def.getId().toString().equals("acsNQZ4CK5QPPNRDOKJABIFNQAABA")) {
                        int index = jml.getItems().indexOf(inv);
                        if (index >= 0 && jml.getItems().size() > index + 1) {
                            Jml.Item item = jml.getItems().get(index + 1);
                            if (item instanceof Jml.Text) {
                                String text = ((Jml.Text) item).getText();
                                if (text.startsWith(".")) {
                                    for (int i = 1; i < text.length(); i++) {
                                        char c = text.charAt(i);
                                        if (!Character.isJavaIdentifierPart(c)) {
                                            String name = text.substring(1, i);
                                            StringBuilder nnn = new StringBuilder();
                                            for (char ch : name.toCharArray()) {
                                                if (ch == '_') {
                                                    continue;
                                                }
                                                nnn.append(ch);
                                            }
                                            name = nnn.toString();
                                            AdsEnumDef e = def;
                                            for (AdsEnumItemDef ei : e.getItems().get(EScope.ALL)) {
                                                if (ei.getName().toUpperCase().equals(name.toUpperCase())) {
                                                    JmlTagInvocation newInvoke = JmlTagInvocation.Factory.newInstance(ei);
                                                    inv.delete();
                                                    jml.getItems().add(index, newInvoke);
                                                    ((Jml.Text) item).setText(text.substring(i));
                                                    break;
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
    }

    private void fixParentPropSqlNameTagInParentRefPropCondition(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof PropSqlNameTag) {
                Definition ownerDef = obj.getDefinition();
                if (ownerDef instanceof AdsPropertyDef && (((AdsPropertyDef) ownerDef).getValue().getType().getTypeId() == EValType.PARENT_REF || ((AdsPropertyDef) ownerDef).getValue().getType().getTypeId() == EValType.ARR_REF)) {
                    PropSqlNameTag tag = (PropSqlNameTag) obj;
                    if (tag.getOwnerType() == PropSqlNameTag.EOwnerType.PARENT) {
                        tag.setOwnerType(PropSqlNameTag.EOwnerType.THIS);
                        report.append("PropSqlNameTag change: PARENT->THIS: " + tag.getQualifiedName() + "\n");
                    }
                }
            }
        }
    }

    private void fixSystemMethodArgumentNames(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof AdsSystemMethodDef) {
                AdsSystemMethodDef sm = (AdsSystemMethodDef) obj;
                sm.syncWithSystem();
                report.append("SystemMethodSync: " + sm.getQualifiedName() + "\n");
            }
        }
    }

    private Branch findBranch(RadixObject obj) {
        while (obj != null) {
            if (obj instanceof Branch) {
                return (Branch) obj;
            }
            obj = obj.getContainer();
        }
        return null;
    }

    private void fixConstSetDefUsage(final List<RadixObject> objects, StringBuilder report) {
        final Id newId = Id.Factory.loadFrom("adcUTMGTTLFTFBHJMCICCD6GSYFZ4");
        AdsClassDef newRef = (AdsClassDef) findBranch(objects.get(0)).find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof AdsClassDef) && ((AdsClassDef) radixObject).getId() == newId;
            }
        });
        Id oldId = Id.Factory.loadFrom("adcYNUC4VNSIFH2VB6Q3WULUQIG6Y");
        Id oldId2 = Id.Factory.loadFrom("adcK2I6L3VRERCQ3K5I3HNY3NF2XY");
        Id oldId3 = Id.Factory.loadFrom("adcZCNPOOQIOBH2ZFVOSQI4W6QF44");


        for (RadixObject obj : objects) {
            if (obj instanceof MethodValue || obj instanceof PropertyValue) {
                AdsTypeDeclaration decl = ((IAdsTypedObject) obj).getType();
                if (decl != null && decl.getPath() != null) {
                    AdsPath path = decl.getPath();
                    if (path.asList().size() == 1 && path.asList().get(0) == oldId) {
                        if (obj instanceof MethodValue) {
                            ((MethodValue) obj).setType(AdsTypeDeclaration.Factory.newInstance(newRef));
                            report.append("ConstDef: " + obj.getQualifiedName() + "\n");
                        } else if (obj instanceof PropertyValue) {
                            ((PropertyValue) obj).setType(AdsTypeDeclaration.Factory.newInstance(newRef));
                            report.append("ConstDef: " + obj.getQualifiedName() + "\n");
                        }
                    } else if (path.asList().size() == 1 && path.asList().get(0) == oldId2) {
                        if (obj instanceof MethodValue) {
                            ((MethodValue) obj).setType(AdsTypeDeclaration.Factory.newPlatformClass("com.trolltech.qt.QSignalEmitter.Signal1"));
                            report.append("ConstDef: " + obj.getQualifiedName() + "\n");


                        } else if (obj instanceof PropertyValue) {
                            ((PropertyValue) obj).setType(AdsTypeDeclaration.Factory.newPlatformClass("com.trolltech.qt.QSignalEmitter.Signal1"));
                            report.append("ConstDef: " + obj.getQualifiedName() + "\n");
                        }

                    } else if (path.asList().size() == 1 && path.asList().get(0) == oldId3) {
                        if (obj instanceof MethodValue) {
                            ((MethodValue) obj).setType(AdsTypeDeclaration.Factory.newPlatformClass("com.trolltech.qt.QSignalEmitter.Signal0"));
                            report.append("ConstDef: " + obj.getQualifiedName() + "\n");
                        } else if (obj instanceof PropertyValue) {
                            ((PropertyValue) obj).setType(AdsTypeDeclaration.Factory.newPlatformClass("com.trolltech.qt.QSignalEmitter.Signal0"));
                            report.append("ConstDef: " + obj.getQualifiedName() + "\n");
                        }

                    }
                }

            }
        }
    }

    private void fixUserFuncGetMethodId(final List<RadixObject> objects, StringBuilder report) {
        Id getMethodIdId = Id.Factory.loadFrom("mthFOOX7DTCW5GA5CUK2VYOOVAC3Q");
        Id onCalcEprId = Id.Factory.loadFrom("mthZJ672NR72ZEPJP2YHQ4KTL2VWY");
        Id onListInstantiableClasses = Id.Factory.loadFrom("mth7MKI5PVAVJHQBGNMFO246UGC4Y");
        Id mapSelectorColumnId = Id.Factory.loadFrom("mthNQB26H257VGMXGEXMFCRH7FNSU");
        Id getEditorPrForChoosenEntity = Id.Factory.loadFrom("mthVXNK6YPP55GBTEQ5CQV7XGG7UA");


        final Id newId = Id.Factory.loadFrom("adcELH54EKVCJAATNTLFHHCSVHPZU");
        AdsClassDef newRef = (AdsClassDef) findBranch(objects.get(0)).find(new VisitorProvider() {
            @Override
            public boolean isTarget(RadixObject radixObject) {
                return (radixObject instanceof AdsClassDef) && ((AdsClassDef) radixObject).getId() == newId;
            }
        });
        for (RadixObject obj : objects) {
            if (obj instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) obj;
                if (method.getId() == getMethodIdId || method.getId() == onCalcEprId || method.getId() == mapSelectorColumnId || method.getId() == getEditorPrForChoosenEntity) {
                    method.getProfile().getReturnValue().setType(AdsTypeDeclaration.Factory.newInstance(newRef));
                    report.append("Return value from Str to Id" + obj.getQualifiedName() + "\n");
                    ((AdsMethodDef) obj).setOverride(true);
                }
                if (method.getId() == onCalcEprId || method.getId() == onListInstantiableClasses) {
                    method.getProfile().getParametersList().get(0).setType(AdsTypeDeclaration.Factory.newInstance(newRef));
                    report.append("Param#1 value from Str to Id" + obj.getQualifiedName() + "\n");
                    ((AdsMethodDef) obj).setOverride(true);
                }
            }

        }
    }

    private void fixEnumItemValueTag(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof ConstValueTag) {
                ConstValueTag tag = (ConstValueTag) obj;
                if (tag.getEnumId().toString().startsWith("ecs") || tag.getEnumId().toString().startsWith("tcs")) {
                    String newIdAsStr = "a" + tag.getEnumId().toString().substring(1);
                    tag.setEnumId(Id.Factory.loadFrom(newIdAsStr));
                    report.append("ConstValueTag: " + obj.getQualifiedName() + "\n");
                }

            }
        }
    }

    private void fixUnfoundStrings(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof ILocalizedDef) {
                ILocalizedDef def = (ILocalizedDef) obj;
                ArrayList<ILocalizedDef.MultilingualStringInfo> ids = new ArrayList<ILocalizedDef.MultilingualStringInfo>();
                def.collectUsedMlStringIds(ids);
                for (ILocalizedDef.MultilingualStringInfo info : ids) {
                    if (info.getId() != null && info.findString() == null) {
                        AdsMultilingualStringDef string = AdsMultilingualStringDef.Factory.newInstance();
                        ((AdsDefinition) def).findLocalizingBundle().getStrings().getLocal().add(string);
                        info.setString(string);
                        report.append("String not found, reset to default: " + obj.getQualifiedName() + "\n");
                    }
                }
            }
        }

    }

    private void fixOverridingMethods(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof AdsMethodDef) {
                AdsMethodDef method = (AdsMethodDef) obj;
                ArrayList<AdsMethodDef> methods = new ArrayList<AdsMethodDef>();
                while (method != null) {
                    methods.add(method);
                    method = method.getHierarchy().findOverridden().get();
                }
                if (methods.size() > 1) {
                    AdsMethodDef root = methods.get(methods.size() - 1);
                    for (int i = 0; i < methods.size() - 1; i++) {
                        method = methods.get(i);
                        method.getProfile().getReturnValue().setType(root.getProfile().getReturnValue().getType());
                        String[] names = new String[method.getProfile().getParametersList().size()];
                        int index = 0;
                        for (MethodParameter p : method.getProfile().getParametersList()) {
                            names[index] = p.getName();
                            index++;
                        }
                        method.getProfile().getParametersList().clear();
                        index = 0;
                        for (MethodParameter p : root.getProfile().getParametersList()) {
                            method.getProfile().getParametersList().add(MethodParameter.Factory.newInstance(index >= names.length ? p.getName() : names[index], p.getType()));
                            index++;
                        }
                        report.append("Profile sync:" + method.getQualifiedName());
                    }
                }

            }
        }
    }

    private void fixJmlTypeRefs(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof Jml) {

                Jml jml = (Jml) obj;
                List<Jml.Item> items = jml.getItems().list();
                for (int i = 0; i < items.size(); i++) {
                    Jml.Item item = items.get(i);
                    if (item instanceof JmlTagTypeDeclaration) {
                        JmlTagTypeDeclaration tag = (JmlTagTypeDeclaration) item;
                        AdsTypeDeclaration type = tag.getType();


                        if (type.getPath() != null && type.getPath().asList().size() == 1 && "adcK2I6L3VRERCQ3K5I3HNY3NF2XY".equals(type.getPath().asList().get(0).toString())) {
                            JmlTagTypeDeclaration newTag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newPlatformClass("com.trolltech.qt.QSignalEmitter.Signal1"));
                            jml.getItems().remove(i);
                            jml.getItems().add(i, newTag);
                        } else if (type.getPath() != null && type.getPath().asList().size() == 1 && "adcZCNPOOQIOBH2ZFVOSQI4W6QF44".equals(type.getPath().asList().get(0).toString())) {
                            JmlTagTypeDeclaration newTag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newPlatformClass("com.trolltech.qt.QSignalEmitter.Signal0"));
                            jml.getItems().remove(i);
                            jml.getItems().add(i, newTag);
                        } else if (type.getPath() != null && type.getPath().asList().size() == 1 && "axcWNRWZTEZQNHTTKHUWP6KTGW4BQ".equals(type.getPath().asList().get(0).toString())) {
                            AdsClassDef clazz = (AdsClassDef) findBranch(jml).find(new VisitorProvider() {
                                @Override
                                public boolean isTarget(RadixObject radixObject) {
                                    return radixObject instanceof AdsClassDef && ((AdsClassDef) radixObject).getId() == Id.Factory.loadFrom("axcLQ3FIEIAOZGJ5JXDMDONEYT45U");
                                }
                            });
                            JmlTagTypeDeclaration newTag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newInstance(clazz));
                            jml.getItems().remove(i);
                            jml.getItems().add(i, newTag);
                        } else if (type.getPath() != null && type.getPath().asList().size() == 1 && "xsdY5AELJNM7FG77FZD4ATT7SFMXA".equals(type.getPath().asList().get(0).toString())) {
                            if (type.getExtStr() != null && type.getExtStr().startsWith("DbpSqml")) {
                                AdsXmlSchemeDef scheme = (AdsXmlSchemeDef) findBranch(obj).find(new VisitorProvider() {
                                    @Override
                                    public boolean isTarget(RadixObject radixObject) {
                                        return radixObject instanceof AdsXmlSchemeDef && ((AdsXmlSchemeDef) radixObject).getId().toString().equals("xsdY5AELJNM7FG77FZD4ATT7SFMXA");
                                    }
                                });
                                JmlTagTypeDeclaration newTag = new JmlTagTypeDeclaration(AdsTypeDeclaration.Factory.newXml(scheme, type.getExtStr().substring(3)));
                                jml.getItems().remove(i);
                                jml.getItems().add(i, newTag);
                            }
                        }
                    }
                    //axcWNRWZTEZQNHTTKHUWP6KTGW4BQ
                    //axcLQ3FIEIAOZGJ5JXDMDONEYT45U
                }
            }
        }
    }

    class PlatformClassCache {

        public final HashMap<String, RadixPlatformClass> platformClasses = new HashMap<String, RadixPlatformClass>();

        public RadixPlatformClass findPlatformClass(AdsDefinition context, String name) {
            RadixPlatformClass result = platformClasses.get(name);
            if (result == null) {
                result = ((AdsSegment) context.getModule().getSegment()).getBuildPath().getPlatformLibs().getKernelLib(context.getUsageEnvironment()).findPlatformClass(name);
                if (result != null) {
                    platformClasses.put(name, result);
                }
            }
            return result;
        }
    }

    private void fixTransparentMethods(final List<RadixObject> objects, StringBuilder report) {

        PlatformClassCache cache = new PlatformClassCache();

        for (RadixObject obj : objects) {
            if (obj instanceof AdsTransparentMethodDef) {
                AdsTransparentMethodDef method = (AdsTransparentMethodDef) obj;
                AdsTransparence t = method.getOwnerClass().getTransparence();
                String publishedClassName = t.getPublishedName();
                RadixPlatformClass clazz = cache.findPlatformClass(method, publishedClassName);
                ArrayList<RadixPlatformClass.Method> sameNames = new ArrayList<RadixPlatformClass.Method>();
                if (clazz != null) {
                    //final AdsTypeDeclaration[] profile = method.getProfile().getNormalizedProfile();
                    RadixPlatformClass.Method publishedMethod = null;
                    //String methodName = new String(method.getPublishedMethodName());
                    while (clazz != null) {
                        publishedMethod = clazz.findMethodByProfile(method);
                        if (publishedMethod != null) {
                            break;
                        }
                        sameNames.addAll(clazz.findMethodsByName(method.getName()));
                        AdsTypeDeclaration decl = clazz.getSuperclass();
                        if (decl != null) {
                            clazz = cache.findPlatformClass(method, decl.getExtStr());
                        } else {
                            break;
                        }
                    }
                    if (publishedMethod == null) {
                        //not found
                        for (RadixPlatformClass.Method m : sameNames) {
                            if (m.getParameterTypes().length == method.getProfile().getParametersList().size()) {
                                method.getTransparence().setPublishedName(new String(m.getRadixSignature()));
                                method.getProfile().getReturnValue().setType(m.getReturnType());
                                method.getProfile().getParametersList().clear();
                                int index = 0;
                                for (AdsTypeDeclaration decl : m.getParameterTypes()) {
                                    method.getProfile().getParametersList().add(MethodParameter.Factory.newInstance("arg" + index, decl));
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private void fixClercRole(final List<RadixObject> objects, StringBuilder report) {
        for (RadixObject obj : objects) {
            if (obj instanceof AdsRoleDef) {

                AdsRoleDef role = (AdsRoleDef) obj;
                ArrayList<AdsRoleDef.Resource> toRemove = new ArrayList<AdsRoleDef.Resource>();
                for (AdsRoleDef.Resource res : role.getResources()) {

                    if (res.defId.toString().equals("aclFSXJ3WLVJTOBDCIVAALOMT5GDM")
                            || res.defId.toString().equals("aclIE2CPLZTGRAGTLYRELNBHAHYQY")
                            || res.defId.toString().equals("aclMK5E225ZKHPBDBSKABIFNQAABA")
                            || res.defId.toString().equals("clcNC745EZ6STOBDCKWAALOMT5GDM")
                            || res.defId.toString().equals("clcNZYA7QDCQ7OBDCKFAALOMT5GDM")
                            || res.defId.toString().equals("aclIE2CPLZTGRAGTLYRELNBHAHYQY")
                            || res.defId.toString().equals("aacKRF23YDP73ORDI45ABQARJXZQE")
                            || res.defId.toString().equals("aecJ6SOXKD3ZHOBDCMTAALOMT5GDM")) {



                        toRemove.add(res);
                    }

                }
                for (AdsRoleDef.Resource res : toRemove) {
                    role.RemoveResourceRestrictions(AdsRoleDef.generateResHashKey(res));
                }
            }
        }
    }
}
