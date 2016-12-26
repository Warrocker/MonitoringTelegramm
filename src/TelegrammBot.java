import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.ArrayList;

public class TelegrammBot extends TelegramLongPollingBot {
    final private SwitchCheckerManager switchCheckerManager;
    TelegrammBot(SwitchCheckerManager scm) {
        switchCheckerManager = scm;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.hasText()) {
            switch (message.getText()) {
                case "/down":
                    printUnreachableHosts(switchCheckerManager.getUnreachableHosts());
                    break;
                case "/stop":
                    switchCheckerManager.stop();
                    sendMsg("Stopping...");
                    break;
                case "/start":
                    switchCheckerManager.start();
                    sendMsg("Starting...");
                    break;
                case "/restart":
                    switchCheckerManager.stop();
                    switchCheckerManager.start();
                    sendMsg("Restarting...");
                    break;
            }
        } else{
            sendMsg("Я не знаю что ответить на это");
        }
    }

    @Override
    public String getBotUsername() {
        return "botname";
    }

    @Override
    public String getBotToken() {
        return "Token";
    }
    private void sendMsg(String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId("chatid(int)");
//        sendMessage.setReplayToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void printUnreachableHosts(ArrayList<Switch> swList){
        for(Switch sw : swList){
            sendDownMessage(sw);
        }
    }
    public void sendDownMessage(Switch sw){
        String messageText = String.format("%s \n Down", sw.toString());
        sendMsg(messageText);
    }
    public void sendUpMessage(Switch sw){
        String messageText = String.format("%s \n UP", sw.toString());
        sendMsg(messageText);
    }
}
