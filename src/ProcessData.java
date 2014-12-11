/**
 * ProcessData
 * 
 * @author Tomas Barry
 * 
 */
public class ProcessData {
	private String[] data;
	private String target;
	private boolean targetFound;

	/**
	 * ProcessData constructor
	 * 
	 * @param data
	 */
	public ProcessData(byte[] data, String target) {
		this.data = (new String(data)).split("\n");
		this.target = target;
		this.targetFound = false;
	}

	/**
	 * 
	 * @param worker
	 */
	public void processTheData(Client worker) {
		for (String s : data) {
			if (s.equals(getTarget())) {
				System.out.println("Sucess finding " + getTarget());
				targetFound = true;
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 
	 * @return
	 */
	public String[] getData() {
		return data;
	}

	/**
	 * 
	 * @return
	 */
	public boolean foundTarget() {
		return targetFound;
	}
}