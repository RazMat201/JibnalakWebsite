package MyTests;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MainTestCases {
	WebDriver driver = new ChromeDriver();
	String JebnalakURL = "https://jebnalak.com/";
	Random rand = new Random();
	String ItemTitle;
	boolean IsSortedCorrectly;
	Actions actions = new Actions(driver);
	String PrimaryCartSubTotal;
	boolean ExpectedResultForJibnalakLogo = true;
	boolean ExpectedResultForInfo = true;

	

@BeforeTest
public void SetUp() {

	driver.get(JebnalakURL);

	driver.manage().window().maximize();
}

@Test(priority=1)
public void HomepageAccessibility() throws InterruptedException {
	
	
	JavascriptExecutor js = (JavascriptExecutor) driver;
	String State = (String) js.executeScript("return document.readyState");
	System.out.println(State);
	Assert.assertEquals(State, "complete");
	
	JavascriptExecutor js1 = (JavascriptExecutor) driver;
	js1.executeScript("window.scrollTo(0, 1100);");
	Thread.sleep(3000);
	WebElement topMenu = driver.findElement(By.cssSelector("#top-menu"));
	WebElement featuredProducts = driver.findElement(By.cssSelector("#ishiproduct-block-carousel"));

	boolean topMenuVisible = topMenu.isDisplayed();
	boolean featuredVisible = featuredProducts.isDisplayed();

	Assert.assertTrue(topMenuVisible, "Top menu is not visible on the homepage.");
	Assert.assertTrue(featuredVisible, "Featured products section is not visible on the homepage.");


}


@Test(priority=2,enabled=true) //Test Case ID: TC_JEB_004
public void ProductSearchFunctionality() throws InterruptedException {
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("window.scrollTo(0, 0);");
	WebElement SearchBar = driver.findElement(By.cssSelector(".search-header__input.search__input"));
	String[] Products = { "bread","Shampoo","coffee"};
	
	int RandomProductIndex = rand.nextInt(Products.length);
	String ChosenProduct = Products[RandomProductIndex];
	String CapitalizedProductName = ChosenProduct.substring(0, 1).toUpperCase() + ChosenProduct.substring(1).toLowerCase();

    SearchBar.sendKeys(ChosenProduct);
	Thread.sleep(2000);


	driver.findElement(By.cssSelector(".search-header__submit.search__submit.btn--link")).click();
     Thread.sleep(5);

    List<WebElement> results = driver.findElements(By.cssSelector(".product-description.col-sm-8.col-md-8.col-lg-9"));
    for (int i = 0; i < results.size(); i++) {
        String ItemTitle = results.get(i).getText();
        boolean ProductAssertion = ItemTitle.contains(CapitalizedProductName);

        if   (!ItemTitle.contains(CapitalizedProductName))  {
        	System.out.print(("No Matched product"));
        }
        
        Assert.assertEquals(true, ProductAssertion, "No matched items : Search result does not contain" + ChosenProduct);

    
        }}
@Test(priority=3,enabled=true)
public void ViewingProductDetails() throws InterruptedException {
	List<WebElement> productsList = driver.findElements(By.cssSelector(".normal_main_content.col-md-9.col-sm-12.col-xs-12 .grid-view-item__image.lazyload"));

	Random rand = new Random();
	int randomIndex = rand.nextInt(productsList.size());
	WebElement randomProduct = productsList.get(randomIndex);
	randomProduct.click();
	Thread.sleep(5000);
	List<WebElement> ProductDetails = new ArrayList<>();

	ProductDetails.add(driver.findElement(By.cssSelector(".col-sm-6.product-single__photos.product-single-left"))); //pic
	ProductDetails.add(driver.findElement(By.id("ProductPrice-product-template"))); //price
	ProductDetails.add(driver.findElement(By.cssSelector(".product-single__title.hidden-sm-down"))); //title
	ProductDetails.add(driver.findElement(By.id("desc"))); //description
	Thread.sleep(5000);

	List<String> detailNames = new ArrayList<>();
	detailNames.add("Product Image");
	detailNames.add("Product Price");
	detailNames.add("Product Title");
	detailNames.add("Product Description");

    Thread.sleep(3000);

    for (int i = 0; i < ProductDetails.size(); i++) {
        WebElement field = ProductDetails.get(i);
        Assert.assertTrue(field.isDisplayed(), detailNames.get(i) + " is missing or not displayed.");
    }
	} 
@Test(priority=4,enabled=true)
public void PrivacyPolicyAccessibility() throws InterruptedException {
	String ExpectedTitle = "Policies";
String PloiciesURL= "https://jebnalak.com/pages/policies";
WebElement ViewMoreOption = driver.findElement(By.className("viewmore"));
Actions actions = new Actions(driver);
actions.moveToElement(ViewMoreOption).perform();
Thread.sleep(3000);
driver.findElement(By.linkText("Policies")).click();
String CurrentURL = driver.getCurrentUrl();
Assert.assertEquals(PloiciesURL, CurrentURL, "You're not in the ploicies page");
WebElement Title = driver.findElement(By.cssSelector(".section-header.text-center"));
String ActualTitle = Title.getText();
	Assert.assertEquals(ExpectedTitle, ActualTitle);
	
	String PageText = driver.findElement(By.cssSelector(".grid__item.medium-up--five-sixths.medium-up--push-one-twelfth")).getText().toLowerCase();
	Assert.assertTrue(PageText.contains("privacy policy:") || PageText.contains("delivery policy:") || PageText.contains("refund policy"), "No policy info found");

	
}


@Test(priority=5,enabled=true) //Test Case ID: TC_JEB_005
public void FilteringSearchResults() throws InterruptedException {
	
	/*List<WebElement> DropDownItems = driver.findElements(By.cssSelector("a.dropdown-item"));
	int RandomDropDownItemIndex = rand.nextInt(DropDownItems.size());
    WebElement ChosenDropDownItem = DropDownItems.get(RandomDropDownItemIndex);
    ChosenDropDownItem.click();
    Thread.sleep(3000);*/
	String URL = "https://jebnalak.com/collections/fresh-poultry";
	driver.get(URL);
    WebElement SortingOptions = driver.findElement(By.id("SortBy"));
    Select dropdown = new Select(SortingOptions);
    dropdown.selectByValue("price-ascending");
    WebElement Productscontainer = driver.findElement(By.cssSelector(".products-display.products-display-collection.grid.grid--uniform.grid--view-items"));

    List<WebElement> priceElements = Productscontainer.findElements(By.cssSelector(".original.is-bold.qv-regularprice"));
    for (int i = 0; i < priceElements.size() - 1; i++) {
        WebElement FirstpriceElement = priceElements.get(i);
        WebElement SecondpriceElement = priceElements.get(i + 1);

        String priceText1 = FirstpriceElement.getText().replaceAll("[^\\d.]", "");
        String priceText2 = SecondpriceElement.getText().replaceAll("[^\\d.]", "");


        int price1 = (int) Double.parseDouble(priceText1); 
        int price2 = (int) Double.parseDouble(priceText2);

        if (price1 > price2) {
            System.out.println("Prices are not in ascending order");
             break;
             
        } else {
             IsSortedCorrectly = true;

            System.out.println("Prices are in ascending order");
        } 
    }
        Assert.assertEquals(IsSortedCorrectly, true);
    } 
@Test(priority=6,enabled=true) //Test Case ID: TC_JEB_007
public void AddingProductstoCart() throws InterruptedException {
    // Hover over Back to School menu
    WebElement MenuElement = driver.findElement(By.cssSelector("a[href='/collections/back-to-school']"));
    actions.moveToElement(MenuElement).perform();
    Thread.sleep(1000);
    System.out.println("Hovered over: " + MenuElement.getText());

    // Click on "Colors & Accessories"
    WebElement Choice = driver.findElement(By.linkText("Colors & Accessories"));
    Choice.click();
    Thread.sleep(3000);

    List<String> addedProducts = new ArrayList<>();
    int itemsToAdd = 3;

    while (addedProducts.size() < itemsToAdd) {
        // Get updated content area and sub-items
        WebElement mainContent = driver.findElement(By.cssSelector(".col-md-9.col-sm-12.col-xs-12.normal_main_content"));
        List<WebElement> SubItems = mainContent.findElements(By.cssSelector(".grid-view-item__image.lazyload"));

        int RandomSubItemIndex = rand.nextInt(SubItems.size());
        WebElement RandomChosenItem = SubItems.get(RandomSubItemIndex);

        // Check if item is sold out
        List<WebElement> overlay = RandomChosenItem.findElements(By.className("outstock-overlay"));
        if (!overlay.isEmpty()) {
            System.out.println("Item is SOLD OUT. Trying another...");
            continue;
        }

        // Click the item
        RandomChosenItem.click();
        Thread.sleep(3000);

        // Add to cart
        driver.findElement(By.id("AddToCartText-product-template")).click();
        Thread.sleep(2000);

        // Wait for success message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement notification = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".col-xs-12.alert.alert-success.animated.fadeOutUp")));
        String message = notification.getText();
        System.out.println(message);
        Assert.assertTrue(notification.isDisplayed(), "Success message did not appear.");
        Thread.sleep(2000);

        // Get and store product name
        String ProductName = driver.findElement(By.cssSelector(".product-single__title.hidden-sm-down")).getText().toLowerCase();
        System.out.println("Added Product: " + ProductName);
        addedProducts.add(ProductName);

        // Reopen the collection page instead of navigating back
        MenuElement = driver.findElement(By.cssSelector("a[href='/collections/snacks-candy']"));
        actions.moveToElement(MenuElement).perform();
        Thread.sleep(1000);
        Choice = driver.findElement(By.linkText("Chocolates"));
        Choice.click();
        Thread.sleep(3000);
    } 

    // Go to Cart
    WebElement Cart = driver.findElement(By.cssSelector(".main-title"));
    Cart.click();
    Thread.sleep(5000);

    PrimaryCartSubTotal = driver.findElement(By.cssSelector(".cart__footer .cart__subtotal")).getText();
    System.out.print("$$$$$ " + PrimaryCartSubTotal);
System.out.println();

    // Get cart items
    List<WebElement> cartItems = driver.findElements(By.className("product-title"));
    List<String> cartProductNames = new ArrayList<>();
    for (int i = 0; i < cartItems.size(); i++) {
        cartProductNames.add(cartItems.get(i).getText().toLowerCase());
    }

    for (int i = 0; i < addedProducts.size(); i++) {
        String added = addedProducts.get(i);
        System.out.print(added);
        System.out.println();
        Assert.assertTrue(cartProductNames.contains(added), "Cart does not contain added product: " + added);
    }
    Thread.sleep(3000);

    System.out.println("All selected products successfully added to cart.");

}
@Test(priority=7,enabled=true) //Test Case ID: TC_JEB_008
public void ViewingandModifyingtheShoppingCart() throws InterruptedException {

    driver.findElement(By.cssSelector(".view-cart.btn")).click();

    // Get list of quantity input fields
    List<WebElement> QuantityInput = driver.findElements(By.cssSelector(".cart__qty-input.quantity.update-cart"));
    for (int i = 0; i < QuantityInput.size(); i++) {
        int randomNumber = rand.nextInt(2, 6);

        QuantityInput = driver.findElements(By.cssSelector(".cart__qty-input.quantity.update-cart"));
        WebElement input = QuantityInput.get(i);
        input.clear();
        input.sendKeys(String.valueOf(randomNumber));
        
        driver.findElement(By.cssSelector(".btn.btn--secondary.update-qty.cart__update.cart__update--large")).click();
        Thread.sleep(3000);
    }

    // Now randomly remove one item
    List<WebElement> removeButtons = driver.findElements(By.cssSelector(".fa.fa-trash-o"));
    if (!removeButtons.isEmpty()) {
        int removeIndex = rand.nextInt(removeButtons.size());
        
        // Re-fetch remove buttons to avoid staleness
        removeButtons = driver.findElements(By.cssSelector(".fa.fa-trash-o"));
        removeButtons.get(removeIndex).click();
        System.out.println("Removed item at index: " + removeIndex);
    } else {
        System.out.println("No items found to remove.");
    }
    Thread.sleep(5000);

    String FinalCartSubTotal = driver.findElement(By.cssSelector(".cart-bottm .cart__subtotal")).getText();
    System.out.print("$$$$$ " +FinalCartSubTotal);
    System.out.println();

    Thread.sleep(5000);
    if (PrimaryCartSubTotal != FinalCartSubTotal) {
    	Assert.assertNotEquals(PrimaryCartSubTotal, FinalCartSubTotal, "Cart subtotal did not change after updating quantities.");
    }
} 
@Test(priority=8,enabled=true) //Test Case ID: TC_JEB_009
public void  ProceedingtoCheckout() throws InterruptedException {
driver.findElement(By.cssSelector(".btn.btn--small-wide.checkout_btn")).click();
Thread.sleep(5000);
String CurrentURL = driver.getCurrentUrl();
Assert.assertTrue(CurrentURL.contains("checkouts"), "Current URL does not contain 'checkouts', you might be in the wrong page");
List<WebElement> checkoutFields = new ArrayList<>();

checkoutFields.add(driver.findElement(By.id("contact")));
checkoutFields.add(driver.findElement(By.id("deliveryAddress")));
checkoutFields.add(driver.findElement(By.name("countryCode"))); //country
checkoutFields.add(driver.findElement(By.name("firstName")));
checkoutFields.add(driver.findElement(By.name("lastName")));
checkoutFields.add(driver.findElement(By.name("address1")));
checkoutFields.add(driver.findElement(By.name("address2")));
checkoutFields.add(driver.findElement(By.name("city")));
checkoutFields.add(driver.findElement(By.name("postalCode")));
checkoutFields.add(driver.findElement(By.name("phone")));
checkoutFields.add(driver.findElement(By.id("shippingMethod")));
checkoutFields.add(driver.findElement(By.id("payment")));
checkoutFields.add(driver.findElement((By.xpath("//strong[text()='Total']"))));
Thread.sleep(5000);
for (int i=0 ; i < checkoutFields.size(); i++) {
    WebElement field = checkoutFields.get(i);
    Assert.assertTrue(field.isDisplayed(), "There's a missing field");
}
}
@Test(priority=9,enabled=true)
public void LanguageSwitchFunctionlity() throws InterruptedException {
	driver.navigate().back();
	WebElement Arrow = driver.findElement(By.cssSelector(
		    "div.ly-switcher-wrapper.ly-breakpoint-2.fixed.bottom_right div.ly-arrow.ly-arrow-black.stroke svg"
		));
	Arrow.click();
	
	WebElement ArabicOption = driver.findElement(By.cssSelector(
		    "ul#languagesSwitcherList-2 li[key='ar'] span.ly-custom-dropdown-list-element-right"));
	ArabicOption.click();
	Thread.sleep(5000);

	String CurrentUrl = driver.getCurrentUrl();
	Assert.assertTrue(CurrentUrl.contains("ar"), "URL does not contain 'ar', it did not switch to arabic");
	
	
	List<WebElement> Fields = new ArrayList<>();
	Fields.add(driver.findElement(By.cssSelector(".home-title.hidden-lg-down")));
	Fields.add(driver.findElement(By.cssSelector(".search-header__submit.search__submit.btn--link")));
	Fields.add(driver.findElement(By.cssSelector(".desktop-user-info.text-center.col-md-12.col-sm-12.col-xs-12")));
	for (int i = 0; i < Fields.size(); i++) {
        String text = Fields.get(i).getText();
        
        boolean containsEnglish = text.matches(".*[a-zA-Z]+.*");

        Assert.assertFalse(containsEnglish, "English letters found, language is not Arabic");
	}
	WebElement ArabicArrow = driver.findElement(By.cssSelector(
			  "div.ly-switcher-wrapper.ly-breakpoint-2.fixed.bottom_right div.ly-arrow.ly-arrow-black.stroke svg"
			));

	ArabicArrow.click();

	WebElement EnglishOption = driver.findElement(By.cssSelector("ul[id='languagesSwitcherList-2'] li[key='en'] span.ly-custom-dropdown-list-element-right"));
	EnglishOption.click();
	Thread.sleep(5000);
	List<WebElement> Fields2 = new ArrayList<>();
	Fields2.add(driver.findElement(By.cssSelector(".home-title.hidden-lg-down")));
	Fields2.add(driver.findElement(By.cssSelector(".search-header__submit.search__submit.btn--link")));
	Fields2.add(driver.findElement(By.cssSelector(".desktop-user-info.text-center.col-md-12.col-sm-12.col-xs-12")));
	for (int n = 0; n < Fields2.size(); n++) {
	    String text = Fields2.get(n).getText();
	        boolean containsArabic = text.matches(".*[\\u0600-\\u06FF]+.*");

	        Assert.assertFalse(containsArabic, "Arabic letters found, language is not English");
	    }
	
}
@Test(priority=10,enabled=true)
public void LogoandInfoVisibility() throws InterruptedException {
	
	WebElement Header = driver.findElement(By.cssSelector(".header-top.site-header-inner"));
	boolean ActualResultForLogo = Header.findElement(By.className("header__logo-image")).isDisplayed();

	Assert.assertEquals(ActualResultForLogo, ExpectedResultForJibnalakLogo, "Logo is not dispalyed");
	
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("window.scrollTo(0, 2500);");
    Thread.sleep(2000);
	WebElement Footer = driver.findElement(By.className("footer-container"));
	boolean ActualResultForInfo = Footer.findElement(By.cssSelector(".footer-content.contact-info.col-md-12")).isDisplayed();

	Assert.assertEquals(ActualResultForInfo, ExpectedResultForInfo, "No info displayed");

}

}