package ie.gmit.dip;


public enum Kernel {

	IDENTITY(new double[][] { { 0, 0, 0 }, { 0, 1, 0 }, { 0, 0, 0 } }),

	EDGE_DETECTION_1(new double[][]  {
			{-1, -1, -1},
			{-1, 8, -1},
			{-1, -1, -1}
	}),
	
	
	EDGE_DETECTION_2(new double[][] {
			{1, 0, -1},
			{0, 0, 0},
			{-1, 0, 1}
	}),
	
	
	LAPLACIAN(new double[][] {
			{0, -1, 0},
			{-1, 4, -1},
			{0, -1, 0}
	}),


	 SHARPEN(new double[][]{
			{0, -1, 0},
			{-1, 5, -1},
			{0, -1, 0}
	 }),


	 VERTICAL_LINES (new double[][] {
			{-1, 2, -1},
			{-1, 2, -1},
			{-1, 2, -1}
	 }),
	
	
	 HORIZONTAL_LINES(new double[][] {
			{-1, -1, -1},
			{2, 2, 2},
			{-1, -1, -1}
	 }),
	
	
	 DIAGONAL_45_LINES(new double[][] {
			{-1, -1, 2},
			{-1, 2, -1},
			{2, -1, -1}
	 }),

	
	 BOX_BLUR(new double[][] {
			{0.111, 0.111, 0.111},
			{0.111, 0.111, 0.111},
			{0.111, 0.111, 0.111}
	 }),
	
	 SOBEL_HORIZONTAL(new double[][] {
			{-1, -2, -1},
			{0, 0, 0},
			{1, 2, 1}
	}),
	
	
	 SOBEL_VERTICAL(new double[][] {
			{-1, 0, 1},
			{-2, 0, 2},
			{-1, 0, 1}
	}),
	 
	 CUSTOM_1(new double[][] {
		 	{-2, -2, -2, -2, -2},
			{-2, -1, -1, -1, -2},
			{0, 0.5, 1, 0.5, -0},
			{2, 1, 1, 1, 2},
			{2, 2, 2, 2, 2}
			}
	 );

	private double[][] kernels;
	
	//Kernel(double[][] k) {
	//	this.setKernels(k);
	//}
	
	Kernel(double[][] kernels) {
		this.kernels = kernels;
	}

	public double[][] getKernels() {
		return kernels;
	}

	public void setKernels(double[][] kernels) {
		this.kernels = kernels;
	}

}

/*
A convolution kernel is a 2D array of numbers that can be used to apply
a filtering effect to an image. Each of the kernels implemented below are
declared as "public static final double[][]":
	 
	 1) public: they can be accessed from outside their class and package.
	 2) static: there is only one instance of this array.
	 3) final: the array is a constant.
	 4) double: the array type is set to double instead of int to make computation easier.

Because the arrays are all static, they belong to the this class Kernel and not to an 
instance of this class. You should therefore refer to each array using the 
ClassName.featureName notation, e.g. double[][] kernel = Kernel.SOBEL_HORIZONTAL; You can
alse refer directly to the indices as follows:
	  for (int row = 0; row < Kernel.SOBEL_HORIZONTAL.length; row++){
	      for (int col = 0; col < Kernel.SOBEL_HORIZONTAL[row].length; col++){
	         ....
		 }
	  }
	
There are lots of other convolution kernels for applying different effects. You should 
feel free to add / delete from here as you see fit. In addition, you should consider
converting the type Kernel from a class into an enum. An enum is more suitable, as it
acts as a container for a suite of constants. Have a look at the enum ConsoleColour 
to see how easy this is to do.	

	private double[][] ds;
	private String name;
	
	private Kernel(double[][] ds, String name) {
	this.ds=ds;
	this.name=name;
	}
	

	public void setDs(double[][] ds) {
		this.ds = ds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void createKernel (double[][] d, String newKernelName) {
		 Menu.kernelSet.);
	 }


*/
