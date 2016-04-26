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

import org.radixware.kernel.common.compiler.lookup.AdsWorkspace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.jdt.core.compiler.CharOperation;
import org.eclipse.jdt.internal.compiler.env.AccessRestriction;
import org.eclipse.jdt.internal.compiler.env.IBinaryType;
import org.eclipse.jdt.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.util.HashtableOfType;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;
import org.radixware.kernel.common.compiler.core.AdsLookup;
import org.radixware.kernel.common.compiler.core.lookup.AdsNameEnvironment;
import org.radixware.kernel.common.defs.Definition;
import org.radixware.kernel.common.defs.ads.src.JavaSourceSupport;
import org.radixware.kernel.common.repository.Layer;

public class AdsLookupEnvironment extends LookupEnvironment {

    private PackageBinding radixTypes;
    private final HashtableOfType knownRadixTypes = new HashtableOfType();
    private final ERuntimeEnvironmentType envType;
    private Map<ReferenceBinding, ParameterizedTypeBinding[]> uniqueParameterizedTypeBindings;
    private Map<ReferenceBinding, RawTypeBinding[]> uniqueRawTypeBindings;
    private Map<ReferenceBinding, WildcardBinding[]> uniqueWildcardBindings;

    public AdsLookupEnvironment(Layer context, ERuntimeEnvironmentType env, CompilerOptions options, ProblemReporter problemReporter) {
        this(context, env, options, problemReporter, false, false);
    }

    public AdsLookupEnvironment(Layer context, ERuntimeEnvironmentType env, CompilerOptions options, ProblemReporter problemReporter, boolean cacheBinaryTypes, boolean useCachedBinaries) {
        this(context, env, options, new AdsTypeRequestor(cacheBinaryTypes, useCachedBinaries), problemReporter);
    }

    public AdsLookupEnvironment(Layer context, ERuntimeEnvironmentType env, CompilerOptions options, AdsTypeRequestor requestor, ProblemReporter problemReporter) {
        this(new AdsNameEnvironment(context, env), options, requestor, problemReporter);
    }

    public AdsLookupEnvironment(AdsNameEnvironment env, CompilerOptions options, AdsTypeRequestor requestor, ProblemReporter problemReporter) {
        super(requestor, options, problemReporter, env);
        requestor.attachLookupEnvironment(this);
        // getRadixTypesPackage();
        envType = env.getEnvironment();
        uniqueParameterizedTypeBindings = new HashMap<>();
        uniqueRawTypeBindings = new HashMap<>(10);
        uniqueWildcardBindings = new HashMap<>(10);
    }

    public WildcardBinding createWildcard(ReferenceBinding genericType, int rank, TypeBinding bound, TypeBinding[] otherBounds, int boundKind) {
        // cached info is array of already created wildcard  types for this type
        if (genericType == null) // pseudo wildcard denoting composite bounds for lub computation
        {
            genericType = ReferenceBinding.LUB_GENERIC;
        }
        WildcardBinding[] cachedInfo = this.uniqueWildcardBindings.get(genericType);
        boolean needToGrow = false;
        int index = 0;
        if (cachedInfo != null) {
            nextCachedType:
            // iterate existing wildcards for reusing one with same information if any
            for (int max = cachedInfo.length; index < max; index++) {
                WildcardBinding cachedType = cachedInfo[index];
                if (cachedType == null) {
                    break nextCachedType;
                }
                if (cachedType.genericType != genericType) {
                    continue nextCachedType; // remain of unresolved type
                }
                if (cachedType.rank != rank) {
                    continue nextCachedType;
                }
                if (cachedType.boundKind != boundKind) {
                    continue nextCachedType;
                }
                if (cachedType.bound != bound) {
                    continue nextCachedType;
                }
                if (cachedType.otherBounds != otherBounds) {
                    int cachedLength = cachedType.otherBounds == null ? 0 : cachedType.otherBounds.length;
                    int length = otherBounds == null ? 0 : otherBounds.length;
                    if (cachedLength != length) {
                        continue nextCachedType;
                    }
                    for (int j = 0; j < length; j++) {
                        if (cachedType.otherBounds[j] != otherBounds[j]) {
                            continue nextCachedType;
                        }
                    }
                }
                // all match, reuse current
                return cachedType;
            }
            needToGrow = true;
        } else {
            cachedInfo = new WildcardBinding[10];
            this.uniqueWildcardBindings.put(genericType, cachedInfo);
        }
        // grow cache ?
        int length = cachedInfo.length;
        if (needToGrow && index == length) {
            System.arraycopy(cachedInfo, 0, cachedInfo = new WildcardBinding[length * 2], 0, length);
            this.uniqueWildcardBindings.put(genericType, cachedInfo);
        }
        // add new binding
        WildcardBinding wildcard = new WildcardBinding(genericType, rank, bound, otherBounds, boundKind, this);
        cachedInfo[index] = wildcard;
        return wildcard;
    }

    @Override
    void updateCaches(UnresolvedReferenceBinding unresolvedType, ReferenceBinding resolvedType) {
        ParameterizedTypeBinding[] typesForKey = this.uniqueParameterizedTypeBindings.get(unresolvedType);
        if (typesForKey != null) { // update the key
            this.uniqueParameterizedTypeBindings.remove(unresolvedType);
            this.uniqueParameterizedTypeBindings.put(resolvedType, typesForKey);
        }
        RawTypeBinding[] typesForRawKey = this.uniqueRawTypeBindings.get(unresolvedType);
        if (typesForRawKey != null) { // update the key
            this.uniqueRawTypeBindings.remove(unresolvedType);
            this.uniqueRawTypeBindings.put(resolvedType, typesForRawKey);
        }

        WildcardBinding[] typesForWCKey = this.uniqueWildcardBindings.get(unresolvedType);
        if (typesForWCKey != null) { // update the key
            this.uniqueWildcardBindings.remove(unresolvedType);
            this.uniqueWildcardBindings.put(resolvedType, typesForWCKey);
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.uniqueParameterizedTypeBindings = new HashMap<>();
        this.uniqueRawTypeBindings = new HashMap<>();
        this.uniqueWildcardBindings = new HashMap<>();
    }

    public void buildClassPath(AdsWorkspace ws) {
        ((AdsNameEnvironment) this.nameEnvironment).ensureClassPath(ws);
    }

    @Override
    ReferenceBinding askForType(PackageBinding packageBinding, char[] name) {
        if (packageBinding == radixTypes) {
            final int index = AdsLookup.isRadixSystemType(name);
            if (index >= 0) {
                final char[] mirror = AdsLookup.RADIX_TYPE_MIRRORS[index];
                ReferenceBinding binding = knownRadixTypes == null ? null : knownRadixTypes.get(mirror);
                if (binding != null) {
                    return binding;
                } else {
                    binding = super.askForType(AdsLookup.RADIX_TYPE_MIRRORS_FULL[index]);
                    if (binding != null) {
                        knownRadixTypes.put(mirror, binding);
                        return binding;
                    }
                }
            }
        }

        final ReferenceBinding binding = super.askForType(packageBinding, name);
        return binding;
    }
    private List<MissingTypeBinding> myMissingTypes;

    private PackageBinding convertName2Package(char[][] constantPoolName, boolean isMissing) {
        if (constantPoolName.length == 1) {
            return this.defaultPackage;
        }

        PackageBinding packageBinding = getPackage0(constantPoolName[0]);
        if (packageBinding == null || packageBinding == TheNotFoundPackage) {
            packageBinding = new PackageBinding(constantPoolName[0], this);
            if (isMissing) {
                packageBinding.tagBits |= TagBits.HasMissingType;
            }
            this.knownPackages.put(constantPoolName[0], packageBinding);
        }

        for (int i = 1, length = constantPoolName.length - 1; i < length; i++) {
            PackageBinding parent = packageBinding;
            if ((packageBinding = parent.getPackage0(constantPoolName[i])) == null || packageBinding == TheNotFoundPackage) {
                packageBinding = new PackageBinding(CharOperation.subarray(constantPoolName, 0, i + 1), parent, this);
                if (isMissing) {
                    packageBinding.tagBits |= TagBits.HasMissingType;
                }
                parent.addPackage(packageBinding);
            }
        }
        return packageBinding;
    }

    @Override
    public MissingTypeBinding createMissingType(PackageBinding packageBinding, char[][] compoundName) {
        NameEnvironmentAnswer answer = this.nameEnvironment.findType(compoundName);
        if (answer != null && answer.isBinaryType() && answer.getBinaryType() instanceof AdsMissingBinaryType) {
            if (packageBinding == null) {
                packageBinding = convertName2Package(compoundName, true /* missing */);
                if (packageBinding == TheNotFoundPackage) {
                    packageBinding = this.defaultPackage;
                }
            }
            AdsMissingTypeBinding missingType = new AdsMissingTypeBinding(((AdsMissingBinaryType) answer.getBinaryType()).referencedDef, packageBinding, compoundName, this);
            if (missingType.id != TypeIds.T_JavaLangObject) {
                // make Object be its superclass - it could in turn be missing as well
                ReferenceBinding objectType = getType(TypeConstants.JAVA_LANG_OBJECT);
                if (objectType == null) {
                    objectType = createMissingType(null, TypeConstants.JAVA_LANG_OBJECT);	// create a proxy for the missing Object type
                }
                missingType.setMissingSuperclass(objectType);
            }
            packageBinding.addType(missingType);
            if (this.myMissingTypes == null) {
                this.myMissingTypes = new ArrayList<>(3);
            }
            this.myMissingTypes.add(missingType);
            return missingType;
        } else {
            return super.createMissingType(packageBinding, compoundName);
        }
    }

    @Override
    public ParameterizedTypeBinding createParameterizedType(ReferenceBinding genericType, TypeBinding[] typeArguments, ReferenceBinding enclosingType) {
        // cached info is array of already created parameterized types for this type
        ParameterizedTypeBinding[] cachedInfo = this.uniqueParameterizedTypeBindings.get(genericType);
        int argLength = typeArguments == null ? 0 : typeArguments.length;
        boolean needToGrow = false;
        int index = 0;
        if (cachedInfo != null) {
            nextCachedType:
            // iterate existing parameterized for reusing one with same type arguments if any
            for (int max = cachedInfo.length; index < max; index++) {
                ParameterizedTypeBinding cachedType = cachedInfo[index];
                if (cachedType == null) {
                    break nextCachedType;
                }
                if (cachedType.actualType() != genericType) {
                    continue nextCachedType; // remain of unresolved type
                }
                if (cachedType.enclosingType() != enclosingType) {
                    continue nextCachedType;
                }
                TypeBinding[] cachedArguments = cachedType.arguments;
                int cachedArgLength = cachedArguments == null ? 0 : cachedArguments.length;
                if (argLength != cachedArgLength) {
                    continue nextCachedType; // would be an error situation (from unresolved binaries)
                }
                for (int j = 0; j < cachedArgLength; j++) {
                    if (typeArguments[j] != cachedArguments[j]) {
                        continue nextCachedType;
                    }
                }
                // all arguments match, reuse current
                return cachedType;
            }
            needToGrow = true;
        } else {
            cachedInfo = new ParameterizedTypeBinding[5];
            this.uniqueParameterizedTypeBindings.put(genericType, cachedInfo);
        }
        // grow cache ?
        int length = cachedInfo.length;
        if (needToGrow && index == length) {
            System.arraycopy(cachedInfo, 0, cachedInfo = new ParameterizedTypeBinding[length * 2], 0, length);
            this.uniqueParameterizedTypeBindings.put(genericType, cachedInfo);
        }
        // add new binding
        ParameterizedTypeBinding parameterizedType = new AdsParameterizedTypeBinding(genericType, typeArguments, enclosingType, this);
        cachedInfo[index] = parameterizedType;
        return parameterizedType;
    }

    @Override
    public RawTypeBinding createRawType(ReferenceBinding genericType, ReferenceBinding enclosingType) {
        // cached info is array of already created raw types for this type
        RawTypeBinding[] cachedInfo = this.uniqueRawTypeBindings.get(genericType);
        boolean needToGrow = false;
        int index = 0;
        if (cachedInfo != null) {
            nextCachedType:
            // iterate existing parameterized for reusing one with same type arguments if any
            for (int max = cachedInfo.length; index < max; index++) {
                RawTypeBinding cachedType = cachedInfo[index];
                if (cachedType == null) {
                    break nextCachedType;
                }
                if (cachedType.actualType() != genericType) {
                    continue nextCachedType; // remain of unresolved type
                }
                if (cachedType.enclosingType() != enclosingType) {
                    continue nextCachedType;
                }
                // all enclosing type match, reuse current
                return cachedType;
            }
            needToGrow = true;
        } else {
            cachedInfo = new RawTypeBinding[1];
            this.uniqueRawTypeBindings.put(genericType, cachedInfo);
        }
        // grow cache ?
        int length = cachedInfo.length;
        if (needToGrow && index == length) {
            System.arraycopy(cachedInfo, 0, cachedInfo = new RawTypeBinding[length * 2], 0, length);
            this.uniqueRawTypeBindings.put(genericType, cachedInfo);
        }
        // add new binding
        RawTypeBinding rawType = new AdsRawTypeBinding(genericType, enclosingType, this);
        cachedInfo[index] = rawType;
        return rawType;

    }

    boolean isMissingType(char[] typeName) {
        for (int i = this.myMissingTypes == null ? 0 : this.myMissingTypes.size(); --i >= 0;) {
            MissingTypeBinding missingType = (MissingTypeBinding) this.myMissingTypes.get(i);
            if (CharOperation.equals(missingType.sourceName, typeName)) {
                return true;
            }
        }
        return super.isMissingType(typeName);
    }

    public ReferenceBinding findType(Definition determinant, ERuntimeEnvironmentType envType, boolean meta, String suffix) {
        char[][] packageName = JavaSourceSupport.getPackageNameComponents(determinant, JavaSourceSupport.UsagePurpose.getPurpose(envType, JavaSourceSupport.CodeType.EXCUTABLE));
        PackageBinding packageBinding = getPackage(packageName);
        if (packageBinding == null) {
            packageBinding = this.defaultPackage;
        }

        if (!packageBinding.isValidBinding()) {
            return null;
        }

        ReferenceBinding type;
        if (suffix != null) {
            type = packageBinding.getType(suffix.toCharArray());
        } else {
            if (meta) {
                type = packageBinding.getType((determinant.getId().toString() + "_mi").toCharArray());
            } else {
                type = packageBinding.getType(determinant.getId().toCharArray());
            }
        }
        return type;
    }

    public ReferenceBinding findType(Definition determinant, ERuntimeEnvironmentType env, String suffix) {
        return findType(determinant, env, false, suffix);
    }

    public ReferenceBinding findType(Definition determinant, ERuntimeEnvironmentType env, boolean meta) {
        return findType(determinant, env, meta, null);
    }
    private List<AdsCompilationUnitDeclaration> acceptedUnits = new ArrayList<>();

    public void acceptCompilationUnit(AdsCompilationUnit unit) {

        final AdsCompilationUnitDeclaration declaration = unit.declaration;
        if (declaration != null) {
            declaration.beginInitialization();
            declaration.completeInitialization();
            acceptedUnits.add(declaration);
        }
    }

    public List<AdsCompilationUnitDeclaration> cleanupAcceptedUnits() {
        final List<AdsCompilationUnitDeclaration> list = new ArrayList<>(acceptedUnits);
        acceptedUnits.clear();
        return list;
    }

    public AdsNameEnvironment getNameEnvironment() {
        return (AdsNameEnvironment) nameEnvironment;
    }

    public final PackageBinding getRadixTypesPackage() {
        if (radixTypes != null) {
            return radixTypes;
        }
        PackageBinding importBinding = getTopLevelPackage(AdsLookup.RADIX_TYPES_PACKAGE[0]);
        int i = 1;

        char[][] compoundName = new char[][]{AdsLookup.RADIX_TYPES_PACKAGE[0]};
        while (importBinding != null && i < 5) {
            compoundName = CharOperation.arrayConcat(compoundName, AdsLookup.RADIX_TYPES_PACKAGE[i]);
            PackageBinding next = importBinding.getPackage0(AdsLookup.RADIX_TYPES_PACKAGE[i]);
            if (next == null) {
                next = new PackageBinding(compoundName, importBinding, this);
                importBinding.addPackage(next);
            }
            importBinding = next;
            i++;
        }

        if (importBinding == null || !importBinding.isValidBinding()) {
            // create a proxy for the missing BinaryType
            this.problemReporter.isClassPathCorrect(
                    AdsLookup.RADIX_TYPES_PACKAGE,
                    null,
                    missingClassFileLocation);
            BinaryTypeBinding missingObject = createMissingType(null, AdsLookup.RADIX_TYPES_PACKAGE);
            importBinding = missingObject.fPackage;
        }
        return radixTypes = (PackageBinding) importBinding;
    }

    public PackageBinding getPackage(char[][] name) {
        if (name.length == 0) {
            return LookupEnvironment.TheNotFoundPackage;
        }
        PackageBinding p = getTopLevelPackage(name[0]);
        int i = 1;
        while (p != null && i < name.length) {
            p = p.getPackage(name[i]);
            i++;
        }
        return p;
    }

    @Override
    public BinaryTypeBinding createBinaryTypeFrom(IBinaryType binaryType, PackageBinding packageBinding, boolean needFieldsAndMethods, AccessRestriction accessRestriction) {
        BinaryTypeBinding binaryBinding = new AdsBinaryTypeBinding(packageBinding, binaryType, this);

        // resolve any array bindings which reference the unresolvedType
        ReferenceBinding cachedType = packageBinding.getType0(binaryBinding.compoundName[binaryBinding.compoundName.length - 1]);
        if (cachedType != null) { // update reference to unresolved binding after having read classfile (knows whether generic for raw conversion)
            if (cachedType instanceof UnresolvedReferenceBinding) {
                ((UnresolvedReferenceBinding) cachedType).setResolvedType(binaryBinding, this);
            } else {
                if (cachedType.isBinaryBinding()) // sanity check... at this point the cache should ONLY contain unresolved types
                {
                    return (BinaryTypeBinding) cachedType;
                }
                // it is possible with a large number of source files (exceeding AbstractImageBuilder.MAX_AT_ONCE) that a member type can be in the cache as an UnresolvedType,
                // but because its enclosingType is resolved while its created (call to BinaryTypeBinding constructor), its replaced with a source type
                return null;
            }
        }
        packageBinding.addType(binaryBinding);
        setAccessRestriction(binaryBinding, accessRestriction);
        // need type annotations before processing methods (for @NonNullByDefault)
        if (this.globalOptions.isAnnotationBasedNullAnalysisEnabled) {
            binaryBinding.scanTypeForNullDefaultAnnotation(binaryType, packageBinding, binaryBinding);
        }
        binaryBinding.cachePartsFrom(binaryType, needFieldsAndMethods);
        return binaryBinding;
    }

    public TypeBinding computeBoxingType(TypeBinding type) {
        switch (type.id) {
            case AdsBinaryTypeBinding.TypeId_JavaMathBigDecimal:
                return AdsBinaryTypeBinding.BASE_BIG_DECIMAL;
            default:
                return super.computeBoxingType(type);
        }
    }
}
