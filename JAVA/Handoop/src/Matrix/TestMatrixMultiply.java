package Matrix;
/**	Test program for MatrixMultiply.
 */

import java.io.*;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;


public class TestMatrixMultiply {

	private static final String DATA_DIR_PATH = "/tmp/MatrixMultiply";
	private static final String INPUT_PATH_A = DATA_DIR_PATH + "/A";
	private static final String INPUT_OATH_B = DATA_DIR_PATH + "/B";
	private static final String OUTPUT_DIR_PATH = DATA_DIR_PATH + "/out";
	private static final String TEMP_DIR_PATH = DATA_DIR_PATH;
	
	
	private static Configuration conf = new Configuration();
	private static FileSystem fs;
	
	private static int[][] A;
	private static int[][] B;
	
	private static Random random = new Random();
	
	public static void writeMatrix (int[][] matrix, int rowDim, int colDim, String pathStr)
		throws IOException
	{
		Path path = new Path(pathStr);
		
		
		SequenceFile.Writer writer = SequenceFile.createWriter(conf, SequenceFile.Writer.file(path),
	               SequenceFile.Writer.compression(SequenceFile.CompressionType.NONE),
	               SequenceFile.Writer.keyClass(MatrixMultiply.IndexPair.class), 
	               SequenceFile.Writer.valueClass(IntWritable.class));

	
		
		MatrixMultiply.IndexPair indexPair = new MatrixMultiply.IndexPair();
		IntWritable el = new IntWritable();
		for (int i = 0; i < rowDim; i++) {
			for (int j = 0; j < colDim; j++) {
				int v = matrix[i][j];
				if (v != 0) {
					indexPair.index1 = i;
					indexPair.index2 = j;
					el.set(v);
					writer.append(indexPair, el);
				}
			}
		}
		writer.close();
	}
	
	private static void fillMatrix (int[][] matrix, Path path)
		throws IOException
	{
		//SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
		SequenceFile.Reader reader = new SequenceFile.Reader(conf, SequenceFile.Reader.file(path));
		
		MatrixMultiply.IndexPair indexPair = new MatrixMultiply.IndexPair();
		IntWritable el = new IntWritable();
		while (reader.next(indexPair, el)) {
			matrix[indexPair.index1][indexPair.index2] = el.get();
		}
		reader.close();
	}
	
	public static int[][] readMatrix (int rowDim, int colDim, String pathStr)
		throws IOException
	{
		Path path = new Path(pathStr);
		int[][] result = new int[rowDim][colDim];
		for (int i = 0; i < rowDim; i++)
			for (int j = 0; j < colDim; j++)
				result[i][j] = 0;
		if (fs.isFile(path)) {
			fillMatrix(result, path);
		} else {
			FileStatus[] fileStatusArray = fs.listStatus(path);
			for (FileStatus fileStatus : fileStatusArray) {
				fillMatrix(result, fileStatus.getPath());
			}
		}
		return result;
	}
	
	
	//checkAnswer (int[][] A, int[][] B, int I, int K, int J)
	//btchayik iza 2 matricet ad b3dn 
	//int[][] X = multiply(A, B, I, K, J);
	//int[][] Y = readMatrix(I, J, OUTPUT_DIR_PATH+"/part-r-00000");
	
	
	
	//bt3be matrice kila sfoura
	private static void zero (int[][] matrix, int rowDim, int colDim) {
		for (int i = 0; i < rowDim; i++)
			for (int j= 0; j < colDim; j++)
				matrix[i][j] = 0;
	}
	
	private static void fillRandom (int[][] matrix, int rowDim, int colDim, boolean sparse) {
		if (sparse) {
			zero(matrix, rowDim, colDim);
			for (int n = 0; n < random.nextInt(10); n++)
				matrix[random.nextInt(rowDim)][random.nextInt(colDim)] = random.nextInt(1000);
		} else {
			for (int i = 0; i < rowDim; i++) {
				for (int j = 0; j < colDim; j++) {
					matrix[i][j] = random.nextInt(1000);
				}
			}
		}
	}
	
	public static void runOneTest (int strategy, int R1, int R2, int I, int K, int J,
		int IB, int KB, int JB)
			throws Exception
	{
		if(strategy == 1 || strategy == 4){
		return;}
			
		MatrixMultiply.runJob(conf, INPUT_PATH_A, INPUT_OATH_B, OUTPUT_DIR_PATH, TEMP_DIR_PATH,
			strategy, R1, R2, I, K, J, IB, KB, JB);
		

	}
	

	

	
	private static void testRandom (boolean sparse, boolean big)
		throws Exception
	{
		
		int strategy ;
		do {
			strategy = random.nextInt(4) + 1;
		}while(strategy == 1 || strategy == 4);
		
		int dimMin = big ? 100 : 10;
		int dimRandom = big ? 100 : 10;
		int I = random.nextInt(dimRandom) + dimMin;
		int K = random.nextInt(dimRandom) + dimMin;
		int J = random.nextInt(dimRandom) + dimMin;
		int IB = random.nextInt(I) + 1;
		int KB = random.nextInt(K) + 1;
		int JB = random.nextInt(J) + 1;
		A = new int[I][K];
		B = new int[K][J];
		fillRandom(A, I, K, sparse);
		fillRandom(B, K, J, sparse);
		writeMatrix(A, I, K, INPUT_PATH_A);
		writeMatrix(B, K, J, INPUT_OATH_B);
		System.out.println("   strategy = " + strategy);
		System.out.println("   I = " + I);
		System.out.println("   K = " + K);
		System.out.println("   J = " + J);
		System.out.println("   IB = " + IB);
		System.out.println("   KB = " + KB);
		System.out.println("   JB = " + JB);
		runOneTest(strategy, 1, 1, I, K, J, IB, KB, JB);
	}

	public static void main (String[] args)
		throws Exception
	{
		//new GenericOptionsParser(conf, args);
		fs = FileSystem.get(conf);
		fs.mkdirs(new Path(DATA_DIR_PATH));
		try {
			
			testRandom(false, true);
		
		} finally {
		//	fs.delete(new Path(DATA_DIR_PATH), true);
		}
	}

}