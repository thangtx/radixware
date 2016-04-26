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

package org.radixware.kernel.designer.ads.editors.clazz.members.transparent;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.openide.util.NbBundle;
import org.radixware.kernel.common.enums.EAccess;
import org.radixware.kernel.designer.common.dialogs.components.selector.ItemNameFilter;


final class SignatureFilter extends ItemNameFilter<ClassMemberItem> {

    private class Parser {

        void parse(String input) {
            final StringTokenizer tokenizer = new StringTokenizer(input, " ");

            String token = null;
            while (tokenizer.hasMoreTokens()) {
                token = tokenizer.nextToken().toLowerCase();

                if (!isModifier(token)) {
                    break;
                }
                token = null;
            }

            SignatureFilter.this.name = token;
        }

        private boolean checkModifier(String name, String token) {
            if (name.equals(token)) {
                access.put(name, Boolean.TRUE);
                return true;
            }
            if (("-" + name).equals(token)) {
                access.put(name, Boolean.FALSE);
                return true;
            }
            return false;
        }

        private boolean isModifier(String token) {

            for (final String modifier : modifiers) {
                if (checkModifier(modifier, token)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static final String[] modifiers = {EAccess.PUBLIC.getName(), EAccess.PROTECTED.getName(), "static", "final", "abstract"};

    private final Map<String, Boolean> access = new HashMap<>();
    private String name;
    private final Parser parser = new Parser();

    public SignatureFilter() {
        field.setToolTipText(NbBundle.getMessage(SignatureFilter.class, "SignatureFilter.ToolTip"));
    }

    @Override
    public boolean accept(ClassMemberItem value) {
        return acceptAccess(value) && acceptName(value);
    }

    @Override
    protected void updatePattern() {
        updateAccess();
        super.updatePattern();
    }

    @Override
    protected String getNameStringPattern() {
        return name != null ? name : "";
    }

    private boolean acceptAccess(ClassMemberItem value) {

        final Boolean isPublic = access.get(EAccess.PUBLIC.getName());
        if (isPublic != null) {
            if ((value.getAccess() == EAccess.PUBLIC) != isPublic) {
                return false;
            }
        }

        final Boolean isProtected = access.get(EAccess.PROTECTED.getName());
        if (isProtected != null) {
            if ((value.getAccess() == EAccess.PROTECTED) != isProtected) {
                return false;
            }
        }

        final Boolean isStatic = access.get("static");
        if (isStatic != null) {
            if (value.isStatic() != isStatic) {
                return false;
            }
        }

        final Boolean isFinal = access.get("final");
        if (isFinal != null) {
            if (value.isFinal() != isFinal) {
                return false;
            }
        }

        final Boolean isAbstract = access.get("abstract");
        if (isAbstract != null) {
            if (value.isAbstract() != isAbstract) {
                return false;
            }
        }
        return true;
    }

    private void updateAccess() {
        synchronized (parser) {

            for (final String modifier : modifiers) {
                access.remove(modifier);
            }

            parser.parse(getText());
        }
    }
}
