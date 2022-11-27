package rank_sort;

import java.util.concurrent.ThreadLocalRandom;

class ParallelRankSort {
	public static void main(String[] args) {

		// Get command line arguments
		int elemQuantity = 1_000_000;
		System.out.println("Problem size: " + elemQuantity);
		int threadNum = 16;


		// Initialize arrays
		int[] readArray = new int[elemQuantity];
		int[] resultArray = new int[elemQuantity];


		// Create initial un-sorted array and print it
		for(int i = 0; i < elemQuantity; i++) {
			readArray[i] = ThreadLocalRandom.current().nextInt(0, 1_000_000);
			resultArray[i] = 0;
		}

		// Start timer
		long start = System.currentTimeMillis();

		// Calculate blocks of operation for threads
		int blockSize = elemQuantity / threadNum;

		// Start threads
		ParallelRankThread threads[] = new ParallelRankThread[threadNum];
		for(int i = 0, j = 0; i < threadNum; i++, j += blockSize) {
			threads[i] = new ParallelRankThread(readArray, resultArray, threadNum, i);
			threads[i].start();
		}

		// Join threads
		try {
			for(int i = 0; i < threadNum; i++) {
				threads[i].join();
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		// Stop timer
		long elapsedTimeMillis = System.currentTimeMillis() - start;
		System.out.println("Duration: " + elapsedTimeMillis + "ms");

	}
}
