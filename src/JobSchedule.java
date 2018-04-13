import java.util.ArrayList;

public class JobSchedule {
	private ArrayList<Job> list;
	//private ArrayList<ArrayList<Job>> incomingList;
	
	public class Job{
		private int time;
		private ArrayList<Job> incomingList;
		private ArrayList<Job> outgoingList;
		private int d;
		private Job parent;
		private boolean finished;
		private JobSchedule schedule;
		private boolean discovered;
		

		
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
			
			// Add this to the outgoing list of j
			j.outgoingList.add(this);
		}
		
		/**
		 * Return the earliest possible starting time for a job
		 * @return
		 * @throws Exception if there is a cycle or if the job cannot be reached
		 */
		public int getStartTime() throws Exception {
			// Initialize the list for DFS
			schedule.initializeDFS();
			
			// Get topological order of the list
			ArrayList<Job> rTopo = schedule.getReverseTopo();
			
			// Run DAGSSS until the job status is true (finished == true)
			for (Job u : rTopo) {
				// Check if all the incoming jobs are done
				boolean incomingDone = true;
				int i = 0;
				while (i < u.incomingList.size() && incomingDone) {
					incomingDone = u.incomingList.get(i).finished;
					i++;
				}
				if (incomingDone) {
					u.finished = true;
				} else {
					return -1;
				}
				
				// Look at all the outgoing list to update them
				for (Job v : u.outgoingList) {
					if(u.d + u.time > v.d || v.d == Integer.MAX_VALUE) {
						if (this.isFinished()) {
							return -1;
						}
						v.d = u.d + u.time;
						v.parent = u;
					}
				}
			}
			
			if(this.d == Integer.MAX_VALUE) {
				return -1;
			}
			
			// Return job.d (distance, ie time, needed to reach the job)
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
		 * Get the list of incoming adjencency (ie, prerequisists)
		 * @return
		 */
		protected ArrayList<Job> incoming(){
			return incomingList;
		}
		
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
		
		
		protected boolean isFinished() {
			return finished;
		}
		
	}
	
	
	public JobSchedule() {
		list = new ArrayList<Job>();
	}
	
	/**
	 * Set all the flags to false
	 */
	private void initializeDFS() {
		for(Job element : list) {
			if (element.incomingList.isEmpty()) {
				element.d = 0;
			} else {
				element.d = Integer.MAX_VALUE;
			}
			element.finished = false;
			element.discovered = false;
		}
	}
	
	/**
	 * Get the list of jobs in reverse topological order
	 * using algo in video "Topological Order and Topological sorting, Taylor, 4:09)
	 * @return the list of jobs in reverse topological order
	 */
	private ArrayList<Job> getReverseTopo() {
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
		return j;
	}
	
	/**
	 * Get a reference to a job at a specific index
	 * @param index job index
	 * @return reference to a job
	 */
	public Job getJob(int index) {
		return list.get(index);
	}
	
	/**
	 * Get the minimum possible completion time for the entire schedule
	 * @return the minimum completion time, or -1 is the schedule is impossible.
	 */
	public int minCompletionTime() {
		return -1;
	}
	

}
