package tdtu.edu.vn.finalproject_suppermarket.Cart;

public interface OnDataChangeListener {
    void onDataChanged(String data);

    void startChange(boolean state);

    void endChange(boolean state);
}
