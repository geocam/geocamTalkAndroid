package gov.nasa.arc.geocam.talk.service;

import gov.nasa.arc.geocam.talk.R;
import gov.nasa.arc.geocam.talk.bean.TalkServerIntent;
import gov.nasa.arc.geocam.talk.bean.GeoCamTalkMessage;
import gov.nasa.arc.geocam.talk.exception.AuthenticationFailedException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import roboguice.inject.InjectResource;
import roboguice.service.RoboIntentService;
import android.content.Intent;
import android.util.Log;

import com.google.inject.Inject;

public class TalkServer extends RoboIntentService implements ITalkServer {

	@Inject
	ITalkJsonConverter jsonConverter;

	@InjectResource(R.string.url_message_list)
	String urlMessageList;

	@InjectResource(R.string.url_create_message)
	String urlCreateMessage;

	@Inject
	ISiteAuth siteAuth;

	@Inject
	IMessageStore messageStore;

	@Inject
	IIntentHelper intentHelper;

	@Inject
	IGeoCamSynchronizationTimerTask geoCamSynchronizationTimerTask;

	public TalkServer() {
		super("DjangoTalkService");
	}

	@Override
	public void getTalkMessages() throws SQLException, ClientProtocolException,
			AuthenticationFailedException, IOException {

		// let's check the server and add any new messages to the database
		String jsonString = null;

		jsonString = siteAuth.get(urlMessageList, null);
		List<GeoCamTalkMessage> newMessages = jsonConverter.deserializeList(jsonString);

		if (newMessages.size() > 0) {
			for(GeoCamTalkMessage message : newMessages) {
				message.setSynchronized(true); // TODO re factor this to not suck, as we 
				                               // iterate again in addMessage
			}
			
			messageStore.addMessage(newMessages);
			intentHelper.BroadcastNewMessages();
		}
	}

	@Override
	public void createTalkMessage(GeoCamTalkMessage message) throws ClientProtocolException,
			AuthenticationFailedException, IOException, SQLException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("message", jsonConverter.serialize(message));
		int responseCode = siteAuth.post(urlCreateMessage, map, message.getAudio());
		if (responseCode != 200) {
			throw new ClientProtocolException("Message could not be created (HTTP error "
					+ responseCode + ")");
		}
		
		messageStore.removeMessage(message);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().contentEquals(TalkServerIntent.SYNCHRONIZE.toString())) {
			try {
				for (GeoCamTalkMessage message : messageStore.getAllLocalMessages()) {
					this.createTalkMessage(message);
				}

				this.getTalkMessages();
				this.geoCamSynchronizationTimerTask.resetTimer();
			} catch (Exception e) {
				Log.e("GeoCam Talk", "Comm Error", e);
				// TODO: Display this to the user (Toast or notification bar)
			}
		}
	}
}