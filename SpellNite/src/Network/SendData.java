/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Network;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author johnn
 */
public class SendData implements Serializable {
    private String msg;
    private List<NetworkObject> objs;
    private int id;

    public SendData(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<NetworkObject> getObjs() {
        return objs;
    }

    public void setObjs(List<NetworkObject> objs) {
        this.objs = objs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
