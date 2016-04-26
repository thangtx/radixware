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

package org.radixware.kernel.server.widgets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import org.radixware.kernel.common.enums.EEventSeverity;

public class TraceStatusBar extends JPanel implements TraceViewListener {

    private static final long serialVersionUID = -1652866715103848252L;
    private final JLabel messageLabel;
    private final JLabel iconLabel;

    public TraceStatusBar() {
        super(new BorderLayout());
        messageLabel = new JLabel();
        iconLabel = new JLabel();
        add(messageLabel, BorderLayout.WEST);
        add(iconLabel, BorderLayout.EAST);
        iconLabel.setHorizontalTextPosition(SwingConstants.LEADING);
        iconLabel.setAlignmentX(SwingConstants.RIGHT);
        super.setPreferredSize(new Dimension(2000, Math.max(messageLabel.getFont().getSize() + 4, 24)));
         
    }
    
    public void setStatus(final String status) {
        iconLabel.setText(status);
    }

    public void setMessage(String message) {
        messageLabel.setText(" " + message);
    }

    public void setImage(ImageIcon image) {
        iconLabel.setIcon(image);
    }

    @Override
    public void severityChanged(TraceView traceList, EEventSeverity oldSeverity, EEventSeverity newSeverity) {
        if (newSeverity == EEventSeverity.NONE) {
            setMessage(Messages.STB_NONE);
            setImage(null);
        } else if (newSeverity == EEventSeverity.DEBUG) {
            setMessage(Messages.STB_DEBUG);
            setImage(new ImageIcon(TraceStatusBar.class.getResource("img/debug.png")));
        } else if (newSeverity == EEventSeverity.EVENT) {
            setMessage(Messages.STB_EVENT);
            setImage(new ImageIcon(TraceStatusBar.class.getResource("img/event.png")));
        } else if (newSeverity == EEventSeverity.WARNING) {
            setMessage(Messages.STB_WARNING);
            setImage(new ImageIcon(TraceStatusBar.class.getResource("img/warning.png")));
        } else if (newSeverity == EEventSeverity.ERROR) {
            setMessage(Messages.STB_ERROR);
            setImage(new ImageIcon(TraceStatusBar.class.getResource("img/error.png")));
        } else if (newSeverity == EEventSeverity.ALARM) {
            setMessage(Messages.STB_ALARM);
            setImage(new ImageIcon(TraceStatusBar.class.getResource("img/alarm.png")));
        }
    }

    private static final class Messages {

        static {
            final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

            STB_ERROR = bundle.getString("STB_ERROR");
            STB_ALARM = bundle.getString("STB_ALARM");
            STB_WARNING = bundle.getString("STB_WARNING");
            STB_EVENT = bundle.getString("STB_EVENT");
            STB_DEBUG = bundle.getString("STB_DEBUG");
            STB_NONE = bundle.getString("STB_NONE");
        }
        static final String STB_ERROR;
        static final String STB_ALARM;
        static final String STB_WARNING;
        static final String STB_EVENT;
        static final String STB_DEBUG;
        static final String STB_NONE;
    }
}
