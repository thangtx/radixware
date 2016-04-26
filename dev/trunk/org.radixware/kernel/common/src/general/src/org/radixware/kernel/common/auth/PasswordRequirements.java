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

package org.radixware.kernel.common.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class PasswordRequirements {

    //PCI-DSS: 8.5.10 Require a minimum password length of at least seven characters.
    private final static PasswordRequirements DEFAULT = new PasswordRequirements(7, true, true);
    private final long minLength;
    private final boolean mustContainLetter;
    private final boolean mustContainDigit;
    private final Collection<String> blackList;

    public PasswordRequirements(final long minLen, final boolean mustContainAChar, final boolean mustContainNChar) {
        this(minLen, mustContainAChar, mustContainNChar, null);
    }

    public PasswordRequirements(final long minLen, final boolean mustContainAChar, final boolean mustContainNChar, final Collection<String> blackList) {
        minLength = minLen;
        mustContainLetter = mustContainAChar;
        mustContainDigit = mustContainNChar;
        this.blackList = Collections.unmodifiableList(blackList != null ? new ArrayList<>(blackList) : Collections.<String>emptyList());
    }

    public static PasswordRequirements getDefault() {
        return DEFAULT;
    }

    public final long getMinLength() {
        return minLength;
    }

    public final boolean mustContainDigit() {
        return mustContainDigit;
    }

    public final boolean mustContainLetter() {
        return mustContainLetter;
    }

    public Collection<String> getBlackList() {
        return blackList;
    }
    
    public final AuthUtils.PwdWeakness checkPassword(final String password) {
        return AuthUtils.checkPwdWeakness(password, this);
    }
}
