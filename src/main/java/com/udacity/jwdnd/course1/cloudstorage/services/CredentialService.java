package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public int createCredential(Credential credential){

        String encodedKey = generateEncodedKey();
        String encryptedPassword = encryptPassword(credential.getPassword(), encodedKey);
        Credential credentialToInsert = new Credential(credential.getCredentialid(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, credential.getUserid());
        return credentialMapper.insertCredential(credentialToInsert);
    }

    public List<Credential> getCredentials(Integer userId){
        return credentialMapper.getCredentials(userId);
    }

    public int deleteCredential(int credentialid){
        return credentialMapper.deleteCredential(credentialid);
    }

    public int updateCredential(Credential credential){

        String encodedKey = generateEncodedKey();
        String encryptedPassword = encryptPassword(credential.getPassword(), encodedKey);
        Credential credentialToUpdate = new Credential(credential.getCredentialid(), credential.getUrl(), credential.getUsername(), encodedKey, encryptedPassword, credential.getUserid());
        return credentialMapper.updateCredential(credentialToUpdate);
    }

    public String getDecryptedPassword(int credentialid, int userid){
        Credential credential = credentialMapper.getCredential(credentialid, userid);

        String decryptedPassword = "";
        if(credential != null){
          decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        }

        return decryptedPassword;
    }

    private String generateEncodedKey(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        return encodedKey;
    }
    private String encryptPassword(String password, String encodedKey){

        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);
        return encryptedPassword;
    }
}