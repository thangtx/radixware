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

import java.util.ArrayList;
import java.util.List;
import org.radixware.kernel.common.defs.RadixObject;
import org.radixware.kernel.common.defs.ads.AdsDefinition;
import org.radixware.kernel.common.defs.ads.clazz.AdsClassDef;
import org.radixware.kernel.common.defs.ads.clazz.AdsExceptionClassDef;
import org.radixware.kernel.common.defs.ads.clazz.members.AdsMethodThrowsList.ThrowsListItem;
import org.radixware.kernel.common.defs.ads.command.AdsCommandDef;
import org.radixware.kernel.common.defs.ads.type.AdsClassType;
import org.radixware.kernel.common.defs.ads.type.AdsType;
import org.radixware.kernel.common.defs.ads.type.AdsTypeDeclaration;
import org.radixware.kernel.common.enums.ERuntimeEnvironmentType;


public class ProfileUtilities {

    public static boolean applyProfileToMethod(AdsTypeDeclaration[] types, String[] names, AdsMethodDef method) {
        if (names.length != types.length - 1) {
            return false;
        }
        AdsMethodDef.Profile methodProfile = method.getProfile();
        methodProfile.getParametersList().clear();
        for (int i = 0; i < names.length - 1; i++) {
            methodProfile.getParametersList().add(MethodParameter.Factory.newInstance(names[i], types[i]));
        }
        methodProfile.getReturnValue().setType(types[names.length]);
        return true;
    }

    public static void computeCommandHandlerProfileAndArgumentNames(AdsCommandDef command, ERuntimeEnvironmentType env, List<AdsTypeDeclaration> profile, List<String> argumentNames) {
        AdsCommandHandlerMethodDef.computeProfile(command, env, profile, argumentNames);
    }

    public static AdsTypeDeclaration[] commandHandlerProfile(AdsCommandDef command, ERuntimeEnvironmentType env) {
        ArrayList<AdsTypeDeclaration> profile = new ArrayList<AdsTypeDeclaration>();
        ArrayList<String> names = new ArrayList<String>();
        computeCommandHandlerProfileAndArgumentNames(command, env, profile, names);
        AdsTypeDeclaration[] result = new AdsTypeDeclaration[profile.size()];
        profile.toArray(result);
        return result;
    }

    public static AdsTypeDeclaration[] convertToContext(AdsTypeDeclaration[] params, AdsClassDef context, AdsClassDef declarator) {
        AdsTypeDeclaration[] newParams = new AdsTypeDeclaration[params.length];
        for (int i = 0; i < params.length; i++) {
            newParams[i] = params[i].getActualType(declarator, context);
        }
        return newParams;
    }

    public static class ProfileCompareResults {

        public final ProfileCompareError returnTypeError;
        public final List<ProfileCompareError> problems;
        private final int state;

        public ProfileCompareResults(ProfileCompareError returnTypeError, List<ProfileCompareError> problems, int state) {
            this.returnTypeError = returnTypeError;
            this.problems = problems;
            this.state = state;
        }

        public int getMissingParameterCount() {
            return state < 0 ? -state : 0;
        }

        public int getRedundantParameterCount() {
            return state > 0 ? state : 0;
        }

        public boolean ok() {
            return state == 0 && returnTypeError == null && problems.isEmpty();
        }
    }

    public static class ProfileCompareError {

        public final int paramIndex;
        public final AdsTypeDeclaration got;
        public final AdsTypeDeclaration expected;

        ProfileCompareError(int paramIndex, AdsTypeDeclaration got, AdsTypeDeclaration expected) {
            this.paramIndex = paramIndex;
            this.got = got;
            this.expected = expected;
        }
    }

    /**
     * compare given method profile arrays (in form argType1 argType2 argType3....returnType)
     * Returns null if profiles are equal
     */
    public static ProfileCompareResults compareProfiles(AdsDefinition context, AdsTypeDeclaration[] what, AdsTypeDeclaration[] with) {
        return compareProfiles(context, what, with, false);
    }

    public static ProfileCompareResults compareProfiles(AdsDefinition context, AdsTypeDeclaration[] what, AdsTypeDeclaration[] with, boolean forOverrideSuite) {
        List<ProfileCompareError> problems = new ArrayList<ProfileCompareError>();
        if (what == null || with == null) {
            throw new IllegalArgumentException();
        }
        if (what.length == 0 || with.length == 0) {
            throw new IllegalArgumentException();
        }
        if (what.length != with.length) {
            if (what.length > with.length) {
                for (int i = 0; i < with.length - 1; i++) {
                    if (!with[i].equalsTo(context, what[i])) {
                        problems.add(new ProfileCompareError(i, what[i], with[i]));
                    }
                }
                for (int i = with.length - 1; i < what.length - 1; i++) {
                    problems.add(new ProfileCompareError(i, what[i], null));
                }
            } else {
                for (int i = 0; i < what.length - 1; i++) {
                    if (!with[i].equalsTo(context, what[i])) {
                        problems.add(new ProfileCompareError(i, what[i], with[i]));
                    }
                }
                for (int i = what.length - 1; i < with.length - 1; i++) {
                    problems.add(new ProfileCompareError(i, null, with[i]));
                }
            }
            if (!with[with.length - 1].equalsTo(context, what[what.length - 1])) {
                return new ProfileCompareResults(new ProfileCompareError(-1, what[what.length - 1], with[with.length - 1]), problems, what.length - with.length);
            } else {
                return new ProfileCompareResults(null, problems, what.length - with.length);
            }
        } else {
            ProfileCompareError rt = null;
            for (int i = 0; i < what.length; i++) {
                if (!with[i].equalsTo(context, what[i])) {
                    if (i == what.length - 1) {
                        if(forOverrideSuite){
                            AdsType higher = what[i].resolve(context).get();
                            AdsType lower = with[i].resolve(context).get();
                            if(higher != null && higher.isSubclassOf(lower)){
                                continue;
                            } else {
                                if (!(higher instanceof AdsClassType) || !(lower instanceof AdsClassType)) {
                                    continue;
                                }
                            }
                        }
                        rt = new ProfileCompareError(i, what[i], with[i]);
                    } else {
                        problems.add(new ProfileCompareError(i, what[i], with[i]));
                    }
                }
            }
            return new ProfileCompareResults(rt, problems, 0);
        }
    }

    /**
     * Performs method profile synchroniaztion to fix profile mismatches in overriding/overwriting method
     */
    public static boolean syncMethodProfileToOverriden(AdsMethodDef method) {
        return method.getProfile().syncToOvr();
    }
    
    public static boolean isCorrectExeptionInThrowList(AdsExceptionClassDef exceptionClass, AdsMethodDef method, List<AdsMethodThrowsList.ThrowsListItem> overThrowsList){
        for (AdsMethodThrowsList.ThrowsListItem item : overThrowsList) {
            AdsType exeptionType = item.getException().resolve(method).get();
            if (exeptionType instanceof AdsClassType) {
                AdsClassDef throwListItemClass = ((AdsClassType) exeptionType).getSource();
                if (throwListItemClass instanceof AdsExceptionClassDef) {
                    if (throwListItemClass == exceptionClass) {
                        return true;
                    }
                    AdsTypeDeclaration superClassRef = exceptionClass.getInheritance().getSuperClassRef();
                    if (superClassRef != null){
                        AdsType superClassType = superClassRef.resolve(method).get();
                        while (superClassType != null) {
                            if (superClassType instanceof AdsClassType) {
                                AdsClassDef superClass = ((AdsClassType) superClassType).getSource();
                                if (throwListItemClass == superClass) {
                                    return true;
                                }
                                superClassRef = superClass.getInheritance().getSuperClassRef();
                                if (superClassRef != null){
                                    superClassType = superClassRef.resolve(method).get();
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }    
                }
            }
        }
        return false;
    }

    public static RadixObject getDescriptionInheritanceOwner(boolean inherited, AdsMethodDef owner, RadixObject member) {
        if (inherited) {
            if (owner == null){
                return null;
            }
            AdsMethodDef ovr = owner.getHierarchy().findOverwritten().get();
            while (ovr != null) {
                RadixObject ovrMember = getDescriptionOwner(ovr, member, owner);
                if (ovrMember != null) {
                    return ovrMember;
                }
                ovr = ovr.getHierarchy().findOverwritten().get();
            }
            ovr = owner.getHierarchy().findOverridden().get();
            while (ovr != null) {
                RadixObject ovrMember = getDescriptionOwner(ovr, member, owner);
                if (ovrMember != null) {
                    return ovrMember;
                }
                ovr = ovr.getHierarchy().findOverridden().get();
            }
            
        }
        return member;
    }
    
    private static RadixObject getDescriptionOwner(AdsMethodDef m, RadixObject member, AdsMethodDef owner) {
        AdsMethodDef.Profile profile = m.getProfile();
        if (member instanceof MethodParameter){
            List<MethodParameter> params = profile.findMethodParameterByName(profile.getParametersList().list(), member.getName());
            if (params != null && !params.isEmpty()) {
                return !params.get(0).isDescriptionInherited() ? params.get(0): null;
            }
        }
        if (member instanceof MethodReturnValue){
            MethodReturnValue returnValue = profile.getReturnValue();
            if (returnValue != null && !returnValue.isDescriptionInherited()) {
                return returnValue;
            }
        }
        if (member instanceof ThrowsListItem){
            AdsType type = ((ThrowsListItem) member).getException().resolve(owner).get();
            if (type instanceof AdsClassType) {
                AdsClassDef itemClass = ((AdsClassType) type).getSource();
                if (itemClass instanceof AdsExceptionClassDef) {
                    AdsMethodThrowsList throwsList = profile.getThrowsList();
                    for (AdsMethodThrowsList.ThrowsListItem item : throwsList) {
                        AdsType exeptionType = item.getException().resolve(owner).get();
                        if (exeptionType instanceof AdsClassType) {
                            AdsClassDef throwListItemClass = ((AdsClassType) exeptionType).getSource();
                            if (throwListItemClass instanceof AdsExceptionClassDef) {
                                if (throwListItemClass == itemClass) {
                                    if (!item.isDescriptionInherited()){
                                        return item;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
