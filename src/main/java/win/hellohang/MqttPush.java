package win.hellohang;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPush {

	private static String MQTT_BROKER = "tcp://118.178.195.40:1883";
	
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
//				byte [] contents = MqttPush.formate();
//				MqttMessage message = new MqttMessage(contents);
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


	private static byte[] formate(){
		short did = 5;
		ByteBuf in = Unpooled.buffer();
		in.writeByte(0x00);
		in.writeBytes(ByteUtils.toLH(did));//did
		in.writeByte(ByteUtils.intToByte(1));//状态
		in.writeByte(ByteUtils.intToByte(98));//电量
		in.writeByte(ByteUtils.intToByte(20));//传感器计数
		in.writeByte(ByteUtils.intToByte(18));//年
		in.writeByte(ByteUtils.intToByte(1));//月
		in.writeByte(ByteUtils.intToByte(5));//日
		in.writeByte(ByteUtils.intToByte(21));//时
		in.writeByte(ByteUtils.intToByte(17));//分
		in.writeByte(ByteUtils.intToByte(56));//秒
		in.writeByte(ByteUtils.intToByte(2));//通道计数

		in.writeByte(ByteUtils.intToByte(111));//通道编号
		in.writeByte(ByteUtils.intToByte(1));//字段类型
		in.writeBytes(ByteUtils.toLH(11));//

		in.writeByte(ByteUtils.intToByte(222));//通道编号
		in.writeByte(ByteUtils.intToByte(2));//字段类型
		in.writeBytes(ByteUtils.toLH(22));//
		in.writeBytes(ByteUtils.toLH(did));
		return in.array();
	}

}
