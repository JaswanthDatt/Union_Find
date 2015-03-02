import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

public class jm212_Project2_Union_Find {

	public static void main(String args[]) throws IOException {
		if (args.length == 1) {
			// Reading a txt file as test case input
			int lines_column = 0, lines_row = 0;
			String line;
			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(args[0]));
				while ((line = br.readLine()) != null) {
					lines_column++;
					String words[] = line.split(" ");
					if (lines_column == 1) {
						for (int i = 0; i < words.length; i++) {
							lines_row++;
						}
					}

				}

			} catch (Exception ex) {
				// System.err.println("Error reading file...");
			} finally {
				br.close();
				// System.out.println("Successfully read file:" + args[0]);
			}

			// System.out.println(lines_row + "........" + lines_column);

			// Need to construct a path tree 1 -open site 0-closed site

			Scanner sc = new Scanner(new File(args[0]));
			int arry[][] = new int[lines_row][lines_column];

			for (int i = 0; i < lines_row; i++) {
				for (int j = 0; j < lines_column; j++) {
					arry[i][j] = Integer.parseInt(sc.next());
					// System.out.print(arry[i][j] + " ");
				}
				System.out.println("");
			}

			sc.close();

			// now initialize all elements to its roots as same as number
			// initially
			int whole_arry[] = new int[lines_column * lines_row];
			// System.out.println("*********1D array*********");
			// We shall go we 1D bcoz, going going for 2D has N^2 time
			// complexity
			for (int i = 0; i < (lines_row * lines_column); i++) {
				whole_arry[i] = i;
				// System.out.print(whole_arry[i]+" ");
			}
			// System.out.println();

			whole_arry = isConnected(arry, whole_arry);
			// for path compression ...
			for (int i = 0; i < (lines_row * lines_column); i++) {
				if (!(whole_arry[i] == i) && whole_arry[i] < i)
					whole_arry[i] = whole_arry[whole_arry[i]];
			}

			// System.out.println();
			// System.out.println("****************1D array after Union/Find**********");

			for (int i = 0; i < (lines_row * lines_column); i++) {
				// System.out.print(whole_arry[i] + " ");
			}

			// System.out.println();
			int array_2D[][] = new int[lines_row][lines_column];

			// System.out.println("2D marix ");
			// Convert 1D array to 2D array
			for (int j = 0; j < lines_column; j++) {
				for (int i = 0; i < lines_row; i++) {
					array_2D[j][i] = whole_arry[i + j * lines_column];

					// System.out.print(array_2D[j][i] + " ");

				}
				System.out.println();
			}

			// TO FIND THE NUMBER OF CLUSTERS IN A pgm image

			HashMap<Integer, Integer> cluster_count = new HashMap<Integer, Integer>();
			int count = 0;
			for (int j = 0; j < lines_column; j++) {
				for (int i = 0; i < lines_row; i++) {
					cluster_count.put(array_2D[j][i], count);

				}
			}

			for (Entry<Integer, Integer> entry : cluster_count.entrySet()) {
				// System.out.println("Key = " + entry.getKey() + ", Value = " +
				// entry.getValue());
			}
			int cluster_total = cluster_count.size();
			// Number of clusters
			System.out.println("Number of Clusters: " + cluster_total);

			// Now color all the clusters using different on values

			// Write data to file and store in ppm file
			// System.out.println("The .pgm image is located @ C:/Users/JASWANTH/Desktop/TestCases/TestCase3.pgm ");
			PrintWriter pw = new PrintWriter(
					"C:/Users/JASWANTH/Desktop/TestCases/TestCase2_pgm.pgm");
			pw.println("P2");
			pw.print(lines_column + " ");
			pw.println(lines_row);
			pw.println(255);
			for (int j = 0; j < lines_row; j++) {
				for (int i = 0; i < lines_column; i++) {
					pw.print(array_2D[j][i] + " ");

				}
				pw.println("\n");
			}

			pw.close();

			// System.out.println("Color Image .ppm ");

			HashMap<Integer, String> ppm = new HashMap<Integer, String>();

			Random rnd = new Random();

			TreeSet<Integer> keyS = new TreeSet<Integer>();
			keyS = new TreeSet<Integer>(cluster_count.keySet());
			int key = 0;
			for (int i = 0; i < cluster_total; i++) {
				key = keyS.pollFirst();
				int R = rnd.nextInt(255);
				int G = rnd.nextInt(255);
				int B = rnd.nextInt(255);
				String s = R + " " + G + " " + B + " ";
				ppm.put(key, s);
				// System.out.println(key+"   "+s );

			}

			Iterator<Map.Entry<Integer, String>> iterator1 = ppm.entrySet()
					.iterator();

			while (iterator1.hasNext()) {
				Map.Entry<Integer, String> entry = iterator1.next();
				// System.out.println( entry.getKey()+"    :   "+
				// entry.getValue());
			}

			int value_cell = 0;
			String key_k = null;
			// System.out.println("The .ppm image is located @ C:/Users/JASWANTH/Desktop/TestCases/TestCase3_ppm.ppm ");
			PrintWriter pw_ppm = new PrintWriter(
					"C:/Users/JASWANTH/Desktop/TestCases/TestCase2_ppm.ppm");
			pw_ppm.println("P3");
			pw_ppm.print(lines_column + " ");
			pw_ppm.println(lines_row);
			pw_ppm.println(255);
			for (int j = 0; j < (lines_column); j++) {
				for (int i = 0; i < (lines_row); i++) {
					value_cell = array_2D[j][i];
					key_k = ppm.get(value_cell);
					pw_ppm.print(key_k + " ");

				}
				pw_ppm.println("\n");
			}

			pw_ppm.close();

		}

		else if (args.length == 3) {
			double percolation_probability = Double.parseDouble(args[0]);
			int no_of_runs = Integer.parseInt(args[1]);
			int board_size = Integer.parseInt(args[2]);
			int percolation_count = 0;

			for (int k = 0; k < no_of_runs; k++) {

				double array_board[][] = new double[board_size][board_size];
				for (int j = 0; j < board_size; j++) {
					for (int i = 0; i < board_size; i++) {
						Random rnd = new Random();
						double num = (double) rnd.nextInt(1000) / (double) 1001;
						array_board[j][i] = num;
					//	System.out.print(array_board[j][i] + " ");
					}
					System.out.println();
				}

				System.out.println();

			//	System.out.println("Block matrix of maze");

				int blocks_0_1[][] = new int[board_size][board_size];
				for (int j = 0; j < board_size; j++) {
					for (int i = 0; i < board_size; i++) {
						if (array_board[j][i] <= percolation_probability) {
							blocks_0_1[j][i] = 1;
							//System.out.print(blocks_0_1[j][i] + " ");
						} else {
							blocks_0_1[j][i] = 0;
						//	System.out.print(blocks_0_1[j][i] + " ");
						}
					}
					System.out.println();
				}

				int whole_arry[] = new int[board_size * board_size];

				for (int i = 0; i < (board_size * board_size); i++) {
					whole_arry[i] = i;
				//	System.out.print(whole_arry[i] + " ");
				}

				System.out.println();

				whole_arry = isConnected(blocks_0_1, whole_arry);

				// for path compression ...
				for (int i = 0; i < (board_size * board_size); i++) {
					if (!(whole_arry[i] == i) && whole_arry[i] < i)
						whole_arry[i] = whole_arry[whole_arry[i]];
				}

				for (int i = 0; i < (board_size * board_size); i++) {
				//	System.out.print(whole_arry[i] + " ");
				}

				// System.out.println();
				int array_2D[][] = new int[board_size][board_size];

				System.out.println();
				// System.out.println("2D marix ");
				// Convert 1D array to 2D array


				for (int j = 0; j < board_size; j++) {
					for (int i = 0; i < board_size; i++) {
						array_2D[j][i] = whole_arry[i + j * board_size];
					//	System.out.print(array_2D[j][i] + " ");
					}
					System.out.println();
				}
				int count=0;

				if(Percolation_done(array_2D))
				{
					percolation_count++;
				}

			}

			System.out.println("Percolation Rate is:"+(double)percolation_count/(double)no_of_runs);

		} else
			System.out
					.println("Incorrect no of arguments...Check them properly");

	}


	private static boolean Percolation_done(int array_2D[][]) {

		int count=0;
		for (int j = 0; j < array_2D.length; j++) {
			for (int i = 0; i <  array_2D.length; i++) {
				if ((array_2D[0][i] != 0)&& (array_2D[0][i] == array_2D[array_2D.length - 1][j]))
				{
					count++;
				}
			}
		}

			if(count>0)
				return true;
			else
				return false;

		}



	private static int[] isConnected(int arry[][], int whole_arry[]) {
		int arr_size = arry.length;

		for (int i = 0; i < arr_size; i++) {
			for (int j = 0; j < arr_size; j++) {

				if ((i - 1) >= 0) {
					if (arry[i][j] == arry[i - 1][j])
						whole_arry = union(whole_arry[i * arr_size + j],
								whole_arry[(i - 1) * arr_size + j], whole_arry);
				}

				if ((j - 1) >= 0) {
					if (arry[i][j] == arry[i][j - 1])
						whole_arry = union(whole_arry[i * arr_size + j],
								whole_arry[i * arr_size + (j - 1)], whole_arry);
				}

				if ((i + 1) < arr_size) {
					if (arry[i][j] == arry[i + 1][j])
						whole_arry = union(whole_arry[i * arr_size + j],
								whole_arry[(i + 1) * arr_size + j], whole_arry);
				}
				if ((j + 1) < arr_size) {
					if (arry[i][j] == arry[i][j + 1])

						whole_arry = union(whole_arry[i * arr_size + j],
								whole_arry[i * arr_size + (j + 1)], whole_arry);
				}
			}
		}
		return whole_arry;

	}

	static int[] union(int a, int b, int whole_arry[]) {
		int root1 = find(a, whole_arry); // Find root for a
		int root2 = find(b, whole_arry); // Find root for b
		if (root1 > root2) {
			whole_arry[root1] = root2;
			// whole_arry[root2] = root2;

		} else {
			whole_arry[root2] = root1;
			// whole_arry[root1] = root1;
		}
		return whole_arry;
	}

	static int find(int curr, int whole_arry[]) {

		if (whole_arry[curr] != curr) {

			curr = whole_arry[curr];
		}
		return curr;
	}

}
