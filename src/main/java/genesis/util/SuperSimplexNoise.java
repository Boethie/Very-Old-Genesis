package genesis.util;

/**
 * @author Kurt Spencer (KdotJPG)
 * 
 * Generates 2D and 3D SuperSimplex noise.
 * 
 * SuperSimplex noise is gradient noise using the lattice of Simplex noise,
 * but with larger kernels that extend all the way to the next-nearest
 * lattice point.
 * 
 * In this case the 3D function is implemented in a manner that uses a
 * fundamentally different lattice placement scheme so that it does not
 * use the 3D Simplex noise skew equation or any of its equivalents, all
 * functions are implemented using a lookup scheme inspired by
 * DigitalShadow's optimized OpenSimplex implementation, and a permutation
 * table of size 1024 is used as opposed to the traditional 256.
 * 
 * Each gradient set is defined as the directions from the center to the
 * vertices of the normalized expanded vertex figure of the lattice for
 * the given dimensionality. These gradient sets are symmetric with the
 * lattice, but don't include directions that follow edges or facets,
 * avoiding much of the constructive interference that often results.
 * 
 * Supports multi-evaluation with first derivatives.
 * 
 * Version 12/28/2015
 */

public class SuperSimplexNoise {

	private short[] perm;
	private short[] perm2D;
	private short[] perm3D;

	public SuperSimplexNoise(long seed) {
		perm = new short[1024];
		perm2D = new short[1024];
		perm3D = new short[1024];
		short[] source = new short[1024]; 
		for (short i = 0; i < 1024; i++)
			source[i] = i;
		for (int i = 1023; i >= 0; i--) {
			seed = seed * 6364136223846793005L + 1442695040888963407L;
			int r = (int)((seed + 31) % (i + 1));
			if (r < 0)
				r += (i + 1);
			perm[i] = source[r];
			perm2D[i] = (short)((perm[i] % 12) * 2);
			perm3D[i] = (short)((perm[i] % 48) * 3);
			source[r] = source[i];
		}
	}
	
	/*
	 * 2D and 3D standard point-evaluation functions
	 */
	
	//2D SuperSimplex noise
	public double eval(double x, double y) {
		double value = 0;
		
		//Get points for A2* lattice
		double s = 0.366025403784439 * (x + y);
		double xs = x + s, ys = y + s;
		
		//Get base points and offsets
		int xsb = fastFloor(xs), ysb = fastFloor(ys);
		double xsi = xs - xsb, ysi = ys - ysb;
		
		//Index to point list
		int a = (int)(xsi + ysi);
		int index =
			(a << 2) |
			(int)(xsi - ysi / 2 + 1 - a / 2.0) << 3 |
			(int)(ysi - xsi / 2 + 1 - a / 2.0) << 4;
		
		double ssi = (xsi + ysi) * -0.211324865405187;
		double xi = xsi + ssi, yi = ysi + ssi;

		//Point contributions
		for (int i = 0; i < 4; i++) {
			LatticePoint2D c = LOOKUP_2D[index + i];

			double dx = xi + c.dx, dy = yi + c.dy;
			double attn = 2.0 / 3.0 - dx * dx - dy * dy;
			if (attn <= 0) continue;

			int pxm = (xsb + c.xsv) & 1023, pym = (ysb + c.ysv) & 1023;
			int gi = perm2D[perm[pxm] ^ pym];
			double extrapolation = GRADIENTS_2D[gi] * dx
					+ GRADIENTS_2D[gi + 1] * dy;
			
			attn *= attn;
			value += attn * attn * extrapolation;
		}
		
		return value;
	}
	
	//3D SuperSimplex noise (implemented using overlapping rotated cubic lattices)
	public double eval(double x, double y, double z) {
		double value = 0;
		
		//Get points for two overlapping rotated cubic lattices.
		double r = (2.0 / 3.0) * (x + y + z);
		double xr = r - x, yr = r - y, zr = r - z;
		double xr2 = xr + 512.5, yr2 = yr + 512.5, zr2 = zr + 512.5;
		
		//Get base points and offsets inside cubes
		int xrb = fastFloor(xr), yrb = fastFloor(yr), zrb = fastFloor(zr);
		int xr2b = fastFloor(xr2), yr2b = fastFloor(yr2), zr2b = fastFloor(zr2);
		double xri = xr - xrb, yri = yr - yrb, zri = zr - zrb;
		double xr2i = xr2 - xr2b, yr2i = yr2 - yr2b, zr2i = zr2 - zr2b;

		//Index to point list for first lattice
		int index1 =
				(+ xri + yri + zri >= 1.5 ? 1*4 : 0) |
				(- xri + yri + zri >= 0.5 ? 2*4 : 0) |
				(+ xri - yri + zri >= 0.5 ? 4*4 : 0) |
				(+ xri + yri - zri >= 0.5 ? 8*4 : 0);

		//Index to point list for second lattice
		int index2 =
				(+ xr2i + yr2i + zr2i >= 1.5 ? 1*4 : 0) |
				(- xr2i + yr2i + zr2i >= 0.5 ? 2*4 : 0) |
				(+ xr2i - yr2i + zr2i >= 0.5 ? 4*4 : 0) |
				(+ xr2i + yr2i - zr2i >= 0.5 ? 8*4 : 0);
		
		//Point contributions for first lattice
		for (int i = 0; i < 4; i++) {
			LatticePoint3D c = LOOKUP_3D[index1 + i];

			double dxr = xri - c.xrv, dyr = yri - c.yrv, dzr = zri - c.zrv;
			double attn = 0.75 - dxr * dxr - dyr * dyr - dzr * dzr;
			if (attn <= 0) continue;
			
			int pxm = (xrb + c.xrv) & 1023, pym = (yrb + c.yrv) & 1023, pzm = (zrb + c.zrv) & 1023;
			int gi = perm3D[perm[perm[pxm] ^ pym] ^ pzm];
			double extrapolation = GRADIENTS_3D_R[gi] * dxr
					+ GRADIENTS_3D_R[gi + 1] * dyr
					+ GRADIENTS_3D_R[gi + 2] * dzr;
			
			attn *= attn;
			value += attn * attn * extrapolation;
		}

		//Point contributions for second lattice
		for (int i = 0; i < 4; i++) {
			LatticePoint3D c = LOOKUP_3D[index2 + i];

			double dxr = xr2i - c.xrv, dyr = yr2i - c.yrv, dzr = zr2i - c.zrv;
			
			double attn = 0.75 - dxr * dxr - dyr * dyr - dzr * dzr;
			if (attn <= 0) continue;

			int pxm = (xr2b + c.xrv) & 1023, pym = (yr2b + c.yrv) & 1023, pzm = (zr2b + c.zrv) & 1023;
			int gi = perm3D[perm[perm[pym] ^ pzm] ^ pxm];
			double extrapolation = GRADIENTS_3D_R[gi] * dxr
					+ GRADIENTS_3D_R[gi + 1] * dyr
					+ GRADIENTS_3D_R[gi + 2] * dzr;
			
			attn *= attn;
			value += attn * attn * extrapolation;
		}
		
		return value;
	}
	
	/*
	 * 2D and 3D multi-instance evaluation functions
	 */
	
	//2D SuperSimplex noise (Multi-eval)
	public static void eval(double x, double y, NoiseInstance2[] instances, double[] destination) {
		
		//Get points for A2* lattice
		double s = 0.366025403784439 * (x + y);
		double xs = x + s, ys = y + s;
		
		//Get base points and offsets
		int xsb = fastFloor(xs), ysb = fastFloor(ys);
		double xsi = xs - xsb, ysi = ys - ysb;
		
		//Index to point list
		int a = (int)(xsi + ysi);
		int index =
			(a << 2) |
			(int)(xsi - ysi / 2 + 1 - a / 2.0) << 3 |
			(int)(ysi - xsi / 2 + 1 - a / 2.0) << 4;
		
		double ssi = (xsi + ysi) * -0.211324865405187;
		double xi = xsi + ssi, yi = ysi + ssi;

		//Point contributions
		for (int i = 0; i < 4; i++) {
			LatticePoint2D c = LOOKUP_2D[index + i];

			double dx = xi + c.dx, dy = yi + c.dy;
			double attn = 2.0 / 3.0 - dx * dx - dy * dy;
			if (attn <= 0) continue;

			int pxm = (xsb + c.xsv) & 1023, pym = (ysb + c.ysv) & 1023;
			for (NoiseInstance2 instance: instances) {
				int gi = instance.noise.perm2D[instance.noise.perm[pxm] ^ pym];
				double gx = GRADIENTS_2D[gi + 0], gy = GRADIENTS_2D[gi + 1];
				double extrapolation = gx * dx + gy * dy;
				double attnSq = attn * attn;
				
				if (instance.valueIndex >= 0) {
					destination[instance.valueIndex] += attnSq * attnSq * extrapolation;
				}
				if (instance.ddxIndex >= 0) {
					destination[instance.ddxIndex] += (gx * attn - 8 * dx * extrapolation) * attnSq * attn;
				}
				if (instance.ddyIndex >= 0) {
					destination[instance.ddyIndex] += (gy * attn - 8 * dy * extrapolation) * attnSq * attn;
				}
			}
		}
	}
	
	//3D SuperSimplex noise (Multi-eval / implemented using overlapping rotated cubic lattices)
	public static void eval(double x, double y, double z, NoiseInstance3[] instances, double[] destination) {
		
		//Get points for two overlapping reflected cubic lattices.
		double r = (2.0 / 3.0) * (x + y + z);
		double xr = r - x, yr = r - y, zr = r - z;
		double xr2 = xr + 512.5, yr2 = yr + 512.5, zr2 = zr + 512.5;
		
		//Get base points and offsets inside cubes
		int xrb = fastFloor(xr), yrb = fastFloor(yr), zrb = fastFloor(zr);
		int xr2b = fastFloor(xr2), yr2b = fastFloor(yr2), zr2b = fastFloor(zr2);
		double xri = xr - xrb, yri = yr - yrb, zri = zr - zrb;
		double xr2i = xr2 - xr2b, yr2i = yr2 - yr2b, zr2i = zr2 - zr2b;

		//Index to point list for first lattice
		int index1 =
				(+ xri + yri + zri >= 1.5 ? 1*4 : 0) |
				(- xri + yri + zri >= 0.5 ? 2*4 : 0) |
				(+ xri - yri + zri >= 0.5 ? 4*4 : 0) |
				(+ xri + yri - zri >= 0.5 ? 8*4 : 0);

		//Index to point list for second lattice
		int index2 =
				(+ xr2i + yr2i + zr2i >= 1.5 ? 1*4 : 0) |
				(- xr2i + yr2i + zr2i >= 0.5 ? 2*4 : 0) |
				(+ xr2i - yr2i + zr2i >= 0.5 ? 4*4 : 0) |
				(+ xr2i + yr2i - zr2i >= 0.5 ? 8*4 : 0);
		
		//Point contributions for first lattice
		for (int i = 0; i < 4; i++) {
			LatticePoint3D c = LOOKUP_3D[index1 + i];

			double dxr = xri - c.xrv, dyr = yri - c.yrv, dzr = zri - c.zrv;
			double attn = 0.75 - dxr * dxr - dyr * dyr - dzr * dzr;
			if (attn <= 0) continue;
			
			double drr = (2.0 / 3.0) * (dxr + dyr + dzr);
			double dx = drr - dxr, dy = drr - dyr, dz = drr - dzr;

			int pxm = (xrb + c.xrv) & 1023, pym = (yrb + c.yrv) & 1023, pzm = (zrb + c.zrv) & 1023;
			for (NoiseInstance3 instance : instances) {
				int gi = instance.noise.perm3D[instance.noise.perm[instance.noise.perm[pxm] ^ pym] ^ pzm];
				double gxr = GRADIENTS_3D_R[gi + 0], gyr = GRADIENTS_3D_R[gi + 1], gzr = GRADIENTS_3D_R[gi + 2];
				double gx = GRADIENTS_3D[gi + 0], gy = GRADIENTS_3D[gi + 1], gz = GRADIENTS_3D[gi + 2];
				double extrapolation = gxr * dxr + gyr * dyr + gzr * dzr;
				double attnSq = attn * attn;

				if (instance.valueIndex >= 0) {
					destination[instance.valueIndex] += attnSq * attnSq * extrapolation;
				}
				if (instance.ddxIndex >= 0) {
					destination[instance.ddxIndex] += (gx * attn - 8 * dx * extrapolation) * attnSq * attn;
				}
				if (instance.ddyIndex >= 0) {
					destination[instance.ddyIndex] += (gy * attn - 8 * dy * extrapolation) * attnSq * attn;
				}
				if (instance.ddzIndex >= 0) {
					destination[instance.ddzIndex] += (gz * attn - 8 * dz * extrapolation) * attnSq * attn;
				}
			}
		}
		
		//Point contributions for second lattice
		for (int i = 0; i < 4; i++) {
			LatticePoint3D c = LOOKUP_3D[index2 + i];

			double dxr = xr2i - c.xrv, dyr = yr2i - c.yrv, dzr = zr2i - c.zrv;
			double attn = 0.75 - dxr * dxr - dyr * dyr - dzr * dzr;
			if (attn <= 0) continue;
			
			double drr = (2.0 / 3.0) * (dxr + dyr + dzr);
			double dx = drr - dxr, dy = drr - dyr, dz = drr - dzr;

			int pxm = (xr2b + c.xrv) & 1023, pym = (yr2b + c.yrv) & 1023, pzm = (zr2b + c.zrv) & 1023;
			for (NoiseInstance3 instance : instances) {
				int gi = instance.noise.perm3D[instance.noise.perm[instance.noise.perm[pxm] ^ pym] ^ pzm];
				double gxr = GRADIENTS_3D_R[gi + 0], gyr = GRADIENTS_3D_R[gi + 1], gzr = GRADIENTS_3D_R[gi + 2];
				double gx = GRADIENTS_3D[gi + 0], gy = GRADIENTS_3D[gi + 1], gz = GRADIENTS_3D[gi + 2];
				double extrapolation = gxr * dxr + gyr * dyr + gzr * dzr;
				double attnSq = attn * attn;

				if (instance.valueIndex >= 0) {
					destination[instance.valueIndex] += attnSq * attnSq * extrapolation;
				}
				if (instance.ddxIndex >= 0) {
					destination[instance.ddxIndex] += (gx * attn - 8 * dx * extrapolation) * attnSq * attn;
				}
				if (instance.ddyIndex >= 0) {
					destination[instance.ddyIndex] += (gy * attn - 8 * dy * extrapolation) * attnSq * attn;
				}
				if (instance.ddzIndex >= 0) {
					destination[instance.ddzIndex] += (gz * attn - 8 * dz * extrapolation) * attnSq * attn;
				}
			}
		}
	}
	
	/*
	 * Utility
	 */
	
	private static int fastFloor(double x) {
		int xi = (int)x;
		return x < xi ? xi - 1 : xi;
	}
	
	/*
	 * Definitions
	 */

	private static final LatticePoint2D[] LOOKUP_2D;
	private static final LatticePoint3D[] LOOKUP_3D;
	static {
		LOOKUP_2D = new LatticePoint2D[8 * 4];
		LOOKUP_3D = new LatticePoint3D[16 * 4];
		
		for (int i = 0; i < 8; i++) {
			int i1, j1, i2, j2;
			if ((i & 1) == 0) {
				if ((i & 2) == 0) { i1 = -1; j1 = 0; } else { i1 = 1; j1 = 0; }
				if ((i & 4) == 0) { i2 = 0; j2 = -1; } else { i2 = 0; j2 = 1; }
			} else {
				if ((i & 2) != 0) { i1 = 2; j1 = 1; } else { i1 = 0; j1 = 1; }
				if ((i & 4) != 0) { i2 = 1; j2 = 2; } else { i2 = 1; j2 = 0; }
			}
			LOOKUP_2D[i * 4 + 0] = new LatticePoint2D(0, 0);
			LOOKUP_2D[i * 4 + 1] = new LatticePoint2D(1, 1);
			LOOKUP_2D[i * 4 + 2] = new LatticePoint2D(i1, j1);
			LOOKUP_2D[i * 4 + 3] = new LatticePoint2D(i2, j2);
		}
		
		for (int i = 0; i < 16; i++) {
			int i1, j1, k1, i2, j2, k2, i3, j3, k3, i4, j4, k4;
			if ((i & 1) != 0) { i1 = j1 = k1 = 1; } else { i1 = j1 = k1 = 0; }
			if ((i & 2) != 0) { i2 = 0; j2 = k2 = 1; } else { i2 = 1; j2 = k2 = 0; }
			if ((i & 4) != 0) { j3 = 0; i3 = k3 = 1; } else { j3 = 1; i3 = k3 = 0; }
			if ((i & 8) != 0) { k4 = 0; i4 = j4 = 1; } else { k4 = 1; i4 = j4 = 0; }
			LOOKUP_3D[i * 4 + 0] = new LatticePoint3D(i1, j1, k1);
			LOOKUP_3D[i * 4 + 1] = new LatticePoint3D(i2, j2, k2);
			LOOKUP_3D[i * 4 + 2] = new LatticePoint3D(i3, j3, k3);
			LOOKUP_3D[i * 4 + 3] = new LatticePoint3D(i4, j4, k4);
		}
	}
	
	private static class LatticePoint2D {
		public int xsv, ysv;
		public double dx, dy;
		public LatticePoint2D(int xsv, int ysv) {
			this.xsv = xsv; this.ysv = ysv;
			double ssv = (xsv + ysv) * -0.211324865405187;
			this.dx = -xsv - ssv;
			this.dy = -ysv - ssv;
		}
	}
	
	private static class LatticePoint3D {
		public int xrv, yrv, zrv;
		public LatticePoint3D(int xrv, int yrv, int zrv) {
			this.xrv = xrv; this.yrv = yrv; this.zrv = zrv;
		}
	}
	
	//2D Gradients: Dodecagon
	private static final double[] GRADIENTS_2D = new double[] {
		                  0,  18.518518518518519,
		  9.259259259259260,  16.037507477489605,
		 16.037507477489605,   9.259259259259260,
		 18.518518518518519,                   0,
		 16.037507477489605,  -9.259259259259260,
		  9.259259259259260, -16.037507477489605,
		                  0, -18.518518518518519,
		 -9.259259259259260, -16.037507477489605,
		-16.037507477489605,  -9.259259259259260,
		-18.518518518518519,                   0,
		-16.037507477489605,   9.259259259259260,
		 -9.259259259259260,  16.037507477489605,
		                  0,  18.518518518518519
	};
	
	//3D Gradients: Normalized expanded Cuboctahedron / Rhombic Dodecahedron.
	private static final double[] GRADIENTS_3D_R = new double[] {
		 10.998199092175922,   4.177070890843320,                   0,
		  4.177070890843320,  10.998199092175922,                   0,
		  7.928086337400578,   7.928086337400578,   3.563593488561014,
		  7.928086337400578,   7.928086337400578,  -3.563593488561014,
		 10.998199092175922,                   0,   4.177070890843320,
		  4.177070890843320,                   0,  10.998199092175922,
		  7.928086337400578,   3.563593488561014,   7.928086337400578,
		  7.928086337400578,  -3.563593488561014,   7.928086337400578,
		                  0,  10.998199092175922,   4.177070890843320,
		                  0,   4.177070890843320,  10.998199092175922,
		  3.563593488561014,   7.928086337400578,   7.928086337400578,
		 -3.563593488561014,   7.928086337400578,   7.928086337400578,
		 10.998199092175922,  -4.177070890843320,                   0,
		  4.177070890843320, -10.998199092175922,                   0,
		  7.928086337400578,  -7.928086337400578,   3.563593488561014,
		  7.928086337400578,  -7.928086337400578,  -3.563593488561014,
		 10.998199092175922,                   0,  -4.177070890843320,
		  4.177070890843320,                   0, -10.998199092175922,
		  7.928086337400578,   3.563593488561014,  -7.928086337400578,
		  7.928086337400578,  -3.563593488561014,  -7.928086337400578,
		                  0,  10.998199092175922,  -4.177070890843320,
		                  0,   4.177070890843320, -10.998199092175922,
		  3.563593488561014,   7.928086337400578,  -7.928086337400578,
		 -3.563593488561014,   7.928086337400578,  -7.928086337400578,
		-10.998199092175922,   4.177070890843320,                   0,
		 -4.177070890843320,  10.998199092175922,                   0,
		 -7.928086337400578,   7.928086337400578,   3.563593488561014,
		 -7.928086337400578,   7.928086337400578,  -3.563593488561014,
		-10.998199092175922,                   0,   4.177070890843320,
		 -4.177070890843320,                   0,  10.998199092175922,
		 -7.928086337400578,   3.563593488561014,   7.928086337400578,
		 -7.928086337400578,  -3.563593488561014,   7.928086337400578,
		                  0, -10.998199092175922,   4.177070890843320,
		                  0,  -4.177070890843320,  10.998199092175922,
		  3.563593488561014,  -7.928086337400578,   7.928086337400578,
		 -3.563593488561014,  -7.928086337400578,   7.928086337400578,
		-10.998199092175922,  -4.177070890843320,                   0,
		 -4.177070890843320, -10.998199092175922,                   0,
		 -7.928086337400578,  -7.928086337400578,   3.563593488561014,
		 -7.928086337400578,  -7.928086337400578,  -3.563593488561014,
		-10.998199092175922,                   0,  -4.177070890843320,
		 -4.177070890843320,                   0, -10.998199092175922,
		 -7.928086337400578,   3.563593488561014,  -7.928086337400578,
		 -7.928086337400578,  -3.563593488561014,  -7.928086337400578,
		                  0, -10.998199092175922,  -4.177070890843320,
		                  0,  -4.177070890843320, -10.998199092175922,
		  3.563593488561014,  -7.928086337400578,  -7.928086337400578,
		 -3.563593488561014,  -7.928086337400578,  -7.928086337400578
	};

	private static final double[] GRADIENTS_3D;
	static {
		GRADIENTS_3D = new double[GRADIENTS_3D_R.length];
		for (int i = 0; i < GRADIENTS_3D_R.length; i += 3) {
			double gxr = GRADIENTS_3D_R[i + 0], gyr = GRADIENTS_3D_R[i + 1], gzr = GRADIENTS_3D_R[i + 2];			
			double grr = (2.0 / 3.0) * (gxr + gyr + gzr);
			double dx = grr - gxr, dy = grr - gyr, dz = grr - gzr;
			GRADIENTS_3D[i + 0] = dx; GRADIENTS_3D[i + 1] = dy; GRADIENTS_3D[i + 2] = dz;
		}
	}
	
	public static class NoiseInstance2 {
		public NoiseInstance2(SuperSimplexNoise noise, int valueIndex,
				int ddxIndex, int ddyIndex) {
			this.noise = noise;
			this.valueIndex = valueIndex;
			this.ddxIndex = ddxIndex;
			this.ddyIndex = ddyIndex;
		}
		public SuperSimplexNoise noise;
		public int valueIndex;
		public int ddxIndex;
		public int ddyIndex;
	}
	
	public static class NoiseInstance3 {
		public NoiseInstance3(SuperSimplexNoise noise, int valueIndex,
				int ddxIndex, int ddyIndex, int ddzIndex) {
			this.noise = noise;
			this.valueIndex = valueIndex;
			this.ddxIndex = ddxIndex;
			this.ddyIndex = ddyIndex;
			this.ddzIndex = ddzIndex;
		}
		public SuperSimplexNoise noise;
		public int valueIndex;
		public int ddxIndex;
		public int ddyIndex;
		public int ddzIndex;
	}
}
