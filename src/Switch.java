
public class Switch {
    private String address;
    private String ip;
    private boolean isUp;
    public Switch(String address, String ip) {
        this.address = address;
        this.ip = ip;
    }
    public String getAddress() {
        return address;
    }

    public String getIp() {
        return ip;
    }
    public void setUp(boolean status) {
        this.isUp = status;
    }
    public boolean isUp() {
        return isUp;
    }

    @Override
    public String toString(){
        return address + "\n" + ip;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Switch && this.getAddress().equals(((Switch) obj).getAddress());
//        return super.equals(obj);
    }
}
