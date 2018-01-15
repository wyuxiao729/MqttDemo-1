package win.hellohang;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;

/** 
 * 发布消息的回调类 
 * 
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。 
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。 
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。 
 * 必须在回调类中实现三个方法： 
 * 
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。 
 * 
 *  public void connectionLost(Throwable cause)在断开连接时调用。 
 * 
 *  public void deliveryComplete(MqttDeliveryToken token)) 
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。 
 *  由 MqttClient.connect 激活此回调。 
 * 
 */  
public class PushCallback implements MqttCallback {  
  
    public void connectionLost(Throwable cause) {  
        // 连接丢失后，一般在这里面进行重连  
        System.out.println("连接断开，可以做重连");  
    }  
  
    public void deliveryComplete(IMqttDeliveryToken token) {  
        System.out.println("deliveryComplete---------" + token.isComplete());  
    }  
  
    public void messageArrived(String topic, MqttMessage message) throws Exception {  
        // subscribe后得到的消息会执行到这里面  
        System.out.println("接收消息主题 : " + topic);  
        System.out.println("接收消息Qos : " + message.getQos());  
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
//        byte[] context = message.getPayload();
//        try {
//            Message model = parseContext(context);
//            System.out.printf(model.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin;i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }

    private Message parseContext(byte [] context){
        ByteBuf in = Unpooled.copiedBuffer(context);
        in.readByte();//保留字段 固定为 0x00;
        int did = ByteUtils.lBytesToInt(in.readBytes(2).array());
        int status = ByteUtils.byteToInt(in.readByte());//设备状态
        int baterry = ByteUtils.byteToInt(in.readByte());//电量
        int sequence = ByteUtils.byteToInt(in.readByte());//传感器计数包
        int year = ByteUtils.byteToInt(in.readByte());//年
        int month = ByteUtils.byteToInt(in.readByte());//月
        int dat = ByteUtils.byteToInt(in.readByte());//日
        int hour = ByteUtils.byteToInt(in.readByte());//时
        int minute = ByteUtils.byteToInt(in.readByte());//分
        int second = ByteUtils.byteToInt(in.readByte());//秒
        int channelSum = ByteUtils.byteToInt(in.readByte()); //通道数(传感器数量)

        List<MessageBody> dataList = new ArrayList<MessageBody>(); //传感器数据
        for (int i = 0; i < channelSum; i++) {
            int channel = ByteUtils.byteToInt(in.readByte()); //通道编号
            int type = ByteUtils.byteToInt(in.readByte()); //类型字段
            byte [] value = new byte[4]; //低位四字节 传感器上报值   ?? 什么 类型的？ int, float, string ????
            in.readBytes(value);
            MessageBody requestData = MessageBody.builder()
                    .channel(channel)
                    .type(type)
                    .value(value)
                    .build();
            dataList.add(requestData);
        }
        int checkSum = ByteUtils.lBytesToInt(in.readBytes(2).array()); //校验位
        Message message = Message.builder()
                .did(did)
                .status(status)
                .baterry(baterry)
                .sequence(sequence)
                .year(year)
                .month(month)
                .dat(dat)
                .hour(hour)
                .minute(minute)
                .second(second)
                .channelSum(channelSum)
                .dataList(dataList)
                .checkSum(checkSum)
                .build();
        return message;
    }

}  
