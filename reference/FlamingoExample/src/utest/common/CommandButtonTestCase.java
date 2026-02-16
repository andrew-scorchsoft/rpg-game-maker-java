package utest.common;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import junit.framework.TestCase;

import org.jvnet.flamingo.common.CommandButtonDisplayState;
import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;

public class CommandButtonTestCase extends TestCase {
	public void testButtonActivationWithSpaceKey() {
		final CountDownLatch latch = new CountDownLatch(1);
		final Throwable[] ts = new Throwable[1];
		final int[] count = new int[] { 0 };
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();
				final JCommandButton button = new JCommandButton("test",
						new EmptyResizableIcon(32));
				button.setDisplayState(CommandButtonDisplayState.BIG);
				frame.add(button, BorderLayout.CENTER);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);

				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						count[0]++;
					}
				});

				Thread.currentThread().setUncaughtExceptionHandler(
						new Thread.UncaughtExceptionHandler() {
							@Override
							public void uncaughtException(Thread t, Throwable e) {
								ts[0] = e;
							}
						});

				new Thread() {
					@Override
					public void run() {
						try {
							button.requestFocus();
							Robot robot = new Robot();
							robot.keyPress(KeyEvent.VK_SPACE);
							robot.keyRelease(KeyEvent.VK_SPACE);
							Thread.sleep(1000);
						} catch (Throwable t) {
							ts[0] = t;
						}
						frame.dispose();
						latch.countDown();
					}
				}.start();
			}
		});
		try {
			assertTrue(latch.await(10, TimeUnit.SECONDS));
		} catch (Exception exc) {
		}
		if (ts[0] != null)
			ts[0].printStackTrace();
		assertNull(ts[0]);
		assertEquals(count[0], 1);
	}

	public void testButtonActivationWithMouse() {
		final CountDownLatch latch = new CountDownLatch(1);
		final Throwable[] ts = new Throwable[1];
		final int[] count = new int[] { 0 };
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final JFrame frame = new JFrame();
				final JCommandButton button = new JCommandButton("test",
						new EmptyResizableIcon(32));
				button.setDisplayState(CommandButtonDisplayState.BIG);
				frame.add(button, BorderLayout.CENTER);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);

				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						count[0]++;
					}
				});

				Thread.currentThread().setUncaughtExceptionHandler(
						new Thread.UncaughtExceptionHandler() {
							@Override
							public void uncaughtException(Thread t, Throwable e) {
								ts[0] = e;
								e.printStackTrace();
							}
						});

				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						try {
							Robot robot = new Robot();
							Point buttonLeftTop = button.getLocationOnScreen();
							int buttonWidth = button.getWidth();
							int buttonHeight = button.getHeight();
							robot.mouseMove(buttonLeftTop.x + buttonWidth / 2,
									buttonLeftTop.y + buttonHeight / 2);
							robot.mousePress(InputEvent.BUTTON1_MASK);
							robot.mouseRelease(InputEvent.BUTTON1_MASK);
							Thread.sleep(1000);
						} catch (Throwable t) {
							ts[0] = t;
						}
						new Thread() {
							@Override
							public void run() {
								frame.dispose();
								latch.countDown();
							}
						}.start();
					}
				});
			}
		});
		try {
			assertTrue(latch.await(10, TimeUnit.SECONDS));
		} catch (Exception exc) {
		}
		assertNull(ts[0]);
		assertEquals(count[0], 1);
	}
}
