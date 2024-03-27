import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;


public class Client {
    static BigInteger clientDHPriv_;
    private static byte[] prk_;
    private static byte[] clientEncrypt_;
    private static byte[] clientMac_;
    private static byte[] clientIV_;
    private static byte[] serverEncrypt_;
    private static byte[] serverMac_;
    private static byte[] serverIV_;




    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, CertificateException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        //--setting up the client's ports//
        final int server_port = 5678;
        Socket clientSocket = new Socket("localhost", server_port);
        ByteArrayOutputStream history = new ByteArrayOutputStream();
        ObjectOutputStream clientOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());

        //--------------------------------------handshake------------------------------------------//
        System.out.println("\n");
        System.out.println("\t---------------client beginning handshake process ----------------");

        Certificate CA = Shared.getCertificate("../../CAcertificate.pem");
        //------------------------------(part1 - send nonce)------------------------------------------//
        //generating the nonce to send to server
        BigInteger nonce = new BigInteger(new SecureRandom().generateSeed(32));
        clientOutputStream.writeObject(nonce);
        history.write(nonce.toByteArray());
        System.out.println("[CLIENT]: has sent: nonce");


        //---------------------------------(part2 - recieve first server message)---------------------------------//
        ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
        Certificate serverCert = (Certificate) inputStream.readObject(); //read Certificate from Server
        BigInteger serverDHPublicKey = (BigInteger) inputStream.readObject(); //read DH public from server
        BigInteger signedServerDHKey = (BigInteger) inputStream.readObject(); //read signed DH from server

        history.write(serverCert.getEncoded());
        history.write(serverDHPublicKey.toByteArray());
        history.write(signedServerDHKey.toByteArray());
        System.out.println("[CLIENT]: has received: server certificate, server DH pub key, signed server DH key");

        assert CA != null;
        Boolean KeysVerified = Shared.verifyKeys(serverCert, serverDHPublicKey.toByteArray(), signedServerDHKey.toByteArray(), CA);

        assert KeysVerified;
        System.out.println("[CLIENT]: Keys are verified: " + KeysVerified);


        //---------------------------------(part3 - client sends)---------------------------------//
        //server and client agree on DH parameters g and N
        //creating the client DH public key
       clientDHPriv_ = new BigInteger(new SecureRandom().generateSeed(32));
       BigInteger clientDHPubKey = Shared.getDHPublicKey(clientDHPriv_);
       Certificate clientCertificate = Shared.getCertificate("../../CASignedClientCertificate.pem");
       BigInteger signedClientDHPubKey = Shared.getSignedDHPubKey("../../clientPrivateKey.der", clientDHPubKey);

       clientOutputStream.writeObject(clientCertificate);
       clientOutputStream.writeObject(clientDHPubKey);
       clientOutputStream.writeObject(signedClientDHPubKey);

        assert clientCertificate != null;
       history.write(clientCertificate.getEncoded());
       history.write(clientDHPubKey.toByteArray());
       history.write(signedClientDHPubKey.toByteArray());

        System.out.println("[CLIENT]: has sent: client certificate, client DH public key, signed client public DH key");

        //---------------------------------(part4- compute shared DH secret!)---------------------------------//
       BigInteger sharedSecret = Shared.computeSharedSecret(clientDHPriv_, serverDHPublicKey);
//       System.out.println("private secret! " + Arrays.toString(sharedSecret.toByteArray()));

        //---------------(part5 - generate shared secret keys to send hmac of handshake history)---------------------//
       makeClientSecretKeys(nonce.toByteArray(),sharedSecret.toByteArray());
       //reading in the hmac of the server's history
       byte[] serverHMAC = (byte[]) inputStream.readObject();
       //using the serverMac_ to verify the read in serverHMAC matches
       byte[] verifyServerMac = Shared.computeHMAC(serverMac_, history.toByteArray());
       if(Arrays.equals(serverHMAC, verifyServerMac)){
            System.out.println("[CLIENT]: handshake successful, hmac of handshake history match!");
       }
       else{
            System.out.println("[CLIENT]: handshake failed with wrong hmac of handshake history");
        }
       //adding it to history before sending back client version
       history.write(serverHMAC);
       System.out.println("[CLIENT]: has received: HMAC of message history from server");

       byte[] hmac = Shared.computeHMAC(clientMac_, history.toByteArray());

       clientOutputStream.writeObject(hmac);
       System.out.println("[CLIENT]: has sent: the HMAC of message history");
       //final history add, maybe not necessary
       history.write(hmac);


        //--------------------------------------MESSAGES-----------------------------------------//
        System.out.println("\n");
        System.out.println("\t---------------client beginning messaging process ----------------");
        byte[] serverEncMessage1 = (byte[]) inputStream.readObject();
        String decrpytedMSGFromServer = Shared.decrypt(serverEncMessage1, serverEncrypt_, serverIV_);
        System.out.println("[CLIENT]: encrypted message from server: " + Arrays.toString(serverEncMessage1));
        System.out.println("[CLIENT]: decrypted message from server: " + decrpytedMSGFromServer);

        byte[] clientMessage1 = "this is the first message from the client.".getBytes();
        byte[] encryptedClientMessage1 = Shared.encrypt(clientMessage1, clientEncrypt_, clientIV_, clientMac_);
        String ogMessage = new String(clientMessage1, StandardCharsets.UTF_8);
        System.out.println("[CLIENT]: og message: " + ogMessage);
        System.out.println("[CLIENT]: encrypted message: " + Arrays.toString(encryptedClientMessage1));

        clientOutputStream.writeObject(encryptedClientMessage1);

       clientSocket.close();
    }

    //makeSecretKeys method
    private static void makeClientSecretKeys(byte[] ogNonce, byte[] DHSharedSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(DHSharedSecret, "HmacSHA256");
        mac.init(spec);
        prk_ = mac.doFinal(DHSharedSecret);
        clientEncrypt_ = Shared.hdkfExpand(prk_, "client encrypt");
        clientMac_ = Shared.hdkfExpand(clientEncrypt_, "client MAC");
        clientIV_ = Shared.hdkfExpand(clientMac_, "client IV");
        serverEncrypt_ = Shared.hdkfExpand(prk_, "server encrypt");
        serverMac_ = Shared.hdkfExpand(serverEncrypt_, "server MAC");
        serverIV_ = Shared.hdkfExpand(serverMac_, "server IV");


    }
}
