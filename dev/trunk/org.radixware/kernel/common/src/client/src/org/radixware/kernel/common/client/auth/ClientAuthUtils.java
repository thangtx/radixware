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

package org.radixware.kernel.common.client.auth;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.localization.MessageProvider;


public class ClientAuthUtils {

    public static PasswordRequirements getPasswordRequirements(final org.radixware.schemas.eas.PasswordRequirements xml, final String userName){
        final Collection<String> blackList = xml.getBlackList()==null ? null : xml.getBlackList().getItemList();
        return new PasswordRequirements(xml.getMinLen(), 
                                        xml.getAlphabeticCharsRequired(),
                                        xml.getAlphabeticCharsInMixedCaseRequired(),
                                        xml.getNumericCharsRequired(),
                                        xml.getSpecialCharsRequired(),
                                        xml.getPwdMustDifferFromName() ? userName : null,
                                        blackList);
    }

    public static String checkPassword(final String password, final PasswordRequirements requirements, final IClientEnvironment env) {
        final Collection<AuthUtils.PwdWeaknessCheckResult> weakness = requirements.checkPassword(password);
        final List<String> messages = new LinkedList<>();        
        for (AuthUtils.PwdWeaknessCheckResult result: weakness){
            if (result!=null && result.getWeakness()!=null){
                if (result.getWeakness()==AuthUtils.PwdWeakness.FORBIDDEN){
                    return getPasswordRequirementMessage(result, env.getMessageProvider());
                }
                messages.add(getPasswordRequirementMessage(result, env.getMessageProvider()));
            }
        }
        if (messages.isEmpty()){
            return null;
        }else if (messages.size()==1){
            return messages.get(0);
        }else{
            final StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append(env.getMessageProvider().translate("ChangePasswordDialog", "The following password complexity requirements were not met:"));
            for (String message: messages){
                msgBuilder.append("\n\t");
                msgBuilder.append(message);
            }
            return msgBuilder.toString();
        }
    }
    
    public static String getPasswordRequirementMessage(final AuthUtils.PwdWeaknessCheckResult checkResult, final MessageProvider mp) {
        switch(checkResult.getWeakness()){
            case FORBIDDEN:
                return mp.translate("ChangePasswordDialog", "It is forbidden to use this password.");
            case STARTS_WITH_FORBIDDEN_CHARS:{
                final String forbiddenChars = checkResult.getDetails();
                if (forbiddenChars==null || forbiddenChars.isEmpty()){
                    return mp.translate("ChangePasswordDialog", "Password must not contain forbidden characters combination.");
                }else{
                    return String.format(mp.translate("ChangePasswordDialog", "Password must not stratrs with \'%1$s\'."),forbiddenChars);
                }
            }
            case ENDS_WITH_FORBIDDEN_CHARS:{
                final String forbiddenChars = checkResult.getDetails();
                if (forbiddenChars==null || forbiddenChars.isEmpty()){
                    return mp.translate("ChangePasswordDialog", "Password must not contain forbidden characters combination.");
                }else{
                    return String.format(mp.translate("ChangePasswordDialog", "Password must not ends with \'%1$s\'."),forbiddenChars);
                }
            }
            case CONTAIN_FORBIDDEN_CHARS:{
                final String forbiddenChars = checkResult.getDetails();
                if (forbiddenChars==null || forbiddenChars.isEmpty()){
                    return mp.translate("ChangePasswordDialog", "Password must not contain forbidden characters combination.");
                }
                if (forbiddenChars.length()==1){
                    return String.format(mp.translate("ChangePasswordDialog", "Password must not contain \'%1$s\' character."),forbiddenChars);
                }else{
                    return String.format(mp.translate("ChangePasswordDialog", "Password must not contain \'%1$s\' characters combination."),forbiddenChars);
                }
            }
            case SAME_AS_USER_NAME:
                return mp.translate("ChangePasswordDialog", "Password must be different from the user name.");
            case TOO_SHORT:
                return String.format(mp.translate("ChangePasswordDialog", "Password must contain at least %1$s characters."), checkResult.getDetails());
            case NO_ALPHABETIC_CHARS:
                return mp.translate("ChangePasswordDialog", "Password must include one ore more alphabetic characters.");
            case ALL_ALPHABETIC_CHARS_IN_SAME_CASE:
                return mp.translate("ChangePasswordDialog", "Password must include both upper-case and lower-case letters.");
            case NO_NUMERIC_CHARS:
                return mp.translate("ChangePasswordDialog", "Password must include one ore more numerical digits.");
            case NO_SPECIAL_CHARS:
                return mp.translate("ChangePasswordDialog", "Password must include one ore more special characters.");
            default:
                return null;
        }
    }
}