package org.eclipse.tractusx.ssi.lib.crypt.ec;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.eclipse.tractusx.ssi.lib.crypt.IPublicKey;
import org.eclipse.tractusx.ssi.lib.model.base.EncodeType;
import org.eclipse.tractusx.ssi.lib.model.base.MultibaseFactory;

/**
 * @author Pascal Manaras <a href="mailto:manaras@xignsys.com">manaras@xignsys.com</a>
 */
public class ECPubKey implements IPublicKey {
  private final ECPublicKey publicKey;

  /**
   * @param encoded DER encoded bytes
   */
  public ECPubKey(byte[] encoded) {
    try {
      KeyFactory kf = KeyFactory.getInstance("EC");
      publicKey = (ECPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public int getKeyLength() {
    return SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())
        .getPublicKeyData()
        .getOctets()
        .length;
  }

  @Override
  public String asStringForStoring() {
    try {
      StringWriter stringWriter = new StringWriter();
      JcaPEMWriter pemWriter = new JcaPEMWriter(stringWriter);
      pemWriter.writeObject(publicKey);
      pemWriter.close();
      return stringWriter.toString();
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public String asStringForExchange(final EncodeType encodeType) {
    return MultibaseFactory.create(encodeType, publicKey.getEncoded()).getEncoded();
  }

  @Override
  public byte[] asByte() {
    return publicKey.getEncoded();
  }

  public ECPublicKey getPublicKey() {
    return publicKey;
  }
}
