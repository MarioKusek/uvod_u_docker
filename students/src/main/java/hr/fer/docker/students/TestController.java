package hr.fer.docker.students;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	private BuildProperties buildProperties;
	private String hostname;

	public TestController(BuildProperties buildProperties, @Value("${HOSTNAME:unknown}") String hostname) {
		this.buildProperties = buildProperties;
		this.hostname = hostname;
	}

	@GetMapping("/health")
	public String health() {
		return "OK";
	}

	@GetMapping("/stats")
	public String getVersion() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \n");

		sb.append("  \"group\": \"");
		sb.append(buildProperties.getGroup());
		sb.append("\",\n");

		sb.append("  \"artifact\": \"");
		sb.append(buildProperties.getArtifact());
		sb.append("\",\n");

		sb.append("  \"version\": \"");
		sb.append(buildProperties.getVersion());
		sb.append("\",\n");


		sb.append("  \"containerName\": \"");
		sb.append(hostname);
		sb.append("\",\n");

		sb.append("  \"memory in MB\": ");
		sb.append(Runtime.getRuntime().maxMemory()/1024/1024);
		sb.append(",\n");

		sb.append("  \"CPU\": ");
		sb.append(Runtime.getRuntime().availableProcessors());
		sb.append("\n");

		sb.append("}");

		return sb.toString();
	}
}
