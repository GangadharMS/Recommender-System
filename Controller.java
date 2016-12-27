import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

import org.junit.runners.Parameterized.UseParametersRunnerFactory;

import matrix.Matrix;
import matrix.NoSquareException;
import regression.MultiLinear;

public class Controller {
	public static void main(String[] args){
		System.out.println("Model building in progress...");
		Scanner inputSource = null;
		double[][] userRatingMatrix = new double[80000][2];
		double[][] ratingMatrix = new double[80000][1];
		int i = 0, j = 0, k = 0, l = 0;
		Matrix beta = null;
		try {
			inputSource = new Scanner(new File("train_all_txt.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Specified file didn't found in the directory.");
			System.exit(0);
		}
		while(inputSource.hasNext()){
			j = 0;
			String eachLine = inputSource.nextLine();
			String eachLineContent[]=eachLine.split("\\s");
			if(Integer.parseInt(eachLineContent[0]) == i){
				
			}
			userRatingMatrix[i][j++] = Double.parseDouble(eachLineContent[0]);
			userRatingMatrix[i++][j] = Double.parseDouble(eachLineContent[1]);
			ratingMatrix[k++][l] = Double.parseDouble(eachLineContent[2]);
			
		}
		//System.out.println("completed....i value is "+i +" k value is "+k);
		final Matrix X = new Matrix(userRatingMatrix);
		final Matrix Y = new Matrix(ratingMatrix);
		final MultiLinear ml = new MultiLinear(X, Y);
		try {
			 beta = ml.calculate();
			assertNotNull(beta);
			
		} catch (NoSquareException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Model construction completed.");
		printY(Y, X, beta, true);
	}
	
	private static void printY(final Matrix Y, final Matrix X, final Matrix beta, final boolean bias) {
		System.out.println("Prediction is in progress using the model constructed...");
		PrintWriter outputWriter = null;
		double[][] test = new double[1586126][3];
//		System.out.println(beta.getValueAt(0, 0));
//		System.out.println(beta.getValueAt(1, 0));
//		System.out.println(beta.getValueAt(2, 0));
		int count = 1;
		int user = count;
		int item = count;
		for(int i=0;i<1586126;i++){//1586126
			if(item == 1683){
				user = ++ count;
				item = 1;
			}
				test[i][0] = user;
				test[i][1] = item++;
				//System.out.println(test[i][0]+"->"+test[i][1]);
					
		}
		//System.out.println("item is "+item);
		final Matrix testMatrix = new Matrix(test);
		if (bias) {
			for (int i=0;i<testMatrix.getNrows();i++) {
				double predictedY =  beta.getValueAt(0, 0);
				for (int j=1; j< beta.getNrows(); j++) {
					predictedY += beta.getValueAt(j, 0) * testMatrix.getValueAt(i, j-1);
					
				}
				
				
				//testMatrix.setValueAt(i, 2, Math.round(predictedY));
				
				testMatrix.setValueAt(i, 2, predictedY);
				
				//System.out.println(Y.getValueAt(i, 0) + " -> " + predictedY);
				//outputWriter.println( predictedY);//Y.getValueAt(i, 0) + 
			}
			//System.out.println("check "+testMatrix);
//			for(int temp = 0; temp<testMatrix.getNrows();temp++){
//				System.out.println(testMatrix.getValueAt(temp, 0) +"-->"+ testMatrix.getValueAt(temp, 1)
//				+" = "+testMatrix.getValueAt(temp, 2));
//			}
			
		} 
		
		
		try {
			try {
				outputWriter = new PrintWriter("results.txt", "UTF-8");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(int temp = 0; temp<testMatrix.getNrows();temp++){
			outputWriter.println(Math.round(testMatrix.getValueAt(temp, 0)) +" "+ Math.round(testMatrix.getValueAt(temp, 1))
			+" "+testMatrix.getValueAt(temp, 2));
		}
		outputWriter.close();
		System.out.println("Prediction completed, check results.txt");
	}
}
