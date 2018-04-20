import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JobScheduleTest {


	@Test
	public void getStartTimeTest() {
		JobSchedule schedule = new JobSchedule();
		JobSchedule.Job job2 = schedule.addJob(2);
		JobSchedule.Job job1 = schedule.addJob(1);
		JobSchedule.Job job3 = schedule.addJob(3);
		job2.requires(job1);
		job3.requires(job2);
		
		assertEquals(3, job3.getStartTime());
		assertEquals(1, job2.getStartTime());
		assertEquals(0, job1.getStartTime());
		
		JobSchedule.Job job5 = schedule.addJob(5);
		job3.requires(job5);
		assertEquals(5, job3.getStartTime());
		
		// Create a cycle
		job1.requires(job3);
		assertEquals(0, job5.getStartTime());
		assertEquals(-1, job1.getStartTime());

	}
	
	@Test
	public void getCompletionTimeTest() {
		
		JobSchedule schedule = new JobSchedule();
		JobSchedule.Job job2 = schedule.addJob(2);
		JobSchedule.Job job1 = schedule.addJob(1);
		JobSchedule.Job job3 = schedule.addJob(3);
		job2.requires(job1);
		job3.requires(job2);
		
		assertEquals(6, schedule.minCompletionTime());
		
		JobSchedule.Job job5 = schedule.addJob(5);
		job3.requires(job5);
		assertEquals(8, schedule.minCompletionTime());
		
		// Create cycle
		job1.requires(job3);
		assertEquals(-1, schedule.minCompletionTime());
	}

}
