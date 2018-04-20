import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class JobSchedule {
	private ArrayList<Job> list;
	private ArrayList<Job> rTopo;
	
	public class Job{
		private int time;
		private ArrayList<Job> incomingList;
		private ArrayList<Job> outgoingList;
		private int d;
		private JobSchedule schedule;
		private boolean discovered;	
		private int inDegree;
		private static final int START_NOT_SET = -1;

		
		/**
		 * Construct a job with a defined time
		 * @param t time needed to complete the job
		 */
		protected Job(int t, JobSchedule thisSchedule) {
			incomingList = new ArrayList<Job>();
			outgoingList = new ArrayList<Job>();
			time = t;
			schedule = thisSchedule;
		}
		
		/**
		 * Set the requirement for the job
		 * @param j job that has to be done before this job can start
		 */
		public void requires(Job j) {
			// Add j to the incoming list of this
			incomingList.add(j);
			j.outgoingList.add(this);
		}
		
		/**
		 * Return the earliest possible starting time for a job
		 * @return
		 * @throws Exception if there is a cycle or if the job cannot be reached
		 */
		public int getStartTime() {
			// Initialize the list for DFS
			schedule.initializeDFS();
						
			// Run DAGSSS until the job status is true (finished == true)
			int i = 0;
			Job u = null;
			while (u != this && i < schedule.rTopo.size()) {
				u = schedule.rTopo.get(i);
				for (Job v : u.incomingList) {
					if (v.d == -1) {
						return -1;
					}
					u.d = Integer.max(u.d, v.d+v.time);
				}
				i++;
			}
			return this.d;
			
		}
		
		/**
		 * Relax edge u->v, where this is v
		 * @param u incoming job, u.time is the weight of the edge
		 * @throws Exception 
		 */
		/**private void relax(Job u) throws Exception {
			if(u.d + u.time > this.d || this.d == Integer.MAX_VALUE) {
				if (this.isFinished()) {
					throw new Exception("No solution");
				}
				this.d = u.d + u.time;
				this.parent = u;
			}
		}/*
		

		
		/**
		 * Get the jobs in reverse topological order
		 * @return
		 */
		private void getRTopological(ArrayList<Job> rTopo){
			this.discovered = true;
			
			for(Job element : incomingList) {
				if(element.discovered == false) {
					element.getRTopological(rTopo);
				}
			}
			rTopo.add(this);
		}
		
		
	}
	
	
	public JobSchedule() {
		list = new ArrayList<Job>();
		rTopo = new ArrayList<Job>();
	}
	
	/**
	 * Set all the flags to false
	 */
	private void initializeDFS() {
		for(Job element : list) {
			if (element.incomingList.isEmpty()) {
				element.d = 0;
			} else {
				element.d = -1;
			}
			element.discovered = false;
		}
	}
	
	/**
	 * Get the list of jobs in reverse topological order
	 * using algo in video "Topological Order and Topological sorting, Taylor, 4:09)
	 * @return the list of jobs in reverse topological order
	 */
	private ArrayList<Job> getReverseTopo() {
		this.initializeDFS();
		ArrayList<Job> rTopo = new ArrayList<Job>();
		for(Job element : list) {
			if(element.discovered == false) {
				element.getRTopological(rTopo);
			}
		}
		return rTopo;
	}
	
	/**
	 * Add a new job to the schedule
	 * @param time time to complete the job
	 * @return a reference to the job that was created
	 */
	public Job addJob(int time) {
		Job j = new Job(time, this);
		list.add(j);
		rTopo = this.getReverseTopo();
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
			u.d = 0;
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
				topoList.add(u);
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