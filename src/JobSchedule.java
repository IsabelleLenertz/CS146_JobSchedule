import java.util.ArrayList;
import java.util.List;

public class JobSchedule {
	private ArrayList<Job> list;
	private boolean distanceUpToDate;
	private int completionTime;
	
	public class Job{
		private int time;
		private ArrayList<Job> outgoingList;
		private int d;
		private int inDegree;
		private int kahnInDegree;

		
		/**
		 * Construct a job with a defined time
		 * @param t time needed to complete the job
		 */
		private Job(int t) {
			outgoingList = new ArrayList<Job>();
			time = t;
			inDegree = 0;
		}
		
		/**
		 * Set the requirement for the job
		 * @param j job that has to be done before this job can start
		 */
		public void requires(Job j) {
			// Add j to the incoming list of this
			j.outgoingList.add(this);
			this.inDegree++;
			distanceUpToDate = false;
		}
		
		/**
		 * Return the earliest possible starting time for a job
		 * @return
		 * @throws Exception if there is a cycle or if the job cannot be reached
		 */
		public int getStartTime() {	
			
			// If there was a modification since last time Kahn's algorithm was ran.
			if(!distanceUpToDate) {
				khanAlgo();
			} 
			
			// Make sure the Job is not in a circle (ie, Kahn's algorithm forced the indegree to 0
			if(this.kahnInDegree == 0) {
				return this.d;
			}
			// If the Job was not reached
			return -1;
		}
		
					
	}
	
	
	public JobSchedule() {
		list = new ArrayList<Job>();
	}
		
	/**
	 * Add a new job to the schedule
	 * @param time time to complete the job
	 * @return a reference to the job that was created
	 */
	public Job addJob(int time) {
		Job j = new Job(time);
		list.add(j);
		distanceUpToDate = false;
		return j;
	}
	
	public Job getJob(int index) {
		return list.get(index);
	}
	
	private void khanAlgo() {
		completionTime= 0;
		distanceUpToDate = true;
		
		// Initializes the in degree to the real in degree and the distance to 0
		for(Job u : list) {
			u.kahnInDegree= u.inDegree;
			u.d = 0;
		}

		// Put all the Job with no requirement in a list
		List<Job> topoList = new ArrayList<Job>();
		for(Job u : list) {
			if(u.kahnInDegree == 0) {
				topoList.add(u);
			}		
		}
		
		// Decrease in degree of all the jobs outgoing in the list
		// Add the job that have now in degree 0
		for (int i = 0; i < topoList.size(); i++) {
			Job u = topoList.get(i);
			completionTime = max(completionTime,  u.d + u.time);
			for(Job v : u.outgoingList) {
				v.kahnInDegree--; 
				v.d = max(v.d, u.d + u.time);
				if(v.kahnInDegree == 0) {
					topoList.add(v);
				}
			}
		}
		
		if (topoList.size() < list.size()) {
			completionTime = -1;
		}
	}

	/**
	 * Get the minimum possible completion time for the entire schedule
	 * @return the minimum completion time, or -1 is the schedule is impossible.
	 */
	public int minCompletionTime() {
		// If there was a modification since last time Kahn's algorithm was ran.
		if(!distanceUpToDate) {
			khanAlgo();
		} 
		return this.completionTime;

	}
	
	private static int max(int a, int b) {
		if (a>b)
			return a;
		else
			return b;
	}
	

}