public class SortTester
{
	public static void main(String[] args)
	{
		int[] array = { 3, 56, 12, 43, 44, 100, 42, 6, 88, 33, 54, 71, 26 };
		ISorter sorter = new IntMergeSorter(array);
		sorter.sort();
		for (int i = 0; i < array.length; i ++)
		{
			System.out.print(array[i] + " ");
		}
	}
}
