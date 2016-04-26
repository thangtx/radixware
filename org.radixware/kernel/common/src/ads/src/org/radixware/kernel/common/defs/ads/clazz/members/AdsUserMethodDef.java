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

package org.radixware.kernel.common.defs.ads.clazz.members;

import java.util.List;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.scml.LineMatcher.ILocationDescriptor;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.enums.EMethodNature;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.jml.IJmlSource;
import org.radixware.kernel.common.jml.Jml;
import org.radixware.kernel.common.jml.JmlTagInvocation;

import org.radixware.kernel.common.scml.Scml;
import org.radixware.schemas.adsdef.AbstractMethodDefinition;
import org.radixware.schemas.adsdef.MethodDefinition;


public class AdsUserMethodDef extends AdsMethodDef implements IJmlSource {

    @Override
    public void afterOverwrite() {
        super.afterOverwrite();
        setDefaultBody(getHierarchy().findOverwritten().get(), true);
    }

    @Override
    public void afterOverride() {
        super.afterOverride();
        setDefaultBody(getHierarchy().findOverridden().get(), false);
    }

    private void setDefaultBody(AdsMethodDef ovr, boolean overwrite) {
        getSources().reset();
        getSource().getItems().clear();
        getProfile().getAccessFlags().setAbstract(false);
        if (overwrite) {
            getSource().getItems().add(Scml.Text.Factory.newInstance("throw new UnsupportedOperationException(\"Not implemented yet.\");"));
        } else {
            if (ovr != null) {
                if (ovr.getProfile().getAccessFlags().isAbstract()) {
                    getSource().getItems().add(Scml.Text.Factory.newInstance("throw new UnsupportedOperationException(\"Not supported yet.\");"));
                } else if (ovr.getOwnerClass().getClassDefType() == EClassType.INTERFACE) {
                    getSource().getItems().clear();
                } else {
                    if (this.getProfile().getReturnValue() != null && this.getProfile().getReturnValue().getType() != null && this.getProfile().getReturnValue().getType() != AdsTypeDeclaration.VOID) {
                        getSource().getItems().add(Scml.Text.Factory.newInstance("return super."));
                    } else {
                        getSource().getItems().add(Scml.Text.Factory.newInstance("super."));
                    }
                    getSource().getItems().add(JmlTagInvocation.Factory.newInstance(ovr));
                    StringBuilder text = new StringBuilder();
                    text.append('(');
                    boolean first = true;
                    for (MethodParameter p : getProfile().getParametersList()) {
                        if (first) {
                            first = false;
                        } else {
                            text.append(", ");
                        }

                        text.append(p.getName());
                    }
                    text.append(");");
                    getSource().getItems().add(Scml.Text.Factory.newInstance(text.toString()));
                }
            }
        }
    }

    public static class Factory {

        public static AdsUserMethodDef newInstance() {
            return new AdsUserMethodDef("newMethod", false);
        }

        public static AdsUserMethodDef newPredefinedInstance(Id id, String name) {
            return new AdsUserMethodDef(id, name);
        }

        public static AdsUserMethodDef newInstance(AdsMethodDef source, boolean overwrite) {
            return new AdsUserMethodDef(source, overwrite);
        }

        public static AdsUserMethodDef newConstructorInstance() {
            return new AdsUserMethodDef("<init>", true);
        }

        public static AdsUserMethodDef newTemporaryInstance(RadixObject container) {
            AdsUserMethodDef m = newInstance();
            m.setContainer(container);
            return m;
        }

        public static AdsUserMethodDef newConstructorTemporaryInstance(RadixObject container) {
            AdsUserMethodDef m = new AdsUserMethodDef("<init>", true);
            m.setContainer(container);
            return m;
        }
    }
    public static final String JML_SOURCE_NAME = "Source";
    private final AdsSources sources = new AdsSources(this) {
        @Override
        protected String defaultName() {
            return JML_SOURCE_NAME;
        }
    };

    public AdsUserMethodDef(AdsMethodDef source, boolean overwrite) {
        super(source, overwrite);
    }

    protected AdsUserMethodDef(AbstractMethodDefinition xDef) {
        super(xDef);
        Jml src;
        if (xDef instanceof MethodDefinition) {
            MethodDefinition xMethod = (MethodDefinition) xDef;
            if (xMethod.getSource() != null) {//old style definition
                sources.loadFrom(xMethod.getSource());
            } else {
                sources.loadFrom(xMethod.getSources());
            }
        }
    }

    protected AdsUserMethodDef(String name, boolean isConstructor) {
        super(name, isConstructor);
    }

    protected AdsUserMethodDef(Id id, String name) {
        super(id, name);
    }

    @Override
    public EMethodNature getNature() {
        return EMethodNature.USER_DEFINED;
    }

    @Override
    public void appendTo(MethodDefinition xDef, ESaveMode saveMode) {
        super.appendTo(xDef, saveMode);
        if (!sources.isEmpty()) {
            sources.appendTo(xDef.addNewSources(), saveMode);
        }
    }

    /**
     * Returns jml source for the method
     */
    @Override
    public Jml getSource(String name) {
        return sources.getSource(name);
    }

    public Jml getSource(ERuntimeEnvironmentType env) {
        return sources.getSource(env);
    }

    public Jml getSource() {
        return sources.getSource((ERuntimeEnvironmentType) null);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        sources.visit(visitor, provider);

    }

    @Override
    public void collectDependences(List<Definition> list) {
        super.collectDependences(list);
    }

    @Override
    public ILocationDescriptor getLocationDescriptor(ERuntimeEnvironmentType env) {
        return sources.getSource(env).getLocationDescriptor();
    }

    @Override
    public boolean isIdInheritanceAllowed() {
        return getProfile().getAccessFlags().isStatic();
    }

    public AdsSources getSources() {
        return sources;
    }

    @Override
    public void setUsageEnvironment(ERuntimeEnvironmentType env) {
        super.setUsageEnvironment(env);
        sources.onOwnerEnvironmentChange();
    }
}
