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

import java.util.*;
import org.radixware.kernel.common.client.dialogs.IMessageBox.StandardButton;
import org.radixware.kernel.common.client.widgets.IButton;
import org.radixware.kernel.common.client.widgets.IButton.ClickHandler;
import org.radixware.kernel.common.client.widgets.actions.Action;
import org.radixware.kernel.common.enums.EDialogButtonType;
import org.radixware.kernel.common.html.Div;
import org.radixware.kernel.common.utils.Utils;
import org.radixware.wps.views.RwtAction;
import org.radixware.wps.views.RwtAction.IActionPresenter;


public class ButtonBox extends AbstractContainer {

    private final List<ClickHandler> handlers = new LinkedList<ClickHandler>();
    private List<Action> actions = null;
    private final ClickHandler defaultClickHandler = new ClickHandler() {
        @Override
        public void onClick(IButton source) {
            List<ClickHandler> list;
            synchronized (handlers) {
                list = new ArrayList<ClickHandler>(handlers);
            }
            for (ClickHandler h : list) {
                h.onClick(source);
            }
        }
    };

    public ButtonBox() {
        super(new Div());
        html.layout("$RWT.buttonBox.layout");
        html.setCss("overflow", "hidden");
        html.addClass("ui-corner-all");
        setHeight(25);
        setMinimumWidth(100);
        setPreferredHeight(25);
    }

    public void addButton(PushButton button) {
        addButton(button, Alignment.RIGHT);
    }

    public void addButton(PushButton button, Alignment align) {
        super.add(align == null || align == Alignment.RIGHT ? 0 : -1, button);
        button.getHtml().setCss("position", "relative");
        if (align == null) {
            align = Alignment.RIGHT;
        }
        switch (align) {
            case LEFT:
                button.getHtml().setCss("float", "left");
                break;
            case RIGHT:
                button.getHtml().setCss("float", "right");
                break;
            case CENTER:
                button.getHtml().setCss("float", "center");
                break;
        }

    }

    @Override
    public void add(int index, UIObject child) {
        super.add(index, child);
        if (child instanceof IButton) {
            ((IButton) child).addClickHandler(defaultClickHandler);
        }
    }

    public PushButton addButton(EDialogButtonType button, Alignment align) {
        if (button != EDialogButtonType.NO_BUTTON) {
            final PushButton pushButton = new PushButton();
            pushButton.setText(StandardButton.getTitle(button, getEnvironment()));
            addButton(pushButton, align);
            return pushButton;
        } else {
            return null;
        }
    }

    public PushButton addButton(EDialogButtonType button) {
        if (EDialogButtonType.NO_BUTTON == button) {
            return null;
        }
        return addButton(button, Alignment.RIGHT);
    }

    public void addClickHandler(ClickHandler clickHandler) {
        synchronized (handlers) {
            if (!handlers.contains(clickHandler)) {
                handlers.add(clickHandler);
            }
        }
    }

    public void removeClickHandler(ClickHandler clickHandler) {
        synchronized (handlers) {
            handlers.remove(clickHandler);
        }
    }

    public Map<EDialogButtonType, PushButton> setStandardButtons(Set<EDialogButtonType> buttons) {
        Map<EDialogButtonType, PushButton> result = new HashMap<>();
        for (EDialogButtonType b : buttons) {
            result.put(b, addButton(b));
        }
        return result;
    }

    private static class ActionButton extends PushButton implements IActionPresenter {

        private Action action;

        private ActionButton(RwtAction action) {
            action.addActionPresenter(this);
            actionStateChanged(action);
            this.action = action;
            addClickHandler(new ClickHandler() {
                @Override
                public void onClick(IButton source) {
                    ActionButton.this.action.trigger();
                }
            });
        }

        @Override
        public final void actionStateChanged(Action a) {
            this.setIcon(a.getIcon());
            this.setToolTip(a.getToolTip());
            this.setVisible(a.isVisible());
            this.setEnabled(a.isEnabled());
            this.setText(a.getText());
        }
    }

    public List<PushButton> setActions(Action[] actions) {
        List<PushButton> result = new LinkedList<PushButton>();
        for (Action a : actions) {
            PushButton button = addAction(a);
            result.add(button);
        }
        return result;
    }

    public PushButton addAction(Action action) {
        return addAction(action, null);
    }

    public PushButton addAction(Action action, Alignment alignment) {
        if (action == null) {
            return null;
        }
        ActionButton button = new ActionButton((RwtAction) action);
        addButton(button, alignment);
        if (actions == null) {
            actions = new LinkedList<Action>();
        }
        actions.add(action);
        return button;
    }

    public Action[] getActions() {
        return actions == null ? new Action[0] : actions.toArray(new Action[actions.size()]);
    }
    
    public Action getAction(String objectName) {
        for (Action action: getActions())
            if (action instanceof RwtAction) {
                RwtAction a = (RwtAction)action;
                if (Utils.equals(a.getObjectName(), objectName))
                    return a;
            }
        return null;
    }
}
