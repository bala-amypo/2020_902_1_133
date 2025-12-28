package com.example.demo;

import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.RegisterRequestDto;
import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.security.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.servlet.SimpleStatusServlet;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
@org.springframework.test.context.ActiveProfiles("test")
@Listeners({TestResultListener.class})
public class MultiLocationInventoryBalancerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private InventoryLevelRepository inventoryLevelRepository;
    @Autowired
    private DemandForecastRepository demandForecastRepository;
    @Autowired
    private TransferSuggestionRepository transferSuggestionRepository;

    @Autowired
    private AuthService authService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InventoryLevelService inventoryLevelService;
    @Autowired
    private DemandForecastService demandForecastService;
    @Autowired
    private InventoryBalancerService inventoryBalancerService;

    private String adminToken;

    private String asJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    private String bearer() {
        return "Bearer " + adminToken;
    }

    private String uniqueStoreName(String base) {
        return base + "-" + UUID.randomUUID();
    }

    private String uniqueSku(String base) {
        return base + "-" + UUID.randomUUID();
    }

    private String uniqueProductName(String base) {
        return base + "-" + UUID.randomUUID();
    }

    @BeforeClass(alwaysRun = true)
    public void initAdminUserAndToken() throws Exception {
        try {
            RegisterRequestDto reg = new RegisterRequestDto();
            reg.setEmail("admin@invbalancer.com");
            reg.setFullName("Admin User");
            reg.setPassword("adminpass123");
            reg.setRole("ROLE_ADMIN");
            authService.register(reg);
        } catch (BadRequestException ignored) {
        }

        AuthRequestDto login = new AuthRequestDto();
        login.setEmail("admin@invbalancer.com");
        login.setPassword("adminpass123");

        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(login)))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode node = objectMapper.readTree(body);
        adminToken = node.get("token").asText();

        Assert.assertNotNull(adminToken, "Admin JWT must not be null");
    }

    @Test(priority = 1, groups = "servlet")
    public void t01_servlet_doGet_returnsExpectedMessage() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        SimpleStatusServlet servlet = new SimpleStatusServlet();
        servlet.doGet(req, resp);

        String content = resp.getContentAsString();
        Assert.assertEquals(content, "Multi-Location Inventory Balancer is running");
    }

    @Test(priority = 2, groups = "servlet")
    public void t02_servlet_doGet_setsPlainTextContentType() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        SimpleStatusServlet servlet = new SimpleStatusServlet();
        servlet.doGet(req, resp);

        Assert.assertEquals(resp.getContentType(), "text/plain");
    }

    @Test(priority = 3, groups = "servlet")
    public void t03_servlet_doGet_statusIsOk() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        SimpleStatusServlet servlet = new SimpleStatusServlet();
        servlet.doGet(req, resp);

        Assert.assertEquals(resp.getStatus(), HttpServletResponse.SC_OK);
    }

    @Test(priority = 4, groups = "servlet")
    public void t04_servlet_multipleCalls_sameOutput() throws Exception {
        SimpleStatusServlet servlet = new SimpleStatusServlet();

        MockHttpServletRequest req1 = new MockHttpServletRequest();
        MockHttpServletResponse resp1 = new MockHttpServletResponse();
        servlet.doGet(req1, resp1);

        MockHttpServletRequest req2 = new MockHttpServletRequest();
        MockHttpServletResponse resp2 = new MockHttpServletResponse();
        servlet.doGet(req2, resp2);

        Assert.assertEquals(resp1.getContentAsString(), resp2.getContentAsString());
    }

    @Test(priority = 5, groups = "servlet")
    public void t05_servlet_responseNotEmpty() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        SimpleStatusServlet servlet = new SimpleStatusServlet();
        servlet.doGet(req, resp);

        String content = resp.getContentAsString();
        Assert.assertFalse(content.isEmpty());
    }

    @Test(priority = 6, groups = "servlet")
    public void t06_servlet_responseContainsNoHtml() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse resp = new MockHttpServletResponse();

        SimpleStatusServlet servlet = new SimpleStatusServlet();
        servlet.doGet(req, resp);

        String content = resp.getContentAsString();
        Assert.assertFalse(content.contains("<html>"));
    }

    @Test(priority = 10, groups = "crud")
    public void t07_auth_login_validCredentials_returnsToken() throws Exception {
        AuthRequestDto login = new AuthRequestDto();
        login.setEmail("admin@invbalancer.com");
        login.setPassword("adminpass123");

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test(priority = 11, groups = "crud")
    public void t08_auth_login_invalidPassword_fails() throws Exception {
        AuthRequestDto login = new AuthRequestDto();
        login.setEmail("admin@invbalancer.com");
        login.setPassword("wrong-password");

        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(login)))
                .andReturn();

        int status = result.getResponse().getStatus();
        Assert.assertTrue(status >= 400);
    }

    @Test(priority = 12, groups = "crud")
    public void t09_createStore_viaController_success() throws Exception {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Main Store"));
        store.setAddress("Central Road");
        store.setRegion("North");

        mockMvc.perform(
                        post("/api/stores")
                                .header("Authorization", bearer())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(store)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeName").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test(priority = 13, groups = "crud")
    public void t10_createProduct_viaController_success() throws Exception {
        Product product = new Product();
        product.setSku(uniqueSku("SKU-MLIB-001"));
        product.setName(uniqueProductName("Balanced Item"));
        product.setCategory("Category-A");

        mockMvc.perform(
                        post("/api/products")
                                .header("Authorization", bearer())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").exists());
    }

    @Test(priority = 14, groups = "crud")
    public void t11_listStores_returnsArray() throws Exception {
        mockMvc.perform(
                        get("/api/stores")
                                .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test(priority = 15, groups = "crud")
    public void t12_listProducts_returnsArray() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .header("Authorization", bearer()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test(priority = 16, groups = "crud")
    public void t13_updateStore_changesRegion() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Region Store-MLIB"));
        store.setAddress("R Street");
        store.setRegion("OldR");
        store = storeService.createStore(store);

        Store update = new Store();
        update.setStoreName(uniqueStoreName("Region Store-Updated"));
        update.setAddress("New R Street");
        update.setRegion("NewR");
        update.setActive(true);

        Store updated = storeService.updateStore(store.getId(), update);

        Assert.assertNotNull(updated.getId());
        Assert.assertEquals(updated.getRegion(), "NewR");
        Assert.assertEquals(updated.getStoreName(), update.getStoreName());
    }

    @Test(priority = 17, groups = "crud")
    public void t14_deactivateStore_setsActiveFalse() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Temp Store-MLIB"));
        store.setAddress("Temp Address");
        store.setRegion("TR");
        store = storeService.createStore(store);

        storeService.deactivateStore(store.getId());

        Store reloaded = storeService.getStoreById(store.getId());
        Assert.assertFalse(reloaded.isActive(), "Store should be inactive after deactivation");
    }

    @Test(priority = 18, groups = "crud")
    public void t15_deactivateProduct_setsActiveFalse() {
        Product product = new Product();
        product.setSku(uniqueSku("SKU-MLIB-002"));
        product.setName(uniqueProductName("Temp Product-MLIB"));
        product.setCategory("TempCat");
        product = productService.createProduct(product);

        productService.deactivateProduct(product.getId());

        Product reloaded = productService.getProductById(product.getId());
        Assert.assertFalse(reloaded.isActive(), "Product should be inactive after deactivation");
    }

    @Test(priority = 19, groups = "crud")
    public void t16_createInventory_forStoreAndProduct_success() throws Exception {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Inv-Store-A"));
        store.setAddress("A Street");
        store.setRegion("R1");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("SKU-INV-A"));
        product.setName(uniqueProductName("Inventory-A"));
        product.setCategory("C1");
        product = productService.createProduct(product);

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(store);
        inv.setProduct(product);
        inv.setQuantity(40);

        mockMvc.perform(
                        post("/api/inventory")
                                .header("Authorization", bearer())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJson(inv)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(40));
    }

    @Test(priority = 20, groups = "crud")
    public void t17_updateInventory_changesQuantity() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Inv-Store-B"));
        store.setAddress("B Street");
        store.setRegion("R2");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("SKU-INV-B"));
        product.setName(uniqueProductName("Inventory-B"));
        product.setCategory("C2");
        product = productService.createProduct(product);

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(store);
        inv.setProduct(product);
        inv.setQuantity(10);
        inventoryLevelService.createOrUpdateInventory(inv);

        InventoryLevel update = new InventoryLevel();
        update.setStore(store);
        update.setProduct(product);
        update.setQuantity(25);

        InventoryLevel result = inventoryLevelService.createOrUpdateInventory(update);
        Assert.assertEquals(result.getQuantity().intValue(), 25);
    }

    @Test(priority = 21, groups = "crud")
    public void t18_createForecast_futureDate_success() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Forecast-Store-A"));
        store.setAddress("FS Street");
        store.setRegion("R3");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("SKU-FCAST-001"));
        product.setName(uniqueProductName("ForecastItem-1"));
        product.setCategory("C3");
        product = productService.createProduct(product);

        DemandForecast forecast = new DemandForecast();
        forecast.setStore(store);
        forecast.setProduct(product);
        forecast.setForecastedDemand(120);
        forecast.setForecastDate(LocalDate.now().plusDays(2));

        DemandForecast saved = demandForecastService.createForecast(forecast);
        Assert.assertNotNull(saved.getId());
    }

    @Test(priority = 22, groups = "crud")
    public void t19_createForecast_pastDate_throwsBadRequest() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Forecast-Store-B"));
        store.setAddress("F2 Street");
        store.setRegion("R4");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("SKU-FCAST-002"));
        product.setName(uniqueProductName("ForecastItem-2"));
        product.setCategory("C4");
        product = productService.createProduct(product);

        DemandForecast forecast = new DemandForecast();
        forecast.setStore(store);
        forecast.setProduct(product);
        forecast.setForecastedDemand(50);
        forecast.setForecastDate(LocalDate.now().minusDays(1));

        try {
            demandForecastService.createForecast(forecast);
            Assert.fail("Expected BadRequestException not thrown");
        } catch (BadRequestException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 23, groups = "crud")
    public void t20_getInventoryForStore_returnsList() {
        List<Store> stores = storeRepository.findAll();
        if (stores.isEmpty()) {
            Store s = new Store();
            s.setStoreName(uniqueStoreName("Inv-Store-C"));
            s.setRegion("R5");
            s.setAddress("C Street");
            storeService.createStore(s);
        }
        Store any = storeRepository.findAll().get(0);
        List<InventoryLevel> list = inventoryLevelService.getInventoryForStore(any.getId());
        Assert.assertNotNull(list);
    }

    @Test(priority = 30, groups = "di")
    public void t21_di_mockMvcInjected() {
        Assert.assertNotNull(mockMvc);
    }

    @Test(priority = 31, groups = "di")
    public void t22_di_servicesInjected() {
        Assert.assertNotNull(storeService);
        Assert.assertNotNull(productService);
        Assert.assertNotNull(inventoryLevelService);
        Assert.assertNotNull(demandForecastService);
        Assert.assertNotNull(inventoryBalancerService);
    }

    @Test(priority = 32, groups = "di")
    public void t23_di_repositoriesInjected() {
        Assert.assertNotNull(storeRepository);
        Assert.assertNotNull(productRepository);
        Assert.assertNotNull(inventoryLevelRepository);
        Assert.assertNotNull(demandForecastRepository);
        Assert.assertNotNull(transferSuggestionRepository);
    }

    @Test(priority = 33, groups = "di")
    public void t24_di_authAndJwtInjected() {
        Assert.assertNotNull(authService);
        Assert.assertNotNull(jwtUtil);
    }

    @Test(priority = 34, groups = "di")
    public void t25_di_userAccountRepositoryInjected() {
        Assert.assertNotNull(userAccountRepository);
    }

    // =====================================================================
    // 4. Hibernate configurations, lifecycle & CRUD behavior
    // =====================================================================

    @Test(priority = 40, groups = "lifecycle")
    public void t26_userAccount_prePersist_setsTimestamps() {
        UserAccount user = new UserAccount();
        user.setEmail("test@example.com");
        user.setFullName("Test User");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        
        UserAccount saved = userAccountRepository.save(user);
        Assert.assertNotNull(saved.getCreatedAt());
        Assert.assertNotNull(saved.getUpdatedAt());
    }

    @Test(priority = 41, groups = "lifecycle")
    public void t27_userAccount_preUpdate_updatesTimestamp() throws InterruptedException {
        UserAccount user = new UserAccount();
        user.setEmail("update@example.com");
        user.setFullName("Update User");
        user.setPassword("password");
        user.setRole("ROLE_USER");
        
        UserAccount saved = userAccountRepository.save(user);
        Thread.sleep(10);
        saved.setFullName("Updated Name");
        UserAccount updated = userAccountRepository.save(saved);
        
        Assert.assertTrue(updated.getUpdatedAt().isAfter(saved.getCreatedAt()) || 
                         updated.getUpdatedAt().equals(saved.getCreatedAt()));
    }

    @Test(priority = 42, groups = "lifecycle")
    public void t28_inventoryLevel_prePersist_setsLastUpdated() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Lifecycle Store"));
        store.setAddress("Test Address");
        store.setRegion("Test Region");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("LIFECYCLE-SKU"));
        product.setName(uniqueProductName("Lifecycle Product"));
        product.setCategory("Test Category");
        product = productService.createProduct(product);

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(store);
        inv.setProduct(product);
        inv.setQuantity(100);
        
        InventoryLevel saved = inventoryLevelService.createOrUpdateInventory(inv);
        Assert.assertNotNull(saved.getLastUpdated());
    }

    @Test(priority = 43, groups = "lifecycle")
    public void t29_inventoryLevel_update_changesLastUpdatedOrSame() throws InterruptedException {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Update Store"));
        store.setAddress("Test Address");
        store.setRegion("Test Region");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("UPDATE-SKU"));
        product.setName(uniqueProductName("Update Product"));
        product.setCategory("Test Category");
        product = productService.createProduct(product);

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(store);
        inv.setProduct(product);
        inv.setQuantity(50);
        
        InventoryLevel saved = inventoryLevelService.createOrUpdateInventory(inv);
        Thread.sleep(10);
        
        InventoryLevel update = new InventoryLevel();
        update.setStore(store);
        update.setProduct(product);
        update.setQuantity(75);
        
        InventoryLevel updated = inventoryLevelService.createOrUpdateInventory(update);
        Assert.assertTrue(updated.getLastUpdated().isAfter(saved.getLastUpdated()) || 
                         updated.getLastUpdated().equals(saved.getLastUpdated()));
    }

    @Test(priority = 44, groups = "lifecycle")
    public void t30_transferSuggestion_prePersist_setsGeneratedAt() {
        Store sourceStore = new Store();
        sourceStore.setStoreName(uniqueStoreName("Source Store"));
        sourceStore.setAddress("Source Address");
        sourceStore.setRegion("Source Region");
        sourceStore = storeService.createStore(sourceStore);

        Product product = new Product();
        product.setSku(uniqueSku("TRANSFER-SKU"));
        product.setName(uniqueProductName("Transfer Product"));
        product.setCategory("Transfer Category");
        product = productService.createProduct(product);

        TransferSuggestion suggestion = new TransferSuggestion();
        suggestion.setSourceStore(sourceStore);
        suggestion.setProduct(product);
        suggestion.setQuantity(25);
        suggestion.setPriority("HIGH");
        suggestion.setStatus("PENDING");
        
        TransferSuggestion saved = transferSuggestionRepository.save(suggestion);
        Assert.assertNotNull(saved.getSuggestedAt());
    }

    @Test(priority = 45, groups = "validation")
    public void t31_store_defaultActiveTrue() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Default Active Store"));
        store.setAddress("Default Address");
        store.setRegion("Default Region");
        
        Store saved = storeService.createStore(store);
        Assert.assertTrue(saved.isActive());
    }

    @Test(priority = 46, groups = "validation")
    public void t32_product_defaultActiveTrue() {
        Product product = new Product();
        product.setSku(uniqueSku("DEFAULT-ACTIVE-SKU"));
        product.setName(uniqueProductName("Default Active Product"));
        product.setCategory("Default Category");
        
        Product saved = productService.createProduct(product);
        Assert.assertTrue(saved.isActive());
    }

    @Test(priority = 47, groups = "validation")
    public void t33_inventory_negativeQuantity_throwsBadRequest() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Negative Store"));
        store.setAddress("Negative Address");
        store.setRegion("Negative Region");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("NEGATIVE-SKU"));
        product.setName(uniqueProductName("Negative Product"));
        product.setCategory("Negative Category");
        product = productService.createProduct(product);

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(store);
        inv.setProduct(product);
        inv.setQuantity(-10);
        
        try {
            inventoryLevelService.createOrUpdateInventory(inv);
            Assert.fail("Expected BadRequestException not thrown");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("Quantity must be >= 0"));
        }
    }

    @Test(priority = 48, groups = "relationships")
    public void t34_store_product_manyToMany_viaInventory() {
        Store store1 = new Store();
        store1.setStoreName(uniqueStoreName("Multi Store 1"));
        store1.setAddress("Address 1");
        store1.setRegion("Region 1");
        store1 = storeService.createStore(store1);

        Store store2 = new Store();
        store2.setStoreName(uniqueStoreName("Multi Store 2"));
        store2.setAddress("Address 2");
        store2.setRegion("Region 2");
        store2 = storeService.createStore(store2);

        Product product = new Product();
        product.setSku(uniqueSku("MULTI-SKU"));
        product.setName(uniqueProductName("Multi Product"));
        product.setCategory("Multi Category");
        product = productService.createProduct(product);

        InventoryLevel inv1 = new InventoryLevel();
        inv1.setStore(store1);
        inv1.setProduct(product);
        inv1.setQuantity(100);
        inventoryLevelService.createOrUpdateInventory(inv1);

        InventoryLevel inv2 = new InventoryLevel();
        inv2.setStore(store2);
        inv2.setProduct(product);
        inv2.setQuantity(200);
        inventoryLevelService.createOrUpdateInventory(inv2);

        List<InventoryLevel> productInventories = inventoryLevelService.getInventoryForProduct(product.getId());
        Assert.assertEquals(productInventories.size(), 2);
    }

    @Test(priority = 49, groups = "relationships")
    public void t35_demandForecast_storeProductMappingCorrect() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Forecast Mapping Store"));
        store.setAddress("Forecast Address");
        store.setRegion("Forecast Region");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("FORECAST-MAPPING-SKU"));
        product.setName(uniqueProductName("Forecast Mapping Product"));
        product.setCategory("Forecast Category");
        product = productService.createProduct(product);

        DemandForecast forecast = new DemandForecast();
        forecast.setStore(store);
        forecast.setProduct(product);
        forecast.setForecastedDemand(150);
        forecast.setForecastDate(LocalDate.now().plusDays(5));
        
        DemandForecast saved = demandForecastService.createForecast(forecast);
        Assert.assertEquals(saved.getStore().getId(), store.getId());
        Assert.assertEquals(saved.getProduct().getId(), product.getId());
    }

    @Test(priority = 50, groups = "uniqueness")
    public void t36_inventoryLevel_uniqueStoreProduct_updatesExisting() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Unique Store"));
        store.setAddress("Unique Address");
        store.setRegion("Unique Region");
        store = storeService.createStore(store);

        Product product = new Product();
        product.setSku(uniqueSku("UNIQUE-SKU"));
        product.setName(uniqueProductName("Unique Product"));
        product.setCategory("Unique Category");
        product = productService.createProduct(product);

        InventoryLevel inv1 = new InventoryLevel();
        inv1.setStore(store);
        inv1.setProduct(product);
        inv1.setQuantity(50);
        InventoryLevel saved1 = inventoryLevelService.createOrUpdateInventory(inv1);

        InventoryLevel inv2 = new InventoryLevel();
        inv2.setStore(store);
        inv2.setProduct(product);
        inv2.setQuantity(75);
        InventoryLevel saved2 = inventoryLevelService.createOrUpdateInventory(inv2);

        Assert.assertEquals(saved1.getId(), saved2.getId());
        Assert.assertEquals(saved2.getQuantity().intValue(), 75);
    }

    @Test(priority = 51, groups = "service")
    public void t37_storeService_getAllStores_returnsNonEmptyList() {
        List<Store> stores = storeService.getAllStores();
        Assert.assertNotNull(stores);
        Assert.assertTrue(stores.size() > 0);
    }

    @Test(priority = 52, groups = "service")
    public void t38_productService_getAllProducts_returnsNonEmptyList() {
        List<Product> products = productService.getAllProducts();
        Assert.assertNotNull(products);
        Assert.assertTrue(products.size() > 0);
    }

    @Test(priority = 53, groups = "service")
    public void t39_getInventoryForProduct_returnsList() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            Product product = products.get(0);
            List<InventoryLevel> inventories = inventoryLevelService.getInventoryForProduct(product.getId());
            Assert.assertNotNull(inventories);
        }
    }

    @Test(priority = 54, groups = "service")
    public void t40_getForecastsForStore_returnsList() {
        List<Store> stores = storeRepository.findAll();
        if (!stores.isEmpty()) {
            Store store = stores.get(0);
            List<DemandForecast> forecasts = demandForecastService.getForecastsForStore(store.getId());
            Assert.assertNotNull(forecasts);
        }
    }

    @Test(priority = 55, groups = "relationships")
    public void t41_singleProduct_multipleStores_viaInventory() {
        Product product = new Product();
        product.setSku(uniqueSku("SINGLE-PRODUCT-SKU"));
        product.setName(uniqueProductName("Single Product"));
        product.setCategory("Single Category");
        product = productService.createProduct(product);

        Store store1 = new Store();
        store1.setStoreName(uniqueStoreName("Single Product Store 1"));
        store1.setAddress("SP Address 1");
        store1.setRegion("SP Region 1");
        store1 = storeService.createStore(store1);

        Store store2 = new Store();
        store2.setStoreName(uniqueStoreName("Single Product Store 2"));
        store2.setAddress("SP Address 2");
        store2.setRegion("SP Region 2");
        store2 = storeService.createStore(store2);

        InventoryLevel inv1 = new InventoryLevel();
        inv1.setStore(store1);
        inv1.setProduct(product);
        inv1.setQuantity(30);
        inventoryLevelService.createOrUpdateInventory(inv1);

        InventoryLevel inv2 = new InventoryLevel();
        inv2.setStore(store2);
        inv2.setProduct(product);
        inv2.setQuantity(70);
        inventoryLevelService.createOrUpdateInventory(inv2);

        List<InventoryLevel> inventories = inventoryLevelService.getInventoryForProduct(product.getId());
        Assert.assertTrue(inventories.size() >= 2);
    }

    @Test(priority = 60, groups = "auth")
    public void t52_registerDuplicateEmail_throwsBadRequest() {
        RegisterRequestDto reg1 = new RegisterRequestDto();
        reg1.setEmail("duplicate@example.com");
        reg1.setFullName("First User");
        reg1.setPassword("password123");
        reg1.setRole("ROLE_USER");
        
        authService.register(reg1);
        
        RegisterRequestDto reg2 = new RegisterRequestDto();
        reg2.setEmail("duplicate@example.com");
        reg2.setFullName("Second User");
        reg2.setPassword("password456");
        reg2.setRole("ROLE_USER");
        
        try {
            authService.register(reg2);
            Assert.fail("Expected BadRequestException not thrown");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("already exists"));
        }
    }

    @Test(priority = 61, groups = "auth")
    public void t53_userAccount_findByEmail_returnsAdmin() {
        java.util.Optional<UserAccount> admin = userAccountRepository.findByEmail("admin@invbalancer.com");
        Assert.assertTrue(admin.isPresent());
        Assert.assertEquals(admin.get().getRole(), "ROLE_ADMIN");
    }

    @Test(priority = 62, groups = "auth")
    public void t54_loginWithWrongPassword_throwsException() {
        AuthRequestDto login = new AuthRequestDto();
        login.setEmail("admin@invbalancer.com");
        login.setPassword("wrongpassword");
        
        try {
            authService.login(login);
            Assert.fail("Expected BadRequestException not thrown");
        } catch (BadRequestException ex) {
            Assert.assertTrue(ex.getMessage().contains("Invalid credentials"));
        }
    }

    @Test(priority = 63, groups = "security")
    public void t55_publicEndpoints_authAndSwaggerAccessibleWithoutToken() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
        
        AuthRequestDto login = new AuthRequestDto();
        login.setEmail("admin@invbalancer.com");
        login.setPassword("adminpass123");
        
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJson(login)))
                .andExpect(status().isOk());
    }

    @Test(priority = 70, groups = "repository")
    public void t56_repo_findInventoryByStoreId() {
        List<Store> stores = storeRepository.findAll();
        if (!stores.isEmpty()) {
            Store store = stores.get(0);
            List<InventoryLevel> inventories = inventoryLevelRepository.findByStore_Id(store.getId());
            Assert.assertNotNull(inventories);
        }
    }

    @Test(priority = 71, groups = "repository")
    public void t57_repo_findForecastsByStoreId() {
        List<Store> stores = storeRepository.findAll();
        if (!stores.isEmpty()) {
            Store store = stores.get(0);
            List<DemandForecast> forecasts = demandForecastRepository.findByStore_Id(store.getId());
            Assert.assertNotNull(forecasts);
        }
    }

    @Test(priority = 72, groups = "repository")
    public void t58_repo_findInventoryByProductId() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            Product product = products.get(0);
            List<InventoryLevel> inventories = inventoryLevelRepository.findByProduct_Id(product.getId());
            Assert.assertNotNull(inventories);
        }
    }

    @Test(priority = 73, groups = "repository")
    public void t59_repo_findSuggestionsByProductId() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
            Product product = products.get(0);
            List<TransferSuggestion> suggestions = transferSuggestionRepository.findByProduct_Id(product.getId());
            Assert.assertNotNull(suggestions);
        }
    }

    @Test(priority = 80, groups = "balancer")
    public void t60_balancer_generateSuggestions_basicFlow() {
        Store sourceStore = new Store();
        sourceStore.setStoreName(uniqueStoreName("Balancer Source"));
        sourceStore.setAddress("Source Address");
        sourceStore.setRegion("Source Region");
        sourceStore = storeService.createStore(sourceStore);

        Product product = new Product();
        product.setSku(uniqueSku("BALANCER-SKU"));
        product.setName(uniqueProductName("Balancer Product"));
        product.setCategory("Balancer Category");
        product = productService.createProduct(product);

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(sourceStore);
        inv.setProduct(product);
        inv.setQuantity(100);
        inventoryLevelService.createOrUpdateInventory(inv);

        DemandForecast forecast = new DemandForecast();
        forecast.setStore(sourceStore);
        forecast.setProduct(product);
        forecast.setForecastedDemand(50);
        forecast.setForecastDate(LocalDate.now().plusDays(7));
        demandForecastService.createForecast(forecast);

        List<TransferSuggestion> suggestions = inventoryBalancerService.generateSuggestions(product.getId());
        Assert.assertNotNull(suggestions);
    }

    @Test(priority = 81, groups = "balancer")
    public void t61_balancer_generateSuggestions_inactiveProduct_throwsBadRequest() {
        Product product = new Product();
        product.setSku(uniqueSku("INACTIVE-BALANCER-SKU"));
        product.setName(uniqueProductName("Inactive Balancer Product"));
        product.setCategory("Inactive Category");
        product = productService.createProduct(product);
        
        productService.deactivateProduct(product.getId());
        
        try {
            inventoryBalancerService.generateSuggestions(product.getId());
            Assert.fail("Expected BadRequestException not thrown");
        } catch (BadRequestException ex) {
            Assert.assertTrue(true);
        }
    }

    @Test(priority = 82, groups = "balancer")
    public void t62_balancer_getSuggestionById_notFoundThrows() {
        try {
            inventoryBalancerService.getSuggestionById(99999L);
            Assert.fail("Expected ResourceNotFoundException not thrown");
        } catch (ResourceNotFoundException ex) {
            Assert.assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test(priority = 83, groups = "balancer")
    public void t63_balancer_getSuggestionsForStore_returnsList() {
        List<Store> stores = storeRepository.findAll();
        if (!stores.isEmpty()) {
            Store store = stores.get(0);
            List<TransferSuggestion> suggestions = inventoryBalancerService.getSuggestionsForStore(store.getId());
            Assert.assertNotNull(suggestions);
        }
    }

    @Test(priority = 90, groups = "integration")
    public void t64_endToEnd_createStoreProductInventoryForecast() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("E2E Store"));
        store.setAddress("E2E Address");
        store.setRegion("E2E Region");
        store = storeService.createStore(store);
        Assert.assertNotNull(store.getId());

        Product product = new Product();
        product.setSku(uniqueSku("E2E-SKU"));
        product.setName(uniqueProductName("E2E Product"));
        product.setCategory("E2E Category");
        product = productService.createProduct(product);
        Assert.assertNotNull(product.getId());

        InventoryLevel inv = new InventoryLevel();
        inv.setStore(store);
        inv.setProduct(product);
        inv.setQuantity(200);
        InventoryLevel savedInv = inventoryLevelService.createOrUpdateInventory(inv);
        Assert.assertNotNull(savedInv.getId());

        DemandForecast forecast = new DemandForecast();
        forecast.setStore(store);
        forecast.setProduct(product);
        forecast.setForecastedDemand(80);
        forecast.setForecastDate(LocalDate.now().plusDays(10));
        DemandForecast savedForecast = demandForecastService.createForecast(forecast);
        Assert.assertNotNull(savedForecast.getId());
    }

    @Test(priority = 91, groups = "integration")
    public void t65_repositories_forecast_inventory_suggestions_notNull() {
        List<DemandForecast> forecasts = demandForecastRepository.findAll();
        List<InventoryLevel> inventories = inventoryLevelRepository.findAll();
        List<TransferSuggestion> suggestions = transferSuggestionRepository.findAll();
        
        Assert.assertNotNull(forecasts);
        Assert.assertNotNull(inventories);
        Assert.assertNotNull(suggestions);
    }

    @Test(priority = 100, groups = "additional")
    public void t66_jwt_generateToken_and_extractEmail() {
        UserAccount user = new UserAccount();
        user.setEmail("jwtuser@test.com");
        user.setRole("ROLE_TEST");

        String token = jwtUtil.generateToken(user);
        Assert.assertNotNull(token);

        String email = jwtUtil.extractEmail(token);
        Assert.assertEquals(email, "jwtuser@test.com");
    }

    @Test(priority = 101, groups = "additional")
    public void t67_jwt_validateToken_forValidToken() {
        UserAccount user = new UserAccount();
        user.setEmail("valid@test.com");
        user.setRole("ROLE_USER");

        String token = jwtUtil.generateToken(user);
        boolean valid = jwtUtil.validateToken(token);
        Assert.assertTrue(valid);
    }

    @Test(priority = 102, groups = "additional")
    public void t68_jwt_extractRole_fromToken() {
        UserAccount user = new UserAccount();
        user.setEmail("roletest@test.com");
        user.setRole("ROLE_ADMIN");

        String token = jwtUtil.generateToken(user);
        String role = jwtUtil.extractRole(token);
        Assert.assertEquals(role, "ROLE_ADMIN");
    }

    @Test(priority = 103, groups = "additional")
    public void t69_jwt_validateToken_forInvalidToken() {
        String invalidToken = "invalid.token.here";
        boolean valid = jwtUtil.validateToken(invalidToken);
        Assert.assertFalse(valid);
    }

    @Test(priority = 104, groups = "additional")
    public void t70_accessApiWithoutToken_isRejected() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/stores"))
                .andReturn();
        int status = result.getResponse().getStatus();
        Assert.assertTrue(status >= 400);
    }

    @Test(priority = 105, groups = "additional")
    public void t71_accessApiWithToken_isAllowed() throws Exception {
        mockMvc.perform(
                        get("/api/stores")
                                .header("Authorization", bearer()))
                .andExpect(status().isOk());
    }

    @Test(priority = 106, groups = "additional")
    public void t72_normalization_storeNamesDistinct() {
        Store s1 = new Store();
        s1.setStoreName(uniqueStoreName("Norm-Store-1"));
        s1.setAddress("NS1");
        s1.setRegion("NR");
        Store saved1 = storeService.createStore(s1);

        Store s2 = new Store();
        s2.setStoreName(uniqueStoreName("Norm-Store-2"));
        s2.setAddress("NS2");
        s2.setRegion("NR");
        Store saved2 = storeService.createStore(s2);

        Assert.assertNotEquals(saved1.getStoreName(), saved2.getStoreName());
    }

    @Test(priority = 107, groups = "additional")
    public void t73_normalization_productSkuAndNameDistinct() {
        Product p1 = new Product();
        p1.setSku(uniqueSku("SKU-NORM-001"));
        p1.setName(uniqueProductName("NormItem-1"));
        p1.setCategory("N1");
        Product saved1 = productService.createProduct(p1);

        Product p2 = new Product();
        p2.setSku(uniqueSku("SKU-NORM-002"));
        p2.setName(uniqueProductName("NormItem-2"));
        p2.setCategory("N2");
        Product saved2 = productService.createProduct(p2);

        Assert.assertNotEquals(saved1.getSku(), saved2.getSku());
        Assert.assertNotEquals(saved1.getName(), saved2.getName());
    }

    @Test(priority = 108, groups = "additional")
    public void t74_multipleProducts_singleStore_viaForecast() {
        Store store = new Store();
        store.setStoreName(uniqueStoreName("Multi-Forecast-Store"));
        store.setAddress("MF Address");
        store.setRegion("MF Region");
        store = storeService.createStore(store);

        Product p1 = new Product();
        p1.setSku(uniqueSku("MF-SKU-1"));
        p1.setName(uniqueProductName("MF Product 1"));
        p1.setCategory("MF Category");
        p1 = productService.createProduct(p1);

        Product p2 = new Product();
        p2.setSku(uniqueSku("MF-SKU-2"));
        p2.setName(uniqueProductName("MF Product 2"));
        p2.setCategory("MF Category");
        p2 = productService.createProduct(p2);

        DemandForecast f1 = new DemandForecast();
        f1.setStore(store);
        f1.setProduct(p1);
        f1.setForecastedDemand(100);
        f1.setForecastDate(LocalDate.now().plusDays(8));
        demandForecastService.createForecast(f1);

        DemandForecast f2 = new DemandForecast();
        f2.setStore(store);
        f2.setProduct(p2);
        f2.setForecastedDemand(150);
        f2.setForecastDate(LocalDate.now().plusDays(8));
        demandForecastService.createForecast(f2);

        List<DemandForecast> forecasts = demandForecastService.getForecastsForStore(store.getId());
        Assert.assertTrue(forecasts.size() >= 2);
    }

    @Test(priority = 109, groups = "additional")
    public void t75_transferSuggestion_completeWorkflow() {
        Store sourceStore = new Store();
        sourceStore.setStoreName(uniqueStoreName("Transfer Source"));
        sourceStore.setAddress("TS Address");
        sourceStore.setRegion("TS Region");
        sourceStore = storeService.createStore(sourceStore);

        Store targetStore = new Store();
        targetStore.setStoreName(uniqueStoreName("Transfer Target"));
        targetStore.setAddress("TT Address");
        targetStore.setRegion("TT Region");
        targetStore = storeService.createStore(targetStore);

        Product product = new Product();
        product.setSku(uniqueSku("TRANSFER-WORKFLOW-SKU"));
        product.setName(uniqueProductName("Transfer Workflow Product"));
        product.setCategory("Transfer Category");
        product = productService.createProduct(product);

        TransferSuggestion suggestion = new TransferSuggestion();
        suggestion.setSourceStore(sourceStore);
        suggestion.setTargetStore(targetStore);
        suggestion.setProduct(product);
        suggestion.setQuantity(50);
        suggestion.setPriority("MEDIUM");
        suggestion.setStatus("PENDING");
        
        TransferSuggestion saved = transferSuggestionRepository.save(suggestion);
        Assert.assertNotNull(saved.getId());
        Assert.assertEquals(saved.getStatus(), "PENDING");
        Assert.assertEquals(saved.getPriority(), "MEDIUM");
    }
}