# shop – multi-module Maven demo

```
common  ──►  domain  ──►  services  ──►  app
                                          │
coverage (aggregates JaCoCo of all modules) ◄
```

| Module     | Depends on         | Contents |
|------------|--------------------|----------|
| `common`   | –                  | `StringUtils`, `MathUtils`, `Validator` |
| `domain`   | common             | `Money`, `Product`, `Customer`, `Order`, `ShippingCalculator` |
| `services` | domain, common     | pricing, inventory, orders, reports, loyalty, promotions, returns |
| `app`      | services           | `ShopApp` wiring, `CommandParser`, `Main` |
| `coverage` | all                | aggregated JaCoCo report |

## Build, test and coverage

```
mvn verify
```

* Per-module report: `<module>/target/site/jacoco/index.html`
* Combined report:   `coverage/target/site/jacoco-aggregate/index.html`

Coverage is intentionally partial (roughly 65–75%): some helpers, `Main`, and a few
branches are deliberately left untested.
