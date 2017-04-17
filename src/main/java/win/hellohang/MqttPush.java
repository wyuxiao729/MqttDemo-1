package win.hellohang;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPush {

	private static String MQTT_BROKER = "tcp://192.168.44.128:1883";
	
	private static String topic = "hello";

	public void publishMessageByMqtt(String content) throws Exception {
		//消息传送的要求
		int qos = 2;
		String clientId = "cspublish_mqtt";
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			MqttClient mqttClient = new MqttClient(MQTT_BROKER, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			connOpts.setConnectionTimeout(1000);
			connOpts.setKeepAliveInterval(2000);

			try {
				mqttClient.connect(connOpts);
				MqttMessage message = new MqttMessage(content.getBytes());
				message.setQos(qos);
				mqttClient.publish(topic, message);
				System.out.println("Message published");
				mqttClient.disconnect();
			} catch (Throwable e) {
				System.out.println("Error " + e.getMessage());
			}
		} catch (MqttException me) {
			System.out.println("reason " + me.getReasonCode());
			System.out.println("msg " + me.getMessage());
			System.out.println("loc " + me.getLocalizedMessage());
			System.out.println("cause " + me.getCause());
			System.out.println("excep " + me);
			me.printStackTrace();
		}

	}
	public static void main(String []args) throws Exception{
		MqttPush handler = new MqttPush();
		handler.publishMessageByMqtt("hello mqtt 你好");
	}
}
