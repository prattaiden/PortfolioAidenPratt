import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;

public class Shared {

    static final public String safe_2048bit_prime_number = "FFFFFFFFFFFFFFFFC90FDAA22168C234C4C6628B80DC1CD129024E08" +
            "8A67CC74020BBEA63B139B22514A08798E3404DDEF9519B3CD3A431B302B0A6DF25F14374FE1356D6D51C245" +
            "E485B576625E7EC6F44C42E9A637ED6B0BFF5CB6F406B7EDEE386BFB5A899FA5AE9F24117C4B1FE649286651ECE45B3D" +
            "C2007CB8A163BF0598DA48361C55D39A69163FA8FD24CF5F83655D23DCA3AD961C62F356208552BB9ED529077096966D" +
            "670C354E4ABC9804F1746C08CA18217C32905E462E36CE3BE39E772C180E86039B2783A2EC07A28FB5C55DF06F4C52C9" +
            "DE2BCBF6955817183995497CEA956AE515D2261898FA051015728E5A8AACAA68FFFFFFFFFFFFFFFF";

    static BigInteger N = new BigInteger(safe_2048bit_prime_number, 16);
    static BigInteger g = new BigInteger("2");

    //used to get the public DH key in the client and server
    public static BigInteger getDHPublicKey(BigInteger DHPrivKey) {
        return g.modPow(DHPrivKey, N);
    }

    //creating and getting a certificate from the filepath
    public static Certificate getCertificate(String filePath) {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            CertificateFactory CF = CertificateFactory.getInstance("X.509");
            Certificate certificate = CF.generateCertificate(inputStream);
            return certificate;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean verifyKeys(Certificate certificate, byte[] dhKey, byte[] dhSignedKey, Certificate caCertificate) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException {
        Signature signature = Signature.getInstance("SHA256WithRSA");
        signature.initVerify(certificate.getPublicKey());
        certificate.verify(caCertificate.getPublicKey());
        signature.update(dhKey);
        return signature.verify(dhSignedKey);

    }


    public static BigInteger getSignedDHPubKey(String privateKeyPath, BigInteger pubDHcKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        //read in server rsa private key
        FileInputStream ServerRSAPriv = new FileInputStream(privateKeyPath);
        byte[] serverPrivKeyBytes = ServerRSAPriv.readAllBytes();
        PKCS8EncodedKeySpec encodedServerPrivKey = new PKCS8EncodedKeySpec(serverPrivKeyBytes);

        //generate an RSA key from that
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //Encoding RSA Key from original Der serverPrivKey
        PrivateKey serverRSAPrivKey = keyFactory.generatePrivate(encodedServerPrivKey);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(serverRSAPrivKey);
        signature.update(pubDHcKey.toByteArray());//adding the data from the public DH key which will allow it to be signed
        byte[] signedPublicDHKey = signature.sign();

        return new BigInteger(signedPublicDHKey);
        //returns signed DH public key
    }

    public static BigInteger computeSharedSecret(BigInteger privateKey, BigInteger publicKey) {
        return publicKey.modPow(privateKey, N);
    }

    public static byte[] hdkfExpand(byte[] input, String tag) { // tag is a string, but that's easily converted to byte[]
        try{
            Mac HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec spec = new SecretKeySpec(input, "RawBytes");
            HMAC.init(spec);
            HMAC.update(tag.getBytes());
            HMAC.update((byte)0x01);
            return Arrays.copyOf(HMAC.doFinal(), 16);
        } catch (NoSuchAlgorithmException | InvalidKeyException e)  {
            throw new RuntimeException(e);
        }
    }

    public static byte[] computeHMAC(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec key2 = new SecretKeySpec(key, "HmacSHA256");
        mac.init(key2);
        return mac.doFinal(data);
    }

    public static Cipher createCipher(Boolean isEncrptCipher, byte[] encryptKey, byte[] initializationVector) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException {
        //Create an AES/CBC/PKCS5Padding cipher
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        //Initialize a SecretKeySpec with encryptKey for AES encryption
        SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey, "AES");

        //Initialize an IvParameterSpec with the initializationVector key
        IvParameterSpec ivParameterSpec = new IvParameterSpec(initializationVector);

        //Check if this cipher is for encrytion or decrytion
        if(isEncrptCipher){
            //Initialize the cipher in ENCRYPT_MODE with the SecretKeySpec and IvParameterSpec
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec); //decrpt mode
        }
        else{
            //Initialize the cipher in DECRYPT_MODE with the SecretKeySpec and IvParameterSpec
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec); //decrpt mode
        }

        return cipher;
    }

    public static byte[] encrypt(byte[] message, byte[] encryptKey, byte[] initializationVector, byte[] macKey) throws IOException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        //Initialize message to byte array output stream
        ByteArrayOutputStream encryptedMessage = new ByteArrayOutputStream();
        encryptedMessage.write(message);

        //Generate a MAC for the message using macKey and append this MAC to the end of msg_byteArrayOutputStream
        byte[] macMsg = computeHMAC(message, macKey);
        encryptedMessage.write(macMsg);

        //Create a AES/CBC/PKCS5Padding cipher for encryption with encryptKey and initializationVector
        Cipher cipher = createCipher(true, encryptKey, initializationVector);

        //Using the cipher, encrypt the data in encryptedMessage
        return cipher.doFinal(encryptedMessage.toByteArray());

    }

    public static String decrypt(byte[] cipherText, byte[] encryptKey, byte[] initializationVector) throws InvalidAlgorithmParameterException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        //Create an AES/CBC/PKCS5Padding cipher for decryption with encryptKey and initializationVector
        Cipher cipher = createCipher(false, encryptKey, initializationVector);

        //Decrypt the cipherText using the decryption cipher to get plainText
        byte[] plainText = cipher.doFinal(cipherText);

        //Separate the decrypted data into the original message (w.o MAC)
        byte[] decryptedMsg = Arrays.copyOf(plainText, plainText.length - 32);

        return new String(decryptedMsg, StandardCharsets.UTF_8);

    }


}
