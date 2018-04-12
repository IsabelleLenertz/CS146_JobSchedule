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
		 */
		public int getStartTime() {
			// Initialize the list for DFS
			schedule.initializeDFS();
			
			// Get topological order of the list
			ArrayList<Job> rTopo = schedule.getReverseTopo();
			
			// Run DAGSSS until the job status is true (finished == true)
			//DAG SSSP 6:59
			
			// Return job.d
			return -1;
		}
		
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
		
		
		protected void isNotDone() {
			finished = false;
		}
		
		protected void isDone() {
			finished = true;
		}
		
		protected boolean status() {
			return finished;
		}
		
		protected void setDiscovered(boolean d) {
			discovered = d;
		}
	}
	
	/**
	 * Set all the flags to false
	 */
	private void initializeDFS() {
		for(Job element : list) {
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
