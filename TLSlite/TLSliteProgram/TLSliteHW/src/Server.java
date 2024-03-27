import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Server {
    static BigInteger serverDHpriv_;
    private static byte[] prk_;
    private static byte[] serverEncrypt_;
    private static byte[] serverMac_;
    private static byte[] serverIV_;
    private static byte[] clientEncrypt_;
    private static byte[] clientMac_;
    private static byte[] clientIV_;

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException, SignatureException, InvalidKeyException, CertificateException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
        //--setting up server--//
        final int server_port = 5678;
        ServerSocket serverListener = new ServerSocket(server_port);
        ByteArrayOutputStream history = new ByteArrayOutputStream();
        System.out.println("\n");
        System.out.println("listening at port: " + server_port + "...");
        Socket socket = serverListener.accept();
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
        System.out.println("heard from " + socket.getInetAddress() + " port: " + socket.getPort());


        //----------------------------------------handshake--------------------------------------//
        System.out.println("\n");
        System.out.println("\t---------------server beginning handshake process ----------------");
        //----------------------------(part1 - recieve nonce)----------------------------//
        //recieving nonce from the client
        Certificate CA = Shared.getCertificate("../../CAcertificate.pem");
        BigInteger recievedNonce = (BigInteger) input.readObject();
        history.write(recievedNonce.toByteArray());
        System.out.println("[SERVER]: received: nonce from client");
//        System.out.println(recievedNonce);

        //-------------(part2 - send the server certificate, serverDHPubKey, and signed DH public key)--------------//
        //creating a server DH private key AND the DH public key

        serverDHpriv_ = new BigInteger(new SecureRandom().generateSeed(32));
        BigInteger serverDHPubKey = Shared.getDHPublicKey(serverDHpriv_);
        Certificate serverCertificate = Shared.getCertificate("../../CASignedServerCertificate.pem");
        BigInteger signedServerDHPublicKey = Shared.getSignedDHPubKey("../../serverPrivateKey.der", serverDHPubKey);

        //sending
        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.writeObject(serverCertificate);
        outputStream.writeObject(serverDHPubKey);
        outputStream.writeObject(signedServerDHPublicKey);
        //writing these to history
        assert serverCertificate != null;
        history.write(serverCertificate.getEncoded());
        history.write(serverDHPubKey.toByteArray());
        history.write(signedServerDHPublicKey.toByteArray());
        System.out.println("[SERVER]: has sent: server certificate, server DH public key, signed server DH public key");

        //----------------(part3 - recieve client certificate, clientDHPubKey, and signed client DH public key)--------------------//
        Certificate clientCertificate = (Certificate) input.readObject(); //read client certificate
        BigInteger clientDHPubKey = (BigInteger) input.readObject(); //read client DH pub key
        BigInteger signedClientDHPubKey = (BigInteger) input.readObject(); //read singed dh pub key

        history.write(clientCertificate.getEncoded());
        history.write(clientDHPubKey.toByteArray());
        history.write(signedClientDHPubKey.toByteArray());
        System.out.println("[SERVER]: has received: client certificate, client DH public key, signed client DH public key");

        assert CA != null;
        Boolean KeysVarified = Shared.verifyKeys(clientCertificate, clientDHPubKey.toByteArray(), signedClientDHPubKey.toByteArray(), CA);

        assert KeysVarified;
        System.out.println("[SERVER]: Keys are verified: " + KeysVarified);


        //-----------------------------(part4- compute shared DH secret!)--------------------------------------------//
        BigInteger sharedSecret = Shared.computeSharedSecret(serverDHpriv_, clientDHPubKey);
        //System.out.println("private secret! " + Arrays.toString(sharedSecret.toByteArray()));

        //---------------(part5 - generate shared secret keys to send hmac of handshake history)---------------------//
        makeServerSecretKeys(recievedNonce.toByteArray(), sharedSecret.toByteArray());
        byte[] hmac = Shared.computeHMAC(serverMac_, history.toByteArray());
        outputStream.writeObject(hmac);
        //final history add
        history.write(hmac);
        System.out.println("[SERVER]: has sent: HMAC of all message history");

        //reading the clients last message
        byte[] clientHMAC = (byte[]) input.readObject();
        //verify with updated history
        byte[] verifyClientMac = Shared.computeHMAC(clientMac_, history.toByteArray());
        if(Arrays.equals(clientHMAC, verifyClientMac)){
            System.out.println("[SERVER]: handshake successful, hmac of handshake history match!");
        }
        else{
            System.out.println("[SERVER]: handshake failed with wrong hmac of handshake history");
        }
        System.out.println("[SERVER]: has received HMAC of message history from client");


        //---------------------------------------------MESSAGES--------------------------------------------//
        System.out.println("\n");
        System.out.println("\t---------------server beginning messaging process ----------------");
        byte[] serverMessage1 = "This is the first message from the server.".getBytes();
        byte[] encryptedServerMSG = Shared.encrypt(serverMessage1, serverEncrypt_, serverIV_,serverMac_);

        String ogMessage = new String(serverMessage1, StandardCharsets.UTF_8);
        System.out.println("[SERVER]: og message: " + ogMessage);
        System.out.println("[SERVER]: encrypted message: " + Arrays.toString(encryptedServerMSG));
        //writing out the encrypted message to the client and the hmac
        outputStream.writeObject(encryptedServerMSG);

        byte[] clientEncMessage1 = (byte[]) input.readObject();
        String clientDecryptedMessage1 = Shared.decrypt(clientEncMessage1, clientEncrypt_, clientIV_);
        System.out.println("[SERVER]: client enc message: " + Arrays.toString(clientEncMessage1));
        System.out.println("[SERVER]: client decrypted message: " + clientDecryptedMessage1);


        socket.close();
        serverListener.close();
    }

    private static void makeServerSecretKeys(byte[] ogNonce, byte[] DHSharedSecret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec spec = new SecretKeySpec(DHSharedSecret, "HmacSHA256");
        mac.init(spec);
        prk_ = mac.doFinal(DHSharedSecret);
        serverEncrypt_ = Shared.hdkfExpand(prk_, "server encrypt");
        serverMac_ = Shared.hdkfExpand(serverEncrypt_, "server MAC");
        serverIV_ = Shared.hdkfExpand(serverMac_, "server IV");
        clientEncrypt_ = Shared.hdkfExpand(prk_, "client encrypt");
        clientMac_ = Shared.hdkfExpand(clientEncrypt_, "client MAC");
        clientIV_ = Shared.hdkfExpand(clientMac_, "client IV");
    }

}
