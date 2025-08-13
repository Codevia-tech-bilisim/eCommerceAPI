package com.huseyinsen.security;// AWS SDK v2 örneği (pom / gradle ile aws-kms eklenmeli)
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.kms.KmsClient;
import software.amazon.awssdk.services.kms.model.EncryptRequest;
import software.amazon.awssdk.services.kms.model.DecryptRequest;

public class KmsUtil {
    private final KmsClient kms;
    private final String keyId;

    public KmsUtil(KmsClient kms, String keyId) { this.kms = kms; this.keyId = keyId; }

    public byte[] encrypt(byte[] plaintext) {
        EncryptRequest req = EncryptRequest.builder()
                .keyId(keyId)
                .plaintext(SdkBytes.fromByteArray(plaintext))
                .build();
        return req == null ? null : kms.encrypt(req).ciphertextBlob().asByteArray();
    }

    public byte[] decrypt(byte[] ciphertext) {
        DecryptRequest req = DecryptRequest.builder()
                .ciphertextBlob(SdkBytes.fromByteArray(ciphertext))
                .build();
        return kms.decrypt(req).plaintext().asByteArray();
    }
}