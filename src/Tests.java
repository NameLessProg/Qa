import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.v107.browser.model.WindowState;

import java.util.List;

public class Tests {
    //Shortcut method for FAST create new WebDriver.

    public static WebDriver setup(){
        System.setProperty("webdriver.chrome.driver","src\\main\\resources\\chromedriver.exe");
        return new ChromeDriver();
    }

    @Test
    void registrationProcessTest() {
        WebDriver driver = setup();
        driver.get("http://tutorialsninja.com/demo/index.php?route=account/register"); // Get to this site

        var firstName = driver.findElement(By.id("input-firstname"));
        var lastName = driver.findElement(By.id("input-lastname"));
        var eMail = driver.findElement(By.id("input-email"));
        var telephoneNub = driver.findElement(By.id("input-telephone"));
        var passwordNum = driver.findElement(By.id("input-password"));
        var confirmBtn = driver.findElement(By.id("input-confirm"));
        var agreeBtn = driver.findElement(By.name("agree"));
        var continueBtn = driver.findElement(By.xpath("//*[@id=\"content\"]/form/div/div/input[2]"));


        firstName.sendKeys("Idan");                 // find the element "First name" & write this text
        lastName.sendKeys("Azuri");                 // find the element "last name" & write this text
        eMail.sendKeys("idn.az01110@gmail.com");    // find the element "Email" & write this text
        telephoneNub.sendKeys("0545646076");        // find the element "Telephone" & write this text
        passwordNum.sendKeys("123456");             // find the element "First name" & write this text
        confirmBtn.sendKeys("123456");              // find the element "Confirm" & write this text

        agreeBtn.click();                                      // find the element "Agree" & write this text
        continueBtn.click();                                   // find the element by xpath & click

        // Check if this is the correct and exact address of the site by matching.
        Assertions.assertEquals("http://tutorialsninja.com/demo/index.php?route=account/success" , driver.getCurrentUrl());
    }

    @Test
    void loginProcessTest() {
        WebDriver driver = setup(); // Call to the Webdriver by "driver" var
        driver.get("http://tutorialsninja.com/demo/index.php?route=account/login"); // Get to this site

        var eMail2 = driver.findElement(By.id("input-email"));
        var password2 = driver.findElement(By.id("input-password"));
        var loginBtn = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/form/input"));

        eMail2.sendKeys("idn.az010155@gmail.com"); // find the element "Email" & write this text
        password2.sendKeys("123456"); // find the element "Password" & write this text
        loginBtn.click();  // find the element by xpath & click

        {
            List<WebElement> elements = driver.findElements(By.xpath("//*[@id=\"account-login\"]/div[1]"));
            Assertions.assertTrue(elements.isEmpty(), "Login failed, Un-existing User");
        }

        // Check if this is the correct and exact address of the site by matching.
        Assertions.assertEquals("http://tutorialsninja.com/demo/index.php?route=account/login" , driver.getCurrentUrl());
    }


    @Test
    public void testAddToCart(){
        WebDriver webDriver =   setup();
        webDriver.get("http://tutorialsninja.com/demo/index.php?route=product/product&product_id=43"); //Go to Product1 page

        //Adding the product to the cart
        webDriver.findElement(By.id("button-cart")).click();
        //Extracting the "Price" from the product and inserting it into variable
        //Extracted text contains Signs (!, @, $ ..) & CANNOT be calculated ->> Replacing Sign With 0
        double pr1 = text2Double(webDriver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/ul[2]/li[1]/h2")));


        //Goes to Product2 page
        webDriver.get("http://tutorialsninja.com/demo/index.php?route=product/product&product_id=33");

        webDriver.findElement(By.id("button-cart")).click(); //Adding to cart

        //Extracting the "Price" from the product and inserting it into variable
        double pr2 = text2Double(webDriver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/ul[2]/li[1]/h2")));

        webDriver.findElement(By.cssSelector("#top-links > ul > li:nth-child(4) > a > i")).click(); //clicking the Shopping Cart

        //Using method to extract the number without the symbol "$"
        double subTotal = text2Double(webDriver.findElement(By.xpath("//*[@id=\"content\"]/div[2]/div/table/tbody/tr[4]/td[2]")));

        Assertions.assertEquals(pr1+pr2,subTotal); //Asserting that product 1 price + product 2 price are EQUALS to the subtotal given
    }

    public double text2Double(WebElement webElement){
        //Extracted text contains Signs (!, @, $ ..) & CANNOT be calculated ->> Replacing Sign With 0
        double converText = Double.parseDouble(webElement.getText().replace('$','0'));
        return converText;
    }

    @Test
    public void testReview() {
        WebDriver webDriver =setup();

        webDriver.get("http://tutorialsninja.com/demo/index.php?route=product/product&product_id=43");

        webDriver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/ul[2]/li[3]/a")).click();

        webDriver.findElement(By.cssSelector("#content > div > div.col-sm-4 > div.rating > p > a:nth-child(7)")).click();

        //Config elements

        webDriver.findElement(By.id("input-name")).sendKeys("LEL");
        //Review MUST be 20 Chars AT-LEAST!
        char review = 'a';

        for (int i = 0; i < 20; i++) {
        webDriver.findElement(By.id("input-review")).sendKeys(review+""+i);
        }
        //Mid-score review
        List<WebElement> reviewScore = webDriver.findElements(By.name("rating"));
        int index = reviewScore.size()/2;
        reviewScore.get(index).click();

        webDriver.findElement(By.id("button-review")).click();

        //Finds the ERROR-Exclamation mark message & gets it in the ARRAY
        List<WebElement> reviewSubmitChecker = webDriver.findElements(By.cssSelector("#form-review > div.alert.alert-danger.alert-dismissible > i"));

        /* Array is EMPTY if the review has sent
        *   in case of array is not empty (Contains Element)
        *       it means the review wasn't sent and the Test has failed
        * */
        Assertions.assertTrue(reviewSubmitChecker.isEmpty(),"Review wasn't delivered");



    }
}

