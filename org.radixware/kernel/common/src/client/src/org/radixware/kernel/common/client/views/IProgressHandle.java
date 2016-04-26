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

package org.radixware.kernel.common.client.views;


public interface IProgressHandle {

    public interface Cancellable {

        public void onCancel();
    }

    public void startProgress(final String text, final boolean canCancel);        
    
    public void startProgress(final String text, final boolean canCancel, final boolean forcedShow);

    public boolean isActive();

    public void cancel();

    public boolean wasCanceled();

    public void finishProgress();

    public void setTitle(final String title);

    public String getTitle();

    public void setText(final String text);

    public String getText();

    public void setMaximumValue(final int value);

    public int getMaximumValue();

    public void setValue(final int value);

    public int getValue();
    
    public void incValue();

    public boolean canCancel();
}
