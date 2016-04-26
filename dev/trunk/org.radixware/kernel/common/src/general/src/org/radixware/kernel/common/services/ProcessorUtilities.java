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

package org.radixware.kernel.common.services;

import javax.lang.model.element.Element;
import javax.lang.model.element.NestingKind;
import javax.lang.model.element.TypeElement;


public class ProcessorUtilities {

    public static String createStringEntry(TypeElement typeElement) {

        String nameWithDots = typeElement.getQualifiedName().toString();
        int nestedDotsCount = 0;
        while (typeElement != null && typeElement.getNestingKind() != NestingKind.TOP_LEVEL) {
            if (typeElement.getNestingKind() == NestingKind.MEMBER) {
                nestedDotsCount++;
                Element enclosingElement = typeElement.getEnclosingElement();
                if (enclosingElement instanceof TypeElement) {
                    typeElement = (TypeElement) enclosingElement;
                } else {
                    throw new IllegalArgumentException("Unable to create entry for element " + typeElement);
                }
            } else {
                throw new IllegalArgumentException("Unable to create entry for element " + typeElement);
            }
        }

        if (nestedDotsCount == 0) {
            return nameWithDots;
        }

        String[] splitName = nameWithDots.split("\\.");


        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splitName.length; i++) {

            sb.append(splitName[i]);
            if (i != splitName.length - 1) {
                if (i >= splitName.length - 1 - nestedDotsCount) {
                    sb.append('$');
                } else {
                    sb.append('.');
                }
            }
        }

        return sb.toString();

    }
}
