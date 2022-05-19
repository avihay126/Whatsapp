import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class WhatsAppScene extends JPanel {
    public static final Color WHITE = Color.white;
    private static final String KIND_OF_FONT = "Ariel";
    public static final String IMAGE_NAME ="whatsapp web.png";
    private static final int FONT_SIZE_BUTTON = 18, SIZE_FONT_LABEL = 20,WELCOME_MASSAGE_WIDTH=350,
            OPEN_BUTTON_WIDTH = 180, OPEN_BUTTON_HEIGHT = 50, OPEN_BUTTON_Y = 450,
            TEXT_FIELD_WIDTH = 200, TEXT_FIELD_HEIGHT = 60,
            CONNECTION_PRINT_WIDTH = 220, CONNECTION_PRINT_HEIGHT = 70,
            ERROR_PRINT_WIDTH = 220, ERROR_PRINT_HEIGHT = 70,
            SEND_PRINT_WIDTH = 300, SEND_PRINT_HEIGHT = 70;

    private ChromeDriver driver;
    private JButton openWhatsApp;
    private JLabel connectionSucceed;
    private JLabel error;
    private JTextField phoneNumberField;
    private JTextField textField;
    private JTextField welcomeSign;
    private WebElement user;
    private boolean connecting;
    private ImageIcon backGround;


    public WhatsAppScene(int x, int y, int width, int height) {
        this.setBounds(x, y, width, height);
        this.setLayout(null);
        textFields();
        openWhatsAppWeb();
        this.setDoubleBuffered(true);
        this.setVisible(true);
        this.backGround = new ImageIcon(IMAGE_NAME);

    }

    private void textFields() {
        this.phoneNumberField = addTextField(this.getWidth() / 3 - TEXT_FIELD_WIDTH / 2, this.getHeight() / 2 - TEXT_FIELD_HEIGHT / 2, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        JLabel phoneNumber = addLabel("Enter Phone Number", this.phoneNumberField.getX(), this.phoneNumberField.getY() - TEXT_FIELD_HEIGHT*5/6, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        this.textField = addTextField(this.getWidth() * 2 / 3 - TEXT_FIELD_WIDTH / 2, this.getHeight() / 2 - TEXT_FIELD_HEIGHT / 2, TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT);
        JLabel textField=addLabel("Enter Your Text",this.textField.getX(),this.phoneNumberField.getY() - TEXT_FIELD_HEIGHT*5/6,TEXT_FIELD_WIDTH,TEXT_FIELD_HEIGHT);
        JLabel welcomeSign = addLabel("Hey! Welcome to our whatsappBot", this.phoneNumberField.getX(), this.getHeight()/4, WELCOME_MASSAGE_WIDTH, TEXT_FIELD_HEIGHT);

    }
    private boolean noNull(String s){
        return s.equals("");

    }
    private boolean validPhoneNumber(String phoneNumber){
        boolean valid=true;
        if (phoneNumber.length()!=10){
            valid=false;
        }else if (phoneNumber.charAt(0)!='0'||phoneNumber.charAt(1)!='5'){
            valid=false;
        }else {
            for (int i = 0; i <phoneNumber.length() ; i++) {
                if (!Character.isDigit(phoneNumber.charAt(i))){
                    valid=false;
                }
            }
        }
        return valid;
    }

    private void openWhatsAppWeb() {
        this.openWhatsApp = addButton("Open WhatsApp", this.getWidth() / 2 - OPEN_BUTTON_WIDTH / 2, OPEN_BUTTON_Y, OPEN_BUTTON_WIDTH, OPEN_BUTTON_HEIGHT);
        this.openWhatsApp.addActionListener((event) -> {

            if (noNull(this.phoneNumberField.getText())||noNull(this.textField.getText())){
                printError("Fill in the empty field");
            }else if (!validPhoneNumber(this.phoneNumberField.getText())){
                printError("Invalid phone number");
            }else {
                userConnect();
            }
            repaint();
        });
    }

    private void initialDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromeDriver\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("user-data-dir=C:\\Users\\DELL\\AppData\\Local\\Google\\Chrome\\User Data\\Default");
        this.driver = new ChromeDriver(chromeOptions);
        this.driver.manage().window().maximize();
    }


    private void printError(String print) {
        new Thread(() -> {
            this.error = addLabel(print, this.getWidth() / 2 - ERROR_PRINT_WIDTH / 2, this.openWhatsApp.getY()-ERROR_PRINT_HEIGHT, ERROR_PRINT_WIDTH, ERROR_PRINT_HEIGHT);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.error.setVisible(false);
        }).start();
    }

    private void userConnect() {
        new Thread(() -> {
            this.openWhatsApp.setVisible(false);
        }).start();
        initialDriver();
        new Thread(() -> {
            connection();
        }).start();
        enterChat();
        sendMessage();
        //temp();
    }
//    private void temp(){
//        try {
//            WebElement elements=this.driver.findElement(By.xpath("/html/body/div[1]/div/div/div[4]/div/div[3]/div/div[2]/div[2]"));
//            System.out.println(elements.getText());
//            List<WebElement> elementList=elements.findElements(By.tagName("div"));
//            System.out.println(elementList.get(0).getText());
//        }catch (Exception e){
//            temp();
//        }
//
//    }

    private void connection() {
        WebElement element = null;
        try {
            element = this.driver.findElement(By.id("side"));
            printConnection();
        } catch (Exception e) {
            connection();
        }
    }

    private void printConnection() {
        new Thread(() -> {
            this.connectionSucceed = addLabel("connection succeeded", this.getWidth() / 2 - CONNECTION_PRINT_WIDTH / 2, this.getHeight() * 2 / 3 - CONNECTION_PRINT_HEIGHT / 2, CONNECTION_PRINT_WIDTH, CONNECTION_PRINT_HEIGHT);
            repaint();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.connectionSucceed.setVisible(false);
        }).start();
    }

    private void enterChat() {
        this.driver.get("https://api.whatsapp.com/send?phone=972" + this.phoneNumberField.getText().substring(1));
        WebElement enterChatButton = this.driver.findElement(By.id("action-button"));
        enterChatButton.click();
        WebElement mainBlock = this.driver.findElement(By.id("fallback_block"));
        List<WebElement> linkElement = mainBlock.findElements(By.tagName("a"));
        String linkToChat = linkElement.get(1).getAttribute("href");
        this.driver.get(linkToChat);
    }

    private void sendMessage() {
        new Thread(() -> {
            WebElement footerTextBox = null;
            try {
                footerTextBox = this.driver.findElement(By.tagName("footer"));
                Thread.sleep(3000);
                WebElement textBox = footerTextBox.findElement(By.cssSelector("div[role='textbox']"));
                textBox.sendKeys(this.textField.getText());
                footerTextBox.findElement(By.cssSelector("button[class='tvf2evcx oq44ahr5 lb5m6g5c svlsagor p2rjqpw5 epia9gcq']")).click();
                JLabel messageSent = addLabel("Message Sent successfully", this.getWidth()/2-SEND_PRINT_WIDTH/2, this.getHeight()*4/5-SEND_PRINT_HEIGHT/2, SEND_PRINT_WIDTH, SEND_PRINT_HEIGHT);
                repaint();
                Thread.sleep(3000);
                messageSent.setVisible(false);
            } catch (Exception e) {
                sendMessage();
            }
        }).start();
    }


    private JButton addButton(String buttonText, int x, int y, int width, int height) {
        JButton button = new JButton(buttonText);
        Font font = new Font(KIND_OF_FONT, Font.BOLD, FONT_SIZE_BUTTON);
        button.setBounds(x, y, width, height);
        button.setVisible(true);
        button.setFont(font);
        this.add(button);
        return button;
    }

    public JLabel addLabel(String labelText, int x, int y, int width, int height) {
        JLabel label = new JLabel(labelText);
        label.setForeground(WHITE);
        Font font = new Font(KIND_OF_FONT, Font.BOLD, SIZE_FONT_LABEL);
        label.setFont(font);
        label.setBounds(x, y, width, height);
        this.add(label);
        return label;
    }

    public JTextField addTextField(int x, int y, int width, int height) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        Font font = new Font(KIND_OF_FONT, Font.BOLD, SIZE_FONT_LABEL);
        textField.setFont(font);
        this.add(textField);
        return textField;
    }
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(this.backGround.getImage(), this.getX(), this.getY(), this.getWidth(), this.getHeight(), null);
    }

}
