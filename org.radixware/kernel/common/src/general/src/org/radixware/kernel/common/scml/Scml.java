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

package org.radixware.kernel.common.scml;

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.IVisitor;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjects;
import org.radixware.kernel.common.defs.VisitorProvider;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.utils.Utils;

/**
 * <p>SСML - Source Code Markup Language.</p>
 * <p>Расширение исходных кодов до гипертекста.
 * Представление исходных кодов в виде элементов, каждый из которых может быть либо чистым текстом, либо тэгом. 
 * Тэги могут быть разных типов и представляют из себя объекты с информацией.
 * <p>SCML решает следующие задачи:</p>
 * <ul>
 * <li>Абстрагирование исходных текстов от переименования объектов проекта: 
 * трансляция, на основании идентификатора объекта в метаинформации тэга, генерирует актуальное имя объекта.</li>
 * <li>Идентификация объекта исходных текстах (для проверки, навигации и т.д.).</li>
 * </ul>
 */
public abstract class Scml extends RadixObject {

    public interface ScmlAreaInfo extends RadixProblem.IAnnotation {

        public Scml getScml();

        public int getSourceStartOffset();

        public int getSourceEndOffset();

        public Scml.Item getStartJmlItem();

        public Scml.Item getEndJmlItem();
    }

    protected Scml() {
        super();
    }

    protected Scml(String name) {
        super(name);
    }

    @Override
    public String toString() {
        String text = "";
        for (Item item : getItems()) {
            text += item.toString();
        }
        return text;
    }

    /**
     * Элемент {@link Scml SCML}.
     */
    public static abstract class Item extends RadixObject {

        private CodePrinter.Monitor monitor;

        protected Item() {
            super();
        }

        public Scml getOwnerScml() {
            for (RadixObject owner = getContainer(); owner != null; owner = owner.getContainer()) {
                if (owner instanceof Scml) {
                    return (Scml) owner;
                }
            }
            return null;
        }

        @Override
        @Deprecated
        public boolean setName(String name) {
            throw new RadixObjectError("Attempt to change name of sqml item.", getOwnerScml());
        }

        public void setSourceMonitor(CodePrinter.Monitor monitor) {
            this.monitor = monitor;
        }

        public CodePrinter.Monitor getSourceMonitor() {
            return this.monitor;
        }

        @Override
        public String getName() {
            return getTypeTitle();
        }

        @Override
        protected void onModified() {
            final Scml scml = getOwnerScml();
            if (scml != null) {
                scml.onModified();
            }
        }
    }

    /**
     * Тэг {@link Scml SCML}.
     */
    public static abstract class Tag extends Scml.Item {

        protected Tag() {
            super();
        }

        @Override
        public String toString() {
            return "[" + this.getClass().getSimpleName() + "]";
        }

        @Override
        public String getToolTip() {
            final List<Definition> dependencies = new ArrayList<Definition>();
            collectDependences(dependencies);
            if (dependencies.size() == 1) {
                // approached in most cases
                final RadixObject radixObject = dependencies.get(0);
                return radixObject.getToolTip();
            } else {
                // ignore name
                final StringBuilder sb = new StringBuilder();
                sb.append("<html><b>");
                final String typeTitle = getTypeTitle();
                sb.append(typeTitle);
                sb.append("</b>");

                final RadixObject ownerForQualifiedName = getOwnerForQualifedName();
                if (ownerForQualifiedName != null) {
                    final String location = ownerForQualifiedName.getQualifiedName();
                    sb.append("<br>Location: " + location);
                }

                appendAdditionalToolTip(sb);
                return sb.toString();
            }
        }
    }

    /**
     * Текст {@link Scml SCML}.
     */
    public static class Text extends Scml.Item {

        /**
         * Use {@linkplain Scml.Text.Factory}.
         */
        protected Text() {
            super();
        }

        protected Text(String text) {
            this();
            this.text = text;
        }
        private String text = "";

        public String getText() {
            return text;
        }

        public void setText(String text) {
            if (!Utils.equals(this.text, text)) {
                this.text = text;
                setEditState(EEditState.MODIFIED);
            }
        }

        @Override
        public String toString() {
            return text;
        }

        public static final class Factory {

            private Factory() {
            }

            public static Text newInstance() {
                return new Text();
            }

            public static Text newInstance(String text) {
                return new Text(text);
            }
        }
    }

    private static class ScmlItems extends RadixObjects<Item> {

        public ScmlItems(Scml ownerScml) {
            super(ownerScml);
        }

        public Scml getOwnerScml() {
            return (Scml) getContainer();
        }

        @Override
        protected void onModified() {
            final Scml scml = getOwnerScml();
            if (scml != null) {
                scml.onModified();
            }
        }
    }
    /**
     * Получить список элементов {@link Scml SCML} выражения.
     */
    private final RadixObjects<Item> items = new ScmlItems(this);

    public RadixObjects<Scml.Item> getItems() {
        return items;
    }

    @Override
    public ClipboardSupport<? extends Scml> getClipboardSupport() {
        return null;
    }

    protected void setText(final String code) {
        final Text text;
        if (getItems().size() == 1 && getItems().get(0) instanceof Text) {
            text = (Text) getItems().get(0);
        } else {
            this.getItems().clear();
            text = Text.Factory.newInstance();
            getItems().add(text);
        }
        text.setText(code);
    }

    @Override
    public void visitChildren(IVisitor visitor, VisitorProvider provider) {
        super.visitChildren(visitor, provider);
        getItems().visit(visitor, provider);
    }
}
