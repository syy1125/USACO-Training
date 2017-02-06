

import java.io.*;
import java.util.*;


@SuppressWarnings("unchecked")
class Matrix<T>
{
	
	int dim;
	T[][] content;
	
	<T> Matrix()
	{
	}
	
	void setDim(int dimension)
	{
		dim = dimension;
		content = (T[][]) new Object[dim][dim];
	}
	
	<T> Matrix(int dimension)
	{
		dim = dimension;
		setDim(dimension);
	}
	
	void clear()
	{
		content = (T[][]) new Object[dim][dim];
	}
	
	boolean setItem(int x, int y, T item)
	{
		if (x < dim && y < dim)
		{
			content[y][x] = item;
			return true;
		}
		else
		{
			return false;
			
		}
	}
	
	void setContent(T[][] cont)
	{
		if (cont.length != cont[0].length)
		{
			throw new IllegalArgumentException();
		}
		else
		{
			setDim(cont.length);
			for (int x = 0; x < cont.length; x++)
			{
				for (int y = 0; y < cont.length; y++)
				{
					content[y][x] = cont[y][x];
				}
			}
		}
	}
	
	T getItem(int x, int y)
	{
		return content[y][x];
	}
	
	T[] getRow(int y)
	{
		return content[y];
	}
	
	T[] getColumn(int x)
	{
		List<T> temp = new ArrayList<T>();
		for (int y = 0; y < content.length; y++)
		{
			temp.add(content[y][x]);
		}
		T[] out = (T[]) temp.toArray(new Object[dim]);
		return out;
	}
	
	T[][] dumpContent()
	{
		return content;
	}
	
	void printContent()
	{
		T[][] temp = dumpContent();
		
		for (int j = 0; j < dim; j++)
		{
			
			for (int i = 0; i < temp.length; i++)
			{
				System.out.print(temp[j][i]);
			}
			System.out.println();
		}
	}
	
	Matrix<T> rotateCW()
	{
		Matrix<T> out = new Matrix<T>(dim);
		
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				out.setItem(i, j, getItem(j, dim - i - 1));
			}
		}
		
		return out;
	}
	
	Matrix<T> rotateCCW()
	{
		Matrix<T> out = new Matrix<T>(dim);
		
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				out.setItem(i, j, getItem(dim - j - 1, i));
			}
		}
		
		return out;
	}
	
	Matrix<T> rotate180()
	{
		return this.rotateCW().rotateCW();
	}
	
	Matrix<T> reflectH()
	{ // Reflected along vertical line
		Matrix<T> out = new Matrix<T>(dim);
		
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				out.setItem(i, j, getItem(dim - i - 1, j));
			}
		}
		
		return out;
	}
	
	Matrix<T> reflectV()
	{// Reflected along horizontal line
		
		Matrix<T> out = new Matrix<T>(dim);
		
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				out.setItem(i, j, getItem(i, dim - j - 1));
			}
		}
		
		return out;
	}
	
	boolean equivalent(Matrix<T> m)
	{
		boolean out = true;
		
		if (dim != m.dim || getClass() != m.getClass())
		{
			out = false;
		}
		else
		{
			
			for (int i = 0; i < dim; i++)
			{
				for (int j = 0; j < dim; j++)
				{
					if (getItem(i, j) != m.getItem(i, j))
					{
						out = false;
					}
				}
			}
		}
		
		return out;
	}
}

public class transform
{
	static class FileIO
	{
		static String[] readFile(String path)
				throws IOException
		{
			BufferedReader in = new BufferedReader(new FileReader(path));
			List<String> s = new ArrayList<String>();
			
			String buffer;
			while ((buffer = in.readLine()) != null)
			{
				s.add(buffer);
			}
			
			in.close();
			
			String[] out = s.toArray(new String[s.size()]);
			return out;
		}
		
		static void writeFile(String path, String[] content)
				throws IOException
		{
			BufferedWriter out = new BufferedWriter(new FileWriter(path));
			
			for (int i = 0; i < content.length; i++)
			{
				out.write(content[i]);
				out.newLine();
			}
			
			out.close();
		}
	}
	
	public static void main(String[] args)
			throws IOException
	{
		String[] in = FileIO.readFile("transform.in");
		int dim = Integer.parseInt(in[0]);
		
		Matrix<Character> m1 = new Matrix<Character>(dim);
		Matrix<Character> m2 = new Matrix<Character>(dim);
		char c1;
		char c2;
		
		for (int i = 0; i < dim; i++)
		{
			for (int j = 0; j < dim; j++)
			{
				c1 = in[j + 1].charAt(i);
				c2 = in[j + dim + 1].charAt(i);
				m1.setItem(i, j, c1);
				m2.setItem(i, j, c2);
			}
		}
		
		int rotType = 7;
		if (m1.rotateCW().equivalent(m2))
		{
			rotType = 1;
		}
		else if (m1.rotate180().equivalent(m2))
		{
			rotType = 2;
		}
		else if (m1.rotateCCW().equivalent(m2))
		{
			rotType = 3;
		}
		else if (m1.reflectH().equivalent(m2))
		{
			rotType = 4;
		}
		else if (m1.equivalent(m2))
		{
			rotType = 6;
		}
		else
		{ // Checking for combination may be more complex
			Matrix<Character> temp = m1.reflectH();
			if (temp.rotateCW().equivalent(m2)
					|| temp.rotate180().equivalent(m2)
					|| temp.rotateCCW().equivalent(m2))
			{
				rotType = 5;
			}
		}
		
		String[] out = {Integer.toString(rotType)};
		
		FileIO.writeFile("transform.out", out);
		System.exit(0);
	}
}
