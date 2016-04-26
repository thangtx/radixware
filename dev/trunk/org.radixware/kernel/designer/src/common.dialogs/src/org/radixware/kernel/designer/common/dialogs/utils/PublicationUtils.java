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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.radixware.kernel.common.defs.ExtendableDefinitions;
import org.radixware.kernel.common.defs.IFilter;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.ITransparency;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsDynamicClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.*;
import org.radixware.kernel.common.defs.ads.platform.PlatformLib;
import org.radixware.kernel.common.defs.ads.platform.RadixPlatformClass;
import org.radixware.kernel.common.defs.ads.type.AdsAccessFlags;
import org.radixware.kernel.common.defs.ads.type.AdsTransparence;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.utils.Utils;


public final class PublicationUtils {

    private PublicationUtils() {
    }

    public static AdsClassDef createTransparentClass(RadixPlatformClass platformClass, PlatformLib lib, ERuntimeEnvironmentType environmentType) {

        if (!Utils.isNotNull(platformClass, lib, environmentType)) {
            return null;
        }

        if (platformClass != null) {
            if (platformClass.isInterface()) {
                return AdsInterfaceClassDef.Factory.newInstance(environmentType);
            } else if (platformClass.isException()) {
                return AdsExceptionClassDef.Factory.newInstance(environmentType);
            } else {
                return AdsDynamicClassDef.Factory.newInstance(environmentType);
            }
        }
        return null;
    }

    public static void setupTransparentClass(AdsClassDef classDef, RadixPlatformClass platformClass,
            String className, boolean isExtendable, String platformClassName) {

        classDef.setName(className);
        if (platformClass != null) {
            if (platformClass.getDeclaration().isGeneric()) {
                final AdsTypeDeclaration.TypeArguments args = classDef.getTypeArguments();
                final List<AdsTypeDeclaration.TypeArgument> plArgs = platformClass.getDeclaration().getGenericArguments().getArgumentList();
                for (final AdsTypeDeclaration.TypeArgument a : plArgs) {
                    args.add(a);
                }
            }

            classDef.getAccessFlags().setAbstract(platformClass.isAbstract());
            classDef.getAccessFlags().setFinal(platformClass.isFinal());

            if (classDef.getTransparence() != null) {
                classDef.getTransparence().setPublishedName(platformClassName);
                classDef.getTransparence().setIsExtendable(isExtendable);
            }
        }
    }

    public static void setupTransparentFieldDef(AdsTransparentPropertyDef property, RadixPlatformClass.Field published) {
        final String name = published.getName();
        property.setName(published.getName());

        property.getTransparence().setPublishedName(name);
        property.getTransparence().setIsExtendable(false);

        final AdsAccessFlags accessFlags = property.getAccessFlags();

        property.setAccessMode(published.getAccess());
        property.setPublished(true);

        accessFlags.setFinal(published.isFinal());
        accessFlags.setStatic(published.isStatic());

        property.getValue().setType(published.getValueType());
    }

    public static void setupTransparentMethodDef(AdsTransparentMethodDef object, RadixPlatformClass.Method published, AdsClassDef owner, boolean saveNames) {

        object.setName(published.isConstructor() ? owner.getName() : published.getName());

        object.getTransparence().setPublishedName(String.valueOf(published.getRadixSignature()));
        object.getTransparence().setIsExtendable(false);


        AdsAccessFlags flags = object.getProfile().getAccessFlags();
        if (published.isAbstract()) {
            flags.setAbstract(true);
        } else if (published.isFinal()) {
            flags.setFinal(true);
        }
        if (published.isStatic()) {
            flags.setStatic(true);
        }

        object.setAccessMode(published.getAccess());
        object.setPublished(true);

        if (!object.isConstructor()) {
            object.getProfile().getReturnValue().setType(published.getReturnType());
        }

        object.getProfile().getThrowsList().clear();

        AdsTypeDeclaration[] throwslist = published.getExceptions();
        if (throwslist != null) {
            for (int i = 0; i <= throwslist.length - 1; i++) {
                object.getProfile().getThrowsList().add(AdsMethodThrowsList.ThrowsListItem.Factory.newInstance(throwslist[i]));
            }
        }


        AdsTypeDeclaration[] params = published.getParameterTypes();
        String[] names = published.getParameterName();
        if (!saveNames) {
            object.getProfile().getParametersList().clear();
            if (params != null) {
                for (int i = 0; i <= params.length - 1; i++) {
                    if (names == null || names.length == 0){
                        object.getProfile().getParametersList().add(MethodParameter.Factory.newInstance("arg" + i, params[i]));
                    } else {
                        object.getProfile().getParametersList().add(MethodParameter.Factory.newInstance(names[i], params[i]));
                    }
                }
                if (published.isVarargs()) {
                    final MethodParameter varargParam = object.getProfile().getParametersList().get(params.length - 1);
                    varargParam.setVariable(true);
                }
            }
        } else {
            AdsMethodParameters adsParams = object.getProfile().getParametersList();
            if (adsParams.size() > 0) {
                List<MethodParameter> newParameters = new ArrayList<>();
                if (params != null) {
                    int newParamsCount = 0;
                    for (int i = 0, size = params.length - 1; i <= size; i++) {
                        boolean contains = containsType(object, adsParams.list(), params[i]);
                        if (contains) {
                            if (i < adsParams.size()) {
                                newParameters.add(MethodParameter.Factory.newInstance(adsParams.get(i).getName(), params[i]));
                            } else {
                                if (names == null || names.length == 0){
                                    newParameters.add(MethodParameter.Factory.newInstance("arg" + newParamsCount, params[i]));
                                    newParamsCount++;
                                } else {
                                    newParameters.add(MethodParameter.Factory.newInstance(names[i], params[i]));
                                }
                            }
                        } else {
                            if (names == null || names.length == 0){
                                newParameters.add(MethodParameter.Factory.newInstance("arg" + newParamsCount, params[i]));
                                newParamsCount++;
                            } else {
                                newParameters.add(MethodParameter.Factory.newInstance(names[i], params[i]));
                            }
                        }
                    }
                }
                adsParams.clear();
                for (MethodParameter mp : newParameters) {
                    adsParams.add(mp);
                }
                if (published.isVarargs()) {
                    final MethodParameter varargParam = adsParams.get(params.length - 1);
                    varargParam.setVariable(true);
                }
            }
        }
    }

    private static boolean containsType(AdsTransparentMethodDef context, List<MethodParameter> params, AdsTypeDeclaration type) {
        for (final MethodParameter mp : params) {
            final AdsTypeDeclaration mpType = mp.getType();
            if (mpType.equalsTo(context, type)) {
                return true;
            } else {
                if (mpType.isGeneric() && type.isGeneric()) {
                    final AdsType mpadsType = mpType.resolve(context).get();
                    final AdsType tadsType = type.resolve(context).get();
                    if (Utils.equals(mpadsType, tadsType)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Gets collection of methods, which can be published
     *
     * @param platformClass instance of {@link RadixPlatformClass} for
     * publishing, can not be null
     * @return collection of methods, which can be published
     */
    public static Collection<RadixPlatformClass.Method> getAllMethodsForPublishing(RadixPlatformClass platformClass) {
        checkPlatformClass(platformClass);

        final Collection<RadixPlatformClass.Method> methods = new ArrayList<>();

        for (final RadixPlatformClass.Method method : platformClass.getMethods()) {
            if (method.getAccess() != EAccess.PRIVATE && method.getAccess() != EAccess.DEFAULT) {
                methods.add(method);
            }
        }
        return methods;
    }

    /**
     * Gets collection of fields, which can be published
     *
     * @param platformClass instance of {@link RadixPlatformClass} for
     * publishing, can not be null
     * @return collection of fields, which can be published
     */
    public static Collection<RadixPlatformClass.Field> getAllFieldsForPublishing(RadixPlatformClass platformClass) {
        checkPlatformClass(platformClass);

        final Collection<RadixPlatformClass.Field> fields = new ArrayList<>();

        for (final RadixPlatformClass.Field field : platformClass.getFields()) {
            if (field.getAccess() != EAccess.PRIVATE && field.getAccess() != EAccess.DEFAULT && field.isStatic()) {
                fields.add(field);
            }
        }

        return fields;
    }

    public static Collection<RadixPlatformClass.Method> getMethods(RadixPlatformClass platformClass, IItemFilter<RadixPlatformClass.Method> filter) {
        checkPlatformClass(platformClass);

        final Collection<RadixPlatformClass.Method> allMethodsForPublishing = getAllMethodsForPublishing(platformClass);
        final Collection<RadixPlatformClass.Method> methods = new ArrayList<>();

        for (final RadixPlatformClass.Method method : allMethodsForPublishing) {
            if (filter.accept(method)) {
                methods.add(method);
            }
        }

        return methods;
    }

    public static Collection<RadixPlatformClass.Field> getFields(RadixPlatformClass platformClass, IItemFilter<RadixPlatformClass.Field> filter) {
        checkPlatformClass(platformClass);

        final Collection<RadixPlatformClass.Field> allFieldsForPublishing = getAllFieldsForPublishing(platformClass);
        final Collection<RadixPlatformClass.Field> fields = new ArrayList<>();

        for (final RadixPlatformClass.Field field : allFieldsForPublishing) {
            if (filter.accept(field)) {
                fields.add(field);
            }
        }

        return fields;
    }

    private static void checkPlatformClass(RadixPlatformClass platformClass) {
        assert platformClass != null : "RadixPlatformClass is null";
    }

    public static class TransparencyFilter<T extends RadixObject> implements IFilter<T> {

        @Override
        public boolean isTarget(T radixObject) {
            if (radixObject instanceof ITransparency) {
                AdsTransparence transparence = ((ITransparency) radixObject).getTransparence();
                return transparence != null && transparence.isTransparent();
            }
            return false;
        }
    }

    public static interface IItemFilter<T> {

        boolean accept(T object);
    }

    public static class TransparentFieldFilter implements IItemFilter<RadixPlatformClass.Field> {

        private final AdsClassDef adsclass;

        public TransparentFieldFilter(AdsClassDef adsclass) {
            this.adsclass = adsclass;
        }

        @Override
        public boolean accept(RadixPlatformClass.Field object) {
            return adsclass.getProperties().findBySignature(object.getRadixSignature(), ExtendableDefinitions.EScope.LOCAL) instanceof AdsTransparentPropertyDef;
        }
    }

    public static class TransparentMethodFilter implements IItemFilter<RadixPlatformClass.Method> {

        private final AdsClassDef adsclass;

        public TransparentMethodFilter(AdsClassDef adsclass) {
            this.adsclass = adsclass;
        }

        @Override
        public boolean accept(RadixPlatformClass.Method object) {
            return adsclass.getMethods().findBySignature(object.getRadixSignature(), ExtendableDefinitions.EScope.LOCAL) instanceof AdsTransparentMethodDef;
        }
    }

    public static final class UnpublishedFieldFilter extends TransparentFieldFilter {

        public UnpublishedFieldFilter(AdsClassDef adsclass) {
            super(adsclass);
        }

        @Override
        public boolean accept(RadixPlatformClass.Field object) {
            return !super.accept(object);
        }
    }

    public static class UnpublishedMethodFilter extends TransparentMethodFilter {

        public UnpublishedMethodFilter(AdsClassDef adsclass) {
            super(adsclass);
        }

        @Override
        public boolean accept(RadixPlatformClass.Method object) {
            return !super.accept(object);
        }
    }
    
    public static final class UnpublishedNotOverridenMethodFilter extends UnpublishedMethodFilter {

        public UnpublishedNotOverridenMethodFilter(AdsClassDef adsclass) {
            super(adsclass);
        }

        @Override
        public boolean accept(RadixPlatformClass.Method object) {
            return super.accept(object)? object.isOverriden() : false;
        }
    }
}
