package com.example.daron.receiptsaver;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReceiptTest {
    private Receipt receipt;

    @Before
    public void setUp() throws Exception {
        receipt = new Receipt("Walmart", "Grocery", "4/26/18", 22.22, "bought groceries", "");
    }
    @Test
    public void getId() {
        assertEquals("Walmart", receipt.getName());
    }
}