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

package org.radixware.kernel.common.jml;

import java.text.MessageFormat;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.compiler.core.ast.RadixObjectLocator;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ExtendableDefinitions.EScope;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsLibUserFuncWrapper;
import org.radixware.kernel.common.defs.ads.clazz.enumeration.AdsFieldParameterDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.clazz.presentation.AdsFilterDef;
import org.radixware.kernel.common.defs.ads.clazz.presentation.IAdsPresentableProperty;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.clazz.AdsPropertyWriter;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.ui.AdsUISignalDef;
import org.radixware.kernel.common.enums.EIsoLanguage;
import org.radixware.kernel.common.enums.EPropNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CodePrinter;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.types.Id;
import org.radixware.schemas.xscml.JmlType;
import org.radixware.schemas.xscml.JmlType.Item;
import org.radixware.schemas.xscml.JmlType.Item.IdReference;


public class JmlTagInvocation extends JmlTagId {

    static class JmlTagInternalPropertyAccess extends JmlTagInvocation {

        private static final String EXT_STR = "#STD_PROP_VALUE#";

        public JmlTagInternalPropertyAccess(AdsDefinition referencedDef) {
            super(referencedDef);
        }

        public JmlTagInternalPropertyAccess(IdReference idRef) {
            super(idRef);
        }

        @Override
        public void appendTo(Item item) {
            super.appendTo(item);
            item.getIdReference().setExtStr(EXT_STR);
        }

        @Override
        public boolean isInternalPropertyAccessor() {
            return true;
        }

        public AdsPropertyDef findOwnerProperty() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof AdsPropertyDef) {
                    return (AdsPropertyDef) owner;
                }
            }
            return null;
        }

        @Override
        protected AdsPath getPathForSave() {
            Jml jml = getOwnerJml();
            if (jml.getContainer() instanceof AdsPropertyDef.Accessor) {
                AdsPropertyDef prop = findOwnerProperty();
                if (prop != null) {
                    return new AdsPath(prop);
                } else {
                    return super.getPathForSave();
                }
            } else {
                return super.getPathForSave();
            }
        }

        @Override
        public Definition resolve(Definition referenceContext) {
            Jml jml = getOwnerJml();
            if (jml.getContainer() instanceof AdsPropertyDef.Accessor) {
                return findOwnerProperty();
            } else {
                return super.resolve(referenceContext);
            }
        }
    }

    public static final class Factory {

        public static JmlTagInvocation newInstance(AdsDefinition referencedDef) {
            return new JmlTagInvocation(referencedDef);
        }

        public static JmlTagInvocation newInternalPropAccessor(AdsDefinition referencedDef) {
            return new JmlTagInternalPropertyAccess(referencedDef);
        }

        public static JmlTagInvocation loadFrom(JmlType.Item.IdReference idRef) {
            if (idRef.getExtStr() != null) {
                if (JmlTagInternalPropertyAccess.EXT_STR.equals(idRef.getExtStr())) {
                    return new JmlTagInternalPropertyAccess(idRef);
                }
            }
            return new JmlTagInvocation(idRef);
        }
    }

    private JmlTagInvocation(JmlType.Item.IdReference idRef) {
        super(idRef);
    }

    private JmlTagInvocation(AdsDefinition referencedDef) {
        super(referencedDef);

    }

    @Override
    public String toString() {
        return MessageFormat.format("[tag invocation={0}]", path.toString());
    }

    @Override
    public void appendTo(Item item) {
        Item.IdReference ref = item.addNewIdReference();
        ref.setPath(getPathForSave().asList());
        ref.setInvoke(true);
    }

    protected AdsPath getPathForSave() {
        return path;
    }

    public boolean isInternalPropertyAccessor() {
        return false;
    }

    @Override
    public String getDisplayName() {
        Jml jml = getOwnerJml();
        if (jml == null) {
            return "UNKNOWN";
        }
        Definition context = jml.getOwnerDefinition();
        Definition def = resolve(context);
        if (def != null) {
            if (def instanceof AdsLibUserFuncWrapper) {
                return ((AdsLibUserFuncWrapper) def).getLibName() + "::" + def.getName();
            } else {
                if (def instanceof AdsMethodDef || def instanceof AdsPropertyDef
                        || def instanceof AdsFilterDef.Parameter || def instanceof AdsEnumClassFieldDef
                        || def instanceof AdsFieldParameterDef || def instanceof AdsUISignalDef) {

                    if (isInternalPropertyAccessor()) {
                        return "internal[" + def.getName() + "]";
                    } else {
                        return def.getName();
                    }
                } else {
                    return def.getQualifiedName(context);
                }
            }
        } else {
            return "unknowndef: " + AdsPath.toString(getPath()) + "]";
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new CodeWriter(this, purpose) {
                    @Override
                    public boolean writeCode(CodePrinter printer) {
                        if (getOwnerJml() == null) {
                            return false;
                        }
                        final RadixObjectLocator locator = (RadixObjectLocator) printer.getProperty(RadixObjectLocator.PRINTER_PROPERTY_NAME);
                        RadixObjectLocator.RadixObjectData marker = null;
                        if (locator != null) {
                            marker = locator.start(JmlTagInvocation.this);
                        }
                        try {
                            Definition def = resolve(getOwnerJml().getOwnerDefinition());
                            if (def != null) {
                                if (def instanceof AdsLibUserFuncWrapper) {
                                    AdsLibUserFuncWrapper ufw = (AdsLibUserFuncWrapper) def;
                                    printer.print(ufw.getId().toString());
                                    return true;
                                } else {
                                    if (isInternalPropertyAccessor() && def instanceof AdsPropertyDef) {
                                        AdsPropertyWriter writer = (AdsPropertyWriter) ((AdsPropertyDef) def).getJavaSourceSupport().getCodeWriter(usagePurpose);
                                        writer.writeInternalInvocation(printer);
                                        return true;
                                    } else {
                                        if (def instanceof IJavaSource) {
                                            writeUsage(printer, (IJavaSource) def);
                                        } else {
                                            printer.printStringLiteral(def.getId().toString());
                                        }
                                        return true;
                                    }
                                }
                            } else {
                                //this is possibly source code lost. try to find something and continue,
                                Id[] ids = path.asArray();
                                //this might be a property or method or constructor; constructor 
                                if (ids.length > 0) {
                                    //try detect constructor
                                    int index = getOwnerJml().getItems().indexOf(JmlTagInvocation.this);
                                    if (index > 0) {
                                        Scml.Item prev = getOwnerJml().getItems().get(index - 1);
                                        if (prev instanceof Scml.Text) {
                                            String text = ((Scml.Text) prev).getText();
                                            if (text.trim().endsWith("new") && ids.length > 1) {// this is constructor invocation
                                                Id[] classIds = new Id[ids.length - 1];
                                                System.arraycopy(ids, 0, classIds, 0, classIds.length);
                                                AdsTypeDeclaration decl = AdsTypeDeclaration.Factory.newInstance(EValType.USER_CLASS, classIds);
                                                writeCode(printer, decl, getOwnerJml());
                                                return true;
                                            }
                                        }
                                    }

                                    printer.print(ids[ids.length - 1]);
                                    return true;
                                }
                                printer.printError();
                                return false;
                            }
                        } finally {
                            if (marker != null) {
                                marker.commit();
                            }
                        }
                    }

                    @Override
                    public void writeUsage(CodePrinter printer) {
                        //do nothing
                    }
                };
            }
        };
    }

    @Override
    public String getToolTip() {
        return getToolTip(EIsoLanguage.ENGLISH);
    }

    @Override
    public String getToolTip(EIsoLanguage language) {
        if (isInternalPropertyAccessor()) {
            final Jml ownerJml = getOwnerJml();
            if (ownerJml == null) {
                return "";
            }
            Definition def = resolve(ownerJml.getOwnerDefinition());
            if (def == null) {
                StringBuilder b = new StringBuilder();
                b.append("<html>");
                b.append("<b><font color=\"#FF0000\">Unresoved Reference</font></b>");
                if (path != null) {
                    Definition something = path.resolveSomething(getOwnerJml().getOwnerDefinition());
                    if (something != null) {
                        b.append("<br>Last resolved owner: ");
                        b.append(something.getQualifiedName());
                    }
                    b.append("<br>Definition Path: ");
                    b.append(path.toString().replace("<", "&lt;").replace(">", "&gt;"));
                }
                b.append("</html>");
                return b.toString();
            } else {
                StringBuilder b = new StringBuilder();
                b.append("<html>");
                b.append("<b><font color=\"\">Internal value of property</font></b><br>");
                b.append(def.getToolTip());
                b.append("</html>");
                return b.toString();
            }
        } else {
            return super.getToolTip(language);
        }
    }

    @Override
    public void check(IProblemHandler problemHandler, Jml.IHistory h) {
        Definition def = basicCheckImpl(problemHandler);
        if (def instanceof AdsPropertyDef) {
            Jml jml = this.getOwnerJml();
            AdsPropertyDef prop = (AdsPropertyDef) def;
            AdsClassDef clazz = prop.getOwnerClass();
            if (prop.getNature() == EPropNature.EXPRESSION) {
                if (((AdsExpressionPropertyDef) prop).isInvisibleForArte()) {
                    error(problemHandler, "Property " + prop.getQualifiedName() + " can not be used in jml code (pure sql property, invisible for ARTE)");
                }
            }
            if (clazz != null && clazz.getUsageEnvironment() == ERuntimeEnvironmentType.SERVER) {
                final ERuntimeEnvironmentType env = jml.getUsageEnvironment();
                if (env.isClientEnv()) {
//                    if (prop.getAccessMode() == EAccess.PRIVATE) {
//                        error(problemHandler, "Property " + prop.getQualifiedName() + " is inaccessible");
//                    }
                    if (!prop.isTransferable(env)) {
                        error(problemHandler, "Property " + prop.getQualifiedName() + " can not be used for client-server interaction for client environment " + env.getName());
                    }
                    if (prop instanceof IAdsPresentableProperty) {
                        ServerPresentationSupport support = ((IAdsPresentableProperty) prop).getPresentationSupport();
                        if (support == null || support.getPresentation() == null || !support.getPresentation().isPresentable()) {
                            error(problemHandler, "Property " + prop.getQualifiedName() + " is not presentable");
                        }
                    }
                }
            }
        }
        if (isInternalPropertyAccessor()) {
            Jml jml = ((JmlTagInternalPropertyAccess) this).getOwnerJml();
            if (jml != null) {
                AdsDefinition ownerDef = jml.getOwnerDef();
                AdsPropertyDef prop = ((JmlTagInternalPropertyAccess) this).findOwnerProperty();
                boolean errorneous = false;
                if (prop != ownerDef) {
                    if (ownerDef instanceof AdsMethodDef) {
                        AdsMethodDef method = (AdsMethodDef) ownerDef;
                        if (!method.isConstructor() || method.getOwnerClass().getProperties().findById(this.getPath().getTargetId(), EScope.LOCAL) == null) {
                            errorneous = true;
                        }
                    } else {
                        errorneous = true;
                    }
                } else {
                    if (prop == null) {
                        errorneous = true;
                    }
                }
                if (errorneous) {
                    error(problemHandler, "Internal property access tag should be used in constructor or property getter or setter only");
                } else {
                    if (prop != null) {
                        if (def == null || (def.getId() != prop.getId())) {
                            error(problemHandler, MessageFormat.format("This internal property access tag should be used only in getter or setter of {0} or in constructor of class {1}", prop.getQualifiedName(def), prop.getOwnerClass().getQualifiedName(prop)));
                        }
                    }

                }
            }
        } else {
            doEnvCheck(def, problemHandler);
//            Definition def = resolve(getOwnerJml().getOwnerDef());
//            if (def instanceof AdsPropertyPresentationPropertyDef) {//check property is publlished in at least one editor page of owner entity model
//                AdsPropertyPresentationPropertyDef pp = (AdsPropertyPresentationPropertyDef) def;
//                EditorPages pages = pp.findPublishingPagesList();
//                if (pages == null) {
//                    error(problemHandler, MessageFormat.format("Invalid owner for property {0}: {1}", def.getQualifiedName(getOwnerJml().getOwnerDef()), pp.getOwnerDef().getQualifiedName()));
//                } else {
//                    if (!((AdsPropertyPresentationPropertyDef) def).isPublishedForEditing()) {
//                        Definition pod = pages.getOwnerDefinition();
//                        error(problemHandler, MessageFormat.format("The property {0} is not published in editor pages of {1} {2}", def.getQualifiedName(getOwnerJml().getOwnerDef()), pod.getTypeTitle().toLowerCase(), pod.getQualifiedName()));
//                    }
//                }
//            }
        }
    }

    @Override
    protected boolean performEnvCheck() {
        return false;
    }
}
