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

package org.radixware.kernel.common.defs.dds;


public interface IDdsAutoDbNamedDefinition extends IDdsDbDefinition {

    /**
     * Является-ли имя объекта в базе данных автогенерируемым.
     * Если имя автогенерируемое, то оно обновляется при изменении модели.
     * Генерация имени производится на основании имени объекта, родительских объектов, их типов и т.д..
     * Не рекомендуется придерживаться формата генерации, он может измениться.
     * @throws NullPointerException possible, if object is not in branch.
     */
    public boolean isAutoDbName();

    public String calcAutoDbName();

    /**
     * Set database name of the definition.
     */
    public void setDbName(String dbName);
}
