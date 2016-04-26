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

package org.radixware.wps.rwt;

import org.radixware.kernel.common.client.types.UnacceptableInput;


public interface ValueEditor<T> {

    public interface ValueChangeListener<T> {
        public void onValueChanged(T oldValue, T newValue);
    }
    
    public interface StartChangeValueListener {
        public void onStartChangeValue();
    }
    
    public interface FinishChangeValueListener {
        public void onFinishChangeValue(boolean valueAccepted);
    }
    
    public interface UnacceptableInputListener{
        public void onUnacceptableInputChanged(UnacceptableInput previous, UnacceptableInput current);
    }
    
    public void addValueChangeListener(ValueEditor.ValueChangeListener<T> listener);

    public void removeValueChangeListener(ValueEditor.ValueChangeListener<T> listener);
    
    public void addStartChangeValueListener(StartChangeValueListener listener);
    
    public void removeStartChangeValueListener(StartChangeValueListener listener);
    
    public void addFinishChangeValueListener(FinishChangeValueListener listener);
    
    public void removeFinishChangeValueListener(FinishChangeValueListener listener);
    
    public void addUnacceptableInputListener(UnacceptableInputListener listener);
    
    public void removeUnacceptableInputListener(UnacceptableInputListener listener);
    
    public boolean hasAcceptableInput();
    
    public UnacceptableInput getUnacceptableInput();
    
    public void setInputText(final String inputText);
    
    public boolean checkInput(final String messageTitle, final String firstMessageLine);
    
    public boolean checkInput();
}