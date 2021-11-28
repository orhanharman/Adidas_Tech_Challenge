package com.adidas.tests;

import com.adidas.utilities.WebDriverFactory;
import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;

public class TechChallengeTask {
    /*
    1- Customer navigation through product categories: Phones, Laptops and Monitors
    2- Navigate to "Laptop" → "Sony vaio i5" and click on "Add to cart". Accept pop up confirmation.
    3- Navigate to "Laptop" → "Dell i7 8gb" and click on "Add to cart". Accept pop up confirmation.
    4 -Navigate to "Cart" → Delete "Dell i7 8gb" from cart.
    5- Click on "Place order".
    6- Fill in all web form fields.
    7- Click on "Purchase"
    8- Capture and log purchase Id and Amount.
    9- Assert purchase amount equals expected.
    10- Click on "Ok"
    */

    WebDriver driver;
    Faker faker = new Faker();

    @BeforeMethod
    public void setUp() {
        driver = WebDriverFactory.getDriver("chrome");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://www.demoblaze.com/index.html");
    }

    @AfterMethod
    public void tearDown() throws InterruptedException {
        Thread.sleep(1000);
        driver.quit();
    }

    @Test
    public void TC1() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, 10);

        //1- Customer navigation through product categories: Phones, Laptops and Monitors
        //2- Navigate to "Laptop" → "Sony vaio i5" and click on "Add to cart". Accept pop up confirmation.

        driver.findElement(By.xpath("//*[@onclick=\"byCat('notebook')\"]")).click();
        driver.findElement(By.xpath("//a[.='Sony vaio i5']")).click();

        WebElement addToCart = driver.findElement(By.xpath("//a[.='Add to cart']"));
        wait.until(ExpectedConditions.elementToBeClickable(addToCart));
        addToCart.click();

        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        alert.accept();

        //3- Navigate to "Laptop" → "Dell i7 8gb" and click on "Add to cart". Accept pop up confirmation.
        driver.findElement(By.cssSelector(".navbar-brand")).click();
        driver.navigate().refresh();
        driver.findElement(By.xpath("//*[@onclick=\"byCat('notebook')\"]")).click();

        driver.findElement(By.xpath("//*[contains(text(),'Sony vaio i7')]")).click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[.='Add to cart']"))));
        driver.findElement(By.xpath("//a[.='Add to cart']")).click();

        wait.until(ExpectedConditions.alertIsPresent());
        alert = driver.switchTo().alert();
        alert.accept();

        //4 -Navigate to "Cart" → Delete "Dell i7 8gb" from cart.
        driver.findElement(By.xpath("//*[text()='Cart']")).click();
        driver.findElement(By.xpath("(//td[contains(text(),'Sony vaio i7')]/./following-sibling::td)[2]/a")).click();
        wait.until(ExpectedConditions.invisibilityOf(driver.findElement(By.xpath("//tbody[@id='tbodyid']//td[contains(text(),'Sony vaio i7')]"))));

        String ExpectedVaioI5Price = driver.findElement(By.xpath("(//*[@class='success']//td)[3]")).getText();  //790

        //5- Click on "Place order".
        driver.findElement(By.xpath("//*[.='Place Order']")).click();

        //6- Fill in all web form fields.
        Thread.sleep(2000);
        driver.findElement(By.cssSelector("#name")).sendKeys(faker.name().firstName());
        driver.findElement(By.cssSelector("#country")).sendKeys(faker.country().name());
        driver.findElement(By.cssSelector("#city")).sendKeys(faker.country().capital());
        driver.findElement(By.cssSelector("#card")).sendKeys(faker.finance().creditCard(CreditCardType.MASTERCARD));
        driver.findElement(By.cssSelector("#month")).sendKeys("" + faker.number().randomDigitNotZero());
        driver.findElement(By.cssSelector("#year")).sendKeys("2022");

        //7- Click on "Purchase"
        driver.findElement(By.xpath("//*[.='Purchase']")).click();

        //8- Capture and log purchase Id and Amount.
        String purchaseLog = driver.findElement(By.xpath("//p[@class='lead text-muted ']")).getText();
        System.out.println("purchaseLog = " + purchaseLog);
        String purchaseID = purchaseLog.substring((purchaseLog.indexOf(" ")+1),purchaseLog.indexOf("\n"));
        purchaseLog = purchaseLog.substring(purchaseLog.indexOf("Amount: "));
        String ActualVaioI5Price = purchaseLog.substring((purchaseLog.indexOf(" ")+1),purchaseLog.indexOf(" USD"));

        System.out.println("purchaseID = " + purchaseID);
        System.out.println("length of purchase ID = " + purchaseID.length());
        System.out.println("ExpectedVaioI5Price = " + ExpectedVaioI5Price);
        System.out.println("ActualVaioI5Price = " + ActualVaioI5Price);

        //9- Assert purchase amount equals expected.
        Assert.assertEquals(ActualVaioI5Price, ExpectedVaioI5Price, "Verify the i5 prices are same");

        //10- Click on "Ok"
        driver.findElement(By.xpath("//*[.='OK']")).click();

    }

}
