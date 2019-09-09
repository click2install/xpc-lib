
package io.xpchain.btc;

/**
 *
 * @author Maurice
 */
public class AddressUtils {

    public static KeyPair createMainnet() {
        return Utils.generateWifKey(false, Address.PUBLIC_KEY_TO_ADDRESS_P2WKH);
    }

    public static KeyPair createMainnetLegacy() {
        return Utils.generateWifKey(false, Address.PUBLIC_KEY_TO_ADDRESS_LEGACY);
    }

    public static KeyPair createTestnet() {
        return Utils.generateWifKey(true, Address.PUBLIC_KEY_TO_ADDRESS_P2WKH);
    }

    public static KeyPair createTestnetLegacy() {
        return Utils.generateWifKey(true, Address.PUBLIC_KEY_TO_ADDRESS_LEGACY);
    }
    
}
