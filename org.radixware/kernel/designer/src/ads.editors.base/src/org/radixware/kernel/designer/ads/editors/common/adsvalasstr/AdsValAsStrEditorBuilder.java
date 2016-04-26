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

package org.radixware.kernel.designer.ads.editors.common.adsvalasstr;

import java.util.*;
import org.radixware.kernel.common.defs.ads.AdsValAsStr;
import org.radixware.kernel.common.defs.ads.AdsValAsStr.IValueController;
import org.radixware.kernel.common.defs.ads.type.AdsEnumType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.designer.common.dialogs.components.values.EditorFactory;
import org.radixware.kernel.designer.common.dialogs.components.values.IValueEditorFactory;


final class AdsValAsStrEditorBuilder extends EditorFactory<AdsValAsStrEditor> {


    private static void initMatchers() {

        /*
         * Matcher for JmlEditor
         */
        BUILDERS_CHAIN.registryMatcher(new IBuildMatcher() {

            @Override
            public boolean match(AdsValAsStrEditorBuilder factory, AdsValAsStr.IValueController context, AdsValAsStr currValue) {
                final boolean supportJml = context.isValueTypeAvailable(AdsValAsStr.EValueType.JML),
                    isJmlValue = currValue != null && currValue.typeEquals(AdsValAsStr.EValueType.JML),
                    isJmlType = jmlSupportType.contains(context.getContextType().getTypeId()),
                    isArray = context.getContextType().isArray();

                if (isJmlValue || isJmlType || isArray) {
                    if (isJmlValue && supportJml && BUILDERS_CHAIN.containsBuilder("JML")) {
                        BUILDERS_CHAIN.getBuilder("JML").build(factory, context, currValue);
                        return true;
                    } else if (BUILDERS_CHAIN.containsBuilder(DEFAULT_EDITOR)) {
                        BUILDERS_CHAIN.getBuilder(DEFAULT_EDITOR).build(factory, context, currValue);
                        return true;
                    }
                }
                return false;
            }
        });

        /*
         * Matcher by AdsType.class
         */
        BUILDERS_CHAIN.registryMatcher(new IBuildMatcher() {

            @Override
            public boolean match(AdsValAsStrEditorBuilder factory, AdsValAsStr.IValueController context, AdsValAsStr currValue) {
                final AdsType adsType = context.getContextType().resolve(context.getContextDefinition()).get();

                if (adsType != null && BUILDERS_CHAIN.containsBuilder(adsType.getClass())) {
                    BUILDERS_CHAIN.getBuilder(adsType.getClass()).build(factory, context, currValue);
                    return true;
                }
                return false;
            }
        });

        /*
         * Matcher by EValType
         */
        BUILDERS_CHAIN.registryMatcher(new IBuildMatcher() {

            @Override
            public boolean match(AdsValAsStrEditorBuilder factory, AdsValAsStr.IValueController context, AdsValAsStr currValue) {
                final AdsTypeDeclaration type = context.getContextType();
                if (type != null && !type.isArray() && BUILDERS_CHAIN.containsBuilder(type.getTypeId())) {
                    BUILDERS_CHAIN.getBuilder(type.getTypeId()).build(factory, context, currValue);
                    return true;
                }
                return false;
            }
        });

        /*
         * Matcher for StringEditor
         */
        BUILDERS_CHAIN.registryMatcher(new IBuildMatcher() {

            @Override
            public boolean match(AdsValAsStrEditorBuilder factory, AdsValAsStr.IValueController context, AdsValAsStr currValue) {
                final EValType valType = context.getContextType().getTypeId();

                if (valAsStrSupportType.contains(valType)) {
                    return BUILDERS_CHAIN.getBuilder(EValType.STR).build(factory, context, currValue);
                }
                return false;
            }
        });

        /*
         * Matcher by default (UNDEFINED)
         */
        BUILDERS_CHAIN.registryMatcher(new IBuildMatcher() {

            @Override
            public boolean match(AdsValAsStrEditorBuilder factory, AdsValAsStr.IValueController context, AdsValAsStr currValue) {
                return BUILDERS_CHAIN.getBuilder(DEFAULT_EDITOR).build(factory, context, currValue);
            }
        });
    }

    private static void initBuilders() {

        BUILDERS_CHAIN.registryBuilder(DEFAULT_EDITOR, new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {
                final DefaultEditorComponent editor = new DefaultEditorComponent();

                factory.setEditor(editor);

                if (context != null) {
                    editor.open(context, currValue);
                    factory.addDefaultFeatures(context, currValue);
                }
                return true;
            }
        });

        BUILDERS_CHAIN.registryBuilder(AdsEnumType.class, new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {
                final AdsEnumDefEditorComponent editor = new AdsEnumDefEditorComponent();

                editor.open(context, currValue);
                factory.setEditor(editor);

                factory.addDefaultFeatures(context, currValue);
                return true;
            }
        });

        BUILDERS_CHAIN.registryBuilder(EValType.BOOL, new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {
                final BoolValueEditorComponent editor = new BoolValueEditorComponent();

                editor.open(context, currValue);
                factory.setEditor(editor);

                factory.addDefaultFeatures(context, currValue);
                return true;
            }
        });

        final IBuilder stringBuilder = new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {

                final StringEditorComponent editor = new StringEditorComponent();

                editor.open(context, currValue);
                factory.setEditor(editor);

                factory.addFeature(AdsValAsStrFutureFactory.getPopupEditorFeature());
                factory.addDefaultFeatures(context, currValue);
                return true;
            }
        };

        BUILDERS_CHAIN.registryBuilder(EValType.STR, stringBuilder);
        BUILDERS_CHAIN.registryBuilder(EValType.XML, stringBuilder);

        BUILDERS_CHAIN.registryBuilder(EValType.DATE_TIME, new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {

                final DateTimeEditorComponent editor = new DateTimeEditorComponent();

                editor.open(context, currValue);
                factory.setEditor(editor);

                factory.addDefaultFeatures(context, currValue);
                return true;
            }
        });

        final IBuilder jmlBuilder = new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {
                final JmlValueEditorComponent editor = new JmlValueEditorComponent();

                editor.open(context, currValue);
                factory.setEditor(editor);

                factory.addDefaultFeatures(context, currValue);
                return true;
            }
        };

        final IBuilder valAsStrBuilder = new IBuilder() {

            @Override
            public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {
                final ValAsStrEditorComponent editor = new ValAsStrEditorComponent();

                editor.open(context, currValue);
                factory.setEditor(editor);

                factory.addDefaultFeatures(context, currValue);
                return true;
            }
        };

        BUILDERS_CHAIN.registryBuilder("JML", jmlBuilder);
        BUILDERS_CHAIN.registryBuilder("VAL_AS_STR", valAsStrBuilder);

        BUILDERS_CHAIN.registryBuilder(EValType.USER_CLASS, jmlBuilder);
        BUILDERS_CHAIN.registryBuilder(EValType.JAVA_CLASS, jmlBuilder);
        BUILDERS_CHAIN.registryBuilder(EValType.JAVA_TYPE, jmlBuilder);

        BUILDERS_CHAIN.registryBuilder(EValType.INT, valAsStrBuilder);
        BUILDERS_CHAIN.registryBuilder(EValType.CHAR, valAsStrBuilder);
        BUILDERS_CHAIN.registryBuilder(EValType.NUM, valAsStrBuilder);
    }

    private final static Object DEFAULT_EDITOR = new Object();
    private final static BuildersChain BUILDERS_CHAIN = new BuildersChain();

    private static final Set<EValType> jmlSupportType = EnumSet.of(
        EValType.USER_CLASS,
        EValType.JAVA_TYPE,
        EValType.JAVA_CLASS,
        EValType.OBJECT,
        EValType.NATIVE_DB_TYPE,
        EValType.PARENT_REF);

    private static final Set<EValType> valAsStrSupportType = EnumSet.of(
        EValType.INT,
        EValType.NUM,
        EValType.STR,
        EValType.CHAR,
        EValType.BOOL,
        EValType.DATE_TIME,
        EValType.BIN,
        EValType.BLOB,
        EValType.CLOB,
        EValType.ARR_BIN,
        EValType.ARR_BLOB,
        EValType.ARR_BOOL,
        EValType.ARR_CLOB,
        EValType.ARR_DATE_TIME,
        EValType.ARR_INT,
        EValType.ARR_NUM,
        EValType.ARR_REF,
        EValType.ARR_STR,
        EValType.ARR_CHAR);

    static {

        initBuilders();

        initMatchers();
    }

    private static final class BuildersChain {

        private final Map<Object, IBuilder> builders;
        private final List<IBuildMatcher> matchers;

        BuildersChain() {
            matchers = new LinkedList<>();
            builders = new HashMap<>();
        }

        private IBuilder getBuilder(Object key) {
            return builders.get(key);
        }

        private void registryBuilder(Object key, IBuilder builder) {
            builders.put(key, builder);
        }

        private boolean containsBuilder(Object key) {
            return builders.containsKey(key);
        }

        private void registryMatcher(IBuildMatcher matcher) {
            matchers.add(matcher);
        }

        public boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue) {

            assert context != null && factory != null;

            if (context != null && factory != null) {
                for (final IBuildMatcher matcher : matchers) {
                    if (matcher.match(factory, context, currValue)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private interface IBuildMatcher {
        boolean match(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue);
    }

    private interface IBuilder {
        boolean build(AdsValAsStrEditorBuilder factory, IValueController context, AdsValAsStr currValue);
    }



    public static IValueEditorFactory<AdsValAsStrEditor> createInstance(AdsValAsStrEditor editor, IValueController context, AdsValAsStr currValue) {
        return new AdsValAsStrEditorBuilder(editor, context, currValue);
    }

    private IValueController context;
    private AdsValAsStr currValue;

    private AdsValAsStrEditorBuilder(AdsValAsStrEditor editor, IValueController context, AdsValAsStr currValue) {
        super(editor);

        this.context = context;
        this.currValue = currValue;
    }

    @Override
    public void buildDefaultEditor() {
        BUILDERS_CHAIN.getBuilder(DEFAULT_EDITOR).build(this, null, null);
        componentSequence.build();
    }

    @Override
    protected boolean assemblyImpl() {
        return BUILDERS_CHAIN.build(this, context, currValue);
    }


    private void addJmlSwitchFeature(IValueController context, AdsValAsStr currValue) {
        final boolean jmlSupport = context != null && context.isValueTypeAvailable(AdsValAsStr.EValueType.JML),
            isJmlValue = currValue != null && currValue.typeEquals(AdsValAsStr.EValueType.JML);

        if (jmlSupport && !isJmlValue) {
            addFeature(AdsValAsStrFutureFactory.getJmlSwitchFeature());
        }
    }

    private void addValAsStrSwitchFeature(IValueController context, AdsValAsStr currValue) {

        boolean valAsStrSupport = false;

        if (context != null && context.getContextType() != null) {
            EValType valType = context.getContextType().getTypeId();
            valAsStrSupport = valAsStrSupportType.contains(valType) && context.isValueTypeAvailable(AdsValAsStr.EValueType.VAL_AS_STR);
        }

        final boolean isValAsStrValue = currValue == null
            || currValue.typeEquals(AdsValAsStr.EValueType.VAL_AS_STR)
            || currValue.typeEquals(AdsValAsStr.EValueType.NULL);

        if (valAsStrSupport && !isValAsStrValue) {
            addFeature(AdsValAsStrFutureFactory.getValAsStrSwitchFeature());
        }
    }

    private void addNullValueFeature(IValueController context, AdsValAsStr currValue) {
        final boolean nullSupport = context != null && context.isValueTypeAvailable(AdsValAsStr.EValueType.NULL);

        if (nullSupport) {
            addFeature(AdsValAsStrFutureFactory.getNullValueFeature());
        }
    }

    private void addDefaultFeatures(IValueController context, AdsValAsStr currValue) {
        addValAsStrSwitchFeature(context, currValue);
        addJmlSwitchFeature(context, currValue);
        addNullValueFeature(context, currValue);
    }
}