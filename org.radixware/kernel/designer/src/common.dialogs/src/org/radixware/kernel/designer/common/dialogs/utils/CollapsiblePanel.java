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

package org.radixware.kernel.designer.common.dialogs.utils;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class CollapsiblePanel extends JPanel implements Collapsible {

    private CollapseButton _collapseButton;
    private boolean _collapsed = true;
    private JPanel _header;
    protected JPanel _content;
    //  private JPanel _spring;
    private String title;

    public CollapsiblePanel(String title, JPanel content) {
        super();

        this.title = title;
        setContent(content);

        Border titledBorder = new TitledBorder(title);
        setBorder(titledBorder);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        _header = new JPanel();
        _header.setLayout(new BoxLayout(_header, BoxLayout.LINE_AXIS));
        _header.setAlignmentX(JComponent.LEFT_ALIGNMENT); //must have
        add(_header);

        _collapseButton = new CollapseButton(this);
      //  _header.add(new JLabel(title));
        _header.add(Box.createHorizontalGlue());
        _header.add(_collapseButton);

    //   _spring = new JPanel();

        if (_content != null) {
            expand();
        }
    }

    public void setContent(JPanel content) {
        this._content = content;
        if (_content != null){
            this._content.setAlignmentX(JComponent.LEFT_ALIGNMENT); //must have
        }
    }

    /**
     *  Tells whether this component is currently collapsed. Useful for
     *  checking the component's status.
     *
     *@returns    true if this component is collapsed, false if it is not.
     */
    public boolean isCollapsed() {
        return _collapsed;
    }

    /**
     *  Tells whether this component is collapsible.
     *
     *@returns    a boolean indicating this component is collapsible.
     */
    public boolean isCollapsible() {
        return collapsible;
    }

    /**
     *  Collapses the panel.
     */
    public void collapse() {

        setVisible(false);

        remove(_content);

        Dimension dim = _collapseButton.getSize();
        _collapseButton.setBounds(0, 0, dim.width, dim.height);

        //  add( _spring );

        _collapsed = true;

        revalidate();
        setVisible(true);
    }

    /**
     *  Uncollapses the panel.
     */
    public final void expand() {

        setVisible(false);

        Dimension dim = _collapseButton.getSize();
        _collapseButton.setBounds(0, 0, dim.width, dim.height);


        add(_content);

        _collapsed = false;

        revalidate();
        setVisible(true);
    }

    //example of usage
    /*
    public static void main(String[] args) {
        JFrame frame = new JFrame("Test frame");

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
        content.add(new JLabel("TEST1"));
        content.add(new JLabel("TEST2"));
        content.add(new JLabel("TEST3"));
        content.add(new JLabel("TEST4"));

        CollapsiblePanel collapsiblePanel = new CollapsiblePanel("Title", content);

        frame.getContentPane().add(collapsiblePanel);
        frame.pack();
        frame.setVisible(true);
    }*/
}
