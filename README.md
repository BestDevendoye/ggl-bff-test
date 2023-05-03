# GGL-BFF-TEST
BFF end-to-end testing

This project provides non-regression tests scoped on the BFF.
Integration tests are written using scenarios based on [Karate](https://github.com/intuit/karate), and we reuse these ones for performance testing using [Gatling](https://github.com/gatling/gatling) integration.

Available env:
- int
- demo
- preprod

### Build
```shell
$ ./mvnw clean package
```

## Integration testing

Karate:

* Documentation: https://intuit.github.io/karate
* Github: https://github.com/intuit/karate

### Run
```shell
$ ./mvnw clean test -Pintegration-testing -Dkarate.env=<ENV>
```

## Performance testing

Gatling integration:

* Documentation: https://intuit.github.io/karate/karate-gatling
* Github: https://github.com/intuit/karate/tree/master/karate-gatling

### Run
```shell
$ ./mvnw clean test -Pperformance-testing -Dkarate.env=<ENV>
```

### Scenarii  description 
* Scenarii https://docs.google.com/spreadsheets/d/13CQjo_GmtssOw6A00ilF_yjZEQQIJy7CnN6THPJx4gs/
* data demo : https://docs.google.com/spreadsheets/d/1NWfCzqy-QRTx3dD7FRG1wCXWf77nZt9x/
* data pp : https://docs.google.com/spreadsheets/d/1-FQdYN7wZJpL0TWRsNYj5W2xcw_Ev894/
