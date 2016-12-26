import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class SwitchCheckerManager implements Observer{
    private ArrayList<Switch> unreachableHosts = new ArrayList<>();
    private ArrayList<Switch> hosts = new ArrayList<>();
    private TelegrammBot bot;
    private boolean isStopped = false;
    SwitchCheckerManager(){
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        bot = new TelegrammBot(this);
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        checkSwitches();
    }
    @Override
    public void update(Observable o, Object arg) {
        Switch receivedSwitch = (Switch) arg;
        if(receivedSwitch.isUp() & unreachableHosts.contains(receivedSwitch)) {
            System.out.println("UP");
            bot.sendUpMessage(receivedSwitch);
            unreachableHosts.remove(receivedSwitch);
        }else if(!receivedSwitch.isUp() & !unreachableHosts.contains(receivedSwitch)) {
            unreachableHosts.add(receivedSwitch);
            System.out.println(unreachableHosts.size());
            bot.sendDownMessage(receivedSwitch);
        }
    }
    public void stop(){
    isStopped = true;
    }
    public void start(){
        if(isStopped) {
            isStopped = false;
            checkSwitches();
        }
    }

    public ArrayList<Switch> getUnreachableHosts() {
        return unreachableHosts;
    }

    private void checkSwitches(){
        while (!isStopped) {
            hosts = DatabaseHelper.getInstance().dbQuery("SELECT alias AS 'address', ipaddress AS 'ip' FROM `ac_node` WHERE 'address' NOT LIKE '%#' ORDER BY alias ASC");
            for (Switch targetSwitch : hosts) {
                new SwitchChecker(targetSwitch, this);
            }
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
