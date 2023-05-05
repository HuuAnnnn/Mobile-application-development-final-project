package tdtu.edu.vn.finalproject_suppermarket;

import androidx.annotation.NonNull;

public class Ward {
    private String nameWithType;

    @NonNull
    @Override
    public String toString() {
        return this.nameWithType;
    }

    public Ward(String nameWithType) {
        this.nameWithType = nameWithType;
    }

    public String getNameWithType() {
        return nameWithType;
    }

    public void setNameWithType(String nameWithType) {
        this.nameWithType = nameWithType;
    }
}
