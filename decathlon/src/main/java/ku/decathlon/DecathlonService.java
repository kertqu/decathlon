package ku.decathlon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyWebContainerFactory;

public class DecathlonService {

	private final static Logger log = LoggerFactory.getLogger(DecathlonService.class);

	static final String DEFAULT_IP = "127.0.0.1";
	static final String DEFAULT_PORT = "9998";
	static final String WEB_ROOT = "/decathlon";

	private String ip;
	private String port;
	private String baseUri;
	private boolean debugEnabled;

	public static void main(String[] args) throws IOException {
		new DecathlonService().start();
	}

	public void start() throws IOException {
		checkDebugMode();
		resolveURI();


		final Map<String, String> initParams = new HashMap<String, String>();
		initParams.put("com.sun.jersey.config.property.packages", "ku.decathlon.resources");
		initParams.put("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.GZIPContentEncodingFilter");		
		initParams.put("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.GZIPContentEncodingFilter");

		log.info("Starting server at " + ip + ":" + port + (debugEnabled ? " (in debug mode)" : " (in normal mode)"));

		// com.sun.grizzly.minWorkerThreads
		// com.sun.grizzly.maxThreads
		SelectorThread selector = GrizzlyWebContainerFactory.create(baseUri, initParams);
		SelectorThread.setEnableNioLogging(debugEnabled);

		log.info(String.format("Started with WADL available at %sapplication.wadl", baseUri, baseUri));
	}

	public static String getDefaultURI() {
		return "http://" + DEFAULT_IP + ":" + DEFAULT_PORT + WEB_ROOT + "/";
	}

	private void checkDebugMode() {
		String debugStr = System.getProperty("debug");
		debugEnabled = debugStr != null && "true".equalsIgnoreCase(debugStr);
	}

	private void resolveURI() {
		String propIp = System.getProperty("ip");
		String propPort = System.getProperty("port");
		ip = propIp != null ? propIp : DEFAULT_IP;
		port = propPort != null ? propPort : DEFAULT_PORT;
		baseUri = "http://" + ip + ":" + port + WEB_ROOT + "/";
	}

}