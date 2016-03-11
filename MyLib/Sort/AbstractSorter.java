public abstract class AbstractSorter<Type> implements ISorter
{
	protected Type[] array;
	
	public AbstractSorter(Type[] data)
	{
		array = data;
	}
}
