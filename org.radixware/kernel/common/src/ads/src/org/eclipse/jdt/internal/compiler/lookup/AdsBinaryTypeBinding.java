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
package org.eclipse.jdt.internal.compiler.lookup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.ads.syntetics.BaseGenerator;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.radixware.kernel.common.compiler.CompilerUtils;
import org.radixware.kernel.common.compiler.core.lookup.locations.AdsClassFileReader;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.Module;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.types.Id;
//import org.radixware.kernel.common.compiler.core.lookup.PropertyBinding;

public class AdsBinaryTypeBinding extends BinaryTypeBinding {
    
    static final char[] GET_PREFIX = new char[]{'g', 'e', 't'};
    static final char[] SET_PREFIX = new char[]{'g', 'e', 't'};
    public static final int TypeId_JavaMathBigDecimal = 1000;
    public static final int TypeId_JavaSqlTimestamp = 1001;
    public static final int TypeId_JavaMathBaseBigDecimal = TypeIds.T_null + 1;
    public static TypeBinding BASE_BIG_DECIMAL = new BaseTypeBinding(TypeId_JavaMathBaseBigDecimal, "num".toCharArray(), "num".toCharArray());
    public Definition definition;
    private  IBinaryMethod[] binaryMethods;
    private AdsLookupEnvironment environment;
    public HashMap<MethodBinding,IBinaryMethod> methodsInfo;
    
    public Definition getDefinition() {
        if (definition != null && isMemberType()) {//try to look for exact member clas
            if (definition.getOwnerDefinition() instanceof Module && definition instanceof AdsClassDef) {
                ReferenceBinding encolsingType = this.enclosingType;
                
                List<String> names = new LinkedList<>();
                names.add(new String(this.sourceName));
                while (encolsingType != null) {
                    names.add(new String(encolsingType.sourceName));
                    encolsingType = encolsingType.enclosingType();
                }
                AdsDefinition root = (AdsDefinition) definition;
                if (!names.isEmpty()) {
                    String name = names.remove(names.size() - 1);
                    if (root.getId().toString().equals(name)) {
                        while (!names.isEmpty()) {
                            name = names.remove(names.size() - 1);
                            Id id = Id.Factory.loadFrom(name);
                            root = root.findComponentDefinition(id).get();
                            if (root == null) {
                                break;
                            }
                        }
                        if (root != null) {
                            definition = root;
                        }
                    }
                }
                
            }
        }
        return definition;
    }
    
    public AdsBinaryTypeBinding(PackageBinding packageBinding, IBinaryType binaryType, AdsLookupEnvironment environment) {
        super(packageBinding, binaryType, environment);
        this.environment = environment;
        if (binaryType instanceof AdsClassFileReader) {
            this.definition = ((AdsClassFileReader) binaryType).sourceDefinition;
        } else {
            if (binaryType instanceof ClassFileReader){
                binaryMethods = binaryType.getMethods();
            }
            this.definition = null;
        }
    }
    
    @Override
    public void computeId() {
        super.computeId();
        if (id == TypeIds.NoId) {//additional check
            if (this.compoundName.length == 3) {
                if (CharOperation.equals(compoundName, BaseGenerator.JAVAMATHBIGDECIMAL_TYPE_NAME)) {
                    id = TypeId_JavaMathBigDecimal;
                } else if (CharOperation.equals(compoundName, BaseGenerator.JAVASQLTIMESTAMP_TYPE_NAME)) {
                    id = TypeId_JavaSqlTimestamp;
                }
            }
        }
    }
    
    @Override
    public FieldBinding[] availableFields() {
        if ((this.tagBits & TagBits.AreFieldsComplete) == 0) {
            computeProperties();
        }
        return super.availableFields();
    }
    private boolean propertiesComputed = false;
    
    private void computeProperties() {
        if (propertiesComputed) {
            return;
        }
        propertiesComputed = true;

        //  MethodBinding[] methods = methods();
        //   List<MethodBinding> getters = new LinkedList<>();
//        Map<String, List<MethodBinding>> setters = new HashMap<>();
//        for (int i = 0; i < methods.length; i++) {
//            MethodBinding method = methods[i];
//            if (CharOperation.prefixEquals(GET_PREFIX, method.selector) && method.parameters.length == 0 && method.returnType.id != TypeIds.T_void) {
//                getters.add(method);
//            }
//        }
//        List<FieldBinding> properties = new ArrayList<>(getters.size());
//        for (MethodBinding binding : getters) {
//         //   PropertyBinding property = new PropertyBinding(this, CharOperation.subarray(binding.selector, 3, -1), binding, null);
//            properties.add(property);
//        }
        //int size = properties.size();
//        if (fields != null && fields.length > 0) {
//            size += fields.length;
//        }
//        FieldBinding[] newFields = new FieldBinding[size];
//        if (fields != null && fields.length > 0) {
//            System.arraycopy(fields, 0, newFields, 0, fields.length);
//            for (int i = fields.length, j = 0; i < newFields.length; i++, j++) {
//                newFields[i] = properties.get(j);
//            }
//        } else {
//            properties.toArray(newFields);
//        }
//        fields = newFields;
        tagBits &= ~TagBits.AreFieldsComplete;
        tagBits &= ~TagBits.AreFieldsSorted;
    }
    
    @Override
    public FieldBinding[] fields() {
        if ((this.tagBits & TagBits.AreFieldsComplete) == 0) {
            computeProperties();
        }
        return super.fields();
    }
    
    @Override
    public FieldBinding getField(char[] fieldName, boolean needResolve) {
        if ((this.tagBits & TagBits.AreFieldsComplete) == 0) {
            computeProperties();
        }
        return super.getField(fieldName, needResolve);
    }

//    @Override
//    public boolean isCompatibleWith(TypeBinding otherType, Scope captureScope) {
//        if (!super.isCompatibleWith(otherType, captureScope)) {
//            return isRadixCompatibleWith(otherType, captureScope);
//        } else {
//            return true;
//        }
//    }
    public boolean isRadixCompatibleWith(TypeBinding otherType, Scope captureScope) {
        switch (otherType.id) {
            case TypeIds.T_JavaLangLong:
                switch (this.id) {
                    case TypeIds.T_JavaLangByte:
                    case TypeIds.T_JavaLangCharacter:
                    case TypeIds.T_JavaLangShort:
                    case TypeIds.T_JavaLangInteger:
                    case TypeIds.T_JavaLangLong:
                        return true;
                    default:
                        return false;
                }
            case TypeIds.T_JavaLangFloat:
                switch (this.id) {
                    case TypeIds.T_JavaLangByte:
                    case TypeIds.T_JavaLangCharacter:
                    case TypeIds.T_JavaLangShort:
                    case TypeIds.T_JavaLangInteger:
                    case TypeIds.T_JavaLangLong:
                    case TypeIds.T_JavaLangFloat:
                        return true;
                    default:
                        return false;
                }
            case TypeIds.T_JavaLangDouble:
                switch (this.id) {
                    case TypeIds.T_JavaLangByte:
                    case TypeIds.T_JavaLangCharacter:
                    case TypeIds.T_JavaLangShort:
                    case TypeIds.T_JavaLangInteger:
                    case TypeIds.T_JavaLangLong:
                    case TypeIds.T_JavaLangFloat:
                    case TypeIds.T_JavaLangDouble:
                        return true;
                    default:
                        return false;
                }
            case TypeId_JavaMathBigDecimal:
                switch (this.id) {
                    case TypeIds.T_JavaLangByte:
                    case TypeIds.T_JavaLangCharacter:
                    case TypeIds.T_JavaLangShort:
                    case TypeIds.T_JavaLangInteger:
                    case TypeIds.T_JavaLangLong:
                    case TypeIds.T_JavaLangFloat:
                    case TypeIds.T_JavaLangDouble:
                    case TypeId_JavaMathBigDecimal:
                        return true;
                    default:
                        return false;
                }
            default:
                return false;
        }
    }
    
    @Override
    public char[] readableName() {
        if (definition != null) {
            String name = CompilerUtils.getTypeDisplayName(definition, compoundName);
            if (name == null) {
                return super.readableName();
            } else {
                return name.toCharArray();
            }
        } else {
            return super.readableName();
        }
    }
    
    @Override
    public char[] shortReadableName() {
        if (definition != null) {
            String name = CompilerUtils.getTypeDisplayName(definition, compoundName);
            if (name == null) {
                return super.readableName();
            } else {
                return name.toCharArray();
            }
        } else {
            return super.shortReadableName();
        }
    }
    
    public IBinaryMethod findMethodInfo(MethodBinding binding){
        if (binaryMethods == null){
            return null;
        }
        
        if (methodsInfo == null){
            initMethodsInfo();
        }
        return methodsInfo.get(binding);
        
    }
    
    private void initMethodsInfo(){
        if (binaryMethods == null){
            return;
        }
        
        methodsInfo =  new HashMap<>(); 
        for (int i = 0; i < binaryMethods.length; i++){
            MethodBinding binding = findMethod(binaryMethods[i].getMethodDescriptor(),binaryMethods[i].getSelector());
            if (binding != null){
                methodsInfo.put(binding, binaryMethods[i]);
            }  
        }
    }
    
    private MethodBinding findMethod(char[] methodDescriptor, char[] selector) {
        if (selector == null) {
            return null;
        }
        int index = -1;
        while (methodDescriptor[++index] != '(') {
        }
        TypeBinding[] parameters = Binding.NO_PARAMETERS;
        int numOfParams = 0;
        char nextChar;
        int paramStart = index;
        while ((nextChar = methodDescriptor[++index]) != ')') {
            if (nextChar != '[') {
                numOfParams++;
                if (nextChar == 'L') {
                    while ((nextChar = methodDescriptor[++index]) != ';') {
                    }
                }
            }
        }

        if (numOfParams > 0) {
            parameters = new TypeBinding[numOfParams];

            index = paramStart + 1;
            int end = paramStart;
            for (int i = 0; i < numOfParams; i++) {

                while ((nextChar = methodDescriptor[++end]) == '[') {
                }

                if (nextChar == 'L') {
                    while ((nextChar = methodDescriptor[++end]) != ';') {
                    }
                }

                TypeBinding param = this.environment.getTypeFromSignature(methodDescriptor, index, end, false, this, null);

                if (param instanceof UnresolvedReferenceBinding) {

                    param = resolveType(param, this.environment, true);
                }
                parameters[i] = param;
                index = end + 1;
            }
        }

        int parameterLength = parameters.length;

        MethodBinding[] methods2 = getMethods(selector, parameterLength);
        loop:
        for (int i = 0, max = methods2.length; i < max; i++) {
            MethodBinding currentMethod = methods2[i];
            TypeBinding[] parameters2 = currentMethod.parameters;
            int currentMethodParameterLength = parameters2.length;
            if (parameterLength == currentMethodParameterLength) {
                for (int j = 0; j < currentMethodParameterLength; j++) {
                    if (parameters[j] != parameters2[j] && parameters[j].erasure() != parameters2[j].erasure()) {
                        continue loop;
                    }
                }
                return currentMethod;

            }
        }
        return null;
    }

    public MethodBinding getHighestOverridenMethod(MethodBinding method) {
        MethodBinding bestMethod = method;
        ReferenceBinding currentType = this;
        if (method.isConstructor()) {
            return bestMethod;
        }
        MethodVerifier verifier = environment.methodVerifier();
        // walk superclasses
        ReferenceBinding[] interfacesToVisit = null;
        int nextPosition = 0;
        do {
            MethodBinding[] superMethods = currentType.getMethods(method.selector);
            for (int i = 0, length = superMethods.length; i < length; i++) {
                if (verifier.doesMethodOverride(method, superMethods[i])) {
                    bestMethod = superMethods[i];
                    break;
                }
            }
            ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
            if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
                if (interfacesToVisit == null) {
                    interfacesToVisit = itsInterfaces;
                    nextPosition = interfacesToVisit.length;
                } else {
                    int itsLength = itsInterfaces.length;
                    if (nextPosition + itsLength >= interfacesToVisit.length) {
                        System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
                    }
                    nextInterface:
                    for (int a = 0; a < itsLength; a++) {
                        ReferenceBinding next = itsInterfaces[a];
                        for (int b = 0; b < nextPosition; b++) {
                            if (next == interfacesToVisit[b]) {
                                continue nextInterface;
                            }
                        }
                        interfacesToVisit[nextPosition++] = next;
                    }
                }
            }
        } while ((currentType = currentType.superclass()) != null);
        if (bestMethod.declaringClass.id == TypeIds.T_JavaLangObject) {
            return bestMethod;
        }
        // walk superinterfaces
        for (int i = 0; i < nextPosition; i++) {
            currentType = interfacesToVisit[i];
            MethodBinding[] superMethods = currentType.getMethods(method.selector);
            for (int j = 0, length = superMethods.length; j < length; j++) {
                MethodBinding superMethod = superMethods[j];
                if (verifier.doesMethodOverride(method, superMethod)) {
                    TypeBinding bestReturnType = bestMethod.returnType;
                    if (bestReturnType == superMethod.returnType
                            || bestMethod.returnType.findSuperTypeOriginatingFrom(superMethod.returnType) != null) {
                        bestMethod = superMethod;
                    }
                    break;
                }
            }
            ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
            if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
                int itsLength = itsInterfaces.length;
                if (nextPosition + itsLength >= interfacesToVisit.length) {
                    System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
                }
                nextInterface:
                for (int a = 0; a < itsLength; a++) {
                    ReferenceBinding next = itsInterfaces[a];
                    for (int b = 0; b < nextPosition; b++) {
                        if (next == interfacesToVisit[b]) {
                            continue nextInterface;
                        }
                    }
                    interfacesToVisit[nextPosition++] = next;
                }
            }
        }
        return bestMethod;
    }

}
