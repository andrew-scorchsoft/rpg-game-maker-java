package utest.slider;

import junit.framework.TestCase;

import org.jvnet.flamingo.slider.DefaultFlexiRangeModel;
import org.jvnet.flamingo.slider.FlexiRangeModel;

public class DefaultFlexiRangeModelTestCase extends TestCase {

	public void testGetValue1() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);
		assertNull(model.getValue());
	}

	public void testGetValue2() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range0, 0.0);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testGetValue3() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range0, 1.0);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testGetValue4() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range1, 0.0);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testGetValue5() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range2, 0.5);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testGetValue6() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range2, 1.0);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testGetValue7() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range3, 0.8);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testGetValue8() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range5, 1.0);
		model.setValue(value);
		assertEquals(value, model.getValue());
	}

	public void testSetValue1() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		FlexiRangeModel.Value value = new FlexiRangeModel.Value(range0, 0.0);
	}

	public void testSetValue2() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		try {
			// should fail since range0 is discrete
			FlexiRangeModel.Value value = new FlexiRangeModel.Value(range0, 0.5);
			model.setValue(value);
		} catch (IllegalArgumentException iae) {
			return;
		}
		assertTrue(false);
	}

	public void testSetValue3() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		try {
			// should fail since fraction is negative
			FlexiRangeModel.Value value = new FlexiRangeModel.Value(range2,
					-0.2);
			model.setValue(value);
		} catch (IllegalArgumentException iae) {
			return;
		}
		assertTrue(false);
	}

	public void testSetValue4() {
		FlexiRangeModel model = new DefaultFlexiRangeModel();
		FlexiRangeModel.Range range0 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range1 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range2 = new FlexiRangeModel.Range(false, 1.0);
		FlexiRangeModel.Range range3 = new FlexiRangeModel.Range(false, 2.0);
		FlexiRangeModel.Range range4 = new FlexiRangeModel.Range(true, 0.0);
		FlexiRangeModel.Range range5 = new FlexiRangeModel.Range(false, 1.0);
		model.setRanges(range0, range1, range2, range3, range4, range5);

		try {
			// should fail since range fraction is larger than 1.5
			FlexiRangeModel.Value value = new FlexiRangeModel.Value(range2, 1.5);
			model.setValue(value);
		} catch (IllegalArgumentException iae) {
			return;
		}
		assertTrue(false);
	}
}
