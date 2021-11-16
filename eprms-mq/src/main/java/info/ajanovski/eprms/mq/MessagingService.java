package info.ajanovski.eprms.mq;

import info.ajanovski.eprms.model.entities.Database;
import info.ajanovski.eprms.model.entities.Repository;
import info.ajanovski.eprms.model.entities.WorkEvaluation;

public interface MessagingService {

	public void setupMQHost(String host);

	public void sendWorkEvaluationNotification(WorkEvaluation we);

	public void sendDatabaseNotification(Database newDb);

	public void sendRepositoryNotification(Repository newRp);

}
