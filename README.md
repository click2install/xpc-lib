### XPC-LIB
Java library for offline interaction with the XPC blockchain.
*Based on code from https://github.com/ValleZ/Paper-Wallet with all the Android code stripped out*

##### Create an address
```
var pair = AddressUtils.createTestnet();
var pair = AddressUtils.createTestnetLegacy();

var pair = AddressUtils.createMainnet();
var pair = AddressUtils.createMainnetLegacy();

var address = pair.address.toString();
var privkey = pair.privateKey.privateKeyEncoded;
```

##### Verify an address
```
var valid = Address.verify(address);
```

##### Create a transaction
3 method overrides are provided to create an XPC transaction in the `Utils` class.

```
var tx = Utils.createTransaction(Transaction baseTransaction, int indexOfOutputToSpend, long confirmations, String outputAddress, String changeAddress, long amountToSend, float satoshisPerVirtualByte, KeyPair keys, @TransactionType int transactionType);

var tx = Utils.createTransaction(byte[] hashOfPrevTransaction, long valueOfUnspentOutput, Transaction.Script scriptOfUnspentOutput, int indexOfOutputToSpend, long confirmations, String outputAddress, String changeAddress, long amountToSend, float satoshisPerVirtualByte, KeyPair keys, @TransactionType int transactionType);

var tx = Utils.createTransaction(List<UnspentOutputInfo> unspentOutputs, String outputAddress, String changeAddress, final long amountToSend, final float satoshisPerVirtualByte, @TransactionType int transactionType);
```