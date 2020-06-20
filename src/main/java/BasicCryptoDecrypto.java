public class BasicCryptoDecrypto implements CryptoDecrypto {
    private int key = 4;

    @Override
    public byte[] encrypt(byte[] data) {
        byte[] enc = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            enc[i] = (byte) ((i % 2 == 0) ? data[i] + key : data[i] - key);
        return enc;
    }

    @Override
    public byte[] decrypt(byte[] data) {
        byte[] dec = new byte[data.length];
        for (int i = 0; i < data.length; i++)
            dec[i] = (byte) ((i % 2 == 0) ? data[i] - key : data[i] + key);
        return dec;
    }
}
