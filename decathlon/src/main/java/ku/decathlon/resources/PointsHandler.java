package ku.decathlon.resources;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ku.decathlon.Calculator;
import ku.decathlon.Calculators;
import ku.decathlon.Errors;
import ku.decathlon.Parameters;
import ku.decathlon.Parameters.Parameter;

public class PointsHandler {
	private final static Logger log = LoggerFactory.getLogger(PointsHandler.class);
	
	static final String FLD_POINTS = "points";
	static final String FLD_SCORE = "score";
	static final String FLD_SEX = "sex";
	static final String FLD_EVENT = "event";
	static final String FLD_STATUS = "status";
	static final String FLD_CODE = "code";

	private static final String MEN = "men";
	private static final String WOMEN = "women";
	
	private static final String AUTOMATIC = "automatic";
	private static final String MANUAL = "manual";


	JSONObject calculatePoints(String sex, int event, String timing, int score)
			throws JSONException {
		JSONObject data = new JSONObject();  
		Parameter parameters[] = sex.equals(MEN) ? Parameters.parametersForMen : Parameters.parametersForWomen;
		int eventNr = event - 1;
		Calculator calc = Calculators.getCalculator(eventNr, parameters, MANUAL.equals(timing));
		if( calc.validate(score) ) {
			int points = calc.calculate(score);
			data.put(FLD_POINTS, points);
			data.put(FLD_SCORE, score);
			data.put(FLD_SEX, sex);
			data.put(FLD_EVENT, parameters[eventNr].name);
			if(timing != null) {
				data.put("timing", timing);
			}
		} else {
			data.put(FLD_CODE, Errors.ERR_ILLEGAL_RANGE);
			data.put(FLD_STATUS, "Score is out of range");
		}
		return data;
	}

	JSONObject validateInput(String sex, int event, String timing, int score) throws JSONException {
		String resStr = null;
		JSONObject res = null;  
		if(!MEN.equals(sex) && !WOMEN.equals(sex)) {
			resStr = "Illegal value for sex: "+sex;
		}
		if(timing !=null && !AUTOMATIC.equals(timing) && !MANUAL.equals(timing)) {
			resStr = "Illegal value for timing: "+timing;
		}
		if(event < 1 || event > 10) {
			resStr = "Value for event out of range: "+event;
		}
		if(resStr != null) {
			res = new JSONObject();
			res.put(FLD_CODE, Errors.ERR_ILLEGAL_ARG);
			res.put(FLD_STATUS, resStr);
		}
		
		return res;
	}

}
