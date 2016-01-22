/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vab.iflex.gateway.paybillservice;

/**
 *
 * @author nhutlm
 */
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.commons.codec.binary.Base64;

public class Stub {

    private final static String HMAC_SHA1_ALGORITHM = "HMACSHA1";

    /**
     * @param accesskey
     * @param partnercode
     * @param signature
     * @return
     * <securityHeader><accesskey></accesskey><timestamp></timestamp><signature
     * ></signature></securityHeader>
     */
    public static SOAPHeaderElement setSoapHeaderElement(String accesskey, String method, String signature) throws Exception {
        SOAPHeaderElement oHeaderElement;
        javax.xml.soap.SOAPElement oElement;

        try {
            oHeaderElement = new SOAPHeaderElement("http://phn.com.vn", "securityHeader");
            oHeaderElement.setPrefix("sec");
            oHeaderElement.setMustUnderstand(false);

            Date now = new Date();
            SimpleDateFormat sdate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String sDateNow = sdate.format(now);

            String signnow = calculateRFC2104HMAC(method + sDateNow, signature);

            oElement = oHeaderElement.addChildElement("accesskey");
            oElement.addTextNode(accesskey);
            oElement = oHeaderElement.addChildElement("timestamp");
            oElement.addTextNode(sDateNow);
            oElement = oHeaderElement.addChildElement("signature");
            oElement.addTextNode(signnow);

            return oHeaderElement;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static String calculateRFC2104HMAC(String data, String key) throws Exception {
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            // base64-encode the hmac
//            result = new BASE64Encoder().encode(rawHmac);
            result = new Base64().encodeAsString(rawHmac);

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
}

