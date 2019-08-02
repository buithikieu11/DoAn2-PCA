import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
public class GiaiNen
{   
	public GiaiNen() throws IOException
	{
   int M=243;
   int N=320;
   int Chieu=N*M;
   double Mean[]=new double[Chieu];//Mean
   List <double[]> U=new ArrayList<double[]>();//Ui
	// Make sure you use \\ instead of \
	String filePath = "TapNen\\Mean.txt";
	Scanner scanner = null;
	File file = new File(filePath);
	try {
		//Opening the file
		scanner = new Scanner(file);
		for(int i=0;i<Chieu;i++)
		{Mean[i] = scanner.nextDouble();
		}
	} catch (Exception e) {
		System.out.println("The file " + filePath + "is not found !");
		e.printStackTrace();
	} finally {
		//Closing the file
		scanner.close();
	}
	for(int u=0;u<10;u++)
	{
		String filepath1="TapNen\\U_Image"+String.valueOf(u)+".txt";
		double a[]=new double[Chieu];
		a=DocFile(filepath1,Chieu);
		U.add(a);
	}
	
	int K=10;
	for(int h=0;h<1;h++)
		 {  double [] W=new double[K];//Tinh Vector trong so
			double[] New_Image=new double[Chieu];
			double[] Com=new double[Chieu];
		    String filepath2="TapNen\\W_Image"+String.valueOf(h)+".txt";
		    W=DocFile(filepath2,10);
		     for(int i=0;i<K;i++)
		     {  
		    	 Com=this.Cong_Hai_Vector(Com,this.Nhan_Vector_With_Number(W[i],U.get(i)));
		     
		     }
		  New_Image=this.Cong_Hai_Vector(Mean,Com);
		  for(int i=0;i<New_Image.length;i++)
		    {
		      if(Math.abs(New_Image[i])>255) New_Image[i]=255;
		    }
		  BufferedImage newimg = new BufferedImage(N, M, BufferedImage.TYPE_BYTE_GRAY);
		  int temp = 0;
		  for(int i = 0; i < M; i++)
			  for(int j = 0; j < N; j++)
			  {
				  Color color = new Color((int)(Math.abs(New_Image[temp])), (int)(Math.abs(New_Image[temp])), (int)(Math.abs(New_Image[temp])));
				  newimg.setRGB(j, i, color.getRGB() );
				  temp++;
			  }
		  File file2 = new File("TapGiaiNen\\Person"+String.valueOf(h)+".png");
		  ImageIO.write(newimg, "png", file2);
		 }
		  
	
	}
	
	public double[] DocFile(String filepath,int Chieu)
	{
	    double[] a=new double[Chieu];
	  
	   Scanner scanner1 = null;
       File file1 = new File(filepath);
		try {
			//Opening the file
			scanner1 = new Scanner(file1);
			for(int i=0;i<Chieu;i++)
			{a[i] = scanner1.nextDouble();
			
		    //System.out.print(Mean[i]+" ");
			}
			
		} catch (Exception e) {
			System.out.println("The file is not found !");
			e.printStackTrace();
		} finally {
			//Closing the file
			scanner1.close();
		}
		return a;
	}
	 public double[]Nhan_Vector_With_Number(double a,double A[])
	 {  double C[]=new double[A.length];
		 for(int i=0;i<A.length;i++)
		 {
			 C[i]=a*A[i];
			 
		 }
		 return C;
	 }
	 public double [] Cong_Hai_Vector(double []a,double b[])
	 { double c[]=new double [a.length];
		 for(int i=0;i<a.length;i++)
		 {
			 c[i]=a[i]+b[i];
		 }
		return c;	 
	 }

	 double euclidianNorm(double []v)
	  {
			
			double elemSum = 0;
			for(int i=0;i<v.length;i++)
				elemSum += v[i]*v[i];
			
			return Math.sqrt(elemSum);

	  }
public static void main(String []args) throws IOException
{
	new GiaiNen();
}

	}
	
