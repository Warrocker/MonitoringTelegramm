import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class SwitchChecker extends Observable implements Runnable{
    private Switch targetSwitch;
    private InetAddress inetAddress = null;
    public SwitchChecker(Switch targetSwitch, Observer o) {
        this.addObserver(o);
        this.targetSwitch = targetSwitch;
        Thread t = new Thread(this);
        t.start();
    }
    @Override
    public void run() {

        try {
            inetAddress = InetAddress.getByName(targetSwitch.getIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            if (inetAddress != null && inetAddress.isReachable(3000)) {
                targetSwitch.setUp(true);
                setChanged();
                notifyObservers(targetSwitch);
            }else {
                targetSwitch.setUp(false);
                setChanged();
                notifyObservers(targetSwitch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
