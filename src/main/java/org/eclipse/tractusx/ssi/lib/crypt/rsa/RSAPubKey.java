package org.eclipse.tractusx.ssi.lib.crypt.rsa;

import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.eclipse.tractusx.ssi.lib.crypt.IPublicKey;
import org.eclipse.tractusx.ssi.lib.model.base.EncodeType;
import org.eclipse.tractusx.ssi.lib.model.base.MultibaseFactory;

/**
 * @author Pascal Manaras <a href="mailto:manaras@xignsys.com">manaras@xignsys.com</a>
 */
public class RSAPubKey implements IPublicKey {

  private final RSAPublicKey publicKey;

  /**
   * @param encoded DER encoded bytes
   */
  public RSAPubKey(byte[] encoded) {
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      publicKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(encoded));
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public int getKeyLength() {
    return publicKey.getModulus().bitLength();
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
    return new byte[0];
  }

  public RSAPublicKey getPublicKey() {
    return publicKey;
  }
}
