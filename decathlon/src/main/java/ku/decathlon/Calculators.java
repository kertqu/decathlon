package ku.decathlon;

import java.math.BigDecimal;

import ku.decathlon.Parameters.Parameter;


public class Calculators{
	public static Calculator getCalculator(int event, Parameter parameters[], boolean manualTiming) {
		if(event == 0 || event == 4 || event == 5 || event == 9) {
			int adjustmentMs = 0;
			if(manualTiming) {
				if(event == 4) {
					adjustmentMs = 140;					
				} else if(event == 0 || event == 5) {
					adjustmentMs = 240;
				}	
			}
			
			return new TrackEventCalculator(parameters[event], adjustmentMs);
		}
		if(event == 1 || event == 3 || event == 7) {
			return new JumpCalculator(parameters[event]);
		}
		if(event == 2 || event == 6 || event == 8) {
			return new ThrowCalculator(parameters[event]);
		}
		throw new IllegalArgumentException("Illegal event "+event);
	}
}
abstract class AbstractCalculator implements Calculator{
	Parameter p;

	public AbstractCalculator(Parameter parameter) {
		this.p = parameter;
	}
	@Override
	public abstract boolean validate(int score);
	@Override
	public abstract int calculate(int score);
	
	protected abstract boolean isZero(int score);
}
class TrackEventCalculator extends AbstractCalculator{
	private final int adjustmentMs;
	public TrackEventCalculator(Parameter parameter, int adjustmentMs) {
		super(parameter);
		this.adjustmentMs = adjustmentMs;
	}
	//	Track events P=a*(b - T)**c [where T is Time in seconds; e.g. 10.43 for 100 metres]
	@Override
	public int calculate(int timeMs) {		
		return isZero(timeMs) ? 0 :			
		p.a.multiply(
			BigDecimal.valueOf(
				Math.pow(
					p.b.subtract(getValue(timeMs)).doubleValue(),
					p.c.doubleValue()
				)
			)
		).intValue();
	}
	private BigDecimal getValue(int timeMs) {
		return new BigDecimal(timeMs+adjustmentMs).divide(new BigDecimal(1000));
	}
	@Override
	public boolean validate(int timeMs) {
		return p.m.compareTo(getValue(timeMs)) <= 0;
	}
	@Override
	protected boolean isZero(int timeMs) {
		return p.b.compareTo(getValue(timeMs)) <= 0;
	}
}	
class JumpCalculator extends AbstractCalculator{
	public JumpCalculator(Parameter parameter) {
		super(parameter);
	}
//	Jumps P=a*(M - b)**c [where M is Measurement in centimetres; e.g. 808 for	"LJ".]
	@Override
	public int calculate(int measurementMm) {
		return isZero(measurementMm) ? 0 :			
		p.a.multiply(
				BigDecimal.valueOf(
					Math.pow(
						getValue(measurementMm).subtract(p.b).doubleValue(),
						p.c.doubleValue()
					)
				)
			).intValue();
	}
	private BigDecimal getValue(int measurementMm) {
		return new BigDecimal(measurementMm).divide(BigDecimal.TEN);
	}
	@Override
	public boolean validate(int measurementMm) {
		return p.m.multiply(BigDecimal.valueOf(100)).compareTo(getValue(measurementMm)) >= 0;
	}
	@Override
	protected boolean isZero(int measurementMm) {
		return p.b.compareTo(getValue(measurementMm)) >= 0;
	}
}
class ThrowCalculator extends AbstractCalculator{
	public ThrowCalculator(Parameter parameter) {
		super(parameter);
	}
//	Throws P=a*(D - b)**c [where D is Distance in metres; e.g. 16.69 for Shot]
	@Override
	public int calculate(int distanceMm) {
		return isZero(distanceMm) ? 0 :			
		p.a.multiply(
				BigDecimal.valueOf(
					Math.pow(
						getValue(distanceMm).subtract(p.b).doubleValue(),
						p.c.doubleValue()
					)
				)
			).intValue();
	}
	private BigDecimal getValue(int distanceMm) {
		return new BigDecimal(distanceMm).divide( BigDecimal.valueOf(1000));
	}
	@Override
	public boolean validate(int distanceMm) {
		return p.m.compareTo(getValue(distanceMm)) >= 0;
	}
	@Override
	protected boolean isZero(int distanceMm) {
		return p.b.compareTo(getValue(distanceMm)) >= 0;
	}
}
