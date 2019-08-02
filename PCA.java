
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import java.io.ByteArrayInputStream;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
public class PCA  {

	public static void main(String[] args) throws IOException {
		new PCA();
	}

	BufferedImage img;
        int a[][];
	public PCA() throws IOException {
		int count=16;
		 int stt=0;
		 int N = 0;
		 int M = 0;
		 //Buoc 1:Xay Dung Cac Vector Khuon Mat Trong Tap Train
		 
		 List<double[]> List_Max=new ArrayList<double[]>();//List_Matrix luu Matrix anh cua tap train
		 while(count!=0)
		 {  
			 try{
				String path="TapDuLieu\\"+"person"+String.valueOf(stt)+".png";
			    img = ImageIO.read(new File(path));
		        int n=img.getWidth();
                int m=img.getHeight();
                N=n;
                M=m;
                double[] max=new double[n*m];
                int dem=0;
		       for (int y = 0; y<img.getHeight();y++)
               {	
                 for (int x = 0; x<img.getWidth();x++)
                	 
                  {
				   int color = img.getRGB(x, y);
			       int b = color & 0xff;
			       max[dem]=(double)b;
				   dem++;
			      }
              
                
               }
		    List_Max.add(max);
		    stt++;
		    count--;
		  }catch(Exception e) {}
			 
	  
	    }
		  int n1=List_Max.get(0).length;
	      //Buoc2:Tính vector khuôn mặt trung bình phi
		  double sum=0;
		  double []phi=new double[n1];
		  for(int x=0;x<n1;x++)
		  {   sum=0;
			  for(int i=0;i<List_Max.size();i++)
				  
			  { sum+=List_Max.get(i)[x];
				  
			  }
			  phi[x]=sum/List_Max.size();
		  }
		    
		
		 //Buoc3:Tinh cac Vector phi[i]=A[i](vector anh khuon mat thu i)-phi(vector khuon mat trung binh)
		  List<double[]> List_Phi_i=new ArrayList<double[]>();//Khoi Tao Vector cac phi_i ban dau deu=0; 
		  { for(int j=0;j<List_Max.size();j++)
		    {  double[] m=new double[n1];
			  for(int i=0;i<n1;i++)
		      { m[i]=0;
			  
		      }
			 List_Phi_i.add(m);  
		    }  
		  
		  }
		  for(int i=0;i<List_Max.size();i++)
		  {
			  for(int j=0;j<n1;j++)
			  {
				  List_Phi_i.get(i)[j]=List_Max.get(i)[j]-phi[j];
			  }
		  }
		 
			
		  //Check
		 //double b=List_Max.get(0).A[0]-phi.A[0];
		 //System.out.print(b-List_Phi_i.get(0).A[0]);
		  
		  //Buoc4:Tim Ma Tran Hiep Phuong Sai C=1/M*A*AT M:So img trong tap Train 
		  double A[][]=new double [n1][List_Max.size()];//Init Ma Tran A
		   for(int i=0;i<n1;i++)
		   {
			   for(int j=0;j<List_Max.size();j++)
			   {
				   A[i][j]=List_Phi_i.get(j)[i];
			   }
		   }
		  
		  double AT[][]=new double [List_Max.size()][n1];//Ma Tran Chuyen Vi Cua A
		  for(int i=0;i<List_Max.size();i++)
			  {for(int j=0;j<n1;j++)
			    {  AT[i][j]=A[j][i];
				  
			    
			    }
			  }
		
		  
		  double C[][]=new double [List_Max.size()][List_Max.size()];//Ma Tran Hiep Phuong Sai
		  C=this.Nhan_matrix(AT,A);
		
		  //Buoc5:Tim Cac Vector Rieng Ung Voi Cac Tri Rieng Lon Nhat		 
		  
		   double []eValues=new double[C.length];
		   double [][]eVectors=new double[C.length][C.length];
		    Array2DRowRealMatrix B = new Array2DRowRealMatrix(C);
			EigenDecomposition eigenvector = new EigenDecomposition(B);
			eValues = eigenvector.getRealEigenvalues();
			
			RealMatrix ABC = new Array2DRowRealMatrix();
			ABC = eigenvector.getV();
			eVectors = ABC.getData();
		
		 //Sort eValues and eVectors in the same time
		 for(int i = 0; i < eValues.length; i++){
				for (int j =i+1; j <eValues.length; j++){
					if (eValues[j] >eValues[i])
					{   double eigenValueTemp = eValues[i];
						eValues[i]= eValues[j];
						eValues[j]= eigenValueTemp;
					
						//swap values in the eigenvector matrix iff j's eigenvalues are smaller than i's.
						for (int k = 0; k < eVectors[0].length; k++){
							double eigenVectorTemp = eVectors[i][k];
							eVectors[i][ k]= eVectors[j][k];
							eVectors[j][k]= eigenVectorTemp;
						}
					}
				}
			}
		   int K=0;
		   double Sum=0;
		   double Sum_All=0;
		   for(int i=0;i<eValues.length;i++)
			   Sum_All+=eValues[i];
			 
		   while(Sum/Sum_All<=0.97)//Chon K Thanh Phan Chinh //0.97
		  {   Sum+=eValues[K];
			  K++;
		  }
		  
		   System.out.print(K);
	      List <double[]> ui=new ArrayList<double[]>();//He Co So Trong Khong Gian Moi
		  for(int i=0;i<K;i++)
		  { double[] m=new double[A.length];
		    m=this.Nhan_Matrix_With_Vector(A,this.Get_Cot(eVectors,i));
			ui.add(m);  
		  }
		 
		 
		  for(int i=0;i<ui.size();i++)
		    { double S=this.euclidianNorm(ui.get(i));
		      for(int j=0;j<ui.get(0).length;j++)
			  { ui.get(i)[j]= ui.get(i)[j]/S;	
			   
			  }
		    }
		 
		  //Buoc 6:Chieu Xuong Khong Gian Moi 
		 //uiT*phii;
		for(int h=0;h<16;h++)
		// int h=2;
		 {	 
			double [] W=new double[K];//Tinh Vector trong so
			double[] New_Image=new double[n1];
			double[] Com=new double[n1];
			
			 
		     for(int i=0;i<K;i++)
		     {  
			    W[i]=this.Nhan_vector(ui.get(i),List_Phi_i.get(h));
			    
		     }
		     
			  try (BufferedWriter bw = new BufferedWriter(new FileWriter("TapNen\\W_Image"+String.valueOf(h)+".txt"))) {
				  
				   for(int i=0;i<W.length;i++)
				   {   
					bw.write(String.valueOf(W[i])+" ");
				   }
					// no need to close it.
					//bw.close();
		 
					System.out.println("Viet file xong!");
		 
				} catch (IOException e) {
		 
					e.printStackTrace();
		 
				}
		     for(int i=0;i<K;i++)
		     {  
		    	Com=this.Cong_Hai_Vector(Com,this.Nhan_Vector_With_Number(W[i],ui.get(i)));
		     
		     }
		     
			
		     
		//New_Image=Com;
		 New_Image=this.Cong_Hai_Vector(phi,Com);
		  for(int i=0;i<New_Image.length;i++)
		    {// System.out.print(New_Image.A[i]+" ");
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
		  File file = new File("TapGiaiNen\\Person"+String.valueOf(h)+".png");
		  ImageIO.write(newimg, "png", file);
		 }
		  
		  
		  try (BufferedWriter bw = new BufferedWriter(new FileWriter("TapNen\\Mean.txt"))) {
			  
			   for(int i=0;i<phi.length;i++)
			   {   
				bw.write(String.valueOf(phi[i])+" ");
			   }
				System.out.println("Viet file xong!");
	 
			} catch (IOException e) {
	 
				e.printStackTrace();
	 
			}
		  for(int i=0;i<ui.size();i++)
		    { 
			  double S=this.euclidianNorm(ui.get(i));
			  try (BufferedWriter bw = new BufferedWriter(new FileWriter("TapNen\\U_Image"+String.valueOf(i)+".txt"))) {
				  
		      for(int j=0;j<ui.get(0).length;j++)
			  { ui.get(i)[j]= ui.get(i)[j]/S;	
			   bw.write(String.valueOf(ui.get(i)[j])+" ");
			   
			  }
			  }
		    
		  catch (IOException e) {
		   	 
				e.printStackTrace();
	 
			
		    }
		    }  
		 
		
 
		  
	
	}
	 
 double euclidianNorm(double []v)
	  {    double elemSum = 0;
			for(int i=0;i<v.length;i++)
				elemSum += v[i]*v[i];
			
			return Math.sqrt(elemSum);

	  }
	
	
	 public double [][] Nhan_matrix(double a[][],double b[][])
	    {
	    	double c[][]=new double [a.length][b[0].length];
	    	for(int i=0;i<a.length;i++)
	    	{
	    		for(int j=0;j<b[0].length;j++)
	    			{c[i][j]=0;
	    			
	    		     for(int k=0;k<a[0].length;k++)
	    		     {
	    		    	 c[i][j]+=a[i][k]*b[k][j];
	    		     }
	    			}
	    	}
	    	return c;
	    }
		
	 public double [] Get_Cot(double[][] W,int z)
	    { //System.out.println("Length la"+W.length);
	      double [] k=new double[W.length];
	       for( int i=0;i<W.length;i++)
	    		   k[i]=W[i][z];
	    	return k;
	    }
	 public double [] Get_Hang(double[][] W,int z)
	    { //System.out.println("Length la"+W.length);
	      double [] k=new double[W.length];
	       for( int i=0;i<W[0].length;i++)
	    		   k[i]=W[z][i];
	    	return k;
	    }
	
	 public  double Nhan_vector(double a[],double b[])
	    {  double c=0;
	    	for(int i=0;i<a.length;i++)
	    		c+=a[i]*b[i];
	    	return c;
	    }
	 public double[]Nhan_Vector_With_Number(double a,double A[])
	 {  double C[]=new double [A.length];
		 for(int i=0;i<A.length;i++)
		 {
			 C[i]=a*A[i];
			 
		 }
		 return C;
	 }
	 public double [] Tru_Hai_Vector(double []a,double b[])
	 { double c[]=new double [a.length];
		 for(int i=0;i<a.length;i++)
		 {
			 c[i]=a[i]-b[i];
		 }
		return c;	 
	 }
	
	 public double [] Cong_Hai_Vector(double []a,double b[])
	 { double c[]=new double [a.length];
		 for(int i=0;i<a.length;i++)
		 {
			 c[i]=a[i]+b[i];
		 }
		return c;	 
	 }
	 
	  double [] Nhan_Matrix_With_Vector(double A[][],double B[])
	  { double c[]=new double [A.length];
    	 for(int i=0;i<A.length;i++)
  	     {    c[i]=0;
  		      for(int k=0;k<A[0].length;k++)
  		      {
  		    	 c[i]+=A[i][k]*B[k];
  		    	 
  		      }
  		    
  			
  	     }
    	return c;
	  }
	 
	
	  
}

    
    

