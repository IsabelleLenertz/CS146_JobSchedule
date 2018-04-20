import java.util.ArrayList;
import java.util.List;

public class JobSchedule {
	private ArrayList<Job> list;
	
	public class Job{
		private int time;
		private ArrayList<Job> outgoingList;
		private int d;
		private int inDegree;

		
		/**
		 * Construct a job with a defined time
		 * @param t time needed to complete the job
		 */
		protected Job(int t) {
			outgoingList = new ArrayList<Job>();
			time = t;
		}
		
		/**
		 * Set the requirement for the job
		 * @param j job that has to be done before this job can start
		 */
		public void requires(Job j) {
			// Add j to the incoming list of this
			j.outgoingList.add(this);
		}
		
		/**
		 * Return the earliest possible starting time for a job
		 * @return
		 * @throws Exception if there is a cycle or if the job cannot be reached
		 */
		public int getStartTime() {				
			// Initializes the in degree to 0
			for(Job u : list) {
				u.inDegree = 0;
			}
			// Get the actual inDegree for each job
			for(Job u : list) {
				for(Job v : u.outgoingList) {
					v.inDegree++;				
				}
				
			}
			// Put all the Job with no requirement in the list
			List<Job> topoList = new ArrayList<Job>();
			for(Job u : list) {
				if(u.inDegree == 0) {
					u.d = 0;
					topoList.add(u);
				} else {
					u.d = -1;
				}
			}
			// Decrease in degree of all the jobs in the list
			// Add the job that have now in degree 0
			int i = 0;
			while(i < topoList.size()) {
				Job u = topoList.get(i);
				for(Job v : u.outgoingList) {
					v.inDegree--; 
					v.d = Integer.max(v.d, u.d + u.time);
					if(v.inDegree == 0)
						if(v == this) {
							return v.d;
						}
						topoList.add(v);
					}
			}		
			// if this was never reached
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
		return j;
	}
	

	/**
	 * Get the minimum possible completion time for the entire schedule
	 * @return the minimum completion time, or -1 is the schedule is impossible.
	 */
	public int minCompletionTime() {
		int maxCompletionTime = 0;
		// Initializes the in degree to 0
		for(Job u : list) {
			u.inDegree = 0;
		}
		// Get the actual inDegree for each job
		for(Job u : list) {
			for(Job v : u.outgoingList) {
				v.inDegree++;				
			}
			
		}
		// Put all the Job with no requirement in the list
		List<Job> topoList = new ArrayList<Job>();
		for(Job u : list) {
			if(u.inDegree == 0) {
				u.d = 0;
				topoList.add(u);
			} else {
				u.d = -1;
			}
		}
		// Decrease in degree of all the jobs in the list
		// Add the job that have now in degree 0
		for (int i = 0; i < topoList.size(); i++) {
			Job u = topoList.get(i);
			for(Job v : u.outgoingList) {
				v.inDegree--; 
				v.d = Integer.max(v.d, u.d + u.time);
				maxCompletionTime = Integer.max(maxCompletionTime,  v.d + v.time);
				if(v.inDegree == 0) {
					topoList.add(v);
				}
			}
		}
		
		if (topoList.size() < list.size()) {
			maxCompletionTime = -1;
		}
		return maxCompletionTime;
	}
	

}