public class DoubleMergeSorter implements IPrimitiveSorter
{
	private double[] array;
	private double[] work;
	
	public DoubleMergeSorter(double[] data)
	{
		array = data;
		work = new double[data.length];
	}
	
	@Override
	public void sort()
	{
		sort(0, array.length);
	}
	
	/**
	 * Sorts a section of the array.
	 * 
	 * @param start The starting index, inclusive
	 * @param end The ending index, exclusive
	 */
	private void sort(int start, int end)
	{
		// If there is only one element to sort,
		if (end - start <= 1) return;
		
		// Sort two halves
		int mid = (start + end) / 2;
		sort(start, mid);
		sort(mid, end);
		
		// Merge.
		int leftIndex = start;
		int rightIndex = mid;
		for (int index = start; index < end; index ++)
		{
			if (leftIndex >= mid) {
				// Reached end on left side
				work[index] = array[rightIndex];
				rightIndex ++;
				continue;
			}
			
			if (rightIndex >= end) {
				// Reached end on right side
				work[index] = array[leftIndex];
				leftIndex ++;
				continue;
			}
			
			// Both sides still have more elements.
			double leftVal = array[leftIndex];
			double rightVal = array[rightIndex];
			
			if (leftVal < rightVal) {
				// Left side is smaller
				work[index] = array[leftIndex];
				leftIndex ++;
				continue;
			}
			else {
				// Right side is smaller
				work[index] = array[rightIndex];
				rightIndex ++;
				continue;
			}
		}
		
		// Move data back into array.
		System.arraycopy(work, start, array, start, end - start);
	}
}
