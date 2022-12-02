package com.bnn.hsm.util;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import sun.security.x509.X509CertImpl;

import javax.xml.crypto.*;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.security.Key;
import java.security.KeyException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CertExtractor {
    private static class SimpleKeySelectorResult implements KeySelectorResult {
        private PublicKey pk;
        SimpleKeySelectorResult(PublicKey pk) {
            this.pk = pk;
        }
        public Key getKey() { return pk; }
    }
    private static class KeyValueKeySelector extends KeySelector {
        public KeySelectorResult select(KeyInfo keyInfo,
                                        Purpose purpose,
                                        AlgorithmMethod method,
                                        XMLCryptoContext context)
                throws KeySelectorException {
            if (keyInfo == null) {
                throw new KeySelectorException("Null KeyInfo object!");
            }
            SignatureMethod sm = (SignatureMethod) method;
            List list = keyInfo.getContent();

            for (int i = 0; i < list.size(); i++) {
                XMLStructure xmlStructure = (XMLStructure) list.get(i);
                if (xmlStructure instanceof KeyValue) {
                    PublicKey pk = null;
                    try {
                        pk = ((KeyValue)xmlStructure).getPublicKey();
                    } catch (KeyException ke) {
                        throw new KeySelectorException(ke);
                    }
                    // make sure algorithm is compatible with method
                    if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                        return new SimpleKeySelectorResult(pk);
                    }
                }
//                else if (xmlStructure instanceof KeyName) {
//                    try {
//                        Certificate certificate = keyStore.getCertificate(((KeyName) xmlStructure).getName());
//                        PublicKey pk = certificate.getPublicKey();
//                        if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
//                            return new SimpleKeySelectorResult(pk);
//                        }
//                    } catch (KeyStoreException kse) {
//                        throw new KeySelectorException(kse);
//                    }
//                }
                else if (xmlStructure instanceof X509Data) {
                    X509Data xd = (X509Data) xmlStructure;
                    Object[] entries = xd.getContent().toArray();
                    X509CRL crl = null;
                    for (Object entry: entries)
                    {
                        if (entry instanceof X509CRL) {
                            crl = (X509CRL) entry;
                        }
                        if (entry instanceof X509CertImpl) {
                            X509CertImpl certImpl = (X509CertImpl) entry;
                            PublicKey pk = certImpl.getPublicKey();
                            if (algEquals(sm.getAlgorithm(), pk.getAlgorithm())) {
                                return new SimpleKeySelectorResult(pk);
                            }
                        }
                    }
                }

            }
            throw new KeySelectorException("No KeyValue element found!");
        }

        static boolean algEquals(String algURI, String algName) {
            if (algName.equalsIgnoreCase("DSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.DSA_SHA1)) {
                return true;
            } else if (algName.equalsIgnoreCase("RSA") &&
                    algURI.equalsIgnoreCase(SignatureMethod.RSA_SHA1)) {
                return true;
            } else {
                return false;
            }
        }
    }

    private List<X509Certificate> chain = new ArrayList<X509Certificate>();
    private int getCertificate(KeyInfo keyInfo)
    {
        Iterator iter = keyInfo.getContent().iterator();
        while (iter.hasNext()) {
            XMLStructure kiType = (XMLStructure) iter.next();
            if (kiType instanceof X509Data) {
                X509Data xd = (X509Data) kiType;
                Object[] entries = xd.getContent().toArray();
                X509CRL crl = null;
                for (int i = 0; ( i < entries.length); i++) {
                    if (entries[i] instanceof X509CRL) {
                        crl = (X509CRL) entries[i];
                    }
                    if (entries[i] instanceof Certificate) {
                        chain.add((X509Certificate) entries[i]);
                    }
                }
            }
        }
        return 0;
    }
    public List<X509Certificate> GetCert(byte [] xml) {
    boolean res = false;
    try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        Document doc = dbFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xml));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
        if(nl.getLength()==0)
        {
            nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "ds:Signature");
            if(nl.getLength()==0) {
                return null;
            }
        }
        String providerName = System.getProperty("jsr106Provider", "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
        XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM", (Provider)Class.forName(providerName).newInstance());
        for (int i = 0; i < nl.getLength(); i++) {
            DOMValidateContext valContext = new DOMValidateContext(new KeyValueKeySelector(), nl.item(i));
            XMLSignature signature = fac.unmarshalXMLSignature(valContext);
            KeyInfo keyInfo = signature.getKeyInfo();
            getCertificate(keyInfo);
        }

    } catch (Exception ex) {
        return null;
    }
    return chain;
}
}
