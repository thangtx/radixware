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

package org.radixware.kernel.common.client.errors;

import org.radixware.kernel.common.client.localization.MessageProvider;


public interface IClientError {

    /**
     * Возвращает заголовок для окна сообщения.
     * @return заголовок для окна сообщения.
     */
    public String getTitle(MessageProvider messageProvider);

    /**
     * Возвращает локализованное сообщение для
     * показа в окне сообщения и в трассе с уровнем
     * error.
     * @return локализованное сообщение
     */
    public String getLocalizedMessage(MessageProvider messageProvider);

    /**
     * Подробная информация об ошибке
     * для показа в окне подробного описания ошибки
     * и в трассе с уровнем debug.
     * @return
     */
    public String getDetailMessage(MessageProvider messageProvider);
}
