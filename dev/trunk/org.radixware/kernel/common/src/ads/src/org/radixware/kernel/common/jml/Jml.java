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

import java.util.List;
import java.util.Map;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.defs.VisitorProviderFactory;
import org.radixware.kernel.common.defs.ads.AdsClipboardSupport;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.IEnvDependent;
import org.radixware.kernel.common.defs.ads.common.AdsVisitorProviders;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.CodeWriter;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;


import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.defs.ads.type.IAdsTypedObject;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.LineMatcher.DefaultLocationDescriptor;
import org.radixware.kernel.common.scml.LineMatcher.ILocationDescriptor;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.scml.ScmlProcessor;
import org.radixware.schemas.xscml.JmlType;

/**
 * Jml (java murkup language) - расширение языка Java поддержкой гипертекстовых
 * структур
 *
 * @see Scml
 */
public class Jml extends Scml implements IJavaSource, IAdsTypedObject, IEnvDependent {

    protected Jml(RadixObject context, String name) {
        super(name);
        setContainer(context);
    }

    public AdsDefinition getOwnerDef() {
        RadixObject obj = getContainer();
        while (obj != null) {
            if (obj instanceof AdsDefinition) {
                return (AdsDefinition) obj;
            }
            obj = obj.getContainer();
        }
        return null;
    }

    /**
     * Фабрика для загрузки и создания Jml
     */
    public static final class Factory {

        public static final Jml loadFrom(RadixObject context, JmlType xJml, String name) {
            if (xJml != null) {
                Jml jml = new Jml(context, name);
                jml.loadFrom(xJml);
                return jml;
            }
            return null;
        }

        public static final Jml newInstance(RadixObject context, String name) {
            return new Jml(context, name);
        }

        public static final Jml newCopy(RadixObject context, Jml source) {
            if (source == null) {
                return null;
            }
            JmlType xJml = JmlType.Factory.newInstance();
            source.appendTo(xJml, AdsDefinition.ESaveMode.NORMAL);
            return loadFrom(context, xJml, null);
        }
    }

    public ILocationDescriptor getLocationDescriptor() {
        return new DefaultLocationDescriptor(getOwnerDef()) {

            @Override
            public String getDetails() {
                return getName();
            }
        };
    }

    @Override
    public String getName() {
        String name = super.getName();
        if (name == null) {
            return "Jml";
        } else {
            return name;
        }
    }

    public static abstract class Tag extends Scml.Tag implements IJavaSource {

        public static final char[] TAG_LEAD_MARKER = " /*##JIS##".toCharArray();
        public static final char[] TAG_TAIL_MARKER = "/*##JIE##".toCharArray();
        public static final char[] TAG_MARKER_TAIL = "##*/".toCharArray();

        public static final class Factory {

            public static final Tag loadFrom(JmlType.Item item) {
                if (item.isSetTypeDeclaration()) {
                    return new JmlTagTypeDeclaration(item.getTypeDeclaration());
                } else if (item.isSetEventCode()) {
                    return new JmlTagEventCode(item.getEventCode());
                } else if (item.isSetIdReference()) {
                    if (item.getIdReference().getInvoke()) {
                        return JmlTagInvocation.Factory.loadFrom(item.getIdReference());
                    } else if (item.getIdReference().getDbName()) {
                        return JmlTagDbName.Factory.loadFrom(item.getIdReference());
                    } else {
                        return new JmlTagId(item.getIdReference());
                    }
                } else if (item.isSetLiteral()) {
                    return new JmlTagLiteral(item.getLiteral());
                } else if (item.isSetLocalizedString()) {
                    return new JmlTagLocalizedString(item.getLocalizedString());
                } else if (item.isSetTask()) {
                    return new JmlTagTask(item.getTask());
                } else if (item.isSetPin()) {
                    return new JmlTagPin(item.getPin());
                } else if (item.isSetData()) {
                    return new JmlTagData(item.getData());
                } else if (item.isSetDbEntity()) {
                    return JmlTagDbEntity.Factory.loadFrom(item.getDbEntity());
                } else if (item.isSetReadLicense()) {
                    return new JmlTagReadLicense(item.getReadLicense());
                } else if (item.isSetCheckLicense()) {
                    return new JmlTagCheckLicense(item.getCheckLicense());
                } else {
                    return null;
                }
            }
        }

        protected Tag() {
        }

        public Jml getOwnerJml() {
            return (Jml) super.getOwnerScml();
        }

        public abstract void appendTo(JmlType.Item item);

        public abstract String getDisplayName();

        @Override
        public ClipboardSupport<? extends Jml.Tag> getClipboardSupport() {
            return new AdsClipboardSupport<Jml.Tag>(this) {

                @Override
                protected XmlObject copyToXml() {
                    JmlType.Item xDef = JmlType.Item.Factory.newInstance();
                    Jml.Tag.this.appendTo(xDef);
                    return xDef;
                }

                @Override
                protected Tag loadFrom(XmlObject xmlObject) {
                    if (xmlObject instanceof JmlType.Item) {
                        JmlType.Item xDef = (JmlType.Item) xmlObject;
                        if (xDef.isSetJava()) {
                            return super.loadFrom(xmlObject);
                        } else {
                            return Tag.Factory.loadFrom(xDef);
                        }
                    } else {
                        return super.loadFrom(xmlObject);
                    }
                }
            };
        }

        public abstract void check(IProblemHandler problemHandler, IHistory checker);

        protected void error(IProblemHandler problemHandler, String message) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newError(this/*
                         * .getOwner().getOwner()
                         */, message));
            }
        }

        protected void warning(IProblemHandler problemHandler, String message) {
            if (problemHandler != null) {
                problemHandler.accept(RadixProblem.Factory.newWarning(this/*
                         * .getOwner().getOwner()
                         */, message));
            }
        }
    }

    public void appendTo(JmlType xDef, AdsDefinition.ESaveMode mode) {
        synchronized (this) {
            if (mode == AdsDefinition.ESaveMode.NORMAL) {
                for (Scml.Item item : getItems()) {
                    if (item instanceof Jml.Tag) {
                        Jml.Tag tag = (Jml.Tag) item;
                        tag.appendTo(xDef.addNewItem());
                    } else {
                        Scml.Text text = (Scml.Text) item;
                        xDef.addNewItem().setJava(text.getText());
                    }
                }
            } else {
                xDef.addNewItem().setJava("<Protected Code>");
            }
        }
    }

    private class JmlClipboardSupport extends AdsClipboardSupport<Jml> {

        public JmlClipboardSupport() {
            super(Jml.this);
        }

        @Override
        protected XmlObject copyToXml() {
            JmlType xJml = JmlType.Factory.newInstance();
            Jml.this.appendTo(xJml, AdsDefinition.ESaveMode.NORMAL);
            return xJml;
        }

        @Override
        protected Jml loadFrom(XmlObject xmlObject) {
            JmlType xJml = (JmlType) xmlObject;
            Jml jml = Jml.Factory.loadFrom(null, xJml, null);
            return jml;
        }
    }

    @Override
    public ClipboardSupport<? extends Jml> getClipboardSupport() {
        return new JmlClipboardSupport();
    }

    private class JmlJavaCodeSupport
            extends JavaSourceSupport {

        public JmlJavaCodeSupport() {
            super(Jml.this);
        }

        @Override
        public CodeWriter getCodeWriter(UsagePurpose purpose) {
            return new JmlCodeWriter(this, Jml.this, purpose);
        }
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JmlJavaCodeSupport();
    }

    public void setJavaCode(final String java) {
        setText(java);
    }

    @Override
    public ERuntimeEnvironmentType getUsageEnvironment() {
        Definition def = getOwnerDefinition();
        if (def instanceof IEnvDependent) {
            return ((IEnvDependent) def).getUsageEnvironment();
        } else {
            return ERuntimeEnvironmentType.COMMON;
        }
    }

    public interface IHistory {

        public Map<Object, Object> getHistory();

        public void setHistory(Map<Object, Object> h);
    }

    private class Checker extends ScmlProcessor implements IHistory {

        IProblemHandler problemHandler;

        private Checker(IProblemHandler problemHandler) {
            this.problemHandler = problemHandler;
        }

        @Override
        protected void processTagInComment(Scml.Tag tag) {
// commented by RADIX-2089
//            if (tag instanceof Jml.Tag) {
//                final IProblemHandler problemHandlerProxy = ProblemHandlerFactory.newErrorToWarningProxy(problemHandler);
//                ((Jml.Tag) tag).check(problemHandlerProxy);
//            }
        }
        
        @Override
        protected CommentsAnalizer getCommentsAnalizer() {
            return CommentsAnalizer.Factory.newJavaCommentsAnalizer();
        }

        @Override
        protected void processText(Text text) {
        }

        @Override
        protected void processTag(Scml.Tag tag) {
            if (tag instanceof Jml.Tag) {
                ((Jml.Tag) tag).check(problemHandler, this);
            }
        }
        private Map<Object, Object> history;

        @Override
        public Map<Object, Object> getHistory() {
            return history;
        }

        @Override
        public void setHistory(Map<Object, Object> h) {
            history = h;
        }
    }

    public void check(IProblemHandler problemHandler, Map<Object, Object> history) {
        Checker c = new Checker(problemHandler);
        c.setHistory(history);
        if (history != null) {
            history.put(JmlTagReadLicense.LicenseTagsCheckContext.class, new JmlTagReadLicense.LicenseTagsCheckContext());
        }
        c.process(this);
    }

    @Override
    public AdsTypeDeclaration getType() {
        throw new IllegalStateException("Don use this method on jml");
    }

    @Override
    public VisitorProvider getTypeSourceProvider(EValType toRefine) {
        if (toRefine == null) {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
        if (toRefine.isEnumAssignableType()) {
            return AdsVisitorProviders.newEnumBasedTypeProvider(toRefine);
        } else if (toRefine == EValType.PARENT_REF || toRefine == EValType.ARR_REF) {
            return AdsVisitorProviders.newEntityObjectTypeProvider(null);
        } else if (toRefine == EValType.XML) {
            return AdsVisitorProviders.newXmlBasedTypesProvider(getUsageEnvironment());
        } else if (toRefine == EValType.USER_CLASS) {
            return AdsVisitorProviders.newClassBasedTypesProvider(getUsageEnvironment());
        } else {
            return VisitorProviderFactory.createEmptyVisitorProvider();
        }
    }

    @Override
    public boolean isTypeAllowed(EValType type) {
        return true;
    }

    @Override
    public boolean isTypeRefineAllowed(EValType type) {
        return type.isRefinableType();
    }

    protected void loadFrom(JmlType xJml) {
        if (xJml != null) {
            getItems().clear();
            List<JmlType.Item> items = xJml.getItemList();
            if (items != null && !items.isEmpty()) {

                for (JmlType.Item item : items) {
                    Scml.Item newItem = null;
                    if (item.isSetJava()) {
                        String java = item.getJava();
                        if (java.isEmpty()) {
                            continue;
                        }
                        newItem = Scml.Text.Factory.newInstance(java);
                    } else {
                        newItem = Jml.Tag.Factory.loadFrom(item);
                    }
                    if (newItem != null) {
                        getItems().add(newItem);
                    }
                }
            }

        }
    }
}
