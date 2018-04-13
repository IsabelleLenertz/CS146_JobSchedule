import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JobScheduleTest {


	@Test
	void getStartTimeTest() throws Exception {
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

}
