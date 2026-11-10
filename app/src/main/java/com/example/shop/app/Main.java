package com.example.shop.app;

/** Command line entry point. */
public final class Main {

    private Main() {}

    public static void main(String[] args) {
        ShopApp app = new ShopApp();
        if (args.length > 0 && "--csv".equals(args[0])) {
            app.runDemo();
            System.out.print(app.reports().toCsv());
        } else if (args.length > 0 && "--help".equals(args[0])) {
            System.out.println("usage: shop [--csv | --help]");
        } else {
            System.out.print(app.runDemo());
        }
    }
}
