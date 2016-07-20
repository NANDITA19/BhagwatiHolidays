package extras;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by rutvik on 07-04-2016 at 05:05 PM.
 */
public class ICICIStringEncryptor {

    public static String encrypt(String inputParam) {

        String key="1000240268105017";

        byte[] abyte2 = (byte[]) null;

        byte[] abyte1 = key.getBytes();

        SecretKeySpec secretkeyspec = new SecretKeySpec(abyte1, "AES");

        SecretKeySpec secretkeyspec1 = secretkeyspec;

        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(1, secretkeyspec1);

            abyte2 = cipher.doFinal(inputParam.getBytes());

        } catch (Exception e) {

            e.printStackTrace();

        }

        String ur_enc_str = Base64Coder.encodeLines(abyte2);

        return ur_enc_str;

    }

}
