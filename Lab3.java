import java.io.*;
import java.util.*;

public class Lab3
{

    /**
     *  Problem 1: Use heaps to sort a given array in O(n log k) time.
     */
    private static void problem1(int[] arr, int k)
    {
        //create the heap using the arr[0] - arr[k+1]
        MinHeap heap = new MinHeap(Arrays.copyOfRange(arr, 0, k+1));
        //set up the index
        int index = 0;

        //set up the for loop, and when there is no new element from the array that can add to heap
        for(int i=k+1; i<arr.length; i++){
            //make the min of heap to arr[index]
            arr[index] = heap.getMin();
            //then remove the min, and add next elements into heap
            heap.removeMin();
            heap.add(arr[i]);
            //increasing index
            index++;
        }

        //when no new element adding to the heap, set up the loop letting elements in heap to arr by order
        while(index<arr.length){
            //get the min in the heap, and add to arr
            arr[index] = heap.getMin();
            //remove the min
            heap.removeMin();
            //increasing the index
            index++;
        }
    }

    /**
     *  Problem 2: Determine the maximum amount of memory you can use in O(n log n) time.
     */
    private static int problem2(int[] capasities, int k)
    {
        //create the result=0
        int result = 0;

        //negate the elements in the array
        for(int i=0; i<capasities.length; i++){
            capasities[i] = -1*capasities[i];
        }

        //add the negated array into heap
        MinHeap h = new MinHeap(capasities);

        //set up the for loop to get the min negative number(which is max positive number)
        for(int i=0; i<k; i++){
            //result += max positive number
            result += h.getMin();
            //get the new number which is max/2
            int halfMin = h.getMin()/2;
            //remove the max from the heap since we already add it to the result
            h.removeMin();
            //add the new number max/2 back to the heap
            h.add(halfMin);
        }

        //return the result
        return result*-1;
    }

    // ---------------------------------------------------------------------
    // Do not change any of the code below!

    private static final int LabNo = 3;
    private static final Random rng = new Random(123456);

    private static boolean testProblem1(int[][] testCase)
    {
        int[] arr = testCase[0];
        int k = testCase[1][0];

        int[] testArr = arr.clone();
        int[] arr2 = arr.clone();
        Arrays.sort(arr2);

        problem1(testArr, k);

        // Check if arr is sorted
        for (int i = 0; i < testArr.length; i++)
        {
            if (arr2[i] != testArr[i])
            {
                return false;
            }
        }

        return true;
    }

    private static boolean testProblem2(int[][] testCase)
    {
        int[] arr = testCase[0];
        int k = testCase[1][0];
        int solution = testCase[1][1];

        int answer = problem2(arr.clone(), k);

        if (solution != answer)
        {
            System.out.println();
            System.out.println("a: " + answer);
            System.out.println("s: " + solution);
        }

        return solution == answer;
    }

    public static void main(String args[])
    {
        System.out.println("Data Structure -- Lab " + LabNo);

        testProblems(1);
        testProblems(2);
    }

    private static void testProblems(int prob)
    {
        int noOfLines = 100000;

        System.out.println("-- -- -- -- --");
        System.out.println(noOfLines + " test cases for problem " + prob + ".");

        boolean passedAll = true;

        for (int i = 1; i <= noOfLines; i++)
        {

            int[][] testCase = null;

            boolean passed = false;
            boolean exce = false;

            try
            {
                switch (prob)
                {
                    case 1:
                        testCase = createProblem1(i);
                        passed = testProblem1(testCase);
                        break;

                    case 2:
                        testCase = createProblem2(i);
                        passed = testProblem2(testCase);
                        break;
                }
            }
            catch (Exception ex)
            {
                passed = false;
                exce = true;
            }

            if (!passed)
            {
                System.out.println("Test " + i + " failed!" + (exce ? " (Exception)" : ""));
                System.out.println("  arr: " + Arrays.toString(testCase[0]));
                System.out.println("    k: " + testCase[1][0]);

                passedAll = false;
                break;
            }
        }

        if (passedAll)
        {
            System.out.println("All test passed.");
        }
    }

    private static int[][] createProblem1(int testNo)
    {
        int size = rng.nextInt(Math.min(50, testNo)) + 5;
        int[] k = new int[] { rng.nextInt(size - 3) + 3 };

        int[] numbers = getRandomNumbers(size);

        Arrays.sort(numbers);
        Hashtable<Integer, Integer> dic = GetShuffle(size, k);

        int[] buffer = new int[size];

        for (int i = 0; i < size; i++)
        {
            int shufInd = dic.get(i);
            buffer[shufInd] = numbers[i];
        }

        for (int i = 0; i < size; i++)
        {
            numbers[i] = buffer[i];
        }

        return new int[][] { numbers, k };
    }

    private static int[][] createProblem2(int testNo)
    {
        int size = rng.nextInt(Math.min(1024, testNo)) + 5;
        int[] numbers = getRandomNumbers(size);

        int log = 0;
        for (int t = testNo; t > 0; t /= 2, log++) { }
        int maxK = rng.nextInt(log * log) + 5;

        int max = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++)
        {
            if (numbers[i] == 0) numbers[i]++;
            max = Math.max(max, numbers[i]);
        }

        for (int i = 0; i < size; i++)
        {
            while (numbers[i] < (max + 1) / 2) numbers[i] *= 2;
        }

        int solution = 0;
        int k = -1;
        int maxNum = 1 << 20;

        for (k = 0; k < maxK; k++)
        {
            int num = -1;
            int r = -1;

            while (size > 1)
            {
                r = rng.nextInt(size);
                num = numbers[r];

                if (num <= maxNum && num >= (max + 1) / 2) break;

                // Too small; exclude.
                size--;
                numbers[r] = numbers[size];
                numbers[size] = num;
            }

            if (size <= 1) break;

            num = 2 * num + rng.nextInt(2);
            max = Math.max(max, num);

            solution += num;
            numbers[r] = num;
        }

        return new int[][]
        {
            numbers,
            new int[] { k, solution }
        };
    }

    private static class ShuffleItem implements Comparable<ShuffleItem>
    {
        public int key;
        public double shift;

        @Override
        public int compareTo(ShuffleItem other) {
            Double thisDbl = shift;
            return thisDbl.compareTo(other.shift);
        }
    }

    private static Hashtable<Integer, Integer> GetShuffle(int size, int[] k)
    {
        ShuffleItem[] arr = new ShuffleItem[size];
        for (int i = 0; i < arr.length; i++)
        {
            arr[i] = new ShuffleItem();
            arr[i].key = i;
            arr[i].shift = i + rng.nextDouble() * k[0];
        }

        Arrays.sort(arr);

        int minDif = Integer.MAX_VALUE;
        int maxDif = Integer.MIN_VALUE;
        int minDifAbs = Integer.MAX_VALUE;
        int maxDifAbs = Integer.MIN_VALUE;

        Hashtable<Integer, Integer> dict = new Hashtable<Integer, Integer>();

        for (int i = 0; i < arr.length; i++)
        {
            int dif = i - arr[i].key;

            minDif = Math.min(minDif, dif);
            maxDif = Math.max(maxDif, dif);
            minDifAbs = Math.min(minDifAbs, Math.abs(dif));
            maxDifAbs = Math.max(maxDifAbs, Math.abs(dif));

            dict.put(arr[i].key, i);
        }

        k[0] = maxDifAbs;
        return dict;
    }

    private static void shuffle(int[] arr, int start, int length)
    {
        // Shuffle
        for (int i = 0; i < length; i++)
        {
            int baseInd = start + i;
            int rndInd = rng.nextInt(length - i) + baseInd;

            int tmp = arr[baseInd];
            arr[baseInd] = arr[rndInd];
            arr[rndInd] = tmp;
        }
    }

    private static int[] getRandomNumbers(int size)
    {
        int maxSize = size * 10;

        int[] integers = new int[maxSize];
        for (int i = 0; i < maxSize; i++)
        {
            integers[i] = i;
        }

        shuffle(integers, 0, integers.length);

        return Arrays.copyOf(integers, size);
    }

}
