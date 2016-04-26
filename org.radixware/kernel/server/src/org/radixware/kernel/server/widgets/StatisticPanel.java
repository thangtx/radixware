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
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class StatisticPanel extends JPanel {

	private static final long serialVersionUID = 5086901875855149496L;

	public interface Provider {

		public String getValuesAxeCaption();

		public int getValue();

		public String getTitle();

		public boolean isStarted();
	}
	private final Provider provider;
	private static final int DEFAULT_TIME_INTERVAL = 30; //�������� �������, ������������ �� ���������, � ��������
	private static final int DEFAULT_SLEEP_INTERVAL = 200; //�����, �� ������� �������� �����, � �������������
	private int sleepInterval; //�����, �� ������� �������� �����, � �������������
	private JFreeChart chart;
	private TimeSeries series;
	private Thread thread;
	private JButton startButton;
	private JButton stopButton;

	private final class SliderPanel extends JPanel {

		private static final long serialVersionUID = -5529478492738371641L;

		private final class SpeedSlider extends JSlider {

			private static final long serialVersionUID = -1978125729894848427L;
			private static final int MIN_VALUE = 100;
			private static final int MAX_VALUE = 1000;

			public SpeedSlider() {
				super();
				setMaximum(MAX_VALUE);
				setMinimum(MIN_VALUE);
				setMajorTickSpacing(100);
				setMinorTickSpacing(100);
				setOrientation(SwingConstants.HORIZONTAL);
				setPaintLabels(false);
				setPaintTicks(true);
				setValue(MAX_VALUE - DEFAULT_SLEEP_INTERVAL + MIN_VALUE);
				addChangeListener(new ChangeListener() {

					@Override
					public void stateChanged(ChangeEvent e) {
						sleepInterval = MAX_VALUE - SpeedSlider.this.getValue() + MIN_VALUE;
						int timeInterval = DEFAULT_TIME_INTERVAL * sleepInterval / DEFAULT_SLEEP_INTERVAL;
						XYPlot plot = chart.getXYPlot();
						ValueAxis axis = plot.getDomainAxis();
						axis.setFixedAutoRange(timeInterval * 1000.0);
					}
				});
			}
		}

		public SliderPanel() {
			super(new BorderLayout());
			JPanel topPanel = new JPanel(new BorderLayout(6, 0));
			topPanel.setBorder(new EmptyBorder(0, 0, 0, 7));
			topPanel.add(new JLabel(Messages.LBL_SPEED), BorderLayout.WEST);
			topPanel.add(new SpeedSlider(), BorderLayout.CENTER);
			JPanel bottomPanel = new JPanel(new BorderLayout());
			bottomPanel.setBorder(new EmptyBorder(0, 55, 0, 0));
			bottomPanel.add(new JLabel(Messages.LBL_MIN), BorderLayout.WEST);
			bottomPanel.add(new JLabel(Messages.LBL_MAX), BorderLayout.EAST);
			add(topPanel, BorderLayout.CENTER);
			add(bottomPanel, BorderLayout.SOUTH);
		}
	}

	private static final class DBPTimePeriod extends RegularTimePeriod {

		private final long start;
		private final long end;

		public DBPTimePeriod(long start, long end) {
			this.start = start;
			this.end = end;
		}

		@Override
		public long getLastMillisecond() {
			return end;
		}

		@Override
		public int compareTo(Object period) {
			return 1;
		}

		@Override
		public void peg(Calendar calendar) {
			//do nothing
		}

		@Override
		public long getLastMillisecond(Calendar calendar) {
			return end;
		}

		@Override
		public long getSerialIndex() {
			return 1;
		}

		@Override
		public long getFirstMillisecond(Calendar calendar) {
			return start;
		}

		@Override
		public RegularTimePeriod previous() {
			return null;
		}

		@Override
		public RegularTimePeriod next() {
			return null;
		}

		@Override
		public long getFirstMillisecond() {
			return start;
		}
	}

	public StatisticPanel(final Provider provider) {
		super(new BorderLayout());
		this.provider = provider;
		sleepInterval = DEFAULT_SLEEP_INTERVAL;
		add(createChart(), BorderLayout.CENTER);
		add(createControlBar(), BorderLayout.SOUTH);
	}

	public void stop() {
		if (thread != null) {
			thread.interrupt();
		}
	}

	public void setStateOn(final boolean val) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				startButton.setEnabled(val);
				stopButton.setEnabled(val);
			}
		});
	}

	private JPanel createChart() {
		series = new TimeSeries("Value Series", DBPTimePeriod.class);
		TimeSeriesCollection seriesCollection = new TimeSeriesCollection(series);
		chart = ChartFactory.createXYBarChart(null, Messages.LBL_TIME, true, provider.getValuesAxeCaption(),
				seriesCollection, PlotOrientation.VERTICAL, false, false, false);
		XYPlot plot = chart.getXYPlot();
		plot.getRenderer().setSeriesPaint(0, Color.BLUE);
		ValueAxis axis = plot.getDomainAxis();
		axis.setAutoRange(true);
		axis.setFixedAutoRange(DEFAULT_TIME_INTERVAL * 1000.0);
		axis = plot.getRangeAxis();
		axis.setAutoRange(true);
		//axis.setRange(0.0, 10.0);
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		ChartPanel panel = new ChartPanel(chart);
		panel.setBorder(new EmptyBorder(10, 0, 0, 0));
		return panel;
	}

	private JPanel createButtonBar() {
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

		startButton = new JButton(Messages.BTN_START);
		startButton.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (!provider.isStarted() || thread != null && thread.getState() != Thread.State.TERMINATED) {
							return;
						}
						thread = new Thread(
								new Runnable() {

									@Override
									public void run() {
										long prevTime = System.currentTimeMillis() - sleepInterval;
										while (true) {
											try {
												long newTime = System.currentTimeMillis();
												series.add(new DBPTimePeriod(prevTime, newTime), provider.getValue());
												prevTime = newTime;
											} catch (Throwable e) {
												return;
											}
											try {
												Thread.sleep(sleepInterval);
											} catch (InterruptedException e) {
												return;
											}
										}
									}
								}, "Statistic Thread of " + provider.getTitle());
						thread.setPriority(Thread.NORM_PRIORITY - 1);
						thread.start();
					}
				});
		buttonPanel.add(startButton);

		stopButton = new JButton(Messages.BTN_STOP);
		stopButton.addActionListener(
				new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						if (thread != null && thread.getState() != Thread.State.TERMINATED) {
							thread.interrupt();
						}
					}
				});
		buttonPanel.add(stopButton);

		return buttonPanel;
	}

	private JPanel createControlBar() {
		JPanel panel = new JPanel(new BorderLayout(10, 0));
		panel.setBorder(new EmptyBorder(0, 10, 10, 10));
		panel.add(new SliderPanel(), BorderLayout.CENTER);
		panel.add(createButtonBar(), BorderLayout.EAST);
		return panel;
	}

	private static final class Messages {

		static {
			final ResourceBundle bundle = ResourceBundle.getBundle("org.radixware.kernel.server.widgets.mess.messages");

			LBL_TIME = bundle.getString("LBL_TIME");
			BTN_START = bundle.getString("BTN_START");
			BTN_STOP = bundle.getString("BTN_STOP");
			LBL_SPEED = bundle.getString("LBL_SPEED");
			LBL_MIN = bundle.getString("LBL_MIN");
			LBL_MAX = bundle.getString("LBL_MAX");

		}
		static final String LBL_TIME;
		static final String BTN_START;
		static final String BTN_STOP;
		static final String LBL_SPEED;
		static final String LBL_MIN;
		static final String LBL_MAX;
	}
}
