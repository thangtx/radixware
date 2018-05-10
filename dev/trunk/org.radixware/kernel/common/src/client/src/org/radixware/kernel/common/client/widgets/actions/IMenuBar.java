/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.radixware.kernel.common.client.widgets.actions;

import org.radixware.kernel.common.client.types.Icon;
import org.radixware.kernel.common.client.widgets.IWidget;

public interface IMenuBar extends IWidget {

    IMenu addSubMenu(String title);

    IMenu addSubMenu(Icon icon, String title);

    void addSubSeparator();

    void clear();

    void insertMenu(IMenu before, IMenu menu);
}
