package ku.decathlon;

import java.math.BigDecimal;

public class Parameters {
	public static class Parameter{
		public final String name;
		final BigDecimal a; 
		final BigDecimal b;
		final BigDecimal c;
		final BigDecimal m;
		public Parameter(String name, String a, String b, String c,String m) {
			super();
			this.name = name;
			this.a = new BigDecimal(a);
			this.b = new BigDecimal(b);
			this.c = new BigDecimal(c);
			this.m = new BigDecimal(m); 
		}
		@Override
		public String toString() {
			return "Parameter [name=" + name + ", a=" + a + ", b=" + b + ", c=" + c + ", m=" + m + "]";
		}
	}
	public static Parameter parametersForMen[] = new  Parameter[] {
		new Parameter("100 m","25.4347","18.00","1.81","9.50"),
		new Parameter("Long jump","0.14354","220.00","1.40","9.49"),
		new Parameter("Shot put","51.39","1.50","1.05","23.99"),
		new Parameter("High jump","0.8465","75.00","1.42","2.59"),
		new Parameter("400 m","1.53775","82","1.81","41.47"),
		new Parameter("110 m hurdles","5.74352","28.50","1.92","12.00"),
		new Parameter("Discus throw","12.91","4.00","1.10","79.41"),
		new Parameter("Pole vault","0.2797","100.00","1.35","6.49"),
		new Parameter("Javelin throw","10.14","7.00","1.08","102.85"),
		new Parameter("1500 m","0.03768","480","1.85","202.23") // 3:22.23
	};
	public static Parameter parametersForWomen[] = new  Parameter[] {
		new Parameter("100 m","17.8570","21.0","1.81","10.80"),
		new Parameter("Long jump","0.188807","210","1.41","7.99"),
		new Parameter("Shot put","56.0211","1.50","1.05","24.40"),
		new Parameter("High jump","1.84523","75","1.348","2.19"),
		new Parameter("400 m","1.34285","91.7","1.81","48.02"),
		new Parameter("100 m hurdles","9.23076","26.70","1.835","11.50"),
		new Parameter("Discus throw","12.3311","3.00","1.10","72.03"),
		new Parameter("Pole vault","0.44125","100","1.35","4.70"),
		new Parameter("Javelin throw","15.9803","3.80","1.04","82.63"),
		new Parameter("1500 m","0.02883","535","1.88","242.23") // 4:02.23
	};
}
