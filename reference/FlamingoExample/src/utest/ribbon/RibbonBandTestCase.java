package utest.ribbon;

import java.awt.Color;

import junit.framework.TestCase;

import org.jvnet.flamingo.common.JCommandButton;
import org.jvnet.flamingo.common.icon.EmptyResizableIcon;
import org.jvnet.flamingo.ribbon.*;

public class RibbonBandTestCase extends TestCase {
	public void testBands() {
		JRibbonFrame frame = new JRibbonFrame();

		JRibbonBand band11 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band12 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band11.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band11.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band12.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task1 = new RibbonTask("Task 1", band11, band12);
		assertEquals(task1.getBandCount(), 2);

		frame.getRibbon().addTask(task1);
		assertEquals(frame.getRibbon().getSelectedTask().getBandCount(), 2);
		assertEquals(frame.getRibbon().getSelectedTask().getBands().size(), 2);
	}

	public void testSelectedTask() {
		JRibbonFrame frame = new JRibbonFrame();

		JRibbonBand band11 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band12 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band11.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band11.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band12.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task1 = new RibbonTask("Task1", band11, band12);
		frame.getRibbon().addTask(task1);

		assertEquals(frame.getRibbon().getSelectedTask(), task1);

		JRibbonBand band21 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band22 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band21.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band21.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band22.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task2 = new RibbonTask("Task2", band21, band22);
		frame.getRibbon().addTask(task2);

		assertEquals(frame.getRibbon().getSelectedTask(), task1);

		frame.getRibbon().setSelectedTask(task1);
		assertEquals(frame.getRibbon().getSelectedTask(), task1);

		frame.getRibbon().setSelectedTask(task1);
		assertEquals(frame.getRibbon().getSelectedTask(), task1);

		frame.getRibbon().setSelectedTask(task2);
		assertEquals(frame.getRibbon().getSelectedTask(), task2);

		frame.getRibbon().setSelectedTask(task2);
		assertEquals(frame.getRibbon().getSelectedTask(), task2);

		frame.getRibbon().setSelectedTask(task1);
		assertEquals(frame.getRibbon().getSelectedTask(), task1);

		frame.getRibbon().setSelectedTask(task2);
		assertEquals(frame.getRibbon().getSelectedTask(), task2);
	}

	public void testSetTaskTitle() {
		JRibbonFrame frame = new JRibbonFrame();
		JRibbon ribbon = frame.getRibbon();

		JRibbonBand band11 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band12 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band11.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band11.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band12.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task1 = new RibbonTask("Task1", band11, band12);
		ribbon.addTask(task1);

		JRibbonBand band21 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band22 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band21.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band21.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band22.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task2 = new RibbonTask("Task2", band21, band22);
		ribbon.addTask(task2);

		assertEquals(task1.getTitle(), "Task1");
		assertEquals(task2.getTitle(), "Task2");

		task1.setTitle("NewTask1");
		assertEquals(task1.getTitle(), "NewTask1");
		assertEquals(task2.getTitle(), "Task2");

		task1.setTitle("NewTask11");
		assertEquals(task1.getTitle(), "NewTask11");
		assertEquals(task2.getTitle(), "Task2");

		task2.setTitle("NewTask2");
		assertEquals(task1.getTitle(), "NewTask11");
		assertEquals(task2.getTitle(), "NewTask2");

		RibbonTask taskCont1_1 = new RibbonTask("Context task 1 1");
		RibbonTask taskCont1_2 = new RibbonTask("Context task 1 2");
		RibbonTask taskCont1_3 = new RibbonTask("Context task 1 3");
		RibbonContextualTaskGroup taskGroup1 = new RibbonContextualTaskGroup(
				"Context 1", Color.red, taskCont1_1, taskCont1_2, taskCont1_3);
		ribbon.addContextualTaskGroup(taskGroup1);

		assertEquals(task1.getTitle(), "NewTask11");
		assertEquals(task2.getTitle(), "NewTask2");
		assertEquals(taskCont1_1.getTitle(), "Context task 1 1");
		assertEquals(taskCont1_2.getTitle(), "Context task 1 2");
		assertEquals(taskCont1_3.getTitle(), "Context task 1 3");

		taskCont1_1.setTitle("New Context task 1 1");
		assertEquals(task1.getTitle(), "NewTask11");
		assertEquals(task2.getTitle(), "NewTask2");
		assertEquals(taskCont1_1.getTitle(), "New Context task 1 1");
		assertEquals(taskCont1_2.getTitle(), "Context task 1 2");
		assertEquals(taskCont1_3.getTitle(), "Context task 1 3");

		taskCont1_2.setTitle("New Context task 1 2");
		assertEquals(task1.getTitle(), "NewTask11");
		assertEquals(task2.getTitle(), "NewTask2");
		assertEquals(taskCont1_1.getTitle(), "New Context task 1 1");
		assertEquals(taskCont1_2.getTitle(), "New Context task 1 2");
		assertEquals(taskCont1_3.getTitle(), "Context task 1 3");

		taskCont1_3.setTitle("New Context task 1 3");
		assertEquals(task1.getTitle(), "NewTask11");
		assertEquals(task2.getTitle(), "NewTask2");
		assertEquals(taskCont1_1.getTitle(), "New Context task 1 1");
		assertEquals(taskCont1_2.getTitle(), "New Context task 1 2");
		assertEquals(taskCont1_3.getTitle(), "New Context task 1 3");
	}

	public void testSetBandTitle() {
		JRibbonFrame frame = new JRibbonFrame();
		JRibbon ribbon = frame.getRibbon();

		JRibbonBand band11 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band12 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band11.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band11.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band12.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task1 = new RibbonTask("Task1", band11, band12);
		ribbon.addTask(task1);

		JRibbonBand band21 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band22 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		band21.addCommandButton(new JCommandButton("Test 11",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band21.addCommandButton(new JCommandButton("Test 12",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);
		band22.addCommandButton(new JCommandButton("Test 21",
				new EmptyResizableIcon(32)), RibbonElementPriority.TOP);

		RibbonTask task2 = new RibbonTask("Task2", band21, band22);
		ribbon.addTask(task2);

		assertEquals(band11.getTitle(), "Band 1");
		assertEquals(band12.getTitle(), "Band 2");
		assertEquals(band21.getTitle(), "Band 1");
		assertEquals(band22.getTitle(), "Band 2");

		band11.setTitle("New Band 1 1");
		assertEquals(band11.getTitle(), "New Band 1 1");
		assertEquals(band12.getTitle(), "Band 2");
		assertEquals(band21.getTitle(), "Band 1");
		assertEquals(band22.getTitle(), "Band 2");

		band12.setTitle("New Band 1 2");
		assertEquals(band11.getTitle(), "New Band 1 1");
		assertEquals(band12.getTitle(), "New Band 1 2");
		assertEquals(band21.getTitle(), "Band 1");
		assertEquals(band22.getTitle(), "Band 2");

		band21.setTitle("New Band 2 1");
		assertEquals(band11.getTitle(), "New Band 1 1");
		assertEquals(band12.getTitle(), "New Band 1 2");
		assertEquals(band21.getTitle(), "New Band 2 1");
		assertEquals(band22.getTitle(), "Band 2");

		band22.setTitle("New Band 2 2");
		assertEquals(band11.getTitle(), "New Band 1 1");
		assertEquals(band12.getTitle(), "New Band 1 2");
		assertEquals(band21.getTitle(), "New Band 2 1");
		assertEquals(band22.getTitle(), "New Band 2 2");
	}

	public void testSetContextualTaskGroupTitle() {
		JRibbonFrame frame = new JRibbonFrame();
		JRibbon ribbon = frame.getRibbon();

		JRibbonBand band11 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band12 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		RibbonTask task1 = new RibbonTask("Task1", band11, band12);
		ribbon.addTask(task1);

		JRibbonBand band21 = new JRibbonBand("Band 1", new EmptyResizableIcon(
				32));
		JRibbonBand band22 = new JRibbonBand("Band 2", new EmptyResizableIcon(
				32));
		RibbonTask task2 = new RibbonTask("Task2", band21, band22);
		ribbon.addTask(task2);

		RibbonTask taskCont1_1 = new RibbonTask("Context task 1 1");
		RibbonTask taskCont1_2 = new RibbonTask("Context task 1 2");
		RibbonTask taskCont1_3 = new RibbonTask("Context task 1 3");
		RibbonContextualTaskGroup taskGroup1 = new RibbonContextualTaskGroup(
				"Context 1", Color.red, taskCont1_1, taskCont1_2, taskCont1_3);
		ribbon.addContextualTaskGroup(taskGroup1);

		RibbonTask taskCont2_1 = new RibbonTask("Context task 2 1");
		RibbonTask taskCont2_2 = new RibbonTask("Context task 2 2");
		RibbonContextualTaskGroup taskGroup2 = new RibbonContextualTaskGroup(
				"Context 2", Color.blue, taskCont2_1, taskCont2_2);
		ribbon.addContextualTaskGroup(taskGroup2);

		assertEquals(taskGroup1.getTitle(), "Context 1");
		assertEquals(taskGroup2.getTitle(), "Context 2");

		taskGroup1.setTitle("New Context 1");
		assertEquals(taskGroup1.getTitle(), "New Context 1");
		assertEquals(taskGroup2.getTitle(), "Context 2");

		taskGroup2.setTitle("New Context 2");
		assertEquals(taskGroup1.getTitle(), "New Context 1");
		assertEquals(taskGroup2.getTitle(), "New Context 2");
	}
}
