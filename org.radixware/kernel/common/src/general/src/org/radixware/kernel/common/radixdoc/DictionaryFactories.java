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

package org.radixware.kernel.common.radixdoc;



public class DictionaryFactories {

    public static IRadixdocDictionary createEnglish() {
        return new IRadixdocDictionary() {
            @Override
            public String getPhraseId(ERadixdocPhrase key) {
                return key.getKey();
            }
        };
    }

    public static IRadixdocDictionary createRussian() {
        return new IRadixdocDictionary() {
            @Override
            public String getPhraseId(ERadixdocPhrase key) {
                switch (key) {
                    case ATTRIBUTES:
                        return "Аттрибуты:";
                    case DESCRIPTION:
                        return "Описание:";
                    case TEST_MODULE:
                        return "Тестовый модуль";
                    case LAYER:
                        return "Слой";
                    case MODULE:
                        return "модуль";
                    default:
                        return key.getKey();
                }
            }
        };
    }
}
