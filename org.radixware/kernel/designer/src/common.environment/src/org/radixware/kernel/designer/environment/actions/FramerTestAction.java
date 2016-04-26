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

package org.radixware.kernel.designer.environment.actions;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import org.radixware.kernel.common.utils.*;
import org.radixware.kernel.designer.common.dialogs.utils.DialogUtils;
import org.radixware.kernel.designer.common.dialogs.utils.ModalDisplayer;


public class FramerTestAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                ModalDisplayer.showModal(new FramerTestDialog());
            }
        });
    }

    private static class FramerTestDialog extends JDialog {

        private Socket socket = null;

        public FramerTestDialog() {
            setLayout(new MigLayout("fill", "", "[shrink][shrink][grow][shrink, nogrid][grow]"));
            add(new JLabel("Frame:"), "split, gapleft 4");
            final JTextField tfFrameFormat = new JTextField();
            add(tfFrameFormat, "growx, gapleft 16, wrap");
            final JPanel transmissionTestingPanel = new JPanel(new MigLayout("fill", "", "[][nogrid]"));
            transmissionTestingPanel.add(new JLabel("Remote address:"));
            final JTextField tfRemoteAddress = new JTextField();
            transmissionTestingPanel.add(tfRemoteAddress, "w 280!, gapleft 16, wrap");
            final JButton btConnect = new JButton("Connect");
            transmissionTestingPanel.add(btConnect);
            final JButton btDisconnect = new JButton("Disconnect");
            transmissionTestingPanel.add(btDisconnect);
            final JButton btSend = new JButton("Send");
            transmissionTestingPanel.add(btSend, "pushx, wrap");
            transmissionTestingPanel.setBorder(BorderFactory.createTitledBorder("Transmission test"));
            add(transmissionTestingPanel, "wrap");
            final JTextArea taClearData = new JTextArea();
            final JPanel clearDataPanel = new JPanel(new BorderLayout());
            clearDataPanel.add(taClearData, BorderLayout.CENTER);
            clearDataPanel.setBorder(BorderFactory.createTitledBorder("Clear data"));
            add(clearDataPanel, "span, grow, wrap");
            final JButton btPack = new JButton("Pack");
            add(btPack);
            final JButton btUnpack = new JButton("Unpack");
            add(btUnpack, "pushx, wrap");
            final JPanel packedDataPanel = new JPanel(new BorderLayout());
            final JTextArea taPackedData = new JTextArea();
            packedDataPanel.add(taPackedData, BorderLayout.CENTER);
            packedDataPanel.setBorder(BorderFactory.createTitledBorder("Packed data"));
            add(packedDataPanel, "span, grow");

            btDisconnect.setEnabled(false);
            btSend.setEnabled(false);

            btPack.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        final String frame = tfFrameFormat.getText();
                        final String clearData = taClearData.getText();
                        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        new Framer(null, bos, frame, frame).sendFrame(Hex.decode(clearData.replaceAll("\\s", "")), null);
                        taPackedData.setText(Hex.encode(bos.toByteArray()));
                    } catch (Exception ex) {
                        DialogUtils.messageError(ex);
                    }
                }
            });
            btUnpack.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        final String frame = tfFrameFormat.getText();
                        final String packedData = taPackedData.getText();
                        final ByteArrayInputStream bis = new ByteArrayInputStream(Hex.decode(packedData.replaceAll("\\s", "")));
                        taClearData.setText(Hex.encode(new Framer(bis, null, frame, frame).recvFrame(0, null)));
                    } catch (Exception ex) {
                        DialogUtils.messageError(ex);
                    }
                }
            });

            btConnect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        socket = new Socket();
                        final CompositeInetSocketAddress cisa = ValueFormatter.parseCompositeInetSocketAddress(tfRemoteAddress.getText());
                        if (cisa.getLocalAddress() != null) {
                            socket.bind(cisa.getLocalAddress());
                        }
                        socket.connect(cisa.getRemoteAddress(), 5000);
                        tfRemoteAddress.setEditable(false);
                        btDisconnect.setEnabled(true);
                        btSend.setEnabled(true);
                        btConnect.setEnabled(false);
                    } catch (Exception ex) {
                        DialogUtils.messageError(ex);
                    }
                }
            });
            btDisconnect.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!socket.isClosed()) {
                        try {
                            socket.close();
                        } catch (IOException ex) {
                            DialogUtils.messageError(ex);
                        } finally {
                            btConnect.setEnabled(true);
                            btSend.setEnabled(false);
                            tfRemoteAddress.setEditable(true);
                        }
                    }
                }
            });
            btSend.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        btPack.doClick();
                        final String frame = tfFrameFormat.getText();
                        final byte[] packet = Hex.decode(taClearData.getText().replaceAll("\\s", ""));
                        new Framer(null, socket.getOutputStream(), frame, frame).sendFrame(packet, null);
                    } catch (Exception ex) {
                        DialogUtils.messageError(ex);
                    }
                }
            });
        }
    }
}
