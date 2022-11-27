package rank_sort;

class ParallelRankThread extends Thread {
	private int[] readArray;
	private int[] resultArray;
	private int threadNum;
	private int threadIndex;

	private int currentItem;
	private int currentPosition;
	private int startIndex;
	private i endIndex;
	private String threadName;
	private int currentThreadNum;

	public ParallelRankThread(int[] readArray, int[] resultArray, int threadNum, int threadIndex) {
		this.readArray = readArray;
		this.resultArray = resultArray;
		this.threadNum = threadNum;
		this.threadIndex = threadIndex;

		this.currentItem = 0;
		this.currentPosition = 0;
		this.startIndex = 0;
		this.endIndex = 0;
		this.threadName = "";
		this.currentThreadNum = 0;
	}

	@Override
	public void run() {
		threadName = Thread.currentThread().getName();
		currentThreadNum = threadIndex;

		int blockSize = (int) Math.ceil(readArray.length / threadNum);
		startIndex = currentThreadNum * blockSize;
		if (threadNum - 1 == currentThreadNum) {
			endIndex = readArray.length;
		} else {
			endIndex = (currentThreadNum + 1) * blockSize;
		}

		// Rank sort
		for (int j = startIndex; j < endIndex; j++) {
			currentItem = readArray[j];
			currentPosition = 0;
			for (int i = 0; i < readArray.length; i++) {
				if (currentItem > readArray[i]) {
					currentPosition++;
				}
				if ((currentItem == readArray[i]) && (j < i)) {
					currentPosition++;
				}
			}
			resultArray[currentPosition] = currentItem;
		}

	}

}
