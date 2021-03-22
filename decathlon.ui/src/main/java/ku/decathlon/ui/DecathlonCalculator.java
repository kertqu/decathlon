package ku.decathlon.ui;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.json.JSONException;
import org.json.JSONObject;


public class DecathlonCalculator {
	
	private static final String MEN = "men";
	private static final String WOMEN = "women";
	
	private static final String AUTOMATIC = "automatic";
	private static final String MANUAL = "manual";

	private static final int CONNECTION_TIMEOUT = 15000; // 15 seconds
	
	private interface ScoreConverter {
		int convert(String score) throws ParseException;
	}
	
	private static ScoreConverter timeScoreConverter = new ScoreConverter(){
		public int convert ( String score) throws ParseException {
			String text = "PT"+score.replace(":", "M").replace(",",".")+"S";
			return (int)Duration.parse(text).toMillis();
		}		
	};
	
	private static ScoreConverter distanceScoreConverter = new ScoreConverter(){
		public int convert(String score) throws ParseException {
			String scoreStr = score;
			NumberFormat numberInstance = NumberFormat.getNumberInstance();
			if(numberInstance instanceof DecimalFormat) {
				DecimalFormatSymbols symbols = ((DecimalFormat)numberInstance).getDecimalFormatSymbols();
				scoreStr = score.replaceAll("[.,]", Character.toString(symbols.getDecimalSeparator()));
			}
			double value = numberInstance.parse(scoreStr).doubleValue();
			return (int)(value*1000.0);
		}		
	};
	
	private ScoreConverter getScoreConvert(String id) {
		switch(id) {
		case "1": 
		case "5": 
		case "6": 
		case "10": 
			return timeScoreConverter;
		default:
			return distanceScoreConverter;
		}
	}
	

	private String host = "localhost";
	private int port = 9998;
	
	PointsResult[] calculate(String [][] results, int sex, int timing) {
		return Arrays.stream(results).map(r -> calculate(r[0], r[1], sex,timing)).collect(Collectors.toList()).toArray(new PointsResult[0]);
	}
	
	private PointsResult calculate(String id, String result, int sex, int timing)  {
		if(result == null || result.trim().isEmpty()) return null;
		
		PointsResult res;
		try {
			res = sendRequest(timing, getScoreConvert(id).convert(result), id, sex);
		} catch (Exception e) {
			res = new PointsResult(id, -1, e.getMessage() );
		}
		return res;
	}

	public void setServerAddress(String res) {
		try {
			String addr[] = res.split(":");
			host = addr[0];
			if(addr.length > 1) port = Integer.valueOf(addr[1]);
		}catch(Exception e) {}
	}

	public String getServerAddress() {
		return host+":" + port;
	}
	
	private PointsResult sendRequest(int timing, long value, String event, int sex) throws HttpException, IOException, JSONException {
		final PointsResult res;
		
		URL url2 = new URL("http",host,port,"/decathlon/points/"+(sex==0?MEN:WOMEN)+"/"+event+"/"+Long.toString(value));
		
		GetMethod request = new GetMethod(url2.toExternalForm());
		request.setQueryString(new NameValuePair[] {
				new NameValuePair("timing", timing == 0 ? AUTOMATIC : MANUAL)});
		request.setDoAuthentication(false);
		HttpClientParams httpClientParams = new HttpClientParams();
		httpClientParams.setConnectionManagerTimeout(CONNECTION_TIMEOUT);
		HttpClient client = new HttpClient(httpClientParams);
		int status = client.executeMethod(request);
		if (status == HttpStatus.SC_OK) {
			 byte[] body = request.getResponseBody();
			 JSONObject jsonRes = new JSONObject(new String(body));	
			 res = new PointsResult(event, jsonRes.getInt("points"), null);
		} else {
			 byte[] body = request.getResponseBody();
			 JSONObject jsonRes = new JSONObject(new String(body));	
			 res = new PointsResult(event, -1, jsonRes.getString("status"));
			 
		}
		return res;
	}
}
