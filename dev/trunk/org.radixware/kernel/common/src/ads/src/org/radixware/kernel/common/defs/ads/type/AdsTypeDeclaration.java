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

import java.util.*;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ast.Wildcard;
import org.eclipse.jdt.internal.compiler.lookup.ArrayBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.WildcardBinding;
import org.radixware.kernel.common.check.IProblemHandler;
import org.radixware.kernel.common.check.RadixProblem;
import org.radixware.kernel.common.defs.*;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityGroupClassDef;
import org.radixware.kernel.common.defs.ads.clazz.entity.AdsEntityObjectClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsCommandHandlerMethodDef;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.enumeration.AdsEnumDef;
import org.radixware.kernel.common.defs.ads.module.AdsPath;
import org.radixware.kernel.common.defs.ads.platform.IPlatformClassPublisher;
import org.radixware.kernel.common.defs.ads.src.IJavaSource;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport.UsagePurpose;
import org.radixware.kernel.common.defs.ads.ui.AdsAbstractUIDef;
import org.radixware.kernel.common.defs.ads.xml.IXmlDefinition;
import org.radixware.kernel.common.defs.dds.DdsTableDef;
import org.radixware.kernel.common.enums.EClassType;
import org.radixware.kernel.common.enums.EDefinitionIdPrefix;
import org.radixware.kernel.common.enums.EValType;
import org.radixware.kernel.common.exceptions.NoConstItemWithSuchNameError;
import org.radixware.kernel.common.exceptions.RadixObjectError;
import org.radixware.kernel.common.repository.Layer;
import org.radixware.kernel.common.repository.Segment;
import org.radixware.kernel.common.repository.ads.AdsSegment;
import org.radixware.kernel.common.resources.icons.RadixIcon;
import org.radixware.kernel.common.types.Id;
import org.radixware.kernel.common.utils.IterableWalker;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.schemas.xscml.TypeDeclaration;

/**
 * Описатель описания типа типизируемых параметров дефиниций слоя ADS(тип
 * свойства, параметр метода, базовый класс и т. д.) с поддержкой параметризации
 * <p>
 * Поддерживается
 *
 */
public class AdsTypeDeclaration extends RadixObject implements IJavaSource {

    public static boolean isObject(AdsTypeDeclaration type) {
        if (type != null && !type.isArray()) {
            return type.getTypeId() == EValType.JAVA_CLASS && "java.lang.Object".equals(type.getExtStr());
        }
        return false;
    }

    public static boolean isAssignable(AdsTypeDeclaration base, AdsTypeDeclaration type, Definition context) {
        if (base == null || type == null) {
            return false;
        }

        if (base == AdsTypeDeclaration.UNDEFINED || type == AdsTypeDeclaration.UNDEFINED) {
            return false;
        }

        if (AdsTypeDeclaration.isObject(base)) {
            return true;
        }

        if (AdsTypeDeclaration.equals(context, base, type)) {
            return true;
        }

        if (base.getTypeId() == type.getTypeId() || (base.getTypeId() == EValType.USER_CLASS && (type.getTypeId() == EValType.PARENT_REF || type.getTypeId() == EValType.OBJECT))) {
            switch (base.getTypeId()) {
                case USER_CLASS:
                case PARENT_REF:
                case OBJECT:
                    final AdsType adsBaseType = base.resolve(context).get();
                    final AdsType adsType = type.resolve(context).get();
                    if (adsBaseType instanceof AdsClassType && adsType instanceof AdsClassType) {
                        final AdsClassDef baseCls = ((AdsClassType) adsBaseType).getSource();
                        final AdsClassDef cls = ((AdsClassType) adsType).getSource();
                        if (baseCls == cls) {
                            return true;
                        }
                        if (baseCls.getClassDefType() != EClassType.INTERFACE) {
                            return cls.getInheritance().isSubclassOf(baseCls);
                        }
                        final AdsTypeDeclaration superClassRef = cls.getInheritance().getSuperClassRef();
                        if (superClassRef != null) {
                            if (AdsTypeDeclaration.isAssignable(base, superClassRef, context)) {
                                return true;
                            }
                        }
                        final List<AdsTypeDeclaration> interfaceRefList = cls.getInheritance().getInerfaceRefList(ExtendableDefinitions.EScope.ALL);
                        for (AdsTypeDeclaration interf : interfaceRefList) {
                            if (AdsTypeDeclaration.isAssignable(base, interf, context)) {
                                return true;
                            }
                        }
                    }
            }
        }

        return false;
    }

    /**
     * Gets list of all generic arguments accessible in this context.
     *
     * @param context
     * @return list of generic arguments
     */
    public static List<TypeArgument> getAccessibleArguments(AdsClassDef context) {
        if (context != null) {
            return context.getNestedClassWalker().walk(new IterableWalker.Acceptor<AdsClassDef, List<TypeArgument>>(new ArrayList<TypeArgument>()) {
                @Override
                public void accept(AdsClassDef object) {
                    getResult().addAll(object.getTypeArguments().getArgumentList());
                    if (!object.isInner()) {
                        cancel();
                    }
                }
            });
        }
        return Collections.<TypeArgument>emptyList();
    }

    public static TypeArgument findArgument(AdsClassDef context, String name) {
        if (context != null && name != null) {
            final List<TypeArgument> accessibleArguments = getAccessibleArguments(context);
            for (final TypeArgument argument : accessibleArguments) {
                if (Objects.equals(name, argument.getName())) {
                    return argument;
                }
            }
        }
        return null;
    }

    public static class TypeArguments implements IJavaSource {

        public static final class Factory {

            public static TypeArguments loadFrom(RadixObject owner, org.radixware.schemas.xscml.TypeArguments xDef) {
                TypeArguments def = loadFromImpl(owner, xDef);
                if (def == null) {
                    return new TypeArguments(owner);
                } else {
                    return def;
                }
            }

            public static TypeArguments newInstance(RadixObject owner) {
                return new TypeArguments(owner);
            }

            private static TypeArguments loadFromImpl(RadixObject owner, org.radixware.schemas.xscml.TypeArguments xDef) {
                if (xDef == null) {
                    return null;
                }
                List<org.radixware.schemas.xscml.TypeArguments.Argument> list = xDef.getArgumentList();
                if (!list.isEmpty()) {
                    TypeArguments def = new TypeArguments(owner);
                    def.arguments = new ArrayList<>();
                    for (org.radixware.schemas.xscml.TypeArguments.Argument xItem : list) {
                        AdsTypeDeclaration type = xItem.getType() == null ? null : AdsTypeDeclaration.Factory.loadFrom(xItem.getType());
                        def.arguments.add(new TypeArgument(xItem.getAlias(), type, xItem.getDerivationRule()));
                    }
                    return def;
                }
                return null;
            }

            static TypeArguments parse(final AdsTypeDeclaration owner, final String typeArgumentList) {
                if (typeArgumentList == null || typeArgumentList.isEmpty()) {
                    return null;
                }
                final TypeArguments arguments = TypeArguments.Factory.newInstance(owner);
                final String typeArguments[] = typeArgumentList.split(",");
                for (String typeArgument : typeArguments) {
                    final String typeArgumentParts[] = typeArgument.split("\\s");
                    TypeArgument.Derivation derivation = TypeArgument.Derivation.NONE;
                    AdsTypeDeclaration referenceType = null;
                    for (String typeArgumentPart : typeArgumentParts) {
                        if (!typeArgumentPart.isEmpty() && !"?".equals(typeArgumentPart)) {
                            if ("extends".equals(typeArgumentPart)) {
                                derivation = TypeArgument.Derivation.EXTENDS;
                            } else if ("super".equals(typeArgumentPart)) {
                                derivation = TypeArgument.Derivation.SUPER;
                            } else {
                                referenceType = AdsTypeDeclaration.Factory.newPlatformClass(typeArgumentPart);
                            }
                        }
                    }
                    final TypeArgument argument = TypeArgument.Factory.newInstance(null, referenceType, derivation);
                    arguments.add(argument);
                }
                return arguments;
            }
        }
        protected List<TypeArgument> arguments = null;
        private RadixObject owner;

        private TypeArguments(RadixObject owner) {
            this.owner = owner;
        }

        @Override
        public JavaSourceSupport getJavaSourceSupport() {
            return new JavaSourceSupport(this) {
                @Override
                public JavaSourceSupport.CodeWriter getCodeWriter(UsagePurpose purpose) {
                    return new AdsTypeDeclarationCodeWriter.ArgumentsWriter(TypeArguments.this, this, purpose);
                }
            };
        }

        @Override
        public boolean equals(Object other) {
            if (super.equals(other)) {
                return true;
            }
            if (other instanceof TypeArguments) {
                TypeArguments args = (TypeArguments) other;
                if (this.arguments != null) {
                    if (args.arguments == null) {
                        return false;
                    }
                    if (args.arguments.size() != this.arguments.size()) {
                        return false;
                    }
                    for (int i = 0, len = arguments.size(); i < len; i++) {
                        TypeArgument thisArg = arguments.get(i);
                        TypeArgument arg = args.arguments.get(i);
                        if (!thisArg.equals(arg)) {
                            return false;
                        }
                    }
                    return true;
                } else {
                    if (args.arguments != null) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + (this.arguments != null ? this.arguments.hashCode() : 0);
            return hash;
        }

        public TypeArgument findByName(String name) {
            if (arguments == null || name == null) {
                return null;
            }
            for (TypeArgument a : arguments) {
                if (name.equals(a.name)) {
                    return a;
                }
            }
            return null;
        }

        public boolean equalsTo(AdsDefinition context, TypeArguments arguments) {
            if (arguments == null) {
                return false;
            }
            if (this.arguments == null) {
                if (arguments.arguments == null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (arguments.arguments == null) {
                    return false;
                } else {
                    if (this.arguments.size() != arguments.arguments.size()) {
                        return false;
                    }
                    for (int i = 0, len = this.arguments.size(); i < len; i++) {
                        TypeArgument thisA = this.arguments.get(i);
                        TypeArgument anotherA = arguments.arguments.get(i);
                        if (!thisA.equalsTo(context, anotherA)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }

        public String getQualifiedName(RadixObject context) {
            StringBuilder builder = new StringBuilder();
            builder.append('<');
            boolean isFirst = true;
            for (TypeArgument a : getArgumentList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(',');
                }
                builder.append(a.getName(context, true));
            }
            builder.append('>');
            return builder.toString();
        }

        public List<TypeArgument> getArgumentList() {
            synchronized (this) {
                if (arguments == null) {
                    return Collections.emptyList();
                } else {
                    return new ArrayList<>(arguments);
                }
            }
        }

        public boolean add(TypeArgument a) {
            synchronized (this) {
                if (arguments == null) {
                    arguments = new ArrayList<>();
                }
                arguments.add(a);
                if (owner != null) {
                    owner.setEditState(EEditState.MODIFIED);
                }
                a.owner = owner;
                return true;
            }
        }

        public boolean add(int index, TypeArgument a) {
            synchronized (this) {
                if (arguments == null) {
                    arguments = new ArrayList<>();
                }
                arguments.add(index, a);
                if (owner != null) {
                    owner.setEditState(EEditState.MODIFIED);
                }
                a.owner = owner;
                return true;
            }
        }

        public boolean remove(TypeArgument a) {
            synchronized (this) {
                if (arguments == null) {
                    return false;
                }
                if (arguments.remove(a)) {
                    if (owner != null) {
                        owner.setEditState(EEditState.MODIFIED);
                    }
                    if (arguments.isEmpty()) {
                        arguments = null;
                    }
                    a.owner = null;
                    return true;
                } else {
                    return false;
                }
            }
        }

        public boolean set(int index, TypeArgument a) {
            synchronized (this) {
                if (arguments == null) {
                    return false;
                }
                if (index < 0 || index >= arguments.size()) {
                    return false;
                }
                arguments.set(index, a);
                if (owner != null) {
                    owner.setEditState(EEditState.MODIFIED);
                }
                a.owner = owner;
                return true;
            }
        }

        public boolean remove(int index) {
            synchronized (this) {
                if (arguments == null) {
                    return false;
                }
                if (index < 0 || index >= arguments.size()) {
                    return false;
                }
                TypeArgument a = arguments.remove(index);
                if (owner != null) {
                    owner.setEditState(EEditState.MODIFIED);
                }
                if (a != null) {
                    a.owner = null;
                }
                return true;
            }
        }

        public boolean isEmpty() {
            return arguments == null || arguments.isEmpty();
        }

        public void appendTo(org.radixware.schemas.xscml.TypeArguments xDef) {
            if (arguments != null) {
                for (TypeArgument a : arguments) {
                    a.appendTo(xDef.addNewArgument());
                }
            }
        }
    }

    public static class TypeArgument {

        public static class Factory {

            /**
             * Example: List<?>
             */
            public static TypeArgument newInstance() {
                return new TypeArgument(null, null, Derivation.NONE);
            }

            /**
             * Example: List<name>
             */
            public static TypeArgument newInstance(String name) {
                return new TypeArgument(name, null, Derivation.NONE);
            }

            /**
             * Example: List<String>
             */
            public static TypeArgument newInstance(AdsTypeDeclaration type) {
                return new TypeArgument(null, type, Derivation.NONE);
            }

            /**
             * Example: List<? extends String>
             */
            public static TypeArgument newInstance(AdsTypeDeclaration type, Derivation derivation) {
                return new TypeArgument(null, type, derivation == null ? Derivation.NONE : derivation);
            }

            /**
             * Example: List<T extends String>
             */
            public static TypeArgument newInstance(String name, AdsTypeDeclaration type, Derivation derivation) {
                return new TypeArgument(name, type, derivation == null ? Derivation.NONE : derivation);
            }
        }

        public enum Derivation {

            EXTENDS,
            SUPER,
            NONE
        }
        private static final String ANONIMOUS = "?";
        List<?> list;
        String name;
        AdsTypeDeclaration type;
        Derivation derivation;
        private RadixObject owner;

        private TypeArgument(String name, AdsTypeDeclaration decl, org.radixware.schemas.xscml.TypeArguments.Argument.DerivationRule.Enum derivation) {
            this.name = name == null || name.isEmpty() ? ANONIMOUS : name;
            this.type = decl;
            this.derivation = org.radixware.schemas.xscml.TypeArguments.Argument.DerivationRule.SUPER == derivation ? Derivation.SUPER : (org.radixware.schemas.xscml.TypeArguments.Argument.DerivationRule.EXTENDS == derivation ? Derivation.EXTENDS : Derivation.NONE);
        }

        private TypeArgument(String name, AdsTypeDeclaration decl, Derivation derivation) {
            this.name = name == null || name.isEmpty() ? ANONIMOUS : name;
            this.type = decl;
            this.derivation = derivation;
        }

        private TypeArgument(TypeArgument source) {
            this.name = source.name;
            this.type = source.type;
            this.derivation = source.derivation;
        }

        private void appendTo(org.radixware.schemas.xscml.TypeArguments.Argument arg) {

            if (derivation == Derivation.SUPER) {
                arg.setDerivationRule(org.radixware.schemas.xscml.TypeArguments.Argument.DerivationRule.SUPER);
            } else if (derivation == Derivation.EXTENDS) {
                arg.setDerivationRule(org.radixware.schemas.xscml.TypeArguments.Argument.DerivationRule.EXTENDS);
            }
            if (name != null) {
                arg.setAlias(name);
            }
            if (type != null) {
                type.appendTo(arg.addNewType());
            }
        }

        public boolean equalsTo(AdsDefinition context, TypeArgument other) {
            if (other == null) {
                return false;
            }
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            if (this.derivation != other.derivation) {
                return false;
            }

            AdsType thisType = type == null ? null : type.resolve(context).get();
            AdsType otherType = other.type == null ? null : other.type.resolve(context).get();

            if (Utils.equals(thisType, otherType)) {
                if (type != null) {
                    return type.equalsTo(context, other.type);
                } else {
                    return other.type == null;
                }
            } else {
                return false;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final TypeArgument other = (TypeArgument) obj;
            if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
                return false;
            }
            if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
                return false;
            }
            if (this.derivation != other.derivation) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + (this.name != null ? this.name.hashCode() : 0);
            hash = 71 * hash + (this.type != null ? this.type.hashCode() : 0);
            hash = 71 * hash + (this.derivation != null ? this.derivation.hashCode() : 0);
            return hash;
        }

        public AdsTypeDeclaration getType() {
            return type;
        }

        public void setType(AdsTypeDeclaration type) {
            this.type = type;
            if (owner != null) {
                owner.setEditState(EEditState.MODIFIED);
            }
        }

        public Derivation getDerivation() {
            return this.derivation;
        }

        public void setDerivation(Derivation d) {
            this.derivation = d;
            if (owner != null) {
                owner.setEditState(EEditState.MODIFIED);
            }
        }

        public String getName() {
            return this.name == null ? ANONIMOUS : this.name;
        }

        public void setName(String name) {
            this.name = ANONIMOUS.equals(name) ? null : name;
            if (owner != null) {
                owner.setEditState(EEditState.MODIFIED);
            }
        }

        public String getName(RadixObject context) {
            return getName(context, false);
        }

        private String getName(RadixObject context, boolean qualified) {
            if (derivation == Derivation.NONE) {
                return type == null ? "?" : type.getName(context, qualified);
            } else {
                StringBuilder builder = new StringBuilder();
                builder.append(getName());
                builder.append(' ');
                builder.append(derivation == Derivation.SUPER ? "super" : "extends");
                builder.append(' ');
                builder.append(type == null ? "?" : type.getName(context, qualified));
                return builder.toString();
            }
        }

        public String getQualifiedName(RadixObject context) {
            return getName(context, true);
        }
    }

    public static class Factory {

        public static AdsTypeDeclaration loadFrom(TypeDeclaration xDecl, AdsTypeDeclaration defaultIfNonDefined) {
            if (xDecl == null || xDecl.getTypeId() == null) {
                return defaultIfNonDefined;
            }
            try {
                return new AdsTypeDeclaration(xDecl);
            } catch (NoConstItemWithSuchNameError e) {
                return defaultIfNonDefined;
            }
        }

        public static AdsTypeDeclaration loadFrom(TypeDeclaration xDecl) {
            return loadFrom(xDecl, UNDEFINED);
        }

        public static AdsTypeDeclaration newInstance(TypeBinding binding) {

            TypeBinding type = binding;
            int dims = 0;
            if (binding instanceof ArrayBinding) {
                type = binding.leafComponentType();
                dims = binding.dimensions();
            }
            AdsTypeDeclaration result;
            if (type.isBaseType()) {
                result = AdsTypeDeclaration.Factory.newPrimitiveType(String.valueOf(type.sourceName()));
            } else {
                if (type instanceof TypeVariableBinding) {
                    result = AdsTypeDeclaration.Factory.newTypeParam(String.valueOf(type.sourceName()));
                } else {
                    result = AdsTypeDeclaration.Factory.newPlatformClass(String.valueOf(CharOperation.concat(((ReferenceBinding) type).qualifiedPackageName(), type.qualifiedSourceName(), '.')));
                }
                if (type instanceof ParameterizedTypeBinding) {
                    ParameterizedTypeBinding pt = (ParameterizedTypeBinding) type;
                    if (pt.arguments != null && pt.arguments.length > 0) {
                        TypeArguments args = TypeArguments.Factory.newInstance(null);
                        for (TypeBinding param : pt.arguments) {
                            TypeArgument arg;
                            if (param instanceof TypeVariableBinding) {
                                arg = TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newTypeParam(String.valueOf(param.sourceName())));
                            } else if (param instanceof WildcardBinding) {
                                WildcardBinding wb = (WildcardBinding) param;
                                arg = TypeArgument.Factory.newInstance("?");
                                switch (wb.boundKind) {
                                    case Wildcard.UNBOUND:
                                        arg.setDerivation(TypeArgument.Derivation.NONE);
                                        break;
                                    case Wildcard.EXTENDS:
                                        arg.setDerivation(TypeArgument.Derivation.EXTENDS);
                                        break;
                                    case Wildcard.SUPER:
                                        arg.setDerivation(TypeArgument.Derivation.SUPER);
                                        break;
                                }
                                if (wb.bound != null) {
                                    arg.setType(AdsTypeDeclaration.Factory.newInstance(wb.bound));
                                }
                            } else {
                                arg = TypeArgument.Factory.newInstance(AdsTypeDeclaration.Factory.newInstance(param));
                            }
                            args.add(arg);
                        }
                        result = result.toGenericType(args);
                    }
                } else if (type instanceof ReferenceBinding) {
                    ReferenceBinding pt = (ReferenceBinding) type;
                    if (pt.typeVariables() != null && pt.typeVariables().length > 0) {
                        TypeArguments args = TypeArguments.Factory.newInstance(null);
                        for (TypeVariableBinding param : pt.typeVariables()) {
                            TypeArgument arg = TypeArgument.Factory.newInstance(String.valueOf(param.sourceName));
                            if (param.firstBound != null) {
                                arg.type = AdsTypeDeclaration.Factory.newInstance(param.firstBound);
                                arg.derivation = TypeArgument.Derivation.EXTENDS;
                            }

                            args.add(arg);
                        }
                        result = result.toGenericType(args);
                    }
                }
            }
            if (dims > 0) {
                return result.toArrayType(dims);
            }

            return result;

        }

        public static AdsTypeDeclaration newInstance(EValType typeId) {
            return new AdsTypeDeclaration(typeId, 0);
        }

        public static AdsTypeDeclaration newEntityGroupArgument(AdsEntityGroupClassDef group) {
            return new AdsTypeDeclaration(EValType.USER_CLASS, new Id[]{group.getBasisId()});
        }

        public static AdsTypeDeclaration newInstance(IAdsTypeSource type) {
            return new AdsTypeDeclaration(type, null, null, 0);
        }

        //public static final AdsTypeDeclaration newInstance(Id id) {
        //    return new AdsTypeDeclaration(id);
        //}
        public static AdsTypeDeclaration newInstance(EValType typeId, Id[] ids) {
            return new AdsTypeDeclaration(typeId, ids);
        }

        public static AdsTypeDeclaration newInstance(EValType typeId, Id id) {
            return new AdsTypeDeclaration(typeId, new Id[]{id});
        }

        public static AdsTypeDeclaration newParentRef(AdsEntityObjectClassDef type) {
            return new AdsTypeDeclaration(type, EValType.PARENT_REF, null, 0);
        }

        public static AdsTypeDeclaration newParentRef(DdsTableDef table) {
            return new AdsTypeDeclaration(table, EValType.PARENT_REF, 0);
        }

        public static AdsTypeDeclaration newArrRef(DdsTableDef table) {
            return new AdsTypeDeclaration(table, EValType.ARR_REF, 0);
        }

        public static AdsTypeDeclaration newArrRef(AdsEntityObjectClassDef type) {
            return new AdsTypeDeclaration(type, EValType.ARR_REF, null, 0);
        }

        public static AdsTypeDeclaration newArrVal(AdsEnumDef type) {
            return new AdsTypeDeclaration(type, type.getItemType().getArrayType(), null, 0);
        }

        public static AdsTypeDeclaration newEntityObject(AdsEntityObjectClassDef type) {
            return new AdsTypeDeclaration(type, EValType.OBJECT, null, 0);
        }

        public static AdsTypeDeclaration newXml(IXmlDefinition type, String typeName) {
            return new AdsTypeDeclaration(type, null, typeName, 0);
        }

        public static AdsTypeDeclaration voidType() {
            return VOID;
        }

        public static AdsTypeDeclaration undefinedType() {
            return UNDEFINED;
        }

        public static AdsTypeDeclaration newInstance(EValType typeId, IAdsTypeSource type, String suffix, int dim) {
            return new AdsTypeDeclaration(type, typeId, suffix, dim);
        }

        public static AdsTypeDeclaration newPlatformClass(String platformClassName) {
            if ("void".equals(platformClassName)) {
                return VOID;
            }
            EValType radixType = null;
            switch (platformClassName) {
                case "java.lang.String":
                    radixType = EValType.STR;
                    break;
                case "java.lang.Long":
                    radixType = EValType.INT;
                    break;
                case "java.lang.Boolean":
                    radixType = EValType.BOOL;
                    break;
                case "java.sql.Timestamp":
                    radixType = EValType.DATE_TIME;
                    break;
                case "java.sql.Blob":
                    radixType = EValType.BLOB;
                    break;
                case "java.sql.Clob":
                    radixType = EValType.CLOB;
                    break;
                case "java.lang.Character":
                    radixType = EValType.CHAR;
                    break;
                case "org.radixware.kernel.common.types.ArrBin":
                    radixType = EValType.ARR_BIN;
                    break;
                case "org.radixware.kernel.common.types.ArrBool":
                    radixType = EValType.ARR_BOOL;
                    break;
                case "org.radixware.kernel.common.types.ArrChar":
                    radixType = EValType.ARR_CHAR;
                    break;
                case "org.radixware.kernel.common.types.ArrDateTime":
                    radixType = EValType.ARR_DATE_TIME;
                    break;
                case "org.radixware.kernel.common.types.ArrInt":
                    radixType = EValType.ARR_INT;
                    break;
                case "org.radixware.kernel.common.types.ArrNum":
                    radixType = EValType.ARR_NUM;
                    break;
                case "org.radixware.kernel.common.types.ArrStr":
                    radixType = EValType.ARR_STR;
                    break;
                case "org.radixware.kernel.common.types.Bin":
                    radixType = EValType.BIN;
                    break;
            }
            if (radixType != null) {
                return new AdsTypeDeclaration(radixType);
            }

            return new AdsTypeDeclaration(platformClassName, EValType.JAVA_CLASS);
        }

        public static AdsTypeDeclaration newTypeParam(String paramName) {
            return new AdsTypeDeclaration(paramName);
        }

        public static AdsTypeDeclaration newPrimitiveType(String primitiveTypeName) {
            if ("void".equals(primitiveTypeName)) {
                return VOID;
            }
            return new AdsTypeDeclaration(primitiveTypeName, EValType.JAVA_TYPE);
        }

        public static AdsTypeDeclaration newCopy(AdsTypeDeclaration source) {
            return new AdsTypeDeclaration(source, null);
        }
    }
    public static final AdsTypeDeclaration VOID = new AdsTypeDeclaration();
    public static final AdsTypeDeclaration UNDEFINED = new AdsTypeDeclaration();
    public static final AdsTypeDeclaration ANY = new AdsTypeDeclaration(EValType.ANY);
    //----------------------------------------------------------------------
    private EValType typeId;
    private AdsPath path = null;
    private String extStr = null;
    private boolean isTypeArgument = false;
    int dimension = 0;
    private TypeArguments genericArguments;

    //----------------------------------------------------------------------
    //creates void type
    private AdsTypeDeclaration() {
        this.genericArguments = null;
    }

    private AdsTypeDeclaration(EValType id) {
        this.genericArguments = null;
        this.typeId = id;
    }

    private AdsTypeDeclaration(EValType valType, int dim) {
        this.typeId = valType;
        this.dimension = dim;
        this.genericArguments = null;
    }

    private AdsTypeDeclaration(DdsTableDef table, EValType valType, int dim) {
        this.typeId = valType;
        this.dimension = dim;
        this.genericArguments = null;
        this.path = new AdsPath(new Id[]{Id.Factory.changePrefix(table.getId(), EDefinitionIdPrefix.ADS_ENTITY_CLASS)});
    }

    private AdsTypeDeclaration(String javaClassName, EValType valType) {
        this.typeId = valType;
        this.path = null;
        if (typeId == EValType.JAVA_CLASS) {
            final int typeArgsIndex = javaClassName.indexOf("<");
            if (typeArgsIndex > 0) {
                this.extStr = javaClassName.substring(0, typeArgsIndex);
                final String typeArgsStr = javaClassName.substring(typeArgsIndex + 1, javaClassName.length() - 1);
                genericArguments = TypeArguments.Factory.parse(this, typeArgsStr);
            } else {
                this.genericArguments = null;
                this.extStr = javaClassName;
            }
        } else {
            this.genericArguments = null;
            this.extStr = javaClassName;
        }
    }

    private AdsTypeDeclaration(String typeArgumentName) {
        this.typeId = EValType.JAVA_CLASS;
        this.path = null;
        final int typeArgsIndex = typeArgumentName.indexOf("<");
        if (typeArgsIndex > 0) {
            this.extStr = typeArgumentName.substring(0, typeArgsIndex);
            final String typeArgsStr = typeArgumentName.substring(typeArgsIndex + 1, typeArgumentName.length() - 1);
            genericArguments = TypeArguments.Factory.parse(this, typeArgsStr);
        } else {
            this.extStr = typeArgumentName;
            this.genericArguments = null;
        }
        this.isTypeArgument = true;
    }

    private AdsTypeDeclaration(EValType typeId, Id[] ids) {
        if (typeId == null || ids == null) {
            throw new NullPointerException();
        }
        this.typeId = typeId;
        this.path = new AdsPath(ids);
        this.extStr = null;
        this.genericArguments = null;
        this.dimension = 0;
    }

    private AdsTypeDeclaration(IAdsTypeSource type, EValType typeId, String suffix, int dim) {
        if (type instanceof AdsEnumDef) {
            if (typeId == null) {
                this.typeId = ((AdsEnumDef) type).getItemType();
            } else {
                this.typeId = typeId;
            }
        } else if (type instanceof AdsClassDef || type instanceof AdsCommandDef || type instanceof AdsAbstractUIDef) {
            if (typeId != null) {
                this.typeId = typeId;
            } else {
                this.typeId = EValType.USER_CLASS;
            }
        } else if (type instanceof IXmlDefinition) {
            this.typeId = EValType.XML;
        } else if (type != null) {
            throw new RadixObjectError("Unsupported type source: " + type.getClass().getName(), this);
        } else {
            this.typeId = typeId;
        }
        if (type != null) {
            this.path = new AdsPath((AdsDefinition) type);
        } else {
            this.path = null;
        }

        this.extStr = suffix;

        this.dimension = dim;
        this.genericArguments = null;
    }

    private AdsTypeDeclaration(TypeDeclaration xDef) {
        this.typeId = xDef.getTypeId();

        this.path = xDef.getPath() == null ? null : new AdsPath(xDef.getPath());

        if (xDef.isSetDimension()) {
            this.dimension = xDef.getDimension();
        } else {
            this.dimension = 0;
        }
        this.extStr = xDef.getExtStr();
        if (xDef.isSetIsArgumentType()) {
            this.isTypeArgument = xDef.getIsArgumentType();
        } else {
            this.isTypeArgument = false;
        }

        if (xDef.isSetGenericArguments()) {
            this.genericArguments = TypeArguments.Factory.loadFromImpl(this, xDef.getGenericArguments());
        } else {
            this.genericArguments = null;
        }
    }

    private AdsTypeDeclaration(AdsTypeDeclaration source, TypeArguments arguments) {
        this.typeId = source.typeId;

        if (source.path != null) {
            this.path = new AdsPath(source.path);
        }
        this.isTypeArgument = source.isTypeArgument;
        this.extStr = source.extStr;
        this.dimension = source.dimension;
        if (source.genericArguments != null && source.genericArguments.arguments != null) {
            this.genericArguments = TypeArguments.Factory.newInstance(this);
            for (TypeArgument arg : source.genericArguments.getArgumentList()) {
                this.genericArguments.add(new TypeArgument(arg));
            }
        } else {
            this.genericArguments = arguments;
        }
    }
    /*
     * private AdsTypeDeclaration(Id id) { if (id == null) { throw new
     * NullPointerException(); } this.typeId = EValType.USER_CLASS; this.path =
     * new AdsPath(new Id[]{id}); this.extStr = null; this.genericArguments =
     * null; this.dimension = 0; }
     */
    //----------------------------------------------------------------------

    public boolean isBasedOn(EValType typeId) {
        return this.typeId == typeId;
    }

    public boolean isVoid() {
        return this == VOID;
    }

    public boolean isUndefined() {
        return this.isTypeArgument == false && this.typeId == null && this.path == null && this.extStr == null && this.dimension == 0 && this.genericArguments == null;
    }

    public boolean isGeneric() {
        return genericArguments != null;
    }

    public TypeArguments getGenericArguments() {
        return genericArguments;
    }

    public void appendTo(TypeDeclaration xDef) {
        if (typeId != null) {
            xDef.setTypeId(typeId);
        }
        if (path != null) {
            xDef.setPath(path.asList());
        }

        if (extStr != null) {
            xDef.setExtStr(extStr);
        }
        if (isTypeArgument) {
            xDef.setIsArgumentType(isTypeArgument);
        }
        if (dimension > 0) {
            xDef.setDimension(dimension);
        }
        if (this.genericArguments != null && !this.genericArguments.isEmpty()) {
            this.genericArguments.appendTo(xDef.addNewGenericArguments());
        }
    }

    public String getName(RadixObject referenceContext) {
        return getName(referenceContext, false);
    }

    @Override
    public String getQualifiedName(RadixObject referenceContext) {
        return getName(referenceContext, true);
    }

    public String getQualifiedName(RadixObject referenceContext, boolean[] unresolved) {
        return getName(referenceContext, true, false, unresolved);
    }

    @Override
    public String getQualifiedName() {
        return getName(null, true);
    }

    public String getHtmlName(RadixObject context, boolean qualified) {
        String name = getName(context, qualified);
        return name.replace("<", "&lt;").replace(">", "&gt;");
    }

    public String getRowName(RadixObject referenceContext) {
        return getName(referenceContext, false, true, null);
    }

    private String getName(RadixObject referenceContext, boolean qualified) {
        return getName(referenceContext, qualified, false, null);
    }

    private String getName(RadixObject referenceContext, boolean qualified, boolean row, boolean[] unresolved) {
        if (this == VOID) {
            return "void";
        }
        if (this == UNDEFINED) {
            if (unresolved != null) {
                unresolved[0] = true;
            }
            return "<undefined>";
        } else {
            Definition referenceContextDef = null;
            if (referenceContext instanceof Definition) {
                referenceContextDef = (Definition) referenceContext;
            } else {
                if (referenceContext != null) {
                    referenceContextDef = referenceContext.getOwnerDefinition();
                }
            }
            final AdsType type = referenceContextDef == null ? null : resolve(referenceContextDef).get();
            if (type != null) {
                final StringBuilder builder = new StringBuilder(100);
                builder.append(qualified ? type.getQualifiedName(referenceContext) : type.getName());
                if (!row) {
                    appendGenericArguments(builder, referenceContext, qualified);
                }
                for (int i = 0; i < dimension; i++) {
                    builder.append("[]");
                }
                return builder.toString();
            } else {
                if (typeId == null) {
                    if (unresolved != null) {
                        unresolved[0] = true;
                    }
                    return "<undefined>";
                }
                switch (typeId) {
                    case JAVA_CLASS:
                    case JAVA_TYPE:
                        if (extStr != null) {
                            if (qualified) {
                                if (!row && genericArguments != null && !genericArguments.isEmpty()) {
                                    final StringBuilder builder = new StringBuilder(extStr);
                                    appendGenericArguments(builder, referenceContext, true);
                                    return builder.toString();
                                } else {
                                    return extStr;
                                }
                            } else {
                                String[] names = extStr.split("\\.");
                                for (int i = 0; i < names.length; i++) {
                                    if (Character.isUpperCase(names[i].charAt(0))) {
                                        StringBuilder builder = new StringBuilder();
                                        for (int j = i; j < names.length; j++) {
                                            if (j > i) {
                                                builder.append('.');
                                            }
                                            builder.append(names[j]);
                                        }
                                        return builder.toString();
                                    }
                                }
                                return names[names.length - 1];
                            }
                        } else {
                            if (unresolved != null) {
                                unresolved[0] = true;
                            }
                            return "<undefined java type>";
                        }
                    default: {
                        if (path != null) {
                            StringBuilder response = new StringBuilder();
                            response.append("<unresolved:");
                            response.append(path.toString());
                            if (extStr != null) {
                                response.append(extStr);
                            }
                            response.append(">");
                            if (unresolved != null) {
                                unresolved[0] = true;
                            }
                            return response.toString();
                        } else {
                            return typeId.getName();
                        }
                    }
                }
            }
        }
    }

    private void appendGenericArguments(final StringBuilder builder, final RadixObject referenceContext, final boolean qualified) {
        if (genericArguments != null && !genericArguments.isEmpty()) {
            builder.append('<');
            boolean isFirst = true;
            for (TypeArgument a : genericArguments.getArgumentList()) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append(',');
                }
                builder.append(a.getName(referenceContext, qualified));
            }
            builder.append('>');
        }
    }

    private AdsType checkArr(AdsType type) {
        if (dimension > 0) {
            return new ArrayType(type, dimension);
        } else {
            return type;
        }
    }

    private AdsTypeDeclaration getArgumentByBaseTypeArgumentName(Definition referenceContext, AdsClassDef root, String argName) {
        AdsTypeDeclaration decl = root.getInheritance().getSuperClassRef();
        if (decl != null && decl.getGenericArguments() != null && !decl.getGenericArguments().isEmpty()) {
            AdsType type = decl.resolve(referenceContext).get();
            if (type instanceof AdsClassType) {
                AdsClassDef clazz = ((AdsClassType) type).getSource();
                int argIndex = -1;
                List<TypeArgument> argList = clazz.getTypeArguments().getArgumentList();
                for (int i = 0; i < argList.size(); i++) {
                    TypeArgument a = argList.get(i);
                    if (a.getName().equals(extStr)) {
                        argIndex = i;
                        break;
                    }
                }
                if (argIndex >= 0) {//argument found
                    List<TypeArgument> extList = decl.getGenericArguments().getArgumentList();
                    if (argIndex < extList.size()) {
                        return extList.get(argIndex).getType();
                    } else {
                        return null;
                    }
                } else {
                    AdsTypeDeclaration argumentTypeFromSuperClass = getArgumentByBaseTypeArgumentName(referenceContext, clazz, argName);
                    if (argumentTypeFromSuperClass != null) {
                        if (argumentTypeFromSuperClass.isTypeArgument) {
                            String newArgName = argumentTypeFromSuperClass.extStr;
                            if (newArgName != null && !newArgName.isEmpty() && !newArgName.equals(argName)) {
                                return getArgumentByBaseTypeArgumentName(referenceContext, root, newArgName);
                            } else {
                                return argumentTypeFromSuperClass;
                            }
                        } else {
                            return argumentTypeFromSuperClass;
                        }
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public List<AdsType> resolveAllTypes(Definition referenceContext) {
        List<AdsType> types = new LinkedList<>();
        AdsType selfType = resolve(referenceContext).get();
        if (selfType != null) {
            types.add(selfType);
        }
        if (genericArguments != null) {
            for (TypeArgument a : genericArguments.getArgumentList()) {
                AdsTypeDeclaration decl = a.getType();
                if (decl != null) {
                    List<AdsType> typesFromArguments = decl.resolveAllTypes(referenceContext);
                    if (!typesFromArguments.isEmpty()) {
                        types.addAll(typesFromArguments);
                    }
                }
            }
        }
        return types;
    }

    public SearchResult<AdsType> resolve(Definition referenceContext) {
        List<AdsType> result = resolveAll(referenceContext);
        return result.isEmpty() ? SearchResult.<AdsType>empty() : SearchResult.list(result);
    }

    public List<AdsType> resolveAll(Definition referenceContext) {

        if (isTypeArgument) {
            AdsClassDef clazz = null;
            for (Definition def = referenceContext; def != null; def = def.getOwnerDefinition()) {
                if (def instanceof AdsClassDef) {
                    clazz = (AdsClassDef) def;
                    break;
                }
            }
            if (clazz != null) {
                for (final TypeArgument arg : getAccessibleArguments(clazz)) {
                    if (arg.getName().equals(extStr)) {
                        return Collections.singletonList(checkArr(new ArgumentType(arg)));
                    }
                }
                AdsTypeDeclaration subst = getArgumentByBaseTypeArgumentName(referenceContext, clazz, extStr);
                if (subst != null) {
                    return subst.resolve(referenceContext).all();
                } else {
                    return Collections.emptyList();
                }

            }
            return Collections.emptyList();
        }
        if (this == ANY) {
            return Collections.singletonList(checkArr(AnyType.getInstance()));
        } else if (this == VOID) {
            return Collections.singletonList(checkArr(VoidType.getInstance()));
        } else if (this == UNDEFINED) {
            return Collections.emptyList();
        } else if (this.typeId == EValType.JAVA_TYPE) {
            if (extStr == null || extStr.isEmpty()) {
                return Collections.emptyList();
            } else {
                JavaType type = JavaType.Factory.forTypeName(extStr);
                if (type == null) {
                    return Collections.singletonList(checkArr(JavaClassType.Factory.forClassName(extStr)));
                } else {
                    return Collections.singletonList(checkArr(type));
                }
            }
        } else if (this.typeId == EValType.JAVA_CLASS) {
            if (extStr == null || extStr.isEmpty()) {
                return Collections.emptyList();
            } else {
                if (referenceContext == null) {
                    return Collections.singletonList(checkArr(JavaClassType.Factory.forClassName(extStr)));
                } else {
                    Module module = referenceContext.getModule();
                    if (module == null) {
                        return Collections.emptyList();
                    }

                    Segment segment = module.getSegment();
                    if (segment == null) {
                        return Collections.emptyList();
                    }
                    Layer layer = segment.getLayer();
                    if (layer == null) {
                        return Collections.emptyList();
                    }
                    AdsSegment adsSegment = (AdsSegment) layer.getAds();

                    if (adsSegment == null) {
                        return Collections.emptyList();
                    }
                    IPlatformClassPublisher publisher = adsSegment.getBuildPath().getPlatformPublishers().findPublisherByName(extStr);
                    if (publisher == null) {
                        return Collections.singletonList(checkArr(JavaClassType.Factory.forClassName(extStr)));
                    } else if (publisher instanceof AdsClassDef) {
                        AdsTransparence t = ((AdsClassDef) publisher).getTransparence();
                        if (t != null && t.isTransparent() && !t.isExtendable()) {
                            return Collections.singletonList(checkArr(AdsClassType.Factory.newInstance((AdsClassDef) publisher)));
                        } else {
                            return Collections.singletonList(checkArr(JavaClassType.Factory.forClassName(extStr)));
                        }
                    } else if (publisher instanceof AdsEnumDef) {
                        boolean ext = ((AdsEnumDef) publisher).isExtendable();
                        if (!ext) {
                            return Collections.singletonList(checkArr(AdsEnumType.Factory.newInstance((AdsEnumDef) publisher)));
                        } else {
                            return Collections.singletonList(checkArr(JavaClassType.Factory.forClassName(extStr)));
                        }
                    } else {
                        return Collections.emptyList();
                    }
                }
            }
        } else if (path == null) {
            return Collections.singletonList(checkArr(RadixType.Factory.newInstance(typeId)));
        } else {
            //try to resolve as command type.
            //Possible context is only handling method for all of other
            //contexts this reference will not be resolved
            List<Id> ids = path.asList();
            if (ids.size() == 1 && ids.get(0).getPrefix() == EDefinitionIdPrefix.COMMAND) {
                if (referenceContext instanceof AdsCommandHandlerMethodDef && ((AdsCommandHandlerMethodDef) referenceContext).getCommandId() == ids.get(0)) {
                    AdsCommandDef command = ((AdsCommandHandlerMethodDef) referenceContext).findCommand();
                    if (command != null) {
                        return Collections.singletonList(checkArr(command.getType(null, null)));
                    } else {
                        return Collections.emptyList();
                    }
                } else {
                    return Collections.emptyList();
                }
            }

            List<Definition> roots = path.resolveAll(referenceContext);
            if (roots.isEmpty()) {
                return Collections.emptyList();
            } else {
                List<AdsType> result = new LinkedList<>();
                for (Definition root : new ArrayList<>(roots)) {
                    if (root instanceof IAdsTypeSource) {
                        result.add(checkArr(((IAdsTypeSource) root).getType(typeId, extStr)));
                    }
                }
                return result;
            }
        }
    }

    //private static final String JAVA_LANG_PACKAGE_PREFIX = "java.lang.";
    //private static final int JAVA_LANG_PACKAGE_PREFIX_LEMN = JAVA_LANG_PACKAGE_PREFIX.length();
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final AdsTypeDeclaration other = (AdsTypeDeclaration) obj;
        if (this.isTypeArgument != other.isTypeArgument) {
            return false;
        }
        if (this.typeId != other.typeId) {
            return false;
        }
        if (this.path != other.path && (this.path == null || !this.path.equals(other.path))) {
            return false;
        }
        if ((this.extStr == null || this.extStr.isEmpty()) ? !(other.extStr == null || other.extStr.isEmpty()) : !this.extStr.equals(other.extStr)) {
            return false;
        }
        if (this.dimension != other.dimension) {
            return false;
        }
        if (this.genericArguments != other.genericArguments && (this.genericArguments == null || !this.genericArguments.equals(other.genericArguments))) {
            return false;
        }
        return true;
    }

    public static boolean equals(AdsTypeDeclaration one, AdsTypeDeclaration another) {
        return equals(null, one, another);
    }

    private static boolean javaClassEqualsTo(Definition context, AdsTypeDeclaration javaType, AdsTypeDeclaration anotherType) {
        if (anotherType.typeId == EValType.JAVA_CLASS) {
            String n1 = javaType.extStr == null ? null : javaType.extStr.replace("$", ".");
            String n2 = anotherType.extStr == null ? null : anotherType.extStr.replace("$", ".");
            return Utils.equals(n1, n2);
        } else {
            if (context != null) {
                AdsType t1 = javaType.resolve(context).get();
                AdsType t2 = anotherType.resolve(context).get();
                if (t1 instanceof AdsDefinitionType && t2 instanceof AdsDefinitionType) {
                    AdsDefinitionType d1 = (AdsDefinitionType) t1;
                    AdsDefinitionType d2 = (AdsDefinitionType) t2;
                    return d1.getSource() == d2.getSource();
                }
            }
        }
        return false;
    }

    public static boolean equals(Definition context, AdsTypeDeclaration one, AdsTypeDeclaration another) {
        if (one == null) {
            return another == null;
        }
        if (another == null) {
            return false;
        }

        if (one.typeId == EValType.JAVA_CLASS) {
            if (javaClassEqualsTo(context, one, another)) {
                return true;
            }
        } else if (another.typeId == EValType.JAVA_CLASS) {
            if (javaClassEqualsTo(context, another, one)) {
                return true;
            }
        }

        return one.equals(another);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (this.typeId != null ? this.typeId.hashCode() : 0);
        hash = 67 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 67 * hash + (this.extStr != null ? this.extStr.hashCode() : 0);
        hash = 67 * hash + this.dimension;
        hash = 67 * hash + (this.genericArguments != null ? this.genericArguments.hashCode() : 0);
        return hash;
    }

    public EValType getTypeId() {
        return typeId;
    }

    public String getExtStr() {
        return extStr;
    }

    public AdsPath getPath() {
        return path;
    }

    public boolean isPure() {
        return (path == null || path.isEmpty()) && (extStr == null || extStr.isEmpty());
    }

    public boolean isArray() {
        return dimension > 0;
    }

    public int getArrayDimensionCount() {
        return dimension;
    }

    public AdsTypeDeclaration toArrayType(int dimension) {
        AdsTypeDeclaration decl = Factory.newCopy(this);
        decl.dimension = dimension;
        return decl;
    }

    public AdsTypeDeclaration toRawType() {
        AdsTypeDeclaration decl = Factory.newCopy(this);
        decl.genericArguments = null;
        return decl;
    }

    public AdsTypeDeclaration toGenericType(TypeArguments typeArguments) {
        AdsTypeDeclaration decl = new AdsTypeDeclaration(this, typeArguments);
        return decl;
    }

    private void error(RadixObject context, IProblemHandler problemHandler, String message) {
        if (problemHandler != null) {
            problemHandler.accept(RadixProblem.Factory.newError(context, message));
        }
    }

    /**
     * Checks type declaration Returns ads type descriptor resolved during check
     * process
     */     
    public AdsType check(final RadixObject initialContext, final IProblemHandler problemHandler, final Map<Object, Object> checkHistory) {
        if (this == AdsTypeDeclaration.VOID) {
            return VoidType.getInstance();
        }

        Definition definition = null;
        RadixObject context = initialContext;
        while (context != null) {
            if (context instanceof Definition) {
                definition = (Definition) context;
                break;
            }
            context = context.getContainer();
        }
        if (definition == null) {
            error(initialContext, problemHandler, "No type resolution context found");
            return null;
        }

        AdsType resolvedType = this.resolve(definition).get(new SearchResult.GetAdvisor<AdsType>() {
            private String getName(AdsType getResult) {
                String name = "";
                if (getResult != null) {
                    name = getResult.getQualifiedName(initialContext);
                }
                return name;
            }

            @Override
            public AdsType advise(SearchResult<AdsType> result, AdsType getResult) {
                if (result.isMultiple()) {
                    StringBuilder builder = new StringBuilder();

                    builder.append("Type reference ").append(getName(getResult)).append(" is ambigous. Candidates are:  ");
                    boolean first = true;
                    for (AdsType t : result.all()) {
                        if (first) {
                            first = false;
                        } else {
                            builder.append(", ");
                        }
                        builder.append(getName(t));
                    }
                    problemHandler.accept(RadixProblem.Factory.newError(initialContext, builder.toString()));
                }
                return getResult;
            }
        });

        if (resolvedType
                == null) {
            StringBuilder builder = new StringBuilder();
            builder.append(typeId != null ? typeId.getName() : "<not defined>");
            builder.append("<");

            if (path != null) {
                builder.append(AdsPath.toString(path));
            }
            if (extStr != null) {
                if (path != null) {
                    builder.append("::");
                }
                builder.append(extStr);
            }
            builder.append(">");
            error(initialContext, problemHandler, "Type reference can not be resolved: " + builder.toString());
        } else {
            resolvedType.check(initialContext, problemHandler, checkHistory);
        }
        return resolvedType;
    }

    @Override
    public JavaSourceSupport getJavaSourceSupport() {
        return new JavaSourceSupport(this) {
            @Override
            public CodeWriter getCodeWriter(UsagePurpose purpose) {
                return new AdsTypeDeclarationCodeWriter(this, AdsTypeDeclaration.this, purpose);
            }
        };
    }

    public boolean isTypeArgument() {
        return isTypeArgument;
    }

    @Override
    public RadixIcon getIcon() {
        if (typeId != null) {
            return RadixObjectIcon.getForValType(typeId);
        }
        return super.getIcon();
    }

    public boolean equalsTo(AdsDefinition context, AdsTypeDeclaration other) {
        if (other == null) {
            return false;
        }
        if (this.dimension != other.dimension) {
            return false;
        }

        if (this.typeId == other.typeId
                && Utils.equals(this.path, other.path)
                && Utils.equals(this.extStr, other.extStr)
                && this.isTypeArgument == other.isTypeArgument) {
            //simply compare type arguments
            if (this.genericArguments == null && other.genericArguments == null) {
                return true;
            } else {
                if (this.genericArguments != null && other.genericArguments != null) {
                    if (this.genericArguments.equalsTo(context, other.genericArguments)) {
                        return true;
                    }
                }
            }
        } else {
            if (this.typeId == other.typeId) {
                switch (this.typeId) {
                    case USER_CLASS:
                    case INT:
                    case CHAR:
                    case STR:
                        if (!Utils.equals(this.path, other.path)) {
                            if (this.path == null && other.path == null) {
                                return true;
                            }
                            if (this.path != null && other.path != null) {
                                if (this.path.getTargetId() == other.path.getTargetId() && this.path.getTargetId().getPrefix() == EDefinitionIdPrefix.COMMAND) {
                                    return true;
                                } else {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    case XML:
                        if (!Utils.equals(this.path, other.path)) {
                            return false;
                        }
                        if (!Utils.equals(extStr, other.extStr)) {
                            return false;
                        }
                        break;
                }
            } else {
                boolean needsResolution = (this.typeId == EValType.USER_CLASS && other.typeId == EValType.JAVA_CLASS)
                        || (this.typeId == EValType.JAVA_CLASS && other.typeId == EValType.USER_CLASS)
                        || (this.typeId == EValType.USER_CLASS && other.typeId == EValType.PARENT_REF)
                        || (this.typeId == EValType.PARENT_REF && other.typeId == EValType.USER_CLASS)
                        || (this.typeId == EValType.USER_CLASS && other.typeId == EValType.OBJECT)
                        || (this.typeId == EValType.OBJECT && other.typeId == EValType.USER_CLASS);
                if (!needsResolution) {
                    return false;
                }
            }
        }

        AdsType type = resolve(context).get();
        AdsType otherType = other.resolve(context).get();
        boolean typesAreEqual = Utils.equals(type, otherType);
        if (typesAreEqual) {
            TypeArguments thisGenericArguments = this.genericArguments;
            if (thisGenericArguments != null && thisGenericArguments.isEmpty()) {
                thisGenericArguments = null;
            }

            TypeArguments otherGenericArguments = other.genericArguments;
            if (otherGenericArguments != null && otherGenericArguments.isEmpty()) {
                otherGenericArguments = null;
            }

            if (thisGenericArguments != null) {
                if (!thisGenericArguments.equalsTo(context, otherGenericArguments)) {
                    return false;
                }
            } else {
                if (otherGenericArguments != null) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean actualizeRequired() {
        if (isTypeArgument && this.extStr != null && !this.extStr.isEmpty()) {
            return true;
        } else {
            if (this.genericArguments != null && !this.genericArguments.isEmpty()) {
                for (TypeArgument a : genericArguments.arguments) {
                    AdsTypeDeclaration decl = a.getType();
                    if (decl != null && decl.actualizeRequired()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public AdsTypeDeclaration getActualType(AdsClassDef declarationContext, AdsClassDef usageContext) {

        if (isTypeArgument && this.extStr != null && !this.extStr.isEmpty()) {
            return usageContext.getInheritance().getActualArgumentType(declarationContext, extStr);
        } else {
            if (this.genericArguments != null && !this.genericArguments.isEmpty()) {

                if (actualizeRequired()) {
                    AdsTypeDeclaration clone = AdsTypeDeclaration.Factory.newCopy(this);
                    for (TypeArgument a : clone.genericArguments.arguments) {
                        AdsTypeDeclaration decl = a.getType();
                        if (decl != null) {
                            AdsTypeDeclaration another = decl.getActualType(declarationContext, usageContext);
                            if (another != decl) {
                                a.setType(another);
                            }
                        }
                    }
                    return clone;
                } else {
                    return this;
                }
            } else {
                return this;
            }
        }

    }

    /**
     * Gets type of array items
     *
     * @return if {@link isArray}() - type of array items, otherwise null
     */
    public AdsTypeDeclaration getArrayItemType() {
        if (isArray()) {
            AdsTypeDeclaration itemType = AdsTypeDeclaration.Factory.newCopy(this);
            itemType.dimension = 0;

            return itemType;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (typeId != null) {
            builder.append(typeId.name());
        } else {
            builder.append("UNDEFINED");
        }
        builder.append(":");
        if (path != null) {
            builder.append(path.toString());
        }
        if (extStr != null) {
            builder.append(":").append(extStr);
        }
        return builder.toString();
    }
}
