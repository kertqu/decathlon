package ku.decathlon.resources;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ku.decathlon.Errors;


@Path("points/{sex}/{event}/{score}")
public class PointsResourse {

	private final static Logger log = LoggerFactory.getLogger(PointsResourse.class);
	
	private PointsHandler handler = new PointsHandler();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPointsResponse(
			@PathParam("sex") String sex,
			@PathParam("event") int event,
			@QueryParam("timing") String timing,
			@PathParam("score") int score
			) {
		Status status = OK;
		String dataStr = "";
		JSONObject data = null;  
		try {
			data = handler.validateInput(sex, event, timing, score);
			if(data == null) {
				data = handler.calculatePoints(sex, event, timing, score);
				if(data.has(PointsHandler.FLD_CODE)) {
					status = BAD_REQUEST;
				}
			} else {
				status = BAD_REQUEST;
			}
		} catch (JSONException e1) {
			log.error("Can't create result", e1);
			status = INTERNAL_SERVER_ERROR;
			dataStr = "System error";
		} catch (Exception e) {
			try { 
				log.error("Unexpected error", e);
				data = new JSONObject();
				data.put(PointsHandler.FLD_CODE, Errors.ERR_ILLEGAL_ARG);
				data.put(PointsHandler.FLD_STATUS, "Server error");
				status = BAD_REQUEST;
			} catch (JSONException e1) {
				log.error("Can't create result", e1);
				status = INTERNAL_SERVER_ERROR;
				dataStr = "System error";
			}
		}
		if(dataStr.isEmpty() && data != null) dataStr = data.toString();
		return Response.status(status).entity(dataStr).build();
	}
}
