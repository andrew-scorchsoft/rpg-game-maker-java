package utest.common;

import java.awt.Dimension;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.jvnet.flamingo.common.AsynchronousLoadListener;
import org.jvnet.flamingo.common.icon.ImageWrapperResizableIcon;

public class ImageIconResizingTestCase extends TestCase {
	protected ImageWrapperResizableIcon icon;

	@Override
	protected void setUp() throws Exception {
		URL resource = ImageIconResizingTestCase.class.getClassLoader()
				.getResource("utest/common/edit-paste.png");
		assertNotNull(resource);
		this.icon = ImageWrapperResizableIcon.getIcon(resource, new Dimension(
				32, 32));
		assertNotNull(this.icon);
		final CountDownLatch latch = new CountDownLatch(1);
		this.icon.addAsynchronousLoadListener(new AsynchronousLoadListener() {
			@Override
			public void completed(boolean success) {
				if (success) {
					latch.countDown();
				}
			}
		});
		try {
			latch.await();
		} catch (InterruptedException ie) {
			assertTrue(false);
		}
	}

	public void testNoCompletedMessageOnSettingSameHeight() {
		final int[] count = new int[] { 0 };
		final CountDownLatch latch = new CountDownLatch(1);
		AsynchronousLoadListener listener = new AsynchronousLoadListener() {
			@Override
			public void completed(boolean success) {
				count[0]++;
				latch.countDown();
			}
		};
		this.icon.addAsynchronousLoadListener(listener);
		// verify the icon height
		assertEquals(this.icon.getIconHeight(), 32);
		// set the icon height to the same value
		this.icon.setDimension(new Dimension(32, 32));
		try {
			// latch.await should not return true as that would mean that
			// the asynchronous load listener was notified
			assertFalse(latch.await(5, TimeUnit.SECONDS));
		} catch (InterruptedException ie) {
			assertTrue(false);
		}
		assertEquals(count[0], 0);
		this.icon.removeAsynchronousLoadListener(listener);
	}

	public void testCompletedMessageOnSettingDifferentHeight() {
		final int[] count = new int[] { 0 };
		final CountDownLatch latch = new CountDownLatch(1);
		AsynchronousLoadListener listener = new AsynchronousLoadListener() {
			@Override
			public void completed(boolean success) {
				if (success) {
					count[0]++;
				}
				latch.countDown();
			}
		};
		this.icon.addAsynchronousLoadListener(listener);
		// verify the icon height
		assertEquals(this.icon.getIconHeight(), 32);
		// set the icon height to different value
		this.icon.setDimension(new Dimension(16, 16));
		try {
			latch.await();
		} catch (InterruptedException ie) {
			assertTrue(false);
		}
		assertEquals(count[0], 1);
		this.icon.removeAsynchronousLoadListener(listener);
	}

	public void testChangedHeightOnSettingDifferentHeight() {
		final CountDownLatch latch = new CountDownLatch(1);
		AsynchronousLoadListener listener = new AsynchronousLoadListener() {
			@Override
			public void completed(boolean success) {
				latch.countDown();
			}
		};
		this.icon.addAsynchronousLoadListener(listener);
		// verify the icon height
		assertEquals(this.icon.getIconHeight(), 32);
		// set the icon height to different value
		this.icon.setDimension(new Dimension(16, 16));
		try {
			latch.await();
		} catch (InterruptedException ie) {
			assertTrue(false);
		}
		assertEquals(icon.getIconHeight(), 16);
		this.icon.removeAsynchronousLoadListener(listener);
	}
}
