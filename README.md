### XPC-LIB
*Based on code from https://github.com/ValleZ/Paper-Wallet with all the Android code stripped out*

Java library for offline interaction with the XPC blockchain.

##### Create an address
```
var address = AddressUtils.createTestnet();
var address = AddressUtils.createTestnetLegacy();

var address = AddressUtils.createMainnet();
var address = AddressUtils.createMainnetLegacy();
```

##### Create a transaction
3 method overrides are provided to create an XPC transaction in the `Utils` class.

```
var tx = Utils.createTransaction(Transaction baseTransaction, int indexOfOutputToSpend, long confirmations, String outputAddress, String changeAddress, long amountToSend, float satoshisPerVirtualByte, KeyPair keys, @TransactionType int transactionType);

var tx = Utils.createTransaction(byte[] hashOfPrevTransaction, long valueOfUnspentOutput, Transaction.Script scriptOfUnspentOutput, int indexOfOutputToSpend, long confirmations, String outputAddress, String changeAddress, long amountToSend, float satoshisPerVirtualByte, KeyPair keys, @TransactionType int transactionType);

var tx = Utils.createTransaction(List<UnspentOutputInfo> unspentOutputs, String outputAddress, String changeAddress, final long amountToSend, final float satoshisPerVirtualByte, @TransactionType int transactionType);
```