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

package org.radixware.kernel.common.client.widgets;

import org.radixware.kernel.common.client.IClientEnvironment;
import org.radixware.kernel.common.client.meta.RadEnumPresentationDef;
import org.radixware.kernel.common.enums.EEditMaskType;
import org.radixware.kernel.common.enums.EValType;

/**
 *Интерфейс фабрики по созданию диалогов и редакторов маски редактирования.
 */
public interface IEditMaskEditorFactory {
    /**
     * Создание виджета редактора для  маски заданного типа
     */
    IEditMaskEditor newEditMaskEditor(IClientEnvironment environment, IWidget parent, EEditMaskType editMaskType);
    /**
     * Создание виджета редактора маски для заданного набора констант
     */
    IEditMaskEditor newEditMaskConstSetEditor(IClientEnvironment environment, IWidget parent, RadEnumPresentationDef enumDef);
    /**
     * Создание виджета редактора маски для  заданного типа значения
     */
    IEditMaskEditor newEditMaskEditor(IClientEnvironment environment, IWidget parent, EValType valType);

    /**
     * Создание диалога редактора для  маски заданного типа
     */
    IEditMaskEditorDialog newEditMaskEditorDialog(IClientEnvironment environment, IWidget parent, EEditMaskType editMaskType);
    /**
     * Создание виджета редактора маски для заданного набора констант
     */
    IEditMaskEditorDialog newEditMaskConstSetEditorDialog(IClientEnvironment environment, IWidget parent, RadEnumPresentationDef enumDef);
    /**
     * Создание виджета редактора маски для  заданного типа значения
     */
    IEditMaskEditorDialog newEditMaskEditorDialog(IClientEnvironment environment, IWidget parent, EValType valType);
}
