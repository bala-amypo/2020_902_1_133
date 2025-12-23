public interface StoreService {
    Store createStore(Store store);
    Store updateStore(Long id, Store store);
    void deleteStore(Long id);
}
@Service
public class StoreServiceImpl implements StoreService {

    public Store createStore(Store store) {
        return store;
    }

    public Store updateStore(Long id, Store store) {
        return store;
    }

    public void deleteStore(Long id) {
    }
}
