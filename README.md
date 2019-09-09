### XPC-LIB
*Mostly based on code from https://github.com/ValleZ/Paper-Wallet with all the Android code stipped out*

##### Create an address
```
var address = AddressUtils.createTestnet();
var address = AddressUtils.createTestnetLegacy();

var address = AddressUtils.createMainnet();
var address = AddressUtils.createMainnetLegacy();
```

##### Create a transaction
3 Overrides are provided to create an XPC transaction in the `Utils` class.

```
var tx = Utils.createTransaction( ... );
```