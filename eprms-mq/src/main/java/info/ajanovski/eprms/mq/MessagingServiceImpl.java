package info.ajanovski.eprms.mq;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import org.slf4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.model.entities.WorkEvaluation;

public class MessagingServiceImpl implements MessagingService {

	@Inject
	private Logger logger;

	private ConnectionFactory factory;

	private static String exchangeMain = "EPRMS";

	public MessagingServiceImpl() {
		factory = new ConnectionFactory();
	}

	public void setupMQHost(String host) {
		factory.setHost(host);
	}

	public JsonValue processValue(String value) {
		return ((value == null) ? JsonValue.NULL : Json.createValue(value));
	}

	public void prepareAndSendMessage(JsonObject jsonObject, String tag, String destination) {
		try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Json.createWriter(baos).writeObject(jsonObject);
			channel.exchangeDeclare(destination, "topic", true);
			channel.basicPublish(destination, tag, null, baos.toByteArray());
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void sendWorkEvaluationNotification(WorkEvaluation we) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("title", processValue(we.getTitle()));
		builder.add("description", processValue(we.getDescription()));
		builder.add("points", processValue(we.getPoints().toString()));
		builder.add("action", "create");
		prepareAndSendMessage(builder.build(), "workeval.create", exchangeMain);
	}

	@Override
	public void sendDatabaseNotification(Database newDb) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("dbType", processValue(newDb.getType()));
		builder.add("dbServer", processValue(newDb.getServer()));
		builder.add("dbPort", processValue(newDb.getPort()));
		builder.add("dbName", processValue(newDb.getName()));
		builder.add("dbOwner", processValue(newDb.getOwner()));
		builder.add("dbPass", processValue(newDb.getPassword()));
		builder.add("dbTunServer", processValue(newDb.getTunnelServer()));
		builder.add("dbTunUser", processValue(newDb.getTunnelUser()));
		builder.add("dbTunPassword", processValue(newDb.getTunnelPassword()));
		builder.add("action", "create");
		prepareAndSendMessage(builder.build(), "db.create", exchangeMain);
	}

	@Override
	public void sendRepositoryNotification(Repository newRp) {
		JsonObjectBuilder builder = Json.createObjectBuilder();
		builder.add("rpName", processValue(newRp.getTitle()));
		builder.add("rpType", processValue(newRp.getType()));
		builder.add("rpUrl", processValue(newRp.getUrl()));
		if (newRp.getPerson() != null) {
			builder.add("rpOwner", processValue(newRp.getPerson().getUserName()));
		} else {
			builder.add("rpOwner", processValue(null));
		}
		builder.add("action", "create");
		prepareAndSendMessage(builder.build(), "repo.create", exchangeMain);
	}

}
