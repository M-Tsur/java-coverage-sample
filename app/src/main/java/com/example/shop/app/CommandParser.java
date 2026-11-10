package com.example.shop.app;

import com.example.shop.common.StringUtils;
import java.util.ArrayList;
import java.util.List;

/** Parses simple text commands such as "add LAP-0001 2". */
public final class CommandParser {

    public static final class Command {
        private final String name;
        private final List<String> args;

        Command(String name, List<String> args) {
            this.name = name;
            this.args = args;
        }

        public String name() {
            return name;
        }

        public List<String> args() {
            return args;
        }
    }

    private CommandParser() {}

    public static Command parse(String line) {
        if (StringUtils.isBlank(line)) {
            throw new IllegalArgumentException("empty command");
        }
        String[] parts = line.trim().split("\\s+");
        List<String> args = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            args.add(parts[i]);
        }
        return new Command(parts[0].toLowerCase(), args);
    }

    public static String help() {
        return String.join("\n",
                "Commands:",
                "  add <sku> <qty>      add stock",
                "  order <sku> <qty>    place an order",
                "  pay <orderId>        pay an order",
                "  ship <orderId>       ship an order",
                "  cancel <orderId>     cancel an order",
                "  report               print the sales report",
                "  quit                 exit");
    }

    public static String execute(ShopApp app, Command cmd) {
        switch (cmd.name()) {
            case "add":
                app.inventory().add(cmd.args().get(0), Integer.parseInt(cmd.args().get(1)));
                return "stock updated";
            case "pay":
                app.orders().pay(cmd.args().get(0));
                return "paid";
            case "ship":
                app.orders().ship(cmd.args().get(0));
                return "shipped";
            case "cancel":
                app.orders().cancel(cmd.args().get(0));
                return "cancelled";
            case "report":
                return app.reports().summary();
            case "help":
                return help();
            default:
                return "unknown command: " + cmd.name();
        }
    }
}
