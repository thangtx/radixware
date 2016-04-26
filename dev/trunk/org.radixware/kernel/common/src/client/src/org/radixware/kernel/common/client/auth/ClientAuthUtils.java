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

import java.util.Collections;
import org.radixware.kernel.common.auth.AuthUtils;
import org.radixware.kernel.common.auth.PasswordRequirements;
import org.radixware.kernel.common.client.IClientEnvironment;


public class ClientAuthUtils {

    public static PasswordRequirements getPasswordRequirements(final org.radixware.schemas.eas.PasswordRequirements xml, final String userName){
        return new PasswordRequirements(xml.getMinLen(), xml.getAlphabeticCharsRequired(), xml.getNumericCharsRequired(), xml.getPwdMustDifferFromName() ? Collections.singletonList(userName) : Collections.<String>emptyList());
    }

    public static String checkPassword(final String password, final PasswordRequirements requirements, final IClientEnvironment env) {
        final AuthUtils.PwdWeakness weakness = requirements.checkPassword(password);
        if (weakness == AuthUtils.PwdWeakness.FROM_BLACK_LIST) {
            return env.getMessageProvider().translate("ChangePasswordDialog", "Password should be different from user name");
        } else if (weakness == AuthUtils.PwdWeakness.TOO_SHORT) {
            return String.format(
                    env.getMessageProvider().translate("ChangePasswordDialog", "Password should contain at least %s characters"),
                    requirements.getMinLength());
        } else if (weakness == AuthUtils.PwdWeakness.NO_NUMERIC_CHARS || weakness == AuthUtils.PwdWeakness.NO_ALPHABETIC_CHARS) {
            if (requirements.mustContainDigit() && requirements.mustContainLetter()) {
                return env.getMessageProvider().translate("ChangePasswordDialog", "Password should contain both numeric and alphabetic characters");
            } else if (requirements.mustContainDigit()) {
                return env.getMessageProvider().translate("ChangePasswordDialog", "Password should contain numeric characters");
            } else {
                return env.getMessageProvider().translate("ChangePasswordDialog", "Password should contain alphabetic characters");
            }
        } else if ((weakness != AuthUtils.PwdWeakness.NONE)) {
            return env.getMessageProvider().translate("ChangePasswordDialog", "Weak password");
        } else {
            return null;
        }
    }
}
