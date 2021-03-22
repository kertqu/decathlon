package ku.decathlon.resources;

import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import ku.decathlon.Errors;

public class PointsCalculationTest {

	PointsHandler service = new PointsHandler();
	
	@Test
	public void testValidateInputSex() throws JSONException {
		JSONObject res = service.validateInput("men", 1, null, 100);
		assertNull(res);
		res = service.validateInput("women", 1, null, 100);
		assertNull(res);
		res = service.validateInput("xx", 1, null, 100);
		assertEquals(Errors.ERR_ILLEGAL_ARG, res.get("code"));
		assertTrue(res.has("status"));
	}
	
	@Test
	public void testValidateInputTiming() throws JSONException {
		JSONObject res = service.validateInput("men", 1, "automatic", 100);
		assertNull(res);
		res = service.validateInput("men", 1, "manual", 100);
		assertNull(res);
		res = service.validateInput("men", 1, "xx", 100);
		assertEquals(Errors.ERR_ILLEGAL_ARG, res.get("code"));
		assertTrue(res.has("status"));
	}

	@Test
	public void testValidateInputEvent() throws JSONException {
		JSONObject res = service.validateInput("men", 1, null, 100);
		assertNull(res);
		res = service.validateInput("men", 10, null, 100);
		assertNull(res);
		res = service.validateInput("men", 0, null, 100);
		assertEquals(Errors.ERR_ILLEGAL_ARG, res.get("code"));
		assertTrue(res.has("status"));
		res = service.validateInput("men", 11, null, 100);
		assertEquals(Errors.ERR_ILLEGAL_ARG, res.get("code"));
		assertTrue(res.has("status"));
	}

	@Test
	public void testCalculateOutOfRange() throws JSONException {
		JSONObject res = service.calculatePoints("men", 1, "automatic", 8000);
		assertEquals(Errors.ERR_ILLEGAL_RANGE, res.get("code"));
		assertTrue(res.has("status"));
	}
	
	@Test
	public void testCalculateTiming() throws JSONException {
		JSONObject res = service.calculatePoints("men", 1, "automatic", 10400);
		assertEquals(999, res.get("points"));
		res = service.calculatePoints("men", 1, "manual", 10400);
		assertEquals(942, res.get("points"));
	}
	
	@Test
	public void testCalculateEdmonton2001Dvorak() throws JSONException {
		JSONObject res = service.calculatePoints("men", 1, null, 10620);
		assertEquals(947, res.get("points"));
		res = service.calculatePoints("men", 2, null, 8070);
		assertEquals(1079, res.get("points"));
		res = service.calculatePoints("men", 3, null, 16570);
		assertEquals(886, res.get("points"));
		res = service.calculatePoints("men", 4, null, 2000);
		assertEquals(803, res.get("points"));
		res = service.calculatePoints("men", 5, null, 47740);
		assertEquals(922, res.get("points"));
		res = service.calculatePoints("men", 6, null, 13800);
		assertEquals(1000, res.get("points"));
		res = service.calculatePoints("men", 7, null, 45510);
		assertEquals(777, res.get("points"));
		res = service.calculatePoints("men", 8, null, 5000);
		assertEquals(910, res.get("points"));
		res = service.calculatePoints("men", 9, null, 68530);
		assertEquals(867, res.get("points"));
		res = service.calculatePoints("men", 10, null, 275130);
		assertEquals(711, res.get("points"));
	}

	@Test
	public void testCalculateEdmonton2001Nool() throws JSONException {
		JSONObject res = service.calculatePoints("men", 1, null, 10600);
		assertEquals(952, res.get("points"));
		res = service.calculatePoints("men", 2, null, 7630);
		assertEquals(967, res.get("points"));
		res = service.calculatePoints("men", 3, null, 14900);
		assertEquals(784, res.get("points"));
		res = service.calculatePoints("men", 4, null, 2030);
		assertEquals(831, res.get("points"));
		res = service.calculatePoints("men", 5, null, 46230);
		assertEquals(997, res.get("points"));
		res = service.calculatePoints("men", 6, null, 14400);
		assertEquals(924, res.get("points"));
		res = service.calculatePoints("men", 7, null, 43400);
		assertEquals(734, res.get("points"));
		res = service.calculatePoints("men", 8, null, 5400);
		assertEquals(1035, res.get("points"));
		res = service.calculatePoints("men", 9, null, 67010);
		assertEquals(844, res.get("points"));
		res = service.calculatePoints("men", 10, null, 269580);
		assertEquals(747, res.get("points"));
	}
	
	@Test
	public void testCalculateSkujyteWR() throws JSONException {
		int firstDay = 0;
		int secondDay = 0;
		JSONObject res = service.calculatePoints("women", 1, null, 12450);
		firstDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 2, null, 6120);
		secondDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 3, null, 16420);
		secondDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 4, null, 1780);
		secondDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 5, null, 57190);
		firstDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 6, null, 14220);
		secondDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 7, null, 46190);
		firstDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 8, null, 3100);
		firstDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 9, null, 48780);
		firstDay += (Integer)res.get("points");
		res = service.calculatePoints("women", 10, null, 315860);
		secondDay += (Integer)res.get("points");
		assertEquals(3898, firstDay);
		assertEquals(4468, secondDay);
		assertEquals(8366, firstDay + secondDay);
	}
}
