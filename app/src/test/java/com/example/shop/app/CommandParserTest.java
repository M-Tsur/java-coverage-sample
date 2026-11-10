package com.example.shop.app;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CommandParserTest {

    @Test
    void parsesNameAndArgs() {
        CommandParser.Command c = CommandParser.parse("  ADD LAP-0001   3 ");
        assertEquals("add", c.name());
        assertEquals(java.util.List.of("LAP-0001", "3"), c.args());
    }

    @Test
    void blankRejected() {
        assertThrows(IllegalArgumentException.class, () -> CommandParser.parse("  "));
    }

    @Test
    void addCommandUpdatesStock() {
        ShopApp app = new ShopApp();
        String out = CommandParser.execute(app, CommandParser.parse("add LAP-0001 5"));
        assertEquals("stock updated", out);
        assertEquals(10, app.inventory().available("LAP-0001"));
    }

    @Test
    void unknownCommand() {
        assertEquals("unknown command: foo", CommandParser.execute(new ShopApp(), CommandParser.parse("foo")));
    }
}
