package core.basesyntax.strategy.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class PurchaseOperationTest {
    private static final String APPLE = "apple";
    private static final int DEFAULT_QUANTITY = 10;
    private static final int PART_OF_DEFAULT_QUANTITY = 1;
    private static final int ZERO_QUANTITY = 0;
    private static final int NEGATIVE_QUANTITY = -1;
    private static OperationHandler operationHandler;

    @BeforeAll
    static void beforeAll() {
        operationHandler = new PurchaseOperation();
    }

    @AfterEach
    void afterEach() {
        Storage.clear();
    }

    @Test
    void process_purchaseNegativeQuantityFruits_notOk() {
        Storage.addFruit(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, APPLE, NEGATIVE_QUANTITY
        );
        assertThrows(IllegalArgumentException.class,
                () -> operationHandler.process(fruitTransaction));
    }

    @Test
    void process_purchaseMoreThanTotalQuantityFruits_noOk() {
        Storage.addFruit(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, APPLE,
                DEFAULT_QUANTITY + PART_OF_DEFAULT_QUANTITY
        );
        assertThrows(IllegalArgumentException.class,
                () -> operationHandler.process(fruitTransaction));
    }

    @Test
    void process_purchaseZeroFruits_notOk() {
        Storage.addFruit(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, APPLE, ZERO_QUANTITY
        );
        assertThrows(IllegalArgumentException.class,
                () -> operationHandler.process(fruitTransaction));
    }

    @Test
    void process_purchaseAllOfQuantity_ok() {
        Storage.addFruit(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, APPLE, DEFAULT_QUANTITY
        );
        operationHandler.process(fruitTransaction);
        assertEquals(0, Storage.getFruits().get(APPLE));
    }

    @Test
    void process_purchasePartOfFruits_ok() {
        Storage.addFruit(APPLE, DEFAULT_QUANTITY);
        FruitTransaction fruitTransaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, APPLE, PART_OF_DEFAULT_QUANTITY
        );
        operationHandler.process(fruitTransaction);
        assertEquals(DEFAULT_QUANTITY - PART_OF_DEFAULT_QUANTITY, Storage.getFruits().get(APPLE));
    }
}
