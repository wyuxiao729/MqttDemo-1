package win.hellohang;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Description :
 * Created by wkp on 2018/1/5 20:52.
 */
@Setter
@Getter
@Builder
@ToString
public class Message {

    //设备ID
    private int did;
    //设备状态
    private int status;
    //设备电量
    private int baterry;
    //传感器数据包计数器
    private int sequence;
    //年
    private int year;
    //月
    private int month;
    //日
    private int dat;
    //时
    private int hour;
    //分
    private int minute;
    //秒
    private int second;
    //通道数
    private int channelSum;
    //传感器数据
    private List<MessageBody> dataList;
    //校验位
    private long checkSum;
}
