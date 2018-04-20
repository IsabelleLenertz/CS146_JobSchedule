import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
		
		JobSchedule.Job j = schedule.getJob(0);
		assertNotNull(j);

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
	
	@Test
	public void teacherSTests() {
		JobSchedule schedule = new JobSchedule();
		schedule.addJob(8); //adds job 0 with time 8
		JobSchedule.Job j1 = schedule.addJob(3); //adds job 1 with time 3
		schedule.addJob(5); //adds job 2 with time 5
		assertEquals(8, schedule.minCompletionTime()); //should return 8, since job 0 takes time 8 to complete.
		
		/* Note it is not the min completion time of any job, but the earliest the entire set can complete. */
		schedule.getJob(0).requires(schedule.getJob(2)); //job 2 must precede job 0
		assertEquals(13, schedule.minCompletionTime()); //should return 13 (job 0 cannot start until time 5)
		schedule.getJob(0).requires(j1); //job 1 must precede job 0
		assertEquals(13, schedule.minCompletionTime()); //should return 13
		assertEquals(5, schedule.getJob(0).getStartTime()); //should return 5
		assertEquals(0, j1.getStartTime()); //should return 0
		assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0
		j1.requires(schedule.getJob(2)); //job 2 must precede job 1
		assertEquals(16, schedule.minCompletionTime()); //should return 16
		assertEquals(8, schedule.getJob(0).getStartTime()); //should return 8
		assertEquals(5, schedule.getJob(1).getStartTime()); //should return 5
		assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0
		schedule.getJob(1).requires(schedule.getJob(0)); //job 0 must precede job 1 (creates loop)
		assertEquals(-1, schedule.minCompletionTime()); //should return -1
		assertEquals(-1, schedule.getJob(0).getStartTime()); //should return -1
		assertEquals(-1, schedule.getJob(1).getStartTime()); //should return -1
		assertEquals(0, schedule.getJob(2).getStartTime()); //should return 0 (no loops in prerequisites)
	}

}
