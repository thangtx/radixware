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

package org.radixware.kernel.server.units.nethub;

import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import org.radixware.kernel.server.units.UnitView;


class NetHubUnitView extends UnitView{
	private final ExecutorService animationExecutor;
	public NetHubUnitView(final NetHubUnit model) {
		super(model);
		animationExecutor = Executors.newCachedThreadPool(
			new ThreadFactory() {
				@Override
				public Thread newThread(Runnable r) {
					final Thread thread = new Thread(r);
					thread.setPriority(Thread.NORM_PRIORITY - 1);
					return thread;
				}
			}
		);
	}

	@Override
	public void dispose() {
		animationExecutor.shutdownNow();
		super.dispose();
	}

	@Override
	protected void initTraceList() {
		super.initTraceList();
		final JToolBar toolBar = getTraceList().getToolBar();
		toolBar.addSeparator();
		final JButton indicator = new ConnectToExtSystemIndicator();
		toolBar.add(indicator);
	}
	
	private final class ConnectToExtSystemIndicator extends JButton implements ExtPortListener{
		private volatile boolean bConnected = false;
		public ConnectToExtSystemIndicator() {
			super();
			setFocusable(false);
			final NetHubUnit netUnit = (NetHubUnit)getUnit();
			netUnit.registerExtPortListener(this);
			if (netUnit.getExtPort().isConnected())
				onConnect(netUnit.getExtPort().getRemoteAddress());
			else
				onDisconnect();
		}

		@Override
		public void onConnect(final SocketAddress remoteAddress) {
			bConnected = true;
			SwingUtilities.invokeLater(
					new Runnable() {
				@Override
						public void run() {
							setIcon(new ImageIcon(NetHubUnitView.class.getResource("img/network-idle.png")));
							String rmAddr = "";
							if (remoteAddress != null)
								rmAddr = String.valueOf(remoteAddress);
							setToolTipText(NetHubMessages.EV_EXT_HOST_CONNECTED + rmAddr);
						}
					}
			);		
		}

		@Override
		public void onDisconnect() {
			bConnected = false;
			SwingUtilities.invokeLater(
					new Runnable() {
				@Override
						public void run() {
							setIcon(new ImageIcon(NetHubUnitView.class.getResource("img/network-offline.png")));
							setToolTipText(NetHubMessages.EV_EXT_HOST_DISCONNECTED);
						}
					}
			);		
		}

		@Override
		public void onRecv(byte[] mess) {
			animate(
				new ImageIcon(NetHubUnitView.class.getResource("img/network-receive.png")),
				new ImageIcon(NetHubUnitView.class.getResource("img/network-idle.png"))
			);
		}

		@Override
		public void onSend(byte[] mess) {
			animate(
				new ImageIcon(NetHubUnitView.class.getResource("img/network-transmit.png")),
				new ImageIcon(NetHubUnitView.class.getResource("img/network-idle.png"))
			);
		}
		
		private final void animate(final ImageIcon first, final ImageIcon second){
			final Runnable	setFirst = new Runnable() {
				@Override
				public void run() {
					if(bConnected)
						setIcon(first);
				};
			};
			final Runnable	setSecond = new Runnable() {
				@Override
				public void run() {
					if(bConnected)
						setIcon(second);
				};
			};
			animationExecutor.execute(new Runnable() {
				@Override
				public void run() {
					for(int i = 0; bConnected && i < 7; i++ ) {
						SwingUtilities.invokeLater(i%2 == 0 ? setFirst : setSecond);
						try {
							Thread.sleep(i%2 == 0 ? 300 : 100);
						} catch (InterruptedException ex) {
							break;
						}
					}
					if (bConnected)
						SwingUtilities.invokeLater(setSecond);
				}
			});
		}
	}
}
