public interface InventoryLevelService {
    InventoryLevel createOrUpdateInventory(InventoryLevel inventory);
    List<InventoryLevel> getInventoryForStore(Long storeId);
    List<InventoryLevel> getInventoryForProduct(Long productId);
}
@Service
public class InventoryLevelServiceImpl implements InventoryLevelService {

    public InventoryLevel createOrUpdateInventory(InventoryLevel inventory) {
        return inventory;
    }

    public List<InventoryLevel> getInventoryForStore(Long storeId) {
        return List.of();
    }

    public List<InventoryLevel> getInventoryForProduct(Long productId) {
        return List.of();
    }
}
