package nguyenvanquan7826.com.demojson;

import org.json.JSONException;
import org.json.JSONObject;

public class Lover {
    private int id;
    private String name;
    private String phone;
    private String beginDate;
    private String endDate;

    public Lover() {
    }

    public Lover(int id, String name, String phone, String beginDate, String endDate) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public Lover setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Lover setName(String name) {
        this.name = name;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public Lover setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public Lover setBeginDate(String beginDate) {
        this.beginDate = beginDate;
        return this;
    }

    public String getEndDate() {
        return endDate;
    }

    public Lover setEndDate(String endDate) {
        this.endDate = endDate;
        return this;
    }

    public String toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Var.KEY_ID, getId());
            jsonObject.put(Var.KEY_NAME, getName());
            jsonObject.put(Var.KEY_PHONE, getPhone());
            jsonObject.put(Var.KEY_BEGIN_DATE, getBeginDate());
            jsonObject.put(Var.KEY_END_DATE, getEndDate());

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

}
