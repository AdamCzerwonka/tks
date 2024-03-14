package com.example.pasik.jws;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.UUID;

@Component
public class Jws {
    RSAKey rsaJWK;
    RSAKey publicKey;

    private final String secretKey = "veryVerySecretKeyasdfasdasdj718ye12bny7gh12873b12yh3b890712";

    public Jws() {
        try {
            this.rsaJWK = new RSAKeyGenerator(2048)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE)
                    .keyID("1")
                    .generate();

            this.publicKey = rsaJWK.toPublicJWK();
        } catch (Exception ignored) {
        }

    }

    public String sign(String unsigned) throws JOSEException {
        Payload payload = new Payload(unsigned);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), payload);
        JWSSigner signer = new MACSigner(secretKey);
        jwsObject.sign(signer);

        return jwsObject.serialize();
    }

    public boolean verify(String token) throws ParseException, JOSEException {
        JWSObject jwsObject = JWSObject.parse(token);
        JWSVerifier verifier = new MACVerifier(secretKey);
        return jwsObject.verify(verifier);
    }

    public boolean verifySign(String token, UUID id) {
        try {
            if (!verify(token)) return false;
            String newSign = sign(id.toString());
            return token.equals(newSign);
        } catch (ParseException | JOSEException e) {
            return false;
        }
    }
}
