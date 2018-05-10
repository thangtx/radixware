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

package org.radixware.kernel.common.defs.ads.type;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.Set;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsInterfaceClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsWrapperClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.form.AdsFormHandlerClassDef;
import org.radixware.kernel.common.defs.ads.clazz.sql.report.AdsUserReportClassDef;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.type.interfacing.RadixClassInterfacing;
import static org.radixware.kernel.common.enums.EClassType.DYNAMIC;
import static org.radixware.kernel.common.enums.EClassType.INTERFACE;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.scml.IHumanReadablePrinter;
import org.radixware.kernel.common.types.Id;


public class AdsClassType extends AdsDefinitionType {

    protected static class EntityGroupModelJavaSourceSupport extends UserClassJavaSourceSupport {

        public EntityGroupModelJavaSourceSupport(AdsClassType type) {
            super(type);
        }

        @Override
        public char[] getLocalTypeName(JavaSourceSupport.UsagePurpose env, boolean isHumanReadable) {
            if (getSourceClass() == null) {
                return "???".toCharArray();
            }
            return (getSourceClass().getId().toString() + "." + EntityGroupModelType.TYPE_SUFFIX).toCharArray();
        }
    }

    protected static class UserClassJavaSourceSupport extends TypeJavaSourceSupport {

        private final AdsClassType type;

        UserClassJavaSourceSupport(AdsClassType type) {
            super(type);
            this.type = type;
        }

        protected AdsClassDef getSourceClass() {
            return type.getSource();
        }

        @Override
        public char[][] getPackageNameComponents(JavaSourceSupport.UsagePurpose purpose, boolean isHumanReadable) {
            if (getSourceClass() == null) {
                return new char[][]{"???".toCharArray()};
            } else {
                if (getSourceClass() instanceof AdsWrapperClassDef) {
                    return null;
                }
                ERuntimeEnvironmentType ownEnv = getSourceClass().getUsageEnvironment();
                switch (getSourceClass().getClassDefType()) {
                    case DYNAMIC:
                    case INTERFACE:
                        final AdsClassDef enclosingClass = getSourceClass().getTopLevelEnclosingClass();
                        return JavaSourceSupport.getPackageNameComponents(enclosingClass, isHumanReadable, ownEnv == ERuntimeEnvironmentType.COMMON || (ownEnv == ERuntimeEnvironmentType.COMMON_CLIENT && !enclosingClass.isDual()) ? JavaSourceSupport.UsagePurpose.getPurpose(ownEnv, purpose.getCodeType()) : purpose);

                }
                return JavaSourceSupport.getPackageNameComponents(getSourceClass(), isHumanReadable, ownEnv == ERuntimeEnvironmentType.COMMON ? JavaSourceSupport.UsagePurpose.getPurpose(ownEnv, purpose.getCodeType()) : purpose);
            }
        }

        @Override
        public char[] getLocalTypeName(JavaSourceSupport.UsagePurpose env, boolean isHumanReadable) {
            if (getSourceClass() == null) {
                return "???".toCharArray();
            }
            return getSourceClass().getRuntimeLocalClassName(isHumanReadable).toCharArray();
        }

        @Override
        public char[] getQualifiedTypeName(JavaSourceSupport.UsagePurpose env, boolean isHumanReadable) {
            final AdsClassDef src = getSourceClass();
            if (src == null) {
                return super.getQualifiedTypeName(env, isHumanReadable);
            } else {
                final AdsTransparence transparence = src.getTransparence();
                if (transparence != null && transparence.isTransparent() && !transparence.isExtendable()) {
                    return src.getTransparence().getPublishedName().toCharArray();
                } else {
//                    if (isHumanReadable) {
//                        return src.getQualifiedName().toCharArray();
//                    }
                    return super.getQualifiedTypeName(env, isHumanReadable);
                }
            }
        }
    }

    public static class InterfaceType extends AdsClassType {

        private InterfaceType(AdsClassDef clazz) {
            super(clazz);
        }
    }

    public static class EntityGroupModelType extends AdsClassType {

        private static final String name_suffix = ":DefaultGroupModel";
        public static final String TYPE_SUFFIX = "DefaultGroupModel";

        public EntityGroupModelType(AdsClassDef clazz) {
            super(clazz);
        }

        @Override
        public AdsEntityClassDef getSource() {
            return (AdsEntityClassDef) source;
        }

        @Override
        public String getName() {
            return super.getName() + name_suffix;
        }

        @Override
        public String getQualifiedName(RadixObject context) {
            return super.getQualifiedName(context) + name_suffix;
        }

        @Override
        protected Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
            ERuntimeEnvironmentType env = getSource().getClientEnvironment();
            if (env == ERuntimeEnvironmentType.COMMON_CLIENT) {
                return EnumSet.of(ERuntimeEnvironmentType.COMMON_CLIENT);
            } else {
                return EnumSet.of(env);
            }

        }

        @Override
        public TypeJavaSourceSupport getJavaSourceSupport() {
            return new EntityGroupModelJavaSourceSupport(this);
        }
    }

    public static class FormHandlerType extends AdsClassType {

        private FormHandlerType(AdsClassDef clazz) {
            super(clazz);
        }

        @Override
        public AdsFormHandlerClassDef getSource() {
            return (AdsFormHandlerClassDef) source;
        }
    }

    public static class EntityObjectType extends AdsClassType {

        private Id entityId;

        protected EntityObjectType(AdsEntityObjectClassDef clazz) {
            super(clazz);
            this.entityId = clazz == null ? null : clazz.getEntityId();

        }

        protected EntityObjectType(Id entityId) {
            super(null);
            this.entityId = entityId;
        }

        @Override
        public AdsEntityObjectClassDef getSource() {
            return (AdsEntityObjectClassDef) source;
        }

        public Id getSourceEntityId() {
            return entityId;
        }
    }

    public static class Factory {

        private Factory() {
            super();
        }

        public static final AdsClassType newInstance(final AdsClassDef clazz) {
            if (clazz instanceof AdsInterfaceClassDef) {
                return new InterfaceType(clazz);
            } else if (clazz instanceof AdsFormHandlerClassDef) {
                return new FormHandlerType(clazz);
            } else if (clazz instanceof AdsEntityObjectClassDef) {
                return new EntityObjectType((AdsEntityObjectClassDef) clazz);
            } else {
                return new AdsClassType(clazz);
            }
        }
    }

    @Override
    public AdsClassDef getSource() {
        return (AdsClassDef) source;
    }

    protected AdsClassType(AdsClassDef clazz) {
        super(clazz);
    }

    @Override
    public String getName() {
        return getName(null, false);
    }

    private String getName(RadixObject context, boolean qualified) {
        if (source == null) {
            return "<Undefined Type>";
        } else {
            StringBuilder builder = new StringBuilder(100);
            builder.append(qualified ? source.getQualifiedName(context) : source.getName());
            return builder.toString();
        }
    }

    @Override
    public String getQualifiedName(RadixObject context) {
        return getName(context, true);
    }

    @Override
    public TypeJavaSourceSupport getJavaSourceSupport() {
        return new UserClassJavaSourceSupport(this);
    }

    @Override
    protected void check(RadixObject referenceContext, ERuntimeEnvironmentType env, IProblemHandler problemHandler) {
        super.check(referenceContext, env, problemHandler);
        if (source != null) {
            Set<ERuntimeEnvironmentType> se = getTypeUsageEnvironments();
            switch (env) {
                case EXPLORER:
                    if (!se.contains(ERuntimeEnvironmentType.EXPLORER) && !se.contains(ERuntimeEnvironmentType.COMMON_CLIENT) && !se.contains(ERuntimeEnvironmentType.COMMON)) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to class {0} is not allowed from explorer context", getSource().getQualifiedName()));
                    }
                    break;
                case WEB:
                    if (!se.contains(ERuntimeEnvironmentType.WEB) && !se.contains(ERuntimeEnvironmentType.COMMON_CLIENT) && !se.contains(ERuntimeEnvironmentType.COMMON)) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to class {0} is not allowed from web context", getSource().getQualifiedName()));
                    }
                    break;
                case COMMON_CLIENT:
                    if (!se.contains(ERuntimeEnvironmentType.COMMON_CLIENT) && !se.contains(ERuntimeEnvironmentType.COMMON)) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to class {0} is not allowed from client-common context", getSource().getQualifiedName()));
                    }
                    if (getSource().isDual()) {
                        AdsClassDef contextOwner = getContextOwnerClass(referenceContext);
                        if (contextOwner != null && !contextOwner.isDual()) {
                            error(referenceContext, problemHandler, MessageFormat.format("Reference to dual class {0} is not allowed from client-common context", getSource().getQualifiedName()));
                        }
                    }
                    break;
                case SERVER:
                    if (!se.contains(ERuntimeEnvironmentType.SERVER) && !se.contains(ERuntimeEnvironmentType.COMMON)) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to client side class {0} is not allowed from server side context", getSource().getQualifiedName()));
                    }

                    break;
                case COMMON:
                    if (!se.contains(ERuntimeEnvironmentType.COMMON)) {
                        error(referenceContext, problemHandler, MessageFormat.format("Reference to non common designed class {0} is not allowed from common context", getSource().getQualifiedName()));
                    }
                    break;
            }
        }
        if (source instanceof AdsUserReportClassDef) {
            error(referenceContext, problemHandler, "User-Defined report " + source.getQualifiedName() + " can not be used as type reference");
        }
    }

    private AdsClassDef getContextOwnerClass(RadixObject context) {
        while (context != null) {
            if (context instanceof AdsClassDef) {
                return (AdsClassDef) context;
            }
            context = context.getContainer();
        }
        return null;
    }

    protected Set<ERuntimeEnvironmentType> getTypeUsageEnvironments() {
        return getSource().getTypeUsageEnvironments();
    }

    @Override
    public boolean isSubclassOf(AdsType type) {
        if (type instanceof AdsClassType) {
            if (source == null) {
                return false;
            }
            AdsClassType act = (AdsClassType) type;
            if (act.source == null) {
                return false;
            }
            return new RadixClassInterfacing(act.getSource()).isSuperFor(getSource());
        } else {
            return false;
        }
    }
}
