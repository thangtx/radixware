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

/*
 * 9/27/11 2:55 PM
 */
package org.radixware.kernel.designer.common.dialogs.components.values;

import org.radixware.kernel.designer.common.dialogs.components.values.ValueEditorComponent.ValueEditorModel;


public abstract class ContextableValueEditorComponent<TValue, TContext>
    extends ValueEditorComponent<TValue> implements IContextableValueEditorComponent<TValue, TContext> {

    public static abstract class DefaultSampleEditorModel<TValue, TContext> extends DefaultEditorModel<TValue, TValue, TContext> {

        @Override
        protected TValue toExternal(TValue local) {
            return local;
        }

        @Override
        protected TValue toLocal(TValue value) {
            return value;
        }

    }

    public static abstract class DefaultEditorModel<TLocalValue, TValue, TContext>
        extends ValueEditorModel<TLocalValue, TValue> implements IContextableValueEditorModel<TValue, TContext> {

        private TContext context;

        protected abstract TValue getValueFromContext(TContext context);

        public DefaultEditorModel() {
            super();
        }

        protected void commitImpl() {
        }

        protected void openImpl(TContext context, TValue currValue) {
            if (isOpened()) {
                setValueInternal(currValue, false, false);
            }
        }

        @Override
        public final void commit() {
            commitImpl();
            setValueChange(false);
        }

        @Override
        public void updateFromContext() {
            reset();
            setLocalValue(toLocal(getValueFromContext(getContext())));
        }

        @Override
        public boolean isValueChanged() {
            return getValueChange();
        }

        @Override
        public final TContext getContext() {
            return context;
        }

        @Override
        public final void open(TContext context) {
            open(context, getValueFromContext(context));
        }

        @Override
        public final void open(TContext context, TValue currValue) {
            this.context = context;
            reset();

            openImpl(context, currValue);
            setValueChange(false);
        }

        @Override
        public boolean isOpened() {
            return context != null;
        }

    }

    protected ContextableValueEditorComponent(final DefaultEditorModel<?, TValue, TContext> model) {
        super(model);
    }

    protected void openImpl(TContext context) {
        getModel().open(context);
    }

    protected void openImpl(TContext context, TValue currValue) {
        getModel().open(context, currValue);
    }

    @Override
    public void commit() {
        getModel().commit();
    }

    @Override
    public final void open(TContext context) {

        disconnectEditorComponent();
        openImpl(context);
        notifyModelOpened();
        connectEditorComponent();
    }

    @Override
    public final void open(TContext context, TValue currValue) {

        disconnectEditorComponent();
        openImpl(context, currValue);
        notifyModelOpened();
        connectEditorComponent();
    }

    @Override
    public boolean isOpened() {
        return getModel().isOpened();
    }

    @Override
    public void update() {
        getModel().updateFromContext();
    }

    @Override
    protected DefaultEditorModel<?, TValue, TContext> getModel() {
        return (DefaultEditorModel<?, TValue, TContext>) super.getModel();
    }

}
