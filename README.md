# prometheus-java-annotation
simple annotation for prometheus summary

There is an option in the gradle build to build a fatjar if that is what you would like, otherwise the regular jar tasks should suffice.

```
    @Path("cream")
    @GET
    @PrometheusSummaryTimer(name = "getCoffee_seconds", help = "help get coffee method in coffee resources", labelNames = {"beverage", "style"}, labels = {"coffee_cream", "iced"})
    public String getCoffeeWithCream() {
        coffees = new Coffees();
        return coffees.retrieveCoffeeWithCream();
    }
```
