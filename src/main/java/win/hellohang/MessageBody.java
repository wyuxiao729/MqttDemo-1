package win.hellohang;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Description :
 * Created by wkp on 2018/1/5 20:53.
 */
@Setter
@Getter
@Builder
@ToString
public class MessageBody {
    //传感器编号
    private int channel;
    //上报数据类型
    private int type;
    //上报数据
    private byte [] value;
}
