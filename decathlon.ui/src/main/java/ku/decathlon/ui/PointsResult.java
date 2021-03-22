package ku.decathlon.ui;

public class PointsResult {
	public final String id;
	public final int points;
	public final String status;
	public PointsResult(String id, int points, String status) {
		super();
		this.id = id;
		this.points = points;
		this.status = status;
	}
	@Override
	public String toString() {
		return "PointsResult [id=" + id + ", points=" + points + ", status=" + status + "]";
	}
}
