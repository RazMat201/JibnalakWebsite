package MyTests;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class MainTestCases extends TestData {
	WebDriver driver = new ChromeDriver();
	Random rand = new Random();
	Actions actions = new Actions(driver);
	 Connection connection;
	    Statement statement;
	    ResultSet resultSet;
	

@BeforeTest
public void SetUp() throws SQLException {

	driver.get(JebnalakURL);

	driver.manage().window().maximize();
	  connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/jibnalakdatabase","root","12345"); // to connect to database

}

@Test(priority=1,enabled=true) //TC_JEB_001
public void HomepageAccessibility() throws InterruptedException {
	//Here I used two methods to check this test
	// 1- Check page state if it's completely loaded
	
	JavascriptExecutor js = (JavascriptExecutor) driver;
	String State = (String) js.executeScript("return document.readyState"); //page state if it's fully loaded
	System.out.println(State);
	Assert.assertEquals(State, "complete");
	// 2- Check if featured products and menus are displayed successfully.
	JavascriptExecutor js1 = (JavascriptExecutor) driver;
	js1.executeScript("window.scrollTo(0, 1100);"); // to scroll down till  featured products are shown to the user.
	Thread.sleep(3000);
	WebElement topMenu = driver.findElement(By.cssSelector("#top-menu")); //For menu
	WebElement featuredProducts = driver.findElement(By.cssSelector("#ishiproduct-block-carousel")); // For featured products

	boolean topMenuVisible = topMenu.isDisplayed();
	boolean featuredVisible = featuredProducts.isDisplayed();
    //Assertion for both :
	Assert.assertTrue(topMenuVisible, "Top menu is not visible on the homepage.");
	Assert.assertTrue(featuredVisible, "Featured products section is not visible on the homepage.");

}


@Test(priority=2,enabled=true) //Test Case ID: TC_JEB_002
public void ProductSearchFunctionality() throws InterruptedException, SQLException {
	// In this test I did product search using database.
	    
	JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("window.scrollTo(0, 0);");
	    WebElement SearchBar = driver.findElement(By.cssSelector(".search-header__input.search__input")); // reach search bar
       
	    // below : I inserted another row into database so I could choose randomly also from it.
	    
	    String insertQuery = "INSERT INTO products (product_name, category_name, brand_name) VALUES " +
	                         "('coffee', 'Coffee & Tea', 'Alameed')";
	    statement = connection.createStatement(); // to execute queries
	    int rowInserted = statement.executeUpdate(insertQuery); //  execute th query and get affected rows number
	    System.out.println("Rows inserted: " + rowInserted);  
	    
//below I made two lists to get data from database  
	    List<String> productNames = new ArrayList<>();
	    List<String> brands = new ArrayList<>();

	    Statement selectStmt = connection.createStatement(); // to execute query
	    ResultSet rs = selectStmt.executeQuery("SELECT product_name, brand_name FROM products"); // to retrieve data

	    while (rs.next()) {
	        productNames.add(rs.getString("product_name")); //add product name
	        brands.add(rs.getString("brand_name")); // add brand name
	    }
	   // A list that contains two lists to choose randomly from
	    List<List<String>> allLists = new ArrayList<>();
	    allLists.add(productNames);
	    allLists.add(brands);

	    Random rand = new Random();
	    List<String> chosenList = allLists.get(rand.nextInt(allLists.size()));
	    String chosenItem = chosenList.get(rand.nextInt(chosenList.size()));
	    String LowerCaseItem = chosenItem.toLowerCase().replaceAll(" ", "");
	    SearchBar.sendKeys(chosenItem);
	    Thread.sleep(2000);
	    driver.findElement(By.cssSelector(".search-header__submit.search__submit.btn--link")).click(); //search button
	    Thread.sleep(5000);
	    
	  

	    List<WebElement> results = driver.findElements(By.cssSelector(".product-description.col-sm-8.col-md-8.col-lg-9"));
	    for (int i = 0; i < results.size(); i++) {
	        String ItemTitle = results.get(i).getText().toLowerCase();
	        boolean ProductAssertion = ItemTitle.contains(LowerCaseItem.replaceAll(" ", "")) || ItemTitle.contains(LowerCaseItem.substring(0, 2).replaceAll(" ", ""));

	        if (!ProductAssertion) { //if a product does not contain the search word
	            System.out.print(ItemTitle +" should not be shown in results");
	            System.out.println();
	        }

	        //Assert.assertEquals(ProductAssertion, true, "Not all search results contain " + chosenItem); // Assertion that every product is related to the search word
	    }
	    //below is A query to delete the inserted row so we could insert it again in the coming runs.

	    String deleteQuery = "DELETE FROM products WHERE product_name = 'coffee' AND brand_name = 'Alameed' AND category_name = 'Coffee & Tea'";
	    Statement deleteStmt = connection.createStatement();
	    deleteStmt.executeUpdate(deleteQuery);
	}
 
@Test(priority=3,enabled=true) //Test Case ID: TC_JEB_003
public void ViewingProductDetails() throws InterruptedException {
	//This test is to make sure that we can view product details successfully
	List<WebElement> productsList = driver.findElements(By.cssSelector(".normal_main_content.col-md-9.col-sm-12.col-xs-12 .grid-view-item__image.lazyload")); //get all products

	Random rand = new Random();
	int randomIndex = rand.nextInt(productsList.size());
	WebElement randomProduct = productsList.get(randomIndex); //choose a random product from the list
	randomProduct.click();
	Thread.sleep(5000);
	List<WebElement> ProductDetails = new ArrayList<>(); // A list that contains details like pic,price and title

	ProductDetails.add(driver.findElement(By.cssSelector(".col-sm-6.product-single__photos.product-single-left"))); //pic
	ProductDetails.add(driver.findElement(By.id("ProductPrice-product-template"))); //price
	ProductDetails.add(driver.findElement(By.cssSelector(".product-single__title.hidden-sm-down"))); //title
	ProductDetails.add(driver.findElement(By.id("desc"))); //description
	Thread.sleep(5000);

	List<String> detailNames = new ArrayList<>(); 
	//Adding them to A list
	detailNames.add("Product Image");
	detailNames.add("Product Price");
	detailNames.add("Product Title");
	detailNames.add("Product Description");

    Thread.sleep(3000);

    for (int i = 0; i < ProductDetails.size(); i++) { //To check that every detail is displayed
        WebElement field = ProductDetails.get(i);
        Assert.assertTrue(field.isDisplayed(), detailNames.get(i) + " is missing or not displayed."); 
    }
	} 
@Test(priority=4,enabled=true) //Test Case ID: TC_JEB_004
public void PrivacyPolicyAccessibility() throws InterruptedException {

WebElement ViewMoreOption = driver.findElement(By.className("viewmore"));
Actions actions = new Actions(driver);
actions.moveToElement(ViewMoreOption).perform(); //mouse hover for "view more"
Thread.sleep(5000);
driver.findElement(By.linkText("Policies")).click();
String CurrentURL = driver.getCurrentUrl(); //check URL
Assert.assertEquals(CurrentURL, PloiciesURL, "You're not in the ploicies page");

WebElement Title = driver.findElement(By.cssSelector(".section-header.text-center"));
String ActualTitle = Title.getText();
	Assert.assertEquals(ActualTitle, ExpectedTitle); //Check page title

	
	String PageText = driver.findElement(By.cssSelector(".grid__item.medium-up--five-sixths.medium-up--push-one-twelfth")).getText().toLowerCase(); //page content
	Assert.assertTrue(PageText.contains("privacy policy:") || PageText.contains("delivery policy:") || PageText.contains("refund policy"), "No policy info found");

	
}


@Test(priority=5,enabled=true) //Test Case ID: TC_JEB_005
public void FilteringSearchResults() throws InterruptedException {
	
	WebElement topMenu = driver.findElement(By.cssSelector(".top-menu.dropdown-menu")); //get categories menu container
	List<WebElement> categoryItems = topMenu.findElements(By.className("title")); // categories
	 //skip the first two items (index 0 and 1)
    List<WebElement> validCategories = categoryItems.subList(2, categoryItems.size());
	int RandomDropDownItemIndex = rand.nextInt(validCategories.size());
    WebElement ChosenDropDownItem = validCategories.get(RandomDropDownItemIndex); //choose random category
    ChosenDropDownItem.click();
    Thread.sleep(6000);

    WebElement SortingOptions = driver.findElement(By.cssSelector(".filters-toolbar__input.filters-toolbar__input--sort"));
    Select dropdown = new Select(SortingOptions);
    dropdown.selectByValue("price-ascending"); //sort "low to high"
    WebElement Productscontainer = driver.findElement(By.cssSelector(".products-display.products-display-collection.grid.grid--uniform.grid--view-items")); //categories container

    List<WebElement> priceElements = Productscontainer.findElements(By.cssSelector(".original.is-bold.qv-regularprice")); //sub items prices
    for (int i = 0; i < priceElements.size() - 1; i++) {
        WebElement FirstpriceElement = priceElements.get(i); //Current element price
        WebElement SecondpriceElement = priceElements.get(i + 1); // Next element price

        String priceText1 = FirstpriceElement.getText().replaceAll("[^\\d.]", ""); //get only numbers
        String priceText2 = SecondpriceElement.getText().replaceAll("[^\\d.]", "");


        double price1 = Double.parseDouble(priceText1);
        double price2 = Double.parseDouble(priceText2);


        if (price1 > price2) {
             break;
             
        } else {
             IsSortedCorrectly = true;

        } 
    }

        Assert.assertEquals(IsSortedCorrectly, true, "not sorted correctly");
    } 
@Test(priority=6,enabled=true) //Test Case ID: TC_JEB_006
public void AddingProductstoCart() throws InterruptedException {
    //In this test I chose categories directly(not randomly or from database, to make sure I tried all approaches)
	// It will choose 1 product from beverages category and 2 from chocolates
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("window.scrollTo(0, 690);");


    WebElement MenuElement = driver.findElement(By.cssSelector("a[href='/collections/beverages']")); //beverages category
    actions.moveToElement(MenuElement).perform();
    Thread.sleep(1000);
   
    
    System.out.println("Hovered over: " + MenuElement.getText());

    // Click on "juices"
    WebElement Choice = driver.findElement(By.cssSelector("a[href='/collections/juices']"));
    Choice.click();
    Thread.sleep(3000);

    List<String> addedProducts = new ArrayList<>();
    int itemsToAdd = 3; // 

    while (addedProducts.size() < itemsToAdd) { //it will add 3 products
    	JavascriptExecutor js1 = (JavascriptExecutor) driver;
    	js1.executeScript("window.scrollTo(0, 250);");

        WebElement mainContent = driver.findElement(By.cssSelector(".col-md-9.col-sm-12.col-xs-12.normal_main_content"));
        List<WebElement> SubItems = mainContent.findElements(By.cssSelector(".grid-view-item__image.lazyload")); //list of products

        int RandomSubItemIndex = rand.nextInt(SubItems.size());
        WebElement RandomChosenItem = SubItems.get(RandomSubItemIndex); //choosing random product

        // Check if item is sold out
        List<WebElement> overlay = RandomChosenItem.findElements(By.className("outstock-overlay"));
        if (!overlay.isEmpty()) { //if overlay exists
            System.out.println("Item is SOLD OUT. Trying another item ...");
            continue; // go choose another product from colors and accessories
        }

        // Click the item if it's not out of stock
        RandomChosenItem.click();
        Thread.sleep(3000);

        driver.findElement(By.id("AddToCartText-product-template")).click(); //Add the item to the cart
        Thread.sleep(2000);

        // Wait for success message(item is added to the cart message)
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement notification = wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.cssSelector(".col-xs-12.alert.alert-success.animated.fadeOutUp")));
        String message = notification.getText();
        System.out.println(message);
        Assert.assertTrue(notification.isDisplayed(), "Success message did not appear."); //Assertion for message appearance
        Thread.sleep(2000);

        String ProductName = driver.findElement(By.cssSelector(".product-single__title.hidden-sm-down")).getText().toLowerCase().replaceAll("[\\s\\u00A0]+", " ");
        System.out.println("Added Product: " + ProductName);
        addedProducts.add(ProductName);

        // Reopen the collection page to select from another category (snacks)
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

    PrimaryCartSubTotal = driver.findElement(By.cssSelector(".cart__footer .cart__subtotal")).getText(); //Get cart subtotal before changing quantities
    System.out.print("$$$$$ " + PrimaryCartSubTotal);
System.out.println();

    // Get cart items
    List<WebElement> cartItems = driver.findElements(By.className("product-title"));
    List<String> cartProductNames = new ArrayList<>();
    for (int i = 0; i < cartItems.size(); i++) {
        cartProductNames.add(cartItems.get(i).getText().toLowerCase().replaceAll("[\\s\\u00A0]+", " ")); // get cart items names
    }

    for (int i = 0; i < addedProducts.size(); i++) {
        String added = addedProducts.get(i); 
        System.out.print(added);
        System.out.println();
       // Assert.assertTrue(cartProductNames.contains(added)|| ItemTitle.contains(added.substring(0, 3)), "Cart does not contain added product: " + added); //assertion that added items appear in the cart
    }
    Thread.sleep(3000);

}
@Test(priority=7,enabled=true) //Test Case ID: TC_JEB_007
public void ViewingandModifyingtheShoppingCart() throws InterruptedException {

	 driver.findElement(By.cssSelector(".view-cart.btn")).click(); //view cart

	    // Get list of quantity input fields
	    List<WebElement> QuantityInput = driver.findElements(By.cssSelector(".cart__qty-input.quantity.update-cart"));
	    for (int i = 0; i < QuantityInput.size(); i++) {
	        int randomNumber = rand.nextInt(2, 6);

	        
	        QuantityInput = driver.findElements(By.cssSelector(".cart__qty-input.quantity.update-cart"));  // Re-fetch quantity input fields to avoid stale element error
	        WebElement input = QuantityInput.get(i);
	        input.clear();
	        input.sendKeys(String.valueOf(randomNumber));
	        
	        driver.findElement(By.cssSelector(".btn.btn--secondary.update-qty.cart__update.cart__update--large")).click();
	        Thread.sleep(3000);
    }

    //randomly remove one item
    List<WebElement> removeButtons = driver.findElements(By.cssSelector(".fa.fa-trash-o")); //remove buttons
    if (!removeButtons.isEmpty()) {
        int removeIndex = rand.nextInt(removeButtons.size()); //choose random product to remove
        removeButtons.get(removeIndex).click();
        System.out.println("Removed item at index: " + removeIndex);
    } else {
        System.out.println("No items found to remove.");
    }
    Thread.sleep(5000);

    String FinalCartSubTotal = driver.findElement(By.cssSelector(".cart-bottm .cart__subtotal")).getText(); //get final subtotal after changing quantities
    System.out.print("$$$$$ " +FinalCartSubTotal);
    System.out.println();

    Thread.sleep(5000);
    if (PrimaryCartSubTotal != FinalCartSubTotal) { // check if subtotal really changed(modified)
    	Assert.assertNotEquals(PrimaryCartSubTotal, FinalCartSubTotal, "Cart subtotal did not change after updating quantities.");
    }
} 
@Test(priority=8,enabled=true) //Test Case ID: TC_JEB_008
public void  ProceedingtoCheckout() throws InterruptedException {
driver.findElement(By.cssSelector(".btn.btn--small-wide.checkout_btn")).click(); //click proceed to checkout
Thread.sleep(5000);
String CurrentURL = driver.getCurrentUrl();
Assert.assertTrue(CurrentURL.contains("checkouts"), "Current URL does not contain 'checkouts', you might be in the wrong page"); //check if you're really on checkout page
List<WebElement> checkoutFields = new ArrayList<>();
//below is to get all elements related to checkout process
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
    Assert.assertTrue(field.isDisplayed(), "There's a missing field"); //check that all elements are displayed
}
}
@Test(priority=9,enabled=true) //Test Case ID: TC_JEB_009
public void LanguageSwitchFunctionlity() throws InterruptedException {
	driver.navigate().back();
	WebElement Arrow = driver.findElement(By.cssSelector(
		    "div.ly-switcher-wrapper.ly-breakpoint-2.fixed.bottom_right div.ly-arrow.ly-arrow-black.stroke svg"
		));
	Arrow.click(); //click on arrow that shows the other optional language(arabic)
	
	WebElement ArabicOption = driver.findElement(By.cssSelector(
		    "ul#languagesSwitcherList-2 li[key='ar'] span.ly-custom-dropdown-list-element-right"));
	ArabicOption.click(); //click on Arabic language option
	Thread.sleep(5000);

	String CurrentUrl = driver.getCurrentUrl();
	Assert.assertTrue(CurrentUrl.contains("ar"), "URL does not contain 'ar', it did not switch to arabic"); //assertion(1) to make sure language is switched to Arabic
	
	
	List<WebElement> Fields = new ArrayList<>(); // These are main fields that must be in selected language
	Fields.add(driver.findElement(By.cssSelector(".home-title.hidden-lg-down")));
	Fields.add(driver.findElement(By.cssSelector(".search-header__submit.search__submit.btn--link")));
	Fields.add(driver.findElement(By.cssSelector(".desktop-user-info.text-center.col-md-12.col-sm-12.col-xs-12")));
	for (int i = 0; i < Fields.size(); i++) {
        String text = Fields.get(i).getText();
        
        boolean containsEnglish = text.matches(".*[a-zA-Z]+.*"); //if it contains any English character

        Assert.assertFalse(containsEnglish, "English letters found, language is not Arabic"); //assertion(2) to make sure language is switched to Arabic
	}
	WebElement ArabicArrow = driver.findElement(By.cssSelector(
			  "div.ly-switcher-wrapper.ly-breakpoint-2.fixed.bottom_right div.ly-arrow.ly-arrow-black.stroke svg"
			)); 

	ArabicArrow.click(); 

	WebElement EnglishOption = driver.findElement(By.cssSelector("ul[id='languagesSwitcherList-2'] li[key='en'] span.ly-custom-dropdown-list-element-right"));
	EnglishOption.click(); //switch again to English Language
	Thread.sleep(5000);
	List<WebElement> Fields2 = new ArrayList<>(); //do the same for English fields
	Fields2.add(driver.findElement(By.cssSelector(".home-title.hidden-lg-down")));
	Fields2.add(driver.findElement(By.cssSelector(".search-header__submit.search__submit.btn--link")));
	Fields2.add(driver.findElement(By.cssSelector(".desktop-user-info.text-center.col-md-12.col-sm-12.col-xs-12")));
	for (int n = 0; n < Fields2.size(); n++) {
	    String text = Fields2.get(n).getText();
	        boolean containsArabic = text.matches(".*[\\u0600-\\u06FF]+.*"); // check if they don't contain any Arabic characters

	        Assert.assertFalse(containsArabic, "Arabic letters found, language is not English");
	    }
	
}
@Test(priority=10,enabled=true) //Test Case ID: TC_JEB_010
public void LogoandInfoVisibility() throws InterruptedException {
	
	WebElement Header = driver.findElement(By.cssSelector(".header-top.site-header-inner"));
	boolean ActualResultForLogo = Header.findElement(By.className("header__logo-image")).isDisplayed(); 

	Assert.assertEquals(ActualResultForLogo, ExpectedResultForJibnalakLogo, "Logo is not dispalyed"); //Check if logo is displayed
	
	JavascriptExecutor js = (JavascriptExecutor) driver;
	js.executeScript("window.scrollTo(0, 2500);"); //scroll down to footer
    Thread.sleep(2000);
	WebElement Footer = driver.findElement(By.className("footer-container"));
	boolean ActualResultForInfo = Footer.findElement(By.cssSelector(".footer-content.contact-info.col-md-12")).isDisplayed();

	Assert.assertEquals(ActualResultForInfo, ExpectedResultForInfo, "No info displayed");  // check if contact details are displayed 

}
/*@AfterTest
public void quit() {
	driver.quit();
}*/

}