package rank_sort;

import static java.lang.Math.ceil;

class RankSortThread extends Thread {
    private final int[] input, output;

    private int currentItem, currentThreadNum, currentPosition;
    private int startIndex;
    private int endIndex;

    public RankSortThread(int[] input, int[] output, int threadNum, int threadIndex) {
        this.input = input;
        this.output = output;

        this.currentItem = 0;
        this.currentPosition = 0;

        int blockSize = (int) ceil((float) input.length / threadNum);
        startIndex = threadIndex * blockSize;
        if (threadNum - 1 == threadIndex) {
            endIndex = input.length;
        } else {
            endIndex = (threadIndex + 1) * blockSize;
        }
    }

    @Override
    public void run() {
        // Rank sorting algorithm
        for (int j = startIndex; j < endIndex; j++) {
            currentItem = input[j];
            currentPosition = 0;
            for (int i = 0; i < input.length; i++) {
                if (currentItem > input[i])
                    currentPosition++;
                if ((currentItem == input[i]) && (j < i))
                    currentPosition++;
            }
            output[currentPosition] = currentItem;
        }

    }

}
