import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class Tests {
    //Shortcut method for FAST create new WebDriver.
    public static WebDriver setup(){
        System.setProperty("webdriver.chrome.driver","src\\main\\resources\\chromedriver.exe");
        return new ChromeDriver();
    }

    @Test
    public void testSignUp(){
        WebDriver webDriver = setup(); //method for FAST create.
        //Opens the Web page according to the given URL
        webDriver.get("http://tutorialsninja.com/demo/index.php?route=account/register");

        //finds all of the Required Elements in the page. (TextBoxes, Buttons etc.)
        WebElement firstName = webDriver.findElement(By.id("input-firstname"));
        WebElement lastName = webDriver.findElement(By.id("input-lastname"));
        WebElement eMail = webDriver.findElement(By.id("input-email"));
        WebElement phoneNum = webDriver.findElement(By.id("input-telephone"));
        WebElement passWord = webDriver.findElement(By.id("input-password"));
        WebElement passWordC = webDriver.findElement(By.id("input-confirm"));
        WebElement privacyC = webDriver.findElement(By.name("agree"));
        WebElement continueBtn = webDriver.findElement(By.xpath("//*[@id=\"content\"]/form/div/div/input[2]"));

        //Select 1 of the 2 opt RadioBtn


        firstName.sendKeys("");
        lastName.sendKeys("");
        eMail.sendKeys("");
        phoneNum.sendKeys("");
        passWord.sendKeys("");
        passWordC.sendKeys("");
        privacyC.click();
        continueBtn.click();


        Assertions.assertEquals("http://tutorialsninja.com/demo/index.php?route=account/success",webDriver.getCurrentUrl());

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

