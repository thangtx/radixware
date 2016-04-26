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

package org.radixware.kernel.common.sqml;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.radixware.kernel.common.defs.ClipboardSupport;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.RadixObjectIcon;
import org.radixware.kernel.common.enums.ENamingPolicy;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.scml.CommentsAnalizer;
import org.radixware.kernel.common.scml.Scml;
import org.radixware.kernel.common.sqml.tags.TaskTag;

/**
 * <p>SQML (Structured Query Markup Language) - SQL extension to {@link Scml Scml}.</p>
 * <p>Owners in DDS:</p>
 * <ul>
 * <li>	DdsModel.BeginScript</li>
 * <li>	DdsModel.EndScript</li>
 * <li>	DdsFunctionDef.Body</li>
 * <li>	DdsCustomText.Text</li>
 * <li>	DdsColumnDef.Expression</li>
 * <li>	DdsTriggerDef.Body</li>
 * <li>	DdsViewDef.Query</li>
 * </ul>
 * <p>Owners in ADS:</p>
 * <ul>
 * <li>	AdsSqlClassDef.Source</li>
 * <li>	AdsFilterDef.Hint</li>
 * <li>	AdsFilterDef.Condition</li>
 * <li>	AdsClassDef.EnabledSortings.Hint</li>
 * <li>	AdsColorSchemeDef.Coloring.Condition</li>
 * <li>	AdsSelectorPresentationDef.DefaultHint</li>
 * <li>	AdsSelectorPresentationDef.ConditionFrom</li>
 * <li>	AdsSelectorPresentationDef.ConditionWhere</li>
 * <li>	AdsExplorerItemDef.ConditionFrom</li>
 * <li>	AdsExplorerItemDef.ConditionWhere</li>
 * <li>	AdsPropertyDef.ParentSelectFrom</li>
 * <li>	AdsPropertyDef.ParentSelectWhere</li>
 * <li>	AdsPropertyDef.Expression</li>
 * </ul>

 */
public class Sqml extends Scml {

    protected Sqml() {
        super();
    }

    protected Sqml(RadixObject context) {
        super();
        setContainer(context);
    }

    @Override
    public ENamingPolicy getNamingPolicy() {
        return ENamingPolicy.CONST;
    }

    @Override
    public String getName() {
        return "Sqml"; // for debug
    }

    public static abstract class Tag extends Scml.Tag {

        protected Tag() {
            super();
        }

        /**
         * Get owner SQML
         * @return SQML or null if tag is not in SQML.
         */
        public Sqml getOwnerSqml() {
            return (Sqml) getOwnerScml();
        }

        /**
         * Get SQML environment
         * @return SQML environment or null if tag is not in SQML or SQML has no environment.
         */
        public ISqmlEnvironment getEnvironment() {
            Sqml sqml = getOwnerSqml();
            if (sqml != null) {
                return sqml.getEnvironment();
            } else {
                return null;
            }
        }
    }

    /**
     * Store SQML into XML Beans object.
     */
    public void appendTo(final org.radixware.schemas.xscml.Sqml xSqml) {
        SqmlWriter.write(this, xSqml);
    }

    /**
     * Restore SQML from XML Beans object.
     * @throws IllegalStateException
     */
    public void loadFrom(final org.radixware.schemas.xscml.Sqml xSqml) {
        SqmlLoader.load(this, xSqml);
    }

    /**
     * Append SQML items from XML Beans object.
     * @throws IllegalStateException
     */
    public void appendFrom(final org.radixware.schemas.xscml.Sqml xSqml) {
        SqmlLoader.append(this, xSqml);
    }

    @Override
    public RadixIcon getIcon() {
        return RadixObjectIcon.SQML;
    }
    
    private volatile ISqmlEnvironment environment = null;
    private final ThreadLocal<ISqmlEnvironment> threadLocalEnvironment = new ThreadLocal<>();
    
    public final void setThreadLocalEnvironment(final ISqmlEnvironment environment) {
        threadLocalEnvironment.set(environment);
    }

    public final ISqmlEnvironment getEnvironment() {
        final ISqmlEnvironment localEnv = threadLocalEnvironment.get();
        if (localEnv != null) {
            return localEnv;
        }
        return environment;
    }

    public void setEnvironment(final ISqmlEnvironment environment) {
        this.environment = environment;
    }

    private class SqmlClipboardSupport extends ClipboardSupport<Sqml> {

        public SqmlClipboardSupport() {
            super(Sqml.this);
        }

        @Override
        protected XmlObject copyToXml() {
            org.radixware.schemas.xscml.Sqml xSqml = org.radixware.schemas.xscml.Sqml.Factory.newInstance();
            Sqml.this.appendTo(xSqml);
            return xSqml;
        }

        @Override
        protected Sqml loadFrom(XmlObject xmlObject) {
            org.radixware.schemas.xscml.Sqml xSqml = (org.radixware.schemas.xscml.Sqml) xmlObject;
            Sqml sqml = new Sqml();
            sqml.loadFrom(xSqml);
            return sqml;
        }
    }

    @Override
    public ClipboardSupport<? extends Sqml> getClipboardSupport() {
        return new SqmlClipboardSupport();
    }

    public void setSql(final String sql) {
        setText(sql);
    }

    public static final class Factory {

        private Factory() {
        }

        public static Sqml newInstance() {
            return new Sqml();
        }

        public static Sqml newInstance(RadixObject context) {
            return new Sqml(context);
        }
    }

    @Override
    public boolean isReadOnly() {
        // overriden for disable editing of PL/SQL Object calculated SQML.
        if (super.isReadOnly()) {
            return true;
        }
        if (getContainer() == null) {
            return true;
        }
        return false;
    }

    /**
     * @return true if SQML contains something except comments, spaces and TaskTag
     */
    public boolean hasImportant() {
        if (getItems().isEmpty()) {
            return false;
        }

        final CommentsAnalizer commentsAnalizer = CommentsAnalizer.Factory.newSqlCommentsAnalizer();
        for (Scml.Item item : getItems()) {
            if (item instanceof Scml.Text) {
                final String text = ((Scml.Text) item).getText();
                for (int i = 0, len = text.length(); i < len; i++) {
                    final char c = text.charAt(i);
                    commentsAnalizer.process(c);
                    if (Character.isLetterOrDigit(c) && !commentsAnalizer.isInComment()) {
                        return true;
                    }
                    if (commentsAnalizer.isInString()) {
                        return true;
                    }
                }
            } else if (!(item instanceof TaskTag) && !commentsAnalizer.isInComment()) {
                return true;
            }
        }

        return false;
    }
}
