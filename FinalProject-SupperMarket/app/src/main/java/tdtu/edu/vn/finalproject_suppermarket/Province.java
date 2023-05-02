package tdtu.edu.vn.finalproject_suppermarket;

public class Province {
    private String _id;
    private String name_with_type;
    private String code;


    public Province(String _id, String name_with_type, String code) {
        this._id = _id;
        this.name_with_type = name_with_type;
        this.code = code;
    }

    // getters and setters
    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName_with_type() {
        return name_with_type;
    }

    public void setName_with_type(String name_with_type) {
        this.name_with_type = name_with_type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
