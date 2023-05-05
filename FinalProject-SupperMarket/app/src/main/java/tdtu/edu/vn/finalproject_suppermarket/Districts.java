package tdtu.edu.vn.finalproject_suppermarket;

import androidx.annotation.NonNull;

public class Districts {
    private String _id;
    private String nameWithType;
    private String code;
    private String parentCode;



    @NonNull
    @Override
    public String toString() {
        return this.nameWithType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNameWithType() {
        return nameWithType;
    }

    public void setNameWithType(String nameWithType) {
        this.nameWithType = nameWithType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Districts(String _id, String nameWithType, String code, String parentCode) {
        this._id = _id;
        this.nameWithType = nameWithType;
        this.code = code;
        this.parentCode = parentCode;
    }
}
