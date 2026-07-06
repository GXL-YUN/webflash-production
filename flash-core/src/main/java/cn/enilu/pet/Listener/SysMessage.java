package cn.enilu.pet.Listener;



import lombok.Data;
import java.io.Serializable;

@Data
public class SysMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String content;
    private Long timestamp;

    public SysMessage() {
    }

    public SysMessage(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }
}